package com.casesoft.dmc.service.shop;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.shop.DressRecordDao;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.shop.DressRecord;
import com.casesoft.dmc.model.stock.EpcStock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by yushen on 2017/9/8.
 */
@Service
@Transactional
public class DressRecordService extends AbstractBaseService<DressRecord, String> {

    @Autowired
    private DressRecordDao dressRecordDao;

    @Override
    public Page<DressRecord> findPage(Page<DressRecord> page, List<PropertyFilter> filters) {
        return this.dressRecordDao.findPage(page, filters);
    }

    @Override
    public void save(DressRecord entity) {
        this.dressRecordDao.saveOrUpdate(entity);
    }

    @Override
    public DressRecord load(String id) {
        return null;
    }

    @Override
    public DressRecord get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<DressRecord> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<DressRecord> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(DressRecord entity) {

    }

    @Override
    public void delete(DressRecord entity) {

    }

    @Override
    public void delete(String id) {

    }

    public EpcStock findStockEpcByCode(String code, String wareHouseId) {
        return this.dressRecordDao.findUnique("from EpcStock where code=? and warehouseId=?", code, wareHouseId);
    }

    public EpcStock findStockEpcInHouseByCode(String code, String wareHouseId) {
        return this.dressRecordDao.findUnique("from EpcStock where code=? and warehouseId=? and inStock=1", code, wareHouseId);
    }

    public EpcStock findStockEpcNotInHouseByCode(String code, String wareHouseId) {
        return this.dressRecordDao.findUnique("from EpcStock where code=? and warehouseId=? and inStock=1 and dressingStatus=1", code, wareHouseId);
    }

    public DressRecord findDressRecord(String code, String businessId) {
        return this.dressRecordDao.findUnique("from DressRecord where dressCode=? and businessId=? and status=1", code, businessId);
    }

    public String findWarehouseByCode(String ownerId) {
        return this.dressRecordDao.findUnique("select defaultWarehId from Unit where id=?", ownerId);
    }

    public String findBusinessNameById(String id) {
        return this.dressRecordDao.findUnique("select name from User where id=?", id);
    }

    public void saveEpcStock(EpcStock epcStock){
        this.dressRecordDao.saveOrUpdateX(epcStock);
    }

    public void dressing(DressRecord dressRecord, EpcStock epcStock) {
        String businessName = this.findBusinessNameById(dressRecord.getBusinessId());
        Style style = CacheManager.getStyleById(dressRecord.getStyleId());
        dressRecord.setId(new GuidCreator().toString());
        dressRecord.setStyleId(epcStock.getStyleId());
        if (CommonUtil.isNotBlank(style)) {
            dressRecord.setSizeName(style.getStyleName());
        }
        dressRecord.setColorId(epcStock.getColorId());
        dressRecord.setSizeId(epcStock.getSizeId());
        dressRecord.setRecordStartTime(new Date());
        dressRecord.setBusinessName(businessName);
        dressRecord.setStatus(Constant.DressingStatus.Dressing);
        this.save(dressRecord);

        epcStock.setDressingStatus(Constant.DressingStatus.Dressing);
        this.saveEpcStock(epcStock);
    }

    public void returnBack(DressRecord dressRecord, EpcStock epcStock) {
        dressRecord.setStatus(Constant.DressingStatus.Returned);
        dressRecord.setRecordEndTime(new Date());
        this.save(dressRecord);
        epcStock.setDressingStatus(Constant.DressingStatus.Returned);
        this.saveEpcStock(epcStock);
    }
}
