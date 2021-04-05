package org.sjcdigital.repasse.service.impl;

import javax.enterprise.context.RequestScoped;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.sjcdigital.repasse.model.transferencia.Acao;
import org.sjcdigital.repasse.service.Service;

@RequestScoped
public class AcaoService extends Service<Acao>{
	
	
	public Acao buscaPorCodigoOuCria(Acao acao){
		try{
			TypedQuery<Acao> buscaAcao = em.createNamedQuery("Acao.porCodigo", Acao.class);
			buscaAcao.setParameter("codigo", acao.getCodigo());
			return buscaAcao.getSingleResult();
		}catch(NoResultException e) {
			this.salvar(acao);
			return acao;
		}		
	}

}
