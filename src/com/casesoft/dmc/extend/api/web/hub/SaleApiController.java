package com.casesoft.dmc.extend.api.web.hub;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.cache.SpringContextUtil;
import com.casesoft.dmc.controller.shop.CustomerUtil;
import com.casesoft.dmc.controller.shop.SaleBillUtil;
import com.casesoft.dmc.controller.sys.UserUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.api.dto.RespMessage;
import com.casesoft.dmc.extend.api.web.ApiBaseController;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.shop.*;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.model.task.Record;
import com.casesoft.dmc.service.shop.CustomerService;
import com.casesoft.dmc.service.shop.SaleBillService;
import com.casesoft.dmc.service.sys.impl.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WingLi on 2017-01-04.
 */
@Controller
@RequestMapping(value = "/api/hub/sale", method = {RequestMethod.POST, RequestMethod.GET})
@Api(description="门店零售模块接口")
public class SaleApiController extends ApiBaseController {

    @Autowired
    private CustomerService customerService;

    @Resource(name="userService")
    private UserService cashierService;

    @Autowired
    private SaleBillService saleBillService;

    @Override
    public String index() {
        return null;
    }


    @RequestMapping(value = "/listCustomerWS.do")
    @ResponseBody
    public RespMessage listCustomerWS() {
        this.logAllRequestParams();

        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        List<Customer> customerList = this.customerService.find(filters);
        return this.returnApiSuccessInfo("调用成功！",customerList);
    }
/*    public MessageBox listWS() throws Exception {
        this.logAllRequestParams();
        System.out.println(this.getRequest().getCharacterEncoding());
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        List<Customer> customerList = this.customerService.find(filters);
        return this.returnSuccessInfo("调用成功！",customerList);
     }*/
    @RequestMapping(value = "/listCashierWS.do")
    @ResponseBody
    public List<User> listCashierWS(String filter_EQI_isAdmin,String filter_EQS_ownerId) {
        this.logAllRequestParams();
        if(filter_EQI_isAdmin.equals("3")) {
            HallApiController hallApiController = (HallApiController) SpringContextUtil.getBean("hallApiController");
            return hallApiController.listBorrowerWS();
        }

        List<PropertyFilter> filters = new ArrayList<>();

        PropertyFilter filter = new PropertyFilter("EQI_type", "" + 4);
        filters.add(filter);
        if(CommonUtil.isNotBlank(filter_EQS_ownerId)) {
            filter = new PropertyFilter("EQS_ownerId", filter_EQS_ownerId);
            filters.add(filter);
        }

        List<User> userList = this.cashierService.find(filters);
        UserUtil.convertToVo(userList);
        return userList;
    }

    @RequestMapping(value = "/findCashierWS.do")
    @ResponseBody
    public RespMessage findCashierWS(String filter_EQI_isAdmin,String filter_EQS_ownerId) {
        List<User> userList = this.listCashierWS(filter_EQI_isAdmin,filter_EQS_ownerId);
        return this.returnApiSuccessInfo(userList);
    }


