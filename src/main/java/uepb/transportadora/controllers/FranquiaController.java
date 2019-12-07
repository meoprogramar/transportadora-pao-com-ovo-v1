package uepb.transportadora.controllers;

import java.util.Map;
import java.util.Map.Entry;

import javax.validation.Valid;

import org.apache.tomcat.util.json.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import uepb.transportadora.models.Franquia;
import uepb.transportadora.services.FranquiaService;
import uepb.transportadora.services.FuncionarioService;

/**
 * FranquiaController - Responsavel por comunicar o back-end com o front-end.
 * @version 1.1
 */

@Controller
@RequestMapping(value = "/franquias")
public class FranquiaController {
	
	/**
	 * FranquiaService - Responsavel por fornecer servicos de CRUD envolvendo Franquias a FranquiaController.
	 */
	@Autowired
	FranquiaService franquiaService;
	
	@Autowired
	FuncionarioService funcionarioService;
	
	/**
	 * franquiasListar - Responsavel por listar as franquias disponiveis (Metodo GET).
	 * @return ModelAndView.
	 */
	@GetMapping()
	public ModelAndView franquiasListar() {	
		try {
			ModelAndView mv = new ModelAndView("franquia/franquiasListar.html");
			mv.addObject("funcLogado", funcionarioService.funcionarioLogado());	
			mv.addObject("franquias", franquiaService.listar());			
			return mv;
		}
		catch(Exception e) {
			return new ModelAndView("erro/erroo	ps.html");	
		}
	}
	
	/**
	 * franquiaCadastrar - Responsavel por exibir o formulario de cadastro de uma franquia (Metodo GET).	
	 * @param franquia - Franquia que vai sofrer o binding no view.
	 * @return ModelAndView.
	 */
	@GetMapping(value = "/cadastrar")
	public ModelAndView franquiaCadastrar(Franquia franquia) {			
		try {
			ModelAndView mv = new ModelAndView("franquia/franquiaCadastrar.html");		
			mv.addObject("franquias", franquiaService.listar());		
			return mv;
		}
		catch(Exception e) {
			return new ModelAndView("erro/erroops.html");	
		}
	}	
	
	/**
	 * franquiaCadastrar - Responsavel por submeter o formulario de cadastro de uma franquia (Metodo POST).	
	 * @param franquia - Franquia preenchida no view. 
	 * @param result - Resultado do binding realizado no view.  
	 * @param franquiasProximasId - Array com o ID das franquias proximas selecionadas. 
	 * @param franquiasProximasDistancia - Array com a distancia das franquias proximas selecionadas. 
	 * @return ModelAndView.
	 */
	@PostMapping(value = "/cadastrar")
	public ModelAndView franquiaCadastrar(
			@Valid Franquia franquia,
			BindingResult result,
			@RequestParam(value="fProximas[]", required=false) Long[] franquiasProximasId,
			@RequestParam(value="fProximasDist[]", required=false) Double[] franquiasProximasDistancia
			) 
	{	
		try {
			if(!franquiaService.validarESalvarFranquiasProximas(franquia, franquiasProximasId, franquiasProximasDistancia)) 
				return franquiaCadastrar(franquia).addObject("mensagem", "Distância da franquia obrigatória."); 
			if(result.hasErrors()) 
				return franquiaCadastrar(franquia); 	
			if(!franquiaService.preencherCoordenadas(franquia)) 
				return franquiaCadastrar(franquia).addObject("mensagem", "Endereço não encontrado."); 
			franquiaService.salvar(franquia);		
			return new ModelAndView("redirect:/franquias");	
		}
		catch(Exception e) {
			return new ModelAndView("erro/erroops.html");	
		}
	}
	
	/**
	 * franquiaEditar - Responsavel por exibir o formulario de edicao de uma franquia (Metodo GET).	
	 * @param id - ID da franquia selecionada para edicao.
	 * @return ModelAndView.
	 */	
	@GetMapping(value = "/editar/{id}")
	public ModelAndView franquiaEditar(@PathVariable(name = "id") Long id) {				
		try {
			ModelAndView mv = new ModelAndView("franquia/franquiaEditar.html");
			mv.addObject("franquia", franquiaService.listar(id));		
			mv.addObject("franquias", franquiaService.listar());	
			return mv;
		}
		catch(Exception e) {
			return new ModelAndView("erro/erroops.html");	
		}
	}
	
