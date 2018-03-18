package com.casesoft.dmc.extend.api.web.epay.fepay.base;

import com.casesoft.dmc.core.vo.MessageBox;

public interface IEPayController <T>{
	public MessageBox barPay(T t);// 条码支付

	public MessageBox qrPay(T t);// 二维码支付

	public MessageBox query(T t);// 查询

	/**
	 * 调用支付宝支付接口时未返回明确的返回结果(如由于系统错误或网络异常导致无返回结果)，可使用本接口将交易进行撤销。
	 * 如果用户支付失败，支付宝会将此订单关闭；如果用户支付成功，支付宝会将支付的资金退还给用户。
	 * */
	public MessageBox cancelOrder(T t);// 取消订货单

	/**
	 * 当交易发生之后一段时间内，由于买家或者卖家的原因需退款，卖家可通过退款接口将支付款退还给买家，支付宝将在收到退款请求并验证成功后，
	 * 按退款规则将支付款按原路退到买家帐号上。 交易超过可退款时间(签约时设置的可退款时间)的订单无法进行退款
	 * 支付宝退款支持单笔交易分多次退款，多次退款需要提交支付宝交易号并设置不同的退款单号；总退款金额不能超过用户实际支付金额。
	 * 分多笔退款时，若一笔退款失败需重新提交，要采用原来的退款单号；
	 */
	public MessageBox refundOrder(T t);// 退订单

}
