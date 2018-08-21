package com.casesoft.dmc.service.search;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.search.DateStockDetailDao;
import com.casesoft.dmc.model.search.DateStockDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2017/12/25.
 */
@Service
@Transactional
public class DateStockDetailService implements IBaseService<DateStockDetail, String> {
    @Autowired
    private DateStockDetailDao dateStockDetailDao;
    @Override
    public Page<DateStockDetail> findPage(Page<DateStockDetail> page, List<PropertyFilter> filters) {
        return this.dateStockDetailDao.findPage(page,filters);
    }

    @Override
    public void save(DateStockDetail entity) {

    }

    @Override
    public DateStockDetail load(String id) {
        return null;
    }

    @Override
    public DateStockDetail get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<DateStockDetail> find(List<PropertyFilter> filters) {
        return this.dateStockDetailDao.find(filters);
    }

    @Override
    public List<DateStockDetail> getAll() {
        return this.getAll();
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(DateStockDetail entity) {

    }

    @Override
    public void delete(DateStockDetail entity) {

    }

    @Override
    public void delete(String id) {

    }
}
