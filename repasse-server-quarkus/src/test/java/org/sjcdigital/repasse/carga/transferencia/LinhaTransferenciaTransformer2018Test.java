package org.sjcdigital.repasse.carga.transferencia;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LinhaTransferenciaTransformer2018Test {

    final String EXEMPLO_LINHA = "\"201401\";" +
                                 "\"Constitucionais e Royalties\";" +
                                 "\"Administração Pública Municipal\";" +
                                 "\"SP\";" +
                                 "\"7099\";" +
                                 "\"SAO JOSE DOS CAMPOS\";" +
                                 "\"-1\";" +
                                 "\"Sem informação\";" +
                                 "\"28\";" +
                                 "\"Encargos especiais\";" +
                                 "\"845\";" +
                                 "\"Outras transferências\";" +
                                 "\"0903\";" +
                                 "\"OPERACOES ESPECIAIS: TRANSFERENCIAS CONSTITUCIONAIS E AS DEC\";" +
                                 "\"0E25\";" +
                                 "\"AUXILIO FINANCEIRO AOS ESTADOS, AO DISTRITO FEDERAL E AOS MUNICIPIOS PARA O FOMENTO DAS EXPORTACOES\";" +
                                 "\"Sem informação\";" +
                                 "\"-1\";" +
                                 "\"Sem informação" +
                                 "\";\"-1\";" +
                                 "\"Sem informação\";" +
                                 "\"-1\";" +
                                 "\"Sem informação\";" +
                                 "\"46643466000106\";" +
                                 "\"MUNICIPIO DE SAO JOSE DOS CAMPOS\";" +
                                 "\"481552,07\"";

    @Test
    public void testTransformacaoLinha() {
        var transformer = new LinhaTransferenciaTransformer2018();
        var transf = transformer.transformaLinha(2014, 1, EXEMPLO_LINHA).get();

        assertEquals(481552.07, transf.getValor(), 0.01);
        assertEquals("SP", transf.getMunicipio().getEstado().getSigla());
        assertEquals("SAO JOSE DOS CAMPOS", transf.getMunicipio().getNome());
        assertEquals(28, transf.getArea().getId());
        assertEquals("Uso Geral", transf.getArea().getNome());
        assertEquals(845, transf.getSubFuncao().getId());
        assertEquals("Outras transferências", transf.getSubFuncao().getNome());
        assertEquals(903, transf.getPrograma().getId());
        assertEquals("OPERACOES ESPECIAIS: TRANSFERENCIAS CONSTITUCIONAIS E AS DEC", transf.getPrograma().getNome());
        assertEquals("0E25", transf.getAcao().getCodigo());
        assertEquals("AUXILIO FINANCEIRO AOS ESTADOS, AO DISTRITO FEDERAL E AOS MUNICIPIOS PARA O FOMENTO DAS EXPORTACOES", transf.getAcao().getNome());
        assertEquals("Sem informação", transf.getAcao().getNomePopular());
        assertEquals("46643466000106", transf.getFavorecido().getCodigo());
        assertEquals("MUNICIPIO DE SAO JOSE DOS CAMPOS", transf.getFavorecido().getNome());
    }

}
