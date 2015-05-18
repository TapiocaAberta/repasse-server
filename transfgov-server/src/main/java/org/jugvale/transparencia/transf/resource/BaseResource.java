package org.jugvale.transparencia.transf.resource;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jugvale.transparencia.transf.model.base.Area;
import org.jugvale.transparencia.transf.model.transferencia.Acao;
import org.jugvale.transparencia.transf.model.transferencia.Favorecido;
import org.jugvale.transparencia.transf.model.transferencia.Programa;
import org.jugvale.transparencia.transf.model.transferencia.SubFuncao;
import org.jugvale.transparencia.transf.service.impl.AcaoService;
import org.jugvale.transparencia.transf.service.impl.AreaService;
import org.jugvale.transparencia.transf.service.impl.FavorecidoService;
import org.jugvale.transparencia.transf.service.impl.ProgramaService;
import org.jugvale.transparencia.transf.service.impl.SubFuncaoService;
import org.jugvale.transparencia.transf.utils.JaxrsUtils;

@Path("")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class BaseResource {

	@Inject
	AreaService areaService;

	@Inject
	SubFuncaoService subFuncaoService;

	@Inject
	ProgramaService programaService;

	@Inject
	AcaoService acaoService;

	@Inject
	FavorecidoService favorecidoService;

	@GET
	@Path("area/{id}")
	public Area areaPorId(@PathParam("id") long id) {
		return JaxrsUtils.lanca404SeNulo(areaService.buscarPorId(id),
				Area.class);
	}

	@GET
	@Path("area")
	public List<Area> todasAreas() {
		return areaService.todos();
	}

	@GET
	@Path("sub-funcao/{id}")
	public SubFuncao subFuncaoPorId(@PathParam("id") long id) {
		return JaxrsUtils.lanca404SeNulo(subFuncaoService.buscarPorId(id),
				SubFuncao.class);
	}

	@GET
	@Path("sub-funcao")
	public List<SubFuncao> todasSubFuncoes() {
		return subFuncaoService.todos();
	}

	@GET
	@Path("programa/{id}")
	public Programa programaPorId(@PathParam("id") long id) {
		return JaxrsUtils.lanca404SeNulo(programaService.buscarPorId(id),
				Programa.class);
	}

	@GET
	@Path("programa")
	public List<Programa> todosPogramas() {
		return programaService.todos();
	}

	@GET
	@Path("acao/{id}")
	public Acao acaoPorId(@PathParam("id") long id) {
		return JaxrsUtils.lanca404SeNulo(acaoService.buscarPorId(id),
				Acao.class);
	}

	@GET
	@Path("acao")
	public List<Acao> todasAcoes() {
		return acaoService.todos();
	}

	@GET
	@Path("favorecido/{id}")
	public Favorecido favorecidoPorId(@PathParam("id") long id) {
		return JaxrsUtils.lanca404SeNulo(favorecidoService.buscarPorId(id),
				Favorecido.class);
	}

	@GET
	@Path("favorecido")
	public List<Favorecido> todosFavorecidos() {
		return favorecidoService.todos();
	}

}
