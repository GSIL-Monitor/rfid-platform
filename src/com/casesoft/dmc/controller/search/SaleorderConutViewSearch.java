package com.casesoft.dmc.controller.search;


import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.json.JSONUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.dao.search.SaleorderCountDao;
import com.casesoft.dmc.model.logistics.BillConstant;
import com.casesoft.dmc.model.search.SaleNodeatilViews;
import com.casesoft.dmc.model.search.SaleorderCountView;
import com.casesoft.dmc.model.search.saleorderCount;
import com.casesoft.dmc.model.stock.InventoryMergeBillDtl;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.logistics.SaleOrderBillService;
import com.casesoft.dmc.service.sys.impl.UnitService;
import net.sf.json.JSONArray;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.filechooser.FileSystemView;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by Administrator on 2017/8/24.
 */
@Controller
@RequestMapping("/search/saleorderCountView")
public class SaleorderConutViewSearch extends BaseController {

    @Autowired
    SaleorderCountDao saleorderCountDao;
    @Autowired
    private UnitService unitService;
    @Autowired
    private SaleOrderBillService saleOrderBillService;
    //@RequestMapping(value = "/index")
    public String index() {
        return "/views/search/SaleorderCountViewSearch";
    }
    @RequestMapping(value = "/index")
    public ModelAndView indexMV() throws Exception {
        ModelAndView mv = new ModelAndView("/views/search/SaleorderCountViewSearch");
        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        User currentUser = getCurrentUser();
        Unit unit = this.unitService.getunitbyId(getCurrentUser().getOwnerId());
        mv.addObject("deportId", unit.getDefaultWarehId());
        Unit unit1 = this.unitService.getunitbyId(unit.getDefaultWarehId());
        mv.addObject("deportName", unit1.getName());
        mv.addObject("roleid", currentUser.getRoleId());
        mv.addObject("groupid", unit.getGroupId());
        return mv;
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public @ResponseBody
    DataSourceResult read(@RequestBody DataSourceRequest request) {

        DataSourceResult dataResult = saleorderCountDao.getList(request);
        List<?> data = dataResult.getData();
        String rootPath = session.getServletContext().getRealPath("/");
        List<SaleorderCountView> datanew =new ArrayList<SaleorderCountView>();
        for(int i=0;i<data.size();i++){
            SaleorderCountView saleorderCountView = (SaleorderCountView) data.get(i);
            String billno = saleorderCountView.getBillno();
            if(billno.contains(BillConstant.BillPrefix.saleOrder)){
                saleorderCountView.setSaletype("销售订单");
               /* if(saleorderCountView.getOutqty()==saleorderCountView.getQty()){
                    saleorderCountView.setScreening("N");
                }else{
                    saleorderCountView.setScreening("Y");
                }*/

            }
            if(billno.contains(BillConstant.BillPrefix.SaleOrderReturn)){
                saleorderCountView.setSaletype("销售退货订单");
               /* if(saleorderCountView.getInitqty()==saleorderCountView.getQty()){
                    saleorderCountView.setScreening("N");
                }else{
                    saleorderCountView.setScreening("Y");
                }*/
               /* Integer qty = saleorderCountView.getQty();
                saleorderCountView.setQty(Integer.parseInt("-"+qty));*/
            }
            File file =  new File(rootPath + "/product/photo/" + saleorderCountView.getStyleid());
            if(file.exists()){
                File[] files = file.listFiles();
                if(files.length > 0){
                    File[] photos = files[0].listFiles();
                    if(photos.length > 0){
                        saleorderCountView.setUrl("/product/photo/" + saleorderCountView.getStyleid()+"/"+files[0].getName()+"/"+photos[0].getName());
                    }
                }
            }
            datanew.add(saleorderCountView);
        }
        dataResult.setData(datanew);
        return dataResult;
    }
    @RequestMapping(value = "/listsale", method = RequestMethod.POST)
    public @ResponseBody
    DataSourceResult readsale(@RequestBody DataSourceRequest request) {

        DataSourceResult dataResult = saleorderCountDao.getSaleList(request);
        List<?> data = dataResult.getData();
        List<SaleNodeatilViews> datanew =new ArrayList<SaleNodeatilViews>();
        for(int i=0;i<data.size();i++){
            SaleNodeatilViews saleNodeatilViews = (SaleNodeatilViews) data.get(i);
            String billno = saleNodeatilViews.getBillno();
            if(billno.contains(BillConstant.BillPrefix.saleOrder)){
                saleNodeatilViews.setSaletype("销售订单");
            }
            if(billno.contains(BillConstant.BillPrefix.SaleOrderReturn)){
                saleNodeatilViews.setSaletype("销售退货订单");
                //Integer qty = saleNodeatilViews.getTotqty();
                //saleNodeatilViews.setTotqty(Integer.parseInt("-"+qty));
            }
            datanew.add(saleNodeatilViews);
        }
        dataResult.setData(datanew);
        return dataResult;
    }
    @RequestMapping(value = "/readSaleBybusinessname", method = RequestMethod.POST)
    public @ResponseBody
    DataSourceResult readSaleBybusinessname(@RequestBody DataSourceRequest request) {

        DataSourceResult dataResult = null;
        try {
            dataResult = saleorderCountDao.getSaleBybusinessnameList(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataResult;
    }
    @RequestMapping(value = "/readSaleByorigname", method = RequestMethod.POST)
    public @ResponseBody
    DataSourceResult readSaleByorigname(@RequestBody DataSourceRequest request) {

        DataSourceResult dataResult = null;
        try {
            dataResult = saleorderCountDao.getSaleByorignameList(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataResult;
    }

//    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public @ResponseBody
    void export(String fileName, String base64, String contentType,
                HttpServletResponse response) throws IOException {

        response.setHeader("Content-Disposition", "attachment;filename="
                + fileName);
        response.setContentType(contentType);

        byte[] data = DatatypeConverter.parseBase64Binary(base64);

        response.setContentLength(data.length);
        response.getOutputStream().write(data);
        response.flushBuffer();
    }


    @RequestMapping(value = "/export")
    @ResponseBody
    public void export(String gridId, String request) throws IOException {
        DataSourceRequest dataSourceRequest = JSON.parseObject(request, DataSourceRequest.class);
        try{
            if(gridId.equals("searchGrid")){
               Long startTime= System.currentTimeMillis();
                DataSourceResult sourceResultSaleDtl = this.saleorderCountDao.getList(dataSourceRequest);
                Long endtTime= System.currentTimeMillis();
                logger.error("查询销售明细所需的时间:"+(endtTime-startTime));
                Long exportstartTime= System.currentTimeMillis();
                List<SaleorderCountView> SaleDtlViewList = (List<SaleorderCountView>) sourceResultSaleDtl.getData();
                ExportParams params = new ExportParams("销售明细", "sheet1", ExcelType.XSSF);
                String path = Constant.Folder.Report_File_Folder;
                String dateString = CommonUtil.getDateString(new Date(), "yyyyMMdd HH_mm_ss");
                File files=new File(path + "\\销售明细-" +  dateString + ".xlsx");

                if(!files.exists())
                    files.mkdirs();
                File file =new File(files,"销售明细-" +  dateString + ".xlsx");
                //Workbook workbook = ExcelExportUtil.exportExcel(params, SaleorderCountView.class, SaleDtlViewList);
                Workbook workbook = ExcelExportUtil.exportBigExcel(params, SaleorderCountView.class, SaleDtlViewList);
                ExcelExportUtil.closeExportBigExcel();
                //String dateString = CommonUtil.getDateString(new Date(), "yyyyMMdd HH_mm_ss");
               // FileOutputStream fos = new FileOutputStream(path + "\\销售明细-" +  dateString + ".xlsx");
                FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());
                workbook.write(fos);
                fos.close();
                FileWriter fileWriter=new FileWriter(file.getName(),true);
                BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
                bufferedWriter.close();
                String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;";
                this.outFile("销售明细-" +  dateString + ".xlsx", file, contentType);
                Long exportendtTime= System.currentTimeMillis();
                logger.error("导出销售明细所需的时间:"+(exportendtTime-exportstartTime));
            }else if(gridId.equals("searchsaleGrid")){
                DataSourceResult sourceResultBillSum = this.saleorderCountDao.getSaleList(dataSourceRequest);
                List<SaleNodeatilViews> BillSumViewList = (List<SaleNodeatilViews>) sourceResultBillSum.getData();
                ExportParams params = new ExportParams("按单据汇总", "sheet1", ExcelType.XSSF);
                //Workbook workbook = ExcelExportUtil.exportExcel(params, SaleNodeatilViews.class, BillSumViewList);
                Workbook workbook = ExcelExportUtil.exportBigExcel(params, SaleNodeatilViews.class, BillSumViewList);
                ExcelExportUtil.closeExportBigExcel();
                String path = FileSystemView.getFileSystemView().getHomeDirectory().getPath();
                String dateString = CommonUtil.getDateString(new Date(), "yyyyMMdd HH_mm_ss");
                File files=new File(path + "\\按单据汇总-" +  dateString + ".xlsx");

                if(!files.exists())
                    files.mkdirs();
                File file =new File(files,"按单据汇总-" +  dateString + ".xlsx");
                //FileOutputStream fos = new FileOutputStream(path + "\\按单据汇总-" +  dateString + ".xlsx");
                FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());
                workbook.write(fos);
                fos.close();
                FileWriter fileWriter=new FileWriter(file.getName(),true);
                BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
                bufferedWriter.close();
                String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;";
                this.outFile("按单据汇总-" +  dateString + ".xlsx", file, contentType);
            }
            //return null;
            //return new MessageBox(true, "导出成功，请在桌面查看");
        }catch (IOException e){
            e.printStackTrace();
           // return new MessageBox(false, "导出失败");
        }
    }
    

    @RequestMapping(value = "/findtitledate", method = RequestMethod.POST)
    @ResponseBody
    public MessageBox findtitledate(String dates){
        System.out.print(dates);
        Map<String,String> map4Json = JSONUtil.getMap4Json(dates);
        //String hql="select new saleorderCount (sum(t.qty), sum(t.totactprice)) from saleorderCountOViews t where 1=1";
        String hqlsqty="select sum(t.qty), sum(t.totactprice),sum(t.gross) from saleorderCountOViews t where 1=1";
        //String hqlstotactprice="select  sum(t.totactprice) from saleorderCountOViews t where 1=1";
        String hqlrqty="select sum(t.qty),sum(t.totactprice),sum(t.gross) from saleorderCountRViews t where 1=1";
        //String hqlrtotactprice="select sum(t.totactprice) from saleorderCountRViews t where 1=1";
        //String hqlrtogross="select sum(t.gross) from saleorderCountRViews t where 1=1";
        //String hqlrtogrossall="select sum(t.grossall) from saleorderCountRViews t where 1=1";
        //String hqlstogrossall="select sum(t.grossall) from saleorderCountOViews t where 1=1";
        //String hqlstogross="select sum(t.gross) from saleorderCountOViews t where 1=1";
        Iterator<Map.Entry<String, String>> iterator = map4Json.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            String key = next.getKey();
            String value=map4Json.get(key);
            if(CommonUtil.isNotBlank(value)){
                if(key.equals("filter_gte_billDate")){
                    String[] split = key.split("_");
                    String time =value+" 00:00:00";
                   hqlsqty+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                   // hqlstotactprice+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqlrqty+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
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
                }else if(key.equals("filter_lte_billDate")){
                    String[] split = key.split("_");
                    String time =value+" 23:59:59";
                    hqlsqty+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    //hqlstotactprice+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqlrqty+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
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
                }else {
                    String[] split = key.split("_");
                    if(split[1].equals("contains")){
                        hqlsqty+=" and "+split[2]+" like '%"+value+"%'";
                       // hqlstotactprice+=" and "+split[2]+" like'%"+value+"%'";
                        hqlrqty+=" and "+split[2]+" like '%"+value+"%'";
                        //hqlrtotactprice+=" and "+split[2]+" like '%"+value+"%'";
                        //hqlrtogross+=" and "+split[2]+" like '%"+value+"%'";
                        //hqlrtogrossall+=" and "+split[2]+" like '%"+value+"%'";
                        //hqlstogrossall+=" and "+split[2]+" like '%"+value+"%'";
                        //hqlstogross+=" and "+split[2]+" like '%"+value+"%'";
                    }else{
                        hqlsqty+=" and "+split[2]+"='"+value+"'";
                        //hqlstotactprice+=" and "+split[2]+"='"+value+"'";
                        hqlrqty+=" and "+split[2]+"='"+value+"'";
                        //hqlrtotactprice+=" and "+split[2]+"='"+value+"'";
                        //hqlrtogross+=" and "+split[2]+"='"+value+"'";
                        //hqlrtogrossall+=" and "+split[2]+"='"+value+"'";
                        //hqlstogrossall+=" and "+split[2]+"='"+value+"'";
                        //hqlstogross+=" and "+split[2]+"='"+value+"'";
                    }


                }

            }
        }
        long saleOrdersStartTime= System.currentTimeMillis();
        Object [] saleOrders=(Object []) this.saleOrderBillService.findsaleOrderOrsaleRetrunMessage(hqlsqty);
        long saleOrdersEndTime= System.currentTimeMillis();
        logger.error("执行saleOrders的时间"+(saleOrdersEndTime-saleOrdersStartTime));
        Integer longsqty= null;
        Double longstotactprice=null;

        Double longstogross=null;
        if(CommonUtil.isNotBlank(saleOrders[0])){
            longsqty=Integer.parseInt(saleOrders[0]+"");
        }else{
            longsqty=0;
        }
        if(CommonUtil.isNotBlank(saleOrders[1])){
            longstotactprice=Double.parseDouble(saleOrders[1]+"");
        }else{
            longstotactprice=0D;
        }

        if(CommonUtil.isNotBlank(saleOrders[2])){
            longstogross=Double.parseDouble(saleOrders[2]+"");
        }else{
            longstogross=0D;
        }
        Integer longrqty =null;
        Double longrtotactprice=null;

        Double longrtogross=null;
        long saleRetrunsStartTime= System.currentTimeMillis();
        Object [] saleRetruns=(Object []) this.saleOrderBillService.findsaleOrderOrsaleRetrunMessage(hqlrqty);
        long saleRetrunsEndTime= System.currentTimeMillis();
        logger.error("执行saleRetruns的时间"+(saleRetrunsEndTime-saleRetrunsStartTime));
        if(CommonUtil.isNotBlank(saleRetruns[0])){
            longrqty=Integer.parseInt(saleRetruns[0]+"");
        }else{
            longrqty=0;
        }
        if(CommonUtil.isNotBlank(saleRetruns[1])){
            longrtotactprice=Double.parseDouble(saleRetruns[1]+"");
        }else{
            longrtotactprice=0D;
        }

        if(CommonUtil.isNotBlank(saleRetruns[2])){
            longrtogross=Double.parseDouble(saleRetruns[2]+"");
        }else{
            longrtogross=0D;
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

        saleorderCount saleorderCounts=new saleorderCount();
        saleorderCounts.setSum(longsqty);
        saleorderCounts.setRsum(longrqty);
        Double Allmony=longstotactprice+longrtotactprice;
        BigDecimal b   =   new BigDecimal(Allmony.floatValue());
        saleorderCounts.setAllmony(b.setScale(2,   BigDecimal.ROUND_HALF_UP).floatValue());
        Double Passall=longstogross+longrtogross;
        BigDecimal   b1   =   new BigDecimal(Passall.floatValue());
        saleorderCounts.setPassall(b1.setScale(2,   BigDecimal.ROUND_HALF_UP).floatValue());
        Double Grossprofits=0D;
        if((longstotactprice+longrtotactprice)==0){
            Grossprofits=0D;
        }else{
           Grossprofits=(longstogross+longrtogross)/(longstotactprice+longrtotactprice)*100;
        }
        BigDecimal   b2   =   new BigDecimal(Grossprofits);
        saleorderCounts.setGrossprofits(b2.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue()+"%");
        return new MessageBox(true, "成功",saleorderCounts);


    }


}
