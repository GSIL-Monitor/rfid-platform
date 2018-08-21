package com.casesoft.dmc.service.logistics;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.logistics.FranchiseeBillDao;
import com.casesoft.dmc.model.logistics.SaleOrderBill;
import com.casesoft.dmc.model.stock.EpcStock;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2017/10/18.
 */
@Service
@Transactional
public class FranchiseeBillService implements IBaseService<SaleOrderBill, String> {
    @Autowired
    private FranchiseeBillDao franchiseeBillDao;
    @Override
    public Page<SaleOrderBill> findPage(Page<SaleOrderBill> page, List<PropertyFilter> filters) {
        return this.franchiseeBillDao.findPage(page, filters);
    }

    @Override
    public void save(SaleOrderBill entity) {

    }

    @Override
    public SaleOrderBill load(String id) {
        return null;
    }

    @Override
    public SaleOrderBill get(String propertyName, Object value) {
        return this.franchiseeBillDao.findUniqueBy(propertyName, value);
    }

    @Override
    public List<SaleOrderBill> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<SaleOrderBill> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(SaleOrderBill entity) {

    }

    @Override
    public void delete(SaleOrderBill entity) {

    }

    @Override
    public void delete(String id) {

    }

    public List<Business> findBusiness(String billno){
        String hql = "from Business b where b.billId=? and b.type=0";
        return this.franchiseeBillDao.find(hql,new Object[] {billno});
    }
    public List<Record> findRecord(String inSql){
        String hql = "from Record record  where "+inSql;
        return this.franchiseeBillDao.find(hql);
    }
    public List<EpcStock> findEpcStock(String code){
        String hql = "from EpcStock b where b.code=?";
        return this.franchiseeBillDao.find(hql,new Object[] {code});
    }
}
