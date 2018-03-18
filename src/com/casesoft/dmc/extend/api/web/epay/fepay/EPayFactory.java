package com.casesoft.dmc.extend.api.web.epay.fepay;


import com.casesoft.dmc.extend.api.web.epay.fepay.alipay.AliPayAdapter;
import com.casesoft.dmc.extend.api.web.epay.fepay.base.EPayConstant;
import com.casesoft.dmc.extend.api.web.epay.fepay.base.EPayInfo;
import com.casesoft.dmc.extend.api.web.epay.fepay.base.IEPayTarget;
import com.casesoft.dmc.extend.api.web.epay.fepay.wechat.WechatAdapter;

/**
 * Created by pc on 2016/6/24.
 */
public class EPayFactory {

    public static IEPayTarget createEPayTarget(EPayInfo ePayInfo){
        switch (ePayInfo.getType()){
            case EPayConstant.EpayType.ALYPAY_TYPE_A:
                return  new AliPayAdapter(ePayInfo);
            case EPayConstant.EpayType.WECHAT_TYPE_A:
                return new WechatAdapter(ePayInfo);
            default:return null;
        }
    }
}
