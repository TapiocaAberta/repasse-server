package org.jugvale.transfgov.ranking;

import java.util.WeakHashMap;
import java.util.function.Supplier;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

import org.jugvale.transfgov.model.ranking.RankingTransferencias;

/**
 * 
 * Uma implementação inicial básica para o cache
 * 
 * @author wsiqueir
 *
 */
@Singleton
public class RankingCache {
	
	/**
	 * Devido ao HHH-9111, o cache vai ser manual
	 */
	WeakHashMap<Integer, RankingTransferencias> cacheRanking;
	
	@PostConstruct
	private void inicializa() {		
		cacheRanking = new WeakHashMap<>();
	}	
	
	public RankingTransferencias retornaOuAdiciona(Integer chave, Supplier<RankingTransferencias> busca) {
		cacheRanking.computeIfAbsent(chave, i -> busca.get());		
		return cacheRanking.get(chave);
	}

}