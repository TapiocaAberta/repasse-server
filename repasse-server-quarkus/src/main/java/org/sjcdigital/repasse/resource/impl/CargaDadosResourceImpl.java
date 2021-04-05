package org.sjcdigital.repasse.resource.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartInput;
import org.sjcdigital.repasse.carga.CargaDadosGeograficosController;
import org.sjcdigital.repasse.carga.CargaDadosPopController;
import org.sjcdigital.repasse.carga.CargaDadosTransfController;
import org.sjcdigital.repasse.carga.CargaIDHController;
import org.sjcdigital.repasse.carga.CargaIndicadorController;
import org.sjcdigital.repasse.carga.RequisicaoCarga;
import org.sjcdigital.repasse.model.base.AnoMes;
import org.sjcdigital.repasse.model.carga.DadosCargaIndicador;
import org.sjcdigital.repasse.model.carga.ResumoDadosTransferencia;
import org.sjcdigital.repasse.model.transferencia.CargaTransfInfo;
import org.sjcdigital.repasse.resource.CargaDadosResource;
import org.sjcdigital.repasse.service.AnoService;
import org.sjcdigital.repasse.service.impl.CargaTransfInfoService;
import org.sjcdigital.repasse.service.impl.TransferenciaService;
import org.sjcdigital.repasse.utils.ArquivoTransfUtils;

import static javax.transaction.Transactional.TxType.NEVER;

/**
 * 
 * Interface REST para permitir a carga de dados
 * 
 * @author wsiqueir
 *
 */
@Transactional(NEVER)
public class CargaDadosResourceImpl implements CargaDadosResource {

    @Inject
    TransferenciaService transferenciaService;

    @Inject
    Logger logger;

    @Inject
    CargaDadosTransfController cargaDadosController;

    @Inject
    CargaDadosPopController cargaDadosPopController;

    @Inject
    CargaTransfInfoService cargaTransfInfoService;

    @Inject
    CargaIDHController cargaIDHController;

    @Inject
    AnoService anoService;

    @Inject
    CargaDadosGeograficosController cargaMicroRegiaoController;

    @Inject
    CargaIndicadorController cargaIndicadorController;

    @Inject
    Event<DadosCargaIndicador> dadosCargaIndicadorEvent;

    @Inject
    Event<RequisicaoCarga> requisicaoCargaEvent;

    public Response baixaECarrega(int ano, int mes) throws IOException {
        verificaSeJaFoiCarregado(ano, mes);
        java.nio.file.Path arquivoCSV = null;
        try {
            logger.info(String.format(MSG_BAIXANDO_INICIO, ano, mes));
            arquivoCSV = ArquivoTransfUtils.baixaDeszipaECriaCSV(ano, mes);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                           .entity(MSG_ERRO_BAIXAR).build();
        }
        logger.info(String.format(MSG_SUCESSO_AO_BAIXAR, ano, mes));
        requisicaoCargaEvent.fireAsync(new RequisicaoCarga(ano, mes, arquivoCSV));
        return Response.ok(String.format(MSG_SUCESSO, ano, mes, ano, mes))
                       .build();
    }

    /**
     * Código macarrão para ler o zip de transferencia do site
     * www.portaldatransparencia.gov.br
     * 
     * @param multipart
     * @return
     * @throws IOException
     */
    public Response recebeCarga(MultipartInput conteudo) throws IOException {
        int ano = 0, mes = 0;
        InputPart conteudoZip = null;
        for (InputPart p : conteudo.getParts()) {
            if (p.getMediaType().toString()
                 .equals(MediaType.APPLICATION_OCTET_STREAM)) {
                conteudoZip = p;
            }
        }
        Objects.requireNonNull(conteudoZip, "Você deve enviar um anexo");
        java.nio.file.Path arquivoCSV = ArquivoTransfUtils
                                                          .deszipaECriaCSV(conteudoZip.getBody(byte[].class, null));
        String dadosData = arquivoCSV.toFile().getName().substring(0, 6);
        ano = Integer.parseInt(dadosData.substring(0, 4));
        mes = Integer.parseInt(dadosData.substring(4, 6));
        verificaSeJaFoiCarregado(ano, mes);
        requisicaoCargaEvent.fireAsync(new RequisicaoCarga(ano, mes, arquivoCSV));
        return Response.ok(String.format(MSG_SUCESSO, ano, mes, ano, mes))
                       .build();
    }

    private void verificaSeJaFoiCarregado(int ano, int mes) {
        if (transferenciaService.temTranferencia(ano, mes))
            throw new WebApplicationException(Response.status(Status.CONFLICT)
                                                      .entity(String.format(MSG_CARGA_REPETIDA, ano, mes))
                                                      .build());
    }

    @Override
    public CargaTransfInfo recuperaInfoCarga(int ano, int mes) {
        return cargaTransfInfoService.porAnoMes(ano, mes);
    }

    @Override
    public List<CargaTransfInfo> todasInfoCargas() {
        return cargaTransfInfoService.todos();
    }

    @Override
    public Response cargaDadosPop() {
        try {
            return Response
                           .ok(cargaDadosPopController.fazCargaDadosPopulacao())
                           .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(e.getMessage()).build();
        }

    }

    @Override
    public List<ResumoDadosTransferencia> resumoDadosTransferencia() {
        List<ResumoDadosTransferencia> resumos = new ArrayList<>();
        List<AnoMes> anos = anoService.anos();
        for (AnoMes anoMes : anos) {
            for (int mes : anoMes.getMeses()) {
                int ano = anoMes.getAno();
                ResumoDadosTransferencia resumo = new ResumoDadosTransferencia();
                resumo.setAno(ano);
                resumo.setMes(mes);
                resumo.setTotalTransferencias(transferenciaService
                                                                  .contaPorAnoMesMunicipio(ano, mes));
                try {
                    resumo.setContagemLinhas(ArquivoTransfUtils
                                                               .contaLinhasdoSite(ano, mes));
                } catch (IOException e) {
                    resumo.setContagemLinhas(-1);
                }
                resumos.add(resumo);
            }
        }
        return resumos;
    }

    @Override
    public Response cargaDadosIDH() {
        logger.info("Iniciando carga de dados do IDH");
        try {
            return Response.ok(cargaIDHController.fazCargaIDH()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @Override
    public Response cargaDadosRegiao() {
        logger.info("Iniciando carga de dados de Região");
        try {
            return Response.ok(cargaMicroRegiaoController.fazCargaDadosGeográficos())
                           .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @Override
    public Response cargaDadosIndicador(DadosCargaIndicador dadosCargaIndicador) {
        logger.info("Iniciando carga de dados de Indicador");
        dadosCargaIndicadorEvent.fireAsync(dadosCargaIndicador);
        return Response.ok("Carga de dados em andamento. Confira os logs").build();
    }

}