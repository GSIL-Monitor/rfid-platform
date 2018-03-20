package com.casesoft.dmc.service.logistics;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.logistics.MergeReplenishBillDtlDao;
import com.casesoft.dmc.model.logistics.MergeReplenishBill;
import com.casesoft.dmc.model.logistics.MergeReplenishBillDtl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2018/3/19.
 */
@Service
@Transactional
public class MergeReplenishBillDtlService implements IBaseService<MergeReplenishBillDtl, String> {
    @Autowired
    private MergeReplenishBillDtlDao mergeReplenishBillDtlDao;
    @Override
    public Page<MergeReplenishBillDtl> findPage(Page<MergeReplenishBillDtl> page, List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public void save(MergeReplenishBillDtl entity) {
        this.mergeReplenishBillDtlDao.saveOrUpdate(entity);
    }

    @Override
    public MergeReplenishBillDtl load(String id) {
        return null;
    }

    @Override
    public MergeReplenishBillDtl get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<MergeReplenishBillDtl> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<MergeReplenishBillDtl> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(MergeReplenishBillDtl entity) {

    }

    @Override
    public void delete(MergeReplenishBillDtl entity) {

    }

    @Override
    public void delete(String id) {

    }
}
