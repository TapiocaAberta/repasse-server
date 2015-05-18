package org.jugvale.transfgov.model.transferencia;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "programa")
@XmlRootElement
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY, region="cache-classes-basicas")
public class Programa {

	@Id
	@Column(name = "pro_id")
	private long id;

	@Column(name = "pro_nome")
	private String nome;

	public Programa() {
	}

	public Programa(long id, String nome) {
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
