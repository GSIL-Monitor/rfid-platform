package com.casesoft.dmc.service.task;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.task.BusinessDtlDao;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.BusinessDtl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2017/8/18.
 */
@Service
@Transactional
public class BusinessDtlService implements IBaseService<BusinessDtl,String> {
    @Autowired
    private BusinessDtlDao businessDtlDao;
    @Override
    public Page<BusinessDtl> findPage(Page<BusinessDtl> page, List<PropertyFilter> filters) {
        return this.businessDtlDao.findPage(page,filters);
    }

    @Override
    public void save(BusinessDtl entity) {

    }

    @Override
    public BusinessDtl load(String id) {
        return null;
    }

    @Override
    public BusinessDtl get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<BusinessDtl> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<BusinessDtl> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(BusinessDtl entity) {

    }

    @Override
    public void delete(BusinessDtl entity) {

    }

    @Override
    public void delete(String id) {

    }
}
