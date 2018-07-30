package com.casesoft.dmc.service.rem;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.rem.UniqueCodeBillDao;
import com.casesoft.dmc.model.rem.UniqueCodeBill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by lly on 2018/7/20.
 */
@Service
@Transactional
public class UniqueCodeBillService extends AbstractBaseService<UniqueCodeBill, String> {
    @Autowired
    private UniqueCodeBillDao uniqueCodeBillDao;

    @Override
    public Page<UniqueCodeBill> findPage(Page<UniqueCodeBill> page, List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public void save(UniqueCodeBill entity) {
        uniqueCodeBillDao.saveOrUpdate(entity);
    }

    @Override
    public UniqueCodeBill load(String id) {
        return null;
    }

    @Override
    public UniqueCodeBill get(String propertyName, Object value) {
        return this.uniqueCodeBillDao.findUniqueBy(propertyName,value);
    }

    @Override
    public List<UniqueCodeBill> find(List<PropertyFilter> filters) {
        return this.uniqueCodeBillDao.find(filters);
    }

    @Override
    public List<UniqueCodeBill> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(UniqueCodeBill entity) {

    }

    @Override
    public void delete(UniqueCodeBill entity) {

    }

    @Override
    public void delete(String id) {

    }
}
