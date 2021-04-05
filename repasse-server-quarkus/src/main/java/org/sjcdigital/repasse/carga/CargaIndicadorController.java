package org.sjcdigital.repasse.carga;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.ObservesAsync;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import org.sjcdigital.repasse.model.base.Area;
import org.sjcdigital.repasse.model.base.Municipio;
import org.sjcdigital.repasse.model.carga.DadosCargaIndicador;
import org.sjcdigital.repasse.model.carga.LinhaCargaIndicador;
import org.sjcdigital.repasse.model.indicador.FocoIndicador;
import org.sjcdigital.repasse.model.indicador.GrupoIndicador;
import org.sjcdigital.repasse.model.indicador.Indicador;
import org.sjcdigital.repasse.model.indicador.ValorIndicador;
import org.sjcdigital.repasse.service.impl.AreaService;
import org.sjcdigital.repasse.service.impl.FocoIndicadorService;
import org.sjcdigital.repasse.service.impl.GrupoIndicadorService;
import org.sjcdigital.repasse.service.impl.IndicadorService;
import org.sjcdigital.repasse.service.impl.MunicipioService;
import org.sjcdigital.repasse.service.impl.ValorIndicadorService;
import org.sjcdigital.repasse.utils.TextoUtils;

import static javax.transaction.Transactional.TxType.NEVER;
import static javax.transaction.Transactional.TxType.REQUIRES_NEW;

@RequestScoped
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
	@Transactional(NEVER)
	public void carregaIndicadores(@ObservesAsync DadosCargaIndicador carga) {
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
	@Transactional(REQUIRES_NEW)
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