package com.casesoft.dmc.service.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.product.ColorDao;
import com.casesoft.dmc.model.product.Color;


@Service
@Transactional
public class ColorService implements IBaseService<Color, String>{

	@Autowired
	private ColorDao colorDao;

	@Override
	@Transactional(readOnly = true)
	public Page<Color> findPage(Page<Color> page, List<PropertyFilter> filters) {
		
		    return this.colorDao.findPage(page, filters);
		
	}

    @Transactional(readOnly = true)
    public Color findById(String id){
        String hql="from Color c where c.id=?";
        return this.colorDao.findUnique(hql,new Object[]{id});
    }

    @Transactional(readOnly = true)
	@Override
	public void save(Color color) {
		 this.colorDao.saveOrUpdate(color);
	}

	public String findMaxColorId(){
    	String hql="select count(*) from Color";
		Long noof = this.colorDao.findUnique(hql);
		Integer intNo = new Integer(noof.intValue());
		System.out.println("intNo="+intNo);
		String newId = intNo==null||intNo==0?"001":CommonUtil.convertIntToString(intNo+1,3);
		System.out.println("newId="+newId);
    	return newId;
	}

	@Override
	public Color load(String id) {
		return null;
	}

	@Override
	public Color get(String propertyName, Object value) {
		return null;
	}

	@Override
	public List<Color> find(List<PropertyFilter> filters) {
		return this.colorDao.find(filters);
	}

	@Override
	public List<Color> getAll() {

		return null;
	}

	@Override
	public <X> List<X> findAll() {

		return null;
	}

	@Override
	public void update(Color entity) {

		
	}

	@Override
	public void delete(Color entity) {

		
	}

	@Override
	public void delete(String id) {

		
	}
	
	

}
