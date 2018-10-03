package org.jugvale.transfgov.carga;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jugvale.transfgov.carga.transferencia.LinhaTransferenciaTransformer;
import org.jugvale.transfgov.carga.transferencia.qualifiers.TransferenciaTransformer2018;
import org.jugvale.transfgov.model.transferencia.CargaTransfInfo;
import org.jugvale.transfgov.service.impl.CargaTransfInfoService;
import org.jugvale.transfgov.service.impl.TransferenciaService;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
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

	@PersistenceContext
	private EntityManager em;
	
	
	/**
	 * Irá carregar o arquivo passado no banco de dados. Linha a linha será
	 * inserida.
	 * 
	 * @param arquivoCSV
	 *            O arquivo para ser carregado
	 * @throws IOException
	 */
	@Asynchronous
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void carregarNoBanco(int ano, int mes, Path arquivoCSV)
			throws IOException {
		AtomicInteger totalSucesso = new AtomicInteger(0);
		AtomicInteger totalFalha = new AtomicInteger(0);
		AtomicInteger totalNaoProcessada = new AtomicInteger(0);
		logger.warning("Contando linhas de transferência para data " + mes + "/" + ano);
		long qtdeLinhas = Files.lines(arquivoCSV).count() - 1;;
		logger.warning("Iniciando carga para data " + mes + "/" + ano);
		CargaTransfInfo cargaTransfInfo = cargaTransfInfoService
				.porAnoMesOuCria(ano, mes, () -> new CargaTransfInfo(ano, mes));
		cargaTransfInfo.setInicio(new Date());
		cargaTransfInfo.setFim(null);
		cargaTransfInfo.setQtdeLinhas((int) qtdeLinhas);
		cargaTransfInfoService.atualizar(cargaTransfInfo);
		Files.lines(arquivoCSV, StandardCharsets.UTF_8)
				.skip(1)
				.forEach( linha -> 
						    processarLinha(ano, mes, totalSucesso, totalFalha, totalNaoProcessada, cargaTransfInfo, linha)
						);
		Files.delete(arquivoCSV);
		cargaTransfInfo.setFim(new Date());
		cargaTransfInfo.setQtdeSucesso(totalSucesso.get());
		cargaTransfInfoService.atualizar(cargaTransfInfo);
		logger.warning("Carga para " + mes + "/" + ano + " terminada");
		limpaCacheHibernate();
	}

    private void processarLinha(int ano, int mes, AtomicInteger totalSucesso, AtomicInteger totalFalha,
            AtomicInteger totalNaoProcessada, CargaTransfInfo cargaTransfInfo, String linha) {
        try {
            if(transfTransformer.transformaLinha(ano, mes, linha).isPresent()) {
                totalSucesso.incrementAndGet();
            } else {
                totalNaoProcessada.incrementAndGet();
            }
        } catch (Exception e) {
        	totalFalha.incrementAndGet();
        	logger.fine("Error: " + e.getMessage());
        	e.printStackTrace();
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