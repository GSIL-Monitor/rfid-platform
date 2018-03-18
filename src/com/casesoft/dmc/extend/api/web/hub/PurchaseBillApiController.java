package com.casesoft.dmc.extend.api.web.hub;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.logistics.BillConvertUtil;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.api.web.ApiBaseController;
import com.casesoft.dmc.model.logistics.BillConstant;
import com.casesoft.dmc.model.logistics.PurchaseOrderBill;
import com.casesoft.dmc.model.logistics.PurchaseOrderBillDtl;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.logistics.PurchaseOrderBillService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * Created by Alvin on 2017/6/22.
 */

@Controller
@RequestMapping( value="/api/hub/purchase", method = {RequestMethod.POST, RequestMethod.GET})
@Api
public class PurchaseBillApiController extends ApiBaseController{

    @Autowired
    private PurchaseOrderBillService purchaseOrderBillService;
    @Override
    public String index() {
        return null;
    }

    @RequestMapping(value="/saveWS")
    @ResponseBody
    public MessageBox save(String purchaseBillStr, String strDtlList,String userId) throws Exception {
        this.logAllRequestParams();
        PurchaseOrderBill purchaseOrderBill = JSON.parseObject(purchaseBillStr,PurchaseOrderBill.class);
        List<PurchaseOrderBillDtl> purchaseOrderBillDtlList = JSON.parseArray(strDtlList,PurchaseOrderBillDtl.class);
        try{
            String prefix = BillConstant.BillPrefix.purchase
                    + CommonUtil.getDateString(new Date(), "yyMMddHHmmssSSS");
            //String billNo = this.purchaseOrderBillService.findMaxBillNo(prefix);
            purchaseOrderBill.setId(prefix);
            purchaseOrderBill.setBillNo(prefix);
            User curUser = CacheManager.getUserById(userId);
            BillConvertUtil.covertToPurchaseBill(purchaseOrderBill, purchaseOrderBillDtlList,curUser);
            this.purchaseOrderBillService.save(purchaseOrderBill, purchaseOrderBillDtlList);
            System.out.println(purchaseOrderBill.getBillNo());
            return new MessageBox(true,"保存成功", purchaseOrderBill.getBillNo());

        }catch (Exception e){
            e.printStackTrace();
            return new MessageBox(false,e.getMessage());
        }
    }

    @RequestMapping(value="/listWS")
    @ResponseBody
    public MessageBox list() throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        String userId = this.getReqParam("userId");
        User curUser = CacheManager.getUserById(userId);
        if(CommonUtil.isNotBlank(curUser)){
            if(!curUser.getId().equals("admin")){
                PropertyFilter filter = new PropertyFilter("EQS_ownerId", curUser.getOwnerId());
                filters.add(filter);
            }
        }
        List<PurchaseOrderBill> purchaseOrderBillList = this.purchaseOrderBillService.find(filters);
        return new MessageBox(true,"ok", purchaseOrderBillList);
    }

    @RequestMapping(value="/findBillDtlWS")
    @ResponseBody
    public MessageBox findBillDtl(String billNo) throws Exception {
        this.logAllRequestParams();
        List<PurchaseOrderBillDtl> purchaseOrderBillDtls = this.purchaseOrderBillService.findBillDtlByBillNo(billNo);
        for(PurchaseOrderBillDtl dtl : purchaseOrderBillDtls){
            dtl.setTotPrice(dtl.getPrice()*dtl.getQty());
            dtl.setTotActPrice(dtl.getActPrice()*dtl.getQty());
        }
        return new MessageBox(true,"ok", purchaseOrderBillDtls);
    }

    @RequestMapping(value="/cancelWS")
    @ResponseBody
    public MessageBox cancel(String billNo) throws Exception {
        PurchaseOrderBill purchaseOrderBill = this.purchaseOrderBillService.get("billNo",billNo);
        purchaseOrderBill.setStatus(BillConstant.BillStatus.Cancel);
        this.purchaseOrderBillService.update(purchaseOrderBill);
        return new MessageBox(true,"撤销成功");
    }



}
