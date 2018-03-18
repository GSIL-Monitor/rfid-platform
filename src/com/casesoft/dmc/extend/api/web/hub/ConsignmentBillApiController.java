package com.casesoft.dmc.extend.api.web.hub;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.logistics.BillConvertUtil;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.api.web.ApiBaseController;
import com.casesoft.dmc.model.logistics.*;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.logistics.ConsignmentBillService;
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
 * Created by admin on 2017/8/21.
 */
@Controller
@RequestMapping( value="/api/hub/consignmentBill", method = {RequestMethod.POST, RequestMethod.GET})
@Api
public class ConsignmentBillApiController extends ApiBaseController {

    @Autowired
    private ConsignmentBillService consignmentBillService;
    @Autowired
    private SaleOrderBillService saleOrderBillService;

    @RequestMapping("/saveWS")
    @ResponseBody
    public MessageBox save(String bill, String strDtlList,String userId) throws Exception {
        this.logAllRequestParams();
        try {
            ConsignmentBill consignmentBill = JSON.parseObject(bill, ConsignmentBill.class);
            List<ConsignmentBillDtl> consignmentBillDtls = JSON.parseArray(strDtlList, ConsignmentBillDtl.class);


            if (CommonUtil.isBlank(consignmentBill.getBillNo())) {
                String prefix = BillConstant.BillPrefix.Consignment + CommonUtil.getDateString(new Date(), "yyMMddHHmmssSSS");
                //String billNo = this.consignmentBillService.findMaxBillNo(prefix);
                consignmentBill.setBillNo(prefix);
                consignmentBill.setId(prefix);
            } else {
                consignmentBill.setId(consignmentBill.getBillNo());
                for (ConsignmentBillDtl sdtl : consignmentBillDtls) {
                    if (CommonUtil.isNotBlank(sdtl.getId())) {
                        sdtl.setBillNo(consignmentBill.getBillNo());
                        sdtl.setBillId(consignmentBill.getBillNo());
                    }
                }
            }
            User user = CacheManager.getUserById(userId);
            BillConvertUtil.convertToConsignmentBillBill(consignmentBill, consignmentBillDtls, user);

                this.consignmentBillService.saveReturnBatch(consignmentBill, consignmentBillDtls);
                return returnSuccessInfo("保存成功", consignmentBill.getBillNo());
        } catch (Exception e) {
            e.printStackTrace();
            return returnFailInfo("保存失败", e.getMessage());
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
        List<ConsignmentBill> consignmentBills = this.consignmentBillService.find(filters);
        return new MessageBox(true,"ok",consignmentBills);
    }

    @RequestMapping(value="/findBillDtlWS")
    @ResponseBody
    public MessageBox findBillDtl(String billNo) throws Exception {
        this.logAllRequestParams();
        List<ConsignmentBillDtl> consignmentBillDtls = this.consignmentBillService.findBillDtlByBillNo(billNo);
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
        for (int i = 0; i < consignmentBillDtls.size(); i++) {
            ConsignmentBillDtl dtl = consignmentBillDtls.get(i);
            dtl.setStyleName(CacheManager.getStyleNameById(dtl.getStyleId()));
            dtl.setColorName(CacheManager.getColorNameById(dtl.getColorId()));
            dtl.setSizeName(CacheManager.getSizeNameById(dtl.getSizeId()));
            if(codeMap.containsKey(dtl.getSku())){
                dtl.setUniqueCodes(codeMap.get(dtl.getSku()));
            }
        }
        return new MessageBox(true,"ok",consignmentBillDtls);
    }

    @Override
    public String index() {
        return null;
    }
}
