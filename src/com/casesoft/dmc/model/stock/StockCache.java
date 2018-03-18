package com.casesoft.dmc.model.stock;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.casesoft.dmc.core.util.CommonUtil;

public class StockCache {

  private String IOWNERID;
  private String ISTORAGEID;
  private String ISKU;
  private String ISTYLEID;
  private String ICOLORID;
  private String ISIZEID;
  private BigDecimal IQTY;
  private BigDecimal STOCKQTY;

  private String OOWNERID;
  private String OSTORAGEID;
  private String OSKU;
  private String OSTYLEID;
  private String OCOLORID;
  private String OSIZEID;
  private BigDecimal OQTY;
  private StockDtl stkDtl;

  public StockDtl getStkDtl() throws ParseException {
    stkDtl = new StockDtl();
    stkDtl.setId(java.util.UUID.randomUUID().toString());
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = new Date();
    stkDtl.setdateTime(df.parse(df.format(date)));
    stkDtl.setOwnerId(IOWNERID);
    stkDtl.setStorageId(ISTORAGEID);
    stkDtl.setSku(ISKU);
    stkDtl.setStyleId(ISTYLEID);
    stkDtl.setColorId(ICOLORID);
    stkDtl.setSizeId(ISIZEID);
    stkDtl.setIqty(Long.parseLong(CommonUtil.objToString(IQTY)));
    stkDtl.setOqty(Long.parseLong(CommonUtil.objToString(OQTY)));
    stkDtl.setQty(Long.parseLong(CommonUtil.objToString(STOCKQTY)));
    if (null == stkDtl.getQty() || 0 == stkDtl.getQty()) {
      if (CommonUtil.isBlank(stkDtl.getSku())) {
        stkDtl.setOwnerId(OOWNERID);
        stkDtl.setStorageId(OSTORAGEID);
        stkDtl.setSku(OSKU);
        stkDtl.setStyleId(OSTYLEID);
        stkDtl.setColorId(OCOLORID);
        stkDtl.setSizeId(OSIZEID);
        stkDtl.setIqty(0L);
      }
      stkDtl.setQty(stkDtl.getIqty() - ((stkDtl.getOqty() == null) ? 0 : stkDtl.getOqty()));
    }
    return stkDtl;
  }

  public String getIOWNERID() {
    return IOWNERID;
  }

  public void setIOWNERID(String iOWNERID) {
    IOWNERID = iOWNERID;
  }

  public String getISTORAGEID() {
    return ISTORAGEID;
  }

  public void setISTORAGEID(String iSTORAGEID) {
    ISTORAGEID = iSTORAGEID;
  }

  public String getISKU() {
    return ISKU;
  }

  public void setISKU(String iSKU) {
    ISKU = iSKU;
  }

  public String getISTYLEID() {
    return ISTYLEID;
  }

  public void setISTYLEID(String iSTYLEID) {
    ISTYLEID = iSTYLEID;
  }

  public String getICOLORID() {
    return ICOLORID;
  }

  public void setICOLORID(String iCOLORID) {
    ICOLORID = iCOLORID;
  }

  public String getISIZEID() {
    return ISIZEID;
  }

  public void setISIZEID(String iSIZEID) {
    ISIZEID = iSIZEID;
  }

  public BigDecimal getIQTY() {
    return IQTY;
  }

  public void setIQTY(BigDecimal iQTY) {
    IQTY = iQTY;
  }

  public BigDecimal getSTOCKQTY() {
    return STOCKQTY;
  }

  public void setSTOCKQTY(BigDecimal sTOCKQTY) {
    STOCKQTY = sTOCKQTY;
  }

  public String getOOWNERID() {
    return OOWNERID;
  }

  public void setOOWNERID(String oOWNERID) {
    OOWNERID = oOWNERID;
  }

  public String getOSTORAGEID() {
    return OSTORAGEID;
  }

  public void setOSTORAGEID(String oSTORAGEID) {
    OSTORAGEID = oSTORAGEID;
  }

  public String getOSKU() {
    return OSKU;
  }

  public void setOSKU(String oSKU) {
    OSKU = oSKU;
  }

  public String getOSTYLEID() {
    return OSTYLEID;
  }

  public void setOSTYLEID(String oSTYLEID) {
    OSTYLEID = oSTYLEID;
  }

  public String getOCOLORID() {
    return OCOLORID;
  }

  public void setOCOLORID(String oCOLORID) {
    OCOLORID = oCOLORID;
  }

  public String getOSIZEID() {
    return OSIZEID;
  }

  public void setOSIZEID(String oSIZEID) {
    OSIZEID = oSIZEID;
  }

  public BigDecimal getOQTY() {
    return OQTY;
  }

  public void setOQTY(BigDecimal oQTY) {
    OQTY = oQTY;
  }

  public StockCache(String iOWNERID, String iSTORAGEID, String iSKU, String iSTYLEID,
      String iCOLORID, String iSIZEID, BigDecimal iQTY, String oSKU, String oOWNERID,
      String oSTORAGEID, BigDecimal oQTY, BigDecimal sTOCKQTY, String oSTYLEID, String oCOLORID,
      String oSIZEID) {
    super();
    this.IOWNERID = iOWNERID;
    this.ISTORAGEID = iSTORAGEID;
    this.ISKU = iSKU;
    this.ISTYLEID = iSTYLEID;
    this.ICOLORID = iCOLORID;
    this.ISIZEID = iSIZEID;
    this.IQTY = iQTY;
    this.STOCKQTY = sTOCKQTY;
    this.OOWNERID = oOWNERID;
    this.OSTORAGEID = oSTORAGEID;
    this.OSKU = oSKU;
    this.OSTYLEID = oSTYLEID;
    this.OCOLORID = oCOLORID;
    this.OSIZEID = oSIZEID;
    this.OQTY = oQTY;
  }

  public StockCache() {
    super();
  }
}
