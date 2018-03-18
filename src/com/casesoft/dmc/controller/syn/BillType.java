package com.casesoft.dmc.controller.syn;

public class BillType {
  // 1是代表入库单0为出库单；2标签检测单3 盘点盈亏单 5标签打印单6:调拨入库单7调拨出库单
  public final static int Inbound = 1;
  public final static int Outbound = 0;
  public final static int TagDetect = 2;
  public final static int Inventory = 3;

  public final static int TagPrint = 5;
  public final static int Transfer_Inbound = 6;
  public final static int Transfer_Outbound = 7;

}
