package com.casesoft.dmc.extend.paris;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.logistics.BillConvertUtil;
import com.casesoft.dmc.controller.task.TaskUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.dao.task.TaskDao;
import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.erp.BillDtl;
import com.casesoft.dmc.model.erp.ErpStock;
import com.casesoft.dmc.model.logistics.*;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.rem.RepositoryManagementBill;
import com.casesoft.dmc.model.rem.RepositoryManagementBillDtl;
import com.casesoft.dmc.model.rem.UniqueCodeBill;
import com.casesoft.dmc.model.search.DetailStockView;
import com.casesoft.dmc.model.shop.SaleBill;
import com.casesoft.dmc.model.stock.CodeFirstTime;
import com.casesoft.dmc.model.stock.EpcStock;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.model.tag.Epc;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.Record;
import com.casesoft.dmc.service.logistics.*;
import com.casesoft.dmc.service.rem.RepositoryManagementBillService;
import com.casesoft.dmc.service.rem.UniqueCodeBillService;
import com.casesoft.dmc.service.search.DetailStockViewService;
import com.casesoft.dmc.service.stock.EpcStockService;
import com.casesoft.dmc.service.syn.IBillWSService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by WingLi on 2016-01-19.
 */
@Transactional
public class ParisService implements IBillWSService {
    private TaskDao taskDao;
    @Autowired
    private UniqueCodeBillService uniqueCodeBillService;
    @Autowired
    private RepositoryManagementBillService repositoryManagementBillService;
    @Autowired
    private PurchaseOrderBillService purchaseOrderBillService;
    @Autowired
    private SaleOrderBillService saleOrderBillService;
    @Autowired
    private EpcStockService epcStockService;
    @Autowired
    private TransferOrderBillService transferOrderBillService;

    @Autowired
    private SaleOrderReturnBillService saleOrderReturnBillService;

    @Autowired
    private PurchaseReturnBillService purchaseReturnBillService;

    @Autowired
    private DetailStockViewService detailStockViewService;

    @Autowired
    private ConsignmentBillService consignmentBillService;

    @Autowired
    private ReplenishBillService replenishBillService;

    public static Bill getStockBill() {
        return stockBill;
    }

    public static void setStockBill(Bill stockBill) {
        ParisService.stockBill = stockBill;
    }

    private static Bill stockBill = new Bill();

