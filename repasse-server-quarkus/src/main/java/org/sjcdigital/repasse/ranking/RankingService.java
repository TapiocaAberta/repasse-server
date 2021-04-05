package org.sjcdigital.repasse.ranking;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import io.quarkus.cache.CacheResult;
import org.sjcdigital.repasse.model.base.DadosMunicipio;
import org.sjcdigital.repasse.model.ranking.RankingTransferencias;
import org.sjcdigital.repasse.model.ranking.ResultadosRanking;
import org.sjcdigital.repasse.service.impl.DadosMunicipioService;

/**
 * 
 * A controversa classe para realização da busca do ranking usando query nativa.
 * 
 * @author wsiqueir
 *
 */
@RequestScoped
public class RankingService {

	private static final String TITULO_RANKING_ANO = "Municípios que mais receberam recursos no ano ";

	@Inject
	EntityManager em;

	@Inject
	DadosMunicipioService dadosMunicipioService;

	@CacheResult(cacheName = "ranking-cache")
	public RankingTransferencias buscaRankingPorAno(int ano) {
		RankingTransferencias rankingTransferencias = dadosBaseRanking(ano);
		List<Long> idMunicipios = rankingTransferencias.getResultados()
				.stream()
				.map(r -> r.getIdMunicipio()).collect(Collectors.toList());
		//TODO: Não será necessário quando tivermos o MIQL no banco. Ao fechar issue #180
		List<String> nomesMunicipios = rankingTransferencias.getResultados()
				.stream()
				// Super gambiarra que será tirada quando tivermos a carga do MIQL
				.map(r -> r.getNomeCidade().split("\\ \\-")[0])
				.collect(Collectors.toList());
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
		Map<String, Float> buscaMiqlParaMunicipios = dadosMunicipioService.buscaMiqlParaMunicipios(nomesMunicipios, ano);
		// mega gambiarra - trocar quando tivermos o MIQL-T completo! Assim teremos carga no banco e talz
		rankingTransferencias
				.getResultados()
				.stream().forEach(r -> {
					String nomeCidade = r.getNomeCidade().split("\\ \\-")[0];
					buscaMiqlParaMunicipios.computeIfPresent(nomeCidade, (nome, miqlt) -> {
						r.setMiqlt(miqlt);
						return miqlt;
					});
				});
//		buscaMiqlParaMunicipios.forEach((nome, miqlt) -> {
//			rankingTransferencias
//			.getResultados()
//			.stream()
//			.filter(r -> r.getNomeCidade().split("\\ \\-")[0].equals(nome))
//			.findFirst().ifPresent(r -> {
//				r.setMiqlt(miqlt);
//			});
//		});
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
