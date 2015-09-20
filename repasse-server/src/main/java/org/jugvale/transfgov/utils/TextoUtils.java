package org.jugvale.transfgov.utils;

import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TextoUtils {

	static private NumberFormat nfPtBr;
	static private Map<String, String> ufIDH;

	static {
		Locale ptBr = new Locale("pt", "BR");
		nfPtBr = NumberFormat.getNumberInstance(ptBr);
		ufIDH = new HashMap<>();
		// dados do atlas
		ufIDH.put("12", "AC");
		ufIDH.put("27", "AL");
		ufIDH.put("16", "AP");
		ufIDH.put("13", "AM");
		ufIDH.put("29", "BA");
		ufIDH.put("23", "CE");
		ufIDH.put("53", "DF");
		ufIDH.put("32", "ES");
		ufIDH.put("52", "GO");
		ufIDH.put("21", "MA");
		ufIDH.put("51", "MT");
		ufIDH.put("50", "MS");
		ufIDH.put("31", "MG");
		ufIDH.put("15", "PA");
		ufIDH.put("25", "PB");
		ufIDH.put("41", "PR");
		ufIDH.put("26", "PE");
		ufIDH.put("22", "PI");
		ufIDH.put("33", "RJ");
		ufIDH.put("24", "RN");
		ufIDH.put("43", "RS");
		ufIDH.put("11", "RO");
		ufIDH.put("14", "RR");
		ufIDH.put("42", "SC");
		ufIDH.put("35", "SP");
		ufIDH.put("28", "SE");
		ufIDH.put("17", "TO");
	}

	/**
	 * 
	 * Converte uma cidade escrita normalmente para caixa alta sem acentos (de
	 * acordo com o padrão do arquivo de transferência)
	 * 
	 * @param original
	 * @return
	 */
	public static String transformaNomeCidade(String original) {
		return Normalizer.normalize(original, Normalizer.Form.NFD)
				.replaceAll("\\p{M}", "").trim().toUpperCase();
	}

	/**
	 * 
	 * Realiza a conversão de uma String para Float usando o locale do Brasil
	 * 
	 * @param valor
	 * @return
	 * @throws ParseException
	 */
	public static float ptBrFloat(String valor) throws ParseException {
		return nfPtBr.parse(valor).floatValue();
	}

	/**
	 * 
	 * O arquivo do Atlas para o IDH usa um código para as UFs. Essa função faz
	 * a correspondência do código para a UF usando o mapa interno
	 * 
	 * @param codigo
	 * @return
	 */
	public static String codParaUF(String codigo) {
		return ufIDH.get(codigo);
	}

}
