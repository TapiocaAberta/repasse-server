package org.jugvale.transfgov.model.agregacao;

import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.jugvale.transfgov.model.base.Estado;
import org.jugvale.transfgov.model.base.Municipio;

/**
 * 
 * Representa uma agregação(soma) de informações de transfências.
 * 
 * @author wsiqueir
 *
 */
@XmlRootElement
public class Agregacao {

	/**
	 * Ano das transferências
	 */
	private int ano;

	/**
	 * Mês das transferências
	 */
	private int mes;

	/**
	 * Estado
	 */
	private Estado estado;

	/**
	 * O município
	 */
	private Municipio municipio;

	/**
	 * O Tipo de agregação de dados
	 */
	private TipoAgregacao tipoAgregacao;

	/**
	 * O conjunto chave valor dos dados agregados
	 */
	private Map<Object, Double> dadosAgregados;

	public Agregacao() {

	}

	public Agregacao(int ano, int mes, Estado estado, Municipio municipio,
			TipoAgregacao tipoAgregacao, Map<Object, Double> dadosAgregados) {
		super();
		this.ano = ano;
		this.mes = mes;
		this.estado = estado;
		this.municipio = municipio;
		this.tipoAgregacao = tipoAgregacao;
		this.dadosAgregados = dadosAgregados;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public int getMes() {
		return mes;
	}

	public void setMes(int mes) {
		this.mes = mes;
	}

	public Map<Object, Double> getDadosAgregados() {
		return dadosAgregados;
	}

	public void setDadosAgregados(Map<Object, Double> dadosAgregados) {
		this.dadosAgregados = dadosAgregados;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	public TipoAgregacao getTipoAgregacao() {
		return tipoAgregacao;
	}

	public void setTipoAgregacao(TipoAgregacao tipoAgregacao) {
		this.tipoAgregacao = tipoAgregacao;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}
}
