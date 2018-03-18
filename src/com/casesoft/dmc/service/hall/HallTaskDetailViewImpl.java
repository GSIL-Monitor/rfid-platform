package com.casesoft.dmc.service.hall;

import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.dao.hall.IHallTaskDetailViewDao;
import com.casesoft.dmc.model.hall.HallTaskDetailView;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by session on 2017/3/22 0022.
 */

@Transactional
@Component
public class HallTaskDetailViewImpl implements IHallTaskDetailViewDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public DataSourceResult getList(DataSourceRequest request){
		return request.toDataSourceResult(sessionFactory.getCurrentSession(), HallTaskDetailView.class);
	}
}
