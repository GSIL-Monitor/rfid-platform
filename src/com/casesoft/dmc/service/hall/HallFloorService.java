package com.casesoft.dmc.service.hall;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.hall.HallFloorDao;
import com.casesoft.dmc.model.hall.HallFloor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by session on 2017/3/10 0010.
 */

@Service
@Transactional
public class HallFloorService extends AbstractBaseService<HallFloor,String>{

	@Autowired
	private HallFloorDao hallFloorDao;

	@Override
	public Page<HallFloor> findPage(Page<HallFloor> page, List<PropertyFilter> filters) {
		return  this.hallFloorDao.findPage(page,filters);
	}

	@Override
	public void save(HallFloor entity) {
		this.hallFloorDao.saveOrUpdate(entity);
	}

	@Override
	public HallFloor load(String id) {

		return this.hallFloorDao.load(id);
	}

	public HallFloor findHallFloorByCode(String code){
		return this.hallFloorDao.findUnique("from HallFloor hf where hf.code=?",new Object[]{code});
	}

	public HallFloor findDefaultFloor(String ownerId){
		List<HallFloor> hallFloorList= this.hallFloorDao.find("from HallFloor hf where hf.asDefault =1 and hf.areaId='E' and ownerId=?",new Object[]{ownerId});
		return hallFloorList.size()==0?null:hallFloorList.get(0);
	}

	public HallFloor findDefaultArea(String ownerId){
		List<HallFloor> hallFloorList= this.hallFloorDao.find("from HallFloor hf where hf.asDefault =1 and hf.areaId='A' and ownerId=?",new Object[]{ownerId});
		return hallFloorList.size()==0?null:hallFloorList.get(0);
	}
	public String findMaxCode(String areaId,String ownerId){
		String codeFlag="SE";
		String hql="";
		if("A".equals(areaId)){
			hql="select max(CAST(SUBSTRING(hf.code,"+(codeFlag.length()+1)+","+(codeFlag.length()+3)+"),integer)) from HallFloor hf where hf.areaId=? and ownerId=?";
			Integer code=this.hallFloorDao.findUnique(hql,new Object[]{areaId,ownerId});
			return code==null?codeFlag+"001":codeFlag+ CommonUtil.convertIntToString(code+1,3);
		} else if("E".equals(areaId)){
			hql="select max(CAST(SUBSTRING(hf.code,"+(ownerId.length()+1)+"),integer)) from HallFloor hf where hf.areaId=?and ownerId=?";
			Integer code=this.hallFloorDao.findUnique(hql,new Object[]{areaId,ownerId});
			return code==null?ownerId+"001":ownerId+ CommonUtil.convertIntToString(code+1,3);
		}
			return null;
	}

	@Override
	public HallFloor get(String propertyName, Object value) {
		return null;
	}

	@Override
	public List<HallFloor> find(List<PropertyFilter> filters) {
		return this.hallFloorDao.find(filters);
	}

	@Override
	public List<HallFloor> getAll() {
		return this.hallFloorDao.getAll();
	}

	@Override
	public <X> List<X> findAll() {
		return null;
	}

	@Override
	public void update(HallFloor entity) {
		this.hallFloorDao.saveOrUpdate(entity);
	}

	@Override
	public void delete(HallFloor entity) {

	}

	@Override
	public void delete(String id) {

	}
}
