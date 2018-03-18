package com.casesoft.dmc.service.search;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.search.SaleorderCountViewDao;
import com.casesoft.dmc.model.logistics.SaleOrderBill;
import com.casesoft.dmc.model.search.SaleorderCountView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2017/12/19.
 */
@Service
@Transactional
public class SaleOrderCountViewService implements IBaseService<SaleorderCountView, String> {
    @Autowired
    private SaleorderCountViewDao saleorderCountViewDao;
    @Override
    public Page<SaleorderCountView> findPage(Page<SaleorderCountView> page, List<PropertyFilter> filters) {
        return this.saleorderCountViewDao.findPage(page, filters);
    }

    @Override
    public void save(SaleorderCountView entity) {

    }

    @Override
    public SaleorderCountView load(String id) {
        return null;
    }

    @Override
    public SaleorderCountView get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<SaleorderCountView> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<SaleorderCountView> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(SaleorderCountView entity) {

    }

    @Override
    public void delete(SaleorderCountView entity) {

    }

    @Override
    public void delete(String id) {

    }
}
