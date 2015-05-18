package org.jugvale.transparencia.transf.carga;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.jugvale.transparencia.transf.model.base.Area;
import org.jugvale.transparencia.transf.model.base.Estado;
import org.jugvale.transparencia.transf.model.base.Municipio;
import org.jugvale.transparencia.transf.model.transferencia.Acao;
import org.jugvale.transparencia.transf.model.transferencia.Favorecido;
import org.jugvale.transparencia.transf.model.transferencia.Programa;
import org.jugvale.transparencia.transf.model.transferencia.SubFuncao;
import org.jugvale.transparencia.transf.model.transferencia.Transferencia;
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
public class CargaDadosController {

	@Inject
	Logger logger;

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
		AtomicInteger totalSucesso = new AtomicInteger(0);
		AtomicInteger totalFalha = new AtomicInteger(0);
		AtomicInteger totalNaoProcessada = new AtomicInteger(0);
		AtomicInteger totalLinhas  = new AtomicInteger();
		mensagens.adicionar(ano, mes, "Iniciando carga em " + new Date());
		Files.lines(arquivoCSV, StandardCharsets.UTF_8).skip(1).forEach(linha -> {
			totalLinhas.incrementAndGet();
			try {
				if(!salvarLinha(ano, mes, linha)){
					totalNaoProcessada.incrementAndGet();
				}
				totalSucesso.incrementAndGet();
				logger.fine(linha);
			} catch (Exception e) {
				totalFalha.incrementAndGet();
				mensagens.adicionar(ano, mes,"Erro na linha "+ totalLinhas.get() +": " + e.getMessage());	
				e.printStackTrace();
			}
		});
		String relatorio = String.format("%d linhas totais. %d linhas adicionadas. %d erros. %d não processadas. ",
				totalLinhas.get(), totalSucesso.get(), totalFalha.get(), totalNaoProcessada);
		Files.delete(arquivoCSV);
		mensagens.adicionar(ano, mes, relatorio);
		mensagens.adicionar(ano, mes, "Carga terminada em " + new Date());
	}

	/**
	 * 
	 * Irá pegar cada dado do CSV e salvar no banco de dados. Campos do CSV:
	 * --
	 * Sigla Unidade Federação
	 * Codigo SIAFI Municipio
	 * Nome Municipio
	 * Codigo Funcao
	 * Nome Funcao
	 * Codigo Sub Funcao 
	 * Nome Sub Funcao
	 * Codigo Programa
	 * Nome Programa
	 * Codigo Acao
	 * Nome Acao
	 * Linguagem Cidadã
	 * Codigo Favorecido
	 * Nome Favorecido
	 * Fonte-Finalidade
	 * Modalidade Aplicação
	 * Número Convênio
	 * Valor Parcela
	 * --
	 * @param linha
	 * @throws Exception
	 * @return
	 * Se a linha foi processada ou não 
	 *
	 */
	public boolean salvarLinha(int ano, int mes, String linha) throws Exception {
		String[] campos = linha.split("\\t");
		if (campos.length != 18) {
			return false;
		}
		String siglaEstado = campos[0];
		String siafiMunicipio = campos[1];
		String nomeMunicipio = campos[2];
		long codigoFuncao = Long.parseLong(campos[3]);
		String nomeFuncao = campos[4];
		long codigoSubFuncao = Long.parseLong(campos[5]);
		String nomeSubFuncao = campos[6];
		long codigoPrograma = Long.parseLong(campos[7]);
		String nomePrograma = campos[8];
		String codigoAcao = campos[9];
		String nomeAcao = campos[10];
		String nomePopular = campos[11];		
		String codigoFavorecido = campos[12];
		String nomeFavorecido = campos[13];
		// GAMBIARRA PARA EVITAR PROBLEMAS COM LOCALE DE FLOATS
		float valor = Float.parseFloat(campos[17].replaceAll("\\,", "").replace("\\.", ","));
		
		Estado estado = estadoService.buscaEstadoPorSiglaOuCria(siglaEstado,
				() -> new Estado(siglaEstado));
		Municipio municipio = municipioService.porEstadoNomeESIAFIOuCria(estado, nomeMunicipio, siafiMunicipio, 
				() -> new Municipio(siafiMunicipio, nomeMunicipio, estado));
		Area area = areaService.buscaPorIdOuCria(codigoFuncao, () -> new Area(codigoFuncao, nomeFuncao));
		SubFuncao subFuncao = subFuncaoService.buscaPorIdOuCria(codigoSubFuncao, () -> new SubFuncao(codigoSubFuncao, nomeSubFuncao, area));
		Programa programa = programaService.buscaPorIdOuCria(codigoPrograma, () -> new Programa(codigoPrograma, nomePrograma));
		Acao acao = acaoService.buscaPorCodigoOuCria(codigoAcao, () -> new Acao(codigoAcao, nomeAcao, nomePopular));
		Favorecido favorecido = favorecidoService.buscaPorCodigoOuCria(
				codigoFavorecido, () -> new Favorecido(nomeFavorecido,
						codigoFavorecido));	
		Transferencia transferencia = new Transferencia();
		transferencia.setAno(ano);
		transferencia.setMes(mes);
		transferencia.setAcao(acao);
		transferencia.setFavorecido(favorecido);

		transferencia.setMunicipio(municipio);
		transferencia.setPrograma(programa);
		transferencia.setSubFuncao(subFuncao);
		transferencia.setValor(valor);
		transferenciaService.salvar(transferencia);
		return true;
	}
}
