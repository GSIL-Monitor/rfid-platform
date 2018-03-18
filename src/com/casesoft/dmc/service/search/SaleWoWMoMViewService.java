package com.casesoft.dmc.service.search;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.search.SaleWoWMoMViewDao;
import com.casesoft.dmc.model.search.SaleWoWMoMView;

@Service
@Transactional
public class SaleWoWMoMViewService  extends AbstractBaseService<SaleWoWMoMView, String>{

	@Autowired
	private SaleWoWMoMViewDao saleWoWMoMViewDao;
	@Override
	public Page<SaleWoWMoMView> findPage(Page<SaleWoWMoMView> page,
			List<PropertyFilter> filters) {
		// TODO Auto-generated method stub
		return saleWoWMoMViewDao.findPage(page, filters);
	}

	@Override
	public void save(SaleWoWMoMView entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SaleWoWMoMView load(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SaleWoWMoMView get(String propertyName, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SaleWoWMoMView> find(List<PropertyFilter> filters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SaleWoWMoMView> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <X> List<X> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(SaleWoWMoMView entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(SaleWoWMoMView entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		
	}

}
