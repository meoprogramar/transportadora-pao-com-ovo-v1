package uepb.transportadora.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uepb.transportadora.models.Encomenda;
import uepb.transportadora.models.Franquia;
import uepb.transportadora.models.Funcionario;
import uepb.transportadora.repository.EncomendaRepository;
import uepb.transportadora.repository.FranquiaRepository;
import uepb.transportadora.repository.FuncionarioRepository;

@Service
public class FranquiaService {

	@Autowired
	private FranquiaRepository repo;
	@Autowired
	private EncomendaRepository repoEncomenda;
	@Autowired
	private FuncionarioRepository repoFuncionario;

	public Franquia listar(Long id) {
		Optional<Franquia> retorno = repo.findById(id);
		return retorno.orElse(null);
	}

	public List<Franquia> listar() {
		List<Franquia> retorno = repo.findAll();
		return retorno;
	}

	public Franquia salvar(Franquia param) {
		return repo.save(param);
	}

	public void remover(Long id) {
		repo.deleteById(id);
	}
	
	public boolean preencherCoordenadas(Franquia franquia){		
		Float[] coordenadas;
		try {
			coordenadas = this.getLocation(franquia.getRua()+", "+franquia.getCidade());
			if(coordenadas.length==0)
				return false;
			franquia.setLatitude(coordenadas[0].toString());
			franquia.setLongitude(coordenadas[1].toString());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//Mudar leitura das coordenadas depois de entregar, usar a biblioteca GSON.
	public Float[] getLocation(String adress) throws Exception {
		String adressFormat=adress.replace(" ", "+");
		String link1 = "https://maps.googleapis.com/maps/api/geocode/json?address=";
		String link2 = "&key=SUA_API_KEY_AQUI";
		//String link2 = "&key=coloque_sua_api_aqui";
		String linkFinal = link1 + adressFormat + link2;
		
		String result = this.getRequest(linkFinal);
		if(result.contains("ZERO_RESULTS"))
			return new Float[]{};
		
		int comeco, fim;
		comeco = result.indexOf("\""+"location"+"\"");
		fim = result.indexOf("\""+"location_type"+"\"");
		String resultadoParte1=result.substring(comeco, fim);
		String resultadoParte2=resultadoParte1.substring(resultadoParte1.indexOf("{")+1, resultadoParte1.indexOf("}"));		
		String latString = resultadoParte2.split(",")[0]; 
		String lngString = resultadoParte2.split(",")[1]; 
		
		try {				
			Float latitude = Float.parseFloat(latString.split(":")[1]); 
			Float longitude = Float.parseFloat(lngString.split(":")[1]); 			
			return new Float[]{latitude, longitude};
		} catch(Exception e) {
			e.printStackTrace();
			return new Float[]{};
		}
	}	
	
	private String getRequest(String url) throws Exception {
	        final URL obj = new URL(url);
	        final HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	
	        con.setRequestMethod("GET");
	        
	        if (con.getResponseCode() != 200) {
	            return null;
	        }
	
	        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	        String inputLine;
	        StringBuffer response = new StringBuffer();
	
	        while ((inputLine = in.readLine()) != null) {
	            response.append(inputLine);
	        }
	        in.close();
	
	        return response.toString();
	}
	
	public List<Franquia> listarFranquiasDependentes(Franquia franquia) {
		List<Franquia> franquiasDependentes = new ArrayList<>(); 
		List<Franquia> franquias = repo.findAll();
		for(Franquia faux: franquias) 
			for(Franquia fpaux: faux.getFranquiasProximas().keySet()) 
				if(fpaux.equals(franquia))
					franquiasDependentes.add(fpaux);			
		return franquiasDependentes;
	}
	
	public List<Funcionario> listarFuncionarios(Franquia franquia) {
		return repoFuncionario.findByFranquia(franquia);
	}
	
	public Map<Franquia, Double> listarFranquiasMaisLucrativas() {		
		List<Franquia> franquias = listar();
		List<Encomenda> encomendas;
		Map<Franquia, Double> franquiasLucro = new HashMap<>();	
		Double lucro=0.0;
		
		for(Franquia franq: franquias) {
			encomendas = listarEncomendasEnviadas(franq);
			lucro=0.0;
			for(Encomenda enco: encomendas) {
				switch(enco.getTipoEncomenda()) {
					case 0: lucro+=10;
							break;
					case 1: lucro+=20;
							break;
					case 2: lucro+=40;
							break;
				}
			}
			franquiasLucro.put(franq, lucro);
		}	
		return sortByValues(franquiasLucro);
	}
	
	public Map<Franquia, Integer> listarFranquiasMaisMovimentadas() {		
		List<Franquia> franquias = listar();
		Map<Franquia, Integer> franquiasMovimento = new HashMap<>();		
		for(Franquia franq: franquias) 
			franquiasMovimento.put(franq, listarRotas(franq).size());		
		return sortByValues(franquiasMovimento);
	}
	
	public List<Encomenda> listarEncomendas(Franquia franquia) {
		return repoEncomenda.findByFranquiaRemetenteOrFranquiaDestino(franquia, franquia);
	}
	
	public List<Encomenda> listarEncomendasEnviadas(Franquia franquia) {
		return repoEncomenda.findByFranquiaRemetente(franquia);
	}
	
	public List<Encomenda> listarEncomendasRecebidas(Franquia franquia) {
		return repoEncomenda.findByFranquiaDestino(franquia);
	}
	
	public List<Encomenda> listarRotas(Franquia franquia) {
		return repoEncomenda.findByRota(franquia);
	}
	
	public boolean validarESalvarFranquiasProximas(Franquia franquia, Long[] franquiasProximas, Double[] franquiasProximasDistancia) {
		boolean validacao = true;
		for(int i=0; i<franquiasProximas.length;i++) {
			if(!(franquiasProximas[i]==-1)) {
				franquia.getFranquiasProximas().put(listar(franquiasProximas[i]), franquiasProximasDistancia[i]);
				if(franquiasProximasDistancia[i]==null)
					validacao = false;
			}
		}
		return validacao;
	}
	
	private static <K, V extends Comparable<V>> Map<K, V> sortByValues(final Map<K, V> map) {
	    Comparator<K> valueComparator =  new Comparator<K>() {
	        public int compare(K k1, K k2) {
	            int compare = map.get(k2).compareTo(map.get(k1));
	            if (compare == 0) return 1;
	            else return compare;
	        }
	    };
	    Map<K, V> sortedByValues = new TreeMap<K, V>(valueComparator);
	    sortedByValues.putAll(map);
	    return sortedByValues;
	}
}
