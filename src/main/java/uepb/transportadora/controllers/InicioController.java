package uepb.transportadora.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/")
public class InicioController {

	@GetMapping()
	public ModelAndView menu() {	
		ModelAndView mv = new ModelAndView("inicio/inicio.html");	
		return mv;	
	}
	
	@GetMapping("/entrar")
	public ModelAndView login(String error, String logout) {	
		try {
	        if(logout != null)
	        	return new ModelAndView("redirect:/");	
	        if(error != null)
	        	return new ModelAndView("inicio/login.html").addObject("mensagem", "Usuário ou senha inválidos.");
			ModelAndView mv = new ModelAndView("inicio/login.html");	
			return mv;		
		}
		catch(Exception e) {
			return new ModelAndView("erro/erroops.html");	
		}
	}
}
