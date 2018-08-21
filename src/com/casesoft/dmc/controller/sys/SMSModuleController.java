package com.casesoft.dmc.controller.sys;

import com.casesoft.dmc.cache.CacheManager;
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
import com.casesoft.dmc.model.sys.SMSModule;
import com.casesoft.dmc.service.sys.SMSModuleService;
import org.apache.commons.httpclient.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/31.
 */
@Controller
@RequestMapping("/sys/SMSModule")
public class SMSModuleController extends BaseController implements IBaseInfoController<SMSModule> {
    @Autowired
    private SMSModuleService sMSModuleService;
    @Override
    @RequestMapping(value = "/index")
    public String index() {
        return "/views/sys/smsModule";
    }
    @RequestMapping("/page")
    @ResponseBody
    @Override
    public Page<SMSModule> findPage(Page<SMSModule> page) throws Exception {
        this.logAllRequestParams();//日志
        List<PropertyFilter> filters=PropertyFilter.buildFromHttpRequest(this.getRequest());
        System.out.println("filters");
        page.setPageProperty();

        page=this.sMSModuleService.findPage(page, filters);
        return page;
    }

    @Override
    public List<SMSModule> list() throws Exception {
        return null;
    }
    @RequestMapping(value="/save")
    @ResponseBody
    @Override
    public MessageBox save(SMSModule entity) throws Exception {
        this.logAllRequestParams();
        HttpProtocolHandler instance = HttpProtocolHandler.getInstance();
        HttpRequest  httpRequest=new HttpRequest(HttpResultType.STRING);
        httpRequest.setMethod("post");
        httpRequest.setUrl("http://api.sms.cn/sms/");
        NameValuePair [] allnameValuePair = new NameValuePair[8];
        NameValuePair nameValuePairaencode=new NameValuePair();
        nameValuePairaencode.setName("encode");
        nameValuePairaencode.setValue("utf8");
        allnameValuePair[0]=nameValuePairaencode;
        NameValuePair nameValuePairas=new NameValuePair();
        nameValuePairas.setName("ac");
        nameValuePairas.setValue("template");
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
        nameValuePairtitle.setName("title");
        nameValuePairtitle.setValue(entity.getTitle());
        allnameValuePair[4]=nameValuePairtitle;
        NameValuePair nameValuePaircontent=new NameValuePair();
        nameValuePaircontent.setName("content");
        nameValuePaircontent.setValue(entity.getContent());
        allnameValuePair[5]=nameValuePaircontent;
        NameValuePair nameValuePairtype =new NameValuePair();
        nameValuePairtype.setName("type");
        nameValuePairtype.setValue("1");
        allnameValuePair[6]=nameValuePairtype;
        NameValuePair nameValuePairdataformat =new NameValuePair();
        nameValuePairdataformat.setName("dataformat");
        nameValuePairdataformat.setValue("2");
        allnameValuePair[7]=nameValuePairdataformat;
        httpRequest.setParameters(allnameValuePair);
        httpRequest.setCharset(AlipayConfig.input_charset);
        HttpResponse httpResponse = instance.execute(httpRequest, "", "");
        String stringResult = httpResponse.getStringResult();

        //解析返回的json
        Map<String,String> map4Json = JSONUtil.getMap4Json(stringResult);
        Iterator<Map.Entry<String, String>> iterator = map4Json.entrySet().iterator();
        boolean issave=true;
        String message="";
        while (iterator.hasNext()){
            Map.Entry<String, String> next = iterator.next();
            String key = next.getKey();
            String value=map4Json.get(key);
            if(key.equals("stat")){
                if(value.equals("100")){
                    entity.setSaveTime(new Date());
                    entity.setApproval("noshow");
                }else{
                    issave=false;
                }
            }
            if(key.equals("templateid")){
                entity.setTemplateid(value);
                entity.setId(value);
            }
            if(key.equals("message")){
                message=value;
            }
        }
       /* if(CommonUtil.isBlank(entity.getId())){
            entity.setSaveTime(new Date());
            entity.setApproval("noshow");
        }*/
        try {
            if(issave){
                this.sMSModuleService.save(entity);
            }
            CacheManager.refreshUserCache();
            return returnSuccessInfo(message);
        }catch (Exception e){
            return returnFailInfo("修改失败");
        }

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
    @RequestMapping(value ="/editPage")
    @ResponseBody
    public MessageBox editPage(String templateid) throws Exception {
        this.logAllRequestParams();
        SMSModule load = this.sMSModuleService.load(templateid);
       /* ModelAndView mv = new ModelAndView("/views/sys/smsModule_edit");
        mv.addObject("pageType","edit");
        mv.addObject("SMSModule",load);*/
        return new MessageBox(true, "",load);
    }

    @RequestMapping(value="/check")
    @ResponseBody
    public  MessageBox check(String templateid){
        this.logAllRequestParams();
        HttpProtocolHandler instance = HttpProtocolHandler.getInstance();
        HttpRequest  httpRequest=new HttpRequest(HttpResultType.STRING);
        httpRequest.setMethod("post");
        httpRequest.setUrl("http://api.sms.cn/sms/");
        NameValuePair [] allnameValuePair = new NameValuePair[5];
        NameValuePair nameValuePairaencode=new NameValuePair();
        nameValuePairaencode.setName("encode");
        nameValuePairaencode.setValue("utf8");
        allnameValuePair[0]=nameValuePairaencode;
        NameValuePair nameValuePairac=new NameValuePair();
        nameValuePairac.setName("ac");
        nameValuePairac.setValue("templatestatus");
        allnameValuePair[1]=nameValuePairac;
        NameValuePair nameValuePairuid=new NameValuePair();
        nameValuePairuid.setName("uid");
        nameValuePairuid.setValue("saber");
        allnameValuePair[2]=nameValuePairuid;
        NameValuePair nameValuePairpwd=new NameValuePair();
        nameValuePairpwd.setName("pwd");
        nameValuePairpwd.setValue("9ff964aa4a69f95bed7b6f6a07b7e284");
        allnameValuePair[3]=nameValuePairpwd;
        NameValuePair nameValuePairtemplateid=new NameValuePair();
        nameValuePairtemplateid.setName("templateid");
        nameValuePairtemplateid.setValue(templateid);
        allnameValuePair[4]=nameValuePairtemplateid;
        httpRequest.setParameters(allnameValuePair);
        httpRequest.setCharset(AlipayConfig.input_charset);
        HttpResponse httpResponse = null;
        try {
            httpResponse = instance.execute(httpRequest, "", "");
            String stringResult = httpResponse.getStringResult();
            //解析返回的json
            Map<String,String> map4Json = JSONUtil.getMap4Json(stringResult);
            Iterator<Map.Entry<String, String>> iterator = map4Json.entrySet().iterator();
            String message="";
            while (iterator.hasNext()){
                Map.Entry<String, String> next = iterator.next();
                String key = next.getKey();
                String value=map4Json.get(key);
                if(key.equals("stat")){
                    if(value.equals("100")){
                        SMSModule load = this.sMSModuleService.load(templateid);
                        load.setApproval("isshow");
                        this.sMSModuleService.save(load);
                    }
                }
                if(key.equals("message")){
                    message=value;
                }
                if(key.equals("question")){
                    message+=value;
                }
            }
            return returnSuccessInfo(message);
        } catch (IOException e) {
            e.printStackTrace();
            return returnFailInfo("修改失败");
        }


    }
    @RequestMapping(value="/update")
    @ResponseBody
    public MessageBox update(SMSModule entity) throws Exception {
        this.logAllRequestParams();
        HttpProtocolHandler instance = HttpProtocolHandler.getInstance();
        HttpRequest  httpRequest=new HttpRequest(HttpResultType.STRING);
        httpRequest.setMethod("post");
        httpRequest.setUrl("http://api.sms.cn/sms/");
        NameValuePair [] allnameValuePair = new NameValuePair[9];
        NameValuePair nameValuePairaencode=new NameValuePair();
        nameValuePairaencode.setName("encode");
        nameValuePairaencode.setValue("utf8");
        allnameValuePair[0]=nameValuePairaencode;
        NameValuePair nameValuePairas=new NameValuePair();
        nameValuePairas.setName("ac");
        nameValuePairas.setValue("template");
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
        nameValuePairtitle.setName("title");
        nameValuePairtitle.setValue(entity.getTitle());
        allnameValuePair[4]=nameValuePairtitle;
        NameValuePair nameValuePaircontent=new NameValuePair();
        nameValuePaircontent.setName("content");
        nameValuePaircontent.setValue(entity.getContent());
        allnameValuePair[5]=nameValuePaircontent;
        NameValuePair nameValuePairtype =new NameValuePair();
        nameValuePairtype.setName("type");
        nameValuePairtype.setValue("1");
        allnameValuePair[6]=nameValuePairtype;
        NameValuePair nameValuePairdataformat =new NameValuePair();
        nameValuePairdataformat.setName("dataformat");
        nameValuePairdataformat.setValue("2");
        allnameValuePair[7]=nameValuePairdataformat;
        NameValuePair nameValuePairtemplateid =new NameValuePair();
        nameValuePairtemplateid.setName("templateid");
        nameValuePairtemplateid.setValue(entity.getTemplateid());
        allnameValuePair[8]=nameValuePairdataformat;
        httpRequest.setParameters(allnameValuePair);
        httpRequest.setCharset(AlipayConfig.input_charset);
        HttpResponse httpResponse = instance.execute(httpRequest, "", "");
        String stringResult = httpResponse.getStringResult();

        //解析返回的json
        Map<String,String> map4Json = JSONUtil.getMap4Json(stringResult);
        Iterator<Map.Entry<String, String>> iterator = map4Json.entrySet().iterator();
        boolean issave=true;
        String message="";
        while (iterator.hasNext()){
            Map.Entry<String, String> next = iterator.next();
            String key = next.getKey();
            String value=map4Json.get(key);
            if(key.equals("stat")){
                if(value.equals("100")){
                    entity.setSaveTime(new Date());
                    entity.setApproval("noshow");
                }else{
                    issave=false;
                }
            }
            if(key.equals("templateid")){
                entity.setTemplateid(value);
                entity.setId(value);
            }
            if(key.equals("message")){
                message=value;
            }
        }
       /* if(CommonUtil.isBlank(entity.getId())){
            entity.setSaveTime(new Date());
            entity.setApproval("noshow");
        }*/
        try {
            if(issave){
                this.sMSModuleService.save(entity);
            }
            CacheManager.refreshUserCache();
            return returnSuccessInfo(message);
        }catch (Exception e){
            return returnFailInfo("修改失败");
        }

    }


}
