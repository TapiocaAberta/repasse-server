package org.jugvale.transparencia.transf.resource;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jugvale.transparencia.transf.model.base.Estado;
import org.jugvale.transparencia.transf.service.impl.EstadoService;

@Path("estado")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class EstadoResource {

	@Inject
	EstadoService estadoService;

	@GET
	public List<Estado> todosEstados() {
		return estadoService.todos();
	}	
	
	@GET
	@Path("/{sigla: [A-Z][A-Z]}")
	public Estado estadoPorSigla(@PathParam("sigla") String sigla) {
		return estadoService.buscaEstadoPorSigla(sigla);		
	}
}
