package org.jugvale.transparencia.transf.resource;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jugvale.transparencia.transf.model.base.Municipio;
import org.jugvale.transparencia.transf.model.transferencia.Transferencia;
import org.jugvale.transparencia.transf.service.impl.MunicipioService;
import org.jugvale.transparencia.transf.service.impl.TransferenciaService;
import org.jugvale.transparencia.transf.utils.JaxrsUtils;

@Path("transferencia")
@Produces("application/json; charset=utf8")
public class TransferenciaResource {
	
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
	@GET
	@Path("{ano}/{mes}/municipio/{municipioId}")
	public List<Transferencia> porAnoMesMunicipio(@PathParam("ano") int ano,
			@PathParam("mes") int mes,
			@PathParam("municipioId") long municipioId) {
		Municipio municipio = JaxrsUtils.lanca404SeNulo(municipioService.buscarPorId(municipioId), Municipio.class);		
		return transferenciaService.buscarPorAnoMesMunicipio(ano, mes, municipio);
	}

}