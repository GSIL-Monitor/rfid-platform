package com.casesoft.dmc.service.sys.impl;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.sys.CompanyDao;
import com.casesoft.dmc.model.sys.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;


@Service
@Transactional
public class CompanyService implements IBaseService<Unit, Serializable>{

	  @Autowired
	private CompanyDao companyDao;
	  @Override
	  @Transactional(readOnly = true)
	public Page<Unit> findPage(Page<Unit> page,List<PropertyFilter> filters) {
		  if (CommonUtil.isBlank(filters)) {
		      return this.companyDao.findPage(page, "from Unit u");
		    }
		    return this.companyDao.findPage(page, filters);
	}

	@Override
	public void save(Unit entity) {

		
	}
	@Override
	public Unit load(Serializable id) {

		return null;
	}
	@Override
	public Unit get(String propertyName, Object value) {

		return null;
	}
	@Override
	public List<Unit> find(List<PropertyFilter> filters) {

		return this.companyDao.find(filters);
	}
	@Override
	public List<Unit> getAll() {

		return null;
	}
	@Override
	public <X> List<X> findAll() {

		return null;
	}
	@Override
	public void update(Unit entity) {

		
	}
	@Override
	public void delete(Unit entity) {

	}
	@Override
	public void delete(Serializable id) {

	}



	

}