    @Transactional(readOnly = true)
    @Override
    public List<Bill> findBills(String[] properties, String[] values)
            throws Exception {

        System.out.println(System.currentTimeMillis());
        int type = Integer.parseInt(values[1]);// token 值
        int obType = 0;
        String deviceId = values[5];
        String ownerId = values[0];
        String beginDate = values[2];
        String endDate = values[3];
        String storageId = "";
        String billId = values[6];
        String unitId = values[7];//所属方id(发货方或者收货方unitId)
        String rmId = values[8];
        List<Bill> billList = new ArrayList<Bill>();
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        if (CommonUtil.isBlank(CacheManager.getDeviceByCode(deviceId))) {
            return billList;
        }
        storageId = CacheManager.getDeviceByCode(deviceId).getStorageId();

        PropertyFilter filterToken = null;
        List<Business> busList = null;
        List<TransferOrderBill> transferOrderBills = null;
        switch (type) {
            case Constant.Token.Storage_Inbound:
                filters.clear();
                filters.add(new PropertyFilter("INI_status", "" + BillConstant.BillStatus.Enter + "," + BillConstant.BillStatus.Doing));
                if (CommonUtil.isNotBlank(billId)) {
                    filters.add(new PropertyFilter("EQS_billNo", billId));
                }
                if (CommonUtil.isNotBlank(beginDate)) {
                    filters.add(new PropertyFilter("GED_billDate", beginDate));
                }
                if (CommonUtil.isNotBlank(unitId)) {
                    filters.add(new PropertyFilter("EQS_destUnitId", unitId));
                }
                if (CommonUtil.isNotBlank(endDate)) {
                    filters.add(new PropertyFilter("LED_billDate", endDate + " 23:59:59"));
                }
                List<PurchaseOrderBill> purchaseOrderBillList = this.purchaseOrderBillService.find(filters);
                billList = BillConvertUtil.convertPurchaseToBill(purchaseOrderBillList, Constant.Token.Storage_Inbound);
                break;
            case Constant.Token.Storage_Outbound:
                filters.clear();
                filters.add(new PropertyFilter("INI_status", "" + BillConstant.BillStatus.Enter + "," + BillConstant.BillStatus.Doing));
                filters.add(new PropertyFilter("NINI_outStatus", "" + BillConstant.BillInOutStatus.OutStore));
                if (CommonUtil.isNotBlank(billId)) {
                    filters.add(new PropertyFilter("EQS_billNo", billId));
                }
                if (CommonUtil.isNotBlank(beginDate)) {
                    filters.add(new PropertyFilter("GED_billDate", beginDate));
                }
                if (CommonUtil.isNotBlank(unitId)) {
                    filters.add(new PropertyFilter("EQS_origUnitId", unitId));
                }
                if (CommonUtil.isNotBlank(endDate)) {
                    filters.add(new PropertyFilter("LED_billDate", endDate + " 23:59:59"));
                }
                List<SaleOrderBill> saleOrderBillList = this.saleOrderBillService.find(filters);
                billList = BillConvertUtil.convertsaleOrderToBill(saleOrderBillList, Constant.Token.Storage_Outbound);
                break;
            case Constant.Token.Storage_Inbound_customer:
                filters.clear();
                filters.add(new PropertyFilter("INI_outStatus", "" + BillConstant.BillInOutStatus.Outing + "," + BillConstant.BillInOutStatus.OutStore));
                if (CommonUtil.isNotBlank(beginDate)) {
                    filters.add(new PropertyFilter("GED_billDate", beginDate));
                }
                if (CommonUtil.isNotBlank(billId)) {
                    filters.add(new PropertyFilter("EQS_billNo", billId));
                }
                if (CommonUtil.isNotBlank(unitId)) {
                    filters.add(new PropertyFilter("EQS_destUnitId", unitId));
                }
                if (CommonUtil.isNotBlank(endDate)) {
                    filters.add(new PropertyFilter("LED_billDate", endDate + " 23:59:59"));
                }
                List<SaleOrderBill> InsaleOrderBillList = this.saleOrderBillService.find(filters);
                billList = BillConvertUtil.convertsaleOrderToBill(InsaleOrderBillList, Constant.Token.Storage_Inbound_customer);
                break;
            case Constant.Token.Storage_Transfer_Inbound:
                filters.clear();
                filters.add(new PropertyFilter("INI_outStatus", "" + BillConstant.BillInOutStatus.Outing + "," + BillConstant.BillInOutStatus.OutStore));
                filters.add(new PropertyFilter("NINI_inStatus", "" + BillConstant.BillInOutStatus.InStore));
                if (CommonUtil.isNotBlank(billId)) {
                    filters.add(new PropertyFilter("EQS_billNo", billId));
                }
                if (CommonUtil.isNotBlank(unitId)) {
                    filters.add(new PropertyFilter("EQS_destUnitId", unitId));
                }
                if (CommonUtil.isNotBlank(beginDate)) {
                    filters.add(new PropertyFilter("GED_billDate", beginDate));
                }
                if (CommonUtil.isNotBlank(endDate)) {
                    filters.add(new PropertyFilter("LED_billDate", endDate + " 23:59:59"));
                }
                transferOrderBills = this.transferOrderBillService.find(filters);
                billList = BillConvertUtil.convertTransferOrderToBill(transferOrderBills, Constant.Token.Storage_Transfer_Inbound);
                break;
            case Constant.Token.Storage_Transfer_Outbound:
                filters.clear();
                filters.add(new PropertyFilter("INI_status", "" + BillConstant.BillStatus.Enter + "," + BillConstant.BillStatus.Doing));
                filters.add(new PropertyFilter("NINI_outStatus", "" + BillConstant.BillInOutStatus.OutStore));
                if (CommonUtil.isNotBlank(billId)) {
                    filters.add(new PropertyFilter("EQS_billNo", billId));
                }
                if (CommonUtil.isNotBlank(unitId)) {
                    filters.add(new PropertyFilter("EQS_origUnitId", unitId));
                }
                if (CommonUtil.isNotBlank(beginDate)) {
                    filters.add(new PropertyFilter("GED_billDate", beginDate));
                }
                if (CommonUtil.isNotBlank(endDate)) {
                    filters.add(new PropertyFilter("LED_billDate", endDate + " 23:59:59"));
                }
                transferOrderBills = this.transferOrderBillService.find(filters);
                billList = BillConvertUtil.convertTransferOrderToBill(transferOrderBills, Constant.Token.Storage_Transfer_Outbound);
                break;
            case Constant.Token.Storage_Refund_Outbound://采购退货出库
                filters.clear();
                filters.add(new PropertyFilter("INI_status", "" + BillConstant.BillStatus.Enter + "," + BillConstant.BillStatus.Doing));
                filters.add(new PropertyFilter("NINI_outStatus", "" + BillConstant.BillInOutStatus.OutStore));
                if (CommonUtil.isNotBlank(billId)) {
                    filters.add(new PropertyFilter("EQS_billNo", billId));
                }
                if (CommonUtil.isNotBlank(unitId)) {
                    filters.add(new PropertyFilter("EQS_origUnitId", unitId));
                }
                if (CommonUtil.isNotBlank(beginDate)) {
                    filters.add(new PropertyFilter("GED_billDate", beginDate));
                }
                if (CommonUtil.isNotBlank(endDate)) {
                    filters.add(new PropertyFilter("LED_billDate", endDate + " 23:59:59"));
                }
                List<PurchaseReturnBill> purchaseReturnBills = this.purchaseReturnBillService.find(filters);
                billList = BillConvertUtil.convertPurchaseReturnToBill(purchaseReturnBills, Constant.Token.Storage_Refund_Outbound);
                break;
            case Constant.Token.Storage_refoundOut_customer://销售退货出库
                filters.clear();
                filters.add(new PropertyFilter("INI_status", "" + BillConstant.BillStatus.Enter + "," + BillConstant.BillStatus.Doing));
                filters.add(new PropertyFilter("NINI_outStatus", "" + BillConstant.BillInOutStatus.OutStore));
                if (CommonUtil.isNotBlank(billId)) {
                    filters.add(new PropertyFilter("EQS_billNo", billId));
                }
                if (CommonUtil.isNotBlank(unitId)) {
                    filters.add(new PropertyFilter("EQS_destUnitId", unitId));
                }
                if (CommonUtil.isNotBlank(beginDate)) {
                    filters.add(new PropertyFilter("GED_billDate", beginDate));
                }
                if (CommonUtil.isNotBlank(endDate)) {
                    filters.add(new PropertyFilter("LED_billDate", endDate + " 23:59:59"));
                }
                List<SaleOrderReturnBill> saleOrderReturnBillList = this.saleOrderReturnBillService.find(filters);
                billList = BillConvertUtil.convertSaleOrderReturnToBill(saleOrderReturnBillList, Constant.Token.Storage_refoundOut_customer);
                break;
            case Constant.Token.Storage_Refund_Inbound://销售退货入库
                filters.clear();
                filters.add(new PropertyFilter("INI_status", "" + BillConstant.BillStatus.Enter + "," + BillConstant.BillStatus.Doing));
                filters.add(new PropertyFilter("NINI_outStatus", "" + BillConstant.BillInOutStatus.OutStore));
                if (CommonUtil.isNotBlank(billId)) {
                    filters.add(new PropertyFilter("EQS_billNo", billId));
                }
                if (CommonUtil.isNotBlank(beginDate)) {
                    filters.add(new PropertyFilter("GED_billDate", beginDate));
                }
                if (CommonUtil.isNotBlank(unitId)) {
                    filters.add(new PropertyFilter("EQS_destUnitId", unitId));
                }
                if (CommonUtil.isNotBlank(endDate)) {
                    filters.add(new PropertyFilter("LED_billDate", endDate + " 23:59:59"));
                }
                List<SaleOrderReturnBill> saleOrderReturnBills = this.saleOrderReturnBillService.find(filters);
                billList = BillConvertUtil.convertSaleOrderReturnToBill(saleOrderReturnBills, Constant.Token.Storage_Refund_Inbound);
                break;
            case Constant.Token.Storage_Consigment_Inbound:
                filters.clear();
                filters.add(new PropertyFilter("INI_status", "" + BillConstant.BillStatus.Enter + "," + BillConstant.BillStatus.Doing));
                if (CommonUtil.isNotBlank(billId)) {
                    filters.add(new PropertyFilter("EQS_billNo", billId));
                }
                if (CommonUtil.isNotBlank(beginDate)) {
                    filters.add(new PropertyFilter("GED_billDate", beginDate));
                }
                if (CommonUtil.isNotBlank(unitId)) {
                    filters.add(new PropertyFilter("EQS_destUnitId", unitId));
                }
                if (CommonUtil.isNotBlank(endDate)) {
                    filters.add(new PropertyFilter("LED_billDate", endDate + " 23:59:59"));
                }
                List<ConsignmentBill> consignmentBillList = this.consignmentBillService.find(filters);
                billList = BillConvertUtil.convertConsignmentBillBill(consignmentBillList, Constant.Token.Storage_Consigment_Inbound);
                break;
            case Constant.Token.Storage_Inventory:
                String warehId = CacheManager.getUnitByCode(unitId).getDefaultWarehId();
                String rackId = null;
                String levelId = null;
                String allocationId = null;
                if (CommonUtil.isNotBlank(rmId)){
                    String []rm = rmId.split("-");
                    if (rm.length == 1){
                        warehId = rm[0];
                    }
                    else if(rm.length ==2){
                        rackId = rm[0]+"-"+rm[1];
                    }
                    else if(rm.length ==3){
                        levelId = rm[0]+"-"+rm[1]+"-"+rm[2];
                    }
                    else{
                        allocationId = rmId;
                    }
                }
                if (CommonUtil.isNotBlank(warehId)) {
                    List<Record> records = new ArrayList<>();
                    List<EpcStock> stocks = epcStockService.findStock(warehId,rackId,levelId,allocationId);
                    Map<String, Integer> skuCountMap = new HashMap<>();
                    for (EpcStock s : stocks) {
                        Record record = new Record();
                        record.setCode(s.getCode());
                        record.setStyleId(s.getStyleId());
                        record.setColorId(s.getColorId());
                        record.setSizeId(s.getSizeId());
                        record.setOwnerId(s.getOwnerId());
                        record.setOrigId(s.getWarehouseId());
                        record.setDestId(s.getWarehouseId());
                        records.add(record);
                        if (skuCountMap.containsKey(s.getSku())) {
                            Integer totQty = skuCountMap.get(s.getSku()) + 1;
                            skuCountMap.put(s.getSku(), totQty);
                        } else {
                            skuCountMap.put(s.getSku(), 1);
                        }
                    }
                    stockBill = new Bill();
                    stockBill.setId("IV" + System.currentTimeMillis());
                    stockBill.setBillNo(stockBill.getId());
                    stockBill.setBillDate(new Date());
                    stockBill.setType(type);
                    stockBill.setOwnerId(unitId);
                    stockBill.setOrigId(warehId);
                    stockBill.setDestId(warehId);
                    stockBill.setRmRecord(rmId);
                    stockBill.setSkuQty((long) skuCountMap.keySet().size());
                    List<BillDtl> billDtlList = new ArrayList<>();
                    for (String sku : skuCountMap.keySet()) {
                        BillDtl billDtl = new BillDtl();
                        billDtl.setId(new GuidCreator().toString());
                        billDtl.setBillId(stockBill.getId());
                        billDtl.setBillNo(stockBill.getBillNo());
                        billDtl.setSku(sku);
                        Product p = CacheManager.getProductByCode(sku);
                        if (CommonUtil.isNotBlank(p)) {
                            billDtl.setStyleId(p.getStyleId());
                            billDtl.setColorId(p.getColorId());
                            billDtl.setSizeId(p.getSizeId());
                        }
                        billDtl.setQty((long) skuCountMap.get(sku));
                        billDtlList.add(billDtl);
                    }
                    stockBill.setTotQty((long) stocks.size());
                    stockBill.setDtlList(billDtlList);

                    stockBill.setRecords(records);
                    if (stockBill.getTotQty() > 0) {
                        billList.add(stockBill);
                    }
                }
                break;

            default:
                break;
        }
        return billList;

    }

