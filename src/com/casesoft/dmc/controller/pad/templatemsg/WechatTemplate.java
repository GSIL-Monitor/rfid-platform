package com.casesoft.dmc.controller.pad.templatemsg;

import com.casesoft.dmc.model.pad.Template.TemplateData;
import com.casesoft.dmc.service.pad.TemplateMsgService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.casesoft.dmc.controller.pad.templatemsg.WX_TemplateMsgUtil.packJsonmsg;

/**
 * 模板消息
 * Created by liu on 2018/6/6.
 */
public class WechatTemplate {

    private TemplateMsgService templateMsgService;

    /**
     * 销售模板消息
     *
     * @param openId   用户id
     * @param actPrice 实际金额
     * @param totQty   实际数量
     * @param billNo   单据编号
     * @param name     店铺名
     */
    public static String senMsg(String openId, String actPrice, String totQty, String billNo, String name) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
        Map<String, TemplateData> param = new HashMap<>();
        param.put("first", new TemplateData("尊敬的顾客，最新交易提醒：", "#696969"));
        param.put("keyword1", new TemplateData(actPrice, "#696969"));
        param.put("keyword2", new TemplateData(totQty, "#696969"));
        param.put("keyword3", new TemplateData(simpleDateFormat.format(date), "#696969"));
        param.put("keyword4", new TemplateData(billNo, "#696969"));
        param.put("keyword5", new TemplateData(name, "#696969"));
        param.put("remark", new TemplateData("如有任何疑问请与销售员联系", "#696969"));
        //注册的微信-模板Id
        String regTempId = "zVkFopoR67ytP7IAR62gORaKM5ohlf43UaEiQS58fXo";
        //调用发送微信消息给用户的接口
        return WX_TemplateMsgUtil.sendWechatMsgToUser(openId, regTempId, "", "#000000", packJsonmsg(param));
    }

    /**
     * 退货模板消息
     *
     * @param openId   用户id
     * @param rBillNo  退货单号
     * @param totQty   退货数量
     * @param actPrice 退货金额
     */
    public static String returnMsg(String openId, String rBillNo, String totQty, String actPrice) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
        Map<String, TemplateData> param = new HashMap<>();
        param.put("first", new TemplateData("尊敬的顾客，最新交易提醒：", "#696969"));
        param.put("keyword1", new TemplateData(rBillNo, "#696969"));
        param.put("keyword2", new TemplateData("AS服装", "#696969"));
        param.put("keyword3", new TemplateData(totQty, "#696969"));
        param.put("keyword4", new TemplateData(actPrice, "#696969"));
        param.put("remark", new TemplateData("如有任何疑问请与销售员联系", "#696969"));
        //注册的微信-模板Id
        String regTempId = "mSincbOtoOfo8KH1t-l0alaGGB9vdaKnsa_EPmxXmdc";
        //调用发送微信消息给用户的接口
        return WX_TemplateMsgUtil.sendWechatMsgToUser(openId, regTempId, "", "#000000", packJsonmsg(param));
    }

    /**
     * 小程序监控数据变化提醒
     * @param openId 小程序操作者openId
     * @param formId 表单绑定的form_id
     * @param styleId 当前编辑的款号
     * @param brandName 为了跳转传参的品牌名字
     * @param description 操作返回的描述
     * @return
     */
    public static String dataChangeSendMsg(String openId, String formId, String styleId, String brandName, String description) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
        Map<String, TemplateData> param = new HashMap<>();
        param.put("keyword1", new TemplateData("ancientstone智慧门店", "#000000"));
        param.put("keyword2", new TemplateData("商品款号［" + styleId + "］", "#000000"));
        param.put("keyword3", new TemplateData(simpleDateFormat.format(date), "#000000"));
        param.put("keyword4", new TemplateData(description, "#000000"));
        param.put("keyword5", new TemplateData("请尽快处理，避免影响业务", "#000000"));
        // 模板Id
        String template_id = "PLibvn3UFpGUY9r-TwcUc5ADI4qdmT4qBNR6E59GeOI";
        // 模版消息跳转链接
        String pageUrl = "pages/product/style_edit/style_edit?styleId=" + styleId + "&brandName=" + brandName;
        //调用发送微信消息给用户的接口
        return WX_TemplateMsgUtil.miniProgramPushMsg(openId, template_id, pageUrl, formId, packJsonmsg(param));
    }
}
