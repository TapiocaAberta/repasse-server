package org.jugvale.transparencia.transf.model.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/*
 * Area em questão. Pode ser educação, saúde, segurança, etc
 * 
 * É o campo Função dos dados de transferência
 * 
 * */
@Entity
@Table(name = "area")
@XmlRootElement
public class Area {

	@Id
	@Column(name = "area_id")
	private long id;

	@Column(name = "area_nome")
	private String nome;

	public Area() {
	}

	public Area(long id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
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

}
