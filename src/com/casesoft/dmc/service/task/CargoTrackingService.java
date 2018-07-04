package com.casesoft.dmc.service.task;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.task.TaskUtil;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.task.TaskDao;
import com.casesoft.dmc.dao.trace.RecordDao;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yushen on 2017/9/19.
 */

@Service
@Transactional
public class CargoTrackingService implements IBaseService<Business, String> {
    @Autowired
    private RecordDao recordDao;

    @Autowired
    private TaskDao taskDao;

    @Override
    public Page<Business> findPage(Page<Business> page, List<PropertyFilter> filters) {
        List<Record> taskRecordList = this.recordDao.find(filters);
//        List<String> taskRecordTaskIdList = new ArrayList<>();
//        for (Record record : taskRecordList) {
//            taskRecordTaskIdList.add(record.getTaskId());
//        }
//        String taskIds = TaskUtil.getSqlStrByList(taskRecordTaskIdList, Business.class, "id");
//        Page<Business> businessPage = taskDao.findPage(page, "from Business business where" + taskIds);

        StringBuilder taskIdListString = new StringBuilder();
        for (int i = 0; i < taskRecordList.size(); i++) {
            taskIdListString.append(taskRecordList.get(i).getTaskId());
            if(i<taskRecordList.size()-1){
                taskIdListString.append(",");
            }
        }

        ArrayList<PropertyFilter> businessPageFilters = new ArrayList<>();
        PropertyFilter businessPageFilter = new PropertyFilter("INS_id", taskIdListString.toString());
        businessPageFilters.add(businessPageFilter);
        Page<Business> businessPage = this.taskDao.findPage(page, businessPageFilters);

        List<Business> businessRows = businessPage.getRows();
        for (Business business : businessRows) {
            if (CommonUtil.isNotBlank(business.getDestId())) {
                Unit destUnit = CacheManager.getUnitById(business.getDestId());
                if(CommonUtil.isNotBlank(destUnit)){
                    business.setDestName(destUnit.getName());
                }
            }
            if (CommonUtil.isNotBlank(business.getDestUnitId())) {
                if (CommonUtil.isNotBlank(CacheManager.getUnitById(business.getDestUnitId()))) {
                    business.setDestUnitName(CacheManager.getUnitById(business.getDestUnitId()).getName());
                } else if(CommonUtil.isNotBlank(CacheManager.getCustomerById(business.getDestUnitId()))){
                    business.setDestUnitName(CacheManager.getCustomerById(business.getDestUnitId()).getName());
                }
            }
            if (CommonUtil.isNotBlank(business.getOrigId())) {
                Unit origUnit = CacheManager.getUnitById(business.getOrigId());
                if(CommonUtil.isNotBlank(origUnit)){
                    business.setOrigName(origUnit.getName());
                }
            }
            if (CommonUtil.isNotBlank(business.getOrigUnitId())) {
                if (CommonUtil.isNotBlank(CacheManager.getUnitById(business.getOrigUnitId()))) {
                    business.setOrigUnitName(CacheManager.getUnitById(business.getOrigUnitId()).getName());
                } else if(CommonUtil.isNotBlank(CacheManager.getCustomerById(business.getOrigUnitId()))){
                    business.setOrigUnitName(CacheManager.getCustomerById(business.getOrigUnitId()).getName());
                }
            }
        }
        return businessPage;
    }

    @Override
    public void save(Business entity) {

    }

    @Override
    public Business load(String id) {
        return null;
    }

    @Override
    public Business get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<Business> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<Business> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(Business entity) {

    }

    @Override
    public void delete(Business entity) {

    }

    @Override
    public void delete(String id) {

    }
}
