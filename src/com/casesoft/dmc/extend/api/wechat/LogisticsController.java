package com.casesoft.dmc.extend.api.wechat;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.logistics.BillConvertUtil;
import com.casesoft.dmc.controller.stock.StockUtil;
import com.casesoft.dmc.controller.task.TaskUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.core.util.secret.EpcSecretUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.dao.search.DetailStockDao;
import com.casesoft.dmc.extend.api.web.ApiBaseController;
import com.casesoft.dmc.extend.api.web.epay.alipay.config.AlipayConfig;
import com.casesoft.dmc.extend.api.web.epay.alipay.util.httpClient.HttpProtocolHandler;
import com.casesoft.dmc.extend.api.web.epay.alipay.util.httpClient.HttpRequest;
import com.casesoft.dmc.extend.api.web.epay.alipay.util.httpClient.HttpResponse;
import com.casesoft.dmc.extend.api.web.epay.alipay.util.httpClient.HttpResultType;
import com.casesoft.dmc.extend.api.wechat.model.SNSUserInfo;
import com.casesoft.dmc.model.logistics.BillConstant;
import com.casesoft.dmc.model.logistics.SaleOrderBill;
import com.casesoft.dmc.model.logistics.SaleOrderBillDtl;
import com.casesoft.dmc.model.product.CustomerPhoto;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.stock.EpcStock;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.model.tag.Epc;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.Record;
import com.casesoft.dmc.service.logistics.SaleOrderBillService;
import com.casesoft.dmc.service.shop.CustomerService;
import com.casesoft.dmc.service.stock.EpcStockService;
import com.casesoft.dmc.service.sys.GuestService;
import com.casesoft.dmc.service.sys.impl.UnitService;
import com.casesoft.dmc.service.task.TaskService;
import com.casesoft.dmc.service.wechat.SNSUserInfoService;
import io.swagger.annotations.Api;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.NameValuePair;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/11/29.
 */
