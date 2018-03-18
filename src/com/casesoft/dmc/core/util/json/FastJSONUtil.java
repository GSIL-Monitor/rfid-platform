package com.casesoft.dmc.core.util.json;

//import org.codehaus.jackson.map.ObjectMapper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.*;

import java.util.HashMap;
import java.util.Map;

public class FastJSONUtil {

  // static ObjectMapper objectMapper = new ObjectMapper();
  //
  // public static String getJsonStr(final Object o) throws Exception {
  // return objectMapper.writeValueAsString(o);
  // }
  //
  // @SuppressWarnings("unchecked")
  // public static Object getObject(final String json, @SuppressWarnings("rawtypes") final Class c)
  // throws Exception {
  // return objectMapper.readValue(json, c);
  // }

  public static String removeField(Object o, final String... args) {
    return filterField(true, o, args);
  }

  public static String retainField(Object o, final String... args) {
    return filterField(false, o, args);
  }

  private static String filterField(final boolean isRemove, final Object o, final String... args) {
    PropertyFilter filter = getFilter(isRemove, args);
    SerializeWriter sw = new SerializeWriter();
    JSONSerializer serializer = new JSONSerializer(sw);
    serializer.getPropertyFilters().add(filter);
    serializer.config(SerializerFeature.UseISO8601DateFormat, true);
    serializer.config(SerializerFeature.DisableCircularReferenceDetect, true);
    serializer.write(o);
    return sw.toString();
  }
  private static SerializerFeature[] features = { SerializerFeature.DisableCircularReferenceDetect};
  public static String getJSONString(Object o) {
    return JSON.toJSONString(o,features);
  }

  public static String renameField(Object o, final Map<String,String> keyValue) {
    NameFilter filter = new NameFilter() {
      public String process(Object source, String name, Object value) {
        for(String key : keyValue.keySet()) {
          if(name.equals(key)) {
            return keyValue.get(key);
          }
        }
        return name;
      }

    };

    SerializeWriter out = new SerializeWriter();
    try {
      JSONSerializer serializer = new JSONSerializer(out);
      serializer.getNameFilters().add(filter);
      serializer.config(SerializerFeature.UseISO8601DateFormat, true);
      serializer.config(SerializerFeature.DisableCircularReferenceDetect, true);
      serializer.write(o);//这里的columns为待转换的对象
      return out.toString();
    } finally {
      out.close();
    }
  }

  public static String renameStyleColorSizeField(Object o) {
    Map<String,String> renameMap = new HashMap<String,String>();
    renameMap.put("styleId","styleNo");
    renameMap.put("colorId","colorNo");
    renameMap.put("sizeId","sizeNo");
    return renameField(o,renameMap);
  }

  private static PropertyFilter getFilter(final boolean isRemove, final String... args) {
    PropertyFilter filter = new PropertyFilter() {
      @Override
      public boolean apply(Object source, String name, Object value) {
        boolean isExist = false;
        if (isRemove) {
          for (String str : args) {
            if (str.equals(name))
              return false;
          }
        } else {
          for (String str : args) {
            if (str.equals(name))
              isExist = true;
          }
          return isExist;
        }
        return true;
      }
    };
    return filter;
  }
}
