/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.casesoft.dmc.core.util;

import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.extend.api.wechat.until.AdvancedUtil;
import com.casesoft.dmc.extend.api.wechat.until.MyX509TrustManager;
import net.sf.json.JSONObject;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author
 */
public class CommonUtil {
  public static final String UTF8 = "UTF-8";
  public static final String TIME_ZONE_UTC = "UTC";
  public static Logger log = LoggerFactory.getLogger(CommonUtil.class);

    public static String addQuotes(String[] args) {
        StringBuffer sb = new StringBuffer();
        for(String arg : args) {
            sb.append(",'").append(arg).append("'");
        }
        return sb.substring(1);
    }

  public static void dateDiff(String startTime, String endTime, String format) throws ParseException {
//按照传入的格式生成一个simpledateformate对象
    SimpleDateFormat sd = new SimpleDateFormat(format);
    long nd = 1000 * 24 * 60 * 60;//一天的毫秒数
    long nh = 1000 * 60 * 60;//一小时的毫秒数
    long nm = 1000 * 60;//一分钟的毫秒数
    long ns = 1000;//一秒钟的毫秒数long diff;try {
//获得两个时间的毫秒时间差异
    Long diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
    long day = diff / nd;//计算差多少天
    long hour = diff % nd / nh;//计算差多少小时
    long min = diff % nd % nh / nm;//计算差多少分钟
    long sec = diff % nd % nh % nm / ns;//计算差多少秒//输出结果
    System.out.println("时间相差：" + day + "天" + hour + "小时" + min + "分钟" + sec + "秒。");
  }
  public static String   dateDiff(Date startTime, Date endTime)  {
//按照传入的格式生成一个simpledateformate对象
    long nd = 1000 * 24 * 60 * 60;//一天的毫秒数
    long nh = 1000 * 60 * 60;//一小时的毫秒数
    long nm = 1000 * 60;//一分钟的毫秒数
    long ns = 1000;//一秒钟的毫秒数long diff;try {
//获得两个时间的毫秒时间差异
    Long diff = endTime.getTime() - startTime.getTime();
    long day = diff / nd;//计算差多少天
    long hour = diff % nd / nh;//计算差多少小时
    long min = diff % nd % nh / nm;//计算差多少分钟
    long sec = diff % nd % nh % nm / ns;//计算差多少秒//输出结果
    return day + "天" + hour + "小时" + min + "分钟" + sec + "秒。";
  }
  public static Object getUniqueResult(List<?> objectList) {
    if (objectList == null || objectList.size() <= 0)
      return null;
    else
      return objectList.get(0);
  }
	public static String getSqlStrByList(List<String> sqhList, Class<?> c,
			String columnName) {
		StringBuffer sql = new StringBuffer("");
		if (CommonUtil.isNotBlank(sqhList)) {
			sql.append(" ").append(c.getSimpleName().toLowerCase()).append(".")
					.append(columnName).append(" IN ( ");
			for (int i = 0; i < sqhList.size(); i++) {
				sql.append("'").append(sqhList.get(i) + "',");
				if ((i + 1) % 999 == 0 && (i + 1) < sqhList.size()) {
					sql.deleteCharAt(sql.length() - 1);
					sql.append(" ) OR ")
							.append(c.getSimpleName().toLowerCase())
							.append(".").append(columnName).append(" IN (");
				}
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" )");
		}else{
			sql.append(" ").append(c.getSimpleName().toLowerCase()).append(".")
			.append(columnName).append(" IN ( '')");
		}
		return sql.toString();
	}
  public static String getDateString(Date date, String datePattern) {
    if (isBlank(date))
      return "";
    SimpleDateFormat df = new SimpleDateFormat(datePattern);
    return df.format(date);
  }

  public static Date converStrToDate(String dateString) throws ParseException {
    String tempStr = dateString;
    if (tempStr.contains("T"))
      tempStr = tempStr.replace("T", " ");
    return converStrToDate(tempStr, "yyyy-MM-dd HH:mm:ss");
  }

  public static Date converStrToDate(String dateString, String datePattern) throws ParseException {
    if (isBlank(dateString))
      return null;
    SimpleDateFormat df = new SimpleDateFormat(datePattern);
    return df.parse(dateString);
  }

