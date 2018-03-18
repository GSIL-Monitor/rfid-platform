package com.casesoft.dmc.service.mirror;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.mirror.ActivityDao;
import com.casesoft.dmc.model.mirror.ActivityInfo;

@Service
@Transactional
public class ActivityInfoService extends AbstractBaseService<ActivityInfo, String> {

	@Autowired
	private ActivityDao activityDao;
	@Override
	public Page<ActivityInfo> findPage(Page<ActivityInfo> page, List<PropertyFilter> filters) {
		return this.activityDao.findPage(page, filters);
	}

    public int findMaxSeqNo(){
        String hql="select max(seqNo) from ActivityInfo";
        Object obj=this.activityDao.findUnique(hql);
        if (obj==null){
               return 0;
        }else {
            return Integer.parseInt(obj.toString());
        }

    }

	@Override
	public void save(ActivityInfo entity) {
		
		this.activityDao.saveOrUpdate(entity);
	}

	@Override
	public ActivityInfo load(String id) {
		return this.activityDao.load(id);
	}

	@Override
	public ActivityInfo get(String propertyName, Object value) {
		return this.activityDao.findUniqueBy(propertyName, value);
	}

	@Override
	public List<ActivityInfo> find(List<PropertyFilter> filters) {
		return this.activityDao.find(filters);
	}

	@Override
	public List<ActivityInfo> getAll() {
		return this.activityDao.getAll();
	}

	@Override
	public <X> List<X> findAll() {
		
		return null;
	}

	@Override
	public void update(ActivityInfo entity) {
		this.activityDao.saveOrUpdate(entity);
	}

	@Override
	public void delete(ActivityInfo entity) {
		this.activityDao.delete(entity);
	}

	@Override
	public void delete(String id) {
		this.activityDao.batchExecute("update ActivityInfo  set seqNo=seqNo-1  where seqNo>(select a.seqNo from ActivityInfo a where a.id=?)",new Object[]{id});
		this.activityDao.delete(id);
	}
    public ActivityInfo findById( String id){
        String hql="from ActivityInfo a where a.id=?";
        return this.activityDao.findUnique(hql,new Object[]{id});
    }
	public Integer findMaxColum() {
		return Integer.parseInt(this.activityDao.findUnique("select count(*) from ActivityInfo").toString());
	}

	public void save(List<ActivityInfo> activityInfoList) {
		this.activityDao.doBatchInsert(activityInfoList);
	}

}
