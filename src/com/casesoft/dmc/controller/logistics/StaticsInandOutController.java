package com.casesoft.dmc.controller.logistics;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.ILogisticsBillController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.echarts.data.Data;
import com.casesoft.dmc.model.logistics.MonthStatisticsInandOut;
import com.casesoft.dmc.model.logistics.StatisticsInandOut;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.logistics.MonthStatisticsService;
import com.casesoft.dmc.service.logistics.StaticsInandOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by chenzhifan on 2017/7/8.
 */
@Controller
@RequestMapping("/logistics/staticsInandOut")
public class StaticsInandOutController extends BaseController implements ILogisticsBillController<StatisticsInandOut> {
    @Autowired
    private StaticsInandOutService staticsInandOutService;

    @Autowired
    private MonthStatisticsService monthStatisticsService;

    @Override
    //@RequestMapping(value = "/index")
    public String index() {
        return"/views/logistics/monthStatistics";
    }
    @RequestMapping(value = "/index")
    public ModelAndView indexMV() throws Exception {
        ModelAndView mv = new ModelAndView("/views/logistics/monthStatistics");
        User user = this.getCurrentUser();
        mv.addObject("OwnerId",user.getOwnerId());
        return mv;
    }

    @Override
    @RequestMapping(value="/page")
    @ResponseBody
    public Page<StatisticsInandOut> findPage(Page<StatisticsInandOut> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        String begigntime=null;
        page = this.staticsInandOutService.findPage(page,filters);
        for(int a=0;a<filters.size();a++){
            String matchType=filters.get(a).getMatchType().name();
            if("GE".equals(matchType)){
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                Object data=filters.get(a).getMatchValue();
                begigntime=sdf.format(data);
            }
        }
        List<StatisticsInandOut> rows = page.getRows();
        /*String warehId="";
        Date date=null;
        String groupId="";
        String sku="";*/
        Map<String,Double> totOwingMap = new HashMap<>();//累计库存金额
        for(int i=0;i<rows.size();i++){
            String warehId="";
            Date date=null;
            String groupId="";
            String sku="";
            StatisticsInandOut statisticsInandOut = rows.get(i);
            if(warehId.equals(statisticsInandOut.getWarehId())&&statisticsInandOut.getTimedate().equals(date)&&statisticsInandOut.getSku().equals(sku)){
                statisticsInandOut.setGroupId(groupId);
            }else{
                Date timedate=null;
                if(CommonUtil.isBlank(begigntime)){
                    timedate=new Date();
                }else {
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟
                    timedate=sdf.parse(begigntime);
                }
                String dateStr;
                Calendar cl = Calendar.getInstance();
                cl.setTime(timedate);
                cl.add(Calendar.MONTH, -1);
                Date timedate1 = cl.getTime();
                String time= CommonUtil.getDateString(timedate1,"yyyy-MM");
                Long monthStatisticsInandOut = this.monthStatisticsService.findMonthStatisticsInandOut(statisticsInandOut.getWarehId(), time, statisticsInandOut.getSku());
                Double monthStatisticsInandOutstockPrice = this.monthStatisticsService.findMonthStatisticsInandOutstockPrice(statisticsInandOut.getWarehId(), time, statisticsInandOut.getSku());
                String time1= CommonUtil.getDateString(timedate,"yyyy-MM-dd");
                String[] split = time1.split("-");
                Long monthStatisticsInandOut1 = this.staticsInandOutService.findMonthStatisticsInandOut(statisticsInandOut.getWarehId(), (split[0]+ "-"+ split[1] + "-01"), time1, statisticsInandOut.getSku());
                Long monthStatisticsInandOutPrice = this.staticsInandOutService.findMonthStatisticsInandOutPrice(statisticsInandOut.getWarehId(), (split[0] + "-" + split[1] + "-01"), time1, statisticsInandOut.getSku());
                if(CommonUtil.isBlank(monthStatisticsInandOut1)){
                    monthStatisticsInandOut1=0L;
                }
                Integer monthStock =0;
                Double monthstockPrice=0D;
                Double monthstockpreval=0D;
                if(CommonUtil.isBlank(monthStatisticsInandOut)){
                    warehId=statisticsInandOut.getWarehId();
                    monthStock=0;
                }else{
                    monthStock=monthStatisticsInandOut.intValue();
                    warehId=statisticsInandOut.getWarehId();
                    date=statisticsInandOut.getTimedate();
                }
                if (CommonUtil.isBlank(monthStatisticsInandOutstockPrice)){
                    monthstockPrice=0D;
                }else {
                    monthstockPrice = monthStatisticsInandOutstockPrice;
                }
                if (CommonUtil.isBlank(monthStatisticsInandOutPrice)){
                    monthstockpreval=0D;
                }else {
                    monthstockpreval = monthStatisticsInandOutPrice.doubleValue();
                }

                if(CommonUtil.isBlank(monthStatisticsInandOut1)){
                    //String groupIds=warehId+"-"+time+statisticsInandOut.getSizeName()+"-"+(monthStock+0);
                    Unit unitByCode = CacheManager.getUnitByCode(warehId);
                    String groupIds="";
                    //groupIds = "库存：-期初库存:" + (monthStock)+"-sku:"+statisticsInandOut.getSku()+"数量:"+statisticsInandOut.getStockQty();
                    if(CommonUtil.isBlank(unitByCode)){
                        groupIds= "库存：-" +  "期初库存:" + (monthStock + monthStatisticsInandOut1)+"-sku:"+statisticsInandOut.getSku()+"期初库存金额:"+(monthstockpreval+monthstockPrice);
                    }else{
                        groupIds = "库存："+unitByCode.getName() + "-" +  "期初库存:" + (monthStock + monthStatisticsInandOut1)+"-sku:"+statisticsInandOut.getSku()+"期初库存金额:"+(monthstockpreval+monthstockPrice);
                    }
                    groupId=groupIds;
                    sku=statisticsInandOut.getSku();
                    statisticsInandOut.setGroupId(groupIds);
                }else {
                    Unit unitByCode = CacheManager.getUnitByCode(warehId);
                    String groupIds="";
                    if(CommonUtil.isBlank(unitByCode)){
                        groupIds= "库存：-" +  "期初库存:" + (monthStock + monthStatisticsInandOut1)+"-sku:"+statisticsInandOut.getSku()+"期初库存金额:"+(monthstockpreval+monthstockPrice);
                    }else{
                        groupIds = "库存："+unitByCode.getName() + "-" +  "期初库存:" + (monthStock + monthStatisticsInandOut1)+"-sku:"+statisticsInandOut.getSku()+"期初库存金额:"+(monthstockpreval+monthstockPrice);
                    }

                    groupId=groupIds;
                    sku=statisticsInandOut.getSku();
                    //计算库存价格
                    if(statisticsInandOut.getToken().equals(Constant.Token.Storage_Inbound)){
                        statisticsInandOut.setTypeinout("采购订单入库");
                        String nump=CommonUtil.getDecimal(statisticsInandOut.getPreval(),"######0.00");
                        statisticsInandOut.setInpreval(Double.parseDouble(nump));
                       // DecimalFormat    df   = new DecimalFormat("######0.00");
                        double v = statisticsInandOut.getPreval() / statisticsInandOut.getInQty();
                       // String num=df.format(v);
                        String num=CommonUtil.getDecimal(v,"######0.00");
                        statisticsInandOut.setInprevalNum(Double.parseDouble(num));
                    }
                    if(statisticsInandOut.getToken().equals(Constant.Token.Storage_Outbound)){
                        statisticsInandOut.setTypeinout("销售订单出库");
                        String nump=CommonUtil.getDecimal(statisticsInandOut.getPreval(),"######0.00");
                        statisticsInandOut.setInpreval(Double.parseDouble(nump));
                        double v = statisticsInandOut.getPreval() / statisticsInandOut.getOutQty();
                        //DecimalFormat    df   = new DecimalFormat("######0.00");
                        //String num=df.format(v);
                        String num=CommonUtil.getDecimal(v,"######0.00");
                        statisticsInandOut.setOutprevalNum(Double.parseDouble(num));
                    }
                    if(statisticsInandOut.getToken().equals(Constant.Token.Storage_Refund_Inbound)){
                        statisticsInandOut.setTypeinout("销售退货申请单入库");
                        String nump=CommonUtil.getDecimal(statisticsInandOut.getPreval(),"######0.00");
                        statisticsInandOut.setInpreval(Double.parseDouble(nump));
                        double v = statisticsInandOut.getPreval() / statisticsInandOut.getInQty();
                       /* DecimalFormat    df   = new DecimalFormat("######0.00");
                        String num=df.format(v);*/
                        String num=CommonUtil.getDecimal(v,"######0.00");
                        statisticsInandOut.setInprevalNum(Double.parseDouble(num));
                    }
                    if(statisticsInandOut.getToken().equals(Constant.Token.Storage_Refund_Outbound)){
                        statisticsInandOut.setTypeinout("采购退货出库");
                        String nump=CommonUtil.getDecimal(statisticsInandOut.getPreval(),"######0.00");
                        statisticsInandOut.setInpreval(Double.parseDouble(nump));
                        double v = statisticsInandOut.getPreval() / statisticsInandOut.getOutQty();
                       /* DecimalFormat    df   = new DecimalFormat("######0.00");
                        String num=df.format(v);*/
                        String num=CommonUtil.getDecimal(v,"######0.00");
                        statisticsInandOut.setOutprevalNum(Double.parseDouble(num));
                    }
                    if(statisticsInandOut.getToken().equals(Constant.Token.Storage_Transfer_Outbound)){
                        statisticsInandOut.setTypeinout("调拨申请单发货");
                        String nump=CommonUtil.getDecimal(statisticsInandOut.getPreval(),"######0.00");
                        statisticsInandOut.setInpreval(Double.parseDouble(nump));
                        double v = statisticsInandOut.getPreval() / statisticsInandOut.getTransferOutQty();
                      /*  DecimalFormat    df   = new DecimalFormat("######0.00");
                        String num=df.format(v);*/
                        String num=CommonUtil.getDecimal(v,"######0.00");
                        statisticsInandOut.setTransferOutprevalNum(Double.parseDouble(num));
                    }
                    if(statisticsInandOut.getToken().equals(Constant.Token.Storage_Transfer_Inbound)){
                        statisticsInandOut.setTypeinout("调拨申请单收货");
                        String nump=CommonUtil.getDecimal(statisticsInandOut.getPreval(),"######0.00");
                        statisticsInandOut.setInpreval(Double.parseDouble(nump));
                        double v = statisticsInandOut.getPreval() / statisticsInandOut.getTransferInQty();
                        /*DecimalFormat    df   = new DecimalFormat("######0.00");
                        String num=df.format(v);*/
                        String num=CommonUtil.getDecimal(v,"######0.00");
                        statisticsInandOut.setTransferInprevalQtyNum(Double.parseDouble(num));
                    }
                    statisticsInandOut.setTime(CommonUtil.getDateString(statisticsInandOut.getTimedate(),"yyyy-MM-dd"));
                    statisticsInandOut.setGroupId(groupIds);
                }
            }

            //累计库存金额
            Double aDouble = totOwingMap.get(statisticsInandOut.getGroupId());
            if(CommonUtil.isBlank(aDouble)){
                totOwingMap.put(statisticsInandOut.getGroupId(),statisticsInandOut.getPreval());
                String nump=CommonUtil.getDecimal(statisticsInandOut.getPreval(),"######0.00");
                statisticsInandOut.setTotpreval( Double.parseDouble(nump));
            }else{
                String nump=CommonUtil.getDecimal(statisticsInandOut.getPreval(),"######0.00");
                totOwingMap.replace(statisticsInandOut.getGroupId(),(aDouble+Double.parseDouble(nump)));
                statisticsInandOut.setTotpreval((aDouble+Double.parseDouble(nump)));
            }
        }
        return page;
    }

    @Override
    public List<StatisticsInandOut> list() throws Exception {
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


}
