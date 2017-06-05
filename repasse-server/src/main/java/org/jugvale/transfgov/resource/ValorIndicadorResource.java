package org.jugvale.transfgov.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.PathSegment;

import org.jugvale.transfgov.model.indicador.ResumoValorIndicador;
import org.jugvale.transfgov.model.indicador.ValorIndicador;

/**
 * @author william
 *
 */
/**
 * @author william
 *
 */
@Path("valor_indicador")
@Produces("application/json; charset=utf8")
public interface ValorIndicadorResource {
	
	/**
	 * 
	 * Todos os ValorIndicador para indicador idIndicador e município idMunicipio e o ano informado
	 * 
	 * @param idIndicador
	 * @param idMunicipio
	 * @param ano
	 * @return
	 */
	@GET
	@Path("/indicador/{idIndicador}/municipio/{idMunicipio}/ano/{ano}")
	public ValorIndicador valoresIndicadoresMunicipioPorAno(@PathParam("idIndicador") long idIndicador, 
			@PathParam("idMunicipio") long idMunicipio, @PathParam("ano") int ano);
	
	/**
	 * 
	 * Todos os ValorIndicador para o município idMunicipio e para todos os anos
	 * 
	 * @param idMunicipio
	 * @return
	 */
	@GET
	@Path("/municipio/{idMunicipio}/indicador")
	public List<ValorIndicador> todosValoresIndicadores(@PathParam("idMunicipio") long idMunicipio);
	
	/**
	 * 
	 * Todos os ValorIndicador para o município idMunicipio e para o ano informado
	 * 
	 * @param idMunicipio
	 * @param ano
	 * @return
	 */
	@GET
	@Path("/municipio/{idMunicipio}/indicador/{ano}")
	public List<ValorIndicador> todosValoresIndicadoresPorAno(@PathParam("idMunicipio") long idMunicipio, @PathParam("ano") int ano);
	
	//      
	/**
	 * 
	 * Todos os valores de indicadores para município idMunicipio e área nomeArea (todos anos)
	 * 
	 * @param idMunicipio
	 * @param area
	 * @return
	 */
	@GET
	@Path("/municipio/{idMunicipio}/area/{nomeArea}/indicador")
	public List<ValorIndicador> todosValoresIndicadoresPorMunicipioArea(@PathParam("idMunicipio") long idMunicipio, @PathParam("nomeArea") String area);
	
	
	/**
	 * 
	 * Todos os valores de indicadores para município idMunicipio e área nomeArea no ano informado
	 * 
	 * @param idMunicipio
	 * @param area
	 * @param ano
	 * @return
	 */
	@GET
	@Path("/municipio/{idMunicipio}/area/{nomeArea}/indicador/ano/{ano}")
	public List<ValorIndicador> todosValoresIndicadoresPorMunicipioAreaAno(@PathParam("idMunicipio") long idMunicipio, @PathParam("nomeArea") String area, @PathParam("ano") int ano);
	
	
	/**
	 * 
	 * Todos os ValorIndicador para indicador idIndicador e município idMunicipio e o ano informado
	 * 
	 * @param idIndicador
	 * @param idMunicipio
	 * @param ano
	 * @return
	 */
	@GET
	@Path("/ano/{ano}/municipios/{municipioIds}/")
	public List<ValorIndicador> valoresIndicadoresMunicipiosPorAno(@PathParam("ano") int ano, @PathParam("municipioIds") PathSegment pathSegment);
	
	

	/**
	 * Resumo dos dados juntamente com as informacoes dos municipios
	 * 
	 * @param ano
	 * @param pathSegment
	 * @return
	 */
	@GET
	@Path("/ano/{ano}/municipios/{municipioIds}/resumo")
	public ResumoValorIndicador resumoValoresIndicadoresMunicipiosPorAno(@PathParam("ano") int ano, @PathParam("municipioIds") PathSegment pathSegment);

}