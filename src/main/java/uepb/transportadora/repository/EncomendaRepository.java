package uepb.transportadora.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import uepb.transportadora.models.Encomenda;
import uepb.transportadora.models.Franquia;

public interface EncomendaRepository extends JpaRepository<Encomenda, Long> {
	List<Encomenda> findByFranquiaRemetenteOrFranquiaDestino(Franquia franquia1, Franquia franquia2);
	List<Encomenda> findByFranquiaRemetente(Franquia franquia);
	List<Encomenda> findByFranquiaDestino(Franquia franquia);
	List<Encomenda> findByRota(Franquia franquia);
}
