package org.sjcdigital.repasse.resource;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.annotation.JsonView;
import org.sjcdigital.repasse.resource.config.jsonview.MunicipioConcisoView;
import org.sjcdigital.repasse.utils.JaxrsUtils;

@Path("transferencia")
public interface TransferenciaResource {

	static final String MSG_NAO_HA_TRANSFERENCIA = "Não há transferências carregadas para %d/%d";
	static final String MSG_TRANSFERENCIA_APAGADAS = "Transferências carregadas para %d/%d apagadas";

	/**
	 * 
	 * Retorna todas as transferências para o ano, mês e município passados
	 * 
	 * @param ano
	 * @param mes
	 * @param municipioId
	 * @return
	 */
	@GET
	@Path("{ano}/{mes}/municipio/{municipioId}")
	@JsonView(MunicipioConcisoView.class)
	@Produces("application/json; charset=utf8")
	public Response porAnoMesMunicipio(@PathParam("ano") int ano,
			@PathParam("mes") int mes,
			@PathParam("municipioId") long municipioId,
			@QueryParam(JaxrsUtils.PARAMETRO_PAGINA) int pg);

	@GET
	@Path("{ano}/{mes}/municipio/{municipioId}/conciso")
	@Produces("application/json; charset=utf8")
	public Response porAnoMesMunicipioConciso(@PathParam("ano") int ano,
			@PathParam("mes") int mes,
			@PathParam("municipioId") long municipioId);

	@DELETE
	@Path("{ano}/{mes}")
	@Produces("text/plan; charset=utf8")
	public Response apagaPorMesAno(@PathParam("ano") int ano,
			@PathParam("mes") int mes);

}