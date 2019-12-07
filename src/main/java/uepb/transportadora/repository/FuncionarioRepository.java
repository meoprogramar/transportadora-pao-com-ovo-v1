package uepb.transportadora.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import uepb.transportadora.models.Franquia;
import uepb.transportadora.models.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
	Funcionario findByUsuario(String usuario);
	List<Funcionario> findByUsuarioAndSenhaIgnoreCase(String usuario, String senha);
	List<Funcionario> findByFranquia(Franquia franquia);	
}
