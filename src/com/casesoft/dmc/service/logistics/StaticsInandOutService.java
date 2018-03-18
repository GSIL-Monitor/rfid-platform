package com.casesoft.dmc.service.logistics;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.logistics.StaticsInandOutDao;
import com.casesoft.dmc.model.logistics.StatisticsInandOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by chenzhifan on 2017/7/8.
 */
@Service
@Transactional
public class StaticsInandOutService implements IBaseService<StatisticsInandOut,String> {
    @Autowired
    private StaticsInandOutDao staticsInandOutDao;
    @Override
    public Page<StatisticsInandOut>  findPage(Page<StatisticsInandOut> page, List<PropertyFilter> filters) {
        return this.staticsInandOutDao.findPage(page, filters);
    }

    @Override
    public void save(StatisticsInandOut entity) {

    }

    @Override
    public StatisticsInandOut load(String id) {
        return null;
    }

    @Override
    public StatisticsInandOut get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<StatisticsInandOut> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<StatisticsInandOut> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(StatisticsInandOut entity) {

    }

    @Override
    public void delete(StatisticsInandOut entity) {

    }

    @Override
    public void delete(String id) {

    }

    public Long  findMonthStatisticsInandOut(String warehid,String begintime,String endtime,String sku){
        Object unique = this.staticsInandOutDao.findUnique("select sum(t.stockQty) from StatisticsInandOut t where t.warehId='" + warehid + "' and '" + begintime + "'<=to_char(t.timedate,'yyyy-MM-dd')and to_char(t.timedate,'yyyy-MM-dd')<='" + endtime + "' and t.sku='" + sku + "'");
        return (Long)unique;
    }

    public Long  findMonthStatisticsInandOutPrice(String warehid,String begintime,String endtime,String sku){
        Object unique = this.staticsInandOutDao.findUnique("select sum(t.preval) from StatisticsInandOut t where t.warehId='" + warehid + "' and '" + begintime + "'<=to_char(t.timedate,'yyyy-MM-dd')and to_char(t.timedate,'yyyy-MM-dd')<='" + endtime + "' and t.sku='" + sku + "'");
        return (Long)unique;
    }
}
