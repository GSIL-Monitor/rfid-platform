package com.casesoft.dmc.controller.sys;

import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.json.JSONUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.api.web.epay.alipay.config.AlipayConfig;
import com.casesoft.dmc.extend.api.web.epay.alipay.util.httpClient.HttpProtocolHandler;
import com.casesoft.dmc.extend.api.web.epay.alipay.util.httpClient.HttpRequest;
import com.casesoft.dmc.extend.api.web.epay.alipay.util.httpClient.HttpResponse;
import com.casesoft.dmc.extend.api.web.epay.alipay.util.httpClient.HttpResultType;
import com.casesoft.dmc.model.sys.GuestView;
import com.casesoft.dmc.model.sys.SMSMessage;
import com.casesoft.dmc.model.sys.SMSModule;
import com.casesoft.dmc.service.sys.GuestViewService;
import com.casesoft.dmc.service.sys.SMSMessageService;
import com.casesoft.dmc.service.sys.SMSModuleService;
import org.apache.commons.httpclient.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/11/2.
 */
@Controller
@RequestMapping("/sys/SMSMessage")
public class SMSMessageController extends BaseController implements IBaseInfoController<SMSMessage> {
    @Autowired
    private SMSMessageService sMSMessageService;
    @Autowired
    private GuestViewService guestViewService;
    @Autowired
    private SMSModuleService sMSModuleService;
    @RequestMapping("/page")
    @ResponseBody
    @Override
    public Page<SMSMessage> findPage(Page<SMSMessage> page) throws Exception {
        this.logAllRequestParams();//日志
        List<PropertyFilter> filters=PropertyFilter.buildFromHttpRequest(this.getRequest());
        System.out.println("filters");
        page.setPageProperty();

        page=this.sMSMessageService.findPage(page, filters);
        return page;
    }

    @Override
    public List<SMSMessage> list() throws Exception {
        return null;
    }

    @Override
    public MessageBox save(SMSMessage entity) throws Exception {
        return null;
    }

    @Override
    public MessageBox edit(String taskId) throws Exception {
        return null;
    }

    @Override
    public MessageBox delete(String taskId) throws Exception {
        return null;
    }

    @Override
    public void exportExcel() throws Exception {

    }

    @Override
    public MessageBox importExcel(MultipartFile file) throws Exception {
        return null;
    }

