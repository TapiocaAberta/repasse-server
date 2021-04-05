package org.sjcdigital.repasse.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.sjcdigital.repasse.model.base.Area;
import org.sjcdigital.repasse.model.transferencia.Acao;
import org.sjcdigital.repasse.model.transferencia.Favorecido;
import org.sjcdigital.repasse.model.transferencia.Programa;
import org.sjcdigital.repasse.model.transferencia.SubFuncao;

@Path("")
@Produces("application/json; charset=utf8")
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
