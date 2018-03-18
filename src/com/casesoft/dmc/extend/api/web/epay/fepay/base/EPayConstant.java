package com.casesoft.dmc.extend.api.web.epay.fepay.base;
/**
 * @author john
 * 
 * */
public class EPayConstant {
	/**
	 * 支付类型
	 * */
	public class EpayType{
		public static final String WECHAT_TYPE_A="WECAHT";//扫描
		public static final String WECHAT_TYPE_P="WECAHT_P";//被扫描
		public static final String ALYPAY_TYPE_A ="ALIPAY";//扫描
		public static final String ALYPAY_TYPE_P ="ALIPAY_P";//被扫描

		public static final String IBOX_WECHAT_TYPE_A="IBOX_WECAHT";//盒子支付扫描
		public static final String IBOX_WECHAT_TYPE_P="IBOX_WECAHT_P";//盒子支付被扫描
		public static final String IBOX_ALYPAY_TYPE_A ="IBOX_ALIPAY";//盒子支付扫描
		public static final String IBOX_ALYPAY_TYPE_P ="IBOX_ALIPAY_P";//盒子支付被扫描
		public static final String IBOX_CARD_TYPE_P ="IBOX_CARD_P";//盒子卡类支付
		public static final String IBOX_TYPE ="IBOX";//盒子未知




	};
	/**
	 * 操作类型
	 * 
	 * */
	public class TradeType{
		public static final String PAY_TYPE="PAY";
		public static final String QUERY_TYPE="QUERY";
		public static final String REFUND_TYPE="REFUND";
		public static final String CANCEL_TYPE="CANCEL";
	}
	public class TradeStatus{
		public static final String WAIT_STATUS="WAIT";//等待付款
		public static final String CLOSED_STATUS="CLOSED";//支付关闭
		public static final String SUCCESS_STATUS="SUCCESS";//支付完成
		public static final String FINISHED_STATUS="FINISHED";//已完成不能退款
		public static final String EXCEPTION_STATUS="EXCEPTION";//第三方支付内部错误
	}
}
