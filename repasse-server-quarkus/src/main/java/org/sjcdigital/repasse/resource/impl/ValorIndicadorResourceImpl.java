package org.sjcdigital.repasse.resource.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.core.PathSegment;

import org.sjcdigital.repasse.model.base.Area;
import org.sjcdigital.repasse.model.base.Municipio;
import org.sjcdigital.repasse.model.indicador.Indicador;
import org.sjcdigital.repasse.model.indicador.ResumoValorIndicador;
import org.sjcdigital.repasse.model.indicador.ValorIndicador;
import org.sjcdigital.repasse.resource.ValorIndicadorResource;
import org.sjcdigital.repasse.service.impl.AreaService;
import org.sjcdigital.repasse.service.impl.IndicadorService;
import org.sjcdigital.repasse.service.impl.MunicipioService;
import org.sjcdigital.repasse.service.impl.ValorIndicadorService;

public class ValorIndicadorResourceImpl implements ValorIndicadorResource {

    // TODO: eliminar boilerplate e código repetido
    // TODO: lançar 404 quando não existir entidades

    @Inject
    IndicadorService indicadorService;
    @Inject
    ValorIndicadorService valorIndicadorService;
    @Inject
    MunicipioService municipioService;
    @Inject
    AreaService areaService;

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

    @Override
    public ResumoValorIndicador resumoValoresIndicadoresMunicipiosPorAno(int ano, PathSegment pathSegment) {
        ResumoValorIndicador resumo = new ResumoValorIndicador();
        resumo.setAno(ano);
        resumo.setMediaIndicadores(valorIndicadorService.mediaPorAno(ano));
        resumo.setValoresMunicipiosSelecionados(valoresIndicadoresMunicipiosPorAno(ano, pathSegment));
        return resumo;
    }

}