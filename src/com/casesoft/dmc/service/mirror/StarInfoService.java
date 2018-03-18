package com.casesoft.dmc.service.mirror;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.mirror.StarDao;
import com.casesoft.dmc.model.mirror.StarInfo;

@Service
@Transactional
public class StarInfoService extends AbstractBaseService<StarInfo, String> {
	@Autowired
	private StarDao starDao;
	@Override
	public Page<StarInfo> findPage(Page<StarInfo> page, List<PropertyFilter> filters) {
		return this.starDao.findPage(page, filters);
	}

	@Override
	public void save(StarInfo entity) {
		
		this.starDao.saveOrUpdate(entity);
	}
	@Override
	public StarInfo load(String id) {
		return this.starDao.load(id);
	}

	@Override
	public StarInfo get(String propertyName, Object value) {
		return this.starDao.findUniqueBy(propertyName, value);
	}

	@Override
	public List<StarInfo> find(List<PropertyFilter> filters) {
		return this.starDao.find(filters);
	}

	public StarInfo findById(String id)
	{
		return this.starDao.findUnique("from StarInfo where id=?",new Object[]{id});
	}
	
	public Integer getMaxseqNo()
	{
		
		Object obj = (Integer) this.starDao.findUnique("select max(seqNo) from StarInfo");
		if(CommonUtil.isBlank(obj)){
			return 0;
		}
		return Integer.parseInt(obj.toString());
	}
	
	@Override
	public List<StarInfo> getAll() {
		return this.starDao.getAll();
	}

	@Override
	public <X> List<X> findAll() {
		
		return null;
	}

	@Override
	public void update(StarInfo entity) {
		this.starDao.saveOrUpdate(entity);
	}

	@Override
	public void delete(StarInfo entity) {
		this.starDao.delete(entity);
	}

	@Override
	public void delete(String id) {
		this.starDao.batchExecute("update StarInfo  set seqNo=seqNo-1  where seqNo>(select s.seqNo from StarInfo s where s.id=?)",new Object[]{id});
		this.starDao.delete(id);
	}
	public Integer findMaxColum() {		
		return Integer.parseInt(this.starDao.findUnique("select count(*) from StarInfo").toString());
    }

	public void save(List<StarInfo> starInfoList) {
		this.starDao.doBatchInsert(starInfoList);
	}	

}
