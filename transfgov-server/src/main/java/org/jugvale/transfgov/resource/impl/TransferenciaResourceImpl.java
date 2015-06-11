package org.jugvale.transfgov.resource.impl;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.jugvale.transfgov.model.base.Municipio;
import org.jugvale.transfgov.model.transferencia.Transferencia;
import org.jugvale.transfgov.resource.TransferenciaResource;
import org.jugvale.transfgov.service.impl.CargaTransfInfoService;
import org.jugvale.transfgov.service.impl.MunicipioService;
import org.jugvale.transfgov.service.impl.TransferenciaService;
import org.jugvale.transfgov.utils.JaxrsUtils;

public class TransferenciaResourceImpl implements TransferenciaResource {
	
	@Inject
	TransferenciaService transferenciaService;
	
	@Inject
	MunicipioService municipioService;
	
	@Context
	UriInfo uriInfo;
	
	@Inject
	CargaTransfInfoService cargaTransfInfoService;

	/**
	 * 
	 * Retorna todas as transferências para o ano, mês e município passados
	 * 
	 * @param ano
	 * @param mes
	 * @param municipioId
	 * @return
	 */
	public Response porAnoMesMunicipio(int ano, int mes, long municipioId, int pg) {	
		ResponseBuilder resposta = Response.ok();		
		Municipio municipio = JaxrsUtils.lanca404SeNulo(municipioService.buscarPorId(municipioId), Municipio.class);		
		long totalResultados = transferenciaService.contaPorAnoMesMunicipio(ano, mes, municipio);
		UriBuilder urlBase = uriInfo.getBaseUriBuilder().path(TransferenciaResource.class).path(TransferenciaResource.class, "porAnoMesMunicipio");		
		JaxrsUtils.constroiLinksNavegacao(uriInfo, urlBase, totalResultados, pg, ano, mes, municipioId).stream().forEach(resposta::links);
		List<Transferencia> resultado = transferenciaService.buscarPorAnoMesMunicipioPaginado(ano, mes, municipio, JaxrsUtils.TOTAL_POR_PAGINA, pg);
		JaxrsUtils.lanca404SeVazio(resultado);
		resposta.entity(resultado);
		return resposta.build();
	}
	
	public Response apagaPorMesAno(int ano, int mes){
		JaxrsUtils.lanca404SeFalso(transferenciaService.temTranferencia(ano, mes), String.format(MSG_NAO_HA_TRANSFERENCIA, mes, ano));
		try {
			transferenciaService.apagaTransferencias(ano, mes);
			cargaTransfInfoService.apagaPorAnoMes(ano, mes);
			return Response.ok().entity(String.format(MSG_TRANSFERENCIA_APAGADAS, mes, ano)).build();
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
		}		
	}	

}