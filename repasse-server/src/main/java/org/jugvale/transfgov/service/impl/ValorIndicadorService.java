package org.jugvale.transfgov.service.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import org.jugvale.transfgov.model.base.Municipio;
import org.jugvale.transfgov.model.indicador.Indicador;
import org.jugvale.transfgov.model.indicador.ValorIndicador;
import org.jugvale.transfgov.service.Service;

@Stateless
public class ValorIndicadorService extends Service<ValorIndicador> {

	
	public boolean verificaSeValorExiste(int ano, Indicador indicador,
			Municipio mun) {
		// há razões para contar antes...
		TypedQuery<Long> buscaFocoIndicadorPorNome = em
				.createNamedQuery("ValorIndicador.contaPorAnoMunicipioIndicador", Long.class);
		buscaFocoIndicadorPorNome.setParameter("ano", ano);
		buscaFocoIndicadorPorNome.setParameter("indicador", indicador);
		buscaFocoIndicadorPorNome.setParameter("municipio", mun);
		return buscaFocoIndicadorPorNome.getSingleResult() > 0;
	}
	
	public ValorIndicador buscaPorAnoMunicipioIndicador(int ano, Indicador indicador,
			Municipio mun) {
		TypedQuery<ValorIndicador> buscaFocoIndicadorPorNome = em
				.createNamedQuery("ValorIndicador.porAnoMunicipioIndicador", ValorIndicador.class);
		buscaFocoIndicadorPorNome.setParameter("ano", ano);
		buscaFocoIndicadorPorNome.setParameter("indicador", indicador);
		buscaFocoIndicadorPorNome.setParameter("municipio", mun);
		return buscaFocoIndicadorPorNome.getSingleResult();
	}
	
	
	public List<ValorIndicador> buscaPorIndicadorEAno(Indicador indicador, int ano) {
		TypedQuery<ValorIndicador> buscaIndicadorPorNome = em.createNamedQuery(
				"ValorIndicador.porIndicadorEAno", ValorIndicador.class);
		buscaIndicadorPorNome.setParameter("indicador", indicador);
		buscaIndicadorPorNome.setParameter("ano", ano);
		return buscaIndicadorPorNome.getResultList();
	}

	public List<ValorIndicador> buscaPorIndicadorEMunicipio(
			Indicador indicador, Municipio m) {
		TypedQuery<ValorIndicador> buscaIndicadorPorNome = em.createNamedQuery(
				"ValorIndicador.porIndicadorEMunicipio", ValorIndicador.class);
		buscaIndicadorPorNome.setParameter("indicador", indicador);
		buscaIndicadorPorNome.setParameter("municipio", m);
		return buscaIndicadorPorNome.getResultList();
	}

}
