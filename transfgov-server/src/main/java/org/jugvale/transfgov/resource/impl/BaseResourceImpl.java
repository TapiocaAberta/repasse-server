package org.jugvale.transfgov.resource.impl;

import java.util.List;

import javax.inject.Inject;

import org.jugvale.transfgov.model.base.Area;
import org.jugvale.transfgov.model.transferencia.Acao;
import org.jugvale.transfgov.model.transferencia.Favorecido;
import org.jugvale.transfgov.model.transferencia.Programa;
import org.jugvale.transfgov.model.transferencia.SubFuncao;
import org.jugvale.transfgov.resource.BaseResource;
import org.jugvale.transfgov.service.impl.AcaoService;
import org.jugvale.transfgov.service.impl.AreaService;
import org.jugvale.transfgov.service.impl.FavorecidoService;
import org.jugvale.transfgov.service.impl.ProgramaService;
import org.jugvale.transfgov.service.impl.SubFuncaoService;
import org.jugvale.transfgov.utils.JaxrsUtils;

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
