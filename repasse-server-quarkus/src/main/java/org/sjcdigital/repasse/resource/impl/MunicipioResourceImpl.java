package org.sjcdigital.repasse.resource.impl;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.sjcdigital.repasse.model.base.DadosMunicipio;
import org.sjcdigital.repasse.model.base.Municipio;
import org.sjcdigital.repasse.resource.MunicipioResource;
import org.sjcdigital.repasse.service.impl.DadosMunicipioService;
import org.sjcdigital.repasse.service.impl.MunicipioService;
import org.sjcdigital.repasse.utils.JaxrsUtils;

public class MunicipioResourceImpl implements MunicipioResource {

	@Inject
	MunicipioService municipioService;
	
	@Inject 
	DadosMunicipioService dadosMunicipioService;
	
	@Context
	UriInfo uriInfo;
	
	public Response todosMunicipios(int pg) {
		ResponseBuilder resposta = Response.ok();
		UriBuilder urlBase = uriInfo.getBaseUriBuilder().path(MunicipioResource.class);
		long totalResultados = municipioService.contaTodos();		
		List<Municipio> resultado = municipioService.todosPaginado(JaxrsUtils.TOTAL_POR_PAGINA, pg);
		JaxrsUtils.lanca404SeVazio(resultado);
		resposta.entity(resultado);
		JaxrsUtils.constroiLinksNavegacao(uriInfo, urlBase, totalResultados, pg).stream().forEach(resposta::links);
		return resposta.build();
	}
	
	public Municipio porNomeEEstado(String sigla, String nome) {
		return municipioService.buscaPorNomeEEstado(sigla, nome);		
	}

	@Override
	public List<DadosMunicipio> dadosMunicipio(String sigla, String nome) {
		Municipio m = porNomeEEstado(sigla, nome);
		return dadosMunicipioService.buscaMunicipio(m);
	}

}