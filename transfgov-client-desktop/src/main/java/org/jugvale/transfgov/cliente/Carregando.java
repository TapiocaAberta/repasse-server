package org.jugvale.transfgov.cliente;

import javafx.beans.property.SimpleBooleanProperty;

public class Carregando {
	
	private static SimpleBooleanProperty estaCarregando;
	
	static {
		estaCarregando = new SimpleBooleanProperty();		
	}
	
	public static SimpleBooleanProperty prop(){
		return estaCarregando;
	}
	
	public static void carregando(boolean valor){
		estaCarregando.setValue(valor);
	}

}
