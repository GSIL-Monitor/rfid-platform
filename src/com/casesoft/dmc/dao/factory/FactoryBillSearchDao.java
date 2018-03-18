package com.casesoft.dmc.dao.factory;

import com.casesoft.dmc.core.dao.BaseHibernateDao;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.model.factory.FactoryBillDtlView;
import com.casesoft.dmc.model.factory.FactoryBillView;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FactoryBillSearchDao extends BaseHibernateDao<FactoryBillView, String> {
	
    public List<FactoryBillView> find(List<PropertyFilter> filters,String[] sort,String order) {
	    Criterion[] criterions = buildCriterionByPropertyFilter(filters);
	    return find(sort,order,criterions);
	 }
	 
	@SuppressWarnings("unchecked")	
	public List<FactoryBillView> find(String[] sort,String order,final Criterion... criterions) {
		Criteria criteria = createCriteria(criterions);
		
		for(String s : sort){
            if(order.equals("asc")){
                criteria.addOrder(Order.asc(s));
            }else{
                criteria.addOrder(Order.desc(s));
            }
		    
		}
		
	    return criteria.list();
    }
}
