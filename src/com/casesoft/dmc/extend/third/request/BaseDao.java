package com.casesoft.dmc.extend.third.request;

import com.casesoft.dmc.core.dao.BaseHibernateDao;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.extend.third.descriptor.*;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.*;
import org.hibernate.transform.ResultTransformer;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by john on 2017-02-22.
 */
public class BaseDao<T, PK extends Serializable> extends BaseHibernateDao<T, PK> {
    private OperatorType operatorType = null;

    public DataResult find(RequestPageData<?> request, Class<?> entityClass) {
        DataResult result = new DataResult();
        Criteria criteria = null;
        if (CommonUtil.isBlank(entityClass)) {
            criteria = this.getSession().createCriteria(this.entityClass);
        } else {
            criteria = this.getSession().createCriteria(entityClass);
        }
        doFilter(criteria, request.getFilter(), entityClass);
        long total = total(criteria);
        result.setTotal(total);
        sort(criteria, sortDescriptors(request));
        page(criteria, request.getTake(), request.getSkip());
        List<GroupDescriptor> groups = request.getGroup();

        if (CommonUtil.isNotBlank(groups)) {
            result.setData(group(criteria, request.getFilter(), groups, entityClass));//分组
        } else {
            List<?> list = criteria.list();
            result.setData(list);
        }
        List<AggregateDescriptor> aggregates = request.getAggregate();
        if (CommonUtil.isNotBlank(aggregates)) {
            result.setAggregates(aggregate(aggregates, request.getFilter(), entityClass));
        }
        this.getSession().clear();
        return result;
    }

    public DataResult find(RequestPageData<?> request) {

        return find(request, null);
    }


    /**
     * @param junction
     * @param filter   解析上传数据
     **/
    private void restrict(Junction junction, FilterDescriptor filter, Class<?> entityClass) {
        String operator = filter.getOperator().toUpperCase();
        String field = filter.getField();
        Object value = filter.getValue();
        boolean ignoreCase = filter.isIgnoreCase();
        Class<?> type = null;
        String[] nullables = {"isnull", "isnotnull", "isempty", "isnotempty"};
        if (!Arrays.asList(nullables).contains(operator)) {
            try {
                operatorType = Enum.valueOf(OperatorType.class, operator);
                if (CommonUtil.isBlank(entityClass)) {
                    type = new PropertyDescriptor(field, this.entityClass).getPropertyType();
                } else {
                    type = new PropertyDescriptor(field, entityClass).getPropertyType();
                }

                if (type == double.class || type == Double.class) {
                    value = Double.parseDouble(value.toString());
                } else if (type == float.class || type == Float.class) {
                    if (operatorType != OperatorType.IN) {
                        value = Float.parseFloat(value.toString());
                    }
                } else if (type == long.class || type == Long.class) {
                    if (operatorType != OperatorType.IN) {
                        value = Long.parseLong(value.toString());
                    }
                } else if (type == int.class || type == Integer.class) {
                    if (operatorType != OperatorType.IN) {
                        value = Integer.parseInt(value.toString());
                    }
                } else if (type == short.class || type == Short.class) {
                    value = Short.parseShort(value.toString());
                } else if (type == boolean.class || type == Boolean.class) {
                    value = Boolean.parseBoolean(value.toString());
                } else if (type == Date.class) {
                    String formate = "yyyy-MM-dd";
                    if (operator.equals("lte")) {
                        formate = "yyyy-MM-dd HH:mm:ss";
                    }
                    if (value.toString().length() > 10) {
                        formate = "yyyy-MM-dd HH:mm:ss";
                    }
                    SimpleDateFormat df = new SimpleDateFormat(formate);
                    String input = value.toString();
                    value = df.parse(input);
                }
            } catch (IntrospectionException e) {
            } catch (NumberFormatException nfe) {
            } catch (ParseException e) {
            } catch (RuntimeException e) {
                throw new IllegalArgumentException(operator + "操作符错误.", e);
            }
        }
        switch (operatorType) {
            case EQ:
                if (value instanceof String) {
                    junction.add(Restrictions.ilike(field, value.toString(), MatchMode.EXACT));
                } else {
                    junction.add(Restrictions.eq(field, value));
                }
                break;
            case NEQ:
                if (value instanceof String) {
                    junction.add(Restrictions.not(Restrictions.ilike(field, value.toString(), MatchMode.EXACT)));
                } else {
                    junction.add(Restrictions.ne(field, value));
                }
                break;
            case GT:
                junction.add(Restrictions.gt(field, value));
                break;
            case GTE:
                junction.add(Restrictions.ge(field, value));
                break;
            case LT:
                junction.add(Restrictions.lt(field, value));
                break;
            case LTE:
                junction.add(Restrictions.le(field, value));
                break;
            case STARTSWITH:
                junction.add(getLikeExpression(field, value.toString(), MatchMode.START, ignoreCase));
                break;
            case ENDSWITH:
                junction.add(getLikeExpression(field, value.toString(), MatchMode.END, ignoreCase));
                break;
            case CONTAINS:
                junction.add(getLikeExpression(field, value.toString(), MatchMode.ANYWHERE, ignoreCase));
                break;
            case DOESNOTCONTAIN:
                junction.add(Restrictions.not(Restrictions.ilike(field, value.toString(), MatchMode.ANYWHERE)));
                break;
            case ISNULL:
                junction.add(Restrictions.isNull(field));
                break;
            case ISNOTNULL:
                junction.add(Restrictions.isNotNull(field));
                break;
            case ISEMPTY:
                junction.add(Restrictions.eq(field, ""));
                break;
            case ISNOTEMPTY:
                junction.add(Restrictions.not(Restrictions.eq(field, "")));
                break;
            case IN:
                junction.add(Restrictions.in(field, (Object[]) org.apache.commons.beanutils.ConvertUtils.convert(value.toString().split(","), type)));

/*
                junction.add(Restrictions.in(field, (Object[]) value.toString().split(",")));
*/
                break;
            case NOTIN:
                junction.add(Restrictions.not(Restrictions.in(field, (Object[]) value.toString().split(","))));
                break;
        }
    }

