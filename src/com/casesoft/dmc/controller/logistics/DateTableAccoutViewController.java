package com.casesoft.dmc.controller.logistics;

import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.ILogisticsBillController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.logistics.DateTableAccoutView;
import com.casesoft.dmc.model.logistics.MonthAccountStatement;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.logistics.DateTableAccoutViewService;
import com.casesoft.dmc.service.logistics.MonthAccountStatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/25.
 */
@Controller
@RequestMapping("/logistics/dateTableAccoutView")
public class DateTableAccoutViewController  extends BaseController implements ILogisticsBillController<DateTableAccoutView> {
    @Autowired
    private DateTableAccoutViewService dateTableAccoutViewService;
    @Autowired
    private MonthAccountStatementService monthAccountStatementService;
    @RequestMapping(value = "/page")
    @ResponseBody
    @Override
    public Page<DateTableAccoutView> findPage(Page<DateTableAccoutView> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        page.setPageProperty();
        page.setOrderBy("unitId,billDate");
        page.setOrder("asc,asc");
        page = this.dateTableAccoutViewService.findPage(page, filters);
        return page;
    }

    @Override
    public List<DateTableAccoutView> list() throws Exception {
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

    @RequestMapping("/index")
    @Override
    public String index() {
        return "/views/logistics/dateTableAccoutView";
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
    @RequestMapping(value = "/findtiltle")
    @ResponseBody
    public MessageBox findtiltle(String startDate,String endDate){
        Map<String, Double> findtiltle = this.dateTableAccoutViewService.findtiltle(startDate, endDate);
        return returnSuccessInfo("保存成功", findtiltle);
    }
    @RequestMapping(value = "/dateStockDetail")
    @ResponseBody
    public MessageBox dateStockDetail(){
        User currentUser = getCurrentUser();
        String stockDetail = this.dateTableAccoutViewService.savedateStockDetail(currentUser);
        return returnSuccessInfo("保存成功", stockDetail);
    }
    @RequestMapping(value = "/allUnitdateStockDetail")
    @ResponseBody
    public void allUnitdateStockDetail(){
       this.dateTableAccoutViewService.allUnitdateStockDetail();
    }
    
}
