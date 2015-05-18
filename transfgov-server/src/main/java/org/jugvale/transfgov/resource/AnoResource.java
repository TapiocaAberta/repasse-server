package org.jugvale.transfgov.resource;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * 
 * Irá retornar as datas disponíveis para acesso
 * 
 * @author wsiqueir
 *
 */
@Path("ano")
@Produces(MediaType.APPLICATION_JSON)
public interface AnoResource {
	@GET
	public Map<Integer, List<Integer>> anos();

}
