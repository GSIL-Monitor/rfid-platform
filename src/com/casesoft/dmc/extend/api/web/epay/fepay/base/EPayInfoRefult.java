package com.casesoft.dmc.extend.api.web.epay.fepay.base;

/**
 * @author john
 * 支付返回信息
 * */
public class EPayInfoRefult {
	
	private String tradeId;//单id
	private String tradeCode;//支付宝交易号
	private String outTradeNo;//商户订单号
	private String resultCode;//返回码
	private String resultMsg;//返回信息
	private String subErrorMsg;//错误明细
	private String subErrorCode;//错误明细代码
	private String totalAmount;//总金额
	private String tradeStatus;//单据状态
	private String buyerId;//买家ID
	private String retryFlag="Y";//是否可以重复操作
	

	public String getRetryFlag() {
		return retryFlag;
	}
	public void setRetryFlag(String retryFlag) {
		this.retryFlag = retryFlag;
	}
	public String getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}
	public String getTradeStatus() {
		return tradeStatus;
	}
	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
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
	
}
