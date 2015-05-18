package org.jugvale.transparencia.transf.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jugvale.transparencia.transf.model.base.Municipio;

@Path("municipio")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public interface MunicipioResource {

	@GET
	public List<Municipio> todosMunicipios();
	@GET
	@Path("{sigla}/{nome}")
	public Municipio porNomeEEstado(@PathParam("sigla") String sigla, @PathParam("nome") String nome);

}
