package com.casesoft.dmc.core.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javassist.expr.NewArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.*;
import org.hibernate.criterion.AggregateProjection;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;
import org.springframework.util.Assert;

import com.casesoft.dmc.core.dao.PropertyFilter.MatchType;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.util.reflection.ReflectionUtils;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class BaseHibernateDao<T, PK extends Serializable> extends SimpleBaseHibernateDao<T, PK>
    implements IBaseDao<T, PK> {

  private static final Object[] Object = null;

public BaseHibernateDao() {
    super();
  }

  public BaseHibernateDao(final SessionFactory sessionFactory, final Class<T> entityClass) {
    super(sessionFactory, entityClass);
  }

  // -- 分页查询函数 --//
  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.IBaseDao#getAll(com.casesoft.dms.core.util.page.Page)
   */
  @Override
  public Page<T> getAll(final Page<T> page) {
    return findPage(page);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.IBaseDao#findPage(com.casesoft.dms.core.util.page.Page,
   * java.lang.String)
   */
  @Override
  public Page<T> findPage(final Page<T> page, final String hql) {
    return findPage(page, hql, new Object[] {});
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.IBaseDao#findPage(com.casesoft.dms.core.util.page.Page,
   * java.lang.String, java.lang.Object)
   */
  @Override
  public Page<T> findPage(final Page<T> page, final String hql, final Object... values) {
    Assert.notNull(page, "page不能为空");

    Query q = createQuery(hql, values);

    if (page.isAutoCount()) {
      long totalCount = countHqlResult(hql, values);
      page.setTotal(totalCount);
    }

    setPageParameterToQuery(q, page);

    List result = q.list();
    page.setRows(result);
    return page;
  }

  public Page<T> findSqlPage(final Page<T> page, final String hql, final Object... values) {
    Assert.notNull(page, "page不能为空");

    Query q = createQuery(hql, values);

    if (page.isAutoCount()) {
      //long totalCount = countSqlResult(hql, values);
      page.setTotal(q.list().size());
    }
    page.setTotPage(page.getTotal()/page.getPageSize()+1);
    setPageParameterToQuery(q, page);

    List result = q.list();
    page.setRows(result);
    return page;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.IBaseDao#findPageByHql(com.casesoft.dms.core.util.page.Page,
   * java.lang.String, java.util.Map)
   */
  @Override
  public Page<T> findPageByHql(final Page<T> page, final String hql, final Map<String, ?> values) {
    Assert.notNull(page, "page不能为空");

    Query q = createQuery(hql, values);

    if (page.isAutoCount()) {
      long totalCount = countHqlResult(hql, values);
      page.setTotal(totalCount);
    }

    setPageParameterToQuery(q, page);

    List result = q.list();
    page.setRows(result);
    return page;
  }
  public Page<T> findPageBySQl(final Page<T> page, final Class<?>className ,final String sql, final Map<String, ?> values) {
    Assert.notNull(page, "page不能为空");

    //Query q = createQuery(hql, values);
    SQLQuery q = createSqlQuery(sql, className,values);
    if (page.isAutoCount()) {
      //long totalCount = countHqlResult(hql, values);
      page.setTotal(q.list().size());
    }
    page.setTotPage(page.getTotal()/page.getPageSize()+1);
    setPageParameterToQuery(q, page);

    List result = q.list();
    page.setRows(result);
    return page;
  }
  public List<T> findBySQl(final Class<?>className ,final String sql,final Map<String, ?> values){
    SQLQuery q = createSqlQuery(sql, className,values);
    List result = q.list();
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.IBaseDao#findPage(com.casesoft.dms.core.util.page.Page,
   * org.hibernate.criterion.Criterion)
   */
  @Override
  public Page<T> findPage(final Page<T> page, final Criterion... criterions) {
    Assert.notNull(page, "page不能为空");

    Criteria c = createCriteria(criterions);

    if (page.isAutoCount()) {
    //  long totalCount = countCriteriaResult(c);
      long totalCount = 0;
      if(page.getSumFields() != null) {
        totalCount = countCriteriaResult(page,c);
      } else {
        totalCount = countCriteriaResult(c);
      }
      page.setTotal(totalCount);
    }

    setPageParameterToCriteria(c, page);

    List result = c.list();
    page.setRows(result); 
    if(CommonUtil.isNotBlank(page.getSumFields())){
    	 addPageResultFooter(page);
    }
    page.setTotPage(page.getTotal()/page.getPageSize()+1);
    return page;
  }


    /**
     * 通过sumFields添加本页Footer
     * @param page     
     * */
    protected void addPageResultFooter(Page<T> page){  	
    	List<T> list = page.getRows();
    	String[] sumFields = page.getSumFields().split(",");
    	
    	final int size = sumFields.length+1;
    	List<Double> count = new ArrayList<Double>();
        for(int i = 0 ; i < size; i++){
        	count.add(0d);
        }
        for(T t: list){
        	for(int i = 1; i < size; i++){    		
        		try {
        			Method m = (Method) t.getClass().getMethod(  
                            "get" + getMethodName(sumFields[i-1]));  
  
                    
					count.set(i, count.get(i)+Double.parseDouble(""+m.invoke(t)));
					
				} catch (Exception e) {
					e.printStackTrace();
				}
        	}
        }
        page.addFooter(0,sumFields, count.toArray());
    	
       
    }
    /**
     *把第一个字母转为大写
     *@param:fildeName
     *@return:String
     * */
    protected static String getMethodName(String fildeName) throws Exception{  
        byte[] items = fildeName.getBytes();  
        items[0] = (byte) ((char) items[0] - 'a' + 'A');  
        return new String(items);  
    }  

/**
   * 统计字段的sum和， Wing Li 2015-04-18
   * @param page
   * @param c
   * @return
   */
  protected long countCriteriaResult(final Page<T> page, final Criteria c) {
    CriteriaImpl impl = (CriteriaImpl) c;

    // 先把Projection、ResultTransformer、OrderBy取出来,清空三者后再执行Count操作
    Projection projection = impl.getProjection();
    ResultTransformer transformer = impl.getResultTransformer();

    List<CriteriaImpl.OrderEntry> orderEntries = null;
    try {
      orderEntries = (List) ReflectionUtils.getFieldValue(impl, "orderEntries");
      ReflectionUtils.setFieldValue(impl, "orderEntries", new ArrayList());
    } catch (Exception e) {
      logger.error("不可能抛出的异常:{}", e.getMessage());
    }
    ProjectionList projectionList = Projections.projectionList().add(Projections.rowCount());
    // 执行Count查询
    if(page.getSumFields() != null) {
      String[] sumFields = page.getSumFields().split(",");
      for(String field : sumFields) {
    		  projectionList.add(Projections.sum(field));    		  
      }
    }
    Object[] countList = (Object[])c.setProjection(projectionList).uniqueResult();
    long totalCount = 0;
    if(countList != null && countList.length>=1) {
      totalCount = countList[0] != null? ((Long)countList[0]):0;
    }
    if(countList.length>1) {
      String sumValues = "";
      String[] sumFields = page.getSumFields().split(",");
      for(int i=1;i<countList.length;i++) {
    	  
    	  if(CommonUtil.isBlank(countList[i])){
    		  countList[i]=0;
    	  }
        sumValues += (","+countList[i]);
       /* page.addFooter(sumFields[i-1], ""+countList[i]);*/
      }
      sumValues = sumValues.substring(1);
      page.addFooter(sumFields, countList);
      page.setSumValues(sumValues);
    }


    // 将之前的Projection,ResultTransformer和OrderBy条件重新设回去
    c.setProjection(projection);

    if (projection == null) {
      c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
    }
    if (transformer != null) {
      c.setResultTransformer(transformer);
    }
    try {
      ReflectionUtils.setFieldValue(impl, "orderEntries", orderEntries);
    } catch (Exception e) {
      logger.error("不可能抛出的异常:{}", e.getMessage());
    }

    return totalCount;
  }


  protected Query setPageParameterToQuery(final Query q, final Page<T> page) {

    if (page.getPageSize() == 0)
      page.setPageSize(40);
    Assert.isTrue(page.getPageSize() > 0, "Page Size must larger than zero");

    // hibernate的firstResult的序号从0开始
    q.setFirstResult(page.getFirst() - 1);
    q.setMaxResults(page.getPageSize());
    return q;
  }

  protected Criteria setPageParameterToCriteria(final Criteria c, final Page<T> page) {

    Assert.isTrue(page.getPageSize() > 0, "Page Size must larger than zero");
    
    // hibernate的firstResult的序号从0开始
    c.setFirstResult(page.getFirst() - 1);
    c.setMaxResults(page.getPageSize());

    if (page.isOrderBySetted()) {
      String[] orderByArray = StringUtils.split(page.getOrderBy(), ',');
      String[] orderArray = StringUtils.split(page.getOrder(), ',');

      Assert.isTrue(orderByArray.length == orderArray.length, "分页多重排序参数中,排序字段与排序方向的个数不相等");
      
      for (int i = 0; i < orderByArray.length; i++) {
        if (Page.ASC.equals(orderArray[i])) {
          c.addOrder(Order.asc(orderByArray[i]));
        } else {
          c.addOrder(Order.desc(orderByArray[i]));
        }
      } 
      
      
    }   
    return c;
  }

  /**
   * 未分页的查询添加排序功能
   * 
   * @param c
   * @param orderArray
   * @return
   */
  protected Criteria setParameterToCriteria(final Criteria c, String[] orderByArray,
      String[] orderArray) {

    Assert.isTrue(orderByArray.length == orderArray.length, "排序字段与排序方向的个数不相等");

    for (int i = 0; i < orderByArray.length; i++) {
      if (Page.ASC.equals(orderArray[i])) {
        c.addOrder(Order.asc(orderByArray[i]));
      } else {
        c.addOrder(Order.desc(orderByArray[i]));
      }
    }
    return c;
  }

  /**
   * 执行count查询获得本次Hql查询所能获得的对象总数.
   * 
   * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
   */
  protected long countHqlResult(final String hql, final Object... values) {
    String countHql = prepareCountHql(hql);

    try {
      Long count = findUnique(countHql, values);
      return count;
    } catch (Exception e) {
      throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
    }
  }
  /**
   * 执行count查询获得本次SQL查询所能获得的对象总数.
   *
   * 本函数只能自动处理简单的SQL语句,复杂的hql查询请另行编写count语句查询.
   */
  protected long countSqlResult(final String hql, final Object... values) {
    String countHql = prepareCountSql(hql);

    try {
      Long count = findSqlUnique(countHql, null);
      return count;
    } catch (Exception e) {
      throw new RuntimeException("sql can't be auto count, sql is:" + countHql, e);
    }
  }

  protected long countHqlResult(final String hql, final Map<String, ?> values) {
    String countHql = prepareCountHql(hql);

    try {
      Long count = findUnique(countHql, values);
      return count;
    } catch (Exception e) {
      throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
    }
  }

  private String prepareCountHql(String orgHql) {
    String fromHql = orgHql;
    // select子句与order by子句会影响count查询,进行简单的排除.
    fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
    fromHql = StringUtils.substringBefore(fromHql, "order by");

    String countHql = "select count(*) " + fromHql;
    return countHql;
  }
  private String prepareCountSql(String orgHql) {
    String fromHql = orgHql;
    // select子句与order by子句会影响count查询,进行简单的排除.
    fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
    fromHql = StringUtils.substringBefore(fromHql, "order by");

    String countHql = "select count(*) from (select count(*) " + fromHql+")";
    return countHql;
  }

  protected long countCriteriaResult(final Criteria c) {
    CriteriaImpl impl = (CriteriaImpl) c;

    // 先把Projection、ResultTransformer、OrderBy取出来,清空三者后再执行Count操作
    Projection projection = impl.getProjection();
    ResultTransformer transformer = impl.getResultTransformer();

    List<CriteriaImpl.OrderEntry> orderEntries = null;
    try {
      orderEntries = (List) ReflectionUtils.getFieldValue(impl, "orderEntries");
      ReflectionUtils.setFieldValue(impl, "orderEntries", new ArrayList());
    } catch (Exception e) {
      logger.error("不可能抛出的异常:{}", e.getMessage());
    }

    // 执行Count查询
    Long totalCountObject = (Long) c.setProjection(Projections.rowCount()).uniqueResult();
    long totalCount = (totalCountObject != null) ? totalCountObject : 0;

    // 将之前的Projection,ResultTransformer和OrderBy条件重新设回去
    c.setProjection(projection);

    if (projection == null) {
      c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
    }
    if (transformer != null) {
      c.setResultTransformer(transformer);
    }
    try {
      ReflectionUtils.setFieldValue(impl, "orderEntries", orderEntries);
    } catch (Exception e) {
      logger.error("不可能抛出的异常:{}", e.getMessage());
    }

    return totalCount;
  }

  // -- 属性过滤条件(PropertyFilter)查询函数 --//

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.IBaseDao#findBy(java.lang.String, java.lang.Object,
   * com.casesoft.dms.core.dao.PropertyFilter.MatchType)
   */
  @Override
  public List<T> findBy(final String propertyName, final Object value, final MatchType matchType) {
    Criterion criterion = buildCriterion(propertyName, value, matchType);
    return find(criterion);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.IBaseDao#find(java.util.List)
   */
  @Override
  public List<T> find(List<PropertyFilter> filters) {
    Criterion[] criterions = buildCriterionByPropertyFilter(filters);
    return find(criterions);
  }

  /**
   * add by yushen
   * 查找出的结果，进行排序，sortMap为排序字段和方向
   */
  public List<T> find(List<PropertyFilter> filters, Map<String, String> sortMap){
    Criterion[] criterions = buildCriterionByPropertyFilter(filters);
    return find(sortMap, criterions);
  }
  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.IBaseDao#find(com.casesoft.dms.core.dao.PropertyFilter)
   */
  @Override
  public List<T> find(PropertyFilter filter) {
    List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
    filters.add(filter);
    return find(filters);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.IBaseDao#findPage(com.casesoft.dms.core.util.page.Page,
   * java.util.List)
   */
  @Override
  public Page<T> findPage(final Page<T> page, final List<PropertyFilter> filters) {
    Criterion[] criterions = buildCriterionByPropertyFilter(filters);
    return findPage(page, criterions);
  }

  // // =================================================
  // /**
  // * winston 增加
  // * 处理非特定le
  // * @param page
  // * @param filters
  // * @return
  // */
  // public <X> Page<X> findPage2(final Page<X> page, final List<PropertyFilter> filters) {
  // Criterion[] criterions = buildCriterionByPropertyFilter(filters);
  // return findPage3(page, criterions);
  // }
  // public <X> Page<X> findPage3(final Page<X> page, final Criterion... criterions) {
  // Assert.notNull(page, "page不能为空");
  //
  // Criteria c = createCriteria(criterions);
  //
  // if (page.isAutoCount()) {
  // long totalCount = countCriteriaResult(c);
  // page.setTotal(totalCount);
  // }
  //
  // setPageParameterToCriteria3(c, page);
  //
  // List result = c.list();
  // page.setRows(result);
  // return page;
  // }
  // protected <X> Criteria setPageParameterToCriteria3(final Criteria c, final Page<X> page) {
  //
  // Assert.isTrue(page.getPageSize() > 0, "Page Size must larger than zero");
  //
  // //hibernate的firstResult的序号从0开始
  // c.setFirstResult(page.getFirst() - 1);
  // c.setMaxResults(page.getPageSize());
  //
  // if (page.isOrderBySetted()) {
  // String[] orderByArray = StringUtils.split(page.getOrderBy(), ',');
  // String[] orderArray = StringUtils.split(page.getOrder(), ',');
  //
  // Assert.isTrue(orderByArray.length == orderArray.length, "分页多重排序参数中,排序字段与排序方向的个数不相等");
  //
  // for (int i = 0; i < orderByArray.length; i++) {
  // if (Page.ASC.equals(orderArray[i])) {
  // c.addOrder(Order.asc(orderByArray[i]));
  // } else {
  // c.addOrder(Order.desc(orderByArray[i]));
  // }
  // }
  // }
  // return c;
  // }
  // ===========================================

  protected Criterion buildCriterion(final String propertyName, final Object propertyValue,
      final MatchType matchType) {
    Assert.hasText(propertyName, "propertyName不能为空");
    Criterion criterion = null;
    // 根据MatchType构造criterion
    switch (matchType) {
    case EQ:
      criterion = Restrictions.eq(propertyName, propertyValue);
      break;
    case LIKE:
      criterion = Restrictions.like(propertyName, (String) propertyValue, MatchMode.ANYWHERE);
      break;
    case LIKESTART:
      criterion = Restrictions.like(propertyName, (String) propertyValue, MatchMode.START);
      break;
    case LIKEEND:
      criterion = Restrictions.like(propertyName, (String) propertyValue, MatchMode.END);
      break;
    case LE:
      criterion = Restrictions.le(propertyName, propertyValue);
      break;
    case LT:
      criterion = Restrictions.lt(propertyName, propertyValue);
      break;
    case GE:
      criterion = Restrictions.ge(propertyName, propertyValue);
      break;
    case GT:
      criterion = Restrictions.gt(propertyName, propertyValue);

      break;// winston
    // 补充限制条件
    case IN:
      criterion = Restrictions.in(propertyName, (Object[]) propertyValue);
      break;
    case NIN:
      criterion = Restrictions.not(Restrictions.in(propertyName, (Object[]) propertyValue));
      break;

    // 补充限制2
    case ISN:
      criterion = Restrictions.isNull(propertyName);
      break;
    case ISNN:
      criterion = Restrictions.isNotNull(propertyName);
    }
    return criterion;
  }

  protected Criterion[] buildCriterionByPropertyFilter(final List<PropertyFilter> filters) {
    List<Criterion> criterionList = new ArrayList<Criterion>();
    for (PropertyFilter filter : filters) {
      if (!filter.hasMultiProperties()) { // 只有一个属性需要比较的情况.
        Criterion criterion = buildCriterion(filter.getPropertyName(), filter.getMatchValue(),
            filter.getMatchType());
        criterionList.add(criterion);
      } else {// 包含多个属性需要比较的情况,进行or处理.
        Disjunction disjunction = Restrictions.disjunction();
        for (String param : filter.getPropertyNames()) {
          Criterion criterion = buildCriterion(param, filter.getMatchValue(), filter.getMatchType());
          disjunction.add(criterion);
        }
        criterionList.add(disjunction);
      }
    }
    return criterionList.toArray(new Criterion[criterionList.size()]);
  }

  // public Serializable doBatchInsert(final List list) {
  //
  // return (Serializable) this.getHibernateTemplate().execute(
  // new HibernateCallback() {
  // long begin = System.currentTimeMillis();
  // public Object doInHibernate(Session session)
  // throws HibernateException, SQLException {
  // Integer rows = 0;
  // for (int i = 0; i < list.size(); i++) {
  // session.save(list.get(i));
  // if ((i + 1) % list.size() == 0) {
  // rows = i;
  // session.flush();
  // session.clear();
  // }
  // }
  // long end = System.currentTimeMillis();
  // logger.debug("批处理耗时："+(end-begin)+"毫秒");
  // return rows;
  // }
  // });
  // }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.IBaseDao#doBatchInsert(java.util.List)
   */
  @Override
  public Serializable doBatchInsert(final List list) {
    long begin = System.currentTimeMillis();
    Integer rows = 0;
    Session session = this.getSession();
    for (int i = 0; i < list.size(); i++) {
      session.saveOrUpdate(list.get(i));
      if ((i + 1) % list.size() == 0) {
        rows = i;
        session.flush();
        session.clear();
      }
    }
    long end = System.currentTimeMillis();
    logger.debug("批处理耗时：" + (end - begin) + "毫秒");
    return rows;
  }
  public Serializable dosaveBatchInsert(final List list) {
    long begin = System.currentTimeMillis();
    Integer rows = 0;
    Session session = this.getSession();
    for (int i = 0; i < list.size(); i++) {
      session.save(list.get(i));
      if ((i + 1) % list.size() == 0) {
        rows = i;
        session.flush();
        session.clear();
      }
    }
    long end = System.currentTimeMillis();
    logger.debug("批处理耗时：" + (end - begin) + "毫秒");
    return rows;
  }

  public Serializable doBatchInsert(final Collection set) {
    long begin = System.currentTimeMillis();
    Integer rows = 0;
    Session session = this.getSession();
    Object[] list = set.toArray();
    for (int i = 0; i < list.length; i++) {
      session.saveOrUpdate(list[i]);
      if ((i + 1) % list.length == 0) {
        rows = i;
        session.flush();
        session.clear();
      }
    }
    long end = System.currentTimeMillis();
    logger.debug("批处理耗时：" + (end - begin) + "毫秒");
    return rows;
  }

  public <X> void saveOrUpdateX(X o) {
    this.getSession().saveOrUpdate(o);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.core.dao.IBaseDao#merge(java.lang.Object)
   */
  @Override
  public void merge(Object o) {
    this.getSession().merge(o);
  }

}
