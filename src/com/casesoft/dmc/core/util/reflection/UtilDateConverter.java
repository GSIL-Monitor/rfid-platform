package com.casesoft.dmc.core.util.reflection;

import org.apache.commons.beanutils.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilDateConverter implements Converter {
	   
	   private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
	  @SuppressWarnings("rawtypes")
	public Object convert(Class type, Object value) {
	      if (value == null) {
	          return value;
	      }
	      if (value instanceof Date) {
	          return value;
	      }
	      if (value instanceof String) {
	          try {
                      String v = (String)value;
                      if((v).contains("/"))
                           v = v.replace("/", "-");
	               return format.parse((String)v);
	          } catch (ParseException e) {
	               e.printStackTrace();
	          }
	      }
	       return null;
	   }
	
	}

