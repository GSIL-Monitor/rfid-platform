package com.casesoft.dmc.extend.tiantan.service;

import com.casesoft.dmc.core.service.ISynErpService;
import com.casesoft.dmc.extend.third.model.DayThirdStock;
import com.casesoft.dmc.extend.third.model.ThirdStock;
import com.casesoft.dmc.extend.tiantan.dao.TiantanSynErpDao;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.product.*;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.shop.FittingRecord;
import com.casesoft.dmc.model.sys.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TiantanSynErpService implements ISynErpService{
	//@Autowired
	private TiantanSynErpDao tiantanSynErpDao;


	@Override
	public List<Style> synchronizeStyle(String paramString) throws Exception {
 		return this.tiantanSynErpDao.findAllStyle();
	}

	@Override
	public List<Color> synchronizeColor(String paramString) throws Exception {
 		return this.tiantanSynErpDao.findAllColor();
	}

	@Override
	public List<Size> synchronizeSize(String paramString) throws Exception {
		return this.tiantanSynErpDao.findAllSize();
	}

	@Override
	public List<Product> synchronizeProduct(String paramString)
			throws Exception {
		// TODO Auto-generated method stub
		return this.tiantanSynErpDao.findAllProduct();
	}

	@Override
	public List<SizeSort> synchronizeSizeSort(String paramString) {
		return this.tiantanSynErpDao.findAllSizeSort();
	}



	@Override
	public List<PropertyKey> synchronizeProperty() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> synchronizeUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> synchronizeUser(String ownerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Customer> synchronizeCustomer(String ownerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Unit> synchronizeUnit(int paramInt) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Unit> synchronizeShop() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Unit> synchronizeWarehouse() throws Exception {
		// TODO Auto-generated method stub
		return this.tiantanSynErpDao.findAllWharehouse("1");
	}

	@Override
	public List<Unit> synchronizeVendor() throws Exception {
		// TODO Auto-generated method stub
		return this.tiantanSynErpDao.findAllVendor("1");
	}

	@Override
	public List<Unit> synchronizeFactory() throws Exception {
		// TODO Auto-generated method stub
		return this.tiantanSynErpDao.findAllFactory("1");
	}

	@Override
	public List<Unit> synchronizeAgent() throws Exception {
		// TODO Auto-generated method stub
		return this.tiantanSynErpDao.findAllAgent("1");
	}

	@Override
	public List<Role> synchronizeRole() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RoleRes> synchronizeRoleRes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Resource> synchronizeResource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List synchronizeProperty(String brandcode) {
		// TODO Auto-generated method stub
		return this.tiantanSynErpDao.findAllPropertyKey(brandcode);
	}

	@Override
	public List synchronizeStorage() throws Exception {
		// TODO Auto-generated method stub
		return this.tiantanSynErpDao.findAllWharehouse("1");
	}

	@Override
	public List synchronizeShop(String ownerId) throws Exception {
		// TODO Auto-generated method stub
		return this.tiantanSynErpDao.findAllShop(ownerId);
	}

	@Override
	public List<Unit> synchronizeVender(String ownerId) throws Exception {
		// TODO Auto-generated method stub
		return this.tiantanSynErpDao.findAllVendor(ownerId);
	}

	public TiantanSynErpDao getTiantanSynErpDao() {
		return tiantanSynErpDao;
	}

	public void setTiantanSynErpDao(TiantanSynErpDao tianTanSynErpDao) {
		this.tiantanSynErpDao = tianTanSynErpDao;
	}

	@Override
	public void synchronizeFitting(List<FittingRecord> fittings) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ThirdStock> synchronizeThirdStock(String ownerId) {
		return null;
	}

	@Override
	public List<DayThirdStock> synchronizeDayThirdStock(String ownerId, String date) {
		return null;
	}

    @Override
    public ProductInfoList synProductInfoList(String storeCode) {
        return null;
    }
}
