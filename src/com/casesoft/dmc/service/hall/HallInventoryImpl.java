package com.casesoft.dmc.service.hall;

import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.dao.hall.IHallInventoryDao;
import com.casesoft.dmc.model.hall.HallInventory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by session on 2017/3/22 0022.
 */
@Transactional
@Component
public class HallInventoryImpl implements IHallInventoryDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public DataSourceResult getList(DataSourceRequest request){
		return request.toDataSourceResult(sessionFactory.getCurrentSession(), HallInventory.class);
	}
}
