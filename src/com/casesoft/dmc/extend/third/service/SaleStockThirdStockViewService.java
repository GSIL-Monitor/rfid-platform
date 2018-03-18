package com.casesoft.dmc.extend.third.service;

/**
 * Created by john on 2017-02-28.
 */

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.extend.third.dao.SaleStockThirdStockViewDao;
import com.casesoft.dmc.extend.third.descriptor.DataResult;
import com.casesoft.dmc.extend.third.model.SaleStockThirdStockView;
import com.casesoft.dmc.extend.third.request.BaseService;
import com.casesoft.dmc.extend.third.request.RequestPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by john on 2017-02-27.
 */
@Service
@Transactional
public class SaleStockThirdStockViewService   extends BaseService<SaleStockThirdStockView, String> {
    @Autowired
    private SaleStockThirdStockViewDao saleStockThirdStockViewDao;
    @Override
    public Page<SaleStockThirdStockView> findPage(Page<SaleStockThirdStockView> page, List<PropertyFilter> filters) {
        return null;
    }
    public DataResult find(RequestPageData<?> request) {
        return this.saleStockThirdStockViewDao.find(request);
    }

    @Override
    public void save(SaleStockThirdStockView entity) {

    }

    @Override
    public SaleStockThirdStockView load(String id) {
        return null;
    }

    @Override
    public SaleStockThirdStockView get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<SaleStockThirdStockView> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<SaleStockThirdStockView> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(SaleStockThirdStockView entity) {

    }

    @Override
    public void delete(SaleStockThirdStockView entity) {

    }

    @Override
    public void delete(String id) {

    }
}
