package com.casesoft.dmc.controller.stock;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.product.StyleUtil;
import com.casesoft.dmc.controller.syn.tool.BillUtil;
import com.casesoft.dmc.controller.task.TaskUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.core.util.secret.EpcSecretUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.dao.search.DetailStockDao;
import com.casesoft.dmc.model.erp.BillDtl;
import com.casesoft.dmc.model.logistics.*;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.search.DetailStockChatView;
import com.casesoft.dmc.model.search.DetailStockCodeView;
import com.casesoft.dmc.model.stock.EpcStock;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.model.tag.Epc;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.Record;
import com.casesoft.dmc.service.erp.ErpBillService;
import com.casesoft.dmc.service.logistics.PurchaseOrderBillService;
import com.casesoft.dmc.service.logistics.SaleOrderBillService;
import com.casesoft.dmc.service.logistics.SaleOrderReturnBillService;
import com.casesoft.dmc.service.logistics.TransferOrderBillService;
import com.casesoft.dmc.service.stock.EpcStockService;
import com.casesoft.dmc.service.sys.impl.UnitService;
import com.casesoft.dmc.service.sys.impl.WarehouseService;
import com.casesoft.dmc.service.task.TaskService;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by WingLi on 2017-01-03.
 */
@Controller
@RequestMapping("/stock/warehStock")
public class WarehStockController extends BaseController {
    @Autowired
    DetailStockDao detailStockDao;
    @Autowired
    private TaskService taskService;
    @Autowired
    private EpcStockService epcStockService;
    @Autowired
    private SaleOrderBillService saleOrderBillService;
    @Autowired
    private SaleOrderReturnBillService saleOrderReturnBillService;
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private TransferOrderBillService transferOrderBillService;

    @Autowired
    private PurchaseOrderBillService purchaseOrderBillService;

    @Autowired
    private ErpBillService erpBillService;