  /**
   * 获取任意时间的下一个月
   * 描述:<描述函数实现的功能>.
   * @param repeatDate yyyyMM格式
   * @param dataFormat 月份格式
   * @return
   */
  public static String getNextMonth(String repeatDate,String dataFormat) {
    String lastMonth = "";
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat dft = new SimpleDateFormat(dataFormat);
    int year = Integer.parseInt(repeatDate.substring(0, 4));
    String monthsString = repeatDate.substring(4, 6);
    int month;
    if ("0".equals(monthsString.substring(0, 1))) {
      month = Integer.parseInt(monthsString.substring(1, 2));
    } else {
      month = Integer.parseInt(monthsString.substring(0, 2));
    }
    cal.set(year,month,Calendar.DATE);
    lastMonth = dft.format(cal.getTime());
    return lastMonth;
  }

  /**
   * 获取任意时间的上一个月
   * 描述:<描述函数实现的功能>.
   * @param repeatDate yyyyMM格式
   * @param dataFormat 月份格式
   * @return
   */
  public static String getLastMonth(String repeatDate,String dataFormat) {
    String lastMonth = "";
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat dft = new SimpleDateFormat(dataFormat);
    int year = Integer.parseInt(repeatDate.substring(0, 4));
    String monthsString = repeatDate.substring(4, 6);
    int month;
    if ("0".equals(monthsString.substring(0, 1))) {
      month = Integer.parseInt(monthsString.substring(1, 2));
    } else {
      month = Integer.parseInt(monthsString.substring(0, 2));
    }
    cal.set(year,month-2,Calendar.DATE);
    lastMonth = dft.format(cal.getTime());
    return lastMonth;
  }


  public static void printAllParam(HttpServletRequest request) {
    Enumeration<?> paramNames = request.getParameterNames();
    while (paramNames.hasMoreElements()) {
      String paramName = (String) paramNames.nextElement();
      System.out.println(paramName + ": " + request.getParameter(paramName));
    }
  }

  public static String objToString(Object o) {
    if (null == o)
      return "0";
    return o.toString();
  }

  public static int getCurrentYear() {
    Calendar cal = Calendar.getInstance();
    int year = cal.get(Calendar.YEAR);
    return year;
  }

  public static int getCurrentMonth() {
    Calendar cal = Calendar.getInstance();
    int month = cal.get(Calendar.MONTH) + 1;
    return month;
  }

  @SuppressWarnings("rawtypes")
  public static boolean isBlank(Object param) {
    if (null == param)
      return true;
    if (param instanceof String) {
      return ((String) param).trim().equals("");
    }
    if (param instanceof Collection) {
      Collection c = (Collection) param;
      return c.size() <= 0;
    }
    if (param instanceof Map) {
      Map map = (Map) param;
      return map.isEmpty() || map.size() <= 0;
    }
    return false;
  }

  public static boolean isNotBlank(Object param) {
    return !isBlank(param);
  }

  public static boolean isPositive(String str) {
    Pattern pattern = Pattern.compile("^\\d+$|-\\d+$"); // 就是判断是否为整数
    Matcher isPositive = pattern.matcher(str);
    return isPositive.matches();
  }

  public static boolean isFloat(String str) {
    Pattern pattern = Pattern.compile("\\d+\\.\\d+$|-\\d+\\.\\d+$");// 判断是否为小数
    Matcher isPositive = pattern.matcher(str);
    return isPositive.matches();
  }

  public static boolean isLetter(char c) {
    return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
  }

