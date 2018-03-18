package com.casesoft.dmc.dao.log;

import org.springframework.stereotype.Repository;

import com.casesoft.dmc.core.dao.BaseHibernateDao;
import com.casesoft.dmc.model.log.RfidLogMessage;

@Repository
public class RfidLogMessageDao extends BaseHibernateDao<RfidLogMessage, String> {

}
