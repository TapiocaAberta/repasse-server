package org.jugvale.transparencia.transf.resource.impl;

import java.util.List;

import javax.inject.Inject;

import org.jugvale.transparencia.transf.model.base.Area;
import org.jugvale.transparencia.transf.model.transferencia.Acao;
import org.jugvale.transparencia.transf.model.transferencia.Favorecido;
import org.jugvale.transparencia.transf.model.transferencia.Programa;
import org.jugvale.transparencia.transf.model.transferencia.SubFuncao;
import org.jugvale.transparencia.transf.resource.BaseResource;
import org.jugvale.transparencia.transf.service.impl.AcaoService;
import org.jugvale.transparencia.transf.service.impl.AreaService;
import org.jugvale.transparencia.transf.service.impl.FavorecidoService;
import org.jugvale.transparencia.transf.service.impl.ProgramaService;
import org.jugvale.transparencia.transf.service.impl.SubFuncaoService;
import org.jugvale.transparencia.transf.utils.JaxrsUtils;

public class BaseResourceImpl implements BaseResource{

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

	public Area areaPorId( long id) {
		return JaxrsUtils.lanca404SeNulo(areaService.buscarPorId(id),
				Area.class);
	}

	public List<Area> todasAreas() {
		return areaService.todos();
	}

	
	public SubFuncao subFuncaoPorId( long id) {
		return JaxrsUtils.lanca404SeNulo(subFuncaoService.buscarPorId(id),
				SubFuncao.class);
	}

	public List<SubFuncao> todasSubFuncoes() {
		return subFuncaoService.todos();
	}

	public Programa programaPorId( long id) {
		return JaxrsUtils.lanca404SeNulo(programaService.buscarPorId(id),
				Programa.class);
	}

	public List<Programa> todosPogramas() {
		return programaService.todos();
	}

	public Acao acaoPorId( long id) {
		return JaxrsUtils.lanca404SeNulo(acaoService.buscarPorId(id),
				Acao.class);
	}

	public List<Acao> todasAcoes() {
		return acaoService.todos();
	}

	public Favorecido favorecidoPorId(long id) {
		return JaxrsUtils.lanca404SeNulo(favorecidoService.buscarPorId(id),
				Favorecido.class);
	}

	public List<Favorecido> todosFavorecidos() {
		return favorecidoService.todos();
	}

}
