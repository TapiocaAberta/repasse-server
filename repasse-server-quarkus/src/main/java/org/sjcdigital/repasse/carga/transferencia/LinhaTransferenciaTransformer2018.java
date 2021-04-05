package org.sjcdigital.repasse.carga.transferencia;

import java.text.ParseException;
import java.util.Optional;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.sjcdigital.repasse.carga.transferencia.qualifiers.TransferenciaTransformer2018;
import org.sjcdigital.repasse.model.base.Area;
import org.sjcdigital.repasse.model.base.Estado;
import org.sjcdigital.repasse.model.base.Municipio;
import org.sjcdigital.repasse.model.transferencia.Acao;
import org.sjcdigital.repasse.model.transferencia.Favorecido;
import org.sjcdigital.repasse.model.transferencia.Programa;
import org.sjcdigital.repasse.model.transferencia.SubFuncao;
import org.sjcdigital.repasse.model.transferencia.Transferencia;
import org.sjcdigital.repasse.utils.TextoUtils;

/**
 * Transformer para dados de CSVs atualizados
 */
@RequestScoped
@TransferenciaTransformer2018
public class LinhaTransferenciaTransformer2018 implements LinhaTransferenciaTransformer {

    private static final int TOTAL_COLUMNS = 26;
    private static final int VALOR_POS = 25;
    private static final int NOME_FAVORECIDO_POS = 24;
    private static final int COD_FAVORECIDO_POS = 23;
    private static final int CODIGO_ACAO_POS = 14;
    private static final int NOME_ACAO_POS = 15;
    private static final int NOME_POP_ACAO_POS = 16;
    private static final int NOME_PROGRAMA_POS = 13;
    private static final int COD_PROGRAMA_POS = 12;
    private static final int NOME_SUB_FUNCAO_POS = 11;
    private static final int COD_SUB_FUNCAO_POS = 10;
    private static final int NOME_FUNCAO_POS = 9;
    private static final int COD_FUNCAO_POS = 8;
    private static final int NOME_MUN_POS = 5;
    private static final int SIAFI_MUN_POS = 4;
    private static final int SIGLA_ESTADO_POS = 3;

    // Precisa ser ajustado para colocar o FUNDEB sob Educação
    private static final int CODIGO_FUNCAO_EDUCACAO = 12;

    private static final String DIVISOR_CSV = ";";
    private static final long CODIGO_MU = 999999;

    @Inject
    Logger logger;

    // TODO: improve this parsing in future to make it column oriented, not based on columns positions
    @Override
    public Optional<Transferencia> transformaLinha(int ano, int mes, String linha) {
        String[] campos = linha.split(DIVISOR_CSV);
        if (campos.length != TOTAL_COLUMNS) {
            logger.fine("Ignoring bad row with " + campos.length + " columns, expected is " + TOTAL_COLUMNS);
            return Optional.empty();
        }
        for (int i = 0; i < campos.length; i++) {
            campos[i] = campos[i].replaceAll("\"", "");
        }
        var siglaEstado = campos[SIGLA_ESTADO_POS];
        var siafiMunicipio = campos[SIAFI_MUN_POS];
        var nomeMunicipio = campos[NOME_MUN_POS];
        // move transferências do FUNDEB(nome popular da ação) para a área de educação (ID 12 até a data de JUL/2015)
        // renomear area de "Encargos Especiais" para "Uso Geral"
        var nomeFuncao = campos[NOME_FUNCAO_POS].equalsIgnoreCase("ENCARGOS ESPECIAIS") ? "Uso Geral" : campos[NOME_FUNCAO_POS];
        var codigoSubFuncao = transformaCodigo(campos[COD_SUB_FUNCAO_POS]);
        var nomeSubFuncao = campos[NOME_SUB_FUNCAO_POS];
        var codigoPrograma = transformaCodigo(campos[COD_PROGRAMA_POS]);
        var nomePrograma = campos[NOME_PROGRAMA_POS];
        var codigoAcao = campos[CODIGO_ACAO_POS];
        var nomeAcao = campos[NOME_ACAO_POS];
        var nomePopular = campos[NOME_POP_ACAO_POS];
        var codigoFavorecido = campos[COD_FAVORECIDO_POS];
        var nomeFavorecido = campos[NOME_FAVORECIDO_POS];
        var codigoFuncao = nomePopular.equalsIgnoreCase("FUNDEB") ? CODIGO_FUNCAO_EDUCACAO : transformaCodigo(campos[COD_FUNCAO_POS]);
        float valor;
        try {
            valor = TextoUtils.ptBrFloat(campos[VALOR_POS]);
        } catch (ParseException e) {
            logger.warning("Error loading value for row. Value: " + campos[VALOR_POS]);
            return Optional.empty();
        }
        Transferencia transferencia = new Transferencia();
        transferencia.setAno(ano);
        transferencia.setMes(mes);
        transferencia.setAcao(new Acao(codigoAcao, nomeAcao, nomePopular));
        transferencia.setFavorecido(new Favorecido(nomeFavorecido,
                                                   codigoFavorecido));
        transferencia.setArea(new Area(
                                       codigoFuncao, nomeFuncao));
        transferencia.setMunicipio(new Municipio(siafiMunicipio,
                                                 nomeMunicipio,
                                                 new Estado(siglaEstado)));
        transferencia.setPrograma(new Programa(codigoPrograma, nomePrograma));
        transferencia.setSubFuncao(new SubFuncao(codigoSubFuncao,
                                                 nomeSubFuncao));
        transferencia.setValor(valor);
        return Optional.of(transferencia);
    }

    private Long transformaCodigo(String codigoStr) {
        return codigoStr.equalsIgnoreCase("MU") ? CODIGO_MU : Long.parseLong(codigoStr);
    }

}
