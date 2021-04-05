package org.sjcdigital.repasse.resource.impl;

import java.util.List;

import javax.inject.Inject;

import org.sjcdigital.repasse.model.base.AnoMes;
import org.sjcdigital.repasse.resource.AnoResource;
import org.sjcdigital.repasse.service.AnoService;

/**
 * 
 * Irá retornar as datas disponíveis para acesso
 * 
 * @author wsiqueir
 *
 */
public class AnoResourceImpl  implements AnoResource {
	
	@Inject
	AnoService anoService;

	public List<AnoMes> anos() {
		return anoService.anos();	
	}

}
