package uepb.transportadora.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class Franquia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@NotEmpty(message = "Nome obrigatório.")
	private String nome;
	
	@Column(nullable = true)
	private boolean temAeroporto;
	
	@Column(nullable = true)
	private boolean franquiaMatriz;
	
	@NotNull
	@NotEmpty(message = "Região obrigatória.")
	private String regiao;
	
	@NotNull
	@NotEmpty(message = "Estado obrigatório.")
	private String estado;
	
	@NotNull
	@NotEmpty(message = "Cidade obrigatória.")
	private String cidade;
	
	@NotNull
	@NotEmpty(message = "Rua obrigatória.")
	private String rua;
	
	@NotNull
	@NotEmpty(message = "Número obrigatório.")
	private String numero;
	
	private String latitude;
	
	private String longitude;
	
	@ElementCollection
	@CollectionTable(name = "franquiasProximas")
	@MapKeyColumn(name = "franquiaProximaId")
	private Map<Franquia, Double> franquiasProximas = new HashMap<Franquia, Double>();
	
	@OneToMany(mappedBy = "franquia")
	private List<Funcionario> funcionarios = new ArrayList<>();

	@OneToMany(mappedBy = "franquiaRemetente")
	private List<Encomenda> encomendas = new ArrayList<>();

	public Franquia() {

	}

	public Franquia(String nome, boolean temAeroporto, boolean franquiaMatriz, String regiao, String estado,
			String cidade, String rua, String numero, String latitude, String longitude) {
		super();
		this.nome = nome;
		this.temAeroporto = temAeroporto;
		this.franquiaMatriz = franquiaMatriz;
		this.regiao = regiao;
		this.estado = estado;
		this.cidade = cidade;
		this.rua = rua;
		this.numero = numero;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getRegiao() {
		return regiao;
	}

	public void setRegiao(String regiao) {
		this.regiao = regiao;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public List<Funcionario> getFuncionarios() {
		return funcionarios;
	}

	public void setFuncionarios(List<Funcionario> funcionarios) {
		this.funcionarios = funcionarios;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public boolean isTemAeroporto() {
		return temAeroporto;
	}

	public void setTemAeroporto(boolean temAeroporto) {
		this.temAeroporto = temAeroporto;
	}

	public List<Encomenda> getEncomendas() {
		return encomendas;
	}

	public void setEncomendas(List<Encomenda> encomendas) {
		this.encomendas = encomendas;
	}

	public boolean isFranquiaMatriz() {
		return franquiaMatriz;
	}

	public void setFranquiaMatriz(boolean franquiaMatriz) {
		this.franquiaMatriz = franquiaMatriz;
	}

	public Map<Franquia, Double> getFranquiasProximas() {
		return franquiasProximas;
	}

	public void setFranquiasProximas(Map<Franquia, Double> franquiasProximas) {
		this.franquiasProximas = franquiasProximas;
	}

}
