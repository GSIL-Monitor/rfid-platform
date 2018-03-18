package com.casesoft.dmc.extend.third.service;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.extend.third.dao.ThirdStockDao;
import com.casesoft.dmc.extend.third.descriptor.DataResult;
import com.casesoft.dmc.extend.third.model.ThirdStock;
import com.casesoft.dmc.extend.third.request.BaseService;
import com.casesoft.dmc.extend.third.request.RequestPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by john on 2017-02-22.
 */
@Service
@Transactional
public class ThirdStockService extends BaseService<ThirdStock, String> {
    @Autowired
    private ThirdStockDao thirdStockDao;

    public DataResult find(RequestPageData<?> request){
        return this.thirdStockDao.find(request);
    }
    @Override
    public Page<ThirdStock> findPage(Page<ThirdStock> page, List<PropertyFilter> filters) {
        return this.thirdStockDao.findPage(page, filters);
    }

    @Override
    public void save(ThirdStock entity) {

    }
    public void doBatchInsert(List<ThirdStock> entities){
        if(CommonUtil.isNotBlank(entities)){
            thirdStockDao.doBatchInsert(entities);
        }
    }

    @Override
    public ThirdStock load(String id) {
        return null;
    }

    @Override
    public ThirdStock get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<ThirdStock> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<ThirdStock> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(ThirdStock entity) {

    }

    @Override
    public void delete(ThirdStock entity) {

    }

    @Override
    public void delete(String id) {

    }
}
