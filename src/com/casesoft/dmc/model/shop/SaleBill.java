package com.casesoft.dmc.model.shop;


import com.alibaba.fastjson.annotation.JSONField;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.Record;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Wing Li on 2014/6/21.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "shop_SaleBill")
public class SaleBill extends BaseModel {
    

    @Column( length = 50)
	private String srcBillId;// 零售退货时，关联的零售单
	@Column( nullable = false)
	private Double totActValue;// 实际总销售额
	@Column()
	private Integer payWay;// 支付方式 0：现金 1.积分卡

    @Column()
    private Double increaseGrate;//增长的积分
    @Column()
    private String mileageCode;//积分增长率代码
	@Column()
	private Double gradeRate;// 积分增长率
	@Column(nullable = false, length = 50)
	private String shopId;

	@Column()
	private String scanTime;
	@Column()
	private Double actCashValue;// 实际现金
	@Column()
	private Double actCardValue;// 信用卡消费
	@Column()
	private Double actVoucherValue;// 购物券
	@Column()
	private Double actGradeValue;// 使用积分
	@Column()
	private Double alipay;//支付宝支付
	
	@Column()
	private Double wxpay;//微信支付
	
	@Column(length = 30)
	private String alipayAccount;//支付宝支付帐号

	@Column(length = 30)
	private String wxpayAccount;//微信支付帐号
	@Column (length = 30)
	private String promotionNo;

	@Column()
	private Double payForCash;// 付款
	@Column()
	private Double backForCash;// 找零
	@Column()
	private Double toZero;// 抹零
	@Column()
	private Double balance;// 差额
	@Column(length=40)
	private String epayOrderNo;//电商支付订单号

	private Integer status=0;
	@Column(name="status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Double getPayForCash() {
		return payForCash;
	}

	public String getEpayOrderNo() {
		return epayOrderNo;
	}

	public void setEpayOrderNo(String epayOrderNo) {
		this.epayOrderNo = epayOrderNo;
	}

	public void setPayForCash(Double payForCash) {
		this.payForCash = payForCash;
	}

	public Double getBackForCash() {
		return backForCash;
	}

	public void setBackForCash(Double backForCash) {
		this.backForCash = backForCash;
	}
	public Double getToZero() {
		return toZero;
	}

	public void setToZero(Double toZero) {
		this.toZero = toZero;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Double getActGradeValue() {
		return actGradeValue;
	}

	public void setActGradeValue(Double gradeValue) {
		this.actGradeValue = gradeValue;
	}

	public Double getActVoucherValue() {
		return actVoucherValue;
	}

	public void setActVoucherValue(Double actVoucherValue) {
		this.actVoucherValue = actVoucherValue;
	}

	public Double getActCardValue() {
		return actCardValue;
	}

	public void setActCardValue(Double actCardValue) {
		this.actCardValue = actCardValue;
	}

	public Double getActCashValue() {
		return actCashValue;
	}

	public void setActCashValue(Double actCashValue) {
		this.actCashValue = actCashValue;
	}
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public String getScanTime() {
		return scanTime;
	}

	public void setScanTime(String scanTime) {
		this.scanTime = scanTime;
	}

	public Double getGradeRate() {
		return gradeRate;
	}

	public void setGradeRate(Double gradeRate) {
		this.gradeRate = gradeRate;
	}

	public String getSrcBillId() {
		return srcBillId;
	}

	public void setSrcBillId(String srcBillId) {
		this.srcBillId = srcBillId;
	}

	public Double getTotActValue() {
		return totActValue;
	}

	public void setTotActValue(Double totActValue) {
		this.totActValue = totActValue;
	}

	@Transient
	private Customer customer;//对应client2Id
	@Transient
	private List<Record> recordList;
	@Transient
	private List<SaleBillDtl> dtlList;

	@Transient
	private Business business;
	@Transient
	private String payWayName;

	public String getPayWayName() {
		return payWayName;
	}

	public void setPayWayName(String payWayName) {
		this.payWayName = payWayName;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Customer getCustomer() {
		return customer;
	}

	public Integer getPayWay() {
		return payWay;
	}

	public void setPayWay(Integer payWay) {
		this.payWay = payWay;
	}

	@Transient
	private String shopName;// 门店

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public List<Record> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<Record> recordList) {
		this.recordList = recordList;
	}

	@Transient
	public List<SaleBillDtl> getDtlList() {
		return dtlList;
	}

	public void setDtlList(List<SaleBillDtl> dtlList) {
		this.dtlList = dtlList;
	}

    public Double getIncreaseGrate() {
        return increaseGrate;
    }

    public void setIncreaseGrate(Double increaseGrate) {
        this.increaseGrate = increaseGrate;
    }

    public String getMileageCode() {
        return mileageCode;
    }

    public void setMileageCode(String mileageCode) {
        this.mileageCode = mileageCode;
    }
    
    public Double getAlipay() {
		return alipay;
	}

	public void setAlipay(Double alipay) {
		this.alipay = alipay;
	}

	public Double getWxpay() {
		return wxpay;
	}

	public void setWxpay(Double wxpay) {
		this.wxpay = wxpay;
	}
	
	public String getAlipayAccount() {
		return alipayAccount;
	}

	public void setAlipayAccount(String alipayAccount) {
		this.alipayAccount = alipayAccount;
	}

	public String getWxpayAccount() {
		return wxpayAccount;
	}

	public void setWxpayAccount(String wxpayAccount) {
		this.wxpayAccount = wxpayAccount;
	}
	public String getPromotionNo() {
		return promotionNo;
	}

	public void setPromotionNo(String promotionNo) {
		this.promotionNo = promotionNo;
	}

}
