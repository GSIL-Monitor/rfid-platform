package com.casesoft.dmc.service.search;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.search.FittingConvertSaleViewDao;
import com.casesoft.dmc.model.search.FittingConvertSaleView;
import com.casesoft.dmc.model.search.SaleWoWMoMView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FittingConvertSaleViewService extends AbstractBaseService<FittingConvertSaleView, String>{

	@Autowired
	private FittingConvertSaleViewDao fittingConvertSaleViewDao;
	@Override
	public Page<FittingConvertSaleView> findPage(
			Page<FittingConvertSaleView> page, List<PropertyFilter> filters) {
        return this.fittingConvertSaleViewDao.findPage(page, filters);

	}
	 
	public List<SaleWoWMoMView> findSaleCountView() {
        return this.fittingConvertSaleViewDao.find("from SaleWoWMoMView s",new Object[]{});

	}
	@Override
	public void save(FittingConvertSaleView entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public FittingConvertSaleView load(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FittingConvertSaleView get(String propertyName, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FittingConvertSaleView> find(List<PropertyFilter> filters) {
		// TODO Auto-generated method stub
		return this.fittingConvertSaleViewDao.find(filters);
	}

	@Override
	public List<FittingConvertSaleView> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <X> List<X> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public <X> List<X> findAll(PropertyFilter  filter) {
		String hql = filter.getHql();
		return this.fittingConvertSaleViewDao.find(hql, filter.getValues());
	}
	@Override
	public void update(FittingConvertSaleView entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(FittingConvertSaleView entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		
	}

}
