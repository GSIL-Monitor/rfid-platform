package com.casesoft.dmc.service.hall;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.hall.HallInventoryDao;
import com.casesoft.dmc.dao.hall.HallInventoryDetailDao;
import com.casesoft.dmc.model.hall.HallInventory;
import com.casesoft.dmc.model.hall.HallInventoryDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2017/3/10 0010.
 */

@Service
@Transactional
public class HallInventoryDetailService extends AbstractBaseService<HallInventoryDetail,String>{

	@Autowired
	private HallInventoryDetailDao hallInventoryDetailDao;

	@Override
	public Page<HallInventoryDetail> findPage(Page<HallInventoryDetail> page, List<PropertyFilter> filters) {
		return this.hallInventoryDetailDao.findPage(page,filters);
	}

	@Override
	public void save(HallInventoryDetail entity) {
		this.hallInventoryDetailDao.save(entity);
	}
	
	@Override
	public HallInventoryDetail load(String id) {
		return this.hallInventoryDetailDao.load(id);
	}

	@Override
	public HallInventoryDetail get(String propertyName, Object value) {
		return null;
	}

	@Override
	public List<HallInventoryDetail> find(List<PropertyFilter> filters) {
		return null;
	}

	@Override
	public List<HallInventoryDetail> getAll() {
		return null;
	}

	@Override
	public <X> List<X> findAll() {
		return null;
	}

	@Override
	public void update(HallInventoryDetail entity) {

	}

	@Override
	public void delete(HallInventoryDetail entity) {

	}

	@Override
	public void delete(String id) {

	}
}
