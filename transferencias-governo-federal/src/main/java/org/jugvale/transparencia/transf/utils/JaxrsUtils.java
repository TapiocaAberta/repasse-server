package org.jugvale.transparencia.transf.utils;

import java.util.Objects;

import javax.ws.rs.WebApplicationException;

public class JaxrsUtils {
	
	public static <T extends Object> T lanca404SeNulo(Object obj, Class<T> tipo) {
		if(Objects.isNull(obj)){
			throw new WebApplicationException(404);			
		}
		return tipo.cast(obj);
	}

}
