package com.casesoft.dmc.service.mirror;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.mirror.NewProductDao;
import com.casesoft.dmc.model.mirror.NewProduct;

@Service
@Transactional
public class NewProductService extends AbstractBaseService<NewProduct, String> {
	@Autowired
	private NewProductDao newProductDao;

    @Transactional(readOnly = true)
	@Override
	public Page<NewProduct> findPage(Page<NewProduct> page, List<PropertyFilter> filters) {
		return this.newProductDao.findPage(page, filters);
	}

    @Transactional(readOnly = true)
    public NewProduct findByStyleId(String styleId){
        String hql="from NewProduct np where np.styleId=?";
        return this.newProductDao.findUnique(hql,new Object[]{styleId});
    }

    @Transactional(readOnly = true)
	@Override
	public void save(NewProduct entity) {
		
		this.newProductDao.saveOrUpdate(entity);
	}

	@Override
	public NewProduct load(String id) {
		return this.newProductDao.load(id);
	}

	@Override
	public NewProduct get(String propertyName, Object value) {
		return this.newProductDao.findUniqueBy(propertyName, value);
	}

	@Override
	public List<NewProduct> find(List<PropertyFilter> filters) {
		return this.newProductDao.find(filters);
	}

	@Override
	public List<NewProduct> getAll() {
		return this.newProductDao.getAll();
	}

	@Override
	public <X> List<X> findAll() {
		
		return null;
	}

	@Override
	public void update(NewProduct entity) {
		this.newProductDao.saveOrUpdate(entity);
	}

	@Override
	public void delete(NewProduct entity) {
		this.newProductDao.delete(entity);
	}

	@Override
	public void delete(String id) {
		this.newProductDao.batchExecute("update NewProduct  set seqNo=seqNo-1  where seqNo>(select p.seqNo from NewProduct p where p.styleId=?)",new Object[]{id});
		this.newProductDao.delete(id);
	}

	public <T> List<T> findStockList(String styleId, String colorId) {
		return this.newProductDao.find("from WmsStockInfo where styleId=? and colorId in ("+colorId+")", new Object[]{styleId});
	}
	
	public <T> List<T> findOtherSkuStockList(String styleId, String sizeId) {
		return this.newProductDao.find("from WmsStockInfo where styleId=? and sizeId!=? ", new Object[]{styleId,sizeId});
	}
	
    public Integer findMaxColum() {		
    	return Integer.parseInt(this.newProductDao.findUnique("select count(*) from NewProduct").toString());
	}

	public void save(List<NewProduct> newProductList) {
		this.newProductDao.doBatchInsert(newProductList);
	}

}
