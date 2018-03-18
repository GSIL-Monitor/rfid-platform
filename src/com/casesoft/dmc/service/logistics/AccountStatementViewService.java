package com.casesoft.dmc.service.logistics;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.logistics.AccountStatementViewDao;
import com.casesoft.dmc.model.logistics.AccountStatementView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by yushen on 2017/7/8.
 */
@Service
@Transactional
public class AccountStatementViewService implements IBaseService<AccountStatementView,String> {

    @Autowired
    private AccountStatementViewDao accountStatementViewDao;

    @Override
    public Page<AccountStatementView> findPage(Page<AccountStatementView> page, List<PropertyFilter> filters) {
        return this.accountStatementViewDao.findPage(page,filters);
    }

    @Override
    public void save(AccountStatementView entity) {
        this.accountStatementViewDao.save(entity);
    }

    @Override
    public AccountStatementView load(String id) {
        return this.accountStatementViewDao.load(id);
    }

    @Override
    public AccountStatementView get(String propertyName, Object value) {
        return this.accountStatementViewDao.findUniqueBy(propertyName,value);
    }

    @Override
    public List<AccountStatementView> find(List<PropertyFilter> filters) {
        return this.accountStatementViewDao.find(filters);
    }

    @Override
    public List<AccountStatementView> getAll() {
        return this.accountStatementViewDao.getAll();
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(AccountStatementView entity) {
        this.accountStatementViewDao.saveOrUpdate(entity);
    }

    @Override
    public void delete(AccountStatementView entity) {
        this.accountStatementViewDao.delete(entity);
    }

    @Override
    public void delete(String id) {
        this.accountStatementViewDao.delete(id);
    }

    //查询两个时间内的对账流水记录
    public List<AccountStatementView> findStatementsInTime(Date startDate, Date endDate) {
        String hql = "from AccountStatementView where billDate between ? and ?";
        return this.accountStatementViewDao.find(hql,new Object[]{startDate,endDate});
    }



}
