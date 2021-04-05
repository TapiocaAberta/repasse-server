package org.sjcdigital.repasse.service.impl;

import javax.enterprise.context.RequestScoped;
import javax.persistence.NoResultException;

import org.sjcdigital.repasse.model.transferencia.Favorecido;
import org.sjcdigital.repasse.service.Service;

@RequestScoped
public class FavorecidoService extends Service<Favorecido> {

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