package uepb.transportadora.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.ScriptAssert;
import org.springframework.data.annotation.Transient;

@Entity
@ScriptAssert(lang = "javascript", script = "_this.franquiaRemetente != _this.franquiaDestino", message = "Franquia remetente e destino inválidas.")
public class Encomenda implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@NotEmpty(message = "Remetente obrigatória.")
	private String remetente;
	
	@NotNull
	@NotEmpty(message = "Destinatário obrigatória.")
	private String destinatario;
	
	@NotNull
	@NotEmpty(message = "Endereço obrigatória.")
	private String enderecoEntrega;
	
	private int tipoEncomenda; // 0 - Normal; 1 - Expressa; 2 - 24hrs
	
	@Transient
	private UUID idAlfaNumerico = UUID.randomUUID();

	@ManyToOne()
	private Franquia franquiaRemetente;
	
	@ManyToOne()	
	private Franquia franquiaDestino;
	
	@ManyToMany()
	private List<Franquia> rota = new ArrayList<>();
	
	@ElementCollection
	@CollectionTable(name = "relatorio")
	private Map<Franquia, String> relatorio = new HashMap<Franquia, String>();

	public Encomenda() {

	}

	public Encomenda(String remetente, String destinatario, String enderecoEntrega, int tipoEncomenda,
			Franquia franquiaRemetente, Franquia franquiaDestino) {
		super();
		this.remetente = remetente;
		this.destinatario = destinatario;
		this.enderecoEntrega = enderecoEntrega;
		this.tipoEncomenda = tipoEncomenda;
		this.franquiaRemetente = franquiaRemetente;
		this.franquiaDestino = franquiaDestino;
	}

	public List<Franquia> getRota() {
		return rota;
	}

	public void setRota(List<Franquia> rota) {
		this.rota = rota;
	}

	public Map<Franquia, String> getRelatorio() {
		return relatorio;
	}

	public void setRelatorio(Map<Franquia, String> relatorio) {
		this.relatorio = relatorio;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRemetente() {
		return remetente;
	}

	public void setRemetente(String remetente) {
		this.remetente = remetente;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public String getEnderecoEntrega() {
		return enderecoEntrega;
	}

	public void setEnderecoEntrega(String enderecoEntrega) {
		this.enderecoEntrega = enderecoEntrega;
	}

	public int getTipoEncomenda() {
		return tipoEncomenda;
	}

	public void setTipoEncomenda(int tipoEncomenda) {
		this.tipoEncomenda = tipoEncomenda;
	}

	public UUID getIdAlfaNumerico() {
		return idAlfaNumerico;
	}

	public void setIdAlfaNumerico(UUID idAlfaNumerico) {
		this.idAlfaNumerico = idAlfaNumerico;
	}

	public Franquia getFranquiaRemetente() {
		return franquiaRemetente;
	}

	public void setFranquiaRemetente(Franquia franquiaRemetente) {
		this.franquiaRemetente = franquiaRemetente;
	}

	public Franquia getFranquiaDestino() {
		return franquiaDestino;
	}

	public void setFranquiaDestino(Franquia franquiaDestino) {
		this.franquiaDestino = franquiaDestino;
	}

}
