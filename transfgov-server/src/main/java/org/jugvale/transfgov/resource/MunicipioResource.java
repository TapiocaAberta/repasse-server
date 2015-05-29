package org.jugvale.transfgov.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jugvale.transfgov.model.base.Municipio;
import org.jugvale.transfgov.utils.JaxrsUtils;

@Path("municipio")
@Produces({ MediaType.APPLICATION_JSON })
public interface MunicipioResource {
	
	@GET
	public Response todosMunicipios(@QueryParam(JaxrsUtils.PARAMETRO_PAGINA) int pg);

	@GET
	@Path("{sigla}/{nome}")
	public Municipio porNomeEEstado(@PathParam("sigla") String sigla, @PathParam("nome") String nome);

}
