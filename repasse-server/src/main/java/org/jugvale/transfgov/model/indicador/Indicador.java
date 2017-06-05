package org.jugvale.transfgov.model.indicador;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jugvale.transfgov.model.base.Area;

/**
 * 
 * Uma forma de medir o município. Por exemplo, Taxa de aprovação
 * (indicador)IDEB (grupo), Média em Matématica(indicador) ENEM(grupo) para a
 * área de educação. nascidos vivos(indicador) Mortalidade infantil(grupo),
 * logevidade para saúde, etc.
 * 
 * @author wsiqueir
 *
 */
@Entity
@XmlRootElement
@Cacheable
@Table(name = "indicador")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "cache-classes-basicas")
@NamedQueries({ @NamedQuery(name = "Indicador.porNome", query = "SELECT ind FROM Indicador ind WHERE ind.nome = :nome"),
		@NamedQuery(name = "Indicador.porGrupo", query = "SELECT ind FROM Indicador ind WHERE ind.grupoIndicador.nome = :nomeGrupo"),
		@NamedQuery(name = "Indicador.porArea", query = "SELECT ind FROM Indicador ind WHERE ind.area = :area") })
		
public class Indicador {

	@Id
	@GeneratedValue
	@Column(name = "ind_id")
	private long id;

	@Column(name = "ind_nome")
	private String nome;

	@Column(name = "ind_descricao", length = 5000)
	private String descricao;

	@ManyToOne
	@JoinColumn(name = "ind_grm_id")
	private GrupoIndicador grupoIndicador;

	@ManyToOne
	@JoinColumn(name = "ind_area_id")
	private Area area;


	public Indicador() {
	}

	public Indicador(String nome, GrupoIndicador grupoIndicador, Area area) {
		this.nome = nome;
		this.grupoIndicador = grupoIndicador;
		this.area = area;
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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


	public GrupoIndicador getGrupoIndicador() {
		return grupoIndicador;
	}

	public void setGrupoIndicador(GrupoIndicador grupoIndicador) {
		this.grupoIndicador = grupoIndicador;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}


	@Override
	public String toString() {
		return "Indicador [id=" + id + ", nome=" + nome + ", descricao=" + descricao + ", grupoIndicador="
				+ grupoIndicador + ", area=" + area + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((area == null) ? 0 : area.hashCode());
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((grupoIndicador == null) ? 0 : grupoIndicador.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Indicador other = (Indicador) obj;
		if (area == null) {
			if (other.area != null)
				return false;
		} else if (!area.equals(other.area))
			return false;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (grupoIndicador == null) {
			if (other.grupoIndicador != null)
				return false;
		} else if (!grupoIndicador.equals(other.grupoIndicador))
			return false;
		if (id != other.id)
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

	
	
}