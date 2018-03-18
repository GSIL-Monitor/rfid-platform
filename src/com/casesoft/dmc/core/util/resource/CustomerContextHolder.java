package com.casesoft.dmc.core.util.resource;

public class CustomerContextHolder {
  public final static String DATA_SOURCE = "dataSource";
  public final static String DATA_SOURCE2 = "dataSource2";

  private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

  public static void setCustomerType(String customerType) {
    contextHolder.set(customerType);
  }

  public static String getCustomerType() {
    String customerType = contextHolder.get();
    return customerType;
  }

  public static void clearCustomerType() {
    contextHolder.remove();
  }
}
