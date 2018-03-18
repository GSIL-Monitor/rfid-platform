package com.casesoft.dmc.extend.api.web.epay.fepay.wechat.extend.business;

import com.tencent.common.Log;
import com.tencent.common.Util;
import com.tencent.protocol.reverse_protocol.ReverseReqData;
import com.tencent.protocol.reverse_protocol.ReverseResData;
import com.tencent.service.ReverseService;
import org.slf4j.LoggerFactory;

public class ReverseBusiness {
	ReverseService reverseService;
	// 打log用
	private static Log log = new Log(
			LoggerFactory.getLogger(ReverseBusiness.class));

	public ReverseBusiness() throws IllegalAccessException,
			InstantiationException, ClassNotFoundException {
		reverseService = new ReverseService();
	}

	public interface ResultListener {
		// 取消失败
		void onFail(ReverseResData reverseResData);

		// 取消成功
		void onSuccess(ReverseResData reverseResData);
        //API返回ReturnCode不合法，支付请求逻辑错误，请仔细检测传过去的每一个参数是否合法，或是看API能否被正常访问
		void onFailByReturnCodeError(ReverseResData reverseResData);

	}

	/**
	 * 进行一次撤销操作
	 *
	 * @param outTradeNo
	 *            商户系统内部的订单号,32个字符内可包含字母, [确保在商户系统唯一]
	 * @return 该订单是否支付成功
	 * @throws Exception
	 */
	public void run(ReverseReqData reverseReqData, ResultListener resultListener)
			throws Exception {

		String reverseResponseString;
		reverseResponseString = reverseService.request(reverseReqData);
		log.i("撤销API返回的数据如下：");
		log.i(reverseResponseString);
		// 将从API返回的XML数据映射到Java对象
		ReverseResData reverseResData = (ReverseResData) Util.getObjectFromXML(
				reverseResponseString, ReverseResData.class);
		if (reverseResData == null) {
			log.i("支付订单撤销请求逻辑错误，请仔细检测传过去的每一个参数是否合法");
            resultListener.onFailByReturnCodeError(reverseResData);
		}else if (reverseResData.getReturn_code().equals("FAIL")) {
			// 注意：一般这里返回FAIL是出现系统级参数错误，请检测Post给API的数据是否规范合法
			log.i("支付订单撤销API系统返回失败，失败信息为：" + reverseResData.getReturn_msg());
			resultListener.onFail(reverseResData);
		} else {
			if (reverseResData.getResult_code().equals("FAIL")) {
				resultListener.onFail(reverseResData);
				log.i("撤销出错，错误码：" + reverseResData.getErr_code() + "     错误信息："
						+ reverseResData.getErr_code_des());
			} else {
				// 查询成功，打印交易状态
				log.i("支付订单撤销成功");
				resultListener.onSuccess(reverseResData);
			}
		}
	}
}
