package org.jugvale.transparencia.transf.service.impl;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.jugvale.transparencia.transf.model.transferencia.FonteFinalidade;
import org.jugvale.transparencia.transf.service.Service;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class FonteFinalidadeService extends Service<FonteFinalidade>{

}
