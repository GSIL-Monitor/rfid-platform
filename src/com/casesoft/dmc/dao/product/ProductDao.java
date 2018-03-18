package com.casesoft.dmc.dao.product;

import org.springframework.stereotype.Repository;

import com.casesoft.dmc.core.dao.BaseHibernateDao;
import com.casesoft.dmc.model.product.Product;

@Repository
public class ProductDao extends BaseHibernateDao<Product, String> {

}
