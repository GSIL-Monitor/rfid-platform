package com.casesoft.dmc.extend.api.web.hub;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.logistics.BillConvertUtil;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.api.web.ApiBaseController;
import com.casesoft.dmc.model.logistics.BillConstant;
import com.casesoft.dmc.model.logistics.TransferOrderBill;
import com.casesoft.dmc.model.logistics.TransferOrderBillDtl;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.logistics.TransferOrderBillService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * Created by yushen on 2017/7/6.
 */

@RequestMapping(value = "/api/hub/transfer", method = {RequestMethod.POST, RequestMethod.GET})
@Controller
@Api
public class TransferBillApiController extends ApiBaseController {

    @Autowired
    private TransferOrderBillService transferOrderBillService;

    @Override
    public String index() {
        return null;
    }

    @RequestMapping(value = "/saveWS")
    @ResponseBody
    public MessageBox save(String transferOrderBillStr, String strDtlList, String userId) throws Exception {
        this.logAllRequestParams();
        try {
            TransferOrderBill transferOrderBill = JSON.parseObject(transferOrderBillStr, TransferOrderBill.class);
            List<TransferOrderBillDtl> transferOrderBillDtlList = JSON.parseArray(strDtlList, TransferOrderBillDtl.class);
            User curUser = CacheManager.getUserById(userId);
            System.out.print(transferOrderBill);
            if (CommonUtil.isBlank(transferOrderBill.getBillNo())) {
                String prefix = BillConstant.BillPrefix.Transfer
                        + CommonUtil.getDateString(new Date(), "yyMMddHHmmssSSS");
                //String billNo = this.transferOrderBillService.findMaxBillNo(prefix);
                transferOrderBill.setId(prefix);
                transferOrderBill.setBillNo(prefix);

            }
            transferOrderBill.setId(transferOrderBill.getBillNo());
            BillConvertUtil.covertToTransferOrderBill(transferOrderBill, transferOrderBillDtlList, curUser);
            this.transferOrderBillService.save(transferOrderBill, transferOrderBillDtlList,null);
            return new MessageBox(true, "保存成功", transferOrderBill.getBillNo());

        } catch (Exception e) {
            e.printStackTrace();
            return new MessageBox(false, e.getMessage());
        }
    }

    @RequestMapping(value = "/listWS")
    @ResponseBody
    public MessageBox list() throws Exception {
        this.logAllRequestParams();
        String userId = this.getReqParam("userId");
        User currentUser = CacheManager.getUserById(userId);
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        if(CommonUtil.isNotBlank(currentUser)){
            if(!currentUser.getOwnerId().equals("1")){
                PropertyFilter filter = new PropertyFilter("EQS_destUnitId_OR_origUnitId", currentUser.getOwnerId());
                filters.add(filter);
            }
        }
        List<TransferOrderBill> transferOrderBills = this.transferOrderBillService.find(filters);
        return new MessageBox(true, "ok", transferOrderBills);
    }

    @RequestMapping(value = "/findBillDtlWS")
    @ResponseBody
    public MessageBox findBillDtl(String billNo) throws Exception {
        this.logAllRequestParams();
        List<TransferOrderBillDtl> transferOrderBillDtls = this.transferOrderBillService.findBillDtlByBillNo(billNo);
        for (TransferOrderBillDtl dtl : transferOrderBillDtls) {
            dtl.setTotPrice(dtl.getPrice() * dtl.getQty());
//            dtl.setTotActPrice(dtl.getActPrice()*dtl.getQty());
        }
        return new MessageBox(true, "ok", transferOrderBillDtls);
    }

    @RequestMapping(value = "/cancelWS")
    @ResponseBody
    public MessageBox cancel(String billNo) throws Exception {
        TransferOrderBill transferOrderBill = this.transferOrderBillService.get("billNo", billNo);
        transferOrderBill.setStatus(BillConstant.BillStatus.Cancel);
        this.transferOrderBillService.update(transferOrderBill);
        return new MessageBox(true, "撤销成功");
    }
}