    @RequestMapping(value = "/posDataUploadWS.do")
    @ResponseBody
    public MessageBox posDataUploadWS(String order, String orderDtls, String recordList, String deviceId) throws Exception {
        this.logAllRequestParams();

        
        String orderStr = order;
        String recordStr = recordList;
        if(CommonUtil.isBlank(CacheManager.getDeviceByCode(deviceId))){
            return this.returnFailInfo("设备号未注册！");
        }
        SaleBill saleBill = JSON.parseObject(orderStr, SaleBill.class);
        List<SaleBillDtl> saleBillDtlList = JSON.parseArray(orderDtls,
                SaleBillDtl.class);
        if(CommonUtil.isBlank(saleBill)||CommonUtil.isBlank(saleBillDtlList)){
            return this.returnFailInfo("商品信息错误！保存失败!");
        }
        List<Record> recordListDto = null;
        String codes = null;
        if (!CommonUtil.isBlank(recordStr) && !recordStr.equals("[]")) {
            recordListDto = JSON.parseArray(recordStr, Record.class);
            codes = SaleBillUtil.convertSaleBillRecord(saleBill,
                    saleBillDtlList, recordListDto, deviceId);
            if (saleBill.getType() == Constant.ScmConstant.SaleBillType.Outbound
                    && this.saleBillService.checkIsSaled(codes)
                    ) {
                return this.returnFailInfo("不能重复销售!");
            }else if(saleBill.getType() == Constant.ScmConstant.SaleBillType.Inbound
                    && !this.saleBillService.checkIsSaled(codes)){
                return this.returnFailInfo("不能重复退货!");
            }
        }else{
            SaleBillUtil.convertSaleBill(saleBill,saleBillDtlList,deviceId);
        }
        Customer customer = null;
        if (CommonUtil.isNotBlank(saleBill.getClient2Id())
                && saleBill.getGradeRate() != 0d) {// 消费者
            customer = customerService.getCustomerById(saleBill.getClient2Id());
            customer.setUsedGrade(customer.getUsedGrade()//已使用的积分
                    +saleBill.getActGradeValue());
            customer.setGrade(customer.getGrade() +saleBill.getIncreaseGrate()-saleBill.getActGradeValue());//可使用积分
            if(saleBill.getType()==Constant.ScmConstant.SaleBillType.Inbound){
                customer.setBuyQty(customer.getBuyQty() - saleBill.getTotOrderQty());
                customer.setBuyAmount(customer.getBuyAmount()
                        - saleBill.getTotActValue());
            }else{
                customer.setBuyQty(customer.getBuyQty() + saleBill.getTotOrderQty());
                customer.setBuyAmount(customer.getBuyAmount()
                        + saleBill.getTotActValue());
            }

        }
        saleBill.setRecordList(recordListDto);
        saleBill.setDtlList(saleBillDtlList);
        saleBill.setShopId(CacheManager.getDeviceByCode(deviceId).getStorageId());
        boolean autoUpload = Boolean.parseBoolean(PropertyUtil
                .getValue("auto_upload_erp"));

        if (!autoUpload) {
            if(saleBill.getIsRfid() == Constant.ScmConstant.IsRfid.IsRfid){
                this.saleBillService.save(saleBill, customer,codes);
            }else{
                this.saleBillService.save(saleBill, customer);
            }

        } else {
            if(saleBill.getIsRfid() == Constant.ScmConstant.IsRfid.IsRfid){
                this.saleBillService.save(saleBill, customer, autoUpload,codes);
            }else{
                this.saleBillService.saveBill(saleBill, customer, autoUpload);
            }
        }
        return this.returnSuccessInfo("保存成功!",saleBill);
    }

    @RequestMapping(value = "/getRefundBillWS.do")
    @ResponseBody
    public MessageBox getRefundBillWS(String uniqueCode, String billNo) throws Exception {
        this.logAllRequestParams();

        if(CommonUtil.isNotBlank(billNo)) {
            return this.getSaleBillByNoWS(billNo,"");
        }

        Assert.notNull(uniqueCode,"uniqueCode 不能为空！");

        //检查是否有销售记录
        List<SaleBillDtl> dtlList = this.saleBillService.findSaleBillDtlListByUniqueCode(uniqueCode);
        if(CommonUtil.isBlank(dtlList)) {
            return this.returnFailInfo("没有唯一码["+uniqueCode+"]的销售记录");
        }

        if (!this.saleBillService.checkIsSaled("'" + uniqueCode + "'")) {
            return this.returnFailInfo("不能重复退货");
        } else {
            SaleBillDtl dtl = this.saleBillService.findDtl(uniqueCode);
            if(CommonUtil.isBlank(dtl)){
                return this.returnFailInfo("返回失败！");
            }else{
                Style style = CacheManager.getStyleById(dtl.getStyleId());
                dtl.setPrice(null == style? 0.0 : style.getPrice());
                return this.returnSuccessInfo("ok", dtl);
            }
        }
    }

