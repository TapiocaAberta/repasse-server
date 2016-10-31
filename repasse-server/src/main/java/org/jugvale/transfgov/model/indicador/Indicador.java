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
public class Indicador {

	@Id
	@Column(name = "ind_id")
	private long id;

	@Column(name = "ind_nome")
	private String nome;

	@Column(name = "ind_descricao", length = 5000)
	private String descricao;

	@ManyToOne
	@JoinColumn(name = "ind_grm_id")
	private GrupoIndicador grupoMetrica;

	@ManyToOne
	@JoinColumn(name = "ind_area_id")
	private Area area;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ind_fom_id")
	private FocoIndicador focoIndicador;

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

	public GrupoIndicador getGrupoMetrica() {
		return grupoMetrica;
	}

	public void setGrupoMetrica(GrupoIndicador grupoMetrica) {
		this.grupoMetrica = grupoMetrica;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public FocoIndicador getFocoIndicador() {
		return focoIndicador;
	}

	public void setFocoIndicador(FocoIndicador focoIndicador) {
		this.focoIndicador = focoIndicador;
	}
	
}