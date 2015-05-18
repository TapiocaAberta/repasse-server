package org.jugvale.transfgov.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jugvale.transfgov.model.base.Estado;

@Path("estado")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public interface EstadoResource {

	@GET
	public List<Estado> todosEstados();
	
	@GET
	@Path("/{sigla: [A-Z][A-Z]}")
	public Estado estadoPorSigla(@PathParam("sigla") String sigla);
	
}
