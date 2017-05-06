package org.jugvale.transfgov.carga;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.jugvale.transfgov.model.base.Area;
import org.jugvale.transfgov.model.base.Municipio;
import org.jugvale.transfgov.model.carga.DadosCargaIndicador;
import org.jugvale.transfgov.model.carga.LinhaCargaIndicador;
import org.jugvale.transfgov.model.indicador.FocoIndicador;
import org.jugvale.transfgov.model.indicador.GrupoIndicador;
import org.jugvale.transfgov.model.indicador.Indicador;
import org.jugvale.transfgov.model.indicador.ValorIndicador;
import org.jugvale.transfgov.service.impl.AreaService;
import org.jugvale.transfgov.service.impl.FocoIndicadorService;
import org.jugvale.transfgov.service.impl.GrupoIndicadorService;
import org.jugvale.transfgov.service.impl.IndicadorService;
import org.jugvale.transfgov.service.impl.MunicipioService;
import org.jugvale.transfgov.service.impl.ValorIndicadorService;
import org.jugvale.transfgov.utils.TextoUtils;

@Stateless
public class CargaIndicadorController {

	@Inject
	GrupoIndicadorService grupoIndicadorService;

	@Inject
	IndicadorService indicadorService;

	@Inject
	FocoIndicadorService focoIndicadorService;

	@Inject
	ValorIndicadorService valorIndicadorService;

	@Inject
	MunicipioService municipioService;

	@Inject
	AreaService areaService;
	
	@Inject
	Logger logger;


	/**
	 * 
	 * Faz a carga de todos os indicadores. Esse método não é assíncrono, assim
	 * temos que tomar cuidado com transaction timeout com cargas de muitas
	 * linhas
	 * 
	 * @param linhas
	 *            As linhas de indicadores a serem carregados
	 * 
	 * @return Uma lista de string com textos resumitivos de cada operação
	 */
	@Asynchronous
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void carregaIndicadores(DadosCargaIndicador carga) {
		String nomeGrupoIndicador = carga.getGrupoIndicador();
		String nomeIndicador = carga.getIndicador();
		String focoIndicador = carga.getFocoIndicador();
		Area area = areaService.buscaPorNome(carga.getArea());
		logger.info("Carregando dados do indicador...");
		GrupoIndicador grupo = grupoIndicadorService.buscaPorNomeOuCria(
				nomeGrupoIndicador,
				() -> new GrupoIndicador(nomeGrupoIndicador));

		Indicador indicador = indicadorService.buscaPorNomeOuCria(
				nomeIndicador, () -> new Indicador(nomeIndicador, grupo, area));

		FocoIndicador foco = focoIndicadorService.buscaPorNomeOuCria(
				focoIndicador,
				() -> new FocoIndicador(focoIndicador, indicador));
		logger.info("Dados do indicador carregados [" + grupo + ", " + indicador + ", " + foco + "]");
		long m = System.currentTimeMillis();
		logger.info("Carregando linhas (pode levar alguns minutos) ");
		List<String> resultado = carga.getLinhas().stream()
				.map(l -> carregaLinha(grupo, indicador, foco, area, l))
				.collect(Collectors.toList());
		logger.info("Fim da carga em " + (System.currentTimeMillis() -m ) + "ms [" + grupo + ", " + indicador + ", " + foco + "]");
		try {
			Path tmpDadosIndicadores = Files.createTempFile("cargaIndicador", "");
			Files.write(tmpDadosIndicadores, resultado.stream().collect(Collectors.joining("\n")).getBytes());
			logger.info("Saida salva em " + tmpDadosIndicadores.getFileName());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 
	 * Insere cada linha e retorna um texto sobre a operação. Linhas repetidas
	 * de valorIndicador serão sobreescritas.
	 * 
	 * @param linha
	 *            A linha de indicador a ser inserido
	 * @return Um texto informando como foi o processo (sucesso, erro, etc)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private String carregaLinha(GrupoIndicador grupoIndicador,
			Indicador indicador, FocoIndicador focoIndicador, Area area,
			LinhaCargaIndicador linha) {
		String nomeMunOriginal = linha.getMunicipio();
		String nomeMunicipio = TextoUtils.transformaNomeCidade(nomeMunOriginal);
		float valor = linha.getValorIndicador();
		ValorIndicador valorIndicador = null;
		Municipio mun;
		int ano = linha.getAno();
		logger.fine("Carregando linha para município " + nomeMunicipio);
		try {
			mun = municipioService.buscaPorNomeEEstado(linha.getUf(),
					nomeMunicipio);
		} catch (Exception e) {
			if(e instanceof NoResultException) {
				return "Município " + nomeMunOriginal + "não encontrado.";
			}
			else {
				e.printStackTrace();
				return "Error inesperado carregando linha" + e.getMessage();
			}
		}
		boolean valorExiste = valorIndicadorService.verificaSeValorExiste(ano,
				indicador, mun);
		if (valorExiste) {
			valorIndicador = valorIndicadorService.buscaPorAnoMunicipioIndicador(ano,
					indicador, mun);
			valorIndicador.setValor(valor);
			valorIndicadorService.atualizar(valorIndicador);
			return valorIndicador + " atualizado.";
		}
		try {
			valorIndicador = new ValorIndicador(valor, ano, indicador, mun);
			valorIndicadorService.salvar(valorIndicador);
			return valorIndicador + " salvo com sucesso.";
		} catch (Exception e) {
			e.printStackTrace();
			return "Erro ao salvar Valor de indicador: " + valorIndicador;
		}
	}
}