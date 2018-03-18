package com.casesoft.dmc.model.product;

import javax.persistence.*;

/**
 * Size entity. @author
 */
@Entity
@Table(name = "PRODUCT_SIZE")
public class Size extends BaseModel implements java.io.Serializable {

  private static final long serialVersionUID = -8720185688061232123L;
 // @JSONField(serialize = false)
  private String id;
  private String sizeId;
  private String sizeName;
  private String sortId;
  private String sortName;

  private Integer seqNo;
  private String isUse;

 
  private String brandCode;
  @Column(length = 30)
  public String getBrandCode() {
    return brandCode;
  }

  public void setBrandCode(String brandCode) {
    this.brandCode = brandCode;
  }

  // Constructors

  /** default constructor */
  public Size() {
  }

  /** minimal constructor */
  public Size(String sizeNo, String sizeName) {
    this.sizeId = sizeNo;
    this.sizeName = sizeName;
  }

  public Size(String id, String sizeId, String sizeName, String sortId) {
	super();
	this.id = id;
	this.sizeId = sizeId;
	this.sizeName = sizeName;
	this.sortId = sortId;
}
  public Size(String id, String sizeId, String sizeName, String sortId,Integer seqNo) {
		super();
		this.id = id;
		this.sizeId = sizeId;
		this.sizeName = sizeName;
		this.sortId = sortId;
		this.seqNo = seqNo;
	}

/** full constructor */
  public Size(String sizeNo, String sizeName, String sortId) {
    this.sizeId = sizeNo;
    this.sizeName = sizeName;
    this.sortId = sortId;
  }

  public Size(String id, String sizeId, String sizeName, String sortId, String brandCode,Integer seqNo) {
	super();
	this.id = id;
	this.sizeId = sizeId;
	this.sizeName = sizeName;
	this.sortId = sortId;
	this.brandCode = brandCode;
	this.seqNo=seqNo;
}

// Property accessors
  @Id
  @Column(nullable = false, length = 10)
  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Column(unique = true, nullable = false, length = 10)
  public String getSizeId() {
    return this.sizeId;
  }

  public void setSizeId(String sizeNo) {
    this.sizeId = sizeNo;
  }

  @Column(nullable = false, length = 45)
  public String getSizeName() {
    return this.sizeName;
  }

  public void setSizeName(String sizeName) {
    this.sizeName = sizeName;
  }

  @Column(length = 45)
  public String getSortId() {
    return this.sortId;
  }

  public void setSortId(String sortId) {
    this.sortId = sortId;
  }

  @Column()
  public Integer getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  @Transient
    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    @Column(length = 2)
    public String getIsUse() {
        return isUse;
    }

    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }

    @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((sizeId == null) ? 0 : sizeId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Size other = (Size) obj;
    if (sizeId == null) {
      if (other.sizeId != null)
        return false;
    } else if (!sizeId.equals(other.sizeId))
      return false;
    return true;
  }


}