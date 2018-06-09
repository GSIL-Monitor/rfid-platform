package com.casesoft.dmc.controller.pad.templatemsg;

import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.pad.Template.TemplateData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.casesoft.dmc.controller.pad.templatemsg.WX_TemplateMsgUtil.packJsonmsg;

/**
 * 发送模板消息
 * Created by liu on 2018/6/6.
 */
@Controller
@RequestMapping("/pad/templatemsg/wechattemplate")
public class WechatTemplate  extends BaseController implements IBaseInfoController<WechatTemplate> {

    private static String openId = "oFR_j04ELJrfU09wM2M8chXbYFXY";
    @RequestMapping("/senWS")
    public void sen(){
        senMsg(openId);
        returnMsg(openId);
    }

    static void senMsg(String openId){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
        Map<String,TemplateData> param = new HashMap<>();
        param.put("first",new TemplateData("尊敬的顾客，最新交易提醒：","#696969"));
        param.put("keyword1",new TemplateData("消费金额","#696969"));
        param.put("keyword2",new TemplateData("购买数量","#696969"));
        param.put("keyword3",new TemplateData(simpleDateFormat.format(date),"#696969"));
        param.put("keyword4",new TemplateData("单据编号","#696969"));
        param.put("keyword5",new TemplateData("购买店铺","#696969"));
        param.put("remark",new TemplateData("如有任何疑问请与销售员联系","#696969"));
        //注册的微信-模板Id
        String regTempId ="5HNerm3lYjxNZddFUf3r_dUiarZhAWer-16iIHDCrpU";
        //调用发送微信消息给用户的接口
        String state = WX_TemplateMsgUtil.sendWechatMsgToUser(openId , regTempId, "", "#000000", packJsonmsg(param));
    }

    static void returnMsg(String openId){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
        Map<String,TemplateData> param = new HashMap<>();
        param.put("first",new TemplateData("尊敬的顾客，最新交易提醒：","#696969"));
        param.put("keyword1",new TemplateData("退货订单号","#696969"));
        param.put("keyword2",new TemplateData("退货商品","#696969"));
        param.put("keyword3",new TemplateData("退货数量","#696969"));
        param.put("keyword4",new TemplateData("退货金额","#696969"));
        param.put("remark",new TemplateData("如有任何疑问请与销售员联系","#696969"));
        //注册的微信-模板Id
        String regTempId ="fBw6ZpgaCzj-4eZV_OeBvKq4ceRRUJ0DB8VvVAtCUk4";
        //调用发送微信消息给用户的接口
        String state = WX_TemplateMsgUtil.sendWechatMsgToUser(openId , regTempId, "", "#000000", packJsonmsg(param));
    }

    @Override
    public Page<WechatTemplate> findPage(Page<WechatTemplate> page) throws Exception {
        return null;
    }

    @Override
    public List<WechatTemplate> list() throws Exception {
        return null;
    }

    @Override
    public MessageBox save(WechatTemplate entity) throws Exception {
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
    public String index() {
        return null;
    }
}
