package org.jugvale.transfgov.service.impl;

import java.util.function.Supplier;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.jugvale.transfgov.model.transferencia.Acao;
import org.jugvale.transfgov.service.Service;

@Stateless
public class AcaoService extends Service<Acao>{
	
	
	public Acao buscaPorCodigoOuCria(String codigo, Supplier<Acao> acaoSupplier){
		try{
			TypedQuery<Acao> buscaAcao = em.createNamedQuery("Acao.porCodigo", Acao.class);
			buscaAcao.setParameter("codigo", codigo);
			return buscaAcao.getSingleResult();
		}catch(NoResultException e) {
			Acao novo = acaoSupplier.get();
			this.salvar(novo);
			return novo;
		}		
	}

}
