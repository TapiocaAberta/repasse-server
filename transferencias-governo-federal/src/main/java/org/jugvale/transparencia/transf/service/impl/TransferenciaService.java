package org.jugvale.transparencia.transf.service.impl;

import java.util.List;

import org.jugvale.transparencia.transf.model.base.Municipio;
import org.jugvale.transparencia.transf.model.transferencia.Transferencia;
import org.jugvale.transparencia.transf.service.Service;

public class TransferenciaService extends Service<Transferencia> {

	/**
	 * Faz a busca de transferência por ano e mês
	 * 
	 * @param ano
	 * @param mes
	 */
	public void buscaTransferencias(int ano, int mes) {
		// TODO
	}

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
		return false;
	}

	public List<Transferencia> buscarPorAnoMesMunicipio(int ano, int mes, Municipio municipio) {
		// TODO implementar busca
		return null;
	}

}
