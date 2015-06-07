package org.jugvale.transfgov.resource;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.PathSegment;

import org.jugvale.transfgov.model.agregacao.Agregacao;
import org.jugvale.transfgov.model.agregacao.TipoAgregacao;
import org.jugvale.transfgov.resource.config.jsonview.MunicipioConcisoView;

import com.fasterxml.jackson.annotation.JsonView;

@Path("agregacao")
@Produces("application/json; charset=utf8")
public interface AgregacaoResource {
	
	final String MSG_AGREGACAO_ERRADA = "A agregação %s não é permitida em agregações %s";
	final String MSG_NAO_HA_DADOS_ANO_MES =  "Não há dados de transferência para ano %d e mês %d";
	final String MSG_NAO_HA_DADOS_ANO  =  "Não há dados de transferência para ano %d";

	@GET
	public TipoAgregacao[] todasAgregacoes();
	
	@GET
	@Path("/{tipoAgregacao}/{ano}/{mes}/municipio/{idMunicipio}")
	@JsonView(MunicipioConcisoView.class)	
	public Agregacao agregaPorAnoMesMunicipio(@PathParam("tipoAgregacao") TipoAgregacao tipoAgregacao, @PathParam("ano") int ano, @PathParam("mes") int mes, @PathParam("idMunicipio") long idMunicipio);
	
	@GET
	@Path("/{tipoAgregacao}/{ano}/municipio/{idMunicipio}")	
	@JsonView(MunicipioConcisoView.class)
	public Agregacao agregaPorAnoMunicipio(@PathParam("tipoAgregacao") TipoAgregacao tipoAgregacao, @PathParam("ano") int ano, @PathParam("idMunicipio") long idMunicipio);
	
	@GET
	@Path("/AREA/area/{areaId}/{ano}/{mes}/municipio/{idMunicipio}")
	@JsonView(MunicipioConcisoView.class)	
	public Agregacao agregaPorAnoMesAreaMunicipio(@PathParam("areaId") long idArea, @PathParam("ano") int ano, @PathParam("mes") int mes, @PathParam("idMunicipio") long idMunicipio);
	
	@GET
	@Path("/MUNICIPIO/{ano}/{mes}/estado/{siglaEstado}")	
	public Agregacao agregaPorAnoEstado(@PathParam("ano") int ano,  @PathParam("mes") int mes, @PathParam("siglaEstado") String siglaEstado);
	
	@GET
	@Path("/ANO/{ano}/municipio/{municipioId}/")
	@JsonView(MunicipioConcisoView.class)
	public Map<Integer, Double> agrupaPorAno(@PathParam("ano") int ano, @PathParam("municipioId") long municipioId);
	
	@GET
	@Path("/ANO/{ano}/{tipoAgregacao}/municipio/{municipioId}/")
	@JsonView(MunicipioConcisoView.class)
	public List<Agregacao> agrupaPorAnoArea(@PathParam("tipoAgregacao") TipoAgregacao tipoAgregacao, @PathParam("ano") int ano, @PathParam("municipioId") long municipioId);
	
	@GET
	@Path("/{tipoAgregacao}/{ano}/municipios/{municipioIds}")	
	@JsonView(MunicipioConcisoView.class)
	public List<Agregacao> agrupaPorAnoArea(@PathParam("tipoAgregacao") TipoAgregacao tipoAgregacao, @PathParam("ano") int ano, @PathParam("municipioIds") PathSegment pathSegment);

}