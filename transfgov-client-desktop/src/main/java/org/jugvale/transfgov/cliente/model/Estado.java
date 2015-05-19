package org.jugvale.transfgov.cliente.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Estado {

	private long id;

	private String nome;

	private String sigla;

	private String regiao;

	public Estado() {
		super();
	}

	public Estado(String sigla) {
		super();
		this.sigla = sigla;
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

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getRegiao() {
		return regiao;
	}

	public void setRegiao(String regiao) {
		this.regiao = regiao;
	}

}
