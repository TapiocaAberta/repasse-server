package org.jugvale.transfgov.ranking;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jboss.resteasy.spi.NotImplementedYetException;
import org.jugvale.transfgov.model.base.Municipio;
import org.jugvale.transfgov.model.ranking.RankingTransferencias;
import org.jugvale.transfgov.model.ranking.ResultadosRanking;

/**
 * 
 * Acessa os dados de transferência diretamente montando um ranking per capita
 * 
 * @author wsiqueir
 *
 */
@RequestScoped
public class RankingController {

	private static final int TAMANHO_RANKING = 50;
	
	@Inject
	RankingService service;
	
	/**
	 * 
	 * Retorna o ranking de repasses para o ano passado.
	 * @param ano
	 * @return
	 */
	public RankingTransferencias rankingPorAno(int ano) {
		RankingTransferencias ranking =  service.buscaRankingPorAno(ano).clone();
		ranking.setResultados(ranking.getResultados().subList(0, TAMANHO_RANKING));
		return ranking;
	}
	
	/**
	 * 
	 * Retorna os dados de ranking para um município específico
	 * @param ano
	 * @param m
	 * @return
	 */
	public ResultadosRanking buscaRankingMunicipio(int ano, Municipio m) {
		RankingTransferencias ranking =  service.buscaRankingPorAno(ano);
		return ranking.getResultados().stream().filter(r -> r.getNomeCidade().equals(m.toString())).findFirst().get();
	}

	public RankingTransferencias rankingPorAnoArea(int ano, String area) {
		// TODO implementar
		throw new NotImplementedYetException();
	}

	public RankingTransferencias rankingPorAnoAcao(int ano, String acao) {
		// TODO implementar
		throw new NotImplementedYetException();
	}

	public RankingTransferencias rankingPorAnoPrograma(int ano, String programa) {
		// TODO implementar
		throw new NotImplementedYetException();
	}

	public RankingTransferencias rankingPorAnoSubFuncao(int ano,
			String subFuncao) {
		// TODO implementar
		throw new NotImplementedYetException();
	}
	
}