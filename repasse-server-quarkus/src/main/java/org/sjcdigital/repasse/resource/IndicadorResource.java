package org.sjcdigital.repasse.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.sjcdigital.repasse.model.indicador.Indicador;

@Path("indicador")
@Produces("application/json; charset=utf8")
public interface IndicadorResource {
	
	@GET
	public List<Indicador> todosIndicadores();
	
	@GET
	@Path("{area}")
	public List<Indicador> indicadoresPorArea(@PathParam("area") String area);

}
