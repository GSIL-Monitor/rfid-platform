package com.casesoft.dmc.service.logistics;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.logistics.MonthStatisticsDao;
import com.casesoft.dmc.model.logistics.MonthStatisticsInandOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2017/7/8.
 */
@Service
@Transactional
public class MonthStatisticsService implements IBaseService<MonthStatisticsInandOut,String> {
    @Autowired
    private MonthStatisticsDao monthStatisticsDao;
    @Override
    @Transactional(readOnly = true)
    public Page<MonthStatisticsInandOut> findPage(Page<MonthStatisticsInandOut> page, List<PropertyFilter> filters) {
        return this.monthStatisticsDao.findPage(page, filters);
    }

    @Override
    public void save(MonthStatisticsInandOut entity) {
        this.monthStatisticsDao.saveOrUpdate(entity);

    }

    @Override
    public MonthStatisticsInandOut load(String id) {
        return null;
    }

    @Override
    public MonthStatisticsInandOut get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<MonthStatisticsInandOut> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<MonthStatisticsInandOut> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(MonthStatisticsInandOut entity) {

    }

    @Override
    public void delete(MonthStatisticsInandOut entity) {

    }

    @Override
    public void delete(String id) {

    }

    public Long findMonthStatisticsInandOut(String warehid,String timedate,String sku){
        Object unique = this.monthStatisticsDao.findUnique("select sum(t.monthStock) from MonthStatisticsInandOut t where t.warehId='" + warehid + "' and to_char(t.timedate,'yyyy-MM')='" + timedate + "' and t.sku='" + sku + "'");
        return (Long)unique;
    }

    public Double findMonthStatisticsInandOutstockPrice(String warehid,String timedate,String sku){
        Object unique = this.monthStatisticsDao.findUnique("select sum(t.stockPrice) from MonthStatisticsInandOut t where t.warehId='" + warehid + "' and to_char(t.timedate,'yyyy-MM')='" + timedate + "' and t.sku='" + sku + "'");
        return (Double)unique;
    }
}
