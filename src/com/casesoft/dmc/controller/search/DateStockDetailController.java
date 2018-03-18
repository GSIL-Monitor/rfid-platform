package com.casesoft.dmc.controller.search;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.ILogisticsBillController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;

import com.casesoft.dmc.model.search.DateStockDetail;
import com.casesoft.dmc.model.search.DetailStockView;
import com.casesoft.dmc.model.stock.InventoryMergeBillDtl;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.service.search.DateStockDetailService;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/12/25.
 */

@Controller
@RequestMapping("/search/DateStockDetailController")
public class DateStockDetailController extends BaseController implements ILogisticsBillController<DateStockDetail> {
    @Autowired
    private DateStockDetailService dateStockDetailService;
    @RequestMapping(value = "/page")
    @ResponseBody
    @Override
    public Page<DateStockDetail> findPage(Page<DateStockDetail> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page = this.dateStockDetailService.findPage(page, filters);
        for (DateStockDetail dtl : page.getRows()) {
            dtl.setInStockPrice((double) Math.round(dtl.getPrice() * dtl.getQty()));
            Unit unit = CacheManager.getUnitById(dtl.getWarehId());
            if(CommonUtil.isNotBlank(unit)){
                dtl.setWarehName(unit.getName());
            }
        }
        return page;
    }

    @Override
    public List<DateStockDetail> list() throws Exception {
        return null;
    }

    @Override
    public MessageBox save(String bill, String strDtlList, String userId) throws Exception {
        return null;
    }

    @Override
    public ModelAndView add() throws Exception {
        return null;
    }

    @Override
    public ModelAndView edit(String billNo) throws Exception {
        return null;
    }

    @Override
    public MessageBox check(String billNo) throws Exception {
        return null;
    }

    @Override
    public MessageBox end(String billNo) throws Exception {
        return null;
    }

    @Override
    public MessageBox cancel(String billNo) throws Exception {
        return null;
    }

    @Override
    public MessageBox convert(String strDtlList, String recordList) throws Exception {
        return null;
    }
    //@RequestMapping(value = "/index")
    @Override
    public String index() {
        return "/views/stock/dateStockDetail";
    }
    @RequestMapping(value = "/index")
    public ModelAndView indexMV() throws Exception {
        ModelAndView mv = new ModelAndView("/views/stock/dateStockDetail");
        Unit unit = CacheManager.getUnitByCode(getCurrentUser().getOwnerId());
        String defaultWarehId = unit.getDefaultWarehId();
        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        mv.addObject("defaultWarehId", defaultWarehId);
        mv.addObject("userId", getCurrentUser().getId());
        return mv;
    }
    @RequestMapping(value = "/export")
    @ResponseBody
    public void export(){
        try {
            this.logAllRequestParams();
            List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                   .getRequest());
            List<DateStockDetail> list=null;
            if(filters.equals(0)){
                list=this.dateStockDetailService.getAll();
            }else{
                list=this.dateStockDetailService.find(filters);
            }


            for (DateStockDetail dtl :list) {
                dtl.setInStockPrice((double) Math.round(dtl.getPrice() * dtl.getQty()));
                Unit unit = CacheManager.getUnitById(dtl.getWarehId());
                if(CommonUtil.isNotBlank(unit)){
                    dtl.setWarehName(unit.getName());
                }
            }
            ExportParams params = new ExportParams("每日结账", "sheet1", ExcelType.XSSF);
            //Workbook workbook = ExcelExportUtil.exportExcel(params, DateStockDetail.class, list);
            Workbook workbook = ExcelExportUtil.exportBigExcel(params, DateStockDetail.class, list);
            ExcelExportUtil.closeExportBigExcel();
            String path = FileSystemView.getFileSystemView().getHomeDirectory().getPath();
            String dateString = CommonUtil.getDateString(new Date(), "yyyyMMdd HH_mm_ss");
            File files=new File(path + "\\每日结账-" +  dateString + ".xlsx");
            if(!files.exists())
                files.mkdirs();
            File file =new File(files,"每日结账 -" +  dateString + ".xlsx");
            // FileOutputStream fos = new FileOutputStream(path + "\\库存查询-" + dateString + ".xlsx");
            FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());
            workbook.write(fos);
            fos.close();
            FileWriter fileWriter=new FileWriter(file.getName(),true);
            BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
            bufferedWriter.close();
            String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;";
            this.outFile("每日结账-" +  dateString + ".xlsx", file, contentType);
        }catch (Exception e) {
            e.printStackTrace();
            //return new MessageBox(false, "导出失败");
        }
    }
}
