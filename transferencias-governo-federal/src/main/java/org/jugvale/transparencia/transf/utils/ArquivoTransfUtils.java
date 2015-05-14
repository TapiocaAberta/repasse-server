package org.jugvale.transparencia.transf.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;

public class ArquivoTransfUtils {
	
	public static ZipFile criaZip(byte[] binario) throws IOException {
		File tempZip = Files.createTempFile("carga", ".zip").toFile();
		IOUtils.write(binario, new FileOutputStream(tempZip));	
		return new ZipFile(tempZip);
	} 
	
	public static Path pegaCSV(ZipFile arquivoZip) throws IOException {
		ZipEntry entryCSV = arquivoZip.entries().nextElement();
		String dadosData = entryCSV.toString().substring(0, 6);
		Path arquivoCSV = Files.createTempFile(dadosData, ".csv");
		IOUtils.copy(arquivoZip.getInputStream(entryCSV), new FileWriter(arquivoCSV.toFile()));		
		return arquivoCSV;		
	}
	
	public static Path deszipaECriaCSV(byte[] binario) throws IOException {
		ZipFile arquivoZip = criaZip(binario);
		Path arquivoCSV = pegaCSV(arquivoZip);
		System.out.println(arquivoCSV);
		Files.delete(Paths.get(arquivoZip.getName()));
		return arquivoCSV;
	}

}
