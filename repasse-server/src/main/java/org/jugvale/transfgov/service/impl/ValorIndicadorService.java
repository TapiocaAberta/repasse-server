package org.jugvale.transfgov.service.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import org.jugvale.transfgov.model.base.Area;
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
		TypedQuery<ValorIndicador> buscaPorIndicadorMunicipio = em.createNamedQuery(
				"ValorIndicador.porIndicadorEMunicipio", ValorIndicador.class);
		buscaPorIndicadorMunicipio.setParameter("indicador", indicador);
		buscaPorIndicadorMunicipio.setParameter("municipio", m);
		return buscaPorIndicadorMunicipio.getResultList();
	}

	public List<ValorIndicador> buscaPorMunicipio(Municipio m) {
		TypedQuery<ValorIndicador> buscaPorMunicipio = em.createNamedQuery(
				"ValorIndicador.porMunicipio", ValorIndicador.class);
		buscaPorMunicipio.setParameter("municipio", m);		
		return buscaPorMunicipio.getResultList();
	}

	public List<ValorIndicador> buscaPorAnoMunicipio(long ano, Municipio m) {
		TypedQuery<ValorIndicador> buscaPorAnoMunicipio = em.createNamedQuery(
				"ValorIndicador.porAnoMunicipio", ValorIndicador.class);
		buscaPorAnoMunicipio.setParameter("municipio", m);	
		buscaPorAnoMunicipio.setParameter("ano", ano);	
		return buscaPorAnoMunicipio.getResultList();
	}

	public List<ValorIndicador> buscaPorMuninipioArea(Municipio m, Area a) {
		TypedQuery<ValorIndicador> buscaPorMunicipioArea = em.createNamedQuery(
				"ValorIndicador.porMunicipioArea", ValorIndicador.class);
		buscaPorMunicipioArea.setParameter("municipio", m);	
		buscaPorMunicipioArea.setParameter("area", a);	
		return buscaPorMunicipioArea.getResultList();
	}

	public List<ValorIndicador> buscaPorMuninipioAreaAno(Municipio m, Area a, long ano) {
		TypedQuery<ValorIndicador> buscaPorMunicipioArea = em.createNamedQuery(
				"ValorIndicador.porMunicipioAreaAno", ValorIndicador.class);
		buscaPorMunicipioArea.setParameter("municipio", m);	
		buscaPorMunicipioArea.setParameter("area", a);
		buscaPorMunicipioArea.setParameter("ano", ano);
		return buscaPorMunicipioArea.getResultList();
	}
	
}
