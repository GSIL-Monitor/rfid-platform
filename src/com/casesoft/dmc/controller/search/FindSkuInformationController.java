package com.casesoft.dmc.controller.search;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.controller.product.StyleUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.search.FindSkuInformation;
import com.casesoft.dmc.service.search.FindSkuInformationService;
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
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/5/3.
 */
@RequestMapping(value = "/search/findSkuInformation")
@Controller
public class FindSkuInformationController extends BaseController implements IBaseInfoController<FindSkuInformation> {
    @Autowired
    private FindSkuInformationService  findSkuInformationService;
    @RequestMapping(value = "/page")
    @ResponseBody
    @Override
    public Page<FindSkuInformation> findPage(Page<FindSkuInformation> page) throws Exception {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page= this.findSkuInformationService.findPagePro(page, filters);
        String rootPath = session.getServletContext().getRealPath("/");
        for(int i=0;i<page.getRows().size();i++){
            String url = StyleUtil.returnImageUrl(page.getRows().get(i).getStyleid(), rootPath);
            page.getRows().get(i).setUrl(url);
        }
        return page;
    }

    @RequestMapping(value = "/exportnew")
    @ResponseBody
    public void export(String pages) throws IOException {
        Page<FindSkuInformation> page=JSON.parseObject(pages, Page.class);
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page= this.findSkuInformationService.findPagePro(page, filters);
        String rootPath = session.getServletContext().getRealPath("/");
        for(int i=0;i<page.getRows().size();i++){
            String url = StyleUtil.returnImageUrl(page.getRows().get(i).getStyleid(), rootPath);
            page.getRows().get(i).setUrl(url);
            if(Integer.parseInt(page.getRows().get(i).getQty())!=Integer.parseInt(page.getRows().get(i).getInqty())){
                page.getRows().get(i).setEndtime("");
            }
            switch (page.getRows().get(i).getInstocktype()) {
                case "XK":
                     page.getRows().get(i).setInstocktype("新款");
                     break;
                case "BH":
                    page.getRows().get(i).setInstocktype("补货");
                    break;
                case "PH":
                    page.getRows().get(i).setInstocktype("供应商配货");
                    break;
                case "JS":
                    page.getRows().get(i).setInstocktype("寄售");
                    break;
                default :
                    page.getRows().get(i).setInstocktype("");
            }
        }
        ExportParams params = new ExportParams("SKU采购单入库统计", "sheet1", ExcelType.XSSF);
        String path = Constant.Folder.Report_File_Folder;
        String dateString = CommonUtil.getDateString(new Date(), "yyyyMMdd HH_mm_ss");
        File files = new File(path + "\\SKU采购单入库统计-" + dateString + ".xlsx");

        if (!files.exists())
            files.mkdirs();
        File file = new File(files, "SKU采购单入库统计-" + dateString + ".xlsx");
        //Workbook workbook = ExcelExportUtil.exportExcel(params, TransferorderCountView.class, SaleDtlViewList);
        Workbook workbook = ExcelExportUtil.exportBigExcel(params, FindSkuInformation.class, page.getRows());
        ExcelExportUtil.closeExportBigExcel();
        //String dateString = CommonUtil.getDateString(new Date(), "yyyyMMdd HH_mm_ss");
        // FileOutputStream fos = new FileOutputStream(path + "\\销售明细-" +  dateString + ".xlsx");
        FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());
        workbook.write(fos);
        fos.close();
        long endTime = System.currentTimeMillis();

        FileWriter fileWriter = new FileWriter(file.getName(), true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.close();
        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;";
        this.outFile("SKU采购单入库统计-" + dateString + ".xlsx", file, contentType);



    }

    @Override
    public List<FindSkuInformation> list() throws Exception {
        return null;
    }

    @Override
    public MessageBox save(FindSkuInformation entity) throws Exception {
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
    @RequestMapping(value = "/index")
    @Override
    public String index() {
        return "/views/search/findSkuInformationSearch";
    }


}
