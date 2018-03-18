package com.casesoft.dmc.extend.playlounge.dao.basic;

import com.casesoft.dmc.extend.third.model.DayThirdStock;
import com.casesoft.dmc.extend.third.model.ThirdStock;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.product.*;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.shop.FittingRecord;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;

import java.util.List;

public interface ISynErpDao {
    List<Style> findAllStyle();

    List<Color> findAllColor();

    List<ColorGroup> findAllColorGroup();

    List<Size> findAllSize();

    List<Product> findAllProduct();

    List<SizeSort> findAllSizeSort();

    List<PropertyKey> findAllPropertyKey(String ownerId);

    List<Unit> findAllVendor(String ownerId);

    List<Unit> findAllAgent(String ownerId);

    List<Unit> findAllFactory(String ownerId);

    List<Unit> findAllShop(String ownerId);

    List<Unit> findAllWarehouse(String ownerId);

    List<Customer> findAllCustomer(String ownerId);

    List<User> findAllUser(String ownerId);

    void batchFittingRecord(List<FittingRecord> fittings);

    List<ThirdStock> synchronizeThirdStock();

    List<DayThirdStock> synchronizeDayThirdStock(String ownerId,String date);

}
