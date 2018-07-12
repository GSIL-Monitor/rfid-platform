package com.casesoft.dmc.core.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.model.logistics.LabelChangeBill;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;

import com.casesoft.dmc.core.util.reflection.ReflectionUtils;

@SuppressWarnings("unchecked")
public class SimpleBaseHibernateDao<T, PK extends Serializable> implements ISimpleBaseDao<T, PK> {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  protected SessionFactory sessionFactory;

  protected Class<T> entityClass;

  /**
   * 用于Dao层子类使用的构造函数. 通过子类的泛型定义取得对象类型Class. eg. public class UserDao extends
   * SimpleHibernateDao<User, Long>
   */
  public SimpleBaseHibernateDao() {
    this.entityClass = ReflectionUtils.getSuperClassGenricType(getClass());
  }

  public SimpleBaseHibernateDao(final SessionFactory sessionFactory, final Class<T> entityClass) {
    this.sessionFactory = sessionFactory;
    this.entityClass = entityClass;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.ISimpleBaseDao#getSessionFactory()
   */
  @Override
  public SessionFactory getSessionFactory() {
    return sessionFactory;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.ISimpleBaseDao#setSessionFactory(org.hibernate.SessionFactory)
   */
  @Override
  @Autowired
  public void setSessionFactory(final SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.ISimpleBaseDao#getSession()
   */
  @Override
  public Session getSession() {
    return sessionFactory.getCurrentSession();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.ISimpleBaseDao#saveOrUpdate(T)
   */
  @Override
  public void saveOrUpdate(final T entity) {
    Assert.notNull(entity, "entity不能为空");
    getSession().saveOrUpdate(entity);
    logger.debug("save entity: {}", entity);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.ISimpleBaseDao#save(T)
   */
  @Override
  public void save(final T entity) {
    getSession().save(entity);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.ISimpleBaseDao#update(T)
   */
  @Override
  public void update(final T entity) {
    Assert.notNull(entity, "entity不能为空");
    getSession().update(entity);
    logger.debug("save entity: {}", entity);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.ISimpleBaseDao#delete(T)
   */
  @Override
  public void delete(final T entity) {
    Assert.notNull(entity, "entity不能为空");
    getSession().delete(entity);
    logger.debug("delete entity: {}", entity);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.ISimpleBaseDao#delete(PK)
   */
  @Override
  public void delete(final PK id) {
    Assert.notNull(id, "id不能为空");
    delete(get(id));
    logger.debug("delete entity {},id is {}", entityClass.getSimpleName(), id);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.ISimpleBaseDao#get(PK)
   */
  @Override
  public T get(final PK id) {
    Assert.notNull(id, "id不能为空");
    return (T) getSession().get(entityClass, id);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.ISimpleBaseDao#load(PK)
   */
  @Override
  public T load(final PK id) {
    Assert.notNull(id, "id不能为空");
    return (T) getSession().load(entityClass, id);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.ISimpleBaseDao#get(java.util.Collection)
   */
  @Override
  public List<T> get(final Collection<PK> ids) {
    return find(Restrictions.in(getIdName(), ids));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.ISimpleBaseDao#getAll()
   */
  @Override
  public List<T> getAll() {
    return find();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.ISimpleBaseDao#getAll(java.lang.String, boolean)
   */

  @Override
  public List<T> getAll(String orderByProperty, boolean isAsc) {
    Criteria c = createCriteria();
    if (isAsc) {
      c.addOrder(Order.asc(orderByProperty));
    } else {
      c.addOrder(Order.desc(orderByProperty));
    }
    return c.list();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.ISimpleBaseDao#findBy(java.lang.String, java.lang.Object)
   */
  @Override
  public List<T> findBy(final String propertyName, final Object value) {
    Assert.hasText(propertyName, "propertyName不能为空");
    Criterion criterion = Restrictions.eq(propertyName, value);
    return find(criterion);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.ISimpleBaseDao#findUniqueBy(java.lang.String, java.lang.Object)
   */
  @Override
  public T findUniqueBy(final String propertyName, final Object value) {
    Assert.hasText(propertyName, "propertyName不能为空");
    Criterion criterion = Restrictions.eq(propertyName, value);
    return (T) createCriteria(criterion).uniqueResult();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.ISimpleBaseDao#find(java.lang.String, java.lang.Object)
   */
  @Override
  public <X> List<X> find(final String hql, final Object... values) throws DataAccessException {
    return createQuery(hql, values).list();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.ISimpleBaseDao#find(java.lang.String, java.util.Map)
   */
  @Override
  public <X> List<X> find(final String hql, final Map<String, ?> values) {
    return createQuery(hql, values).list();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.ISimpleBaseDao#findUnique(java.lang.String, java.lang.Object)
   */
  @Override
  public <X> X findUnique(final String hql, final Object... values) {
    return (X) createQuery(hql, values).uniqueResult();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.ISimpleBaseDao#findUnique(java.lang.String, java.util.Map)
   */
  @Override
  public <X> X findUnique(final String hql, final Map<String, ?> values) {
    return (X) createQuery(hql, values).uniqueResult();
  }
  public <X> X findSqlUnique(final String hql, final Map<String, ?> values) {
    return (X) createSqlQuery(hql, values).uniqueResult();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.ISimpleBaseDao#batchExecute(java.lang.String, java.lang.Object)
   */
  @Override
  public int batchExecute(final String hql, final Object... values) {
    return createQuery(hql, values).executeUpdate();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.ISimpleBaseDao#batchExecute(java.lang.String, java.util.Map)
   */
  @Override
  public int batchExecute(final String hql, final Map<String, ?> values) {
    return createQuery(hql, values).executeUpdate();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.ISimpleBaseDao#createQuery(java.lang.String, java.lang.Object)
   */
  @Override
  public Query createQuery(final String queryString, final Object... values) {
    Assert.hasText(queryString, "queryString不能为空");
    Query query = getSession().createQuery(queryString);
    if (values != null) {
      for (int i = 0; i < values.length; i++) {
        query.setParameter(i, values[i]);
      }
    }
    return query;
  }

  public SQLQuery createSqlQuery(final String queryString, final Object... values){
    Assert.hasText(queryString, "queryString不能为空");
    SQLQuery query = getSession().createSQLQuery(queryString);
    if (values != null) {
      for (int i = 0; i < values.length; i++) {
        query.setParameter(i, values[i]);
      }
    }
    //query.addEntity(LabelChangeBill.class);
    return query;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.ISimpleBaseDao#createQuery(java.lang.String, java.util.Map)
   */
  @Override
  public Query createQuery(final String queryString, final Map<String, ?> values) {
    Assert.hasText(queryString, "queryString不能为空");
    Query query = getSession().createQuery(queryString);
    if (values != null) {
      query.setProperties(values);
    }
    return query;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.ISimpleBaseDao#find(org.hibernate.criterion.Criterion)
   */

  @Override
  public List<T> find(final Criterion... criterions) {
    return createCriteria(criterions).list();
  }

  /**
   * add by yushen
   * 查找出的结果，进行排序，sortMap为排序字段和方向
   */
  public List<T> find(final Map<String, String> sortMap, final Criterion... criterions) {
    return createCriteria(sortMap, criterions).list();
  }
  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.ISimpleBaseDao#findUnique(org.hibernate.criterion.Criterion)
   */
  @Override
  public T findUnique(final Criterion... criterions) {
    return (T) createCriteria(criterions).uniqueResult();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.ISimpleBaseDao#createCriteria(org.hibernate.criterion.Criterion)
   */
  @Override
  public Criteria createCriteria(final Criterion... criterions) {
    Criteria criteria = getSession().createCriteria(entityClass);
    for (Criterion c : criterions) {
      criteria.add(c);
    }
    return criteria;
  }

  /**
   * add by yushen
   * 查找出的结果，进行排序，sortMap为排序字段和方向
   */
  public Criteria createCriteria(final Map<String, String> sortMap, final Criterion... criterions) {
    Criteria criteria = getSession().createCriteria(entityClass);
    for (Criterion c : criterions) {
      criteria.add(c);
    }
    if(CommonUtil.isNotBlank(sortMap)){
      for(Map.Entry<String, String> entry : sortMap.entrySet()){
        if(entry.getValue()=="asc"){
          criteria.addOrder(Order.asc(entry.getKey()));
        }else if(entry.getValue()=="desc"){
          criteria.addOrder(Order.desc(entry.getKey()));
        }
      }
    }
    return criteria;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.ISimpleBaseDao#initProxyObject(java.lang.Object)
   */
  @Override
  public void initProxyObject(Object proxy) {
    Hibernate.initialize(proxy);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.ISimpleBaseDao#flush()
   */
  @Override
  public void flush() {
    getSession().flush();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.ISimpleBaseDao#distinct(org.hibernate.Query)
   */
  @Override
  public Query distinct(Query query) {
    query.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
    return query;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.ISimpleBaseDao#distinct(org.hibernate.Criteria)
   */
  @Override
  public Criteria distinct(Criteria criteria) {
    criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
    return criteria;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.ISimpleBaseDao#getIdName()
   */
  @Override
  public String getIdName() {
    ClassMetadata meta = getSessionFactory().getClassMetadata(entityClass);
    return meta.getIdentifierPropertyName();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.ISimpleBaseDao#isPropertyUnique(java.lang.String,
   * java.lang.Object, java.lang.Object)
   */
  @Override
  public boolean isPropertyUnique(final String propertyName, final Object newValue,
      final Object oldValue) {
    if (newValue == null || newValue.equals(oldValue)) {
      return true;
    }
    Object object = findUniqueBy(propertyName, newValue);
    return (object == null);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.ISimpleBaseDao#findBySQL(java.lang.String, java.lang.Object)
   */
  @Override
  public <X> List<X> findBySQL(String sql, Object... values) {
    return creatSQLQuery(sql, values).list();
  }

  /**
   * winston add 根据SQL查询唯一结果
   * 
   * @param sql
   * @param values
   * @return
   */
  public <X> X findUniqueBySQL(String sql, Object... values) {
    return (X) creatSQLQuery(sql, values).uniqueResult();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.ISimpleBaseDao#creatSQLQuery(java.lang.String, java.lang.Object)
   */
  @Override
  public SQLQuery creatSQLQuery(String sql, Object... values) {
    SQLQuery query = this.getSession().createSQLQuery(sql);
    if (null != values) {
      for (int i = 0; i < values.length; i++) {
        query.setParameter(i, values[i]);
      }
    }
    return query;
  }

  public <X> List<X> findInLength(final String hql, final int length, final Object... values) {
    Query query = this.createQuery(hql, values);
    query.setFirstResult(0);
    query.setMaxResults(length);
    return query.list();
  }

  public List<T> findObjectBySQL(String sql, Object... values) {
    return creatSQLQuery(sql, values).addEntity(entityClass).list();
  }

}
