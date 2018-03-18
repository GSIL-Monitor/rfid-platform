package com.casesoft.dmc.service.logistics;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.logistics.MonthInitialAdjustmentDao;
import com.casesoft.dmc.model.logistics.MonthAccountStatement;
import com.casesoft.dmc.model.logistics.MonthInitialAdjustment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yushen on 2017/10/20.
 */
@Service
@Transactional
public class MonthInitialAdjustmentService implements IBaseService<MonthInitialAdjustment, String> {

    @Autowired
    private MonthInitialAdjustmentDao monthInitialAdjustmentDao;
    @Override
    public Page<MonthInitialAdjustment> findPage(Page<MonthInitialAdjustment> page, List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public void save(MonthInitialAdjustment entity) {
        this.monthInitialAdjustmentDao.saveOrUpdate(entity);
    }

    public void saveAdjustment(MonthInitialAdjustment mia, MonthAccountStatement mas){
        this.save(mia);
        this.monthInitialAdjustmentDao.saveOrUpdateX(mas);
    }

    @Override
    public MonthInitialAdjustment load(String id) {
        return null;
    }

    @Override
    public MonthInitialAdjustment get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<MonthInitialAdjustment> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<MonthInitialAdjustment> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(MonthInitialAdjustment entity) {

    }

    @Override
    public void delete(MonthInitialAdjustment entity) {

    }

    @Override
    public void delete(String id) {

    }
}
