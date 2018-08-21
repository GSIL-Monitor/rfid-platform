package com.casesoft.dmc.controller.search;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.controller.product.StyleUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.core.util.json.JSONUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.dao.search.TransferorderCountDao;
import com.casesoft.dmc.model.logistics.TransferOrderBill;
import com.casesoft.dmc.model.search.TransByOrig;
import com.casesoft.dmc.model.search.TransByStyleId;
import com.casesoft.dmc.model.search.TransferorderCountView;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.logistics.TransferOrderBillService;
import com.casesoft.dmc.service.sys.impl.UnitService;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
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

import java.io.*;
import java.util.*;

/**
 * Created by Administrator on 2018/3/1.
 */
@Controller
@RequestMapping("/search/transferorderCountViewSearch")
public class TransferorderCountViewSearch extends BaseController {
    @Autowired
    TransferorderCountDao transferorderCountDao;
    @Autowired
    private UnitService unitService;
    @Autowired
    private TransferOrderBillService transferOrderBillService;
    @Override
    public String index() {
        return null;
    }
    @RequestMapping(value = "/index")
    public ModelAndView indexMV() throws Exception {
        ModelAndView mv = new ModelAndView("/views/search/TransferorderCountViewSearch");
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

        DataSourceResult dataResult = transferorderCountDao.getList(request);
        List<?> data = dataResult.getData();
        String rootPath = session.getServletContext().getRealPath("/");
        List<TransferorderCountView> datanew =new ArrayList<TransferorderCountView>();
        for(int i=0;i<data.size();i++){
            TransferorderCountView saleorderCountView = (TransferorderCountView) data.get(i);

           /* File file =  new File(rootPath + "/product/photo/" + saleorderCountView.getStyleId());
            if(file.exists()){
                File[] files = file.listFiles();
                if(files.length > 0){
                    File[] photos = files[0].listFiles();
                    if(photos.length > 0){
                        saleorderCountView.setUrl("/product/photo/" + saleorderCountView.getStyleId()+"/"+files[0].getName()+"/"+photos[0].getName());
                    }
                }
            }*/
            String url = StyleUtil.returnImageUrl(saleorderCountView.getStyleId(), rootPath);
            saleorderCountView.setUrl(url);
            datanew.add(saleorderCountView);
        }
        dataResult.setData(datanew);
        return dataResult;
    }
    @RequestMapping(value = "/listreadTran", method = RequestMethod.POST)
    public @ResponseBody
    DataSourceResult readTran(@RequestBody DataSourceRequest request) {

        DataSourceResult dataResult = transferorderCountDao.getTranList(request);

        return dataResult;
    }
    @RequestMapping(value = "/readTransByStyleId", method = RequestMethod.POST)
    public @ResponseBody
    DataSourceResult readTransByStyleId(@RequestBody DataSourceRequest request) {

        DataSourceResult dataResult = null;
        try {
            dataResult = transferorderCountDao.getTransByStyleId(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataResult;
    }
    @RequestMapping(value = "/readTransByOrig", method = RequestMethod.POST)
    public @ResponseBody
    DataSourceResult readTransByOrig(@RequestBody DataSourceRequest request) {

        DataSourceResult dataResult = null;
        try {
            dataResult = transferorderCountDao.getTransByOrig(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataResult;
    }
    @RequestMapping(value = "/readTransBystyleandsize", method = RequestMethod.POST)
    public @ResponseBody
    DataSourceResult readTransBystyleandsize(@RequestBody DataSourceRequest request) {

        DataSourceResult dataResult = null;
        try {
            long startTime = System.currentTimeMillis();
            dataResult = transferorderCountDao.getTransBystyleandsize(request);
            long endTime = System.currentTimeMillis();
            logger.error("查询款式和颜色分组的时间："+(endTime-startTime));
            ArrayList<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
            list=(ArrayList<Map<String,Object>>)dataResult.getData();
            long startSizeTime = System.currentTimeMillis();
            String rootPath = this.getSession().getServletContext().getRealPath("/");
            List<Map<String, Object>> maps = this.transferOrderBillService.fillTransMap(list,rootPath);
            long endSizeTime = System.currentTimeMillis();
            logger.error("查询款式和颜色分组尺寸的时间："+(endTime-startTime));
            dataResult.setData(maps);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataResult;
    }
    @RequestMapping(value = "/findtitledate", method = RequestMethod.POST)
    @ResponseBody
    public MessageBox findtitledate(String dates){
        System.out.print(dates);
        Map<String,String> map4Json = JSONUtil.getMap4Json(dates);
        String hqlbillNo="select t.billNo from TransferorderCountView t where 1=1";
        String hqlstyleid="select t.styleId from TransferorderCountView t where 1=1";
        String hqlsumnum="select sum(t.qty) from TransferorderCountView t where 1=1";
        String hqlorigid="select t.origId from TransferorderCountView t where 1=1";
        String hqldestid="select t.destId from TransferorderCountView t where 1=1";
        Iterator<Map.Entry<String, String>> iterator = map4Json.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            String key = next.getKey();
            String value=map4Json.get(key);
            if(CommonUtil.isNotBlank(value)){
                if(key.equals("filter_gte_billDate")){
                    String[] split = key.split("_");
                    String time =value+" 00:00:00";
                    hqlbillNo+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqlstyleid+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqlsumnum+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqlorigid+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqldestid+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";

                }else if(key.equals("filter_lte_billDate")){
                    String[] split = key.split("_");
                    String time =value+" 23:59:59";
                    hqlbillNo+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqlstyleid+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqlsumnum+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqlorigid+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqldestid+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";

                }else {
                    String[] split = key.split("_");
                    if(split[1].equals("contains")){
                        hqlbillNo+=" and "+split[2]+" like '%"+value+"%'";
                        hqlstyleid+=" and "+split[2]+" like'%"+value+"%'";
                        hqlsumnum+=" and "+split[2]+" like '%"+value+"%'";
                        hqlorigid+=" and "+split[2]+" like '%"+value+"%'";
                        hqldestid+=" and "+split[2]+" like '%"+value+"%'";

                    }else{
                        hqlbillNo+=" and "+split[2]+"='"+value+"'";
                        hqlstyleid+=" and "+split[2]+"='"+value+"'";
                        hqlsumnum+=" and "+split[2]+"='"+value+"'";
                        hqlorigid+=" and "+split[2]+"='"+value+"'";
                        hqldestid+=" and "+split[2]+"='"+value+"'";

                    }


                }

            }
        }
        hqlbillNo+=" group by t.billNo ";
        hqlstyleid+=" group by t.styleId ";
        hqlorigid+=" group by t.origId ";
        hqldestid+=" group by t.destId ";
        Map<String,String> map= new HashMap<String,String>();
        List<Object> billNos = this.transferOrderBillService.findgroupSum(hqlbillNo);
        if(CommonUtil.isNotBlank(billNos)){
            map.put("billNos",billNos.size()+"");
        }else{
            map.put("billNos","0");
        }
        List<Object> styleids = this.transferOrderBillService.findgroupSum(hqlstyleid);
        if(CommonUtil.isNotBlank(styleids)){
            map.put("styleids",styleids.size()+"");
        }else{
            map.put("styleids","0");
        }
        Long aDouble = this.transferOrderBillService.findtransferCountnum(hqlsumnum);
        if(CommonUtil.isNotBlank(aDouble)){
            map.put("sumnum",aDouble+"");
        }else{
            map.put("sumnum","0");
        }
        List<Object> origids = this.transferOrderBillService.findgroupSum(hqlorigid);
        if(CommonUtil.isNotBlank(origids)){
            map.put("origids",origids.size()+"");
        }else{
            map.put("origids","0");
        }
        List<Object> destids = this.transferOrderBillService.findgroupSum(hqldestid);
        if(CommonUtil.isNotBlank(origids)){
            map.put("destids",destids.size()+"");
        }else{
            map.put("destids","0");
        }
        return new MessageBox(true, "成功",map);
    }
    @RequestMapping(value = "/export")
    @ResponseBody
    public void export(String gridId, String request) throws IOException {
        DataSourceRequest dataSourceRequest = JSON.parseObject(request, DataSourceRequest.class);
        try{
            if(gridId.equals("searchGrid")) {

                long startTime = System.currentTimeMillis();
                DataSourceResult sourceResultSaleDtl = this.transferorderCountDao.getList(dataSourceRequest);
                List<TransferorderCountView> SaleDtlViewList = (List<TransferorderCountView>) sourceResultSaleDtl.getData();
                String rootPath = session.getServletContext().getRealPath("/");
                for(int i=0;i<SaleDtlViewList.size();i++){
                    String url = StyleUtil.returnImageUrl(SaleDtlViewList.get(i).getStyleId(), rootPath);
                    SaleDtlViewList.get(i).setUrl(url);
                }
                ExportParams params = new ExportParams("调拨明细", "sheet1", ExcelType.XSSF);
                //ExportParams params = new ExportParams("大数据测试", "测试");
                String path = Constant.Folder.Report_File_Folder;
                String dateString = CommonUtil.getDateString(new Date(), "yyyyMMdd HH_mm_ss");
                File files = new File(path + "\\调拨明细-" + dateString + ".xlsx");

                if (!files.exists())
                    files.mkdirs();
                File file = new File(files, "调拨明细-" + dateString + ".xlsx");
                //Workbook workbook = ExcelExportUtil.exportExcel(params, TransferorderCountView.class, SaleDtlViewList);
                Workbook workbook = ExcelExportUtil.exportBigExcel(params, TransferorderCountView.class, SaleDtlViewList);
                ExcelExportUtil.closeExportBigExcel();
                //String dateString = CommonUtil.getDateString(new Date(), "yyyyMMdd HH_mm_ss");
                // FileOutputStream fos = new FileOutputStream(path + "\\销售明细-" +  dateString + ".xlsx");
                FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());
                workbook.write(fos);
                fos.close();
                long endTime = System.currentTimeMillis();
                System.out.println("库存调拨明细导出："+(endTime - startTime));
                logger.error("库存调拨明细导出："+(endTime - startTime));
                FileWriter fileWriter = new FileWriter(file.getName(), true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.close();
                String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;";
                this.outFile("调拨明细-" + dateString + ".xlsx", file, contentType);
            }else if(gridId.equals("searchTranGrid")){
                long startTime = System.currentTimeMillis();
                DataSourceResult sourceResultSaleDtl = this.transferorderCountDao.getTranList(dataSourceRequest);
                List<TransferOrderBill> TransferOrderList = (List<TransferOrderBill>) sourceResultSaleDtl.getData();
                String rootPath = session.getServletContext().getRealPath("/");
                ExportParams params = new ExportParams("调拨", "sheet1", ExcelType.XSSF);
                //ExportParams params = new ExportParams("大数据测试", "测试");
                String path = Constant.Folder.Report_File_Folder;
                String dateString = CommonUtil.getDateString(new Date(), "yyyyMMdd HH_mm_ss");
                File files = new File(path + "\\调拨-" + dateString + ".xlsx");

                if (!files.exists())
                    files.mkdirs();
                File file = new File(files, "调拨-" + dateString + ".xlsx");
                //Workbook workbook = ExcelExportUtil.exportExcel(params, TransferorderCountView.class, SaleDtlViewList);
                Workbook workbook = ExcelExportUtil.exportBigExcel(params, TransferOrderBill.class, TransferOrderList);
                ExcelExportUtil.closeExportBigExcel();
                //String dateString = CommonUtil.getDateString(new Date(), "yyyyMMdd HH_mm_ss");
                // FileOutputStream fos = new FileOutputStream(path + "\\销售明细-" +  dateString + ".xlsx");
                FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());
                workbook.write(fos);
                fos.close();
                long endTime = System.currentTimeMillis();
                System.out.println("库存调拨导出："+(endTime - startTime));
                logger.error("库存调拨导出："+(endTime - startTime));
                FileWriter fileWriter = new FileWriter(file.getName(), true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.close();
                String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;";
                this.outFile("调拨-" + dateString + ".xlsx", file, contentType);
            }else if(gridId.equals("searchTranStyleGrid")){
                long startTime = System.currentTimeMillis();
                DataSourceResult sourceResultSaleDtl = this.transferorderCountDao.getTransByStyleId(dataSourceRequest);
                List<TransByStyleId> TransByStyleIdList = (List<TransByStyleId>) sourceResultSaleDtl.getData();

                ExportParams params = new ExportParams("调拨按商品汇总", "sheet1", ExcelType.XSSF);
                //ExportParams params = new ExportParams("大数据测试", "测试");
                String path = Constant.Folder.Report_File_Folder;
                String dateString = CommonUtil.getDateString(new Date(), "yyyyMMdd HH_mm_ss");
                File files = new File(path + "\\调拨按商品汇总-" + dateString + ".xlsx");

                if (!files.exists())
                    files.mkdirs();
                File file = new File(files, "调拨按商品汇总-" + dateString + ".xlsx");
                //Workbook workbook = ExcelExportUtil.exportExcel(params, TransferorderCountView.class, SaleDtlViewList);
                Workbook workbook = ExcelExportUtil.exportBigExcel(params, TransByStyleId.class, TransByStyleIdList);
                ExcelExportUtil.closeExportBigExcel();
                //String dateString = CommonUtil.getDateString(new Date(), "yyyyMMdd HH_mm_ss");
                // FileOutputStream fos = new FileOutputStream(path + "\\销售明细-" +  dateString + ".xlsx");
                FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());
                workbook.write(fos);
                fos.close();
                long endTime = System.currentTimeMillis();
                System.out.println("调拨按商品汇总导出："+(endTime - startTime));
                logger.error("调拨按商品汇总导出："+(endTime - startTime));
                FileWriter fileWriter = new FileWriter(file.getName(), true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.close();
                String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;";
                this.outFile("调拨按商品汇总-" + dateString + ".xlsx", file, contentType);
            }else if(gridId.equals("searchTransByOrigGrid")){
                long startTime = System.currentTimeMillis();
                DataSourceResult transByOrigDtl = this.transferorderCountDao.getTransByOrig(dataSourceRequest);
                List<TransByOrig> TransByOrigList = (List<TransByOrig>) transByOrigDtl.getData();

                ExportParams params = new ExportParams("调拨按仓库汇总", "sheet1", ExcelType.XSSF);
                //ExportParams params = new ExportParams("大数据测试", "测试");
                String path = Constant.Folder.Report_File_Folder;
                String dateString = CommonUtil.getDateString(new Date(), "yyyyMMdd HH_mm_ss");
                File files = new File(path + "\\调拨按仓库汇总-" + dateString + ".xlsx");

                if (!files.exists())
                    files.mkdirs();
                File file = new File(files, "调拨按仓库汇总-" + dateString + ".xlsx");
                //Workbook workbook = ExcelExportUtil.exportExcel(params, TransferorderCountView.class, SaleDtlViewList);
                Workbook workbook = ExcelExportUtil.exportBigExcel(params, TransByOrig.class, TransByOrigList);
                ExcelExportUtil.closeExportBigExcel();
                //String dateString = CommonUtil.getDateString(new Date(), "yyyyMMdd HH_mm_ss");
                // FileOutputStream fos = new FileOutputStream(path + "\\销售明细-" +  dateString + ".xlsx");
                FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());
                workbook.write(fos);
                fos.close();
                long endTime = System.currentTimeMillis();
                System.out.println("调拨按仓库汇总导出："+(endTime - startTime));
                logger.error("调拨按仓库汇总导出："+(endTime - startTime));
                FileWriter fileWriter = new FileWriter(file.getName(), true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.close();
                String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;";
                this.outFile("调拨按仓库汇总-" + dateString + ".xlsx", file, contentType);
            }else if(gridId.equals("searchTransByStyleIdandSizeIdGrid")){
                DataSourceResult dataResult = this.transferorderCountDao.getTransBystyleandsize(dataSourceRequest);
                List<ExcelExportEntity> entity = new ArrayList<ExcelExportEntity>();
                ArrayList<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
                list=(ArrayList<Map<String,Object>>)dataResult.getData();
                //拼接表头map
                Map<String,Object> keynmap=list.get(0);
                Iterator it = keynmap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    Object key = entry.getKey();
                    if(key.equals("styleid")){
                        ExcelExportEntity excelentity = new ExcelExportEntity("款号", key);
                        excelentity.setWidth(40D);
                        entity.add(excelentity);
                    }
                    if(key.equals("colorid")){
                        ExcelExportEntity excelentity = new ExcelExportEntity("款号", key);
                        excelentity.setWidth(40D);
                        entity.add(excelentity);
                    }
                    if(key.equals("billno")){
                        ExcelExportEntity excelentity = new ExcelExportEntity("款号", key);
                        excelentity.setWidth(40D);
                        entity.add(excelentity);
                    }
                    if(key.equals("styleName")){
                        ExcelExportEntity excelentity = new ExcelExportEntity("款名", key);
                        excelentity.setWidth(40D);
                        entity.add(excelentity);
                    }

                }
                String sizeArray = PropertyUtil.getValue("sizeArray");
                String[] sizeArrays = sizeArray.split(",");
                for(int b=0;b<sizeArrays.length;b++){
                    ExcelExportEntity excelentity = new ExcelExportEntity(sizeArrays[b], sizeArrays[b]);
                    excelentity.setWidth(40D);
                    entity.add(excelentity);
                }
                ExcelExportEntity excelentity = new ExcelExportEntity("图片", "Url");
                excelentity.setHeight(30D);
                excelentity.setWidth(30D);
                excelentity.setExportImageType(1);
                excelentity.setType(2);
                entity.add(excelentity);
                long startSizeTime = System.currentTimeMillis();
                String rootPath = this.getSession().getServletContext().getRealPath("/");
                List<Map<String, Object>> maps = this.transferOrderBillService.fillTransMap(list,rootPath);
                Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("调拨按商品尺码汇总", "sheet1", ExcelType.XSSF), entity,
                        maps);
                String path = Constant.Folder.Report_File_Folder;
                String dateString = CommonUtil.getDateString(new Date(), "yyyyMMdd HH_mm_ss");
                File files = new File(path + "\\调拨按商品尺码汇总");

                if (!files.exists())
                    files.mkdirs();
                File file = new File(files, "调拨按商品尺码汇总-" + dateString + ".xlsx");
                file.createNewFile();
           /* for(DetailStockChatView d : list){
                if(CommonUtil.isBlank(d.getUrl())){
                    //没有图片设置默认图片
                    d.setUrl("/product/photo/noImg.png");
                }
            }*/

                FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());
                workbook.write(fos);
                FileWriter fileWriter = new FileWriter(file.getName(), true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.close();
                String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;";
                this.outFile("调拨按商品尺码汇总-" + dateString + ".xlsx", file, contentType);
            }
            //return null;
            //return new MessageBox(true, "导出成功，请在桌面查看");
        }catch (Exception e){
            e.printStackTrace();
            // return new MessageBox(false, "导出失败");
        }
    }


}
