package com.casesoft.dmc.service.hall;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.hall.HallInventoryDao;
import com.casesoft.dmc.extend.third.descriptor.DataResult;
import com.casesoft.dmc.extend.third.request.BaseService;
import com.casesoft.dmc.extend.third.request.RequestPageData;
import com.casesoft.dmc.model.hall.HallInventory;
import com.casesoft.dmc.model.hall.HallInventoryDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by session on 2017/3/10 0010.
 */
@Service
@Transactional
public class HallInventoryService extends BaseService<HallInventory,String>{

	@Autowired
	private HallInventoryDao hallInventoryDao;

	@Override
	public Page<HallInventory> findPage(Page<HallInventory> page, List<PropertyFilter> filters) {
		return null;
	}

	@Override
	public void save(HallInventory entity) {
		this.hallInventoryDao.saveOrUpdate(entity);
	}

	public void save(HallInventory inv, List<HallInventoryDetail> detailList){
		this.hallInventoryDao.saveOrUpdate(inv);
		this.hallInventoryDao.doBatchInsert(detailList);
	}

	@Override
	public HallInventory load(String id) {
		return this.hallInventoryDao.load(id);
	}


	@Override
	public HallInventory get(String propertyName, Object value) {
		return null;
	}

	@Override
	public List<HallInventory> find(List<PropertyFilter> filters) {
		return this.hallInventoryDao.find(filters);
	}

	@Override
	public DataResult find(RequestPageData<?> request){
		return this.hallInventoryDao.find(request);
	}
	@Override
	public List<HallInventory> getAll() {
		return this.hallInventoryDao.getAll();
	}

	public HallInventory findByTaskId(String taskId){
		return this.hallInventoryDao.findUnique("from HallInventory where taskId=?",new Object[]{taskId});
	}

	public String findBillMaxCode(String prefix) {
		String hql = "select max(CAST(SUBSTRING(hb.taskId,"
				+ (prefix.length() + 1) + "),integer))"
				+ " from HallInventory as hb where hb.taskId like ?";
		Integer code = this.hallInventoryDao.findUnique(hql, prefix + '%');
		return code == null ? (prefix + "001") : prefix
				+ CommonUtil.convertIntToString(code + 1, 3);
	}
	@Override
	public <X> List<X> findAll() {
		return null;
	}

	@Override
	public void update(HallInventory entity) {

	}

	@Override
	public void delete(HallInventory entity) {

	}

	@Override
	public void delete(String id) {

	}
}
