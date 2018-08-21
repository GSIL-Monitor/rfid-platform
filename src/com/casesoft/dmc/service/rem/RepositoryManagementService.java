package com.casesoft.dmc.service.rem;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.rem.RepositoryManagementDao;
import com.casesoft.dmc.model.rem.RepositoryManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by lly on 2018/7/8.
 */
@Service
@Transactional
public class RepositoryManagementService extends AbstractBaseService<RepositoryManagement, String> {
    @Autowired
    private RepositoryManagementDao repositoryManagementDao;

    @Override
    public Page<RepositoryManagement> findPage(Page<RepositoryManagement> page, List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public void save(RepositoryManagement entity){
        this.repositoryManagementDao.saveOrUpdate(entity);
    }

    @Override
    public RepositoryManagement load(String id) {
        return null;
    }

    @Override
    public RepositoryManagement get(String propertyName, Object value) {
        return repositoryManagementDao.get((String)value);
    }

    @Override
    public List<RepositoryManagement> find(List<PropertyFilter> filters) {
        return repositoryManagementDao.find(filters);
    }

    @Override
    public List<RepositoryManagement> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(RepositoryManagement entity) {

    }

    @Override
    public void delete(RepositoryManagement entity) {

    }

    @Override
    public void delete(String id) {

    }

}
