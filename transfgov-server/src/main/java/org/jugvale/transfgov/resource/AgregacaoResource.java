package org.jugvale.transfgov.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jugvale.transfgov.model.agregacao.Agregacao;
import org.jugvale.transfgov.model.agregacao.TipoAgregacao;

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
	public Agregacao agregaPorAnoMesMunicipio(@PathParam("tipoAgregacao") TipoAgregacao tipoAgregacao, @PathParam("ano") int ano, @PathParam("mes") int mes, @PathParam("idMunicipio") long idMunicipio);
	
	@GET
	@Path("/{tipoAgregacao}/{ano}/municipio/{idMunicipio}")	
	public Agregacao agregaPorAnoMunicipio(@PathParam("tipoAgregacao") TipoAgregacao tipoAgregacao, @PathParam("ano") int ano, @PathParam("idMunicipio") long idMunicipio);
	
	@GET
	@Path("/AREA/area/{areaId}/{ano}/{mes}/municipio/{idMunicipio}")	
	public Agregacao agregaPorAnoMesAreaMunicipio(@PathParam("areaId") long idArea, @PathParam("ano") int ano, @PathParam("mes") int mes, @PathParam("idMunicipio") long idMunicipio);
	
	@GET
	@Path("/MUNICIPIO/{ano}/{mes}/estado/{siglaEstado}")	
	public Agregacao agregaPorAnoEstado(@PathParam("ano") int ano,  @PathParam("mes") int mes, @PathParam("siglaEstado") String siglaEstado);

}