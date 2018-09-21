package com.casesoft.dmc.controller.search;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.dao.search.BuyerKpiDao;
import com.casesoft.dmc.model.search.BuyerKpi;
import com.casesoft.dmc.model.search.BuyerKpiStyleDetail;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/search/buyerKpi")
public class BuyerKpiController extends BaseController {
    @Autowired
    private BuyerKpiDao buyerKpiDao;

    @Override
    @RequestMapping("/index")
    public String index() {
        return "/views/search/buyerKpi";
    }

    @RequestMapping("/getBuyerKpi")
    @ResponseBody
    public DataSourceResult getBuyerKpi(@RequestBody DataSourceRequest request) {
        DataSourceResult buyerKpi = null;
        try {
            buyerKpi = buyerKpiDao.getBuyerKpi(request);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return buyerKpi;
    }

    @RequestMapping(value = "/excelExport")
    @ResponseBody
    public void export(String request){
        try {
            DataSourceRequest dataSourceRequest = JSON.parseObject(request, DataSourceRequest.class);
            long startTime = System.currentTimeMillis();
            DataSourceResult styleDetail = buyerKpiDao.getBuyerKpi(dataSourceRequest);
            logger.error("查询买手KPI所需的时间:" + (System.currentTimeMillis() - startTime));
            Long exportstartTime = System.currentTimeMillis();
            List<BuyerKpi> buyerKpiList = (List<BuyerKpi>) styleDetail.getData();
            ExportParams params = new ExportParams("买手KPI", "sheet1", ExcelType.XSSF);
            String path = Constant.Folder.Report_File_Folder;
            String dateString = CommonUtil.getDateString(new Date(), "yyyyMMdd HH_mm_ss");
            File files = new File(path + "\\买手KPI-" + dateString + ".xlsx");
            if (!files.exists()) {
                files.mkdirs();
            }
            File file = new File(files, "买手KPI-" + dateString + ".xlsx");
            Workbook workbook = ExcelExportUtil.exportBigExcel(params, BuyerKpi.class, buyerKpiList);
            ExcelExportUtil.closeExportBigExcel();
            FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());
            workbook.write(fos);
            fos.close();
            logger.error("买手KPI导出：" + (System.currentTimeMillis() - startTime));
            FileWriter fileWriter = new FileWriter(file.getName(), true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.close();
            String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;";
            this.outFile("买手KPI导出-" + dateString + ".xlsx", file, contentType);
            Long exportendtTime = System.currentTimeMillis();
            logger.error("导出买手KPI所需的时间:" + (exportendtTime - exportstartTime));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
