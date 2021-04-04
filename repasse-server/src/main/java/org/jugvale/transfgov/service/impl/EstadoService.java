package org.jugvale.transfgov.service.impl;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.jugvale.transfgov.model.base.Estado;
import org.jugvale.transfgov.service.Service;

@Stateless
public class EstadoService extends Service<Estado> {

    public Estado buscaEstadoPorSigla(String sigla) {
        TypedQuery<Estado> buscaPorSigla = em.createNamedQuery("Estado.porSigla", Estado.class);
        buscaPorSigla.setParameter("sigla", sigla);
        return getFistResult(buscaPorSigla);
    }

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
