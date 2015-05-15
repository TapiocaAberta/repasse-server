package org.jugvale.transparencia.transf.resource.providers;

import javax.persistence.NoResultException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class MapeaNoResultExceptionTo404 implements ExceptionMapper<NoResultException> {

	@Override
	public Response toResponse(NoResultException e) {
		return Response.status(Status.NOT_FOUND).build();
	}

}
