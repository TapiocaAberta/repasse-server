package org.jugvale.transfgov.model.carga;

/**
 * 
 * A linha de carga suportada para os indicadores. Essa linha de carga deverá
 * ser usada depois para realizar a carga no banco dos dados então normalizados.
 * 
 * @author wsiqueir
 *
 */
public class LinhaCargaIndicador {

	private String municipio;

	private String uf;

	private float valorIndicador;

	private int ano;
	

	public LinhaCargaIndicador() {
	}

	public LinhaCargaIndicador(String municipio, String uf,
			float valorIndicador, int ano) {
		super();
		this.municipio = municipio;
		this.uf = uf;
		this.valorIndicador = valorIndicador;
		this.ano = ano;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String nomeMun) {
		this.municipio = nomeMun;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}


	public float getValorIndicador() {
		return valorIndicador;
	}

	public void setValorIndicador(float valorIndicador) {
		this.valorIndicador = valorIndicador;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

}