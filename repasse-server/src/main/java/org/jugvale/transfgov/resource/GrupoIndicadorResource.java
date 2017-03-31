package org.jugvale.transfgov.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jugvale.transfgov.model.indicador.FocoIndicador;
import org.jugvale.transfgov.model.indicador.GrupoIndicador;
import org.jugvale.transfgov.model.indicador.Indicador;

/**
 * 
 * Endpoint que contém as informações relacionadas aos metadados dos indicadores
 * @author wsiqueir
 *
 */
@Path("grupo_indicador")
@Produces("application/json; charset=utf8")
public interface GrupoIndicadorResource {

	@GET
	public List<GrupoIndicador> grupos();

	@GET
	@Path("{nomeGrupo}")
	public GrupoIndicador grupo(@PathParam("nomeGrupo") String nomeGrupo);

	@GET
	@Path("{nomeGrupo}/indicadores")
	public List<Indicador> indicadoresParaGrupo(@PathParam("nomeGrupo") String nomeGrupo);

	@GET
	@Path("{nomeGrupo}/indicadores/{nomeIndicador}/foco")
	public List<FocoIndicador> focosParaGrupo(@PathParam("nomeGrupo") String nomeGrupo,
			@PathParam("nomeIndicador") String nomeIndicador);
	

}