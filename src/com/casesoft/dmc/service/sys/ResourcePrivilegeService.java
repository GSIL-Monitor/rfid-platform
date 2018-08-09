package com.casesoft.dmc.service.sys;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.sys.ResourcePrivilegeDao;
import com.casesoft.dmc.model.sys.ResourcePrivilege;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2018/6/11.
 */
@Service
@Transactional
public class ResourcePrivilegeService implements IBaseService<ResourcePrivilege,String> {
    @Autowired
    private ResourcePrivilegeDao resourcePrivilegeDao;
    @Override
    public Page<ResourcePrivilege> findPage(Page<ResourcePrivilege> page, List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public void save(ResourcePrivilege entity) {
        this.resourcePrivilegeDao.save(entity);
    }
    public void saveAll(List<ResourcePrivilege> saveLists){
        this.resourcePrivilegeDao.doBatchInsert(saveLists);
    }

    @Override
    public ResourcePrivilege load(String id) {
        return this.resourcePrivilegeDao.load(id);
    }

    @Override
    public ResourcePrivilege get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<ResourcePrivilege> find(List<PropertyFilter> filters) {
        return this.resourcePrivilegeDao.find(filters);
    }

    @Override
    public List<ResourcePrivilege> getAll() {
        return this.resourcePrivilegeDao.getAll();
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(ResourcePrivilege entity) {
        this.resourcePrivilegeDao.update(entity);
    }

    @Override
    public void delete(ResourcePrivilege entity) {

    }

    @Override
    public void delete(String id) {

    }

    public List<ResourcePrivilege> findResourceButtonByCodeAndRoleId(String code, String roleId, String type){
        String hql="from ResourcePrivilege t where t.code=? and t.roleId=? and t.type=?";
        List<ResourcePrivilege> list = this.resourcePrivilegeDao.find(hql, new Object[]{code, roleId,type});
        return list;
    }
    /*
    * @param url  Controller url 路径 sys_resource 中URL
    * @param roleId 当前登陆账户角色id
    * @param type  查询类型 按钮/字段
    * */
    public List<ResourcePrivilege> findButtonByCodeAndRoleId(String url, String roleId, String type){
        String hql="select t from ResourcePrivilege t,Resource r where r.url=? and t.roleId=? and t.type=? and r.code = t.code ";
        List<ResourcePrivilege> list = this.resourcePrivilegeDao.find(hql, new Object[]{url,roleId,type});
        return list;
    }
    /*
     * @param url  Controller url 路径sys_resource 中URL
     * @param roleId 当前登陆账户角色id
     * */
    public List<ResourcePrivilege> findPrivilege(String url, String roleId){
        String hql="select t from ResourcePrivilege t,Resource r where r.url=? and t.roleId=?  and r.code = t.code ";
        List<ResourcePrivilege> list = this.resourcePrivilegeDao.find(hql, new Object[]{url,roleId});
        return list;
    }
}
