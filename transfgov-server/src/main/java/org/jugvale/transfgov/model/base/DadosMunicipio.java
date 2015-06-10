package org.jugvale.transfgov.model.base;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
		@NamedQuery(name = "DadosMunicipio.porAnoEMunicipio", query = "SELECT d FROM DadosMunicipio d WHERE d.ano = :ano AND d.municipio = :municipio"),
		@NamedQuery(name = "DadosMunicipio.porMunicipio", query = "SELECT d FROM DadosMunicipio d WHERE d.municipio = :municipio") })
public class DadosMunicipio {

	@Id
	@GeneratedValue
	private int id;
	private int ano;
	@ManyToOne
	@JoinColumn(name = "municipio_mun_id")
	private Municipio municipio;
	private long populacao;
	
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

}