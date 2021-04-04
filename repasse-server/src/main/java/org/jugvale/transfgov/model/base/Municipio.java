package org.jugvale.transfgov.model.base;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jugvale.transfgov.resource.config.jsonview.MunicipioCompletoView;
import org.jugvale.transfgov.resource.config.jsonview.MunicipioConcisoView;

import com.fasterxml.jackson.annotation.JsonView;

@XmlRootElement
@Entity
@Table(name = "municipio")
@NamedQueries({
		@NamedQuery(name = "Municipio.porEstadoNomeSIAFI", query = "SELECT m FROM Municipio m WHERE m.nome = :nome AND m.codigoSIAFI = :siafi AND m.estado = :estado"),
		@NamedQuery(name = "Municipio.porNomeESigla", query = "SELECT m FROM Municipio m WHERE m.nome = :nome AND m.estado.sigla = :sigla"),
		@NamedQuery(name = "Municipio.porRegiao", query = "SELECT m FROM Municipio m WHERE m.regiao = :regiao"),	
		@NamedQuery(name = "Municipio.porSigla", query = "SELECT m FROM Municipio m WHERE m.estado.sigla = :sigla ORDER BY m.nome") })
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "cache-classes-basicas")
public class Municipio {

	@Id
	@GeneratedValue
	@Column(name = "mun_id")
	@JsonView(MunicipioConcisoView.class)
	private long id;

	@Column(name = "mun_cod_siafi")
	@JsonView(MunicipioConcisoView.class)
	private String codigoSIAFI;

	@Column(name = "mun_nome")
	@JsonView(MunicipioConcisoView.class)
	private String nome;

	@Column(name = "mun_regiao")
	@JsonView(MunicipioConcisoView.class)
	private String regiao;

	@Column(name = "mun_latitude")
	@JsonView(MunicipioCompletoView.class)
	private float latitute;

	@Column(name = "mun_longitude")
	@JsonView(MunicipioCompletoView.class)
	private float longitude;

	@ManyToOne(optional = false, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "estado_est_id")
	@JsonView(MunicipioCompletoView.class)
	private Estado estado;

	public Municipio() {
		super();
	}

	@Override
	public String toString() {
		return nome + " - " + estado.getSigla();
	}

	public Municipio(String codigoSIAFI, String nome, Estado estado) {
		super();
		this.codigoSIAFI = codigoSIAFI;
		this.nome = nome;
		this.estado = estado;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getRegiao() {
		return regiao;
	}

	public void setRegiao(String regiao) {
		this.regiao = regiao;
	}

	public float getLatitute() {
		return latitute;
	}

	public void setLatitute(float latitute) {
		this.latitute = latitute;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public String getCodigoSIAFI() {
		return codigoSIAFI;
	}

	public void setCodigoSIAFI(String codigoSIAFI) {
		this.codigoSIAFI = codigoSIAFI;
	}

}
