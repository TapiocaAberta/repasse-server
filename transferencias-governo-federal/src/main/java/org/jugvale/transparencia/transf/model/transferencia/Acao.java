package org.jugvale.transparencia.transf.model.transferencia;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "acao")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Acao.porCodigo", query = "SELECT a FROM Acao a WHERE a.codigo = :codigo") })
public class Acao {

	@Id
	@GeneratedValue
	@Column(name = "acao_id")
	private long id;

	@Column(name = "acao_codigo")
	private String codigo;

	@Column(name = "acao_nome")
	private String nome;

	@Column(name = "acao_popular")
	private String nomePopular;

	public Acao() {
		super();
	}

	public Acao(String codigo, String nome, String nomePopular) {
		this.codigo = codigo;
		this.nome = nome;
		this.nomePopular = nomePopular;
	}

	@Override
	public String toString() {
		return Objects.isNull(nomePopular) ? nome : nome + "/"
				+ nomePopular;
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

	public String getNomePopular() {
		return nomePopular;
	}

	public void setNomePopular(String nomePopular) {
		this.nomePopular = nomePopular;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

}
