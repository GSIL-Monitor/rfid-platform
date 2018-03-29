package com.casesoft.dmc.service.logistics;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.logistics.ChangeReplenishBillDtlDao;
import com.casesoft.dmc.dao.logistics.ReplenishBillDtlDao;
import com.casesoft.dmc.model.logistics.ChangeReplenishBillDtl;
import com.casesoft.dmc.model.logistics.ReplenishBill;
import com.casesoft.dmc.model.logistics.ReplenishBillDtl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2018/3/20.
 */
@Service
@Transactional
public class ReplenishBillDtlService implements IBaseService<ReplenishBillDtl, String> {
    @Autowired
    private ReplenishBillDtlDao replenishBillDtlDao;
    @Autowired
    private ChangeReplenishBillDtlDao changeReplenishBillDtlDao;
    @Override
    public Page<ReplenishBillDtl> findPage(Page<ReplenishBillDtl> page, List<PropertyFilter> filters) {
        return this.replenishBillDtlDao.findPage(page,filters);
    }

    @Override
    public void save(ReplenishBillDtl entity) {

    }

    @Override
    public ReplenishBillDtl load(String id) {
        return null;
    }

    @Override
    public ReplenishBillDtl get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<ReplenishBillDtl> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<ReplenishBillDtl> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(ReplenishBillDtl entity) {

    }

    @Override
    public void delete(ReplenishBillDtl entity) {

    }

    @Override
    public void delete(String id) {

    }

    public List<ChangeReplenishBillDtl> findChangeReplenishBillDtl(String replenishNo,String sku){
        String hql="from ChangeReplenishBillDtl t where t.ReplenishNo=? and t.sku=? order by t.billDate desc";
        List<ChangeReplenishBillDtl> unique = this.changeReplenishBillDtlDao.find(hql, new Object[]{replenishNo, sku});
        return unique;

    }

}
