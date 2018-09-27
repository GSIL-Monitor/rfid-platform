package com.casesoft.dmc.controller.logistics;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.cache.SpringContextUtil;
import com.casesoft.dmc.controller.task.TaskUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.erp.BillDtl;
import com.casesoft.dmc.model.logistics.*;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.stock.CodeFirstTime;
import com.casesoft.dmc.model.stock.EpcStock;
import com.casesoft.dmc.model.stock.InventoryRecord;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.model.tag.Epc;
import com.casesoft.dmc.model.tag.Init;
import com.casesoft.dmc.model.tag.InitDtl;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.BusinessDtl;
import com.casesoft.dmc.model.task.Record;
import com.casesoft.dmc.service.logistics.PurchaseOrderBillService;
import com.casesoft.dmc.service.stock.EpcStockService;
import com.casesoft.dmc.service.tag.InitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by Alvin on 2017/6/17.
 */
public class BillConvertUtil {
    /**
     * 转换为采购订单单据（保存调用）
     */
    private static EpcStockService epcStockService = (EpcStockService) SpringContextUtil.getBean("epcStockService");
    private static PurchaseOrderBillService purchaseOrderBillService = (PurchaseOrderBillService) SpringContextUtil.getBean("purchaseOrderBillService");

    public static void covertToPurchaseBill(PurchaseOrderBill purchaseOrderBill, List<PurchaseOrderBillDtl> purchaseOrderBillDtlList, User curUser) {
        if (CommonUtil.isNotBlank(curUser)) {
            purchaseOrderBill.setOprId(curUser.getCode());
            purchaseOrderBill.setOwnerId(curUser.getOwnerId());
        }
        Long totQty = 0L;
        Long actQty = 0L;
        Long rcvQty = 0L;
        Double totPrice = 0D;
        Double actPrice = 0D;
        Double rcvVal = 0D;
        for (PurchaseOrderBillDtl dtl : purchaseOrderBillDtlList) {
            if (dtl.getArrival()==null){
                dtl.setArrival(0);
            }
            dtl.setId(new GuidCreator().toString());
            dtl.setBillId(purchaseOrderBill.getId());
            dtl.setBillNo(purchaseOrderBill.getBillNo());
            if(dtl.getActPrintQty()==null){
                dtl.setActPrintQty(0);
            }
            dtl.setActQty(dtl.getQty());
            if(dtl.getInQty()==null){
                dtl.setInQty(0);
            }
            dtl.setPrintQty(dtl.getQty().intValue()-dtl.getActPrintQty());
            totQty += dtl.getQty();
            actQty += dtl.getQty();
            totPrice += dtl.getPrice() * dtl.getQty();
            actPrice += dtl.getActPrice() * dtl.getQty();
            rcvQty += dtl.getInQty();
            rcvVal += dtl.getInQty() * dtl.getActPrice();
        }
        if (CommonUtil.isBlank(purchaseOrderBill.getOwnerId())) {
            purchaseOrderBill.setOwnerId("1");
        }
        if (CommonUtil.isBlank(purchaseOrderBill.getStatus())) {
            purchaseOrderBill.setStatus(BillConstant.BillStatus.Enter);
        }
        Unit ventory = CacheManager.getUnitByCode(purchaseOrderBill.getOrigUnitId());
        if (CommonUtil.isNotBlank(ventory)) {
            purchaseOrderBill.setOrigUnitName(ventory.getName());
        }
        Unit orderWarehouse = CacheManager.getUnitByCode(purchaseOrderBill.getOrderWarehouseId());
        if (CommonUtil.isNotBlank(orderWarehouse)) {
            purchaseOrderBill.setOrderWarehouseName(orderWarehouse.getName());
        }
        Unit dest = CacheManager.getUnitByCode(purchaseOrderBill.getDestId());
        purchaseOrderBill.setDestName(dest.getName());
        Unit destUnit = CacheManager.getUnitByCode(dest.getOwnerId());
        purchaseOrderBill.setDestUnitId(destUnit.getId());
        purchaseOrderBill.setDestUnitName(destUnit.getName());
        purchaseOrderBill.setTotQty(totQty);
        purchaseOrderBill.setTotPrice(totPrice);
        purchaseOrderBill.setActQty(actQty);
        purchaseOrderBill.setActPrice(actPrice);
        purchaseOrderBill.setTotInQty(rcvQty);
        purchaseOrderBill.setTotInVal(rcvVal);

    }

