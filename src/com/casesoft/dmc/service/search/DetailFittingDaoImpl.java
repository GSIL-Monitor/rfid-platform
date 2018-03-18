package com.casesoft.dmc.service.search;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.dao.search.DetailFittingDao;
import com.casesoft.dmc.dao.search.DetailInboundDao;
import com.casesoft.dmc.model.search.DetailFittingView;
import com.casesoft.dmc.model.search.DetailInboundView;

@Transactional
@Component
public class DetailFittingDaoImpl implements DetailFittingDao{
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public DataSourceResult getList(DataSourceRequest request) {
		return request.toDataSourceResult(sessionFactory.getCurrentSession(), DetailFittingView.class);
	}

}
