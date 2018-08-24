package com.casesoft.dmc.service.cfg;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.cfg.DeviceRunLogDao;
import com.casesoft.dmc.model.cfg.DeviceRunLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by WinLi on 2017-03-30.
 */
@Service
@Transactional
public class DeviceRunLogService {
    @Autowired
    private DeviceRunLogDao deviceRunLogDao;

    @Transactional(readOnly = true)
    public Page<DeviceRunLog> findPage(Page<DeviceRunLog> page, List<PropertyFilter> filters) {
        return this.deviceRunLogDao.findPage(page, filters);
    }

    public void save(DeviceRunLog entity) {
        this.deviceRunLogDao.saveOrUpdate(entity);
    }
    @Transactional(readOnly = true)
    public DeviceRunLog findLastDayLog(String deviceId, String logDate) {
        return this.deviceRunLogDao.findUnique("from DeviceRunLog log where log.deviceId=? and log.logDate=?",
                new Object[]{deviceId,logDate});
    }


    public void deleteRunLog(String deviceId, String logDate) {
        this.deviceRunLogDao.batchExecute("delete from DeviceRunLog log where log.deviceId=? and log.logDate=?",
                deviceId,logDate);
    }
}
