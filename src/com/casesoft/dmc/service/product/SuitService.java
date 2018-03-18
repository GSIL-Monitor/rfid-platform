package com.casesoft.dmc.service.product;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.product.SuitDao;
import com.casesoft.dmc.model.product.StyleCombine;
import com.casesoft.dmc.model.product.StyleDescription;
import com.casesoft.dmc.model.product.Suit;
import com.casesoft.dmc.model.product.SuitDtl;

@Service
@Transactional
public class SuitService extends AbstractBaseService<Suit, String> {

	@Autowired
	private SuitDao suitDao;
	@Override
	public Page<Suit> findPage(Page<Suit> page, List<PropertyFilter> filters) {
		// TODO Auto-generated method stub
		return this.suitDao.findPage(page, filters);
	}

	@Override
	public void save(Suit entity) {
		this.suitDao.saveOrUpdate(entity);
		if(CommonUtil.isNotBlank(entity.getList())){
			this.suitDao.doBatchInsert(entity.getList());
		}
	}
	
	public void saveStyleDescriptions(List<StyleDescription> lists) {
 		if(CommonUtil.isNotBlank(lists)){
			this.suitDao.doBatchInsert(lists);
		}
	}
 	public void save(List<Suit> entitts) {
 		if(CommonUtil.isBlank(entitts)){
 			return;
 		}
 		List<SuitDtl> dtls=new ArrayList<>();
 		for(Suit s:entitts){
 			if(CommonUtil.isNotBlank(s.getList())){
 				dtls.addAll(s.getList());
 			}
 			this.suitDao.createQuery("delete SuitDtl s where s.suitCode=?", new Object[]{s.getCode()}).executeUpdate();
 		}
		this.suitDao.doBatchInsert(entitts);
		this.suitDao.doBatchInsert(dtls);
	}
 	public void saveStyleCollocationList(List<StyleCombine> combines) {
 		if(CommonUtil.isNotBlank(combines)){
 			this.suitDao.doBatchInsert(combines);
 		}
	}
	public List<StyleCombine> getAllStyleCombines() {
 		String hql="select new com.casesoft.dmc.model.product.StyleCombine(s.styleId,s.colorId)  "
 				+ "from  StyleCombine s group by s.styleId,s.colorId";
 		return this.suitDao.find(hql, new Object[]{});
	}
	public List<StyleCombine> getStyleCombines(String styleId,String colorId) {
		String hql="from StyleCombine s where s.styleId=? and s.colorId=?";
 		return this.suitDao.find(hql, new Object[]{styleId,colorId});
	}
	public List<Suit> getSuits(String styleId,String colorId) {
		String hql="select new com.casesoft.dmc.model.product.Suit(s.code, s.name, s.remark, s.imageUrl,"+
				"s.price, s.date) from Suit s,SuitDtl d where d.suitCode=s.code and d.styleId=? and d.colorId=?";
 		return this.suitDao.find(hql, new Object[]{styleId,colorId});
	}
	public List<StyleDescription> getStyleDescriptions(String styleIds) {
		String hql="  from StyleDescription styledescription where "+styleIds;
 		return this.suitDao.find(hql, new Object[]{});
	}
	@Override
	public Suit load(String id) {
		// TODO Auto-generated method stub
		return this.suitDao.load(id);
	}

	@Override
	public Suit get(String propertyName, Object value) {
		// TODO Auto-generated method stub
		return this.suitDao.findUniqueBy(propertyName, value);
	}

	@Override
	public List<Suit> find(List<PropertyFilter> filters) {
		// TODO Auto-generated method stub
		return this.suitDao.find(filters);
	}

	@Override
	public List<Suit> getAll() {
		// TODO Auto-generated method stub
		return this.suitDao.getAll();
	}
    public List<SuitDtl> findDtlByCode(String code){
    	String hql = "from SuitDtl dtl where dtl.suitCode = ?";
    	return this.suitDao.find(hql, new Object[]{code});
    }
	@Override
	public <X> List<X> findAll() {
		// TODO Auto-generated method stub
		return this.suitDao.find("from SuitDtl");
	}

	@Override
	public void update(Suit entity) {
		// TODO Auto-generated method stub
		
	}
	public void deleteDtlByCode(String code){
		this.suitDao.batchExecute("delete SuitDtl dtl where dtl.suitCode =?", new Object[]{code});
	}

	@Override
	public void delete(Suit entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		
	}

	
}
