package com.casesoft.dmc.controller.syn.tool;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.task.*;

/**
 * Created by john on 2017/1/12.
 * <p>
 * 转换单据-任务工具类
 */
public class TaskAdjustUtil {
    public static void adjustStorageId(Business bus) {
        Bill bill = bus.getBill();
        if (CommonUtil.isBlank(bill)) {
            return;
        }
        if (CommonUtil.isNotBlank(bill.getOrigUnitId())) {
            Unit u = CacheManager.getUnitByCode(bill.getOrigUnitId());
            if (CommonUtil.isNotBlank(u)) {
                if (u.getType() == Constant.UnitType.Vender ||
                        u.getType() == Constant.UnitType.Agent
                        || u.getType() == Constant.UnitType.Headquarters) {
                    bus.setOrigUnitId(u.getId());
                }
                if (u.getType() == Constant.UnitType.Warehouse
                        || u.getType() == Constant.UnitType.Shop) {
                    bus.setOrigId(u.getId());
                    bus.setOrigUnitId(u.getOwnerId());
                }
            }
        }
        if (CommonUtil.isNotBlank(bill.getOrigId())) {
            Unit u = CacheManager.getUnitByCode(bill.getOrigId());
            if (CommonUtil.isNotBlank(u)) {
                if (u.getType() == Constant.UnitType.Vender ||
                        u.getType() == Constant.UnitType.Agent
                        || u.getType() == Constant.UnitType.Headquarters) {
                    bus.setOrigUnitId(u.getId());
                    bus.setOrigId("");
                }
                if (u.getType() == Constant.UnitType.Warehouse
                        || u.getType() == Constant.UnitType.Shop) {
                    bus.setOrigId(u.getId());
                    bus.setOrigUnitId(u.getOwnerId());
                }
            }
        }
        if (CommonUtil.isNotBlank(bill.getDestUnitId())) {
            Unit u = CacheManager.getUnitByCode(bill.getDestUnitId());
            if (CommonUtil.isNotBlank(u)) {
                if (u.getType() == Constant.UnitType.Vender ||
                        u.getType() == Constant.UnitType.Agent
                        || u.getType() == Constant.UnitType.Headquarters) {
                    bus.setDestUnitId(u.getId());
                }
                if (u.getType() == Constant.UnitType.Warehouse
                        || u.getType() == Constant.UnitType.Shop) {
                    bus.setDestId(u.getId());
                    bus.setDestUnitId(u.getOwnerId());
                }
            }
        }
        if (CommonUtil.isNotBlank(bill.getDestId())) {
            Unit u = CacheManager.getUnitByCode(bill.getDestId());
            if (CommonUtil.isNotBlank(u)) {
                if (u.getType() == Constant.UnitType.Vender ||
                        u.getType() == Constant.UnitType.Agent
                        || u.getType() == Constant.UnitType.Headquarters) {
                    bus.setDestUnitId(u.getId());
                    bus.setDestId("");
                }
                if (u.getType() == Constant.UnitType.Warehouse
                        || u.getType() == Constant.UnitType.Shop) {
                    bus.setDestId(u.getId());
                    bus.setDestUnitId(u.getOwnerId());
                }
            }
        }
        copyBusinessUnitInfo(bus);

    }

    /**
     * 拷贝任务信息
     *
     * @param bus
     */
    private static void copyBusinessUnitInfo(Business bus) {
        for (BusinessDtl dtl : bus.getDtlList()) {
            dtl.setOrigId(bus.getOrigId());
            dtl.setOrigUnitId(bus.getOrigUnitId());
            dtl.setDestUnitId(bus.getDestUnitId());
            dtl.setDestId(bus.getDestId());
        }
        for (Box box : bus.getBoxList()) {

            for (BoxDtl dtl : box.getBoxDtls()) {

            }
        }
        for (Record r : bus.getRecordList()) {
            r.setOrigId(bus.getOrigId());
            r.setOrigUnitId(bus.getOrigUnitId());
            r.setDestUnitId(bus.getDestUnitId());
            r.setDestId(bus.getDestId());
        }
    }
}
