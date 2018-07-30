package com.casesoft.dmc.service.rem;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.rem.RepositoryManagementBillDao;
import com.casesoft.dmc.model.rem.RepositoryManagementBill;
import com.casesoft.dmc.model.rem.RepositoryManagementBillDtl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by lly on 2018/7/20.
 */
@Service
@Transactional
public class RepositoryManagementBillService extends AbstractBaseService<RepositoryManagementBill, String> {
    @Autowired
    private RepositoryManagementBillDao repositoryManagementBillDao;
    @Override
    public Page<RepositoryManagementBill> findPage(Page<RepositoryManagementBill> page, List<PropertyFilter> filters) {
        return this.repositoryManagementBillDao.findPage(page,filters);
    }

    @Override
    public void save(RepositoryManagementBill entity) {

    }

    public void save(RepositoryManagementBill entity, List<RepositoryManagementBillDtl> repositoryManagementBillDtls) {
        this.repositoryManagementBillDao.batchExecute("delete from RepositoryManagementBillDtl where billNo=?", entity.getBillNo());
        //保存单据明细表
        this.repositoryManagementBillDao.doBatchInsert(repositoryManagementBillDtls);
        this.repositoryManagementBillDao.saveOrUpdate(entity);
    }

    @Override
    public RepositoryManagementBill load(String id) {
        return null;
    }

    @Override
    public RepositoryManagementBill get(String propertyName, Object value) {
        return this.repositoryManagementBillDao.findUniqueBy(propertyName, value);
    }

    @Override
    public List<RepositoryManagementBill> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<RepositoryManagementBill> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(RepositoryManagementBill entity) {
        this.repositoryManagementBillDao.saveOrUpdate(entity);
    }

    @Override
    public void delete(RepositoryManagementBill entity) {

    }

    @Override
    public void delete(String id) {

    }
    public Integer findBillStatus(String billNo) {
        return this.repositoryManagementBillDao.findUnique("select status from RepositoryManagementBill where id = ?", billNo);
    }
}
