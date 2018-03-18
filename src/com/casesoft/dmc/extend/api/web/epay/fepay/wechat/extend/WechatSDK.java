package com.casesoft.dmc.extend.api.web.epay.fepay.wechat.extend;

import com.casesoft.dmc.extend.api.web.epay.fepay.wechat.extend.business.ReverseBusiness;
import com.casesoft.dmc.extend.api.web.epay.fepay.wechat.extend.business.ScanPayQueryBusiness;
import com.tencent.WXPay;
import com.tencent.protocol.pay_query_protocol.ScanPayQueryReqData;
import com.tencent.protocol.reverse_protocol.ReverseReqData;

public class WechatSDK extends WXPay{
	 /**
     * 运行取消订单的业务逻辑
     * @param refundQueryReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @param resultListener 商户需要自己监听被扫支付业务逻辑可能触发的各种分支事件，并做好合理的响应处理
     * @throws Exception
     */
    public static void doReverseBusiness(ReverseReqData reverseReqData,ReverseBusiness.ResultListener resultListener) throws Exception {
        new ReverseBusiness().run(reverseReqData,resultListener);
    }
    /**
     * 运行支付查询的业务逻辑
     * @param reverseReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @param resultListener 商户需要自己监听被扫支付业务逻辑可能触发的各种分支事件，并做好合理的响应处理
     * @throws Exception
     */
    public static void doScanPayQueryBusiness(ScanPayQueryReqData scanPayQueryReqData,ScanPayQueryBusiness.ResultListener resultListener) throws Exception {
        new ScanPayQueryBusiness().run(scanPayQueryReqData,resultListener);
    }
}
