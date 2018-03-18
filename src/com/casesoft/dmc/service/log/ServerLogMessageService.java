package com.casesoft.dmc.service.log;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.log.ServerLogMessageDao;
import com.casesoft.dmc.model.log.ServerLogMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by WingLi on 2017/1/11.
 */
@Service
@Transactional
public class ServerLogMessageService {
    @Autowired
    private ServerLogMessageDao serverLogMessageDao;
    @Transactional(readOnly = true)
    public Page<ServerLogMessage> findPage(Page<ServerLogMessage> page,
                                         List<PropertyFilter> filters) {
        return this.serverLogMessageDao.findPage(page, filters);
    }


    public void save(ServerLogMessage entity) {
        this.serverLogMessageDao.save(entity);
    }
}
