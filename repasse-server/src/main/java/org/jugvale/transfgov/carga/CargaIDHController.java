package org.jugvale.transfgov.carga;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.jugvale.transfgov.model.base.DadosMunicipio;
import org.jugvale.transfgov.model.base.Municipio;
import org.jugvale.transfgov.service.impl.DadosMunicipioService;
import org.jugvale.transfgov.service.impl.MunicipioService;
import org.jugvale.transfgov.utils.TextoUtils;

/**
 * 
 * Realiza a carga dos dados de IDH. A fonte dos dados é o Atlas Brasil
 * 
 * TODO: Seria possível realizar um merge das classes de controller para fazer um código melhor
 * 
 * @author wsiqueir
 *
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class CargaIDHController {

	final String ARQUIVOS_IDH[] = { "/dados/idh_1991_2014.csv" };
	
	final String SEPARADOR = ";";
	
	@Inject
	MunicipioService municipioService;
	
	@Inject
	DadosMunicipioService dadosMunicipioService;
	
	@Inject
	Logger logger;

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public String fazCargaIDH() throws IOException {
		logger.info("Iniciando carga de dados para população"); 
		StringBuffer relatorioFinal = new StringBuffer("Carga iniciada em: " + new Date());
		ArrayList<String> naoEncontrados = new ArrayList<>();
		for (int i = 0; i < ARQUIVOS_IDH.length; i++) {		
			String url = getClass().getResource(ARQUIVOS_IDH[i]).getFile();
			Path arquivo = Paths.get(url);
			Files.lines(arquivo).skip(1).forEach(l -> {
				// ANO	UF	Município	IDHM	IDHM_E	IDHM_L	IDHM_R
				String[] col = l.split(SEPARADOR);
				int ano = Integer.parseInt(col[0].trim());
				String uf = TextoUtils.codParaUF(col[1]);
				String nomeCidade = TextoUtils.transformaNomeCidade(col[2]);

				try {
					float idhm = TextoUtils.ptBrFloat(col[3]);
					float idhe = TextoUtils.ptBrFloat(col[4]);
					float idhl = TextoUtils.ptBrFloat(col[5]);
					float idhr = TextoUtils.ptBrFloat(col[6]);
					System.out.printf("%d %s %s %f %f %f %f", ano, uf, nomeCidade, idhm, idhe, idhr, idhl);
					insereDados(ano, uf, nomeCidade, idhm, idhe, idhr, idhl);
				} catch (Exception  e) {
					e.printStackTrace();
					naoEncontrados.add(ano + ": \"" + nomeCidade  +  "\" - \"" + uf + "\"" );
				}
			});
			relatorioFinal.append("<br />erro ao inserir dados de IDH para os seguintes municipio: (provavelmente por incompatibilidades entre os dados, veja os logs): <br />");
			relatorioFinal.append(naoEncontrados.stream().collect(Collectors.joining("<br/>")));
			logger.info("Fim carga de dados de IDH"); 
		}
		return relatorioFinal.toString();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void insereDados(int ano, String uf, String nomeMunicipio, float idhm, float idhe, float idhr, float idhl) {
			Municipio m  = municipioService.buscaPorNomeEEstado(uf, nomeMunicipio);
			DadosMunicipio dadosMunicipio = dadosMunicipioService.buscaPorAnoMunicipioOuCria(ano, m);
			dadosMunicipio.setIdhEducacao(idhe);
			dadosMunicipio.setIdhm(idhm);
			dadosMunicipio.setIdhLongevidade(idhl);
			dadosMunicipio.setIdhRenda(idhr);
			dadosMunicipioService.atualizar(dadosMunicipio);
	}
}