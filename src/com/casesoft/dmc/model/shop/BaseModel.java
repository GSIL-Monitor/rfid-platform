package com.casesoft.dmc.model.shop;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.beans.Transient;
import java.util.Date;

/**
 * Created by Wing Li on 2014/6/21.
 */
@MappedSuperclass
public abstract class BaseModel {
	@Id
	@Column(nullable = false, length = 50)
	private String id;
	@Column( nullable = false, length = 50)
	private String ownerId;
	@Column(nullable = false, length = 50)
	private String billNo;

	@Column( nullable = false, length = 19)
	private Date billDate;
	@Column(length = 50)
	private String client2Id;// 顾客
	@Column( length = 50)
	private String clientId;// 收银员

	@Column(length = 200)
	private String remark;
	@Column()
	private Long totOrderQty;// 订单总数量
	@Column()
	private double totOrderValue;// 订单总金额
	@Column()
	private double totOrderTax;// 订单总税额,交多少税
	@Column( nullable = false)
	private int type;
	@Column()
	private int isRfid;

	private String clientName;// 收银员
	private String client2Name;// 顾客

	@Transient
	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	@Transient
	public String getClient2Name() {
		return client2Name;
	}

	public void setClient2Name(String client2Name) {
		this.client2Name = client2Name;
	}

	public int getIsRfid() {
		return isRfid;
	}

	public void setIsRfid(int isRfid) {
		this.isRfid = isRfid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public String getClient2Id() {
		return client2Id;
	}

	public void setClient2Id(String client2Id) {
		this.client2Id = client2Id;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getTotOrderQty() {
		return totOrderQty;
	}

	public void setTotOrderQty(Long totOrderQty) {
		this.totOrderQty = totOrderQty;
	}

	public double getTotOrderValue() {
		return totOrderValue;
	}

	public void setTotOrderValue(double totOrderValue) {
		this.totOrderValue = totOrderValue;
	}

	public double getTotOrderTax() {
		return totOrderTax;
	}

	public void setTotOrderTax(double totOrderTax) {
		this.totOrderTax = totOrderTax;
	}
}
