package com.casesoft.dmc.dao.task;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.casesoft.dmc.core.dao.BaseHibernateDao;
import com.casesoft.dmc.model.task.Business;

@Repository
public class TaskDao extends BaseHibernateDao<Business, String> {

	@SuppressWarnings("unchecked")
	public List<Object> queryFitting(final String hql, int length, final Object... values) {
		Query query = this.createQuery(hql, values);
		query.setFirstResult(0);
		query.setMaxResults(length);
		return query.list();
	}
}