	/**
	 * franquiaEditar - Responsavel por exibir o formulario de edicao de uma franquia (Metodo GET).	
	 * @param franquia - Franquia instanciada que vai sofrer o binding no view.
	 * @return ModelAndView.
	 */
	@GetMapping(value = "/editar")
	public ModelAndView franquiaEditar(Franquia franquia) {		
		try {
			ModelAndView mv = new ModelAndView("franquia/franquiaEditar.html");
			mv.addObject("franquias", franquiaService.listar());	
			return mv;
		}
		catch(Exception e) {
			return new ModelAndView("erro/erroops.html");	
		}		
	}
	
	/**
	 * franquiaEditar - Responsavel por submeter o formulario de edicao de uma franquia (Metodo POST).	
	 * @param franquia - Franquia preenchida no view.
	 * @param result - Resultado do binding realizado no view.    
	 * @param franquiasProximasId - Array com o ID das franquias proximas selecionadas. 
	 * @param franquiasProximasDistancia - Array com a distancia das franquias proximas selecionadas. 
	 * @return ModelAndView.
	 */
	@PostMapping(value = "/editar")
	public ModelAndView franquiaEditar(
			@Valid Franquia franquia,
			BindingResult result,
			@RequestParam(value="fProximas[]", required=false) Long[] franquiasProximasId,
			@RequestParam(value="fProximasDist[]", required=false) Double[] franquiasProximasDistancia
			) 
	{			
		try {
			if(!franquiaService.validarESalvarFranquiasProximas(franquia, franquiasProximasId, franquiasProximasDistancia)) 
				return franquiaEditar(franquia).addObject("mensagem", "Distância da franquia obrigatória."); 
			if(result.hasErrors()) 
				return franquiaEditar(franquia); 
			if(!franquiaService.preencherCoordenadas(franquia)) 
				return franquiaCadastrar(franquia).addObject("mensagem", "Endereço não encontrado."); 
			franquiaService.salvar(franquia);		
			return new ModelAndView("redirect:/franquias");	
		}
		catch(Exception e) {
			return new ModelAndView("erro/erroops.html");	
		}	
	}
	
	@GetMapping(value = "/relatorio/{id}")
	public ModelAndView franquiaRelatorio(@PathVariable(name = "id") Long id) {				
		try {
			ModelAndView mv = new ModelAndView("franquia/franquiaRelatorio.html");
			mv.addObject("franquia", franquiaService.listar(id));	
			mv.addObject("encomendasEnviadas", franquiaService.listarEncomendasEnviadas(franquiaService.listar(id)));		
			mv.addObject("encomendasRecebidas", franquiaService.listarEncomendasRecebidas(franquiaService.listar(id)));		
			mv.addObject("encomendasRotas", franquiaService.listarRotas(franquiaService.listar(id)));	
			mv.addObject("franquiasLucro", franquiaService.listarFranquiasMaisLucrativas());				
			mv.addObject("franquiasMovimento", franquiaService.listarFranquiasMaisMovimentadas());			
			return mv;
		}
		catch(Exception e) {
			return new ModelAndView("erro/erroops.html");	
		}
	}
	
	/**
	 * franquiaRemover - Responsavel por remover uma franquia (Metodo GET).	 	
	 * @param id - ID da franquia selecionada para remocao.
	 * @return ModelAndView.
	 */
	@GetMapping(value = "/remover/{id}")
	public ModelAndView franquiaRemover(@PathVariable(name = "id") Long id)
	{			
		try {
			if((franquiaService.listarEncomendas(franquiaService.listar(id)).size()>0) || 
				(franquiaService.listarRotas(franquiaService.listar(id)).size()>0) ||
				(franquiaService.listarFranquiasDependentes(franquiaService.listar(id)).size()>0) ||
				(franquiaService.listarFuncionarios(franquiaService.listar(id)).size()>0))
				return franquiasListar().addObject("mensagem", "Franquia com encomendas, rotas ou funcionários pendentes."); 
			franquiaService.remover(id);			
			return new ModelAndView("redirect:/franquias");				
		}
		catch(Exception e) {
			return new ModelAndView("erro/erroops.html");	
		}	
	}
}
