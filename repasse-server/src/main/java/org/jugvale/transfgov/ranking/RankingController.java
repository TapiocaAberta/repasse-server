package org.jugvale.transfgov.ranking;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.resteasy.spi.NotImplementedYetException;
import org.jugvale.transfgov.model.base.Municipio;
import org.jugvale.transfgov.model.ranking.RankingTransferencias;
import org.jugvale.transfgov.model.ranking.ResultadosRanking;
import org.jugvale.transfgov.service.impl.DadosMunicipioService;

/**
 * 
 * Acessa os dados de transferência diretamente montando um ranking per capita
 * 
 * @author wsiqueir
 *
 */
@RequestScoped
public class RankingController {

	private static final String TITULO_RANKING_ANO = "Municípios que mais receberam recursos no ano ";
	private static final int TAMANHO_RANKING = 50;
	
	@PersistenceContext
	EntityManager em;	

	@Inject
	RankingCache cache;
	
	@Inject 
	DadosMunicipioService dadosMunicipioService;

	/**
	 * 
	 * Retorna o ranking de repasses para o ano passado.
	 * @param ano
	 * @return
	 */
	public RankingTransferencias rankingPorAno(int ano) {
		// usa um clone para manter a lista cheia em cache
		RankingTransferencias ranking =  (cache.retornaOuAdiciona(ano, () -> buscaRankingPorAno(ano))).clone();
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
		RankingTransferencias ranking =  cache.retornaOuAdiciona(ano, () -> buscaRankingPorAno(ano));
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
	
	private RankingTransferencias buscaRankingPorAno(int ano) {
		RankingTransferencias rankingTransferencias = dadosBaseRanking(ano);
		List<Long> idMunicipios = rankingTransferencias.getResultados().stream().map(r -> r.getIdMunicipio()).collect(Collectors.toList());
		// Vamos preencher o IDHM, poderia também vir  da query Transferencia.Ranking.porAno?
		dadosMunicipioService.buscaIDHParaMunicipiosAnoMaisRecente(idMunicipios, ano).forEach(d -> {
			rankingTransferencias.getResultados().stream()
			.filter(r -> r.getIdMunicipio() == d.getMunicipio().getId())
			.findFirst().ifPresent(r -> {
				r.setIdhm(d.getIdhm());
			});
		});;
		return rankingTransferencias;
	}

	private RankingTransferencias dadosBaseRanking(int ano) {
		// Devido HHH-9111, fazer cache desse método manualmente  - Incluir o IDHM? Aumentaria demais a query?
		Query buscaRanking = em.createNamedQuery("Transferencia.Ranking.porAno");
		RankingTransferencias ranking = new RankingTransferencias();
		@SuppressWarnings("unchecked")
		List<Object[]> resultado = buscaRanking.setParameter("ano", ano).getResultList();
		AtomicInteger posicao = new AtomicInteger(1);
		List<ResultadosRanking> resultadosRanking = resultado.stream().map(r -> {
				Object[] o = (Object[]) r;
				String municipio = String.valueOf(o[0]);
				double totalPerCapita = (double) o[1];
				double total = (double) o[2];				
				BigInteger populacao = (BigInteger) o[3];
				BigInteger idMunicipio = (BigInteger) o[4];
				return new ResultadosRanking(posicao.getAndIncrement(), municipio, populacao.intValue(), total, totalPerCapita, idMunicipio.longValue());
			}).collect(Collectors.toList());
		ranking.setResultados(resultadosRanking);
		ranking.setNome(TITULO_RANKING_ANO + ano);
		ranking.setAno(ano);
		return ranking;
	}

}