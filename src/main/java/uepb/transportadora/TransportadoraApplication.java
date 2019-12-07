package uepb.transportadora;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import uepb.transportadora.models.Franquia;
import uepb.transportadora.models.Funcionario;
import uepb.transportadora.services.EncomendaService;
import uepb.transportadora.services.FranquiaService;
import uepb.transportadora.services.FuncionarioService;

@SpringBootApplication
public class TransportadoraApplication implements CommandLineRunner {

	@Autowired
	FranquiaService franquiaService;
	@Autowired
	FuncionarioService funcionarioService;
	@Autowired
	EncomendaService encomendaService;
	
	public static void main(String[] args) {
		SpringApplication.run(TransportadoraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if(franquiaService.listar().size()==0) {
			//Exemplares
			Franquia f1 = new Franquia("RapidaoCometa", true, true, "Nordeste", "Paraíba", "Joao Pessoa", "R. Empresário João Rodrigues Alves", "466", "-7.148122", "-34.843422");
			Franquia f2 = new Franquia("BomQueSo", true, false, "Sul", "Sao Paulo", "Sao Paulo", "Rua Germano Vítor dos Santos", "329", "-23.713900", "-46.693512");
			Franquia f3 = new Franquia("Transnacional", false, true, "Suldeste", "Rio de Janeiro", "Nova Iguaçu", "Rua Marechal Bevilaqua", "446", "-22.9239745", "-43.3688227");
			Franquia f4 = new Franquia("Ferrotrada", true, true, "Nordeste", "Rio Grande do Norte", "Natal", "Av. Prudente de Morais", "3720", "-5.820062", "-35.213145");
			Franquia f5 = new Franquia("Borborema", false, false, "Nordeste", "Paraíba", "Campina Grande", "Rua Elpídio de Almeida", "127", "-7.216646", "-35.877895");
			Franquia f6 = new Franquia("BelaMinas", true, false, "Nordeste", "Bahia", "Salvador", "R. José Bezerra da Silva", "66", "-12.940291", "-38.503356");
			
			franquiaService.salvar(f1);
			franquiaService.salvar(f2);
			franquiaService.salvar(f3);
			franquiaService.salvar(f4);
			franquiaService.salvar(f5);
			franquiaService.salvar(f6);
			
			f1.getFranquiasProximas().put(f5, (double) 133);
			f1.getFranquiasProximas().put(f4, (double) 188);
			f1.getFranquiasProximas().put(f6, (double) 924);
			
			f2.getFranquiasProximas().put(f6, (double) 1985);
			f2.getFranquiasProximas().put(f3, (double) 426);
			f2.getFranquiasProximas().put(f5, (double) 2649);
			
			f3.getFranquiasProximas().put(f2, (double) 426);
			f3.getFranquiasProximas().put(f6, (double) 1603);
			f3.getFranquiasProximas().put(f5, (double) 1913);
			
			f4.getFranquiasProximas().put(f1, (double) 188);
			f4.getFranquiasProximas().put(f5, (double) 254);
			f4.getFranquiasProximas().put(f6, (double) 1090);
			
			f5.getFranquiasProximas().put(f1, (double) 133);
			f5.getFranquiasProximas().put(f4, (double) 254);
			f5.getFranquiasProximas().put(f6, (double) 876);
			
			f6.getFranquiasProximas().put(f1, (double) 133);
			f6.getFranquiasProximas().put(f2, (double) 1985);
			f6.getFranquiasProximas().put(f3, (double) 1603);
			
			franquiaService.salvar(f1);
			franquiaService.salvar(f2);
			franquiaService.salvar(f3);
			franquiaService.salvar(f4);
			franquiaService.salvar(f5);
			franquiaService.salvar(f6);
			
			Funcionario func1 = new Funcionario("João Fernandes Silva", "joaofer", new BCryptPasswordEncoder().encode("123"), true, f1); 
			Funcionario func2 = new Funcionario("Priscilla Karina de Sá", "priscilla", new BCryptPasswordEncoder().encode("123"), false, f3); 
			Funcionario func3 = new Funcionario("Mariana Kleber Machado", "mariana", new BCryptPasswordEncoder().encode("123"), false, f6); 
			Funcionario func4 = new Funcionario("admin", "admin", new BCryptPasswordEncoder().encode("admin"), true, f4);
			
			funcionarioService.salvar(func1);
			funcionarioService.salvar(func2);
			funcionarioService.salvar(func3);
			funcionarioService.salvar(func4);		
		}
	}

}
