package org.jugvale.transfgov.resource.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.core.PathSegment;

import org.jugvale.transfgov.model.base.Area;
import org.jugvale.transfgov.model.base.Municipio;
import org.jugvale.transfgov.model.indicador.Indicador;
import org.jugvale.transfgov.model.indicador.ValorIndicador;
import org.jugvale.transfgov.resource.ValorIndicadorResource;
import org.jugvale.transfgov.service.impl.AreaService;
import org.jugvale.transfgov.service.impl.IndicadorService;
import org.jugvale.transfgov.service.impl.MunicipioService;
import org.jugvale.transfgov.service.impl.ValorIndicadorService;

public class ValorIndicadorResourceImpl implements ValorIndicadorResource {

	// TODO: eliminar boilerplate e código repetido
	// TODO: lançar 404 quando não existir entidades
	
	@Inject
	private IndicadorService indicadorService;
	@Inject
	private ValorIndicadorService valorIndicadorService;
	@Inject
	private MunicipioService municipioService;
	@Inject
	private AreaService areaService;

	@Override
	public ValorIndicador valoresIndicadoresMunicipioPorAno(long idIndicador, long idMunicipio, int ano) {
		Municipio mun = municipioService.buscarPorId(idMunicipio);
		Indicador ind = indicadorService.buscarPorId(idIndicador);
		return valorIndicadorService.buscaPorAnoMunicipioIndicador(ano, ind, mun);
	}

	@Override
	public List<ValorIndicador> todosValoresIndicadores(long idMunicipio) {
		Municipio m = municipioService.buscarPorId(idMunicipio);
		return valorIndicadorService.buscaPorMunicipio(m);
	}

	@Override
	public List<ValorIndicador> todosValoresIndicadoresPorAno(long idMunicipio, int ano) {
		Municipio m = municipioService.buscarPorId(idMunicipio);
		return valorIndicadorService.buscaPorAnoMunicipio(ano, m);
	}

	@Override
	public List<ValorIndicador> todosValoresIndicadoresPorMunicipioArea(long idMunicipio, String area) {
		Municipio m = municipioService.buscarPorId(idMunicipio);
		Area a = areaService.buscaPorNome(area);
		return valorIndicadorService.buscaPorMuninipioArea(m, a);
	}

	@Override
	public List<ValorIndicador> todosValoresIndicadoresPorMunicipioAreaAno(long idMunicipio, String area, int ano) {
		Municipio m = municipioService.buscarPorId(idMunicipio);
		Area a = areaService.buscaPorNome(area);
		return valorIndicadorService.buscaPorMuninipioAreaAno(m, a, ano);
	}

	@Override
	public List<ValorIndicador> valoresIndicadoresMunicipiosPorAno(int ano, PathSegment pathSegment) {
		Set<Long> ids = pathSegment.getMatrixParameters().keySet().stream().map(Long::parseLong).collect(Collectors.toSet());
		return valorIndicadorService.buscaPorAnoMunicipios(ano, ids);
	}
}