package uepb.transportadora.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import uepb.transportadora.models.Encomenda;
import uepb.transportadora.models.Franquia;
import uepb.transportadora.models.Funcionario;
import uepb.transportadora.services.EncomendaService;
import uepb.transportadora.services.FranquiaService;
import uepb.transportadora.services.FuncionarioService;

@Controller
@RequestMapping(value = "/encomendas")
public class EncomendaController {
	
	/**
	 * EncomendaService - Responsavel por fornecer servicos de CRUD envolvendo Encomendas a EncomendaController.
	 */
	@Autowired
	EncomendaService encomendaService;
	
	/**
	 * FuncionarioService - Responsavel por fornecer servicos de CRUD envolvendo Funcionarios a EncomendaController.
	 */
	@Autowired
	FuncionarioService funcionarioService;
	
	/**
	 * FranquiaService - Responsavel por fornecer servicos de CRUD envolvendo Franquias a EncomendaController.
	 */
	@Autowired
	FranquiaService franquiaService;
	
	/**
	 * encomendasListar - Responsavel por listar as encomendas cadastradas (Metodo GET).
	 * @return ModelAndView.
	 */
	@GetMapping()
	public ModelAndView encomendasListar() {			
		try {
			ModelAndView mv = new ModelAndView("encomenda/encomendasListar.html");
			mv.addObject("encomendas", encomendaService.listar());			
			return mv;				
		}
		catch(Exception e) {
			return new ModelAndView("erro/erroops.html");	
		}
	}
	
	/**
	 * encomendaCadastrar - Responsavel por exibir o formulario de cadastro de uma encomenda (Metodo GET).
	 * @param encomenda - encomenda que vai sofrer o binding no view.
	 * @return ModelAndView.
	 */
	@GetMapping(value = "/cadastrar")
	public ModelAndView encomendaCadastrar(Encomenda encomenda) {		
		try {
			ModelAndView mv = new ModelAndView("encomenda/encomendaCadastrar.html");	
			mv.addObject("franquias", franquiaService.listar());		
			return mv;			
		}
		catch(Exception e) {
			return new ModelAndView("erro/erroops.html");	
		}
	}
	
	/**
	 * encomendaCadastrar - Responsavel por submeter o formulario de cadastro de uma encomenda (Metodo POST).		
	 * @param encomenda - Encomenda preenchida no view. 
	 * @param result - Resultado do binding realizado no view.  
	 * @return ModelAndView.
	 */
	@PostMapping(value = "/cadastrar")
	public ModelAndView encomendaCadastrar(
			@Valid Encomenda encomenda,
			BindingResult result
			) 
	{				
		try {
			if(result.hasErrors()) 
				return encomendaCadastrar(encomenda); 	
			List<Franquia> rota = encomendaService.calcularRota(encomenda);			
			if(rota.size()==0) 
				return encomendaCadastrar(encomenda).addObject("mensagem", "Não existe rotas possíveis entre as franquias."); 
			encomenda.setRota(rota);
			encomendaService.atualizarRelatorio(encomenda);	
			encomendaService.salvar(encomenda);
			return new ModelAndView("redirect:/encomendas");		
		}
		catch(Exception e) {
			return new ModelAndView("erro/erroops.html");	
		}		
	}
	
	/**
	 * encomendaRelatorio - Responsavel por exibir o relatorio de uma encomenda (Metodo GET).	
	 * @param id - ID da encomenda selecionada para edicao.
	 * @return ModelAndView.
	 */
	@GetMapping(value = "/relatorio/{id}")
	public ModelAndView encomendaRelatorio(@PathVariable(name = "id") Long id) {		
		try {
			ModelAndView mv = new ModelAndView("encomenda/encomendaRelatorio.html");
			mv.addObject("funcionario", funcionarioService.funcionarioLogado());	
			mv.addObject("encomenda", encomendaService.listar(id));
			return mv;
		}
		catch(Exception e) {
			return new ModelAndView("erro/erroops.html");	
		}
	}
	
	/**
	 * encomendaRelatorio - Responsavel por submeter as mudancas no relatorio de uma encomenda (Metodo POST).	
	 * @param encomenda - Encomenda preenchida no view.
	 * @return ModelAndView.
	 */
	@PostMapping(value = "/relatorio")
	public ModelAndView encomendaRelatorio(Encomenda encomenda) 
	{	
		try {
			Encomenda encomendaTemp = encomendaService.listar(encomenda.getId());	
			encomendaService.atualizarRelatorio(encomendaTemp);
			encomendaService.salvar(encomendaTemp);
			return new ModelAndView("redirect:/encomendas/relatorio/"+encomendaTemp.getId());	
		}
		catch(Exception e) {
			return new ModelAndView("erro/erroops.html");	
		}		
	}
}
