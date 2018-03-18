package com.casesoft.dmc.model.shop;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Wing Li on 2014/6/21.
 */
@Entity
@Table(name = "SHOP_SALEBILLDTL")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class SaleBillDtl extends BaseModelDtl {

	private double actPrice;// 实际单价

	private double percent;// 打折率

	private double actValue;// 实际销售额

	private String priceType;// 单价类型

	private String uniqueCode;

	private double refundPrice;
	private String refundBillId;

	private double increaseGrate;// 增长的积分
	private String rackId;//货架编号
	private double gradeRate;// 积分增长率
	@Column(length=50)
	public String getRackId() {
		return rackId;
	}

	public void setRackId(String rackId) {
		this.rackId = rackId;
	}

	@Column()
	public double getGradeRate() {
		return gradeRate;
	}

	public void setGradeRate(double gradeRate) {
		this.gradeRate = gradeRate;
	}

	@Column()
	public double getIncreaseGrate() {
		return increaseGrate;
	}

	public void setIncreaseGrate(double increaseGrate) {
		this.increaseGrate = increaseGrate;
	}

	@Column(length = 50)
	private String client2Id;
	private Date billDate;

	public String getClient2Id() {
		return client2Id;
	}

	public void setClient2Id(String client2Id) {
		this.client2Id = client2Id;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@Column()
	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	@Column(length = 32)
	public String getUniqueCode() {
		return uniqueCode;
	}

	public void setUniqueCode(String uniqueCode) {
		this.uniqueCode = uniqueCode;
	}

	@Column(nullable = true)
	public double getActPrice() {
		return actPrice;
	}

	public void setActPrice(double actPrice) {
		this.actPrice = actPrice;
	}

	@Column(nullable = true)
	public double getPercent() {
		return percent;
	}

	public void setPercent(double percent) {
		this.percent = percent;
	}

	@Column( nullable = true)
	public double getActValue() {
		return actValue;
	}

	public void setActValue(double actValue) {
		this.actValue = actValue;
	}

	@Column( length = 10)
	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

	@Column()
	public double getRefundPrice() {
		return refundPrice;
	}

	public void setRefundPrice(double refundPrice) {
		this.refundPrice = refundPrice;
	}

	@Column()
	public String getRefundBillId() {
		return refundBillId;
	}

	public void setRefundBillId(String refundBillId) {
		this.refundBillId = refundBillId;
	}
	@Column( length = 20)
	private String barcode;
	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	private String promotionNo;
	@Column(length = 20)

	public String getPromotionNo() {
		return promotionNo;
	}

	public void setPromotionNo(String promotionNo) {
		this.promotionNo = promotionNo;
	}
}
