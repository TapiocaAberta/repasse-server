package org.jugvale.transfgov.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jugvale.transfgov.model.indicador.Indicador;

@Path("valor_indicador")
@Produces("application/json; charset=utf8")
public interface IndicadorResource {
	
	@GET
	public List<Indicador> todosIndicadores();
	
	@Path("{area}")
	@GET
	public List<Indicador> indicadoresPorArea(@PathParam("area") String area);

}
