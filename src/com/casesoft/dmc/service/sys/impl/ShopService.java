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
import com.casesoft.dmc.dao.sys.ShopDao;
import com.casesoft.dmc.model.sys.Unit;

@Service
@Transactional
public class ShopService implements IBaseService<Unit, String>{
	
	@Autowired
	private ShopDao shopDao;

	@Override
	@Transactional(readOnly = true)
	public Page<Unit> findPage(Page<Unit> page, List<PropertyFilter> filters) {
		 if (CommonUtil.isBlank(filters)) {
		      return this.shopDao.findPage(page, "from Unit u");
		    }
		    return this.shopDao.findPage(page, filters);
	}

	public Unit findUnitByCode(String Code){
		return (Unit)this.shopDao.findUnique("from Unit u where u.code=?",new Object[]{Code});
	}
	
	@Transactional(readOnly = true)
    public String findMaxCode(int type) {
        String unitFlag = Constant.UnitCodePrefix.Shop;//"S";

        String hql = "select max(CAST(SUBSTRING(unit.code,"+(unitFlag.length()+1)+"),integer))"
                + " from Unit as unit where unit.type=? and unit.src='01' ";
        Integer code = (Integer) this.shopDao.findUnique(hql, new Object[] { type });
        return code == null ? (unitFlag + "001") : unitFlag
                + CommonUtil.convertIntToString(code + 1, 3);
    }
	
	
	
	
	
	@Override
	public void save(Unit entity) {
		this.shopDao.saveOrUpdate(entity);
		
	}

	@Override
	public Unit load(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Unit get(String propertyName, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Unit> find(List<PropertyFilter> filters) {

		return this.shopDao.find(filters);
	}

	@Override
	public List<Unit> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <X> List<X> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Unit entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Unit entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		
	} 
	

}
