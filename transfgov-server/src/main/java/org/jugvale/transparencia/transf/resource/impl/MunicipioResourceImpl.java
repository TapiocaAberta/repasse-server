package org.jugvale.transparencia.transf.resource.impl;

import java.util.List;

import javax.inject.Inject;
import org.jugvale.transparencia.transf.model.base.Municipio;
import org.jugvale.transparencia.transf.resource.MunicipioResource;
import org.jugvale.transparencia.transf.service.impl.MunicipioService;

public class MunicipioResourceImpl implements MunicipioResource {

	@Inject
	MunicipioService municipioService;

	public List<Municipio> todosMunicipios() {
		return municipioService.todos();
	}
	
	public Municipio porNomeEEstado(String sigla, String nome) {
		return municipioService.buscaPorNomeEEstado(sigla, nome);		
	}

}
