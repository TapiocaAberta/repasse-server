package org.jugvale.transfgov.utils;

import java.text.Normalizer;

public class TextoUtils {

	public static String transformaNomeCidade(String original) {
		return Normalizer.normalize(original, Normalizer.Form.NFD)
				.replaceAll("\\p{M}", "").trim().toUpperCase();
	}

}
