package org.jugvale.transfgov.model.base;

import java.util.List;

public class AnoMes {
	
	private int ano;
	private List<Integer> meses;
	
	public AnoMes() {
		super();
	}
	public AnoMes(int ano, List<Integer> meses) {
		super();
		this.ano = ano;
		this.meses = meses;
	}
	public int getAno() {
		return ano;
	}
	public void setAno(int ano) {
		this.ano = ano;
	}
	public List<Integer> getMeses() {
		return meses;
	}
	public void setMeses(List<Integer> meses) {
		this.meses = meses;
	}
	

}
