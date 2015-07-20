package org.jugvale.transfgov.agregacao;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.resteasy.spi.NotImplementedYetException;
import org.jugvale.transfgov.model.ranking.RankingTransferencias;

/**
 * 
 * Acessa os dados de transferência diretamente montando um ranking per capita
 * 
 * @author wsiqueir
 *
 */
@RequestScoped
public class RankingController {

	@PersistenceContext
	EntityManager em;

	public RankingTransferencias rankingPorAno(int ano) {
		// TODO implementar
		throw new NotImplementedYetException();
	}

	public RankingTransferencias rankingPorAnoArea(int ano, String area) {
		// TODO implementar
		throw new NotImplementedYetException();
	}

	public RankingTransferencias rankingPorAnoAcao(int ano, String acao) {
		// TODO implementar
		throw new NotImplementedYetException();
	}

	public RankingTransferencias rankingPorAnoPrograma(int ano, String programa) {
		// TODO implementar
		throw new NotImplementedYetException();
	}

	public RankingTransferencias rankingPorAnoSubFuncao(int ano,
			String subFuncao) {
		// TODO implementar
		throw new NotImplementedYetException();
	}

}
