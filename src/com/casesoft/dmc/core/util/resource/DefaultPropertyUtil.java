package com.casesoft.dmc.core.util.resource;

import java.util.HashMap;
import java.util.Map;

public class DefaultPropertyUtil {

  public static Map<String, String> resourceMap = new HashMap<String, String>();
  static {
    resourceMap.put("MilanUpload", "D:\\MilanUpload");
    resourceMap.put("style_length", "6");
    resourceMap.put("color_length", "2");
    resourceMap.put("size_length", "2");
    resourceMap.put("serial_length", "6");
  }

  public static String getValue(String key) {
    return resourceMap.get(key);
  }
}