    @Transactional(readOnly = true)
    @Override
    public List<BillDtl> findBillDtls(String[] properties, String[] values)
            throws Exception {
        String billId = values[0];
        int type = Integer.parseInt(values[1]);

        List<BillDtl> dtlList = new ArrayList<>();
        List<TransferOrderBillDtl> transferOrderBillDtlList;
        switch (type) {
            case Constant.Token.Storage_Inbound:
                List<PurchaseOrderBillDtl> purchaseOrderBillDtlList = this.purchaseOrderBillService.findBillDtlByBillNo(billId);
                dtlList = BillConvertUtil.covertPurchaseDtlToBillDtl(purchaseOrderBillDtlList);
                break;
            case Constant.Token.Storage_Outbound:
                List<SaleOrderBillDtl> saleOrderBillDtlList = this.saleOrderBillService.findBillDtlByBillNo(billId);
                dtlList = BillConvertUtil.covertSaleOrderDtlToBillDtl(saleOrderBillDtlList, Constant.Token.Storage_Outbound);
                break;
            case Constant.Token.Storage_Inbound_customer:
                List<SaleOrderBillDtl> saleOrderBillDtlList1 = this.saleOrderBillService.findBillDtlByBillNo(billId);
                dtlList = BillConvertUtil.covertSaleOrderDtlToBillDtl(saleOrderBillDtlList1, Constant.Token.Storage_Inbound_customer);
                break;
            case Constant.Token.Storage_Transfer_Inbound:
                transferOrderBillDtlList = this.transferOrderBillService.findBillDtlByBillNo(billId);
                dtlList = BillConvertUtil.convertTransferOrderDtlToBillDtl(transferOrderBillDtlList, Constant.Token.Storage_Transfer_Inbound);
                break;
            case Constant.Token.Storage_Refund_Outbound://采购退货出库
                List<PurchaseReturnBillDtl> purchaseReturnBillDtls = this.purchaseReturnBillService.findDetailsByBillNo(billId);
                dtlList = BillConvertUtil.convertPurchaseReturnDtlToBillDtl(purchaseReturnBillDtls);
                break;
            case Constant.Token.Storage_Transfer_Outbound:
                transferOrderBillDtlList = this.transferOrderBillService.findBillDtlByBillNo(billId);
                dtlList = BillConvertUtil.convertTransferOrderDtlToBillDtl(transferOrderBillDtlList, Constant.Token.Storage_Transfer_Inbound);
                break;

            case Constant.Token.Storage_refoundOut_customer://销售退货出库
                List<SaleOrderReturnBillDtl> saleOrderReturnBillDtls = this.saleOrderReturnBillService.findDtlByBillNo(billId);
                dtlList = BillConvertUtil.convertSaleOrderReturnDtlToBillDtl(saleOrderReturnBillDtls, Constant.Token.Storage_refoundOut_customer);
                break;
            case Constant.Token.Storage_Refund_Inbound://销售退货入库
                List<SaleOrderReturnBillDtl> saleOrderReturnBillDtlList = this.saleOrderReturnBillService.findDtlByBillNo(billId);
                dtlList = BillConvertUtil.convertSaleOrderReturnDtlToBillDtl(saleOrderReturnBillDtlList, Constant.Token.Storage_Refund_Inbound);
                break;
            case Constant.Token.Storage_Consigment_Inbound:
                List<ConsignmentBillDtl> consignmentBillDtlList = this.consignmentBillService.findDtlByBillNo(billId);
                dtlList = BillConvertUtil.convertConsignmentBillDtlListToBillDtl(consignmentBillDtlList, Constant.Token.Storage_Consigment_Inbound);
                break;
            case Constant.Token.Storage_Inventory://盘点
                dtlList = stockBill.getDtlList();

                break;
            default:
                break;
        }
        return dtlList;
    }

