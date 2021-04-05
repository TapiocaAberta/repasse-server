package org.sjcdigital.repasse.service.impl;

import javax.enterprise.context.RequestScoped;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.sjcdigital.repasse.model.base.Estado;
import org.sjcdigital.repasse.service.Service;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.REQUIRES_NEW;

@RequestScoped
public class EstadoService extends Service<Estado> {

    public Estado buscaEstadoPorSigla(String sigla) {
        TypedQuery<Estado> buscaPorSigla = em.createNamedQuery("Estado.porSigla", Estado.class);
        buscaPorSigla.setParameter("sigla", sigla);
        return getFistResult(buscaPorSigla);
    }

    @Transactional(REQUIRES_NEW)
    public Estado buscaEstadoPorSiglaOuCria(Estado estado) {
        Estado _estado = buscaEstadoPorSigla(estado.getSigla());
        if (_estado == null) {
            this.salvar(estado);
            return estado;
        } else {
            return _estado;
        }
    }

}
