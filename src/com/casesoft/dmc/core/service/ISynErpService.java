package com.casesoft.dmc.core.service;

import com.casesoft.dmc.extend.third.model.DayThirdStock;
import com.casesoft.dmc.extend.third.model.ThirdStock;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.product.*;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.shop.FittingRecord;
import com.casesoft.dmc.model.sys.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Wing Li on 2014/6/1.
 */
public interface ISynErpService {

    List<Style> synchronizeStyle(String paramString)
            throws Exception;

    List<Color> synchronizeColor(String paramString)
            throws Exception;

    List<Size> synchronizeSize(String paramString)
            throws Exception;

    List<Product> synchronizeProduct(String paramString)
            throws Exception;


    List<SizeSort> synchronizeSizeSort(String paramString);


    List<PropertyKey> synchronizeProperty();

    List<User> synchronizeUser();

    List<User> synchronizeUser(String ownerId);

    List<Customer> synchronizeCustomer(String ownerId);

    List<Unit> synchronizeUnit(int paramInt) throws Exception;

    List<Unit> synchronizeShop() throws Exception;

    List<Unit> synchronizeWarehouse() throws Exception;

    List<Unit> synchronizeVendor() throws Exception;

    List<Unit> synchronizeFactory() throws Exception;

    List<Unit> synchronizeAgent() throws Exception;

    List<Role> synchronizeRole();

    List<RoleRes> synchronizeRoleRes();

    List<Resource> synchronizeResource();

    List synchronizeProperty(String brandcode);

    List synchronizeStorage() throws Exception;

    List synchronizeShop(String ownerId) throws Exception;

    List<Unit> synchronizeVender(String ownerId) throws Exception;

    void synchronizeFitting(List<FittingRecord> fittings);

    List<ThirdStock> synchronizeThirdStock(String ownerId);

    List<DayThirdStock> synchronizeDayThirdStock(String ownerId, String date);
    ProductInfoList synProductInfoList(String storeCode);

}
