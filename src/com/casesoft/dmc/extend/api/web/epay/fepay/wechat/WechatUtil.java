package com.casesoft.dmc.extend.api.web.epay.fepay.wechat;

import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.extend.api.web.epay.fepay.base.EPayInfo;
import com.casesoft.dmc.extend.api.web.epay.fepay.base.EPayInfoRefult;
import com.tencent.protocol.pay_protocol.ScanPayResData;
import com.tencent.protocol.pay_query_protocol.ScanPayQueryResData;
import com.tencent.protocol.refund_protocol.RefundResData;
import com.tencent.protocol.reverse_protocol.ReverseResData;

public class WechatUtil {
	/**
	 * (支付)转换公共接口
	 * */
	public static  void convertToScanPayRes(ScanPayResData scanPayResData,
											EPayInfo ePayInfo, EPayInfoRefult ePayInfoRefult) {
		ePayInfo.setOpenId(scanPayResData.getOpenid());
		ePayInfo.setResultMsg(scanPayResData.getReturn_msg());
		ePayInfo.setTradeCode(scanPayResData.getTransaction_id());
		ePayInfo.setResultCode(scanPayResData.getReturn_code());
		ePayInfo.setSubErrorCode(scanPayResData.getErr_code());
		ePayInfo.setSubErrorMsg(scanPayResData.getErr_code_des());
		/*
		 * 定义统一返回结果
		 */
		ePayInfoRefult.setOutTradeNo(ePayInfo.getOutTradeNo());
		ePayInfoRefult.setResultCode(ePayInfo.getResultCode());
		ePayInfoRefult.setResultMsg(ePayInfo.getResultMsg());
		ePayInfoRefult.setTradeCode(ePayInfo.getTradeCode());
		ePayInfoRefult.setBuyerId(scanPayResData.getOpenid());
		ePayInfoRefult
				.setTotalAmount(String.valueOf(ePayInfo.getTotalAmount()));
		ePayInfoRefult.setSubErrorCode(scanPayResData.getErr_code());
		ePayInfoRefult.setSubErrorMsg(scanPayResData.getErr_code_des());
	}
	/**
	 *（退款） 转换公共接口
	 * */
	public static  void convertToRefundRes(RefundResData refundResData,
			EPayInfo ePayInfo, EPayInfoRefult ePayInfoRefult) {
		//ePayInfo.setOpenId(refundResData.getOpenid());
		ePayInfo.setResultMsg(refundResData.getReturn_msg());
		ePayInfo.setTradeCode(refundResData.getTransaction_id());
		ePayInfo.setResultCode(refundResData.getReturn_code());
		ePayInfo.setSubErrorCode(refundResData.getErr_code());
		ePayInfo.setSubErrorMsg(refundResData.getErr_code_des());
		/*
		 * 定义统一返回结果
		 */
		ePayInfoRefult.setOutTradeNo(ePayInfo.getOutTradeNo());
		ePayInfoRefult.setResultCode(ePayInfo.getResultCode());
		ePayInfoRefult.setResultMsg(ePayInfo.getResultMsg());
		ePayInfoRefult.setTradeCode(ePayInfo.getTradeCode());
		//ePayInfoRefult.setBuyerId(refundResData.getOpenid());
		ePayInfoRefult
				.setTotalAmount(String.valueOf(ePayInfo.getTotalAmount()));
		ePayInfoRefult.setSubErrorCode(refundResData.getErr_code());
		ePayInfoRefult.setSubErrorMsg(refundResData.getErr_code_des());
		ePayInfoRefult.setTotalAmount(String.valueOf(Double.parseDouble(refundResData.getRefund_fee())/100));
	}
	
	/**
	 *（查询） 转换公共接口
	 * */
	public static  void convertToScanPayQueryRes(ScanPayQueryResData scanPayQueryResData,
			EPayInfo ePayInfo, EPayInfoRefult ePayInfoRefult) {
		//ePayInfo.setOpenId(refundResData.getOpenid());
		ePayInfo.setResultMsg(scanPayQueryResData.getReturn_msg());
		ePayInfo.setTradeCode(scanPayQueryResData.getTransaction_id());
		ePayInfo.setResultCode(scanPayQueryResData.getReturn_code());
		ePayInfo.setSubErrorCode(scanPayQueryResData.getErr_code());
		ePayInfo.setSubErrorMsg(scanPayQueryResData.getErr_code_des());
		/*
		 * 定义统一返回结果
		 */
		ePayInfoRefult.setOutTradeNo(ePayInfo.getOutTradeNo());
		ePayInfoRefult.setResultCode(ePayInfo.getResultCode());
		ePayInfoRefult.setResultMsg(ePayInfo.getResultMsg());
		ePayInfoRefult.setTradeCode(ePayInfo.getTradeCode());
		//ePayInfoRefult.setBuyerId(refundResData.getOpenid());
		ePayInfoRefult
				.setTotalAmount(String.valueOf(ePayInfo.getTotalAmount()));
		ePayInfoRefult.setSubErrorCode(scanPayQueryResData.getErr_code());
		ePayInfoRefult.setSubErrorMsg(scanPayQueryResData.getErr_code_des());
		if(CommonUtil.isNotBlank(scanPayQueryResData.getTotal_fee())){
			ePayInfoRefult.setTotalAmount(String.valueOf(Double.parseDouble(scanPayQueryResData.getTotal_fee())/100));
		}
	}
	/**
	 *（查询） 转换公共接口
	 * */
	public static  void convertToReverseRes(ReverseResData reverseResData,
			EPayInfo ePayInfo, EPayInfoRefult ePayInfoRefult) {
		//ePayInfo.setOpenId(refundResData.getOpenid());
		ePayInfo.setResultMsg(reverseResData.getReturn_msg());
	//	ePayInfo.setTradeCode(reverseResData.getTransaction_id());
		ePayInfo.setResultCode(reverseResData.getReturn_code());
		ePayInfo.setSubErrorCode(reverseResData.getErr_code());
		ePayInfo.setSubErrorMsg(reverseResData.getErr_code_des());
		/*
		 * 定义统一返回结果
		 */
		ePayInfoRefult.setOutTradeNo(ePayInfo.getOutTradeNo());
		ePayInfoRefult.setResultCode(ePayInfo.getResultCode());
		ePayInfoRefult.setResultMsg(ePayInfo.getResultMsg());
		ePayInfoRefult.setTradeCode(ePayInfo.getTradeCode());
		//ePayInfoRefult.setBuyerId(refundResData.getOpenid());
		ePayInfoRefult
				.setTotalAmount(String.valueOf(ePayInfo.getTotalAmount()));
		ePayInfoRefult.setSubErrorCode(reverseResData.getErr_code());
		ePayInfoRefult.setSubErrorMsg(reverseResData.getErr_code_des());
	//	ePayInfoRefult.setTotalAmount(String.valueOf(Double.parseDouble(reverseResData.getTotal_fee())/100));
	}
}
