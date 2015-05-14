package org.jugvale.transparencia.transf.carga;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.jugvale.transparencia.transf.service.impl.AcaoService;
import org.jugvale.transparencia.transf.service.impl.AreaService;
import org.jugvale.transparencia.transf.service.impl.EstadoService;
import org.jugvale.transparencia.transf.service.impl.FavorecidoService;
import org.jugvale.transparencia.transf.service.impl.FonteFinalidadeService;
import org.jugvale.transparencia.transf.service.impl.MunicipioService;
import org.jugvale.transparencia.transf.service.impl.ProgramaService;
import org.jugvale.transparencia.transf.service.impl.SubFuncaoService;
import org.jugvale.transparencia.transf.service.impl.TransferenciaService;

@Stateless
@Asynchronous
public class CargaDadosController {

	@Inject
	MensagensCargaSingleton mensagens;

	@Inject
	TransferenciaService transferenciaService;

	@Inject
	MunicipioService municipioService;

	@Inject
	AcaoService acaoService;

	@Inject
	EstadoService estadoService;

	@Inject
	AreaService areaService;

	@Inject
	SubFuncaoService subFuncaoService;

	@Inject
	ProgramaService programaService;

	@Inject
	FonteFinalidadeService fonteFinalidadeService;

	@Inject
	FavorecidoService favorecidoService;

	/**
	 * Irá carregar o arquivo passado no banco de dados. Linha a linha será
	 * inserida.
	 * 
	 * @param arquivoCSV
	 *            O arquivo para ser carregado
	 * @throws IOException
	 */
	@Asynchronous
	public void carregarNoBanco(int ano, int mes, Path arquivoCSV)
			throws IOException {
		mensagens.limpar(ano, mes);
		AtomicInteger sucesso = new AtomicInteger(0);
		AtomicInteger falha = new AtomicInteger(0);
		mensagens.adicionar(ano, mes, "Iniciando carga em " + new Date());
		Files.lines(arquivoCSV, StandardCharsets.UTF_8).skip(1).forEach(linha -> {
			try {
				salvarLinha(linha);
				sucesso.incrementAndGet();
			} catch (Exception e) {
				falha.incrementAndGet();
				mensagens.adicionar(ano, mes, "Error: " + e.getMessage());
			}
		});
		String relatorio = String.format("%d linhas adicionadas. %d erros",
				sucesso.get(), falha.get());
		Files.delete(arquivoCSV);
		mensagens.adicionar(ano, mes, relatorio);
		mensagens.adicionar(ano, mes, "Carga terminada em " + new Date());
	}

	public void salvarLinha(String linha) throws Exception {
		String[] campos = linha.split("\\t");
		for (int i = 0; i < campos.length; i++) {
			System.out.println("\"" + campos[i] + "\"");
		}
		System.out.println("----------------");

	}

}
