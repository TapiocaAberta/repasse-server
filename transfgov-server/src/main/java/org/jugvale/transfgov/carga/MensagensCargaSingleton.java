package org.jugvale.transfgov.carga;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.inject.Inject;

/**
 * 
 * Contém as mensagens que aconteceram durante a carga de dados para um dado
 * ano/mes
 * 
 * @author wsiqueir
 *
 */
@Singleton
public class MensagensCargaSingleton {

	private Map<String, List<String>> mensagensCarga;

	@Inject
	Logger logger;

	@PostConstruct
	public void inicializa() {
		mensagensCarga = new HashMap<>();
	}

	public Map<String, List<String>> mensagens() {
		return mensagensCarga;
	}

	public void limpar() {
		inicializa();
	}

	public void limpar(int ano, int mes) {
		mensagensCarga.remove(chave(ano, mes));
	}

	public void adicionar(int ano, int mes, String valor) {
		logger.warning(String.format("Carga %d/%d: %s", ano, mes, valor));
		String chave = chave(ano, mes);
		mensagensCarga.putIfAbsent(chave, new ArrayList<>());
		mensagensCarga.get(chave).add(valor);
	}

	public List<String> recuperarMensages(int ano, int mes) {
		return mensagensCarga.get(chave(ano, mes));
	}

	private String chave(int ano, int mes) {
		return String.format("%d-%d", ano, mes);
	}

}
