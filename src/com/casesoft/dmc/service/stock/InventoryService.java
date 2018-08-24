package com.casesoft.dmc.service.stock;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.stock.InventoryDao;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.stock.InventoryRecord;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.BusinessDtl;
import com.casesoft.dmc.model.task.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class InventoryService implements IBaseService<Business, String> {
    @Autowired
    private InventoryDao inventoryDao;

    @Transactional(readOnly = true)
    @Override
    public Page<Business> findPage(Page<Business> page, List<PropertyFilter> filters) {
        return inventoryDao.findPage(page, filters);
    }

    @Transactional(readOnly = true)
    public Business findById(String id) {
        return this.inventoryDao.findUniqueBy("id", id);//.find(hql,new Object[]{id});
    }

    @Transactional(readOnly = true)
    public List<BusinessDtl> findBusinessDtl(String taskId, String filter_LIKES_styleId, String filter_LIKES_sku) {
        String hql = "from BusinessDtl busdtl where busdtl.taskId=? and busdtl.styleId like ? and busdtl.sku like ?";
        return this.inventoryDao.find(hql, new Object[]{taskId, "%" + filter_LIKES_styleId + "%", "%" + filter_LIKES_sku + "%"});
    }

    @Transactional(readOnly = true)
    public List<Record> findcodedetailPage(String taskId, String sku) {
        String hql = "from Record t where t.taskId=? and t.sku like ?";
        return this.inventoryDao.find(hql, new Object[]{taskId, "%" + sku + "%"});
    }

    @Transactional(readOnly = true)
    public List<Record> findRecord(String taskId) {
        String hql = "from Record r where r.taskId=?";
        return this.inventoryDao.find(hql, new Object[]{taskId});
    }


    @Override
    public void save(Business entity) {

    }

    @Override
    public Business load(String id) {
        return null;
    }

    @Override
    public Business get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<Business> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<Business> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(Business entity) {

    }

    @Override
    public void delete(Business entity) {

    }

    @Override
    public void delete(String id) {

    }

    public void updateEpcStockInStock(String codes) {
        String hql = "update EpcStock t set t.inStock=0 where t.code in" + codes;
        this.inventoryDao.batchExecute(hql);
    }

    /* public void updateEpcStockInStocks(String codes){
         String hql="update EpcStock t set t.inStock=0 where t.code in ("+codes+")";
         this.inventoryDao.batchExecute(hql);
     }*/
    public void updateEpcStockInStockss(String codes) {
        String hql = "update EpcStock t set t.inStock=0 where t.code in (?)";
        this.inventoryDao.batchExecute(hql, codes);
    }

    public void updateEpcStockInsStockss(String codes) {
        String hql = "update EpcStock t set t.inStock=1 where t.code in (?)";
        this.inventoryDao.batchExecute(hql, codes);
    }

    public List<InventoryRecord> findInventoryRecord(String billNo, boolean isChecked, String sku) {
        List<InventoryRecord> inventoryRecordList;
        if (isChecked) {
            inventoryRecordList = this.inventoryDao.find("from InventoryRecord where billNo=? and (isScanned=0 or isScanned is null) and sku like ?", billNo, "%" + sku + "%");
        } else {
            inventoryRecordList = this.inventoryDao.find("from InventoryRecord where billNo=? and sku like ?", billNo, "%" + sku + "%");
        }
        for (InventoryRecord inventoryRecord: inventoryRecordList) {
            Style style = CacheManager.getStyleById(inventoryRecord.getStyleId());
            if(CommonUtil.isNotBlank(style)){
                inventoryRecord.setStyleName(style.getStyleName());
            }
        }
        return inventoryRecordList;
    }
}
