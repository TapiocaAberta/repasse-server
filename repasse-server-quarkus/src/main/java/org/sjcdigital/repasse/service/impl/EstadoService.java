package org.sjcdigital.repasse.service.impl;

import javax.enterprise.context.Dependent;
import javax.transaction.Transactional;

import org.sjcdigital.repasse.model.base.Estado;
import org.sjcdigital.repasse.service.Service;

@Dependent
public class EstadoService extends Service<Estado> {

    public Estado buscaEstadoPorSigla(String sigla) {
        var buscaPorSigla = em.createNamedQuery("Estado.porSigla", Estado.class);
        buscaPorSigla.setParameter("sigla", sigla);
        return getFistResult(buscaPorSigla);
    }

    @Transactional
    public Estado buscaEstadoPorSiglaOuCria(Estado estado) {
        var _estado = buscaEstadoPorSigla(estado.getSigla());
        if (_estado == null) {
            this.salvar(estado);
            return estado;
        } else {
            return _estado;
        }
    }

}