    /**
     * @param field
     * @param value
     * @param mode
     * @param ignoreCase
     * @return 解析like sql
     */
    private static Criterion getLikeExpression(String field, String value, MatchMode mode, boolean ignoreCase) {
        SimpleExpression expression = Restrictions.like(field, value, mode);
        if (ignoreCase == true) {
            expression = expression.ignoreCase();
        }
        return expression;
    }

    /**
     * @param criteria
     * @param filter
     */
    private void doFilter(Criteria criteria, FilterDescriptor filter, Class<?> entityClass) {
        if (filter != null) {
            List<FilterDescriptor> filters = filter.getFilters();
            if (!filters.isEmpty()) {
                Junction junction = Restrictions.conjunction();
                if (!filter.getFilters().isEmpty() && filter.getLogic().toString().equals("or")) {
                    junction = Restrictions.disjunction();
                }
                for (FilterDescriptor entry : filters) {
                    if (!entry.getFilters().isEmpty()) {
                        doFilter(criteria, entry, entityClass);
                    } else {
                        restrict(junction, entry, entityClass);
                    }
                }
                criteria.add(junction);
            }
        }
    }

    private void sort(Criteria criteria, List<SortDescriptor> sort) {
        if (CommonUtil.isNotBlank(sort)) {
            for (SortDescriptor entry : sort) {
                String field = entry.getField();
                String dir = entry.getDir();
                if (dir.equalsIgnoreCase("asc")) {
                    criteria.addOrder(Order.asc(field));
                } else if (dir.equalsIgnoreCase("desc")) {
                    criteria.addOrder(Order.desc(field));
                }
            }
        }
    }

