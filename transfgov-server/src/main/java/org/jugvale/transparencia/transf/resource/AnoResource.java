package org.jugvale.transparencia.transf.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jugvale.transparencia.transf.service.impl.TransferenciaService;

/**
 * 
 * Irá retornar as datas disponíveis para acesso
 * 
 * @author wsiqueir
 *
 */
@Path("ano")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class AnoResource {
	
	@Inject
	TransferenciaService transferenciaService;
	
	/**
	 * Retorna todos os anos e meses já carregados. (método custoso e feio por causa do design de repetir ano e mês em cada transferência)
	 * @return
	 */
	@GET
	public Map<Integer, List<Integer>> anos() {
		// TODO: Melhorar (é possível, tenho ctza)
		Map<Integer, List<Integer>> anos = new HashMap<>();
		for (Integer ano : transferenciaService.todosAnos()) {
			anos.put(ano, transferenciaService.mesesDisponiveis(ano));
		}
		return anos;	
	}

}