  public static boolean isLetterOrNum(String str) {
    if (isBlank(str))
      return false;
    boolean flag = false;
    for (int i = 0; i < str.length(); i++) {
      char c = str.charAt(i);
      if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
        flag = true;
      } else {
        break;
      }
    }
    return flag;
  }

  public static String convertIntToString(int value, int length) {
    StringBuilder buf = new StringBuilder("" + value);
    while (buf.toString().length() < length) {
      buf.insert(0, "0");
    }
    return buf.toString();
  }

  /**
   * 转换颜色编码
   * 
   * @throws Exception
   */
  @Deprecated
  public static String convertToColor(int value) throws Exception {
    int l = PropertyUtil.getIntValue("color_length");
    return convertIntToString(value, l);
  }

  @Deprecated
  public static String convertToStyle(int value) throws Exception {
    int l = PropertyUtil.getIntValue("style_length");
    return convertIntToString(value, l);
  }

  @Deprecated
  public static String convertToSize(int value) throws Exception {
    int l = PropertyUtil.getIntValue("size_length");
    return convertIntToString(value, l);
  }

  /**
   * @toString :十六进制ASCII码转换为字符串
   * @return 字符串
   */
  public static String toString(String str) {
    // String str = "77617463682e657865";
    StringBuffer result = new StringBuffer();
    for (int i = 0; i < str.length(); i += 2) {
      result.append((char) Integer.parseInt(str.substring(i, i + 2), 16));
    }
    return result.toString();
  }

  /**
   * @toHexAscii :字符串转换为十六进制ASCII码
   * @return ASCII码
   */
  public static String toHexAscii(String str) {
    // String s = "watch.exe";// 字符串
    StringBuffer result = new StringBuffer();
    char[] chars = str.toCharArray(); // 把字符中转换为字符数组
    for (int i = 0; i < chars.length; i++) {
      char c = chars[i];
      if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z')
        result.append(Integer.toHexString((int) c));
      else
        result.append(c);
    }
    // result.insert(0, (result.length() / 2));
    return result.toString();
  }

  public static String toHexAscii(String str, int index) {
    String starStr = str.substring(0, index);
    String endStr = str.substring(index + 1);
    char c = str.charAt(index);
    return starStr + Integer.toHexString(c) + endStr;
  }

  public static String asciiToChar(String str, int startIndex) {
    String starStr = str.substring(0, startIndex);
    String endStr = str.substring(startIndex + 2);
    String asciiStr = str.substring(startIndex, startIndex + 2);
    // int asciiInt = Integer.parseInt(asciiStr);
    return starStr + (char) Integer.parseInt(asciiStr, 16) + endStr;
  }

  public static String getSuccessJson(String msg) {
    return "{\"success\":\"true\",\"msg\":\"" + msg + "\"}";
  }

  public static String getFailJson(String msg) {
    return "{\"success\":\"false\",\"msg\":\"" + msg + "\"}";
  }

  public static boolean isSuccessInfo(String msg) {
    return msg.contains("\"success\":\"true\"");
  }

  public static String randomNumeric(int count) {
    return RandomStringUtils.randomNumeric(count);
  }

  public static String produceIntToString(int i, int digit) {
    if(digit == 0) {
      return "";
    }
    StringBuilder buf = new StringBuilder("" + i);
    while (buf.toString().length() < digit) {
      buf.insert(0, "0");
    }
    return buf.toString();
  }

  /**
   * 加密epc
   */
  public static String encryptEpc(String epc) {
    return epc;
  }

  public static String addDay(Date d, int add, String dateFormat) {
    SimpleDateFormat df = new SimpleDateFormat(dateFormat);
    return df.format(new Date(d.getTime() + add * 24 * 60 * 60 * 1000));
  }

  public static String addDay(String d, int add, String dateFormat) throws Exception {
    SimpleDateFormat df = new SimpleDateFormat(dateFormat);
    // Date date = df.parse(d);
    // return df.format(new Date(date.getTime() + add * 24 * 60 * 60 * 1000));
    Calendar c = Calendar.getInstance();
    Date myDate = df.parse(d);
    c.setTime(myDate);
    // c.add(Calendar.MONTH, 8);
    c.add(Calendar.DATE, add);
    myDate = c.getTime();
    return df.format(myDate);
  }

  public static String addDay(String d, int add) throws Exception {
    return addDay(d, add, "yyyy-MM-dd");
  }

  public static String reduceDay(String d, int reduce, String dateFormat) throws Exception {
    SimpleDateFormat df = new SimpleDateFormat(dateFormat);
    // Date date = df.parse(d);
    // return df.format(new Date(date.getTime() - reduce * 24 * 60 * 60 * 1000));
    Calendar c = Calendar.getInstance();
    Date myDate = df.parse(d);
    c.setTime(myDate);
    c.add(Calendar.DATE, -reduce);
    myDate = c.getTime();
    return df.format(myDate);
  }

  public static String reduceDay(String d, int reduce) throws Exception {
    return reduceDay(d, reduce, "yyyy-MM-dd");
  }

  public static long dateInterval(String d1, String d2) throws Exception {
    return dateInterval(d1, d2, "yyyy-MM-dd");
  }

  public static long dateInterval(String d1, String d2, String dataFormat) throws Exception {
    SimpleDateFormat sdf = new SimpleDateFormat(dataFormat);
    Date date1 = sdf.parse(d1);
    Date date2 = sdf.parse(d2);
    // 从间隔毫秒变成间隔天数
    long gap = (date2.getTime() - date1.getTime()) / (1000 * 3600 * 24);
    return gap;
  }

  public static boolean isInStock(int token) {
    boolean result = false;
    switch (token) {
//    case 8://成品入库登记
//    case 11://代理经销商收货
//    case 14://门店入库确认
//    case 17://顾客退货给门店
//    case 19://门店调拨入库
//    case 25://仓库调拨入库登记
//    case 23://品牌商接收代理商或门店退货入库
      case Constant.Token.Label_Data_Receive:// = 4;//品牌商标签接收
      case Constant.Token.Label_Data_Feedback:// = 6;//供应商接收到标签
      case Constant.Token.Storage_Inbound:// = 8;//
      case Constant.Token.Storage_Refund_Inbound://= 23;//代理商或门店退给总部
      case Constant.Token.Storage_Transfer_Inbound:// = 25;
      case Constant.Token.Storage_Adjust_Inbound:// = 29;
      case Constant.Token.Shop_Adjust_Inbound:// = 31;
      case Constant.Token.Shop_Inbound:// = 14;//门店入库
      case Constant.Token.Shop_Transfer_Inbound:// = 19;
      case Constant.Token.Shop_Sales_refund:// = 17;
      result = true;
      break;
    }
    return result;
  }

  public static boolean isOutStock(int token) {
    boolean result = false;
    switch (token) {
//    case 10://出库给代理商门店
//    case 13://品牌商代理经销商发货
//    case 15://门店销售
//    case 18://门店调拨出库
//    case 26://退给供应商
//    case 24://仓库调拨出库
//    case 27://门店或代理商退货出库
      case Constant.Token.Label_Data_Send://  = 5;//品牌商将标签发放给供应商
      case Constant.Token.Factory_Box_Send://  = 7;//加工厂装箱发货
      case Constant.Token.Storage_Refund_Outbound://  = 26;//退货给供应商
      case Constant.Token.Storage_Transfer_Outbound://  = 24;
      case Constant.Token.Storage_Outbound://  = 10;
      case Constant.Token.Storage_Adjust_Outbound :// = 28;
      case Constant.Token.Shop_Sales://  = 15;
      case Constant.Token.Shop_Refund_Outbound://  = 27;
      case Constant.Token.Shop_Adjust_Outbound://  = 30;
      case Constant.Token.Shop_Transfer_Outbound://  = 18;
      result = true;
      break;
    }
    return result;
  }

  public static String getDecimal(double value, String decimal) {
    DecimalFormat df2 = new DecimalFormat(decimal);
    return df2.format(value);
  }

  public static String convertSkuToEpc(String uniqueCode,int[] indexChars) throws Exception {
    String epc = uniqueCode;
    for (int i = 0; i < indexChars.length; i++) {
      int charIndex = indexChars[i] + i;
      epc = CommonUtil.toHexAscii(epc, charIndex);
    }
    return epc;
  }

  public static String convertEpcToUniqueCode(String epc,int[] indexChars) throws Exception {
    String uniqueCode = epc;
    for (int i = 0; i < indexChars.length; i++) {
      int charIndex = indexChars[i];
      uniqueCode = CommonUtil.asciiToChar(uniqueCode, charIndex);
    }
    return uniqueCode;
  }

  public static int daysBetween(Date smdate,Date bdate) throws ParseException
  {
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    smdate=sdf.parse(sdf.format(smdate));
    bdate=sdf.parse(sdf.format(bdate));
    Calendar cal = Calendar.getInstance();
    cal.setTime(smdate);
    long time1 = cal.getTimeInMillis();
    cal.setTime(bdate);
    long time2 = cal.getTimeInMillis();
    long between_days=(time2-time1)/(1000*3600*24);

    return Integer.parseInt(String.valueOf(between_days));
  }

  //region 16/32位MD5加密
  /***
   * MD5加码 生成32位小写md5码
   */
  public static String string2MD5(String inStr) {
    try {
      String sign = sign(inStr, "UTF-8");
      return sign;
    } catch (Exception e) {
      e.printStackTrace();
      return "";

    }
  }

  // MD5加码。32位小写
  public static String sign(String data, String encode) throws Exception {
    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(data.getBytes(encode));
    byte[] bArray = md.digest();
    StringBuilder output = new StringBuilder(32);
    for (int i = 0; i < bArray.length; i++) {
      String temp = Integer.toHexString(bArray[i] & 0xff);
      if (temp.length() < 2) {
        output.append("0");
      }
      output.append(temp);
    }
    return output.toString();
  }

  /**
   * 发送https请求
   *
   * @param requestUrl 请求地址
   * @param requestMethod 请求方式（GET、POST）
   * @param outputStr 提交的数据
   * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
   */
  public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {
    JSONObject jsonObject = null;
    try {
      // 创建SSLContext对象，并使用我们指定的信任管理器初始化
      TrustManager[] tm = {new MyX509TrustManager()};
      SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
      sslContext.init(null, tm, new java.security.SecureRandom());
      // 从上述SSLContext对象中得到SSLSocketFactory对象
      SSLSocketFactory ssf = sslContext.getSocketFactory();

      URL url = new URL(requestUrl);
      HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
      conn.setSSLSocketFactory(ssf);

      conn.setDoOutput(true);
      conn.setDoInput(true);
      conn.setUseCaches(false);
      // 设置请求方式（GET/POST）
      conn.setRequestMethod(requestMethod);

      // 当outputStr不为null时向输出流写数据
      if (null != outputStr) {
        OutputStream outputStream = conn.getOutputStream();
        // 注意编码格式
        outputStream.write(outputStr.getBytes("UTF-8"));
        outputStream.close();
      }

      // 从输入流读取返回内容
      InputStream inputStream = conn.getInputStream();
      InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
      String str = null;
      StringBuffer buffer = new StringBuffer();
      while ((str = bufferedReader.readLine()) != null) {
        buffer.append(str);
      }

      // 释放资源
      bufferedReader.close();
      inputStreamReader.close();
      inputStream.close();
      inputStream = null;
      conn.disconnect();
      jsonObject = JSONObject.fromObject(buffer.toString());
    } catch (ConnectException ce) {
      log.error("连接超时：{}", ce);
    } catch (Exception e) {
      log.error("https请求异常：{}", e);
    }
    return jsonObject;
    //endregion
  }


  /**
   * @Description:加密-32位小写
   * @author:chenzhifan
   * @time:2016年5月23日 上午11:15:33
   */
  public static String encrypt32(String encryptStr) {
    MessageDigest md5;
    try {
      md5 = MessageDigest.getInstance("MD5");
      byte[] md5Bytes = md5.digest(encryptStr.getBytes());
      StringBuffer hexValue = new StringBuffer();
      for (int i = 0; i < md5Bytes.length; i++) {
        int val = ((int) md5Bytes[i]) & 0xff;
        if (val < 16)
          hexValue.append("0");
        hexValue.append(Integer.toHexString(val));
      }
      encryptStr = hexValue.toString();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return encryptStr;
  }
  /**
   * 表单和表体的联合查询hql拼接
   * czf
   *
   */
  public static String hqlbyBillandBillDel(Class<?> billClass,Class<?> billDtlClass, List<PropertyFilter> filters,String constructorParameter){
    String tablePath=billClass.getName();

    String billDtlTable=billDtlClass.getName();
    constructorParameter="t."+constructorParameter;
    constructorParameter=constructorParameter.replaceAll(",",",t.");
    String hql="select new "+tablePath+"("+constructorParameter+") from "+tablePath+" t,"+billDtlTable+" dtl where t.id=dtl.billId";
    String hqlQuery = hqlQueryCondition(filters);
    hql=hql+hqlQuery+" group by "+constructorParameter;
    return hql;
  }

  /**
   * 根据filters拼接hql的查询条件
   * @param filters
   * @return
   */
  public static String hqlQueryCondition(List<PropertyFilter> filters){
    String hql="";
    for(int i=0;i<filters.size();i++){
      PropertyFilter propertyFilter = filters.get(i);
      String propertyName = propertyFilter.getPropertyNames()[0];
      PropertyFilter.MatchType matchType = propertyFilter.getMatchType();
      String name = matchType.name();
      if(propertyName.split(".")[1].equals("billDate")&&name.equals("GE")){
        Date matchValue =(Date) propertyFilter.getMatchValue();
        String dateString = CommonUtil.getDateString(matchValue, "yyyy-MM-dd");
        hql+=" and "+propertyName+" >= to_date('"+dateString+"','yyyy-MM-dd')";
      }
      if(propertyName.split(".")[1].equals("billDate")&&name.equals("LE")){
        Date matchValue =(Date) propertyFilter.getMatchValue();
        String dateString = CommonUtil.getDateString(matchValue, "yyyy-MM-dd");
        hql+=" and "+propertyName+" <=to_date('"+dateString+"','yyyy-MM-dd')";
      }
      if(name.equals("EQ")){
        String value =(String) propertyFilter.getMatchValue();
        hql+="and "+propertyName+"='"+value+"'";
      }
      if(name.equals("LIKE")){
        String value =(String) propertyFilter.getMatchValue();
        hql+="and "+propertyName+"like '%"+value+"%'";
      }
    }

    return hql;
  }

}
