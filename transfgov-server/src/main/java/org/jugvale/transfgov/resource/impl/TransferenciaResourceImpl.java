package org.jugvale.transfgov.resource.impl;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jugvale.transfgov.model.base.Municipio;
import org.jugvale.transfgov.model.transferencia.Transferencia;
import org.jugvale.transfgov.resource.TransferenciaResource;
import org.jugvale.transfgov.service.impl.MunicipioService;
import org.jugvale.transfgov.service.impl.TransferenciaService;
import org.jugvale.transfgov.utils.JaxrsUtils;

public class TransferenciaResourceImpl implements TransferenciaResource {
	
	@Inject
	TransferenciaService transferenciaService;
	
	@Inject
	MunicipioService municipioService;

	/**
	 * 
	 * Retorna todas as transferências para o ano, mês e município passados
	 * 
	 * @param ano
	 * @param mes
	 * @param municipioId
	 * @return
	 */
	public List<Transferencia> porAnoMesMunicipio(int ano, int mes, long municipioId) {
		Municipio municipio = JaxrsUtils.lanca404SeNulo(municipioService.buscarPorId(municipioId), Municipio.class);		
		return transferenciaService.buscarPorAnoMesMunicipio(ano, mes, municipio);
	}
	
	public Response apagaPorMesAno(int ano, int mes){
		JaxrsUtils.lanca404SeFalso(transferenciaService.temTranferencia(ano, mes), String.format(MSG_NAO_HA_TRANSFERENCIA, mes, ano));
		try {
			transferenciaService.apagaTransferencias(ano, mes);
			return Response.ok().entity(String.format(MSG_TRANSFERENCIA_APAGADAS, mes, ano)).build();
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
		}
		
	}	

}