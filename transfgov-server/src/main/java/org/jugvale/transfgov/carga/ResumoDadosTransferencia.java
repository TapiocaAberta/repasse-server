package org.jugvale.transfgov.carga;

public class ResumoDadosTransferencia {
	
	private int ano;
	private int mes;
	private long totalTransferencias;
	private long contagemLinhas;
	
	public int getAno() {
		return ano;
	}
	public void setAno(int ano) {
		this.ano = ano;
	}
	public int getMes() {
		return mes;
	}
	public void setMes(int mes) {
		this.mes = mes;
	}
	public long getTotalTransferencias() {
		return totalTransferencias;
	}
	public void setTotalTransferencias(long totalTransferencias) {
		this.totalTransferencias = totalTransferencias;
	}
	public long getContagemLinhas() {
		return contagemLinhas;
	}
	public void setContagemLinhas(long contagemLinhas) {
		this.contagemLinhas = contagemLinhas;
	}

}
