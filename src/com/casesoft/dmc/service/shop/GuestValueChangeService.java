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
        String hql = "from GuestValueChange where orderId = ? and unitId = ? order by updateAt desc";
        return this.guestValueChangeDao.find(hql, orderId, unitId);
    }

    //销售单 客户金额变动
    public void saveValueChange(SaleOrderBill saleOrderBill, String unitId , Double preBalance) {
        //保存变动
        GuestValueChange change = new GuestValueChange();
        change.setId(new GuidCreator().toString());
        change.setOrderId(saleOrderBill.getId());
        change.setUnitId(unitId);
        if(CommonUtil.isNotBlank(CacheManager.getUnitById(unitId))){
            change.setUnitName(CacheManager.getUnitById(unitId).getName());
        }
        change.setActPrice(saleOrderBill.getActPrice());
        change.setPayPrice(saleOrderBill.getPayPrice());
        Double diffPrice = saleOrderBill.getActPrice() - saleOrderBill.getPayPrice();
        change.setDiffPrice(diffPrice);
        change.setPreBalance(preBalance);
        change.setAfterBalance(preBalance + change.getDiffPrice());
        change.setStatus(Constant.ChangeRecordStatus.normal);
        change.setUpdateAt(new Date());
        change.setRecordDate(new Date());
        this.save(change);
    }

    //销售单 改变客户或者交易金额变动时，客户金额回退
    public void saveValueBackoff(SaleOrderBill saleOrderBill, String unitId , Double preBalance) {
        //兼容之前已开订单，没有改动记录的情况
        List<GuestValueChange> preChangeList = this.findLatestByOrderIdAndUnitId(saleOrderBill.getId(), unitId);
        if(CommonUtil.isNotBlank(preChangeList)){
            //上一次变动记录
            GuestValueChange latestChange = preChangeList.get(0);

            //保存变动
            GuestValueChange change = new GuestValueChange();
            change.setId(new GuidCreator().toString());
            change.setOrderId(saleOrderBill.getId());
            change.setUnitId(unitId);
            if(CommonUtil.isNotBlank(CacheManager.getUnitById(unitId))){
                change.setUnitName(CacheManager.getUnitById(unitId).getName());
            }
            change.setActPrice(latestChange.getActPrice());
            change.setPayPrice(latestChange.getPayPrice());
            change.setDiffPrice(0-latestChange.getDiffPrice());
            change.setPreBalance(preBalance);
            change.setAfterBalance(preBalance + change.getDiffPrice());
            change.setStatus(Constant.ChangeRecordStatus.normal);
            change.setUpdateAt(new Date());
            change.setRecordDate(new Date());
            this.save(change);
        }
    }
}
