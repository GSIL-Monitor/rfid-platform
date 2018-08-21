package com.casesoft.dmc.service.product;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.core.util.json.JSONUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.product.SendInventotyDao;
import com.casesoft.dmc.dao.product.StyleDao;
import com.casesoft.dmc.extend.api.web.epay.alipay.util.httpClient.HttpResponse;
import com.casesoft.dmc.model.product.PaymentMessage;
import com.casesoft.dmc.model.product.SendInventory;
import com.casesoft.dmc.model.product.SendStreamNOMessage;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.task.BusinessDtl;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

/**
 * Created by Administrator on 2018/1/12.
 */
@Service
@Transactional
public class SendInventoryService extends AbstractBaseService<SendInventory, String> {
    @Autowired
    private SendInventotyDao sendInventotyDao;
    @Autowired
    private StyleDao styleDao;

    @Override
    public Page<SendInventory> findPage(Page<SendInventory> page, List<PropertyFilter> filters) {
        return this.sendInventotyDao.findPage(page,filters);
    }

    @Override
    public void save(SendInventory entity) {

    }

    @Override
    public SendInventory load(String id) {
        return null;
    }

    @Override
    public SendInventory get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<SendInventory> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<SendInventory> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(SendInventory entity) {

    }

    @Override
    public void delete(SendInventory entity) {

    }

    @Override
    public void delete(String id) {

    }

    public List<SendInventory> findStyleAll(){
        String hql="from SendInventory t where t.pushsuccess='N'";
        List<SendInventory> SendInventoryList = this.sendInventotyDao.find(hql);
        return SendInventoryList;

    }

