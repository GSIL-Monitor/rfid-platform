package com.casesoft.dmc.extend.api.web.epay.fepay.alipay;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.vo.MessageBox;


import com.casesoft.dmc.extend.api.web.epay.alipay.constants.AliPayCode;
import com.casesoft.dmc.extend.api.web.epay.fepay.base.EPayConstant;
import com.casesoft.dmc.extend.api.web.epay.fepay.base.EPayInfo;
import com.casesoft.dmc.extend.api.web.epay.fepay.base.EPayInfoRefult;
import com.casesoft.dmc.extend.api.web.epay.fepay.base.IEPayTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AliPayAdapter implements IEPayTarget {
    private Logger logger = LoggerFactory.getLogger(getClass());

	private final static String timeOutEx="1m";
	private AlipayClient alipayClient;
	private EPayInfo ePayInfo;
	public  AliPayAdapter(EPayInfo ePayInfo){
		this.ePayInfo=ePayInfo;
	}
	@SuppressWarnings("finally")
	@Override
	public MessageBox barPay() {
		logger.info("条码支付");
		logger.info(ePayInfo.getType()+":支付.类型："+ePayInfo.getTradeType());
		MessageBox messageBox=null;
		EPayInfoRefult ePayInfoRefult=new EPayInfoRefult();
		try{
			alipayClient= AlipayFactory.getAlipayClient();
			JSONObject json=new JSONObject();
			json.put("out_trade_no", ePayInfo.getOutTradeNo());
			json.put("scene", "bar_code");
			json.put("total_amount", ePayInfo.getTotalAmount());
			json.put("subject", ePayInfo.getSubject());
			json.put("auth_code", ePayInfo.getAuthCode());
			json.put("timeout_express", timeOutEx);
			json.put("store_id", CacheManager.getDeviceByCode(ePayInfo.getDeviceId()).getOwnerId());
			json.put("operator_id", ePayInfo.getOperatorCode());
			json.put("terminal_id", ePayInfo.getDeviceId());
		     AlipayTradePayRequest request = new AlipayTradePayRequest();
			 request.setBizContent(json.toJSONString());
			 AlipayTradePayResponse response = null;
					// 使用SDK，调用交易下单接口
			//request.setApiVersion("3.0");

			response = alipayClient.execute(request);
			if (CommonUtil.isNotBlank(response) ) {
				ePayInfo.setBuyerLogonId(response.getBuyerLogonId());
				ePayInfo.setOpenId(response.getBuyerUserId());
				ePayInfo.setResultMsg(response.getMsg());
				ePayInfo.setTradeCode(response.getTradeNo());
				ePayInfo.setResultCode(response.getCode());
				/*
				* 定义统一返回结果
				* */
				ePayInfoRefult.setOutTradeNo(ePayInfo.getOutTradeNo());
				ePayInfoRefult.setResultCode(ePayInfo.getResultCode());
				ePayInfoRefult.setResultMsg(ePayInfo.getResultMsg());
				ePayInfoRefult.setTradeCode(ePayInfo.getTradeCode());
				ePayInfoRefult.setBuyerId(response.getBuyerLogonId());
				//ePayInfoRefult.set
				ePayInfoRefult.setTotalAmount(response.getReceiptAmount());

				switch(response.getCode()){
					case AliPayCode.ResultCode.SUCCESS:
						ePayInfo.setReceiptAmount(Double.parseDouble(response.getReceiptAmount()));
						ePayInfo.setBuyerPayAmount(Double.parseDouble(response.getBuyerPayAmount()));
						ePayInfoRefult.setTradeStatus(EPayConstant.TradeStatus.SUCCESS_STATUS);
						ePayInfoRefult.setRetryFlag("N");
					messageBox= new MessageBox(true, ePayInfo.getResultMsg(),ePayInfoRefult);
					break;
				case AliPayCode.ResultCode.EXCEPTION:
				case AliPayCode.ResultCode.FAILURE:
					ePayInfo.setSubErrorCode(response.getSubCode());
					ePayInfo.setSubErrorMsg(response.getSubMsg());
					ePayInfoRefult.setRetryFlag("N");
					ePayInfoRefult.setSubErrorCode(response.getSubCode());
					ePayInfoRefult.setSubErrorMsg(response.getSubMsg());
					ePayInfoRefult.setTradeStatus(EPayConstant.TradeStatus.EXCEPTION_STATUS);
					messageBox= new MessageBox(false, "支付宝业务处理失败",ePayInfoRefult);
					break;
				case AliPayCode.ResultCode.PROCCESS:
					ePayInfoRefult.setRetryFlag("N");
					ePayInfoRefult.setTradeStatus(EPayConstant.TradeStatus.WAIT_STATUS);
					messageBox= new  MessageBox(false, "支付宝处理中",ePayInfoRefult);
					break;
				}
			}else{
				ePayInfoRefult.setTradeStatus(EPayConstant.TradeStatus.EXCEPTION_STATUS);
				messageBox= new MessageBox(false, "支付出系统无相应！",ePayInfoRefult);
			}

		}catch (AlipayApiException e) {
			ePayInfoRefult.setTradeStatus(EPayConstant.TradeStatus.EXCEPTION_STATUS);
			messageBox= new MessageBox(false, "支付出现错误！",ePayInfoRefult);
			logger.error("支付出现错误！");
			e.printStackTrace();

		}catch(Exception e){
			ePayInfoRefult.setTradeCode(EPayConstant.TradeStatus.EXCEPTION_STATUS);
			messageBox= new MessageBox(false, "系统内部错误！",ePayInfoRefult);
			logger.error("系统内部错误！");
			e.printStackTrace();
		}finally{
			return messageBox;
		}
	}

	@Override
	public MessageBox qrPay() {

		return null;
	}

	@Override
	public MessageBox query() {
		logger.info("查询");
		logger.info(ePayInfo.getType()+":支付.类型："+ePayInfo.getTradeType());
		AlipayClient alipayClient = AlipayFactory.getAlipayClient();
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		JSONObject json=new JSONObject();
		json.put("trade_no", ePayInfo.getTradeCode());
		json.put("out_trade_no", ePayInfo.getOutTradeNo());
		request.setBizContent(json.toJSONString());
		AlipayTradeQueryResponse response = null;
		MessageBox messageBox=null;
		EPayInfoRefult ePayInfoRefult=new EPayInfoRefult();

		try {
			response = alipayClient.execute(request);

			if (CommonUtil.isNotBlank(response)) {
				ePayInfoRefult.setResultCode(response.getCode());
				ePayInfoRefult.setResultMsg(response.getMsg());
				ePayInfoRefult.setOutTradeNo(response.getOutTradeNo());
				ePayInfoRefult.setTradeCode(response.getTradeNo());
				ePayInfoRefult.setBuyerId(response.getBuyerLogonId());
				switch(response.getCode()){
					case AliPayCode.ResultCode.SUCCESS:
						ePayInfoRefult.setTotalAmount(response.getBuyerPayAmount());
						if ("TRADE_SUCCESS".equalsIgnoreCase(response
								.getTradeStatus())) {
							ePayInfoRefult.setTradeStatus(EPayConstant.TradeStatus.SUCCESS_STATUS);
						} else if ("WAIT_BUYER_PAY".equalsIgnoreCase(response
								.getTradeStatus())) {
							ePayInfoRefult.setTradeStatus(EPayConstant.TradeStatus.WAIT_STATUS);
						} else if ("TRADE_CLOSED".equalsIgnoreCase(response
								.getTradeStatus())) {
							// 表示未付款关闭，或已付款的订单全额退款后关闭
							ePayInfoRefult.setTradeStatus(EPayConstant.TradeStatus.CLOSED_STATUS);
						} else if ("TRADE_FINISHED".equalsIgnoreCase(response
								.getTradeStatus())) {
							// 此状态，订单不可退款或撤销
							ePayInfoRefult.setTradeStatus(EPayConstant.TradeStatus.FINISHED_STATUS);
						}
						messageBox= new MessageBox(true,"查询成功",ePayInfoRefult);
					break;
				case AliPayCode.ResultCode.EXCEPTION:
				case AliPayCode.ResultCode.FAILURE:
					ePayInfoRefult.setSubErrorCode(response.getSubCode());
					ePayInfoRefult.setSubErrorMsg(response.getSubMsg());
					ePayInfoRefult.setTradeCode(EPayConstant.TradeStatus.EXCEPTION_STATUS);
					messageBox= new MessageBox(false, "支付宝业务处理失败",ePayInfoRefult);

					break;
				case AliPayCode.ResultCode.PROCCESS:
					ePayInfoRefult.setTradeCode(EPayConstant.TradeStatus.WAIT_STATUS);
					messageBox= new  MessageBox(false, "支付宝业务处理中",ePayInfoRefult);
					break;
				}
			}else {
				ePayInfoRefult.setTradeCode(EPayConstant.TradeStatus.EXCEPTION_STATUS);
				messageBox= new MessageBox(false, "支付出系统无相应！",ePayInfoRefult);			}
		} catch (AlipayApiException e) {
			ePayInfoRefult.setTradeCode(EPayConstant.TradeStatus.EXCEPTION_STATUS);
			messageBox= new MessageBox(false, "系统内部错误！",ePayInfoRefult);
			logger.error("系统内部错误！");
			e.printStackTrace();
		}finally {
			return messageBox;
		}
	}

	@Override
	public MessageBox cancelOrder() {
		AlipayClient alipayClient = AlipayFactory.getAlipayClient();
		AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("out_trade_no",ePayInfo.getOutTradeNo());
		request.setBizContent(jsonObject.toJSONString());
		AlipayTradeCancelResponse response = null;
		EPayInfoRefult ePayInfoRefult=new EPayInfoRefult();
		MessageBox messageBox=null;
		try {
			response=alipayClient.execute(request);
			if (CommonUtil.isNotBlank(response)) {
				ePayInfoRefult.setResultCode(response.getCode());
				ePayInfoRefult.setResultMsg(response.getMsg());
				ePayInfoRefult.setOutTradeNo(response.getOutTradeNo());
				ePayInfoRefult.setTradeCode(response.getTradeNo());
				ePayInfoRefult.setRetryFlag(response.getRetryFlag());
				switch(response.getCode()){
					case AliPayCode.ResultCode.SUCCESS:
						ePayInfoRefult.setTradeStatus(EPayConstant.TradeStatus.SUCCESS_STATUS);
						if("close".equalsIgnoreCase(response.getAction())){
							messageBox= new MessageBox(true, "撤销成功！关闭交易，无退款",ePayInfoRefult);
						}else{
							messageBox= new MessageBox(true, "撤销成功！有退款",ePayInfoRefult);
						}
						break;
					case AliPayCode.ResultCode.EXCEPTION:
					case AliPayCode.ResultCode.FAILURE:
						ePayInfoRefult.setSubErrorCode(response.getSubCode());
						ePayInfoRefult.setSubErrorMsg(response.getSubMsg());
						ePayInfoRefult.setTradeStatus(EPayConstant.TradeStatus.EXCEPTION_STATUS);
						messageBox= new MessageBox(false, "支付宝处理失败",ePayInfoRefult);

						break;
					case AliPayCode.ResultCode.PROCCESS:
						ePayInfoRefult.setTradeCode(EPayConstant.TradeStatus.WAIT_STATUS);
						messageBox= new  MessageBox(false, "支付宝业务处理中",ePayInfoRefult);
						break;
				}
			}else{
				ePayInfoRefult.setTradeStatus(EPayConstant.TradeStatus.EXCEPTION_STATUS);
				messageBox= new MessageBox(false, "支付出系统无相应！",ePayInfoRefult);
			}
		} catch (AlipayApiException e) {
			ePayInfoRefult.setTradeStatus(EPayConstant.TradeStatus.EXCEPTION_STATUS);
			messageBox= new MessageBox(false, "系统内部错误！",ePayInfoRefult);
			logger.error("系统内部错误！");
			e.printStackTrace();
		}finally {
			return messageBox;
		}
	}
	@Override
	public MessageBox refundOrder() {
		logger.info("申请退款");
		logger.info(ePayInfo.getType()+":支付.类型："+ePayInfo.getTradeType());
		MessageBox messageBox=null;
		EPayInfoRefult ePayInfoRefult=new EPayInfoRefult();
		try{
			alipayClient=AlipayFactory.getAlipayClient();
			JSONObject json=new JSONObject();
			json.put("trade_no", ePayInfo.getTradeCode());
			json.put("refund_amount", ePayInfo.getRefundAmount());
			json.put("out_request_no", ePayInfo.getOutTradeNo());
			json.put("subject", ePayInfo.getSubject());
			json.put("refund_reason", ePayInfo.getRemark());
			json.put("store_id", CacheManager.getDeviceByCode(ePayInfo.getDeviceId()).getOwnerId());
			json.put("terminal_id", ePayInfo.getDeviceId());
			AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
			request.setBizContent(json.toJSONString());
			AlipayTradeRefundResponse response = null;


			// 使用SDK，调用交易下单接口
			response = alipayClient.execute(request);
			if (CommonUtil.isNotBlank(response)) {
				ePayInfo.setBuyerLogonId(response.getBuyerLogonId());
				ePayInfo.setOpenId(response.getBuyerUserId());
				ePayInfo.setResultMsg(response.getMsg());
				ePayInfo.setTradeCode(response.getTradeNo());
				ePayInfo.setResultCode(response.getCode());
				/*
				* 定义统一返回结果
				* */
				ePayInfoRefult.setOutTradeNo(ePayInfo.getOutTradeNo());
				ePayInfoRefult.setResultCode(ePayInfo.getResultCode());
				ePayInfoRefult.setResultMsg(ePayInfo.getResultMsg());
				ePayInfoRefult.setTradeCode(ePayInfo.getTradeCode());
				ePayInfoRefult.setBuyerId(response.getBuyerUserId());

				switch(response.getCode()){
					case AliPayCode.ResultCode.SUCCESS:
						ePayInfoRefult.setTradeStatus(EPayConstant.TradeStatus.SUCCESS_STATUS);
						ePayInfoRefult.setRetryFlag("N");
						messageBox= new MessageBox(true, ePayInfo.getResultMsg(),ePayInfoRefult);
						break;
					case AliPayCode.ResultCode.EXCEPTION:
					case AliPayCode.ResultCode.FAILURE:
						ePayInfo.setSubErrorCode(response.getSubCode());
						ePayInfo.setSubErrorMsg(response.getSubMsg());
						ePayInfoRefult.setRetryFlag("N");
						ePayInfoRefult.setSubErrorCode(response.getSubCode());
						ePayInfoRefult.setSubErrorMsg(response.getSubMsg());
						ePayInfoRefult.setTradeStatus(EPayConstant.TradeStatus.EXCEPTION_STATUS);
						messageBox= new MessageBox(false, "支付宝业务处理失败",ePayInfoRefult);
						break;
					case AliPayCode.ResultCode.PROCCESS:
						ePayInfoRefult.setRetryFlag("N");
						ePayInfoRefult.setTradeStatus(EPayConstant.TradeStatus.WAIT_STATUS);
						messageBox= new  MessageBox(false, "支付宝处理中",ePayInfoRefult);
						break;
				}
			}else{
				ePayInfoRefult.setTradeStatus(EPayConstant.TradeStatus.EXCEPTION_STATUS);
				messageBox= new MessageBox(false, "支付出系统无相应！",ePayInfoRefult);
			}

		}catch (AlipayApiException e) {
			ePayInfoRefult.setTradeStatus(EPayConstant.TradeStatus.EXCEPTION_STATUS);
			messageBox= new MessageBox(false, "支付出现错误！",ePayInfoRefult);
			logger.error("支付出现错误！");
			e.printStackTrace();

		}catch(Exception e){
			ePayInfoRefult.setTradeStatus(EPayConstant.TradeStatus.EXCEPTION_STATUS);
			messageBox= new MessageBox(false, "系统内部错误！",ePayInfoRefult);
			logger.error("系统内部错误！");
			e.printStackTrace();
		}finally{
			return messageBox;
		}
	}


}
