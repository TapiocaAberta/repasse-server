package org.jugvale.transfgov.service.impl;

import java.util.function.Supplier;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.jugvale.transfgov.model.base.Estado;
import org.jugvale.transfgov.model.base.Municipio;
import org.jugvale.transfgov.service.Service;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class MunicipioService extends Service<Municipio>{
	
	public Municipio porEstadoNomeESIAFI(Estado estado, String nome, String siafi) {		
		TypedQuery<Municipio> buscaMunicipios = em.createNamedQuery("Municipio.porEstadoNomeSIAFI", Municipio.class);
		buscaMunicipios.setParameter("estado", estado);
		buscaMunicipios.setParameter("siafi", siafi);
		buscaMunicipios.setParameter("nome", nome);
		return buscaMunicipios.getSingleResult();
	}
	
	public Municipio porEstadoNomeESIAFIOuCria(Estado estado, String nome, String siafi, Supplier<Municipio> novoMunicipioSupplier) {
		try{
			return porEstadoNomeESIAFI(estado, nome, siafi);
		} catch (NoResultException e) {
			Municipio novo = novoMunicipioSupplier.get();
			this.salvar(novo);
			return novo;
		}
	}

	public Municipio buscaPorNomeEEstado(String sigla, String nome) {		
		TypedQuery<Municipio> buscaMunicipios = em.createNamedQuery("Municipio.porNomeESigla", Municipio.class);
		buscaMunicipios.setParameter("sigla", sigla);
		buscaMunicipios.setParameter("nome", nome);
		return buscaMunicipios.getSingleResult();	
	}

}
