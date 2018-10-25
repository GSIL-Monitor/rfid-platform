package com.casesoft.dmc.service.sys.impl;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.sys.PricingRulesDao;
import com.casesoft.dmc.model.cfg.MultiLevelRelation;
import com.casesoft.dmc.model.cfg.PropertyType;
import com.casesoft.dmc.model.sys.PricingRules;
import com.casesoft.dmc.service.cfg.MultiLevelRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
// 默认将类中的所有函数纳入事务管理.
@Transactional
public class PricingRulesService extends AbstractBaseService<PricingRules,String> {

  @Autowired
  private PricingRulesDao pricingRulesDao;
  @Autowired
  private MultiLevelRelationService multiLevelRelationService;

  public PricingRules findBySC(String series,String class3){
    if(CommonUtil.isNotBlank(class3)){
      return this.pricingRulesDao.findUnique("from PricingRules where series = '"+series+"' and class3 = ?",new Object[]{class3});
    }else{
      return this.pricingRulesDao.findUnique("from PricingRules where series = '"+series+"' and class3 is null and isDefault ='Y'");
    }
  }

  public Boolean isExist(String series,String class3){
    if(CommonUtil.isNotBlank(class3)){
      Long count = this.pricingRulesDao.findUnique("select count(*) from PricingRules where series = '"+series+"' and class3 = ?",new Object[]{class3});
      return count>0?true:false;
    }else{
      return false;
    }
  }

  public PricingRules findPricingRulesBySC(String series,String class3){
    PricingRules pricingRules = this.pricingRulesDao.findUnique("from PricingRules where series = '"+series+"' and class3 = ?",new Object[]{class3});
    String parentId = "";
    Integer depth = 0;
    MultiLevelRelation currentLevelClass3 = this.multiLevelRelationService.load(class3);
    if(CommonUtil.isNotBlank(currentLevelClass3)) {
      parentId = currentLevelClass3.getParentId();
      depth = currentLevelClass3.getDepth();
    }
    if (CommonUtil.isNotBlank(pricingRules)){
      return pricingRules;
    }else if(depth > 1 && CommonUtil.isNotBlank(parentId)) {
      return findPricingRulesBySC(series, parentId);
    }else {
        return this.pricingRulesDao.findUnique("from PricingRules where series = '"+series+"' and class3 is null");
    }
  }

  @Transactional(readOnly = true)
  @Override
  public Page<PricingRules> findPage(Page<PricingRules> page, List<PropertyFilter> filters) {
    return this.pricingRulesDao.findPage(page,filters);
  }

  @Transactional(readOnly = true)
  @Override
  public void save(PricingRules pricingRules) {
    this.pricingRulesDao.saveOrUpdate(pricingRules);
  }

  @Transactional(readOnly = true)
  public PricingRules findById(String name){
    String hql = "from PricingRules p where p.name = ?";
    return this.pricingRulesDao.findUnique(hql,new Object[]{name});
  }

  @Override
  @Transactional(readOnly = true)
  public PricingRules load(String id) {
    return pricingRulesDao.load(id);
  }

  @Override
  @Transactional(readOnly = true)
  public PricingRules get(String propertyName, Object value) {
    return this.pricingRulesDao.findUniqueBy(propertyName,value);
  }

  @Override
  @Transactional(readOnly = true)
  public List<PricingRules> find(List<PropertyFilter> filters) {
    return this.pricingRulesDao.find(filters);
  }

  @Override
  @Transactional(readOnly = true)
  public List<PricingRules> getAll() {
    return this.pricingRulesDao.getAll();
  }

  @Override
  public <X> List<X> findAll() {
    return null;
  }

  @Override
  public void update(PricingRules pricingRules) {
    this.pricingRulesDao.update(pricingRules);
  }

  @Override
  public void delete(PricingRules pricingRules) {
    this.pricingRulesDao.delete(pricingRules);
  }

  @Override
  public void delete(String id) {
    this.pricingRulesDao.delete(id);
  }

  public List<PropertyType> findPricingRulesPropertyType() {
    return this.pricingRulesDao.find("from PropertyType where type=?",new Object[]{"商品代码分类"});
  }

  public List<PricingRules> findAllUseRule(String series) {
    return this.pricingRulesDao.find("from PricingRules where isAllUse='Y' and series=?",series);
  }
}
