package com.casesoft.dmc.controller.logistics;

import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.ILogisticsBillController;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.logistics.DayStatisticsInandOut;
import com.casesoft.dmc.model.search.DetailStockView;
import com.casesoft.dmc.service.logistics.DayStatisticsService;
import com.casesoft.dmc.service.logistics.StaticsInandOutService;
import com.casesoft.dmc.service.search.DetailStockViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/9/29 0029.
 */
@Controller
@RequestMapping("/logistics/staticsInandOutday")
public class DayStatisticsController extends BaseController implements ILogisticsBillController<DayStatisticsInandOut> {
    @Autowired
    private DayStatisticsService dayStatisticsService;

    @Autowired
    private StaticsInandOutService staticsInandOutService;


    @Autowired
    private DetailStockViewService detailStockViewService;
    @Override
    public Page<DayStatisticsInandOut> findPage(Page<DayStatisticsInandOut> page) throws Exception {
        return null;
    }

    @Override
    public List<DayStatisticsInandOut> list() throws Exception {
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
        return null;
    }
    @RequestMapping(value="/saveDayStatisticsInandOut")
    @ResponseBody
    public void saveDayStatisticsInandOut(){
        //得到当前时间
        Date data =new Date(); Calendar cl = Calendar.getInstance();
        cl.setTime(data);
        cl.add(Calendar.DAY_OF_MONTH, -1);
        data = cl.getTime();
        String dateString = CommonUtil.getDateString(data, "yyyy-MM-dd");
        List<DetailStockView> all = this.detailStockViewService.findAll();
        for(int i=0;i<all.size();i++){
            DetailStockView detailStockView = all.get(i);
            //MonthStatisticsInandOut monthStatisticsInandOut = new MonthStatisticsInandOut();
            DayStatisticsInandOut dayStatisticsInandOut=new DayStatisticsInandOut();
            dayStatisticsInandOut.setId(detailStockView.getWarehId()+"-"+dateString+"-"+detailStockView.getSku()+"-"+detailStockView.getInsotretype());
            dayStatisticsInandOut.setColorId( detailStockView.getColorId());
            dayStatisticsInandOut.setColorName(detailStockView.getColorname());
            dayStatisticsInandOut.setSizeId(detailStockView.getSizeId());
            dayStatisticsInandOut.setSku(detailStockView.getSku());
            dayStatisticsInandOut.setStyleId(detailStockView.getStyleId());
            dayStatisticsInandOut.setStyleName(detailStockView.getStyleEName());
            dayStatisticsInandOut.setPrecast(detailStockView.getPrecast());
            dayStatisticsInandOut.setPrice(detailStockView.getPrice());
            dayStatisticsInandOut.setPuprice(detailStockView.getPuprice());
            dayStatisticsInandOut.setWsprice(detailStockView.getWsprice());
            dayStatisticsInandOut.setWarehId(detailStockView.getWarehId());
            dayStatisticsInandOut.setTimedate(data);
            dayStatisticsInandOut.setStockPrice(detailStockView.getStockprice());
            dayStatisticsInandOut.setTimemonth(dateString);
            int qty = detailStockView.getQty();
            dayStatisticsInandOut.setMonthStock(Long.parseLong(String.valueOf(qty)));
            this.dayStatisticsService.save(dayStatisticsInandOut);

        }
    }
}
