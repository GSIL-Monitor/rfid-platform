package com.casesoft.dmc.extend.api.wechat;

import com.casesoft.dmc.controller.product.StyleUtil;
import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.json.JSONUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.dao.search.SaleorderCountDao;
import com.casesoft.dmc.extend.api.web.ApiBaseController;
import com.casesoft.dmc.model.logistics.BillConstant;
import com.casesoft.dmc.model.logistics.SaleBybusinessname;
import com.casesoft.dmc.model.search.*;
import com.casesoft.dmc.service.logistics.SaleOrderBillService;
import com.casesoft.dmc.service.search.SaleNodeatilViewsService;
import com.casesoft.dmc.service.search.SaleOrderCountViewService;
import com.casesoft.dmc.service.sys.impl.UnitService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Administrator on 2017/12/18.
 */
@Controller
@RequestMapping(value = "/api/wechat/SaleCountView", method = {RequestMethod.POST, RequestMethod.GET})
@Api(description = "微信库存接口")
public class WxSaleCountViewController extends ApiBaseController {
    @Autowired
    private UnitService unitService;
    @Autowired
    private SaleOrderBillService saleOrderBillService;
    @Autowired
    private SaleOrderCountViewService saleOrderCountViewService;
    @Autowired
    private SaleNodeatilViewsService saleNodeatilViewsService;
    @Autowired
    private SaleorderCountDao saleorderCountDao;

    @Override
    public String index() {
        return null;
    }

