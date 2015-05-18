package org.jugvale.transfgov.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jugvale.transfgov.model.base.Area;
import org.jugvale.transfgov.model.transferencia.Acao;
import org.jugvale.transfgov.model.transferencia.Favorecido;
import org.jugvale.transfgov.model.transferencia.Programa;
import org.jugvale.transfgov.model.transferencia.SubFuncao;

@Path("")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public interface BaseResource {


	@GET
	@Path("area/{id}")
	public Area areaPorId(@PathParam("id") long id);

	@GET
	@Path("area")
	public List<Area> todasAreas();

	@GET
	@Path("sub-funcao/{id}")
	public SubFuncao subFuncaoPorId(@PathParam("id") long id);

	@GET
	@Path("sub-funcao")
	public List<SubFuncao> todasSubFuncoes();

	@GET
	@Path("programa/{id}")
	public Programa programaPorId(@PathParam("id") long id);

	@GET
	@Path("programa")
	public List<Programa> todosPogramas();
	@GET
	@Path("acao/{id}")
	public Acao acaoPorId(@PathParam("id") long id);

	@GET
	@Path("acao")
	public List<Acao> todasAcoes();
	@GET
	@Path("favorecido/{id}")
	public Favorecido favorecidoPorId(@PathParam("id") long id);

	@GET
	@Path("favorecido")
	public List<Favorecido> todosFavorecidos();

}
