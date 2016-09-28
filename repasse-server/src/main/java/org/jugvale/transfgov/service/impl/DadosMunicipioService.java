package org.jugvale.transfgov.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.commons.io.IOUtils;
import org.jugvale.transfgov.model.base.DadosMunicipio;
import org.jugvale.transfgov.model.base.Municipio;
import org.jugvale.transfgov.service.Service;
import org.jugvale.transfgov.utils.TextoUtils;

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

	public List<DadosMunicipio> buscaIDHParaMunicipiosAnoMaisRecente(List<Long> idMunicipios, int ano) {
		ano = em.createNamedQuery("DadosMunicipio.anoMaisRecenteIDH", Integer.class).setParameter("ano", ano).getSingleResult();
		TypedQuery<DadosMunicipio> buscaContagemPorAno = em.createNamedQuery("DadosMunicipio.buscaIDHParaMunicipios", DadosMunicipio.class);
		buscaContagemPorAno.setParameter("ids", idMunicipios); 
		buscaContagemPorAno.setParameter("ano", ano); 
		return buscaContagemPorAno.getResultList();
	}
	
	
	/**
	 * Busca o valor do MIQL.
	 * @param nomesMunicipios
	 * @param ano
	 * @return
	 */
	public Map<String, Float> buscaMiqlParaMunicipios(List<String> nomesMunicipios, int ano) {
		// TODO: Ao termos acesso a todos os dados, melhorar isso para incluir o MIQL em DadosMunicipios
		HashMap<String, Float> dados = new HashMap<>();
		// TEMP 
		final String ARQUIVOS_MIQL[] = { "/dados/miql_2010.csv" };
		for (int i = 0; i < ARQUIVOS_MIQL.length; i++) {	
			try {
				InputStream is = getClass().getResourceAsStream(ARQUIVOS_MIQL[i]);
				Path arquivo = Files.createTempFile("miql", "");
				Files.write(arquivo, IOUtils.toByteArray(is));
				Files.lines(arquivo).skip(1).forEach(l -> {
					String[] col = l.split(";");
					String nomeMun = TextoUtils.transformaNomeCidade(col[0]).replaceAll("\"", "");
					float valorMiqlt = Float.parseFloat(col[9]);
					nomesMunicipios.stream().filter(n -> n.equals(nomeMun)).findFirst().ifPresent(s -> {
						dados.put(s, valorMiqlt);
					});
				});
				Files.delete(arquivo);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return dados;
	}
}