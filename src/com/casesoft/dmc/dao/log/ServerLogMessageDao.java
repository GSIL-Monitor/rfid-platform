package com.casesoft.dmc.dao.log;

import com.casesoft.dmc.core.dao.BaseHibernateDao;
import com.casesoft.dmc.model.log.ServerLogMessage;
import org.springframework.stereotype.Repository;

/**
 * Created by WingLi on 2017/1/11.
 */
@Repository
public class ServerLogMessageDao extends BaseHibernateDao<ServerLogMessage, String> {
}
