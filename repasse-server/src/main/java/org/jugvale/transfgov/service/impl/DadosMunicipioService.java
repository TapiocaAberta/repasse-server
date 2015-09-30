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
	
	public Long somaPopulacaoPorAno(int ano) {
		TypedQuery<Long> buscaDadosMunicipios = em.createNamedQuery("DadosMunicipio.somaPopulacaoPorAno", Long.class);
		buscaDadosMunicipios.setParameter("ano", ano);
		return buscaDadosMunicipios.getSingleResult();	
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
	

	public boolean temDadosParaAno(int ano) {
		TypedQuery<Boolean> buscaContagemPorAno = em.createNamedQuery("DadosMunicipio.temDadosParaAno", Boolean.class);
		buscaContagemPorAno.setParameter("ano", ano); 
		return buscaContagemPorAno.getSingleResult();
	}
	
	public Long somaPorAnoOuMaisRecente(int ano) {
		ano = anoOuMaisRecente(ano);
		return somaPopulacaoPorAno(ano);
	}
	
	public int anoOuMaisRecente(int ano) {
		if(!temDadosParaAno(ano)) {
			TypedQuery<Integer> buscaAnoMaisRecente = em.createNamedQuery("DadosMunicipio.anoMaisRecente", Integer.class);
			ano = buscaAnoMaisRecente.getSingleResult().intValue();
		}
		return ano;
	}

	public List<DadosMunicipio> buscaIDHParaMunicipios(List<Long> idMunicipios) {
		TypedQuery<DadosMunicipio> buscaContagemPorAno = em.createNamedQuery("DadosMunicipio.ultimoIDHParaMunicipios", DadosMunicipio.class);
		buscaContagemPorAno.setParameter("ids", idMunicipios); 
		return buscaContagemPorAno.getResultList();
	}
	
}