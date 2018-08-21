package com.casesoft.dmc.controller.stock;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.syn.tool.BillUtil;
import com.casesoft.dmc.controller.task.TaskUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.logistics.BillConstant;
import com.casesoft.dmc.model.logistics.InventoryBill;
import com.casesoft.dmc.model.stock.*;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.service.stock.EpcStockService;
import com.casesoft.dmc.service.stock.InventoryMergeService;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by yushen on 2017/12/4.
 */
@Controller
@RequestMapping("/stock/InventoryMerge")
public class InventoryMergeController extends BaseController implements IBaseInfoController<InventoryMergeBill> {
    @Autowired
    private InventoryMergeService inventoryMergeService;
    @Autowired
    private EpcStockService epcStockService;


    @Override
    public String index() {
        return "/views/stock/inventoryMergeBill";
    }

    @RequestMapping("/index")
    public ModelAndView indexMV() {
        ModelAndView mv = new ModelAndView("/views/stock/inventoryMergeBill");
        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        return mv;
    }

    @RequestMapping("/viewMergeBillDtl")
    public ModelAndView viewMergeBillDtl(String id) {
        InventoryMergeBill mergeBill = this.inventoryMergeService.get("id", id);
        Unit unit = CacheManager.getUnitById(mergeBill.getWarehouseId());
        if (CommonUtil.isNotBlank(unit)) {
            mergeBill.setWarehouseName(unit.getName());
        }
        ModelAndView mv = new ModelAndView("views/stock/inventoryMergeBillDtl");
        mv.addObject("mergeBill", mergeBill);
        mv.addObject("userId",getCurrentUser().getId());
        return mv;

    }

    @RequestMapping("/page")
    @ResponseBody
    @Override
    public Page<InventoryMergeBill> findPage(Page<InventoryMergeBill> page) throws Exception {
        this.logAllRequestParams();
        page.setPageProperty();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        return this.inventoryMergeService.findPage(page, filters);
    }

    @RequestMapping("/findMergeBillDtl")
    @ResponseBody
    public List<InventoryMergeBillDtl> findMergeBillDtl(String id, boolean isChecked, String sku) throws Exception {
        return this.inventoryMergeService.findMergeBillDtl(id, isChecked, sku);
    }

    @RequestMapping("/findOrigBill")
    @ResponseBody
    public List<Bill> findOrigBill(String billNo) {
        List<String> origBillList = this.inventoryMergeService.findOrigBillList(billNo);
        String billNoListStr = TaskUtil.getSqlStrByList(origBillList, Bill.class, "billNo");
        return this.inventoryMergeService.findErpBillByBillNoList(billNoListStr);
    }

    @Override
    public List<InventoryMergeBill> list() throws Exception {
        return null;
    }

    @Override
    public MessageBox save(InventoryMergeBill entity) throws Exception {
        return null;
    }

