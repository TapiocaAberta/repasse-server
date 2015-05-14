package org.jugvale.transparencia.transf.resource;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jugvale.transparencia.transf.model.base.Municipio;
import org.jugvale.transparencia.transf.service.impl.MunicipioService;


@Path("/municipio")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class MunicipioResource {
	
	@Inject 
	MunicipioService service;
	
	@GET
	public List<Municipio> todos() {
		return service.todos();		
	}

}
