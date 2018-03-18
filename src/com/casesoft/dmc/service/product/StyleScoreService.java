package com.casesoft.dmc.service.product;

import java.util.List;

import com.casesoft.dmc.model.product.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.product.StyleScoreDao;


@Service
@Transactional
public class StyleScoreService extends AbstractBaseService<StyleScore, String> {
	@Autowired
	private StyleScoreDao styleScoreDao;
	@Override
	public Page<StyleScore> findPage(Page<StyleScore> page,
			List<PropertyFilter> filters) {
		return this.styleScoreDao.findPage(page, filters);
	}

	@Override
	public void save(StyleScore entity) {
		this.styleScoreDao.save(entity);
	}

	@Override
	public StyleScore load(String id) {
		// TODO Auto-generated method stub
		return this.styleScoreDao.load(id);
	}

	@Override
	public StyleScore get(String propertyName, Object value) {
		// TODO Auto-generated method stub
		return this.styleScoreDao.findUniqueBy(propertyName, value);
	}

	@Override
	public List<StyleScore> find(List<PropertyFilter> filters) {
		
		return this.styleScoreDao.find(filters);
	}

	@Override
	public List<StyleScore> getAll() {
		// TODO Auto-generated method stub
		return this.styleScoreDao.getAll();
	}

	@Override
	public <X> List<X> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(StyleScore entity) {
		this.styleScoreDao.saveOrUpdate(entity);
	}

	@Override
	public void delete(StyleScore entity) {
		this.styleScoreDao.delete(entity);
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		this.styleScoreDao.delete(id);
	}

	public void save(List<StyleScore> list) {
		this.styleScoreDao.doBatchInsert(list);
	}

	
	public List<CountResult> findScoreCount(String styleId) {
		String hql ="select new com.casesoft.dmc.model.product.CountResult(sum(s.score),avg(s.score)) from StyleScore s where s.styleId=? ";
		return this.styleScoreDao.find(hql, new Object[]{styleId});
		
	}
	

	
}
