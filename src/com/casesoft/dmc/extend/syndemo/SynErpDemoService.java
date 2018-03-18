package com.casesoft.dmc.extend.syndemo;

import com.casesoft.dmc.core.service.ISynErpService;
import com.casesoft.dmc.extend.third.model.DayThirdStock;
import com.casesoft.dmc.extend.third.model.ThirdStock;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.product.*;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.shop.FittingRecord;
import com.casesoft.dmc.model.sys.*;

import java.util.List;

/**
 * Created by WingLi on 2016-01-19.
 */
public class SynErpDemoService implements ISynErpService{


    @Override
    public List<Style> synchronizeStyle(String paramString) throws Exception {
        return null;
    }

    @Override
    public List<Color> synchronizeColor(String paramString) throws Exception {
        return null;
    }

    @Override
    public List<Size> synchronizeSize(String paramString) throws Exception {
        return null;
    }

    @Override
    public List<Product> synchronizeProduct(String paramString) throws Exception {
        return null;
    }


    @Override
    public List<PropertyKey> synchronizeProperty() {
        return null;
    }

    @Override
    public List<User> synchronizeUser() {
        return null;
    }

    @Override
    public List<User> synchronizeUser(String ownerId) {
        return null;
    }

    @Override
    public List<Customer> synchronizeCustomer(String ownerId) {
        return null;
    }

    @Override
    public List<Unit> synchronizeUnit(int paramInt) throws Exception {
        return null;
    }

    @Override
    public List<Unit> synchronizeShop() throws Exception {
        return null;
    }

    @Override
    public List<Unit> synchronizeWarehouse() throws Exception {
        return null;
    }

    @Override
    public List<Unit> synchronizeVendor() throws Exception {
        return null;
    }

    @Override
    public List<Unit> synchronizeFactory() throws Exception {
        return null;
    }

    @Override
    public List<Unit> synchronizeAgent() throws Exception {
        return null;
    }

    @Override
    public List<Role> synchronizeRole() {
        return null;
    }

    @Override
    public List<RoleRes> synchronizeRoleRes() {
        return null;
    }

    @Override
    public List<Resource> synchronizeResource() {
        return null;
    }

    @Override
    public List synchronizeProperty(String brandcode) {
        return null;
    }

    @Override
    public List synchronizeStorage() throws Exception {
        return null;
    }

    @Override
    public List synchronizeShop(String ownerId) throws Exception {
        return null;
    }

    @Override
    public List<Unit> synchronizeVender(String ownerId) throws Exception {
        return null;
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
	public List<SizeSort> synchronizeSizeSort(String paramString) {
		// TODO Auto-generated method stub
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
