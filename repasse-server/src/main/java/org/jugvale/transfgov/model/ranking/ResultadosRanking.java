package org.jugvale.transfgov.model.ranking;

/**
 * 
 * Cada linha de um ranking
 * 
 * @author wsiqueir
 *
 */
public class ResultadosRanking {

	/**
	 * A sigla do estado da cidade
	 */
	private String siglaEstado;
	/**
	 * O nome da cidade
	 */
	private String nomeCidade;
	/**
	 * A população para o ano em questão
	 */
	private int populacao;
	/**
	 * O valor total transferido
	 */
	private double valorTotal;
	/**
	 * O valor Per Capita
	 */
	private double valorPerCapita;

	public String getSiglaEstado() {
		return siglaEstado;
	}

	public void setSiglaEstado(String siglaEstado) {
		this.siglaEstado = siglaEstado;
	}

	public String getNomeCidade() {
		return nomeCidade;
	}

	public void setNomeCidade(String nomeCidade) {
		this.nomeCidade = nomeCidade;
	}

	public int getPopulacao() {
		return populacao;
	}

	public void setPopulacao(int populacao) {
		this.populacao = populacao;
	}

	public double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public double getValorPerCapita() {
		return valorPerCapita;
	}

	public void setValorPerCapita(double valorPerCapita) {
		this.valorPerCapita = valorPerCapita;
	}

}
