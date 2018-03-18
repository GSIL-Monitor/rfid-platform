package com.casesoft.dmc.service.search;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.search.DetailStockCodeViewDao;
import com.casesoft.dmc.extend.third.request.BaseService;
import com.casesoft.dmc.model.logistics.SaleOrderReturnBill;
import com.casesoft.dmc.model.search.DetailStockChatView;
import com.casesoft.dmc.model.search.DetailStockCodeView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2017/12/12.
 */
@Service
@Transactional
public class DetailStockCodeViewService extends BaseService<DetailStockCodeView, String> {
    @Autowired
    private DetailStockCodeViewDao detailStockCodeViewDao;
    @Override
    public Page<DetailStockCodeView> findPage(Page<DetailStockCodeView> page, List<PropertyFilter> filters) {
        return this.detailStockCodeViewDao.findPage(page, filters);
    }

    @Override
    public void save(DetailStockCodeView entity) {

    }

    @Override
    public DetailStockCodeView load(String id) {
        return null;
    }

    @Override
    public DetailStockCodeView get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<DetailStockCodeView> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<DetailStockCodeView> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(DetailStockCodeView entity) {

    }

    @Override
    public void delete(DetailStockCodeView entity) {

    }

    @Override
    public void delete(String id) {

    }

    public List<Long> sumStyleQty(String styleId, String warehId) {
        return null;
    }
}
