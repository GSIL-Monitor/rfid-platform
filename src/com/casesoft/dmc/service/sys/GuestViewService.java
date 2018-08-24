package com.casesoft.dmc.service.sys;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.sys.GuestViewDao;
import com.casesoft.dmc.model.sys.GuestView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alvin on 2017/7/11.
 */
@Service
@Transactional
public class GuestViewService implements IBaseService<GuestView, String> {

    @Autowired
    private GuestViewDao guestViewDao;

    @Override
    public Page<GuestView> findPage(Page<GuestView> page, List<PropertyFilter> filters) {
        return this.guestViewDao.findPage(page, filters);
    }

    @Override
    public void save(GuestView entity) {

    }

    @Override
    public GuestView load(String id) {
        return this.guestViewDao.load(id);
    }

    @Override
    public GuestView get(String propertyName, Object value) {
        return this.guestViewDao.findUniqueBy(propertyName, value);
    }

    @Override
    public List<GuestView> find(List<PropertyFilter> filters) {
        return this.guestViewDao.find(filters);
    }

    @Override
    public List<GuestView> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(GuestView entity) {

    }

    @Override
    public void delete(GuestView entity) {

    }

    @Override
    public void delete(String id) {

    }

    public List<GuestView> findGuests() {
        return this.guestViewDao.find("from GuestView");
    }

    public Map<String, String> countInfoByCondition(String unitType, String nameOrTel) {
        String hql = "select count(storedValue) from GuestView where unitType like ? and storedValue>0 and ( name like ? or tel like ?)";
        Long storedValueCount = this.guestViewDao.findUnique(hql, new Object[]{unitType + '%', nameOrTel + '%', nameOrTel + '%'});

        hql = "select sum(storedValue) from GuestView where unitType like ?  and storedValue>0 and ( name like ? or tel like ?)";
        Double storedValueSum = this.guestViewDao.findUnique(hql, new Object[]{unitType + '%', nameOrTel + '%', nameOrTel + '%'});

        hql = "select count(owingValue) from GuestView where unitType like ?  and owingValue>0 and ( name like ? or tel like ?)";
        Long owingValueCount = this.guestViewDao.findUnique(hql, new Object[]{unitType + '%', nameOrTel + '%', nameOrTel + '%'});

        hql = "select sum(owingValue) from GuestView where unitType like ? and owingValue>0 and ( name like ? or tel like ?)";
        Double owingValueSum = this.guestViewDao.findUnique(hql, new Object[]{unitType + '%', nameOrTel + '%', nameOrTel + '%'});

        HashMap<String, String> numInfoMap = new HashMap<>();
        numInfoMap.put("totStoredQty", CommonUtil.isBlank(storedValueCount) ? "0" : storedValueCount.toString());
        numInfoMap.put("totStoredValue", CommonUtil.isBlank(storedValueSum) ? "0" : storedValueSum.toString());
        numInfoMap.put("totOwingQty", CommonUtil.isBlank(owingValueCount) ? "0" : owingValueCount.toString());
        numInfoMap.put("totOwingValue", CommonUtil.isBlank(owingValueSum) ? "0" : owingValueSum.toString());

            return numInfoMap;
    }

    public String getGuestNum(String unitType, String nameOrTel) {
        String hql = "select count(id) from GuestView where unitType like ? and ( name like ? or tel like ?)";
        Long guestNum = this.guestViewDao.findUnique(hql, new Object[]{'%' + unitType + '%', '%' + nameOrTel + '%', '%' + nameOrTel + '%'});

        return CommonUtil.isBlank(guestNum) ? "0" : guestNum.toString();
    }

    public Map<String, String> countInfoByCondition(String unitType, String nameOrTel, String ownerId) {
        String hql = "select count(storedValue) from GuestView where unitType like ? and storedValue>0 and ( name like ? or tel like ?) and ownerId=?";
        Long storedValueCount = this.guestViewDao.findUnique(hql, new Object[]{'%' + unitType + '%', '%' + nameOrTel + '%', '%' + nameOrTel + '%', ownerId});

        hql = "select sum(storedValue) from GuestView where unitType like ?  and storedValue>0 and ( name like ? or tel like ?) and ownerId=?";
        Double storedValueSum = this.guestViewDao.findUnique(hql, new Object[]{'%' + unitType + '%', '%' + nameOrTel + '%', '%' + nameOrTel + '%', ownerId});

        hql = "select count(owingValue) from GuestView where unitType like ?  and owingValue>0 and ( name like ? or tel like ?) and ownerId=?";
        Long owingValueCount = this.guestViewDao.findUnique(hql, new Object[]{'%' + unitType + '%', '%' + nameOrTel + '%', '%' + nameOrTel + '%', ownerId});

        hql = "select sum(owingValue) from GuestView where unitType like ? and owingValue>0 and ( name like ? or tel like ?) and ownerId=?";
        Double owingValueSum = this.guestViewDao.findUnique(hql, new Object[]{'%' + unitType + '%', '%' + nameOrTel + '%', '%' + nameOrTel + '%', ownerId});

        HashMap<String, String> numInfoMap = new HashMap<>();
        numInfoMap.put("totStoredQty", CommonUtil.isBlank(storedValueCount) ? "0" : storedValueCount.toString());
        numInfoMap.put("totStoredValue", CommonUtil.isBlank(storedValueSum) ? "0" : storedValueSum.toString());
        numInfoMap.put("totOwingQty", CommonUtil.isBlank(owingValueCount) ? "0" : owingValueCount.toString());
        numInfoMap.put("totOwingValue", CommonUtil.isBlank(owingValueSum) ? "0" : owingValueSum.toString());

        return numInfoMap;
    }

    public String getGuestNum(String unitType, String nameOrTel, String ownerId) {
        String hql = "select count(id) from GuestView where unitType like ? and ( name like ? or tel like ?) and ownerId=?";
        Long guestNum = this.guestViewDao.findUnique(hql, new Object[]{'%' + unitType + '%', '%' + nameOrTel + '%', '%' + nameOrTel + '%', ownerId});

        return CommonUtil.isBlank(guestNum) ? "0" : guestNum.toString();
    }


    public GuestView findByid(String id){
        String hql="from GuestView t where t.id=?";
        GuestView guestView = this.guestViewDao.findUnique(hql, new Object[]{id});
        return guestView;
    }

    public List<GuestView> findbybirth(String birth){
        String hql="from GuestView t where to_char(t.birth,'MM-dd')=?";
        List<GuestView> GuestViewlist = this.guestViewDao.find(hql, new Object[]{birth});
        return GuestViewlist;
    }

}
