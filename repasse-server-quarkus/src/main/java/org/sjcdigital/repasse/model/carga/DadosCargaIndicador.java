package org.sjcdigital.repasse.model.carga;

import java.util.List;

public class DadosCargaIndicador {

	private String indicador;
	private String grupoIndicador;
	private String focoIndicador;
	private String area;
	private List<LinhaCargaIndicador> linhas;

	public String getIndicador() {
		return indicador;
	}

	public void setIndicador(String indicador) {
		this.indicador = indicador;
	}

	public String getGrupoIndicador() {
		return grupoIndicador;
	}

	public void setGrupoIndicador(String grupoIndicador) {
		this.grupoIndicador = grupoIndicador;
	}

	public String getFocoIndicador() {
		return focoIndicador;
	}

	public void setFocoIndicador(String focoIndicador) {
		this.focoIndicador = focoIndicador;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public List<LinhaCargaIndicador> getLinhas() {
		return linhas;
	}

	public void setLinhas(List<LinhaCargaIndicador> linhas) {
		this.linhas = linhas;
	}

}
