package com.casesoft.dmc.service.log;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.log.RfidLogMessageDao;
import com.casesoft.dmc.model.log.RfidLogMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RfidLogMessageService extends AbstractBaseService<RfidLogMessage,String>{

	@Autowired
	private RfidLogMessageDao rfidLogMessageDao;
	@Override
	public Page<RfidLogMessage> findPage(Page<RfidLogMessage> page,
			List<PropertyFilter> filters) {
		// TODO Auto-generated method stub
		return this.rfidLogMessageDao.findPage(page, filters);
	}

	@Override
	public void save(RfidLogMessage entity) {
		// TODO Auto-generated method stub
		this.rfidLogMessageDao.save(entity);
	}

	@Override
	public RfidLogMessage load(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RfidLogMessage get(String propertyName, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RfidLogMessage> find(List<PropertyFilter> filters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RfidLogMessage> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <X> List<X> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(RfidLogMessage entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(RfidLogMessage entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		
	}

}
