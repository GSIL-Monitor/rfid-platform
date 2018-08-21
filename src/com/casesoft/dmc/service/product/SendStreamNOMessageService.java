package com.casesoft.dmc.service.product;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.core.util.json.JSONUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.product.SendStreamNOMessageDao;
import com.casesoft.dmc.extend.api.web.epay.alipay.util.httpClient.HttpResponse;
import com.casesoft.dmc.model.product.SendStreamNOMessage;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/17.
 */
@Service
@Transactional
public class SendStreamNOMessageService extends AbstractBaseService<SendStreamNOMessage, String> {
    @Autowired
    private SendStreamNOMessageDao sendStreamNOMessageDao;
    @Override
    public Page<SendStreamNOMessage> findPage(Page<SendStreamNOMessage> page, List<PropertyFilter> filters) {
        return this.sendStreamNOMessageDao.findPage(page,filters);
    }

    @Override
    public void save(SendStreamNOMessage entity) {

    }

    @Override
    public SendStreamNOMessage load(String id) {
        return null;
    }

    @Override
    public SendStreamNOMessage get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<SendStreamNOMessage> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<SendStreamNOMessage> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(SendStreamNOMessage entity) {

    }

    @Override
    public void delete(SendStreamNOMessage entity) {

    }

    @Override
    public void delete(String id) {

    }

    public String WxShopStreamNO(){
        String hql="from SendStreamNOMessage t where t.pushsuccess='N'";
        String message="";
        List<SendStreamNOMessage> sendStreamNOMessages = this.sendStreamNOMessageDao.find(hql);
        for(SendStreamNOMessage sendStreamNOMessage:sendStreamNOMessages){
            Map<String,String> map=new HashMap<String,String>();
            map.put("rfidOrderNo",sendStreamNOMessage.getBillNo());
            map.put("logisticsOrderNo",sendStreamNOMessage.getStreamNO());
            String sendJson = JSONUtil.getJsonString4JavaPOJO(map);
            HttpClient httpclient = new HttpClient();

            try {
                String wxshop_Path = PropertyUtil.getValue("wxshop_Path");
                if(CommonUtil.isNotBlank(wxshop_Path)){
                    PostMethod method = new PostMethod(wxshop_Path+"/v2/order/shippedForRfid");
                    method.setRequestHeader("Content-type", "application/json; charset=UTF-8");
                    method.setRequestHeader("Accept", "application/json; charset=UTF-8");
                    // 设置为默认的重试策略
                    method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());

                    method.setRequestBody(sendJson);

                    httpclient.executeMethod(method);
                    HttpResponse response = new HttpResponse();
                    response.setStringResult(method.getResponseBodyAsString());
                    response.setResponseHeaders(method.getResponseHeaders());
                    String stringResult = response.getStringResult();
                    System.out.println(stringResult);

                    if(CommonUtil.isNotBlank(stringResult)) {
                        Map mapresulrt = JSONUtil.getMap4Json(stringResult);
                        Integer errorCode = Integer.parseInt(mapresulrt.get("errorCode") + "");
                        if(errorCode == 200){
                            sendStreamNOMessage.setPushsuccess("Y");
                            this.sendStreamNOMessageDao.update(sendStreamNOMessage);
                            message="推送成功";
                        }
                        if(errorCode==11008){

                            message="推送失败";
                        }
                        if(errorCode==500){

                            message="推送失败";
                        }

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return message;
    }
}
