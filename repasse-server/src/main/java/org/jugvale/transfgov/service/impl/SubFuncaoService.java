package org.jugvale.transfgov.service.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import org.jugvale.transfgov.model.base.Area;
import org.jugvale.transfgov.model.transferencia.SubFuncao;
import org.jugvale.transfgov.service.Service;

@Stateless
public class SubFuncaoService extends Service<SubFuncao> {

	public List<SubFuncao> buscaPorArea(Area a) {
		TypedQuery<SubFuncao> buscaSubFuncao = em.createNamedQuery("SubFuncao.porArea", SubFuncao.class);
		buscaSubFuncao.setParameter("area", a);
		return buscaSubFuncao.getResultList();
	}
	
}