    private List<?> groupBy(FilterDescriptor filterDescriptor, List<?> items, List<GroupDescriptor> groupDescriptors,
                            List<SimpleExpression> parentRestrictions, Class<?> entityClass)
            throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        if (CommonUtil.isNotBlank(items) && CommonUtil.isNotBlank(groupDescriptors)) {
            List<List<SimpleExpression>> restrictions = new ArrayList<List<SimpleExpression>>();
            GroupDescriptor descriptor = groupDescriptors.get(0);
            List<AggregateDescriptor> aggregates = descriptor.getAggregates();
            String field = descriptor.getField();
            Method accessor = null;
            if (CommonUtil.isBlank(entityClass)) {
                accessor = new PropertyDescriptor(field, this.entityClass).getReadMethod();
            } else {
                accessor = new PropertyDescriptor(field, entityClass).getReadMethod();
            }
            Object groupValue = accessor.invoke(items.get(0));
            List<Object> groupItems = createGroupItem(groupDescriptors.size() > 1, field, groupValue, filterDescriptor, result, aggregates, parentRestrictions, entityClass);
            List<SimpleExpression> groupRestriction = new ArrayList<SimpleExpression>(parentRestrictions);
            groupRestriction.add(Restrictions.eq(field, groupValue));
            restrictions.add(groupRestriction);
            for (Object item : items) {
                Object currentValue = accessor.invoke(item);

                if (!groupValue.equals(currentValue)) {
                    groupValue = currentValue;
                    groupItems = createGroupItem(groupDescriptors.size() > 1, field, groupValue, filterDescriptor, result, aggregates, parentRestrictions, entityClass);
                    groupRestriction = new ArrayList<SimpleExpression>(parentRestrictions);
                    groupRestriction.add(Restrictions.eq(field, groupValue));
                    restrictions.add(groupRestriction);
                }
                groupItems.add(item);
            }
            if (groupDescriptors.size() > 1) {
                Integer counter = 0;
                for (Map<String, Object> g : result) {
                    g.put("items", groupBy(filterDescriptor, (List<?>) g.get("items"), groupDescriptors.subList(1, groupDescriptors.size()), restrictions.get(counter++), entityClass));
                }
            }
        }
        return result;
    }

    private List<Object> createGroupItem(Boolean hasSubgroups,
                                         String field, Object groupValue,
                                         FilterDescriptor filterDescriptor,
                                         List<Map<String, Object>> result,
                                         List<AggregateDescriptor> aggregates,
                                         List<SimpleExpression> aggregateRestrictions, Class<?> entityClass) {

        Map<String, Object> groupItem = new HashMap<String, Object>();
        List<Object> groupItems = new ArrayList<Object>();
        result.add(groupItem);
        if (groupValue instanceof Date) { // format date
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = formatter.format(((Date) groupValue).getTime());
            groupItem.put("value", formattedDate);
        } else {
            groupItem.put("value", groupValue);
        }
        groupItem.put("field", field);
        groupItem.put("hasSubgroups", hasSubgroups);

        if (CommonUtil.isNotBlank(aggregates)) {
            Criteria criteria = null;
            if (CommonUtil.isBlank(entityClass)) {
                criteria = this.getSession().createCriteria(entityClass);
            } else {
                criteria = this.getSession().createCriteria(this.entityClass);
            }
            doFilter(criteria, filterDescriptor, entityClass); // filter the set by the selected criteria
            SimpleExpression currentRestriction = Restrictions.eq(field, groupValue);

            if (aggregateRestrictions != null && !aggregateRestrictions.isEmpty()) {
                for (SimpleExpression simpleExpression : aggregateRestrictions) {
                    criteria.add(simpleExpression);
                }
            }
            criteria.add(currentRestriction);

            groupItem.put("aggregates", calculateAggregates(criteria, aggregates));
        } else {
            groupItem.put("aggregates", new HashMap<String, Object>());
        }
        groupItem.put("items", groupItems);
        return groupItems;
    }

    @SuppressWarnings({"serial", "unchecked"})
    private static Map<String, Object> calculateAggregates(Criteria criteria, List<AggregateDescriptor> aggregates) {
        return (Map<String, Object>) criteria
                .setProjection(createAggregatesProjection(aggregates))
                .setResultTransformer(new ResultTransformer() {
                    @Override
                    public Object transformTuple(Object[] value, String[] aliases) {
                        Map<String, Object> result = new HashMap<String, Object>();
                        for (int i = 0; i < aliases.length; i++) {
                            String alias = aliases[i];
                            Map<String, Object> aggregate;

                            String name = alias.split("_")[0];
                            if (result.containsKey(name)) {
                                ((Map<String, Object>) result.get(name)).put(alias.split("_")[1], value[i]);
                            } else {
                                aggregate = new HashMap<String, Object>();
                                aggregate.put(alias.split("_")[1], value[i]);
                                result.put(name, aggregate);
                            }
                        }

                        return result;
                    }

                    @SuppressWarnings("rawtypes")
                    @Override
                    public List transformList(List collection) {
                        return collection;
                    }
                })
                .list()
                .get(0);
    }

    private static ProjectionList createAggregatesProjection(List<AggregateDescriptor> aggregates) {
        ProjectionList projections = Projections.projectionList();
        for (AggregateDescriptor aggregate : aggregates) {
            String alias = aggregate.getField() + "_" + aggregate.getAggregate();
            if (aggregate.getAggregate().equals("count")) {
                projections.add(Projections.count(aggregate.getField()), alias);
            } else if (aggregate.getAggregate().equals("sum")) {
                projections.add(Projections.sum(aggregate.getField()), alias);
            } else if (aggregate.getAggregate().equals("average")) {
                projections.add(Projections.avg(aggregate.getField()), alias);
            } else if (aggregate.getAggregate().equals("min")) {
                projections.add(Projections.min(aggregate.getField()), alias);
            } else if (aggregate.getAggregate().equals("max")) {
                projections.add(Projections.max(aggregate.getField()), alias);
            }
        }
        return projections;
    }

    private List<?> group(Criteria criteria, FilterDescriptor filterDescriptor, List<GroupDescriptor> groupDescriptors, Class<?> entityClass) {
        List<?> result = new ArrayList<Object>();
        if (CommonUtil.isNotBlank(groupDescriptors)) {
            try {
                result = groupBy(filterDescriptor, criteria.list(), groupDescriptors, new ArrayList<SimpleExpression>(), entityClass);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (HibernateException e) {
                e.printStackTrace();
            } catch (IntrospectionException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private long total(Criteria criteria) {
        long total = (long) criteria.setProjection(Projections.rowCount()).uniqueResult();
        criteria.setProjection(null);
        criteria.setResultTransformer(Criteria.ROOT_ENTITY);
        return total;
    }

    private void page(Criteria criteria, int take, int skip) {
        if (take > 0) {
            criteria.setMaxResults(take);
            criteria.setFirstResult(skip);
        }
    }

    /**
     * @param aggregates
     * @param filters
     * @return 计算总计
     */
    private Map<String, Object> aggregate(List<AggregateDescriptor> aggregates, FilterDescriptor filters, Class<?> entityClass) {
        Criteria criteria = null;
        if (CommonUtil.isBlank(entityClass)) {
            criteria = this.getSession().createCriteria(this.entityClass);
        } else {
            criteria = this.getSession().createCriteria(entityClass);
        }
        doFilter(criteria, filters, entityClass);
        return calculateAggregates(criteria, aggregates);
    }

    private List<SortDescriptor> sortDescriptors(RequestPageData<?> requestPageData) {
        List<SortDescriptor> sort = new ArrayList<SortDescriptor>();
        List<GroupDescriptor> groups = requestPageData.getGroup();
        List<SortDescriptor> sorts = requestPageData.getSort();
        if (groups != null) {
            sort.addAll(groups);
        }
        if (sorts != null) {
            sort.addAll(sorts);
        }
        return sort;
    }
}
