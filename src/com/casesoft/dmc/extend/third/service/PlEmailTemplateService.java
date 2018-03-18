package com.casesoft.dmc.extend.third.service;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.extend.third.dao.PlEmailTemplateDao;
import com.casesoft.dmc.extend.third.descriptor.DataResult;
import com.casesoft.dmc.extend.third.model.PlEmailTemplate;
import com.casesoft.dmc.extend.third.request.BaseService;
import com.casesoft.dmc.extend.third.request.RequestPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by GuoJunwen on 2017/3/3 0003.
 */
@Service
@Transactional
public class PlEmailTemplateService extends BaseService<PlEmailTemplate,String> {


    @Autowired
    private PlEmailTemplateDao plEmailTemplateDao;

    @Override
    public Page<PlEmailTemplate> findPage(Page<PlEmailTemplate> page, List<PropertyFilter> filters) {
        return this.plEmailTemplateDao.findPage(page,filters);
    }

    public DataResult find(RequestPageData<?> request) {
        return this.plEmailTemplateDao.find(request);
    }

    @Override
    public void save(PlEmailTemplate plEmailTemplate) {
          this.plEmailTemplateDao.saveOrUpdate(plEmailTemplate);
    }

    public String getMaxId(){
        String FIRSTID="PL";
        String hql="select max(CAST(SUBSTRING(p.id,"+(FIRSTID.length()+1)+"),integer)) from PlEmailTemplate p";
        Integer Id=this.plEmailTemplateDao.findUnique(hql);
        return Id==null?(FIRSTID+"000001"):FIRSTID+ CommonUtil.convertIntToString(Id+1,6);
    }

    public PlEmailTemplate findById(String id){
       String hql="from PlEmailTemplate p where p.id=?";
        return this.plEmailTemplateDao.findUnique(hql,new Object[]{id});
    }

    @Override
    public PlEmailTemplate load(String id) {
        return null;
    }

    @Override
    public PlEmailTemplate get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<PlEmailTemplate> find(List<PropertyFilter> filters) {
        return this.plEmailTemplateDao.find(filters);
    }

    @Override
    public List<PlEmailTemplate> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(PlEmailTemplate entity) {

    }

    @Override
    public void delete(PlEmailTemplate entity) {

    }

    @Override
    public void delete(String id) {

    }
}
