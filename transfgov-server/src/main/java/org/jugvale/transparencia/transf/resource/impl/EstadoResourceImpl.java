package org.jugvale.transparencia.transf.resource.impl;

import java.util.List;

import javax.inject.Inject;

import org.jugvale.transparencia.transf.model.base.Estado;
import org.jugvale.transparencia.transf.resource.EstadoResource;
import org.jugvale.transparencia.transf.service.impl.EstadoService;

public class EstadoResourceImpl implements EstadoResource {

	@Inject
	EstadoService estadoService;

	public List<Estado> todosEstados() {
		return estadoService.todos();
	}	
	
	public Estado estadoPorSigla(String sigla) {
		return estadoService.buscaEstadoPorSigla(sigla);		
	}
}
