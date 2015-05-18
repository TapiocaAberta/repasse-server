package org.jugvale.transfgov.resource.impl;

import java.util.List;

import javax.inject.Inject;

import org.jugvale.transfgov.model.base.Estado;
import org.jugvale.transfgov.model.base.Municipio;
import org.jugvale.transfgov.resource.EstadoResource;
import org.jugvale.transfgov.service.impl.EstadoService;
import org.jugvale.transfgov.service.impl.MunicipioService;

public class EstadoResourceImpl implements EstadoResource {

	@Inject
	EstadoService estadoService;
	
	@Inject
	MunicipioService municipioService;

	public List<Estado> todosEstados() {
		return estadoService.todos();
	}	
	
	public Estado estadoPorSigla(String sigla) {
		return estadoService.buscaEstadoPorSigla(sigla);		
	}

	@Override
	public List<Municipio> municipiosPorEstado(String sigla) {
		return municipioService.porSiglaEstado(sigla);
	}

}
