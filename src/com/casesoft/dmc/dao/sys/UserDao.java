package com.casesoft.dmc.dao.sys;

import org.springframework.stereotype.Repository;

import com.casesoft.dmc.core.dao.BaseHibernateDao;
import com.casesoft.dmc.model.sys.User;

@Repository
public class UserDao extends BaseHibernateDao<User, String> {

}
