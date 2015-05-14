package org.jugvale.transparencia.transf.model.transferencia;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "acao")
@XmlRootElement
public class Acao {

	@Id
	@Column(name = "acao_id")
	private long id;
	
	@Column(name = "acao_nome")
	private String nome;
	
	@Column(name = "acao_popular")
	private String nomePopular;

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

	public String getNomePopular() {
		return nomePopular;
	}

	public void setNomePopular(String nomePopular) {
		this.nomePopular = nomePopular;
	}
}
