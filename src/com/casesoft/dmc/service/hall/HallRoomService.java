package com.casesoft.dmc.service.hall;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.hall.HallRoomDao;
import com.casesoft.dmc.model.sys.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by session on 2017/3/10 0010.
 */

@Service
@Transactional
public class HallRoomService extends AbstractBaseService<Unit,String>{

	@Autowired
	private HallRoomDao hallRoomDao;

	@Override
	public Page<Unit> findPage(Page<Unit> page, List<PropertyFilter> filters) {
		return this.hallRoomDao.findPage(page,filters);
	}

	@Override
	public void save(Unit entity) {
		this.hallRoomDao.saveOrUpdate(entity);

	}

	public Unit findRoomByCode(String code){
		return (Unit)this.hallRoomDao.findUnique("from Unit h where h.code=? and h.type=6",new Object[]{code});
	}

	public String findMaxCode(){
		String codeFlag="HR";
		String hql="select max(CAST(SUBSTRING(hr.code,"+(codeFlag.length()+1)+"),integer)) from Unit hr where hr.type=6";
		Integer code = this.hallRoomDao.findUnique(hql,new Object[]{});
		return code==null?codeFlag+"001":codeFlag+ CommonUtil.convertIntToString(code+1,3);
	}

	@Override
	public Unit load(String id) {
		return this.hallRoomDao.load(id);
	}

	@Override
	public Unit get(String propertyName, Object value) {
		return null;
	}

	@Override
	public List<Unit> find(List<PropertyFilter> filters) {
		return this.hallRoomDao.find(filters);
	}

	@Override
	public List<Unit> getAll() {
		 List<Unit> hallRoomList =this.hallRoomDao.find("from Unit hallRoom where type=6",new Object[]{});
		 return hallRoomList.size()>0?hallRoomList:null;
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
	public void delete(String id) {

	}
}
