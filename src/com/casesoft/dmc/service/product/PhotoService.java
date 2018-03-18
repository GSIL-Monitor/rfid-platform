package com.casesoft.dmc.service.product;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.product.PhotoDao;
import com.casesoft.dmc.model.product.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by session on 2017-05-04.
 */
@Service
@Transactional
public class PhotoService implements IBaseService<Photo,String>{

	@Autowired
	private PhotoDao photoDao;
	@Override
	public Page<Photo> findPage(Page<Photo> page, List<PropertyFilter> filters) {
		return this.photoDao.findPage(page,filters);
	}

	@Override
	public void save(Photo photo) {
		this.photoDao.saveOrUpdate(photo);
	}

	public void saveBatch(List<Photo> photoList){
		this.photoDao.doBatchInsert(photoList);
	}

	@Override
	public Photo load(String id) {
		return null;
	}

	@Override
	public Photo get(String propertyName, Object value) {
		return null;
	}

	@Override
	public List<Photo> find(List<PropertyFilter> filters) {
		return this.photoDao.find(filters);
	}

	public Integer getMaxSeq(){
		String hql="select Max(seqNo) from Photo";
		Integer seqNo=(Integer) this.photoDao.findUnique(hql,new Object[]{});
		return seqNo==null?1:seqNo+1;
	}

	public Photo getPhotoByUrl(String src){
		System.out.print(src);
		String hql="from Photo p where p.src =?";
		return this.photoDao.findUnique(hql,new Object[]{src});
//		return photoList==null?null:photoList.get(0);
	}
	@Override
	public List<Photo> getAll() {
		return this.photoDao.getAll();
	}

	@Override
	public <X> List<X> findAll() {
		return null;
	}

	@Override
	public void update(Photo entity) {

	}

	@Override
	public void delete(Photo entity) {
		this.photoDao.delete(entity);
	}

	@Override
	public void delete(String id) {

	}
}
