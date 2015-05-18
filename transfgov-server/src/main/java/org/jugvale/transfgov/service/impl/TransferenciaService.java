package org.jugvale.transfgov.service.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.TypedQuery;

import org.jugvale.transfgov.model.base.Area;
import org.jugvale.transfgov.model.base.Estado;
import org.jugvale.transfgov.model.base.Municipio;
import org.jugvale.transfgov.model.transferencia.Transferencia;
import org.jugvale.transfgov.service.Service;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class TransferenciaService extends Service<Transferencia> {

	/**
	 * Limpa todos os dados de transferencias para esse ano e mes
	 * 
	 * @param ano
	 * @param mes
	 */
	public void limpaTransferencias(int ano, int mes) {
		// TODO
	}

	/**
	 * 
	 * Verifica se há transferências para aquele ano e mês
	 * @param ano
	 * @param mes
	 * @return
	 */
	public boolean temTranferencia(int ano, int mes) {		
		TypedQuery<Long> buscaTransferencia = em.createNamedQuery("Transferencia.quantidadePorMesEAno", Long.class);
		buscaTransferencia.setParameter("ano", ano);
		buscaTransferencia.setParameter("mes", mes);
		return buscaTransferencia.getSingleResult() > 0;
	}
	
	public boolean temTranferencia(int ano) {		
		TypedQuery<Long> buscaTransferencia = em.createNamedQuery("Transferencia.quantidadePorAno", Long.class);
		buscaTransferencia.setParameter("ano", ano);
		return buscaTransferencia.getSingleResult() > 0;
	}	

	public List<Transferencia> buscarPorAnoMesMunicipio(int ano, int mes, Municipio municipio) {
		TypedQuery<Transferencia> buscaTransferencia = em.createNamedQuery("Transferencia.porAnoMesMunicipio", Transferencia.class);
		buscaTransferencia.setParameter("ano", ano);
		buscaTransferencia.setParameter("mes", mes);
		buscaTransferencia.setParameter("municipio", municipio);
		return buscaTransferencia.getResultList();
	}
	
	public List<Transferencia> buscarPorAnoMunicipio(int ano, Municipio municipio) {
		TypedQuery<Transferencia> buscaTransferencia = em.createNamedQuery("Transferencia.porAnoMunicipio", Transferencia.class);
		buscaTransferencia.setParameter("ano", ano);
		buscaTransferencia.setParameter("municipio", municipio);
		return buscaTransferencia.getResultList();
	}

	public List<Transferencia> buscarPorAnoMesEstado(int ano, int mes, Estado estado) {
		TypedQuery<Transferencia> buscaTransferencia = em.createNamedQuery("Transferencia.porAnoMesEstado", Transferencia.class);
		buscaTransferencia.setParameter("ano", ano);
		buscaTransferencia.setParameter("mes", mes);
		buscaTransferencia.setParameter("estado", estado);
		return buscaTransferencia.getResultList();
	}

	public List<Transferencia> buscarPorAnoMesAreaMunicipio(int ano, int mes, Area area, Municipio municipio) {
		TypedQuery<Transferencia> buscaTransferencia = em.createNamedQuery("Transferencia.porAnoMesAreaMunicipio", Transferencia.class);
		buscaTransferencia.setParameter("ano", ano);
		buscaTransferencia.setParameter("mes", mes);
		buscaTransferencia.setParameter("area", area);
		buscaTransferencia.setParameter("municipio", municipio);
		return buscaTransferencia.getResultList();
	}

	public List<Integer> todosAnos() {
		return em.createNamedQuery("Transferencia.todosAnos", Integer.class).getResultList();
	}

	public List<Integer> mesesDisponiveis(int ano) {
		TypedQuery<Integer> buscaMeses = em.createNamedQuery("Transferencia.mesesPorAno", Integer.class);
		buscaMeses.setParameter("ano", ano);
		return buscaMeses.getResultList();
	}	

}
