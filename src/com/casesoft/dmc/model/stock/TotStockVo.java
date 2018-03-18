package com.casesoft.dmc.model.stock;

public class TotStockVo {

  private Long qty;

  private Long iQty;
  private Long oQty;

  public TotStockVo(Long qty, Long iQty, Long oQty) {
    super();
    this.qty = qty;
    this.iQty = iQty;
    this.oQty = oQty;
  }

  public Long getQty() {
    return qty;
  }

  public void setQty(Long qty) {
    this.qty = qty;
  }

  public Long getIQty() {
    return iQty;
  }

  public void setIqty(Long iQty) {
    this.iQty = iQty;
  }

  public Long getOQty() {
    return oQty;
  }

  public void setOqty(Long oQty) {
    this.oQty = oQty;
  }

}
