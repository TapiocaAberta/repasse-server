package org.jugvale.transfgov.carga.transferencia;

import java.text.ParseException;
import java.util.Optional;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.jugvale.transfgov.carga.transferencia.qualifiers.TransferenciaTransformer2018;
import org.jugvale.transfgov.model.base.Area;
import org.jugvale.transfgov.model.base.Estado;
import org.jugvale.transfgov.model.base.Municipio;
import org.jugvale.transfgov.model.transferencia.Acao;
import org.jugvale.transfgov.model.transferencia.Favorecido;
import org.jugvale.transfgov.model.transferencia.Programa;
import org.jugvale.transfgov.model.transferencia.SubFuncao;
import org.jugvale.transfgov.model.transferencia.Transferencia;
import org.jugvale.transfgov.service.impl.AcaoService;
import org.jugvale.transfgov.service.impl.AreaService;
import org.jugvale.transfgov.service.impl.EstadoService;
import org.jugvale.transfgov.service.impl.FavorecidoService;
import org.jugvale.transfgov.service.impl.FonteFinalidadeService;
import org.jugvale.transfgov.service.impl.MunicipioService;
import org.jugvale.transfgov.service.impl.ProgramaService;
import org.jugvale.transfgov.service.impl.SubFuncaoService;
import org.jugvale.transfgov.service.impl.TransferenciaService;
import org.jugvale.transfgov.utils.TextoUtils;

/**
 * Transformer para dados de CSVs atualizados
 */
@Stateless
@TransferenciaTransformer2018
public class LinhaTransferenciaTransformer2018 implements LinhaTransferenciaTransformer {
    
    private static final int TOTAL_COLUMNS = 26;
    private static final int VALOR_POS = 25;
    private static final int NOME_FAVORECIDO_POS = 24;
    private static final int COD_FAVORECIDO_POS = 23;
    private static final int CODIGO_ACAO_POS = 14;
    private static final int NOME_ACAO_POS = 15;
    private static final int NOME_PROGRAMA_POS = 13;
    private static final int COD_PROGRAMA_POS = 12;
    private static final int NOME_SUB_FUNCAO_POS = 11;
    private static final int COD_SUB_FUNCAO_POS = 10;
    private static final int NOME_FUNCAO_POS = 9;
    private static final int COD_FUNCAO_POS = 8;
    private static final int NOME_POP_FUNCAO_POS = 14;
    private static final int NOME_MUN_POS = 5;
    private static final int SIAFI_MUN_POS = 4;
    private static final int SIGLA_ESTADO_POS = 3;

    private static final String DIVISOR_CSV = ";";
    private static final long CODIGO_MU = 999999;

    @Inject
    MunicipioService municipioService;

    @Inject
    AcaoService acaoService;

    @Inject
    EstadoService estadoService;

    @Inject
    AreaService areaService;

    @Inject
    SubFuncaoService subFuncaoService;

    @Inject
    ProgramaService programaService;

    @Inject
    FonteFinalidadeService fonteFinalidadeService;

    @Inject
    FavorecidoService favorecidoService;

    @Inject
    private TransferenciaService transferenciaService;
    
    @Inject
    Logger logger;

    // TODO: improve this parsing in future to make it column oriented, not based on columns positions
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Optional<Transferencia> transformaLinha(int ano, int mes, String linha) {
        String[] campos = linha.split(DIVISOR_CSV);
        if (campos.length != TOTAL_COLUMNS) {
            logger.fine("Ignoring bad row with " + campos.length + " columns, expected is " + TOTAL_COLUMNS);
            return Optional.empty();
        }
        for (int i = 0; i < campos.length; i++) {
            campos[i] = campos[i].replaceAll("\"", "");
        }
        String siglaEstado = campos[SIGLA_ESTADO_POS];
        String siafiMunicipio = campos[SIAFI_MUN_POS];
        String nomeMunicipio = campos[NOME_MUN_POS];
        // move transferências do FUNDEB(nome popular da ação) para a área de educação (ID 12 até a data de JUL/2015)
        long codigoFuncao = campos[NOME_POP_FUNCAO_POS].equalsIgnoreCase("FUNDEB")? 12 :transformaCodigo(campos[COD_FUNCAO_POS]);
        // renomear area de "Encargos Especiais" para "Uso Geral"
        String nomeFuncao = campos[NOME_FUNCAO_POS].equalsIgnoreCase("ENCARGOS ESPECIAIS")? "Uso Geral" : campos[NOME_FUNCAO_POS];
        long codigoSubFuncao = transformaCodigo(campos[COD_SUB_FUNCAO_POS]);
        String nomeSubFuncao = campos[NOME_SUB_FUNCAO_POS];
        long codigoPrograma =  transformaCodigo(campos[COD_PROGRAMA_POS]);
        String nomePrograma = campos[NOME_PROGRAMA_POS];
        String codigoAcao = campos[CODIGO_ACAO_POS];
        String nomeAcao = campos[NOME_ACAO_POS];
        String nomePopular = campos[NOME_POP_FUNCAO_POS];
        String codigoFavorecido = campos[COD_FAVORECIDO_POS];
        String nomeFavorecido = campos[NOME_FAVORECIDO_POS];
        float valor;
        try {
            valor = TextoUtils.ptBrFloat(campos[VALOR_POS]);
        } catch (ParseException e) {
            logger.warning("Error loading value for row. Value: " + campos[VALOR_POS]);
            
            return Optional.empty();
        }
        Estado estado = estadoService.buscaEstadoPorSiglaOuCria(new Estado(siglaEstado));
        
        Municipio municipio = municipioService.porEstadoNomeESIAFIOuCria(new Municipio(siafiMunicipio, 
                        nomeMunicipio, 
                        estado));
        Area area = areaService.buscaPorIdOuCria(codigoFuncao, new Area(
                codigoFuncao, nomeFuncao));
        SubFuncao subFuncao = subFuncaoService.buscaPorIdOuCria(
                codigoSubFuncao, () -> new SubFuncao(codigoSubFuncao,
                        nomeSubFuncao));
        Programa programa = programaService.buscaPorIdOuCria(codigoPrograma,
                () -> new Programa(codigoPrograma, nomePrograma));
        Acao acao = acaoService.buscaPorCodigoOuCria(codigoAcao,
                () -> new Acao(codigoAcao, nomeAcao, nomePopular));
        Favorecido favorecido = favorecidoService.buscaPorCodigoOuCria(
                codigoFavorecido, () -> new Favorecido(nomeFavorecido,
                        codigoFavorecido));
        Transferencia transferencia = new Transferencia();
        transferencia.setAno(ano);
        transferencia.setMes(mes);
        transferencia.setAcao(acao);
        transferencia.setFavorecido(favorecido);
        transferencia.setArea(area);
        transferencia.setMunicipio(municipio);
        transferencia.setPrograma(programa);
        transferencia.setSubFuncao(subFuncao);
        transferencia.setValor(valor);
        transferenciaService.salvar(transferencia);
        return Optional.of(transferencia);
    }

    private Long transformaCodigo(String codigoStr) {
        return codigoStr.equalsIgnoreCase("MU") ? CODIGO_MU : Long.parseLong(codigoStr);
    }

}
