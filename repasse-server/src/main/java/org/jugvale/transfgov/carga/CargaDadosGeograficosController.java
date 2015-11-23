package org.jugvale.transfgov.carga;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.io.IOUtils;
import org.jugvale.transfgov.model.base.Municipio;
import org.jugvale.transfgov.service.impl.MunicipioService;
import org.jugvale.transfgov.utils.TextoUtils;

@Stateless
public class CargaDadosGeograficosController {

	final String ARQUIVOS_REGIAO[] = { "/dados/divisao_regioes.csv" };

	final String SEPARADOR = ";";

	@Inject
	MunicipioService municipioService;

	@Inject
	Logger logger;

	@PersistenceContext
	private EntityManager em;

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public String fazCargaDadosGeográficos() throws IOException {
		logger.info("Iniciando carga de dados geográficos dos municípios");
		StringBuffer relatorioFinal = new StringBuffer("Carga iniciada em: "
				+ new Date());
		ArrayList<String> naoEncontrados = new ArrayList<>();
		for (int i = 0; i < ARQUIVOS_REGIAO.length; i++) {
			// Não funciona para WAR; URL também não
			// String url =
			// getClass().getResource(ARQUIVOS_REGIAO[i]).getFile();
			InputStream is = getClass().getResourceAsStream(ARQUIVOS_REGIAO[i]);
			Path arquivo = Files.createTempFile("regiao", "");
			Files.write(arquivo, IOUtils.toByteArray(is));
			Files.lines(arquivo)
					.skip(1)
					.forEach(l -> {
						// NomeMun SiglaUF DIVUR_N3
							String[] col = l.split(SEPARADOR);
							String nomeMun = TextoUtils
									.transformaNomeCidade(col[0]);
							String uf = col[1];
							String regiao = col[2];
							float lat = 0, lon = 0;
							try {
								lat = TextoUtils.ptBrFloat(col[3]);
								lon = TextoUtils.ptBrFloat(col[4]);
								logger.info(nomeMun + " - " + lat + " - " + lon);
							} catch (Exception e1) {	
								logger.info("Sem latitude/longitude para " + nomeMun);
							}
							logger.info("Buscando município: " + nomeMun + " - " + uf
									+ ". Região: " + regiao);
							try {
								buscaESalvaMunicipio(nomeMun, uf, regiao, lat, lon);
							} catch (Exception e) {
								e.printStackTrace();
								naoEncontrados.add("\"" + nomeMun + "\" - \""
										+ uf + "\"");
							}
						});
			Files.delete(arquivo);
			relatorioFinal
					.append("<br />erro ao inserir dados de região para os seguintes municipio: (provavelmente por incompatibilidades entre os dados, veja os logs): <br />");
			relatorioFinal.append(naoEncontrados.stream().collect(
					Collectors.joining("<br/>")));
			logger.info("Fim carga de dados das regiões dos municípios");
		}
		return relatorioFinal.toString();

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private Municipio buscaESalvaMunicipio(String nomeMun, String uf, String regiao, float lat, float lon) {
		Municipio m = municipioService.buscaPorNomeEEstado(uf, nomeMun);
		// deve ser atualizado diretamente no banco, pois está no cache
		municipioService.atualizaDadosGeograficos(m.getId(), regiao, lat, lon);
		return m;
	}

}