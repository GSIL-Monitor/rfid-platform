package com.casesoft.dmc.controller.logistics;

import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.ILogisticsBillController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.logistics.AccountStatementView;
import com.casesoft.dmc.model.logistics.MonthAccountStatement;
import com.casesoft.dmc.service.logistics.AccountStatementViewService;
import com.casesoft.dmc.service.logistics.MonthAccountStatementService;
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
@RequestMapping("/logistics/accountStatementView")
public class AccountStatementViewController extends BaseController implements ILogisticsBillController<AccountStatementView> {

    @Autowired
    AccountStatementViewService accountStatementViewService;

    @Autowired
    MonthAccountStatementService monthAccountStatementService;

    @RequestMapping("/index")
    @Override
    public String index() {
        return "/views/logistics/accountStatementView";
    }

    @RequestMapping(value = "/page")
    @ResponseBody
    @Override
    public Page<AccountStatementView> findPage(Page<AccountStatementView> page) throws Exception {
        this.logAllRequestParams();
        String unitId = this.getReqParam("filter_EQS_unitId");
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        page.setPageProperty();
        page.setOrderBy("unitId,billDate");
        page.setOrder("asc,asc");
        page = this.accountStatementViewService.findPage(page, filters);
        Date endDate= CommonUtil.converStrToDate(this.getReqParam("filter_GED_billDate"),"yyyy-MM-dd");
        Date startDate= CommonUtil.converStrToDate(CommonUtil.getDateString(endDate,"yyyy-MM-01"),"yyyy-MM-dd");
        Map<String,MonthAccountStatement> monthAccountStatementMap = new HashMap<>();
        if(CommonUtil.isNotBlank(endDate)){
            List<MonthAccountStatement> monthAccountStatementList= getInitialBills(endDate);

            for(MonthAccountStatement m :monthAccountStatementList){
                monthAccountStatementMap.put(m.getUnitId(),m);
            }
            //月结期初到查询起始时间的对账流水单
            List<AccountStatementView> accountStatementViews = this.accountStatementViewService.findStatementsInTime(startDate, endDate);

            //
            for(AccountStatementView statement :accountStatementViews){
                MonthAccountStatement tempM = monthAccountStatementMap.get(statement.getUnitId());
                if(CommonUtil.isNotBlank(tempM)){
                    Double initialVal = tempM.getTotVal();
                    initialVal += statement.getDiffPrice();
                    tempM.setTotVal(initialVal);
                    monthAccountStatementMap.put(statement.getUnitId(),tempM);
                }else {
                    tempM = new MonthAccountStatement();
                    Double initialVal;
                    if(CommonUtil.isNotBlank(tempM.getTotVal())){
                        initialVal = tempM.getTotVal();
                    }else {
                        initialVal = 0D;
                    }
                    initialVal += statement.getDiffPrice();
                    tempM.setTotVal(initialVal);
                    monthAccountStatementMap.put(statement.getUnitId(),tempM);
                }
            }
        }

        Map<String,Double> totOwingMap = new HashMap<>();//存客户或供应商累计欠款金额
        for(AccountStatementView a: page.getRows()){
            //取出期初欠款金额
            Double initialOwingVal;
            if(CommonUtil.isBlank(monthAccountStatementMap.get(a.getUnitId()))){
                initialOwingVal=0D;
            }else {
                initialOwingVal = monthAccountStatementMap.get(a.getUnitId()).getTotVal();
            }
            if(totOwingMap.containsKey(a.getUnitId())){
                Double totOwingVal = totOwingMap.get(a.getUnitId());
                totOwingVal += a.getDiffPrice();
                totOwingMap.put(a.getUnitId(),Math.round(totOwingVal*100)*0.01D);
            }else{
                Double totOwingVal = initialOwingVal+a.getDiffPrice();
                totOwingMap.put(a.getUnitId(),Math.round(totOwingVal*100)*0.01D);
            }
            String groupId;
            if("0".equals(a.getUnitType())){
                groupId = "期初欠供应商"+a.getUnitId()+" 金额："+initialOwingVal;
            }else{
                groupId = "客户"+a.getUnitId()+" 期初欠款："+initialOwingVal;
            }
            a.setGroupId(groupId);
            a.setTotalOwingVal(totOwingMap.get(a.getUnitId()));
        }
        if(!monthAccountStatementMap.isEmpty() && page.getRows().isEmpty()){

            AccountStatementView a = new AccountStatementView();

        }
        return page;
    }

    @Override
    public List<AccountStatementView> list() throws Exception {
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

    public  List<MonthAccountStatement> getInitialBills(Date date){
        //根据日期取出月份
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1);
        Date lastMonthDate = cal.getTime();
        String month = CommonUtil.getDateString(lastMonthDate,"yyyy-MM");
        System.out.println("--月份为："+month);
        //取出对应月份和unitId下的月结账单
        List<MonthAccountStatement> monthAccountStatementList = this.monthAccountStatementService.getStatement(month);
        return monthAccountStatementList;
    }



}
