package com.casesoft.dmc.service.shop;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.shop.GuestValueChangeDao;
import com.casesoft.dmc.extend.third.request.BaseService;
import com.casesoft.dmc.model.logistics.SaleOrderBill;
import com.casesoft.dmc.model.shop.GuestValueChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by yushen on 2018/7/3.
 */
@Service
@Transactional
public class GuestValueChangeService extends BaseService<GuestValueChange, String> {
    @Autowired
    private GuestValueChangeDao guestValueChangeDao;

    @Override
    public Page<GuestValueChange> findPage(Page<GuestValueChange> page, List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public void save(GuestValueChange entity) {
        this.guestValueChangeDao.saveOrUpdate(entity);
    }

    @Override
    public GuestValueChange load(String id) {
        return this.guestValueChangeDao.get(id);
    }

    @Override
    public GuestValueChange get(String propertyName, Object value) {
        return this.guestValueChangeDao.findUniqueBy(propertyName, value);
    }

    @Override
    public List<GuestValueChange> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<GuestValueChange> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(GuestValueChange entity) {

    }

    @Override
    public void delete(GuestValueChange entity) {

    }

    @Override
    public void delete(String id) {

    }

    public List<GuestValueChange> findLatestByOrderIdAndUnitId(String orderId, String unitId){
        String hql = "from GuestValueChange where orderId = ? and unitId = ? order by recordTime desc";
        return this.guestValueChangeDao.find(hql, orderId, unitId);
    }

    //销售单 客户金额变动
    public void saveValueChange(String billId, Double actPrice, Double payPrice, String unitId , Double preBalance, Integer status) {
        //保存变动
        GuestValueChange change = new GuestValueChange();
        change.setId(new GuidCreator().toString());
        change.setOrderId(billId);
        change.setUnitId(unitId);
        change.setActPrice(actPrice);
        change.setPayPrice(payPrice);
        Double diffPrice = actPrice - payPrice;
        change.setDiffPrice(diffPrice);
        change.setPreBalance(preBalance);
        change.setAfterBalance(preBalance + change.getDiffPrice());
        change.setStatus(status);
        change.setUpdateAt(new Date());
        change.setRecordDate(new Date());
        change.setRecordTime((new Date().getTime()));
        this.save(change);
    }

    //销售单 改变客户或者交易金额变动时，客户金额回退
    public void saveValueBackoff(String billId, String unitId , Double preBalance, Integer status) {
        //保存变动
        GuestValueChange change = new GuestValueChange();
        change.setId(new GuidCreator().toString());
        change.setOrderId(billId);
        change.setUnitId(unitId);

        //兼容之前已开订单，没有改动记录的情况
        List<GuestValueChange> preChangeList = this.findLatestByOrderIdAndUnitId(billId, unitId);
        if(CommonUtil.isNotBlank(preChangeList)){
            //上一次变动记录
            GuestValueChange latestChange = preChangeList.get(0);

            change.setActPrice(latestChange.getActPrice());
            change.setPayPrice(latestChange.getPayPrice());
            change.setDiffPrice(0-latestChange.getDiffPrice());
        }else {
            Double preActPrice =  this.guestValueChangeDao.findUnique("select s.actPrice from SaleOrderBill as s where s.billNo = ?", billId);
            Double prePayPrice = this.guestValueChangeDao.findUnique("select s.payPrice from SaleOrderBill as s where s.billNo = ?", billId);
            change.setActPrice(preActPrice);
            change.setPayPrice(prePayPrice);
            Double diffPrice = preActPrice - prePayPrice;
            change.setDiffPrice(0-diffPrice);
        }
        change.setPreBalance(preBalance);
        change.setAfterBalance(preBalance + change.getDiffPrice());
        change.setStatus(status);
        change.setUpdateAt(new Date());
        change.setRecordDate(new Date());
        change.setRecordTime((new Date()).getTime());
        this.save(change);
    }
}
