package com.casesoft.dmc.service.task;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.task.OutboundTaskDao;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.BusinessDtl;
import com.casesoft.dmc.model.task.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OutboundTaskService implements IBaseService<Business,String>{

	@Autowired
	private OutboundTaskDao outboundTaskDao;
	
	@Transactional(readOnly = true)
	@Override
	public Page<Business> findPage(Page<Business> page,
			List<PropertyFilter> filters) {
		return this.outboundTaskDao.findPage(page, filters);
	}
	@Transactional(readOnly = true)
	public Business findById(String id){
		return this.outboundTaskDao.findUniqueBy("id", id);//.find(hql,new Object[]{id});
	}

    @Transactional(readOnly = true)
    public List<BusinessDtl> findBusinessDtl(String taskId,String filter_LIKES_styleId,String filter_LIKES_sku){
        String hql="from BusinessDtl busdtl where busdtl.taskId=? and busdtl.styleId like ? and busdtl.sku like ?";
        return this.outboundTaskDao.find(hql,new Object[]{taskId,"%"+filter_LIKES_styleId+"%","%"+filter_LIKES_sku+"%"});
    }

    @Transactional(readOnly = true)
    public List<Record> findRecord(String taskId){
        String hql="from Record r where r.taskId=?";
        return this.outboundTaskDao.find(hql,new Object[]{taskId});
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Business entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		
	}

}
