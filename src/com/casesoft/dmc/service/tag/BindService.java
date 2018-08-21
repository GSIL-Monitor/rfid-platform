package com.casesoft.dmc.service.tag;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.tag.BindDao;
import com.casesoft.dmc.extend.third.descriptor.DataResult;
import com.casesoft.dmc.extend.third.request.BaseService;
import com.casesoft.dmc.extend.third.request.RequestPageData;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.tag.EpcBindBarcode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BindService extends BaseService<EpcBindBarcode,String> {

	@Autowired
	private BindDao bindDao;
	public Product findProductBybarcode(String code) {
		return this.bindDao.findUnique("from Product p where barcode = ? or code=?",new Object[]{code,code});
	}

    @Override
    public DataResult find(RequestPageData<?> request) {

        return bindDao.find(request);
    }



    @Override
	@Transactional(readOnly = true)
	public Page<EpcBindBarcode> findPage(Page<EpcBindBarcode> page,
			List<PropertyFilter> filters) {
		
		return this.bindDao.findPage(page, filters);
	}

	@Transactional(readOnly = true)
	public EpcBindBarcode findBycode(String barcode){
		
		return this.bindDao.findUnique(barcode);
	}
	@Override
	public void save(EpcBindBarcode entity) {
	   this.bindDao.saveOrUpdate(entity);
		
	}

	@Override
	public EpcBindBarcode load(String epc) {
		EpcBindBarcode epcBindBarcode=this.bindDao.load(epc);
		return epcBindBarcode;
	}

	@Override
	public EpcBindBarcode get(String epc, Object value) {

		return this.bindDao.get(value.toString());
	}

	@Override
	public List<EpcBindBarcode> find(List<PropertyFilter> filters) {

		return this.bindDao.find(filters);
	}

	@Override
	public List<EpcBindBarcode> getAll() {

		return this.bindDao.getAll();
	}

    public void saveList(List<EpcBindBarcode> tmps) {
		long version=findMaxProductTempVersion()+1;
		if(CommonUtil.isNotBlank(tmps)){
			for(EpcBindBarcode productTemp:tmps){
				productTemp.setDisabled(false);
				productTemp.setVersion(version);
			}
            this.bindDao.doBatchInsert(tmps);
        }
    }
	public long findMaxProductTempVersion() {
		String hql = "select max(productTemp.version) from EpcBindBarcode  productTemp";
		Number i = this.bindDao.findUnique(hql, new Object[] { });
		return i == null ? 0 : i.longValue();
	}
    public List<EpcBindBarcode> listAllBind(){
        return this.bindDao.find("from EpcBindBarcode bind", new Object[]{});
    }

	@Override
	public <X> List<X> findAll() {

		return null;
	}

	@Override
	public void update(EpcBindBarcode entity) {

		
	}

	@Override
	public void delete(EpcBindBarcode entity) {
    	entity.setDisabled(true);
		this.bindDao.update(entity);
	}

	@Override
	public void delete(String id) {

		
	}
	
	public void deleteBindInEPCList(String codes) {
		  if(CommonUtil.isNotBlank(codes)){
			  long version=findMaxProductTempVersion()+1;
			  this.bindDao.batchExecute("update EpcBindBarcode  epcbindbarcode " +
					  "set epcbindbarcode.version=?,epcbindbarcode.disabled=? where "+codes,new Object[]{version,true});
		  }
	}

	public long getMaxVersionNo() {
		String hql = "select max(bind.version) from EpcBindBarcode  bind";
		Number i = this.bindDao.findUnique(hql, new Object[] { });
		return i == null ? 0 : i.longValue();
	}

}
