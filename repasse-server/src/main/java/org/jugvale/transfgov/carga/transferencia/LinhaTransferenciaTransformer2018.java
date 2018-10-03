package org.jugvale.transfgov.carga.transferencia;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Optional;

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
 * Transformer para dados a partir de 2018
 */
@Stateless
@TransferenciaTransformer2018
public class LinhaTransferenciaTransformer2018 implements LinhaTransferenciaTransformer{

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

    // TODO: improve this parsing in future to make it column oriented, not based on columns positions
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Optional<Transferencia> transformaLinha(int ano, int mes, String linha) {
        String[] campos = linha.split(DIVISOR_CSV);
        if (campos.length != 18) {
            return Optional.empty();
        }
        for (int i = 0; i < campos.length; i++) {
            campos[i] = campos[i].replaceAll("\"", "");
        }
        String siglaEstado = campos[3];
        String siafiMunicipio = campos[4];
        String nomeMunicipio = campos[5];
        // move transferências do FUNDEB(nome popular da ação) para a área de educação (ID 12 até a data de JUL/2015)
        long codigoFuncao = campos[14].equalsIgnoreCase("FUNDEB")? 12 :transformaCodigo(campos[6]);
        // renomear area de "Encargos Especiais" para "Uso Geral"
        String nomeFuncao = campos[7].equalsIgnoreCase("ENCARGOS ESPECIAIS")? "Uso Geral" : campos[7];
        long codigoSubFuncao = transformaCodigo(campos[8]);
        String nomeSubFuncao = campos[9];
        long codigoPrograma =  transformaCodigo(campos[10]);
        String nomePrograma = campos[11];
        String codigoAcao = campos[12];
        String nomeAcao = campos[13];
        String nomePopular = campos[14];
        String codigoFavorecido = campos[15];
        String nomeFavorecido = campos[16];
        float valor;
        try {
            valor = TextoUtils.ptBrFloat(campos[17]);
        } catch (ParseException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        Estado estado = estadoService.buscaEstadoPorSiglaOuCria(siglaEstado,
                () -> new Estado(siglaEstado));
        Municipio municipio = municipioService.porEstadoNomeESIAFIOuCria(
                estado, nomeMunicipio, siafiMunicipio, () -> new Municipio(
                        siafiMunicipio, nomeMunicipio, estado));
        Area area = areaService.buscaPorIdOuCria(codigoFuncao, () -> new Area(
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
        return codigoStr.equalsIgnoreCase("MU")? CODIGO_MU: Long.parseLong(codigoStr);
    }    

}
