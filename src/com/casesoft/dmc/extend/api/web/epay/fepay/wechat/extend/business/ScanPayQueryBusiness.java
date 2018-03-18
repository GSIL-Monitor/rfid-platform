package com.casesoft.dmc.extend.api.web.epay.fepay.wechat.extend.business;

import com.tencent.common.Log;
import com.tencent.common.Util;
import com.tencent.protocol.pay_query_protocol.ScanPayQueryReqData;
import com.tencent.protocol.pay_query_protocol.ScanPayQueryResData;
import com.tencent.service.ScanPayQueryService;
import org.slf4j.LoggerFactory;

public class ScanPayQueryBusiness {
	private static Log log = new Log(
			LoggerFactory.getLogger(ScanPayQueryBusiness.class));
	ScanPayQueryService scanPayQueryService = new ScanPayQueryService();

	public ScanPayQueryBusiness() throws IllegalAccessException,
			InstantiationException, ClassNotFoundException {
		scanPayQueryService = new ScanPayQueryService();
	}

	public interface ResultListener {
		// 查询失败
		void onFail(ScanPayQueryResData scanPayQueryResData);

		// 查询成功
		void onSuccess(ScanPayQueryResData scanPayQueryResData);

		// API返回ReturnCode不合法，支付请求逻辑错误，请仔细检测传过去的每一个参数是否合法，或是看API能否被正常访问
		void onFailByReturnCodeError(ScanPayQueryResData scanPayQueryResData);
	}

	/**
	 * 进行一次支付订单查询操作
	 *
	 * @param outTradeNo
	 *            商户系统内部的订单号,32个字符内可包含字母, [确保在商户系统唯一]
	 * @return 该订单是否支付成功
	 * @throws Exception
	 */
	public void run(ScanPayQueryReqData scanPayQueryReqData,
			ResultListener resultListener) throws Exception {

		String payQueryServiceResponseString;
		payQueryServiceResponseString = scanPayQueryService
				.request(scanPayQueryReqData);

		log.i("支付订单查询API返回的数据如下：");
		log.i(payQueryServiceResponseString);

		// 将从API返回的XML数据映射到Java对象
		ScanPayQueryResData scanPayQueryResData = (ScanPayQueryResData) Util
				.getObjectFromXML(payQueryServiceResponseString,
						ScanPayQueryResData.class);
		if (scanPayQueryResData == null
				|| scanPayQueryResData.getReturn_code() == null) {
			log.i("支付订单查询请求逻辑错误，请仔细检测传过去的每一个参数是否合法");
			resultListener.onFailByReturnCodeError(scanPayQueryResData);
		} else if (scanPayQueryResData.getReturn_code().equals("FAIL")) {
			// 注意：一般这里返回FAIL是出现系统级参数错误，请检测Post给API的数据是否规范合法
			log.i("支付订单查询API系统返回失败，失败信息为："
					+ scanPayQueryResData.getReturn_msg());
			resultListener.onFailByReturnCodeError(scanPayQueryResData);
		} else {
			if (scanPayQueryResData.getResult_code().equals("SUCCESS")) {// 业务层成功
				resultListener.onSuccess(scanPayQueryResData);
			} else {
				resultListener.onFail(scanPayQueryResData);
				log.i("查询出错，错误码：" + scanPayQueryResData.getErr_code()
						+ "     错误信息：" + scanPayQueryResData.getErr_code_des());
			}
		}
	}

}
