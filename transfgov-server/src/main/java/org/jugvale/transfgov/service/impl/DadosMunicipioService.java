package org.jugvale.transfgov.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.jugvale.transfgov.model.base.DadosMunicipio;
import org.jugvale.transfgov.model.base.Municipio;
import org.jugvale.transfgov.service.Service;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class DadosMunicipioService extends Service<DadosMunicipio> {

	public DadosMunicipio buscaPorAnoMunicipio(int ano, Municipio m) {
		TypedQuery<DadosMunicipio> buscaDadosMunicipios = em.createNamedQuery("DadosMunicipio.porAnoEMunicipio", DadosMunicipio.class);
		buscaDadosMunicipios.setParameter("ano", ano);
		buscaDadosMunicipios.setParameter("municipio", m);
		return buscaDadosMunicipios.getSingleResult();	
	}
	
	public List<DadosMunicipio> buscaMunicipio(Municipio m) {
		TypedQuery<DadosMunicipio> buscaDadosMunicipios = em.createNamedQuery("DadosMunicipio.porMunicipio", DadosMunicipio.class);
		buscaDadosMunicipios.setParameter("municipio", m);
		return buscaDadosMunicipios.getResultList();	
	}
	
	public DadosMunicipio buscaPorAnoMunicipioOuCria(int ano, Municipio m) {
		try{
			return buscaPorAnoMunicipio(ano, m);
		}catch(NoResultException e){
			DadosMunicipio dadosMunicipio = new DadosMunicipio();
			dadosMunicipio.setAno(ano);
			dadosMunicipio.setMunicipio(m);
			salvar(dadosMunicipio);
			return dadosMunicipio;
		} 
	}

	/**
	 * Tenta buscar a população para o ano ou então traz a medida mais recente de população
	 * @param ano
	 * @param municipio
	 * @return
	 */
	public DadosMunicipio buscaPorAnoMunicipioOuMaisRecente(int ano,
			Municipio municipio) {
		try {
			return buscaPorAnoMunicipio(ano, municipio);
		} catch (NoResultException e) {
			List<DadosMunicipio> dados = buscaMunicipio(municipio);
			Collections.sort(dados, Collections.reverseOrder(Comparator.comparingInt(DadosMunicipio::getAno)));
			return dados.get(0);			
		}		
	}
	
}
