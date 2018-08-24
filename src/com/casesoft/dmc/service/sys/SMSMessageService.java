package com.casesoft.dmc.service.sys;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.sys.SMSMessageDao;
import com.casesoft.dmc.model.sys.SMSMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2017/11/2.
 */
@Service
@Transactional
public class SMSMessageService implements IBaseService<SMSMessage, String> {
    @Autowired
    private SMSMessageDao sMSMessageDao;
    @Override
    public Page<SMSMessage> findPage(Page<SMSMessage> page, List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public void save(SMSMessage entity) {

    }

    @Override
    public SMSMessage load(String id) {
        return null;
    }

    @Override
    public SMSMessage get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<SMSMessage> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<SMSMessage> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(SMSMessage entity) {

    }

    @Override
    public void delete(SMSMessage entity) {

    }

    @Override
    public void delete(String id) {

    }
}
