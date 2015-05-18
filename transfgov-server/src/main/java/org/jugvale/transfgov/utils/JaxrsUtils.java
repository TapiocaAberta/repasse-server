package org.jugvale.transfgov.utils;

import java.util.Objects;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class JaxrsUtils {

	public static <T extends Object> T lanca404SeNulo(Object obj, Class<T> tipo) {
		if (Objects.isNull(obj)) {
			throw new WebApplicationException(404);
		}
		return tipo.cast(obj);
	}

	public static void lanca404SeFalso(boolean valor, String mensagem) {
		if (!valor)
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).entity(mensagem).build());
	}
}
