package com.casesoft.dmc.controller.logistics;

import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.ILogisticsBillController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.echarts.data.Data;
import com.casesoft.dmc.model.logistics.MonthStatisticsInandOut;
import com.casesoft.dmc.model.search.DetailStockView;
import com.casesoft.dmc.service.logistics.MonthStatisticsService;
import com.casesoft.dmc.service.logistics.StaticsInandOutService;
import com.casesoft.dmc.service.search.DetailStockViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/7/8.
 */
@Controller
@RequestMapping("/logistics/staticsInandOutmonth")
public class MonthStatisticsController extends BaseController implements ILogisticsBillController<MonthStatisticsInandOut> {
    @Autowired
    private MonthStatisticsService monthStatisticsService;

    @Autowired
    private StaticsInandOutService staticsInandOutService;


    @Autowired
    private DetailStockViewService detailStockViewService;



    @Override
    @RequestMapping(value="/page")
    @ResponseBody
    public Page<MonthStatisticsInandOut> findPage(Page<MonthStatisticsInandOut> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page = this.monthStatisticsService.findPage(page,filters);
        return page;
    }

    @Override
    public List<MonthStatisticsInandOut> list() throws Exception {
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

    @Override
    public String index() {
        return "/views/logistics/monthStatistics";
    }

    @RequestMapping(value="/saveMonthStatisticsInandOut")
    @ResponseBody
    public void saveMonthStatisticsInandOut(){
        //得到当前时间
        Date data =new Date(); Calendar cl = Calendar.getInstance();
        cl.setTime(data);
        cl.add(Calendar.MONTH, -1);
        data = cl.getTime();
        String dateString = CommonUtil.getDateString(data, "yyyy-MM");
        List<DetailStockView> all = this.detailStockViewService.findAll();
        for(int i=0;i<all.size();i++){
            DetailStockView detailStockView = all.get(i);
            MonthStatisticsInandOut monthStatisticsInandOut = new MonthStatisticsInandOut();
            monthStatisticsInandOut.setId(detailStockView.getWarehId()+"-"+dateString+"-"+detailStockView.getSku()+"-"+detailStockView.getInsotretype());
            monthStatisticsInandOut.setColorId( detailStockView.getColorId());
            monthStatisticsInandOut.setColorName(detailStockView.getColorname());
            monthStatisticsInandOut.setSizeId(detailStockView.getSizeId());
            monthStatisticsInandOut.setSku(detailStockView.getSku());
            monthStatisticsInandOut.setStyleId(detailStockView.getStyleId());
            monthStatisticsInandOut.setStyleName(detailStockView.getStyleEName());
            monthStatisticsInandOut.setPrecast(detailStockView.getPrecast());
            monthStatisticsInandOut.setPrice(detailStockView.getPrice());
            monthStatisticsInandOut.setPuprice(detailStockView.getPuprice());
            monthStatisticsInandOut.setWsprice(detailStockView.getWsprice());
            monthStatisticsInandOut.setWarehId(detailStockView.getWarehId());
            monthStatisticsInandOut.setTimedate(data);
            monthStatisticsInandOut.setStockPrice(detailStockView.getStockprice());
            monthStatisticsInandOut.setTimemonth(dateString);
            int qty = detailStockView.getQty();
            monthStatisticsInandOut.setMonthStock(Long.parseLong(String.valueOf(qty)));
            this.monthStatisticsService.save(monthStatisticsInandOut);

        }
    }
}
