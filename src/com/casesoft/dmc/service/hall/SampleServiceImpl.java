package com.casesoft.dmc.service.hall;

import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.dao.hall.ISampleDao;
import com.casesoft.dmc.model.hall.Sample;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by session on 2017/3/23 0023.
 */
@Transactional
@Component
public class SampleServiceImpl implements ISampleDao{

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public DataSourceResult getList(DataSourceRequest request){
		return request.toDataSourceResult(sessionFactory.getCurrentSession(),Sample.class);
	}
}
