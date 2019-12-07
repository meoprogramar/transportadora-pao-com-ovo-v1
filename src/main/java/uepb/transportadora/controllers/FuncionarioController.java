package uepb.transportadora.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import uepb.transportadora.models.Funcionario;
import uepb.transportadora.services.FranquiaService;
import uepb.transportadora.services.FuncionarioService;

@Controller
@RequestMapping(value = "/funcionarios")
public class FuncionarioController {
	
	@Autowired
	FuncionarioService funcionarioService;
	@Autowired
	FranquiaService franquiaService;
	
	@GetMapping()
	public ModelAndView funcionariosListar() {			
		try {
			ModelAndView mv = new ModelAndView("funcionario/funcionariosListar.html");
			mv.addObject("funcLogado", funcionarioService.funcionarioLogado());	
			mv.addObject("funcionarios", funcionarioService.listar());	
			return mv;		
		}
		catch(Exception e) {
			return new ModelAndView("erro/erroops.html");	
		}
	}
	
	@GetMapping(value = "/cadastrar")
	public ModelAndView funcionarioCadastrar(Funcionario funcionario) {		
		try {
			ModelAndView mv = new ModelAndView("funcionario/funcionarioCadastrar.html");		
			mv.addObject("franquias", franquiaService.listar());		
			return mv;			
		}
		catch(Exception e) {
			return new ModelAndView("erro/erroops.html");	
		}
	}
	
	@PostMapping(value = "/cadastrar")
	public ModelAndView funcionarioCadastrar(
			@Valid Funcionario funcionario,
			BindingResult result
			) 
	{	
		try {
			if(result.hasErrors()) 
				return funcionarioCadastrar(funcionario); 
			funcionario.setSenha(new BCryptPasswordEncoder().encode(funcionario.getSenha()));
			funcionarioService.salvar(funcionario);	
			return new ModelAndView("redirect:/funcionarios");
		}
		catch(Exception e) {
			return new ModelAndView("erro/erroops.html");	
		}		
	}
	
	@GetMapping(value = "/editar/{id}")
	public ModelAndView funcionarioEditar(@PathVariable(name = "id") Long id) {				
		try {
			ModelAndView mv = new ModelAndView("funcionario/funcionarioEditar.html");
			mv.addObject("funcLogado", funcionarioService.funcionarioLogado());	
			mv.addObject("funcionario", funcionarioService.listar(id));			
			mv.addObject("franquias", franquiaService.listar());		
			return mv;
		}
		catch(Exception e) {
			return new ModelAndView("erro/erroops.html");	
		}
	}
	
	@GetMapping(value = "/editar")
	public ModelAndView funcionarioEditar(Funcionario funcionario) {				
		try {
			ModelAndView mv = new ModelAndView("funcionario/funcionarioEditar.html");	
			mv.addObject("franquias", franquiaService.listar());		
			return mv;
		}
		catch(Exception e) {
			return new ModelAndView("erro/erroops.html");	
		}
	}
	
	@PostMapping(value = "/editar")
	public ModelAndView funcionarioEditar(
			@Valid Funcionario funcionario,
			BindingResult result
			) 
	{	
		try {
			if(result.hasErrors()) 
				return funcionarioEditar(funcionario); 	
			funcionarioService.salvar(funcionario);	
			return new ModelAndView("redirect:/funcionarios");
		}
		catch(Exception e) {
			return new ModelAndView("erro/erroops.html");	
		}		
	}
	
	@GetMapping(value = "/remover/{id}")
	public ModelAndView funcionarioRemover(@PathVariable(name = "id") Long id)
	{	
		try {
			funcionarioService.remover(id);
			return new ModelAndView("redirect:/funcionarios");
		}
		catch(Exception e) {
			return new ModelAndView("erro/erroops.html");	
		}
	}
}