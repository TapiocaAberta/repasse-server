package org.jugvale.transfgov.cliente.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Municipio {

	private long id;

	private String codigoSIAFI;

	private String nome;

	private String regiao;

	private float latitute;

	private float longitude;

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
