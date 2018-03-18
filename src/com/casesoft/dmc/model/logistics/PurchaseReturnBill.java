package com.casesoft.dmc.model.logistics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Session on 2017-06-28.
 */
@Entity
@Table(name = "LOGISTICS_PURCHASERETURNBILL")
public class PurchaseReturnBill extends BaseBill{
	@Id
	@Column()
	private String id;
	@Column()
	private Long totOutQty=0L;//
	@Column()
	private Long totInQty=0L;
	@Column()
	private Integer outStatus=0;
	@Column()
	private Integer inStatus =0;
	@Column()
	private Double totOutVal=0D;//

	@Column()
	private  Double totInVal = 0D;

	@Column
	private Double totStockVal =0D;//库存成本

	public Double getTotStockVal() {
		return totStockVal;
	}
	public void setTotStockVal(Double totStockVal) {
		this.totStockVal = totStockVal;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public Long getTotOutQty() {
		return totOutQty;
	}

	public void setTotOutQty(Long totOutQty) {
		this.totOutQty = totOutQty;
	}


	public Double getTotOutVal() {
		return totOutVal;
	}

	public void setTotOutVal(Double totOutVal) {
		this.totOutVal = totOutVal;
	}

	public Long getTotInQty() {
		return totInQty;
	}

	public void setTotInQty(Long totInQty) {
		this.totInQty = totInQty;
	}

	public Double getTotInVal() {
		return totInVal;
	}

	public void setTotInVal(Double totInVal) {
		this.totInVal = totInVal;
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
}
