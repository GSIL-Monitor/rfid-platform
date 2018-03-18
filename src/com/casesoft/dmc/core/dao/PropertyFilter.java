package com.casesoft.dmc.core.dao;

import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.reflection.ConvertUtils;
import com.casesoft.dmc.core.util.reflection.ServletUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PropertyFilter {

  /** 多个属性间OR关系的分隔符. */
  public static final String OR_SEPARATOR = "_OR_";

  /** 属性比较类型. */

  public enum MatchType {
    IN, NIN, // 新增 in,not in
    ISN, ISNN, // 新增2 is null,is not null

    EQ, // Equal to
    LIKE, LIKESTART, // like '%value'
    LIKEEND, // like 'value%'
    LT, // Less To
    GT, // Greater than
    LE, GE;// //Greater than or equal to
  }

  /** 属性数据类型. */
  public enum PropertyType {
    S(String.class), I(Integer.class), L(Long.class), N(Double.class), D(Date.class), B(
        Boolean.class);

    private Class<?> clazz;

    private PropertyType(Class<?> clazz) {
      this.clazz = clazz;
    }

    public Class<?> getValue() {
      return clazz;
    }
  }

  private MatchType matchType = null;

  private Object matchValue = null;

  private Class<?> propertyClass = null;

  private String[] propertyNames = null;

  public PropertyFilter(final String filterName, final String value) {
    String firstPart = StringUtils.substringBefore(filterName, "_");// LIKES
    String matchTypeCode = StringUtils.substring(firstPart, 0, firstPart.length() - 1);// LIKE
    String propertyTypeCode = StringUtils.substring(firstPart, firstPart.length() - 1,
        firstPart.length());// S

    try {
      matchType = Enum.valueOf(MatchType.class, matchTypeCode);
    } catch (RuntimeException e) {
      throw new IllegalArgumentException("filter名称" + filterName + "没有按规则编写,无法得到属性比较类型.", e);
    }

    try {
      propertyClass = Enum.valueOf(PropertyType.class, propertyTypeCode).getValue();
    } catch (RuntimeException e) {
      throw new IllegalArgumentException("filter名称" + filterName + "没有按规则编写,无法得到属性值类型.", e);
    }

    String propertyNameStr = StringUtils.substringAfter(filterName, "_");
    Assert.isTrue(StringUtils.isNotBlank(propertyNameStr), "filter名称" + filterName
        + "没有按规则编写,无法得到属性名称.");
    propertyNames = StringUtils.splitByWholeSeparator(propertyNameStr, PropertyFilter.OR_SEPARATOR);

    // 如果MatchType 为IN或NIN，作特殊处理
    if (matchType.name().contains("IN")) {
      String[] values = value.split(","); // IN中的value用“,”隔开
      this.matchValue = ConvertUtils.convertStringToObject(values, propertyClass);
    } else

      this.matchValue = ConvertUtils.convertStringToObject(value, propertyClass);
      if(firstPart.equals("LED")){// 设置为当天23：59：59
   	     Date d = (Date) this.matchValue;
   	     long t=d.getTime();
   	     t = t+24*60*60*1000-1;
   	     d.setTime(t);
   	     this.matchValue = d;
     }
  }

  public boolean hasMultiProperties() {

    return (this.propertyNames.length > 1);
  }

  public String getPropertyName() {
    Assert.isTrue(propertyNames.length == 1, "There are not only one property in this filter.");
    return propertyNames[0];
  }

  public MatchType getMatchType() {
    return matchType;
  }

  public Object getMatchValue() {
    return matchValue;
  }

  public String[] getPropertyNames() {
    return propertyNames;
  }

  public static List<PropertyFilter> buildFromParams(String[] properties, String[] values) {
    List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
    Assert.isTrue(properties.length == values.length, "属性数值长度必须和值长度相同");
    for (int i = 0; i < properties.length; i++) {
      PropertyFilter filter = new PropertyFilter(properties[i], values[i]);
      filters.add(filter);
    }
    return filters;
  }

  /**
   * 从HttpRequest中创建PropertyFilter列表, 默认Filter属性名前缀为filter.
   * 
   * @see #buildFromHttpRequest(HttpServletRequest, String)
   */
  public static List<PropertyFilter> buildFromHttpRequest(final HttpServletRequest request) {
    return buildFromHttpRequest(request, "filter");
  }

  /**
   * 从HttpRequest中创建PropertyFilter列表 PropertyFilter命名规则为Filter属性前缀_比较类型属性类型_属性名.
   * 
   * eg. filter_EQS_name filter_LIKES_name_OR_email
   */
  public static List<PropertyFilter> buildFromHttpRequest(final HttpServletRequest request,
      final String filterPrefix) {
    List<PropertyFilter> filterList = new ArrayList<PropertyFilter>();

    // 从request中获取含属性前缀名的参数,构造去除前缀名后的参数Map.
    Map<String, Object> filterParamMap = ServletUtils.getParametersStartingWith(request,
        filterPrefix + "_");

    // 分析参数Map,构造PropertyFilter列表
    for (Map.Entry<String, Object> entry : filterParamMap.entrySet()) {
      String filterName = entry.getKey();

      // 如果value值为空,则忽略此filter.
      if (null != entry.getValue()) {
          String value = (String) entry.getValue();
        PropertyFilter filter = new PropertyFilter(filterName, value);
        filterList.add(filter);
      }
    }

    return filterList;
  }

  // 将属性过滤成Hql
  private String hql;
  private Object[] values;

  public PropertyFilter(String hql, Object[] values) {
    this.hql = hql;
    this.values = values;
  }

  public String getHql() {
    return hql;
  }

  public void setHql(String hql) {
    this.hql = hql;
  }

  public Object[] getValues() {
    return values;
  }

  public void setValues(Object[] values) {
    this.values = values;
  }

  /**
   * demo: record_EQS_taskId 根据字符串生成hql
   * 
   * @param request
   * @param filterPrefix
   * @return
   */
  public static PropertyFilter buildHqlFromHttpRequest(final HttpServletRequest request,
      final String filterPrefix) {
    // 从request中获取含属性前缀名的参数,构造去除前缀名后的参数Map.
    Map<String, Object> filterParamMap = ServletUtils.getParametersStartingWith(request,
        filterPrefix + "_");
    String className = WordUtils.capitalize(filterPrefix);// StringUtils.capitalise(filterPrefix);
    String hqlBuf = "from " + className + " " + filterPrefix + "";
    String hqlEnd = "";

    Object[] os = new Object[] {};
    String order = "";
    // 分析参数，构造hql
    for (Map.Entry<String, Object> entry : filterParamMap.entrySet()) {
      String filterName = entry.getKey();
      if (filterName.contains("ORDER"))
        order += "," + buildHql(filterPrefix, filterName);
      else {
        String result = buildHql(filterPrefix, filterName);
        hqlEnd += (" and " + result);
        Object value = entry.getValue();
        if (result.contains("like")) {
          String val = (String) value;
          value = ("%" + val + "%");
        }
          if(result.contains("in?")) {
              String val = (String) value;
              String[] values = val.split(",");
              String temp = "";
              for(String v : values) {
                  temp = temp + ",'" +v+"'";
              }
              temp = temp.substring(1);
              hqlEnd = hqlEnd.replace("in?"," in ("+temp+")");
          }

        //判断比较类型
        String firstPart = filterName.split("_")[0];
        if(firstPart.length()==3 && !firstPart.contains("IN")) {//EQI
            String propertyTypeCode = StringUtils.substring(firstPart, firstPart.length() - 1,
                    firstPart.length());// S
            Class propertyClass = null;
            try {
                propertyClass = Enum.valueOf(PropertyType.class, propertyTypeCode).getValue();
            } catch (RuntimeException e) {
                throw new IllegalArgumentException("filter名称" + filterName + "没有按规则编写,无法得到属性值类型.", e);
            }


            Object matchValue = ConvertUtils.convertStringToObject(value.toString(), propertyClass);
            os = ArrayUtils.add(os,matchValue);
        } else {
            if(!firstPart.contains("IN")) {
                os = ArrayUtils.add(os, value);
            }
        }
      }
    }
    if(CommonUtil.isNotBlank(hqlEnd.toString())) {
       hqlBuf = hqlBuf + " where " + hqlEnd.substring(4);
    }

    if (!order.equals(""))
      hqlBuf += (" order by " + order.substring(1));
    return new PropertyFilter(hqlBuf, os);

  }

  /**
   * EQ, // Equal to LIKE, LIKESTART, // like '%value' LIKEEND, // like 'value%' LT, // Less To GT,
   * // Greater than LE, GE;// //Greater than or equal to
   * 
   * @param filterName
   *          = EQ_taskId ==>obj.taskId=
   * @return
   */
  protected static String buildHql(String objName, String filterName) {
    String[] filters = StringUtils.split(filterName, "_");
    String prefix = objName + "." + filters[1];
    if (filters[0].contains("EQ"))
      return prefix + "=? ";
    if (filters[0].contains("LIKE"))
      return prefix + " like " + "? ";
    // if(filters[0].contains("LIKEEND"))
    // return prefix+" like "+"'?%' ";
    if (filters[0].contains("LT"))
      return prefix + "<? ";
    if (filters[0].contains("GT"))
      return prefix + ">?";
    if (filters[0].contains("LE"))
      return prefix + "<=? ";
    if (filters[0].contains("GE"))
      return prefix + ">=? ";
    if (filters[0].contains("ORDER"))
      return filters[1];
      if(filters[0].contains("IN"))
          return prefix + " in?";
    return "";
  }
  
  public static List<PropertyFilter> createOneFilter(String filterName, String value) {
	  List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
	  PropertyFilter filter = new PropertyFilter(filterName,value);
	  filters.add(filter);
	  return filters;
  }

}
