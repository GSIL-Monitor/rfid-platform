package com.casesoft.dmc.service.sys.impl;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.sys.EmailDao;
import com.casesoft.dmc.model.sys.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class EmailService implements IBaseService<Email,String> {

    @Autowired
    private EmailDao emailDao;

    @Transactional(readOnly = true)
    @Override
    public Page<Email> findPage(Page<Email> page, List<PropertyFilter> filters) {
        return this.emailDao.findPage(page,filters);
    }

    @Override
    public void save(Email email) {
       this.emailDao.saveOrUpdate(email);
    }

    public Email findById(String id){
       String hql="from Email e where e.id=?";
        return this.emailDao.findUnique(hql,new Object[]{id});
    }

    @Override
    public Email load(String id) {
        return null;
    }

    @Override
    public Email get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<Email> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<Email> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(Email entity) {

    }

    @Override
    public void delete(Email entity) {

    }

    @Override
    public void delete(String id) {

    }
}
