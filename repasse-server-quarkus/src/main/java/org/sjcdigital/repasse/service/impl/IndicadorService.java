package org.sjcdigital.repasse.service.impl;

import java.util.List;
import java.util.function.Supplier;

import javax.enterprise.context.RequestScoped;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.sjcdigital.repasse.model.base.Area;
import org.sjcdigital.repasse.model.indicador.Indicador;
import org.sjcdigital.repasse.service.Service;

import static javax.transaction.Transactional.TxType.REQUIRES_NEW;

@RequestScoped
@Transactional(REQUIRES_NEW)
public class IndicadorService extends Service<Indicador> {

	public Indicador buscaPorNomeOuCria(String nomeindicador,
				Supplier<Indicador> criaIndicador) {
			Indicador indicador;
			try {
				indicador = buscaPorNome(nomeindicador);
			} catch (NoResultException e) {
				indicador = criaIndicador.get();
				salvar(indicador);
			}
			return indicador;
		}

		public Indicador buscaPorNome(String indicador) {
			TypedQuery<Indicador> buscaIndicadorPorNome = em.createNamedQuery(
					"Indicador.porNome", Indicador.class);
			buscaIndicadorPorNome.setParameter("nome", indicador);
			return buscaIndicadorPorNome.getSingleResult();
			
		}
		
		public List<Indicador> buscaPorGrupo(String grupo) {
			TypedQuery<Indicador> buscaIndicadorPorNome = em.createNamedQuery(
					"Indicador.porGrupo", Indicador.class);
			buscaIndicadorPorNome.setParameter("nomeGrupo", grupo);
			return buscaIndicadorPorNome.getResultList();
		}

		public List<Indicador> buscaPorArea(Area a) {
			TypedQuery<Indicador> buscaIndicadorPorArea = em.createNamedQuery(
					"Indicador.porArea", Indicador.class);
			buscaIndicadorPorArea.setParameter("area", a);
			return buscaIndicadorPorArea.getResultList();
		}

}
