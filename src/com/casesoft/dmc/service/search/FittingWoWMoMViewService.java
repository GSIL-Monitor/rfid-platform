package com.casesoft.dmc.service.search;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.search.FittingWoWMoMViewDao;
import com.casesoft.dmc.model.search.FittingWoWMoMView;

@Service
@Transactional
public class FittingWoWMoMViewService  extends AbstractBaseService<FittingWoWMoMView, String>{

	@Autowired
	private FittingWoWMoMViewDao fittingWoWMoMViewDao;
	@Override
	public Page<FittingWoWMoMView> findPage(Page<FittingWoWMoMView> page,
			List<PropertyFilter> filters) {
		// TODO Auto-generated method stub
		return fittingWoWMoMViewDao.findPage(page, filters);
	}

	@Override
	public void save(FittingWoWMoMView entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public FittingWoWMoMView load(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FittingWoWMoMView get(String propertyName, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FittingWoWMoMView> find(List<PropertyFilter> filters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FittingWoWMoMView> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <X> List<X> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(FittingWoWMoMView entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(FittingWoWMoMView entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		
	}

}
