package org.jugvale.transfgov.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jugvale.transfgov.model.base.Estado;
import org.jugvale.transfgov.model.base.Municipio;
import org.jugvale.transfgov.resource.config.jsonview.MunicipioConcisoView;

import com.fasterxml.jackson.annotation.JsonView;

@Path("estado")
@Produces("application/json; charset=utf8")
public interface EstadoResource {

	@GET
	public List<Estado> todosEstados();
	
	@GET
	@Path("/{sigla: [A-Z][A-Z]}")
	public Estado estadoPorSigla(@PathParam("sigla") String sigla);
	
	
	@GET
	@Path("/{sigla: [A-Z][A-Z]}/municipios")
	@JsonView(MunicipioConcisoView.class)
	public List<Municipio> municipiosPorEstado(@PathParam("sigla") String sigla);
	
}
