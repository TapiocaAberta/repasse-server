package org.jugvale.transparencia.transf.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jugvale.transparencia.transf.model.transferencia.Transferencia;

@Path("transferencia")
@Produces("application/json; charset=utf8")
public interface TransferenciaResource {

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
	public List<Transferencia> porAnoMesMunicipio(@PathParam("ano") int ano,@PathParam("mes") int mes, @PathParam("municipioId") long municipioId);

}