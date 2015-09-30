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
	 * A posição no ranking
	 */
	private int posicao;
	

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
	
	private long idMunicipio; 
	
	public ResultadosRanking() {
		super();
	}

	public ResultadosRanking(int posicao, String nomeCidade, int populacao,
			double valorTotal, double valorPerCapita, long idMunicipio) {
		super();
		this.posicao = posicao;
		this.nomeCidade = nomeCidade;
		this.populacao = populacao;
		this.valorTotal = valorTotal;
		this.valorPerCapita = valorPerCapita;
		this.idMunicipio = idMunicipio;
	}

	public ResultadosRanking(String nomeCidade, int populacao,
			double valorTotal, double valorPerCapita) {
		super();
		this.nomeCidade = nomeCidade;
		this.populacao = populacao;
		this.valorTotal = valorTotal;
		this.valorPerCapita = valorPerCapita;
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

	public int getPosicao() {
		return posicao;
	}

	public void setPosicao(int posicao) {
		this.posicao = posicao;
	}

	public long getIdMunicipio() {
		return idMunicipio;
	}

	public void setIdMunicipio(long idMunicipio) {
		this.idMunicipio = idMunicipio;
	}		
	
}