package com.casesoft.dmc.service.factory;

import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.factory.FactoryBillDtlSearchDao;
import com.casesoft.dmc.extend.third.descriptor.DataResult;
import com.casesoft.dmc.extend.third.request.BaseService;
import com.casesoft.dmc.extend.third.request.RequestPageData;
import com.casesoft.dmc.model.factory.FactoryBillDtlView;
import com.casesoft.dmc.model.search.DetailInboundView;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by GuoJunwen on 2017-05-15.
 */
@Service
@Transactional
public class FactoryBillDtlSearchService extends BaseService<FactoryBillDtlView,String> {

    @Autowired
    private FactoryBillDtlSearchDao factoryBillDtlSearchDao;
    @Autowired
    private SessionFactory sessionFactory;


    public DataSourceResult find(DataSourceRequest request) {
        return request.toDataSourceResult(sessionFactory.getCurrentSession(), FactoryBillDtlView.class);
    }

    @Override
    public Page<FactoryBillDtlView> findPage(Page<FactoryBillDtlView> page, List<PropertyFilter> filters) {

        return this.factoryBillDtlSearchDao.findPage(page,filters);
    }

    @Override
    public void save(FactoryBillDtlView entity) {
        this.factoryBillDtlSearchDao.save(entity);
    }

    @Override
    public FactoryBillDtlView load(String id) {
        return null;
    }

    @Override
    public FactoryBillDtlView get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<FactoryBillDtlView> find(List<PropertyFilter> filters) {
        return this.factoryBillDtlSearchDao.find(filters);
    }

    @Override
    public List<FactoryBillDtlView> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(FactoryBillDtlView entity) {
        this.factoryBillDtlSearchDao.update(entity);
    }

    @Override
    public void delete(FactoryBillDtlView entity) {

    }

    @Override
    public void delete(String id) {

    }

    public void signFactoryRecordByIds(String ids) {
        this.factoryBillDtlSearchDao.batchExecute("update FactoryRecord factoryrecord set factoryrecord.sign='Y' where "+ids);
    }

    public void unsignFactoryRecordByIds(String ids) {
        this.factoryBillDtlSearchDao.batchExecute("update FactoryRecord factoryrecord set factoryrecord.sign='N' where "+ids);
    }
}
