package org.jugvale.transfgov.resource;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jugvale.transfgov.model.ranking.RankingTransferencias;
import org.jugvale.transfgov.ranking.RankingController;
import org.jugvale.transfgov.service.impl.TransferenciaService;
import org.jugvale.transfgov.utils.JaxrsUtils;

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

	@PathParam("ano")
	int ano;
	
	
	@Inject
	TransferenciaService transferenciaService;
	
	@GET
	public RankingTransferencias rankingPorAno() {
		JaxrsUtils.lanca404SeFalso(transferenciaService.temTranferencia(ano));
		return controller.rankingPorAno(ano);
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