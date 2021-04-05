package org.sjcdigital.repasse.carga.transferencia;

import java.text.ParseException;
import java.util.Optional;

import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;

import org.sjcdigital.repasse.carga.transferencia.qualifiers.TransferenciaTransformerAntigo;
import org.sjcdigital.repasse.model.base.Area;
import org.sjcdigital.repasse.model.base.Estado;
import org.sjcdigital.repasse.model.base.Municipio;
import org.sjcdigital.repasse.model.transferencia.Acao;
import org.sjcdigital.repasse.model.transferencia.Favorecido;
import org.sjcdigital.repasse.model.transferencia.Programa;
import org.sjcdigital.repasse.model.transferencia.SubFuncao;
import org.sjcdigital.repasse.model.transferencia.Transferencia;
import org.sjcdigital.repasse.utils.TextoUtils;

import static javax.transaction.Transactional.TxType.REQUIRES_NEW;

/**
 * Transformer que funcionava até início de 2018.
 */
@RequestScoped
@TransferenciaTransformerAntigo
public class LinhaTransferenciaTransformerAntigo implements LinhaTransferenciaTransformer {

    private static final String DIVISOR_CSV = "\t";

    /**
     * 
     * Irá pegar cada dado do CSV e salvar no banco de dados. Campos do CSV: --
     * Sigla Unidade Federação Codigo SIAFI Municipio Nome Municipio Codigo
     * Funcao Nome Funcao Codigo Sub Funcao Nome Sub Funcao Codigo Programa Nome
     * Programa Codigo Acao Nome Acao Linguagem Cidadã Codigo Favorecido Nome
     * Favorecido Fonte-Finalidade Modalidade Aplicação Número Convênio Valor
     * Parcela --
     * 
     * @param linha
     * @throws Exception
     * @return Optional 
     *
     */
    @Override
    @Transactional(REQUIRES_NEW)
    public Optional<Transferencia> transformaLinha(int ano, int mes, String linha) {
        String[] campos = linha.split(DIVISOR_CSV);
        if (campos.length != 18) {
            return Optional.empty();
        }
        String siglaEstado = campos[0];
        String siafiMunicipio = campos[1];
        String nomeMunicipio = campos[2];
        // move transferências do FUNDEB(nome popular da ação) para a área de educação (ID 12 até a data de JUL/2015)
        long codigoFuncao = campos[11].equals("FUNDEB") ? 12 : Long.parseLong(campos[3]);
        // renomear area de "Encargos Especiais" para "Uso Geral"
        String nomeFuncao = campos[7].toUpperCase().equals("ENCARGOS ESPECIAIS") ? "Uso Geral" : campos[7];
        long codigoSubFuncao = Long.parseLong(campos[5]);
        String nomeSubFuncao = campos[6];
        long codigoPrograma = Long.parseLong(campos[7]);
        String nomePrograma = campos[8];
        String codigoAcao = campos[9];
        String nomeAcao = campos[10];
        String nomePopular = campos[11];
        String codigoFavorecido = campos[12];
        String nomeFavorecido = campos[13];
        float valor;
        try {
            valor = TextoUtils.ptBrFloat(campos[17]);
        } catch (ParseException e) {
            e.printStackTrace();
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

}
