package com.casesoft.dmc.service.trace;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.trace.BoxDao;
import com.casesoft.dmc.model.task.Box;
import com.casesoft.dmc.model.task.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoxService extends AbstractBaseService<Box, String> {

	@Autowired
	private BoxDao boxDao;
	
	@Transactional(readOnly = true)
	@Override
	public Page<Box> findPage(Page<Box> page, List<PropertyFilter> filters) {
		return boxDao.findPage(page, filters);
	}

	@Override
	public void save(Box entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Box load(String id) {
		// TODO Auto-generated method stub
		return null;
	}

        @Transactional(readOnly = true)
	@Override
	public Box get(String propertyName, Object value) {
		return this.boxDao.findUniqueBy(propertyName, value);
	}

        @Transactional(readOnly = true)
	@Override
	public List<Box> find(List<PropertyFilter> filters) {
		return this.boxDao.find(filters);
	}        

	@Override
	public List<Box> getAll() {
		// TODO Auto-generated method stub
		return null;
	}
        
        @Transactional(readOnly = true)       
        public List<Record> findByBoxAndToken(String boxNo, int inToken) {            
            String hql = "from Record as rIn where rIn.cartonId=? and rIn.token=?";
            switch(inToken) {
                case 8: hql += " and rIn.code not in (select rOut.code from Record as rOut where rOut.token=10)";break;
                case 11: hql += " and rIn.code not in (select rOut.code from Record as rOut where rOut.token=13)";break;
                case 14 : hql += " and rIn.code not in (select rOut.code from Record as rOut where rOut.token=15 or rOut.token=18)";break;
                case 17 : hql += " and rIn.code not in (select rOut.code from Record as rOut where rOut.token=15 or rOut.token=18)";break;  
                default: break;
            }
            
            return this.boxDao.find(hql, new Object[]{boxNo,inToken});
        }

     public Box findBoxbycartonId(String cartonId){
    	return this.boxDao.findUnique("from Box b where b.cartonId=?",new Object[]{cartonId});
     }
     public List<Box> findBoxbycartonIds(String cartonIds){
     	return this.boxDao.find("from Box box where "+cartonIds,new Object[]{});
      }
     public List<String> findBoxsId(int token,String uniqueCode,String sku,String styleId,String colorId,String sizeId){
    	 String hql="select distinct cartonId from Record r where r.token =? and r.code like ? and r.sku like ?"
    	 		+ " and r.styleId like ? and r.colorId like ? and r.sizeId like ?";
    	 switch(token) {
         case 8: hql += " and r.code not in (select rOut.code from Record as rOut where rOut.token=10)";break;
         case 11: hql += " and r.code not in (select rOut.code from Record as rOut where rOut.token=13)";break;
         case 14 : hql += " and r.code not in (select rOut.code from Record as rOut where rOut.token=15 or rOut.token=18)";break;
         case 17 : hql += " and r.code not in (select rOut.code from Record as rOut where rOut.token=15 or rOut.token=18)";break;  
         default: break;
     }
    	return this.boxDao.find(hql, 
    	 		new Object[]{token,uniqueCode+"%", sku+"%", styleId+"%", colorId+"%", sizeId+"%"});
     }
     public void boxBindFloorId(String cartonId,String floorId){
    	 String hql="update Box b set b.floorId='"+floorId+"' where b.cartonId in(" +
    	 		cartonId+")";
    	 this.boxDao.batchExecute(hql, new Object[]{});
     }
	@Override
	public <X> List<X> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Box entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Box entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		
	}

	public BoxDao getBoxDao() {
		return boxDao;
	}

	public void setBoxDao(BoxDao boxDao) {
		this.boxDao = boxDao;
	}
	
	

}
