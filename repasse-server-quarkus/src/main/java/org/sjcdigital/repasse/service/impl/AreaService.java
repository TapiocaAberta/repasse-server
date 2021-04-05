package org.sjcdigital.repasse.service.impl;

import javax.enterprise.context.RequestScoped;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.sjcdigital.repasse.model.base.Area;
import org.sjcdigital.repasse.service.Service;

import static javax.transaction.Transactional.TxType.REQUIRES_NEW;

@RequestScoped
@Transactional(REQUIRES_NEW)
public class AreaService extends Service<Area> {

	public Area buscaPorNome(String nome) {
		TypedQuery<Area> buscaAreaPorNome = em.createNamedQuery("Area.porNome",
				Area.class);
		buscaAreaPorNome.setParameter("nome", nome);
		return buscaAreaPorNome.getSingleResult();
	}

}
