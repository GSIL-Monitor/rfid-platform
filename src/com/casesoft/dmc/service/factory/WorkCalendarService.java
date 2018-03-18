package com.casesoft.dmc.service.factory;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.factory.WorkCalendarDao;
import com.casesoft.dmc.model.factory.WorkCalendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Alvin-PC on 2017/2/22 0022.
 */

@Service
@Transactional
public class WorkCalendarService extends AbstractBaseService<WorkCalendar,String> {

    @Autowired
    private WorkCalendarDao workCalendarDao;
    @Override
    public Page<WorkCalendar> findPage(Page<WorkCalendar> page, List<PropertyFilter> filters) {
        return this.workCalendarDao.findPage(page, filters);
    }

    @Override
    public void save(WorkCalendar entity) {
        this.workCalendarDao.saveOrUpdate(entity);
    }

    @Override
    public WorkCalendar load(String id) {
        return this.workCalendarDao.load(id);
    }

    @Override
    public WorkCalendar get(String propertyName, Object value) {
        return this.workCalendarDao.findUniqueBy(propertyName, value);
    }

    @Override
    public List<WorkCalendar> find(List<PropertyFilter> filters) {
        return this.workCalendarDao.find(filters);
    }

    @Override
    public List<WorkCalendar> getAll() {
        return this.workCalendarDao.getAll();
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(WorkCalendar entity) {

    }

    @Override
    public void delete(WorkCalendar entity) {

    }

    @Override
    public void delete(String id) {

    }

    public void save(List<WorkCalendar> list) {
        this.workCalendarDao.doBatchInsert(list);
    }
}
