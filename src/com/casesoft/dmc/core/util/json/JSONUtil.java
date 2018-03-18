
package com.casesoft.dmc.core.util.json;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.util.CycleDetectionStrategy;
@SuppressWarnings({"unchecked","rawtypes"})
public class JSONUtil {

	public static Object getObject4JsonString(String jsonString, Class pojoCalss) {
		Object pojo;
		JSONObject jsonObject = JSONObject.fromObject(jsonString,configJson("yyyy-MM-dd"));
		//String[] dateFormats = new String[] {"yyyy/MM/dd"};    
		//JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(dateFormats));    
		pojo = JSONObject.toBean(jsonObject, pojoCalss);
		return pojo;
	}
	
	/**
	 * 将包含集合的JSON字符串转化为Map
	 * @param jsonString
	 * @param pojoClass
	 * @param setClass
	 * @return
	 */
//	public static Map getObject5JsonString(String jsonString,Class pojoClass,Class setClass) {
//		Object pojo;
//		JSONObject jsonObject = JSONObject.fromObject(jsonString,configJson("yyyy-MM-dd"));
//		
//		String s = jsonObject.getString("rows");
//		List l = getList4Json(s,setClass);
//		pojo = JSONObject.toBean(jsonObject,pojoClass);
//		Map map = new HashMap();
//		map.put(Constant.MAIN, pojo);
//		map.put(Constant.DETAILS, l);
//		return map;
//	}
	
	
	/**
	 * 转换带子List属性的JavaBean
	 */
	
	public static Object getObjectWithSubListFromJson(String jsonString,Class parentC,Class subC) {
		Map<String,Class> classMap = new HashMap<String,Class>();
		classMap.put("rows",subC);
		return getObjectWithSubListFromJson(jsonString,parentC,classMap);
	}
	
	@SuppressWarnings("static-access")
	public static Object getObjectWithSubListFromJson(String jsonString,Class c,Map<String,Class> subClassMap) {
		JSONObject json = JSONObject.fromObject(jsonString);
		Object o = json.toBean(json, c, subClassMap);
		return o;
	}

	public static List getJsonStr2List(String jsonString, Class pojoCalss) {
		List result = new ArrayList();
		Object[] dtoArray = JSONUtil.getObjectArray4Json(jsonString);
		//warehouseService.delWarehouseMaterial(warehosueId[0]);// 添加前先删除原有数据
		if (dtoArray != null && dtoArray.length > 0) {
			for (int i = 0; i < dtoArray.length; i++) {
				try {
					Class c = Class.forName(pojoCalss.getName());
					Object o = c.newInstance();
					o = JSONUtil.getObject4JsonString(dtoArray[i].toString(),
							pojoCalss);
					result.add(o);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}

			}
		}

		return result;
	}

	
	public static String getStrByArray(Collection c) {
		JSONArray arr = JSONArray.fromObject(c);
		return arr.toString();
	}
	


	public static Map getMap4Json(String jsonString) {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		Iterator keyIter = jsonObject.keys();
		String key;
		Object value;
		Map valueMap = new HashMap();

		while (keyIter.hasNext()) {
			key = (String) keyIter.next();
			value = jsonObject.get(key);
			valueMap.put(key, value);
		}

		return valueMap;
	}

	public static Object[] getObjectArray4Json(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		return jsonArray.toArray();

	}

	public static List getList4Json(String jsonString, Class pojoClass) {

		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		JSONObject jsonObject;
		Object pojoValue;

		List list = new ArrayList();
		for (int i = 0; i < jsonArray.size(); i++) {

			jsonObject = jsonArray.getJSONObject(i);
			pojoValue = JSONObject.toBean(jsonObject, pojoClass);
			list.add(pojoValue);

		}
		return list;

	}
	
	

	public static String[] getStringArray4Json(String jsonString) {

		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		String[] stringArray = new String[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			stringArray[i] = jsonArray.getString(i);

		}

		return stringArray;
	}

	public static Long[] getLongArray4Json(String jsonString) {

		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		Long[] longArray = new Long[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			longArray[i] = jsonArray.getLong(i);

		}
		return longArray;
	}

	public static Integer[] getIntegerArray4Json(String jsonString) {

		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		Integer[] integerArray = new Integer[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			integerArray[i] = jsonArray.getInt(i);

		}
		return integerArray;
	}

	@SuppressWarnings("unused")
	public static Date[] getDateArray4Json(String jsonString, String DataFormat) {

		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		Date[] dateArray = new Date[jsonArray.size()];
		String dateString = "";
		Date date = null;

		for (int i = 0; i < jsonArray.size(); i++) {
			dateString = jsonArray.getString(i);
			//  date = DateUtil.stringToDate(dateString, DataFormat);
			dateArray[i] = date;

		}
		return dateArray;
	}

	public static Double[] getDoubleArray4Json(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		Double[] doubleArray = new Double[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			doubleArray[i] = jsonArray.getDouble(i);

		}
		return doubleArray;
	}

