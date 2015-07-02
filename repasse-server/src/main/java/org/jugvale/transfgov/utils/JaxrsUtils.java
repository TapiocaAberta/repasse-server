package org.jugvale.transfgov.utils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

public class JaxrsUtils {
	
	private static final String NAO_HA_RESULTADOS = "Não há resultados...";
	public final static int TOTAL_POR_PAGINA = 30;	
	public final static String PARAMETRO_PAGINA = "pg"; 
	

	public static <T extends Object> T lanca404SeNulo(Object obj, Class<T> tipo) {
		if (Objects.isNull(obj)) {
			throw new WebApplicationException(404);
		}
		return tipo.cast(obj);
	}
	
	public static void lanca404SeVazio(List<?> dados) {
		lanca404SeFalso(dados.size() > 0);
	}
	
	public static void lanca404SeFalso(boolean valor) {
		lanca404SeFalso(valor, NAO_HA_RESULTADOS);
	}

	public static void lanca404SeFalso(boolean valor, String mensagem) {
		if (!valor)
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).entity(mensagem).build());
	}
	
	/**
	 * 
	 * Constrói links de navegação. Método feio, mas útil para construir Links sem repetir código
	 * 
	 * @param uriInfo
	 * @param urlBase
	 * @param totalResultados
	 * @param pg
	 * @param parametrosPath
	 * @return
	 */
	public static List<Link> constroiLinksNavegacao(UriInfo uriInfo, UriBuilder urlBase, long totalResultados, int pg, Object...parametrosPath){
		ArrayList<Link> links = new ArrayList<>();
		Link esse = Link.fromUri(uriInfo.getAbsolutePathBuilder().queryParam(PARAMETRO_PAGINA, pg).build()).rel("self").title("Essa página").build();
		links.add(esse);
		if(pg < totalResultados / JaxrsUtils.TOTAL_POR_PAGINA) {
			URI proximo = urlBase.clone().queryParam(PARAMETRO_PAGINA, pg + 1).build(parametrosPath);			
			links.add(Link.fromUri(proximo).rel("next").title("Próxima Página").build());
		}
		if(pg > 0) {			
			URI anterior = urlBase.clone().queryParam(PARAMETRO_PAGINA, pg - 1).build(parametrosPath);
			links.add(Link.fromUri(anterior).rel("prev").title("Página Anterior").build());
		}
		return links;		
	}
}
