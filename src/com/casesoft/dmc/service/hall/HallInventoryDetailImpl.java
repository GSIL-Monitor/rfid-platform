package com.casesoft.dmc.service.hall;

import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.model.hall.HallInventoryDetail;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by session on 2017/4/5 0005.
 */

@Transactional
@Component
public class HallInventoryDetailImpl {

	@Autowired
	private SessionFactory sessionFactory;

	public DataSourceResult getList(DataSourceRequest request){
		return request.toDataSourceResult(sessionFactory.getCurrentSession(), HallInventoryDetail.class);
	}
}
