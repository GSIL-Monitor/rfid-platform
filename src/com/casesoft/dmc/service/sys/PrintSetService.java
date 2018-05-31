package com.casesoft.dmc.service.sys;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.sys.PrintSetDao;
import com.casesoft.dmc.model.sys.PrintSet;
import com.casesoft.dmc.model.sys.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2018/5/31.
 */
@Service
@Transactional
public class PrintSetService implements IBaseService<PrintSet,String> {
    @Autowired
    private PrintSetDao printSetDao;
    @Override
    public Page<PrintSet> findPage(Page<PrintSet> page, List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public void save(PrintSet entity) {

    }

    @Override
    public PrintSet load(String id) {
        return null;
    }

    @Override
    public PrintSet get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<PrintSet> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<PrintSet> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(PrintSet entity) {

    }

    @Override
    public void delete(PrintSet entity) {

    }

    @Override
    public void delete(String id) {

    }

    public void savePrintSetMessage( PrintSet entity){

        this.printSetDao.saveOrUpdate(entity);
    }

    public PrintSet findPrintSet(String ruleReceipt,String type,String ownerId){
        String hql="from PrintSet t where t.ownerId=? and t.type=? and t.ruleReceipt=?";
        PrintSet printSet = this.printSetDao.findUnique(hql, new Object[]{ownerId, type, ruleReceipt});
        return printSet;
    }
}
