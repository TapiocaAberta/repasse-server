package org.sjcdigital.repasse.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.fasterxml.jackson.annotation.JsonView;
import org.sjcdigital.repasse.model.base.Estado;
import org.sjcdigital.repasse.model.base.Municipio;
import org.sjcdigital.repasse.resource.config.jsonview.MunicipioConcisoView;

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
