package com.casesoft.dmc.service.wechat;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.core.util.json.JSONUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.shop.CustomerDao;
import com.casesoft.dmc.dao.sys.GuestViewDao;
import com.casesoft.dmc.dao.sys.UnitDao;
import com.casesoft.dmc.dao.wechat.SNSUserInfoDao;
import com.casesoft.dmc.extend.api.web.epay.alipay.config.AlipayConfig;
import com.casesoft.dmc.extend.api.web.epay.alipay.util.httpClient.HttpProtocolHandler;
import com.casesoft.dmc.extend.api.web.epay.alipay.util.httpClient.HttpRequest;
import com.casesoft.dmc.extend.api.web.epay.alipay.util.httpClient.HttpResponse;
import com.casesoft.dmc.extend.api.web.epay.alipay.util.httpClient.HttpResultType;
import com.casesoft.dmc.extend.api.wechat.model.SNSUserInfo;
import com.casesoft.dmc.extend.third.request.BaseService;
import com.casesoft.dmc.model.logistics.ConsignmentBill;
import com.casesoft.dmc.model.product.CustomerPhoto;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.sys.GuestView;
import com.casesoft.dmc.model.sys.Unit;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.NameValuePair;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/13.
 */
@Service
@Transactional
public class SNSUserInfoService extends BaseService<SNSUserInfo, String> {
    @Autowired
    private SNSUserInfoDao sNSUserInfoDao;
    @Autowired
    private UnitDao unitDao;
    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private GuestViewDao guestViewDao;
    @Override
    public Page<SNSUserInfo> findPage(Page<SNSUserInfo> page, List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public void save(SNSUserInfo entity) {
      this.sNSUserInfoDao.saveOrUpdate(entity);
    }

    @Override
    public SNSUserInfo load(String id) {
        return null;
    }

    @Override
    public SNSUserInfo get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<SNSUserInfo> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<SNSUserInfo> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(SNSUserInfo entity) {

    }

    @Override
    public void delete(SNSUserInfo entity) {

    }

    @Override
    public void delete(String id) {

    }

    public void sendtourk(Unit guest, SNSUserInfo sNSUserInfo,HttpServletRequest request) throws Exception {
        HttpProtocolHandler instance = HttpProtocolHandler.getInstance();
        HttpRequest httpRequest=new HttpRequest(HttpResultType.STRING);
        httpRequest.setMethod("post");
        httpRequest.setUrl("http://open.umtrix.com/h/KeObK/face/add_member?av=1");
        NameValuePair[] allnameValuePair = new NameValuePair[5];
        NameValuePair avValuePair=new NameValuePair();
        avValuePair.setName("av");
        avValuePair.setValue("1");
        allnameValuePair[0]=avValuePair;
        NameValuePair three_member_idValuePair=new NameValuePair();
        three_member_idValuePair.setName("three_member_id");
        three_member_idValuePair.setValue(sNSUserInfo.getUnionid());
        allnameValuePair[1]=three_member_idValuePair;
        NameValuePair imgurlValuePair=new NameValuePair();
        //得到图片的路径
        String rootPath = request.getSession().getServletContext().getRealPath("");
        String filePath = rootPath + "product\\photo\\customer";
        String hql="from CustomerPhoto t where t.unionid=?";
        CustomerPhoto customerPhotothis=this.sNSUserInfoDao.findUnique(hql,new Object[]{sNSUserInfo.getUnionid()});
        String src=customerPhotothis.getSrc().replace("/","\\");
        filePath=filePath+src;
        String   myfilePath   =   java.net.URLEncoder.encode(filePath,   "utf-8");
        imgurlValuePair.setName("imgurl");
        imgurlValuePair.setValue(myfilePath);
        allnameValuePair[2]=imgurlValuePair;
        NameValuePair realnameValuePair=new NameValuePair();
        realnameValuePair.setName("realname");
        realnameValuePair.setValue(guest.getName());
        allnameValuePair[3]=realnameValuePair;
        NameValuePair sexValuePair=new NameValuePair();
        sexValuePair.setName("sex");
        if(sNSUserInfo.getSex()==1){
            sexValuePair.setValue("1");
        }
        if(sNSUserInfo.getSex()==2){
            sexValuePair.setValue("0");
        }
        allnameValuePair[4]=sexValuePair;
        httpRequest.setCharset(AlipayConfig.input_charset);
        httpRequest.setParameters(allnameValuePair);
        //获取appid和secret
        String appid = PropertyUtil.getValue("appid");
        String secret = PropertyUtil.getValue("secret");
        //按照悠客的规定进行md5加密
        String md5=secret+appid+"/h/KeObK/face/add_memberav=1";
        String md5str=CommonUtil.encrypt32(md5);
        String baseStr=appid+":"+md5str;
        //获取Authenticate

        //按照悠客的规定进行base64加密
         byte[] textByte = baseStr.getBytes("UTF-8");
        Base64 base64 = new Base64();
        String appidText = base64.encodeToString(textByte);
        httpRequest.setAuthenticate(appidText);
        HttpResponse httpResponse;
        httpResponse = instance.execute(httpRequest, "", "");
        if(CommonUtil.isNotBlank(httpResponse)) {
            String stringResult = httpResponse.getStringResult();
            if (CommonUtil.isNotBlank(stringResult)) {
                Map<String, Object> map = JSONUtil.getMap4Json(stringResult);
                Integer errorCode = Integer.parseInt(map.get("code") + "");

                if (errorCode == 0) {

                    guest.setSendSucess("Y");
                }else{
                    guest.setSendSucess("N");
                }
                this.unitDao.update(guest);
            }
        }

    }
    public void sendCustomertourk(Customer guest, SNSUserInfo sNSUserInfo, HttpServletRequest request) throws Exception {
        HttpProtocolHandler instance = HttpProtocolHandler.getInstance();
        HttpRequest httpRequest=new HttpRequest(HttpResultType.STRING);
        httpRequest.setMethod("post");
        httpRequest.setUrl("http://open.umtrix.com/h/KeObK/face/add_member?av=1");
        NameValuePair[] allnameValuePair = new NameValuePair[5];
        NameValuePair avValuePair=new NameValuePair();
        avValuePair.setName("av");
        avValuePair.setValue("1");
        allnameValuePair[0]=avValuePair;
        NameValuePair three_member_idValuePair=new NameValuePair();
        three_member_idValuePair.setName("three_member_id");
        three_member_idValuePair.setValue(sNSUserInfo.getUnionid());
        allnameValuePair[1]=three_member_idValuePair;
        NameValuePair imgurlValuePair=new NameValuePair();
        //得到图片的路径
        String rootPath = request.getSession().getServletContext().getRealPath("");
        String filePath = rootPath + "product\\photo\\customer";
        String hql="from CustomerPhoto t where t.unionid=?";
        CustomerPhoto customerPhotothis=this.sNSUserInfoDao.findUnique(hql,new Object[]{sNSUserInfo.getUnionid()});
        String src=customerPhotothis.getSrc().replace("/","\\");
        filePath=filePath+src;
        String   myfilePath   =   java.net.URLEncoder.encode(filePath,   "utf-8");
        imgurlValuePair.setName("imgurl");
        imgurlValuePair.setValue(myfilePath);
        allnameValuePair[2]=imgurlValuePair;
        NameValuePair realnameValuePair=new NameValuePair();
        realnameValuePair.setName("realname");
        realnameValuePair.setValue(guest.getName());
        allnameValuePair[3]=realnameValuePair;
        NameValuePair sexValuePair=new NameValuePair();
        sexValuePair.setName("sex");
        if(sNSUserInfo.getSex()==1){
            sexValuePair.setValue("1");
        }
        if(sNSUserInfo.getSex()==2){
            sexValuePair.setValue("0");
        }
        allnameValuePair[4]=sexValuePair;
        httpRequest.setCharset(AlipayConfig.input_charset);
        httpRequest.setParameters(allnameValuePair);
        //获取appid和secret
        String appid = PropertyUtil.getValue("appid");
        String secret = PropertyUtil.getValue("secret");
        //按照悠客的规定进行md5加密
        String md5=secret+appid+"/h/KeObK/face/add_memberav=1";
        String md5str=CommonUtil.encrypt32(md5);
        String baseStr=appid+":"+md5str;
        //获取Authenticate

        //按照悠客的规定进行base64加密
        byte[] textByte = baseStr.getBytes("UTF-8");
        Base64 base64 = new Base64();
        String appidText = base64.encodeToString(textByte);
        httpRequest.setAuthenticate(appidText);
        HttpResponse httpResponse;
        httpResponse = instance.execute(httpRequest, "", "");
        if(CommonUtil.isNotBlank(httpResponse)) {
            String stringResult = httpResponse.getStringResult();
            if (CommonUtil.isNotBlank(stringResult)) {
                Map<String, Object> map = JSONUtil.getMap4Json(stringResult);
                Integer errorCode = Integer.parseInt(map.get("code") + "");

                if (errorCode == 0) {

                    guest.setSendSucess("Y");
                }else{
                    guest.setSendSucess("N");
                }
                this.customerDao.update(guest);
            }
        }

    }
    public void sendNoCustomertourk(HttpServletRequest request) throws Exception {
        if (Boolean.parseBoolean(PropertyUtil.getValue("is_urk"))){
            String hql="from GuestView t where t.sendsucess='N'";
            List<GuestView> GuestViews = this.guestViewDao.find(hql);
            for(int i=0;i<GuestViews.size();i++){
                GuestView guestView=GuestViews.get(i);
                String hqlCustomer="from CustomerPhoto t where t.unionid=?";
                CustomerPhoto customerPhotothis=this.sNSUserInfoDao.findUnique(hqlCustomer,new Object[]{guestView.getUnionid()});
                if(CommonUtil.isNotBlank(customerPhotothis)) {
                    String hqlSNSUserInfo = "from SNSUserInfo t where t.unionid=?";
                    SNSUserInfo sNSUserInfo = this.sNSUserInfoDao.findUnique(hqlSNSUserInfo, new Object[]{guestView.getUnionid()});
                    HttpProtocolHandler instance = HttpProtocolHandler.getInstance();
                    HttpRequest httpRequest = new HttpRequest(HttpResultType.STRING);
                    httpRequest.setMethod("post");
                    httpRequest.setUrl("http://open.umtrix.com/h/KeObK/face/add_member?av=1");
                    NameValuePair[] allnameValuePair = new NameValuePair[5];
                    NameValuePair avValuePair = new NameValuePair();
                    avValuePair.setName("av");
                    avValuePair.setValue("1");
                    allnameValuePair[0] = avValuePair;
                    NameValuePair three_member_idValuePair = new NameValuePair();
                    three_member_idValuePair.setName("three_member_id");
                    three_member_idValuePair.setValue(sNSUserInfo.getUnionid());
                    allnameValuePair[1] = three_member_idValuePair;
                    NameValuePair imgurlValuePair = new NameValuePair();
                    //得到图片的路径
                    String rootPath = request.getSession().getServletContext().getRealPath("");
                    String filePath = rootPath + "/product/photo/customer";
                    String src = customerPhotothis.getSrc().replace("/", "\\");
                    filePath = filePath + src;
                    String myfilePath = java.net.URLEncoder.encode(filePath, "utf-8");
                    imgurlValuePair.setName("imgurl");
                    imgurlValuePair.setValue(myfilePath);
                    allnameValuePair[2] = imgurlValuePair;
                    NameValuePair realnameValuePair = new NameValuePair();
                    realnameValuePair.setName("realname");
                    realnameValuePair.setValue(guestView.getName());
                    allnameValuePair[3] = realnameValuePair;
                    NameValuePair sexValuePair = new NameValuePair();
                    sexValuePair.setName("sex");
                    if (sNSUserInfo.getSex() == 1) {
                        sexValuePair.setValue("1");
                    }
                    if (sNSUserInfo.getSex() == 2) {
                        sexValuePair.setValue("0");
                    }
                    allnameValuePair[4] = sexValuePair;
                    httpRequest.setCharset(AlipayConfig.input_charset);
                    httpRequest.setParameters(allnameValuePair);
                    //获取appid和secret
                    String appid = PropertyUtil.getValue("appid");
                    String secret = PropertyUtil.getValue("secret");
                    //按照悠客的规定进行md5加密
                    String md5 = secret + appid + "/h/KeObK/face/add_memberav=1";
                    String md5str = CommonUtil.encrypt32(md5);
                    String baseStr = appid + ":" + md5str;
                    //获取Authenticate

                    //按照悠客的规定进行base64加密
                    byte[] textByte = baseStr.getBytes("UTF-8");
                    Base64 base64 = new Base64();
                    String appidText = base64.encodeToString(textByte);
                    httpRequest.setAuthenticate(appidText);
                    HttpResponse httpResponse;
                    httpResponse = instance.execute(httpRequest, "", "");
                    if (CommonUtil.isNotBlank(httpResponse)) {
                        String stringResult = httpResponse.getStringResult();
                        if (CommonUtil.isNotBlank(stringResult)) {
                            Map<String, Object> map = JSONUtil.getMap4Json(stringResult);
                            Integer errorCode = Integer.parseInt(map.get("code") + "");

                            if (errorCode == 0) {
                                Unit unitById = CacheManager.getUnitById(guestView.getId());
                                if (CommonUtil.isNotBlank(unitById)) {
                                    unitById.setSendSucess("Y");
                                    this.unitDao.update(unitById);
                                }
                                Customer customerById = CacheManager.getCustomerById(guestView.getId());
                                if (CommonUtil.isNotBlank(customerById)) {
                                    customerById.setSendSucess("Y");
                                    this.customerDao.update(customerById);
                                }

                            } else {
                                Unit unitById = CacheManager.getUnitById(guestView.getId());
                                if (CommonUtil.isNotBlank(unitById)) {
                                    unitById.setSendSucess("N");
                                    this.unitDao.update(unitById);
                                }
                                Customer customerById = CacheManager.getCustomerById(guestView.getId());
                                if (CommonUtil.isNotBlank(customerById)) {
                                    customerById.setSendSucess("N");
                                    this.customerDao.update(customerById);
                                }
                            }

                        }
                    }
                }

            }
        }



    }




}
