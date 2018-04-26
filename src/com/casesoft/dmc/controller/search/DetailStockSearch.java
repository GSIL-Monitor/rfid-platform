package com.casesoft.dmc.controller.search;


import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.dao.search.DetailStockDao;
import com.casesoft.dmc.model.search.DetailStockChatView;
import com.casesoft.dmc.model.search.DetailStockView;
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

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/search/detailStockViewSearch")
public class DetailStockSearch  extends BaseController {

    @Autowired
    DetailStockDao detailStockDao;

    @RequestMapping(value = "/index")
    public String index() {
        return "/views/search/detailStockViewSearch";
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public @ResponseBody
    DataSourceResult read(@RequestBody DataSourceRequest request) {
        return detailStockDao.getList(request);
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
    public void export(String request) throws IOException {
        try {
            DataSourceRequest dataSourceRequest = JSON.parseObject(request, DataSourceRequest.class);
            DataSourceResult sourceResultSaleDtl = this.detailStockDao.getList(dataSourceRequest);
            List<DetailStockView> detailStockViews = (List<DetailStockView>) sourceResultSaleDtl.getData();
            ExportParams params = new ExportParams("库存，按SKU汇总", "sheet1", ExcelType.XSSF);
            String path = Constant.Folder.Report_File_Folder;
            String dateString = CommonUtil.getDateString(new Date(), "yyyyMMdd HH_mm_ss");
            File files=new File(path + "\\库存，按SKU汇总");
            if(!files.exists())
                files.mkdirs();
            File file =new File(files,"库存，按SKU汇总 -" +  dateString + ".xlsx");
            file.createNewFile();
            //Workbook workbook = ExcelExportUtil.exportExcel(params, DetailStockView.class, detailStockViews);
            Workbook workbook = ExcelExportUtil.exportBigExcel(params, DetailStockView.class, detailStockViews);
            ExcelExportUtil.closeExportBigExcel();
            FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());
            workbook.write(fos);
            FileWriter fileWriter=new FileWriter(file.getName(),true);
            BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
            bufferedWriter.close();
            String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;";
            this.outFile("库存，按SKU汇总-" +  dateString + ".xlsx", file, contentType);
            //return new MessageBox(true, "导出成功，请在桌面查看");
        } catch (IOException e) {
            e.printStackTrace();
            //return new MessageBox(false, "导出失败");
        }
    }

}