    @RequestMapping(value = "/getSaleBillByNoWS.do")
    @ResponseBody
    public MessageBox getSaleBillByNoWS(String billNo, String uniqueCode) throws Exception {
        this.logAllRequestParams();
        if (CommonUtil.isBlank(billNo)) {
            if (!this.saleBillService.checkIsSaled("'" + uniqueCode + "'")) {
                return this.returnApiFailInfo("不能重复退货");
            }
            SaleBillDtl tempDtl = this.saleBillService.findDtl(uniqueCode);
            if (CommonUtil.isBlank(tempDtl)) {
                return this.returnApiFailInfo("无对应零售单信息");
            }
            if (CommonUtil.isNotBlank(tempDtl.getRefundBillId())) {
                return this.returnApiFailInfo("该商品已退货");
            }
            billNo = tempDtl.getBillNo();
        }
        SaleBill saleBill = this.saleBillService.get("billNo", billNo);
        if (CommonUtil.isBlank(saleBill)) {
            return this.returnFailInfo("无对应零售单信息");
        }
        List<Object> saleBillDtlList = this.saleBillService.findSaledDtlList(billNo);

        if (CommonUtil.isBlank(saleBillDtlList)) {
            return this.returnFailInfo("无可退货商品信息");
        }
        StringBuffer sb = new StringBuffer();
        for (Object arg : saleBillDtlList) {
            sb.append(",'").append(arg.toString()).append("'");
        }

        String canBeRefundUniqueCode =
                this.saleBillService.checkUniqueCodeIsRefund(sb.substring(1));
        if (CommonUtil.isBlank(canBeRefundUniqueCode)) {
            return this.returnFailInfo("该零售单已全部退货");
        } else {
            saleBillDtlList = this.saleBillService.findSaledDtlList(billNo, canBeRefundUniqueCode.split(","));
            for(Object o : saleBillDtlList) {
                SaleBillDtl dtl = (SaleBillDtl) o;
                dtl.setStyleName(CacheManager.getStyleNameById(dtl.getStyleId()));
                dtl.setColorName(CacheManager.getColorNameById(dtl.getColorId()));
                dtl.setSizeName(CacheManager.getSizeNameById(dtl.getSizeId()));
            }
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("saleBill", saleBill);
            resultMap.put("dtlList", saleBillDtlList);
            return this.returnSuccessInfo("获取成功",resultMap);
        }
    }
    /**
     * 喜好分析
     * */
    @RequestMapping(value = "/customerLikeAnakysisWS.do")
    @ResponseBody
    public MessageBox customerLikeAnakysisWS() throws Exception{
        this.logAllRequestParams();
        String code = this.getReqParam("code");
        Customer customer = this.customerService.get("code", code);
        if(CommonUtil.isBlank(customer)){
            return  this.returnFailInfo("用户不存在");
        }
        List<Object[]> reasons=this.saleBillService.findSeason(code);
        List<SaleBillColorAnalysis> colorAnalyses=this.saleBillService.findSaleBillDtlColorTop(code,4);
        List<SaleBillSizeAnalysis> sizeAnalyses=this.saleBillService.findSaleBillDtlSizeTop(code,4);
        List<StyleSubClassAnalysis> styleSubClassAnalyses=this.saleBillService.findSaleBillDtlStyleClass4Top(code,4);
        List<StyleMaterialAnalysis> styleMaterialAnalyses=this.saleBillService.findSaleBillDtlStyleClass8Top(code,4);
        Double refundRate=this.saleBillService.findRefundRate(customer.getId());
        Double associatedRate=this.saleBillService.findAssociatedRate(customer.getId());
        customer.setRefundRate(refundRate);
        customer.setAssociatedRate(associatedRate);
        customer.setColorLike(colorAnalyses);
        customer.setSizeLike(sizeAnalyses);
        customer.setStyleSubClassLike(styleSubClassAnalyses);
        customer.setStyleMateriaLike(styleMaterialAnalyses);
        CustomerUtil.covertProperty(customer,reasons);
        return this.returnSuccessInfo("下载成功！",customer);
    }
    @RequestMapping(value = "/listDtlWS.do")
    @ResponseBody
    public MessageBox listDtlWS() {
        this.logAllRequestParams();
        try {
            PropertyFilter filter = PropertyFilter.buildHqlFromHttpRequest(
                    this.getRequest(), "saleBillDtl");
            List<SaleBillDtl> listDtl = this.saleBillService.findAll(filter);
            SaleBillUtil.convertToVo(listDtl);
           return this.returnSuccessInfo("下载成功", listDtl);
        } catch (Exception e) {
            e.printStackTrace();
            return this.returnFailInfo("下载失败");
        }
     }
     /**
      * @更新销售状态
      * */
     @RequestMapping("/updateIboxPayBillWS")
     @ResponseBody
    public MessageBox updateIboxPayBillWS(Integer payWay,String billNo,Double totActValue,Double backForCash) throws Exception{
         this.logAllRequestParams();
       /*  String pa=this.getReqParam("payWay");
        Integer payWay =Integer.parseInt(this.getReqParam("payWay"));
        String billNo = this.getReqParam("billNo");
        Double totActValue =  Double.parseDouble(this.getReqParam("totActValue"));
        Double backForCash = Double.parseDouble(this.getReqParam("backForCash"));*/
        SaleBill saleBill = this.saleBillService.get("billNo", billNo);
        if(CommonUtil.isNotBlank(saleBill)){
            saleBill.setPayWay(payWay);
            saleBill.setTotActValue(totActValue);
            saleBill.setBackForCash(backForCash);
            saleBill.setStatus(Constant.ScmConstant.SaleBillStatus.Pay);
            this.saleBillService.update(saleBill);
            return this.returnSuccessInfo("销售单更新成功");
        }else{
            return this.returnFailInfo("销售单不存在");
        }

    }
}