    @Override
    public List<BillDtl> findBillDtls(String id) {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public String findBillsJSON(String[] properties, String[] values)
            throws Exception {
        return null;
    }

    @Override
    public String findBillDtlsJSON(String[] properties, String[] values)
            throws Exception {
        return null;
    }

    @Override
    public String findBillDtlsJSON(String id) throws Exception {
        return null;
    }

    @Override
    public MessageBox uploadToERP(Bill bill) {
        return null;
    }

    @Override
    public MessageBox uploadTaskToErp(Business bus) {
        try {
            if (CommonUtil.isNotBlank(bus.getBillNo())) {
                TransferOrderBill transferOrderBill;
                List<TransferOrderBillDtl> transferOrderBillDtlList;
                String warehouseId="";
                String OwnerId="";
                bus.setBeginTime(new Date());
                switch (bus.getToken().intValue()) {
                    case Constant.Token.Storage_Inbound:
                        PurchaseOrderBill purchaseOrderBill = this.purchaseOrderBillService.get("id",bus.getBillNo());
                        List<PurchaseOrderBillDtl> dtlListPI = this.purchaseOrderBillService.findBillDtlByBillNo(bus.getBillNo());
                        List<PurchaseOrderBillDtl> purchaseOrderBillDtlList = this.copyNewPIBillDtl(dtlListPI);
                        BillConvertUtil.covertToPurchaseBusiness(purchaseOrderBill, purchaseOrderBillDtlList, bus);
                        String srcBillNo = purchaseOrderBill.getSrcBillNo();
                        if(CommonUtil.isNotBlank(srcBillNo)){
                            ReplenishBill replenishBill = this.replenishBillService.get("id", srcBillNo);
                            List<ReplenishBillDtl> replenishBillDtlList = this.replenishBillService.findBillDtl(srcBillNo);
                            //复制一遍，后面保存的时候会先删除
                            ArrayList<ReplenishBillDtl> newReplenishBillDtlList = new ArrayList<>();
                            for (ReplenishBillDtl dtl : replenishBillDtlList) {
                                ReplenishBillDtl replenishBillDtl = new ReplenishBillDtl();
                                BeanUtils.copyProperties(dtl, replenishBillDtl);
                                replenishBillDtl.setId(new GuidCreator().toString());
                                newReplenishBillDtlList.add(replenishBillDtl);
                            }
                            BillConvertUtil.convertPurchaseToReplenish(purchaseOrderBill,purchaseOrderBillDtlList,replenishBill,newReplenishBillDtlList);
                            this.purchaseOrderBillService.save(purchaseOrderBill, purchaseOrderBillDtlList);
                            this.replenishBillService.saveMessage(replenishBill, newReplenishBillDtlList);
                        }
                        warehouseId=purchaseOrderBill.getDestId();
                        OwnerId=purchaseOrderBill.getDestUnitId();
                        break;
                    case Constant.Token.Storage_Outbound:
                        SaleOrderBill saleOrderBill = this.saleOrderBillService.load(bus.getBillNo());
                        List<SaleOrderBillDtl> dtlListSO = this.saleOrderBillService.findBillDtlByBillNo(bus.getBillNo());
                        List<SaleOrderBillDtl> saleOrderBillDtlList = this.copyNewSOBillDtl(dtlListSO);
                        BillConvertUtil.covertToSaleOrderBusiness(saleOrderBill, saleOrderBillDtlList, bus);
                        this.saleOrderBillService.save(saleOrderBill, saleOrderBillDtlList,null);
                        break;
                    case Constant.Token.Storage_Inbound_customer:
                        SaleOrderBill saleOrderInBill = this.saleOrderBillService.load(bus.getBillNo());
                        List<SaleOrderBillDtl> dtlListSOI = this.saleOrderBillService.findBillDtlByBillNo(bus.getBillNo());
                        List<SaleOrderBillDtl> saleOrderInBillDtlList = this.copyNewSOBillDtl(dtlListSOI);
                        BillConvertUtil.covertToSaleOrderBusinessIn(saleOrderInBill, saleOrderInBillDtlList, bus);
                        this.saleOrderBillService.saveBusinessIn(saleOrderInBill, saleOrderInBillDtlList,bus);
                        warehouseId=saleOrderInBill.getDestId();
                        OwnerId=saleOrderInBill.getDestUnitId();
                        break;
                    case Constant.Token.Storage_Transfer_Outbound:
                        transferOrderBill = this.transferOrderBillService.get("billNo", bus.getBillNo());
                        List<TransferOrderBillDtl> dtlListTRO = this.transferOrderBillService.findBillDtlByBillNo(bus.getBillNo());
                        transferOrderBillDtlList = this.copyNewTRBillDtl(dtlListTRO);
                        BillConvertUtil.covertToTransferOrderBusiness(transferOrderBill, transferOrderBillDtlList, bus, Constant.Token.Storage_Transfer_Outbound);
                        this.transferOrderBillService.save(transferOrderBill, transferOrderBillDtlList);
                        break;
                    case Constant.Token.Storage_Transfer_Inbound:
                        transferOrderBill = this.transferOrderBillService.get("billNo", bus.getBillNo());
                        List<TransferOrderBillDtl> dtlListTRI = this.transferOrderBillService.findBillDtlByBillNo(bus.getBillNo());
                        transferOrderBillDtlList = this.copyNewTRBillDtl(dtlListTRI);
                        BillConvertUtil.covertToTransferOrderBusiness(transferOrderBill, transferOrderBillDtlList, bus, Constant.Token.Storage_Transfer_Inbound);
                        this.transferOrderBillService.saveBusinessIn(transferOrderBill, transferOrderBillDtlList,bus);
                        warehouseId=transferOrderBill.getDestId();
                        OwnerId=transferOrderBill.getDestUnitId();
                        break;
                    case Constant.Token.Storage_Refund_Outbound://采购退货出库
                        PurchaseReturnBill purchaseReturnBill = this.purchaseReturnBillService.load(bus.getBillNo());
                        List<PurchaseReturnBillDtl> dtlListPR = this.purchaseReturnBillService.findDetailsByBillNo(bus.getBillNo());
                        List<PurchaseReturnBillDtl> purchaseReturnBillDtls = this.copyNewPRBillDtl(dtlListPR);
                        BillConvertUtil.convertPurchaseReturnBillToBusiness(purchaseReturnBill, purchaseReturnBillDtls, bus);
                        this.purchaseReturnBillService.saveBatch(purchaseReturnBill, purchaseReturnBillDtls);
                        break;
                    case Constant.Token.Storage_refoundOut_customer://销售退货出库
                        SaleOrderReturnBill saleOrderReturnBill = this.saleOrderReturnBillService.load(bus.getBillNo());
                        List<SaleOrderReturnBillDtl> dtlListSRO = this.saleOrderReturnBillService.findDetailsByBillNo(bus.getBillNo());
                        List<SaleOrderReturnBillDtl> saleOrderReturnOutBillDtl = this.copyNewSRBillDtl(dtlListSRO);
                        BillConvertUtil.convertToSaleOrderReturnBusinessOut(saleOrderReturnBill, saleOrderReturnOutBillDtl, bus);
                        this.saleOrderReturnBillService.saveReturnBatch(saleOrderReturnBill, saleOrderReturnOutBillDtl,null);
                        break;
                    case Constant.Token.Storage_Refund_Inbound://销售退货入库
                        SaleOrderReturnBill saleOrderReturnBill1 = this.saleOrderReturnBillService.load(bus.getBillNo());
                        List<SaleOrderReturnBillDtl> dtlListSRI = this.saleOrderReturnBillService.findDtlByBillNo(bus.getBillNo());
                        List<SaleOrderReturnBillDtl> saleOrderReturnInBillDtl = this.copyNewSRBillDtl(dtlListSRI);
                        BillConvertUtil.convertSaleOrderReturnBusinessIn(saleOrderReturnBill1, saleOrderReturnInBillDtl, bus);
                        this.saleOrderReturnBillService.saveReturnBatch(saleOrderReturnBill1, saleOrderReturnInBillDtl,null);
                        warehouseId=saleOrderReturnBill1.getDestId();
                        OwnerId=saleOrderReturnBill1.getDestUnitId();
                        break;
                    case Constant.Token.Storage_Consigment_Inbound:
                        ConsignmentBill consignmentBill = this.consignmentBillService.findBillByBillNo(bus.getBillNo());
                        List<ConsignmentBillDtl> consignmentBillDtlList = this.consignmentBillService.findDtlByBillNo(bus.getBillNo());
                        BillConvertUtil.convertConsignmentBillBillIn(consignmentBill, consignmentBillDtlList, bus);
                        warehouseId=consignmentBill.getDestId();
                        OwnerId=consignmentBill.getDestUnitId();
                        break;
                    case Constant.Token.Storage_Inventory:
                        Bill bill = bus.getBill();
                        bus.setDestId(bill.getDestId());
                        bus.setDestUnitId(bill.getDestUnitId());
                        bus.setOrigId(bill.getOrigId());
                        bus.setOrigUnitId(bill.getOrigUnitId());
                        BillConvertUtil.convertToUploadInventoryRecord(bus);
                        this.taskDao.doBatchInsert(bus.getInventoryRecordList());
                        break;


                }
                //记录第一次入库时间
                if(bus.getType().equals(Constant.TaskType.Inbound)){
                    List<Record> recordList = bus.getRecordList();
                    ArrayList<CodeFirstTime> list=new ArrayList<CodeFirstTime>();

                    for(Record r : recordList){
                        CodeFirstTime codeFirstTime =this.taskDao.findUnique("from CodeFirstTime where code=? and warehouseId=?",new Object[]{r.getCode(),warehouseId});
                        BillConvertUtil.setEpcStockPrice(codeFirstTime,r,list,warehouseId);
                    }
                    if(list.size()!=0){
                        this.taskDao.doBatchInsert(list);
                    }
                }
            }
            else{
                switch (bus.getToken().intValue()) {
                    case Constant.Token.Storage_Repository_Adjust:
                        RepositoryManagementBill repositoryManagementBill =new RepositoryManagementBill();
                        //解析上传的新库位
                        String rmId = bus.getRmId();
                        String wareId = null;
                        String rackId = null;
                        String levelId = null;
                        String allocationId = null;
                        if (CommonUtil.isNotBlank(rmId)){
                            String []rm = rmId.split("-");
                            wareId = rm[0];
                            rackId = rm[0]+"-"+rm[1];
                            levelId = rm[0]+"-"+rm[1]+"-"+rm[2];
                            allocationId = rmId;
                        }
                        //判断是否有不在同一仓库的code
                        List<String> rmcodes = TaskUtil.getRecordCodes(bus.getRecordList());
                        String rmcode = TaskUtil.getSqlStrByList(rmcodes, EpcStock.class, "code");
                        // 判断是否一个仓库
                        List<EpcStock> rmlist = this.getInStock(rmcode, wareId);
                        TaskUtil.getDifferEpcStockCodes(rmlist, rmcodes);
                        if(CommonUtil.isNotBlank(rmcodes)){
                            return new MessageBox(false, "存在不在所选仓库的唯一码!" + JSON.toJSON(rmcodes));
                        }
                        else{
                            if(CommonUtil.isNotBlank(rmlist)){
                                String userId = bus.getOperator();
                                User user = CacheManager.getUserById(userId);
                                repositoryManagementBill.setOwnerId(user.getOwnerId());
                                if (CommonUtil.isBlank(repositoryManagementBill.getBillNo())) {
                                    String prefix = BillConstant.BillPrefix.rmADjust
                                            + CommonUtil.getDateString(new Date(), "yyMMddHHmmssSSS");
                                    repositoryManagementBill.setId(prefix);
                                    repositoryManagementBill.setBillNo(prefix);
                                    repositoryManagementBill.setStatus(Constant.TaskStatus.Submitted);
                                    repositoryManagementBill.setBillDate(new Date());
                                } else {
                                    //查询单据状态
                                    Integer status = this.repositoryManagementBillService.findBillStatus(repositoryManagementBill.getBillNo());
                                    if (status != Constant.ScmConstant.BillStatus.saved && !userId.equals("admin")) {
                                        return new MessageBox(false, "单据不是录入状态无法保存,请返回");
                                    }
                                }
                                repositoryManagementBill.setOrigId(wareId);
                                repositoryManagementBill.setNrackId(rackId);
                                repositoryManagementBill.setNlevelId(levelId);
                                repositoryManagementBill.setNallocationId(allocationId);

                                List<RepositoryManagementBillDtl> repositoryManagementBillDtls = new ArrayList<>();
                                for (EpcStock epcStock : rmlist){
                                    RepositoryManagementBillDtl repositoryManagementBillDtl = new RepositoryManagementBillDtl();
                                    if(CommonUtil.isBlank(repositoryManagementBillDtl.getId())){
                                        repositoryManagementBillDtl.setId(new GuidCreator().toString());
                                    }
                                    repositoryManagementBillDtl.setBillId(repositoryManagementBill.getId());
                                    repositoryManagementBillDtl.setBillNo(repositoryManagementBill.getBillNo());
                                    repositoryManagementBillDtl.setStatus(repositoryManagementBill.getStatus());
                                    repositoryManagementBillDtl.setQty(1L);
                                    repositoryManagementBillDtl = this.covert(repositoryManagementBillDtl,epcStock);
                                    repositoryManagementBillDtls.add(repositoryManagementBillDtl);
                                    String oldRmId = null;
                                    if(CommonUtil.isNotBlank(epcStock.getFloorAllocation())){
                                        String r =epcStock.getFloorRack().split("-")[1];
                                        String l = epcStock.getFloorArea().split("-")[2];
                                        String a = epcStock.getFloorAllocation().split("-")[3];
                                        oldRmId = r+"号货架-"+l+"号货层-"+a+"号货位";
                                    }
                                    //保存变动记录表
                                    this.saveOldRm(repositoryManagementBill.getBillNo(),epcStock.getSku(),epcStock.getCode(),oldRmId,epcStock.getWarehouseId(),userId);
                                }
                                bus.setBillNo(repositoryManagementBill.getBillNo());
                                repositoryManagementBillService.save(repositoryManagementBill,repositoryManagementBillDtls);
                            }
                        }
                        break;
                }
            }

            bus.setEndTime(new Date());
            return new MessageBox(true, "");
        } catch (Exception e) {
            e.printStackTrace();
            return new MessageBox(false, "更新订单信息失败:" + e.getMessage());
        }

    }

    private List<PurchaseOrderBillDtl> copyNewPIBillDtl(List<PurchaseOrderBillDtl> dtlList) {
        List<PurchaseOrderBillDtl> purchaseOrderBillDtlList = new ArrayList<>();
        for(PurchaseOrderBillDtl dtl : dtlList){
            PurchaseOrderBillDtl purchaseOrderBillDtl = new PurchaseOrderBillDtl();
            BeanUtils.copyProperties(dtl,purchaseOrderBillDtl);
            purchaseOrderBillDtl.setId(new GuidCreator().toString());
            purchaseOrderBillDtlList.add(purchaseOrderBillDtl);
        }
        return purchaseOrderBillDtlList;
    }

    private List<TransferOrderBillDtl> copyNewTRBillDtl(List<TransferOrderBillDtl> dtlList){
        List<TransferOrderBillDtl> transferOrderBillDtlList = new ArrayList<>();
        for (TransferOrderBillDtl dtl : dtlList) {
            TransferOrderBillDtl newDtl = new TransferOrderBillDtl();
            BeanUtils.copyProperties(dtl,newDtl);
            newDtl.setId(new GuidCreator().toString());
            transferOrderBillDtlList.add(newDtl);
        }
        return transferOrderBillDtlList;
    }

    private List<SaleOrderBillDtl> copyNewSOBillDtl(List<SaleOrderBillDtl> dtlList){
        List<SaleOrderBillDtl> saleOrderBillDtlList = new ArrayList<>();
        for (SaleOrderBillDtl dtl : dtlList) {
            SaleOrderBillDtl newDtl = new SaleOrderBillDtl();
            BeanUtils.copyProperties(dtl,newDtl);
            newDtl.setId(new GuidCreator().toString());
            saleOrderBillDtlList.add(newDtl);
        }
        return saleOrderBillDtlList;
    }

    private List<SaleOrderReturnBillDtl> copyNewSRBillDtl(List<SaleOrderReturnBillDtl> dtlList){
        List<SaleOrderReturnBillDtl> SaleOrderReturnBillDtlList = new ArrayList<>();
        for (SaleOrderReturnBillDtl dtl : dtlList) {
            SaleOrderReturnBillDtl newDtl = new SaleOrderReturnBillDtl();
            BeanUtils.copyProperties(dtl,newDtl);
            newDtl.setId(new GuidCreator().toString());
            SaleOrderReturnBillDtlList.add(newDtl);
        }
        return SaleOrderReturnBillDtlList;
    }

    private List<PurchaseReturnBillDtl> copyNewPRBillDtl(List<PurchaseReturnBillDtl> dtlList){
        List<PurchaseReturnBillDtl> purchaseReturnBillDtlList = new ArrayList<>();
        for (PurchaseReturnBillDtl dtl : dtlList) {
            PurchaseReturnBillDtl newDtl = new PurchaseReturnBillDtl();
            BeanUtils.copyProperties(dtl,newDtl);
            newDtl.setId(new GuidCreator().toString());
            purchaseReturnBillDtlList.add(newDtl);
        }
        return purchaseReturnBillDtlList;
    }

    private List<ConsignmentBillDtl> copyNewCMBillDtl(List<ConsignmentBillDtl> dtlList){
        List<ConsignmentBillDtl> consignmentBillDtlList = new ArrayList<>();
        for (ConsignmentBillDtl dtl : dtlList) {
            ConsignmentBillDtl newDtl = new ConsignmentBillDtl();
            BeanUtils.copyProperties(dtl,newDtl);
            newDtl.setId(new GuidCreator().toString());
            consignmentBillDtlList.add(newDtl);
        }
        return consignmentBillDtlList;
    }

    @Override
    public MessageBox delete(String id) {
        return new MessageBox(false, "");
    }

    @Override
    public MessageBox update(Business bus) {
        return new MessageBox(false, "");
    }

    @Override
    public MessageBox uploadPosToERP(SaleBill bill) {
        return new MessageBox(true, "");
    }

    public EpcStockService getEpcStockService() {
        return epcStockService;
    }

    public void setEpcStockService(EpcStockService epcStockService) {
        this.epcStockService = epcStockService;
    }

    @Override
    public List<ErpStock> findErpStock(String[] properties, String[] values)
            throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Product> findErpBasic(String styleId, String colorId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Bill productBill(String[] properties, String[] values) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String destroyBill(String[] properties, String[] values) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MessageBox checkEpcStock(Business bus) {
        MessageBox msgBox = new MessageBox(true, "不检查EPC库存");
        Map<String, Record> recordMap = new HashMap<>();
        for (Record r : bus.getRecordList()) {
            recordMap.put(r.getCode(), r);
        }
        String codeStr = "";
        switch (bus.getToken().intValue()) {
            case Constant.Token.Storage_Adjust_Inbound:
            case Constant.Token.Storage_Refund_Inbound:
            case Constant.Token.Storage_Inbound:
            case Constant.Token.Shop_Adjust_Inbound:
            case Constant.Token.Shop_Transfer_Inbound:
            case Constant.Token.Shop_Inbound:
            case Constant.Token.Shop_Sales_refund:
            case Constant.Token.Storage_Inbound_agent_refund:
            case Constant.Token.Storage_Transfer_Inbound:
            case Constant.Token.Storage_Inbound_customer:
            case Constant.Token.Storage_Consigment_Inbound:

                List<String> codeList = TaskUtil.getRecordCodes(bus.getRecordList());
                String codes = TaskUtil.getSqlStrByList(codeList, EpcStock.class, "code");
                codeStr = codes;
                // 未入库的
                List<EpcStock> list = this.getInStock(codes, null);
                List<String> copyList = TaskUtil.getDifferEpcStockCodes(list, codeList);
                if (CommonUtil.isNotBlank(list)) {
                    msgBox = new MessageBox(false, "存在不能入库的唯一码!" + JSON.toJSON(copyList));
                }
                break;
            case Constant.Token.Storage_Repository_Adjust:
                List<String> rmcodes = TaskUtil.getRecordCodes(bus.getRecordList());
                String rmcode = TaskUtil.getSqlStrByList(rmcodes, EpcStock.class, "code");
                codeStr = rmcode;
                // 未入库的
                List<EpcStock> rmlist = this.getInStock(rmcode, null);
                List<String> copyLists = TaskUtil.getDifferEpcStockCodes(rmlist, rmcodes);
                if (CommonUtil.isBlank(rmlist)) {
                    msgBox = new MessageBox(false, "存在不能调整的唯一码!" + JSON.toJSON(copyLists));
                }
                break;

            case Constant.Token.Storage_Adjust_Outbound:
            case Constant.Token.Storage_Outbound:
            case Constant.Token.Storage_Transfer_Outbound:
            case Constant.Token.Shop_Adjust_Outbound:
            case Constant.Token.Shop_Refund_Outbound:
            case Constant.Token.Shop_Sales:
            case Constant.Token.Shop_Transfer_Outbound:
            case Constant.Token.Storage_Outbound_agent:
            case Constant.Token.Storage_refoundOut_customer:
            case Constant.Token.Storage_Refund_Outbound:
                List<String> ocodeList = TaskUtil.getRecordCodes(bus.getRecordList());
                String ocodes = TaskUtil.getSqlStrByList(ocodeList, EpcStock.class, "code");
                codeStr = ocodes;
                String ostorageId = bus.getOrigId();
                // 未入库的
                List<EpcStock> olist = this.getInStock(ocodes, ostorageId);
                List<String> ocopyList = TaskUtil.getDifferEpcStockCodes(olist, ocodeList);
                if (ocodeList.size() != 0) {
                    msgBox = new MessageBox(false, "存在不能出库的唯一码!" + JSON.toJSON(ocodeList));
                }
                break;
        }
        if (CommonUtil.isNotBlank(codeStr)) {
            List<EpcStock> stockList = this.epcStockService.findEpcByCodes(codeStr);
            for (EpcStock s : stockList) {
                Record r = recordMap.get(s.getCode());
                if (CommonUtil.isNotBlank(s.getInSotreType())) {
                    r.setExtField(s.getInSotreType());
                }
            }
            bus.setRecordList(new ArrayList<>(recordMap.values()));
        }
        return msgBox;
    }

    private List<EpcStock> getInStock(String codes, String warehouse1Id,
                                      String warehouse2Id) {
        String hql = "from EpcStock epcstock where epcstock.warehouse2Id=? and epcstock.inStock=0 and ("
                + codes + ")";
        return this.taskDao.find(hql, new Object[]{warehouse2Id});
    }

    private List<EpcStock> getInStock(String codes, String origId) {

        String hql = "from EpcStock epcstock where  epcstock.inStock=1 and ("
                + codes + ")";
        if(CacheManager.getCheckWarehhouse()) {
            if (CommonUtil.isNotBlank(origId)) {
                hql += " and epcstock.warehouseId='" + origId + "'";
            }
        }
        return this.taskDao.find(hql, new Object[]{});
    }


    @Override
    public MessageBox checkEpcStock(String uniqueCodeList, int token, String deviceId) {
        String storageId = "";
        switch (token) {
            case Constant.Token.Storage_Inbound:// = 8;
            case Constant.Token.Storage_Transfer_Inbound:// = 25;

            case Constant.Token.Storage_Refund_Inbound:// = 23;//代理商或门店退给总部

                storageId = CacheManager.getDeviceByCode(deviceId)
                        .getStorageId();
                List<String> incodeList_8 = new ArrayList<String>(Arrays
                        .asList(uniqueCodeList.split(",")));
                String codes_8 = TaskUtil.getSqlStrByList(incodeList_8,
                        EpcStock.class, "code");
                List<EpcStock> list_8 = null;
                list_8 = this.checkInStock(codes_8);// 在库（有记录）
                List<String> ocopyList_8 = TaskUtil.getDifferEpcStockCodes(list_8,
                        incodeList_8);
                if (CommonUtil.isNotBlank(ocopyList_8)) {
                    return new MessageBox(false, "存在不能入库的唯一码", JSON.toJSON(ocopyList_8));
                }
                break;
            //	case Constant.Token.Storage_Transfer_Inbound:// = 25;
            case Constant.Token.Storage_Adjust_Inbound:// = 29;
                storageId = CacheManager.getDeviceByCode(deviceId)
                        .getOwnerId();
            case Constant.Token.Shop_Adjust_Inbound:// = 31;
            case Constant.Token.Shop_Transfer_Inbound:// = 19;
            case Constant.Token.Shop_Sales_refund:// = 17;
                List<String> incodeList = new ArrayList<String>(Arrays.asList(uniqueCodeList.split(",")));
                String codes = TaskUtil.getSqlStrByList(incodeList, EpcStock.class,
                        "code");
                if (CommonUtil.isBlank(storageId)) {
                    storageId = CacheManager.getDeviceByCode(deviceId)
                            .getStorageId();
                }
                // 未入库的
                List<EpcStock> list = this.getInStock(codes, "",
                        storageId);
                TaskUtil.getDifferEpcStockCodes(list, incodeList);
                if (CommonUtil.isNotBlank(incodeList)) {
                    return new MessageBox(false, "存在不能装箱的唯一码!", JSON.toJSON(incodeList));
                }

                break;
            case Constant.Token.Storage_Refund_Outbound:// = 26;//退货给供应商
            case Constant.Token.Storage_Transfer_Outbound:// = 24;
            case Constant.Token.Storage_Other_Outbound:
            case Constant.Token.Storage_Outbound:// = 10;
            case Constant.Token.Storage_Adjust_Outbound:// = 28;
                storageId = CacheManager.getDeviceByCode(deviceId).getOwnerId();
            case Constant.Token.Shop_TransferSale_Outbound:
            /*storageId=bus.getDestUnitId();*/
            case Constant.Token.Shop_Sales:// = 15;
            case Constant.Token.Shop_Other_Outbound:
            case Constant.Token.Shop_Refund_Outbound:// = 27;
            case Constant.Token.Shop_Adjust_Outbound:// = 30;
            case Constant.Token.Shop_Transfer_Outbound:// = 18;
                if (CommonUtil.isBlank(storageId)) {
                    storageId = CacheManager.getDeviceByCode(deviceId)
                            .getStorageId();
                }
                // 在库的
                List<String> outcodeList = new ArrayList<String>(Arrays.asList(uniqueCodeList.split(",")));
                String outcodes = TaskUtil.getSqlStrByList(outcodeList,
                        EpcStock.class, "code");
                List<EpcStock> outList = this.getInStock(outcodes,
                        storageId);// 在库（有记录）
                TaskUtil.getDifferEpcStockCodes(outList, outcodeList);// 在库的唯一码
                if (outcodeList.size() != 0) {
                    return new MessageBox(false, "存在不能装箱的唯一码!", JSON.toJSON(outcodeList));
                }
                break;
        }
        return new MessageBox(true, "检测成功！");
    }


    private List<EpcStock> checkInStock(String codes) {
        String hql = "from EpcStock epcstock where  epcstock.inStock=1 and ("
                + codes + ")";
        return this.taskDao.find(hql, new Object[]{});
    }

    private List<EpcStock> getInStockWS(String codes) {
        String hql = "from EpcStock epcstock where epcstock.inStock=1 and ("
                + codes + ")";
        return this.taskDao.find(hql, new Object[]{});
    }

    @Override
    public List<Record> findRecordByTask(String taskId) {
        if (taskId.split("-").length > 1) {
            Integer token = Integer.parseInt(taskId.split("-")[1]);
            String incodeStr = "";
            List<String> inCodeList = this.taskDao.find("select distinct(r.code) from Record r ,Business bus  where r.taskId=bus.id and bus.type=1 and bus.billNo=?"
                    ,taskId.split("-")[0]);
            for(String code : inCodeList){
                incodeStr += ","+code;
            }
            if(CommonUtil.isNotBlank(incodeStr)){
                incodeStr = incodeStr.substring(1);
            }
            switch (token) {
                case Constant.Token.Storage_Inbound:
                     List<Epc> epcList = this.taskDao.find("select e from Epc e ,Init i where e.billNo=i.billNo and i.fileName like '%"+taskId.split("-")[0]+"%'");
                     List<Record> inRecordList = new ArrayList<>();
                     for(Epc e : epcList){
                         if(incodeStr.indexOf(e.getCode()) == -1){
                             Record r = new Record();
                             r.setCode(e.getCode());
                             r.setStyleId(e.getStyleId());
                             r.setColorId(e.getColorId());
                             r.setSizeId(e.getSizeId());
                             r.setSku(e.getSku());
                             inRecordList.add(r);
                         }
                     }
                     return inRecordList;
                case Constant.Token.Storage_Adjust_Inbound:
                case Constant.Token.Storage_Refund_Inbound:
                case Constant.Token.Shop_Adjust_Inbound:
                case Constant.Token.Shop_Transfer_Inbound:
                case Constant.Token.Shop_Inbound:
                case Constant.Token.Shop_Sales_refund:
                case Constant.Token.Storage_Inbound_agent_refund:
                case Constant.Token.Storage_Transfer_Inbound:
                case Constant.Token.Storage_Inbound_customer:

                    List<Record> records = this.taskDao.find( "select r from Record r ,Business bus  where r.taskId=bus.id and bus.type=0 and bus.billNo=?"
                            , new Object[]{taskId.split("-")[0]});
                    List<Record> resultList = new ArrayList<>();
                    for(Record r: records){
                        if(incodeStr.indexOf(r.getCode())==-1){
                            resultList.add(r);
                        }
                    }
                    return resultList;
                case Constant.Token.Storage_Consigment_Inbound:
                    List<BillRecord> billRecordList = this.taskDao.find("from BillRecord  where billNo = ?", new Object[]{taskId.split("-")[0]});
                    List<Record> recordList = new ArrayList<>();
                    for (BillRecord billRecord : billRecordList) {
                        if(incodeStr.indexOf(billRecord.getCode())==-1){
                            Record record = new Record();
                            record.setCode(billRecord.getCode());
                            recordList.add(record);
                        }
                    }
                    return recordList;

            }
        } else {
            if (CommonUtil.isNotBlank(stockBill)) {
                return stockBill.getRecords();
            }
            return this.taskDao.find("from Record r where taskId=?", new Object[]{taskId});
        }
        return null;
    }

    @Override
    public MessageBox updateUnitInfo(Business bus) {
        return new MessageBox(false, "");
    }


    public TaskDao getTaskDao() {
        return taskDao;
    }

    public void setTaskDao(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    public PurchaseOrderBillService getPurchaseOrderBillService() {
        return purchaseOrderBillService;
    }

    public void setPurchaseOrderBillService(PurchaseOrderBillService purchaseOrderBillService) {
        this.purchaseOrderBillService = purchaseOrderBillService;
    }

    public SaleOrderBillService getSaleOrderBillService() {
        return saleOrderBillService;
    }

    public void setSaleOrderBillService(SaleOrderBillService saleOrderBillService) {
        this.saleOrderBillService = saleOrderBillService;
    }
    //记录商品原库位
    private void saveOldRm(String billNo,String sku,String code,String rmId,String warehouseId,String userId){
        UniqueCodeBill uniqueCodeBill = new UniqueCodeBill();
        uniqueCodeBill.setId(billNo+code);
        uniqueCodeBill.setBillNo(billNo);
        uniqueCodeBill.setSku(sku);
        uniqueCodeBill.setUniqueCode(code);
        uniqueCodeBill.setOldRm(rmId);
        uniqueCodeBill.setWarehouseId(warehouseId);
        uniqueCodeBill.setUserId(userId);

        try {
            uniqueCodeBillService.save(uniqueCodeBill);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private RepositoryManagementBillDtl covert(RepositoryManagementBillDtl repositoryManagementBillDtl,EpcStock epcStock){
        repositoryManagementBillDtl.setStyleId(epcStock.getStyleId());
        repositoryManagementBillDtl.setStyleName(epcStock.getStyleName());
        repositoryManagementBillDtl.setColorId(epcStock.getColorId());
        repositoryManagementBillDtl.setStyleName(epcStock.getColorName());
        repositoryManagementBillDtl.setSizeId(epcStock.getSizeId());
        repositoryManagementBillDtl.setSizeName(epcStock.getSizeName());
        repositoryManagementBillDtl.setSku(epcStock.getSku());
        repositoryManagementBillDtl.setPrice(epcStock.getPrice());
        repositoryManagementBillDtl.setOrackId(epcStock.getFloorRack());
        repositoryManagementBillDtl.setOlevelId(epcStock.getFloorArea());
        repositoryManagementBillDtl.setOallocationId(epcStock.getFloorAllocation());
        repositoryManagementBillDtl.setWarehouseId(epcStock.getWarehouseId());
        repositoryManagementBillDtl.setUniqueCodes(epcStock.getCode());
        return repositoryManagementBillDtl;
    }
}
