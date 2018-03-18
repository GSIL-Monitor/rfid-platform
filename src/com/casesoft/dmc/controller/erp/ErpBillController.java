package com.casesoft.dmc.controller.erp;


import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.erp.BillDtl;
import com.casesoft.dmc.model.search.DateStockDetail;
import com.casesoft.dmc.model.stock.InventoryMergeBillDtl;
import com.casesoft.dmc.model.stock.InventoryRecord;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.service.erp.ErpBillService;
import com.casesoft.dmc.service.stock.InventoryService;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.jeecgframework.poi.excel.export.ExcelExportServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.Date;
import java.util.List;

/**
 * Created by yushen on 2017/10/30.
 */

@Controller
@RequestMapping("/stock/erpBill")
public class ErpBillController extends BaseController implements IBaseInfoController<Bill> {

    @Autowired
    private ErpBillService erpBillService;
    @Autowired
    private InventoryService inventoryService;

    @Override
    public String index() {
        return "/views/stock/inventoryStockBill";
    }

    @RequestMapping(value = "/index")
    public ModelAndView indexMV() {
        ModelAndView mv = new ModelAndView("/views/stock/inventoryStockBill");
        Unit shop = CacheManager.getUnitByCode(getCurrentUser().getOwnerId());
        mv.addObject("defaultWarehId", shop.getDefaultWarehId());
        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        return mv;
    }

    @RequestMapping(value="/page")
    @ResponseBody
    @Override
    public Page<Bill> findPage(Page<Bill> page) throws Exception {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page=this.erpBillService.findPage(page, filters);
        for (Bill bill:page.getRows()){
            if (bill.getDestId()!=null){
                bill.setDestName(CacheManager.getUnitById(bill.getDestId()).getName());
            }
            if(bill.getOrigId()!=null){
                bill.setOrigName(CacheManager.getUnitById(bill.getOrigId()).getName());
            }
        }
        return page;
    }

    @RequestMapping(value="/findErpBillDtl")
    @ResponseBody
    public List<BillDtl> findErpBillDtl(String billNo, boolean isChecked, String sku){
        return this.erpBillService.findErpBillDtl(billNo, isChecked, sku);
    }

    @RequestMapping(value="/findInventoryRecord")
    @ResponseBody
    public List<InventoryRecord> findInventoryRecord(String billNo, boolean isChecked, String sku){
        List<InventoryRecord> InventoryRecordList;
        if(CommonUtil.isBlank(sku)){
            sku="";
        }
        InventoryRecordList = this.inventoryService.findInventoryRecord(billNo, isChecked, sku);
        return InventoryRecordList;
    }

    @RequestMapping(value="/detail")
    @ResponseBody
    public ModelAndView showDetail(String id)throws Exception{
        this.logAllRequestParams();
        Bill bill=this.erpBillService.findErpBillById(id);
        if (bill.getDestId()!=null){
            bill.setDestName(CacheManager.getUnitById(bill.getDestId()).getName());
            bill.setOrigName(CacheManager.getUnitById(bill.getOrigId()).getName());
        }
        ModelAndView mv = new ModelAndView();
        mv.addObject("erpBill", bill);
        mv.setViewName("views/stock/inventoryStockBillDetail");
        return mv;
    }

    @Override
    public List<Bill> list() throws Exception {
        return null;
    }

    @Override
    public MessageBox save(Bill entity) throws Exception {
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

    @RequestMapping("/exportExcel")
    @ResponseBody
    public void exportExcel(String id, boolean isChecked, String sku) throws IOException {
        try{
            Bill bill=this.erpBillService.findErpBillById(id);
            Unit unit = CacheManager.getUnitById(bill.getOrigId());
            if (CommonUtil.isNotBlank(unit)) {
                bill.setOrigName(unit.getName());
            }
            String sheetTitle = "仓库：["+bill.getOrigId() +"]"+ bill.getOrigName()+"，盘点日期："+ CommonUtil.getDateString(bill.getBillDate(),"yyyyMMdd");
            List<BillDtl> erpBillDtl = this.erpBillService.findErpBillDtl(id, isChecked, sku);
            List<InventoryRecord> inventoryRecord = this.inventoryService.findInventoryRecord(id, isChecked, sku);
            ExportParams params1 = new ExportParams("SKU明细，" +sheetTitle, "sheet1", ExcelType.XSSF);
            ExportParams params2 = new ExportParams("唯一码明细，" + sheetTitle, "sheet2", ExcelType.XSSF);



            String path = Constant.Folder.Report_File_Folder;
            String dateString = CommonUtil.getDateString(new Date(), "yyyyMMdd HH_mm_ss");
            File files=new File(path + "\\盘点单据");
            if(!files.exists())
                files.mkdirs();

            File file =new File(files,"\\盘点单据-" + id + "-" + dateString + ".xlsx");
            file.createNewFile();
            //Workbook workbook = ExcelExportUtil.exportExcel(params1, BillDtl.class, erpBillDtl);
            Workbook workbook = ExcelExportUtil.exportBigExcel(params1, BillDtl.class, erpBillDtl);
            ExcelExportUtil.closeExportBigExcel();
            (new ExcelExportServer()).createSheet(workbook, params2, InventoryRecord.class, inventoryRecord);
            FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());
            workbook.write(fos);
            FileWriter fileWriter=new FileWriter(file.getName(),true);
            BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
            bufferedWriter.close();
            String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;";
            this.outFile("\\盘点单据-" + id + "-" + dateString + ".xlsx", file, contentType);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public MessageBox importExcel(MultipartFile file) throws Exception {
        return null;
    }

}
