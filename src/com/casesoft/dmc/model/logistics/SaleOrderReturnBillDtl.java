package com.casesoft.dmc.model.logistics;

import javax.persistence.*;

/**
 * Created by Session on 2017-06-28.
 */
@Entity
@Table(name = "LOGISTICS_SaleOrderRETURN_DTL")
public class SaleOrderReturnBillDtl extends BaseBillDtl{
	@Id
	@Column
	private String id;

	@Column
	private Integer outStatus=0;
	@Column
	private Integer inStatus=0;
	@Column
	private Long inQty=0L;
	@Column
	private Long outQty =0L;
	@Column
	private Double outVal =0D;
	@Column
	private Double inVal =0D;
	@Column
	private Double stockVal=0D;//库存成本
	@Transient
	private Double tagPrice;

	@Transient
	private String noOutPutCode;//不能出库的唯一码

	public String getNoOutPutCode() {
		return noOutPutCode;
	}

	public void setNoOutPutCode(String noOutPutCode) {
		this.noOutPutCode = noOutPutCode;
	}

	public Double getTagPrice() {
		return tagPrice;
	}

	public void setTagPrice(Double tagPrice) {
		this.tagPrice = tagPrice;
	}

	public Double getStockVal() {
		return stockVal;
	}

	public void setStockVal(Double stockVal) {
		this.stockVal = stockVal;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getOutStatus() {
		return outStatus;
	}
	public void setOutStatus(Integer outStatus) {
		this.outStatus = outStatus;
	}
	public Integer getInStatus() {
		return inStatus;
	}
	public void setInStatus(Integer inStatus) {
		this.inStatus = inStatus;
	}
	public Long getInQty() {
		return inQty;
	}
	public void setInQty(Long inQty) {
		this.inQty = inQty;
	}
	public Long getOutQty() {
		return outQty;
	}
	public void setOutQty(Long outQty) {
		this.outQty = outQty;
	}
	public Double getOutVal() {
		return outVal;
	}
	public void setOutVal(Double outVal) {
		this.outVal = outVal;
	}
	public Double getInVal() {
		return inVal;
	}
	public void setInVal(Double inVal) {
		this.inVal = inVal;
	}


	
}
