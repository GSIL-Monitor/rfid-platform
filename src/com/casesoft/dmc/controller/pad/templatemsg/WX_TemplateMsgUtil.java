package com.casesoft.dmc.controller.pad.templatemsg;

import com.alibaba.fastjson.JSONObject;
import com.casesoft.dmc.controller.pad.WeiXinUtils;
import com.casesoft.dmc.model.pad.Template.TemplateData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by ltc on 2018/6/9.
 */
public class WX_TemplateMsgUtil {
    private static Logger log = LoggerFactory.getLogger(WX_TemplateMsgUtil.class);

    /**
     * 封装模板详细信息
     *
     * @return JSONObject
     */
    public static JSONObject packJsonmsg(Map<String, TemplateData> param) {
        JSONObject json = new JSONObject();
        for (Map.Entry<String, TemplateData> entry : param.entrySet()) {
            JSONObject keyJson = new JSONObject();
            TemplateData dta = entry.getValue();
            keyJson.put("value", dta.getValue());
            keyJson.put("color", dta.getColor());
            json.put(entry.getKey(), keyJson);
        }
        return json;
    }

    /**
     * 发送微信消息(模板消息)
     *
     * @param touser    用户 OpenID
     * @param templatId 模板消息ID
     * @param clickUrl  URL置空，则在发送后，点击模板消息会进入一个空白页面（ios），或无法点击（android）。
     * @param topColor  标题颜色
     * @param data      详细内容
     * @return String
     */
    static String sendWechatMsgToUser(String touser, String templatId, String clickUrl, String topColor, JSONObject data) {
        String tmpurl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + WeiXinUtils.getAccessToken().getToken();
        JSONObject json = new JSONObject();
        json.put("touser", touser);
        json.put("template_id", templatId);
        json.put("url", clickUrl);
        json.put("topcolor", topColor);
        json.put("data", data);
        try {
            JSONObject result = WX_HttpsUtil.httpsRequest(tmpurl, "POST", json.toString());
            JSONObject resultJson = new JSONObject(result);
            log.info("发送微信消息返回信息：" + resultJson.get("errcode"));
            String errmsg = (String) resultJson.get("errmsg");
            if (!"ok".equals(errmsg)) {  //如果为errmsg为ok，则代表发送成功，公众号推送信息给用户了。
                return "error";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }

    /**
     * 小程序推送模版消息
     * <p>
     * 平台：ancientstone智慧门店
     * 维度：$商品款号
     * 时间：当前时间
     * 描述：接口返回的描述信息
     * 提示：请尽快处理，避免影响业务
     */
    static String miniProgramPushMsg(String touser, String template_id, String page, String form_id, JSONObject data) {
        String tmpurl = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=" + WeiXinUtils.getMiniProgramAccessToken().getToken();
        JSONObject json = new JSONObject();
        json.put("touser", touser);
        json.put("template_id", template_id);
        json.put("page", page);
        json.put("form_id", form_id);
        json.put("data", data);
        try {
            JSONObject result = WX_HttpsUtil.httpsRequest(tmpurl, "POST", json.toString());
            JSONObject resultJson = new JSONObject(result);
            log.info("发送微信消息返回信息：" + resultJson.get("errcode"));
            String errmsg = (String) resultJson.get("errmsg");
            if (!"ok".equals(errmsg)) {
                return "错误码：" + resultJson.get("errcode") + "，错误信息：" + errmsg;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }
}
