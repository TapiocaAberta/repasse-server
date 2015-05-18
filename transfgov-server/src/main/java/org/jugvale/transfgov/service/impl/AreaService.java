package org.jugvale.transfgov.service.impl;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.jugvale.transfgov.model.base.Area;
import org.jugvale.transfgov.service.Service;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AreaService extends Service<Area> {
	
}
