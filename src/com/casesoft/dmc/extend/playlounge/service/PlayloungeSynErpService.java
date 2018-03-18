package com.casesoft.dmc.extend.playlounge.service;

import com.casesoft.dmc.core.service.ISynErpService;
import com.casesoft.dmc.extend.playlounge.dao.PlayloungeSynErpDao;
import com.casesoft.dmc.extend.third.model.DayThirdStock;
import com.casesoft.dmc.extend.third.model.ThirdStock;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.product.*;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.shop.FittingRecord;
import com.casesoft.dmc.model.sys.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class PlayloungeSynErpService implements ISynErpService {
    private PlayloungeSynErpDao playloungeSynErpDao;


    @Override
    public List<Style> synchronizeStyle(String paramString) throws Exception {
        return this.playloungeSynErpDao.findAllStyle();
    }

    @Override
    public List<Color> synchronizeColor(String paramString) throws Exception {
        return this.playloungeSynErpDao.findAllColor();
    }

    @Override
    public List<Size> synchronizeSize(String paramString) throws Exception {
        return this.playloungeSynErpDao.findAllSize();
    }

    @Override
    public List<Product> synchronizeProduct(String paramString)
            throws Exception {
        // TODO Auto-generated method stub
        return this.playloungeSynErpDao.findAllProduct();
    }

    @Override
    public List<SizeSort> synchronizeSizeSort(String paramString) {
        return this.playloungeSynErpDao.findAllSizeSort();
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
        return this.playloungeSynErpDao.findAllUser("1");
    }

    @Override
    public List<Customer> synchronizeCustomer(String ownerId) {
        // TODO Auto-generated method stub
        return this.playloungeSynErpDao.findAllCustomer("1");
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
        return this.playloungeSynErpDao.findAllWarehouse("1");
    }

    @Override
    public List<Unit> synchronizeVendor() throws Exception {
        // TODO Auto-generated method stub
        return this.playloungeSynErpDao.findAllVendor("1");
    }

    @Override
    public List<Unit> synchronizeFactory() throws Exception {
        // TODO Auto-generated method stub
        return this.playloungeSynErpDao.findAllFactory("1");
    }

    @Override
    public List<Unit> synchronizeAgent() throws Exception {
        // TODO Auto-generated method stub
        return this.playloungeSynErpDao.findAllAgent("1");
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
        return this.playloungeSynErpDao.findAllPropertyKey("1");
    }

    @Override
    public List synchronizeStorage() throws Exception {
        // TODO Auto-generated method stub
        return this.playloungeSynErpDao.findAllWarehouse("1");
    }

    @Override
    public List synchronizeShop(String ownerId) throws Exception {
        // TODO Auto-generated method stub
        return this.playloungeSynErpDao.findAllShop("1");
    }

    @Override
    public List<Unit> synchronizeVender(String ownerId) throws Exception {
        // TODO Auto-generated method stub
        return this.playloungeSynErpDao.findAllVendor("1");
    }

    public PlayloungeSynErpDao getPlayloungeSynErpDao() {
        return playloungeSynErpDao;
    }

    public void setPlayloungeSynErpDao(PlayloungeSynErpDao playloungeSynErpDao) {
        this.playloungeSynErpDao = playloungeSynErpDao;
    }

    public void synchronizeFitting(List<FittingRecord> fittings) {
        this.playloungeSynErpDao.batchFittingRecord(fittings);
    }

    @Override
    public List<ThirdStock> synchronizeThirdStock(String ownerId) {
        return this.playloungeSynErpDao.synchronizeThirdStock();
    }

    @Override
    public List<DayThirdStock> synchronizeDayThirdStock(String ownerId, String date) {
        return this.playloungeSynErpDao.synchronizeDayThirdStock(ownerId,date);
    }

    @Override
    public ProductInfoList synProductInfoList(String storeCode) {
        return null;
    }
}
