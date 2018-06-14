package com.casesoft.dmc.service.pad;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.pad.TemplateMsgDao;
import com.casesoft.dmc.extend.third.request.BaseService;
import com.casesoft.dmc.model.pad.Template.TemplateMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * Created by ltc on 2018/6/14.
 */
@Service
@Transactional
public class TemplateMsgService  extends BaseService<TemplateMsg, String> {
    @Autowired
    private TemplateMsgDao templateMsgDao;
    @Override
    public Page<TemplateMsg> findPage(Page<TemplateMsg> page, List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public void save(TemplateMsg entity) {
        this.templateMsgDao.saveOrUpdate(entity);
    }

    @Override
    public TemplateMsg load(String id) {
        return null;
    }

    @Override
    public TemplateMsg get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<TemplateMsg> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<TemplateMsg> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(TemplateMsg entity) {

    }

    @Override
    public void delete(TemplateMsg entity) {

    }

    @Override
    public void delete(String id) {

    }
}
