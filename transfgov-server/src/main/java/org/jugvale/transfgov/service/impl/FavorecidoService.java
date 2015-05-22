package org.jugvale.transfgov.service.impl;

import java.util.function.Supplier;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.jugvale.transfgov.model.transferencia.Favorecido;
import org.jugvale.transfgov.service.Service;

@Stateless
public class FavorecidoService extends Service<Favorecido> {

	public Favorecido buscaPorCodigoOuCria(String codigo,
			Supplier<Favorecido> favorecidoSupplier) {
		try {
			TypedQuery<Favorecido> buscaFavorecido = em.createNamedQuery(
					"Favorecido.porCodigo", Favorecido.class);
			buscaFavorecido.setParameter("codigo", codigo);
			return buscaFavorecido.getSingleResult();
		} catch (NoResultException e) {
			Favorecido novo = favorecidoSupplier.get();
			this.salvar(novo);
			return novo;
		}

	}

}
