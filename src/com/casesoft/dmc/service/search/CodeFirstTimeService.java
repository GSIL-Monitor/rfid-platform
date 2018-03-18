package com.casesoft.dmc.service.search;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.search.CodeFirstTimeDao;
import com.casesoft.dmc.model.stock.CodeFirstTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yushen on 2017/12/13.
 */
@Service
@Transactional
public class CodeFirstTimeService implements IBaseService<CodeFirstTime,String> {
    @Autowired
    private CodeFirstTimeDao codeFirstTimeDao;

    @Override
    public Page<CodeFirstTime> findPage(Page<CodeFirstTime> page, List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public void save(CodeFirstTime entity) {

    }

    @Override
    public CodeFirstTime load(String id) {
        return null;
    }

    @Override
    public CodeFirstTime get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<CodeFirstTime> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<CodeFirstTime> getAll() {
        return this.codeFirstTimeDao.getAll();
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(CodeFirstTime entity) {

    }

    @Override
    public void delete(CodeFirstTime entity) {

    }

    @Override
    public void delete(String id) {

    }

    public List<CodeFirstTime> findByIds (String idListStr){
        String hql = "from CodeFirstTime codefirsttime where" + idListStr;
        return this.codeFirstTimeDao.find(hql);
    }
}
