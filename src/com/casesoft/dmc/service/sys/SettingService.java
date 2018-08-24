package com.casesoft.dmc.service.sys;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.sys.SettingDao;
import com.casesoft.dmc.model.sys.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by WingLi on 2016-04-25.
 */
@Service
@Transactional
public class SettingService extends AbstractBaseService<Setting, String>  {
    @Autowired
    private SettingDao settingDao;


    @Override
    public Page<Setting> findPage(Page<Setting> page, List<PropertyFilter> filters) {
        return this.settingDao.findPage(page,filters);
    }

    public void save(Setting entity) {
        this.settingDao.saveOrUpdate(entity);
    }

    @Transactional(readOnly = true)
    @Override
    public Setting load(String id) {
        return this.settingDao.load(id);
    }

    @Override
    public Setting get(String propertyName, Object value) {
        return this.settingDao.findUniqueBy(propertyName,value);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Setting> find(List<PropertyFilter> filters) {
        return this.settingDao.find(filters);
    }


    @Transactional(readOnly = true)
    public List<Setting> getAll() {
        return this.settingDao.getAll();
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }


    public void update(Setting entity) {
        this.settingDao.saveOrUpdate(entity);
    }

    public void delete(Setting entity) {
        this.settingDao.delete(entity);
    }

    
    public void delete(String id) {
        this.settingDao.delete(id);
    }
}
