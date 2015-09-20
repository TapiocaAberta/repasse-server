package org.jugvale.transfgov.utils;

import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class TextoUtils {
	
	static private NumberFormat nfPtBr;
	
	static {
		Locale ptBr = new Locale("pt", "BR");
		nfPtBr = NumberFormat.getNumberInstance(ptBr);
	}

	public static String transformaNomeCidade(String original) {
		return Normalizer.normalize(original, Normalizer.Form.NFD)
				.replaceAll("\\p{M}", "").trim().toUpperCase();
	}
	
	public static float ptBrFloat(String valor) throws ParseException {
		return nfPtBr.parse(valor).floatValue();
	}

}
