package org.sjcdigital.repasse.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;

public class ArquivoTransfUtils {

    public static final String URL_TRANSFERENCIA_BASE = "http://www.portaltransparencia.gov.br/download-de-dados/transferencias/%d%02d";
    /**
     * Cache para a contagem de linhas e assim evitarmos muitos downloads. TODO: usar infinispan invés disso
     */
    private static final Map<String, Long> cacheContagemLinhas;

    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");

    static {
        cacheContagemLinhas = new HashMap<>();
    }

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
        Path arquivoCSV = createTempFile("repasses" + dadosData + ".csv");
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
        var urlArquivoZip = new URL(String.format(URL_TRANSFERENCIA_BASE, ano,
                                                  mes));
        var tempZip = createTempFile("carga_" + ano + "_" + mes + ".zip").toFile();
        var stream = urlArquivoZip.openStream();
        IOUtils.copy(stream, new FileOutputStream(tempZip));
        try {
            stream.close();
        } catch (IOException e) {
            System.out.println("ERROR AO FECHAR CONEXÃO: " + e.getMessage());
            throw e;
        }
        var arquivoCSV = pegaCSV(new ZipFile(tempZip));
        Files.delete(Paths.get(tempZip.getPath()));
        return arquivoCSV;
    }

    /**
     * 
     * Simplesmente irá contar a quantidade de linhas que têm no arquivo do site
     * de transparência para referência
     * 
     * @param ano
     * @param mes
     * @return
     * @throws IOException
     */
    public static long contaLinhasdoSite(int ano, int mes) throws IOException {
        return contaLinhasdoSite(ano, mes, baixaDeszipaECriaCSV(ano, mes));
    }

    public static long contaLinhasdoSite(int ano, int mes, Path csv) throws IOException {
        String chave = ano + "-" + mes;
        if (cacheContagemLinhas.containsKey(chave)) {
            return cacheContagemLinhas.get(chave);
        }
        long totalLinhas = Files.lines(csv).count() - 1;
        Files.delete(csv);
        cacheContagemLinhas.put(chave, totalLinhas);
        return totalLinhas;

    }

    private static Path createTempFile(String name) throws IOException {
        var path = Paths.get(TEMP_DIR, name);
        if (path.toFile().exists()) {
            Files.delete(path);
        }
        Files.createFile(path);
        return path;
    }

}
