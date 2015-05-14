package org.jugvale.transparencia.transf.model.transferencia;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "programa")
@XmlRootElement
public class Programa {
	
	@Id
	@Column(name="pro_id")
	private long id;
	
	@Column(name="pro_nome")
	private String nome;

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
}
