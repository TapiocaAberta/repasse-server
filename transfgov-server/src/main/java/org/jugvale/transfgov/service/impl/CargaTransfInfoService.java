package org.jugvale.transfgov.service.impl;

import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.jugvale.transfgov.model.transferencia.CargaTransfInfo;
import org.jugvale.transfgov.service.Service;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class CargaTransfInfoService extends Service<CargaTransfInfo> {
	
	@Inject
	Logger logger;
	
	public List<CargaTransfInfo> todosPorAnoMes(int ano, int mes) {		
		return porAnoMesQuery(ano, mes).getResultList();
	}	

	public CargaTransfInfo porAnoMes(int ano, int mes) {
		return porAnoMesQuery(ano, mes).getSingleResult();
	}
	
	public TypedQuery<CargaTransfInfo> porAnoMesQuery(int ano, int mes) {
		TypedQuery<CargaTransfInfo> buscaCargaTransfInfo = em.createNamedQuery(
				"CargaTransfInfo.porAnoMes", CargaTransfInfo.class);
		buscaCargaTransfInfo.setParameter("ano", ano);
		buscaCargaTransfInfo.setParameter("mes", mes);
		return buscaCargaTransfInfo;
	}

	public CargaTransfInfo porAnoMesOuCria(int ano, int mes,
			Supplier<CargaTransfInfo> cria) {
		try {
			return porAnoMes(ano, mes);
		} catch (NoResultException e) {
			CargaTransfInfo novo = cria.get();
			this.salvar(novo);
			return novo;
		}
	}

	public void apagaPorAnoMes(int ano, int mes) {
		CargaTransfInfo c = porAnoMes(ano, mes);
		remover(c);
	}

}
