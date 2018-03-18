package com.casesoft.dmc.extend.api.web.hub;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.logistics.BillConvertUtil;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.api.web.ApiBaseController;
import com.casesoft.dmc.model.logistics.BillConstant;
import com.casesoft.dmc.model.logistics.BillRecord;
import com.casesoft.dmc.model.logistics.SaleOrderBill;
import com.casesoft.dmc.model.logistics.SaleOrderBillDtl;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.logistics.SaleOrderBillService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by koudepei on 2017/6/2.
 *
 * 销售订单接口
 *
 */

@Controller
@RequestMapping( value="/api/hub/saleOrder", method = {RequestMethod.POST, RequestMethod.GET})
@Api
public class SaleOrderBillApiController extends ApiBaseController{

    @Autowired
    private SaleOrderBillService saleOrderBillService;
    @Override
    public String index() {
        return null;
    }

    @RequestMapping(value="/saveWS")
    @ResponseBody
    public MessageBox save(String saleOrderBillStr, String strDtlList,String userId) throws Exception {
        this.logAllRequestParams();
        SaleOrderBill saleOrderBill = JSON.parseObject(saleOrderBillStr,SaleOrderBill.class);
        List<SaleOrderBillDtl> saleOrderBillDtlList = JSON.parseArray(strDtlList,SaleOrderBillDtl.class);
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
            this.saleOrderBillService.save(saleOrderBill,saleOrderBillDtlList);
            return new MessageBox(true,"保存成功",saleOrderBill.getBillNo());

        }catch (Exception e){
            e.printStackTrace();
            return new MessageBox(false,e.getMessage());
        }
    }

    @RequestMapping(value="/listWS")
    @ResponseBody
    public MessageBox list() throws Exception {
        this.logAllRequestParams();
        String userId = this.getReqParam("userId");
        User curUser = CacheManager.getUserById(userId);
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        if(CommonUtil.isNotBlank(curUser)){
            if(!curUser.getId().equals("admin")){
                PropertyFilter filter = new PropertyFilter("EQS_ownerId", curUser.getOwnerId());
                filters.add(filter);
            }
        }
        List<SaleOrderBill> saleOrderBillList = this.saleOrderBillService.find(filters);
        return new MessageBox(true,"ok",saleOrderBillList);
    }

    @RequestMapping(value="/findBillDtlWS")
    @ResponseBody
    public MessageBox findBillDtl(String billNo) throws Exception {
        this.logAllRequestParams();
        List<SaleOrderBillDtl> saleOrderBillDtls = this.saleOrderBillService.findBillDtlByBillNo(billNo);
        List<BillRecord> billRecordList = this.saleOrderBillService.getBillRecod(billNo);
        Map<String,String> codeMap = new HashMap<>();
        for(BillRecord r : billRecordList){
            if(codeMap.containsKey(r.getSku())){
                String code = codeMap.get(r.getSku());
                code += ","+r.getCode();
                codeMap.put(r.getSku(),code);
            }else{
                codeMap.put(r.getSku(),r.getCode());
            }
        }
        for (int i = 0; i < saleOrderBillDtls.size(); i++) {
            SaleOrderBillDtl dtl = saleOrderBillDtls.get(i);
            dtl.setStyleName(CacheManager.getStyleNameById(dtl.getStyleId()));
            dtl.setColorName(CacheManager.getColorNameById(dtl.getColorId()));
            dtl.setSizeName(CacheManager.getSizeNameById(dtl.getSizeId()));
            if(codeMap.containsKey(dtl.getSku())){
                dtl.setUniqueCodes(codeMap.get(dtl.getSku()));
            }
        }
        return new MessageBox(true,"ok",saleOrderBillDtls);
    }

    @RequestMapping(value="/cancelWS")
    @ResponseBody
    public MessageBox cancel(String billNo) throws Exception {
        SaleOrderBill saleOrderBill = this.saleOrderBillService.get("billNo",billNo);
        saleOrderBill.setStatus(BillConstant.BillStatus.Cancel);
        this.saleOrderBillService.update(saleOrderBill);
        return new MessageBox(true,"撤销成功");
    }



}
