package org.sjcdigital.repasse.model.base;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * Dados gerais anuais do município.
 * 
 * @author wsiqueir
 *
 */
@XmlRootElement
@Entity
@Table(name = "dados_municipio", uniqueConstraints = @UniqueConstraint(columnNames = {
		"ano", "municipio_mun_id" }))
@NamedQueries({
		@NamedQuery(name = "DadosMunicipio.porAnoEMunicipio", query = "SELECT d FROM DadosMunicipio d WHERE d.ano = :ano AND d.municipio = :municipio", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "DadosMunicipio.porMunicipio", query = "SELECT d FROM DadosMunicipio d WHERE d.municipio = :municipio", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "DadosMunicipio.anoMaisRecente", query = "SELECT max(d.ano) FROM DadosMunicipio d", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "DadosMunicipio.contaLinhasPorAno", query = "SELECT count(d) FROM DadosMunicipio d where d.ano = :ano", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "DadosMunicipio.temDadosParaAno", query = "SELECT case when (count(d) > 0)  then true else false end FROM DadosMunicipio d where d.ano = :ano", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),		
		@NamedQuery(name = "DadosMunicipio.somaPopulacaoPorAno", query = "SELECT sum(d.populacao) FROM DadosMunicipio d WHERE d.ano = :ano", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "DadosMunicipio.anoMaisRecenteIDH", query = "SELECT max(d.ano) FROM DadosMunicipio d WHERE d.ano <= :ano AND d.idhm > 0", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "DadosMunicipio.buscaIDHParaMunicipios", query = "SELECT d FROM DadosMunicipio d WHERE d.municipio.id IN :ids AND d.ano = :ano", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }) 

	})
public class DadosMunicipio {

	@Id
	@GeneratedValue
	private int id;
	private int ano;
	@ManyToOne
	@JoinColumn(name = "municipio_mun_id")
	private Municipio municipio;
	private long populacao;
	private float idhm;
	/**
	 * IDH educação
	 */
	private float idhEducacao;
	/**
	 * IDH Longetividade
	 */
	private float idhLongevidade;
	/**
	 * IDH Renda
	 */
	private float idhRenda;
	
	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	public long getPopulacao() {
		return populacao;
	}

	public void setPopulacao(long populacao) {
		this.populacao = populacao;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getIdhm() {
		return idhm;
	}

	public void setIdhm(float idhm) {
		this.idhm = idhm;
	}

	public float getIdhEducacao() {
		return idhEducacao;
	}

	public void setIdhEducacao(float idhe) {
		this.idhEducacao = idhe;
	}

	public float getIdhLongevidade() {
		return idhLongevidade;
	}

	public void setIdhLongevidade(float idhl) {
		this.idhLongevidade = idhl;
	}

	public float getIdhRenda() {
		return idhRenda;
	}

	public void setIdhRenda(float idhr) {
		this.idhRenda = idhr;
	}
}