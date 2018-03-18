package com.casesoft.dmc.dao.shop;

import com.casesoft.dmc.model.shop.Customer;
import org.springframework.stereotype.Repository;

import com.casesoft.dmc.core.dao.BaseHibernateDao;

@Repository
public class CustomerDao extends BaseHibernateDao<Customer, String> {

}
