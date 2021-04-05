package org.sjcdigital.repasse.resource.impl;

import java.util.List;

import javax.inject.Inject;

import org.sjcdigital.repasse.model.base.Estado;
import org.sjcdigital.repasse.model.base.Municipio;
import org.sjcdigital.repasse.resource.EstadoResource;
import org.sjcdigital.repasse.service.impl.EstadoService;
import org.sjcdigital.repasse.service.impl.MunicipioService;

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
