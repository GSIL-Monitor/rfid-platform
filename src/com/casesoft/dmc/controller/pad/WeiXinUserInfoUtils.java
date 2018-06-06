package com.casesoft.dmc.controller.pad;


import com.casesoft.dmc.model.pad.AccessToken;
import net.sf.json.JSONObject;

/**
 * @author scw
 * @create Created by ltcon 2018/6/7
 * @desc 用于获取微信用户的信息
 **/
public class WeiXinUserInfoUtils {
    private static final String GET_USERINFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    /**
     * 获取微信用户账号的相关信息
     *
     * @param opendID 用户的openId，这个通过当用户进行了消息交互的时候，才有
     * @return
     */
    public static JSONObject getUserInfo(String opendID) {
        AccessToken accessToken = WeiXinUtils.getAccessToken();
        //获取access_token
        String token = accessToken.getToken();
        String url = GET_USERINFO_URL.replace("ACCESS_TOKEN", token);
        url = url.replace("OPENID", opendID);
        JSONObject jsonObject = WeiXinUtils.doGetStr(url);
        return jsonObject;
    }
}