    /**
     * add by Anna
     * @param sbillDate 开始时间
     * @param ebillDate 结束时间
     * 进销存信息内查看销售量-详细
     */
    @RequestMapping(value = "/findSaleDtl.do")
    @ResponseBody
    public Map<String, Object> findSaleDtl(String styleId,String sbillDate,String ebillDate) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        List<SaleorderCountView> saleorderCountViews = this.saleOrderBillService.findsaleDtlByStyleId(styleId,sbillDate,ebillDate);
        map.put("saleorderCountViews", saleorderCountViews);
        return map;
    }

    @RequestMapping(value = "/findtitledate")
    @ResponseBody
    public MessageBox findtitledate(String dates) {
        System.out.print(dates);
        Map<String, String> map4Json = JSONUtil.getMap4Json(dates);
        //String hql="select new saleorderCount (sum(t.qty), sum(t.totactprice)) from saleorderCountOViews t where 1=1";
        String hqlsqty = "select sum(t.qty), sum(t.totactprice),sum(t.gross) from saleorderCountOViews t where 1=1";
        //String hqlstotactprice="select  sum(t.totactprice) from saleorderCountOViews t where 1=1";
        String hqlrqty = "select sum(t.qty),sum(t.totactprice),sum(t.gross) from saleorderCountRViews t where 1=1";
        //String hqlrtotactprice="select sum(t.totactprice) from saleorderCountRViews t where 1=1";
        //String hqlrtogross="select sum(t.gross) from saleorderCountRViews t where 1=1";
        //String hqlrtogrossall="select sum(t.grossall) from saleorderCountRViews t where 1=1";
        //String hqlstogrossall="select sum(t.grossall) from saleorderCountOViews t where 1=1";
        //String hqlstogross="select sum(t.gross) from saleorderCountOViews t where 1=1";
        Iterator<Map.Entry<String, String>> iterator = map4Json.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            String key = next.getKey();
            String value = map4Json.get(key);
            if (CommonUtil.isNotBlank(value)) {
                if (key.equals("filter_gte_billDate")) {
                    String[] split = key.split("_");
                    String time = value + " 00:00:00";
                    hqlsqty += " and " + split[2] + " >= to_date('" + time + "','yyyy-MM-dd HH24:mi:ss')";
                    // hqlstotactprice+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqlrqty += " and " + split[2] + " >= to_date('" + time + "','yyyy-MM-dd HH24:mi:ss')";
                    // hqlrtotactprice+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    //hqlrtogross+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    //hqlrtogrossall+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    //hqlstogrossall+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    //hqlstogross+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                  /* hqlsqty+=" and '"+value+" '<= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlstotactprice+=" and '"+value+" '<= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlrqty+=" and '"+value+" '<= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlrtotactprice+=" and '"+value+" '<= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlrtogross+=" and '"+value+" '<= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlrtogrossall+=" and '"+value+" '<= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlstogrossall+=" and '"+value+" '<= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlstogross+=" and '"+value+" '<= to_char("+split[2]+",'yyyy-MM-dd')";*/
                } else if (key.equals("filter_lte_billDate")) {
                    String[] split = key.split("_");
                    String time = value + " 23:59:59";
                    hqlsqty += " and " + split[2] + " <= to_date('" + time + "','yyyy-MM-dd HH24:mi:ss')";
                    //hqlstotactprice+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqlrqty += " and " + split[2] + " <= to_date('" + time + "','yyyy-MM-dd HH24:mi:ss')";
                    //hqlrtotactprice+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    //hqlrtogross+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    //hqlrtogrossall+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    //hqlstogrossall+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    //hqlstogross+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                 /*   hqlsqty+=" and '"+value+"' >= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlstotactprice+=" and '"+value+"' >= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlrqty+=" and '"+value+"' >= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlrtotactprice+=" and '"+value+"' >= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlrtogross+=" and '"+value+"' >= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlrtogrossall+=" and '"+value+"' >= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlstogrossall+=" and '"+value+"' >= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlstogross+=" and '"+value+"' >= to_char("+split[2]+",'yyyy-MM-dd')";*/
                } else {
                    String[] split = key.split("_");
                    if (split[1].equals("contains")) {
                        hqlsqty += " and " + split[2] + " like '%" + value + "%'";
                        // hqlstotactprice+=" and "+split[2]+" like'%"+value+"%'";
                        hqlrqty += " and " + split[2] + " like '%" + value + "%'";
                        //hqlrtotactprice+=" and "+split[2]+" like '%"+value+"%'";
                        //hqlrtogross+=" and "+split[2]+" like '%"+value+"%'";
                        //hqlrtogrossall+=" and "+split[2]+" like '%"+value+"%'";
                        //hqlstogrossall+=" and "+split[2]+" like '%"+value+"%'";
                        //hqlstogross+=" and "+split[2]+" like '%"+value+"%'";
                    } else {
                        hqlsqty += " and " + split[2] + "='" + value + "'";
                        //hqlstotactprice+=" and "+split[2]+"='"+value+"'";
                        hqlrqty += " and " + split[2] + "='" + value + "'";
                        //hqlrtotactprice+=" and "+split[2]+"='"+value+"'";
                        //hqlrtogross+=" and "+split[2]+"='"+value+"'";
                        //hqlrtogrossall+=" and "+split[2]+"='"+value+"'";
                        //hqlstogrossall+=" and "+split[2]+"='"+value+"'";
                        //hqlstogross+=" and "+split[2]+"='"+value+"'";
                    }


                }

            }
        }
        long saleOrdersStartTime = System.currentTimeMillis();
        Object[] saleOrders = (Object[]) this.saleOrderBillService.findsaleOrderOrsaleRetrunMessage(hqlsqty);
        long saleOrdersEndTime = System.currentTimeMillis();
        logger.error("执行saleOrders的时间" + (saleOrdersEndTime - saleOrdersStartTime));
        Integer longsqty = null;
        Double longstotactprice = null;

        Double longstogross = null;
        if (CommonUtil.isNotBlank(saleOrders[0])) {
            longsqty = Integer.parseInt(saleOrders[0] + "");
        } else {
            longsqty = 0;
        }
        if (CommonUtil.isNotBlank(saleOrders[1])) {
            longstotactprice = Double.parseDouble(saleOrders[1] + "");
        } else {
            longstotactprice = 0D;
        }

        if (CommonUtil.isNotBlank(saleOrders[2])) {
            longstogross = Double.parseDouble(saleOrders[2] + "");
        } else {
            longstogross = 0D;
        }
        Integer longrqty = null;
        Double longrtotactprice = null;

        Double longrtogross = null;
        long saleRetrunsStartTime = System.currentTimeMillis();
        Object[] saleRetruns = (Object[]) this.saleOrderBillService.findsaleOrderOrsaleRetrunMessage(hqlrqty);
        long saleRetrunsEndTime = System.currentTimeMillis();
        logger.error("执行saleRetruns的时间" + (saleRetrunsEndTime - saleRetrunsStartTime));
        if (CommonUtil.isNotBlank(saleRetruns[0])) {
            longrqty = Integer.parseInt(saleRetruns[0] + "");
        } else {
            longrqty = 0;
        }
        if (CommonUtil.isNotBlank(saleRetruns[1])) {
            longrtotactprice = Double.parseDouble(saleRetruns[1] + "");
        } else {
            longrtotactprice = 0D;
        }

        if (CommonUtil.isNotBlank(saleRetruns[2])) {
            longrtogross = Double.parseDouble(saleRetruns[2] + "");
        } else {
            longrtogross = 0D;
        }
       /* Integer longsqty= null;
        long aLongstartTime= System.currentTimeMillis();
        Long aLong = this.saleOrderBillService.findsaleOrderCount(hqlsqty);
        long aLongendTime= System.currentTimeMillis();
        logger.error("执行aLong的时间"+(aLongendTime-aLongstartTime));
        if(CommonUtil.isBlank(aLong)){
            longsqty=0;
        }else{
            longsqty= aLong.intValue();
        }
        long longstotactpricestartTime= System.currentTimeMillis();
        Double longstotactprice= this.saleOrderBillService.findsaleOrderCountnum(hqlstotactprice);
        long longstotactpriceendTime= System.currentTimeMillis();
        logger.error("执行longstotactprice的时间"+(longstotactpriceendTime-longstotactpricestartTime));
        if(CommonUtil.isBlank(longstotactprice)){
            longstotactprice=0.D;
        }
        Integer longrqty= null;
        long countstartTime= System.currentTimeMillis();
        Long count = this.saleOrderBillService.findsaleOrderCount(hqlrqty);
        long countpriceendTime= System.currentTimeMillis();
        logger.error("执行count的时间"+(countpriceendTime-countstartTime));
        if(CommonUtil.isBlank(count)){
            longrqty=0;
        }else{
            longrqty=count.intValue();
        }
        long longrtotactpricestartTime= System.currentTimeMillis();
        Double longrtotactprice= this.saleOrderBillService.findsaleOrderCountnum(hqlrtotactprice);
        long longrtotactpriceendTime= System.currentTimeMillis();
        logger.error("执行longrtotactprice的时间"+(longrtotactpriceendTime-longrtotactpricestartTime));
        if(CommonUtil.isBlank(longrtotactprice)){
            longrtotactprice=0.D;
        }
        long longrtogrossstartTime= System.currentTimeMillis();
        Double longrtogross= this.saleOrderBillService.findsaleOrderCountnum(hqlrtogross);
        long longrtogrossendTime= System.currentTimeMillis();
        logger.error("执行longrtogross的时间"+(longrtogrossendTime-longrtogrossstartTime));
        if(CommonUtil.isBlank(longrtogross)){
            longrtogross=0.D;
        }
        long longrtogrossallstartTime= System.currentTimeMillis();
        Double longrtogrossall= this.saleOrderBillService.findsaleOrderCountnum(hqlrtogrossall);
        long longrtogrossallendTime= System.currentTimeMillis();
        logger.error("执行longrtogross的时间"+(longrtogrossallendTime-longrtogrossallstartTime));
        if(CommonUtil.isBlank(longrtogrossall)){
            longrtogrossall=0.D;
        }
        long longstogrossallstartTime= System.currentTimeMillis();
        Double longstogrossall= this.saleOrderBillService.findsaleOrderCountnum(hqlstogrossall);
        long longstogrossallendTime= System.currentTimeMillis();
        logger.error("执行longstogrossall的时间"+(longstogrossallendTime-longstogrossallstartTime));
        if(CommonUtil.isBlank(longstogrossall)){
            longstogrossall=0.D;
        }
        long longstogrossstartTime= System.currentTimeMillis();
        Double longstogross= this.saleOrderBillService.findsaleOrderCountnum(hqlstogross);
        long longstogrossendTime= System.currentTimeMillis();
        logger.error("执行longstogross的时间"+(longstogrossendTime-longstogrossstartTime));
        if(CommonUtil.isBlank(longstogross)){
            longstogross=0.D;
        }*/

        //saleorderCount saleorderCount = this.saleOrderBillService.findsaleOrderCount(hql);

        saleorderCount saleorderCounts = new saleorderCount();
        saleorderCounts.setSum(longsqty);
        saleorderCounts.setRsum(longrqty);
        Double Allmony = longstotactprice + longrtotactprice;
        BigDecimal b = new BigDecimal(Allmony.floatValue());
        saleorderCounts.setAllmony(b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
        Double Passall = longstogross + longrtogross;
        BigDecimal b1 = new BigDecimal(Passall.floatValue());
        saleorderCounts.setPassall(b1.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
        Double Grossprofits = 0D;
        if ((longstotactprice + longrtotactprice) == 0) {
            Grossprofits = 0D;
        } else {
            Grossprofits = (longstogross + longrtogross) / (longstotactprice + longrtotactprice) * 100;
        }
        BigDecimal b2 = new BigDecimal(Grossprofits);
        saleorderCounts.setGrossprofits(b2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "%");
        return new MessageBox(true, "成功", saleorderCounts);


    }

    @RequestMapping(value = "/findSaleList.do")
    @ResponseBody
    public MessageBox findSaleList(String pageSize, String pageNo, String sortName, String order) {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());

        Page<SaleorderCountView> page = new Page<SaleorderCountView>(Integer.parseInt(pageSize));
        page.setPage(Integer.parseInt(pageSize));
        page.setPageNo(Integer.parseInt(pageNo));
        if (CommonUtil.isNotBlank(sortName)) {
            page.setOrderBy(sortName);
        }
        if (CommonUtil.isNotBlank(order)) {
            page.setOrder(order);
        }
        page = this.saleOrderCountViewService.findPage(page, filters);
        for (int i = 0; i < page.getRows().size(); i++) {
            SaleorderCountView saleorderCountView = page.getRows().get(i);
            String billno = saleorderCountView.getBillno();
            if (billno.contains(BillConstant.BillPrefix.saleOrder)) {
             /*   saleorderCountView.setSaletype("销售订单");*/
                saleorderCountView.setIshow(true);
            }
            if (billno.contains(BillConstant.BillPrefix.SaleOrderReturn)) {
               /* saleorderCountView.setSaletype("销售退货");
                Integer qty = saleorderCountView.getQty();*//**/
               /* saleorderCountView.setQty(Integer.parseInt("-"+qty));*/
                saleorderCountView.setIshow(true);
            }
        }
        return this.returnSuccessInfo("获取成功", page.getRows());
    }

    @RequestMapping(value = "/findSaleBillList.do")
    @ResponseBody
    public MessageBox findSaleBillList(String pageSize, String pageNo, String sortName, String order) {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        Page<SaleNodeatilViews> page = new Page<SaleNodeatilViews>(Integer.parseInt(pageSize));
        page.setPage(Integer.parseInt(pageNo));
        page.setPageNo(Integer.parseInt(pageNo));
        if (CommonUtil.isNotBlank(sortName)) {
            page.setOrderBy(sortName);
        }
        if (CommonUtil.isNotBlank(order)) {
            page.setOrder(order);
        }
        page = this.saleNodeatilViewsService.findPage(page, filters);
        for (int i = 0; i < page.getRows().size(); i++) {
            SaleNodeatilViews saleNodeatilViews = page.getRows().get(i);
            String billno = saleNodeatilViews.getBillno();
            if (billno.contains(BillConstant.BillPrefix.saleOrder)) {
                saleNodeatilViews.setSaletype("销售订单");
                saleNodeatilViews.setIshow(true);
            }
            if (billno.contains(BillConstant.BillPrefix.SaleOrderReturn)) {
                saleNodeatilViews.setSaletype("销售退货");
                Integer qty = saleNodeatilViews.getTotqty();
                //saleNodeatilViews.setTotqty(Integer.parseInt("-"+qty));
                saleNodeatilViews.setIshow(true);
            }
            if (CommonUtil.isNotBlank(saleNodeatilViews.getCustomertypeid())) {
                if (saleNodeatilViews.getCustomertypeid().equals("CT-AT")) {
                    saleNodeatilViews.setCustomertypeid("省代客户");
                } else if (saleNodeatilViews.getCustomertypeid().equals("CT-ST")) {
                    saleNodeatilViews.setCustomertypeid("门店客户");
                } else if (saleNodeatilViews.getCustomertypeid().equals("CT-LS")) {
                    saleNodeatilViews.setCustomertypeid("零售客户");
                } else {
                    saleNodeatilViews.setCustomertypeid("");
                }
            } else {
                saleNodeatilViews.setCustomertypeid("");
            }

        }
        return this.returnSuccessInfo("获取成功", page.getRows());
    }

    @RequestMapping(value = "/WxfindSaleBybusinessnameList.do")
    @ResponseBody
    public MessageBox WxfindSaleBybusinessnameList(String pageSize, String pageNo) {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        List<SaleBybusinessname> saleBybusinessnames = null;
        try {
            saleBybusinessnames = this.saleNodeatilViewsService.WxfindSaleBybusinessnameList(pageSize, pageNo, filters);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.returnSuccessInfo("获取成功", saleBybusinessnames);
    }

    @RequestMapping(value = "/WxfindSaleByorignameList.do")
    @ResponseBody
    public MessageBox WxfindSaleByorignameList(String pageSize, String pageNo) {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        List<SaleBybusinessname> saleBybusinessnames = null;
        try {
            saleBybusinessnames = this.saleNodeatilViewsService.WxfindSaleByorignameList(pageSize, pageNo, filters);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.returnSuccessInfo("获取成功", saleBybusinessnames);
    }

    @RequestMapping(value = "/WxfindSalebystyleidList.do")
    @ResponseBody
    public MessageBox WxfindSalebystyleidList(String pageSize, String pageNo) {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        List<Salebystyleid> salebystyleids = null;
        try {
            salebystyleids = this.saleNodeatilViewsService.WxfindSalebystyleidList(pageSize, pageNo, filters);
            String rootPath = this.getSession().getServletContext().getRealPath("/");
            for (Salebystyleid salebystyleid : salebystyleids) {
               /* File file =  new File(rootPath + "/product/photo/" + salebystyleid.getStyleid());
                if(file.exists()){
                    File[] files = file.listFiles();
                    if(files.length > 0){
                        File[] photos = files[0].listFiles();
                        if(photos.length > 0){
                            salebystyleid.setUrl("/product/photo/" + salebystyleid.getStyleid()+"/"+files[0].getName()+"/"+photos[0].getName());
                        }
                    }
                }*/
                String url = StyleUtil.returnImageUrl(salebystyleid.getStyleid(), rootPath);
                salebystyleid.setUrl(url);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.returnSuccessInfo("获取成功", salebystyleids);
    }


}
