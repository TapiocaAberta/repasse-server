package org.sjcdigital.repasse.model.base;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/*
 * Area em questão. Pode ser educação, saúde, segurança, etc
 * 
 * É o campo Função dos dados de transferência
 * 
 * */
@Entity
@Table(name = "area")
@XmlRootElement
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY, region="cache-classes-basicas")
@NamedQueries({
	@NamedQuery(name = "Area.porNome", query = "SELECT a FROM Area a WHERE a.nome = :nome")
})
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
