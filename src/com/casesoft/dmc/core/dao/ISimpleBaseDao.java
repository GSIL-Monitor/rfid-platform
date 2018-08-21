package com.casesoft.dmc.core.dao;

import org.hibernate.*;
import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ISimpleBaseDao<T, PK extends Serializable> {

	/**
	 * 取得sessionFactory.
	 */
	public abstract SessionFactory getSessionFactory();

	/**
	 * 采用@Autowired按类型注入SessionFactory, 当有多个SesionFactory的时候在子类重载本函数.
	 */
	@Autowired
	public abstract void setSessionFactory(final SessionFactory sessionFactory);

	/**
	 * 取得当前Session.
	 */
	public abstract Session getSession();

	/**
	 * 保存新增或修改的对象.
	 */
	public abstract void saveOrUpdate(final T entity);

	public abstract void save(final T entity);

	/**
	 * 保存新增或修改的对象.
	 */
	public abstract void update(final T entity);

	/**
	 * 删除对象.
	 * 
	 * @param entity 对象必须是session中的对象或含id属性的transient对象.
	 */
	public abstract void delete(final T entity);

	/**
	 * 按id删除对象.
	 */
	public abstract void delete(final PK id);

	/**
	 * 按id获取对象.
	 */
	public abstract T get(final PK id);

	/**
	 * 根据ID加载对象
	 */
	public abstract T load(final PK id);

	/**
	 * 按id列表获取对象列表.
	 */
	public abstract List<T> get(final Collection<PK> ids);

	/**
	 *	获取全部对象.
	 */
	public abstract List<T> getAll();

	/**
	 *	获取全部对象, 支持按属性行序.
	 */

	public abstract List<T> getAll(String orderByProperty, boolean isAsc);

	/**
	 * 按属性查找对象列表, 匹配方式为相等.
	 */
	public abstract List<T> findBy(final String propertyName, final Object value);

	/**
	 * 按属性查找唯一对象, 匹配方式为相等.
	 */
	public abstract T findUniqueBy(final String propertyName, final Object value);

	/**
	 * 按HQL查询对象列表.
	 * 
	 * @param values 数量可变的参数,按顺序绑定.
	 */
	public abstract <X> List<X> find(final String hql, final Object... values)
			throws DataAccessException;

	/**
	 * 按HQL查询对象列表.
	 * 
	 * @param values 命名参数,按名称绑定.
	 */
	public abstract <X> List<X> find(final String hql,
			final Map<String, ?> values);

	/**
	 * 按HQL查询唯一对象.
	 * 
	 * @param values 数量可变的参数,按顺序绑定.
	 */
	public abstract <X> X findUnique(final String hql, final Object... values);

	/**
	 * 按HQL查询唯一对象.
	 * 
	 * @param values 命名参数,按名称绑定.
	 */
	public abstract <X> X findUnique(final String hql,
			final Map<String, ?> values);

	/**
	 * 执行HQL进行批量修改/删除操作.
	 * 
	 * @param values 数量可变的参数,按顺序绑定.
	 * @return 更新记录数.
	 */
	public abstract int batchExecute(final String hql, final Object... values);

	/**
	 * 执行HQL进行批量修改/删除操作.
	 * 
	 * @param values 命名参数,按名称绑定.
	 * @return 更新记录数.
	 */
	public abstract int batchExecute(final String hql,
			final Map<String, ?> values);

	/**
	 * 根据查询HQL与参数列表创建Query对象.
	 * 与find()函数可进行更加灵活的操作.
	 * 
	 * @param values 数量可变的参数,按顺序绑定.
	 */
	public abstract Query createQuery(final String queryString,
			final Object... values);

	/**
	 * 根据查询HQL与参数列表创建Query对象.
	 * 与find()函数可进行更加灵活的操作.
	 * 
	 * @param values 命名参数,按名称绑定.
	 */
	public abstract Query createQuery(final String queryString,
			final Map<String, ?> values);

	/**
	 * 按Criteria查询对象列表.
	 * 
	 * @param criterions 数量可变的Criterion.
	 */

	public abstract List<T> find(final Criterion... criterions);

	/**
	 * 按Criteria查询唯一对象.
	 * 
	 * @param criterions 数量可变的Criterion.
	 */
	public abstract T findUnique(final Criterion... criterions);

	/**
	 * 根据Criterion条件创建Criteria.
	 * 与find()函数可进行更加灵活的操作.
	 * 
	 * @param criterions 数量可变的Criterion.
	 */
	public abstract Criteria createCriteria(final Criterion... criterions);

	public abstract void initProxyObject(Object proxy);

	public abstract void flush();

	public abstract Query distinct(Query query);

	public abstract Criteria distinct(Criteria criteria);

	public abstract String getIdName();

	public abstract boolean isPropertyUnique(final String propertyName,
			final Object newValue, final Object oldValue);

	/**
	 * 创建原生态SQL查询
	 */
	public abstract <X> List<X> findBySQL(String sql, Object... values);

	public abstract SQLQuery creatSQLQuery(String sql, Object... values);

}