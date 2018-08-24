package com.casesoft.dmc.service.cfg;

import java.util.ArrayList;
import java.util.List;

import com.casesoft.dmc.dao.sys.VendorDao;
import com.casesoft.dmc.model.cfg.VO.TreeVO;
import com.casesoft.dmc.model.sys.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.cfg.PropertyKeyDao;
import com.casesoft.dmc.dao.cfg.PropertyTypeDao;
import com.casesoft.dmc.dao.sys.VendorDao;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.cfg.PropertyType;
import com.casesoft.dmc.model.sys.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class PropertyService implements IBaseService<PropertyType, String> {


    @Autowired
    private PropertyKeyDao propertyKeyDao;

    @Autowired
    private PropertyTypeDao propertyTypeDao;

    @Autowired
    private VendorDao vendorDao;
    @Autowired
    private MultiLevelRelationService multiLevelRelationService;


    //模糊查詢成分
    public List<PropertyKey> findByRemark(String term){
        String hql = "from PropertyKey where type = 'C11' and name like '%"+term+"%'";
        return this.propertyKeyDao.find(hql);
    }
    @Override
    @Transactional(readOnly = true)
    public Page<PropertyType> findPage(Page<PropertyType> page,
                                       List<PropertyFilter> filters) {
        return this.propertyTypeDao.findPage(page, filters);
    }

    //add by Anna
    @Transactional(readOnly = true)
    public Page<PropertyKey> findPageForKey(Page<PropertyKey> page, List<PropertyFilter> filters) {
        return this.propertyKeyDao.findPage(page, filters);
    }


    @Transactional(readOnly = true)
    public List<PropertyKey> getPropertyKeyByType(String type) {
        String hql = "from PropertyKey p where p.type=?";
        return this.propertyKeyDao.find(hql, new Object[]{type});

    }
    public List<PropertyKey>getPKByType(String type){
        String hql="from PropertyKey where type=? and ynuse='Y'";
        return this.propertyKeyDao.find(hql,new Object[]{type});
    }


    public PropertyType findPropertyTypebyid(String id) {
        return this.propertyTypeDao.findUnique("from PropertyType u where u.id='" + id + "'");
    }

    public PropertyKey findPropertyKeybyid(String id) {
        return this.propertyKeyDao.findUnique("from PropertyKey u where u.id='" + id + "'");
    }

    public PropertyKey findPropertyKeybytype(String code) {
        return this.propertyKeyDao.findUnique("from PropertyKey u where u.code='" + code + "'");
    }

    @Override
    public void save(PropertyType entity) {
        // TODO Auto-generated method stub
        this.propertyTypeDao.saveOrUpdate(entity);
    }

    public void saveKey(PropertyKey entity, Unit unit) {
        // TODO Auto-generated method stub
        //this.propertyTypeDao.saveOrUpdate(entity);
        this.propertyKeyDao.saveOrUpdate(entity);
        this.vendorDao.saveOrUpdate(unit);
    }

    public void saveKey(PropertyKey entity) {
        // TODO Auto-generated method stub
        //this.propertyTypeDao.saveOrUpdate(entity);
        this.propertyKeyDao.saveOrUpdate(entity);
    }

    //add by yushen 添加多级分类
    public void saveKey(PropertyKey entity, String parentId, String multiLevelType){
        saveKey(entity);
        this.multiLevelRelationService.save(entity, parentId, multiLevelType);
    }

    public Integer findtypeNum(String type) {
        String hql = "select count(type) from PropertyType t where t.type='" + type + "'";
        Long unique = this.propertyTypeDao.findUnique(hql);
        return unique.intValue();
    }

    public Integer findtkeyNum(String type) {
        String hql = "select count(type) from PropertyKey t where t.type='" + type + "'";
        Long unique = this.propertyTypeDao.findUnique(hql);
        return unique.intValue();
    }

    @Override
    public PropertyType load(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PropertyType get(String propertyName, Object value) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<PropertyType> find(List<PropertyFilter> filters) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<PropertyType> getAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void update(PropertyType entity) {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete(PropertyType entity) {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete(String id) {
        // TODO Auto-generated method stub

    }

    public List<List<PropertyKey>> getPropertyKeyByTypes(String[] typeList) {
        List<List<PropertyKey>> result = new ArrayList<>();
        String hql = "from PropertyKey p where p.type=?";
        List<PropertyKey> propertyKeyList = new ArrayList<>();
        for (String type : typeList) {
            propertyKeyList = this.propertyKeyDao.find(hql, new Object[]{type});
            result.add(propertyKeyList);
        }
        return result;
    }

    public PropertyKey findPropertyKeyBytypeAndCode(String class1) {
        String hql = "from PropertyKey t where t.type='C1' and code=?";
        PropertyKey unique = this.propertyKeyDao.findUnique(hql, new Object[]{class1});
        return unique;
    }

    public PropertyKey findPropertyKeyByNameAndType(String name, String type) {
        return this.propertyKeyDao.findUnique("from PropertyKey t where t.name=? and t.type=?", new Object[]{name, type});
    }

    public List<TreeVO> listPropertyTree(String multiLevelType) {
        return this.multiLevelRelationService.listTree(multiLevelType);
    }
}
