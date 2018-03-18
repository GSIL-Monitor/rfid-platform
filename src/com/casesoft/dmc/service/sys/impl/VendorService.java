package com.casesoft.dmc.service.sys.impl;



import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.sys.VendorDao;
import com.casesoft.dmc.model.sys.Unit;


@Service
@Transactional
public class VendorService implements IBaseService<Unit, Serializable>{

	 @Autowired
	  private VendorDao vendorDao;

    @Transactional(readOnly = true)
    public Unit findById(String id){
        String hql="from Unit u where u.id=?";
        return this.vendorDao.findUnique(hql,new Object[]{id});
    }

	  @Override
	  @Transactional(readOnly = true)
	  public Page<Unit> findPage(Page<Unit> page, List<PropertyFilter> filters) {
	    if (CommonUtil.isBlank(filters)) {
	      return this.vendorDao.findPage(page, "from Unit u");
	    }
	    return this.vendorDao.findPage(page, filters);
	  }

	@Override
	public void save(Unit entity) {
		
		vendorDao.saveOrUpdate(entity);
		
	}

	@Override
	public Unit load(Serializable id) {

		return null;
	}

	public Unit findUniqueByCode(String code){
    	String hql="from Unit u where u.code=?";
    	return this.vendorDao.findUnique(hql,new Object[]{code});
	}

	@Override
	public Unit get(String propertyName, Object value) {

		return null;
	}

	@Override
	public List<Unit> find(List<PropertyFilter> filters) {
		
		return this.vendorDao.find(filters);
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
	@Transactional(readOnly = true)
	public String findMaxCode(int type) {

		String unitFlag = Constant.UnitCodePrefix.Vender;//"V";
		String hql = "select max(CAST(SUBSTRING(unit.code,"+(unitFlag.length()+1)+"),integer))"
                + " from Unit as unit where unit.type=? and unit.src='01' and unit.code like '"+unitFlag+"%'";
        Integer code = this.vendorDao.findUnique(hql, new Object[] { type });
        return code == null ? (unitFlag + "001") : unitFlag
                + CommonUtil.convertIntToString(code + 1, 3);
		
	}
	
     
}
