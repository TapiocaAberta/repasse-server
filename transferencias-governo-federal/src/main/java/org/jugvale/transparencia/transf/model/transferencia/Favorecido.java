package org.jugvale.transparencia.transf.model.transferencia;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "favorecido")
@XmlRootElement
@NamedQueries({	
	@NamedQuery(name="Favorecido.porCodigo", query="SELECT f FROM Favorecido f WHERE f.codigo = :codigo")	
})
public class Favorecido {

	@Id
	@GeneratedValue
	@Column(name = "fav_id")
	private long id;

	@Column(name = "fav_nome")
	private String nome;
	
	@Column(name = "fav_codigo")
	private String codigo;

	public Favorecido() {
		super();
	}
	
	public Favorecido(String nome, String codigo) {
		super();
		this.nome = nome;
		this.codigo = codigo;
	}	
	
	@Override
	public String toString() {
		return nome;
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

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

}
