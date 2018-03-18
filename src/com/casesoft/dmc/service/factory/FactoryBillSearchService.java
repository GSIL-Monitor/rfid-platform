package com.casesoft.dmc.service.factory;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.factory.FactoryBillSearchDao;
import com.casesoft.dmc.model.factory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FactoryBillSearchService extends AbstractBaseService<FactoryBillView, String> {

	@Autowired
	FactoryBillSearchDao factoryBillSearchDao;
	@Override
	public Page<FactoryBillView> findPage(Page<FactoryBillView> page,
			List<PropertyFilter> filters) {
		
		return this.factoryBillSearchDao.findPage(page, filters);
	}

	@Override
	public void save(FactoryBillView entity) {
		
		
	}

	@Override
	public FactoryBillView load(String id) {
		
		return this.factoryBillSearchDao.load(id);
	}

	@Override
	public FactoryBillView get(String propertyName, Object value) {
		return this.factoryBillSearchDao.findUniqueBy(propertyName, value);
	}

	@Override
	public List<FactoryBillView> find(List<PropertyFilter> filters) {
		
		return this.factoryBillSearchDao.find(filters);
	}
	
	public List<FactoryBillView> find(List<PropertyFilter> filters,
			String[] sort, String order) {
		
		return this.factoryBillSearchDao.find(filters,sort,order);
	}
	

	@Override
	public List<FactoryBillView> getAll() {
	
		return null;
	}

	@Override
	public <X> List<X> findAll() {
		
		return null;
	}

	@Override
	public void update(FactoryBillView entity) {

	}


	@Override
	public void delete(FactoryBillView entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		
	}

	public List<BillSchedule> findAllSchedule() {
		return this.factoryBillSearchDao.find("from BillSchedule");
	}

	//mysql initepc.progress null 不可以识别
	public List<FactoryRecord> findRecordByCodeLastTask(String inCode) {
		String hql = "select new FactoryRecord (initepc.code,(case when initepc.progress is null then ' ' else substr(initepc.progress,1,2) end),'-',(case when initepc.progress is null then ' ' else substr(initepc.progress,4) end),'N')"
				+ "from InitEpc initepc where " + inCode;
		List<FactoryRecord> list =  this.factoryBillSearchDao.find(hql);
		return list;
	}



    public List<String> findScanedRecordByCode(String inCode, Integer token,String type) {
        String hql = "select DISTINCT(factoryrecord.code)"
                + "from FactoryRecord factoryrecord where " + inCode+" and token=? and factoryrecord.type= ?";
        return this.factoryBillSearchDao.find(hql,new Object[]{token,type});
    }


    public List<FactoryBriefBill> findBillInfoByCodes(String inCode) {
        String hql ="select new com.casesoft.dmc.model.factory.FactoryBriefBill(b.billNo,initepc.code) from FactoryBill b ,InitEpc initepc where b.billNo = initepc.billNo and "+inCode;
        return this.factoryBillSearchDao.find(hql);
    }

    public List<FactoryRecord> findRecordListByCode(String code) {
        return this.factoryBillSearchDao.find("select new FactoryRecord(r.token,r.scanTime,r.type,t.operator,'',r.totalTime,r.isOutSource)from FactoryRecord r,FactoryTask t where r.code=? and r.taskId = t.taskId order by r.scanTime desc" ,new Object[]{code});
    }

    public FactoryBill findBillByBillNo(String billNo) {
        return this.factoryBillSearchDao.findUnique("from FactoryBill where billNo=?",new Object[]{billNo});
    }

    public List<FactoryBriefBill> findRecodListByBillNo(String billNo) {
        String hql ="select new com.casesoft.dmc.model.factory.FactoryBriefBill(initepc.code) from InitEpc initepc where initepc.billNo=?";
        return this.factoryBillSearchDao.find(hql,new Object[]{billNo});
    }
}
