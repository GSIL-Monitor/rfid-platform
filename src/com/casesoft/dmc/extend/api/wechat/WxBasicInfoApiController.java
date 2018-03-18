package com.casesoft.dmc.extend.api.wechat;

import com.casesoft.dmc.controller.sys.ResourceUtil;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.json.JSONUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.core.vo.SidebarMenu;
import com.casesoft.dmc.extend.api.web.ApiBaseController;
import com.casesoft.dmc.extend.api.web.epay.alipay.config.AlipayConfig;
import com.casesoft.dmc.extend.api.web.epay.alipay.util.httpClient.HttpProtocolHandler;
import com.casesoft.dmc.extend.api.web.epay.alipay.util.httpClient.HttpRequest;
import com.casesoft.dmc.extend.api.web.epay.alipay.util.httpClient.HttpResponse;
import com.casesoft.dmc.extend.api.web.epay.alipay.util.httpClient.HttpResultType;
import com.casesoft.dmc.model.sys.Resource;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.sys.impl.ResourceService;
import com.casesoft.dmc.service.sys.impl.UserService;
import io.swagger.annotations.Api;
import org.apache.commons.httpclient.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Session on 2017-12-06.
 */

@Controller
@RequestMapping(value = "/api/wx/basic")
@Api(description = "基础信息接口")
public class WxBasicInfoApiController extends ApiBaseController{

	@Autowired
	private UserService userService;

	@Autowired
	private ResourceService resourceService;

	@RequestMapping(value = "/loginWS.do")
	@ResponseBody
	public MessageBox loginWS(String userCode,String password,String phoneNumber){
		this.logAllRequestParams();
		User user=null;
		if(CommonUtil.isBlank(phoneNumber))
			user=this.userService.getUser(userCode,password);
		else{
			user=this.userService.findUserByPhone(phoneNumber);
		}
		if(CommonUtil.isBlank(user))
			return returnFailInfo("登陆失败");
		return returnSuccessInfo("登陆成功",user);
	}

	@RequestMapping(value = "/findwxWS.do")
	@ResponseBody
	public MessageBox findwxWS(String appid,String secret,String js_code,String grant_type){
		try {
			HttpProtocolHandler instance7 = HttpProtocolHandler.getInstance();
			HttpRequest httpRequest7=new HttpRequest(HttpResultType.STRING);
			httpRequest7.setMethod("post");
			httpRequest7.setUrl("https://api.weixin.qq.com/sns/jscode2session");
			NameValuePair[] allnameValuePair = new NameValuePair[4];
			NameValuePair nameValuePairaenappid=new NameValuePair();
			nameValuePairaenappid.setName("appid");
			nameValuePairaenappid.setValue(appid);
			allnameValuePair[0]=nameValuePairaenappid;
			NameValuePair nameValuePairaenasecret=new NameValuePair();
			nameValuePairaenasecret.setName("secret");
			nameValuePairaenasecret.setValue(secret);
			allnameValuePair[1]=nameValuePairaenasecret;
			NameValuePair nameValuePairaenajs_code=new NameValuePair();
			nameValuePairaenajs_code.setName("js_code");
			nameValuePairaenajs_code.setValue(js_code);
			allnameValuePair[2]=nameValuePairaenajs_code;
			NameValuePair nameValuePairaenagrant_type=new NameValuePair();
			nameValuePairaenagrant_type.setName("grant_type");
			nameValuePairaenagrant_type.setValue(grant_type);
			allnameValuePair[3]=nameValuePairaenagrant_type;
			httpRequest7.setCharset(AlipayConfig.input_charset);
			httpRequest7.setParameters(allnameValuePair);
			HttpResponse httpResponse = null;
			httpResponse = instance7.execute(httpRequest7, "", "");
			String stringResult = httpResponse.getStringResult();
			Map<String,String> map4Json = JSONUtil.getMap4Json(stringResult);
			return returnSuccessInfo("登陆成功",map4Json);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}
	@RequestMapping(value = "/showSideBarMenuWS.do")
	@ResponseBody
	public List<SidebarMenu> getMenus(String roleId){
		this.logAllRequestParams();
		List<Resource> resourceList = this.resourceService.getWXResourceByRole(roleId);
		List<SidebarMenu> tree = ResourceUtil.convertToSidebarMenuVo(resourceList);
		return tree;
	}

	@Override
	public String index() {
		return null;
	}
}
