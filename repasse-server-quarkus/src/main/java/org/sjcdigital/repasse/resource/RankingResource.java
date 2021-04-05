package org.sjcdigital.repasse.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.sjcdigital.repasse.model.base.Municipio;
import org.sjcdigital.repasse.model.ranking.RankingTransferencias;
import org.sjcdigital.repasse.model.ranking.ResultadosRanking;
import org.sjcdigital.repasse.ranking.RankingController;
import org.sjcdigital.repasse.service.impl.DadosMunicipioService;
import org.sjcdigital.repasse.service.impl.MunicipioService;
import org.sjcdigital.repasse.service.impl.TransferenciaService;
import org.sjcdigital.repasse.utils.JaxrsUtils;

/**
 * 
 * Classe boilerplate para expor as operações do Ranking. <br />
 * 
 * @author wsiqueir
 *
 */
@Path("ranking/{ano}")
@Produces(MediaType.APPLICATION_JSON)
public class RankingResource {
	// TODO: verificar parâmetros e retornar 404 quando ano, área, acao ou programa não existirem.
	
	@Inject
	RankingController controller;
	
	@Inject
	DadosMunicipioService dadosMunicipioService;
	
	@Inject
	MunicipioService municipioService;

	@PathParam("ano")
	int ano;
	
	@Inject
	TransferenciaService transferenciaService;
	
	@GET
	public RankingTransferencias rankingPorAno() {
		ano = dadosMunicipioService.anoOuMaisRecente(ano);
		JaxrsUtils.lanca404SeFalso(transferenciaService.temTranferencia(ano));
		return controller.rankingPorAno(ano);
	} 
	
	
	@GET
	@Path("/{sigla}/{nome}")
	public ResultadosRanking rankingPorAnoMunicipio(@PathParam("sigla") String sigla, @PathParam("nome") String nome) {
		ano = dadosMunicipioService.anoOuMaisRecente(ano);
		Municipio m = municipioService.buscaPorNomeEEstado(sigla, nome);
		JaxrsUtils.lanca404SeFalso(transferenciaService.temTranferencia(ano));
		return controller.buscaRankingMunicipio(ano, m);
	} 
	
	
	/**
	 * 
	 * Monta o ranking levando em conta os IDs passados como parâmetros.
	 * 
	 * @param idsMunicipios
	 * @return
	 */
	@GET
	@Path("municipios/{idsMunicipios}")
	public List<ResultadosRanking>  rankingPorAnoMunicipios(@PathParam("idsMunicipios") String idsMunicipios) {
		List<ResultadosRanking> resultado = new ArrayList<>();
		ano = dadosMunicipioService.anoOuMaisRecente(ano);
		JaxrsUtils.lanca404SeFalso(transferenciaService.temTranferencia(ano));
		Stream.of(idsMunicipios.split("\\,")).forEach(id -> {
			Municipio m = municipioService.buscarPorId(Long.parseLong(id));
			resultado.add(controller.buscaRankingMunicipio(ano, m));
		});
		return resultado;
	} 
	
	@GET
	@Path("area/{area}")
	public RankingTransferencias rankingPorAnoArea(@PathParam("area") String area) {
		JaxrsUtils.lanca404SeFalso(transferenciaService.temTranferencia(ano));
		return controller.rankingPorAnoArea(ano, area);
	} 
	
	@GET
	@Path("acao/{acao}")
	public RankingTransferencias rankingPorAnoAcao(@PathParam("acao") String acao) {
		JaxrsUtils.lanca404SeFalso(transferenciaService.temTranferencia(ano));
		return controller.rankingPorAnoAcao(ano, acao);
	} 
	
	@GET
	@Path("programa/{programa}")
	public RankingTransferencias rankingPorAnoPrograma(@PathParam("programa") String programa) {
		JaxrsUtils.lanca404SeFalso(transferenciaService.temTranferencia(ano));		
		return controller.rankingPorAnoPrograma(ano, programa);
	} 
	
	@GET
	@Path("sub-funcao/{subFuncao}")
	public RankingTransferencias rankingPorAnoSubFuncao(@PathParam("subFuncao") String subFuncao) {
		JaxrsUtils.lanca404SeFalso(transferenciaService.temTranferencia(ano));
		return controller.rankingPorAnoSubFuncao(ano, subFuncao);
	} 
	
}