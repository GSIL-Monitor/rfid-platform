package com.casesoft.dmc.extend.api.web.epay.fepay.alipay.constants;

public class AliPayCode {
	/**
	 * 返回码
	 * */
	public class ResultCode {
		public final static String SUCCESS = "10000";// 业务处理成功
		public final static String FAILURE = "40004";// 业务处理失败
		public final static String PROCCESS = "10003";// 业务处理中
		public final static String EXCEPTION = "20000";// 业务出现未知错误或者系统异常

	}
	/**
	 * 交易状态
	 * 
	 * */
	public class TradeStatus{
		public final static String WAIT_BUYER_PAY = "WAIT_BUYER_PAY";// 交易创建，等待买家付款
		public final static String TRADE_CLOSED = "TRADE_CLOSED";//未付款交易超时关闭，支付完成后全额退款
		public final static String TRADE_SUCCESS = "TRADE_SUCCESS";//交易支付成功
		public final static String TRADE_FINISHED = "TRADE_FINISHED";// 交易结束，不可退款
	}

}
