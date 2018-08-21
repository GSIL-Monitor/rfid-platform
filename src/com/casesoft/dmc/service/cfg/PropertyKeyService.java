package com.casesoft.dmc.service.cfg;

import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.cfg.PropertyKeyDao;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.cfg.PropertyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PropertyKeyService extends AbstractBaseService<PropertyKey, String> {

  @Autowired
  private PropertyKeyDao propertyKeyDao;

  public PropertyKeyDao getPropertyDao() {
    return propertyKeyDao;
  }

  public void setPropertyDao(PropertyKeyDao propertyDao) {
    this.propertyKeyDao = propertyDao;
  }

  @Override
  @Transactional(readOnly = true)
  public Page<PropertyKey> findPage(Page<PropertyKey> page, List<PropertyFilter> filters) {
    // TODO Auto-generated method stub
    return this.propertyKeyDao.findPage(page, filters);
  }

  public void saveAllPropertyKey(List<PropertyKey> list){
	  this.propertyKeyDao.doBatchInsert(list);
  }
  @Override
  public void save(PropertyKey entity) {
    this.propertyKeyDao.save(entity);
  }

  @Override
  public PropertyKey load(String id) {
    // TODO Auto-generated method stub
    return this.propertyKeyDao.load(id);
  }

  @Override
  public PropertyKey get(String propertyName, Object value) {
    return this.propertyKeyDao.findUniqueBy(propertyName,value);
  }

  @Override
  public List<PropertyKey> find(List<PropertyFilter> filters) {
    return this.propertyKeyDao.find(filters);
  }

  @Override
  public List<PropertyKey> getAll() {
    // TODO Auto-generated method stub
    return this.propertyKeyDao.getAll();
  }

  @Override
  public <X> List<X> findAll() {
    String hql = "from PropertyType pt";
    return this.propertyKeyDao.find(hql, new Object[] {});

  }


  public String initProId() {
    String hql = "select max(CAST(SUBSTRING(p.id,3),integer)) from PropertyKey as p";
    Integer id = this.propertyKeyDao.findUnique(hql, new Object[] {});
    String prefix = Constant.Code.Property_Key_Code_prefix;
    return id == null ? (prefix + "001") : prefix + CommonUtil.convertIntToString(id + 1, 3);
  }

  public List<PropertyKey> findKeyList(Integer type) {
    String hql = "from PropertyKey p where p.type=?";
    return this.propertyKeyDao.find(hql, new Object[] { type });

  }

  @Override
  public void update(PropertyKey entity) {
    // TODO Auto-generated method stub
    this.propertyKeyDao.update(entity);
  }

  @Override
  public void delete(PropertyKey entity) {
    // TODO Auto-generated method stub

  }

  @Override
  public void delete(String id) {
    // TODO Auto-generated method stub

  }

public List<PropertyKey> findPropertyKey() {
	String hql = ""
               +"from PropertyKey k where k.type in ('BD','C1','C2','C3','C4','C5','C6','C7',"
			   +"'C8','C9','C10')";
return this.propertyKeyDao.find(hql);
}
public List<PropertyType> findPrpertyByType(){
	String hql = " from PropertyType k where k.keyId in"
			+ "('C1','C2','C3','C4','C5','C6','C7','C8','C9','C10')";
    return this.propertyKeyDao.find(hql);
}
public List<PropertyKey> findclassname(){
  String hql="from PropertyKey where type='C1'";
  List<PropertyKey> propertyKeys = this.propertyKeyDao.find(hql);
  return propertyKeys;
}

}
