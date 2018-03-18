package com.casesoft.dmc.core.dao;

public class SecretUtil {

	  public static String jia(String password) {
		  String str = "";
		  for(int i=0;i<password.length();i++) {
			  char c = password.charAt(i);
			  str += ((char)(c+2));
		  }
		  return str;
	  }
	  public static String decrypt(String password) {
		  String str = "";
		  for(int i=0;i<password.length();i++) {
			  char c = password.charAt(i);
			  str += (char)(c-2);
		  }
		  return str;
	  }
}
