package org.jugvale.transfgov.resource.impl;

import java.util.List;

import javax.inject.Inject;

import org.jugvale.transfgov.model.indicador.FocoIndicador;
import org.jugvale.transfgov.model.indicador.GrupoIndicador;
import org.jugvale.transfgov.model.indicador.Indicador;
import org.jugvale.transfgov.resource.GrupoIndicadorResource;
import org.jugvale.transfgov.service.impl.FocoIndicadorService;
import org.jugvale.transfgov.service.impl.GrupoIndicadorService;
import org.jugvale.transfgov.service.impl.IndicadorService;

public class GrupoIndicadorResourceImpl implements GrupoIndicadorResource {

	@Inject
	GrupoIndicadorService grupoIndicadorService;
	
	@Inject
	IndicadorService indicadorService;
	
	@Inject
	FocoIndicadorService focoIndicadorService;
	
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
