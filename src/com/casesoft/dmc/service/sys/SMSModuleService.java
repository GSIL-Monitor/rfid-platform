package com.casesoft.dmc.service.sys;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.sys.SMSModuleDao;
import com.casesoft.dmc.model.logistics.SaleOrderBill;
import com.casesoft.dmc.model.sys.SMSModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2017/10/31.
 */
@Service
@Transactional
public class SMSModuleService implements IBaseService<SMSModule, String> {
    @Autowired
    private SMSModuleDao sMSModuleDao;
    @Override
    public Page<SMSModule> findPage(Page<SMSModule> page, List<PropertyFilter> filters) {
        return this.sMSModuleDao.findPage(page,filters);
    }

    @Override
    public void save(SMSModule entity) {
      this.sMSModuleDao.saveOrUpdateX(entity);
    }

    @Override
    public SMSModule load(String id) {
        return this.sMSModuleDao.load(id);
    }

    @Override
    public SMSModule get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<SMSModule> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<SMSModule> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(SMSModule entity) {

    }

    @Override
    public void delete(SMSModule entity) {

    }

    @Override
    public void delete(String id) {

    }
}
