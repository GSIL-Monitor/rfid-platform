package com.casesoft.dmc.core.util.file;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.resource.DefaultPropertyUtil;

public class PropertyUtil {
  protected static Logger logger = LoggerFactory.getLogger(PropertyUtil.class);

  public final static String filePath = "/config.properties";

  public static Properties properties;

  public static ResourceBundle bundle;

  public static String getFilePath() throws UnsupportedEncodingException {
    URL resource = PropertyUtil.class.getResource(filePath);
    String filePath = resource.getPath();
    filePath = URLDecoder.decode(filePath, "utf-8");// 处理文件夹空格bug
    if (filePath.startsWith("/")) {
      filePath = filePath.substring(1);
    }
    return filePath;
  }

  public static void init(String path) {
    if (null == path) {
      init();
      return;
    }
    FileInputStream fis;
    try {
      fis = new FileInputStream(path);
      properties = new Properties();
      properties.load(fis);
    } catch (FileNotFoundException e) {
      logger.error("属性文件没有发现!!!");
      e.printStackTrace();
    } catch (IOException e) {
      logger.error("读取属性文件出错!!!");
      e.printStackTrace();
    }
    // initI18NConfigFile();
  }

  public static void initI18NConfigFile() {
    // Locale l=Locale.getDefault();
    // bundle=ResourceBundle.getBundle("sysCfg",l);
  }

  public static void init() {
    FileInputStream fis;
    try {
      fis = new FileInputStream(getFilePath());
      properties = new Properties();
      properties.load(fis);
    } catch (FileNotFoundException e) {
      logger.error("属性文件没有发现!!!");
      e.printStackTrace();
    } catch (IOException e) {
      logger.error("读取属性文件出错!!!");
      e.printStackTrace();
    }
    // initI18NConfigFile();
  }

  public static String getValue(String key) throws Exception {
    String value = "";
    try {
      value = (String) properties.get(key.trim());
    } catch (Exception e) {
      return DefaultPropertyUtil.getValue(key.trim());
    }
    if (CommonUtil.isBlank(value)) {
      throw new Exception("从属性文件中获取" + key + "的值为null");
    }
    return value;
  }

  public static Integer getIntValue(String key) throws Exception {
    String value = getValue(key.trim());
    return Integer.parseInt(value.trim());
  }

  /**
   * 设置属性值
   * 
   * @param key
   * @return
   */
  public static void setValue(String key, String value) {
    setValue(key, value, true);
  }

  public static void setValue(String key, String value, boolean isSave) {
    properties.setProperty(key, value);
    if (isSave) {
      storeProperties();
    }
  }

  public static void storeProperties() {
    try {
      OutputStream os = new FileOutputStream(getFilePath());
      properties.store(os, "保存属性文件");
    } catch (IOException e) {
      logger.debug("保存属性文件失败!");
    } finally {

    }
  }

  public static String getI18N(String key) {
    String value = "";
    try {
      value = bundle.getString(key);
    } catch (Exception e) {
      return key;
    }

    return value;

  }

  public static String getI18N(String key, Object... param) {
    return MessageFormat.format(key, param);
  }

}
