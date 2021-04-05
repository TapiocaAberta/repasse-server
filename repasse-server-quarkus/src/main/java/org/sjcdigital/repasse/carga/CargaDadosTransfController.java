package org.sjcdigital.repasse.carga;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.ObservesAsync;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.sjcdigital.repasse.carga.transferencia.LinhaTransferenciaTransformer;
import org.sjcdigital.repasse.carga.transferencia.qualifiers.TransferenciaTransformer2018;
import org.sjcdigital.repasse.model.transferencia.CargaTransfInfo;
import org.sjcdigital.repasse.model.transferencia.Transferencia;
import org.sjcdigital.repasse.service.impl.CargaTransfInfoService;
import org.sjcdigital.repasse.service.impl.TransferenciaService;

import static javax.transaction.Transactional.TxType.NEVER;
import static javax.transaction.Transactional.TxType.REQUIRES_NEW;

@RequestScoped
@Transactional(REQUIRES_NEW)
public class CargaDadosTransfController {

    @Inject
    Logger logger;

    @Inject
    CargaTransfInfoService cargaTransfInfoService;

    @Inject
    TransferenciaService transferenciaService;

    @Inject
    @TransferenciaTransformer2018
    LinhaTransferenciaTransformer transfTransformer;

    @Inject
    Event<NovaTransferenciaEvent> novaTransferenciaEvent;

    @Inject
    EntityManager em;

    /**
     * Irá carregar o arquivo passado no banco de dados. Linha a linha será
     * inserida.
     * 
     * @param arquivoCSV
     *            O arquivo para ser carregado
     * @throws IOException
     */
    @Transactional(NEVER)
    public void carregarNoBanco(@ObservesAsync RequisicaoCarga requisicao) throws IOException {
        var mes = requisicao.getMes();
        var ano = requisicao.getAno();
        var arquivoCSV = requisicao.getArquivoCSV();
        var totalSucesso = new AtomicInteger(0);
        var totalFalha = new AtomicInteger(0);
        var totalNaoProcessada = new AtomicInteger(0);
        logger.warning("Contando linhas de transferência para data " + mes + "/" + ano);
        long qtdeLinhas = Files.lines(arquivoCSV).count() - 1;

        logger.warning("Iniciando carga para data " + mes + "/" + ano);
        CargaTransfInfo cargaTransfInfo = cargaTransfInfoService
                                                                .porAnoMesOuCria(ano, mes, () -> new CargaTransfInfo(ano, mes));
        cargaTransfInfo.setInicio(new Date());
        cargaTransfInfo.setFim(null);
        cargaTransfInfo.setQtdeLinhas((int) qtdeLinhas);
        cargaTransfInfoService.atualizar(cargaTransfInfo);
        Files.lines(arquivoCSV, StandardCharsets.UTF_8)
             .skip(1)
             .forEach(linha -> processarLinha(ano, mes, totalSucesso, totalFalha, totalNaoProcessada, cargaTransfInfo, linha));
        Files.delete(arquivoCSV);
        cargaTransfInfo.setFim(new Date());
        cargaTransfInfo.setQtdeSucesso(totalSucesso.get());
        cargaTransfInfoService.atualizar(cargaTransfInfo);
        logger.warning("Carga para " + mes + "/" + ano + " terminada");
        limpaCacheHibernate();
    }

    private void processarLinha(int ano,
                                int mes,
                                AtomicInteger totalSucesso,
                                AtomicInteger totalFalha,
                                AtomicInteger totalNaoProcessada,
                                CargaTransfInfo cargaTransfInfo,
                                String linha) {
        try {
            Optional<Transferencia> transferenciaOp = transfTransformer.transformaLinha(ano, mes, linha);
            if (transferenciaOp.isPresent()) {
                totalSucesso.incrementAndGet();
                novaTransferenciaEvent.fireAsync(new NovaTransferenciaEvent(transferenciaOp.get()));
            } else {
                totalNaoProcessada.incrementAndGet();
            }
        } catch (Exception e) {
            totalFalha.incrementAndGet();
            logger.fine("Error: " + e.getMessage());
        }
        cargaTransfInfo
                       .setQtdeNaoProcessada(totalNaoProcessada
                                                               .get());
        cargaTransfInfo.setQtdeFalhas(totalFalha.get());
        cargaTransfInfo.setQtdeSucesso(totalSucesso.get());
        cargaTransfInfoService.atualizar(cargaTransfInfo);
    }

    public void limpaCacheHibernate() {
        Session s = (Session) em.getDelegate();
        SessionFactory sf = s.getSessionFactory();
        sf.getCache().evictAllRegions();
    }

}