@Controller
@RequestMapping(value = "/api/wechat/logistics", method = {RequestMethod.POST, RequestMethod.GET})
@Api(description="微信销售接口")
public class LogisticsController extends ApiBaseController {
    @Autowired
    DetailStockDao detailStockDao;
    @Autowired
    private TaskService taskService;
    @Autowired
    private EpcStockService epcStockService;
    @Autowired
    private GuestService guestService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private SaleOrderBillService saleOrderBillService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private SNSUserInfoService sNSUserInfoService;
    @Override
    public String index() {
        return null;
    }
    @RequestMapping(value = "/ScanCode.do")
    @ResponseBody
    public MessageBox ScanCode(String warehId, String code, Integer type){
        try {
            if (code.length() != 13) {
                String epcCode = code.toUpperCase();
                code = EpcSecretUtil.decodeEpc(epcCode).substring(0, 13);
            }
            EpcStock epcStock;
            if (Constant.TaskType.Outbound == type) {
                epcStock = this.epcStockService.findEpcInCode(warehId, code);
            } else {
                epcStock = this.epcStockService.findEpcNotInCode(warehId, code);
            }
            if (CommonUtil.isNotBlank(epcStock)) {
                StockUtil.convertEpcStock(epcStock);
                Style style = CacheManager.getStyleById(epcStock.getStyleId());
                if (CommonUtil.isNotBlank(style)) {
                    epcStock.setStyleName(style.getStyleName());
                }
                return new MessageBox(true, "", epcStock);
            } else {
                if (Constant.TaskType.Outbound == type) {
                    return new MessageBox(false, "唯一码:" + code + "不能出库");
                } else {
                    return new MessageBox(false, "唯一码:" + code + "不能入库");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new MessageBox(false, e.getMessage());
        }


    }
    @RequestMapping(value="/saveWS")
    @ResponseBody
    public MessageBox save(String saleOrderBillStr, String strDtlList,String strEpcList,String userId) throws Exception {
        this.logAllRequestParams();
        SaleOrderBill saleOrderBill = JSON.parseObject(saleOrderBillStr,SaleOrderBill.class);
        List<SaleOrderBillDtl> saleOrderBillDtlList = JSON.parseArray(strDtlList,SaleOrderBillDtl.class);
        List<Epc> epcList = JSON.parseArray(strEpcList, Epc.class);
        try{
            if(CommonUtil.isBlank(saleOrderBill.getBillNo())){
                String prefix = BillConstant.BillPrefix.saleOrder
                        + CommonUtil.getDateString(new Date(), "yyMMddHHmmssSSS");
                //String billNo = this.saleOrderBillService.findMaxBillNo(prefix);
                saleOrderBill.setId(prefix);
                saleOrderBill.setBillNo(prefix);
            }
            saleOrderBill.setId(saleOrderBill.getBillNo());
            User curUser = CacheManager.getUserById(userId);
            BillConvertUtil.covertToSaleOrderBill(saleOrderBill,saleOrderBillDtlList,curUser);
            Business business = BillConvertUtil.covertToSaleOrderBusinessOut(saleOrderBill, saleOrderBillDtlList, epcList, curUser);
            this.saleOrderBillService.saveweChat(saleOrderBill,saleOrderBillDtlList,business,epcList);
            return new MessageBox(true,"保存成功",saleOrderBill.getBillNo());

        }catch (Exception e){
            e.printStackTrace();
            return new MessageBox(false,e.getMessage());
        }
    }

    @RequestMapping(value = "/saveNewCustomer")
    @ResponseBody
    public MessageBox saveNewCustomer(String unit,String user,String SNSUserInfo,HttpServletRequest request) throws Exception {
        Unit entity = JSON.parseObject(unit, Unit.class);
        User Users = JSON.parseObject(user, User.class);
        SNSUserInfo sNSUserInfo=JSON.parseObject(SNSUserInfo, SNSUserInfo.class);
        if(entity.getType()!=6) {
            Unit guest = this.guestService.getGuestById(entity.getId());
            if (CommonUtil.isBlank(guest)) {
                guest = new Unit();
                String newId = this.guestService.findMaxNo(Constant.GuestPrefix.DefaultGuest);
                guest.setId(newId);
                guest.setCode(newId);
                guest.setCreateTime(new Date());
                guest.setCreatorId(Users.getId());
                guest.setStoredValue(0.0);
                guest.setOwingValue(0.0);
            }
            guest.setName(entity.getName());
            guest.setStatus(entity.getStatus());
            guest.setSex(entity.getSex());
            guest.setType(entity.getType());
            guest.setLinkman(Users.getId());
            guest.setTel(entity.getTel());
            guest.setEmail(entity.getEmail());
            guest.setBankCode(entity.getBankCode());//变更为bankCode
            guest.setBankAccount(entity.getBankAccount());//变更为BankAccount
            guest.setDepositBank(entity.getDepositBank());
            guest.setBirth(entity.getBirth());
            guest.setPhone(entity.getPhone());

            guest.setOwnerId(Users.getOwnerId());
            guest.setFax(entity.getFax());
            guest.setDiscount(entity.getDiscount());
            guest.setAddress(entity.getAddress());
            guest.setProvince(entity.getProvince());
            guest.setCity(entity.getCity());
            guest.setAreaId(entity.getAreaId());//原为County
            guest.setRemark(entity.getRemark());
            guest.setUpdaterId(Users.getId());
            guest.setUpdateTime(new Date());
            guest.setUnionid(sNSUserInfo.getUnionid());
		/*	guest.setStoredValue(entity.getStoredValue());
			guest.setStoreDate(entity.getStoreDate());*/
            try {
                this.guestService.save(guest);
                this.sNSUserInfoService.save(sNSUserInfo);
                if (Boolean.parseBoolean(PropertyUtil.getValue("is_urk"))){
                    //推送会员信息给悠客
                   this.sNSUserInfoService.sendtourk(entity,sNSUserInfo,request);

                }
                CacheManager.refreshUnitCache();
                return returnSuccessInfo("保存成功");
            } catch (Exception e) {
                e.printStackTrace();
                return returnFailInfo("保存失败");
            }
        } else {
            Customer guest =this.customerService.getById(entity.getId());
            if(CommonUtil.isBlank(guest)){
                guest =new Customer();
                String maxNo =this.customerService.getMaxNo(Constant.ScmConstant.CodePrefix.Customer);
                guest.setId(maxNo);
                guest.setCode(maxNo);
                guest.setCreateTime(new Date());
                guest.setCreatorId(Users.getId());
                guest.setStoredValue(0.0);
                guest.setOwingValue(0.0);
            }
            guest.setName(entity.getName());
            guest.setStatus(entity.getStatus());
            guest.setSex(entity.getSex());
            guest.setType(entity.getType());
            guest.setLinkman(Users.getId());
            guest.setTel(entity.getTel());
            guest.setEmail(entity.getEmail());
            guest.setBankCode(entity.getBankCode());
            guest.setBankAccount(entity.getBankAccount());
            guest.setDepositBank(entity.getDepositBank());
            guest.setBirth(entity.getBirth());
            guest.setPhone(entity.getPhone());
            guest.setOwnerId(Users.getOwnerId());
            guest.setFax(entity.getFax());
            guest.setDiscount(entity.getDiscount());
            guest.setAddress(entity.getAddress());
            guest.setProvince(entity.getProvince());
            guest.setCity(entity.getCity());
            guest.setAreaId(entity.getAreaId());//原为County
            guest.setRemark(entity.getRemark());
            guest.setUpdaterId(Users.getId());
            guest.setUpdateTime(new Date());
            guest.setUnionid(sNSUserInfo.getUnionid());
		/*	guest.setStoredValue(entity.getStoredValue());
			guest.setStoreDate(entity.getStoreDate());*/

            try{
                this.customerService.save(guest);
                this.sNSUserInfoService.save(sNSUserInfo);
                if (Boolean.parseBoolean(PropertyUtil.getValue("is_urk"))){
                    //推送会员信息给悠客
                    this.sNSUserInfoService.sendCustomertourk(guest,sNSUserInfo,request);

                }
                CacheManager.refreshUnitCache();
                CacheManager.refreshCustomer();
                return returnSuccessInfo("保存成功");
            } catch (Exception e){
                e.printStackTrace();
                return returnFailInfo("保存失败");
            }
        }
    }
    @RequestMapping(value = "/save")
    @ResponseBody
    public MessageBox save(String unit,String userId) throws Exception {
        Unit entity = JSON.parseObject(unit, Unit.class);
        if(entity.getType()!=6) {
            Unit guest = this.guestService.getGuestById(entity.getId());
            if (CommonUtil.isBlank(guest)) {
                guest = new Unit();
                String newId = this.guestService.findMaxNo(Constant.GuestPrefix.DefaultGuest);
                guest.setId(newId);
                guest.setCode(newId);
                guest.setCreateTime(new Date());
                guest.setCreatorId(userId);
                guest.setStoredValue(0.0);
                guest.setOwingValue(0.0);
            }
            guest.setName(entity.getName());
            guest.setStatus(entity.getStatus());
            guest.setSex(entity.getSex());
            guest.setType(entity.getType());
            guest.setLinkman(entity.getLinkman());
            guest.setTel(entity.getTel());
            guest.setEmail(entity.getEmail());
            guest.setBankCode(entity.getBankCode());//变更为bankCode
            guest.setBankAccount(entity.getBankAccount());//变更为BankAccount
            guest.setDepositBank(entity.getDepositBank());
            guest.setBirth(entity.getBirth());
            guest.setPhone(entity.getPhone());
            guest.setOwnerId(entity.getOwnerId());
            guest.setFax(entity.getFax());
            guest.setDiscount(entity.getDiscount());
            guest.setAddress(entity.getAddress());
            guest.setProvince(entity.getProvince());
            guest.setCity(entity.getCity());
            guest.setAreaId(entity.getAreaId());//原为County
            guest.setRemark(entity.getRemark());
            guest.setUpdaterId(userId);
            guest.setUpdateTime(new Date());
		/*	guest.setStoredValue(entity.getStoredValue());
			guest.setStoreDate(entity.getStoreDate());*/
            try {
                this.guestService.save(guest);
                CacheManager.refreshUnitCache();
                return returnSuccessInfo("保存成功");
            } catch (Exception e) {
                e.printStackTrace();
                return returnFailInfo("保存失败");
            }
        } else {
            Customer guest =this.customerService.getById(entity.getId());
            if(CommonUtil.isBlank(guest)){
                guest =new Customer();
                String maxNo =this.customerService.getMaxNo(Constant.ScmConstant.CodePrefix.Customer);
                guest.setId(maxNo);
                guest.setCode(maxNo);
                guest.setCreateTime(new Date());
                guest.setCreatorId(userId);
                guest.setStoredValue(0.0);
                guest.setOwingValue(0.0);
            }
            guest.setName(entity.getName());
            guest.setStatus(entity.getStatus());
            guest.setSex(entity.getSex());
            guest.setType(entity.getType());
            guest.setLinkman(entity.getLinkman());
            guest.setTel(entity.getTel());
            guest.setEmail(entity.getEmail());
            guest.setBankCode(entity.getBankCode());
            guest.setBankAccount(entity.getBankAccount());
            guest.setDepositBank(entity.getDepositBank());
            guest.setBirth(entity.getBirth());
            guest.setPhone(entity.getPhone());
            guest.setOwnerId(entity.getOwnerId());
            guest.setFax(entity.getFax());
            guest.setDiscount(entity.getDiscount());
            guest.setAddress(entity.getAddress());
            guest.setProvince(entity.getProvince());
            guest.setCity(entity.getCity());
            guest.setAreaId(entity.getAreaId());//原为County
            guest.setRemark(entity.getRemark());
            guest.setUpdaterId(this.getCurrentUser().getId());
            guest.setUpdateTime(new Date());
		/*	guest.setStoredValue(entity.getStoredValue());
			guest.setStoreDate(entity.getStoreDate());*/

            try{
                this.customerService.save(guest);
                CacheManager.refreshUnitCache();
                CacheManager.refreshCustomer();
                return returnSuccessInfo("保存成功");
            } catch (Exception e){
                e.printStackTrace();
                return returnFailInfo("保存失败");
            }
        }
    }
    @RequestMapping(value = "/findunitList")
    @ResponseBody
    public MessageBox findunitList(String types){
        //String hql="from Unit where type in ("+types+")";

        List<Unit> list=this.unitService.findbytype(types);
        return returnSuccessInfo("保存成功",list);
    }

    @Test
    public void Test(){
        HttpProtocolHandler instance7 = HttpProtocolHandler.getInstance();
        HttpRequest httpRequest7=new HttpRequest(HttpResultType.STRING);
        httpRequest7.setMethod("post");
        httpRequest7.setUrl("http://open.umtrix.com/h/KeObK/face/add_member?av=1");
        NameValuePair[] allnameValuePair = new NameValuePair[5];
        NameValuePair avValuePair=new NameValuePair();
        avValuePair.setName("av");
        avValuePair.setValue("1");
        allnameValuePair[0]=avValuePair;
        NameValuePair three_member_idValuePair=new NameValuePair();
        three_member_idValuePair.setName("three_member_id");
        three_member_idValuePair.setValue("1");
        allnameValuePair[1]=three_member_idValuePair;
        NameValuePair imgurlValuePair=new NameValuePair();
        //得到图片的路径

        imgurlValuePair.setName("imgurl");
        imgurlValuePair.setValue("http%3a%2f%2fwww.ulucu.com%2freceive");
        allnameValuePair[2]=imgurlValuePair;
        NameValuePair realnameValuePair=new NameValuePair();
        realnameValuePair.setName("realname");
        realnameValuePair.setValue("saber");
        allnameValuePair[3]=realnameValuePair;
        NameValuePair sexValuePair=new NameValuePair();
        sexValuePair.setName("sex");

            sexValuePair.setValue("1");

        allnameValuePair[4]=sexValuePair;
        httpRequest7.setCharset(AlipayConfig.input_charset);
        httpRequest7.setParameters(allnameValuePair);
        //获取Authenticate
        try {
            String a="c25faa2011ffef85f12e10177/h/KeObK/face/add_memberav=1";

            String md5str=CommonUtil.encrypt32(a);
            String appid="10177:"+md5str;
        byte[] textByte = appid.getBytes("UTF-8");
        Base64 base64 = new Base64();
        //编码
        String appidText = base64.encodeToString(textByte);
        httpRequest7.setAuthenticate(appidText);
        HttpResponse httpResponse;


            httpResponse = instance7.execute(httpRequest7, "", "");
            System.out.println(httpResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }





}
