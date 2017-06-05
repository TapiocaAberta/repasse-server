package org.jugvale.transfgov.model.indicador;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.jugvale.transfgov.model.base.Municipio;

/**
 * 
 * O valor do indicador para um dado ano e município. Os indicadores suportados
 * são somente anuais e com valores numéricos.
 * 
 * @author wsiqueir
 *
 */
@Entity
@XmlRootElement
@Table(name = "valor_indicador")
@NamedQueries({
		@NamedQuery(name = "ValorIndicador.porAnoMunicipioIndicador", query = "SELECT v FROM ValorIndicador v where v.ano = :ano  AND v.municipio = :municipio AND v.indicador = :indicador", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "ValorIndicador.contaPorAnoMunicipioIndicador", query = "SELECT count(v) FROM ValorIndicador v where v.ano = :ano  AND v.municipio = :municipio AND v.indicador = :indicador", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "ValorIndicador.porIndicadorEAno", query = "SELECT v FROM ValorIndicador v where v.ano = :ano  AND v.indicador = :indicador"),
		@NamedQuery(name = "ValorIndicador.porIndicadorEMunicipio", query = "SELECT v FROM ValorIndicador v where v.indicador = :indicador  AND v.municipio = :municipio", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }), 
		@NamedQuery(name = "ValorIndicador.porMunicipio", query = "SELECT v FROM ValorIndicador v where v.municipio = :municipio"),
		@NamedQuery(name = "ValorIndicador.porAnoMunicipio", query = "SELECT v FROM ValorIndicador v where v.municipio = :municipio AND v.ano = :ano", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "ValorIndicador.porMunicipioArea", query = "SELECT v FROM ValorIndicador v where v.municipio = :municipio AND v.indicador.area= :area", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "ValorIndicador.porAnoMunicipios", query = "SELECT v FROM ValorIndicador v where v.municipio.id IN (:municipios) AND v.ano = :ano", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "ValorIndicador.mediaPorAno", query = "SELECT v.indicador, avg(v.valor) FROM ValorIndicador v where v.ano = :ano group by v.indicador", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "ValorIndicador.porMunicipioAreaAno", query = "SELECT v FROM ValorIndicador v where v.municipio = :municipio AND v.indicador.area= :area AND v.ano = :ano", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }), })

public class ValorIndicador {

	@Id
	@GeneratedValue
	@Column(name = "vin_id")
	private long id;

	@Column(name = "vin_valor")
	private float valor;

	@Column(name = "vin_ano")
	private int ano;

	@ManyToOne(optional = true)
	@JoinColumn(name = "vin_ind_id")
	private Indicador indicador;

	@ManyToOne
	@JoinColumn(name = "vin_mun_id")
	private Municipio municipio;

	public ValorIndicador() {
	}

	public ValorIndicador(float valor, int ano, Indicador indicador,
			Municipio municipio) {
		this.valor = valor;
		this.ano = ano;
		this.indicador = indicador;
		this.municipio = municipio;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public float getValor() {
		return valor;
	}

	public void setValor(float valor) {
		this.valor = valor;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public Indicador getIndicador() {
		return indicador;
	}

	public void setIndicador(Indicador indicador) {
		this.indicador = indicador;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	@Override
	public String toString() {
		return "ValorIndicador [id=" + id + ", valor=" + valor + ", ano=" + ano
				+ ", indicador=" + indicador + ", municipio=" + municipio + "]";
	}

}
