package com.casesoft.dmc.service.search;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.search.DetailStockViewDao;
import com.casesoft.dmc.model.search.DetailStockView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WingLi on 2016-01-24.
 */
@Service
@Transactional
public class DetailStockViewService {
    @Autowired
    private DetailStockViewDao stockViewDao;

    @Transactional(readOnly = true)
    public Page<DetailStockView> findPage(Page<DetailStockView> page, List<PropertyFilter> filters, String ownerId) {
        Page<DetailStockView> stockPage = this.stockViewDao.findPage(page, filters);
        if (!ownerId.equals("1")) {
            for (DetailStockView stock : stockPage.getRows()) {
                stock.setPrecast(-1D);
                stock.setPuprice(-1D);
                stock.setWsprice(-1D);
                stock.setStockprice(-1D);
            }
        }
        return stockPage;
    }

    @Transactional(readOnly = true)
    public List<DetailStockView> findAll() {
        return this.stockViewDao.getAll();
    }

    @Transactional(readOnly = true)
    public List<DetailStockView> findStock(String warehId) {
        return this.stockViewDao.find("from DetailStockView where warehId=?", warehId);
    }


    public Map<String, String> stockSumInfoBySku(String sku, String ownerId, String warehId) {
        String numSumhql;
        String valueSumHql;
        Long storedNumSum;
        Double storedValueSum=0D;
        if(CommonUtil.isNotBlank(warehId)){
            numSumhql = "select sum(qty) from DetailStockView where sku like ? and warehId=?";
            valueSumHql = "select sum(stockprice) from DetailStockView where sku like ? and warehId=?";
            storedNumSum = this.stockViewDao.findUnique(numSumhql, '%' + sku + '%', warehId);
            if(ownerId.equals("1")){
                storedValueSum = this.stockViewDao.findUnique(valueSumHql, '%' + sku + '%', warehId);
            }
        }else {
            numSumhql = "select sum(qty) from DetailStockView where sku like ?";
            valueSumHql = "select sum(stockprice) from DetailStockView where sku like ?";
            storedNumSum = this.stockViewDao.findUnique(numSumhql, '%' + sku + '%');
            if(ownerId.equals("1")){
                storedValueSum = this.stockViewDao.findUnique(valueSumHql, '%' + sku + '%');
            }
        }

        Map<String, String> sumInfo = new HashMap<>();
        sumInfo.put("totNum", CommonUtil.isBlank(storedNumSum) ? "0" : storedNumSum.toString());
        sumInfo.put("totValue", CommonUtil.isBlank(storedValueSum) ? "0" : storedValueSum.toString());
        return sumInfo;
    }


    public Map<String,Long> sumStockByStyleId(String styleId, String warehId) {
        Long thisTotQty = this.stockViewDao.findUnique("select sum(qty) from DetailStockView where styleId = ? and warehId=?",new Object[]{styleId,warehId});
        Long otherTotQty = this.stockViewDao.findUnique("select sum(qty) from DetailStockView where styleId = ? and warehId <>?",new Object[]{styleId,warehId});
        Map<String,Long> map = new HashMap<>();
        map.put("thisQty",thisTotQty);
        map.put("otherQty",otherTotQty);
        return map;
    }
}
