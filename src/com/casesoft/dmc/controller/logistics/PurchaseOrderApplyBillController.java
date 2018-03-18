package com.casesoft.dmc.controller.logistics;

/**
 * Created by Administrator on 2017/7/31.
 */

import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.ILogisticsBillController;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.logistics.BillConstant;
import com.casesoft.dmc.model.logistics.MonthAccountStatement;
import com.casesoft.dmc.model.logistics.PurchaseOrderBill;
import com.casesoft.dmc.service.logistics.MonthAccountStatementService;
import com.casesoft.dmc.service.logistics.PurchaseOrderBillService;
import com.casesoft.dmc.service.tag.InitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("logistics/purchaseApply")
public class PurchaseOrderApplyBillController extends BaseController implements ILogisticsBillController<PurchaseOrderBill> {
    @Autowired
    private PurchaseOrderBillService purchaseOrderBillService;
    @Autowired
    private InitService initService;

    @Autowired
    private MonthAccountStatementService monthAccountStatementService;
    @Override
    public Page<PurchaseOrderBill> findPage(Page<PurchaseOrderBill> page) throws Exception {
        return null;
    }

    @Override
    public List<PurchaseOrderBill> list() throws Exception {
        return null;
    }

    @Override
    public MessageBox save(String bill, String strDtlList, String userId) throws Exception {
        return null;
    }

    @Override
    public ModelAndView add() throws Exception {
        return null;
    }

    @Override
    public ModelAndView edit(String billNo) throws Exception {
        return null;
    }

    @Override
    public MessageBox check(String billNo) throws Exception {
        return null;
    }

    @Override
    public MessageBox end(String billNo) throws Exception {
        return null;
    }
    @RequestMapping(value="/cancel")
    @ResponseBody
    @Override
    public MessageBox cancel(String billNo) throws Exception {
        PurchaseOrderBill purchaseOrderBill = this.purchaseOrderBillService.get("billNo",billNo);
        purchaseOrderBill.setStatus(BillConstant.BillStatus.Cancel);
        String origUnitId = purchaseOrderBill.getOrigUnitId();
        Date billDate = purchaseOrderBill.getBillDate();
        String dateString = CommonUtil.getDateString(billDate, "yyyy-MM");
        MonthAccountStatement monthAccountStatement = this.monthAccountStatementService.getStatementbymonthaAndunitid(dateString, origUnitId);
        //this.monthAccountStatementService.cancel(monthAccountStatement,purchaseOrderBill);
        this.purchaseOrderBillService.cancelApply(purchaseOrderBill,monthAccountStatement);

        return new MessageBox(true,"撤销成功");
    }

    @Override
    public MessageBox convert(String strDtlList, String recordList) throws Exception {
        return null;
    }
    @RequestMapping(value = "/index")
    @Override
    public String index() {
        return "/views/logistics/purchaseOrderApplyBill";
    }
}
