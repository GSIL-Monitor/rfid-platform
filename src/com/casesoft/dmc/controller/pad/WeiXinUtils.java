package com.casesoft.dmc.controller.pad;

import com.casesoft.dmc.model.pad.AccessToken;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import static com.casesoft.dmc.controller.pad.ProjectConst.ACCESS_TOKEN_URL;

/**
 * @author ltc
 * @create 2018-06-07
 * @desc 用户获取access_token,众号调用各接口时都需使用access_token
 **/
public class WeiXinUtils {
    public static String APPID = "wx22237cc4fab99714";
    public static String APPSECRET = "a17e22c9496208a36d12dce6acd0fab5";
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
     * @return accessToken
     */
    public static AccessToken getAccessToken(){
        AccessToken accessToken = new AccessToken();
        String url =ACCESS_TOKEN_URL.replace("APPID" ,APPID).replace("APPSECRET",APPSECRET);
        JSONObject jsonObject = doGetStr(url);
        if(jsonObject !=null){
            accessToken.setToken(jsonObject.getString("access_token"));
            accessToken.setExpireIn(jsonObject.getInt("expires_in"));
        }
        return accessToken;
    }
}
