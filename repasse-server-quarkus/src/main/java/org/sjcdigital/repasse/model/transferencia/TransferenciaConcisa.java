package org.sjcdigital.repasse.model.transferencia;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TransferenciaConcisa {
	
	@JsonIgnore
	private Transferencia transferencia;

	public TransferenciaConcisa(Transferencia transferencia) {
		this.transferencia = transferencia;
	}
	
	public String getAcao() {
		return transferencia.getAcao().getNome();
	}
	
	public String getPrograma() {
		return transferencia.getPrograma().getNome();
	}
	
	public String getArea() {
		return transferencia.getArea().getNome();
	}
	
	public String getSubFuncao() {
		return transferencia.getSubFuncao().getNome();
	}
	
	public String getFavorecido() {
		return transferencia.getFavorecido().getNome();
	}
	
	public float getValor() {
		return transferencia.getValor();
	}
}
