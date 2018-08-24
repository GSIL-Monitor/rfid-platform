package com.casesoft.dmc.service.search;

import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.dao.search.DetailSaleDao;
import com.casesoft.dmc.model.search.DetailSaleView;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class DetailSaleDaoImpl implements DetailSaleDao{
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public DataSourceResult getList(DataSourceRequest request) {
		return request.toDataSourceResult(sessionFactory.getCurrentSession(), DetailSaleView.class);
	}

}
