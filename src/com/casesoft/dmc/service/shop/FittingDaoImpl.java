package com.casesoft.dmc.service.shop;

import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.dao.shop.FittingDao;
import com.casesoft.dmc.model.shop.FittingView;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class FittingDaoImpl implements FittingDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public DataSourceResult getList(DataSourceRequest request) {
        return request.toDataSourceResult(sessionFactory.getCurrentSession(), FittingView.class);
    }
}
