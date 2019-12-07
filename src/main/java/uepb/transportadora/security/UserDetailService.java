package uepb.transportadora.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import uepb.transportadora.models.Funcionario;
import uepb.transportadora.services.FuncionarioService;

@Repository
public class UserDetailService implements UserDetailsService {

	@Autowired
	FuncionarioService funcionarioService;
	
	@Override
	public UserDetails loadUserByUsername(String usuario) throws UsernameNotFoundException {
		Funcionario funcionario = funcionarioService.verificarUsuario(usuario);
		if(funcionario==null)
			throw new UsernameNotFoundException("Usuário não encontrado.");
		return funcionario;
	}
}
