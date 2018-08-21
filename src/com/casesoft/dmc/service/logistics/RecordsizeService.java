package com.casesoft.dmc.service.logistics;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.logistics.RecordsizeDao;
import com.casesoft.dmc.model.logistics.Recordsize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2018/3/19.
 */
@Service
@Transactional
public class RecordsizeService implements IBaseService<Recordsize, String> {
    @Autowired
    private RecordsizeDao recordsizeDao;
    @Override
    public Page<Recordsize> findPage(Page<Recordsize> page, List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public void save(Recordsize entity) {
        this.recordsizeDao.saveOrUpdate(entity);

    }

    @Override
    public Recordsize load(String id) {
        return null;
    }

    @Override
    public Recordsize get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<Recordsize> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<Recordsize> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(Recordsize entity) {

    }

    @Override
    public void delete(Recordsize entity) {

    }

    @Override
    public void delete(String id) {

    }
}
