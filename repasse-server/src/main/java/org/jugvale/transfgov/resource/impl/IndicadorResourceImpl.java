package org.jugvale.transfgov.resource.impl;

import java.util.List;

import javax.inject.Inject;

import org.jugvale.transfgov.model.base.Area;
import org.jugvale.transfgov.model.indicador.Indicador;
import org.jugvale.transfgov.resource.IndicadorResource;
import org.jugvale.transfgov.service.impl.AreaService;
import org.jugvale.transfgov.service.impl.IndicadorService;

public class IndicadorResourceImpl implements IndicadorResource {
	
	@Inject
	IndicadorService indicadorService;

	@Inject
	AreaService areaService;
	
	@Override
	public List<Indicador> todosIndicadores() {
		return indicadorService.todos();
	}

	@Override
	public List<Indicador> indicadoresPorArea(String area) {
		Area a = areaService.buscaPorNome(area);
		return indicadorService.buscaPorArea(a);
	}

}
