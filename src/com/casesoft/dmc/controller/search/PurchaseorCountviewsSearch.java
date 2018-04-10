package com.casesoft.dmc.controller.search;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.product.StyleUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.ImgUtil;
import com.casesoft.dmc.core.util.json.JSONUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.dao.search.PurchaseorCountDao;
import com.casesoft.dmc.model.logistics.BillConstant;
import com.casesoft.dmc.model.search.*;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.logistics.PurchaseOrderBillService;
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

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.util.*;

/**
 * Created by Administrator on 2017/10/11.
 */
@Controller
@RequestMapping("/search/PurchaseorCountviews")
public class PurchaseorCountviewsSearch extends BaseController {
    @Autowired
    PurchaseorCountDao purchaseorCountDao;
    @Autowired
    private UnitService unitService;
    @Autowired
    private PurchaseOrderBillService purchaseOrderBillService;
    @Override
    public String index() {
        return null;
    }
    @RequestMapping(value = "/index")
    public ModelAndView indexMV() throws Exception {
        ModelAndView mv = new ModelAndView("/views/search/PurchaseorCountviewsSearch");
        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        User currentUser = getCurrentUser();
        Unit unit = this.unitService.getunitbyId(getCurrentUser().getOwnerId());
        mv.addObject("deportId", unit.getDefaultWarehId());
        Unit unit1 = this.unitService.getunitbyId(unit.getDefaultWarehId());
        mv.addObject("deportName", unit1.getName());
        mv.addObject("roleid", currentUser.getRoleId());
        return mv;
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public @ResponseBody
    DataSourceResult read(@RequestBody DataSourceRequest request) {

        DataSourceResult dataResult = purchaseorCountDao.getList(request);
        String rootPath = session.getServletContext().getRealPath("/");
       List<?> data = dataResult.getData();
        List<PurchaseorCountviews> datanew =new ArrayList<PurchaseorCountviews>();
        for(int i=0;i<data.size();i++){
            PurchaseorCountviews purchaseorCountviews = (PurchaseorCountviews) data.get(i);
            String billno = purchaseorCountviews.getBillid();
            if(billno.contains(BillConstant.BillPrefix.purchase)){
                purchaseorCountviews.setSaletype("采购订单");
            }
            if(billno.contains(BillConstant.BillPrefix.purchaseReturn)){
                purchaseorCountviews.setSaletype("采购退货订单");
              /*  Integer qty = purchaseorCountviews.getQty();
                purchaseorCountviews.setQty(Integer.parseInt("-"+qty));*/
            }
            File file =  new File(rootPath + "/product/photo/" + purchaseorCountviews.getStyleid());
            if(file.exists()){
                File[] files = file.listFiles();
                if(files.length > 0){
                    File[] photos = files[0].listFiles();
                    if(photos.length > 0){
                        purchaseorCountviews.setUrl("/product/photo/" + purchaseorCountviews.getStyleid()+"/"+files[0].getName()+"/"+photos[0].getName());
                    }
                }
            }
            datanew.add(purchaseorCountviews);
        }
        dataResult.setData(datanew);
        return dataResult;
    }
    @RequestMapping(value = "/listpurchase", method = RequestMethod.POST)
    public @ResponseBody
    DataSourceResult readpurchase(@RequestBody DataSourceRequest request) {

        DataSourceResult dataResult = purchaseorCountDao.getpurchaseList(request);
        String rootPath = session.getServletContext().getRealPath("/");
        List<?> data = dataResult.getData();
        List<PurchaseNodeatilViews> datanew =new ArrayList<PurchaseNodeatilViews>();
        for(int i=0;i<data.size();i++){
            PurchaseNodeatilViews purchaseNodeatilViews = (PurchaseNodeatilViews) data.get(i);
            String billno = purchaseNodeatilViews.getBillno();
            if(billno.contains(BillConstant.BillPrefix.purchase)){
                purchaseNodeatilViews.setSaletype("采购订单");
            }
            if(billno.contains(BillConstant.BillPrefix.purchaseReturn)){
                purchaseNodeatilViews.setSaletype("采购退货订单");
                Integer qty = purchaseNodeatilViews.getTotqty();
                purchaseNodeatilViews.setTotqty(Integer.parseInt("-"+qty));
            }

            datanew.add(purchaseNodeatilViews);
        }
        dataResult.setData(datanew);
        return dataResult;
    }
   @RequestMapping(value = "/export", method = RequestMethod.POST)
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
    @RequestMapping(value = "/exportnew")
    @ResponseBody
    public void export(String gridId, String request) throws IOException {
        try {
            DataSourceRequest dataSourceRequest = JSON.parseObject(request, DataSourceRequest.class);
            if(gridId.equals("searchGrid")){
                long startTime = System.currentTimeMillis();
                DataSourceResult sourceResultSaleDtl = purchaseorCountDao.getList(dataSourceRequest);
                List<PurchaseorCountviews> purchaseorCountViewList=(List<PurchaseorCountviews>)sourceResultSaleDtl.getData();
                String rootPath = session.getServletContext().getRealPath("/");
                for(PurchaseorCountviews d:purchaseorCountViewList){
                    File file =  new File(rootPath + "/product/photo/" + d.getStyleid());
                    if(file.exists()){
                        File[] files = file.listFiles();
                        if(files.length > 0){
                            File[] photos = files[0].listFiles();
                            if(photos.length > 0){
                                //d.setUrl("/product/photo/" + d.getStyleId()+"/"+files[0].getName()+"/"+photos[0].getName());
                                String url = StyleUtil.exportImgUrl(d.getStyleid(), rootPath, ImgUtil.ImgExt.small);
                                d.setUrl(url);
                            }
                        }
                    }
                    if (CommonUtil.isBlank(d.getUrl())) {
                        //没有图片设置默认图片
                        d.setUrl("/product/photo/noImg.png");
                    }
                }
                ExportParams params = new ExportParams("采购明细", "sheet1", ExcelType.XSSF);
                String path = Constant.Folder.Report_File_Folder;
                String dateString = CommonUtil.getDateString(new Date(), "yyyyMMdd HH_mm_ss");
                File files = new File(path + "\\采购明细-" + dateString + ".xlsx");

                if (!files.exists())
                    files.mkdirs();
                File file = new File(files, "采购明细-" + dateString + ".xlsx");
                //Workbook workbook = ExcelExportUtil.exportExcel(params, TransferorderCountView.class, SaleDtlViewList);
                Workbook workbook = ExcelExportUtil.exportBigExcel(params, PurchaseorCountviews.class, purchaseorCountViewList);
                ExcelExportUtil.closeExportBigExcel();
                //String dateString = CommonUtil.getDateString(new Date(), "yyyyMMdd HH_mm_ss");
                // FileOutputStream fos = new FileOutputStream(path + "\\销售明细-" +  dateString + ".xlsx");
                FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());
                workbook.write(fos);
                fos.close();
                long endTime = System.currentTimeMillis();
                System.out.println("采购明细明细导出："+(endTime - startTime));
                logger.error("采购明细明细导出："+(endTime - startTime));
                FileWriter fileWriter = new FileWriter(file.getName(), true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.close();
                String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;";
                this.outFile("采购明细明细-" + dateString + ".xlsx", file, contentType);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @RequestMapping(value = "/findtitledate", method = RequestMethod.POST)
    @ResponseBody
    public MessageBox findtitledate(String dates){
        System.out.print(dates);
        Map<String,String> map4Json = JSONUtil.getMap4Json(dates);

        String hqlpurchasesum="select sum(t.qty),count(distinct t.billid),sum(t.totactprice) from PurchaseorCountviews t where 1=1";

        Iterator<Map.Entry<String, String>> iterator = map4Json.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            String key = next.getKey();
            String value=map4Json.get(key);
            if(CommonUtil.isNotBlank(value)){
                if(key.equals("filter_gte_billDate")){
                    String[] split = key.split("_");
                    String time =value+" 00:00:00";
                    hqlpurchasesum+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";

                }else if(key.equals("filter_lte_billDate")){
                    String[] split = key.split("_");
                    String time =value+" 23:59:59";
                    hqlpurchasesum+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";

                }else {
                    String[] split = key.split("_");
                    if(split[1].equals("contains")){
                        hqlpurchasesum+=" and "+split[2]+" like '%"+value+"%'";

                    }else{
                        hqlpurchasesum+=" and "+split[2]+"='"+value+"'";

                    }


                }

            }
        }

        long startTime=System.currentTimeMillis();   //获取开始时间
       Object[] objects= (Object[])this.purchaseOrderBillService.findpurchaseCount(hqlpurchasesum);
       long endTime=System.currentTimeMillis(); //获取结束时间
        logger.error("执行aLong的时间"+(endTime-startTime));

        purchaseCount purchasecount = new purchaseCount();
        purchasecount.setPurchasesum(Integer.parseInt(objects[0]+""));
        purchasecount.setPurchasonesum(Integer.parseInt(objects[1]+""));
        purchasecount.setPurchasmony(Double.parseDouble(objects[2]+""));
        return new MessageBox(true, "成功",purchasecount);


    }

    @RequestMapping(value = "/readpurchaseBybusinessname", method = RequestMethod.POST)
    public @ResponseBody
    DataSourceResult readSaleBybusinessname(@RequestBody DataSourceRequest request) {

        DataSourceResult dataResult = null;
        try {
            dataResult = purchaseorCountDao.getPurchaseBybusinessnameList(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataResult;
    }
    @RequestMapping(value = "/readpurchaseBydestunitid", method = RequestMethod.POST)
    public @ResponseBody
    DataSourceResult readSaleBydestunitid(@RequestBody DataSourceRequest request) {

        DataSourceResult dataResult = null;
        try {
            dataResult = purchaseorCountDao.getPurchaseBydestunitidList(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataResult;
    }

}


