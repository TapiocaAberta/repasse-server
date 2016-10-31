package org.jugvale.transfgov.model.indicador;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 
 * O foco do indicador, quem está sendo medido? Por ex: Taxa de aprovação(indicador) do IDEB (grupo)do 1
 * ano(foco), Mortalidade infantil(indicador) até 6 meses(foco)
 * 
 * @author wsiqueir
 *
 */
@Entity
@XmlRootElement
@Cacheable
@Table(name = "foco_indicador")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "cache-classes-basicas")
public class FocoIndicador {

	@Id
	@Column(name = "fom_id")
	private long id;
	
	@Column(name = "fom_foco")
	private String foco;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "fom_ind_id")
	private Indicador indicador;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFoco() {
		return foco;
	}

	public void setFoco(String foco) {
		this.foco = foco;
	}

	public Indicador getIndicador() {
		return indicador;
	}

	public void setIndicador(Indicador indicador) {
		this.indicador = indicador;
	}

}