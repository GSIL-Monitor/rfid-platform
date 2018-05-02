package com.casesoft.dmc.service.sys.impl;

import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.sys.UnitDao;
import com.casesoft.dmc.model.location.Area;
import com.casesoft.dmc.model.location.City;
import com.casesoft.dmc.model.location.Province;
import com.casesoft.dmc.model.sys.Unit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UnitService extends AbstractBaseService<Unit, String> {

    @Autowired
    private UnitDao unitDao;

    @Override
    @Transactional(readOnly = true)
    public Page<Unit> findPage(Page<Unit> page, List<PropertyFilter> filters) {
        return this.unitDao.findPage(page, filters);
    }

    @Override
    public void save(Unit entity) {
        this.unitDao.save(entity);
    }

    public void saveList(List<Unit> unitList) {
        this.unitDao.doBatchInsert(unitList);
    }

    @Override
    public Unit load(String id) {
        return this.unitDao.load(id);
    }

    @Transactional(readOnly = true)
    public String findMaxCode(int type) {
        String unitFlag = "";
        switch (type) {
            case Constant.UnitType.Headquarters:// .1:
                unitFlag = Constant.UnitCodePrefix.Headquarters;//"B";
                break;
            case Constant.UnitType.Agent:// 2:
                unitFlag = Constant.UnitCodePrefix.Agent;//"A";
                break;
            case Constant.UnitType.Factory://3:
                unitFlag = Constant.UnitCodePrefix.Factory;//"F";
                break;
            case Constant.UnitType.Shop://4:
                unitFlag = Constant.UnitCodePrefix.Shop;//"S";
                break;
            case Constant.UnitType.Warehouse://9:
                unitFlag = Constant.UnitCodePrefix.Warehouse;//"W";
                break;
            case Constant.UnitType.SampleRoom://6:
                unitFlag = Constant.UnitCodePrefix.SampleRoom;//"E";
                break;
            case Constant.UnitType.Department://7:
                unitFlag = Constant.UnitCodePrefix.Department;//"D";
                break;
            case Constant.UnitType.Vender://0:
                unitFlag = Constant.UnitCodePrefix.Vender;//"V";
                break;
        }
        String hql = "select max(CAST(SUBSTRING(unit.code," + (unitFlag.length() + 1) + "),integer))"
                + " from Unit as unit where unit.type=? and unit.isThird is null";
        Integer code = this.unitDao.findUnique(hql, new Object[]{type});
        return code == null ? (unitFlag + "001") : unitFlag
                + CommonUtil.convertIntToString(code + 1, 3);
    }

    @Override
    public Unit get(String propertyName, Object value) {
        return this.unitDao.findUniqueBy(propertyName, value);
    }

    public Unit getunitbyId(String id) {
        return this.unitDao.findUnique("from Unit where id = ?", new Object[]{id});
    }

    @Override
    @Transactional(readOnly = true)
    public List<Unit> find(List<PropertyFilter> filters) {
        return this.unitDao.find(filters);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Unit> getAll() {
        return this.unitDao.getAll();
    }

    public List<Unit> find(String propertyName, Object value) {
        return this.unitDao.findBy(propertyName, value);
    }
    public List<Unit> findbytype(String types) {
        String hql="from Unit t where t.type in ("+types+")";
        List<Unit> list=this.unitDao.find(hql);
        return list;
    }
    @Override
    public void update(Unit entity) {
        this.unitDao.saveOrUpdate(entity);

    }

    @Override
    public void delete(Unit entity) {

    }

    @Override
    public void delete(String id) {

    }

    public UnitDao getUnitDao() {
        return unitDao;
    }

    public void setUnitDao(UnitDao unitDao) {
        this.unitDao = unitDao;
    }

    @Override
    public <X> List<X> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    public Unit getCompanyHeader() {
        String hql = "from Unit u where u.type=1";
        return this.unitDao.findUnique(hql, new Object[]{});
    }

    public List<Unit> findUnitbyNames(String names) {
        String hql = "from Unit u where u.name in (" + names + ") order by u.name";
        return this.unitDao.find(hql);
    }

    public Unit findUnitByCode(String code, Integer type) {
        String hql = "from Unit u where u.code =? and type=?";
        return this.unitDao.findUnique(hql,new Object[]{code, type});
    }

    public List<Unit> findShopAddrNotNull() {
        String hql = "from Unit u where u.type = 4 and u.address is not null";
        return this.unitDao.find(hql);
    }

    public List<Unit> findShop() {
        return this.unitDao.find("from Unit u where u.type = 4 and groupId='DP'");
    }

    public List<Unit> findVendors() {
        return this.unitDao.find("from Unit where type=0");
    }


    public Map<String, String> findunitsumbynameandphone(String nameOrTel) {
        HashMap<String, String> map = new HashMap<>();
        Object sumowingValue = this.unitDao.findUnique("select sum(t.owingValue)  from Unit t where t.owingValue > 0 and t.type=0 and (t.name like '%" + nameOrTel + "% ' or t.phone like '%" + nameOrTel + "%')");
        if (CommonUtil.isBlank(sumowingValue)) {
            sumowingValue = "0";
        }
        map.put("totOwingValue", sumowingValue.toString());
        Object countowingValue = this.unitDao.findUnique("select count(t.owingValue)  from Unit t where t.owingValue > 0 and t.type=0 and (t.name like '%" + nameOrTel + "% ' or t.phone like '%" + nameOrTel + "%')");
        if (CommonUtil.isBlank(countowingValue)) {
            countowingValue = "0";
        }
        map.put("totOwingQty", countowingValue.toString());
        Object sumstoredValue = this.unitDao.findUnique("select sum(t.storedValue)  from Unit t where t.storedValue > 0 and t.type=0 and  (t.name like '%" + nameOrTel + "% ' or t.phone like '%" + nameOrTel + "%')");
        if (CommonUtil.isBlank(sumstoredValue)) {
            sumstoredValue = "0";
        }
        map.put("totStoredValue", sumstoredValue.toString());
        Object countstoredValue = this.unitDao.findUnique("select count(t.storedValue) from Unit t where t.storedValue > 0 and t.type=0 and (t.name like '%" + nameOrTel + "% ' or t.phone like '%" + nameOrTel + "%')");
        if (CommonUtil.isBlank(countstoredValue)) {
            countstoredValue = "0";
        }
        map.put("totStroedQty", countstoredValue.toString());
        return map;
    }

    public List<Province> findAllProvince() {
        return this.unitDao.find("from Province");
    }

    public List<City> findCityInfo(String provinceId) {
        return this.unitDao.find("from City where father=?",new Object[]{provinceId});
    }

    public List<Area> findAreaInfo(String cityId) {
        return this.unitDao.find("from Area where father=?",new Object[]{cityId});
    }
    /**
     * 设置门店默认客户
     * */
    public int setDefaultCustomer(String shopId, String id, String userId) {
        return this.unitDao.batchExecute("update Unit set defalutCustomerId=?,updaterId=?,updateTime=? where id=?",id,userId,new Date(),shopId);
    }
    /**
     * 设置门店默认销售员
     * */
    public int setDefalutSaleStaff(String id, String shopId, String userId) {
        return this.unitDao.batchExecute("update Unit set defaultSaleStaffId=?,updaterId=?,updateTime=? where id=?",id,userId,new Date(),shopId);
    }

 /* public Object findunitcoountbynameandphone(String name,String phone){
    Object unique = this.unitDao.findUnique("select sum(t.storedValue) ,count(t.storedValue) from Unit t where t.storedValue > 0 and (t.name like '%" + name + "% ' and t.phone like '%" + phone + "%')");
    return unique;
  }*/

}