    @RequestMapping("/mergeBill")
    @ResponseBody
    public MessageBox mergeBill(String billListStr) throws Exception {
        try {
            List<Bill> billList = JSON.parseArray(billListStr, Bill.class);
            List<InventoryMergeOrigBill> origBillLists = new ArrayList<>();
            List<String> billNoList = new ArrayList<>();

            String prefix = BillConstant.BillPrefix.inventoryMergeBill
                    + CommonUtil.getDateString(new Date(), "yyMMdd");
            String billNo = this.inventoryMergeService.findMaxMGBillNo(prefix);

            for (Bill bill : billList) {
                billNoList.add(bill.getId());
                InventoryMergeOrigBill inventoryMergeOrigBill = new InventoryMergeOrigBill();
                inventoryMergeOrigBill.setId(billNo + "-" + bill.getId());
                inventoryMergeOrigBill.setMergeBillNo(billNo);
                inventoryMergeOrigBill.setInventoryBillNo(bill.getId());
                origBillLists.add(inventoryMergeOrigBill);
            }
            String billNoListStr = TaskUtil.getSqlStrByList(billNoList, InventoryRecord.class, "billNo");

            InventoryMergeBill mergeBill = new InventoryMergeBill();
            mergeBill.setId(billNo);
            mergeBill.setBillNo(billNo);
            mergeBill.setWarehouseId(billList.get(0).getOrigId());
            mergeBill.setMergeBillQty(billList.size());
            mergeBill.setMergeDate(new Date());
            mergeBill.setInventoryDate(billList.get(0).getBillDate());
            mergeBill.setInventoryBillList(origBillLists);
            mergeBill.setOwnerId(CacheManager.getUnitById(mergeBill.getWarehouseId()).getOwnerId());

            List<InventoryMergeBillDtl> mergeBillDtlList = this.inventoryMergeService.mergeBillDtl(billNoListStr);
            for (InventoryMergeBillDtl mergeBillDtl : mergeBillDtlList) {
                mergeBillDtl.setId(new GuidCreator().toString());
                mergeBillDtl.setBillNo(billNo);
            }
            Long totScannedQty = 0L;
            for (InventoryMergeBillDtl mergeBillDtl : mergeBillDtlList) {
                totScannedQty = totScannedQty + mergeBillDtl.getInStock();
            }

            mergeBill.setTotMergeQty((long) mergeBillDtlList.size());
            mergeBill.setTotScannedQty(totScannedQty);

            this.inventoryMergeService.save(mergeBill, mergeBillDtlList);
            return new MessageBox(true, "合并成功，请转到盘点合并单中查看");
        } catch (Exception e) {
            e.printStackTrace();
            return new MessageBox(false, "合并失败");
        }
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
        try {
            InventoryMergeBill mergeBill = this.inventoryMergeService.get("id", id);
            Unit unit = CacheManager.getUnitById(mergeBill.getWarehouseId());
            if (CommonUtil.isNotBlank(unit)) {
                mergeBill.setWarehouseName(unit.getName());
            }
            String sheetTitle = "仓库：["+mergeBill.getWarehouseId() +"]"+ mergeBill.getWarehouseName()+"，盘点日期："+ CommonUtil.getDateString(mergeBill.getInventoryDate(),"yyyyMMdd");
            List<InventoryMergeBillDtl> mergeBillDtl = this.inventoryMergeService.findMergeBillDtl(id, isChecked, sku);
            ExportParams params = new ExportParams(sheetTitle, "sheet1", ExcelType.XSSF);
            String path = Constant.Folder.Report_File_Folder;
            String dateString = CommonUtil.getDateString(new Date(), "yyyyMMdd HH_mm_ss");
            File files=new File(path + "\\盘点合并");
            if(!files.exists())
                files.mkdirs();
            File file =new File(files,"盘点合并-" +  dateString + ".xlsx");
            file.createNewFile();
            //Workbook workbook = ExcelExportUtil.exportExcel(params, InventoryMergeBillDtl.class, mergeBillDtl);
            Workbook workbook = ExcelExportUtil.exportBigExcel(params, InventoryMergeBillDtl.class, mergeBillDtl);
            ExcelExportUtil.closeExportBigExcel();
            FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());
            workbook.write(fos);
            FileWriter fileWriter=new FileWriter(file.getName(),true);
            BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
            bufferedWriter.close();
            String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;";
            this.outFile("盘点合并-" +  dateString + ".xlsx", file, contentType);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    public MessageBox importExcel(MultipartFile file) throws Exception {
        return null;
    }

    /**
     *
     * @param reason 后台提交的原因
     * @param billNo 原始单号
     * @param setDtlList 盘点调出的数据
     * @param userId 操作用户
     * @return 结果
     */
    @RequestMapping("/saveDtlOut")
    @ResponseBody
    public MessageBox saveDtlOut(String reason,String billNo,String setDtlList,String userId){
            this.logAllRequestParams();
        try {
            /*获取code的集合*/
            List<String> codeList = new LinkedList<>();
            /*将前端的json数据转化为List集合，获取code的集合*/
            List<InventoryMergeBillDtl> inventoryMergeBillDtlList = JSON.parseArray(setDtlList, InventoryMergeBillDtl.class);
            for (InventoryMergeBillDtl inventoryMergeBillDtl : inventoryMergeBillDtlList) {
                codeList.add(inventoryMergeBillDtl.getCode());
            }
            /*根据code查询出EpcStock的集合*/
            List<EpcStock> epcStockList = this.epcStockService.findEpcByCodes(TaskUtil.getSqlStrByList(codeList, EpcStock.class, "code"));
            /*根据部门编号获取InventoryMergeBill的对象*/
            InventoryMergeBill inventoryMergeBill = this.inventoryMergeService.get("billNo", billNo);

            List<Business> businessList = new ArrayList<>();
            /*获取操作的用户*/
            User currentUser = CacheManager.getUserById(userId);
            StringBuffer resultStr = new StringBuffer();
            /*合并单据and生成转换单*/
            List<InventoryBill> inventoryBillList = BillUtil.convertInventoryBill(inventoryMergeBillDtlList, reason, inventoryMergeBill, epcStockList, currentUser, businessList, resultStr);
            /*保存修改后的各个单据*/
            this.inventoryMergeService.mergeBillDtlBill(inventoryMergeBillDtlList, inventoryBillList, businessList,epcStockList);
            return new MessageBox(true, resultStr.toString());
        }catch (Exception e){
            e.printStackTrace();
            return new MessageBox(false, "合并失败");
        }
    }
}
