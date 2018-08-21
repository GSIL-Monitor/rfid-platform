package com.casesoft.dmc.core.dao;

import com.casesoft.dmc.core.dao.PropertyFilter.MatchType;
import com.casesoft.dmc.core.util.page.Page;
import org.hibernate.criterion.Criterion;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface IBaseDao<T, PK extends Serializable> {

	//-- 分页查询函数 --//
	public abstract Page<T> getAll(final Page<T> page);

	public abstract Page<T> findPage(final Page<T> page, final String hql);

	public abstract Page<T> findPage(final Page<T> page, final String hql,
			final Object... values);

	public abstract Page<T> findPageByHql(final Page<T> page, final String hql,
			final Map<String, ?> values);

	public abstract Page<T> findPage(final Page<T> page,
			final Criterion... criterions);

	public abstract List<T> findBy(final String propertyName,
			final Object value, final MatchType matchType);

	public abstract List<T> find(List<PropertyFilter> filters);

	public abstract List<T> find(PropertyFilter filter);

	public abstract Page<T> findPage(final Page<T> page,
			final List<PropertyFilter> filters);

	@SuppressWarnings("rawtypes")
	public abstract Serializable doBatchInsert(final List list);

	public abstract void merge(Object o);

}