package com.casesoft.dmc.service.product;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.product.ColorDao;
import com.casesoft.dmc.model.product.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


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

	@Override
	public void save(Color color) {
		 this.colorDao.saveOrUpdate(color);
	}

	public void saveAndDelete(Color color , String oldId){
    	if (oldId!=null&&!oldId.equals("")){
			this.colorDao.delete(oldId);
		}
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

		return this.colorDao.findUniqueBy(propertyName,value);
	}

	@Override
	public List<Color> find(List<PropertyFilter> filters) {
		return this.colorDao.find(filters);
	}

	public List<Color> find(List<PropertyFilter> filters, Map<String, String> sortMap){
		return this.colorDao.find(filters, sortMap);
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
