package com.casesoft.dmc.controller.search;


import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.product.StyleUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.json.FastJSONUtil;
import com.casesoft.dmc.core.util.json.JSONUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.dao.search.SaleorderCountDao;
import com.casesoft.dmc.model.logistics.SaleByOrignames;
import com.casesoft.dmc.model.logistics.SaleBybusinessname;
import com.casesoft.dmc.model.search.SaleNodeatilViews;
import com.casesoft.dmc.model.search.SaleorderCountView;
import com.casesoft.dmc.model.search.saleorderCount;
import com.casesoft.dmc.model.sys.ResourcePrivilege;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.logistics.SaleOrderBillService;
import com.casesoft.dmc.service.sys.ResourcePrivilegeService;
import com.casesoft.dmc.service.sys.impl.UnitService;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.jeecgframework.poi.excel.entity.params.ExcelExportEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.swing.filechooser.FileSystemView;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Administrator on 2017/8/24.
 */
@Controller
@RequestMapping("/search/saleorderFranchisee")
public class SaleorderFranchiseeSearch extends BaseController{

    @Autowired
    SaleorderCountDao saleorderCountDao;
    @Autowired
    private UnitService unitService;
    @Autowired
    private SaleOrderBillService saleOrderBillService;
    @Autowired
    private ResourcePrivilegeService resourcePrivilegeService;
    //@RequestMapping(value = "/index")
    public String index() {
        return "/views/search/SaleorderFranchiseeSearch";
    }
    @RequestMapping(value = "/index")
    public ModelAndView indexMV() throws Exception {
        ModelAndView mv = new ModelAndView("/views/search/SaleorderFranchiseeSearch");
        User currentUser = getCurrentUser();
        List<ResourcePrivilege> resourceButtontableList = this.resourcePrivilegeService.findButtonByCodeAndRoleId("/search/saleorderFranchiseeSearch",currentUser.getRoleId(),"table");
        List<ResourcePrivilege> resourceButtondivList = this.resourcePrivilegeService.findButtonByCodeAndRoleId("/search/saleorderFranchiseeSearch",currentUser.getRoleId(),"div");
        mv.addObject("tableRole", FastJSONUtil.getJSONString(resourceButtontableList));
        mv.addObject("divRole", FastJSONUtil.getJSONString(resourceButtondivList));
        String ownerId = currentUser.getOwnerId();
        boolean isJMS = false;
        if(CommonUtil.isNotBlank(ownerId)) {
            Unit unitById = CacheManager.getUnitById(ownerId);
            if (CommonUtil.isBlank(unitById)) {
                unitById = this.unitService.getunitbyId(ownerId);
            }
            if (CommonUtil.isNotBlank(unitById)) {
                if (CommonUtil.isNotBlank(unitById.getGroupId())) {
                    if (unitById.getGroupId().equals("JMS")) {
                        isJMS = true;
                    }
                }
            }
        }

        mv.addObject("isJMS",isJMS);
        mv.addObject("ownerId", ownerId);
        Unit unit = this.unitService.getunitbyId(ownerId);
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
            String url = StyleUtil.returnImageUrl(saleorderCountView.getStyleid(), rootPath);
            saleorderCountView.setUrl(url);
            datanew.add(saleorderCountView);
        }
        dataResult.setData(datanew);
        return dataResult;
    }
    @RequestMapping(value = "/listsale", method = RequestMethod.POST)
    public @ResponseBody
    DataSourceResult readsale(@RequestBody DataSourceRequest request) {
        DataSourceResult dataResult = saleorderCountDao.getSaleList(request);
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
    //根据权限获得导出字段
    private List<ExcelExportEntity> getExcelField(List<ExcelExportEntity> entityList,Class clazz,boolean precast,boolean gross,boolean grossprofits){
        // 获取实体类的所有属性信息，返回Field数组
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if(!field.isAccessible()){
                field.setAccessible(true);
            }
            //获取字段上注解的名称
            Excel excel = field.getAnnotation(Excel.class);
            if("gross".equals(field.getName())){
                if(gross){
                    entityList.add(new ExcelExportEntity(excel.name(),field.getName(),25));
                }
            }
            else if("precast".equals(field.getName())){
                if(precast){
                    entityList.add(new ExcelExportEntity(excel.name(),field.getName(),25));
                }

            }
            else if("grossprofits".equals(field.getName())){
                if(grossprofits){
                    entityList.add(new ExcelExportEntity(excel.name(),field.getName(),25));
                }
            }
            else {
                entityList.add(new ExcelExportEntity(excel.name(),field.getName(),25));
            }
        }
        return entityList;
    }


    @RequestMapping(value = "/export")
    @ResponseBody
    public void export(String gridId, String request) throws IOException {
        DataSourceRequest dataSourceRequest = JSON.parseObject(request, DataSourceRequest.class);
        //根据权限获得字段
        User currentUser = getCurrentUser();
        List<ResourcePrivilege> resourceButtondivList = this.resourcePrivilegeService.findButtonByCodeAndRoleId("/search/saleorderCountView",currentUser.getRoleId(),"table");
        boolean precast= false;
        boolean gross = false;
        boolean grossprofits = false;
        for (ResourcePrivilege resourcePrivilege : resourceButtondivList){
            if("precast".equals(resourcePrivilege.getPrivilegeId())){
                if(resourcePrivilege.getIsShow() == 0){
                    precast = true;
                }
            }
            if("gross".equals(resourcePrivilege.getPrivilegeId())){
                if(resourcePrivilege.getIsShow() == 0){
                    gross = true;
                }
            }
            if("grossprofits".equals(resourcePrivilege.getPrivilegeId())){
                if(resourcePrivilege.getIsShow() == 0){
                    grossprofits = true;
                }
            }
        }
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
                Long startTime= System.currentTimeMillis();
                DataSourceResult sourceResultBillSum = this.saleorderCountDao.getSaleList(dataSourceRequest);
                Long endtTime= System.currentTimeMillis();
                logger.error("查询销售单据所需的时间:"+(endtTime-startTime));
                Long exportstartTime= System.currentTimeMillis();
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
                Long exportendtTime= System.currentTimeMillis();
                logger.error("导出销售单据所需的时间:"+(exportendtTime-exportstartTime));
            }else if(gridId.equals("searchsalebusinessnameGrid")){
                Long startTime= System.currentTimeMillis();
                DataSourceResult sourceResultbusinessnameDtl = this.saleorderCountDao.getSaleBybusinessnameList(dataSourceRequest);
                Long endtTime= System.currentTimeMillis();
                logger.error("查询按销售员汇总所需的时间:"+(endtTime-startTime));
                Long exportstartTime= System.currentTimeMillis();
                List<SaleBybusinessname> SalebusinessnameList = (List<SaleBybusinessname>) sourceResultbusinessnameDtl.getData();

                //反射获得类字段
                Class clazz = SaleBybusinessname.class;
                List<ExcelExportEntity> entityList = new ArrayList<ExcelExportEntity>();
                entityList = this.getExcelField(entityList,clazz,precast,gross,grossprofits);
                List<Map<String,Object>> dataResult = new ArrayList<>();
                for(SaleBybusinessname saleBybusinessname :SalebusinessnameList){
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("busnissname",saleBybusinessname.getBusnissname());
                    map.put("origname",saleBybusinessname.getOrigname());
                    map.put("salesum",saleBybusinessname.getSalesum());
                    map.put("salereturnsum",saleBybusinessname.getSalereturnsum());
                    map.put("salemoney",saleBybusinessname.getSalemoney());
                    map.put("salereturnmoney",saleBybusinessname.getSalereturnmoney());
                    map.put("totactprice",saleBybusinessname.getTotactprice());
                    if(gross){
                        map.put("gross",saleBybusinessname.getGross());
                    }
                    if(precast){
                        map.put("precast",saleBybusinessname.getPrecast());
                    }
                    if(grossprofits){
                        map.put("grossprofits",saleBybusinessname.getGrossprofits());
                    }
                    dataResult.add(map);
                }
                ExportParams params = new ExportParams("按销售员汇总", "sheet1", ExcelType.XSSF);
                String path = Constant.Folder.Report_File_Folder;
                String dateString = CommonUtil.getDateString(new Date(), "yyyyMMdd HH_mm_ss");
                File files=new File(path + "\\按销售员汇总-" +  dateString + ".xlsx");

                if(!files.exists())
                    files.mkdirs();
                File file =new File(files,"按销售员汇总-" +  dateString + ".xlsx");
                Workbook workbook = ExcelExportUtil.exportExcel(params,entityList, dataResult);


               /* ExportParams params = new ExportParams("按销售员汇总", "sheet1", ExcelType.XSSF);
                String path = Constant.Folder.Report_File_Folder;
                String dateString = CommonUtil.getDateString(new Date(), "yyyyMMdd HH_mm_ss");
                File files=new File(path + "\\按销售员汇总-" +  dateString + ".xlsx");

                if(!files.exists())
                    files.mkdirs();
                File file =new File(files,"按销售员汇总-" +  dateString + ".xlsx");
                //Workbook workbook = ExcelExportUtil.exportExcel(params, SaleorderCountView.class, SaleDtlViewList);
                Workbook workbook = ExcelExportUtil.exportBigExcel(params, SaleBybusinessname.class, SalebusinessnameList);*/
                //ExcelExportUtil.closeExportExcel();
                //String dateString = CommonUtil.getDateString(new Date(), "yyyyMMdd HH_mm_ss");
                // FileOutputStream fos = new FileOutputStream(path + "\\销售明细-" +  dateString + ".xlsx");
                FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());
                workbook.write(fos);
                fos.close();
                FileWriter fileWriter=new FileWriter(file.getName(),true);
                BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
                bufferedWriter.close();
                String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;";
                this.outFile("按销售员汇总-" +  dateString + ".xlsx", file, contentType);
                Long exportendtTime= System.currentTimeMillis();
                logger.error("导出按销售员汇总所需的时间:"+(exportendtTime-exportstartTime));
            }else if(gridId.equals("searchsaleorignameGrid")){
                Long startTime= System.currentTimeMillis();
                DataSourceResult sourceResultbusinessnameDtl = this.saleorderCountDao.getSaleByorignameList(dataSourceRequest);
                Long endtTime= System.currentTimeMillis();
                logger.error("查询按部门汇总所需的时间:"+(endtTime-startTime));
                Long exportstartTime= System.currentTimeMillis();
                List<SaleByOrignames> SaleOrignamesList = (List<SaleByOrignames>) sourceResultbusinessnameDtl.getData();
                //反射获得类字段
                Class clazz = SaleByOrignames.class;
                List<ExcelExportEntity> entityList = new ArrayList<ExcelExportEntity>();
                entityList = this.getExcelField(entityList,clazz,precast,gross,grossprofits);
                List<Map<String,Object>> dataResult = new ArrayList<>();
                for(SaleByOrignames saleByOrignames :SaleOrignamesList){
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("origname",saleByOrignames.getOrigname());
                    map.put("salesum",saleByOrignames.getSalesum());
                    map.put("salereturnsum",saleByOrignames.getSalereturnsum());
                    map.put("salemoney",saleByOrignames.getSalemoney());
                    map.put("salereturnmoney",saleByOrignames.getSalereturnmoney());
                    map.put("totactprice",saleByOrignames.getTotactprice());
                    if(gross){
                        map.put("gross",saleByOrignames.getGross());
                    }
                    if(precast){
                        map.put("precast",saleByOrignames.getPrecast());
                    }
                    if(grossprofits){
                        map.put("grossprofits",saleByOrignames.getGrossprofits());
                    }
                    dataResult.add(map);
                }
                ExportParams params = new ExportParams("按部门汇总", "sheet1", ExcelType.XSSF);
                String path = Constant.Folder.Report_File_Folder;
                String dateString = CommonUtil.getDateString(new Date(), "yyyyMMdd HH_mm_ss");
                File files=new File(path + "\\按部门汇总-" +  dateString + ".xlsx");

                if(!files.exists())
                    files.mkdirs();
                File file =new File(files,"按部门汇总-" +  dateString + ".xlsx");
                //Workbook workbook = ExcelExportUtil.exportExcel(params, SaleorderCountView.class, SaleDtlViewList);
                Workbook workbook = ExcelExportUtil.exportExcel(params,entityList, dataResult);
                //ExcelExportUtil.closeExportBigExcel();
                //String dateString = CommonUtil.getDateString(new Date(), "yyyyMMdd HH_mm_ss");
                // FileOutputStream fos = new FileOutputStream(path + "\\销售明细-" +  dateString + ".xlsx");
                FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());
                workbook.write(fos);
                fos.close();
                FileWriter fileWriter=new FileWriter(file.getName(),true);
                BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
                bufferedWriter.close();
                String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;";
                this.outFile("按部门汇总-" +  dateString + ".xlsx", file, contentType);
                Long exportendtTime= System.currentTimeMillis();
                logger.error("导出按部门汇总所需的时间:"+(exportendtTime-exportstartTime));
            }
            //return null;
            //return new MessageBox(true, "导出成功，请在桌面查看");
        }catch (Exception e){
            e.printStackTrace();
           // return new MessageBox(false, "导出失败");
        }
    }
    

    @RequestMapping(value = "/findtitledate", method = RequestMethod.POST)
    @ResponseBody
    public MessageBox findtitledate(String dates){
        System.out.print(dates);
        Map<String,String> map4Json = JSONUtil.getMap4Json(dates);
        String hqlsqty="select sum(t.qty), sum(t.totactprice),sum(t.gross) from saleorderCountOViews t where 1=1";
        String hqlrqty="select sum(t.qty), sum(t.totactprice),sum(t.gross) from saleorderCountRViews t where 1=1";
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
                    hqlrqty+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                }else if(key.equals("filter_lte_billDate")){
                    String[] split = key.split("_");
                    String time =value+" 23:59:59";
                    hqlsqty+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqlrqty+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                }else {
                    String[] split = key.split("_");
                    if(split[1].equals("contains")){
                        hqlsqty+=" and "+split[2]+" like '%"+value+"%'";
                        hqlrqty+=" and "+split[2]+" like '%"+value+"%'";
                    }else{
                        hqlsqty+=" and "+split[2]+"='"+value+"'";
                        hqlrqty+=" and "+split[2]+"='"+value+"'";
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
