package com.casesoft.dmc.controller.pad;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.model.pad.AccessToken;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Date;

import static com.casesoft.dmc.controller.pad.ProjectConst.ACCESS_TOKEN_URL;

/**
 * @author ltc
 * @create 2018-06-07
 * @desc 用户获取access_token, 众号调用各接口时都需使用access_token
 **/
public class WeiXinUtils {
    public static String APPID;
    public static String APPSECRET;
    public static String MINI_PRAOGRAM_APPID;
    public static String MINI_PRAOGRAM_APPSECRET;

    /**
     * Get请求，方便到一个url接口来获取结果
     *
     * @param url 请求accessToken的url
     */
    public static JSONObject doGetStr(String url) {
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        JSONObject jsonObject = null;
        try {
            HttpResponse response = defaultHttpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity, "UTF-8");
                jsonObject = JSONObject.fromObject(result);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 获取accessToken
     *
     * @return accessToken
     */
    public static AccessToken getAccessToken() {
        try {
            APPID = PropertyUtil.getValue("APPID");
            APPSECRET = PropertyUtil.getValue("APPSECRET");
        } catch (Exception e) {
            e.printStackTrace();
        }
        AccessToken accessToken = new AccessToken();
        Long nowDate = new Date().getTime();
        accessToken = CacheManager.getAccessToken();
        if (accessToken != null && accessToken.getTime() != null && (nowDate - accessToken.getTime() < 1800 * 1000)) {
            System.out.println("accessToken存在不需要" + accessToken.getToken());
            return accessToken;
        } else {
            CacheManager.remove();
            String url = ACCESS_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
            JSONObject jsonObject = doGetStr(url);
            Object errcode = jsonObject.get("errcode");
            //如果不存在错误码,则代表此次交易正常
            if (errcode == null || "0".equals(errcode)) {
                accessToken.setToken(jsonObject.getString("access_token"));
                accessToken.setExpireIn(jsonObject.getInt("expires_in"));
                accessToken.setTime(new Date().getTime());
                CacheManager.iniAccessToken(accessToken);
                System.out.println("获取成功");
                System.out.println(accessToken.getToken());
                return accessToken;
            } else {
                System.out.println("当前获取accesstoken失败，错误码为：" + errcode + "错误原因为：" + jsonObject.getString("errmsg"));
                return null;
            }
        }
    }

    /**
     * add by Anna on 2018-08-24
     * 获取小程序的accessToken
     * @return miniProgramAccessToken
     */
    public static AccessToken getMiniProgramAccessToken() {
        try {
            MINI_PRAOGRAM_APPID = PropertyUtil.getValue("MINI_PRAOGRAM_APPID");
            MINI_PRAOGRAM_APPSECRET = PropertyUtil.getValue("MINI_PRAOGRAM_APPSECRET");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Long nowDate = new Date().getTime();
        AccessToken miniProgramAccessToken = CacheManager.getMiniProgramAccessToken();
        if (miniProgramAccessToken != null && miniProgramAccessToken.getTime() != null && (nowDate - miniProgramAccessToken.getTime() < 1800 * 1000)) {
            System.out.println("access_token［" + miniProgramAccessToken.getToken() + "］存在，且未过期");
            return miniProgramAccessToken;
        } else {
            String url = ACCESS_TOKEN_URL.replace("APPID", MINI_PRAOGRAM_APPID).replace("APPSECRET", MINI_PRAOGRAM_APPSECRET);
            JSONObject jsonObject = doGetStr(url);
            Object errcode = jsonObject.get("errcode");
            //如果不存在错误码,则代表此次交易正常
            if (errcode == null || "0".equals(errcode)) {
                miniProgramAccessToken = miniProgramAccessToken == null ? new AccessToken() : miniProgramAccessToken;
                miniProgramAccessToken.setToken(jsonObject.getString("access_token"));
                miniProgramAccessToken.setExpireIn(jsonObject.getInt("expires_in"));
                miniProgramAccessToken.setTime(new Date().getTime());
                CacheManager.initMiniProgramAccessToken(miniProgramAccessToken);
                System.out.println("小程序Token获取成功");
                System.out.println(miniProgramAccessToken.getToken());
                return miniProgramAccessToken;
            } else {
                System.out.println("当前获取accesstoken失败，错误码为：" + errcode + "错误原因为：" + jsonObject.getString("errmsg"));
                return null;
            }
        }
    }

}
