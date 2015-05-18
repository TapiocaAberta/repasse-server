package org.jugvale.transparencia.transf.resource.impl;

import java.util.List;

import javax.inject.Inject;
import org.jugvale.transparencia.transf.model.base.Municipio;
import org.jugvale.transparencia.transf.model.transferencia.Transferencia;
import org.jugvale.transparencia.transf.resource.TransferenciaResource;
import org.jugvale.transparencia.transf.service.impl.MunicipioService;
import org.jugvale.transparencia.transf.service.impl.TransferenciaService;
import org.jugvale.transparencia.transf.utils.JaxrsUtils;

public class TransferenciaResourceImpl implements TransferenciaResource{
	
	@Inject
	TransferenciaService transferenciaService;
	
	@Inject
	MunicipioService municipioService;

	/**
	 * 
	 * Retorna todas as transferências para o ano, mês e município passados
	 * 
	 * @param ano
	 * @param mes
	 * @param municipioId
	 * @return
	 */
	public List<Transferencia> porAnoMesMunicipio(int ano, int mes, long municipioId) {
		Municipio municipio = JaxrsUtils.lanca404SeNulo(municipioService.buscarPorId(municipioId), Municipio.class);		
		return transferenciaService.buscarPorAnoMesMunicipio(ano, mes, municipio);
	}

}