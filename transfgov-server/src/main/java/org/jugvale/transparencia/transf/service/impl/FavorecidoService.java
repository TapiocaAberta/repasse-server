package org.jugvale.transparencia.transf.service.impl;

import java.util.function.Supplier;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.jugvale.transparencia.transf.model.transferencia.Favorecido;
import org.jugvale.transparencia.transf.service.Service;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
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