   public void sendNoInventory() throws Exception {
        String hql="from SendInventory t where t.pushsuccess='N'";
        List<SendInventory> SendInventoryList = this.sendInventotyDao.find(hql);
        //得到要发送的list
        List<Map<String,Object>> sendlist= new ArrayList<Map<String,Object>>();
        for(int i=0;i<sendlist.size();i++){

            if(i==0){
                Map<String,Object> map=new HashMap<String,Object>();
                map.put("rfidStyleId",SendInventoryList.get(i).getStyleId());
                Map<String,String> delmap=new HashMap<String,String>();
                List<Map<String,String>> skuFormListList= new ArrayList<Map<String,String>>();
                delmap.put("spec",SendInventoryList.get(i).getColorId()+","+SendInventoryList.get(i).getSizeId());
                delmap.put("price","0");
                delmap.put("amount","-"+SendInventoryList.get(i).getQty());
                skuFormListList.add(delmap);
                map.put("skuFormList",skuFormListList);
                sendlist.add(map);
            }else{
                for(int a=0;a<sendlist.size();a++){
                    if(sendlist.get(a).get("rfidStyleId").equals(SendInventoryList.get(i).getStyleId())){
                        List<Map<String,String>> lists= ( List<Map<String,String>>)sendlist.get(a).get("skuFormList");
                        Map<String,String> delmap=new HashMap<String,String>();
                        delmap.put("spec",SendInventoryList.get(i).getColorId()+","+SendInventoryList.get(i).getSizeId());
                        delmap.put("price","0");
                        delmap.put("amount",+SendInventoryList.get(i).getQty()+"");
                        lists.add(delmap);

                    }else{
                        Map<String,Object> map=new HashMap<String,Object>();
                        map.put("rfidStyleId",SendInventoryList.get(i).getStyleId());
                        Map<String,String> delmap=new HashMap<String,String>();
                        List<Map<String,String>> skuFormListList= new ArrayList<Map<String,String>>();
                        delmap.put("spec",SendInventoryList.get(i).getColorId()+","+SendInventoryList.get(i).getSizeId());
                        delmap.put("price","0");
                        delmap.put("amount",SendInventoryList.get(i).getQty()+"");
                        skuFormListList.add(delmap);
                        map.put("skuFormList",skuFormListList);
                        sendlist.add(map);
                    }
                }
            }

        }
        String jsonStringByList = JSONUtil.getJsonStringByList(sendlist);
        //String a="[{\"rfidStyleId\":\"SA12306\",\"skuFormList\": [{\"spec\": \"黄色,S\",\"price\":\"0\",\"amount\": \"-1\"},{\"spec\":\"黄色,L\",\"price\": \"0\",\"amount\": \"-1\"}]},{\"rfidStyleId\": \"SA12307\",\"skuFormList\": [{\"spec\":\"黄色,S\",\"price\": \"0\",\"amount\":\"5\"},{\"spec\": \"黄色,L\",\"price\": \"0\",\"amount\":\"5\"}]},{\"rfidStyleId\": \"SA12308\",\"skuFormList\": [{\"spec\": \"黄色,S\",\"price\": \"0\",\"amount\": \"-1\"},{\"spec\": \"黄色,L\",\"price\": \"0\",\"amount\": \"-1\"}]}]";
        HttpClient httpclient = new HttpClient();
       String wxshop_Path = PropertyUtil.getValue("wxshop_Path");
       try {
           List<SendInventory> savelist = new ArrayList<SendInventory>();
           if(CommonUtil.isNotBlank(wxshop_Path)){
               PostMethod method = new PostMethod(wxshop_Path+"/v2/product/inventoryChangeForRfid");
               method.setRequestHeader("Content-type", "application/json; charset=UTF-8");
               method.setRequestHeader("Accept", "application/json; charset=UTF-8");
               // 设置为默认的重试策略
               method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());

               method.setRequestBody(jsonStringByList);

               httpclient.executeMethod(method);
               HttpResponse response = new HttpResponse();
               response.setStringResult(method.getResponseBodyAsString());
               response.setResponseHeaders(method.getResponseHeaders());
               String stringResult = response.getStringResult();
               System.out.println(stringResult);

               if(CommonUtil.isNotBlank(stringResult)) {
                   Map mapresulrt = JSONUtil.getMap4Json(stringResult);
                   Integer errorCode = Integer.parseInt(mapresulrt.get("errorCode") + "");

                   if (errorCode != 200) {
                       for (SendInventory sendInventorys : SendInventoryList) {
                           SendInventory sendInventory = new SendInventory();
                           sendInventory.setId(new GuidCreator().toString());
                           sendInventory.setColorId(sendInventorys.getColorId());
                           sendInventory.setQty(Integer.parseInt(sendInventorys.getQty() + ""));
                           sendInventory.setSizeId(sendInventorys.getSizeId());
                           sendInventory.setStyleId(sendInventorys.getStyleId());
                           sendInventory.setStyleName(CacheManager.getStyleNameById(sendInventorys.getStyleId()));
                           sendInventory.setPushsuccess("N");
                           sendInventory.setBillDate(new Date());
                           sendInventory.setMessage(mapresulrt.get("moreInfo") + "");
                           savelist.add(sendInventory);
                       }

                   }else {
                       String id="";
                       for(int i=0;i<SendInventoryList.size();i++){
                           if(i==0){
                               id+="'"+SendInventoryList.get(i).getId()+"'";
                           }else{
                               id+=",'"+SendInventoryList.get(i).getId()+"'";
                           }
                       }
                       String updatehql="update SendInventory t set t.pushsuccess=Y where t.id in("+id+")";
                       this.sendInventotyDao.batchExecute(updatehql);
                   }
               }else{
                   for (SendInventory sendInventorys : SendInventoryList) {
                       SendInventory sendInventory = new SendInventory();
                       sendInventory.setId(new GuidCreator().toString());
                       sendInventory.setColorId(sendInventorys.getColorId());
                       sendInventory.setQty(Integer.parseInt(sendInventorys.getQty() + ""));
                       sendInventory.setSizeId(sendInventorys.getSizeId());
                       sendInventory.setStyleId(sendInventorys.getStyleId());
                       sendInventory.setStyleName(CacheManager.getStyleNameById(sendInventorys.getStyleId()));
                       sendInventory.setPushsuccess("N");
                       sendInventory.setBillDate(new Date());
                       sendInventory.setMessage("访问接口没成功");
                       savelist.add(sendInventory);
                   }
               }

           }else{
               for (SendInventory sendInventorys : SendInventoryList) {
                   SendInventory sendInventory = new SendInventory();
                   sendInventory.setId(new GuidCreator().toString());
                   sendInventory.setColorId(sendInventorys.getColorId());
                   sendInventory.setQty(Integer.parseInt(sendInventorys.getQty() + ""));
                   sendInventory.setSizeId(sendInventorys.getSizeId());
                   sendInventory.setStyleId(sendInventorys.getStyleId());
                   sendInventory.setStyleName(CacheManager.getStyleNameById(sendInventorys.getStyleId()));
                   sendInventory.setPushsuccess("N");
                   sendInventory.setBillDate(new Date());
                   sendInventory.setMessage("访问接口没成功");
                   savelist.add(sendInventory);
               }
           }
           this.sendInventotyDao.doBatchInsert(savelist);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void WxChatStockUpdate(  List<BusinessDtl> dtlList) throws Exception {
        //筛选数据，styleid推送成功的
        logger.error("SendInventoryService中筛选数据，styleid推送成功的"+dtlList);
        List<Style> list=new ArrayList<Style>();
        for(BusinessDtl businessDtl:dtlList) {
            list.add(CacheManager.getStyleById(businessDtl.getStyleId()));
        }
        //String hql="from Style t where t.id in ("+styleid+")";

        for(int i=0;i<list.size();i++){
            if(CommonUtil.isBlank(list.get(i).getIspush())||CommonUtil.isBlank(list.get(i).getPushsuccess())||list.get(i).getIspush().equals("N")||list.get(i).getPushsuccess().equals("N")){
                list.remove(i);
            }
        }
        //得到有用的数据
        for(int i=0;i<list.size();i++){
           for(int a=0;a<dtlList.size();a++){
               if(list.get(i).getStyleId()!=dtlList.get(a).getSizeId()){
                   dtlList.remove(a);
               }
           }
        }
        try {
        //得到要发送的list
            if(list.size()!=0) {


                    List<Map<String, Object>> sendlist = new ArrayList<Map<String, Object>>();
                    for (int i = 0; i < dtlList.size(); i++) {

                        if (i == 0) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("rfidStyleId", dtlList.get(i).getStyleId());
                            Map<String, String> delmap = new HashMap<String, String>();
                            List<Map<String, String>> skuFormListList = new ArrayList<Map<String, String>>();
                            delmap.put("spec", dtlList.get(i).getColorId() + "," + dtlList.get(i).getSizeId());
                            delmap.put("price", "0");
                            delmap.put("amount", "-" + dtlList.get(i).getQty());
                            skuFormListList.add(delmap);
                            map.put("skuFormList", skuFormListList);
                            sendlist.add(map);
                        } else {
                            for (int a = 0; a < sendlist.size(); a++) {
                                if (sendlist.get(a).get("rfidStyleId").equals(dtlList.get(i).getStyleId())) {
                                    List<Map<String, String>> lists = (List<Map<String, String>>) sendlist.get(a).get("skuFormList");
                                    Map<String, String> delmap = new HashMap<String, String>();
                                    delmap.put("spec", dtlList.get(i).getColorId() + "," + dtlList.get(i).getSizeId());
                                    delmap.put("price", "0");
                                    delmap.put("amount", +dtlList.get(i).getQty() + "");
                                    lists.add(delmap);

                                } else {
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("rfidStyleId", dtlList.get(i).getStyleId());
                                    Map<String, String> delmap = new HashMap<String, String>();
                                    List<Map<String, String>> skuFormListList = new ArrayList<Map<String, String>>();
                                    delmap.put("spec", dtlList.get(i).getColorId() + "," + dtlList.get(i).getSizeId());
                                    delmap.put("price", "0");
                                    delmap.put("amount", dtlList.get(i).getQty() + "");
                                    skuFormListList.add(delmap);
                                    map.put("skuFormList", skuFormListList);
                                    sendlist.add(map);
                                }
                            }
                        }

                    }
                    String jsonStringByList = JSONUtil.getJsonStringByList(sendlist);
                    //String a="[{\"rfidStyleId\":\"SA12306\",\"skuFormList\": [{\"spec\": \"黄色,S\",\"price\":\"0\",\"amount\": \"-1\"},{\"spec\":\"黄色,L\",\"price\": \"0\",\"amount\": \"-1\"}]},{\"rfidStyleId\": \"SA12307\",\"skuFormList\": [{\"spec\":\"黄色,S\",\"price\": \"0\",\"amount\":\"5\"},{\"spec\": \"黄色,L\",\"price\": \"0\",\"amount\":\"5\"}]},{\"rfidStyleId\": \"SA12308\",\"skuFormList\": [{\"spec\": \"黄色,S\",\"price\": \"0\",\"amount\": \"-1\"},{\"spec\": \"黄色,L\",\"price\": \"0\",\"amount\": \"-1\"}]}]";
                    HttpClient httpclient = new HttpClient();
                    String wxshop_Path = PropertyUtil.getValue("wxshop_Path");
                    List<SendInventory> savelist = new ArrayList<SendInventory>();
                    if(CommonUtil.isNotBlank(wxshop_Path)){
                        PostMethod method = new PostMethod(wxshop_Path + "/v2/product/inventoryChangeForRfid");
                        method.setRequestHeader("Content-type", "application/json; charset=UTF-8");
                        method.setRequestHeader("Accept", "application/json; charset=UTF-8");
                        // 设置为默认的重试策略
                        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());

                        method.setRequestBody(jsonStringByList);

                        httpclient.executeMethod(method);
                        HttpResponse response = new HttpResponse();
                        response.setStringResult(method.getResponseBodyAsString());
                        response.setResponseHeaders(method.getResponseHeaders());
                        String stringResult = response.getStringResult();
                        logger.error("SendInventoryService中WxChatStockUpdate接口的结果" + stringResult);

                        if (CommonUtil.isNotBlank(stringResult)) {
                            Map mapresulrt = JSONUtil.getMap4Json(stringResult);
                            Integer errorCode = Integer.parseInt(mapresulrt.get("errorCode") + "");

                            if (errorCode != 200) {
                                for (BusinessDtl businessDtl : dtlList) {
                                    SendInventory sendInventory = new SendInventory();
                                    sendInventory.setId(new GuidCreator().toString());
                                    sendInventory.setColorId(businessDtl.getColorId());
                                    sendInventory.setQty(Integer.parseInt(businessDtl.getQty() + ""));
                                    sendInventory.setSizeId(businessDtl.getSizeId());
                                    sendInventory.setStyleId(businessDtl.getStyleId());
                                    sendInventory.setStyleName(CacheManager.getStyleNameById(businessDtl.getStyleId()));
                                    sendInventory.setPushsuccess("N");
                                    sendInventory.setBillDate(new Date());
                                    sendInventory.setMessage(mapresulrt.get("moreInfo") + "");
                                    savelist.add(sendInventory);
                                }

                            }
                        } else {
                            for (BusinessDtl businessDtl : dtlList) {
                                SendInventory sendInventory = new SendInventory();
                                sendInventory.setId(new GuidCreator().toString());
                                sendInventory.setColorId(businessDtl.getColorId());
                                sendInventory.setQty(Integer.parseInt(businessDtl.getQty() + ""));
                                sendInventory.setSizeId(businessDtl.getSizeId());
                                sendInventory.setStyleId(businessDtl.getStyleId());
                                sendInventory.setStyleName(CacheManager.getStyleNameById(businessDtl.getStyleId()));
                                sendInventory.setPushsuccess("N");
                                sendInventory.setBillDate(new Date());
                                sendInventory.setMessage("访问接口没成功");
                                savelist.add(sendInventory);
                            }
                        }
                    }else{
                        for (BusinessDtl businessDtl : dtlList) {
                            SendInventory sendInventory = new SendInventory();
                            sendInventory.setId(new GuidCreator().toString());
                            sendInventory.setColorId(businessDtl.getColorId());
                            sendInventory.setQty(Integer.parseInt(businessDtl.getQty() + ""));
                            sendInventory.setSizeId(businessDtl.getSizeId());
                            sendInventory.setStyleId(businessDtl.getStyleId());
                            sendInventory.setStyleName(CacheManager.getStyleNameById(businessDtl.getStyleId()));
                            sendInventory.setPushsuccess("N");
                            sendInventory.setBillDate(new Date());
                            sendInventory.setMessage("访问接口没成功");
                            savelist.add(sendInventory);
                        }
                    }

                    this.sendInventotyDao.doBatchInsert(savelist);
              }
        } catch (IOException e) {
            e.printStackTrace();
        }




    }


    public List<PaymentMessage> findPaymentMessage(String billno){
        String hql="from PaymentMessage t where t.billNo = ?";
        List<PaymentMessage> PaymentMessages = this.sendInventotyDao.find(hql, new Object[]{billno});
        return PaymentMessages;
    }

    public String sendStreamNO(String StreamNO,String billNo){
        Map<String,String> map=new HashMap<String,String>();
        map.put("rfidOrderNo",billNo);
        map.put("logisticsOrderNo",StreamNO);
        String sendJson = JSONUtil.getJsonString4JavaPOJO(map);
        HttpClient httpclient = new HttpClient();
        String message="";
        try {
            String wxshop_Path = PropertyUtil.getValue("wxshop_Path");
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
                    message="推送成功";
                }
                if(errorCode==11008){
                    //获取PaymentMessage
                    String hql="from PaymentMessage t where t.billNo = ?";
                    List<PaymentMessage> PaymentMessages = this.sendInventotyDao.find(hql, new Object[]{billNo});
                    List<SendStreamNOMessage> SendStreamNOMessages=new ArrayList<SendStreamNOMessage>();
                    for(PaymentMessage paymentMessage:PaymentMessages){
                        SendStreamNOMessage sendStreamNOMessage=new SendStreamNOMessage();
                        sendStreamNOMessage.setId(new GuidCreator().toString());
                        sendStreamNOMessage.setAddress(paymentMessage.getAddress());
                        sendStreamNOMessage.setBillNo(paymentMessage.getBillNo());
                        sendStreamNOMessage.setCustomName(paymentMessage.getCustomName());
                        sendStreamNOMessage.setPhone(paymentMessage.getPhone());
                        sendStreamNOMessage.setStreamNO(StreamNO);
                        sendStreamNOMessage.setMessage(mapresulrt.get("moreInfo")+"");
                        sendStreamNOMessage.setPushsuccess("N");
                        SendStreamNOMessages.add(sendStreamNOMessage);
                    }
                    this.sendInventotyDao.doBatchInsert(SendStreamNOMessages);
                    message="推送失败";
                }
                if(errorCode==500){
                   //获取PaymentMessage
                    String hql="from PaymentMessage t where t.billNo = ?";
                    List<PaymentMessage> PaymentMessages = this.sendInventotyDao.find(hql, new Object[]{billNo});
                    List<SendStreamNOMessage> SendStreamNOMessages=new ArrayList<SendStreamNOMessage>();
                    for(PaymentMessage paymentMessage:PaymentMessages){
                        SendStreamNOMessage sendStreamNOMessage=new SendStreamNOMessage();
                        sendStreamNOMessage.setId(new GuidCreator().toString());
                        sendStreamNOMessage.setAddress(paymentMessage.getAddress());
                        sendStreamNOMessage.setBillNo(paymentMessage.getBillNo());
                        sendStreamNOMessage.setCustomName(paymentMessage.getCustomName());
                        sendStreamNOMessage.setPhone(paymentMessage.getPhone());
                        sendStreamNOMessage.setStreamNO(StreamNO);
                        sendStreamNOMessage.setMessage(mapresulrt.get("moreInfo")+"");
                        sendStreamNOMessage.setPushsuccess("N");
                        SendStreamNOMessages.add(sendStreamNOMessage);
                    }
                    this.sendInventotyDao.doBatchInsert(SendStreamNOMessages);
                    message="推送失败";
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }
}
