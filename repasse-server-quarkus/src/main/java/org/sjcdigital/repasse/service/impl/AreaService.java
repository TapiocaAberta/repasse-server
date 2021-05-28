package org.sjcdigital.repasse.service.impl;

import javax.enterprise.context.Dependent;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.sjcdigital.repasse.model.base.Area;
import org.sjcdigital.repasse.service.Service;

@Dependent
public class AreaService extends Service<Area> {

    @Transactional
	public Area buscaPorNome(String nome) {
		TypedQuery<Area> buscaAreaPorNome = em.createNamedQuery("Area.porNome",
				Area.class);
		buscaAreaPorNome.setParameter("nome", nome);
		return buscaAreaPorNome.getSingleResult();
	}

}
