package com.casesoft.dmc.service.search;

import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.search.PurchaseSaleStockDao;
import com.casesoft.dmc.model.search.PurchaseSaleStock;
import com.casesoft.dmc.model.search.SaleorderCountView;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Alvin on 2018/2/3.
 */
@Service
@Transactional
public class PurchaseSaleStockService implements IBaseService<PurchaseSaleStock,String>{

    @Autowired
    private PurchaseSaleStockDao purchaseSaleStockDao;
    @Override
    public Page<PurchaseSaleStock> findPage(Page<PurchaseSaleStock> page, List<PropertyFilter> filters) {
        return this.purchaseSaleStockDao.findPage(page, filters);
    }

    @Override
    public void save(PurchaseSaleStock entity) {
    }

    @Override
    public PurchaseSaleStock load(String id) {
        return this.load(id);
    }

    @Override
    public PurchaseSaleStock get(String propertyName, Object value) {
        return this.purchaseSaleStockDao.findUnique(propertyName, value);
    }

    @Override
    public List<PurchaseSaleStock> find(List<PropertyFilter> filters) {
        return this.purchaseSaleStockDao.find(filters);
    }

    @Override
    public List<PurchaseSaleStock> getAll() {
        return this.purchaseSaleStockDao.getAll();
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(PurchaseSaleStock entity) {

    }

    @Override
    public void delete(PurchaseSaleStock entity) {

    }

    @Override
    public void delete(String id) {

    }

    public List<PurchaseSaleStock> exportExcels(String warehId,String sku){
        String hql="from PurchaseSaleStock where 1=1";
        if(CommonUtil.isNotBlank(warehId)){
            hql+=" and warehId='"+warehId+"'";
        }
        if(CommonUtil.isNotBlank(sku)){
            hql+=" and sku like '%"+sku+"%'";
        }
        List<PurchaseSaleStock> PurchaseSaleStocks = this.purchaseSaleStockDao.find(hql);
       return PurchaseSaleStocks;
    }
}
