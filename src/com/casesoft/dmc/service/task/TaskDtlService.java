package com.casesoft.dmc.service.task;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.task.TaskDtlDao;
import com.casesoft.dmc.model.task.BusinessDtl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2015/4/17 0017.
 */
@Service
@Transactional
public class TaskDtlService extends AbstractBaseService<BusinessDtl, String> {
    @Autowired
    private TaskDtlDao taskDtlDao;
    @Override
    public Page<BusinessDtl> findPage(Page<BusinessDtl> page, List<PropertyFilter> filters) {
        return this.taskDtlDao.findPage(page,filters);
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
        return taskDtlDao.find(filters);
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
