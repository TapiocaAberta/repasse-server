package org.sjcdigital.repasse.service.impl;

import javax.enterprise.context.Dependent;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import org.sjcdigital.repasse.model.transferencia.Favorecido;
import org.sjcdigital.repasse.service.Service;

@Dependent
public class FavorecidoService extends Service<Favorecido> {

    @Transactional
    public Favorecido buscaPorCodigoOuCria(Favorecido favorecido) {
        try {
            var buscaFavorecido = em.createNamedQuery("Favorecido.porCodigo",
                                                      Favorecido.class);
            buscaFavorecido.setParameter("codigo", favorecido.getCodigo());
            return buscaFavorecido.getSingleResult();
        } catch (NoResultException e) {
            this.salvar(favorecido);
            return favorecido;
        }
    }
}