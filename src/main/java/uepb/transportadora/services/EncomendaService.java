package uepb.transportadora.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uepb.transportadora.dijkstra.*;
import uepb.transportadora.models.Encomenda;
import uepb.transportadora.models.Franquia;
import uepb.transportadora.repository.EncomendaRepository;
import uepb.transportadora.repository.FranquiaRepository;

@Service
public class EncomendaService {

	@Autowired
	private EncomendaRepository repo;
	@Autowired
	private FranquiaRepository repof;

	public Encomenda listar(Long id) {
		Optional<Encomenda> retorno = repo.findById(id);
		return retorno.orElse(null);
	}

	public List<Encomenda> listar() {
		List<Encomenda> retorno = repo.findAll();
		return retorno;
	}

	public Encomenda salvar(Encomenda param) {
		return repo.save(param);
	}

	public void remover(Long id) {
		repo.deleteById(id);
	}
	
	public String horaAtual() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - hh:mm:ss");
		return dateFormat.format(new Date());
	}
	
	public void atualizarRelatorio(Encomenda encomenda) {
		if(encomenda.getRelatorio().size() < encomenda.getRota().size())
			encomenda.getRelatorio().put(encomenda.getRota().get(encomenda.getRelatorio().size()), this.horaAtual());
	}
	
	public List<Franquia> calcularRota(Encomenda encomenda) {
		List<Franquia> rota = new ArrayList<>();
		switch(encomenda.getTipoEncomenda()) {
			case 0: 
				rota = calcularRotaNormal(encomenda); 
				break;
			case 1: 
				rota = calcularRotaExpressa(encomenda);
				break;
			case 2: 
				rota = calcularRota24hrs(encomenda);
				break;
		}	
		return rota;
	}	
	
	private List<Franquia> calcularRotaNormal(Encomenda encomenda){
		if(encomenda.getFranquiaDestino().equals(encomenda.getFranquiaRemetente()))
			return new ArrayList<>();
		
		Random ran = new Random();
		List<Franquia> rotaTotal = new ArrayList<>();
		List<Franquia> rotaFinal = new ArrayList<>();
		List<Long> franquiasVisitadas = new ArrayList<Long>(); 		
		Franquia franquiaAtual = encomenda.getFranquiaRemetente();
		List<Franquia> franquiasProximasTemp = new ArrayList<>(franquiaAtual.getFranquiasProximas().keySet());
		Franquia franquiaAtualTemp;
		
		franquiasVisitadas.add(franquiaAtual.getId());
		rotaTotal.add(franquiaAtual);
		rotaFinal.add(franquiaAtual);
		
		while(encomenda.getFranquiaDestino().getId() != franquiaAtual.getId()) {			
			if(franquiasProximasTemp.size()!=0) {
				franquiaAtualTemp = franquiasProximasTemp.get(ran.nextInt(franquiasProximasTemp.size())); 
				franquiasProximasTemp.remove(franquiaAtualTemp);				
				
				//System.out.println("franquiaAtual "+franquiaAtual.getNome()+" franquiasProximasTemp: "+franquiaAtualTemp.getNome());
				if(!franquiasVisitadas.contains(franquiaAtualTemp.getId())) {
					franquiasProximasTemp = new ArrayList<>(franquiaAtualTemp.getFranquiasProximas().keySet());
					franquiasProximasTemp.remove(franquiaAtual);					
					franquiaAtual = franquiaAtualTemp;
					franquiasVisitadas.add(franquiaAtual.getId());
					rotaTotal.add(franquiaAtual);
					rotaFinal.add(franquiaAtual);
				}
			} else {
				if(encomenda.getFranquiaRemetente().equals(franquiaAtual) || (rotaFinal.size()==0))
					return new ArrayList<>();
				
				//System.out.println("Indice a remover: "+(rotaFinal.size()-1)+" tamanho: "+rotaFinal.size());
				rotaFinal.remove(rotaFinal.size()-1);					
				franquiaAtual = rotaTotal.get(rotaTotal.indexOf(franquiaAtual)-1);				
				franquiasProximasTemp = new ArrayList<>(franquiaAtual.getFranquiasProximas().keySet());
				franquiasProximasTemp.remove(rotaTotal.get(rotaTotal.indexOf(franquiaAtual)+1));
			}				
		}		
		return rotaFinal;
	}

	private List<Franquia> calcularRotaExpressa(Encomenda encomenda) {
		List<Franquia> franquias = repof.findAll();
		List<Franquia> rotaFinal = new ArrayList<Franquia>();
		List<Node> nos = new ArrayList<Node>();
		Node noInicial = null, noSave;
		Graph graph = new Graph();
		
		if(encomenda.getFranquiaDestino().equals(encomenda.getFranquiaRemetente()))
			return new ArrayList<>();
		for (Franquia franq : franquias) {
			if (encomenda.getFranquiaRemetente().equals(franq)) {
				noInicial = new Node(franq);
				noSave = noInicial;
			} else
				noSave = new Node(franq);
			nos.add(noSave);
			graph.addNode(noSave);
		}
		for (Node noAux : nos) {
			for (Node noAux2 : nos) {
				if (noAux.getFranq().getFranquiasProximas().containsKey(noAux2.getFranq())) {
					int distancia = noAux.getFranq().getFranquiasProximas().get(noAux2.getFranq()).intValue();
					noAux.addDestination(noAux2, distancia);
				}
			}
		}
		graph = Dijkstra.calculateShortestPathFromSource(graph, noInicial);
		for (Node no : graph.getNodes()) {
			if (no.getFranq().equals(encomenda.getFranquiaDestino())) {
				for (Node noRota : no.getShortestPath())
					rotaFinal.add(noRota.getFranq());
				if (!rotaFinal.isEmpty())
					rotaFinal.add(no.getFranq());
				break;
			}
		}
		return rotaFinal;
	}

	private List<Franquia> calcularRota24hrs(Encomenda encomenda) {
		List<Franquia> rotaFinal = new ArrayList<Franquia>();
		
		if(encomenda.getFranquiaDestino().equals(encomenda.getFranquiaRemetente()) || !(encomenda.getFranquiaRemetente().isTemAeroporto()))
			return new ArrayList<>();
		
		rotaFinal.add(encomenda.getFranquiaRemetente());
		Franquia franquiaAtual = encomenda.getFranquiaDestino();
		if (!franquiaAtual.isTemAeroporto()) {
			Double menorDist = Double.MAX_VALUE;
			for (Entry<Franquia, Double> franqProximas : franquiaAtual.getFranquiasProximas().entrySet())
				if ((franqProximas.getValue() < menorDist) & (franqProximas.getKey().isTemAeroporto()) & (!franqProximas.getKey().equals(encomenda.getFranquiaRemetente()))) {
					franquiaAtual = franqProximas.getKey();
					menorDist = franqProximas.getValue(); 
				}
			
			if (franquiaAtual.equals(encomenda.getFranquiaDestino()))
				rotaFinal.clear();
		}
		if(!rotaFinal.isEmpty())
			rotaFinal.add(franquiaAtual);
		return rotaFinal;
	}
}
