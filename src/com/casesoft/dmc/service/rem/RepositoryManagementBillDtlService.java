package com.casesoft.dmc.service.rem;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.rem.RepositoryManagementBillDtlDao;
import com.casesoft.dmc.model.rem.RepositoryManagementBillDtl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by boy on 2018/7/20.
 */
@Service
@Transactional
public class RepositoryManagementBillDtlService extends AbstractBaseService<RepositoryManagementBillDtl, String> {
    @Autowired
    private RepositoryManagementBillDtlDao repositoryManagementBillDtlDao;
    @Override
    public Page<RepositoryManagementBillDtl> findPage(Page<RepositoryManagementBillDtl> page, List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public void save(RepositoryManagementBillDtl entity) {
        repositoryManagementBillDtlDao.saveOrUpdate(entity);
    }

    @Override
    public RepositoryManagementBillDtl load(String id) {
        return null;
    }

    @Override
    public RepositoryManagementBillDtl get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<RepositoryManagementBillDtl> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<RepositoryManagementBillDtl> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(RepositoryManagementBillDtl entity) {

    }

    @Override
    public void delete(RepositoryManagementBillDtl entity) {

    }

    @Override
    public void delete(String id) {

    }
    public List<RepositoryManagementBillDtl> findBillDtlByBillNo(String billNo) {
        return this.repositoryManagementBillDtlDao.find("from  RepositoryManagementBillDtl where billNo=?", new Object[]{billNo});
    }
}
