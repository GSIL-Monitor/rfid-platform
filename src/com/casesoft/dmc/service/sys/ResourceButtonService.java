package com.casesoft.dmc.service.sys;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.sys.ResourceButtonDao;
import com.casesoft.dmc.model.sys.ResourceButton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2018/6/11.
 */
@Service
@Transactional
public class ResourceButtonService implements IBaseService<ResourceButton,String> {
    @Autowired
    private ResourceButtonDao resourceButtonDao;
    @Override
    public Page<ResourceButton> findPage(Page<ResourceButton> page, List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public void save(ResourceButton entity) {

    }

    @Override
    public ResourceButton load(String id) {
        return this.resourceButtonDao.load(id);
    }

    @Override
    public ResourceButton get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<ResourceButton> find(List<PropertyFilter> filters) {
        return this.resourceButtonDao.find(filters);
    }

    @Override
    public List<ResourceButton> getAll() {
        return this.resourceButtonDao.getAll();
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(ResourceButton entity) {
        this.resourceButtonDao.update(entity);
    }

    @Override
    public void delete(ResourceButton entity) {

    }

    @Override
    public void delete(String id) {

    }

    public List<ResourceButton> findResourceButtonByCodeAndRoleId(String code,String roleId){
        String hql="from ResourceButton t where t.code=? and t.roleId=?";
        List<ResourceButton> list = this.resourceButtonDao.find(hql, new Object[]{code, roleId});
        return list;
    }
}
