package com.casesoft.dmc.service.hall;

import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.hall.HallTaskDetailViewDao;
import com.casesoft.dmc.extend.third.descriptor.DataResult;
import com.casesoft.dmc.extend.third.request.RequestPageData;
import com.casesoft.dmc.model.hall.HallTaskDetailView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by session on 2017/3/10 0010.
 */

@Service
@Transactional
public class HallTaskDetailViewService extends AbstractBaseService<HallTaskDetailView,String>{

	@Autowired
	private HallTaskDetailViewDao hallTaskDetailViewDao;

	@Override
	public Page<HallTaskDetailView> findPage(Page<HallTaskDetailView> page, List<PropertyFilter> filters) {
		return this.hallTaskDetailViewDao.findPage(page,filters);
	}

	public DataResult find(RequestPageData<?> request){ return this.hallTaskDetailViewDao.find(request);}

	@Override
	public void save(HallTaskDetailView entity) {

	}

	@Override
	public HallTaskDetailView load(String id) {
		return null;
	}

	@Override
	public HallTaskDetailView get(String propertyName, Object value) {
		return null;
	}

	@Override
	public List<HallTaskDetailView> find(List<PropertyFilter> filters) {
		return this.hallTaskDetailViewDao.find(filters);
	}

	@Override
	public List<HallTaskDetailView> getAll() {
		return this.hallTaskDetailViewDao.getAll();
	}

	@Override
	public <X> List<X> findAll() {
		return null;
	}

	@Override
	public void update(HallTaskDetailView entity) {

	}

	@Override
	public void delete(HallTaskDetailView entity) {

	}

	@Override
	public void delete(String id) {

	}
}
