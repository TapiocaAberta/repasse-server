package org.sjcdigital.repasse.service.impl;

import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.sjcdigital.repasse.model.transferencia.CargaTransfInfo;
import org.sjcdigital.repasse.service.Service;

import static javax.transaction.Transactional.TxType.REQUIRES_NEW;

@Dependent
public class CargaTransfInfoService extends Service<CargaTransfInfo> {

    @Inject
    Logger logger;

    public List<CargaTransfInfo> todosPorAnoMes(int ano, int mes) {
        return porAnoMesQuery(ano, mes).getResultList();
    }

    public CargaTransfInfo porAnoMes(int ano, int mes) {
        return porAnoMesQuery(ano, mes).getSingleResult();
    }

    public TypedQuery<CargaTransfInfo> porAnoMesQuery(int ano, int mes) {
        TypedQuery<CargaTransfInfo> buscaCargaTransfInfo = em.createNamedQuery(
                                                                               "CargaTransfInfo.porAnoMes", CargaTransfInfo.class);
        buscaCargaTransfInfo.setParameter("ano", ano);
        buscaCargaTransfInfo.setParameter("mes", mes);
        return buscaCargaTransfInfo;
    }

    @Transactional(REQUIRES_NEW)
    public CargaTransfInfo porAnoMesOuCria(int ano,
                                           int mes,
                                           Supplier<CargaTransfInfo> cria) {
        try {
            return porAnoMes(ano, mes);
        } catch (NoResultException e) {
            CargaTransfInfo novo = cria.get();
            this.salvar(novo);
            return novo;
        }
    }

    @Transactional(REQUIRES_NEW)
    public void apagaPorAnoMes(int ano, int mes) {
        CargaTransfInfo c = porAnoMes(ano, mes);
        remover(c);
    }

}
