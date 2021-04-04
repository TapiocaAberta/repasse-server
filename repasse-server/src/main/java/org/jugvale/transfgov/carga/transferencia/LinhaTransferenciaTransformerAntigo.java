package org.jugvale.transfgov.carga.transferencia;

import java.text.ParseException;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.jugvale.transfgov.carga.transferencia.qualifiers.TransferenciaTransformerAntigo;
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
import org.jugvale.transfgov.utils.TextoUtils;

/**
 * Transformer que funcionava até início de 2018.
 */
@Stateless
@TransferenciaTransformerAntigo
public class LinhaTransferenciaTransformerAntigo implements LinhaTransferenciaTransformer{

    private static final String DIVISOR_CSV = "\t";
    
    
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
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Optional<Transferencia> transformaLinha(int ano, int mes, String linha) {
        String[] campos = linha.split(DIVISOR_CSV);
        if (campos.length != 18) {
            return Optional.empty();
        }
        String siglaEstado = campos[0];
        String siafiMunicipio = campos[1];
        String nomeMunicipio = campos[2];
        // move transferências do FUNDEB(nome popular da ação) para a área de educação (ID 12 até a data de JUL/2015)
        long codigoFuncao = campos[11].equals("FUNDEB")? 12 :Long.parseLong(campos[3]);
        // renomear area de "Encargos Especiais" para "Uso Geral"
        String nomeFuncao = campos[7].toUpperCase().equals("ENCARGOS ESPECIAIS")? "Uso Geral" : campos[7];
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
        Estado estado = estadoService.buscaEstadoPorSiglaOuCria(new Estado(siglaEstado));
        Municipio municipio = municipioService.porEstadoNomeESIAFIOuCria(new Municipio(
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
        return Optional.ofNullable(transferencia);
    }

}
