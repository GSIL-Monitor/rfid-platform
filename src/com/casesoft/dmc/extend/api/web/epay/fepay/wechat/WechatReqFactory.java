package com.casesoft.dmc.extend.api.web.epay.fepay.wechat;

import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.extend.api.web.epay.fepay.base.EPayInfo;
import com.tencent.protocol.pay_protocol.ScanPayReqData;
import com.tencent.protocol.pay_query_protocol.ScanPayQueryReqData;
import com.tencent.protocol.refund_protocol.RefundReqData;
import com.tencent.protocol.refund_query_protocol.RefundQueryReqData;
import com.tencent.protocol.reverse_protocol.ReverseReqData;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

public class WechatReqFactory {

	/**
	 * 扫码支付
	 * 
	 * @param authCode
	 *            这个是扫码终端设备从用户手机上扫取到的支付授权号，这个号是跟用户用来支付的银行卡绑定的，有效期是1分钟
	 * @param body
	 *            要支付的商品的描述信息，用户会在支付成功页面里看到这个信息
	 * @param attach
	 *            支付订单里面可以填的附加数据，API会将提交的这个附加数据原样返回
	 * @param outTradeNo
	 *            商户系统内部的订单号,32个字符内可包含字母, 确保在商户系统唯一
	 * @param totalFee
	 *            订单总金额，单位为“分”，只能整数
	 * @param deviceInfo
	 *            商户自己定义的扫码支付终端设备号，方便追溯这笔交易发生在哪台终端设备上
	 * @param spBillCreateIP
	 *            订单生成的机器IP
	 * @param timeStart
	 *            订单生成时间， 格式为yyyyMMddHHmmss，如2009年12 月25 日9 点10 分10
	 *            秒表示为20091225091010。时区为GMT+8 beijing。该时间取自商户服务器
	 * @param timeExpire
	 *            订单失效时间，格式同上
	 * @param goodsTag
	 *            商品标记，微信平台配置的商品标记，用于优惠券或者满减使用
	 * @throws UnknownHostException
	 */
	@SuppressWarnings("deprecation")
	public static ScanPayReqData productScanPayReqData(EPayInfo info) {
		Date timeExpire = info.getTimestamp();
		timeExpire.setMinutes(timeExpire.getMinutes() + 1);
		String ip = "0.0.0.0";
		try {
			ip = InetAddress.getLocalHost().getHostAddress().toString();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			return new ScanPayReqData(info.getAuthCode(), info.getSubject(),
					null, info.getOutTradeNo(),
					(int) (info.getTotalAmount() * 100), info.getDeviceId(), ip,
					CommonUtil.getDateString(info.getTimestamp(),
							"yyyyMMddHHmmss"), CommonUtil.getDateString(
							timeExpire, "yyyyMMddHHmmss"), null);
		}
	}

	/**
	 * 请求支付查询服务
	 * 
	 * @param transactionID
	 *            是微信系统为每一笔支付交易分配的订单号，通过这个订单号可以标识这笔交易，它由支付订单API支付成功时返回的数据里面获取到。
	 *            建议优先使用
	 * @param outTradeNo
	 *            商户系统内部的订单号,transaction_id 、out_trade_no
	 *            二选一，如果同时存在优先级：transaction_id>out_trade_no
	 * @return API返回的XML数据
	 * @throws Exception
	 */
	public static ScanPayQueryReqData productScanPayQueryReqData(EPayInfo info) {
		return new ScanPayQueryReqData(info.getTradeCode(),
				info.getOutTradeNo());
	}

	/**
	 * 请求退款服务
	 * 
	 * @param transactionID
	 *            是微信系统为每一笔支付交易分配的订单号，通过这个订单号可以标识这笔交易，它由支付订单API支付成功时返回的数据里面获取到。
	 *            建议优先使用
	 * @param outTradeNo
	 *            商户系统内部的订单号,transaction_id 、out_trade_no
	 *            二选一，如果同时存在优先级：transaction_id>out_trade_no
	 * @param deviceInfo
	 *            微信支付分配的终端设备号，与下单一致
	 * @param outRefundNo
	 *            商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
	 * @param totalFee
	 *            订单总金额，单位为分
	 * @param refundFee
	 *            退款总金额，单位为分,可以做部分退款
	 * @param opUserID
	 *            操作员帐号, 默认为商户号
	 * @param refundFeeType
	 *            货币类型，符合ISO 4217标准的三位字母代码，默认为CNY（人民币）
	 */
	public static RefundReqData productRefundReqData(EPayInfo info) {
		return new RefundReqData(info.getTradeCode(),null, info.getDeviceId(),
				info.getOutTradeNo(), (int) (info.getTotalAmount() * 100),
				(int) (info.getRefundAmount() * 100), info.getOperatorCode(),
				null);
	}
	 /**
     * 请求退款查询服务
     * @param transactionID 是微信系统为每一笔支付交易分配的订单号，通过这个订单号可以标识这笔交易，它由支付订单API支付成功时返回的数据里面获取到。建议优先使用
     * @param outTradeNo 商户系统内部的订单号,transaction_id 、out_trade_no 二选一，如果同时存在优先级：transaction_id>out_trade_no
     * @param deviceInfo 微信支付分配的终端设备号，与下单一致
     * @param outRefundNo 商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
     * @param refundID 来自退款API的成功返回，微信退款单号refund_id、out_refund_no、out_trade_no 、transaction_id 四个参数必填一个，如果同事存在优先级为：refund_id>out_refund_no>transaction_id>out_trade_no
     */
	public static RefundQueryReqData productRefundQueryReqData(EPayInfo info) {
		return new RefundQueryReqData(null, null,
				info.getDeviceId(), info.getOutTradeNo(), null);
	}

    /**
     * 请求撤销服务
     * @param transactionID 是微信系统为每一笔支付交易分配的订单号，通过这个订单号可以标识这笔交易，它由支付订单API支付成功时返回的数据里面获取到。建议优先使用
     * @param outTradeNo 商户系统内部的订单号,transaction_id 、out_trade_no 二选一，如果同时存在优先级：transaction_id>out_trade_no
     * @return API返回的XML数据
     * @throws Exception
     */
	public static ReverseReqData productReverseReqData(EPayInfo info) {
		return new ReverseReqData(info.getTradeCode(),info.getOutTradeNo());
	}
}
