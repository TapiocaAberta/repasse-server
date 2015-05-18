package org.jugvale.transparencia.transf.service.impl;

import java.util.function.Supplier;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.jugvale.transparencia.transf.model.base.Estado;
import org.jugvale.transparencia.transf.service.Service;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class EstadoService extends Service<Estado>{
	
	public Estado buscaEstadoPorSigla(String sigla) {
		TypedQuery<Estado> buscaPorSigla = em.createNamedQuery("Estado.porSigla", Estado.class);
		buscaPorSigla.setParameter("sigla", sigla);
		return buscaPorSigla.getSingleResult();		
	}
	
	public Estado buscaEstadoPorSiglaOuCria(String sigla, Supplier<Estado> novoEstadoSupplier) {
		try {
			return buscaEstadoPorSigla(sigla);
		} catch (NoResultException e) {
			Estado novo = novoEstadoSupplier.get();
			this.salvar(novo);
			return novo;
		}		
	}

}
