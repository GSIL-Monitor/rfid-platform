/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.casesoft.dmc.controller.syn;

import com.casesoft.dmc.model.erp.Bill;

/**
 * 
 * @author WinstonLi
 */
public class BillVo extends Bill {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private String deviceId1;
  private String deviceId2;

  public String getDeviceId1() {
    return deviceId1;
  }

  public void setDeviceId1(String deviceId1) {
    this.deviceId1 = deviceId1;
  }

  public String getDeviceId2() {
    return deviceId2;
  }

  public void setDeviceId2(String deviceId2) {
    this.deviceId2 = deviceId2;
  }

}
