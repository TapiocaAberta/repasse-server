package org.jugvale.transfgov.model.indicador;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 
 * Um grupo de indicador, por exemplo: IDEB, ENEM na educação, mortalidade
 * infantil na saúde entre outros. Pode ser visto uma forma de agrupar
 * indicadores, semelhante a uma categoria. <br>
 * O motivo é que um grupo pode ter vários indicadores, por exemplo, o IDEB pode
 * ter a nota brasil.
 * 
 * 
 * @author wsiqueir
 *
 */
@Entity
@XmlRootElement
@Cacheable
@Table(name = "grupo_indicador")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "cache-classes-basicas")
public class GrupoIndicador {

	@Id
	@Column(name = "grm_id")
	private long id;

	@Column(name = "grm_nome")
	private String nome;

	@Column(name = "grm_descricao", length = 5000)
	private String descricao;

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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}