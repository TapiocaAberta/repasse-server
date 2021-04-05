package org.sjcdigital.repasse.model.transferencia;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 
 * Irá conter informações sobre a carga dos dados de transferëncias
 * 
 * @author wsiqueir
 *
 */
@Entity
@Table(name = "carga_transferencia", uniqueConstraints = @UniqueConstraint(columnNames = {
		"ano", "mes" }))
@NamedQueries({ @NamedQuery(name = "CargaTransfInfo.porAnoMes", query = "SELECT c FROM CargaTransfInfo c WHERE c.ano = :ano AND c.mes = :mes") })
public class CargaTransfInfo {

	@Id
	@GeneratedValue
	private int id;
	private int ano;
	private int mes;
	private int qtdeLinhas;
	private int qtdeNaoProcessada;
	private int qtdeSucesso;
	private int qtdeFalhas;
	private Date inicio;
	private Date fim;

	public CargaTransfInfo() {

	}

	public CargaTransfInfo(int ano, int mes) {
		super();
		this.ano = ano;
		this.mes = mes;
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

	public int getQtdeSucesso() {
		return qtdeSucesso;
	}

	public void setQtdeSucesso(int qtdeSucesso) {
		this.qtdeSucesso = qtdeSucesso;
	}

	public int getQtdeFalhas() {
		return qtdeFalhas;
	}

	public void setQtdeFalhas(int qtdeFalhas) {
		this.qtdeFalhas = qtdeFalhas;
	}

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getFim() {
		return fim;
	}

	public void setFim(Date fim) {
		this.fim = fim;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQtdeLinhas() {
		return qtdeLinhas;
	}

	public void setQtdeLinhas(int qtdeLinhas) {
		this.qtdeLinhas = qtdeLinhas;
	}

	public int getQtdeNaoProcessada() {
		return qtdeNaoProcessada;
	}

	public void setQtdeNaoProcessada(int qtdeNaoProcessada) {
		this.qtdeNaoProcessada = qtdeNaoProcessada;
	}

}
