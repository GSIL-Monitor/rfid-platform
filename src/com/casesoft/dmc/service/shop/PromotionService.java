package com.casesoft.dmc.service.shop;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.shop.PromotionDao;
import com.casesoft.dmc.model.shop.PromotionBill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by WinLi on 2017-05-17.
 */
@Service
@Transactional
public class PromotionService extends AbstractBaseService<PromotionBill, String> {

    @Autowired
    private PromotionDao promotionDao;

    @Override
    public Page<PromotionBill> findPage(Page<PromotionBill> page, List<PropertyFilter> filters) {
        return this.promotionDao.findPage(page,filters);
    }

    @Override
    public void save(PromotionBill entity) {

    }

    @Override
    public PromotionBill load(String id) {
        return null;
    }

    @Override
    public PromotionBill get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<PromotionBill> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<PromotionBill> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(PromotionBill entity) {

    }

    @Override
    public void delete(PromotionBill entity) {

    }

    @Override
    public void delete(String id) {

    }
}
