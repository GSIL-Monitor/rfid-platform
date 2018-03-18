package com.casesoft.dmc.dao.factory;

import com.casesoft.dmc.core.dao.BaseHibernateDao;
import com.casesoft.dmc.model.factory.Token;
import org.springframework.stereotype.Repository;

/**
 * Created by GuoJunwen on 2017-05-04.
 */
@Repository
public class FactoryTokenDao extends BaseHibernateDao<Token,String> {
}
