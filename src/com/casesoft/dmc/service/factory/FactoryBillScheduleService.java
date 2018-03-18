package com.casesoft.dmc.service.factory;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.factory.FactoryBillScheduleDao;
import com.casesoft.dmc.extend.third.request.BaseService;
import com.casesoft.dmc.model.factory.BillSchedule;
import com.casesoft.dmc.model.factory.FactoryBill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GuoJunwen on 2017-05-17.
 */
@Service
@Transactional
public class FactoryBillScheduleService extends BaseService<BillSchedule,String> {

    @Autowired
    private FactoryBillScheduleDao factoryBillScheduleDao;

    @Override
    public Page<BillSchedule> findPage(Page<BillSchedule> page, List<PropertyFilter> filters) {
        return this.factoryBillScheduleDao.findPage(page,filters);
    }

    public void saveList(List<FactoryBill> billList, List<BillSchedule> scheduleList) {

        List<String> billNoStrList = new ArrayList<String>();
        for (FactoryBill bill : billList) {
            billNoStrList.add(bill.getBillNo());
        }
        String inBillNo = CommonUtil.getSqlStrByList(billNoStrList, BillSchedule.class, "billNo");
        this.factoryBillScheduleDao.batchExecute("delete BillSchedule billschedule  where "+inBillNo);
        this.factoryBillScheduleDao.doBatchInsert(billList);
        this.factoryBillScheduleDao.doBatchInsert(scheduleList);
    }

    @Override
    public void save(BillSchedule entity) {
         this.factoryBillScheduleDao.saveOrUpdate(entity);
    }

    @Override
    public BillSchedule load(String id) {
        return null;
    }

    @Override
    public BillSchedule get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<BillSchedule> find(List<PropertyFilter> filters) {
        return this.factoryBillScheduleDao.find(filters);
    }

    @Override
    public List<BillSchedule> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(BillSchedule entity) {

    }

    @Override
    public void delete(BillSchedule entity) {
         this.factoryBillScheduleDao.delete(entity);
    }

    @Override
    public void delete(String id) {

    }


    public void deleteSchedule(String type, Integer token, String billNo, String schedule) {
        String hql="delete from BillSchedule  where type=? and token=? and billNo=? and schedule=?";
        this.factoryBillScheduleDao.batchExecute(hql,new Object[]{type,token,billNo,schedule});
    }
}
