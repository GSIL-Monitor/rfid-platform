package com.casesoft.dmc.controller.logistics;

import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.ILogisticsBillController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.logistics.BillConstant;
import com.casesoft.dmc.model.logistics.PaymentGatheringBill;
import com.casesoft.dmc.model.shop.payDetail;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.logistics.PaymentGatheringBillService;
import com.casesoft.dmc.service.shop.payDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by yushen on 2017/7/10.
 */
@Controller
@RequestMapping("/logistics/paymentGatheringBill")
public class PaymentGatheringBillController extends BaseController implements ILogisticsBillController<PaymentGatheringBill> {
    @Autowired
    private PaymentGatheringBillService paymentGatheringBillService;
    @Autowired
    private com.casesoft.dmc.service.shop.payDetailService payDetailService;

    @RequestMapping("/index")
    @Override
    public String index() {
        return "/views/logistics/paymentGatheringBill";
    }

    @RequestMapping("/page")
    @Override
    public Page<PaymentGatheringBill> findPage(Page<PaymentGatheringBill> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        page.setPageProperty();
        page = this.paymentGatheringBillService.findPage(page, filters);
        return page;
    }

    @Override
    public List<PaymentGatheringBill> list() throws Exception {
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

    @Override
    public MessageBox cancel(String billNo) throws Exception {
        return null;
    }

    @Override
    public MessageBox convert(String strDtlList, String recordList) throws Exception {
        return null;
    }

    @RequestMapping("/save")
    @ResponseBody
    public MessageBox saves(PaymentGatheringBill paymentGatheringBill) throws Exception {
        this.logAllRequestParams();
        String prefix = BillConstant.PayBill.PayForBill
                + CommonUtil.getDateString(new Date(), "yyMMdd");
        String maxNo = this.paymentGatheringBillService.getMaxNo(prefix);
        paymentGatheringBill.setId(maxNo);
        paymentGatheringBill.setBillNo(maxNo);
        paymentGatheringBill.setBillDate(new Date());
        User currentUser = this.getCurrentUser();
        paymentGatheringBill.setOwnerId(currentUser.getOwnerId());
        paymentGatheringBill.setOprId(currentUser.getCode());
        paymentGatheringBill.setBillType("2");

        try {
            this.paymentGatheringBillService.saveandunit(paymentGatheringBill);
            return returnSuccessInfo("修改成功");
        } catch (Exception e) {
            return returnFailInfo("修改失败");
        }
    }

    @RequestMapping("/gatheringSave")
    @ResponseBody
    public MessageBox gatheringSave(PaymentGatheringBill paymentGatheringBill) throws Exception {
        try {
            this.logAllRequestParams();
            String prefix = BillConstant.PayBill.ReceiptBill
                    + CommonUtil.getDateString(new Date(), "yyMMdd");
            String maxNo = this.paymentGatheringBillService.getMaxNo(prefix);
            paymentGatheringBill.setId(maxNo);
            paymentGatheringBill.setBillNo(maxNo);
            paymentGatheringBill.setBillDate(new Date());
            User currentUser = this.getCurrentUser();
            paymentGatheringBill.setOwnerId(currentUser.getOwnerId());
            paymentGatheringBill.setOprId(currentUser.getCode());
            //保存收银表
            payDetail payDetail = new payDetail();
            payDetail.setId(paymentGatheringBill.getBillNo()+paymentGatheringBill.getPayType());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            payDetail.setPayDate(df.format(new Date()));
            payDetail.setCustomerId(paymentGatheringBill.getCustomsId());
            payDetail.setShop(paymentGatheringBill.getOwnerId());
            payDetail.setBillNo(paymentGatheringBill.getBillNo());
            payDetail.setPayType(paymentGatheringBill.getPayType());
            payDetail.setPayPrice(paymentGatheringBill.getPayPrice().toString());
            payDetail.setActPayPrice(paymentGatheringBill.getPayPrice().toString());
            this.payDetailService.save(payDetail);
            this.paymentGatheringBillService.saveGuest(paymentGatheringBill);
            return returnSuccessInfo("修改成功");
        } catch (Exception e) {
            return returnFailInfo("修改失败");
        }
    }
}
