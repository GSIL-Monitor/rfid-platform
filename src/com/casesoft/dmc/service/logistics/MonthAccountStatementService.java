package com.casesoft.dmc.service.logistics;

import com.casesoft.dmc.controller.logistics.MonthAccountUtil;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.logistics.MonthAccountStatementDao;
import com.casesoft.dmc.model.logistics.MonthAccountStatement;
import com.casesoft.dmc.model.logistics.PurchaseOrderBill;
import com.casesoft.dmc.model.sys.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by yushen on 2017/7/8.
 */
@Service
@Transactional
public class MonthAccountStatementService implements IBaseService<MonthAccountStatement, String> {

    @Autowired
    private MonthAccountStatementDao monthAccountStatementDao;

    @Override
    public Page<MonthAccountStatement> findPage(Page<MonthAccountStatement> page, List<PropertyFilter> filters) {
        return this.monthAccountStatementDao.findPage(page, filters);
    }

    @Override
    public void save(MonthAccountStatement entity) {
        this.monthAccountStatementDao.save(entity);
    }

    @Override
    public MonthAccountStatement load(String id) {
        return this.monthAccountStatementDao.load(id);
    }

    @Override
    public MonthAccountStatement get(String propertyName, Object value) {
        return this.monthAccountStatementDao.findUniqueBy(propertyName, value);
    }

    @Override
    public List<MonthAccountStatement> find(List<PropertyFilter> filters) {
        return this.monthAccountStatementDao.find(filters);
    }

    @Override
    public List<MonthAccountStatement> getAll() {
        return this.monthAccountStatementDao.getAll();
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(MonthAccountStatement entity) {
        this.monthAccountStatementDao.saveOrUpdate(entity);
    }

    @Override
    public void delete(MonthAccountStatement entity) {
        this.monthAccountStatementDao.delete(entity);
    }

    @Override
    public void delete(String id) {
        this.monthAccountStatementDao.delete(id);
    }


    //根据月份取出期初账单
    public List<MonthAccountStatement> getStatement(String month) {
        String hql = "from MonthAccountStatement where month=?";
        return this.monthAccountStatementDao.find(hql,new Object[]{month});
    }

    public void batchSave(List<MonthAccountStatement> statements){
        this.monthAccountStatementDao.doBatchInsert(statements);
    }
    public MonthAccountStatement getStatementbymonthaAndunitid(String month,String unitid) {
        String hql = "from MonthAccountStatement where month=? and unitId=?";
        return this.monthAccountStatementDao.findUnique(hql,new Object[]{month,unitid});
        //return this.monthAccountStatementDao.find(hql,new Object[]{month,unitid});
    }

   /* public void cancel(MonthAccountStatement entity,PurchaseOrderBill purchaseOrderBill) {


        Double actPrice = purchaseOrderBill.getActPrice();
        if(CommonUtil.isBlank(actPrice)){
            actPrice=0.0;
        }
        Double payPrice = purchaseOrderBill.getPayPrice();
        if(CommonUtil.isBlank(payPrice)){
            payPrice=0.0;
        }
        *//*Double owingValue = unit.getOwingValue()==null?0.0:unit.getOwingValue();
        unit.setOwingValue(owingValue-(actPrice-payPrice));*//*
        entity.setTotVal(entity.getTotVal()-(actPrice-payPrice));
        this.monthAccountStatementDao.saveOrUpdate(entity);
    }*/

    //add by yushen 公共方法提取，更新月结表数据。
    public void updateMonthAccountData(Date billDate, String unitId, Double diffPrice, Boolean isAdd){
        Map<String, MonthAccountStatement> preMonthAccountMap = new HashMap<>();
        List<MonthAccountStatement> preMonthAcountList = this.monthAccountStatementDao.find("from MonthAccountStatement where unitId = ? order by month  asc", unitId);
        for (MonthAccountStatement m : preMonthAcountList) {
            preMonthAccountMap.put(m.getId(), m);
        }
        List<MonthAccountStatement> updateMonthAccountList = new ArrayList<>();
        MonthAccountUtil.updateMonthAcoutData(billDate, preMonthAccountMap, updateMonthAccountList, unitId, diffPrice, isAdd);
        this.monthAccountStatementDao.doBatchInsert(updateMonthAccountList);
    }
}
