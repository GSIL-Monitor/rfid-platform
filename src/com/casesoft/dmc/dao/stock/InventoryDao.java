package com.casesoft.dmc.dao.stock;

import com.casesoft.dmc.core.dao.BaseHibernateDao;
import com.casesoft.dmc.model.task.Business;
import org.springframework.stereotype.Repository;

@Repository
public class InventoryDao extends BaseHibernateDao<Business,String> {
}
