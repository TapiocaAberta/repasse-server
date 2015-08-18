package org.jugvale.transfgov.ranking;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.resteasy.spi.NotImplementedYetException;
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

	private static final String TITULO_RANKING_ANO = "Municípios que mais receberam recursos no ano";
	private static final int TAMANHO_RANKING = 10;
	
	@PersistenceContext
	EntityManager em;	

	@Inject
	RankingCache cache;

	/**
	 * 
	 * Retorna o ranking de repasses para o ano passado.
	 * @param ano
	 * @return
	 */
	public RankingTransferencias rankingPorAno(int ano) {
		return cache.retornaOuAdiciona(ano, () -> buscaRankingPorAno(ano));
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
	
	private RankingTransferencias buscaRankingPorAno(int ano) {
		// Devido HHH-9111, fazer cache desse método manualmente 
		Query buscaRanking = em.createNamedQuery("Ranking.porAno");
		RankingTransferencias ranking = new RankingTransferencias();
		@SuppressWarnings("unchecked")
		List<Object[]> resultado = buscaRanking.setParameter("ano", ano).setMaxResults(TAMANHO_RANKING).getResultList();	
		List<ResultadosRanking> resultadosRanking = resultado.stream().map(r -> {
				Object[] o = (Object[]) r;
				String municipio = String.valueOf(o[0]);
				double totalPerCapita = (double) o[1];
				double total = (double) o[2];				
				BigInteger populacao = (BigInteger) o[3];
				return new ResultadosRanking(municipio, populacao.intValue(), total, totalPerCapita);
			}).collect(Collectors.toList());
		ranking.setResultados(resultadosRanking);
		ranking.setNome(TITULO_RANKING_ANO);
		ranking.setAno(ano);
		return ranking;
	}

}