package com.casesoft.dmc.extend.api.wechat;

/**
 * Created by Alvin on 2018/3/1.
 * **
 * 微信公众号相关的配置
 * **
 */
public class WechatConfig {
    //public static final String APP_ID = "wx22237cc4fab99714";
    public static final String APP_ID = "wx75333d2856f90801";
    //public static final String APP_SECRET = "3e6d9d061593a19d4207342c900bcf89";
    public static final String APP_SECRET = "a5f8a3c364c18a8f0f50f62c9d94823f";
    public static final String WECHAT_TOKEN = "xx";// 服务号的Token令牌
    // 授权链接
    public static final String OAUTH_AUTHORIZE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize";
    // 获取token的链接
    public static final String OAUTH_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
    // 刷新token
    public static final String OAUTH_REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token";
    // 获取授权用户信息
    public static final String SNS_USERINFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info";
    // 判断用户accessToken是否有效
    public static final String SNS_AUTH_URL = "https://api.weixin.qq.com/sns/auth";

    public static final String AUTHDENY="authdeny";
}
