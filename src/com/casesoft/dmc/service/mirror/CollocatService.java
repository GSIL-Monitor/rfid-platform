package com.casesoft.dmc.service.mirror;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.mirror.MirrorCollocationDao;
import com.casesoft.dmc.model.mirror.Collocat;

@Service
@Transactional
public class CollocatService extends AbstractBaseService<Collocat, String> {
	@Autowired
	private MirrorCollocationDao mirrorCollocationDao;
	@Override
	public Page<Collocat> findPage(Page<Collocat> page, List<PropertyFilter> filters) {
		return this.mirrorCollocationDao.findPage(page, filters);
	}

	@Override
	public void save(Collocat entity) {
		
		this.mirrorCollocationDao.saveOrUpdate(entity);
	}

	@Override
	public Collocat load(String id) {
		return this.mirrorCollocationDao.load(id);
	}

	public Collocat findCollocatById(String id){
		return (Collocat) this.mirrorCollocationDao.findUnique("from Collocat where id='"+id+"'");
	}
	
	public Integer findMaxSeqNofromCollocat(){
		Object seq =(Integer)this.mirrorCollocationDao.findUnique("select max(seqNo) from Collocat");
		if(CommonUtil.isBlank(seq)){
			return 0;
		}
		return Integer.parseInt(seq.toString());
	}
	
	@Override
	public Collocat get(String propertyName, Object value) {
		return this.mirrorCollocationDao.findUniqueBy(propertyName, value);
	}

	@Override
	public List<Collocat> find(List<PropertyFilter> filters) {
		return this.mirrorCollocationDao.find(filters);
	}

	@Override
	public List<Collocat> getAll() {
		return this.mirrorCollocationDao.getAll();
	}

	@Override
	public <X> List<X> findAll() {
		
		return null;
	}

	@Override
	public void update(Collocat entity) {
		this.mirrorCollocationDao.saveOrUpdate(entity);
	}

	@Override
	public void delete(Collocat entity) {
		this.mirrorCollocationDao.delete(entity);
	}

	@Override
	public void delete(String id) {
		this.mirrorCollocationDao.batchExecute("update Collocat  set seqNo=seqNo-1  where seqNo>(select c.seqNo from Collocat c where c.id=?)",new Object[]{id});
		this.mirrorCollocationDao.delete(id);
	}

	public List<Collocat> findCollocatBystyleId(String styleIds) {
		return this.mirrorCollocationDao.find("from Collocat c where c.id like '%"+styleIds+"%'");
	}
	
    public <X> List<X> findStyleByStyleIds(String styleIds) {		
		return this.mirrorCollocationDao.find("from Style where styleId in ("+styleIds+")");
	}
    public Integer findMaxColum() {		
		return Integer.parseInt(this.mirrorCollocationDao.findUnique("select count(*) from Collocat").toString());
	}

	public void save(List<Collocat> collocatList) {
		this.mirrorCollocationDao.doBatchInsert(collocatList);
	}


}
