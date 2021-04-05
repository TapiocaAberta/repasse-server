package org.sjcdigital.repasse.resource.impl;

import java.util.List;

import javax.inject.Inject;

import org.sjcdigital.repasse.model.indicador.FocoIndicador;
import org.sjcdigital.repasse.model.indicador.GrupoIndicador;
import org.sjcdigital.repasse.model.indicador.Indicador;
import org.sjcdigital.repasse.resource.GrupoIndicadorResource;
import org.sjcdigital.repasse.service.impl.AreaService;
import org.sjcdigital.repasse.service.impl.FocoIndicadorService;
import org.sjcdigital.repasse.service.impl.GrupoIndicadorService;
import org.sjcdigital.repasse.service.impl.IndicadorService;
import org.sjcdigital.repasse.service.impl.MunicipioService;
import org.sjcdigital.repasse.service.impl.ValorIndicadorService;

public class GrupoIndicadorResourceImpl implements GrupoIndicadorResource {

	@Inject
	GrupoIndicadorService grupoIndicadorService;

	@Inject
	IndicadorService indicadorService;

	@Inject
	FocoIndicadorService focoIndicadorService;

	@Inject
	ValorIndicadorService valorIndicadorService;

	@Inject
	MunicipioService municipioService;

	@Inject
	AreaService areaService;

	@Override
	public List<GrupoIndicador> grupos() {
		return grupoIndicadorService.todos();
	}

	@Override
	public GrupoIndicador grupo(String nomeGrupo) {
		return grupoIndicadorService.buscaPorNome(nomeGrupo);
	}

	@Override
	public List<Indicador> indicadoresParaGrupo(String nomeGrupo) {
		return indicadorService.buscaPorGrupo(nomeGrupo);
	}

	@Override
	public List<FocoIndicador> focosParaGrupo(String nomeGrupo, String nomeIndicador) {
		return focoIndicadorService.buscaPorGrupoEIndicador(nomeGrupo, nomeIndicador);
	}

}
