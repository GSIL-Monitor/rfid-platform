package com.casesoft.dmc.service.hall;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.hall.HallTaskDetailDao;
import com.casesoft.dmc.extend.third.descriptor.DataResult;
import com.casesoft.dmc.extend.third.request.RequestPageData;
import com.casesoft.dmc.model.hall.HallTaskDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by session on 2017/3/10 0010.
 */
@Service
@Transactional
public class HallTaskDetailService extends AbstractBaseService<HallTaskDetail,String>{

	@Autowired
	private HallTaskDetailDao hallTaskDetailDao;

	@Override
	public Page<HallTaskDetail> findPage(Page<HallTaskDetail> page, List<PropertyFilter> filters) {
		return this.hallTaskDetailDao.findPage(page,filters);
	}

	public DataResult find(RequestPageData<?> request){
		return this.hallTaskDetailDao.find(request);
	}

	@Override
	public void save(HallTaskDetail entity) {
		this.hallTaskDetailDao.save(entity);
	}

	@Override
	public HallTaskDetail load(String id) {
		return this.hallTaskDetailDao.load(id);
	}

	@Override
	public HallTaskDetail get(String propertyName, Object value) {
		return null;
	}

	@Override
	public List<HallTaskDetail> find(List<PropertyFilter> filters) {
		return null;
	}

	@Override
	public List<HallTaskDetail> getAll() {
		return null;
	}

	@Override
	public <X> List<X> findAll() {
		return null;
	}

	@Override
	public void update(HallTaskDetail entity) {

	}

	@Override
	public void delete(HallTaskDetail entity) {

	}

	@Override
	public void delete(String id) {

	}
}
