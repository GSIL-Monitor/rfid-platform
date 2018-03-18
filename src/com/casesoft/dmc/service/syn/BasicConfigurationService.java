package com.casesoft.dmc.service.syn;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.syn.BasicConfigurationDao;
import com.casesoft.dmc.model.syn.BasicConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by pc on 2016-12-18.
 */

@Service
@Transactional
public class BasicConfigurationService extends AbstractBaseService<BasicConfiguration, String> {
    @Autowired
    private BasicConfigurationDao basicConfigurationDao;
    @Override
    public Page<BasicConfiguration> findPage(Page<BasicConfiguration> page, List<PropertyFilter> filters) {
        return this.basicConfigurationDao.findPage(page, filters);
    }

    @Override
    public void save(BasicConfiguration entity) {
        this.basicConfigurationDao.save(entity);
    }
     public void save(List<BasicConfiguration> list) {
        this.basicConfigurationDao.doBatchInsert(list);
    }

    @Override
    public BasicConfiguration load(String id) {
        return this.basicConfigurationDao.get(id);
    }

    @Override
    public BasicConfiguration get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<BasicConfiguration> find(List<PropertyFilter> filters) {
        return this.basicConfigurationDao.find(filters);
    }

    @Override
    public List<BasicConfiguration> getAll() {
        return this.basicConfigurationDao.getAll();
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(BasicConfiguration entity) {
        this.basicConfigurationDao.update(entity);
    }

    @Override
    public void delete(BasicConfiguration entity) {

    }

    @Override
    public void delete(String id) {

    }
}
