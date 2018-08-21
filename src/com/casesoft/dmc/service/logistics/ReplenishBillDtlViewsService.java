package com.casesoft.dmc.service.logistics;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.logistics.ReplenishBillDtlViewsDao;
import com.casesoft.dmc.model.logistics.ReplenishBillDtlViews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2018/3/20.
 */
@Service
@Transactional
public class ReplenishBillDtlViewsService implements IBaseService<ReplenishBillDtlViews, String> {
    @Autowired
    private ReplenishBillDtlViewsDao replenishBillDtlViewsDao;
    @Override
    public Page<ReplenishBillDtlViews> findPage(Page<ReplenishBillDtlViews> page, List<PropertyFilter> filters) {
        return this.replenishBillDtlViewsDao.findPage(page,filters);
    }

    @Override
    public void save(ReplenishBillDtlViews entity) {

    }

    @Override
    public ReplenishBillDtlViews load(String id) {
        return null;
    }

    @Override
    public ReplenishBillDtlViews get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<ReplenishBillDtlViews> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<ReplenishBillDtlViews> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(ReplenishBillDtlViews entity) {

    }

    @Override
    public void delete(ReplenishBillDtlViews entity) {

    }

    @Override
    public void delete(String id) {

    }
}
