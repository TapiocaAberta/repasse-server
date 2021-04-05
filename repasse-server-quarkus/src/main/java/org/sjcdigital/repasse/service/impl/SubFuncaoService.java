package org.sjcdigital.repasse.service.impl;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.persistence.TypedQuery;

import org.sjcdigital.repasse.model.base.Area;
import org.sjcdigital.repasse.model.transferencia.SubFuncao;
import org.sjcdigital.repasse.service.Service;

@RequestScoped
public class SubFuncaoService extends Service<SubFuncao> {

	public List<SubFuncao> buscaPorArea(Area a) {
		TypedQuery<SubFuncao> buscaSubFuncao = em.createNamedQuery("SubFuncao.porArea", SubFuncao.class);
		buscaSubFuncao.setParameter("area", a);
		return buscaSubFuncao.getResultList();
	}
	
}