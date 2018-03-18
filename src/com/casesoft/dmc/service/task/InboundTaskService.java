package com.casesoft.dmc.service.task;

import java.util.List;

import com.casesoft.dmc.model.task.BusinessDtl;
import com.casesoft.dmc.model.task.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.task.InboundTaskDao;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.task.Business;


@Service
@Transactional
public class InboundTaskService implements IBaseService<Business, String>{
	
	@Autowired
	private InboundTaskDao inboundTaskDao; 
	
	
	@Override
	@Transactional(readOnly = true)
	public Page<Business> findPage(Page<Business> page,
			List<PropertyFilter> filters) {

		 return this.inboundTaskDao.findPage(page, filters);
	}

    @Transactional(readOnly = true)
    public Business findById(String id){
        return this.inboundTaskDao.findUniqueBy("id", id);//.find(hql,new Object[]{id});
    }

	@Transactional(readOnly = true)
	public Business findBySearchId(String id){
		return this.inboundTaskDao.findUniqueBy("id", id);
		
	}

    @Transactional(readOnly = true)
    public List<BusinessDtl> findBusinessDtl(String taskId,String styleId,String sku){
        String hql="from BusinessDtl busdtl where busdtl.taskId=? and styleId like? and sku like?";
        return this.inboundTaskDao.find(hql,new Object[]{taskId,"%"+styleId+"%","%"+sku+"%"});
    }

    @Transactional(readOnly = true)
    public List<Record> findRecord(String taskId){
        String hql="from Record r where r.taskId=?";
        return this.inboundTaskDao.find(hql,new Object[]{taskId});
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