	public static String getJsonString4JavaPOJO(Object javaObj) {

		JSONObject json;
		JsonConfig config = configJson(null,"yyyy-MM-dd");
		json = JSONObject.fromObject(javaObj,config);
		return json.toString();

	}
	
	public static String getJsonString4JavaPOJO(Object javaObj, String[] excludes) {

		JSONObject json;
		JsonConfig jsonConfig = configJson(excludes,"");
		json = JSONObject.fromObject(javaObj,jsonConfig);
		return json.toString();

	}
	
	public static String getJsonStringByList(List<?> l) {
		JSONArray j = new JSONArray();
		j.addAll(l);
		return j.toString();
	}
	
	public static String getJsonStringByList(List<?> l, String[] excludes) {
		JSONArray j = null;
		JsonConfig jsonConfig = configJson(excludes,"");
		j = JSONArray.fromObject(l, jsonConfig);
		return j.toString();
	}
	
	public static Object getObjectFormStr(String jsonString) {
	    JSONObject json = JSONObject.fromObject(jsonString)	;
	    return JSONObject.toBean(json);
	}

	public static String getJsonString4JavaPOJO(Object javaObj,
			String dataFormat) {

		JSONObject json;
		JsonConfig jsonConfig = configJson(dataFormat);
		json = JSONObject.fromObject(javaObj, jsonConfig);
		return json.toString();
	}
	
	public static String getJsonStringFormCollection(Collection c, String dataFormat) {
		JSONArray json;
		JsonConfig jsonConfig = configJson(dataFormat);
		json = JSONArray.fromObject(c, jsonConfig);
		return json.toString();
	}
	public static String getJsonStringFormCollection(Collection c) {
		JSONArray json;
		JsonConfig jsonConfig = configJson("yyyy-MM-dd");
		json = JSONArray.fromObject(c, jsonConfig);
		return json.toString();
	}

	public static JsonConfig configJson(String datePattern) {
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[] { "" });
		jsonConfig.setIgnoreDefaultExcludes(false);
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		jsonConfig.registerJsonValueProcessor(Date.class,   
		//new DateJsonValueProcessor(datePattern)); 
		new JsonDateValueProcessor(datePattern));

		return jsonConfig;
	}

	public static JsonConfig configJson(String[] excludes, String datePattern) {
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(excludes);
		jsonConfig.setIgnoreDefaultExcludes(false);
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		jsonConfig.registerJsonValueProcessor(Date.class,   
		new JsonDateValueProcessor(datePattern));   

		return jsonConfig;
	}

	public static String formatBytableCols(Object[] obj, String[] tableCols) {
		StringBuffer result = new StringBuffer();
		result.append("{");
		if (obj != null && obj.length > 0) {
			for (int i = 0; i < obj.length; i++) {
				try {
					result.append(tableCols[i]);
					result.append(":'");
					if (obj[i] instanceof String) {
						String s = null;
						if (obj[i].toString() != null) {
							s = obj[i].toString().replace("\"", "\\\"")
									.replace("'", "\\'").replace("\n", "<br/>")
									.replace("\b", "\\b").replace("\f", "\\f")
									.replace("\r", "").replace("\t", "");
						}
						result.append(s);
					} else if (obj[i] instanceof Integer) {
						result.append((Integer) obj[i]);
					} else if (obj[i] instanceof Long) {
						result.append((Long) obj[i]);
					} else if (obj[i] instanceof Double) {
						result.append((Double) obj[i]);
					} else if (obj[i] instanceof java.sql.Date) {
						result.append((java.sql.Date) obj[i]);
					} else if (obj[i] instanceof Float) {
						result.append((Float) obj[i]);
					}
					result.append("'");
					if (i < obj.length - 1) {
						result.append(",");
					}

				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				}

			}
		}
		result.append("}");
		System.out.println(result.toString());
		return result.toString();
	}


	public static String formatBytableCols(List<Object[]> list,
			String[] tableCols) {
		StringBuffer result = new StringBuffer();
		result.append("[");
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Object[] obj = (Object[]) list.get(i);
				result.append(formatBytableCols(obj, tableCols));
				if (i < list.size() - 1)
					result.append(",");
			}

		}
		result.append("]");
		return result.toString();
	}
	
}
class JsonDateValueProcessor implements JsonValueProcessor {

	private String format = "";//"yy-MM-dd HH:mm:ss";
	private SimpleDateFormat sdf = null;//new SimpleDateFormat(format);
	public JsonDateValueProcessor(String dataFormat) {
		this.format = dataFormat;
		sdf = new SimpleDateFormat(this.format);
	}
	public Object processArrayValue(Object arg0, JsonConfig arg1) {
		return this.process(arg0);
	}

	public Object processObjectValue(String arg0, Object arg1, JsonConfig arg2) {
		return this.process(arg1);
	}
	
	private Object process(Object value){
		if(value==null){
			return "";
		}else if(value instanceof Date){
			return sdf.format((Date)value);
		}else{
			return value.toString();
		}
		
	}

}
