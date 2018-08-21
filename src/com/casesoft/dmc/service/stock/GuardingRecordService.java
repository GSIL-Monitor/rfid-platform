package com.casesoft.dmc.service.stock;

import com.casesoft.dmc.controller.task.TaskUtil;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.stock.GuardingRecordDao;
import com.casesoft.dmc.model.cfg.Device;
import com.casesoft.dmc.model.stock.EpcStock;
import com.casesoft.dmc.model.stock.GuardingRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yushen on 2017/9/13.
 */
@Service
@Transactional
public class GuardingRecordService extends AbstractBaseService {
    @Autowired
    private GuardingRecordDao guardingRecordDao;

    @Override
    public Page findPage(Page page, List list) {
        return null;
    }

    public Page<GuardingRecord> findPages(Page<GuardingRecord> page, List<PropertyFilter> filters) {
        return this.guardingRecordDao.findPage(page, filters);
    }
    @Override
    public void save(Object entity) {

    }

    @Override
    public Object load(Serializable id) {
        return null;
    }

    @Override
    public Object get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List getAll() {
        return null;
    }

    @Override
    public void update(Object entity) {

    }

    @Override
    public void delete(Object entity) {

    }

    @Override
    public void delete(Serializable id) {

    }

    @Override
    public List findAll() {
        return null;
    }

    @Override
    public List find(List list) {
        return null;
    }

    public List<EpcStock> findEpcCodes(String codes) {
        return this.guardingRecordDao.find("from EpcStock epcstock where " + codes);
    }

    public void saveRecord(List<String> uniqueCodeList, Device device, List<EpcStock> alertEpcStockList) {

        List<EpcStock> epcStockList = this.findEpcCodes(TaskUtil.getSqlStrByList(uniqueCodeList, EpcStock.class, "code"));

        Map<String, EpcStock> alertEpcStockMap = new HashMap<>();
        for (EpcStock epcStock : alertEpcStockList) {
            alertEpcStockMap.put(epcStock.getCode(), epcStock);
        }

        for (EpcStock epcStock : epcStockList) {
            GuardingRecord guardingRecord = new GuardingRecord();
            guardingRecord.setId(new GuidCreator().toString());
            guardingRecord.setCode(epcStock.getCode());
            guardingRecord.setRecordDate(new Date());
            guardingRecord.setDeviceId(device.getId());
            guardingRecord.setDeviceOwnerId(device.getOwnerId());
            if (CommonUtil.isNotBlank(alertEpcStockMap.get(epcStock.getCode()))) {
                guardingRecord.setIsAlert("Y");
            }
            guardingRecord.setStyleId(epcStock.getStyleId());
            guardingRecord.setColorId(epcStock.getColorId());
            guardingRecord.setSizeId(epcStock.getSizeId());
            this.guardingRecordDao.saveOrUpdate(guardingRecord);
        }
    }
}
