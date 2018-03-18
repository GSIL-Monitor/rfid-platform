package com.casesoft.dmc.service.factory;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.factory.FactoryWorkTimeDao;
import com.casesoft.dmc.model.factory.FactoryWorkTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Alvin-PC on 2017/3/6 0006.
 */

@Service
@Transactional
public class FactoryWorkTimeService extends AbstractBaseService<FactoryWorkTime, String> {


    @Autowired
    private FactoryWorkTimeDao factoryWorkTimeDao;
    @Override
    public Page<FactoryWorkTime> findPage(Page<FactoryWorkTime> page, List<PropertyFilter> filters) {
        return this.factoryWorkTimeDao.findPage(page, filters);
    }

    @Override
    public void save(FactoryWorkTime entity) {
        this.factoryWorkTimeDao.saveOrUpdate(entity);
    }

    @Override
    public FactoryWorkTime load(String id) {
        return null;
    }

    @Override
    public FactoryWorkTime get(String propertyName, Object value) {
        return this.factoryWorkTimeDao.findUniqueBy(propertyName, value);
    }

    @Override
    public List<FactoryWorkTime> find(List<PropertyFilter> filters) {
        return this.factoryWorkTimeDao.find(filters);
    }

    @Override
    public List<FactoryWorkTime> getAll() {
        return this.factoryWorkTimeDao.getAll();
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(FactoryWorkTime entity) {

    }

    @Override
    public void delete(FactoryWorkTime entity) {
        this.factoryWorkTimeDao.delete(entity);
    }

    @Override
    public void delete(String id) {
          this.factoryWorkTimeDao.delete(id);
    }

    public FactoryWorkTime findUniqueFactoryWorkTime(String code, Integer token) {
        return  this.factoryWorkTimeDao.findUnique("from FactoryWorkTime where code = ? and token = ? ", new Object[]{code,token});
    }

    public void deleteWorkTime(String code, Integer token) {
        this.factoryWorkTimeDao.batchExecute("delete from FactoryWorkTime where code=? and token =?", code,token);
    }

    public void saveList(List<FactoryWorkTime> workTimeList) {
        this.factoryWorkTimeDao.doBatchInsert(workTimeList);
    }
}
