package com.casesoft.dmc.service.exchange;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.exchange.InterfaceModelDao;
import com.casesoft.dmc.dao.exchange.InterfaceModelDtlDao;
import com.casesoft.dmc.model.exchange.InterfaceModel;
import com.casesoft.dmc.model.exchange.InterfaceModelDtl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by GuoJunwen on 2017-06-06.
 */
@Service
@Transactional
public class InterfaceModelService extends AbstractBaseService<InterfaceModel, String> {

    @Autowired
    private InterfaceModelDao interfaceModelDao;

    @Autowired
    private InterfaceModelDtlDao interfaceModelDtlDao;

    @Override
    public Page<InterfaceModel> findPage(Page<InterfaceModel> page, List<PropertyFilter> filters) {
        return this.interfaceModelDao.findPage(page,filters);
    }

    @Override
    public void save(InterfaceModel entity) {
        this.interfaceModelDao.saveOrUpdate(entity);
    }

    @Override
    public InterfaceModel load(String id) {
        return null;
    }

    @Override
    public InterfaceModel get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<InterfaceModel> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<InterfaceModel> getAll() {
        return this.interfaceModelDao.getAll();
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(InterfaceModel entity) {

    }

    @Override
    public void delete(InterfaceModel entity) {
          this.interfaceModelDao.delete(entity);
    }

    @Override
    public void delete(String id) {
         this.interfaceModelDao.delete(id);
    }

    public InterfaceModel findByCode(String code) {
        String hql="from InterfaceModel where code=?";
       return this.interfaceModelDao.findUnique(hql,new Object[]{code});
    }

    public String findMaxCode() {
        String hql = "select max(CAST(code,integer))from InterfaceModel";
        Integer code = this.interfaceModelDao.findUnique(hql);
        return code == null ?  "0000001" : CommonUtil.convertIntToString(code + 1, 7);
    }

    public void saveDtl(InterfaceModelDtl interfaceModelDtl){
         this.interfaceModelDtlDao.saveOrUpdate(interfaceModelDtl);
    }


    public void deleteDtl(String id) {
        this.interfaceModelDtlDao.delete(id);
    }

    public String findDtlMaxId() {
        String hql = "select max(CAST(id,integer))from InterfaceModelDtl";
        Integer code = this.interfaceModelDao.findUnique(hql);
        return code == null ?  "0000001" : CommonUtil.convertIntToString(code + 1, 7);
    }

    public List<InterfaceModelDtl> findDtl(String modelCode) {
        String hql = "from InterfaceModelDtl where modelCode=? order by id asc";
        return this.interfaceModelDtlDao.find(hql,new Object[]{modelCode});
    }
}
