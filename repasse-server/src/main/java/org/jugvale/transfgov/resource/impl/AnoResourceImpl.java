package org.jugvale.transfgov.resource.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.jugvale.transfgov.model.base.AnoMes;
import org.jugvale.transfgov.resource.AnoResource;
import org.jugvale.transfgov.service.AnoService;

/**
 * 
 * Irá retornar as datas disponíveis para acesso
 * 
 * @author wsiqueir
 *
 */
@Stateless
public class AnoResourceImpl  implements AnoResource {
	
	@Inject
	AnoService anoService;

	public List<AnoMes> anos() {
		return anoService.anos();	
	}

}
