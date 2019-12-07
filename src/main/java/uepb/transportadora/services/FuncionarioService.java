package uepb.transportadora.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import uepb.transportadora.models.Funcionario;
import uepb.transportadora.repository.FuncionarioRepository;

@Service
public class FuncionarioService {

	@Autowired
	private FuncionarioRepository repo;
	
	public Funcionario funcionarioLogado() {
		Funcionario funcTemp = (Funcionario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();		
		return listar(funcTemp.getId());
	}

	public Funcionario listar(Long id) {
		Optional<Funcionario> retorno = repo.findById(id);
		return retorno.orElse(null);
	}

	public List<Funcionario> listar() {
		List<Funcionario> retorno = repo.findAll();
		return retorno;
	}

	public Funcionario salvar(Funcionario param) {		
		return repo.save(param);
	}

	public void remover(Long id) {
		repo.deleteById(id);
	}
	
	public Funcionario verificarUsuario(String usuario) {	
		return repo.findByUsuario(usuario);
	}
}
