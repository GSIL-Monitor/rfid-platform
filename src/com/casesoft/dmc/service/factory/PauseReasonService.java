package com.casesoft.dmc.service.factory;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.factory.PauseReasonDao;
import com.casesoft.dmc.model.factory.PauseReason;
import com.casesoft.dmc.model.factory.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class PauseReasonService extends AbstractBaseService<PauseReason, String> {
	
	
	@Autowired
	private PauseReasonDao pauseReasonDao;

    @Override
    public Page<PauseReason> findPage(Page<PauseReason> page, List<PropertyFilter> filters) {
        return this.pauseReasonDao.findPage(page, filters);
    }

    @Override
    public void save(PauseReason entity) {
        this.pauseReasonDao.saveOrUpdateX(entity);
    }

    @Override
    public PauseReason load(String id) {
        return this.pauseReasonDao.load(id);
    }

    @Override
    public PauseReason get(String propertyName, Object value) {
        return this.pauseReasonDao.findUniqueBy(propertyName, value);
    }

    @Override
    public List<PauseReason> find(List<PropertyFilter> filters) {
        return this.pauseReasonDao.find(filters);
    }

    @Override
    public List<PauseReason> getAll() {
        return this.pauseReasonDao.getAll();
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(PauseReason entity) {

    }

    @Override
    public void delete(PauseReason entity) {

    }

    @Override
    public void delete(String id) {
        this.pauseReasonDao.delete(id);
    }

    public List<Token> findToken() {
        return this.pauseReasonDao.find("from Token where locked=0 and types like '%P%' order by sortIndex asc");
    }
}
