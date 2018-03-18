package com.casesoft.dmc.service.mirror;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.mirror.BrandInfoDao;
import com.casesoft.dmc.model.mirror.BrandInfo;

@Service
@Transactional
public class BrandInfoService extends AbstractBaseService<BrandInfo, String> {

	@Autowired
	private BrandInfoDao brandInfoDao;
	@Override
	public Page<BrandInfo> findPage(Page<BrandInfo> page, List<PropertyFilter> filters) {
		return this.brandInfoDao.findPage(page, filters);
	}

	@Override
	public void save(BrandInfo entity) {
		
		this.brandInfoDao.saveOrUpdate(entity);
	}

	@Override
	public BrandInfo load(String id) {
		return this.brandInfoDao.load(id);
	}

	@Override
	public BrandInfo get(String propertyName, Object value) {
		return this.brandInfoDao.findUniqueBy(propertyName, value);
	}

	@Override
	public List<BrandInfo> find(List<PropertyFilter> filters) {
		return this.brandInfoDao.find(filters);
	}

	@Override
	public List<BrandInfo> getAll() {
		return this.brandInfoDao.getAll();
	}

	@Override
	public <X> List<X> findAll() {
		
		return null;
	}

	@Override
	public void update(BrandInfo entity) {
		this.brandInfoDao.saveOrUpdate(entity);
	}

	@Override
	public void delete(BrandInfo entity) {
		this.brandInfoDao.delete(entity);
	}
	public BrandInfo findBrandBybrand(String brand)
	{
		return this.brandInfoDao.findUnique("from BrandInfo where brand='"+brand+"'");
	}

	@Override
	public void delete(String id) {
		this.brandInfoDao.batchExecute("update BrandInfo  set seqNo=seqNo-1  where seqNo>(select b.seqNo from BrandInfo b where b.brand=?)",new Object[]{id});
		this.brandInfoDao.delete(id);
	}
	
	public Integer getMaxSeqNo() {
		Object seq = this.brandInfoDao.findUnique("select max(seqNo) from BrandInfo");
		if(CommonUtil.isBlank(seq)) {
			return 0;
		}
		return Integer.parseInt(seq.toString());
	}
	
	public Integer findMaxColum() {
		
		return Integer.parseInt(this.brandInfoDao.findUnique("select count(*) from BrandInfo").toString());
		
	}

	public void save(List<BrandInfo> brandList) {
		this.brandInfoDao.doBatchInsert(brandList);
	}

}
