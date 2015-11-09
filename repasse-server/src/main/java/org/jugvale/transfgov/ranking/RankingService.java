package org.jugvale.transfgov.ranking;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.cache.annotation.CacheResult;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jugvale.transfgov.model.base.DadosMunicipio;
import org.jugvale.transfgov.model.ranking.RankingTransferencias;
import org.jugvale.transfgov.model.ranking.ResultadosRanking;
import org.jugvale.transfgov.service.impl.DadosMunicipioService;

/**
 * 
 * A controversa classe para realização da busca do ranking usando query nativa.
 * 
 * @author wsiqueir
 *
 */
@Stateless
public class RankingService {

	private static final String TITULO_RANKING_ANO = "Municípios que mais receberam recursos no ano ";

	@PersistenceContext
	EntityManager em;

	@Inject
	DadosMunicipioService dadosMunicipioService;

	@CacheResult(cacheName = "ranking-cache")
	public RankingTransferencias buscaRankingPorAno(int ano) {
		RankingTransferencias rankingTransferencias = dadosBaseRanking(ano);
		List<Long> idMunicipios = rankingTransferencias.getResultados()
				.stream()
				.map(r -> r.getIdMunicipio()).collect(Collectors.toList());
		// Vamos preencher o IDHM, poderia também vir da query
		// Transferencia.Ranking.porAno?
		List<DadosMunicipio> dadosIDH = dadosMunicipioService
				.buscaIDHParaMunicipiosAnoMaisRecente(idMunicipios, ano);
		dadosIDH.forEach(d -> {
			rankingTransferencias
					.getResultados()
					.stream()
					.filter(r -> r.getIdMunicipio() == d.getMunicipio().getId())
					.findFirst().ifPresent(r -> {
						r.setIdhm(d.getIdhm());
					});
		});
		;
		return rankingTransferencias;
	}

	private RankingTransferencias dadosBaseRanking(int ano) {
		// Devido HHH-9111, fazer cache desse método manualmente - Incluir o
		// IDHM? Aumentaria demais a query?
		Query buscaRanking = em
				.createNamedQuery("Transferencia.Ranking.porAno");
		RankingTransferencias ranking = new RankingTransferencias();
		@SuppressWarnings("unchecked")
		List<Object[]> resultado = buscaRanking.setParameter("ano", ano)
				.getResultList();
		AtomicInteger posicao = new AtomicInteger(1);
		List<ResultadosRanking> resultadosRanking = resultado
				.stream()
				.map(r -> {
					Object[] o = (Object[]) r;
					String municipio = String.valueOf(o[0]);
					double totalPerCapita = (double) o[1];
					double total = (double) o[2];
					BigInteger populacao = (BigInteger) o[3];
					BigInteger idMunicipio = (BigInteger) o[4];
					return new ResultadosRanking(posicao.getAndIncrement(),
							municipio, populacao.intValue(), total,
							totalPerCapita, idMunicipio.longValue());
				}).collect(Collectors.toList());
		ranking.setResultados(resultadosRanking);
		ranking.setNome(TITULO_RANKING_ANO + ano);
		ranking.setAno(ano);
		return ranking;
	}
}
