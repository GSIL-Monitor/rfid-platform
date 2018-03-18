package com.casesoft.dmc.service.factory;

import com.casesoft.dmc.controller.factory.FactoryConstant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.factory.FactoryTaskDao;
import com.casesoft.dmc.model.factory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class FactoryTaskService extends AbstractBaseService<FactoryTask, String> {
	
	
	@Autowired
	private FactoryTaskDao factoryTaskDao;
	@Override
	public Page<FactoryTask> findPage(Page<FactoryTask> page,
			List<PropertyFilter> filters) {
		
		return this.factoryTaskDao.findPage(page, filters);
	}

	@Override
	public void save(FactoryTask task){
		
		this.factoryTaskDao.doBatchInsert(task.getRecordList());
		if(task.getIsOutSource().equals("Y")){
			if(task.getType().equals(FactoryConstant.TaskType.Inbound)){
				FactoryOutSorceBill bill = new FactoryOutSorceBill(task);
				bill.setIsIn("N");
				this.factoryTaskDao.saveOrUpdateX(bill);
			}else{
				FactoryOutSorceBill bill = findFactoryOutSorceBill(task.getOutTaskId());
				bill.setIsIn("Y");
				bill.setInTaskId(task.getTaskId());
				this.factoryTaskDao.saveOrUpdateX(bill);
				this.factoryTaskDao.save(task);
			}
			
		}else{
			this.factoryTaskDao.save(task);
		}		
		this.factoryTaskDao.doBatchInsert(task.getEpcList());
	}

	private FactoryOutSorceBill findFactoryOutSorceBill(String outTaskId) {
		return this.factoryTaskDao.findUnique("from FactoryOutSorceBill where taskId=?", new Object[]{outTaskId});
	}

	@Override
	public FactoryTask load(String id) {
		
		return this.factoryTaskDao.load(id);
	}

	@Override
	public FactoryTask get(String propertyName, Object value) {
		
		return this.factoryTaskDao.findUniqueBy(propertyName, value);
	}

	@Override
	public List<FactoryTask> find(List<PropertyFilter> filters) {
		
		return this.factoryTaskDao.find(filters);
	}

	@Override
	public List<FactoryTask> getAll() {
		
		return this.factoryTaskDao.getAll();
	}

	@Override
	public <X> List<X> findAll() {
		
		return null;
	}

	@Override
	public void update(FactoryTask entity) {
		
		
	}

	@Override
	public void delete(FactoryTask entity) {
		
		
	}

	@Override
	public void delete(String id) {
		
		
	}
	
	public List<InitEpc> findEpcList(String billNo) {
		String hql ="from InitEpc initepc where initepc.billNo=?";	
		return this.factoryTaskDao.find(hql, new Object[]{billNo});
	}

	public List<InitEpc> findEpcListBycodes(String inCode) {
		String hql ="from InitEpc initepc where "+inCode;		
		return this.factoryTaskDao.find(hql);
	}

	public List<FactoryOutSorceBill> findOutSourceBill() {
		return this.factoryTaskDao.find("from FactoryOutSorceBill where isIn='N'");
	}

	public List<FactoryRecord> getOuSourceRecord(String taskId) {
		String hql ="select new FactoryRecord(r.code) from FactoryRecord r where r.taskId=?";
		return this.factoryTaskDao.find(hql,new Object[]{taskId});
	}

    public List<FactoryRecord> getOuSourceRecordAndBill(String taskId) {
        String hql ="select new FactoryRecord(r.code,epc.billNo) from FactoryRecord r,InitEpc epc where r.taskId=? and r.code=epc.code";
        return this.factoryTaskDao.find(hql,new Object[]{taskId});
    }

	public void updateBillSchedule(String inBillNo, String inBillDate,
			String inEndDate, String inUploadNo, Integer token, String type, String taskTime) {
		String hql="update BillSchedule billschedule set billschedule.taskTime=? where "+inBillNo+" and "+inBillDate
				+" and "+inEndDate+" and "+inUploadNo+" and billschedule.token=? and billschedule.type=?";
		this.factoryTaskDao.batchExecute(hql, taskTime,token,type);
	}

	public List<FactoryBill> findFactoryBillList(String inBillNo,
			String inBillDate, String inEndDate, String inUploadNo) {
		String hql="from  FactoryBill factorybill  where "+inBillNo+" and "+inBillDate
				+" and "+inEndDate+" and "+inUploadNo;
		return this.factoryTaskDao.find(hql);
	}

	public void updateBill(String inBillNo, String inBillDate,
			String inEndDate, String inUploadNo,String taskTime) {
		String hql="update FactoryBill factorybill set factorybill.outDate=? where "+inBillNo+" and "+inBillDate
				+" and "+inEndDate+" and "+inUploadNo;
		this.factoryTaskDao.batchExecute(hql, taskTime);
		
	}


    public void updateBillProgress(String inBillNo, String inBillDate, String inEndDate, String inUploadNo, String progress) {
        String hql="update FactoryBill factorybill set factorybill.progress=? where "+inBillNo+" and "+inBillDate
                +" and "+inEndDate+" and "+inUploadNo;
        this.factoryTaskDao.batchExecute(hql, progress);
    }


    public List<WorkCalendar> findWorkCalendarList(String startDay, String endDay) {
        return this.factoryTaskDao.find("from WorkCalendar where day between ? and ? order by day asc",new Object[]{startDay,endDay});
    }

    public FactoryRecord findUniqueRecord(String code, String taskId) {
        return this.factoryTaskDao.findUnique("from FactoryRecord where code=? and taskId=?",new Object[]{code,taskId});
    }


    public FactoryBill findUniqueFactoryBill(String billNo, String billDate, String endDate, String uploadNo) {
        return this.factoryTaskDao.findUnique("from FactoryBill where billNo=? and billDate=? and endDate=? and uploadNo=?"
                ,new Object[]{billNo,billDate,endDate,uploadNo});
    }
}
