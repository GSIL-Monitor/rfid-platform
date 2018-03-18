package com.casesoft.dmc.service.logistics;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.logistics.DayStatisticsDao;
import com.casesoft.dmc.model.logistics.DayStatisticsInandOut;
import com.casesoft.dmc.model.logistics.MonthStatisticsInandOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2017/9/29 0029.
 */
@Service
@Transactional
public class DayStatisticsService implements IBaseService<DayStatisticsInandOut,String> {
    @Autowired
    private DayStatisticsDao dayStatisticsDao;
    @Override
    public Page<DayStatisticsInandOut> findPage(Page<DayStatisticsInandOut> page, List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public void save(DayStatisticsInandOut entity) {
         this.dayStatisticsDao.save(entity);
    }

    @Override
    public DayStatisticsInandOut load(String id) {
        return null;
    }

    @Override
    public DayStatisticsInandOut get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<DayStatisticsInandOut> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<DayStatisticsInandOut> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(DayStatisticsInandOut entity) {

    }

    @Override
    public void delete(DayStatisticsInandOut entity) {

    }

    @Override
    public void delete(String id) {

    }
}
