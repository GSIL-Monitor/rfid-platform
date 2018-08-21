package com.casesoft.dmc.dao.task;

import com.casesoft.dmc.core.dao.BaseHibernateDao;
import com.casesoft.dmc.model.task.Business;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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
