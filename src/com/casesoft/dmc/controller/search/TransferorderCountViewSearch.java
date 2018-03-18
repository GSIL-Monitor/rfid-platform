package com.casesoft.dmc.controller.search;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.controller.product.StyleUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;

import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.ImgUtil;
import com.casesoft.dmc.core.util.json.JSONUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.dao.search.TransferorderCountDao;

import com.casesoft.dmc.model.search.TransferorderCountView;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.logistics.TransferOrderBillService;
import com.casesoft.dmc.service.sys.impl.UnitService;

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

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.*;
import java.util.logging.Logger;

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

            File file =  new File(rootPath + "/product/photo/" + saleorderCountView.getStyleId());
            if(file.exists()){
                File[] files = file.listFiles();
                if(files.length > 0){
                    File[] photos = files[0].listFiles();
                    if(photos.length > 0){
                        saleorderCountView.setUrl("/product/photo/" + saleorderCountView.getStyleId()+"/"+files[0].getName()+"/"+photos[0].getName());
                    }
                }
            }
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
            map.put("billNos","");
        }
        List<Object> styleids = this.transferOrderBillService.findgroupSum(hqlstyleid);
        if(CommonUtil.isNotBlank(styleids)){
            map.put("styleids",styleids.size()+"");
        }else{
            map.put("styleids","");
        }
        Long aDouble = this.transferOrderBillService.findtransferCountnum(hqlsumnum);
        if(CommonUtil.isNotBlank(aDouble)){
            map.put("sumnum",aDouble+"");
        }else{
            map.put("sumnum","");
        }
        List<Object> origids = this.transferOrderBillService.findgroupSum(hqlorigid);
        if(CommonUtil.isNotBlank(origids)){
            map.put("origids",origids.size()+"");
        }else{
            map.put("origids","");
        }
        List<Object> destids = this.transferOrderBillService.findgroupSum(hqldestid);
        if(CommonUtil.isNotBlank(origids)){
            map.put("destids",destids.size()+"");
        }else{
            map.put("destids","");
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
                for (TransferorderCountView d : SaleDtlViewList) {
                    File file =  new File(rootPath + "/product/photo/" + d.getStyleId());
                    if(file.exists()){
                        File[] files = file.listFiles();
                        if(files.length > 0){
                            File[] photos = files[0].listFiles();
                            if(photos.length > 0){
                                //d.setUrl("/product/photo/" + d.getStyleId()+"/"+files[0].getName()+"/"+photos[0].getName());
                                String url = StyleUtil.exportImgUrl(d.getStyleId(), rootPath, ImgUtil.ImgExt.small);
                                d.setUrl(url);
                            }
                        }
                    }
                    if (CommonUtil.isBlank(d.getUrl())) {
                        //没有图片设置默认图片
                        d.setUrl("/product/photo/noImg.png");
                    }
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
            }
            //return null;
            //return new MessageBox(true, "导出成功，请在桌面查看");
        }catch (IOException e){
            e.printStackTrace();
            // return new MessageBox(false, "导出失败");
        }
    }


}