    /**
     * 转换申请单为采购订单单据（保存调用）
     */
    public static void replenishBillcovertToPurchaseBill(ReplenishBill replenishBill, List<ReplenishBillDtl> dels, List<PurchaseOrderBill> savelist, List<PurchaseOrderBillDtl> saveDelList, User curUser, List<ChangeReplenishBillDtl> saveChangeList) {
        //根据商家replenishBillDel的sku做分组
        Map<String, List<ReplenishBillDtl>> map = new HashMap<String, List<ReplenishBillDtl>>();//根据商家分组保存
        for (int i = 0; i < dels.size(); i++) {
            if (CommonUtil.isNotBlank(dels.get(i).getClass1())) {
                if (i == 0) {
                    List<ReplenishBillDtl> list = new ArrayList<ReplenishBillDtl>();
                    list.add(dels.get(i));
                    map.put(dels.get(i).getClass1(), list);
                } else {
                    if (map.containsKey(dels.get(i).getClass1())) {
                        List<ReplenishBillDtl> replenishBillDtls = map.get(dels.get(i).getClass1());
                        replenishBillDtls.add(dels.get(i));
                        map.put(dels.get(i).getClass1(), replenishBillDtls);
                    } else {
                        List<ReplenishBillDtl> list = new ArrayList<ReplenishBillDtl>();
                        list.add(dels.get(i));
                        map.put(dels.get(i).getClass1(), list);
                    }
                }
            } else {
                if (map.containsKey("null")) {
                    List<ReplenishBillDtl> replenishBillDtls = map.get("null");
                    replenishBillDtls.add(dels.get(i));
                    map.put("null", replenishBillDtls);
                } else {
                    List<ReplenishBillDtl> list = new ArrayList<ReplenishBillDtl>();
                    list.add(dels.get(i));
                    map.put("null", list);
                }
            }

        }
        //循环保存数据
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String key = (String) entry.getKey();
            List<ReplenishBillDtl> keyDtls = (List<ReplenishBillDtl>) entry.getValue();
            PurchaseOrderBill purchaseOrderBill = new PurchaseOrderBill();
            String prefix = BillConstant.BillPrefix.purchase
                    + CommonUtil.getDateString(new Date(), "yyMMddHHmmssSSS");
            purchaseOrderBill.setId(prefix);
            purchaseOrderBill.setBillNo(prefix);
            if (CommonUtil.isNotBlank(curUser)) {
                purchaseOrderBill.setOprId(curUser.getCode());
            }
            Long totQty = 0L;
            Long actQty = 0L;
            Long rcvQty = 0L;
            Double totPrice = 0D;
            Double actPrice = 0D;
            Double rcvVal = 0D;
            for (ReplenishBillDtl dtl : keyDtls) {
                PurchaseOrderBillDtl purchaseOrderBillDtl = new PurchaseOrderBillDtl();
                purchaseOrderBillDtl.setId(new GuidCreator().toString());
                purchaseOrderBillDtl.setBillId(purchaseOrderBill.getId());
                purchaseOrderBillDtl.setBillNo(purchaseOrderBill.getBillNo());
                /*purchaseOrderBillDtl.setActPrintQty(0);
                purchaseOrderBillDtl.setActQty(Long.parseLong(dtl.getConvertQty()+""));
                purchaseOrderBillDtl.setInQty(0);
                purchaseOrderBillDtl.setPrintQty(dtl.getConvertQty());*/
                purchaseOrderBillDtl.setInStockType("BH");
                purchaseOrderBillDtl.setStyleId(dtl.getStyleId());
                purchaseOrderBillDtl.setSku(dtl.getSku());
                purchaseOrderBillDtl.setSizeId(dtl.getSizeId());
                purchaseOrderBillDtl.setColorId(dtl.getColorId());
                purchaseOrderBillDtl.setQty(Long.parseLong(dtl.getConvertQty() + ""));
                purchaseOrderBillDtl.setActPrintQty(0);
                purchaseOrderBillDtl.setPrintQty(dtl.getConvertQty());
                purchaseOrderBillDtl.setInQty(0);
                purchaseOrderBillDtl.setPrice(dtl.getPrice());
                purchaseOrderBillDtl.setTotPrice(dtl.getPrice() * dtl.getConvertQty());
                purchaseOrderBillDtl.setActPrice(dtl.getActPrice());
                purchaseOrderBillDtl.setTotActPrice(dtl.getActPrice() * dtl.getConvertQty());
                purchaseOrderBillDtl.setStatus(0);
                purchaseOrderBillDtl.setInStatus(0);
                saveDelList.add(purchaseOrderBillDtl);
                totQty += dtl.getConvertQty();
                actQty += dtl.getConvertQty();
                totPrice += dtl.getPrice() * dtl.getConvertQty();
                actPrice += dtl.getActPrice() * dtl.getConvertQty();
                rcvQty += dtl.getConvertQty();
                rcvVal += dtl.getConvertQty() * dtl.getActPrice();
                //添加操作详情
                ChangeReplenishBillDtl changeReplenishBillDtl = new ChangeReplenishBillDtl();
                changeReplenishBillDtl.setId(new GuidCreator().toString());
                changeReplenishBillDtl.setReplenishNo(replenishBill.getBillNo());
                changeReplenishBillDtl.setSku(purchaseOrderBillDtl.getSku());
                changeReplenishBillDtl.setPurchaseNo(purchaseOrderBill.getBillNo());
                changeReplenishBillDtl.setBillDate(new Date());
                changeReplenishBillDtl.setQty(purchaseOrderBillDtl.getQty() + "");
                //添加预计时间

                saveChangeList.add(changeReplenishBillDtl);
            }
            if (CommonUtil.isNotBlank(key) && !key.equals("null")) {
                purchaseOrderBill.setOrigUnitId(key);
            }


            purchaseOrderBill.setBuyahandId(replenishBill.getBuyahandId());
            purchaseOrderBill.setBillDate(new Date());
            purchaseOrderBill.setDestId("AUTO_WH001");
            if (CommonUtil.isBlank(purchaseOrderBill.getOwnerId())) {
                purchaseOrderBill.setOwnerId("1");
            }
            if (CommonUtil.isBlank(purchaseOrderBill.getStatus())) {
                purchaseOrderBill.setStatus(BillConstant.BillStatus.Enter);
            }
            Unit ventory = CacheManager.getUnitByCode(replenishBill.getOrigUnitId());
            if (CommonUtil.isNotBlank(ventory)) {
                purchaseOrderBill.setOrigUnitName(ventory.getName());
            }

            Unit dest = CacheManager.getUnitByCode(purchaseOrderBill.getDestId());
            purchaseOrderBill.setDestName(dest.getName());
            Unit destUnit = CacheManager.getUnitByCode(dest.getOwnerId());
            purchaseOrderBill.setDestUnitId(destUnit.getId());
            purchaseOrderBill.setDestUnitName(destUnit.getName());
            purchaseOrderBill.setTotQty(totQty);
            purchaseOrderBill.setTotPrice(totPrice);
            purchaseOrderBill.setActQty(actQty);
            purchaseOrderBill.setActPrice(actPrice);
            purchaseOrderBill.setTotInQty(rcvQty);
            purchaseOrderBill.setTotInVal(rcvVal);
            purchaseOrderBill.setSrcBillNo(replenishBill.getBillNo());
            savelist.add(purchaseOrderBill);

        }

    }

    /**
     * 转换申请单为采购退货订单单据（保存调用）
     */
    public static void replenishBillcovertToPurchaseReturnBill(ReplenishBill replenishBill, List<ReplenishBillDtl> dels, List<PurchaseReturnBill> savelist, List<PurchaseReturnBillDtl> saveDelList, User curUser, List<ChangeReplenishBillDtl> saveChangeList) {
        //根据商家replenishBillDel的sku做分组
        Map<String, List<ReplenishBillDtl>> map = new HashMap<String, List<ReplenishBillDtl>>();//根据商家分组保存
        for (int i = 0; i < dels.size(); i++) {
            if (CommonUtil.isNotBlank(dels.get(i).getClass1())) {
                if (i == 0) {
                    List<ReplenishBillDtl> list = new ArrayList<ReplenishBillDtl>();
                    list.add(dels.get(i));
                    map.put(dels.get(i).getClass1(), list);
                } else {
                    if (map.containsKey(dels.get(i).getClass1())) {
                        List<ReplenishBillDtl> replenishBillDtls = map.get(dels.get(i).getClass1());
                        replenishBillDtls.add(dels.get(i));
                        map.put(dels.get(i).getClass1(), replenishBillDtls);
                    } else {
                        List<ReplenishBillDtl> list = new ArrayList<ReplenishBillDtl>();
                        list.add(dels.get(i));
                        map.put(dels.get(i).getClass1(), list);
                    }
                }
            } else {
                if (map.containsKey("null")) {
                    List<ReplenishBillDtl> replenishBillDtls = map.get("null");
                    replenishBillDtls.add(dels.get(i));
                    map.put("null", replenishBillDtls);
                } else {
                    List<ReplenishBillDtl> list = new ArrayList<ReplenishBillDtl>();
                    list.add(dels.get(i));
                    map.put("null", list);
                }
            }

        }
        //循环保存数据
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String key = (String) entry.getKey();
            List<ReplenishBillDtl> keyDtls = (List<ReplenishBillDtl>) entry.getValue();
            PurchaseReturnBill purchaseReturnBill = new PurchaseReturnBill();
            String prefix = BillConstant.BillPrefix.purchaseReturn
                    + CommonUtil.getDateString(new Date(), "yyMMddHHmmssSSS");
            purchaseReturnBill.setId(prefix);
            purchaseReturnBill.setBillNo(prefix);
            if (CommonUtil.isNotBlank(curUser)) {
                purchaseReturnBill.setOprId(curUser.getCode());
            }
            Long totQty = 0L;
            Long actQty = 0L;
            Long rcvQty = 0L;
            Double totPrice = 0D;
            Double actPrice = 0D;
            Double rcvVal = 0D;
            for (ReplenishBillDtl dtl : keyDtls) {
                PurchaseReturnBillDtl purchaseReturnBillDtl = new PurchaseReturnBillDtl();
                purchaseReturnBillDtl.setId(new GuidCreator().toString());
                purchaseReturnBillDtl.setBillId(purchaseReturnBill.getId());
                purchaseReturnBillDtl.setBillNo(purchaseReturnBill.getBillNo());
                /*purchaseOrderBillDtl.setActPrintQty(0);
                purchaseOrderBillDtl.setActQty(Long.parseLong(dtl.getConvertQty()+""));
                purchaseOrderBillDtl.setInQty(0);
                purchaseOrderBillDtl.setPrintQty(dtl.getConvertQty());*/
                /*purchaseReturnBillDtl.getOutStockType("BH");*/
                purchaseReturnBillDtl.setSku(dtl.getSku());
                purchaseReturnBillDtl.setStyleId(dtl.getStyleId());
                purchaseReturnBillDtl.setSizeId(dtl.getSizeId());
                purchaseReturnBillDtl.setColorId(dtl.getColorId());
                purchaseReturnBillDtl.setQty(Long.parseLong(dtl.getConvertQty() + ""));

                purchaseReturnBillDtl.setInQty(0L);
                purchaseReturnBillDtl.setPrice(dtl.getPrice());
                purchaseReturnBillDtl.setTotPrice(dtl.getPrice() * dtl.getConvertQty());
                purchaseReturnBillDtl.setActPrice(dtl.getActPrice());
                purchaseReturnBillDtl.setTotActPrice(dtl.getActPrice() * dtl.getConvertQty());
                purchaseReturnBillDtl.setStatus(0);
                purchaseReturnBillDtl.setInStatus(0);
                saveDelList.add(purchaseReturnBillDtl);
                totQty += dtl.getConvertQty();
                actQty += dtl.getConvertQty();
                totPrice += dtl.getPrice() * dtl.getConvertQty();
                actPrice += dtl.getActPrice() * dtl.getConvertQty();
                rcvQty += dtl.getConvertQty();
                rcvVal += dtl.getConvertQty() * dtl.getActPrice();
                //添加操作详情
                ChangeReplenishBillDtl changeReplenishBillDtl = new ChangeReplenishBillDtl();
                changeReplenishBillDtl.setId(new GuidCreator().toString());
                changeReplenishBillDtl.setReplenishNo(replenishBill.getBillNo());
                changeReplenishBillDtl.setSku(purchaseReturnBillDtl.getSku());
                changeReplenishBillDtl.setPurchaseNo(purchaseReturnBill.getBillNo());
                changeReplenishBillDtl.setBillDate(new Date());
                changeReplenishBillDtl.setQty(purchaseReturnBillDtl.getQty() + "");
                //添加预计时间

                saveChangeList.add(changeReplenishBillDtl);
            }
            if (CommonUtil.isNotBlank(key) && !key.equals("null")) {
                purchaseReturnBill.setOrigUnitId(key);
            }
            /*  purchaseReturnBill.setBuyahandId(replenishBill.getBuyahandId());*/
            purchaseReturnBill.setBillDate(new Date());
            purchaseReturnBill.setDestId("AUTO_WH001");
            if (CommonUtil.isBlank(purchaseReturnBill.getOwnerId())) {
                purchaseReturnBill.setOwnerId("1");
            }
            if (CommonUtil.isBlank(purchaseReturnBill.getStatus())) {
                purchaseReturnBill.setStatus(BillConstant.BillStatus.Enter);
            }
            Unit ventory = CacheManager.getUnitByCode(replenishBill.getOrigUnitId());
            if (CommonUtil.isNotBlank(ventory)) {
                purchaseReturnBill.setOrigUnitName(ventory.getName());
            }

            Unit dest = CacheManager.getUnitByCode(purchaseReturnBill.getDestId());
            purchaseReturnBill.setDestName(dest.getName());
            Unit destUnit = CacheManager.getUnitByCode(dest.getOwnerId());
            purchaseReturnBill.setDestUnitId(destUnit.getId());
            purchaseReturnBill.setDestUnitName(destUnit.getName());
            purchaseReturnBill.setTotQty(totQty);
            purchaseReturnBill.setTotPrice(totPrice);
            purchaseReturnBill.setActQty(actQty);
            purchaseReturnBill.setActPrice(actPrice);
            purchaseReturnBill.setTotInQty(rcvQty);
            purchaseReturnBill.setTotInVal(rcvVal);
            purchaseReturnBill.setSrcBillNo(replenishBill.getBillNo());
            savelist.add(purchaseReturnBill);

        }

    }

    /**
     * modify by yushen
     * 微信小程序补货处理，将补货单转为采购单据
     */
    public static void covertToPurchaseWeChatBill(PurchaseOrderBill purchaseOrderBill, List<PurchaseOrderBillDtl> purchaseOrderBillDtlList, User curUser) {
        if (CommonUtil.isNotBlank(curUser)) {
            purchaseOrderBill.setOprId(curUser.getCode());
        }
        Long totQty = 0L;
        Long actQty = 0L;
        Long rcvQty = 0L;
        Double totPrice = 0D;
        Double actPrice = 0D;
        Double rcvVal = 0D;
        for (PurchaseOrderBillDtl dtl : purchaseOrderBillDtlList) {
            dtl.setId(new GuidCreator().toString());
            dtl.setBillId(purchaseOrderBill.getId());
            dtl.setBillNo(purchaseOrderBill.getBillNo());
            dtl.setActPrintQty(0);
            dtl.setActQty(dtl.getQty());
            dtl.setInQty(0);
            dtl.setPrintQty(dtl.getQty().intValue());
            Style style = CacheManager.getStyleById(dtl.getStyleId());
            dtl.setPrice(style.getPreCast());
            dtl.setActPrice(style.getPreCast());
            dtl.setTotActPrice(style.getPreCast() * Double.parseDouble(dtl.getQty() + ""));
            dtl.setTotPrice(style.getPreCast() * Double.parseDouble(dtl.getQty() + ""));
            totQty += dtl.getQty();
            actQty += dtl.getQty();
            totPrice += dtl.getPrice() * dtl.getQty();
            actPrice += dtl.getActPrice() * dtl.getQty();
            rcvQty += dtl.getInQty();
            rcvVal += dtl.getInQty() * dtl.getActPrice();
        }
        if (CommonUtil.isBlank(purchaseOrderBill.getOwnerId())) {
            purchaseOrderBill.setOwnerId("1");
        }
        if (CommonUtil.isBlank(purchaseOrderBill.getStatus())) {
            purchaseOrderBill.setStatus(BillConstant.BillStatus.Enter);
        }
        Unit ventory = CacheManager.getUnitByCode(purchaseOrderBill.getOrigUnitId());
        if (CommonUtil.isNotBlank(ventory)) {
            purchaseOrderBill.setOrigUnitName(ventory.getName());
        }

        Unit dest = CacheManager.getUnitByCode(purchaseOrderBill.getDestId());
        purchaseOrderBill.setDestName(dest.getName());
        Unit destUnit = CacheManager.getUnitByCode(dest.getOwnerId());
        purchaseOrderBill.setDestUnitId(destUnit.getId());
        purchaseOrderBill.setDestUnitName(destUnit.getName());
        purchaseOrderBill.setTotQty(totQty);
        purchaseOrderBill.setTotPrice(totPrice);
        purchaseOrderBill.setActQty(actQty);
        purchaseOrderBill.setActPrice(actPrice);
        purchaseOrderBill.setTotInQty(rcvQty);
        purchaseOrderBill.setTotInVal(rcvVal);


    }

    /**
     * 转换为采购退货申请单据（保存调用）
     */
    public static void convertToPurchaseReturnBill(PurchaseReturnBill purchaseReturnBill, List<PurchaseReturnBillDtl> purchaseReturnBillDtlList, User curUser) {
        if (CommonUtil.isNotBlank(curUser)) {
            purchaseReturnBill.setOprId(curUser.getCode());
        }
        Long totQty = 0L;
        Long outQty = 0L;
        Double totPrice = 0D;
        Double actPrice = 0D;
        Double outVal = 0D;
        List<BillRecord> billRecordList = new ArrayList<>();
        for (PurchaseReturnBillDtl dtl : purchaseReturnBillDtlList) {
            dtl.setId(new GuidCreator().toString());
            dtl.setBillId(purchaseReturnBill.getId());
            dtl.setBillNo(purchaseReturnBill.getBillNo());
            dtl.setTotPrice(-1 * Math.abs(dtl.getPrice() * dtl.getQty()));
            dtl.setTotActPrice(-1 * Math.abs(dtl.getActPrice() * dtl.getQty()));
            totQty += dtl.getQty();
            totPrice += -1 * Math.abs(dtl.getPrice() * dtl.getQty());
            actPrice += -1 * Math.abs(dtl.getActPrice() * dtl.getQty());
            outQty += dtl.getOutQty();
            outVal += dtl.getOutQty() * dtl.getActPrice();
            if (CommonUtil.isNotBlank(dtl.getUniqueCodes())) {
                for (String code : dtl.getUniqueCodes().split(",")) {
                    if(CommonUtil.isNotBlank(code)) {
                        BillRecord billRecord = new BillRecord(dtl.getBillNo() + "-" + code, code, dtl.getBillNo(), dtl.getSku());
                        billRecordList.add(billRecord);
                    }
                }
            }
        }
        purchaseReturnBill.setBillRecordList(billRecordList);
        if (CommonUtil.isBlank(purchaseReturnBill.getOwnerId())) {
            purchaseReturnBill.setOwnerId(curUser.getOwnerId());
        }
        if (CommonUtil.isBlank(purchaseReturnBill.getStatus())) {
            purchaseReturnBill.setStatus(BillConstant.BillStatus.Enter);
        }
        Unit origUnit = CacheManager.getUnitByCode(CacheManager.getUnitByCode(purchaseReturnBill.getOrigId()).getOwnerId());
        purchaseReturnBill.setOrigUnitId(origUnit.getId());
        purchaseReturnBill.setOrigUnitName(origUnit.getName());
        Unit destUnit = CacheManager.getUnitByCode(purchaseReturnBill.getDestUnitId());
        purchaseReturnBill.setDestName(destUnit.getName());
        purchaseReturnBill.setActQty(totQty);
        purchaseReturnBill.setTotQty(totQty);
        purchaseReturnBill.setActPrice(actPrice);
        purchaseReturnBill.setTotPrice(totPrice);
        purchaseReturnBill.setTotOutQty(outQty);
        purchaseReturnBill.setTotOutVal(outVal);
    }

    /**
     * 采购单转标签初始化
     */
    public static Init covertToTagBirth(String taskId, List<PurchaseOrderBillDtl> purchaseOrderBillDtlList, InitService epcService, User currentUser) {
        Init master = new Init();
        master.setBillNo(taskId);
        Long totQty = 0L;
        List<InitDtl> initDtlList = new ArrayList<>();
        for (PurchaseOrderBillDtl dtl : purchaseOrderBillDtlList) {
            if (dtl.getArrival() > 0 && dtl.getQty() > dtl.getActPrintQty()) {
                if (dtl.getArrival() == (dtl.getQty().intValue() - dtl.getActPrintQty().intValue())) {
                    dtl.setPrintStatus(BillConstant.PrintStatus.Print);
                } else {
                    dtl.setPrintStatus(BillConstant.PrintStatus.Printting);
                }
                //提取到货数，操作完成之后将本次到货数设为0
                Integer arrival = dtl.getArrival();
                dtl.setArrival(0);
                dtl.setActPrintQty(dtl.getActPrintQty()+arrival);
                InitDtl detail = new InitDtl();
                detail.setId(taskId + "-" + dtl.getSku());
                detail.setStyleId(dtl.getStyleId());
                detail.setColorId(dtl.getColorId());
                detail.setSizeId(dtl.getSizeId());
                detail.setSku(dtl.getSku());
                detail.setStartNum(epcService.findMaxNoBySkuNo(dtl.getSku()) + 1);
                detail.setEndNum(epcService.findMaxNoBySkuNo(dtl.getSku())
                        + arrival);
                detail.setQty(arrival);
                detail.setOwnerId(currentUser.getOwnerId());
                detail.setStatus(1);
                totQty += arrival;
                detail.setBillNo(taskId);
                initDtlList.add(detail);

            }
            dtl.setPrintQty((int) (dtl.getQty() - dtl.getActPrintQty()));
        }
        master.setTotEpc(totQty);
        master.setDtlList(initDtlList);
        master.setOwnerId(currentUser.getOwnerId());
        master.setHostId(currentUser.getId());
        master.setTotSku(initDtlList.size());
        master.setBillDate(new Date());
        master.setFileName("采购单" + purchaseOrderBillDtlList.get(0).getBillNo());
        master.setStatus(1);
        master.setRemark(purchaseOrderBillDtlList.get(0).getBillNo());
        return master;
    }

    /**
     * 标签替代转标签初始化
     */
    public static Init labelcovertToTagBirth(String taskId, List<LabelChangeBillDel> labelChangeBillDels, InitService epcService, User currentUser, String prefix, String newStylesuffix,String changeType) {
        Init master = new Init();
        master.setBillNo(taskId);
        Long totQty = 0L;
        List<InitDtl> initDtlList = new ArrayList<>();
        org.slf4j.Logger  logger = LoggerFactory.getLogger(LabelChangeBill.class);
        boolean isUseOldStyle=false;
        for (LabelChangeBillDel dtl : labelChangeBillDels) {
            InitDtl detail = new InitDtl();
            String styleId="";
            if(changeType.equals(BillConstant.ChangeType.Series)){
                styleId = dtl.getStyleId();
                int styleIdLength = styleId.length();
                String styleTail=styleId.substring(styleIdLength-2,styleIdLength);
                if(styleTail.equals(BillConstant.styleNew.Alice)||styleTail.equals(BillConstant.styleNew.AncientStone)){
                    styleId=styleId.substring(0,styleIdLength-2);
                    Style style= CacheManager.getStyleById(styleId);
                    if(CommonUtil.isBlank(style)){
                        logger.error(dtl.getBillNo()+":BillConvertUtil没有"+styleId);
                        isUseOldStyle=false;
                    }else{
                        logger.error(dtl.getBillNo()+":BillConvertUtil有"+styleId);
                        isUseOldStyle=true;
                    }
                }else{
                    logger.error(dtl.getBillNo()+":BillConvertUtil"+styleId+"后缀没有AA和AS");
                    isUseOldStyle=false;
                }
                String stylePDTail=styleId.substring(styleIdLength-4,styleIdLength-2);
                if(stylePDTail.equals(BillConstant.styleNew.PriceDiscount)){
                    styleId=styleId.substring(0,styleIdLength-4);
                }
                if(!isUseOldStyle){
                    detail.setId(taskId + "-" + styleId + newStylesuffix + dtl.getColorId() + dtl.getSizeId());
                    detail.setStyleId(styleId + newStylesuffix);
                    detail.setSku(styleId + newStylesuffix + dtl.getColorId() + dtl.getSizeId());
                }else{
                    detail.setId(taskId + "-" + styleId + dtl.getColorId() + dtl.getSizeId());
                    detail.setStyleId(styleId);
                    detail.setSku(styleId+ dtl.getColorId() + dtl.getSizeId());
                }

            }else if(changeType.equals(BillConstant.ChangeType.Shop)){
                styleId = dtl.getStyleId();
                int styleIdLength = styleId.length();
                String styleTail=styleId.substring(styleIdLength-2,styleIdLength);
                if(styleTail.equals(BillConstant.styleNew.Shop)){
                    styleId=styleId.substring(0,styleIdLength-2);
                    Style style= CacheManager.getStyleById(styleId);
                    if(CommonUtil.isBlank(style)){
                        logger.error(dtl.getBillNo()+":BillConvertUtil没有"+styleId);
                        isUseOldStyle=false;
                    }else{
                        logger.error(dtl.getBillNo()+":BillConvertUtil有"+styleId);
                        isUseOldStyle=true;
                    }
                }else{
                    logger.error(dtl.getBillNo()+":BillConvertUtil"+styleId+"后缀没有CS");
                    isUseOldStyle=false;
                }
                String stylePDTail=styleId.substring(styleIdLength-4,styleIdLength-2);
                if(stylePDTail.equals(BillConstant.styleNew.PriceDiscount)){
                    styleId=styleId.substring(0,styleIdLength-4);
                }
                if(!isUseOldStyle){
                    detail.setId(taskId + "-" + styleId + newStylesuffix + dtl.getColorId() + dtl.getSizeId());
                    detail.setStyleId(styleId + newStylesuffix);
                    detail.setSku(styleId + newStylesuffix + dtl.getColorId() + dtl.getSizeId());
                }else{
                    detail.setId(taskId + "-" + styleId + dtl.getColorId() + dtl.getSizeId());
                    detail.setStyleId(styleId);
                    detail.setSku(styleId+ dtl.getColorId() + dtl.getSizeId());
                }
            }else{
                styleId = dtl.getStyleId();
                detail.setId(taskId + "-" + styleId + newStylesuffix +CommonUtil.getInt(dtl.getDiscount())+ dtl.getColorId() + dtl.getSizeId());
                detail.setStyleId(styleId + newStylesuffix+CommonUtil.getInt(dtl.getDiscount()));
                detail.setSku(styleId + newStylesuffix + CommonUtil.getInt(dtl.getDiscount())+dtl.getColorId() + dtl.getSizeId());
            }
            Style style = CacheManager.getStyleById(styleId);
               /* detail.setStyleName(style.getStyleName());
                detail.setColorName(dtl.getColorId());
                detail.setSizeName(dtl.getSizeId());*/
            detail.setColorId(dtl.getColorId());
            detail.setSizeId(dtl.getSizeId());
            detail.setStartNum(epcService.findMaxNoBySkuNo(detail.getSku()) + 1);
            detail.setEndNum(epcService.findMaxNoBySkuNo(detail.getSku())
                    + dtl.getQty());
            detail.setQty(dtl.getQty());
            detail.setOwnerId("1");
            detail.setStatus(1);
            totQty += dtl.getQty();
            detail.setBillNo(taskId);
            initDtlList.add(detail);


        }
        master.setTotEpc(totQty);
        master.setDtlList(initDtlList);
        master.setOwnerId("1");
        master.setHostId(currentUser.getId());
        master.setTotSku(initDtlList.size());
        master.setBillDate(new Date());
        master.setFileName("标签转换单" + prefix);
        master.setStatus(1);
        master.setRemark(prefix);
        return master;
    }

    /**
     * 采购单转入库单（web页面调用）
     */
    public static Business covertToPurchaseBusiness(PurchaseOrderBill purchaseOrderBill, List<PurchaseOrderBillDtl> purchaseOrderBillDtlList, List<Epc> epcList, User currentUser) {
        Map<String, PurchaseOrderBillDtl> purchaseBillDtlMap = new HashMap<>();
        for (PurchaseOrderBillDtl dtl : purchaseOrderBillDtlList) {
            purchaseBillDtlMap.put(dtl.getSku(), dtl);
        }
        String taksId = "TSK" + CommonUtil.getDateString(new Date(), "yyyyMMdd") + System.currentTimeMillis();
        Business bus = new Business();
        Map<String, BusinessDtl> businessDtlMap = new HashMap<>();
        Map<String, String> styleCountMap = new HashMap<>();
        List<Record> recordList = new ArrayList<>();
        List<BillRecord> billRecordList = new ArrayList<>();
        Double totPreVal = 0D;
        Double totRcvPrice = 0d;
        for (Epc e : epcList) {
            String sku = e.getSku();
            styleCountMap.put(e.getStyleId(), e.getStyleId());
            if (purchaseBillDtlMap.containsKey(sku)) {
                PurchaseOrderBillDtl dtl = purchaseBillDtlMap.get(sku);

                dtl.setInQty(dtl.getInQty() + 1);
                dtl.setInVal(dtl.getInQty() * dtl.getActPrice());
                dtl.setInStatus(BillConstant.BillDtlStatus.Ining);
                if (dtl.getInQty().intValue() == dtl.getQty().intValue()) {
                    dtl.setStatus(BillConstant.BillDtlStatus.InStore);
                    dtl.setInStatus(BillConstant.BillDtlStatus.InStore);
                }
                totRcvPrice += dtl.getPrice();
                purchaseBillDtlMap.put(sku, dtl);
            }
            if (businessDtlMap.containsKey(e.getSku())) {
                BusinessDtl dtl = businessDtlMap.get(sku);
                PurchaseOrderBillDtl purchaseOrderBillDtl = purchaseBillDtlMap.get(sku);
                dtl.setQty(dtl.getQty() + 1);
                dtl.setPreVal(dtl.getQty() * purchaseOrderBillDtl.getActPrice());
                businessDtlMap.put(sku, dtl);
            } else {
                BusinessDtl dtl = new BusinessDtl(taksId, currentUser.getOwnerId(), Constant.Token.Storage_Inbound, "KE000001", sku, 1);
                PurchaseOrderBillDtl purchaseOrderBillDtl = purchaseBillDtlMap.get(sku);
                dtl.setId(new GuidCreator().toString());
                dtl.setStyleId(e.getStyleId());
                dtl.setColorId(e.getColorId());
                dtl.setSizeId(e.getSizeId());
                dtl.setPreVal(dtl.getQty() * purchaseOrderBillDtl.getActPrice());
                dtl.setType(Constant.TaskType.Inbound);
                dtl.setDestId(purchaseOrderBill.getDestId());
                dtl.setOrigUnitId(purchaseOrderBill.getOrigUnitId());
                businessDtlMap.put(sku, dtl);
            }


            Record record = new Record(e.getCode(), taksId, taksId, Constant.Token.Storage_Inbound, "KE000001", "");
            record.setOrigUnitId(purchaseOrderBill.getOrigUnitId());
            record.setOwnerId(currentUser.getOwnerId());
            record.setDestId(purchaseOrderBill.getDestId());
            record.setSku(sku);
            record.setStyleId(e.getStyleId());
            record.setColorId(e.getColorId());
            record.setSizeId(e.getSizeId());
            record.setPrice(purchaseBillDtlMap.get(record.getSku()).getActPrice());
            record.setScanTime(new Date());
            PurchaseOrderBillDtl dtl = purchaseBillDtlMap.get(record.getSku());
            record.setExtField(dtl.getInStockType());//record中增加入库类型
            record.setId(new GuidCreator().toString());
            record.setCageId(purchaseOrderBill.getCageId());
            record.setRackId(purchaseOrderBill.getRackId());
            record.setLevelId(purchaseOrderBill.getLevelId());
            record.setAllocationId(purchaseOrderBill.getAllocationId());
            record.setType(Constant.TaskType.Inbound);
            recordList.add(record);
            BillRecord billRecord = new BillRecord(purchaseOrderBill.getBillNo() + "-" + record.getCode(), record.getCode(), purchaseOrderBill.getBillNo(), record.getSku());
            billRecordList.add(billRecord);
        }
        bus.setDtlList(new ArrayList<>(businessDtlMap.values()));
        bus.setId(taksId);
        bus.setToken(Constant.Token.Storage_Inbound);
        bus.setBeginTime(new Date());
        bus.setBillId(purchaseOrderBill.getBillNo());
        bus.setBillNo(purchaseOrderBill.getBillNo());
        bus.setDestId(purchaseOrderBill.getDestId());
        bus.setEndTime(new Date());
        bus.setOrigUnitId(purchaseOrderBill.getOrigUnitId());
        bus.setOwnerId(currentUser.getOwnerId());
        bus.setDeviceId("KE000001");
        bus.setStatus(Constant.TaskStatus.Submitted);
        bus.setTotCarton(1L);
        bus.setTotEpc((long) epcList.size());
        bus.setTotPrice(totRcvPrice);
        bus.setTotSku((long) bus.getDtlList().size());
        bus.setTotStyle((long) styleCountMap.size());
        bus.setType(Constant.TaskType.Inbound);
        bus.setRecordList(recordList);

        purchaseOrderBill.setTotInQty(purchaseOrderBill.getTotInQty() + epcList.size());
        purchaseOrderBill.setTotInVal(purchaseOrderBill.getTotInVal() + totRcvPrice);
        purchaseOrderBill.setStatus(BillConstant.BillStatus.Doing);
        purchaseOrderBill.setInStatus(BillConstant.BillInOutStatus.Ining);
        if (purchaseOrderBill.getTotInQty().intValue() == purchaseOrderBill.getTotQty().intValue()) {
            purchaseOrderBill.setInStatus(BillConstant.BillInOutStatus.InStore);
            purchaseOrderBill.setStatus(BillConstant.BillStatus.End);
        }
        purchaseOrderBill.setBillRecordList(billRecordList);
        return bus;
    }

    /**
     * 采购退货管理出库(web扫描唯一码提交)
     *
     * @param purchaseReturnBill
     * @param purchaseOrderBillDtlList
     * @param epcList
     * @param currentUser
     * @return
     */
    public static Business covertToPurchaseBillBusiness(PurchaseReturnBill purchaseReturnBill, List<PurchaseReturnBillDtl> purchaseOrderBillDtlList, List<Epc> epcList, User currentUser) {
        Map<String, PurchaseReturnBillDtl> purchaseBillDtlMap = new HashMap<>();
        for (PurchaseReturnBillDtl dtl : purchaseOrderBillDtlList) {
            purchaseBillDtlMap.put(dtl.getSku(), dtl);
        }
        String taksId = "TSK" + CommonUtil.getDateString(new Date(), "yyyyMMdd") + System.currentTimeMillis();
        Business bus = new Business();
        Map<String, BusinessDtl> businessDtlMap = new HashMap<>();
        Map<String, String> styleCountMap = new HashMap<>();
        List<Record> recordList = new ArrayList<>();
        Double totRcvPrice = 0d;

        for (Epc e : epcList) {
            String sku = e.getSku();
            styleCountMap.put(e.getStyleId(), e.getStyleId());
            if (purchaseBillDtlMap.containsKey(sku)) {
                PurchaseReturnBillDtl dtl = purchaseBillDtlMap.get(sku);
                dtl.setOutQty(dtl.getOutQty() + 1);
                dtl.setOutVal(dtl.getOutQty() * dtl.getActPrice());
                dtl.setOutStatus(BillConstant.BillDtlStatus.Outing);
                if (dtl.getOutQty().intValue() == dtl.getQty().intValue()) {
                    dtl.setStatus(BillConstant.BillDtlStatus.OutStore);
                    dtl.setOutStatus(BillConstant.BillDtlStatus.OutStore);
                }
                totRcvPrice += dtl.getPrice();
                dtl.setStockVal(0D);
                purchaseBillDtlMap.put(sku, dtl);
            }
            if (businessDtlMap.containsKey(e.getSku())) {
                BusinessDtl dtl = businessDtlMap.get(sku);
                dtl.setQty(dtl.getQty() + 1);
                businessDtlMap.put(sku, dtl);
            } else {
                BusinessDtl dtl = new BusinessDtl(taksId, currentUser.getOwnerId(), Constant.Token.Storage_Refund_Outbound, "KE000001", sku, 1);
                dtl.setId(new GuidCreator().toString());
                dtl.setStyleId(e.getStyleId());
                dtl.setColorId(e.getColorId());
                dtl.setSizeId(e.getSizeId());
                dtl.setType(Constant.TaskType.Outbound);
                dtl.setDestId(purchaseReturnBill.getDestId());
                dtl.setDestUnitId(purchaseReturnBill.getDestUnitId());
                dtl.setOrigId(purchaseReturnBill.getOrigId());
                dtl.setOrigUnitId(purchaseReturnBill.getOrigUnitId());
                dtl.setPreVal(0D);
                businessDtlMap.put(sku, dtl);
            }


            Record record = new Record(e.getCode(), taksId, taksId, Constant.Token.Storage_Refund_Outbound, "KE000001", "");
            record.setOwnerId(currentUser.getOwnerId());
            record.setDestId(purchaseReturnBill.getDestId());
            record.setDestUnitId(purchaseReturnBill.getDestUnitId());
            record.setOrigId(purchaseReturnBill.getOrigId());
            record.setOrigUnitId(purchaseReturnBill.getOrigUnitId());
            record.setSku(sku);
            record.setStyleId(e.getStyleId());
            record.setColorId(e.getColorId());
            record.setSizeId(e.getSizeId());
            record.setScanTime(new Date());
            PurchaseReturnBillDtl dtl = purchaseBillDtlMap.get(record.getSku());
            /* record.setExtField(dtl.getInStockType());//record中增加入库类型*/
            record.setId(new GuidCreator().toString());
            record.setType(Constant.TaskType.Outbound);
            recordList.add(record);
        }
        bus.setDtlList(new ArrayList<>(businessDtlMap.values()));
        bus.setId(taksId);
        bus.setToken(Constant.Token.Storage_Refund_Outbound);
        bus.setBeginTime(new Date());
        bus.setEndTime(new Date());
        bus.setBillId(purchaseReturnBill.getBillNo());
        bus.setBillNo(purchaseReturnBill.getBillNo());
        bus.setDestId(purchaseReturnBill.getDestId());
        bus.setDestUnitId(purchaseReturnBill.getDestUnitId());
        bus.setOrigId(purchaseReturnBill.getOrigId());
        bus.setOrigUnitId(purchaseReturnBill.getOrigUnitId());
        bus.setOwnerId(currentUser.getOwnerId());
        bus.setDeviceId("KE000001");
        bus.setStatus(Constant.TaskStatus.Submitted);
        bus.setTotCarton(1L);
        bus.setTotEpc((long) epcList.size());
        bus.setTotPrice(totRcvPrice);
        bus.setTotSku((long) bus.getDtlList().size());
        bus.setTotStyle((long) styleCountMap.size());
        bus.setType(Constant.TaskType.Outbound);
        bus.setRecordList(recordList);
        purchaseReturnBill.setTotOutQty(purchaseReturnBill.getTotOutQty() + epcList.size());
        purchaseReturnBill.setTotOutVal(purchaseReturnBill.getTotOutVal() + totRcvPrice);
        if (purchaseReturnBill.getTotOutQty().intValue() == purchaseReturnBill.getTotQty().intValue()) {
            purchaseReturnBill.setStatus(BillConstant.BillStatus.End);
            purchaseReturnBill.setOutStatus(BillConstant.BillStatus.End);
        } else {
            purchaseReturnBill.setStatus(BillConstant.BillStatus.Doing);
            purchaseReturnBill.setOutStatus(BillConstant.BillInOutStatus.Outing);
        }

        List<String> codeStrList = new ArrayList<>();
        Map<String, Record> recordMap = new HashMap<>();
        for (Record r : bus.getRecordList()) {
            codeStrList.add(r.getCode());
            recordMap.put(r.getCode(), r);
        }
        List<EpcStock> epcStockList = epcStockService.findEpcByCodes(TaskUtil.getSqlStrByList(codeStrList, EpcStock.class, "code"));
        Double totPreVal = 0D;
        for (EpcStock s : epcStockList) {
            BusinessDtl dtl = businessDtlMap.get(s.getSku());
            Double preVal = CommonUtil.isBlank(s.getStockPrice()) ? 0D : s.getStockPrice();
            dtl.setPreVal(dtl.getPreVal() + preVal);
            totPreVal += preVal;
            Record r = recordMap.get(s.getCode());
            r.setPrice(preVal);
            PurchaseReturnBillDtl purchaseReturnBillDtl = purchaseBillDtlMap.get(s.getSku());
            purchaseReturnBillDtl.setStockVal(purchaseReturnBillDtl.getStockVal() + preVal);
        }
        purchaseReturnBill.setTotStockVal(purchaseReturnBill.getTotStockVal() + totPreVal);
        bus.setTotPreVal(totPreVal);
        bus.setRecordList(new ArrayList<>(recordMap.values()));
        return bus;
    }

    /**
     * 销售退货单管理出库(web扫描唯一码提交)
     *
     * @param saleOrderReturnBill
     * @param saleOrderReturnBillDtlList
     * @param epcList
     * @param currentUser
     * @return
     */
    public static Business covertToSaleReturnOrderBusinessOut(SaleOrderReturnBill saleOrderReturnBill, List<SaleOrderReturnBillDtl> saleOrderReturnBillDtlList, List<Epc> epcList, User currentUser, String AbnormalCodeMessagecodes,List<AbnormalCodeMessage> abnormalCodeMessageByBillNo) {
        Map<String, SaleOrderReturnBillDtl> saleOrderReturnBillDtlMap = new HashMap<>();
        for (SaleOrderReturnBillDtl dtl : saleOrderReturnBillDtlList) {
            saleOrderReturnBillDtlMap.put(dtl.getSku(), dtl);
        }
        String taksId = "TSK" + CommonUtil.getDateString(new Date(), "yyyyMMdd") + System.currentTimeMillis();
        Business bus = new Business();
        Map<String, BusinessDtl> businessDtlMap = new HashMap<>();
        Map<String, String> styleCountMap = new HashMap<>();
        List<Record> recordList = new ArrayList<>();
        Double totRcvPrice = 0d;
        List<BillRecord> billRecordList = new ArrayList<>();
        for (Epc e : epcList) {
            String sku = e.getSku();
            styleCountMap.put(e.getStyleId(), e.getStyleId());
            if (saleOrderReturnBillDtlMap.containsKey(sku)) {
                SaleOrderReturnBillDtl dtl = saleOrderReturnBillDtlMap.get(sku);
                if(CommonUtil.isNotBlank(AbnormalCodeMessagecodes)){
                    if(AbnormalCodeMessagecodes.indexOf(e.getCode())!=-1){
                        BillRecord billRecord = new BillRecord(dtl.getBillNo() + "-" + e.getCode(), e.getCode(), dtl.getBillNo(), dtl.getSku());
                        billRecordList.add(billRecord);
                    }
                }
                /*BillRecord billRecord = new BillRecord(dtl.getBillNo() + "-" + e.getCode(), e.getCode(), dtl.getBillNo(), dtl.getSku());
                billRecordList.add(billRecord);*/
                dtl.setOutQty(dtl.getOutQty() + 1);
                dtl.setOutVal(dtl.getOutQty() * dtl.getActPrice());
                dtl.setOutStatus(BillConstant.BillDtlStatus.Outing);
                if (dtl.getOutQty().intValue() == dtl.getQty().intValue()) {
                    dtl.setStatus(BillConstant.BillDtlStatus.OutStore);
                    dtl.setOutStatus(BillConstant.BillDtlStatus.OutStore);
                }
                totRcvPrice += dtl.getPrice();
                dtl.setStockVal(0D);
                saleOrderReturnBillDtlMap.put(sku, dtl);

            }
            if (businessDtlMap.containsKey(e.getSku())) {
                BusinessDtl dtl = businessDtlMap.get(sku);
                dtl.setQty(dtl.getQty() + 1);
                businessDtlMap.put(sku, dtl);
            } else {
                BusinessDtl dtl = new BusinessDtl(taksId, currentUser.getOwnerId(), Constant.Token.Storage_refoundOut_customer, "KE000001", sku, 1);
                dtl.setId(new GuidCreator().toString());
                dtl.setStyleId(e.getStyleId());
                dtl.setColorId(e.getColorId());
                dtl.setSizeId(e.getSizeId());
                dtl.setType(Constant.TaskType.Outbound);
                dtl.setDestId(saleOrderReturnBill.getDestId());
                dtl.setDestUnitId(saleOrderReturnBill.getDestUnitId());
                dtl.setOrigId(saleOrderReturnBill.getOrigId());
                dtl.setOrigUnitId(saleOrderReturnBill.getOrigUnitId());
                dtl.setPreVal(0D);
                businessDtlMap.put(sku, dtl);
            }


            Record record = new Record(e.getCode(), taksId, taksId, Constant.Token.Storage_refoundOut_customer, "KE000001", "");
            record.setOwnerId(currentUser.getOwnerId());
            record.setDestId(saleOrderReturnBill.getDestId());
            record.setDestUnitId(saleOrderReturnBill.getDestUnitId());
            record.setOrigId(saleOrderReturnBill.getOrigId());
            record.setOrigUnitId(saleOrderReturnBill.getOrigUnitId());
            record.setSku(sku);
            record.setStyleId(e.getStyleId());
            record.setColorId(e.getColorId());
            record.setSizeId(e.getSizeId());
            record.setPrice(saleOrderReturnBillDtlMap.get(record.getSku()).getActPrice());
            record.setScanTime(new Date());
            SaleOrderReturnBillDtl dtl = saleOrderReturnBillDtlMap.get(record.getSku());
            /* record.setExtField(dtl.getInStockType());//record中增加入库类型*/
            record.setId(new GuidCreator().toString());
            record.setType(Constant.TaskType.Outbound);
            recordList.add(record);
        }
        bus.setDtlList(new ArrayList<>(businessDtlMap.values()));
        bus.setId(taksId);
        bus.setToken(Constant.Token.Storage_refoundOut_customer);
        bus.setBeginTime(new Date());
        bus.setEndTime(new Date());
        bus.setBillId(saleOrderReturnBill.getBillNo());
        bus.setBillNo(saleOrderReturnBill.getBillNo());
        bus.setDestId(saleOrderReturnBill.getDestId());
        bus.setDestUnitId(saleOrderReturnBill.getDestUnitId());
        bus.setOrigId(saleOrderReturnBill.getOrigId());
        bus.setOrigUnitId(saleOrderReturnBill.getOrigUnitId());
        bus.setOwnerId(currentUser.getOwnerId());
        bus.setDeviceId("KE000001");
        bus.setStatus(Constant.TaskStatus.Submitted);
        bus.setTotCarton(1L);
        bus.setTotEpc((long) epcList.size());
        bus.setTotPrice(totRcvPrice);
        bus.setTotSku((long) bus.getDtlList().size());
        bus.setTotStyle((long) styleCountMap.size());
        bus.setType(Constant.TaskType.Outbound);
        bus.setRecordList(recordList);
        saleOrderReturnBill.setBillRecordList(billRecordList);
        saleOrderReturnBill.setTotOutQty(saleOrderReturnBill.getTotOutQty() + epcList.size());
        saleOrderReturnBill.setTotOutVal(saleOrderReturnBill.getTotOutVal() + totRcvPrice);
        saleOrderReturnBill.setStatus(BillConstant.BillStatus.Doing);
        if (saleOrderReturnBill.getTotOutQty().intValue() == saleOrderReturnBill.getTotQty().intValue()) {
            saleOrderReturnBill.setOutStatus(BillConstant.BillInOutStatus.OutStore);
        } else {
            saleOrderReturnBill.setOutStatus(BillConstant.BillInOutStatus.Outing);
        }
        //出库填写EpcStock中成本价格
        List<String> codeStrList = new ArrayList<>();
        Map<String, Record> recordMap = new HashMap<>();
        for (Record r : bus.getRecordList()) {
            codeStrList.add(r.getCode());
            recordMap.put(r.getCode(), r);
        }
        List<EpcStock> epcStockList = epcStockService.findEpcByCodes(TaskUtil.getSqlStrByList(codeStrList, EpcStock.class, "code"));
        Double totPreVal = 0D;
        for (EpcStock s : epcStockList) {
            BusinessDtl dtl = businessDtlMap.get(s.getSku());
            Double preVal = CommonUtil.isBlank(s.getStockPrice()) ? 0D : s.getStockPrice();
            dtl.setPreVal(dtl.getPreVal() + preVal);
            totPreVal += preVal;
            Record r = recordMap.get(s.getCode());
            r.setPrice(preVal);
            SaleOrderReturnBillDtl saleOrderReturnBillDtl = saleOrderReturnBillDtlMap.get(s.getSku());
            saleOrderReturnBillDtl.setStockVal(saleOrderReturnBillDtl.getStockVal() + preVal);
            //设置利润
           /* saleOrderBillDtl.setProfit(saleOrderBillDtl.getOutVal()-saleOrderBillDtl.getTotStockVal());
            saleOrderBillDtl.setProfitRate(Double.parseDouble(CommonUtil.getDecimal(saleOrderBillDtl.getTotStockVal()/saleOrderBillDtl.getOutQty()*100,"######0.00")));*/

        }
        saleOrderReturnBill.setTotStockVal(saleOrderReturnBill.getTotStockVal() + totPreVal);
        bus.setTotPreVal(totPreVal);

        bus.setRecordList(new ArrayList<>(recordMap.values()));
        if(CommonUtil.isNotBlank(abnormalCodeMessageByBillNo)&&abnormalCodeMessageByBillNo.size()!=0){
            String codes="";
            for(Epc epc:epcList){
                if(CommonUtil.isNotBlank(codes)){
                    codes+=","+epc.getCode();
                }else{
                    codes+=epc.getCode();
                }
            }
            for(AbnormalCodeMessage abnormalCodeMessage:abnormalCodeMessageByBillNo){
                if(codes.indexOf(abnormalCodeMessage.getCode())!=-1){
                    abnormalCodeMessage.setStatus(0);
                }
            }
        }

        return bus;
    }

    /**
     * 销售退货单管理入库（唯一码扫描入库）
     *
     * @param saleOrderReturnBill
     * @param purchaseOrderBillDtlList
     * @param epcList
     * @param currentUser
     * @return
     */
    public static Business covertToSaleReturnOrderBusinessIn(SaleOrderReturnBill saleOrderReturnBill, List<SaleOrderReturnBillDtl> purchaseOrderBillDtlList, List<Epc> epcList, User currentUser) {
        Map<String, SaleOrderReturnBillDtl> purchaseBillDtlMap = new HashMap<>();
        for (SaleOrderReturnBillDtl dtl : purchaseOrderBillDtlList) {
            purchaseBillDtlMap.put(dtl.getSku(), dtl);
        }
        String taksId = "TSK" + CommonUtil.getDateString(new Date(), "yyyyMMdd") + System.currentTimeMillis();
        Business bus = new Business();
        Map<String, BusinessDtl> businessDtlMap = new HashMap<>();
        Map<String, String> styleCountMap = new HashMap<>();
        List<Record> recordList = new ArrayList<>();
        List<BillRecord> billRecordList = new ArrayList<>();
        Double totRcvPrice = 0d;
        for (Epc e : epcList) {
            String sku = e.getSku();
            styleCountMap.put(e.getStyleId(), e.getStyleId());
            if (purchaseBillDtlMap.containsKey(sku)) {
                SaleOrderReturnBillDtl dtl = purchaseBillDtlMap.get(sku);
                /*BillRecord billRecord = new BillRecord(dtl.getBillNo() + "-" + e.getCode(), e.getCode(), dtl.getBillNo(), dtl.getSku());
                billRecordList.add(billRecord);*/
                dtl.setInQty(dtl.getInQty() + 1);
                dtl.setInVal(dtl.getInQty() * dtl.getActPrice());
                dtl.setInStatus(BillConstant.BillDtlStatus.Ining);
                if (dtl.getInQty().intValue() == dtl.getQty().intValue()) {
                    dtl.setInStatus(BillConstant.BillDtlStatus.InStore);
                    dtl.setStatus(BillConstant.BillDtlStatus.InStore);
                }
                totRcvPrice += dtl.getPrice();
                purchaseBillDtlMap.put(sku, dtl);
                dtl.setStockVal(dtl.getInVal());
            }
            if (businessDtlMap.containsKey(e.getSku())) {
                BusinessDtl dtl = businessDtlMap.get(sku);
                dtl.setQty(dtl.getQty() + 1);
                businessDtlMap.put(sku, dtl);
            } else {
                BusinessDtl dtl = new BusinessDtl(taksId, currentUser.getOwnerId(), Constant.Token.Storage_Refund_Inbound, "KE000001", sku, 1);
                dtl.setId(new GuidCreator().toString());
                dtl.setStyleId(e.getStyleId());
                dtl.setColorId(e.getColorId());
                dtl.setSizeId(e.getSizeId());
                dtl.setType(Constant.TaskType.Inbound);
                dtl.setDestId(saleOrderReturnBill.getDestId());
                dtl.setDestUnitId(saleOrderReturnBill.getDestUnitId());
                dtl.setOrigId(saleOrderReturnBill.getOrigId());
                dtl.setOrigUnitId(saleOrderReturnBill.getOrigUnitId());
                businessDtlMap.put(sku, dtl);
            }
            Record record = new Record(e.getCode(), taksId, taksId, Constant.Token.Storage_Refund_Inbound, "KE000001", "");
            record.setOwnerId(currentUser.getOwnerId());
            record.setDestId(saleOrderReturnBill.getDestId());
            record.setDestUnitId(saleOrderReturnBill.getDestUnitId());
            record.setOrigId(saleOrderReturnBill.getOrigId());
            record.setOrigUnitId(saleOrderReturnBill.getOrigUnitId());
            record.setSku(sku);
            record.setStyleId(e.getStyleId());
            record.setColorId(e.getColorId());
            record.setSizeId(e.getSizeId());
            record.setPrice(purchaseBillDtlMap.get(record.getSku()).getActPrice());
            record.setScanTime(new Date());
            record.setId(new GuidCreator().toString());
            record.setType(Constant.TaskType.Inbound);
            recordList.add(record);
        }
        bus.setDtlList(new ArrayList<>(businessDtlMap.values()));
        bus.setId(taksId);
        bus.setToken(Constant.Token.Storage_Refund_Inbound);
        bus.setBeginTime(new Date());
        bus.setEndTime(new Date());
        bus.setBillId(saleOrderReturnBill.getBillNo());
        bus.setBillNo(saleOrderReturnBill.getBillNo());
        bus.setDestId(saleOrderReturnBill.getDestId());
        bus.setDestUnitId(saleOrderReturnBill.getDestUnitId());
        bus.setOrigId(saleOrderReturnBill.getOrigId());
        bus.setOrigUnitId(saleOrderReturnBill.getOrigUnitId());
        bus.setOwnerId(currentUser.getOwnerId());
        bus.setDeviceId("KE000001");
        bus.setStatus(Constant.TaskStatus.Submitted);
        bus.setTotCarton(1L);
        bus.setTotEpc((long) epcList.size());
        bus.setTotPrice(totRcvPrice);
        bus.setTotSku((long) bus.getDtlList().size());
        bus.setTotStyle((long) styleCountMap.size());
        bus.setType(Constant.TaskType.Inbound);
        bus.setRecordList(recordList);
        bus.setTotPreVal(totRcvPrice);
        // 销售退货入库库更新任务成本
        Double totPreVal = 0D;
        for (Record r : bus.getRecordList()) {
            BusinessDtl dtl = businessDtlMap.get(r.getSku());
            SaleOrderReturnBillDtl saleOrderReturnBillDtl = purchaseBillDtlMap.get(r.getSku());
            r.setPrice(saleOrderReturnBillDtl.getActPrice());
            totPreVal += r.getPrice();
            dtl.setPreVal(dtl.getQty() * saleOrderReturnBillDtl.getActPrice());
        }
        bus.setTotPreVal(totPreVal);
        saleOrderReturnBill.setBillRecordList(billRecordList);
        saleOrderReturnBill.setTotInQty(saleOrderReturnBill.getTotInQty() + epcList.size());
        saleOrderReturnBill.setTotInVal(saleOrderReturnBill.getTotInVal() + totRcvPrice);
        if (saleOrderReturnBill.getTotInQty().intValue() == saleOrderReturnBill.getTotQty().intValue()) {
            saleOrderReturnBill.setStatus(BillConstant.BillStatus.End);
            saleOrderReturnBill.setInStatus(BillConstant.BillInOutStatus.InStore);
        } else {
            saleOrderReturnBill.setStatus(BillConstant.BillStatus.Doing);
            saleOrderReturnBill.setInStatus(BillConstant.BillInOutStatus.Ining);
        }

        return bus;
    }

    /**
     * 寄存单管理入库（唯一码扫描入库）
     *
     * @param consignmentBill
     * @param consignmentBillDtlList
     * @param epcList
     * @param currentUser
     * @return
     */
    public static Business covertToConsignmentBillBusinessIn(ConsignmentBill consignmentBill, List<ConsignmentBillDtl> consignmentBillDtlList, List<Epc> epcList, User currentUser) {
        Map<String, ConsignmentBillDtl> purchaseBillDtlMap = new HashMap<>();
        for (ConsignmentBillDtl dtl : consignmentBillDtlList) {
            purchaseBillDtlMap.put(dtl.getSku(), dtl);
        }
        String taksId = "TSK" + CommonUtil.getDateString(new Date(), "yyyyMMdd") + System.currentTimeMillis();
        Business bus = new Business();
        Map<String, BusinessDtl> businessDtlMap = new HashMap<>();
        Map<String, String> styleCountMap = new HashMap<>();
        List<Record> recordList = new ArrayList<>();
        Double totRcvPrice = 0d;
        for (Epc e : epcList) {
            String sku = e.getSku();
            styleCountMap.put(e.getStyleId(), e.getStyleId());
            if (purchaseBillDtlMap.containsKey(sku)) {
                ConsignmentBillDtl dtl = purchaseBillDtlMap.get(sku);
                dtl.setInQty(dtl.getInQty() + 1);
                dtl.setInVal(dtl.getInQty() * dtl.getActPrice());
                dtl.setInStatus(BillConstant.BillDtlStatus.Ining);
                if (dtl.getInQty().intValue() == dtl.getQty().intValue()) {
                    dtl.setInStatus(BillConstant.BillDtlStatus.InStore);
                    dtl.setStatus(BillConstant.BillDtlStatus.InStore);
                }
                totRcvPrice += dtl.getPrice();
                purchaseBillDtlMap.put(sku, dtl);
                dtl.setStockVal(dtl.getInVal());
            }
            if (businessDtlMap.containsKey(e.getSku())) {
                BusinessDtl dtl = businessDtlMap.get(sku);
                dtl.setQty(dtl.getQty() + 1);
                businessDtlMap.put(sku, dtl);
            } else {
                BusinessDtl dtl = new BusinessDtl(taksId, currentUser.getOwnerId(), Constant.Token.Storage_Consigment_Inbound, "KE000001", sku, 1);
                dtl.setId(new GuidCreator().toString());
                dtl.setStyleId(e.getStyleId());
                dtl.setColorId(e.getColorId());
                dtl.setSizeId(e.getSizeId());
                dtl.setType(Constant.TaskType.Inbound);
                dtl.setDestId(consignmentBill.getDestId());
                dtl.setDestUnitId(consignmentBill.getDestUnitId());
                dtl.setOrigId(consignmentBill.getOrigId());
                dtl.setOrigUnitId(consignmentBill.getOrigUnitId());
                businessDtlMap.put(sku, dtl);
            }
            Record record = new Record(e.getCode(), taksId, taksId, Constant.Token.Storage_Consigment_Inbound, "KE000001", "");
            record.setOwnerId(currentUser.getOwnerId());
            record.setDestId(consignmentBill.getDestId());
            record.setDestUnitId(consignmentBill.getDestUnitId());
            record.setOrigId(consignmentBill.getOrigId());
            record.setOrigUnitId(consignmentBill.getOrigUnitId());
            record.setSku(sku);
            record.setStyleId(e.getStyleId());
            record.setColorId(e.getColorId());
            record.setSizeId(e.getSizeId());
            record.setPrice(purchaseBillDtlMap.get(record.getSku()).getActPrice());
            record.setScanTime(new Date());
            ConsignmentBillDtl consignmentBillDtl = purchaseBillDtlMap.get(record.getSku());
            record.setExtField(consignmentBillDtl.getInStockType());//record中增加入库类型
            record.setId(new GuidCreator().toString());
            record.setType(Constant.TaskType.Inbound);
            recordList.add(record);
        }
        bus.setDtlList(new ArrayList<>(businessDtlMap.values()));
        bus.setId(taksId);
        bus.setToken(Constant.Token.Storage_Refund_Inbound);
        bus.setBeginTime(new Date());
        bus.setEndTime(new Date());
        bus.setBillId(consignmentBill.getBillNo());
        bus.setBillNo(consignmentBill.getBillNo());
        bus.setDestId(consignmentBill.getDestId());
        bus.setDestUnitId(consignmentBill.getDestUnitId());
        bus.setOrigId(consignmentBill.getOrigId());
        bus.setOrigUnitId(consignmentBill.getOrigUnitId());
        bus.setOwnerId(currentUser.getOwnerId());
        bus.setDeviceId("KE000001");
        bus.setStatus(Constant.TaskStatus.Submitted);
        bus.setTotCarton(1L);
        bus.setTotEpc((long) epcList.size());
        bus.setTotPrice(totRcvPrice);
        bus.setTotSku((long) bus.getDtlList().size());
        bus.setTotStyle((long) styleCountMap.size());
        bus.setType(Constant.TaskType.Inbound);
        bus.setRecordList(recordList);
        bus.setTotPreVal(totRcvPrice);
        // 销售退货入库库更新任务成本
        Double totPreVal = 0D;
        for (Record r : bus.getRecordList()) {
            BusinessDtl dtl = businessDtlMap.get(r.getSku());
            ConsignmentBillDtl consignmentBillDtl = purchaseBillDtlMap.get(r.getSku());
            r.setPrice(consignmentBillDtl.getActPrice());
            totPreVal += r.getPrice();
            dtl.setPreVal(dtl.getQty() * consignmentBillDtl.getActPrice());
        }
        bus.setTotPreVal(totPreVal);


        consignmentBill.setTotInQty(consignmentBill.getTotInQty() + epcList.size());
        consignmentBill.setTotInVal(consignmentBill.getTotInVal() + totRcvPrice);
        if (consignmentBill.getTotInQty().intValue() == consignmentBill.getTotQty().intValue()) {
            consignmentBill.setStatus(BillConstant.BillStatus.End);
            consignmentBill.setInStatus(BillConstant.BillInOutStatus.InStore);
        } else {
            consignmentBill.setStatus(BillConstant.BillStatus.Doing);
            consignmentBill.setInStatus(BillConstant.BillInOutStatus.Ining);
        }

        return bus;
    }


    /**
     * 客户端上传采购单入库更新单据信息
     */
    public static void covertToPurchaseBusiness(PurchaseOrderBill purchaseOrderBill, List<PurchaseOrderBillDtl> purchaseOrderBillDtlList, Business bus) {
        Map<String, PurchaseOrderBillDtl> purchaseBillDtlMap = new HashMap<>();
        for (PurchaseOrderBillDtl dtl : purchaseOrderBillDtlList) {
            purchaseBillDtlMap.put(dtl.getSku(), dtl);
        }
        Double totRcvPrice = 0d;
        Double totPreVal = 0D;
        for (BusinessDtl dtl : bus.getDtlList()) {
            PurchaseOrderBillDtl purchaseOrderBillDtl = purchaseBillDtlMap.get(dtl.getSku());
            purchaseOrderBillDtl.setId(new GuidCreator().toString());
            // System.out.println(dtl.getSku()+"-"+dtl.getQty());
            if (CommonUtil.isBlank(dtl.getQty())) {
                System.out.println("dtlQty is null:" + dtl.getSku());
            }
            if (CommonUtil.isBlank(purchaseOrderBillDtl)) {
                System.out.println("purchaseOrderBillDtl is null:" + dtl.getSku());
            }
            purchaseOrderBillDtl.setInQty(purchaseOrderBillDtl.getInQty() + (int) dtl.getQty());
            purchaseOrderBillDtl.setInVal(purchaseOrderBillDtl.getInQty() * purchaseOrderBillDtl.getActPrice());
            purchaseOrderBillDtl.setInStatus(BillConstant.BillDtlStatus.Ining);
            if (purchaseOrderBillDtl.getInQty().intValue() == purchaseOrderBillDtl.getQty().intValue()) {
                purchaseOrderBillDtl.setStatus(BillConstant.BillDtlStatus.InStore);
                purchaseOrderBillDtl.setStatus(BillConstant.BillDtlStatus.InStore);
            }
            totRcvPrice += purchaseOrderBillDtl.getInVal();
            totPreVal += purchaseOrderBillDtl.getInQty() + purchaseOrderBillDtl.getActPrice();
            dtl.setPreVal(dtl.getQty() * purchaseOrderBillDtl.getActPrice());
        }
        //把出入库任务Record中的唯一码转为单据中的billRecord
        List<BillRecord> billRecordList = new ArrayList<>();
        List<String> codeStrList = new ArrayList<>();
        for (Record r : bus.getRecordList()) {
            codeStrList.add(r.getCode());
            BillRecord billRecord = new BillRecord(purchaseOrderBill.getBillNo() + "-" + r.getCode(), r.getCode(), purchaseOrderBill.getBillNo(), r.getSku());
            billRecordList.add(billRecord);
        }
        bus.setTotPreVal(totPreVal);
        bus.setDestId(purchaseOrderBill.getDestId());
        bus.setDestUnitId(purchaseOrderBill.getDestUnitId());
        bus.setOrigId(purchaseOrderBill.getOrigId());
        bus.setOrigUnitId(purchaseOrderBill.getOrigUnitId());
        purchaseOrderBill.setBillRecordList(billRecordList);//单据关联保存唯一码
        purchaseOrderBill.setTotInQty(purchaseOrderBill.getTotInQty() + bus.getTotEpc());
        purchaseOrderBill.setTotInVal(purchaseOrderBill.getTotInVal() + totRcvPrice);
        purchaseOrderBill.setStatus(BillConstant.BillStatus.Doing);
        purchaseOrderBill.setInStatus(BillConstant.BillInOutStatus.Ining);
        if (purchaseOrderBill.getTotInQty().intValue() == purchaseOrderBill.getTotQty().intValue()) {
            purchaseOrderBill.setStatus(BillConstant.BillStatus.End);
            purchaseOrderBill.setInStatus(BillConstant.BillInOutStatus.InStore);
        }
        for (Record r : bus.getRecordList()) {
            PurchaseOrderBillDtl dtl = purchaseBillDtlMap.get(r.getSku());
            r.setPrice(dtl.getActPrice());
            r.setExtField(dtl.getInStockType());//record中增加入库类型
        }
    }

    /**
     * 完善新增销售单据信息
     *
     * @param saleOrderBill
     * @param saleOrderBillDtlList
     * @param curUser
     */
    public static void covertToSaleOrderBill(SaleOrderBill saleOrderBill, List<SaleOrderBillDtl> saleOrderBillDtlList, User curUser) {
        if (CommonUtil.isNotBlank(curUser)) {
            saleOrderBill.setOprId(curUser.getCode());
        }
        Long totQty = 0L;
        Long actQty = 0L;
        Integer totOutQty = 0;
        Integer totInQty = 0;
        Double totPrice = 0D;
        Double actPrice = 0D;
        Double totOutVal = 0D;
        Double totInVal = 0D;
        List<BillRecord> billRecordList = new ArrayList<>();
        for (SaleOrderBillDtl dtl : saleOrderBillDtlList) {
            dtl.setId(new GuidCreator().toString());
            dtl.setBillId(saleOrderBill.getId());
            dtl.setBillNo(saleOrderBill.getBillNo());
            dtl.setActQty(dtl.getQty());
            totQty += dtl.getQty();
            actQty += dtl.getQty();
            totPrice += dtl.getPrice() * dtl.getQty();
            actPrice += dtl.getActPrice() * dtl.getQty();
            totOutQty += dtl.getOutQty();
            totInQty += dtl.getInQty();
            totOutVal = totOutQty * dtl.getActPrice();
            totInVal = totInQty * dtl.getActPrice();
            if (CommonUtil.isBlank(dtl.getReturnQty())) {
                dtl.setReturnQty(0);
            }
            if (CommonUtil.isNotBlank(dtl.getUniqueCodes())) {
                for (String code : dtl.getUniqueCodes().split(",")) {
                    if(CommonUtil.isNotBlank(code)) {
                        BillRecord billRecord = new BillRecord(dtl.getBillNo() + "-" + code, code, dtl.getBillNo(), dtl.getSku());
                        billRecordList.add(billRecord);
                    }
                }
            }


        }
        saleOrderBill.setBillRecordList(billRecordList);
        if (CommonUtil.isBlank(saleOrderBill.getOwnerId())) {
            saleOrderBill.setOwnerId(curUser.getOwnerId());
        }
        if (CommonUtil.isBlank(saleOrderBill.getStatus())) {
            saleOrderBill.setStatus(BillConstant.BillStatus.Enter);
        }
        //发货方和仓库
        Unit ventory = CacheManager.getUnitByCode(saleOrderBill.getOrigId());//发货仓库
        saleOrderBill.setOrigName(ventory.getName());
        saleOrderBill.setOrigId(ventory.getId());//发货仓名词
        saleOrderBill.setOrigUnitId(ventory.getOwnerId());
        Unit orgunit = CacheManager.getUnitByCode(ventory.getOwnerId());
        saleOrderBill.setOrigUnitName(orgunit.getName());
        //收货仓库和收货方(即有可能是门店、代理商或者零售客户)
        Unit orgunit1 = CacheManager.getUnitByCode(saleOrderBill.getDestUnitId());
        if (CommonUtil.isBlank(orgunit1)) {
            Customer customer = CacheManager.getCustomerById(saleOrderBill.getDestUnitId());
            saleOrderBill.setDestUnitName(customer.getName());
        } else {
            saleOrderBill.setDestUnitName(orgunit1.getName());
        }
        saleOrderBill.setTotQty(totQty);
        saleOrderBill.setTotPrice(totPrice);
        saleOrderBill.setActQty(actQty);
        saleOrderBill.setActPrice(0D + Math.round(actPrice));
        saleOrderBill.setTotOutQty(totOutQty.longValue());
        saleOrderBill.setTotInQty(totInQty.longValue());
        saleOrderBill.setTotOutVal(totOutVal);
        saleOrderBill.setTotInVal(totInVal);
        if (CommonUtil.isNotBlank(saleOrderBill.getBusnissId())) {
            saleOrderBill.setBusnissName(CacheManager.getUserById(saleOrderBill.getBusnissId()).getName());
        }
    }

    /**
     * 完善新增销售单据信息在退货时
     *
     * @param saleOrderBill
     * @param saleOrderBillDtlList
     * @param curUser
     */
    public static void covertToSaleOrderBillOnRetrun(SaleOrderBill saleOrderBill, List<SaleOrderBillDtl> saleOrderBillDtlList, User curUser) {
        if (CommonUtil.isNotBlank(curUser)) {
            saleOrderBill.setOprId(curUser.getCode());
        }
        Long totQty = 0L;
        Long actQty = 0L;
        Integer totOutQty = 0;
        Integer totInQty = 0;
        Double totPrice = 0D;
        Double actPrice = 0D;
        Double totOutVal = 0D;
        Double totInVal = 0D;
        List<BillRecord> billRecordList = new ArrayList<>();
        for (SaleOrderBillDtl dtl : saleOrderBillDtlList) {
            dtl.setId(new GuidCreator().toString());
            dtl.setBillId(saleOrderBill.getId());
            dtl.setBillNo(saleOrderBill.getBillNo());
            dtl.setActQty(dtl.getQty());
            //totQty += dtl.getQty();
            //actQty += dtl.getQty();
            //totPrice += dtl.getPrice() * dtl.getQty();
            //actPrice += dtl.getActPrice() * dtl.getQty();
            //totOutQty += dtl.getOutQty();
            //totInQty += dtl.getInQty();
            // totOutVal = totOutQty * dtl.getActPrice();
            //totInVal = totInQty * dtl.getActPrice();
            if (CommonUtil.isBlank(dtl.getReturnQty())) {
                dtl.setReturnQty(0);
            }
            if (CommonUtil.isNotBlank(dtl.getUniqueCodes())) {
                for (String code : dtl.getUniqueCodes().split(",")) {
                    if(CommonUtil.isNotBlank(code)) {
                        BillRecord billRecord = new BillRecord(dtl.getBillNo() + "-" + code, code, dtl.getBillNo(), dtl.getSku());
                        billRecordList.add(billRecord);
                    }
                }
            }


        }
        saleOrderBill.setBillRecordList(billRecordList);

        if (CommonUtil.isBlank(saleOrderBill.getStatus())) {
            saleOrderBill.setStatus(BillConstant.BillStatus.Enter);
        }
        //发货方和仓库
        Unit ventory = CacheManager.getUnitByCode(saleOrderBill.getOrigId());//发货仓库
        saleOrderBill.setOrigName(ventory.getName());
        saleOrderBill.setOrigId(ventory.getId());//发货仓名词
        saleOrderBill.setOrigUnitId(ventory.getOwnerId());
        Unit orgunit = CacheManager.getUnitByCode(ventory.getOwnerId());
        saleOrderBill.setOrigUnitName(orgunit.getName());
        //收货仓库和收货方(即有可能是门店、代理商或者零售客户)
        Unit orgunit1 = CacheManager.getUnitByCode(saleOrderBill.getDestUnitId());
        if (CommonUtil.isBlank(orgunit1)) {
            Customer customer = CacheManager.getCustomerById(saleOrderBill.getDestUnitId());
            saleOrderBill.setDestUnitName(customer.getName());
        } else {
            saleOrderBill.setDestUnitName(orgunit1.getName());
        }
        /*saleOrderBill.setTotQty(totQty);
        saleOrderBill.setTotPrice(totPrice);
        saleOrderBill.setActQty(actQty);
        saleOrderBill.setActPrice(actPrice);
        saleOrderBill.setTotOutQty(totOutQty.longValue());
        saleOrderBill.setTotInQty(totInQty.longValue());
        saleOrderBill.setTotOutVal(totOutVal);
        saleOrderBill.setTotInVal(totInVal);*/
        if (CommonUtil.isNotBlank(saleOrderBill.getBusnissId())) {
            saleOrderBill.setBusnissName(CacheManager.getUserById(saleOrderBill.getBusnissId()).getName());
        }
    }

    /**
     * 销售订单单保存退货单
     */

    public static void saveReturnSaleOrder(SaleOrderBill saleOrderBill, List<SaleOrderBillDtl> saleOrderBillDtlList, List<SaleOrderReturnBillDtl> saleOrderReturnBillDtls, SaleOrderReturnBill saleOrderReturnBill, String billNo, String code) {
        Integer totReturnQty = 0;
        Double totPrice = 0D;
        for (int i = 0; i < saleOrderBillDtlList.size(); i++) {
            SaleOrderReturnBillDtl saleOrderReturnBillDtl = new SaleOrderReturnBillDtl();
            SaleOrderBillDtl saleOrderBillDtl = saleOrderBillDtlList.get(i);
            if (saleOrderBillDtl.getReturnQty() > 0) {
                saleOrderBillDtl.setReturnbillNo(billNo);
                if (saleOrderBillDtl.getOutQty().intValue() + saleOrderBillDtl.getReturnQty() == saleOrderBillDtl.getQty().intValue()) {
                    saleOrderBillDtl.setOutStatus(BillConstant.BillDtlStatus.OutStore);
                    saleOrderBillDtl.setStatus(BillConstant.BillDtlStatus.OutStore);
                }
                if (saleOrderBillDtl.getInQty().intValue() + saleOrderBillDtl.getReturnQty() == saleOrderBillDtl.getQty().intValue()) {
                    saleOrderBillDtl.setInStatus(BillConstant.BillDtlStatus.OutStore);
                    saleOrderBillDtl.setStatus(BillConstant.BillDtlStatus.InStore);
                }
                totReturnQty += saleOrderBillDtl.getReturnQty();
                totPrice += saleOrderBillDtl.getActPrice() * saleOrderBillDtl.getReturnQty();
                saleOrderReturnBillDtl.setStockVal(saleOrderBillDtl.getStockVal());
                saleOrderReturnBillDtl.setOutQty(Long.parseLong(saleOrderBillDtl.getOutQty().toString()));
                saleOrderReturnBillDtl.setOutVal(saleOrderBillDtl.getOutVal());
                saleOrderReturnBillDtl.setQty(Long.parseLong(saleOrderBillDtl.getReturnQty() + ""));
                saleOrderReturnBillDtl.setInVal(saleOrderBillDtl.getInVal());
                saleOrderReturnBillDtl.setInQty(Long.parseLong(saleOrderBillDtl.getInQty().toString()));
                saleOrderReturnBillDtl.setOutStatus(saleOrderBillDtl.getOutStatus());
                saleOrderReturnBillDtl.setInStatus(saleOrderBillDtl.getInStatus());
                saleOrderReturnBillDtl.setStatus(saleOrderBillDtl.getStatus());
                saleOrderReturnBillDtl.setActPrice(saleOrderBillDtl.getActPrice());
                saleOrderReturnBillDtl.setActQty(saleOrderBillDtl.getActQty());
                saleOrderReturnBillDtl.setBarcode(saleOrderBillDtl.getBarcode());
                saleOrderReturnBillDtl.setId(new GuidCreator().toString());
                saleOrderReturnBillDtl.setBillId(billNo);
                saleOrderReturnBillDtl.setBillNo(billNo);
                saleOrderReturnBillDtl.setColorId(saleOrderBillDtl.getColorId());
                saleOrderReturnBillDtl.setColorName(saleOrderBillDtl.getColorName());
                saleOrderReturnBillDtl.setManualQty(saleOrderBillDtl.getManualQty());
                saleOrderReturnBillDtl.setPreManualQty(saleOrderBillDtl.getPreManualQty());
                saleOrderReturnBillDtl.setPrice(saleOrderBillDtl.getPrice());
                saleOrderReturnBillDtl.setRemark(saleOrderBillDtl.getRemark());
                saleOrderReturnBillDtl.setScanQty(saleOrderBillDtl.getScanQty());
                saleOrderReturnBillDtl.setSizeId(saleOrderBillDtl.getSizeId());
                saleOrderReturnBillDtl.setSizeName(saleOrderBillDtl.getSizeName());
                saleOrderReturnBillDtl.setSku(saleOrderBillDtl.getSku());
                saleOrderReturnBillDtl.setStyleId(saleOrderBillDtl.getStyleId());
                saleOrderReturnBillDtl.setStyleName(saleOrderBillDtl.getStyleName());
                saleOrderReturnBillDtl.setTotActPrice(0 - (saleOrderBillDtl.getActPrice() * saleOrderBillDtl.getReturnQty()));
                saleOrderReturnBillDtl.setTotPrice(0 - saleOrderBillDtl.getTotPrice());
            }
            saleOrderReturnBillDtls.add(saleOrderReturnBillDtl);
        }
        saleOrderBill.setTotRetrunQty(totReturnQty);
        saleOrderReturnBill.setCustomer(saleOrderBill.getDestUnitId());
        saleOrderReturnBill.setCustomerName(saleOrderBill.getDestUnitName());
        saleOrderReturnBill.setBillDate(new Date());
        saleOrderReturnBill.setBillNo(billNo);
        saleOrderReturnBill.setId(billNo);
        saleOrderReturnBill.setActPrice(0D - Math.round(totPrice));
        saleOrderReturnBill.setActQty(saleOrderBill.getActQty());
        saleOrderReturnBill.setActSkuQty(saleOrderBill.getActSkuQty());
        saleOrderReturnBill.setBillType(saleOrderBill.getBillType());
        saleOrderReturnBill.setBusnissId(saleOrderBill.getBusnissId());
        saleOrderReturnBill.setDestId(saleOrderBill.getOrigId());
        saleOrderReturnBill.setDestAddr(saleOrderBill.getOrigAddr());
        saleOrderReturnBill.setDestName(saleOrderBill.getOrigName());
        saleOrderReturnBill.setDestUnitId(saleOrderBill.getOrigUnitId());
        saleOrderReturnBill.setDestUnitName(saleOrderBill.getOrigUnitName());
        saleOrderReturnBill.setOutStatus(saleOrderBill.getOutStatus());
        saleOrderReturnBill.setOrigAddr(saleOrderBill.getDestAddr());
        saleOrderReturnBill.setOrigName(saleOrderBill.getDestName());
        saleOrderReturnBill.setOrigId(saleOrderBill.getDestId());
        saleOrderReturnBill.setOrigUnitId(saleOrderBill.getDestUnitId());
        saleOrderReturnBill.setOrigUnitName(saleOrderBill.getDestUnitName());
        saleOrderReturnBill.setOwnerId(saleOrderBill.getOwnerId());
        saleOrderReturnBill.setTotPrice(0D + Math.round(totPrice));
        saleOrderReturnBill.setPayPrice(0D - Math.round(totPrice));
        saleOrderReturnBill.setTotQty(Long.parseLong(totReturnQty + ""));
        saleOrderReturnBill.setReturnCode(code);
        saleOrderReturnBill.setCustomerType(saleOrderBill.getCustomerTypeId());
        saleOrderReturnBill.setDiscount(saleOrderBill.getDiscount());
        saleOrderReturnBill.setRemark(saleOrderBill.getId());
        saleOrderReturnBill.setSrcBillNo(saleOrderBill.getId());
        saleOrderReturnBill.setStatus(BillConstant.BillStatus.Enter);

    }

    /**
     * 销售订单单保存退货单
     */

    public static void saveReturnConsignmentBill(ConsignmentBill consignmentBill, List<ConsignmentBillDtl> billDtlByBillNo, List<SaleOrderReturnBillDtl> saleOrderReturnBillDtls, SaleOrderReturnBill saleOrderReturnBill, String billNo, String stau, String code) {
        Integer totReturnQty = 0;
        Double totPrice = 0D;
        Double actprice = 0D;
        Logger logger = LoggerFactory.getLogger(ConsignmentBill.class);
        /* int countsum=0;*/
        for (int i = 0; i < billDtlByBillNo.size(); i++) {
            SaleOrderReturnBillDtl saleOrderReturnBillDtl = new SaleOrderReturnBillDtl();
            ConsignmentBillDtl consignmentBillDtl = billDtlByBillNo.get(i);
            /*if(saleOrderBillDtl.getReturnQty()>0){*/
              /*  consignmentBillDtl.setReturnbillNo(billNo);code
                totReturnQty+=saleOrderBillDtl.getReturnQty();
                totPrice+=saleOrderBillDtl.getActPrice()*saleOrderBillDtl.getReturnQty();*/
            saleOrderReturnBillDtl.setStockVal(consignmentBillDtl.getStockVal());
            saleOrderReturnBillDtl.setOutQty(Long.parseLong(consignmentBillDtl.getOutQty().toString()));
            saleOrderReturnBillDtl.setOutVal(consignmentBillDtl.getOutVal());

            if (stau.equals("m")) {
                saleOrderReturnBillDtl.setQty(Long.parseLong(consignmentBillDtl.getReadysale() + ""));
                actprice += consignmentBillDtl.getReadysale() * consignmentBillDtl.getActPrice();
                saleOrderReturnBillDtl.setInQty(Long.parseLong(consignmentBillDtl.getReadysale() + ""));
                logger.error("寄存单：" + consignmentBill.getBillNo() + "-SKU-" + consignmentBillDtl.getSku() + "-原始数量" + consignmentBillDtl.getReadysale());
                logger.error("寄存单：" + consignmentBill.getBillNo() + "-SKU-" + consignmentBillDtl.getSku() + "-转退款数量" + saleOrderReturnBillDtl.getQty() + "-转换退单单号" + billNo);
            } else if (stau.equals("q")) {
                //saleOrderReturnBillDtl.setQty(Long.parseLong((consignmentBillDtl.getQty()-consignmentBillDtl.getSale())+""));
                saleOrderReturnBillDtl.setQty(Long.parseLong(consignmentBillDtl.getOutQty() - consignmentBillDtl.getBeforeoutQty() + ""));
                //saleOrderReturnBillDtl.setOutQty(Long.parseLong((consignmentBillDtl.getQty()-consignmentBillDtl.getSale())+""));
                saleOrderReturnBillDtl.setInQty(Long.parseLong(consignmentBillDtl.getOutQty() - consignmentBillDtl.getBeforeoutQty() + ""));
                logger.error("寄存单：" + consignmentBill.getBillNo() + "-SKU-" + consignmentBillDtl.getSku() + "-原始数量" + (consignmentBillDtl.getOutQty() - consignmentBillDtl.getBeforeoutQty()));
                logger.error("寄存单：" + consignmentBill.getBillNo() + "-SKU-" + consignmentBillDtl.getSku() + "-转退货数量" + saleOrderReturnBillDtl.getQty() + "-转换退单单号" + billNo);

                // saleOrderReturnBillDtl.setInQty(Long.parseLong((consignmentBillDtl.getQty()-consignmentBillDtl.getSale())+""));
            }
            totReturnQty += Integer.parseInt(saleOrderReturnBillDtl.getQty() + "");
            saleOrderReturnBillDtl.setInVal(consignmentBillDtl.getInVal());
            //saleOrderReturnBillDtl.setInQty(0L);
            saleOrderReturnBillDtl.setDiscount(consignmentBill.getDiscount());
            saleOrderReturnBillDtl.setOutStatus(consignmentBillDtl.getOutStatus());
            saleOrderReturnBillDtl.setInStatus(consignmentBillDtl.getInStatus());
            saleOrderReturnBillDtl.setStatus(consignmentBillDtl.getStatus());
            saleOrderReturnBillDtl.setActPrice(consignmentBillDtl.getActPrice());
            saleOrderReturnBillDtl.setActQty(saleOrderReturnBillDtl.getQty());
            saleOrderReturnBillDtl.setBarcode(consignmentBillDtl.getBarcode());
            saleOrderReturnBillDtl.setId(new GuidCreator().toString());
            saleOrderReturnBillDtl.setBillId(billNo);
            saleOrderReturnBillDtl.setBillNo(billNo);
            saleOrderReturnBillDtl.setColorId(consignmentBillDtl.getColorId());
            saleOrderReturnBillDtl.setColorName(consignmentBillDtl.getColorName());
            saleOrderReturnBillDtl.setManualQty(consignmentBillDtl.getManualQty());
            saleOrderReturnBillDtl.setPreManualQty(consignmentBillDtl.getPreManualQty());
            saleOrderReturnBillDtl.setPrice(consignmentBillDtl.getPrice());
            saleOrderReturnBillDtl.setRemark(consignmentBillDtl.getRemark());
            saleOrderReturnBillDtl.setScanQty(consignmentBillDtl.getScanQty());
            saleOrderReturnBillDtl.setSizeId(consignmentBillDtl.getSizeId());
            saleOrderReturnBillDtl.setSizeName(consignmentBillDtl.getSizeName());
            saleOrderReturnBillDtl.setSku(consignmentBillDtl.getSku());
            saleOrderReturnBillDtl.setStyleId(consignmentBillDtl.getStyleId());
            saleOrderReturnBillDtl.setStyleName(consignmentBillDtl.getStyleName());
            if (stau.equals("m")) {
                saleOrderReturnBillDtl.setTotActPrice(0D - saleOrderReturnBillDtl.getActQty() * saleOrderReturnBillDtl.getActPrice());
                saleOrderReturnBillDtl.setTotPrice(0D - saleOrderReturnBillDtl.getActQty() * saleOrderReturnBillDtl.getPrice());
            } else if (stau.equals("q")) {
                saleOrderReturnBillDtl.setActPrice(0D);
                saleOrderReturnBillDtl.setTotActPrice(0D);
                saleOrderReturnBillDtl.setTotPrice(0D);
            }

            //countsum+=consignmentBillDtl.getReadysale();
            /* }*/
            saleOrderReturnBillDtls.add(saleOrderReturnBillDtl);
        }
        /*saleOrderBill.setTotRetrunQty(totReturnQty);*/
        saleOrderReturnBill.setCustomer(consignmentBill.getCustomer());
        saleOrderReturnBill.setDiscount(consignmentBill.getDiscount());
        saleOrderReturnBill.setCustomerName(consignmentBill.getCustomerName());
        saleOrderReturnBill.setBillDate(new Date());
        saleOrderReturnBill.setBillNo(billNo);
        saleOrderReturnBill.setId(billNo);
        if (stau.equals("m")) {
            saleOrderReturnBill.setActPrice(0D - Math.round(actprice));
            saleOrderReturnBill.setPayPrice(0D - Math.round(actprice));
            logger.error("价格退货  退货单：" + billNo);
        }
        if (stau.equals("q")) {
            saleOrderReturnBill.setActPrice(0D);
            saleOrderReturnBill.setPayPrice(0D);
            logger.error("价格退货  退货单：" + billNo);
        }

        saleOrderReturnBill.setActQty(consignmentBill.getActQty());
        saleOrderReturnBill.setActSkuQty(consignmentBill.getActSkuQty());
        saleOrderReturnBill.setBillType(consignmentBill.getBillType());
        saleOrderReturnBill.setBusnissId(consignmentBill.getBusnissId());
        saleOrderReturnBill.setDestId(consignmentBill.getDestId());
        saleOrderReturnBill.setDestAddr(consignmentBill.getOrigAddr());
        saleOrderReturnBill.setDestName(consignmentBill.getOrigName());
        saleOrderReturnBill.setDestUnitId(consignmentBill.getOrigUnitId());
        saleOrderReturnBill.setDestUnitName(consignmentBill.getOrigUnitName());
        saleOrderReturnBill.setOutStatus(BillConstant.BillInOutStatus.OutStore);
        saleOrderReturnBill.setOrigAddr(consignmentBill.getDestAddr());
        saleOrderReturnBill.setOrigName(consignmentBill.getDestName());
        saleOrderReturnBill.setOrigId(consignmentBill.getDestId());
        saleOrderReturnBill.setOrigUnitId(consignmentBill.getOrigUnitId());
        saleOrderReturnBill.setOrigUnitName(consignmentBill.getOrigUnitName());
        saleOrderReturnBill.setOwnerId(consignmentBill.getOwnerId());
        saleOrderReturnBill.setTotPrice(totPrice);
        saleOrderReturnBill.setOprId(consignmentBill.getOprId());
        String TotInQty = billDtlByBillNo.size() + "";
        saleOrderReturnBill.setTotInQty(Long.parseLong(TotInQty));
        /*  saleOrderReturnBill.setPayPrice(totPrice);*/
        saleOrderReturnBill.setTotQty(Long.parseLong(totReturnQty + ""));
        saleOrderReturnBill.setReturnCode(code);
        saleOrderReturnBill.setCustomerType(consignmentBill.getCustomerType());
        if (stau.equals("m")) {
            saleOrderReturnBill.setRemark(consignmentBill.getId() + "退款");
        }
        if (stau.equals("q")) {
            saleOrderReturnBill.setRemark(consignmentBill.getId() + "退货");
        }
        saleOrderReturnBill.setSrcBillNo(consignmentBill.getId());
        saleOrderReturnBill.setStatus(BillConstant.BillStatus.Enter);

    }

    /**
     * 销售订单单上传(出库任务)更新单据信息（客户端上传）
     */
    public static void covertToSaleOrderBusiness(SaleOrderBill saleOrderBill, List<SaleOrderBillDtl> saleOrderBillDtlList, Business bus) {
        Map<String, SaleOrderBillDtl> saleOrderBillDtlMap = new HashMap<>();
        List<BillRecord> billRecordList = new ArrayList<>();
        for (SaleOrderBillDtl dtl : saleOrderBillDtlList) {
            saleOrderBillDtlMap.put(dtl.getSku(), dtl);
        }
        Double totOutPrice = 0d;
        Map<String, BusinessDtl> businessDtlMap = new HashMap<>();
        for (BusinessDtl dtl : bus.getDtlList()) {
            SaleOrderBillDtl saleOrderBillDtl = saleOrderBillDtlMap.get(dtl.getSku());
            saleOrderBillDtl.setOutQty(saleOrderBillDtl.getOutQty() + (int) dtl.getQty());
            saleOrderBillDtl.setOutVal(saleOrderBillDtl.getOutQty() * saleOrderBillDtl.getActPrice());
            saleOrderBillDtl.setOutStatus(BillConstant.BillDtlStatus.Outing);
            if (saleOrderBillDtl.getOutQty().intValue() + saleOrderBillDtl.getReturnQty() == saleOrderBillDtl.getQty().intValue()) {
                saleOrderBillDtl.setOutStatus(BillConstant.BillDtlStatus.OutStore);
                saleOrderBillDtl.setStatus(BillConstant.BillDtlStatus.OutStore);
            }
            dtl.setPreVal(0D);
            businessDtlMap.put(dtl.getSku(), dtl);
            totOutPrice += saleOrderBillDtl.getOutVal();
        }
        saleOrderBill.setTotOutQty(saleOrderBill.getTotOutQty() + bus.getTotEpc());
        saleOrderBill.setTotOutVal(saleOrderBill.getTotOutVal() + totOutPrice);
        saleOrderBill.setStatus(BillConstant.BillStatus.Doing);
        saleOrderBill.setOutStatus(BillConstant.BillInOutStatus.Outing);
        if (saleOrderBill.getTotOutQty().intValue() + saleOrderBill.getTotRetrunQty() == saleOrderBill.getTotQty().intValue()) {
            saleOrderBill.setOutStatus(BillConstant.BillInOutStatus.OutStore);
            if (saleOrderBill.getCustomerTypeId().equals(BillConstant.customerType.Customer)) {
                saleOrderBill.setStatus(BillConstant.BillStatus.End);
            }
        }

        //出库填写EpcStock中成本价格
        List<String> codeStrList = new ArrayList<>();
        Map<String, Record> recordMap = new HashMap<>();
        for (Record r : bus.getRecordList()) {
            codeStrList.add(r.getCode());
            recordMap.put(r.getCode(), r);
        }
        List<EpcStock> epcStockList = epcStockService.findEpcByCodes(TaskUtil.getSqlStrByList(codeStrList, EpcStock.class, "code"));
        Double totPreVal = 0D;
        for (EpcStock s : epcStockList) {
            BusinessDtl dtl = businessDtlMap.get(s.getSku());
            Double preVal = CommonUtil.isBlank(s.getStockPrice()) ? 0D : s.getStockPrice();
            dtl.setPreVal(dtl.getPreVal() + preVal);
            totPreVal += preVal;
            Record r = recordMap.get(s.getCode());
            r.setPrice(preVal);
            SaleOrderBillDtl saleOrderBillDtl = saleOrderBillDtlMap.get(s.getSku());
            BillRecord billRecord = new BillRecord(saleOrderBillDtl.getBillNo() + "-" + s.getCode(), s.getCode(), saleOrderBillDtl.getBillNo(), saleOrderBillDtl.getSku());
            billRecordList.add(billRecord);
            saleOrderBillDtl.setStockVal(saleOrderBillDtl.getStockVal() + preVal);
            //设置利润
            if (saleOrderBillDtl.getOutVal().intValue() == 0) {
                saleOrderBillDtl.setProfit(0D);
                saleOrderBillDtl.setProfitRate(0D);

            } else {
                saleOrderBillDtl.setProfit(saleOrderBillDtl.getOutVal() - saleOrderBillDtl.getStockVal());
                if (saleOrderBillDtl.getStockVal().intValue() != 0) {
                    saleOrderBillDtl.setProfitRate(Double.parseDouble(CommonUtil.getDecimal(saleOrderBillDtl.getProfit() / saleOrderBillDtl.getStockVal() * 100, "######0.00")));
                } else {
                    saleOrderBillDtl.setProfitRate(0d);
                }
            }

        }
        bus.setTotPreVal(totPreVal);
        bus.setDestId(saleOrderBill.getDestId());
        bus.setDestUnitId(saleOrderBill.getDestUnitId());
        bus.setOrigId(saleOrderBill.getOrigId());
        bus.setOrigUnitId(saleOrderBill.getOrigUnitId());
        saleOrderBill.setBillRecordList(billRecordList);
        saleOrderBill.setTotStockVal(totPreVal);
        saleOrderBill.setTotStockVal(saleOrderBill.getTotStockVal() + totPreVal);
        saleOrderBill.setProfit(saleOrderBill.getTotOutVal() - totPreVal);
        //设置利润
        saleOrderBill.setProfitRate(Double.parseDouble(CommonUtil.getDecimal(totPreVal / saleOrderBill.getTotOutVal() * 100, "######0.00")));
        bus.setRecordList(new ArrayList<>(recordMap.values()));

    }

    /**
     * 销售订单单上传(入库任务)更新单据信息（客户端上传）
     */
    public static void covertToSaleOrderBusinessIn(SaleOrderBill saleOrderBill, List<SaleOrderBillDtl> saleOrderBillDtlList, Business bus) {
        Map<String, SaleOrderBillDtl> saleOrderBillDtlMap = new HashMap<>();
        for (SaleOrderBillDtl dtl : saleOrderBillDtlList) {
            saleOrderBillDtlMap.put(dtl.getSku(), dtl);
        }
        Double totInPrice = 0d;
        Map<String, BusinessDtl> businessDtlMap = new HashMap<>();
        for (BusinessDtl dtl : bus.getDtlList()) {
            SaleOrderBillDtl saleOrderBillDtl = saleOrderBillDtlMap.get(dtl.getSku());
            saleOrderBillDtl.setInQty(saleOrderBillDtl.getInQty() + (int) dtl.getQty());
            saleOrderBillDtl.setInVal(saleOrderBillDtl.getInQty() * saleOrderBillDtl.getActPrice());
            saleOrderBillDtl.setInStatus(BillConstant.BillDtlStatus.Ining);
            if (saleOrderBillDtl.getInQty().intValue() + saleOrderBillDtl.getReturnQty() == saleOrderBillDtl.getQty().intValue()) {
                saleOrderBillDtl.setInStatus(BillConstant.BillDtlStatus.InStore);
                saleOrderBillDtl.setStatus(BillConstant.BillDtlStatus.InStore);
            }
            businessDtlMap.put(dtl.getSku(), dtl);
            totInPrice += saleOrderBillDtl.getInVal();
            dtl.setPreVal(saleOrderBillDtl.getInVal());
        }
        //把出入库任务Record中的唯一码转为单据中的billRecord
        List<BillRecord> billRecordList = new ArrayList<>();
        List<String> codeStrList = new ArrayList<>();
        for (Record r : bus.getRecordList()) {
            codeStrList.add(r.getCode());
        }
        List<EpcStock> epcStockList = epcStockService.findEpcByCodes(TaskUtil.getSqlStrByList(codeStrList, EpcStock.class, "code"));
        for (EpcStock s : epcStockList) {
            BillRecord billRecord = new BillRecord(saleOrderBill.getBillNo() + "-" + s.getCode(), s.getCode(), saleOrderBill.getBillNo(), s.getSku());
            billRecordList.add(billRecord);
        }
        saleOrderBill.setBillRecordList(billRecordList);
        saleOrderBill.setTotInQty(saleOrderBill.getTotInQty() + bus.getTotEpc());
        saleOrderBill.setTotInVal(saleOrderBill.getTotInVal() + totInPrice);
        saleOrderBill.setStatus(BillConstant.BillStatus.Doing);
        saleOrderBill.setInStatus(BillConstant.BillInOutStatus.Ining);
        if (saleOrderBill.getTotInQty().intValue() + saleOrderBill.getTotRetrunQty() == saleOrderBill.getTotQty().intValue()) {
            saleOrderBill.setInStatus(BillConstant.BillInOutStatus.InStore);
            saleOrderBill.setStatus(BillConstant.BillStatus.End);
        }
        bus.setTotPreVal(totInPrice);
        bus.setDestId(saleOrderBill.getDestId());
        bus.setDestUnitId(saleOrderBill.getDestUnitId());
        bus.setOrigId(saleOrderBill.getOrigId());
        bus.setOrigUnitId(saleOrderBill.getOrigUnitId());
        //销售出库更新任务成本
        Double totPreVal = 0D;
        for (Record r : bus.getRecordList()) {
            BusinessDtl dtl = businessDtlMap.get(r.getSku());
            SaleOrderBillDtl saleOrderBillDtl = saleOrderBillDtlMap.get(r.getSku());
            r.setPrice(saleOrderBillDtl.getActPrice());
            totPreVal += r.getPrice();
            dtl.setPreVal(dtl.getQty() * saleOrderBillDtl.getActPrice());
        }
        bus.setTotPreVal(totPreVal);


    }

    /**
     * 转换销售退货单（保存调用）
     */
    public static void convertToSaleOrderReturnBill(SaleOrderReturnBill bill, List<SaleOrderReturnBillDtl> saleOrderReturnBillDtls, User user) {
        if (CommonUtil.isNotBlank(user)) {
            bill.setOprId(user.getCode());
        }
        Long totQty = 0L;
        Long actQty = 0L;
        Long outQty = 0L;
        Long inQty = 0L;
        Double totPrice = 0D;
        Double totActPrice = 0D;
        Double totOutVal = 0D;
        Double totInVal = 0D;

        List<BillRecord> billRecordList = new ArrayList<>();
        for (SaleOrderReturnBillDtl detail : saleOrderReturnBillDtls) {

            /*从EpcStock中取出三字段信息 add by Anna*/
            /*List<EpcStock> epcStockList = epcStockService.findSaleReturnFilterByOriginIdDtl(detail.getUniqueCodes(), bill.getDestId());
            Map<String,EpcStock> epcStockMap = new HashMap<>()

            EpcStock epcStock;
            String originBillNo;
            Date lastSaleTime;
            Long saleCycle;
            if (epcStockList.size() == 0 || epcStockList.isEmpty()) {
                originBillNo = null;
                lastSaleTime = null;
                saleCycle = null;
            } else {
                Long cycle = ((new Date()).getTime() - epcStockList.get(0).getLastSaleTime().getTime()) / 1000 / 60 / 60 / 24;
                epcStockList.get(0).setSaleCycle(cycle);
                epcStock = epcStockList.get(0);
                originBillNo = epcStock.getOriginBillNo();
                lastSaleTime = epcStock.getLastSaleTime();
                saleCycle = epcStock.getSaleCycle();
            }*/
            /* end */

            detail.setId(new GuidCreator().toString());
            detail.setBillId(bill.getId());
            detail.setBillNo(bill.getBillNo());
            totQty += detail.getQty();
            detail.setActQty(detail.getQty());
            actQty += detail.getActQty();
            totPrice += -1 * Math.abs(detail.getPrice() * detail.getQty());
            totActPrice += -1 * detail.getActPrice() * detail.getQty();
            outQty += detail.getOutQty();
            inQty += detail.getInQty();
            totOutVal = outQty * detail.getActPrice();
            totInVal = inQty * detail.getActPrice();
            if (CommonUtil.isNotBlank(detail.getUniqueCodes())) {
                for (String code : detail.getUniqueCodes().split(",")) {
                    BillRecord billRecord = new BillRecord(detail.getBillNo() + "-" + code, code, detail.getBillNo(), detail.getSku());
                    List<EpcStock> epcStockList = epcStockService.findSaleReturnFilterByOriginIdDtl(code, bill.getDestId(), null);
                    EpcStock epcStock;
                    String originBillNo;
                    Date lastSaleTime;
                    Long saleCycle;
                    if (!(epcStockList.size() == 0 || epcStockList.isEmpty())) {
                        Long cycle = ((new Date()).getTime() - epcStockList.get(0).getLastSaleTime().getTime()) / 1000 / 60 / 60 / 24;
                        epcStockList.get(0).setSaleCycle(cycle);
                        epcStock = epcStockList.get(0);
                        originBillNo = epcStock.getOriginBillNo();
                        lastSaleTime = epcStock.getLastSaleTime();
                        saleCycle = epcStock.getSaleCycle();
                        billRecord.setOriginBillNo(originBillNo);
                        billRecord.setLastSaleTime(lastSaleTime);
                        billRecord.setSaleCycle(saleCycle);
                    }
                    billRecordList.add(billRecord);
                }
            }

        }
        bill.setBillRecordList(billRecordList);
        if (CommonUtil.isBlank(bill.getOwnerId())) {
            bill.setOwnerId(user.getOwnerId());
        }
        if (CommonUtil.isBlank(bill.getStatus())) {
            bill.setStatus(BillConstant.BillStatus.Enter);
        }
//            供应商、仓库、客户
        Unit dest = CacheManager.getUnitByCode(bill.getDestId());
        bill.setDestName(dest.getName());
        bill.setDestUnitId(dest.getOwnerId());
        if(CommonUtil.isNotBlank(dest.getOwnerId())){
            Unit destUnit = CacheManager.getUnitByCode(dest.getOwnerId());
            bill.setDestUnitName(destUnit.getName());
        }

        if(CommonUtil.isNotBlank(bill.getOrigId())){
            Unit origUnit = CacheManager.getUnitByCode(bill.getOrigId());
            bill.setOrigName(origUnit.getName());
        }
        bill.setTotQty(totQty);
        bill.setTotPrice(totPrice);
//        bill.setActPrice(totActPrice);
        bill.setActQty(actQty);
        bill.setTotOutQty(outQty);
        bill.setTotInQty(inQty);
        bill.setTotInVal(totInVal);
        bill.setTotOutVal(totOutVal);
        if (CommonUtil.isNotBlank(bill.getBusnissId())) {
            bill.setBusnissName(CacheManager.getUserById(bill.getBusnissId()).getName());
        }
    }

    /**
     * 转换寄售单（保存调用）
     */
    public static void convertToConsignmentBillBill(ConsignmentBill bill, List<ConsignmentBillDtl> consignmentBillDtls, User user) {

        if (CommonUtil.isNotBlank(user)) {
            bill.setOprId(user.getCode());
        }

        Long totQty = 0L;
        Long actQty = 0L;
        Long outQty = 0L;
        Long inQty = 0L;
        Double totPrice = 0D;
        Double totActPrice = 0D;
        Double totOutVal = 0D;
        Double totInVal = 0D;
        List<BillRecord> billRecordList = new ArrayList<>();
        for (ConsignmentBillDtl detail : consignmentBillDtls) {

            detail.setId(new GuidCreator().toString());
            detail.setBillId(bill.getId());
            detail.setBillNo(bill.getBillNo());
            detail.setInStockType(BillConstant.InStockType.Consignment);
            if (CommonUtil.isBlank(detail.getReadysale())) {
                detail.setReadysale(0);
            }
            if (CommonUtil.isBlank(detail.getOutMonyQty())) {
                detail.setOutMonyQty(0);
            }
            totQty += detail.getQty();
            detail.setActQty(detail.getQty());
            actQty += detail.getActQty();
            totPrice += -1 * Math.abs(detail.getPrice() * detail.getQty());
            totActPrice += -1 * detail.getActPrice() * detail.getQty();
            outQty += detail.getOutQty();
            inQty += detail.getInQty();
            totOutVal = outQty * detail.getActPrice();
            totInVal = inQty * detail.getActPrice();
            if (CommonUtil.isNotBlank(detail.getUniqueCodes())) {
                for (String code : detail.getUniqueCodes().split(",")) {
                    BillRecord billRecord = new BillRecord(detail.getBillNo() + "-" + code, code, detail.getBillNo(), detail.getSku());
                    billRecordList.add(billRecord);
                }
            }

        }
        bill.setBillRecordList(billRecordList);
        if (CommonUtil.isBlank(bill.getOwnerId())) {
            bill.setOwnerId(user.getOwnerId());
        }
        if (CommonUtil.isBlank(bill.getStatus())) {
            bill.setStatus(BillConstant.BillStatus.Enter);
        }
//            供应商、仓库、客户
        Unit dest = CacheManager.getUnitByCode(bill.getDestId());
        bill.setDestName(dest.getName());
        bill.setDestUnitId(dest.getOwnerId());
        Unit destUnit = CacheManager.getUnitByCode(dest.getOwnerId());
        bill.setDestUnitName(destUnit.getName());
        bill.setTotQty(totQty);
        bill.setTotPrice(totPrice);
//        bill.setActPrice(totActPrice);
        bill.setActQty(actQty);
        bill.setTotOutQty(outQty);
        bill.setTotInQty(inQty);
        bill.setTotInVal(totInVal);
        bill.setTotOutVal(totOutVal);
        if (CommonUtil.isNotBlank(bill.getBusnissId())) {
            bill.setBusnissName(CacheManager.getUserById(bill.getBusnissId()).getName());
        }
    }

    /**
     * 寄售单上传入库任务
     */
    public static void convertConsignmentBillBillIn(ConsignmentBill bill, List<ConsignmentBillDtl> ConsignmentBillDtl, Business bus) {
        Map<String, ConsignmentBillDtl> consignmentBillDtlMap = new HashMap<>();
        for (ConsignmentBillDtl dtl : ConsignmentBillDtl) {
            consignmentBillDtlMap.put(dtl.getSku(), dtl);
        }
        List<String> codeStrList = new ArrayList<>();
        Map<String, BusinessDtl> businessDtlMap = new HashMap<>();
        Map<String, Record> recordMap = new HashMap<>();
        for (Record record : bus.getRecordList()) {
            codeStrList.add(record.getCode());
            recordMap.put(record.getCode(), record);
        }

        Double totPrice = 0d;
        for (BusinessDtl dtl : bus.getDtlList()) {
            ConsignmentBillDtl consignmentBillDtl = consignmentBillDtlMap.get(dtl.getSku());
            consignmentBillDtl.setInQty(consignmentBillDtl.getInQty() + (int) dtl.getQty());
            consignmentBillDtl.setInVal(consignmentBillDtl.getInVal() * consignmentBillDtl.getPrice());
            consignmentBillDtl.setInStatus(BillConstant.BillDtlStatus.Ining);
            if (consignmentBillDtl.getInQty().intValue() == consignmentBillDtl.getQty().intValue()) {
                consignmentBillDtl.setInStatus(BillConstant.BillDtlStatus.InStore);
                consignmentBillDtl.setStatus(BillConstant.BillDtlStatus.InStore);
            }
            totPrice += consignmentBillDtl.getInVal();
            businessDtlMap.put(dtl.getSku(), dtl);
            dtl.setPreVal(0D);
        }
        //把出入库任务Record中的唯一码转为单据中的billRecord
        List<BillRecord> billRecordList = new ArrayList<>();
        List<EpcStock> epcStockList = epcStockService.findEpcByCodes(TaskUtil.getSqlStrByList(codeStrList, EpcStock.class, "code"));
        for (EpcStock s : epcStockList) {
            BillRecord billRecord = new BillRecord(bill.getBillNo() + "-" + s.getCode(), s.getCode(), bill.getBillNo(), s.getSku());
            billRecordList.add(billRecord);
        }
        bill.setBillRecordList(billRecordList);
        bill.setTotInQty(bill.getTotInQty() + bus.getTotEpc());
        bill.setTotInVal(bill.getTotInVal() + totPrice);
        bill.setStatus(BillConstant.BillStatus.Doing);
        bill.setInStatus(BillConstant.BillInOutStatus.Ining);
        if (bill.getTotInQty().intValue() == bill.getTotQty().intValue()) {
            bill.setInStatus(BillConstant.BillInOutStatus.InStore);
            bill.setStatus(BillConstant.BillStatus.End);
        }
        bus.setDestId(bill.getDestId());
        bus.setDestUnitId(bill.getDestUnitId());
        bus.setOrigId(bill.getOrigId());
        bus.setOrigUnitId(bill.getOrigUnitId());
    }

    /**
     * 转换调拨申请单（保存调用）
     */
    public static void covertToTransferOrderBill(TransferOrderBill transferOrderBill, List<TransferOrderBillDtl> transferOrderBillDtlList, User curUser) {
        if (CommonUtil.isNotBlank(curUser)) {
            transferOrderBill.setOprId(curUser.getCode());
        }
        Long totQty = 0L;       //单据数量
        Long totOutQty = 0L;    //出库总数量
        Double totOutVal = 0D;//出库总金额
        Long totInQty = 0L;    //入库总数量
        Double totInVal = 0D;  //入库总金额
        List<BillRecord> billRecordList = new ArrayList<>();
        for (TransferOrderBillDtl dtl : transferOrderBillDtlList) {
            dtl.setId(new GuidCreator().toString());
            dtl.setBillId(transferOrderBill.getId());
            dtl.setBillNo(transferOrderBill.getBillNo());
            dtl.setActQty(dtl.getQty());
            totQty += dtl.getQty();
            totOutQty += dtl.getOutQty();
            totInQty += dtl.getInQty();
            totOutVal = totOutQty * dtl.getPrice();
            totInVal = totInQty * dtl.getPrice();
            if (CommonUtil.isNotBlank(dtl.getUniqueCodes())) {
                for (String code : dtl.getUniqueCodes().split(",")) {
                    if(CommonUtil.isNotBlank(code)) {
                        BillRecord billRecord = new BillRecord(dtl.getBillNo() + "-" + code, code, dtl.getBillNo(), dtl.getSku());
                        billRecordList.add(billRecord);
                    }
                }
            }
        }
        transferOrderBill.setBillRecordList(billRecordList);
        transferOrderBill.setOwnerId(curUser.getOwnerId());
        if (CommonUtil.isBlank(transferOrderBill.getStatus())) {
            transferOrderBill.setStatus(BillConstant.BillStatus.Enter);
        }
        Unit origUnit = CacheManager.getUnitByCode(transferOrderBill.getOrigUnitId());
        transferOrderBill.setOrigUnitName(origUnit.getName());
        Unit orig = CacheManager.getUnitByCode(transferOrderBill.getOrigId());
        transferOrderBill.setOrigName(orig.getName());
        Unit destUnit = CacheManager.getUnitByCode(transferOrderBill.getDestUnitId());
        transferOrderBill.setDestUnitName(destUnit.getName());
        Unit dest = CacheManager.getUnitByCode(transferOrderBill.getDestId());
        transferOrderBill.setDestName(dest.getName());

        transferOrderBill.setTotQty(totQty);
        transferOrderBill.setTotOutQty(totOutQty);
        transferOrderBill.setTotOutVal(totOutVal);
        transferOrderBill.setTotInQty(totInQty);
        transferOrderBill.setTotInVal(totInVal);
    }

    /***
     * 采购单转入库单据（客户端查询调用）
     */
    public static List<Bill> convertPurchaseToBill(List<PurchaseOrderBill> purchaseOrderBillList, int token) {
        List<Bill> billList = new ArrayList<>();
        for (PurchaseOrderBill p : purchaseOrderBillList) {
            Bill bill = new Bill();
            bill.setId(p.getId());
            bill.setBillNo(p.getBillNo());
            bill.setBillDate(p.getBillDate());
            bill.setOwnerId(p.getOwnerId());
            bill.setOrigId(p.getOrigId());
            bill.setDestId(p.getDestId());
            bill.setDestUnitId(p.getDestUnitId());
            bill.setOrigUnitId(p.getOrigUnitId());
            bill.setOrigUnitName(p.getOrigUnitName());
            bill.setTotQty(p.getTotQty());
            bill.setType(token);
            bill.setBillType("" + token);
            bill.setScanQty(p.getTotInQty());
            billList.add(bill);
        }
        return billList;
    }

    /***
     * 销售订单转出入库单据（客户端查询调用）
     */
    public static List<Bill> convertsaleOrderToBill(List<SaleOrderBill> saleOrderBillList, int token) {
        List<Bill> billList = new ArrayList<>();
        for (SaleOrderBill s : saleOrderBillList) {
            Bill bill = new Bill();
            bill.setId(s.getId());
            bill.setBillNo(s.getBillNo());
            bill.setBillDate(s.getBillDate());
            bill.setOwnerId(s.getOwnerId());
            bill.setOrigId(s.getOrigId());
            bill.setDestId(s.getDestId());
            bill.setDestUnitId(s.getDestUnitId());
            bill.setOrigUnitId(s.getOrigUnitId());
            bill.setOrigUnitName(s.getOrigUnitName());
            bill.setTotQty(s.getTotQty());
            bill.setType(token);
            bill.setBillType("" + token);
            if (Constant.Token.Storage_Outbound == token) {
                bill.setScanQty(s.getTotOutQty());
            } else if (Constant.Token.Storage_Inbound_customer == token) {
                bill.setScanQty(s.getTotInQty());
            }
            billList.add(bill);
        }
        return billList;
    }

    /**
     * 销售退货单据转出库卡单据（客户端查询调用）
     */
    public static List<Bill> convertSaleOrderReturnToBill(List<SaleOrderReturnBill> saleOrderReturnBills, int token) {
        List<Bill> billList = new ArrayList<>();

        for (SaleOrderReturnBill saleOrderReturnBill : saleOrderReturnBills) {
            Bill bill = new Bill();
            bill.setId(saleOrderReturnBill.getId());
            bill.setBillNo(saleOrderReturnBill.getBillNo());
            bill.setBillDate(saleOrderReturnBill.getBillDate());
            bill.setOwnerId(saleOrderReturnBill.getOwnerId());
            bill.setOrigId(saleOrderReturnBill.getOrigId());
            bill.setDestId(saleOrderReturnBill.getDestId());
//			bill.setDestUnitId(saleOrderReturnBill.getDestUnitId());
            bill.setOrigUnitId(saleOrderReturnBill.getOrigUnitId());
            bill.setOrigUnitName(saleOrderReturnBill.getOrigUnitName());
            bill.setTotQty(saleOrderReturnBill.getTotQty());
            bill.setType(token);
            bill.setBillType("" + token);
            if (Constant.Token.Shop_Refund_Outbound == token)
                bill.setScanQty(saleOrderReturnBill.getTotOutQty());
            else if (Constant.Token.Storage_Refund_Inbound == token)
                bill.setScanQty(saleOrderReturnBill.getTotInQty());
            billList.add(bill);
        }
        return billList;
    }

    /**
     * 获取寄售单单据信息（客户端查询调用）
     */
    public static List<Bill> convertConsignmentBillBill(List<ConsignmentBill> consignmentBillList, int token) {
        List<Bill> billList = new ArrayList<>();

        for (ConsignmentBill consignmentBill : consignmentBillList) {
            Bill bill = new Bill();
            bill.setId(consignmentBill.getId());
            bill.setBillNo(consignmentBill.getBillNo());
            bill.setBillDate(consignmentBill.getBillDate());
            bill.setOwnerId(consignmentBill.getOwnerId());
            bill.setOrigId(consignmentBill.getOrigId());
            bill.setDestId(consignmentBill.getDestId());
            bill.setOrigUnitId(consignmentBill.getOrigUnitId());
            bill.setOrigUnitName(consignmentBill.getOrigUnitName());
            bill.setTotQty(consignmentBill.getTotQty());
            bill.setType(token);
            bill.setBillType("" + token);
            bill.setScanQty(consignmentBill.getTotInQty());
            billList.add(bill);
        }
        return billList;
    }

    /**
     * 采购退货单转出库单据（客户端查询调用）
     *
     * @param purchaseReturnBills
     * @param token
     * @return
     */
    public static List<Bill> convertPurchaseReturnToBill(List<PurchaseReturnBill> purchaseReturnBills, int token) {
        List<Bill> billList = new ArrayList<>();
        for (PurchaseReturnBill purchaseReturnBill : purchaseReturnBills) {
            Bill bill = new Bill();
            bill.setId(purchaseReturnBill.getId());
            bill.setId(purchaseReturnBill.getId());
            bill.setBillNo(purchaseReturnBill.getBillNo());
            bill.setBillDate(purchaseReturnBill.getBillDate());
            bill.setOwnerId(purchaseReturnBill.getOwnerId());
            bill.setOrigId(purchaseReturnBill.getOrigId());
            bill.setDestId(purchaseReturnBill.getDestId());
            bill.setDestUnitId(purchaseReturnBill.getDestUnitId());
            bill.setOrigUnitId(purchaseReturnBill.getOrigUnitId());
            bill.setOrigUnitName(purchaseReturnBill.getOrigUnitName());
            bill.setTotQty(purchaseReturnBill.getTotQty());
            bill.setType(token);
            bill.setBillType("" + token);
            bill.setScanQty(purchaseReturnBill.getTotOutQty());
            billList.add(bill);
        }
        return billList;
    }

    /***
     * 采购订单明细转入库单明细（客户端查询调用）
     */
    public static List<BillDtl> covertPurchaseDtlToBillDtl(List<PurchaseOrderBillDtl> purchaseOrderBillDtlList) {
        List<BillDtl> billDtlList = new ArrayList<>();
        for (PurchaseOrderBillDtl dtl : purchaseOrderBillDtlList) {
            BillDtl billDtl = new BillDtl();
            billDtl.setId(dtl.getId());
            billDtl.setScanQty((long) dtl.getInQty());
            billDtl.setBillId(dtl.getBillId());
            billDtl.setBillNo(dtl.getBillNo());
            billDtl.setSku(dtl.getSku());
            billDtl.setStyleId(dtl.getStyleId());
            billDtl.setColorId(dtl.getColorId());
            billDtl.setSizeId(dtl.getSizeId());
            billDtl.setPrice(dtl.getPrice());
            billDtl.setQty(dtl.getQty());
            billDtlList.add(billDtl);

        }
        return billDtlList;
    }

    /***
     * 销售订单明细转出入库单据明细（客户端查询调用）
     */
    public static List<BillDtl> covertSaleOrderDtlToBillDtl(List<SaleOrderBillDtl> saleOrderBillDtlList, int token) {
        List<BillDtl> billDtlList = new ArrayList<>();
        for (SaleOrderBillDtl dtl : saleOrderBillDtlList) {
            BillDtl billDtl = new BillDtl();
            billDtl.setId(dtl.getId());
            if (Constant.Token.Storage_Outbound == token) {
                billDtl.setScanQty((long) dtl.getOutQty());
            } else {
                billDtl.setScanQty((long) dtl.getInQty());
            }
            billDtl.setBillId(dtl.getBillId());
            billDtl.setBillNo(dtl.getBillNo());
            billDtl.setSku(dtl.getSku());
            billDtl.setStyleId(dtl.getStyleId());
            billDtl.setColorId(dtl.getColorId());
            billDtl.setSizeId(dtl.getSizeId());
            billDtl.setPrice(dtl.getPrice());
            billDtl.setQty(dtl.getQty());
            billDtlList.add(billDtl);
        }
        return billDtlList;
    }

    /**
     * 调拨申请明细转换单据明细（客户端查询）
     */
    public static List<BillDtl> convertTransferOrderDtlToBillDtl(List<TransferOrderBillDtl> transferOutOrderBillDtlList, int token) {
        List<BillDtl> billDtlList = new ArrayList<>();
        for (TransferOrderBillDtl dtl : transferOutOrderBillDtlList) {
            BillDtl billDtl = new BillDtl();
            billDtl.setId(dtl.getId());
            if (token == Constant.Token.Storage_Transfer_Outbound) {
                billDtl.setScanQty((long) dtl.getOutQty());
            } else {
                billDtl.setScanQty((long) dtl.getInQty());
            }
            billDtl.setBillId(dtl.getBillId());
            billDtl.setBillNo(dtl.getBillNo());
            billDtl.setSku(dtl.getSku());
            billDtl.setStyleId(dtl.getStyleId());
            billDtl.setColorId(dtl.getColorId());
            billDtl.setSizeId(dtl.getSizeId());
            billDtl.setPrice(dtl.getPrice());
            billDtl.setQty(dtl.getQty());
            billDtlList.add(billDtl);
        }
        return billDtlList;
    }

    /**
     * 采购退货明细转换单据明细（客户端查询）
     */
    public static List<BillDtl> convertPurchaseReturnDtlToBillDtl(List<PurchaseReturnBillDtl> purchaseReturnBillDtlList) {
        List<BillDtl> billDtlList = new ArrayList<>();
        for (PurchaseReturnBillDtl pr : purchaseReturnBillDtlList) {
            BillDtl billDtl = new BillDtl();
            billDtl.setId(pr.getId());
            billDtl.setScanQty(pr.getOutQty());
            billDtl.setBillId(pr.getBillId());
            billDtl.setBillNo(pr.getBillNo());
            billDtl.setSku(pr.getSku());
            billDtl.setStyleId(pr.getStyleId());
            billDtl.setColorId(pr.getColorId());
            billDtl.setSizeId(pr.getSizeId());
            billDtl.setPrice(pr.getPrice());
            billDtl.setQty(pr.getQty());
            billDtlList.add(billDtl);
        }
        return billDtlList;
    }

    public static List<BillDtl> convertConsignmentBillDtlListToBillDtl(List<ConsignmentBillDtl> consignmentBillDtlList, int token) {
        List<BillDtl> billDtlList = new ArrayList<>();
        for (ConsignmentBillDtl dtl : consignmentBillDtlList) {
            BillDtl billDtl = new BillDtl();
            billDtl.setId(dtl.getId());
            billDtl.setScanQty((long) dtl.getInQty());
            billDtl.setBillId(dtl.getBillId());
            billDtl.setBillNo(dtl.getBillNo());
            billDtl.setSku(dtl.getSku());
            billDtl.setStyleId(dtl.getStyleId());
            billDtl.setColorId(dtl.getColorId());
            billDtl.setSizeId(dtl.getSizeId());
            billDtl.setPrice(dtl.getPrice());
            billDtl.setQty(dtl.getQty());
            billDtlList.add(billDtl);
        }
        return billDtlList;
    }

    /**
     * 销售订单单转出入库单据（客户端查询）
     */
    public static List<BillDtl> convertSaleOrderReturnDtlToBillDtl(List<SaleOrderReturnBillDtl> saleOrderReturnBillDtls, int token) {
        List<BillDtl> billDtlList = new ArrayList<>();
        for (SaleOrderReturnBillDtl dtl : saleOrderReturnBillDtls) {
            BillDtl billDtl = new BillDtl();
            billDtl.setId(dtl.getId());
            if (token == Constant.Token.Storage_Refund_Inbound) {
                billDtl.setScanQty(dtl.getInQty());
            } else {
                billDtl.setScanQty(dtl.getOutQty());
            }
            billDtl.setBillId(dtl.getBillId());
            billDtl.setBillNo(dtl.getBillNo());
            billDtl.setSku(dtl.getSku());
            billDtl.setStyleId(dtl.getStyleId());
            billDtl.setColorId(dtl.getColorId());
            billDtl.setSizeId(dtl.getSizeId());
            billDtl.setPrice(dtl.getPrice());
            billDtl.setQty(dtl.getQty());
            billDtlList.add(billDtl);
        }
        return billDtlList;
    }

    /**
     * 调拨申请单转出入库单据（客户端查询）
     */
    public static List<Bill> convertTransferOrderToBill(List<TransferOrderBill> transferOrderBills, int token) {
        List<Bill> billList = new ArrayList<>();
        for (TransferOrderBill p : transferOrderBills) {
            Bill bill = new Bill();
            bill.setId(p.getId());
            bill.setBillNo(p.getBillNo());
            bill.setBillDate(p.getBillDate());
            bill.setOwnerId(p.getOwnerId());
            bill.setOrigId(p.getOrigId());
            bill.setDestId(p.getDestId());
            bill.setDestUnitId(p.getDestUnitId());
            bill.setOrigUnitId(p.getOrigUnitId());
            bill.setOrigUnitName(p.getOrigUnitName());
            bill.setTotQty(p.getTotQty());
            bill.setType(token);
            bill.setBillType("" + token);
            if (token == Constant.Token.Storage_Transfer_Outbound) {
                bill.setScanQty(p.getTotOutQty());
            } else {
                bill.setScanQty(p.getTotInQty());
            }
            billList.add(bill);
        }
        return billList;
    }

    /**
     * 客户端提交调拨单据转换接口（上传调拨出库，入库）
     **/
    public static void covertToTransferOrderBusiness(TransferOrderBill transferOrderBill, List<TransferOrderBillDtl> transferOrderBillDtlList, Business bus, int token) {

        Map<String, TransferOrderBillDtl> transferOrderBillDtlMap = new HashMap<>();
        for (TransferOrderBillDtl dtl : transferOrderBillDtlList) {
            transferOrderBillDtlMap.put(dtl.getSku(), dtl);
        }
        List<String> codeStrList = new ArrayList<>();
        Map<String, BusinessDtl> businessDtlMap = new HashMap<>();
        Map<String, Record> recordMap = new HashMap<>();
        for (Record record : bus.getRecordList()) {
            codeStrList.add(record.getCode());
            recordMap.put(record.getCode(), record);
        }

        Double totPrice = 0d;
        if (token == Constant.Token.Storage_Transfer_Outbound) {
            for (BusinessDtl dtl : bus.getDtlList()) {
                TransferOrderBillDtl transferOrderBillDtl = transferOrderBillDtlMap.get(dtl.getSku());
                transferOrderBillDtl.setOutQty(transferOrderBillDtl.getOutQty() + (int) dtl.getQty());
                transferOrderBillDtl.setOutVal(transferOrderBillDtl.getOutQty() * transferOrderBillDtl.getPrice());
                transferOrderBillDtl.setOutStatus(BillConstant.BillDtlStatus.Outing);
                if (transferOrderBillDtl.getOutQty().intValue() == transferOrderBillDtl.getQty().intValue()) {
                    transferOrderBillDtl.setOutStatus(BillConstant.BillDtlStatus.OutStore);
                    transferOrderBillDtl.setStatus(BillConstant.BillDtlStatus.OutStore);
                }
                totPrice += transferOrderBillDtl.getOutVal();
                businessDtlMap.put(dtl.getSku(), dtl);
                dtl.setPreVal(0D);
            }
            transferOrderBill.setTotOutQty(transferOrderBill.getTotOutQty() + bus.getTotEpc());
            transferOrderBill.setTotOutVal(transferOrderBill.getTotOutVal() + totPrice);
            transferOrderBill.setStatus(BillConstant.BillStatus.Doing);
            transferOrderBill.setOutStatus(BillConstant.BillInOutStatus.Outing);
            if (transferOrderBill.getTotOutQty().intValue() == transferOrderBill.getTotQty().intValue()) {
                transferOrderBill.setOutStatus(BillConstant.BillInOutStatus.OutStore);
            }

        } else {
            for (BusinessDtl dtl : bus.getDtlList()) {
                TransferOrderBillDtl transferOrderBillDtl = transferOrderBillDtlMap.get(dtl.getSku());
                transferOrderBillDtl.setInQty(transferOrderBillDtl.getInQty() + (int) dtl.getQty());
                transferOrderBillDtl.setInVal(transferOrderBillDtl.getInVal() * transferOrderBillDtl.getPrice());
                transferOrderBill.setInStatus(BillConstant.BillDtlStatus.Ining);
                if (transferOrderBillDtl.getInQty().intValue() == transferOrderBillDtl.getQty().intValue()) {
                    transferOrderBillDtl.setInStatus(BillConstant.BillDtlStatus.InStore);
                    transferOrderBillDtl.setStatus(BillConstant.BillDtlStatus.InStore);
                }
                totPrice += transferOrderBillDtl.getInVal();
                businessDtlMap.put(dtl.getSku(), dtl);
                dtl.setPreVal(0D);
            }
            transferOrderBill.setTotInQty(transferOrderBill.getTotInQty() + bus.getTotEpc());
            transferOrderBill.setTotInVal(transferOrderBill.getTotInVal() + totPrice);
            transferOrderBill.setStatus(BillConstant.BillStatus.Doing);
            transferOrderBill.setInStatus(BillConstant.BillInOutStatus.Ining);
            if (transferOrderBill.getTotInQty().intValue() == transferOrderBill.getTotQty().intValue()) {
                transferOrderBill.setInStatus(BillConstant.BillInOutStatus.InStore);
                transferOrderBill.setStatus(BillConstant.BillStatus.End);
            }
        }
        List<BillRecord> billRecordList = new ArrayList<>();
        List<EpcStock> epcStockList = epcStockService.findEpcByCodes(TaskUtil.getSqlStrByList(codeStrList, EpcStock.class, "code"));
        Double totPreVal = 0D;
        for (EpcStock stock : epcStockList) {
            Double preVal = CommonUtil.isBlank(stock.getStockPrice()) ? 0D : stock.getStockPrice();
            totPreVal += preVal;
            BusinessDtl businessDtl = businessDtlMap.get(stock.getSku());
            businessDtl.setPreVal(businessDtl.getPreVal() + preVal);
            Record record = recordMap.get(stock.getCode());
            record.setPrice(preVal);
            BillRecord billRecord = new BillRecord(transferOrderBill.getBillNo() + "-" + stock.getCode(), stock.getCode(), transferOrderBill.getBillNo(), stock.getSku());
            billRecordList.add(billRecord);
        }
        transferOrderBill.setBillRecordList(billRecordList);
        bus.setTotPreVal(totPreVal);
        bus.setDestId(transferOrderBill.getDestId());
        bus.setDestUnitId(transferOrderBill.getDestUnitId());
        bus.setOrigId(transferOrderBill.getOrigId());
        bus.setOrigUnitId(transferOrderBill.getOrigUnitId());
        bus.setRecordList(new ArrayList<>(recordMap.values()));
    }

    /**
     * Wang Yushen
     * 将销售订单，销售订单详表，EPC信息转换为 Business， BusinessDtl，Record 的出库信息
     *
     * @param saleOrderBill
     * @param saleOrderBillDtlList
     * @param epcList
     * @param currentUser
     * @return Business的出库信息
     */
    public static Business covertToSaleOrderBusinessOut(SaleOrderBill saleOrderBill, List<SaleOrderBillDtl> saleOrderBillDtlList, List<Epc> epcList, User currentUser) {
        Map<String, SaleOrderBillDtl> saleOrderBillDtlMap = new HashMap<>();
        List<BillRecord> billRecordList = new ArrayList<>();
        for (SaleOrderBillDtl billDtl : saleOrderBillDtlList) {
            saleOrderBillDtlMap.put(billDtl.getSku(), billDtl);
        }
        String taksId = "TSK" + CommonUtil.getDateString(new Date(), "yyyyMMdd") + System.currentTimeMillis();
        Business bus = new Business();
        Map<String, BusinessDtl> businessDtlMap = new HashMap<>();
        Map<String, String> styleCountMap = new HashMap<>();
        List<Record> recordList = new ArrayList<>();
        List<String> codeStrList = new ArrayList<>();
        Map<String, Record> recordMap = new HashMap<>();
        Double totOutPrice = 0d;
        for (Epc epc : epcList) {
            String skuOfEpc = epc.getSku();
            styleCountMap.put(epc.getStyleId(), epc.getStyleId());

            if (saleOrderBillDtlMap.containsKey(skuOfEpc)) {
                SaleOrderBillDtl billDtl = saleOrderBillDtlMap.get(skuOfEpc);
                BillRecord billRecord = new BillRecord(billDtl.getBillNo() + "-" + epc.getCode(), epc.getCode(), billDtl.getBillNo(), billDtl.getSku());
                billRecordList.add(billRecord);
                billDtl.setOutQty(billDtl.getOutQty() + 1);
                billDtl.setOutVal(billDtl.getOutQty() * billDtl.getActPrice());
                billDtl.setOutStatus(BillConstant.BillDtlStatus.Outing);
                if (billDtl.getOutQty().intValue() + billDtl.getReturnQty() == billDtl.getQty().intValue()) {
                    billDtl.setOutStatus(BillConstant.BillDtlStatus.OutStore);
                    billDtl.setStatus(BillConstant.BillDtlStatus.OutStore);
                }
                totOutPrice += saleOrderBillDtlMap.get(skuOfEpc).getPrice();
                saleOrderBillDtlMap.put(skuOfEpc, billDtl);
            }
            if (businessDtlMap.containsKey(epc.getSku())) {
                BusinessDtl businessDtl = businessDtlMap.get(skuOfEpc);
                businessDtl.setQty(businessDtl.getQty() + 1);
                businessDtlMap.put(skuOfEpc, businessDtl);
            } else {
                BusinessDtl businessDtl = new BusinessDtl(taksId, currentUser.getOwnerId(), Constant.Token.Storage_Outbound, "KE000001", skuOfEpc, 1);
                businessDtl.setId(new GuidCreator().toString());
                businessDtl.setStyleId(epc.getStyleId());
                businessDtl.setColorId(epc.getColorId());
                businessDtl.setSizeId(epc.getSizeId());
                businessDtl.setType(Constant.TaskType.Outbound);
                businessDtl.setDestId(saleOrderBill.getDestId());
                businessDtl.setDestUnitId(saleOrderBill.getDestUnitId());
                businessDtl.setOrigId(saleOrderBill.getOrigId());
                businessDtl.setOrigUnitId(saleOrderBill.getOrigUnitId());
                businessDtl.setPreVal(0D);
                businessDtlMap.put(skuOfEpc, businessDtl);
            }
            Record record = new Record(epc.getCode(), taksId, taksId, Constant.Token.Storage_Outbound, "KE000001", "");
            record.setOwnerId(currentUser.getOwnerId());
            record.setDestId(saleOrderBill.getDestId());
            record.setDestUnitId(saleOrderBill.getDestUnitId());
            record.setOrigId(saleOrderBill.getOrigId());
            record.setOrigUnitId(saleOrderBill.getOrigUnitId());
            record.setSku(skuOfEpc);
            record.setStyleId(epc.getStyleId());
            record.setColorId(epc.getColorId());
            record.setSizeId(epc.getSizeId());
            record.setPrice(saleOrderBillDtlMap.get(skuOfEpc).getActPrice());
            record.setScanTime(new Date());
            record.setId(new GuidCreator().toString());
            record.setType(Constant.TaskType.Outbound);
            recordList.add(record);
            codeStrList.add(record.getCode());
            recordMap.put(record.getCode(), record);
        }
        bus.setDtlList(new ArrayList<>(businessDtlMap.values()));
        bus.setId(taksId);
        bus.setToken(Constant.Token.Storage_Outbound);
        bus.setBeginTime(new Date());
        bus.setEndTime(new Date());
        bus.setBillId(saleOrderBill.getBillNo());
        bus.setBillNo(saleOrderBill.getBillNo());
        bus.setDestId(saleOrderBill.getDestId());
        bus.setDestUnitId(saleOrderBill.getDestUnitId());
        bus.setOrigId(saleOrderBill.getOrigId());
        bus.setOrigUnitId(saleOrderBill.getOrigUnitId());
        bus.setOwnerId(currentUser.getOwnerId());
        bus.setDeviceId("KE000001");
        bus.setStatus(Constant.TaskStatus.Submitted);
        bus.setTotCarton(1L);
        bus.setTotEpc((long) epcList.size());
        bus.setTotPrice(totOutPrice);
        bus.setTotSku((long) bus.getDtlList().size());
        bus.setTotStyle((long) styleCountMap.size());
        bus.setType(Constant.TaskType.Outbound);
        bus.setRecordList(recordList);
        List<EpcStock> epcStockList = epcStockService.findEpcByCodes(TaskUtil.getSqlStrByList(codeStrList, EpcStock.class, "code"));
        Double totPreVal = 0D;
        for (EpcStock s : epcStockList) {
            BusinessDtl dtl = businessDtlMap.get(s.getSku());
            Double preVal = CommonUtil.isBlank(s.getStockPrice()) ? 0D : s.getStockPrice();
            dtl.setPreVal(dtl.getPreVal() + preVal);
            totPreVal += preVal;
            Record r = recordMap.get(s.getCode());
            r.setExtField(s.getInSotreType());
            r.setPrice(preVal);
            SaleOrderBillDtl saleOrderBillDtl = saleOrderBillDtlMap.get(s.getSku());
            saleOrderBillDtl.setStockVal(saleOrderBillDtl.getStockVal() + preVal);
            //设置利润
            if (saleOrderBillDtl.getOutVal().intValue() == 0) {
                saleOrderBillDtl.setProfit(0D);
                saleOrderBillDtl.setProfitRate(0D);

            } else {
                saleOrderBillDtl.setProfit(saleOrderBillDtl.getOutVal() - saleOrderBillDtl.getStockVal());
                if (saleOrderBillDtl.getStockVal().intValue() != 0) {
                    saleOrderBillDtl.setProfitRate(Double.parseDouble(CommonUtil.getDecimal(saleOrderBillDtl.getProfit() / saleOrderBillDtl.getOutVal()* 100, "######0.00")));
                } else {
                    saleOrderBillDtl.setProfitRate(0d);
                }

            }
        }
        bus.setTotPreVal(totPreVal);
        saleOrderBill.setBillRecordList(billRecordList);
        saleOrderBill.setTotStockVal(saleOrderBill.getTotStockVal() + totPreVal);
        saleOrderBill.setProfit(saleOrderBill.getTotOutVal() - totPreVal);
        /*//设置利润
        saleOrderBill.setProfitRate(Double.parseDouble(CommonUtil.getDecimal(totPreVal/saleOrderBill.getTotOutVal()*100,"######0.00")));
        bus.setRecordList(new ArrayList<>(recordMap.values()));*/

        saleOrderBill.setTotOutQty(saleOrderBill.getTotOutQty() + epcList.size());
        saleOrderBill.setTotOutVal(saleOrderBill.getTotOutVal() + totOutPrice);
        //设置利润
        saleOrderBill.setProfitRate(Double.parseDouble(CommonUtil.getDecimal(totPreVal / saleOrderBill.getTotOutVal() * 100, "######0.00")));
        bus.setRecordList(new ArrayList<>(recordMap.values()));
        saleOrderBill.setStatus(BillConstant.BillStatus.Doing);
        saleOrderBill.setOutStatus(BillConstant.BillInOutStatus.Outing);
        if (saleOrderBill.getTotOutQty().intValue() + saleOrderBill.getTotRetrunQty() == saleOrderBill.getTotQty().intValue()) {
            saleOrderBill.setOutStatus(BillConstant.BillInOutStatus.OutStore);
            if (saleOrderBill.getCustomerTypeId().equals(BillConstant.customerType.Customer)) {
                saleOrderBill.setStatus(BillConstant.BillStatus.End);
            }
        }
        return bus;
    }

    /**
     * Wang Yushen
     * 将寄售订单，寄售订单详表，EPC信息转换为 Business， BusinessDtl，Record 的出库信息
     *
     * @param consignmentBill
     * @param ConsignmentBillDtllist
     * @param epcList
     * @param currentUser
     * @return Business的出库信息
     */
    public static Business covertToConsignmentBillBusinessOut(ConsignmentBill consignmentBill, List<ConsignmentBillDtl> ConsignmentBillDtllist, List<Epc> epcList, User currentUser) {
        Map<String, ConsignmentBillDtl> ConsignmentBillDtlMap = new HashMap<>();
        for (ConsignmentBillDtl dtl : ConsignmentBillDtllist) {
            ConsignmentBillDtlMap.put(dtl.getSku(), dtl);
        }
        String taksId = "TSK" + CommonUtil.getDateString(new Date(), "yyyyMMdd") + System.currentTimeMillis();
        Business bus = new Business();
        Map<String, BusinessDtl> businessDtlMap = new HashMap<>();
        Map<String, String> styleCountMap = new HashMap<>();
        List<Record> recordList = new ArrayList<>();
        Double totRcvPrice = 0d;
        List<BillRecord> billRecordList = new ArrayList<>();
        for (Epc e : epcList) {
            String sku = e.getSku();

            styleCountMap.put(e.getStyleId(), e.getStyleId());
            if (ConsignmentBillDtlMap.containsKey(sku)) {
                ConsignmentBillDtl dtl = ConsignmentBillDtlMap.get(sku);
                BillRecord billRecord = new BillRecord(dtl.getBillNo() + "-" + e.getCode(), e.getCode(), dtl.getBillNo(), dtl.getSku());
                billRecordList.add(billRecord);
                /* dtl.setOutQty(dtl.getOutQty() + 1);*/
                dtl.setOutVal(dtl.getOutQty() * dtl.getActPrice());
                dtl.setOutStatus(BillConstant.BillDtlStatus.Outing);
                if (dtl.getOutQty().intValue() == dtl.getQty().intValue()) {
                    dtl.setStatus(BillConstant.BillDtlStatus.OutStore);
                    dtl.setOutStatus(BillConstant.BillDtlStatus.OutStore);
                }
                totRcvPrice += dtl.getActPrice();
                dtl.setTotActPrice(0 - dtl.getActPrice() * dtl.getInQty());
                dtl.setStockVal(0D);
                ConsignmentBillDtlMap.put(sku, dtl);

            }
            ConsignmentBillDtl consignmentBillDtl = ConsignmentBillDtlMap.get(sku);
            if (CommonUtil.isNotBlank(consignmentBillDtl)) {
                if (businessDtlMap.containsKey(e.getSku())) {
                    BusinessDtl dtl = businessDtlMap.get(sku);
                    dtl.setQty(dtl.getQty() + 1);
                    businessDtlMap.put(sku, dtl);
                } else {
                    BusinessDtl dtl = new BusinessDtl(taksId, currentUser.getOwnerId(), Constant.Token.Storage_refoundOut_customer, "KE000001", sku, 1);
                    dtl.setId(new GuidCreator().toString());
                    dtl.setStyleId(e.getStyleId());
                    dtl.setColorId(e.getColorId());
                    dtl.setSizeId(e.getSizeId());
                    dtl.setType(Constant.TaskType.Outbound);
                    dtl.setDestId(consignmentBill.getDestId());
                    dtl.setDestUnitId(consignmentBill.getDestUnitId());
                    dtl.setOrigId(consignmentBill.getOrigId());
                    dtl.setOrigUnitId(consignmentBill.getOrigUnitId());
                    dtl.setPreVal(0D);
                    businessDtlMap.put(sku, dtl);
                }


                Record record = new Record(e.getCode(), taksId, taksId, Constant.Token.Storage_refoundOut_customer, "KE000001", "");
                record.setOwnerId(currentUser.getOwnerId());
                record.setDestId(consignmentBill.getDestId());
                record.setDestUnitId(consignmentBill.getDestUnitId());
                record.setOrigId(consignmentBill.getDestId());
                record.setOrigUnitId(consignmentBill.getDestUnitId());
                record.setSku(sku);
                record.setStyleId(e.getStyleId());
                record.setColorId(e.getColorId());
                record.setSizeId(e.getSizeId());
                record.setPrice(consignmentBillDtl.getActPrice());
                record.setScanTime(new Date());
                /* record.setExtField(dtl.getInStockType());//record中增加入库类型*/
                record.setId(new GuidCreator().toString());
                record.setType(Constant.TaskType.Outbound);
                recordList.add(record);
            }

        }
        bus.setDtlList(new ArrayList<>(businessDtlMap.values()));
        bus.setId(taksId);
        bus.setToken(Constant.Token.Storage_refoundOut_customer);
        bus.setBeginTime(new Date());
        bus.setEndTime(new Date());
        bus.setBillId(consignmentBill.getBillNo());
        bus.setBillNo(consignmentBill.getBillNo());
        bus.setDestId(consignmentBill.getDestId());
        bus.setDestUnitId(consignmentBill.getDestUnitId());
        bus.setOrigId(consignmentBill.getOrigId());
        bus.setOrigUnitId(consignmentBill.getOrigUnitId());
        bus.setOwnerId(currentUser.getOwnerId());
        bus.setDeviceId("KE000001");
        bus.setStatus(Constant.TaskStatus.Submitted);
        bus.setTotCarton(1L);
        bus.setTotEpc((long) epcList.size());
        bus.setTotPrice(totRcvPrice);
        bus.setTotSku((long) bus.getDtlList().size());
        bus.setTotStyle((long) styleCountMap.size());
        bus.setType(Constant.TaskType.Outbound);
        bus.setRecordList(recordList);
        consignmentBill.setBillRecordList(billRecordList);
        /* consignmentBill.setTotOutQty(consignmentBill.getTotOutQty() + epcList.size());*/
        consignmentBill.setTotOutVal(totRcvPrice);
       /* if (consignmentBill.getTotOutQty().intValue() == consignmentBill.getTotQty().intValue()) {
            consignmentBill.setStatus(BillConstant.BillStatus.End);
            consignmentBill.setOutStatus(BillConstant.BillInOutStatus.OutStore);
        } else {
            consignmentBill.setStatus(BillConstant.BillStatus.Doing);
            consignmentBill.setOutStatus(BillConstant.BillInOutStatus.Outing);
        }*/
        consignmentBill.setStatus(BillConstant.BillStatus.Consign);
        //出库填写EpcStock中成本价格
        List<String> codeStrList = new ArrayList<>();
        Map<String, Record> recordMap = new HashMap<>();
        for (Record r : bus.getRecordList()) {
            codeStrList.add(r.getCode());
            recordMap.put(r.getCode(), r);
        }
        List<EpcStock> epcStockList = epcStockService.findEpcByCodes(TaskUtil.getSqlStrByList(codeStrList, EpcStock.class, "code"));
        Double totPreVal = 0D;
        for (EpcStock s : epcStockList) {
            BusinessDtl dtl = businessDtlMap.get(s.getSku());
            Double preVal = CommonUtil.isBlank(s.getStockPrice()) ? 0D : s.getStockPrice();
            dtl.setPreVal(dtl.getPreVal() + preVal);
            totPreVal += preVal;
            Record r = recordMap.get(s.getCode());
            r.setPrice(preVal);
            ConsignmentBillDtl consignmentBillDtl = ConsignmentBillDtlMap.get(s.getSku());
            consignmentBillDtl.setStockVal(consignmentBillDtl.getStockVal() + preVal);
            //设置利润
           /* saleOrderBillDtl.setProfit(saleOrderBillDtl.getOutVal()-saleOrderBillDtl.getTotStockVal());
            saleOrderBillDtl.setProfitRate(Double.parseDouble(CommonUtil.getDecimal(saleOrderBillDtl.getTotStockVal()/saleOrderBillDtl.getOutQty()*100,"######0.00")));*/

        }
        consignmentBill.setTotStockVal(consignmentBill.getTotStockVal() + totPreVal);
        bus.setTotPreVal(totPreVal);

        bus.setRecordList(new ArrayList<>(recordMap.values()));

        return bus;
    }

    /**
     * Wang Yushen
     * 将销售订单，销售订单详表，EPC信息转换为 Business， BusinessDtl，Record 的入库信息
     * web
     *
     * @param saleOrderBill
     * @param saleOrderBillDtlList
     * @param epcList
     * @param currentUser
     * @return Business的入库信息
     */
    public static Business covertToSaleOrderBusinessIn(SaleOrderBill saleOrderBill, List<SaleOrderBillDtl> saleOrderBillDtlList, List<Epc> epcList, User currentUser) {
        Map<String, SaleOrderBillDtl> saleOrderBillDtlMap = new HashMap<>();
        // List<BillRecord> billRecordList = new ArrayList<>();
        for (SaleOrderBillDtl billDtl : saleOrderBillDtlList) {
            saleOrderBillDtlMap.put(billDtl.getSku(), billDtl);
        }

        String taksId = "TSK" + CommonUtil.getDateString(new Date(), "yyyyMMdd") + System.currentTimeMillis();
        Business bus = new Business();
        Map<String, BusinessDtl> businessDtlMap = new HashMap<>();
        Map<String, String> styleCountMap = new HashMap<>();
        List<Record> recordList = new ArrayList<>();
        Double totInPrice = 0d;
        for (Epc epc : epcList) {
            String skuOfEpc = epc.getSku();
            styleCountMap.put(epc.getStyleId(), epc.getStyleId());
            if (saleOrderBillDtlMap.containsKey(skuOfEpc)) {
                SaleOrderBillDtl billDtl = saleOrderBillDtlMap.get(skuOfEpc);
                //BillRecord billRecord = new BillRecord(billDtl.getBillNo()+"-"+epc.getCode(),epc.getCode(),billDtl.getBillNo(),billDtl.getSku());
                //billRecordList.add(billRecord);
                billDtl.setInQty(billDtl.getInQty() + 1);
                billDtl.setInVal(billDtl.getInQty() * billDtl.getPrice());
                billDtl.setInStatus(BillConstant.BillDtlStatus.Ining);
                if (billDtl.getInQty().intValue() + billDtl.getReturnQty() == billDtl.getQty().intValue()) {
                    billDtl.setInStatus(BillConstant.BillDtlStatus.InStore);
                    billDtl.setStatus(BillConstant.BillDtlStatus.InStore);
                }
                totInPrice += saleOrderBillDtlMap.get(skuOfEpc).getPrice();
                saleOrderBillDtlMap.put(skuOfEpc, billDtl);
                billDtl.setStockVal(billDtl.getInVal());
            }
            if (businessDtlMap.containsKey(epc.getSku())) {
                BusinessDtl businessDtl = businessDtlMap.get(skuOfEpc);
                businessDtl.setQty(businessDtl.getQty() + 1);
                businessDtlMap.put(skuOfEpc, businessDtl);
            } else {
                BusinessDtl businessDtl = new BusinessDtl(taksId, currentUser.getOwnerId(), Constant.Token.Storage_Inbound_customer, "KE000001", skuOfEpc, 1);
                businessDtl.setId(new GuidCreator().toString());
                businessDtl.setStyleId(epc.getStyleId());
                businessDtl.setColorId(epc.getColorId());
                businessDtl.setSizeId(epc.getSizeId());
                businessDtl.setType(Constant.TaskType.Inbound);
                businessDtl.setDestId(saleOrderBill.getDestId());
                businessDtl.setDestUnitId(saleOrderBill.getDestUnitId());
                businessDtl.setOrigId(saleOrderBill.getOrigId());
                businessDtl.setOrigUnitId(saleOrderBill.getOrigUnitId());
                businessDtlMap.put(skuOfEpc, businessDtl);
            }
            Record record = new Record(epc.getCode(), taksId, taksId, Constant.Token.Storage_Inbound_customer, "KE000001", "");
            record.setOwnerId(currentUser.getOwnerId());
            record.setOrigUnitId(saleOrderBill.getOrigUnitId());
            record.setOrigId(saleOrderBill.getOrigId());
            record.setDestUnitId(saleOrderBill.getDestUnitId());
            record.setDestId(saleOrderBill.getDestId());
            record.setSku(skuOfEpc);
            record.setStyleId(epc.getStyleId());
            record.setColorId(epc.getColorId());
            record.setSizeId(epc.getSizeId());
            record.setPrice(saleOrderBillDtlMap.get(skuOfEpc).getActPrice());
            record.setScanTime(new Date());
            record.setId(new GuidCreator().toString());
            record.setType(Constant.TaskType.Inbound);
            recordList.add(record);
        }
        bus.setDtlList(new ArrayList<>(businessDtlMap.values()));
        bus.setId(taksId);
        bus.setToken(Constant.Token.Storage_Inbound_customer);
        bus.setBeginTime(new Date());
        bus.setEndTime(new Date());
        bus.setBillId(saleOrderBill.getBillNo());
        bus.setBillNo(saleOrderBill.getBillNo());
        bus.setDestId(saleOrderBill.getDestId());
        bus.setDestUnitId(saleOrderBill.getDestUnitId());
        bus.setOrigId(saleOrderBill.getOrigId());
        bus.setOrigUnitId(saleOrderBill.getOrigUnitId());
        bus.setOwnerId(currentUser.getOwnerId());
        bus.setDeviceId("KE000001");
        bus.setStatus(Constant.TaskStatus.Submitted);
        bus.setTotCarton(1L);
        bus.setTotEpc((long) epcList.size());
        bus.setTotPrice(totInPrice);
        bus.setTotSku((long) bus.getDtlList().size());
        bus.setTotStyle((long) styleCountMap.size());
        bus.setType(Constant.TaskType.Inbound);
        bus.setRecordList(recordList);
        bus.setTotPreVal(totInPrice);
        //销售入库更新任务成本
        Double totPreVal = 0D;
        for (Record r : bus.getRecordList()) {
            BusinessDtl dtl = businessDtlMap.get(r.getSku());
            SaleOrderBillDtl saleOrderBillDtl = saleOrderBillDtlMap.get(r.getSku());
            r.setPrice(saleOrderBillDtl.getActPrice());
            totPreVal += r.getPrice();
            dtl.setPreVal(dtl.getQty() * saleOrderBillDtl.getActPrice());

        }
        bus.setTotPreVal(totPreVal);
        // saleOrderBill.setBillRecordList(billRecordList);
        saleOrderBill.setTotInQty(saleOrderBill.getTotInQty() + epcList.size());
        saleOrderBill.setTotInVal(saleOrderBill.getTotInVal() + totInPrice);
        saleOrderBill.setStatus(BillConstant.BillStatus.Doing);
        saleOrderBill.setInStatus(BillConstant.BillInOutStatus.Ining);
        if (saleOrderBill.getTotInQty().intValue() + saleOrderBill.getTotRetrunQty() == saleOrderBill.getTotQty().intValue()) {
            saleOrderBill.setInStatus(BillConstant.BillInOutStatus.InStore);
            saleOrderBill.setStatus(BillConstant.BillStatus.End);
        }
        return bus;
    }


    /**
     * Wang Yushen
     * 将调拨单，调拨单单详表，EPC信息转换为 Business， BusinessDtl，Record 的出库信息
     *
     * @param transferOrderBill
     * @param transferOrderBillDtlList
     * @param epcList
     * @param currentUser
     * @return Business的出库信息
     */
    public static Business covertToTransferOrderBusinessOut(TransferOrderBill transferOrderBill, List<TransferOrderBillDtl> transferOrderBillDtlList, List<Epc> epcList, User currentUser) {

        Map<String, TransferOrderBillDtl> TransferOrderBillDtlMap = new HashMap<>();
        for (TransferOrderBillDtl billDtl : transferOrderBillDtlList) {
            TransferOrderBillDtlMap.put(billDtl.getSku(), billDtl);
        }

        String taksId = "TSK" + CommonUtil.getDateString(new Date(), "yyyyMMdd") + System.currentTimeMillis();
        Business bus = new Business();
        Map<String, BusinessDtl> businessDtlMap = new HashMap<>();
        Map<String, String> styleCountMap = new HashMap<>();
        List<Record> recordList = new ArrayList<>();
        Double totOutPrice = 0d;
        for (Epc epc : epcList) {
            String skuOfEpc = epc.getSku();
            styleCountMap.put(epc.getStyleId(), epc.getStyleId());
            if (TransferOrderBillDtlMap.containsKey(skuOfEpc)) {
                TransferOrderBillDtl billDtl = TransferOrderBillDtlMap.get(skuOfEpc);
                billDtl.setOutQty(billDtl.getOutQty() + 1);
                billDtl.setOutVal(billDtl.getOutQty() * billDtl.getPrice());
                billDtl.setOutStatus(BillConstant.BillDtlStatus.Outing);
                if (billDtl.getOutQty().intValue() == billDtl.getQty().intValue()) {
                    billDtl.setOutStatus(BillConstant.BillDtlStatus.OutStore);
                    billDtl.setStatus(BillConstant.BillDtlStatus.OutStore);
                }
                totOutPrice += TransferOrderBillDtlMap.get(skuOfEpc).getPrice();
                TransferOrderBillDtlMap.put(skuOfEpc, billDtl);
            }
            if (businessDtlMap.containsKey(epc.getSku())) {
                BusinessDtl businessDtl = businessDtlMap.get(skuOfEpc);
                businessDtl.setQty(businessDtl.getQty() + 1);
                businessDtlMap.put(skuOfEpc, businessDtl);
            } else {
                BusinessDtl businessDtl = new BusinessDtl(taksId, currentUser.getOwnerId(), Constant.Token.Storage_Transfer_Outbound, "KE000001", skuOfEpc, 1);
                businessDtl.setId(new GuidCreator().toString());
                businessDtl.setStyleId(epc.getStyleId());
                businessDtl.setColorId(epc.getColorId());
                businessDtl.setSizeId(epc.getSizeId());
                businessDtl.setPreVal(TransferOrderBillDtlMap.get(skuOfEpc).getTotPrice());
                businessDtl.setType(Constant.TaskType.Outbound);
                businessDtl.setDestId(transferOrderBill.getDestId());
                businessDtl.setDestUnitId(transferOrderBill.getDestUnitId());
                businessDtl.setOrigId(transferOrderBill.getOrigId());
                businessDtl.setOrigUnitId(transferOrderBill.getOrigUnitId());
                businessDtlMap.put(skuOfEpc, businessDtl);
            }
            Record record = new Record(epc.getCode(), taksId, taksId, Constant.Token.Storage_Transfer_Outbound, "KE000001", "");
            record.setOwnerId(currentUser.getOwnerId());
            record.setOrigUnitId(transferOrderBill.getOrigUnitId());
            record.setOrigId(transferOrderBill.getOrigId());
            record.setDestUnitId(transferOrderBill.getDestUnitId());
            record.setDestId(transferOrderBill.getDestId());
            record.setSku(skuOfEpc);
            record.setStyleId(epc.getStyleId());
            record.setColorId(epc.getColorId());
            record.setSizeId(epc.getSizeId());
            record.setPrice(TransferOrderBillDtlMap.get(skuOfEpc).getPrice());
            record.setScanTime(new Date());
            record.setId(new GuidCreator().toString());
            record.setType(Constant.TaskType.Outbound);
            recordList.add(record);
        }
        bus.setDtlList(new ArrayList<>(businessDtlMap.values()));
        bus.setId(taksId);
        bus.setToken(Constant.Token.Storage_Transfer_Outbound);
        bus.setBeginTime(new Date());
        bus.setEndTime(new Date());
        bus.setBillId(transferOrderBill.getBillNo());
        bus.setBillNo(transferOrderBill.getBillNo());
        bus.setDestId(transferOrderBill.getDestId());
        bus.setDestUnitId(transferOrderBill.getDestUnitId());
        bus.setOrigId(transferOrderBill.getOrigId());
        bus.setOrigUnitId(transferOrderBill.getOrigUnitId());
        bus.setOwnerId(currentUser.getOwnerId());
        bus.setDeviceId("KE000001");
        bus.setStatus(Constant.TaskStatus.Submitted);
        bus.setTotCarton(1L);
        bus.setTotEpc((long) epcList.size());
        bus.setTotPrice(totOutPrice);
        bus.setTotSku((long) bus.getDtlList().size());
        bus.setTotStyle((long) styleCountMap.size());
        bus.setType(Constant.TaskType.Outbound);
        bus.setTotPreVal(totOutPrice);
        bus.setRecordList(recordList);

        transferOrderBill.setTotOutQty(transferOrderBill.getTotOutQty() + epcList.size());
        transferOrderBill.setTotOutVal(transferOrderBill.getTotOutVal() + totOutPrice);
        transferOrderBill.setStatus(BillConstant.BillStatus.Doing);
        transferOrderBill.setOutStatus(BillConstant.BillInOutStatus.Outing);
        if (transferOrderBill.getTotOutQty().intValue() == transferOrderBill.getTotQty().intValue()) {
            transferOrderBill.setOutStatus(BillConstant.BillInOutStatus.OutStore);
        }
        return bus;
    }

    /**
     * Wang Yushen
     * 将调拨单，调拨单单详表，EPC信息转换为 Business， BusinessDtl，Record 的入库信息
     *
     * @param transferOrderBill
     * @param transferOrderBillDtlList
     * @param epcList
     * @param currentUser
     * @return Business的出库信息
     */
    public static Business covertToTransferOrderBusinessIn(TransferOrderBill transferOrderBill, List<TransferOrderBillDtl> transferOrderBillDtlList, List<Epc> epcList, User currentUser) {
        Map<String, TransferOrderBillDtl> TransferOrderBillDtlMap = new HashMap<>();
        for (TransferOrderBillDtl billDtl : transferOrderBillDtlList) {
            TransferOrderBillDtlMap.put(billDtl.getSku(), billDtl);
        }

        String taksId = "TSK" + CommonUtil.getDateString(new Date(), "yyyyMMdd") + System.currentTimeMillis();
        Business bus = new Business();
        Map<String, BusinessDtl> businessDtlMap = new HashMap<>();
        Map<String, String> styleCountMap = new HashMap<>();
        List<Record> recordList = new ArrayList<>();
        Double totInPrice = 0d;
        for (Epc epc : epcList) {
            String skuOfEpc = epc.getSku();
            styleCountMap.put(epc.getStyleId(), epc.getStyleId());
            if (TransferOrderBillDtlMap.containsKey(skuOfEpc)) {
                TransferOrderBillDtl billDtl = TransferOrderBillDtlMap.get(skuOfEpc);
                billDtl.setInQty(billDtl.getInQty() + 1);
                billDtl.setInVal(billDtl.getInQty() * billDtl.getPrice());
                billDtl.setInStatus(BillConstant.BillDtlStatus.Ining);
                if (billDtl.getInQty().intValue() == billDtl.getQty().intValue()) {
                    billDtl.setInStatus(BillConstant.BillDtlStatus.InStore);
                    billDtl.setStatus(BillConstant.BillDtlStatus.InStore);
                }
                totInPrice += TransferOrderBillDtlMap.get(skuOfEpc).getPrice();
                TransferOrderBillDtlMap.put(skuOfEpc, billDtl);
            }
            if (businessDtlMap.containsKey(epc.getSku())) {
                BusinessDtl businessDtl = businessDtlMap.get(skuOfEpc);
                businessDtl.setQty(businessDtl.getQty() + 1);
                businessDtlMap.put(skuOfEpc, businessDtl);
            } else {
                BusinessDtl businessDtl = new BusinessDtl(taksId, currentUser.getOwnerId(), Constant.Token.Storage_Transfer_Inbound, "KE000001", skuOfEpc, 1);
                businessDtl.setId(new GuidCreator().toString());
                businessDtl.setStyleId(epc.getStyleId());
                businessDtl.setColorId(epc.getColorId());
                businessDtl.setSizeId(epc.getSizeId());
                businessDtl.setPreVal(TransferOrderBillDtlMap.get(skuOfEpc).getTotPrice());
                businessDtl.setType(Constant.TaskType.Inbound);
                businessDtl.setDestId(transferOrderBill.getDestId());
                businessDtl.setDestUnitId(transferOrderBill.getDestUnitId());
                businessDtl.setOrigId(transferOrderBill.getOrigId());
                businessDtl.setOrigUnitId(transferOrderBill.getOrigUnitId());
                businessDtlMap.put(skuOfEpc, businessDtl);
            }
            Record record = new Record(epc.getCode(), taksId, taksId, Constant.Token.Storage_Transfer_Inbound, "KE000001", "");
            record.setOwnerId(currentUser.getOwnerId());
            record.setOrigUnitId(transferOrderBill.getOrigUnitId());
            record.setOrigId(transferOrderBill.getOrigId());
            record.setDestUnitId(transferOrderBill.getDestUnitId());
            record.setDestId(transferOrderBill.getDestId());
            record.setSku(skuOfEpc);
            record.setStyleId(epc.getStyleId());
            record.setColorId(epc.getColorId());
            record.setSizeId(epc.getSizeId());
            record.setPrice(TransferOrderBillDtlMap.get(skuOfEpc).getPrice());
            record.setScanTime(new Date());
            record.setId(new GuidCreator().toString());
            record.setType(Constant.TaskType.Inbound);
            recordList.add(record);
        }
        bus.setDtlList(new ArrayList<>(businessDtlMap.values()));
        bus.setId(taksId);
        bus.setToken(Constant.Token.Storage_Transfer_Inbound);
        bus.setBeginTime(new Date());
        bus.setEndTime(new Date());
        bus.setBillId(transferOrderBill.getBillNo());
        bus.setBillNo(transferOrderBill.getBillNo());
        bus.setDestId(transferOrderBill.getDestId());
        bus.setDestUnitId(transferOrderBill.getDestUnitId());
        bus.setOrigId(transferOrderBill.getOrigId());
        bus.setOrigUnitId(transferOrderBill.getOrigUnitId());
        bus.setOwnerId(currentUser.getOwnerId());
        bus.setDeviceId("KE000001");
        bus.setStatus(Constant.TaskStatus.Submitted);
        bus.setTotCarton(1L);
        bus.setTotEpc((long) epcList.size());
        bus.setTotPrice(totInPrice);
        bus.setTotSku((long) bus.getDtlList().size());
        bus.setTotStyle((long) styleCountMap.size());
        bus.setType(Constant.TaskType.Inbound);
        bus.setTotPreVal(totInPrice);
        bus.setRecordList(recordList);

        transferOrderBill.setTotInQty(transferOrderBill.getTotInQty() + epcList.size());
        transferOrderBill.setTotInVal(transferOrderBill.getTotInVal() + totInPrice);
        transferOrderBill.setStatus(BillConstant.BillStatus.Doing);
        transferOrderBill.setInStatus(BillConstant.BillInOutStatus.Ining);
        if (transferOrderBill.getTotInQty().intValue() == transferOrderBill.getTotQty().intValue()) {
            transferOrderBill.setStatus(BillConstant.BillStatus.End);
            transferOrderBill.setInStatus(BillConstant.BillInOutStatus.InStore);
        }
        return bus;
    }

    /**
     * Session
     * 采购退货出库（客户端上传任务）
     *
     * @param purchaseReturnBill
     * @param purchaseReturnBillDtls
     * @param bus
     */
    public static void convertPurchaseReturnBillToBusiness(PurchaseReturnBill purchaseReturnBill, List<PurchaseReturnBillDtl> purchaseReturnBillDtls, Business bus) {

        Map<String, PurchaseReturnBillDtl> detailMap = new HashMap<String, PurchaseReturnBillDtl>();
        if (CommonUtil.isNotBlank(purchaseReturnBillDtls)) {
            for (PurchaseReturnBillDtl p : purchaseReturnBillDtls) {
                detailMap.put(p.getSku(), p);
            }
        }
        Double totPrice = 0d;
        Map<String, BusinessDtl> businessDtlMap = new HashMap<>();
        for (BusinessDtl dtl : bus.getDtlList()) {
            PurchaseReturnBillDtl purchaseReturnBillDtl = detailMap.get(dtl.getSku());
            purchaseReturnBillDtl.setOutQty(purchaseReturnBillDtl.getOutQty() + (int) dtl.getQty());
            purchaseReturnBillDtl.setOutVal(purchaseReturnBillDtl.getOutQty() * purchaseReturnBillDtl.getActPrice());
            purchaseReturnBillDtl.setOutStatus(BillConstant.BillDtlStatus.Outing);
            if (purchaseReturnBillDtl.getOutQty().intValue() == purchaseReturnBillDtl.getQty().intValue()) {
                purchaseReturnBillDtl.setStatus(BillConstant.BillDtlStatus.OutStore);
                purchaseReturnBillDtl.setOutStatus(BillConstant.BillDtlStatus.OutStore);
            }
            totPrice += purchaseReturnBillDtl.getOutVal();
            businessDtlMap.put(dtl.getSku(), dtl);
            dtl.setPreVal(0D);
        }
        purchaseReturnBill.setTotOutQty(purchaseReturnBill.getTotOutQty() + bus.getTotEpc());
        purchaseReturnBill.setTotOutVal(purchaseReturnBill.getTotOutQty() + totPrice);
        purchaseReturnBill.setStatus(BillConstant.BillStatus.Doing);
        if (purchaseReturnBill.getTotOutQty().intValue() == purchaseReturnBill.getTotQty().intValue()) {
            purchaseReturnBill.setStatus(BillConstant.BillStatus.End);
        }
        List<String> codeStrList = new ArrayList<>();
        Map<String, Record> recordMap = new HashMap<>();
        for (Record r : bus.getRecordList()) {
            codeStrList.add(r.getCode());
            recordMap.put(r.getCode(), r);
        }
        List<BillRecord> billRecordList = new ArrayList<>();
        List<EpcStock> epcStockList = epcStockService.findEpcByCodes(TaskUtil.getSqlStrByList(codeStrList, EpcStock.class, "code"));
        Double totPreVal = 0D;
        for (EpcStock s : epcStockList) {
            Double preVal = CommonUtil.isBlank(s.getStockPrice()) ? 0D : s.getStockPrice();
            BusinessDtl dtl = businessDtlMap.get(s.getSku());
            dtl.setPreVal(dtl.getPreVal() + preVal);
            totPreVal += preVal;
            Record r = recordMap.get(s.getCode());
            r.setPrice(preVal);
            PurchaseReturnBillDtl purchaseReturnBillDtl = detailMap.get(s.getSku());
            //PurchaseReturnBillDtl purchaseReturnBillDtl = detailMap.get(s.getCode());
            purchaseReturnBillDtl.setStockVal(purchaseReturnBillDtl.getStockVal() + preVal);
            BillRecord billRecord = new BillRecord(purchaseReturnBill.getBillNo() + "-" + s.getCode(), s.getCode(), purchaseReturnBill.getBillNo(), s.getSku());
            billRecordList.add(billRecord);
        }
        purchaseReturnBill.setTotStockVal(purchaseReturnBill.getTotStockVal() + totPreVal);
        purchaseReturnBill.setBillRecordList(billRecordList);
        bus.setTotPreVal(totPreVal);
        bus.setDestId(purchaseReturnBill.getDestId());
        bus.setDestUnitId(purchaseReturnBill.getDestUnitId());
        bus.setOrigId(purchaseReturnBill.getOrigId());
        bus.setOrigUnitId(purchaseReturnBill.getOrigUnitId());
        bus.setRecordList(new ArrayList<>(recordMap.values()));
    }

    /**
     * Session
     * 销售退货上传（出库任务）更新单据信息（客户端上传任务）
     */
    public static void convertToSaleOrderReturnBusinessOut(SaleOrderReturnBill saleOrderReturnBill, List<SaleOrderReturnBillDtl> saleOrderReturnBillDtls, Business bus) {
        Map<String, SaleOrderReturnBillDtl> saleOrderReturnDtlMap = new HashMap<>();
        for (SaleOrderReturnBillDtl dtl : saleOrderReturnBillDtls) {
            saleOrderReturnDtlMap.put(dtl.getSku(), dtl);
        }
        Double totOutPrice = 0d;
        Map<String, BusinessDtl> businessDtlMap = new HashMap<>();
        for (BusinessDtl dtl : bus.getDtlList()) {
            SaleOrderReturnBillDtl saleOrderReturnBillDtl = saleOrderReturnDtlMap.get(dtl.getSku());
            saleOrderReturnBillDtl.setOutQty(saleOrderReturnBillDtl.getOutQty() + dtl.getQty());
            saleOrderReturnBillDtl.setOutVal(saleOrderReturnBillDtl.getOutQty() + saleOrderReturnBillDtl.getActPrice());
            saleOrderReturnBillDtl.setOutStatus(BillConstant.BillDtlStatus.OutStore);
            if (saleOrderReturnBill.getTotOutQty().intValue() == saleOrderReturnBillDtl.getQty().intValue()) {
                saleOrderReturnBillDtl.setOutStatus(BillConstant.BillDtlStatus.OutStore);
                saleOrderReturnBillDtl.setStatus(BillConstant.BillDtlStatus.OutStore);
            }
            dtl.setPreVal(0D);
            businessDtlMap.put(dtl.getSku(), dtl);
            totOutPrice += saleOrderReturnBillDtl.getOutVal();
        }
        saleOrderReturnBill.setTotOutQty(saleOrderReturnBill.getTotOutQty() + bus.getTotEpc());
        saleOrderReturnBill.setTotOutVal(saleOrderReturnBill.getTotOutVal() + totOutPrice);
        saleOrderReturnBill.setStatus(BillConstant.BillStatus.Doing);
        saleOrderReturnBill.setOutStatus(BillConstant.BillInOutStatus.Outing);
        if (saleOrderReturnBill.getTotOutQty().intValue() == saleOrderReturnBill.getTotQty().intValue()) {
            saleOrderReturnBill.setOutStatus(BillConstant.BillInOutStatus.OutStore);
            saleOrderReturnBill.setStatus(BillConstant.BillStatus.End);
        }
        //出库填写EpcStock中成本价格
        List<String> codeStrList = new ArrayList<>();
        Map<String, Record> recordMap = new HashMap<>();
        for (Record r : bus.getRecordList()) {
            codeStrList.add(r.getCode());
            recordMap.put(r.getCode(), r);
        }
        List<EpcStock> epcStockList = epcStockService.findEpcByCodes(TaskUtil.getSqlStrByList(codeStrList, EpcStock.class, "code"));
        Double totPreVal = 0D;
        List<BillRecord> billRecordList = new ArrayList<>();
        for (EpcStock s : epcStockList) {
            BusinessDtl dtl = businessDtlMap.get(s.getSku());
            Double preVal = CommonUtil.isBlank(s.getStockPrice()) ? 0D : s.getStockPrice();
            dtl.setPreVal(dtl.getPreVal() + preVal);
            totPreVal += preVal;
            Record r = recordMap.get(s.getCode());
            r.setPrice(preVal);
            SaleOrderReturnBillDtl saleOrderReturnBillDtl = saleOrderReturnDtlMap.get(s.getSku());
            saleOrderReturnBillDtl.setStockVal(saleOrderReturnBillDtl.getStockVal() + preVal);
            BillRecord billRecord = new BillRecord(saleOrderReturnBillDtl.getBillNo() + "-" + s.getCode(), s.getCode(), saleOrderReturnBillDtl.getBillNo(), saleOrderReturnBillDtl.getSku());
            billRecordList.add(billRecord);
        }
        saleOrderReturnBill.setBillRecordList(billRecordList);
        bus.setTotPreVal(totPreVal);
        bus.setDestId(saleOrderReturnBill.getDestId());
        bus.setDestUnitId(saleOrderReturnBill.getDestUnitId());
        bus.setOrigId(saleOrderReturnBill.getOrigId());
        bus.setOrigUnitId(saleOrderReturnBill.getOrigUnitId());
        bus.setRecordList(new ArrayList<>(recordMap.values()));
        for (Record r : bus.getRecordList()) {
            SaleOrderReturnBillDtl saleOrderReturnBillDtl = saleOrderReturnDtlMap.get(r.getSku());
            r.setPrice(saleOrderReturnBillDtl.getActPrice());
        }
    }

    /**
     * Session
     * 销售退货上传(入库任务)更新单据信息
     */
    public static void convertSaleOrderReturnBusinessIn(SaleOrderReturnBill saleOrderReturnBill, List<SaleOrderReturnBillDtl> saleOrderReturnBillDtls, Business bus) {
        Map<String, SaleOrderReturnBillDtl> detailMap = new HashMap<String, SaleOrderReturnBillDtl>();
        for (SaleOrderReturnBillDtl dtl : saleOrderReturnBillDtls) {
            detailMap.put(dtl.getSku(), dtl);
        }
        Double totInPrice = 0d;
        Map<String, BusinessDtl> businessDtlMap = new HashMap<>();
        for (BusinessDtl dtl : bus.getDtlList()) {
            SaleOrderReturnBillDtl saleOrderReturnBillDtl = detailMap.get(dtl.getSku());
            saleOrderReturnBillDtl.setInQty(saleOrderReturnBillDtl.getInQty() + dtl.getQty());
            saleOrderReturnBillDtl.setInVal(saleOrderReturnBillDtl.getQty() + saleOrderReturnBillDtl.getActPrice());
            saleOrderReturnBillDtl.setInStatus(BillConstant.BillDtlStatus.Ining);
            if (saleOrderReturnBillDtl.getInQty().intValue() == saleOrderReturnBillDtl.getQty().intValue()) {
                saleOrderReturnBillDtl.setInStatus(BillConstant.BillDtlStatus.InStore);
                saleOrderReturnBillDtl.setStatus(BillConstant.BillDtlStatus.InStore);
            }
            businessDtlMap.put(dtl.getSku(), dtl);
            totInPrice += saleOrderReturnBillDtl.getInVal();
            dtl.setPreVal(saleOrderReturnBillDtl.getInVal());
        }
        saleOrderReturnBill.setTotInQty(saleOrderReturnBill.getTotInQty() + bus.getTotEpc());
        saleOrderReturnBill.setTotOutVal(saleOrderReturnBill.getTotInQty() * saleOrderReturnBill.getActPrice());
        saleOrderReturnBill.setStatus(BillConstant.BillStatus.Doing);
        saleOrderReturnBill.setInStatus(BillConstant.BillInOutStatus.Ining);
        if (saleOrderReturnBill.getTotInQty().intValue() == saleOrderReturnBill.getTotQty().intValue()) {
            saleOrderReturnBill.setStatus(BillConstant.BillStatus.End);
            saleOrderReturnBill.setInStatus(BillConstant.BillInOutStatus.InStore);
        }
        bus.setTotPreVal(totInPrice);
        //销售出库更新任务成本
        Double totPreVal = 0D;
        List<BillRecord> billRecordList = new ArrayList<>();
        for (Record r : bus.getRecordList()) {
            BusinessDtl businessDtl = businessDtlMap.get(r.getSku());
            SaleOrderReturnBillDtl saleOrderReturnBillDtl = detailMap.get(r.getSku());
            BillRecord billRecord = new BillRecord(saleOrderReturnBillDtl.getBillNo() + "-" + r.getCode(), r.getCode(), saleOrderReturnBillDtl.getBillNo(), saleOrderReturnBillDtl.getSku());
            billRecordList.add(billRecord);
            r.setPrice(saleOrderReturnBillDtl.getActPrice());
            totPreVal += r.getPrice();
            businessDtl.setPreVal(businessDtl.getQty() * saleOrderReturnBillDtl.getActPrice());
        }
        saleOrderReturnBill.setBillRecordList(billRecordList);
        bus.setTotPreVal(totPreVal);
        bus.setDestId(saleOrderReturnBill.getDestId());
        bus.setDestUnitId(saleOrderReturnBill.getDestUnitId());
        bus.setOrigId(saleOrderReturnBill.getOrigId());
        bus.setOrigUnitId(saleOrderReturnBill.getOrigUnitId());

    }


    /**
     * Chen zhi fan
     * 将在库的code制成出库 Business， BusinessDtl，Record 的出库信息
     *
     * @param epcStock
     * @param currentUser
     * @return Business的出库信息
     * ArrayList<EpcStock> onlist
     */
    public static Business covertToInventoryBusinessOut(EpcStock epcStock, InventoryBill inventoryBill, User currentUser, String type) {
       /* Map<String, SaleOrderBillDtl> saleOrderBillDtlMap = new HashMap<>();
        List<BillRecord> billRecordList = new ArrayList<>();
        for (SaleOrderBillDtl billDtl : saleOrderBillDtlList) {
            saleOrderBillDtlMap.put(billDtl.getSku(), billDtl);
        }
        String taksId = "TSK" + CommonUtil.getDateString(new Date(), "yyyyMMdd") + System.currentTimeMillis();
        Business bus = new Business();
        Map<String, BusinessDtl> businessDtlMap = new HashMap<>();
        Map<String, String> styleCountMap = new HashMap<>();
        List<Record> recordList = new ArrayList<>();
        List<String> codeStrList = new ArrayList<>();
        Map<String,Record> recordMap = new HashMap<>();
        Double totOutPrice = 0d;
        for (Epc epc : epcList) {
            String skuOfEpc = epc.getSku();
            styleCountMap.put(epc.getStyleId(), epc.getStyleId());

            if (saleOrderBillDtlMap.containsKey(skuOfEpc)) {
                SaleOrderBillDtl billDtl = saleOrderBillDtlMap.get(skuOfEpc);
                BillRecord billRecord = new BillRecord(billDtl.getBillNo()+"-"+epc.getCode(),epc.getCode(),billDtl.getBillNo(),billDtl.getSku());
                billRecordList.add(billRecord);
                billDtl.setOutQty(billDtl.getOutQty() + 1);
                billDtl.setOutVal(billDtl.getOutQty() * billDtl.getPrice());
                billDtl.setOutStatus(BillConstant.BillDtlStatus.Outing);
                if (billDtl.getOutQty().intValue()+billDtl.getReturnQty() == billDtl.getQty().intValue()) {
                    billDtl.setOutStatus(BillConstant.BillDtlStatus.OutStore);
                    billDtl.setStatus(BillConstant.BillDtlStatus.OutStore);
                }
                totOutPrice += saleOrderBillDtlMap.get(skuOfEpc).getPrice();
                saleOrderBillDtlMap.put(skuOfEpc, billDtl);
            }
            if (businessDtlMap.containsKey(epc.getSku())) {
                BusinessDtl businessDtl = businessDtlMap.get(skuOfEpc);
                businessDtl.setQty(businessDtl.getQty() + 1);
                businessDtlMap.put(skuOfEpc, businessDtl);
            } else {
                BusinessDtl businessDtl = new BusinessDtl(taksId, currentUser.getOwnerId(), Constant.Token.Storage_Outbound, "KE000001", skuOfEpc, 1);
                businessDtl.setId(new GuidCreator().toString());
                businessDtl.setStyleId(epc.getStyleId());
                businessDtl.setColorId(epc.getColorId());
                businessDtl.setSizeId(epc.getSizeId());
                businessDtl.setType(Constant.TaskType.Outbound);
                businessDtl.setDestId(saleOrderBill.getDestId());
                businessDtl.setDestUnitId(saleOrderBill.getDestUnitId());
                businessDtl.setOrigId(saleOrderBill.getOrigId());
                businessDtl.setOrigUnitId(saleOrderBill.getOrigUnitId());
                businessDtl.setPreVal(0D);
                businessDtlMap.put(skuOfEpc, businessDtl);
            }
            Record record = new Record(epc.getCode(), taksId, taksId, Constant.Token.Storage_Outbound, "KE000001", "");
            record.setOwnerId(currentUser.getOwnerId());
            record.setDestId(saleOrderBill.getDestId());
            record.setDestUnitId(saleOrderBill.getDestUnitId());
            record.setOrigId(saleOrderBill.getOrigId());
            record.setOrigUnitId(saleOrderBill.getOrigUnitId());
            record.setSku(skuOfEpc);
            record.setStyleId(epc.getStyleId());
            record.setColorId(epc.getColorId());
            record.setSizeId(epc.getSizeId());
            record.setPrice(saleOrderBillDtlMap.get(skuOfEpc).getActPrice());
            record.setScanTime(new Date());
            record.setId(new GuidCreator().toString());
            record.setType(Constant.TaskType.Outbound);
            recordList.add(record);
            codeStrList.add(record.getCode());
            recordMap.put(record.getCode(),record);
        }
        bus.setDtlList(new ArrayList<>(businessDtlMap.values()));
        bus.setId(taksId);
        bus.setToken(Constant.Token.Storage_Outbound);
        bus.setBeginTime(new Date());
        bus.setEndTime(new Date());
        bus.setBillId(saleOrderBill.getBillNo());
        bus.setBillNo(saleOrderBill.getBillNo());
        bus.setDestId(saleOrderBill.getDestId());
        bus.setDestUnitId(saleOrderBill.getDestUnitId());
        bus.setOrigId(saleOrderBill.getOrigId());
        bus.setOrigUnitId(saleOrderBill.getOrigUnitId());
        bus.setOwnerId(currentUser.getOwnerId());
        bus.setDeviceId("KE000001");
        bus.setStatus(Constant.TaskStatus.Submitted);
        bus.setTotCarton(1L);
        bus.setTotEpc((long) epcList.size());
        bus.setTotPrice(totOutPrice);
        bus.setTotSku((long) bus.getDtlList().size());
        bus.setTotStyle((long) styleCountMap.size());
        bus.setType(Constant.TaskType.Outbound);
        bus.setRecordList(recordList);
        List<EpcStock> epcStockList = epcStockService.findEpcByCodes(TaskUtil.getSqlStrByList(codeStrList, EpcStock.class,"code"));
        Double totPreVal = 0D;
        for(EpcStock s : epcStockList){
            BusinessDtl dtl = businessDtlMap.get(s.getSku());
            Double preVal = CommonUtil.isBlank(s.getStockPrice())?0D:s.getStockPrice();
            dtl.setPreVal(dtl.getPreVal()+preVal);
            totPreVal += preVal;
            Record r = recordMap.get(s.getCode());
            r.setExtField(s.getInSotreType());
            r.setPrice(preVal);
            SaleOrderBillDtl saleOrderBillDtl = saleOrderBillDtlMap.get(s.getSku());
            saleOrderBillDtl.setStockVal(saleOrderBillDtl.getStockVal()+preVal);
            //设置利润
            saleOrderBillDtl.setProfit(saleOrderBillDtl.getOutVal()-saleOrderBillDtl.getStockVal());
            saleOrderBillDtl.setProfitRate(Double.parseDouble(CommonUtil.getDecimal(saleOrderBillDtl.getStockVal()/saleOrderBillDtl.getOutVal()*100,"######0.00")));

        }
        bus.setTotPreVal(totPreVal);
        saleOrderBill.setBillRecordList(billRecordList);
        saleOrderBill.setTotStockVal(saleOrderBill.getTotStockVal()+totPreVal);
        saleOrderBill.setProfit(saleOrderBill.getTotOutVal()-totPreVal);
        *//*//*///设置利润
      /*  saleOrderBill.setProfitRate(Double.parseDouble(CommonUtil.getDecimal(totPreVal/saleOrderBill.getTotOutVal()*100,"######0.00")));
        bus.setRecordList(new ArrayList<>(recordMap.values()));*//**//*

        saleOrderBill.setTotOutQty(saleOrderBill.getTotOutQty() + epcList.size());
        saleOrderBill.setTotOutVal(saleOrderBill.getTotOutVal() + totOutPrice);
        //设置利润
        saleOrderBill.setProfitRate(Double.parseDouble(CommonUtil.getDecimal(totPreVal/saleOrderBill.getTotOutVal()*100,"######0.00")));
        bus.setRecordList(new ArrayList<>(recordMap.values()));
        saleOrderBill.setStatus(BillConstant.BillStatus.Doing);
        saleOrderBill.setOutStatus(BillConstant.BillInOutStatus.Outing);
        if (saleOrderBill.getTotOutQty().intValue()+saleOrderBill.getTotRetrunQty() == saleOrderBill.getTotQty().intValue()) {
            saleOrderBill.setOutStatus(BillConstant.BillInOutStatus.OutStore);
            if(saleOrderBill.getCustomerTypeId().equals(BillConstant.customerType.Customer)){
                saleOrderBill.setStatus(BillConstant.BillStatus.End);
            }
        }*/
        String taksId = "TSK" + CommonUtil.getDateString(new Date(), "yyyyMMdd") + System.currentTimeMillis();
        Business bus = new Business();
        ArrayList<BusinessDtl> listDtl = new ArrayList<BusinessDtl>();
        ArrayList<Record> listrecord = new ArrayList<Record>();
        /*for(EpcStock epcStock:onlist){*/
        //BusinessDtl businessDtl = new BusinessDtl();
        BusinessDtl businessDtl = new BusinessDtl(taksId, currentUser.getOwnerId(), Constant.Token.Storage_Outbound, "KE000001", epcStock.getSku(), 1);
        businessDtl.setId(new GuidCreator().toString());
        businessDtl.setQty(1L);
        Double preVal = CommonUtil.isBlank(epcStock.getStockPrice()) ? 0D : epcStock.getStockPrice();
        businessDtl.setPreVal(preVal);
        if (type.equals("on")) {
            businessDtl.setDestId(epcStock.getOwnerId());
            businessDtl.setDestUnitId(epcStock.getWarehouseId());
            businessDtl.setToken(Constant.Token.Storage_Adjust_Outbound);
        }
        if (type.equals("no")) {
            businessDtl.setOrigId(epcStock.getOwnerId());
            businessDtl.setOrigUnitId(epcStock.getWarehouseId());
            businessDtl.setToken(Constant.Token.Storage_Adjust_Inbound);
        }
        businessDtl.setStyleId(epcStock.getStyleId());
        businessDtl.setColorId(epcStock.getColorId());
        businessDtl.setSizeId(epcStock.getSizeId());
        businessDtl.setType(Constant.TaskType.Outbound);

        listDtl.add(businessDtl);
        Record record = new Record(epcStock.getCode(), taksId, taksId, Constant.Token.Storage_Outbound, "KE000001", "");
        record.setOwnerId(currentUser.getOwnerId());
        record.setDestId(inventoryBill.getDestId());
        record.setDestUnitId(inventoryBill.getDestUnitId());
        record.setOrigId(inventoryBill.getOrigId());
        record.setOrigUnitId(inventoryBill.getOrigUnitId());
        record.setSku(inventoryBill.getSku());
        record.setStyleId(epcStock.getStyleId());
        record.setColorId(epcStock.getColorId());
        record.setSizeId(epcStock.getSizeId());
        record.setPrice(inventoryBill.getActPrice());
        record.setScanTime(new Date());
        record.setId(new GuidCreator().toString());
        record.setType(Constant.TaskType.Outbound);
        listrecord.add(record);
        /*  }*/
        bus.setDtlList(listDtl);
        bus.setRecordList(listrecord);
        bus.setId(taksId);
        bus.setToken(Constant.Token.Storage_Outbound);
        bus.setBeginTime(new Date());
        bus.setEndTime(new Date());
        bus.setBillId(inventoryBill.getBillNo());
        bus.setBillNo(inventoryBill.getBillNo());
        bus.setDestId(inventoryBill.getDestId());
        bus.setDestUnitId(inventoryBill.getDestUnitId());
        bus.setOrigId(inventoryBill.getOrigId());
        bus.setOrigUnitId(inventoryBill.getOrigUnitId());
        bus.setOwnerId(currentUser.getOwnerId());
        bus.setDeviceId("KE000001");
        bus.setStatus(Constant.TaskStatus.Submitted);
        bus.setTotCarton(1L);
        bus.setTotEpc(1L);
        if (type.equals("on")) {

            bus.setToken(Constant.Token.Storage_Adjust_Outbound);
        }
        if (type.equals("no")) {

            bus.setToken(Constant.Token.Storage_Adjust_Inbound);
        }
        /* bus.setTotPrice(totOutPrice);*/
        bus.setTotSku((long) bus.getDtlList().size());
        bus.setTotStyle((long) bus.getDtlList().size());
        bus.setType(Constant.TaskType.Outbound);

        return bus;
    }

    /**
     * 用于盘点任务上传时，保存当前库存数据。
     *
     * @param bus 单据任务
     */
    public static void convertToUploadInventoryRecord(Business bus) {
        if (CommonUtil.isNotBlank(bus.getOrigId())) {
            List<EpcStock> epcStockList = epcStockService.findStock(bus.getOrigId(),null,null,null);
            List<Record> recordList = bus.getRecordList();

            Map<String, String> recordMap = new HashMap<>();
            for (Record record : recordList) {
                recordMap.put(record.getCode(), record.getCode());
            }

            List<InventoryRecord> inventoryRecordList = new ArrayList<>();
            Date date = new Date();
            for (EpcStock epcStock : epcStockList) {
                InventoryRecord inventoryRecord = new InventoryRecord();
                inventoryRecord.setBillNo(bus.getBill().getBillNo());
                inventoryRecord.setCode(epcStock.getCode());
                inventoryRecord.setId(inventoryRecord.getBillNo() + "-" + inventoryRecord.getCode());
                inventoryRecord.setWarehouseId(epcStock.getWarehouseId());
                inventoryRecord.setSku(epcStock.getSku());
                inventoryRecord.setStyleId(epcStock.getStyleId());
                inventoryRecord.setColorId(epcStock.getColorId());
                inventoryRecord.setSizeId(epcStock.getSizeId());
                inventoryRecord.setRecordDate(date);
                inventoryRecord.setIsScanned(0);
                if (CommonUtil.isNotBlank(recordMap.get(inventoryRecord.getCode()))) {
                    inventoryRecord.setIsScanned(1);
                }
                inventoryRecordList.add(inventoryRecord);
            }
            bus.setInventoryRecordList(inventoryRecordList);
        }
    }

    public static void setEpcStockPrice(CodeFirstTime codeFirstTime, Record r, ArrayList<CodeFirstTime> list, String warehouseId) {
        if (CommonUtil.isBlank(codeFirstTime)) {
            CodeFirstTime newcodeFirstTime = new CodeFirstTime();
            newcodeFirstTime.setId(r.getCode() + "-" + warehouseId);
            newcodeFirstTime.setCode(r.getCode());
            newcodeFirstTime.setWarehouseId(warehouseId);
            newcodeFirstTime.setFirstTime(new Date());
            Unit unitByCode = CacheManager.getUnitByCode(warehouseId);
            if (CommonUtil.isNotBlank(unitByCode) && CommonUtil.isNotBlank(unitByCode.getGroupId()) && unitByCode.getGroupId().equals("JMS")) {
                newcodeFirstTime.setWarehousePrice(r.getPrice());
            } else {
                Style styleById = CacheManager.getStyleById(r.getStyleId());
                newcodeFirstTime.setWarehousePrice(styleById.getPreCast());
                r.setPrice(styleById.getPreCast());
            }
            list.add(newcodeFirstTime);
        } else {
            r.setPrice(codeFirstTime.getWarehousePrice());
        }
    }

    /**
     * @param replenishBill        补货单
     * @param replenishBillDtlList 补货单明细
     * @param curUser              当前用户
     *                             补货单数据保存
     */
    public static void covertToReplenishBill(ReplenishBill replenishBill, List<ReplenishBillDtl> replenishBillDtlList, User curUser) {
        int sumqty = 0;
        for (ReplenishBillDtl replenishBillDtl : replenishBillDtlList) {
            replenishBillDtl.setId(new GuidCreator().toString());
            replenishBillDtl.setBillId(replenishBill.getId());
            replenishBillDtl.setBillNo(replenishBill.getId());
            Style styleById = CacheManager.getStyleById(replenishBillDtl.getStyleId());
            replenishBillDtl.setClass1(styleById.getClass1());
            replenishBillDtl.setStyleName(styleById.getStyleName());
            if (replenishBillDtl.getQty() > replenishBillDtl.getActConvertQty()) {
                replenishBillDtl.setStatus(1);
            } else {
                replenishBillDtl.setStatus(0);
            }
            sumqty += replenishBillDtl.getQty();
        }
        replenishBill.setTotQty(Long.parseLong(sumqty + ""));
        replenishBill.setOwnerId(curUser.getOwnerId());
        replenishBill.setBillDate(new Date());
        replenishBill.setStatus(BillConstant.BillStatus.Enter);
    }

    /**
     * add by yushen
     * 用于小程序补货处理
     */
    public static void convertReplenishInProcessing(ReplenishBill replenishBill, List<ReplenishBillDtl> replenishBillDtlList, String option) throws Exception {
        Long totQty = replenishBill.getTotQty();

        Integer sumDtlConvertQty = 0;
        Integer sumDtlCancelQty = 0;
        for (ReplenishBillDtl dtl : replenishBillDtlList) {
            if ("CONVERT".equals(option)) {
                dtl.setActConvertQty(dtl.getActConvertQty() + dtl.getConvertQty());
                dtl.setConvertQty(0);
                dtl.setActConvertquitQty(dtl.getConvertquitQty() + dtl.getActConvertquitQty());
                dtl.setConvertquitQty(0);
            } else if ("CANCEL".equals(option)) {
                dtl.setActConvertQty(dtl.getActConvertQty() - dtl.getConvertQty());
                dtl.setConvertQty(0);
            }
            //设置明细状态
            if (dtl.getActConvertQty() + dtl.getActConvertquitQty() > dtl.getQty().intValue()) {
                throw new Exception(dtl.getSku() + "超出单据需求数量");
            }else if(dtl.getActConvertQty() == 0 && dtl.getActConvertquitQty() == 0) {//订单
                dtl.setStatus(BillConstant.replenishBillDtlStatus.Order);
            }else if(dtl.getActConvertQty() + dtl.getActConvertquitQty() < dtl.getQty().intValue()){//部分处理
                dtl.setStatus(BillConstant.replenishBillDtlStatus.Doing);
            }else if (dtl.getActConvertQty() == dtl.getQty().intValue()) {//全部处理
                dtl.setStatus(BillConstant.replenishBillDtlStatus.End);
            }else if (dtl.getActConvertQty() + dtl.getActConvertquitQty() == dtl.getQty().intValue() && dtl.getActConvertQty() > 0 && dtl.getActConvertquitQty() > 0) {//部分完成部分撤销
                dtl.setStatus(BillConstant.replenishBillDtlStatus.EndWithCancel);
            }else if(dtl.getActConvertquitQty() == dtl.getQty().intValue()){//全部撤销
                dtl.setStatus(BillConstant.replenishBillDtlStatus.Cancel);
            }
            sumDtlConvertQty += dtl.getActConvertQty();
            sumDtlCancelQty += dtl.getActConvertquitQty();
        }

        //设置表头状态
        if (sumDtlConvertQty == 0 && sumDtlCancelQty == 0) {//订单
            replenishBill.setStatus(BillConstant.BillStatus.Enter);
        } else if (sumDtlConvertQty + sumDtlCancelQty < totQty.intValue()) {//部分处理
            replenishBill.setStatus(BillConstant.BillStatus.Doing);
            replenishBill.setTotConvertQty(Long.valueOf(sumDtlConvertQty));
            replenishBill.setTotCancelQty(Long.valueOf(sumDtlCancelQty));
        } else if (sumDtlConvertQty == totQty.intValue()) {//全部处理
            replenishBill.setStatus(BillConstant.BillStatus.End);
            replenishBill.setTotConvertQty(Long.valueOf(sumDtlConvertQty));
        }else if(sumDtlConvertQty + sumDtlCancelQty == totQty.intValue() && sumDtlConvertQty > 0 && sumDtlCancelQty > 0) { //部分完成部分撤销
            replenishBill.setStatus(BillConstant.BillStatus.EndWithCancel);
            replenishBill.setTotConvertQty(Long.valueOf(sumDtlConvertQty));
            replenishBill.setTotCancelQty(Long.valueOf(sumDtlCancelQty));
        }else if(sumDtlCancelQty == totQty.intValue()){//全部撤销
            replenishBill.setStatus(BillConstant.BillStatus.ThirdPartyCancel);
            replenishBill.setTotCancelQty(Long.valueOf(sumDtlCancelQty));
        } else if (sumDtlConvertQty > totQty.intValue()) {
            throw new Exception("超出单据总需求数");
        }

    }

    /**
     * add by yushen 采购单入库后，反写补货单入库数量
     */
    public static void convertPurchaseToReplenish(PurchaseOrderBill purchaseOrderBill, List<PurchaseOrderBillDtl> purchaseOrderBillDtlList, ReplenishBill replenishBill, List<ReplenishBillDtl> replenishBillDtlList) {
        replenishBill.setTotInQty(purchaseOrderBill.getTotInQty() + replenishBill.getTotInQty());
        Map<String, PurchaseOrderBillDtl> purchaseDtlMap = new HashMap<>();
        for (PurchaseOrderBillDtl pDtl : purchaseOrderBillDtlList) {
            purchaseDtlMap.put(pDtl.getSku(), pDtl);
        }
        for (ReplenishBillDtl rDtl : replenishBillDtlList) {
            String currentSku = rDtl.getSku();
            if (CommonUtil.isNotBlank(purchaseDtlMap.get(currentSku))) {
                rDtl.setInQty(purchaseDtlMap.get(currentSku).getInQty() + rDtl.getInQty());
            }
        }
    }

    public static void covertToLabelChangeBill(LabelChangeBill labelChangeBill, List<LabelChangeBillDel> labelChangeBillDels, User curUser) {
        if (CommonUtil.isNotBlank(curUser)) {
            labelChangeBill.setOprId(curUser.getCode());
        }
        if (CommonUtil.isBlank(labelChangeBill.getOwnerId())) {
            labelChangeBill.setOwnerId(curUser.getOwnerId());
        }
        List<BillRecord> billRecordList = new ArrayList<>();
        for (LabelChangeBillDel dtl : labelChangeBillDels) {
            dtl.setId(new GuidCreator().toString());
            dtl.setBillId(labelChangeBill.getId());
            dtl.setBillNo(labelChangeBill.getBillNo());
            if (CommonUtil.isNotBlank(dtl.getUniqueCodes())) {
                for (String code : dtl.getUniqueCodes().split(",")) {
                    if(CommonUtil.isNotBlank(code)) {
                        BillRecord billRecord = new BillRecord(dtl.getBillNo() + "-" + code, code, dtl.getBillNo(), dtl.getSku());
                        billRecordList.add(billRecord);
                    }
                }
            }
        }
        labelChangeBill.setBillRecordList(billRecordList);
    }

    /*
     * add by czf
     * 用于标签管理出库
     */
    public static Business covertToLabelChangeBusinessOut(LabelChangeBill labelChangeBill, List<LabelChangeBillDel> labelChangeBillDelList, List<Epc> epcList, User currentUser) {
        Map<String, LabelChangeBillDel> labelChangeBillDelMap = new HashMap<>();
        List<BillRecord> billRecordList = new ArrayList<>();
        for (LabelChangeBillDel billDtl : labelChangeBillDelList) {
            labelChangeBillDelMap.put(billDtl.getSku(), billDtl);
        }
        String taksId = "TSK" + CommonUtil.getDateString(new Date(), "yyyyMMdd") + System.currentTimeMillis();
        Business bus = new Business();
        Map<String, BusinessDtl> businessDtlMap = new HashMap<>();
        Map<String, String> styleCountMap = new HashMap<>();
        List<Record> recordList = new ArrayList<>();
        List<String> codeStrList = new ArrayList<>();
        Map<String, Record> recordMap = new HashMap<>();
        Double totOutPrice = 0d;
        for (Epc epc : epcList) {
            String skuOfEpc = epc.getSku();
            styleCountMap.put(epc.getStyleId(), epc.getStyleId());

            if (labelChangeBillDelMap.containsKey(skuOfEpc)) {
                LabelChangeBillDel billDtl = labelChangeBillDelMap.get(skuOfEpc);
                BillRecord billRecord = new BillRecord(billDtl.getBillNo() + "-" + epc.getCode(), epc.getCode(), billDtl.getBillNo(), billDtl.getSku());
                billRecordList.add(billRecord);
                billDtl.setOutQty(billDtl.getOutQty() + 1);
                totOutPrice += labelChangeBillDelMap.get(skuOfEpc).getPrice();
                labelChangeBillDelMap.put(skuOfEpc, billDtl);
            }
            if (businessDtlMap.containsKey(epc.getSku())) {
                BusinessDtl businessDtl = businessDtlMap.get(skuOfEpc);
                businessDtl.setQty(businessDtl.getQty() + 1);
                businessDtlMap.put(skuOfEpc, businessDtl);
            } else {
                BusinessDtl businessDtl = new BusinessDtl(taksId, currentUser.getOwnerId(), Constant.Token.Storage_Outbound, "KE000001", skuOfEpc, 1);
                businessDtl.setId(new GuidCreator().toString());
                businessDtl.setStyleId(epc.getStyleId());
                businessDtl.setColorId(epc.getColorId());
                businessDtl.setSizeId(epc.getSizeId());
                businessDtl.setType(Constant.TaskType.Outbound);
                businessDtl.setDestId(labelChangeBill.getOrigId());
                businessDtl.setDestUnitId(labelChangeBill.getDestUnitId());
                businessDtl.setOrigId(labelChangeBill.getOrigId());
                businessDtl.setOrigUnitId(labelChangeBill.getOrigUnitId());
                businessDtl.setPreVal(0D);
                businessDtlMap.put(skuOfEpc, businessDtl);
            }
            Record record = new Record(epc.getCode(), taksId, taksId, Constant.Token.Storage_Outbound, "KE000001", "");
            record.setOwnerId(currentUser.getOwnerId());
            record.setDestId(labelChangeBill.getOrigId());
            record.setDestUnitId(labelChangeBill.getDestUnitId());
            record.setOrigId(labelChangeBill.getOrigId());
            record.setOrigUnitId(labelChangeBill.getOrigUnitId());
            record.setSku(skuOfEpc);
            record.setStyleId(epc.getStyleId());
            record.setColorId(epc.getColorId());
            record.setSizeId(epc.getSizeId());
            record.setPrice(labelChangeBillDelMap.get(skuOfEpc).getActPrice());
            record.setScanTime(new Date());
            record.setId(new GuidCreator().toString());
            record.setType(Constant.TaskType.Outbound);
            recordList.add(record);
            codeStrList.add(record.getCode());
            recordMap.put(record.getCode(), record);
        }
        bus.setDtlList(new ArrayList<>(businessDtlMap.values()));
        bus.setId(taksId);
        bus.setToken(Constant.Token.Storage_Adjust_Outbound);
        bus.setBeginTime(new Date());
        bus.setEndTime(new Date());
        bus.setBillId(labelChangeBill.getBillNo());
        bus.setBillNo(labelChangeBill.getBillNo());
        bus.setDestId(labelChangeBill.getOrigId());
        bus.setDestUnitId(labelChangeBill.getDestUnitId());
        bus.setOrigId(labelChangeBill.getOrigId());
        bus.setOrigUnitId(labelChangeBill.getOrigUnitId());
        bus.setOwnerId(currentUser.getOwnerId());
        bus.setDeviceId("KE000001");
        bus.setStatus(Constant.TaskStatus.Submitted);
        bus.setTotCarton(1L);
        bus.setTotEpc((long) epcList.size());
        bus.setTotPrice(totOutPrice);
        bus.setTotSku((long) bus.getDtlList().size());
        bus.setTotStyle((long) styleCountMap.size());
        bus.setType(Constant.TaskType.Outbound);
        bus.setRecordList(recordList);
        labelChangeBill.setBillRecordList(billRecordList);

        return bus;
    }

    /*
     *add by czf
     * 用于标签管理入库
     */
    public static Business covertToLabelChangeBusinessIn(LabelChangeBill labelChangeBill, List<LabelChangeBillDel> labelChangeBillDelList, List<Epc> epcList, User currentUser) {
        Map<String, LabelChangeBillDel> labelChangeBillDelMap = new HashMap<>();
        for (LabelChangeBillDel dtl : labelChangeBillDelList) {
            if (labelChangeBill.getChangeType().equals(BillConstant.ChangeType.Series)) {
                boolean isUseOldStyle=false;
                String styleId = dtl.getStyleId();
                //判断最后两位是有AA,AS,PD
                int styleIdLength = styleId.length();
                String styleTail=styleId.substring(styleIdLength-2,styleIdLength);
                if(styleTail.equals(BillConstant.styleNew.Alice)||styleTail.equals(BillConstant.styleNew.AncientStone)){
                    styleId=styleId.substring(0,styleIdLength-2);
                    Style style= CacheManager.getStyleById(styleId);
                    isUseOldStyle = !CommonUtil.isBlank(style);
                }
                String stylePDTail=styleId.substring(styleIdLength-4,styleIdLength-2);
                if(stylePDTail.equals(BillConstant.styleNew.PriceDiscount)){
                    styleId=styleId.substring(0,styleIdLength-4);
                }
                if(!isUseOldStyle){
                    labelChangeBillDelMap.put(styleId + labelChangeBill.getNowclass9().split("-")[1] + dtl.getColorId() + dtl.getSizeId(), dtl);
                }else{
                    labelChangeBillDelMap.put(styleId + dtl.getColorId() + dtl.getSizeId(), dtl);
                }

            }
            if (labelChangeBill.getChangeType().equals(BillConstant.ChangeType.Price)) {
                labelChangeBillDelMap.put(dtl.getStyleId() + BillConstant.styleNew.PriceDiscount +CommonUtil.getInt(dtl.getDiscount())+ dtl.getColorId() + dtl.getSizeId(), dtl);
            }
            if (labelChangeBill.getChangeType().equals(BillConstant.ChangeType.Shop)) {
                boolean isUseOldStyle=false;
                String styleId = dtl.getStyleId();
                //判断最后两位是有AA,AS,PD
                int styleIdLength = styleId.length();
                String styleTail=styleId.substring(styleIdLength-2,styleIdLength);
                if(styleTail.equals(BillConstant.styleNew.Shop)){
                    styleId=styleId.substring(0,styleIdLength-2);
                    Style style= CacheManager.getStyleById(styleId);
                    isUseOldStyle = !CommonUtil.isBlank(style);
                }
                String stylePDTail=styleId.substring(styleIdLength-4,styleIdLength-2);
                if(stylePDTail.equals(BillConstant.styleNew.PriceDiscount)){
                    styleId=styleId.substring(0,styleIdLength-4);
                }
                if(!isUseOldStyle){
                    labelChangeBillDelMap.put(styleId + labelChangeBill.getChangeType() + dtl.getColorId() + dtl.getSizeId(), dtl);
                }else{
                    labelChangeBillDelMap.put(styleId + dtl.getColorId() + dtl.getSizeId(), dtl);
                }

            }

        }
        String taksId = "TSK" + CommonUtil.getDateString(new Date(), "yyyyMMdd") + System.currentTimeMillis();
        Business bus = new Business();
        Map<String, BusinessDtl> businessDtlMap = new HashMap<>();
        Map<String, String> styleCountMap = new HashMap<>();
        List<Record> recordList = new ArrayList<>();
        Double totRcvPrice = 0d;
        for (Epc e : epcList) {
            String sku = e.getSku();
            styleCountMap.put(e.getStyleId(), e.getStyleId());
            if (labelChangeBillDelMap.containsKey(sku)) {
                LabelChangeBillDel dtl = labelChangeBillDelMap.get(sku);
                dtl.setInQty(dtl.getInQty() + 1);

                totRcvPrice += dtl.getPrice();
                labelChangeBillDelMap.put(sku, dtl);
                //dtl.setStockVal(dtl.getInVal());
            }
            if (businessDtlMap.containsKey(e.getSku())) {
                BusinessDtl dtl = businessDtlMap.get(sku);
                dtl.setQty(dtl.getQty() + 1);
                businessDtlMap.put(sku, dtl);
            } else {
                BusinessDtl dtl = new BusinessDtl(taksId, currentUser.getOwnerId(), Constant.Token.Storage_Adjust_Inbound, "KE000001", sku, 1);
                dtl.setId(new GuidCreator().toString());
                dtl.setStyleId(e.getStyleId());
                dtl.setColorId(e.getColorId());
                dtl.setSizeId(e.getSizeId());
                dtl.setType(Constant.TaskType.Inbound);
                dtl.setDestId(labelChangeBill.getOrigId());
                dtl.setDestUnitId(labelChangeBill.getDestUnitId());
                dtl.setOrigId(labelChangeBill.getOrigId());
                dtl.setOrigUnitId(labelChangeBill.getOrigUnitId());
                businessDtlMap.put(sku, dtl);
            }
            Record record = new Record(e.getCode(), taksId, taksId, Constant.Token.Storage_Adjust_Inbound, "KE000001", "");
            record.setOwnerId(currentUser.getOwnerId());
            record.setDestId(labelChangeBill.getOrigId());
            record.setDestUnitId(labelChangeBill.getDestUnitId());
            record.setOrigId(labelChangeBill.getOrigId());
            record.setOrigUnitId(labelChangeBill.getOrigUnitId());
            record.setSku(sku);
            record.setStyleId(e.getStyleId());
            record.setColorId(e.getColorId());
            record.setSizeId(e.getSizeId());
            record.setPrice(labelChangeBillDelMap.get(record.getSku()).getActPrice());
            record.setScanTime(new Date());
            LabelChangeBillDel labelChangeBillDel = labelChangeBillDelMap.get(record.getSku());
            //record.setExtField(labelChangeBillDel.getInStockType());//record中增加入库类型
            record.setId(new GuidCreator().toString());
            record.setType(Constant.TaskType.Inbound);
            recordList.add(record);
        }
        bus.setDtlList(new ArrayList<>(businessDtlMap.values()));
        bus.setId(taksId);
        bus.setToken(Constant.Token.Storage_Adjust_Inbound);
        bus.setBeginTime(new Date());
        bus.setEndTime(new Date());
        bus.setBillId(labelChangeBill.getBillNo());
        bus.setBillNo(labelChangeBill.getBillNo());
        bus.setDestId(labelChangeBill.getOrigId());
        bus.setDestUnitId(labelChangeBill.getDestUnitId());
        bus.setOrigId(labelChangeBill.getOrigId());
        bus.setOrigUnitId(labelChangeBill.getOrigUnitId());
        bus.setOwnerId(currentUser.getOwnerId());
        bus.setDeviceId("KE000001");
        bus.setStatus(Constant.TaskStatus.Submitted);
        bus.setTotCarton(1L);
        bus.setTotEpc((long) epcList.size());
        bus.setTotPrice(totRcvPrice);
        bus.setTotSku((long) bus.getDtlList().size());
        bus.setTotStyle((long) styleCountMap.size());
        bus.setType(Constant.TaskType.Inbound);
        bus.setRecordList(recordList);
        bus.setTotPreVal(totRcvPrice);
        return bus;
    }
    /*add by czf
     *保存异常code信息的
     */
    public static List<AbnormalCodeMessage> fullAbnormalCodeMessage(List< ? extends Object> DtlList,Integer type,String code) throws Exception{
       /* //获取AbnormalCodeMessage反射对象
        Class<AbnormalCodeMessage> CodeClass = (Class<AbnormalCodeMessage>) Class.forName("com.casesoft.dmc.model.logistics.AbnormalCodeMessage");
        Map<String,String> AbnormalCodeMessageMap=new HashMap<String,String>();//临时保存AbnormalCodeMessage的数据*/
        List<AbnormalCodeMessage> list=new ArrayList<AbnormalCodeMessage>();
        //判断泛型中noOutPutCode是否存在，是否有数据
        if(CommonUtil.isNotBlank(DtlList)&&DtlList.size()>0){
             for(Object t:DtlList){
                 Class<?> aClass = t.getClass();
                 Field[] fields = aClass.getDeclaredFields();
                 for (Field field : fields) {
                     field.setAccessible(true);
                   /*  System.out.println(field.getName());*/
                      if(field.getName().equals("noOutPutCode")){
                          Method m = aClass.getMethod("get"+getMethodName(field.getName()));
                          String noOutPutCode = (String) m.invoke(t);// 调用getter方法获取属性值
                          if(CommonUtil.isNotBlank(noOutPutCode)){
                              String[] noOutPutCodes = noOutPutCode.split(",");
                              for(int i=0;i<noOutPutCodes.length;i++){
                                  AbnormalCodeMessage abnormalCodeMessage=new AbnormalCodeMessage();
                                  abnormalCodeMessage.setCode(noOutPutCodes[i]);
                                  Method mBillNo = aClass.getMethod("getBillNo");
                                  abnormalCodeMessage.setBillNo((String) mBillNo.invoke(t));
                                  Method mColorId = aClass.getMethod("getColorId");
                                  abnormalCodeMessage.setColorId((String) mColorId.invoke(t));
                                  Method mSku = aClass.getMethod("getSku");
                                  abnormalCodeMessage.setSku((String) mSku.invoke(t));
                                  Method mStyleId = aClass.getMethod("getStyleId");
                                  abnormalCodeMessage.setStyleId((String) mStyleId.invoke(t));
                                  Method mSizeId = aClass.getMethod("getSizeId");
                                  abnormalCodeMessage.setSizeId((String) mSizeId.invoke(t));
                                  abnormalCodeMessage.setId(mBillNo.invoke(t) +"_"+noOutPutCodes[i]);
                                  if(code.indexOf(noOutPutCodes[i])!=-1){
                                      abnormalCodeMessage.setStatus(0);
                                  }else {
                                      abnormalCodeMessage.setStatus(1);
                                  }
                                  abnormalCodeMessage.setType(type);
                                  list.add(abnormalCodeMessage);
                              }
                          }

                      }
                 }
             }
        }


        return list;
    }
    /*add by czf
     *把一个字符串的第一个字母大写、效率是最高的
     */
    private static String getMethodName(String fildeName) throws Exception{
        byte[] items = fildeName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }
    /*add by czf
     *销售单转成调拨申请单
     */
    public static Map<String,Object> saleChangeTr(List<SaleOrderBillDtl> billDtlByBillNo,SaleOrderBill saleOrderBill,Unit unit,User user){
        Map<String,Object> map=new HashMap<String,Object>();
        TransferOrderBill transferOrderBill=new TransferOrderBill();
        List<TransferOrderBillDtl> saveList=new ArrayList<TransferOrderBillDtl>();
        String prefix = BillConstant.BillPrefix.Transfer
                + CommonUtil.getDateString(new Date(), "yyMMddHHmmssSSS");
        //填充调拨申请单详情的数据
        Long totQty=0L;
        for(SaleOrderBillDtl saleOrderBillDtl:billDtlByBillNo){
            if(CommonUtil.isNotBlank(saleOrderBillDtl.getChangeTRqty())&&saleOrderBillDtl.getChangeTRqty()>0){
                TransferOrderBillDtl transferOrderBillDtl=new TransferOrderBillDtl();
                transferOrderBillDtl.setId((new GuidCreator().toString()));
                transferOrderBillDtl.setQty(Long.parseLong(saleOrderBillDtl.getChangeTRqty()+""));
                transferOrderBillDtl.setActQty(Long.parseLong(saleOrderBillDtl.getChangeTRqty()+""));
                transferOrderBillDtl.setActPrice(0D);
                transferOrderBillDtl.setBillId(prefix);
                transferOrderBillDtl.setBillNo(prefix);
                transferOrderBillDtl.setSizeId(saleOrderBillDtl.getSizeId());
                transferOrderBillDtl.setStyleId(saleOrderBillDtl.getStyleId());
                transferOrderBillDtl.setColorId(saleOrderBillDtl.getColorId());
                transferOrderBillDtl.setPrice(saleOrderBillDtl.getPrice());
                transferOrderBillDtl.setInQty(0);
                transferOrderBillDtl.setManualQty(0L);
                transferOrderBillDtl.setPreManualQty(0L);
                transferOrderBillDtl.setScanQty(0L);
                transferOrderBillDtl.setSku(saleOrderBillDtl.getSku());
                transferOrderBillDtl.setTotActPrice(0D);
                transferOrderBillDtl.setTotPrice(0D);
                transferOrderBillDtl.setOutVal(0D);
                transferOrderBillDtl.setOutQty(0);
                transferOrderBillDtl.setInQty(0);
                transferOrderBillDtl.setInVal(0D);
                transferOrderBillDtl.setInStatus(0);
                transferOrderBillDtl.setOutStatus(0);
                transferOrderBillDtl.setDiscount(100D);
                totQty+=Long.parseLong(saleOrderBillDtl.getChangeTRqty()+"");
                saveList.add(transferOrderBillDtl);
            }
        }
        map.put("billDel",saveList);
        //填充调拨申请单详情的数据
        if(saveList.size()>0){
            transferOrderBill.setId(prefix);
            transferOrderBill.setActPrice(0D);
            transferOrderBill.setActQty(0L);
            transferOrderBill.setBillDate(new Date());
            transferOrderBill.setBillNo(prefix);
            transferOrderBill.setDestId(saleOrderBill.getOrigId());
            transferOrderBill.setDestName(saleOrderBill.getOrigName());
            transferOrderBill.setDestUnitId(saleOrderBill.getOrigUnitId());
            transferOrderBill.setDestUnitName(saleOrderBill.getOrigUnitName());
            transferOrderBill.setOrigId(unit.getId());
            transferOrderBill.setOrigName(unit.getName());
            transferOrderBill.setOrigUnitId(unit.getOwnerId());
            if(CommonUtil.isNotBlank(CacheManager.getUnitById(unit.getOwnerId()))) {
                transferOrderBill.setOrigUnitName(CacheManager.getUnitById(unit.getOwnerId()).getName());
            }
            transferOrderBill.setOwnerId(user.getOwnerId());
            transferOrderBill.setOprId(user.getId());
            transferOrderBill.setPayPrice(0D);
            transferOrderBill.setStatus(BillConstant.BillStatus.Doing);
            transferOrderBill.setTotQty(totQty);
            transferOrderBill.setTotOutVal(0D);
            transferOrderBill.setTotOutQty(0L);
            transferOrderBill.setTotInQty(0L);
            transferOrderBill.setTotInVal(0D);
            transferOrderBill.setInStatus(0);
            transferOrderBill.setOutStatus(0);
            transferOrderBill.setSrcBillNo(saleOrderBill.getId());
        }
        map.put("bill",transferOrderBill);
        return map;
    }

    /**
     * 将Bill转为调拨单
     * 客户端生成单据上传时调用
     * @param transferOrderBill //转换后单据信息
     * @param transferOrderBillDtlList  //转换后单据明细信息
     * @param bill //转换前单据信息
     * @param billDtlList //转换前单据明细信息
     * @param bus 任务单信息
     * **/
    public static void convertBillTransferOrderBill(TransferOrderBill transferOrderBill, List<TransferOrderBillDtl> transferOrderBillDtlList, Bill bill, List<BillDtl> billDtlList,Business bus) {
        Double totPrice = 0D;
        transferOrderBill = new TransferOrderBill();
        transferOrderBill.setId(bill.getBillNo());
        transferOrderBill.setBillDate(bill.getBillDate());
        transferOrderBill.setBillType("S");
        transferOrderBill.setStatus(BillConstant.BillStatus.Enter);
        transferOrderBill.setTotQty(bill.getTotQty());
        transferOrderBill.setActQty(bill.getActQty());
        transferOrderBill.setSkuQty(bill.getSkuQty());
        transferOrderBill.setOrigId(bill.getOrigId());
        Unit orig = CacheManager.getUnitByCode(bill.getOrigUnitId());
        if(CommonUtil.isNotBlank(orig)){
            transferOrderBill.setOrigName(orig.getName());
        }
        transferOrderBill.setOrigUnitId(bill.getOrigUnitId());
        transferOrderBill.setDestId(bill.getDestId());
        transferOrderBill.setDestUnitId(bill.getDestUnitId());
        transferOrderBill.setOprId(bus.getOperator());
        transferOrderBill.setRemark(bill.getRemark());
        for(BillDtl billDtl : billDtlList){
            TransferOrderBillDtl trDtl = new TransferOrderBillDtl();
            trDtl.setId(new GuidCreator().toString());
            trDtl.setBillId(transferOrderBill.getId());
            trDtl.setBillNo(transferOrderBill.getBillNo());
            trDtl.setStyleId(billDtl.getStyleId());
            trDtl.setColorId(billDtl.getColorId());
            trDtl.setSizeId(billDtl.getSizeId());
            Style style = CacheManager.getStyleById(billDtl.getStyleId());
            trDtl.setPrice(style.getPrice());
            trDtl.setTotPrice(billDtl.getQty()*trDtl.getPrice());
            totPrice+= trDtl.getTotPrice();
            trDtl.setSku(billDtl.getSku());
        }
        transferOrderBill.setTotPrice(totPrice);
    }
}
