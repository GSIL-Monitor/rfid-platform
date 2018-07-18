package com.casesoft.dmc.controller.sys;

import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.search.SaleorderCountView;
import com.casesoft.dmc.model.sys.BusinessAccount;
import com.casesoft.dmc.service.sys.BusinessAccountService;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * Created by Administrator on 2018/7/11.
 */
@Controller
@RequestMapping(value = "/sys/businessAccount")
public class BusinessAccountController extends BaseController implements IBaseInfoController<BusinessAccount> {
    @Autowired
    private BusinessAccountService businessAccountService;
    @RequestMapping(value="/page")
    @ResponseBody
    @Override
    public Page<BusinessAccount> findPage(Page<BusinessAccount> page) throws Exception {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page = this.businessAccountService.findPage(page, filters);
        return page;
    }

    @Override
    public List<BusinessAccount> list() throws Exception {
        return null;
    }

    @Override
    public MessageBox save(BusinessAccount entity) throws Exception {
        return null;
    }

    @Override
    public MessageBox edit(String taskId) throws Exception {
        return null;
    }

    @Override
    public MessageBox delete(String taskId) throws Exception {
        return null;
    }

    @Override
    public void exportExcel() throws Exception {

    }

    @Override
    public MessageBox importExcel(MultipartFile file) throws Exception {
        return null;
    }

    @Override
    @RequestMapping(value = "/index")
    public String index() {
         return "/views/sys/businessAccount";
    }
    @RequestMapping(value = "/export")
    @ResponseBody
    public void export() throws IOException {
        try {
            List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                    .getRequest());
            Long startTime= System.currentTimeMillis();
            List<BusinessAccount> businessAccountList = this.businessAccountService.findBySql(filters);
            for(BusinessAccount businessAccount:businessAccountList){
                if(CommonUtil.isBlank(businessAccount.getPayprice())){
                    businessAccount.setPayprice(new BigDecimal(0));
                }
                if(CommonUtil.isBlank(businessAccount.getSaleprice())){
                    businessAccount.setSaleprice(new BigDecimal(0));
                }else{
                    double v = businessAccount.getSaleprice().doubleValue();
                    businessAccount.setSaleprice(new BigDecimal(CommonUtil.doubleChange(v,2)).setScale(2,BigDecimal.ROUND_HALF_DOWN));
                }
                if(CommonUtil.isBlank(businessAccount.getSalereturnprice())){
                    businessAccount.setSalereturnprice(new BigDecimal(0));
                }else{
                    double v = businessAccount.getSalereturnprice().doubleValue();
                    businessAccount.setSalereturnprice(new BigDecimal(CommonUtil.doubleChange(v,2)).setScale(2,BigDecimal.ROUND_HALF_DOWN));
                }
                if(CommonUtil.isBlank(businessAccount.getOwingValue())){
                    businessAccount.setOwingValue(new BigDecimal(0));
                }else{
                    double v = businessAccount.getOwingValue().doubleValue();
                    Double aDouble = CommonUtil.doubleChange(v, 2);
                    businessAccount.setOwingValue(new BigDecimal(aDouble).setScale(2,BigDecimal.ROUND_HALF_DOWN));
                    System.out.print(businessAccount.getOwingValue());
                }
            }
            Long endtTime= System.currentTimeMillis();
            logger.error("查询加盟商对账所需的时间:"+(endtTime-startTime));
            Long exportstartTime= System.currentTimeMillis();
            ExportParams params = new ExportParams("加盟商对账", "sheet1", ExcelType.XSSF);
            String path = Constant.Folder.Report_File_Folder;
            String dateString = CommonUtil.getDateString(new Date(), "yyyyMMdd HH_mm_ss");
            File files=new File(path + "\\加盟商对账-" +  dateString + ".xlsx");

            if(!files.exists())
                files.mkdirs();
            File file =new File(files,"加盟商对账-" +  dateString + ".xlsx");
            //Workbook workbook = ExcelExportUtil.exportExcel(params, SaleorderCountView.class, SaleDtlViewList);
            Workbook workbook = ExcelExportUtil.exportBigExcel(params, BusinessAccount.class, businessAccountList);
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
            this.outFile("加盟商对账-" +  dateString + ".xlsx", file, contentType);
            Long exportendtTime= System.currentTimeMillis();
            logger.error("导出加盟商对账的时间:"+(exportendtTime-exportstartTime));
        } catch (Exception e) {
            e.printStackTrace();
            // return new MessageBox(false, "导出失败");
        }
    }
}
