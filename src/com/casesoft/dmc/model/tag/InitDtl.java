package com.casesoft.dmc.model.tag;

import javax.persistence.*;

/**
 * InitDtlDao entity. @author
 */
@Entity
@Table(name = "TAG_INITDTL")
public class InitDtl implements java.io.Serializable {

  private static final long serialVersionUID = -3991016494021088428L;
  private String id;
  private String billNo;
  private String ownerId;
  private Integer status;
  private String styleId;
  private String colorId;
  private String sizeId;
  private String sku;
  private long qty;
  private long startNum;
  private long endNum;

  private Long detectQty;
  private Long receiveQty;
  private String styleName;
  private String colorName;
  private String sizeName;

  @Column()
  public Long getDetectQty() {
    return detectQty;
  }

  public void setDetectQty(Long detectQty) {
    this.detectQty = detectQty;
  }

  @Column()
  public Long getReceiveQty() {
    return receiveQty;
  }

  public void setReceiveQty(Long receiveQty) {
    this.receiveQty = receiveQty;
  }

  /** default constructor */
  public InitDtl() {
  }

  // Property accessors
  @Id
  @Column(nullable = false, length = 50)
  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Column(nullable = false, length = 50)
  public String getBillNo() {
    return this.billNo;
  }

  public void setBillNo(String billNo) {
    this.billNo = billNo;
  }

  @Column(nullable = false, length = 50)
  public String getOwnerId() {
    return this.ownerId;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  @Column(nullable = false)
  public Integer getStatus() {
    return this.status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(nullable = false, length = 20)
  public String getStyleId() {
    return this.styleId;
  }

  public void setStyleId(String styleId) {
    this.styleId = styleId;
  }

  @Column(nullable = false, length = 20)
  public String getColorId() {
    return this.colorId;
  }

  public void setColorId(String colorId) {
    this.colorId = colorId;
  }

  @Column(nullable = false, length = 10)
  public String getSizeId() {
    return this.sizeId;
  }

  public void setSizeId(String sizeId) {
    this.sizeId = sizeId;
  }

  @Column(nullable = false, length = 50)
  public String getSku() {
    return this.sku;
  }

  public void setSku(String sku) {
    this.sku = sku;
  }

  @Column(nullable = false)
  public long getQty() {
    return this.qty;
  }

  public void setQty(long qty) {
    this.qty = qty;
  }

  @Column(nullable = false)
  public long getStartNum() {
    return this.startNum;
  }

  public void setStartNum(long startNum) {
    this.startNum = startNum;
  }

  @Column(nullable = false)
  public long getEndNum() {
    return this.endNum;
  }

  public void setEndNum(long endNum) {
    this.endNum = endNum;
  }

  // //===================>
 private String unicode;

  @Transient
  public String getUnicode() {
    return unicode;
  }

  public void setUnicode(String unicode) {
    this.unicode = unicode;
  }

  @Transient
  public String getStyleName() {
    return styleName;
  }

  public void setStyleName(String styleName) {
    this.styleName = styleName;
  }

  @Transient
  public String getColorName() {
    return colorName;
  }

  public void setColorName(String colorName) {
    this.colorName = colorName;
  }

  @Transient
  public String getSizeName() {
    return sizeName;
  }

  public void setSizeName(String sizeName) {
    this.sizeName = sizeName;
  }
//john 添加0,没有编辑过，1编辑过
  private Integer editStatus;
  
	@Column()
	public Integer getEditStatus() {
		return editStatus;
	}

	public void setEditStatus(Integer editStatus) {
		this.editStatus = editStatus;
	}
}