package com.casesoft.dmc.model.stock;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "STOCK_STOCK")
public class Stock implements Serializable {
  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;
  private String id;
  private String fkId;
  private String ownerId;
  private String storageId;
  private Date dateTime;

  private Long stockQty;
  private Long iqty;
  private Long oqty;
  private Long totSku;

    private int storeType;
    @Column(nullable = false)
    public int getStoreType() {
        return storeType;
    }

    public void setStoreType(int storeType) {
        this.storeType = storeType;
    }

  @Id
  @Column(nullable = false, length = 50)
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Column()
  public Long getOqty() {
    return oqty;
  }

  public void setOqty(Long oqty) {
    this.oqty = oqty;
  }

  @Column()
  public Long getTotSku() {
    return totSku;
  }

  public void setTotSku(Long totSku) {
    this.totSku = totSku;
  }

  @Column()
  public Date getDateTime() {
    return dateTime;
  }

  public void setDateTime(Date dateTime) {
    this.dateTime = dateTime;
  }

  @Column(nullable = false, length = 50)
  public String getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  @Column(nullable = false, length = 50)
  public String getStorageId() {
    return storageId;
  }

  public void setStorageId(String storageId) {
    this.storageId = storageId;
  }

  @Column(nullable = false)
  public Long getStockQty() {
    return stockQty;
  }

  public void setStockQty(Long stockQty) {
    this.stockQty = stockQty;
  }

  @Column()
  public Long getIqty() {
    return iqty;
  }

  public void setIqty(Long iqty) {
    this.iqty = iqty;
  }

  @Column(unique = true, nullable = false, length = 100)
  public String getFkId() {
    return fkId;
  }

  public void setFkId(String fkId) {
    this.fkId = fkId;
  }

  private List<StockDtl> dtlList = new ArrayList<StockDtl>();

  @Transient
  public List<StockDtl> getDtlList() {
    return dtlList;
  }

  public void setDtlList(List<StockDtl> dtlList) {
    this.dtlList = dtlList;
  }

  public void addDtl(StockDtl dtl) {
    this.dtlList.add(dtl);
    this.iqty = (this.iqty == null ? 0 : this.iqty) + dtl.getIqty();
    this.oqty = (this.oqty == null ? 0 : this.oqty) + dtl.getOqty();
    this.stockQty = (this.stockQty == null ? 0 : this.stockQty) + dtl.getQty();
    if (this.totSku == null)
      this.totSku = new Long(0);
    this.totSku++;
  }
}
