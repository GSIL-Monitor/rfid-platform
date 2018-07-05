package com.casesoft.dmc.service.shop;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.shop.GuestValueChangeDao;
import com.casesoft.dmc.extend.third.request.BaseService;
import com.casesoft.dmc.model.logistics.SaleOrderBill;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.shop.GuestValueChange;
import com.casesoft.dmc.model.sys.Unit;
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

    public void saveValueChange(SaleOrderBill saleOrderBill, Unit unit, Customer customer) {
        GuestValueChange change = load(saleOrderBill.getId() + "-" + saleOrderBill.getDestUnitId());
        if(CommonUtil.isBlank(change)) {
            change = new GuestValueChange();
            change.setId(saleOrderBill.getId()+"-"+saleOrderBill.getDestUnitId());
            change.setOrderId(saleOrderBill.getId());
            change.setUnitId(saleOrderBill.getDestUnitId());
            if(CommonUtil.isNotBlank(CacheManager.getUnitById(saleOrderBill.getDestUnitId()))){
                change.setUnitName(CacheManager.getUnitById(saleOrderBill.getDestUnitId()).getName());
            }
            change.setRecordDate(new Date());
        }
        change.setActPrice(saleOrderBill.getActPrice());
        change.setPayPrice(saleOrderBill.getPayPrice());
        Double diffPrice = saleOrderBill.getActPrice() - saleOrderBill.getPayPrice();
        change.setDiffPrice(diffPrice);
        Double preBalance = 0D;
        if(CommonUtil.isNotBlank(unit)){
            preBalance = unit.getOwingValue();
        }else if(CommonUtil.isNotBlank(customer)){
            preBalance = customer.getOwingValue();
        }
        change.setPreBalance(preBalance);
        change.setAfterBalance(preBalance + diffPrice);
        change.setStatus(Constant.ChangeRecordStatus.normal);
        change.setUpdateAt(new Date());
        save(change);
    }
}
