package org.jugvale.transfgov.service.impl;

import java.util.function.Supplier;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.jugvale.transfgov.model.indicador.GrupoIndicador;
import org.jugvale.transfgov.service.Service;

@Stateless
public class GrupoIndicadorService extends Service<GrupoIndicador> {

	public GrupoIndicador buscaPorNomeOuCria(String grupoIndicador,
			Supplier<GrupoIndicador> criaGrupoIndicador) {
		GrupoIndicador gi;
		try {
			gi = buscaPorNome(grupoIndicador);
		} catch (NoResultException e) {
			gi = criaGrupoIndicador.get();
			salvar(gi);
		}
		return gi;
	}

	public GrupoIndicador buscaPorNome(String grupoIndicador) {
		TypedQuery<GrupoIndicador> buscaGrupoIndicadorPorNome = em.createNamedQuery(
				"GrupoIndicador.porNome", GrupoIndicador.class);
		buscaGrupoIndicadorPorNome.setParameter("nome", grupoIndicador);
		return buscaGrupoIndicadorPorNome.getSingleResult();
	}
}
