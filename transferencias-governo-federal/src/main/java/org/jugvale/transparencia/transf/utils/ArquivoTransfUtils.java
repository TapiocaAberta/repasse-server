package org.jugvale.transparencia.transf.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;

public class ArquivoTransfUtils {

	public static final String URL_TRANSFERENCIA_BASE = "http://arquivos.portaldatransparencia.gov.br/downloads.asp?a=%d&m=%s&consulta=Transferencias";

	/**
	 * 
	 * Cria um arquivo zip de transferências com os dados binários passados
	 * 
	 * @param binario
	 *            Os dados binários
	 * 
	 * @return O arquivo ZIP
	 * @throws IOException
	 */
	public static ZipFile criaZip(byte[] binario) throws IOException {
		File tempZip = Files.createTempFile("carga", ".zip").toFile();
		IOUtils.write(binario, new FileOutputStream(tempZip));
		return new ZipFile(tempZip);
	}

	/**
	 * 
	 * Retira o CSV do arquivo ZIP passado
	 * 
	 * @param arquivoZip
	 *            O arquivo ZIP que contém o CSV
	 * @return O arquivo temporário com o CSV
	 * @throws IOException
	 */
	public static Path pegaCSV(ZipFile arquivoZip) throws IOException {
		ZipEntry entryCSV = arquivoZip.entries().nextElement();
		String dadosData = entryCSV.toString().substring(0, 6);
		Path arquivoCSV = Files.createTempFile(dadosData, ".csv");
		IOUtils.copy(arquivoZip.getInputStream(entryCSV), new FileWriter(
				arquivoCSV.toFile()), StandardCharsets.ISO_8859_1);
		return arquivoCSV;
	}

	/**
	 * 
	 * Faz o processo de realizar o unzip do arquivo binário passado na forma de
	 * bytes e retorna o CSV
	 * 
	 * @param binario
	 *            Os dados binários
	 * @return O arquivo CSV
	 * @throws IOException
	 */
	public static Path deszipaECriaCSV(byte[] binario) throws IOException {
		ZipFile arquivoZip = criaZip(binario);
		Path arquivoCSV = pegaCSV(arquivoZip);
		Files.delete(Paths.get(arquivoZip.getName()));
		return arquivoCSV;
	}

	/**
	 * Baixa os dados do portal da transparência, deszipa e cria o CSV
	 * 
	 * @param ano
	 *            Ano
	 * @param mes
	 *            Mes
	 * @return Path do arquivo CSV
	 * @throws IOException 
	 */
	public static Path baixaDeszipaECriaCSV(int ano, int mes) throws IOException {
		String mesStr = String.valueOf(mes);
		if (mesStr.length() == 1)
			mesStr = "0" + mesStr;
		URL urlArquivoZip = new URL(String.format(URL_TRANSFERENCIA_BASE, ano, mesStr));
		File tempZip = Files.createTempFile("carga", ".zip").toFile();
		IOUtils.copy(urlArquivoZip.openStream(), new FileOutputStream(tempZip));
		Path arquivoCSV = pegaCSV(new ZipFile(tempZip));
		Files.delete(Paths.get(tempZip.getPath()));
		return arquivoCSV;
	}

}
