package com.casesoft.dmc.extend.api.web.epay.fepay.wechat;

import com.casesoft.dmc.core.vo.MessageBox;


import com.casesoft.dmc.extend.api.web.epay.fepay.base.EPayConstant;
import com.casesoft.dmc.extend.api.web.epay.fepay.base.EPayInfo;
import com.casesoft.dmc.extend.api.web.epay.fepay.base.EPayInfoRefult;
import com.casesoft.dmc.extend.api.web.epay.fepay.base.IEPayTarget;
import com.casesoft.dmc.extend.api.web.epay.fepay.wechat.extend.WechatSDK;
import com.casesoft.dmc.extend.api.web.epay.fepay.wechat.extend.business.ReverseBusiness;
import com.casesoft.dmc.extend.api.web.epay.fepay.wechat.extend.business.ScanPayQueryBusiness;
import com.tencent.business.RefundBusiness;
import com.tencent.business.ScanPayBusiness;
import com.tencent.protocol.pay_protocol.ScanPayReqData;
import com.tencent.protocol.pay_protocol.ScanPayResData;
import com.tencent.protocol.pay_query_protocol.ScanPayQueryReqData;
import com.tencent.protocol.pay_query_protocol.ScanPayQueryResData;
import com.tencent.protocol.refund_protocol.RefundResData;
import com.tencent.protocol.reverse_protocol.ReverseReqData;
import com.tencent.protocol.reverse_protocol.ReverseResData;
import com.thoughtworks.xstream.XStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WechatAdapter implements IEPayTarget {
	private Logger logger = LoggerFactory.getLogger(getClass());
 	private EPayInfo ePayInfo;
	public WechatAdapter(EPayInfo ePayInfo) {
		this.ePayInfo = ePayInfo;
        XStream xStreamForResponseData = new XStream();

		WechatSDK.initSDKConfiguration(WechatConfigure.getKey(),
				WechatConfigure.getAppID(), WechatConfigure.getMchID(),
				WechatConfigure.getSubMchID(),
				WechatConfigure.getCertLocalPath(),
				WechatConfigure.getCertPassword());
	}

	@SuppressWarnings("finally")
	@Override
	public MessageBox barPay() {
		logger.info("条码支付");
		logger.info(ePayInfo.getType() + ":支付.类型：" + ePayInfo.getTradeType());
		final MessageBox messageBox= new MessageBox();
		final EPayInfoRefult ePayInfoRefult = new EPayInfoRefult();
		
		try {
			ScanPayReqData scanPayReqData = WechatReqFactory
					.productScanPayReqData(ePayInfo);
			WechatSDK.doScanPayBusiness(scanPayReqData,
					new ScanPayBusiness.ResultListener() {

						@Override
						public void onFail(ScanPayResData scanPayResData) {
							 WechatUtil.convertToScanPayRes(scanPayResData, ePayInfo, ePayInfoRefult);
							ePayInfoRefult.setRetryFlag("N");
							ePayInfoRefult
									.setTradeStatus(EPayConstant.TradeStatus.EXCEPTION_STATUS);
							messageBox.setSuccess(false);
							messageBox.setMsg("微信业务处理失败！支付失败！");
							messageBox.setResult(ePayInfoRefult);
						}

						@Override
						public void onFailByAuthCodeExpire(
								ScanPayResData scanPayResData) {
							WechatUtil.convertToScanPayRes(scanPayResData, ePayInfo, ePayInfoRefult);
							ePayInfoRefult.setRetryFlag("N");
							ePayInfoRefult
									.setTradeStatus(EPayConstant.TradeStatus.EXCEPTION_STATUS);
							messageBox.setSuccess(false);
							messageBox.setMsg("微信业务处理失败！用户用来支付的二维码已经过期！");
							messageBox.setResult(ePayInfoRefult);
						}

						@Override
						public void onFailByAuthCodeInvalid(
								ScanPayResData scanPayResData) {
							WechatUtil.convertToScanPayRes(scanPayResData, ePayInfo, ePayInfoRefult);
							ePayInfoRefult.setRetryFlag("N");
							ePayInfoRefult
									.setTradeStatus(EPayConstant.TradeStatus.EXCEPTION_STATUS);
							messageBox.setSuccess(false);
							messageBox.setMsg("微信业务处理失败！授权码无效，提示用户刷新一维码/二维码，之后重新扫码支付！");
							messageBox.setResult(ePayInfoRefult);
						}

						@Override
						public void onFailByMoneyNotEnough(
								ScanPayResData scanPayResData) {
							WechatUtil.convertToScanPayRes(scanPayResData, ePayInfo, ePayInfoRefult);
							ePayInfoRefult.setRetryFlag("N");
							ePayInfoRefult
									.setTradeStatus(EPayConstant.TradeStatus.EXCEPTION_STATUS);
							messageBox.setSuccess(false);
							messageBox.setMsg("微信业务处理失败！用户余额不足，换其他卡支付或是用现金支付！");
							messageBox.setResult(ePayInfoRefult);
						}

						@Override
						public void onFailByReturnCodeError(
								ScanPayResData scanPayResData) {
							WechatUtil.convertToScanPayRes(scanPayResData, ePayInfo, ePayInfoRefult);
							ePayInfoRefult.setRetryFlag("N");
							ePayInfoRefult
									.setTradeStatus(EPayConstant.TradeStatus.EXCEPTION_STATUS);
							messageBox.setSuccess(false);
							messageBox.setMsg("微信业务处理失败！API返回ReturnCode不合法！");
							messageBox.setResult(ePayInfoRefult);
						}

						@Override
						public void onFailByReturnCodeFail(
								ScanPayResData scanPayResData) {
							WechatUtil.convertToScanPayRes(scanPayResData, ePayInfo, ePayInfoRefult);
							ePayInfoRefult.setRetryFlag("N");
							ePayInfoRefult
									.setTradeStatus(EPayConstant.TradeStatus.EXCEPTION_STATUS);
							messageBox.setSuccess(false);
							messageBox.setMsg("微信业务处理失败！API返回ReturnCode为FAIL！");
							messageBox.setResult(ePayInfoRefult);
						}

						@Override
						public void onFailBySignInvalid(
								ScanPayResData scanPayResData) {
							WechatUtil.convertToScanPayRes(scanPayResData, ePayInfo, ePayInfoRefult);
							ePayInfoRefult.setRetryFlag("N");
							ePayInfoRefult
									.setTradeStatus(EPayConstant.TradeStatus.EXCEPTION_STATUS);
							messageBox.setSuccess(false);
							messageBox.setMsg("微信业务处理失败！签名失败！");
							messageBox.setResult(ePayInfoRefult);
						}

						@Override
						public void onSuccess(ScanPayResData scanPayResData) {
							WechatUtil.convertToScanPayRes(scanPayResData, ePayInfo, ePayInfoRefult);
							ePayInfoRefult
									.setTradeStatus(EPayConstant.TradeStatus.SUCCESS_STATUS);
							ePayInfoRefult.setRetryFlag("N");
							messageBox.setSuccess(true);
							messageBox.setMsg(ePayInfo
									.getResultMsg());
							messageBox.setResult(ePayInfoRefult);
				
						}
					});
		} catch (Exception e) {
			ePayInfoRefult.setTradeCode(EPayConstant.TradeStatus.EXCEPTION_STATUS);
			messageBox.setMsg("系统内部错误");
			messageBox.setSuccess(false);
			messageBox.setResult(ePayInfoRefult);
			logger.error("系统内部错误！");
			e.printStackTrace();
		}finally{			
			return messageBox;
		}

	}

	@Override
	public MessageBox qrPay() {
		return this.barPay();
	}

	@SuppressWarnings("finally")
	@Override
	public MessageBox query() {
		logger.info("查询");
		logger.info(ePayInfo.getType() + ":支付.类型：" + ePayInfo.getTradeType());
		final MessageBox messageBox= new MessageBox();
		final EPayInfoRefult ePayInfoRefult = new EPayInfoRefult();
		try {
		 
			WechatSDK.doScanPayQueryBusiness(new ScanPayQueryReqData(null, ePayInfo.getOutTradeNo()),new ScanPayQueryBusiness.ResultListener(){

				@Override
				public void onFail(ScanPayQueryResData scanPayQueryResData) {
					WechatUtil.convertToScanPayQueryRes(scanPayQueryResData, ePayInfo, ePayInfoRefult);
					ePayInfoRefult.setRetryFlag("N");
					ePayInfoRefult.setTradeStatus(EPayConstant.TradeStatus.EXCEPTION_STATUS);	
					messageBox.setMsg("微信支付查询失败！");
					messageBox.setSuccess(false);
					messageBox.setResult(ePayInfoRefult);
				}

				@Override
				public void onSuccess(ScanPayQueryResData scanPayQueryResData) {
					WechatUtil.convertToScanPayQueryRes(scanPayQueryResData, ePayInfo, ePayInfoRefult);
					ePayInfoRefult.setRetryFlag("N");
				/*	SUCCESS—支付成功
					REFUND—转入退款
					NOTPAY—未支付
					CLOSED—已关闭
					REVOKED—已撤销（刷卡支付）
					USERPAYING--用户支付中
					PAYERROR--支付失败(其他原因，如银行返回失败)*/
					switch(scanPayQueryResData.getTrade_state()){
					case "SUCCESS":
						ePayInfoRefult.setTradeStatus(EPayConstant.TradeStatus.SUCCESS_STATUS);	
						break;
					case "REFUND":
						ePayInfoRefult.setTradeStatus(EPayConstant.TradeStatus.SUCCESS_STATUS);
						break;
					case "NOTPAY":
						ePayInfoRefult.setTradeStatus(EPayConstant.TradeStatus.FINISHED_STATUS);
						break;
					case "CLOSED":
						ePayInfoRefult.setTradeStatus(EPayConstant.TradeStatus.CLOSED_STATUS);
						break;
					case "REVOKED":
						ePayInfoRefult.setTradeStatus(EPayConstant.TradeStatus.FINISHED_STATUS);
						break;
					case "USERPAYING":
						ePayInfoRefult.setTradeStatus(EPayConstant.TradeStatus.WAIT_STATUS);
						break;
					case "PAYERROR":
						ePayInfoRefult.setTradeStatus(EPayConstant.TradeStatus.EXCEPTION_STATUS);
						break;

					}
					messageBox.setMsg("微信支付查询成功!");
					messageBox.setSuccess(true);
					messageBox.setResult(ePayInfoRefult);
				}

				@Override
				public void onFailByReturnCodeError(
						ScanPayQueryResData scanPayQueryResData) {
					WechatUtil.convertToScanPayQueryRes(scanPayQueryResData, ePayInfo, ePayInfoRefult);
					ePayInfoRefult.setRetryFlag("N");
					ePayInfoRefult.setTradeStatus(EPayConstant.TradeStatus.EXCEPTION_STATUS);	
					messageBox.setMsg("微信查询失败！API返回ReturnCode不合法，支付请求逻辑错误");
					messageBox.setSuccess(false);
					messageBox.setResult(ePayInfoRefult);
				}
				
			});
		}catch(Exception e){
			ePayInfoRefult.setRetryFlag("N");
			ePayInfoRefult
					.setTradeStatus(EPayConstant.TradeStatus.EXCEPTION_STATUS);
			messageBox.setSuccess(false);
			messageBox.setMsg("系统内部错误！");
			messageBox.setResult(ePayInfoRefult);
			e.printStackTrace();
		} finally {
			return messageBox;
		}
	}

	@SuppressWarnings("finally")
	@Override
	public MessageBox cancelOrder() {
		logger.info("取消订单！类型："+ePayInfo.getType());
		final MessageBox messageBox= new MessageBox();
		final EPayInfoRefult ePayInfoRefult = new EPayInfoRefult();
		try {
			WechatSDK.doReverseBusiness(new ReverseReqData("", ePayInfo.getOutTradeNo()), new ReverseBusiness.ResultListener() {
				
				@Override
				public void onSuccess(ReverseResData reverseResData) {
					WechatUtil.convertToReverseRes(reverseResData, ePayInfo, ePayInfoRefult);
					ePayInfoRefult.setRetryFlag("N");
					ePayInfoRefult
							.setTradeStatus(EPayConstant.TradeStatus.SUCCESS_STATUS);
					messageBox.setSuccess(true);
					messageBox.setMsg("微信支付撤销成功！");
					messageBox.setResult(ePayInfoRefult);
				}
				
				@Override
				public void onFailByReturnCodeError(ReverseResData reverseResData) {	
					WechatUtil.convertToReverseRes(reverseResData, ePayInfo, ePayInfoRefult);
					ePayInfoRefult.setRetryFlag("N");
					ePayInfoRefult
							.setTradeStatus(EPayConstant.TradeStatus.EXCEPTION_STATUS);
					messageBox.setSuccess(false);
					messageBox.setMsg("微信支付撤销失败！API返回ReturnCode不合法，支付请求逻辑错误");
					messageBox.setResult(ePayInfoRefult);
				}
				
				@Override
				public void onFail(ReverseResData reverseResData) {
					WechatUtil.convertToReverseRes(reverseResData, ePayInfo, ePayInfoRefult);
					ePayInfoRefult.setRetryFlag("N");
					ePayInfoRefult
							.setTradeStatus(EPayConstant.TradeStatus.EXCEPTION_STATUS);
					messageBox.setSuccess(false);
					messageBox.setMsg("微信支付撤销失败！");
					messageBox.setResult(ePayInfoRefult);					
				}
			});
		 
		} catch (Exception e) {
			ePayInfoRefult.setRetryFlag("N");
			ePayInfoRefult
					.setTradeStatus(EPayConstant.TradeStatus.EXCEPTION_STATUS);
			messageBox.setSuccess(false);
			messageBox.setMsg("系统内部错误！");
			messageBox.setResult(ePayInfoRefult);
			e.printStackTrace();
		} finally {
			return messageBox;
		}
	}

	@SuppressWarnings("finally")
	@Override
	public MessageBox refundOrder() {
		logger.info("申请退款");
		logger.info(ePayInfo.getType() + ":支付.类型：" + ePayInfo.getTradeType());
		final MessageBox messageBox= new MessageBox();
		final EPayInfoRefult ePayInfoRefult = new EPayInfoRefult();
		try {
		
			WechatSDK.doRefundBusiness(WechatReqFactory.productRefundReqData(ePayInfo), new RefundBusiness.ResultListener() {
				@Override
				public void onRefundSuccess(RefundResData refundResData) {
					WechatUtil.convertToRefundRes(refundResData, ePayInfo, ePayInfoRefult);
					ePayInfoRefult.setRetryFlag("N");
					ePayInfoRefult
							.setTradeStatus(EPayConstant.TradeStatus.SUCCESS_STATUS);
					messageBox.setSuccess(true);
					messageBox.setMsg("微信退款成功！");
					messageBox.setResult(ePayInfoRefult);
				}
				
				@Override
				public void onRefundFail(RefundResData refundResData) {
					WechatUtil.convertToRefundRes(refundResData, ePayInfo, ePayInfoRefult);
					ePayInfoRefult.setRetryFlag("N");
					ePayInfoRefult
							.setTradeStatus(EPayConstant.TradeStatus.EXCEPTION_STATUS);
					messageBox.setSuccess(false);
					messageBox.setMsg("微信退款失败！");
					messageBox.setResult(ePayInfoRefult);
				}
				
				@Override
				public void onFailBySignInvalid(RefundResData refundResData) {
					WechatUtil.convertToRefundRes(refundResData, ePayInfo, ePayInfoRefult);
					ePayInfoRefult.setRetryFlag("N");
					ePayInfoRefult
							.setTradeStatus(EPayConstant.TradeStatus.EXCEPTION_STATUS);
					messageBox.setSuccess(false);
					messageBox.setMsg("微信退款失败！支付请求API返回的数据签名验证失败，有可能数据被篡改了！");
					messageBox.setResult(ePayInfoRefult);
				}
				
				@Override
				public void onFailByReturnCodeFail(RefundResData refundResData) {
					WechatUtil.convertToRefundRes(refundResData, ePayInfo, ePayInfoRefult);
					ePayInfoRefult.setRetryFlag("N");
					ePayInfoRefult
							.setTradeStatus(EPayConstant.TradeStatus.EXCEPTION_STATUS);
					messageBox.setSuccess(false);
					messageBox.setMsg("微信退款失败！API返回ReturnCode为FAIL");
					messageBox.setResult(ePayInfoRefult);
				}
				
				@Override
				public void onFailByReturnCodeError(RefundResData refundResData) {
					WechatUtil.convertToRefundRes(refundResData, ePayInfo, ePayInfoRefult);
					ePayInfoRefult.setRetryFlag("N");
					ePayInfoRefult
							.setTradeStatus(EPayConstant.TradeStatus.EXCEPTION_STATUS);
					messageBox.setSuccess(false);
					messageBox.setMsg("微信退款失败！API返回ReturnCode不合法，支付请求逻辑错误");
					messageBox.setResult(ePayInfoRefult);
				}
			});
		} catch (Exception e) {
			messageBox.setSuccess(false);
			messageBox.setMsg("系统内部错误！");
			messageBox.setResult(ePayInfoRefult);
			e.printStackTrace();
		}finally{
			return messageBox;
		}
	}

}
