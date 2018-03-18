package com.casesoft.dmc.controller.logistics;

import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.ILogisticsBillController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.logistics.MonthAccountStatement;
import com.casesoft.dmc.model.sys.GuestView;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.service.logistics.MonthAccountStatementService;
import com.casesoft.dmc.service.sys.GuestViewService;
import com.casesoft.dmc.service.sys.impl.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


import java.util.*;

/**
 * Created by yushen on 2017/7/10.
 */

@Controller
@RequestMapping("/logistics/monthAccountStatement")
public class MonthAccountStatementController extends BaseController implements ILogisticsBillController<MonthAccountStatement> {

    @Autowired
    private MonthAccountStatementService monthAccountStatementService;

    @RequestMapping("/index")
    @Override
    public String index() {
        return "/views/logistics/monthAccountStatement";
    }

    @RequestMapping(value = "/page")
    @ResponseBody
    @Override
    public Page<MonthAccountStatement> findPage(Page<MonthAccountStatement> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        page.setPageProperty();
        page = this.monthAccountStatementService.findPage(page, filters);
        return page;
    }

    @Override
    public List<MonthAccountStatement> list() throws Exception {
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

    @Autowired
    private UnitService unitService;
    @Autowired
    private GuestViewService guestViewService;

    @RequestMapping("/autoSave")
    public void autoSave() throws Exception {
        //取出月份减一的时间
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, -1);
        Date date = cal.getTime();

        String dateNow = CommonUtil.getDateString(new Date(), "yyyy-MM-dd");
        String month = CommonUtil.getDateString(date, "yyyy-MM");
        List<MonthAccountStatement> monthAccountStatementList = new ArrayList<>();
        //取供应商信息放入月结对账表
        List<Unit> vendors = this.unitService.findVendors();
        for (Unit vendor : vendors) {
            MonthAccountStatement monthAccountStatement = new MonthAccountStatement();
            monthAccountStatement.setId(vendor.getCode() + "-" + month);
            monthAccountStatement.setBillDate(CommonUtil.converStrToDate(dateNow, "yyyy-MM-dd"));
            monthAccountStatement.setMonth(month);
            monthAccountStatement.setBillType("付款");
            monthAccountStatement.setUnitId(vendor.getCode());
            monthAccountStatement.setUnitType("供应商");
            monthAccountStatement.setOwnerId(vendor.getOwnerId());
            if (CommonUtil.isBlank(vendor.getOwingValue())) {
                monthAccountStatement.setTotVal(0D);
            } else {
                monthAccountStatement.setTotVal(vendor.getOwingValue());
            }
            monthAccountStatementList.add(monthAccountStatement);
        }
        //取客户信息放入对账月结表
        List<GuestView> guests = this.guestViewService.findGuests();
        for (GuestView guest : guests) {
            MonthAccountStatement monthAccountStatement = new MonthAccountStatement();
            monthAccountStatement.setId(guest.getId() + "-" + month);
            monthAccountStatement.setBillDate(CommonUtil.converStrToDate(dateNow, "yyyy-MM-dd"));
            monthAccountStatement.setMonth(month);
            monthAccountStatement.setBillType("收款");
            monthAccountStatement.setUnitId(guest.getId());
            monthAccountStatement.setUnitType("客户");
            monthAccountStatement.setOwnerId(guest.getOwnerId());
            if (CommonUtil.isBlank(guest.getOwingValue())) {
                monthAccountStatement.setTotVal(0D);
            } else {
                monthAccountStatement.setTotVal(guest.getOwingValue());
            }
            monthAccountStatementList.add(monthAccountStatement);
        }
        this.monthAccountStatementService.batchSave(monthAccountStatementList);
    }

    @RequestMapping("getMas")
    @ResponseBody
    public MessageBox getMas(String masId) {
        MonthAccountStatement mas = this.monthAccountStatementService.get("id", masId);
        if (CommonUtil.isNotBlank(mas)) {
            return new MessageBox(true, "", mas);
        }else {
            return new MessageBox(false,"");
        }
    }
}
