package com.casesoft.dmc.controller.search;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.dao.search.BuyerKpiStyleDetailDao;
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
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/search/buyerKpiStyleDetail")
public class BuyerKpiStyleDetailController extends BaseController {

    @Autowired
    private BuyerKpiStyleDetailDao buyerKpiStyleDetailDao;

    @Override
    @RequestMapping("/index")
    public String index() {
        return "/views/search/buyerKpiStyleDetail";
    }


    @RequestMapping("/viewBuyerKpiStyleDetail")
    ModelAndView viewBuyerKpiStyleDetail(String buyerId, String startDate, String endDate) {
        ModelAndView mv = new ModelAndView("views/search/buyerKpiStyleDetail");
        mv.addObject("buyerId", buyerId);
        mv.addObject("startDate", startDate);
        mv.addObject("endDate", endDate);
        return mv;
    }

    @RequestMapping("/getStyleDetail")
    @ResponseBody
    public DataSourceResult getStyleDetail(@RequestBody DataSourceRequest request) {
        DataSourceResult styleDetail = null;
        try {
            styleDetail = buyerKpiStyleDetailDao.getStyleDetail(request);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return styleDetail;
    }

    @RequestMapping(value = "/excelExport")
    @ResponseBody
    public void export(String request){
        try {
            DataSourceRequest dataSourceRequest = JSON.parseObject(request, DataSourceRequest.class);
            long startTime = System.currentTimeMillis();
            DataSourceResult styleDetail = buyerKpiStyleDetailDao.getStyleDetail(dataSourceRequest);
            logger.error("查询买手KPI款明细所需的时间:" + (System.currentTimeMillis() - startTime));
            Long exportstartTime = System.currentTimeMillis();
            List<BuyerKpiStyleDetail> styleDetailList = (List<BuyerKpiStyleDetail>) styleDetail.getData();
            ExportParams params = new ExportParams("买手KPI款明细", "sheet1", ExcelType.XSSF);
            String path = Constant.Folder.Report_File_Folder;
            String dateString = CommonUtil.getDateString(new Date(), "yyyyMMdd HH_mm_ss");
            File files = new File(path + "\\买手KPI款明细-" + dateString + ".xlsx");
            if (!files.exists()) {
                files.mkdirs();
            }
            File file = new File(files, "买手KPI款明细-" + dateString + ".xlsx");
            Workbook workbook = ExcelExportUtil.exportBigExcel(params, BuyerKpiStyleDetail.class, styleDetailList);
            ExcelExportUtil.closeExportBigExcel();
            FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());
            workbook.write(fos);
            fos.close();
            logger.error("买手KPI款明细导出：" + (System.currentTimeMillis() - startTime));
            FileWriter fileWriter = new FileWriter(file.getName(), true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.close();
            String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;";
            this.outFile("买手KPI款明细导出-" + dateString + ".xlsx", file, contentType);
            Long exportendtTime = System.currentTimeMillis();
            logger.error("导出买手KPI款明细所需的时间:" + (exportendtTime - exportstartTime));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
