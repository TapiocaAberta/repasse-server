package org.jugvale.transparencia.transf.resource;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartInput;
import org.jugvale.transparencia.transf.carga.CargaDadosController;
import org.jugvale.transparencia.transf.carga.MensagensCargaSingleton;
import org.jugvale.transparencia.transf.service.impl.TransferenciaService;
import org.jugvale.transparencia.transf.utils.ArquivoTransfUtils;

/**
 * 
 * Interface REST para permitir a carga de dados
 * 
 * @author wsiqueir
 *
 */
@Path("carga")
@Stateless
public class CargaDadosResource {
	
	final String MSG_SUCESSO = "Dados para ano %d e mês %d agendados. Acesse GET /carga/%d/%d para ver o andamento.";
	final String MSG_ERRO = "Já existem dados para ano %d  mes %d. Limpe os dados antes de tentar uma nova carga";

	@Inject
	TransferenciaService transferenciaService;

	@Inject
	CargaDadosController cargaDadosController;

	@Inject
	MensagensCargaSingleton mensagens;

	/**
	 * Código macarrão para ler o zip de transferencia do site www.portaldatransparencia.gov.br
	 * 
	 * @param multipart
	 * @return
	 * @throws IOException
	 */
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response recebeCarga(MultipartInput conteudo) throws IOException {
		int ano = 0, mes = 0;
		ResponseBuilder resposta;
		InputPart conteudoZip = null;
		for(InputPart p : conteudo.getParts()) {
			if(p.getMediaType().toString().equals(MediaType.APPLICATION_OCTET_STREAM)) {
				conteudoZip = p;
			}
		}
		Objects.requireNonNull(conteudoZip, "Você deve enviar um anexo");
		java.nio.file.Path arquivoCSV = ArquivoTransfUtils.deszipaECriaCSV(conteudoZip.getBody(byte[].class, null));
		String dadosData = arquivoCSV.toFile().getName().substring(0, 6);
		ano = Integer.parseInt(dadosData.substring(0, 4));
		mes = Integer.parseInt(dadosData.substring(4, 6));			
		cargaDadosController.carregarNoBanco(ano, mes, arquivoCSV);
		if (transferenciaService.temTranferencia(ano, mes)) {
			resposta =  Response.status(Status.CONFLICT).entity(String.format(MSG_ERRO, ano, mes));
		} else {			
			resposta = Response.ok(String.format(MSG_SUCESSO, ano, mes, ano, mes));
		}		
		return resposta.build();	
	}

	@GET
	@Path("/{ano}/{mes}/mensages")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<String> recuperaMensagensCarga(@PathParam("ano") int ano,
			@PathParam("mes") int mes) {
		return mensagens.recuperarMensages(ano, mes);
	}

	@DELETE
	@Path("/{ano}/{mes}/mensages")
	public void limpaMensages(@PathParam("ano") int ano,
			@PathParam("mes") int mes) {
		mensagens.limpar(ano, mes);
	}

	@DELETE
	@Path("/mensages")
	public void limpaMensages() {
		mensagens.limpar();
	}

}
