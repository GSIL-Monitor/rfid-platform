package com.casesoft.dmc.extend.api.web.epay.fepay.base;

import javax.persistence.*;
import java.util.Date;

/**
 * 电子账单
 * */
@Entity
@Table(name="epay_Info")
public class
EPayInfo {
	@SequenceGenerator(name = "S_EpayInfo", sequenceName = "S_EpayInfo")
	@GeneratedValue(generator = "S_EpayInfo", strategy = GenerationType.SEQUENCE)
	@Id
	@Column(name = "id")
	private Long id;
	@Column(name = "tradeType",nullable = false, length = 20)
	private String tradeType;
	@Column(name = "sellerId",length = 30)
	private String sellerId;//卖家支付宝用户 ID
	@Column(name = "operatorCode",nullable = false, length = 20)
	private String operatorCode;//操作员
	@Column(name = "tradeId", length = 35)
	private String tradeId;//单id
	@Column(name = "tradeCode", length = 100)
	private String tradeCode;//支付宝交易号
	@Column(name = "outTradeNo", length = 100,nullable=false)
	private String outTradeNo;//商户订单号
	@Column(name = "deviceId", length = 20,nullable=false)
	private String deviceId;//设备Id
	@Column(name = "taskId", length = 35,nullable=false)
	private String taskId;//任务号
	@Column(name = "totalAmount",nullable=false)
	private double totalAmount;//交易额
	@Column(name = "refundAmount",nullable=false)
	private double refundAmount;//交易额
	@Column(name = "receiptAmount")
	private double receiptAmount;//商户实际收到
	@Column(name = "buyerPayAmount")
	private double buyerPayAmount;//买家实际付款

	@Column(name = "authCode", length = 35)
	private String authCode;//条码、二维码（用户编码）
	@Column(name = "type", length = 20,nullable=false)
	private String type;//方式，微信，支付宝，银行卡等
	@Column(name = "remark", length = 300,nullable=false)
	private String remark;
	@Column(name = "timestamp",nullable=false)
	private Date timestamp;
	@Column(name = "gmtPayment")
	private String  gmtPayment;
	@Column(name = "resultCode", length = 35,nullable=false)
	private String resultCode;//返回码
	@Column(name = "resultMsg", length = 300,nullable=false)
	private String resultMsg;//返回信息

	@Column(name = "subErrorMsg", length = 300)
	private String subErrorMsg;//错误明细
	@Column(name = "subErrorCode", length = 35)
	private String subErrorCode;//错误明细代码
	
	@Column(name = "openId", length = 100)
	private String openId;//买家支付宝用户号
	@Column(name = "buyerLogonId", length = 100)
	private String buyerLogonId;//买家支付宝账号
	@Column(name = "subject", nullable=false,length = 300)
	private String subject;//主题
	@Column(name = "goodDetail", length = 500)
	private String goodDetail;//明细（sku）
	
	public double getRefundAmount() {
		return refundAmount;
	}
	public void setRefundAmount(double refundAmount) {
		this.refundAmount = refundAmount;
	}
	public double getBuyerPayAmount() {
		return buyerPayAmount;
	}
	public void setBuyerPayAmount(double buyerPayAmount) {
		this.buyerPayAmount = buyerPayAmount;
	}
	public double getReceiptAmount() {
		return receiptAmount;
	}
	public void setReceiptAmount(double receiptAmount) {
		this.receiptAmount = receiptAmount;
	}
	public String getGmtPayment() {
		return gmtPayment;
	}
	public void setGmtPayment(String gmtPayment) {
		this.gmtPayment = gmtPayment;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getGoodDetail() {
		return goodDetail;
	}
	public void setGoodDetail(String goodDetail) {
		this.goodDetail = goodDetail;
	}
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultMsg() {
		return resultMsg;
	}
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	public String getSubErrorMsg() {
		return subErrorMsg;
	}
	public void setSubErrorMsg(String subErrorMsg) {
		this.subErrorMsg = subErrorMsg;
	}
	public String getSubErrorCode() {
		return subErrorCode;
	}
	public void setSubErrorCode(String subErrorCode) {
		this.subErrorCode = subErrorCode;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getBuyerLogonId() {
		return buyerLogonId;
	}
	public void setBuyerLogonId(String buyerLogonId) {
		this.buyerLogonId = buyerLogonId;
	}
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public String getOperatorCode() {
		return operatorCode;
	}
	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}
	public String getTradeId() {
		return tradeId;
	}
	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}
	public String getTradeCode() {
		return tradeCode;
	}
	public void setTradeCode(String tradeCode) {
		this.tradeCode = tradeCode;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
