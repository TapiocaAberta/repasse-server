package org.jugvale.transfgov.service.impl;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.TypedQuery;

import org.jugvale.transfgov.model.base.Area;
import org.jugvale.transfgov.service.Service;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AreaService extends Service<Area> {

	public Area buscaPorNome(String nome) {
		TypedQuery<Area> buscaAreaPorNome = em.createNamedQuery("Area.porNome",
				Area.class);
		buscaAreaPorNome.setParameter("nome", nome);
		return buscaAreaPorNome.getSingleResult();
	}

}
