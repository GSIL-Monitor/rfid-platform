package com.casesoft.dmc.extend.third.service;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.extend.third.dao.PlFittingAnalysisViewDao;
import com.casesoft.dmc.extend.third.descriptor.DataResult;
import com.casesoft.dmc.extend.third.model.PlFittingAnalysisView;
import com.casesoft.dmc.extend.third.request.BaseService;
import com.casesoft.dmc.extend.third.request.RequestPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by GuoJunwen on 2017/3/3 0003.
 */
@Service
@Transactional
public class PlFittingAnalysisViewService extends BaseService<PlFittingAnalysisView,String> {
    @Autowired
    private PlFittingAnalysisViewDao plFittingAnalysisViewDao;

    @Override
    public Page<PlFittingAnalysisView> findPage(Page<PlFittingAnalysisView> page, List<PropertyFilter> filters) {
        return null;
    }

    public DataResult find(RequestPageData<?> request) {
        return this.plFittingAnalysisViewDao.find(request);
    }

    @Override
    public void save(PlFittingAnalysisView entity) {

    }

    @Override
    public PlFittingAnalysisView load(String id) {
        return null;
    }

    @Override
    public PlFittingAnalysisView get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<PlFittingAnalysisView> find(List<PropertyFilter> filters) {
        return this.plFittingAnalysisViewDao.find(filters);
    }

    @Override
    public List<PlFittingAnalysisView> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(PlFittingAnalysisView entity) {

    }

    @Override
    public void delete(PlFittingAnalysisView entity) {

    }

    @Override
    public void delete(String id) {

    }
}
