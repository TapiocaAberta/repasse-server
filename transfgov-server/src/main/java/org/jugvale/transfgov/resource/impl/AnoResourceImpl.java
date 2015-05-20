package org.jugvale.transfgov.resource.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.jugvale.transfgov.model.base.AnoMes;
import org.jugvale.transfgov.resource.AnoResource;
import org.jugvale.transfgov.service.impl.TransferenciaService;

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
	TransferenciaService transferenciaService;
	
	/**
	 * Retorna todos os anos e meses já carregados. (método custoso e feio por causa do design de repetir ano e mês em cada transferência)
	 * @return
	 */
	public List<AnoMes> anos() {
		// TODO: Melhorar (é possível, tenho ctza)
		List<AnoMes> lista = new ArrayList<>();
		for (Integer ano : transferenciaService.todosAnos()) {
			lista.add(new AnoMes(ano, transferenciaService.mesesDisponiveis(ano)));
		}
		return lista;	
	}

}
