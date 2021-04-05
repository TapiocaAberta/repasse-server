package org.sjcdigital.repasse.resource.impl;

import java.util.List;

import javax.inject.Inject;

import org.sjcdigital.repasse.model.base.Area;
import org.sjcdigital.repasse.model.indicador.Indicador;
import org.sjcdigital.repasse.resource.IndicadorResource;
import org.sjcdigital.repasse.service.impl.AreaService;
import org.sjcdigital.repasse.service.impl.IndicadorService;

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
