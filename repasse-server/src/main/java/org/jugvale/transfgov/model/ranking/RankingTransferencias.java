package org.jugvale.transfgov.model.ranking;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wsiqueir
 *
 */
public class RankingTransferencias {

	/**
	 * Um nome para esse ranking, por exemplo "Ranking de transferências para a área de Educação em 2015"
	 */
	private String nome;
	
	
	/**
	 *  O ano em questão
	 */
	private int ano;
	
	/**
	 * A linhas do ranking
	 */
	List<ResultadosRanking> resultados;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public List<ResultadosRanking> getResultados() {
		return resultados;
	}

	public void setResultados(List<ResultadosRanking> resultados) {
		this.resultados = resultados;
	}	

	@Override
	public RankingTransferencias clone() {
		RankingTransferencias clone = new RankingTransferencias();
		clone.setAno(this.getAno());
		clone.setNome(this.nome);
		List<ResultadosRanking> r = new ArrayList<>();
		this.getResultados().forEach(r::add);
		clone.setResultados(r);
		return clone;
	}

}
