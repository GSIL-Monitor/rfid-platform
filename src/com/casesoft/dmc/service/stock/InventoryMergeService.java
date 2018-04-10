package com.casesoft.dmc.service.stock;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.logistics.InventoryBillDao;
import com.casesoft.dmc.dao.stock.EpcStockDao;
import com.casesoft.dmc.dao.stock.InventoryMergeDao;
import com.casesoft.dmc.dao.task.BusinessDtlDao;
import com.casesoft.dmc.dao.task.TaskDao;
import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.logistics.InventoryBill;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.stock.EpcStock;
import com.casesoft.dmc.model.stock.InventoryMergeBill;
import com.casesoft.dmc.model.stock.InventoryMergeBillDtl;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.BusinessDtl;
import com.casesoft.dmc.model.task.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yushen on 2017/12/4.
 */
@Service
@Transactional
public class InventoryMergeService implements IBaseService<InventoryMergeBill, String> {
    @Autowired
    private InventoryMergeDao inventoryMergeDao;

    @Autowired
    private InventoryBillDao inventoryBillDao;
    @Autowired
    private EpcStockDao epcStockDao;
    @Autowired
    private TaskDao taskDao;

    @Override
    public Page<InventoryMergeBill> findPage(Page<InventoryMergeBill> page, List<PropertyFilter> filters) {
        Page<InventoryMergeBill> mergeBillPage = this.inventoryMergeDao.findPage(page, filters);
        for (InventoryMergeBill mergeBill :
                mergeBillPage.getRows()) {
            Unit unit = CacheManager.getUnitById(mergeBill.getWarehouseId());
            if (CommonUtil.isNotBlank(unit)) {
                mergeBill.setWarehouseName(unit.getName());
            }
        }
        return mergeBillPage;
    }

    @Override
    public void save(InventoryMergeBill entity) {
        this.inventoryMergeDao.saveOrUpdateX(entity);

    }

    public void save(InventoryMergeBill mergeBill, List<InventoryMergeBillDtl> mergeBillDtlList) {
        this.save(mergeBill);
        this.inventoryMergeDao.doBatchInsert(mergeBillDtlList);
        if (CommonUtil.isNotBlank(mergeBill.getInventoryBillList())) {
            this.inventoryMergeDao.doBatchInsert(mergeBill.getInventoryBillList());
        }
    }

    public void save( List<InventoryMergeBillDtl> mergeBillDtlList){
        this.inventoryMergeDao.doBatchInsert(mergeBillDtlList);
    }

    @Override
    public InventoryMergeBill load(String id) {
        return null;
    }

    @Override
    public InventoryMergeBill get(String propertyName, Object value) {
        return this.inventoryMergeDao.findUniqueBy(propertyName, value);
    }

    @Override
    public List<InventoryMergeBill> find(List<PropertyFilter> filters) {
        return null;
    }

    public void update(InventoryMergeBillDtl entity){
    }

    public List<InventoryMergeBillDtl> findMergeBillDtl(String id, boolean isChecked, String sku) {
        if (CommonUtil.isBlank(sku)) {
            sku = "";
        }
        List<InventoryMergeBillDtl> mergeBillDtlList;
        if (isChecked) {
            mergeBillDtlList = this.inventoryMergeDao.find("from InventoryMergeBillDtl where billNo=? and inStock = 0 and sku like ?", id, "%" + sku + "%");
        } else {
            mergeBillDtlList = this.inventoryMergeDao.find("from InventoryMergeBillDtl where billNo=? and sku like ?", id, "%" + sku + "%");
        }
        for (InventoryMergeBillDtl mergeBillDtl : mergeBillDtlList) {

            Style style = CacheManager.getStyleById(mergeBillDtl.getStyleId());
            if (CommonUtil.isNotBlank(style)) {
                mergeBillDtl.setStyleName(style.getStyleName());
                mergeBillDtl.setPrice(style.getPrice());
            }
        }
        return mergeBillDtlList;
    }

    @Override
    public List<InventoryMergeBill> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(InventoryMergeBill entity) {

    }

    @Override
    public void delete(InventoryMergeBill entity) {

    }

    @Override
    public void delete(String id) {

    }

    public String findMaxMGBillNo(String prefix) {
        String hql = "select max(CAST(SUBSTRING(billNo,9),integer)) from InventoryMergeBill where billNo like ?";
        Integer code = this.inventoryMergeDao.findUnique(hql, prefix + '%');
        return code == null ? (prefix + "001") : prefix + CommonUtil.convertIntToString(code + 1, 3);
    }

    public List<InventoryMergeBillDtl> mergeBillDtl(String billNoListStr) {
        String hql = "select new com.casesoft.dmc.model.stock.InventoryMergeBillDtl(inventoryrecord.sku, inventoryrecord.styleId, inventoryrecord.colorId, inventoryrecord.sizeId, inventoryrecord.code, \n" +
                "count(inventoryrecord.code), sum(inventoryrecord.isScanned), \n" +
                "(CASE WHEN sum(isScanned) > 0 THEN 1 ELSE 0 END))\n" +
                "from InventoryRecord inventoryrecord where " + billNoListStr + "\n" +
                "group by inventoryrecord.sku, inventoryrecord.styleId, inventoryrecord.colorId, inventoryrecord.sizeId, inventoryrecord.code";
        return this.inventoryMergeDao.find(hql);
    }


    public List<String> findOrigBillList(String billNo) {
        return this.inventoryMergeDao.find("select inventoryBillNo from InventoryMergeOrigBill where mergeBillNo=?", billNo);
    }

    public List<Bill> findErpBillByBillNoList(String billNoListStr) {
        return this.inventoryMergeDao.find("from Bill bill where" + billNoListStr);
    }

    public void mergeBillDtlBill(List<InventoryMergeBillDtl> inventoryMergeBillDtlList, List<InventoryBill> inventoryBillList, List<Business> businessList,List<EpcStock> epcStockList) {
        this.inventoryMergeDao.doBatchInsert(inventoryMergeBillDtlList);
        this.taskDao.doBatchInsert(inventoryBillList);
        this.epcStockDao.doBatchInsert(epcStockList);
        this.taskDao.doBatchInsert(businessList);

        List<BusinessDtl> dtlList = new ArrayList<>();
        List<Record> recordList = new ArrayList<>();
        for (Business business :businessList){
            List<BusinessDtl> businessDtlList = business.getDtlList();
            dtlList.addAll(businessDtlList);
            List<Record> rdList = business.getRecordList();
            recordList.addAll(rdList);
        }
        this.taskDao.doBatchInsert(dtlList);
        this.taskDao.doBatchInsert(recordList);
    }
}
