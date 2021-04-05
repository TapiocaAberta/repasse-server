package org.sjcdigital.repasse.service.impl;

import java.util.function.Supplier;

import javax.enterprise.context.RequestScoped;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.sjcdigital.repasse.model.indicador.GrupoIndicador;
import org.sjcdigital.repasse.service.Service;

import static javax.transaction.Transactional.TxType.REQUIRES_NEW;

@RequestScoped
@Transactional(REQUIRES_NEW)
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
