package com.casesoft.dmc.service.sys;

import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.sys.UnitDao;
import com.casesoft.dmc.model.sys.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by GuoJunwen on 2017-05-08.
 */
@Service
@Transactional
public class FactoryService implements IBaseService<Unit,String> {

    @Autowired
    private UnitDao unitDao;

    @Override
    public Page<Unit> findPage(Page<Unit> page, List<PropertyFilter> filters) {
        return this.unitDao.findPage(page,filters);
    }

    @Override
    public void save(Unit entity) {
      this.unitDao.saveOrUpdate(entity);
    }

    @Transactional(readOnly = true)
    public String findMaxCode() {
        String unitFlag = Constant.UnitCodePrefix.Factory;//"AUTO_FC";

        String hql = "select max(CAST(SUBSTRING(unit.code,"+(unitFlag.length()+1)+"),integer))"
                + " from Unit as unit where unit.type=3 and unit.src='01' and unit.code like'"+unitFlag+"%'";
        Integer code = this.unitDao.findUnique(hql);
        return code == null ? (unitFlag + "001") : unitFlag
                + CommonUtil.convertIntToString(code + 1, 3);
    }

    public Unit findUnitById(String id){
        String hql="from Unit u where u.id=?";
        return this.unitDao.findUnique(hql,new Object[]{id});
    }

    public List findAllFactory(){
        String hql="select u.name,u.code from Unit u where u.type=3";
        return this.unitDao.find(hql);
    }

    @Override
    public Unit load(String id) {
        return null;
    }

    @Override
    public Unit get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<Unit> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<Unit> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(Unit entity) {

    }

    @Override
    public void delete(Unit entity) {

    }

    @Override
    public void delete(String id) {

    }
}
