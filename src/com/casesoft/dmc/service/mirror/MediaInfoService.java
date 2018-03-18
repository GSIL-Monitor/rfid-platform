package com.casesoft.dmc.service.mirror;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.mirror.MediaDao;
import com.casesoft.dmc.model.mirror.MediaInfo;

@Service
@Transactional
public class MediaInfoService extends AbstractBaseService<MediaInfo, String> {

	@Autowired
	private MediaDao mediaDao;

    @Transactional(readOnly = true)
    @Override
	public Page<MediaInfo> findPage(Page<MediaInfo> page, List<PropertyFilter> filters) {
		return this.mediaDao.findPage(page, filters);
	}

    @Transactional(readOnly = true)
    public MediaInfo findById(String id){
        String hql="from MediaInfo m where m.id=?";
        return this.mediaDao.findUnique(hql,new Object[]{id});
    }
	@Override
	public void save(MediaInfo entity) {
		
		this.mediaDao.saveOrUpdate(entity);
	}

    public int findMaxSeqNo(){
        String hql="select max(seqNo) from MediaInfo";
        Object obj=this.mediaDao.findUnique(hql);
        if (obj==null){
            return 0;
        }else {
            return Integer.parseInt(obj.toString());
        }
    }

	@Override
	public MediaInfo load(String id) {
		return this.mediaDao.load(id);
	}

	@Override
	public MediaInfo get(String propertyName, Object value) {
		return this.mediaDao.findUniqueBy(propertyName, value);
	}

	@Override
	public List<MediaInfo> find(List<PropertyFilter> filters) {
		return this.mediaDao.find(filters);
	}

	@Override
	public List<MediaInfo> getAll() {
		return this.mediaDao.getAll();
	}

	@Override
	public <X> List<X> findAll() {
		
		return null;
	}

	@Override
	public void update(MediaInfo entity) {
		this.mediaDao.saveOrUpdate(entity);
	}

	@Override
	public void delete(MediaInfo entity) {
		this.mediaDao.delete(entity);
	}

	@Override
	public void delete(String id) {
		this.mediaDao.batchExecute("update MediaInfo  set seqNo=seqNo-1  where seqNo>(select m.seqNo from MediaInfo m where m.id=?)",new Object[]{id});
		this.mediaDao.delete(id);
	}
	public Integer findMaxColum() {		
		return Integer.parseInt(this.mediaDao.findUnique("select count(*) from MediaInfo").toString());
	}

	public void save(List<MediaInfo> mediaList) {
		this.mediaDao.doBatchInsert(mediaList);
	}

}
