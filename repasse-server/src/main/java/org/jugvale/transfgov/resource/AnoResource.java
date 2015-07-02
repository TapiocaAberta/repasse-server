package org.jugvale.transfgov.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jugvale.transfgov.model.base.AnoMes;

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
	public List<AnoMes> anos();

}
