package org.jugvale.transparencia.transf.resource;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jugvale.transparencia.transf.model.base.Municipio;
import org.jugvale.transparencia.transf.model.transferencia.Transferencia;
import org.jugvale.transparencia.transf.service.impl.MunicipioService;
import org.jugvale.transparencia.transf.service.impl.TransferenciaService;

@Path("transferencia")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class TransferenciaResource {
	
	@Inject
	TransferenciaService transferenciaService;
	
	@Inject
	MunicipioService municipioService;

	@GET
	@Path("{ano}/{mes}/municipio/{municipioId}")
	public List<Transferencia> porAnoMesMunicipio(@PathParam("ano") int ano,
			@PathParam("mes") int mes,
			@PathParam("municipioId") long municipioId) {
		Municipio municipio = municipioService.buscarPorId(municipioId);
		return transferenciaService.buscarPorAnoMesMunicipio(ano, mes, municipio);
	}

}