    @Override
    @RequestMapping(value = "/index")
    public String index() {
        return "/views/sys/smsMessage";
    }
    @RequestMapping("/sendMessage")
    public void  sendMessage(){
        //获取当前时间
        Format f = new SimpleDateFormat("MM-dd");
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DAY_OF_MONTH, 7);// 今天+7天
        Date tomorrow = c.getTime();
        //查询7天后
        HttpProtocolHandler instance7 = HttpProtocolHandler.getInstance();
        HttpRequest httpRequest7=new HttpRequest(HttpResultType.STRING);
        httpRequest7.setMethod("post");
        httpRequest7.setUrl("http://api.sms.cn/sms/");
        List<GuestView> findby7birth = this.guestViewService.findbybirth(f.format(tomorrow));
        for(int i=0;i<findby7birth.size();i++){
            GuestView guestView = findby7birth.get(i);
            NameValuePair[] allnameValuePair = new NameValuePair[6];
            NameValuePair nameValuePairaencode=new NameValuePair();
            nameValuePairaencode.setName("encode");
            nameValuePairaencode.setValue("utf8");
            allnameValuePair[0]=nameValuePairaencode;
            NameValuePair nameValuePairas=new NameValuePair();
            nameValuePairas.setName("ac");
            nameValuePairas.setValue("send");
            allnameValuePair[1]=nameValuePairas;
            NameValuePair nameValuePairuid=new NameValuePair();
            nameValuePairuid.setName("uid");
            nameValuePairuid.setValue("saber");
            allnameValuePair[2]=nameValuePairuid;
            nameValuePairuid.setName("uid");
            nameValuePairuid.setValue("saber");
            NameValuePair nameValuePairpwd=new NameValuePair();
            nameValuePairpwd.setName("pwd");
            nameValuePairpwd.setValue("9ff964aa4a69f95bed7b6f6a07b7e284");
            allnameValuePair[3]=nameValuePairpwd;
            NameValuePair nameValuePairtitle=new NameValuePair();
            nameValuePairtitle.setName("template");
            nameValuePairtitle.setValue("413126");
            allnameValuePair[4]=nameValuePairtitle;
            NameValuePair nameValuePaircontent=new NameValuePair();
            nameValuePaircontent.setName("mobile");
            nameValuePaircontent.setValue(guestView.getPhone());
            allnameValuePair[5]=nameValuePaircontent;
            NameValuePair nameValuePairtype =new NameValuePair();
            nameValuePairtype.setName("content");
            String value="content={'name':'"+guestView.getName()+"'}";
            nameValuePairtype.setValue(value);
            allnameValuePair[6]=nameValuePairtype;

            httpRequest7.setCharset(AlipayConfig.input_charset);
            HttpResponse httpResponse = null;
            try {
                httpResponse = instance7.execute(httpRequest7, "", "");
                String stringResult = httpResponse.getStringResult();
                SMSModule smsModule = this.sMSModuleService.load("413126");
                //解析返回的json
                SMSMessage sMSMessage=new SMSMessage();
                Map<String,String> map4Json = JSONUtil.getMap4Json(stringResult);
                Iterator<Map.Entry<String, String>> iterator = map4Json.entrySet().iterator();
                boolean issave=true;
                while (iterator.hasNext()){
                    Map.Entry<String, String> next = iterator.next();
                    String key = next.getKey();
                    String values=map4Json.get(key);
                    if(key.equals("stat")){
                        if(value.equals("100")){
                            sMSMessage.setCustomer(guestView.getName());
                            sMSMessage.setIphone(guestView.getPhone());
                            String messages=smsModule.getContent().replace("{$name}",guestView.getName());
                            sMSMessage.setMessage(messages);
                            sMSMessage.setSendTime(new Date());
                        }else{
                            issave=false;
                        }
                    }
                }
                if(issave){
                    this.sMSMessageService.save(sMSMessage);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        //查询当天
        List<GuestView> findbynowbirth = this.guestViewService.findbybirth(f.format(today));
        //发送短信
        HttpProtocolHandler instance = HttpProtocolHandler.getInstance();
        HttpRequest httpRequest=new HttpRequest(HttpResultType.STRING);
        httpRequest.setMethod("post");
        httpRequest.setUrl("http://api.sms.cn/sms/");
        for(int i=0;i<findbynowbirth.size();i++){
            GuestView guestView = findbynowbirth.get(i);
            NameValuePair[] allnameValuePair = new NameValuePair[7];
            NameValuePair nameValuePairaencode=new NameValuePair();
            nameValuePairaencode.setName("encode");
            nameValuePairaencode.setValue("utf8");
            allnameValuePair[0]=nameValuePairaencode;
            NameValuePair nameValuePairas=new NameValuePair();
            nameValuePairas.setName("ac");
            nameValuePairas.setValue("send");
            allnameValuePair[1]=nameValuePairas;
            NameValuePair nameValuePairuid=new NameValuePair();
            nameValuePairuid.setName("uid");
            nameValuePairuid.setValue("saber");
            allnameValuePair[2]=nameValuePairuid;
            nameValuePairuid.setName("uid");
            nameValuePairuid.setValue("saber");
            NameValuePair nameValuePairpwd=new NameValuePair();
            nameValuePairpwd.setName("pwd");
            nameValuePairpwd.setValue("9ff964aa4a69f95bed7b6f6a07b7e284");
            allnameValuePair[3]=nameValuePairpwd;
            NameValuePair nameValuePairtitle=new NameValuePair();
            nameValuePairtitle.setName("template");
            nameValuePairtitle.setValue("412970");
            allnameValuePair[4]=nameValuePairtitle;
            NameValuePair nameValuePaircontent=new NameValuePair();
            nameValuePaircontent.setName("mobile");
            nameValuePaircontent.setValue(guestView.getPhone());
            allnameValuePair[5]=nameValuePaircontent;
            NameValuePair nameValuePairtype =new NameValuePair();
            nameValuePairtype.setName("content");
            String value="content={'name':'"+guestView.getName()+"'}";
            nameValuePairtype.setValue(value);
            allnameValuePair[6]=nameValuePairtype;

            httpRequest.setCharset(AlipayConfig.input_charset);
            HttpResponse httpResponse = null;
            try {
                httpResponse = instance.execute(httpRequest, "", "");
                String stringResult = httpResponse.getStringResult();
                SMSModule smsModule = this.sMSModuleService.load("413126");
                //解析返回的json
                SMSMessage sMSMessage=new SMSMessage();
                Map<String,String> map4Json = JSONUtil.getMap4Json(stringResult);
                Iterator<Map.Entry<String, String>> iterator = map4Json.entrySet().iterator();
                boolean issave=true;
                while (iterator.hasNext()){
                    Map.Entry<String, String> next = iterator.next();
                    String key = next.getKey();
                    String values=map4Json.get(key);
                    if(key.equals("stat")){
                        if(value.equals("100")){
                            sMSMessage.setCustomer(guestView.getName());
                            sMSMessage.setIphone(guestView.getPhone());
                            String messages=smsModule.getContent().replace("{$name}",guestView.getName());
                            sMSMessage.setMessage(messages);
                            sMSMessage.setSendTime(new Date());
                        }else{
                            issave=false;
                        }
                    }
                }
                if(issave){
                    this.sMSMessageService.save(sMSMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        
    }
}
