package org.sjcdigital.repasse.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.annotation.JsonView;
import org.sjcdigital.repasse.model.base.DadosMunicipio;
import org.sjcdigital.repasse.model.base.Municipio;
import org.sjcdigital.repasse.resource.config.jsonview.MunicipioConcisoView;
import org.sjcdigital.repasse.utils.JaxrsUtils;

@Path("municipio")
@Produces({ MediaType.APPLICATION_JSON })
public interface MunicipioResource {
	
	@GET
	public Response todosMunicipios(@QueryParam(JaxrsUtils.PARAMETRO_PAGINA) int pg);

	@GET
	@Path("{sigla}/{nome}")
	public Municipio porNomeEEstado(@PathParam("sigla") String sigla, @PathParam("nome") String nome);
	
	@GET
	@Path("{sigla}/{nome}/dados")
	@JsonView(MunicipioConcisoView.class)
	public List<DadosMunicipio> dadosMunicipio(@PathParam("sigla") String sigla, @PathParam("nome") String nome);

}
