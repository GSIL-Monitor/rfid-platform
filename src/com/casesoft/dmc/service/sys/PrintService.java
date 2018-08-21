package com.casesoft.dmc.service.sys;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.sys.PrintDao;
import com.casesoft.dmc.model.sys.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2017/7/23.
 */
@Service
@Transactional
public class PrintService implements IBaseService<Print,String> {
    @Autowired
    private PrintDao printDao;
    @Override
    public Page<Print> findPage(Page<Print> page, List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public void save(Print entity) {
        this.printDao.save(entity);
    }

    @Override
    public Print load(String id) {
        return null;
    }

    @Override
    public Print get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<Print> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<Print> getAll() {
        return this.printDao.getAll();
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(Print entity) {
        this.printDao.update(entity);
    }

    @Override
    public void delete(Print entity) {

    }

    @Override
    public void delete(String id) {

    }

    public Print findPrint(Long id){

        List<Print> list = this.printDao.find("from Print rr where rr.id=?", new Object[]{id});
        return list.get(0);
    }
}
