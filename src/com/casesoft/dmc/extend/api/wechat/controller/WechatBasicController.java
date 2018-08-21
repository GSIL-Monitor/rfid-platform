package com.casesoft.dmc.extend.api.wechat.controller;

import com.casesoft.dmc.extend.api.web.ApiBaseController;
import com.casesoft.dmc.extend.api.wechat.WechatConfig;
import com.casesoft.dmc.extend.api.wechat.model.SNSUserInfo;
import com.casesoft.dmc.extend.api.wechat.model.WeixinOauth2Token;
import com.casesoft.dmc.extend.api.wechat.until.AdvancedUtil;
import com.casesoft.dmc.service.sys.impl.UserService;
import com.casesoft.dmc.service.wechat.SNSUserInfoService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by admin on 2018/3/6.
 */

@Controller
@RequestMapping(value = "/api/wx/basic")
@Api(description = "微信公众号关注接口")
public class WechatBasicController extends ApiBaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private SNSUserInfoService sNSUserInfoService;
    /**
     * 微信授权后回调请求
     * */
    @RequestMapping("getOauth2Info")
    public void getOauth2Info(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.logAllRequestParams();
        // 用户同意授权后，能获取到code
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        // 用户同意授权
        if(!WechatConfig.AUTHDENY.equals(code)){
            WeixinOauth2Token weixinOauth2Token  =  AdvancedUtil.getOauth2AccessToken(WechatConfig.APP_ID,WechatConfig.APP_SECRET,code);
            String accessToken = weixinOauth2Token.getAccessToken();
            // 用户标识
            String openId = weixinOauth2Token.getOpenId();
            // 获取用户信息
            SNSUserInfo snsUserInfo = AdvancedUtil.getSNSUserInfo(accessToken, openId);

            this.sNSUserInfoService.save(snsUserInfo);


        }

    }
    @Override
    public String index() {
        return null;
    }
}
