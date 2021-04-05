package org.sjcdigital.repasse.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.sjcdigital.repasse.model.base.AnoMes;
import org.sjcdigital.repasse.service.impl.TransferenciaService;

@RequestScoped
public class AnoService {

	@Inject
	TransferenciaService transferenciaService;

	/**
	 * Retorna todos os anos e meses já carregados. (método custoso e feio por
	 * causa do design de repetir ano e mês em cada transferência)
	 * 
	 * @return
	 */
	public List<AnoMes> anos() {
		// TODO: Melhorar (é possível, tenho ctza)
		List<AnoMes> lista = new ArrayList<>();
		for (Integer ano : transferenciaService.todosAnos()) {
			lista.add(new AnoMes(ano, transferenciaService
					.mesesDisponiveis(ano)));
		}
		return lista;
	}

}
