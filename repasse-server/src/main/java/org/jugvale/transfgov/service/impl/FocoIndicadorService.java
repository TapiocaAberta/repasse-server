package org.jugvale.transfgov.service.impl;

import java.util.List;
import java.util.function.Supplier;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.jugvale.transfgov.model.indicador.FocoIndicador;
import org.jugvale.transfgov.service.Service;

@Stateless
public class FocoIndicadorService extends Service<FocoIndicador> {

	public FocoIndicador buscaPorNomeOuCria(String focoIndicador,
			Supplier<FocoIndicador> criaFocoIndicador) {
		FocoIndicador foco;
		try {
			foco = buscaPorNome(focoIndicador);
		} catch (NoResultException e) {
			foco = criaFocoIndicador.get();
			salvar(foco);
		}
		return foco;
	}

	public FocoIndicador buscaPorNome(String focoIndicador) {
		TypedQuery<FocoIndicador> buscaFocoIndicadorPorNome = em
				.createNamedQuery("FocoIndicador.porNome", FocoIndicador.class);
		buscaFocoIndicadorPorNome.setParameter("foco", focoIndicador);
		return buscaFocoIndicadorPorNome.getSingleResult();
	}
	
	public List<FocoIndicador> buscaPorGrupoEIndicador(String nomeGrupo, String indicador) {
		TypedQuery<FocoIndicador> buscaFocoPorGrupoIndicador = em
				.createNamedQuery("FocoIndicador.porGrupoEIndicador", FocoIndicador.class);
		buscaFocoPorGrupoIndicador.setParameter("grupo", nomeGrupo);
		buscaFocoPorGrupoIndicador.setParameter("indicador", indicador);
		return buscaFocoPorGrupoIndicador.getResultList();
	}

}
