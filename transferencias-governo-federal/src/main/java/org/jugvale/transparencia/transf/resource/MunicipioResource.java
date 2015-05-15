package org.jugvale.transparencia.transf.resource;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jugvale.transparencia.transf.model.base.Municipio;
import org.jugvale.transparencia.transf.service.impl.MunicipioService;

@Path("municipio")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class MunicipioResource {

	@Inject
	MunicipioService municipioService;

	@GET
	public List<Municipio> todosMunicipios() {
		return municipioService.todos();
	}
	
	@GET
	@Path("{sigla}/{nome}")
	public Municipio porNomeEEstado(@PathParam("sigla") String sigla, @PathParam("nome") String nome) {
		return municipioService.buscaPorNomeEEstado(sigla, nome);		
	}

}
