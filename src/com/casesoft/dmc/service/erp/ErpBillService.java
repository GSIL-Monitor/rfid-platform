package com.casesoft.dmc.service.erp;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.erp.ErpBillDao;
import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.erp.BillDtl;
import com.casesoft.dmc.model.product.Style;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yushen on 2017/10/30.
 */
@Service
@Transactional
public class ErpBillService implements IBaseService<Bill, String> {

    @Autowired
    private ErpBillDao erpBillDao;

    @Override
    public Page<Bill> findPage(Page<Bill> page, List<PropertyFilter> filters) {
        return this.erpBillDao.findPage(page, filters);
    }

    public Bill findErpBillById(String id) {
        return this.erpBillDao.findUniqueBy("id", id);
    }

    @Override
    public void save(Bill entity) {

    }

    @Override
    public Bill load(String id) {
        return null;
    }

    @Override
    public Bill get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<Bill> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<Bill> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(Bill entity) {

    }

    @Override
    public void delete(Bill entity) {

    }

    @Override
    public void delete(String id) {

    }

    public List<BillDtl> findErpBillDtl(String billNo, boolean isChecked, String sku) {
        if(CommonUtil.isBlank(sku)){
            sku="";
        }
        List<BillDtl> billDtlList;
        if (isChecked) {
            billDtlList = this.erpBillDao.find("from BillDtl where billNo=? and qty > actQty and sku like ?", billNo, "%" + sku + "%");
        } else {
            billDtlList = this.erpBillDao.find("from BillDtl where billNo=? and sku like ?", billNo, "%" + sku + "%");
        }
        for (BillDtl billDtl: billDtlList) {
            Style style = CacheManager.getStyleById(billDtl.getStyleId());
            if(CommonUtil.isNotBlank(style)){
                billDtl.setStyleName(style.getStyleName());
                billDtl.setPrice(style.getPrice());
                billDtl.setDiffQty(billDtl.getQty() - billDtl.getActQty());
            }
        }
        return billDtlList;
    }
}
