package org.jugvale.transfgov.resource;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.plugins.providers.multipart.MultipartInput;

/**
 * 
 * Interface REST para permitir a carga de dados
 * 
 * @author wsiqueir
 *
 */
public interface CargaDadosResource {
	
	final String MSG_SUCESSO = "Dados para ano %d e mês %d agendados para carga. Acesse GET /carga/%d/%d/mensagens para ver o andamento.";
	final String MSG_CARGA_REPETIDA = "Já existem dados para ano %d  mes %d. Limpe os dados antes de tentar uma nova carga";
	final String MSG_ERRO_BAIXAR = "Erro ao baixar dados. Verifique se o ano e mês estão disponíveis no portal da transparência. Mais detalhes no log do servidor";

	@POST
	@Path("{ano}/{mes}")
	public Response baixaECarrega(@PathParam("ano") int ano, @PathParam("mes") int mes) throws IOException;

	/**
	 * Código macarrão para ler o zip de transferencia do site www.portaldatransparencia.gov.br
	 * 
	 * @param multipart
	 * @return
	 * @throws IOException
	 */
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response recebeCarga(MultipartInput conteudo) throws IOException;

	@GET
	@Path("/{ano}/{mes}/mensagens")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<String> recuperaMensagensCarga(@PathParam("ano") int ano, @PathParam("mes") int mes);

	@DELETE
	@Path("/{ano}/{mes}/mensagens")
	public void limpaMensages(@PathParam("ano") int ano, @PathParam("mes") int mes);
	
	@DELETE
	@Path("/mensages")
	public void limpaMensages();

}