    @RequestMapping(value = "/index")
    public String index() {
        String ownerId = this.getCurrentUser().getOwnerId();
        User currentUser = this.getCurrentUser();
        Unit unitById = CacheManager.getUnitById(currentUser.getOwnerId());
        if (CommonUtil.isBlank(unitById)) {
            unitById = this.unitService.getunitbyId(currentUser.getOwnerId());
        }
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        if (CommonUtil.isNotBlank(unitById.getGroupId())) {
            if (unitById.getGroupId().equals("JMS")) {
                PropertyFilter filter = new PropertyFilter("EQS_ownerId", unitById.getId());
                filters.add(filter);
                List<Unit> units = this.warehouseService.find(filters);
                this.getRequest().setAttribute("JMSCODE", units.get(0).getCode());
                this.getRequest().setAttribute("JMSNAME", units.get(0).getName());
            }
        }
        this.getRequest().setAttribute("ownerId", ownerId);
        this.getRequest().setAttribute("roleId", currentUser.getRoleId());
        return "/views/stock/warehStock";
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    @ResponseBody
    public DataSourceResult read(@RequestBody DataSourceRequest request) {
        return detailStockDao.getList(request, this.session);
    }

    @RequestMapping(value = "/pageCode", method = RequestMethod.POST)
    @ResponseBody
    public DataSourceResult readCode(@RequestBody DataSourceRequest request) {
        return detailStockDao.getCodeList(request, this.session);
    }

    @RequestMapping(value = "/pageStyle", method = RequestMethod.POST)
    @ResponseBody
    public DataSourceResult readStyle(@RequestBody DataSourceRequest request) {
        return detailStockDao.getStyleList(request, this.session);
    }

    //    @RequestMapping(value = "/export", method = RequestMethod.POST)
    @ResponseBody
    public void export(String fileName, String base64, String contentType,
                       HttpServletResponse response) throws IOException {

        response.setHeader("Content-Disposition", "attachment;filename="
                + fileName);
        response.setContentType(contentType);

        byte[] data = DatatypeConverter.parseBase64Binary(base64);

        response.setContentLength(data.length);
        response.getOutputStream().write(data);
        response.flushBuffer();
    }

    @RequestMapping(value = "/exportStyle")
    @ResponseBody
    public void exportStyle(String request) throws IOException {
        try {
            DataSourceRequest dataSourceRequest = JSON.parseObject(request, DataSourceRequest.class);
            DataSourceResult sourceResultSaleDtl = this.detailStockDao.getStyleList(dataSourceRequest, this.session);
            List<DetailStockChatView> resultList = (List<DetailStockChatView>) sourceResultSaleDtl.getData();
            String rootPath = session.getServletContext().getRealPath("/");
            for (DetailStockChatView dsc : resultList) {
                String url = StyleUtil.returnImageUrl(dsc.getStyleId(), rootPath);
                dsc.setUrl(url);
            }
            ExportParams params = new ExportParams("库存，按款汇总", "sheet1", ExcelType.XSSF);
            String path = Constant.Folder.Report_File_Folder;
            String dateString = CommonUtil.getDateString(new Date(), "yyyyMMdd HH_mm_ss");
            File files = new File(path + "\\库存，按款汇总");

            if (!files.exists())
                files.mkdirs();
            File file = new File(files, "库存，按款汇总-" + dateString + ".xlsx");
            file.createNewFile();
            for (DetailStockChatView d : resultList) {
                if (CommonUtil.isBlank(d.getUrl())) {
                    //没有图片设置默认图片
                    d.setUrl("/product/photo/noImg.png");
                }
            }
            //Workbook workbook = ExcelExportUtil.exportExcel(params, DetailStockChatView.class, resultList);
            Workbook workbook = ExcelExportUtil.exportBigExcel(params, DetailStockChatView.class, resultList);
            ExcelExportUtil.closeExportBigExcel();
            FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());
            workbook.write(fos);
            FileWriter fileWriter = new FileWriter(file.getName(), true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.close();
            String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;";
            this.outFile("库存，按款汇总-" + dateString + ".xlsx", file, contentType);
        } catch (IOException e) {
            e.printStackTrace();
            // return new MessageBox(false, "导出失败");
        }
    }

    @RequestMapping(value = "/export")
    @ResponseBody
    public void export(String request) throws IOException {
        try {
            DataSourceRequest dataSourceRequest = JSON.parseObject(request, DataSourceRequest.class);
            DataSourceResult sourceResultSaleDtl = this.detailStockDao.getCodeList(dataSourceRequest);
            List<DetailStockCodeView> detailStockCodeViews = (List<DetailStockCodeView>) sourceResultSaleDtl.getData();
            ExportParams params = new ExportParams("库存，按Code汇总", "sheet1", ExcelType.XSSF);
            String rootPath = session.getServletContext().getRealPath("/");
            for (DetailStockCodeView detailStockCodeView : detailStockCodeViews) {
                String url = StyleUtil.returnImageUrl(detailStockCodeView.getStyleId(), rootPath);
                detailStockCodeView.setUrl(url);
            }
            String path = Constant.Folder.Report_File_Folder;
            String dateString = CommonUtil.getDateString(new Date(), "yyyyMMdd HH_mm_ss");
            File files = new File(path + "\\库存，按Code汇总");
            if (!files.exists())
                files.mkdirs();
            File file = new File(files, "库存查询唯一码-" + dateString + ".xlsx");
            file.createNewFile();
            //FileOutputStream fos = new FileOutputStream(path + "\\库存查询唯一码-" + dateString + ".xlsx");
            //Workbook workbook = ExcelExportUtil.exportExcel(params, DetailStockCodeView.class, detailStockCodeViews);
            Workbook workbook = ExcelExportUtil.exportBigExcel(params, DetailStockCodeView.class, detailStockCodeViews);
            ExcelExportUtil.closeExportBigExcel();
            FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());
            workbook.write(fos);
            FileWriter fileWriter = new FileWriter(file.getName(), true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.close();
            String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;";
            this.outFile("库存，按Code汇总-" + dateString + ".xlsx", file, contentType);
            //return new MessageBox(true, "导出成功，请在桌面查看");
        } catch (IOException e) {
            e.printStackTrace();
            // return new MessageBox(false, "导出失败");
        }
    }


    /**
     * @param warehId 仓库id 出库为发货仓，入库为收货仓
     * @param code    唯一码
     * @param type    出库入库类型 0,出库,1入库
     * @return Messbox true ,允许操作，false允许出，入库提示msg信息
     */
    @RequestMapping("checkEpcStockAndFindDate")
    @ResponseBody
    public MessageBox checkEpcStockAndFindDate(String warehId, String code, String billNo, Integer type, boolean isCheckWareHouse) {
        try {
            if (code.length() != 13) {
                String epcCode = code.toUpperCase();
                code = EpcSecretUtil.decodeEpc(epcCode).substring(0, 13);
            }

            if (CommonUtil.isNotBlank(billNo)) {
                List<Business> businessList = this.taskService.findBusinessByBillNo(billNo);
                if (CommonUtil.isNotBlank(businessList)) {
                    List<String> taskIdList = new ArrayList<>();
                    for (Business business : businessList) {
                        taskIdList.add(business.getId());
                    }
                    String taskIdStr = TaskUtil.getSqlStrByList(taskIdList, Record.class, "taskId");
                    List<Record> recordList = this.taskService.findRecordByTaskIdAndType(taskIdStr, type);
                    List<String> codeList = new ArrayList<>();
                    for (Record record : recordList) {
                        codeList.add(record.getCode());
                    }
                    if (codeList.contains(code)) {
                        if (type == 0) {
                            return new MessageBox(false, "唯一码:" + code + " 在本单已出，不能重复出库");
                        } else if (type == 1) {
                            return new MessageBox(false, "唯一码:" + code + " 在本单已入，不能重复入库");
                        }
                    }
                }
            }

            EpcStock epcStock;

            List<EpcStock> epcStockList = new ArrayList<>();
            if (Constant.TaskType.Outbound == type) {
                epcStockList = this.epcStockService.findSaleReturnFilterByDestIdDtl(code, warehId, 1);
                if (epcStockList.size() == 0 || epcStockList.isEmpty()) {
                    epcStock = this.epcStockService.findEpcInCode(warehId, code, isCheckWareHouse);
                } else {
                    epcStock = epcStockList.get(0);
                    Long cycle = Long.parseLong("" + CommonUtil.daysBetween(epcStock.getLastSaleTime(), new Date()));
                    epcStock.setSaleCycle(cycle);
                }

            } else {
                epcStockList = this.epcStockService.findSaleReturnFilterByOriginIdDtl(code, warehId, 0);
                if (epcStockList.size() == 0 || epcStockList.isEmpty()) {
                    epcStock = this.epcStockService.findEpcNotInCode(warehId, code, isCheckWareHouse);
                } else {
                    epcStock = epcStockList.get(0);
                    Long cycle = Long.parseLong("" + CommonUtil.daysBetween(epcStock.getLastSaleTime(), new Date()));
                    epcStock.setSaleCycle(cycle);
                }
            }
            if (CommonUtil.isNotBlank(epcStock)) {
                StockUtil.convertEpcStock(epcStock);
                return new MessageBox(true, "", epcStock);
            } else {
                if (Constant.TaskType.Outbound == type) {
                    //查询这个不在库唯一码的epcStock
                    EpcStock epcAllowInCode = this.epcStockService.findEpcAllowInCode(code);
                    StockUtil.convertEpcStock(epcAllowInCode);
                    return new MessageBox(false, "唯一码:" + code + "不能出库", epcAllowInCode);
                } else {
                    return new MessageBox(false, "唯一码:" + code + "不能入库");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new MessageBox(false, e.getMessage());
        }

    }

    /**
     * 标签管理code检验是否入库
     *
     * @param warehId 仓库id 出库为发货仓，入库为收货仓
     * @param code    唯一码
     * @param type    出库入库类型 0,出库,1入库
     * @return Messbox true ,允许操作，false允许出，入库提示msg信息
     */
    @RequestMapping("checkLaberEpcStockAndFindDate")
    @ResponseBody
    public MessageBox checkLaberEpcStockAndFindDate(String warehId, String code, String billNo, Integer type, String class9) {
        try {
            if (code.length() != 13) {
                String epcCode = code.toUpperCase();
                code = EpcSecretUtil.decodeEpc(epcCode).substring(0, 13);
            }

            EpcStock epcAllowInCode = this.epcStockService.findEpcAllowInCode(code);
            Style style = CacheManager.getStyleById(epcAllowInCode.getStyleId());
            if (!style.getClass9().equals(class9)) {
                return new MessageBox(false, "唯一码:" + code + "系列不对");
            }
            if (CommonUtil.isNotBlank(billNo)) {
                List<Business> businessList = this.taskService.findBusinessByBillNo(billNo);
                if (CommonUtil.isNotBlank(businessList)) {
                    List<String> taskIdList = new ArrayList<>();
                    for (Business business : businessList) {
                        taskIdList.add(business.getId());
                    }
                    String taskIdStr = TaskUtil.getSqlStrByList(taskIdList, Record.class, "taskId");
                    List<Record> recordList = this.taskService.findRecordByTaskIdAndType(taskIdStr, type);
                    List<String> codeList = new ArrayList<>();
                    for (Record record : recordList) {
                        codeList.add(record.getCode());
                    }
                    if (codeList.contains(code)) {
                        if (type == 0) {
                            return new MessageBox(false, "唯一码:" + code + " 在本单已出，不能重复出库");
                        } else if (type == 1) {
                            return new MessageBox(false, "唯一码:" + code + " 在本单已入，不能重复入库");
                        }
                    }
                }
            }

            EpcStock epcStock;

            List<EpcStock> epcStockList = new ArrayList<>();
            if (Constant.TaskType.Outbound == type) {
                epcStockList = this.epcStockService.findSaleReturnFilterByDestIdDtl(code, warehId, 1);
                if (epcStockList.size() == 0 || epcStockList.isEmpty()) {
                    epcStock = this.epcStockService.findEpcInCode(warehId, code, false);
                } else {
                    epcStock = epcStockList.get(0);
                    Long cycle = Long.parseLong("" + CommonUtil.daysBetween(epcStock.getLastSaleTime(), new Date()));
                    epcStock.setSaleCycle(cycle);
                }

            } else {
                epcStockList = this.epcStockService.findSaleReturnFilterByOriginIdDtl(code, warehId, 0);
                if (epcStockList.size() == 0 || epcStockList.isEmpty()) {
                    epcStock = this.epcStockService.findEpcNotInCode(warehId, code, false);
                } else {
                    epcStock = epcStockList.get(0);
                    Long cycle = Long.parseLong("" + CommonUtil.daysBetween(epcStock.getLastSaleTime(), new Date()));
                    epcStock.setSaleCycle(cycle);
                }
            }
            if (CommonUtil.isNotBlank(epcStock)) {
                StockUtil.convertEpcStock(epcStock);
                return new MessageBox(true, "", epcStock);
            } else {
                if (Constant.TaskType.Outbound == type) {
                    return new MessageBox(false, "唯一码:" + code + "不能出库");
                } else {
                    return new MessageBox(false, "唯一码:" + code + "不能入库");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new MessageBox(false, e.getMessage());
        }

    }


    /**
     * @param code 唯一码
     * @return Messbox true ,允许操作，false允许出，入库提示msg信息
     * 通过唯一吗查销售订单接口
     */
    @RequestMapping("checksaleEpcStock")
    @ResponseBody
    public MessageBox checksaleEpcStock(String code) {
        if (code.length() != 13) {
            String epcCode = code.toUpperCase();
            code = EpcSecretUtil.decodeEpc(epcCode).substring(0, 13);
        }
        EpcStock epcStock;
        try {
            epcStock = this.epcStockService.findProductByCode(code);
            List<SaleOrderBillDtl> saleOrderBillDtlListBysku = this.saleOrderBillService.findSaleOrderBillDtlListBysku(epcStock.getSku());
            if (saleOrderBillDtlListBysku.size() == 0) {
                return new MessageBox(false, "沒有查詢結果");
            } else {
                return new MessageBox(true, "", saleOrderBillDtlListBysku);
            }


        } catch (Exception e) {
            e.printStackTrace();
            return new MessageBox(false, e.getMessage());
        }

    }

    /**
     * add by Anna 2018-04-20
     * 销售退货流程新增查看 原始单号＋最近销售日期＋销售周期
     *
     * @param warehId 仓库id 出库为发货仓，入库为收货仓
     * @param code    唯一码
     * @return Messbox true ,允许操作，false允许出，入库提示msg信息
     */
    @RequestMapping("inCheckEpcStockAndFindDate")
    @ResponseBody
    public MessageBox inCheckEpcStockAndFindDate(String warehId, String code, String billNo) {
        if (code.length() != 13) {
            String epcCode = code.toUpperCase();
            code = EpcSecretUtil.decodeEpc(epcCode).substring(0, 13);
        }
        try {
            if (CommonUtil.isNotBlank(billNo)) {
                List<Business> businessList = this.taskService.findBusinessByBillNo(billNo);
                if (CommonUtil.isNotBlank(businessList)) {
                    List<String> taskIdList = new ArrayList<>();
                    for (Business business : businessList) {
                        taskIdList.add(business.getId());
                    }
                    String taskIdStr = TaskUtil.getSqlStrByList(taskIdList, Record.class, "taskId");
                    List<Record> recordList = this.taskService.findRecordByTaskIdAndType(taskIdStr, 1);
                    List<String> codeList = new ArrayList<>();
                    for (Record record : recordList) {
                        codeList.add(record.getCode());
                    }
                    if (codeList.contains(code)) {
                        return new MessageBox(false, "唯一码:" + code + " 在本单已入，不能重复入库");
                    }
                }
            }

            List<EpcStock> epcStockList = this.epcStockService.findSaleReturnFilterByOriginIdDtl(code, warehId, 0);
            EpcStock epcStock;
            if (epcStockList.size() == 0 || epcStockList.isEmpty()) {
                epcStock = this.epcStockService.findEpcAllowInCode(code);
            } else {
                epcStock = epcStockList.get(0);
                Long cycle = Long.parseLong("" + CommonUtil.daysBetween(epcStock.getLastSaleTime(), new Date()));
                epcStock.setSaleCycle(cycle);
            }
            if (CommonUtil.isNotBlank(epcStock)) {
                StockUtil.convertEpcStock(epcStock);
                if (epcStock.getInStock() == 0) {
                    return new MessageBox(true, "", epcStock);
                } else {
                    return new MessageBox(false, "唯一码:" + code + "不能入库");
                }
            } else {
                Epc tagEpc = this.epcStockService.findTagEpcByCode(code);
                if (CommonUtil.isNotBlank(tagEpc)) {
                    epcStock = new EpcStock();
                    epcStock.setId(code);
                    epcStock.setCode(code);
                    epcStock.setSku(tagEpc.getSku());
                    epcStock.setStyleId(tagEpc.getStyleId());
                    epcStock.setColorId(tagEpc.getColorId());
                    epcStock.setSizeId(tagEpc.getSizeId());
                    epcStock.setInStock(0);
                    epcStock.setWarehouseId(warehId);
                    StockUtil.convertEpcStock(epcStock);
                    return new MessageBox(true, "", epcStock);
                } else {
                    return new MessageBox(false, "唯一码:" + code + "不能入库");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new MessageBox(false, e.getMessage());
        }
    }


    /**
     * 直接入库唯一码验证
     *
     * @param warehId 仓库id 出库为发货仓，入库为收货仓
     * @param code    唯一码
     * @return Messbox true ,允许操作，false允许出，入库提示msg信息
     */
    @RequestMapping("inCheckEpcStock")
    @ResponseBody
    public MessageBox inCheckEpcStock(String warehId, String code, String billNo) {
        if (code.length() != 13) {
            String epcCode = code.toUpperCase();
            code = EpcSecretUtil.decodeEpc(epcCode).substring(0, 13);
        }
        try {
            if (CommonUtil.isNotBlank(billNo)) {
                List<Business> businessList = this.taskService.findBusinessByBillNo(billNo);
                if (CommonUtil.isNotBlank(businessList)) {
                    List<String> taskIdList = new ArrayList<>();
                    for (Business business : businessList) {
                        taskIdList.add(business.getId());
                    }
                    String taskIdStr = TaskUtil.getSqlStrByList(taskIdList, Record.class, "taskId");
                    List<Record> recordList = this.taskService.findRecordByTaskIdAndType(taskIdStr, 1);
                    List<String> codeList = new ArrayList<>();
                    for (Record record : recordList) {
                        codeList.add(record.getCode());
                    }
                    if (codeList.contains(code)) {
                        return new MessageBox(false, "唯一码:" + code + " 在本单已入，不能重复入库");
                    }
                }
            }
            EpcStock epcStock = this.epcStockService.findEpcAllowInCode(code);
            if (CommonUtil.isNotBlank(epcStock)) {
                StockUtil.convertEpcStock(epcStock);
                if (epcStock.getInStock() == 0) {
                    return new MessageBox(true, "", epcStock);
                } else {
                    return new MessageBox(false, "唯一码:" + code + "不能入库");
                }
            } else {
                Epc tagEpc = this.epcStockService.findTagEpcByCode(code);
                if (CommonUtil.isNotBlank(tagEpc)) {
                    epcStock = new EpcStock();
                    epcStock.setId(code);
                    epcStock.setCode(code);
                    epcStock.setSku(tagEpc.getSku());
                    epcStock.setStyleId(tagEpc.getStyleId());
                    epcStock.setColorId(tagEpc.getColorId());
                    epcStock.setSizeId(tagEpc.getSizeId());
                    epcStock.setInStock(0);
                    epcStock.setWarehouseId(warehId);
                    StockUtil.convertEpcStock(epcStock);
                    return new MessageBox(true, "", epcStock);
                } else {
                    return new MessageBox(false, "唯一码:" + code + "不能入库");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new MessageBox(false, e.getMessage());
        }
    }


    /**
     * add by lly
     *
     * @param warehId 仓库id 出库为发货仓，入库为收货仓
     * @param code    唯一码
     * @return Messbox true ,允许操作，false允许出，入库提示msg信息
     * <p>
     * 获取当前在库唯一码信息
     */
    @RequestMapping("getEpcStock")
    @ResponseBody
    public MessageBox getEpcStock(String warehId, String code, boolean isCheckWareHouse) {
        try {
            if (code.length() != 13) {
                String epcCode = code.toUpperCase();
                code = EpcSecretUtil.decodeEpc(epcCode).substring(0, 13);
            }

            EpcStock epcStock;
            epcStock = this.epcStockService.findEpcInCode(warehId, code, isCheckWareHouse);
            //得到款名
            Style style = CacheManager.getStyleById(epcStock.getStyleId());
            epcStock.setStyleName(style.getStyleName());
            if (CommonUtil.isNotBlank(epcStock)) {
                StockUtil.convertEpcStock(epcStock);
                return new MessageBox(true, "", epcStock);
            } else {
                return new MessageBox(false, "唯一码:" + code + "不在当前选中仓库");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new MessageBox(false, "唯一码:" + code + "不在当前选中仓库或不在库");
        }

    }

    /**
     * @param warehId 仓库id 出库为发货仓，入库为收货仓
     * @param code    唯一码
     * @param type    出入库类型 0出库，1入库
     * @return Messbox true ,允许操作，false允许出，入库提示msg信息
     * <p>
     * 扫描检测唯一吗是否可以出/入库(存在库存表中)
     */
    @RequestMapping("checkEpcStock")
    @ResponseBody
    public MessageBox checkEpcStock(String warehId, String code, Integer type, String billNo, boolean isCheckWareHouse) {
        try {
            if (code.length() != 13) {
                String epcCode = code.toUpperCase();
                code = EpcSecretUtil.decodeEpc(epcCode).substring(0, 13);
            }

            if (CommonUtil.isNotBlank(billNo)) {
                List<Business> businessList = this.taskService.findBusinessByBillNo(billNo);
                if (CommonUtil.isNotBlank(businessList)) {
                    List<String> taskIdList = new ArrayList<>();
                    for (Business business : businessList) {
                        taskIdList.add(business.getId());
                    }
                    String taskIdStr = TaskUtil.getSqlStrByList(taskIdList, Record.class, "taskId");
                    List<Record> recordList = this.taskService.findRecordByTaskIdAndType(taskIdStr, type);
                    List<String> codeList = new ArrayList<>();
                    for (Record record : recordList) {
                        codeList.add(record.getCode());
                    }
                    if (codeList.contains(code)) {
                        if (type == 0) {
                            return new MessageBox(false, "唯一码:" + code + " 在本单已出，不能重复出库");
                        } else if (type == 1) {
                            return new MessageBox(false, "唯一码:" + code + " 在本单已入，不能重复入库");
                        }
                    }
                }
            }

            EpcStock epcStock;
            if (Constant.TaskType.Outbound == type) {
                epcStock = this.epcStockService.findEpcInCode(warehId, code, isCheckWareHouse);
            } else {
                epcStock = this.epcStockService.findEpcNotInCode(warehId, code, isCheckWareHouse);
            }
            if (CommonUtil.isNotBlank(epcStock)) {
                StockUtil.convertEpcStock(epcStock);
                return new MessageBox(true, "", epcStock);
            } else {
                if (Constant.TaskType.Outbound == type) {
                    //查询这个不在库唯一码的epcStock
                    EpcStock epcAllowInCode = this.epcStockService.findEpcAllowInCode(code);
                    if (CommonUtil.isNotBlank(epcAllowInCode)) {
                        StockUtil.convertEpcStock(epcAllowInCode);
                        return new MessageBox(false, "唯一码:" + code + "不能出库", epcAllowInCode);
                    } else {
                        return new MessageBox(false, "唯一码:" + code + "不存在");
                    }
                } else {
                    return new MessageBox(false, "唯一码:" + code + "不能入库");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new MessageBox(false, e.getMessage());
        }

    }

    @RequestMapping(value = "/findNotInTransfer")
    @ResponseBody
    public List<Epc> findNotInTransfer(String billNo) {
        TransferOrderBill transferOrderBill = this.transferOrderBillService.get("id", billNo);
        List<TransferOrderBillDtl> transferOrderBillDtls = this.transferOrderBillService.findBillDtlByBillNo(billNo);
        Map<String, String> codeMap = new HashMap<>();
        List<BillRecord> billRecordList = this.transferOrderBillService.getBillRecod(billNo);
     /*   String code="";
        for(BillRecord r : billRecordList){
           if(CommonUtil.isBlank(code)){
               code+=r.getCode();
           }else{
               code+=","+r.getCode();
           }
        }*/
        for (BillRecord r : billRecordList) {
            if (codeMap.containsKey(r.getSku())) {
                String code = codeMap.get(r.getSku());
                code += "," + r.getCode();
                codeMap.put(r.getSku(), code);
            } else {
                codeMap.put(r.getSku(), r.getCode());
            }
        }
        ArrayList<Epc> list = new ArrayList<Epc>();
        for (TransferOrderBillDtl dtl : transferOrderBillDtls) {
            if (codeMap.containsKey(dtl.getSku())) {
                String[] split = codeMap.get(dtl.getSku()).split(",");
                if (CommonUtil.isNotBlank(split)) {
                    for (int i = 0; i < split.length; i++) {
                        MessageBox messageBox = checkEpcStock(transferOrderBill.getDestId(), split[i], 1, billNo, false);
                        if (messageBox.getSuccess()) {
                            Epc epc = new Epc();
                            epc.setCode(split[i]);
                            epc.setSku(dtl.getSku());
                            epc.setColorId(dtl.getColorId());
                            epc.setSizeId(dtl.getSizeId());
                            epc.setStyleId(dtl.getStyleId());
                            list.add(epc);
                        }
                    }

                }
            }
        }
        return list;
    }

    /**
     * @param warehId 仓库id 出库为发货仓，入库为收货仓
     * @param codes   唯一码
     * @param type    出入库类型 0出库，1入库
     * @return Messbox true ,允许操作，false允许出，入库提示msg信息
     */
    @RequestMapping({"/checkCodes", "/checkCodesWS"})
    @ResponseBody
    public MessageBox checkCodes(String warehId, String codes, Integer type, String billNo) {
        //判断单据单据是否是商城
        try {
            boolean is_wxshop = Boolean.parseBoolean(PropertyUtil
                    .getValue("is_wxshop"));
            if (CommonUtil.isNotBlank(is_wxshop)) {
                if (Boolean.parseBoolean(PropertyUtil.getValue("is_wxshop"))) {
                    if (billNo.startsWith(BillConstant.BillPrefix.saleOrder)) {
                        //微商城销售单更新
                        this.saleOrderBillService.ShopBilldeal(billNo, codes);
                    }
                }
            }

            List<String> codeList = new ArrayList<>();

            if (type == -1) {
                type = 1;
            }
            for (String code : codes.split(",")) {
                if (CommonUtil.isNotBlank(code)) {
                    codeList.add(code);
                }
            }
            if (CommonUtil.isNotBlank(billNo)) {
                List<Business> businessList = this.taskService.findBusinessByBillNo(billNo);
                if (CommonUtil.isNotBlank(businessList)) {
                    List<String> taskIdList = new ArrayList<>();
                    for (Business business : businessList) {
                        taskIdList.add(business.getId());
                    }
                    String taskIdStr = TaskUtil.getSqlStrByList(taskIdList, Record.class, "taskId");
                    List<Record> recordList = this.taskService.findRecordByTaskIdAndType(taskIdStr, type);
                    Map<String, Record> recordMap = new HashMap<>();
                    for (Record record : recordList) {
                        recordMap.put(record.getCode(), record);
                    }
                    for (int i = codeList.size() - 1; i >= 0; i--) {
                        if (CommonUtil.isNotBlank(recordMap.get(codeList.get(i)))) {
                            codeList.remove(codeList.get(i));
                        }
                    }
                }
            }
            List<EpcStock> epcStockList;
            String result = "";
            if (CommonUtil.isNotBlank(codeList)) {
                epcStockList = this.epcStockService.findEpcInCodes(warehId, TaskUtil.getSqlStrByList(codeList, EpcStock.class, "code"));
                if (Constant.TaskType.Outbound == type) {
                    for (EpcStock s : epcStockList) {
                        result += "," + s.getCode();
                    }
                } else {
                    for (int i = epcStockList.size() - 1; i >= 0; i--) {
                        codeList.remove(epcStockList.get(i).getCode());
                    }
                    for (String code : codeList) {
                        result += "," + code;
                    }
                }
            }
            if (result.length() > 0) {
                return new MessageBox(true, "ok", result.substring(1));
            } else {
                return new MessageBox(true, "ok", result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new MessageBox(false, e.getMessage());
        }
    }


    /**
     * Wang Yushen
     * 根据前端穿回的code值，查询数据库中的EPC信息，返回EPC对象
     *
     * @param code
     * @return EPC对象
     * @throws Exception
     */
    @RequestMapping(value = "/addUniqCode")
    @ResponseBody
    public MessageBox addUniqCode(String code) throws Exception {
        this.logAllRequestParams();
        if (code.length() != 13) {
            String epcCode = code.toUpperCase();
            code = EpcSecretUtil.decodeEpc(epcCode).substring(0, 13);
        }
        EpcStock epcStock = this.epcStockService.findProductByCode(code);
        if (CommonUtil.isNotBlank(epcStock)) {
            StockUtil.convertEpcStock(epcStock);
            return this.returnSuccessInfo("", epcStock);
        } else {
            return this.returnFailInfo("唯一码: " + code + " 从未入库");
        }
    }

    /***
     * Anna
     * 销售退货表－唯一码明细中显示 原始单据，销售
     *
     * @param uniqueCodes 单据明细中，唯一码拼接起来的字符串
     * @return 唯一码明细
     */
    @RequestMapping(value = "/findCodeSaleReturnList")
    @ResponseBody
    public List<EpcStock> findCodeSaleReturnList(String uniqueCodes, String billNo) {
        List<String> codeList = new ArrayList<>();
        if (CommonUtil.isNotBlank(uniqueCodes)) {
            for (String code : uniqueCodes.split(",")) {
                codeList.add(code);
            }
        }
        Map<String, BillRecord> billRecordMap = new HashMap<>();
        if (CommonUtil.isNotBlank(billNo) && CommonUtil.isNotBlank(uniqueCodes)) {
            List<BillRecord> billRecordList = this.saleOrderReturnBillService.getBillRecordForCycle(TaskUtil.getSqlStrByList(codeList, BillRecord.class, "code"), billNo);
            for (BillRecord b : billRecordList) {
                billRecordMap.put(b.getCode(), b);
            }
        }
        List<EpcStock> epcStockList = new ArrayList<>();
        if (CommonUtil.isNotBlank(uniqueCodes)) {
            epcStockList = this.epcStockService.findSaleReturnEpcByCodes(TaskUtil.getSqlStrByList(codeList, EpcStock.class, "code"));
            for (EpcStock epc : epcStockList) {
                BillRecord billRecord = billRecordMap.get(epc.getCode());
                if (CommonUtil.isNotBlank(billRecord)) {
                    if (CommonUtil.isNotBlank(billRecord.getLastSaleTime())) {
                        epc.setLastSaleTime(billRecord.getLastSaleTime());
                    }
                    if (CommonUtil.isNotBlank(billRecord.getOriginBillNo())) {
                        epc.setOriginBillNo(billRecord.getOriginBillNo());
                    }
                    if (CommonUtil.isNotBlank(billRecord.getSaleCycle())) {
                        epc.setSaleCycle(billRecord.getSaleCycle());
                    }
                }
                Unit unit = CacheManager.getUnitById(epc.getWarehouseId());
                epc.setFloor(unit.getName());
            }
        }

        return epcStockList;
    }

    /***
     * Wang Yushen
     * 用于显示单据明细中的唯一码
     *
     * @param uniqueCodes 单据明细中，唯一码拼接起来的字符串
     * @return 唯一码明细
     */
    @RequestMapping(value = "/findCodeList")
    @ResponseBody
    public List<EpcStock> findCodeList(String uniqueCodes) {
        String codeListStringForSql = "";
        if (CommonUtil.isNotBlank(uniqueCodes)) {
            String[] codesArray = uniqueCodes.split(",");
            StringBuilder CodeListString = new StringBuilder();
            CodeListString.append("epcstock.code in (");
            for (int i = 0; i < codesArray.length; i++) {
                CodeListString.append("'").append(codesArray[i]).append("'");
                if (i == codesArray.length - 1) {
                    CodeListString.append(")");
                } else {
                    CodeListString.append(",");
                }
            }
            codeListStringForSql = CodeListString.toString();
        }
        List<EpcStock> epcStockList = this.epcStockService.findEpcByCodes(codeListStringForSql);
        for (EpcStock epc : epcStockList) {
            Unit unit = CacheManager.getUnitById(epc.getWarehouseId());
            epc.setFloor(unit.getName());
        }
        return epcStockList;
    }

    /***
     * Wang Yushen
     * 用于显示单据明细中的唯一码
     *
     * @param uniqueCodes 单据明细中，唯一码拼接起来的字符串
     * @return 唯一码明细
     */
    @RequestMapping(value = "/findCodeinList")
    @ResponseBody
    public List<EpcStock> findCodeinList(String uniqueCodes, String origId, String destId) {
        String codeListStringForSql = "";
        if (CommonUtil.isNotBlank(uniqueCodes)) {
            String[] codesArray = uniqueCodes.split(",");
            StringBuilder CodeListString = new StringBuilder();
            CodeListString.append("epcstock.code in (");
            for (int i = 0; i < codesArray.length; i++) {
                CodeListString.append("'").append(codesArray[i]).append("'");
                if (i == codesArray.length - 1) {
                    CodeListString.append(")");
                } else {
                    CodeListString.append(",");
                }
            }
            codeListStringForSql = CodeListString.toString();
        }
        List<EpcStock> epcStockList = this.epcStockService.findEpcByCodes(codeListStringForSql);
        List<EpcStock> epcStockLists = new ArrayList<EpcStock>();
        for (EpcStock epc : epcStockList) {
            if (epc.getWarehouseId().equals(origId) && epc.getInStock() != 1 && epc.getWarehouse2Id().equals(destId)) {
                Unit unit = CacheManager.getUnitById(epc.getWarehouseId());
                epc.setFloor(unit.getName());
                epcStockLists.add(epc);
            }

        }
        return epcStockLists;
    }

    /**
     * Wang Yushen
     * 根据单号、SKU返回唯一码 （用于采购订单）
     *
     * @param sku
     * @param billNo
     * @return 以逗号拼接的唯一码字符串
     */
    @RequestMapping(value = "/findCodesStr")
    @ResponseBody
    public MessageBox findCodesStr(String sku, String billNo) {

        List<Business> businessList = this.taskService.findBusinessByBillNo(billNo);

        if (CommonUtil.isNotBlank(businessList)) {
            List<String> taskIdList = new ArrayList<>();
            for (Business business : businessList) {
                taskIdList.add(business.getId());
            }

            String taskIdStr = TaskUtil.getSqlStrByList(taskIdList, Record.class, "taskId");
            List<Record> recordList = this.taskService.findRecordByTaskIdAndSku(taskIdStr, sku);

            StringBuilder codesStrBuilder = new StringBuilder();
            for (int i = 0; i < recordList.size(); i++) {
                codesStrBuilder.append(recordList.get(i).getCode());
                if (i < recordList.size() - 1) {
                    codesStrBuilder.append(",");
                }
            }

            String codesString = codesStrBuilder.toString();
            return new MessageBox(true, "", codesString);
        }
        return new MessageBox(false, "当前单据没有出入库记录");
    }

    /**
     * 仓库库存查询时，用于显示唯一码明细...
     *
     * @param sku         SKU
     * @param warehouseId 仓库ID
     * @return 唯一码明细
     */
    @RequestMapping("findInStockCodesBySku")
    @ResponseBody
    public List<EpcStock> findInStockCodesBySku(String sku, String warehouseId) {

        List<EpcStock> inStockCodes = this.epcStockService.findInStockEpcBySku(sku, warehouseId);
        for (EpcStock epc : inStockCodes) {
            Unit unit = CacheManager.getUnitById(epc.getWarehouseId());
            epc.setFloor(unit.getName());
        }
        return inStockCodes;
    }

    /**
     * Wang Yushen
     * 查询已卖出的唯一码
     *
     * @param code        唯一码
     * @param warehouseId 仓库
     * @return EPC对象
     * @throws Exception 查询失败
     */
    @RequestMapping(value = "/searchCodeForExchange")
    @ResponseBody
    public MessageBox searchCodeForExchange(String code, String warehouseId) throws Exception {
        this.logAllRequestParams();
        if (code.length() != 13) {
            String epcCode = code.toUpperCase();
            code = EpcSecretUtil.decodeEpc(epcCode).substring(0, 13);
        }
        EpcStock epcStock = this.epcStockService.findEpcAllowInCode(warehouseId, code);
        if (CommonUtil.isNotBlank(epcStock)) {
            StockUtil.convertEpcStock(epcStock);
            return this.returnSuccessInfo("", epcStock);
        } else {
            return this.returnFailInfo("查询不到唯一码：" + code);
        }
    }

    /**
     * add by yushen 订单中通过加号添加商品时，扫唯一码时先查询出款号返回给前端。前端通过查询出来的款号查对应的商品。
     */
    @RequestMapping("/getStyleIdByCode")
    @ResponseBody
    public MessageBox getStyleIdByCode(String code) throws Exception {
        this.logAllRequestParams();
        if (code.length() != 13) {
            String epcCode = code.toUpperCase();
            code = EpcSecretUtil.decodeEpc(epcCode).substring(0, 13);
        }
        EpcStock epcStock = this.epcStockService.findStockEpcByCode(code);
        if (CommonUtil.isNotBlank(epcStock)) {
            return new MessageBox(true, "查询成功", epcStock.getStyleId());
        } else {
            return new MessageBox(false, "查询不到唯一码：" + code + " 对应的商品");
        }

    }

    /**
     * @param codes  唯一码信息多个,分割
     * @param userId 当前登陆用户ID
     * @param type   0,校验不在库,1 检验在库
     * @return 判断唯一码是否在同一仓库
     **/
    @RequestMapping("/findScanCodeInfoWS")
    @ResponseBody
    public MessageBox findScanCodeInfo(String codes, String userId, Integer type) {
        String ownerId = CacheManager.getUserById(userId).getOwnerId();
        List<String> codeList = new ArrayList<>();
        List<EpcStock> errorList = new ArrayList<>();
        if (CommonUtil.isNotBlank(codes)) {
            for (String code : codes.split(",")) {
                codeList.add(code);
            }
            List<EpcStock> epcStockList = this.epcStockService.findEpcCodes(CommonUtil.getSqlStrByList(codeList, EpcStock.class, "code"), type);
            for (EpcStock epcStock : epcStockList) {
                Unit u = CacheManager.getUnitByCode(epcStock.getWarehouseId());
                if (CommonUtil.isNotBlank(u)) {
                    epcStock.setStorage(u.getName());
                }
                epcStock.setStyleName(CacheManager.getStyleNameById(epcStock.getStyleId()));
                epcStock.setColorName(CacheManager.getColorNameById(epcStock.getColorId()));
                epcStock.setSizeName(CacheManager.getSizeNameById(epcStock.getSizeId()));
                if (!ownerId.equals(epcStock.getOwnerId())) {
                    errorList.add(epcStock);
                }
            }
            if (errorList.size() > 0) {
                return new MessageBox(false, "不是本店的唯一码", errorList);
            }
            //按照是否在库分组
            Map<Integer, List<EpcStock>> epcStockMap = epcStockList.stream().collect(Collectors.groupingBy(EpcStock::getInStock));
            //按照仓库分组
            Map<String, List<EpcStock>> epcWarehouseMap = epcStockList.stream().collect(Collectors.groupingBy(EpcStock::getWarehouseId));
            if (type == Constant.InStock.InStore) {
                //校验唯一码在库时候（出库，调整等用到）
                if (epcWarehouseMap.size() > 1) {
                    return new MessageBox(false, "存在多个仓库", epcWarehouseMap.values());
                } else if (CommonUtil.isNotBlank(epcStockMap.get(Constant.InStock.NotInStore))) {
                    return new MessageBox(false, "存在不在库的唯一码", epcStockMap.get(Constant.InStock.NotInStore));
                } else {
                    return new MessageBox(true, "ok", epcStockList);
                }
            } else {
                //校验唯一码不在库时候（入库用到）
                if (CommonUtil.isNotBlank(epcStockMap.get(Constant.InStock.InStore))) {
                    return new MessageBox(false, "存在不能入库唯一码", epcStockMap.get(Constant.InStock.InStore));
                } else {
                    return new MessageBox(true, "ok", epcStockList);
                }
            }

        } else {
            return new MessageBox(false, "没有唯一码信息");
        }
    }

    /*
     * @Author Alvin.Ma
     * @Date  2018/11/22 11:16
     * @Param uniqueCodes 上传唯一码数据 "['code1','code2']" json 数组 必填
     * @Param warehouseId 仓库编号  必填
     * @Param type 出入库类型 1入库，0出库,3盘点  必填
     * @Param isAdd true,false 是否增加  必填
     * @Param rfidType 唯一码类型 'code'或者'epc'
     * @Param billNo 单号
     * @return  返回messagebox reslut为 Map<String,List<EpcStock>>  "rightEpc":校验正确唯一码List<EpcStock>，"errorEpc":校验未通过唯一码List<EpcStock>
     * "noInBill"List<EpcStock> 非本单商品
     * @Description
    **/
    @RequestMapping("/checkUniqueCodes")
    @ResponseBody
    public MessageBox checkUniqueCodes(String uniqueCodes, String warehouseId, int type, Boolean isAdd, String rfidType, String billNo) throws Exception {
        this.logAllRequestParams();
        List<String> uniqueCodeList = new ArrayList<>();
        Map<String, BillDtl> billDtlMap = new HashMap<>();
        Map<String, EpcStock> epcStokMap = new HashMap<>();//需要校验的唯一码
        List<EpcStock> rightEpcList = new ArrayList<>();
        List<EpcStock> errorEpcList = new ArrayList<>();
        List<EpcStock> noInBillEpcList = new ArrayList<>();
        if (rfidType.equals(Constant.RfidType.UniqueCode)) {
            uniqueCodeList = JSON.parseArray(uniqueCodes, String.class);
        } else {
            for (String epc : JSON.parseArray(uniqueCodes, String.class)) {
                //将epc信息转换为唯一码
                uniqueCodeList.add(EpcSecretUtil.decodeEpc(epc).substring(0, 13));
            }
        }
        if (!isAdd) {
            //扫码新增单据内容无需校验非本单唯一码,出入库需要校验
            if (CommonUtil.isNotBlank(billNo)) {
                //传入单号获取单据明细 ，唯一码明细
                List<BillDtl> billDtlList = this.erpBillService.findErpBillDtlByBillNo(billNo, type);
                for (BillDtl dtl : billDtlList) {
                    billDtlMap.put(dtl.getSku(), dtl);
                }
                List<BillRecord> billRecordList = this.epcStockService.findBillRecordList(billNo, CommonUtil.getSqlStrByList(uniqueCodeList, BillRecord.class, "code"));
                //更新单据明细唯一码
                if (CommonUtil.isNotBlank(billRecordList)) {
                    for (BillRecord r : billRecordList) {
                        BillDtl dtl = billDtlMap.get(r.getSku());
                        List<String> codeList = dtl.getCodeList();
                        if (CommonUtil.isBlank(codeList)) {
                            codeList = new ArrayList<>();
                        }
                        codeList.add(r.getCode());
                        dtl.setCodeList(codeList);
                        billDtlMap.put(dtl.getSku(), dtl);
                    }
                }
                if (billNo.subSequence(0, 2).equals(BillConstant.BillPrefix.purchase)) {
                    List<Epc> epcList = this.purchaseOrderBillService.findNotInEpc(billNo);
                    for (Epc e : epcList) {
                        BillDtl dtl = billDtlMap.get(e.getSku());
                        List<String> codeList = dtl.getCodeList();
                        if (CommonUtil.isBlank(codeList)) {
                            codeList = new ArrayList<>();
                        }
                        codeList.add(e.getCode());
                        dtl.setCodeList(codeList);
                        billDtlMap.put(dtl.getSku(), dtl);
                    }
                }

            }
        }

        List<EpcStock> epcStokcList = this.epcStockService.findEpcByCodes(CommonUtil.getSqlStrByList(uniqueCodeList, EpcStock.class, "code"));
        for (EpcStock e : epcStokcList) {
            epcStokMap.put(e.getCode(), e);
        }
        List<Epc> tagEpcList = this.epcStockService.findTagEpcByCodes(CommonUtil.getSqlStrByList(uniqueCodeList, Epc.class, "code"));
        for (Epc epc : tagEpcList) {
            if (!epcStokMap.containsKey(epc.getCode())) {
                //说明该唯一码没有出入库记录将epc 转为EpcStock 未入库
                EpcStock epcStock = new EpcStock();
                epcStock.setId(epc.getCode());
                epcStock.setCode(epc.getCode());
                epcStock.setSku(epc.getSku());
                epcStock.setStyleId(epc.getStyleId());
                epcStock.setColorId(epc.getColorId());
                epcStock.setSizeId(epc.getSizeId());
                epcStock.setInStock(0);
                epcStock.setWarehouseId(warehouseId);
                StockUtil.convertEpcStock(epcStock);
                epcStokMap.put(epcStock.getCode(), epcStock);
            }
        }
        for (String code : epcStokMap.keySet()) {
            EpcStock epcStock = epcStokMap.get(code);
            StockUtil.convertEpcStock(epcStock);
            if (type == Constant.TaskType.Inbound) {
                //入库校验
                if (isAdd) {
                    if (epcStock.getInStock() == 0) {
                        rightEpcList.add(epcStock);
                    } else {
                        errorEpcList.add(epcStock);
                    }
                } else {
                    BillDtl dtl = billDtlMap.get(epcStock.getSku());
                    if (epcStock.getInStock() == 0) {
                        //判断是否满足入库条件当前唯一码可以入库
                        if (CommonUtil.isBlank(dtl)) {
                            //明细为空表示不是当前单据加入noInBillEpcList
                            epcStock.setRemark("非本单商品");
                            noInBillEpcList.add(epcStock);
                        } else {
                            List<String> codeList = dtl.getCodeList();
                            if (CommonUtil.isNotBlank(codeList)) {
                                if (codeList.size() < dtl.getQty()) {
                                    //如果唯一码数量小于sku数量该明改单需校验sku,否则需要匹配唯一码
                                    epcStock.setRemark("校验通过");
                                    rightEpcList.add(epcStock);
                                } else {
                                    if (codeList.contains(epcStock.getCode())) {
                                        epcStock.setRemark("校验通过");
                                        rightEpcList.add(epcStock);
                                    } else {
                                        epcStock.setRemark("非本单商品");
                                        noInBillEpcList.add(epcStock);
                                    }
                                }
                            } else {
                                epcStock.setRemark("校验通过");
                                rightEpcList.add(epcStock);
                            }

                        }
                    } else {
                        if (CommonUtil.isBlank(dtl)) {
                            epcStock.setRemark("校验未通过,不能入库");
                        } else {
                            List<String> codeList = dtl.getCodeList();
                            if (CommonUtil.isNotBlank(codeList)) {
                                if (codeList.contains(epcStock.getCode())) {
                                    //说明改单已经入库
                                    epcStock.setRemark("校验未通过,已入库无需入库");
                                } else {
                                    epcStock.setRemark("校验未通过,不能入库");
                                }
                            } else {
                                epcStock.setRemark("校验未通过,不能入库");
                            }
                        }
                        errorEpcList.add(epcStock);
                    }
                }
            } else if (type == Constant.TaskType.Outbound) {
                //出库校验
                if (isAdd) {
                    //新增无需校验单据明细
                    if (epcStock.getWarehouseId().equals(warehouseId) && epcStock.getInStock() == 1) {
                        //判断是否可以出库
                        rightEpcList.add(epcStock);
                    } else {
                        errorEpcList.add(epcStock);
                    }
                } else {
                    BillDtl dtl = billDtlMap.get(epcStock.getSku());
                    if (epcStock.getWarehouseId().equals(warehouseId) && epcStock.getInStock() == 1) {
                        //判断是否可以出库
                        if (CommonUtil.isBlank(dtl)) {
                            //明细为空表示不是当前单据加入noInBillEpcList
                            epcStock.setRemark("非本单商品");
                            noInBillEpcList.add(epcStock);
                        } else {
                            List<String> codeList = dtl.getCodeList();
                            if (CommonUtil.isNotBlank(codeList)) {
                                if (codeList.size() < dtl.getQty()) {
                                    //如果唯一码数量小于sku数量该明改单需校验sku,否则需要匹配唯一码
                                    epcStock.setRemark("校验通过");
                                    rightEpcList.add(epcStock);
                                } else {
                                    if (codeList.contains(epcStock.getCode())) {
                                        epcStock.setRemark("校验通过");
                                        rightEpcList.add(epcStock);
                                    } else {
                                        epcStock.setRemark("非本单商品");
                                        noInBillEpcList.add(epcStock);
                                    }
                                }
                            } else {
                                epcStock.setRemark("校验通过");
                                rightEpcList.add(epcStock);
                            }

                        }
                    } else {
                        if (CommonUtil.isBlank(dtl)) {
                            epcStock.setRemark("校验未通过,不能出库");
                        } else {
                            List<String> codeList = dtl.getCodeList();
                            if (CommonUtil.isNotBlank(codeList)) {
                                if (codeList.contains(epcStock.getCode())) {
                                    //说明改单已经入库
                                    epcStock.setRemark("校验未通过,已出库无需出库");
                                } else {
                                    epcStock.setRemark("校验未通过,不能出库");
                                }
                            } else {
                                epcStock.setRemark("校验未通过,不能出库");
                            }
                        }
                        errorEpcList.add(epcStock);
                    }


                }

            } else if (type == Constant.TaskType.Inventory) {
                //盘点待开发
            }
        }
        Map<String, List<EpcStock>> resultMap = new HashMap<>();
        resultMap.put("rightEpc", rightEpcList);
        resultMap.put("errorEpc", errorEpcList);
        resultMap.put("noInBill", noInBillEpcList);
        return new MessageBox(true, "ok", resultMap);
    }

    /*
     * @Author Alvin.Ma
     * @Date  2018/11/30 17:28
     * @Param
     * @return
     * @Description 盘点接口
    **/

}
