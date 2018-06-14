package com.casesoft.dmc.controller.pad.templatemsg;

import com.casesoft.dmc.model.pad.Template.TemplateData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.casesoft.dmc.controller.pad.templatemsg.WX_TemplateMsgUtil.packJsonmsg;

/**
 * 模板消息
 * Created by liu on 2018/6/6.
 */
public class WechatTemplate{
    /**
     * 销售模板消息
     * @param openId 用户id
     * @param actPrice 实际金额
     * @param totQty 实际数量
     * @param billNo 单据编号
     * @param name 店铺名
     */
    public static void senMsg(String openId,String actPrice,String totQty,String billNo,String name){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
        Map<String,TemplateData> param = new HashMap<>();
        param.put("first",new TemplateData("尊敬的顾客，最新交易提醒：","#696969"));
        param.put("keyword1",new TemplateData(actPrice,"#696969"));
        param.put("keyword2",new TemplateData(totQty,"#696969"));
        param.put("keyword3",new TemplateData(simpleDateFormat.format(date),"#696969"));
        param.put("keyword4",new TemplateData(billNo,"#696969"));
        param.put("keyword5",new TemplateData(name,"#696969"));
        param.put("remark",new TemplateData("如有任何疑问请与销售员联系","#696969"));
        //注册的微信-模板Id
        String regTempId ="zVkFopoR67ytP7IAR62gORaKM5ohlf43UaEiQS58fXo";
        //调用发送微信消息给用户的接口
        String state = WX_TemplateMsgUtil.sendWechatMsgToUser(openId , regTempId, "", "#000000", packJsonmsg(param));
    }

    /**
     * 退货模板消息
     * @param openId 用户id
     * @param rBillNo 退货单号
     * @param totQty 退货数量
     * @param actPrice 退货金额
     */
    public static void returnMsg(String openId ,String rBillNo,String totQty,String actPrice){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
        Map<String,TemplateData> param = new HashMap<>();
        param.put("first",new TemplateData("尊敬的顾客，最新交易提醒：","#696969"));
        param.put("keyword1",new TemplateData(rBillNo,"#696969"));
        param.put("keyword2",new TemplateData("AS服装","#696969"));
        param.put("keyword3",new TemplateData(totQty,"#696969"));
        param.put("keyword4",new TemplateData(actPrice,"#696969"));
        param.put("remark",new TemplateData("如有任何疑问请与销售员联系","#696969"));
        //注册的微信-模板Id
        String regTempId ="mSincbOtoOfo8KH1t-l0alaGGB9vdaKnsa_EPmxXmdc";
        //调用发送微信消息给用户的接口
        String state = WX_TemplateMsgUtil.sendWechatMsgToUser(openId , regTempId, "", "#000000", packJsonmsg(param));
    }
}
