package org.sjcdigital.repasse.model.indicador;

import java.util.List;
import java.util.Map;

/**
 * 
 * Um objeto para manter um resumo sobre valores indicadores de municipio(os)
 * 
 * @author william
 *
 */
public class ResumoValorIndicador {

	private int ano;
	private Map<Indicador, Double> mediaIndicadores;
	private List<ValorIndicador> valoresMunicipiosSelecionados;

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public List<ValorIndicador> getValoresMunicipiosSelecionados() {
		return valoresMunicipiosSelecionados;
	}

	public void setValoresMunicipiosSelecionados(List<ValorIndicador> valoresMunicipiosSelecionados) {
		this.valoresMunicipiosSelecionados = valoresMunicipiosSelecionados;
	}

	public Map<Indicador, Double> getMediaIndicadores() {
		return mediaIndicadores;
	}

	public void setMediaIndicadores(Map<Indicador, Double> mediaIndicadores) {
		this.mediaIndicadores = mediaIndicadores;
	}

}