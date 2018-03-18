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
 * Created by WingLi on 2016-12-23.
 */
@Service
@Transactional
public class AgentService implements IBaseService<Unit,String> {

    @Autowired
    private UnitDao unitDao;

    @Transactional(readOnly = true)
    @Override
    public Page<Unit> findPage(Page<Unit> page, List<PropertyFilter> filters) {
        return this.unitDao.findPage(page,filters);
    }

    public Unit findUnique(String code) {
        return this.unitDao.findUniqueBy("code",code);
    }

    @Transactional(readOnly = true)
    public String findMaxCode(int type) {
        String unitFlag = Constant.UnitCodePrefix.Agent;//"AUTO_AG";

        String hql = "select max(CAST(SUBSTRING(unit.code,"+(unitFlag.length()+1)+"),integer))"
                + " from Unit as unit where unit.type=? and unit.src='01' and unit.code like'"+unitFlag+"%'";
        Integer code = this.unitDao.findUnique(hql, new Object[] { type});
        return code == null ? (unitFlag + "001") : unitFlag
                + CommonUtil.convertIntToString(code + 1, 3);
    }
    public String findMaxCodes(int type) {
        String unitFlag = Constant.UnitCodePrefix.Franchisee;//"AUTO_AG";

        String hql = "select max(CAST(SUBSTRING(unit.code,"+(unitFlag.length()+1)+"),integer))"
                + " from Unit as unit where unit.type=? and unit.src='01' and unit.code like'"+unitFlag+"%'";
        Integer code = this.unitDao.findUnique(hql, new Object[] { type});
        return code == null ? (unitFlag + "001") : unitFlag
                + CommonUtil.convertIntToString(code + 1, 3);
    }

    @Override
    public void save(Unit entity) {
 /*   	this.unitDao.getSession().flush();*/
        this.unitDao.saveOrUpdate(entity);
    }

    @Transactional(readOnly = true)
    @Override
    public Unit load(String id) {
        return null;
    }
    @Transactional(readOnly = true)
    @Override
    public Unit get(String propertyName, Object value) {
        return null;
    }
    @Transactional(readOnly = true)
    @Override
    public List<Unit> find(List<PropertyFilter> filters) {
        return this.unitDao.find(filters);
    }
    @Transactional(readOnly = true)
    @Override
    public List<Unit> getAll() {
        return this.unitDao.getAll();
    }
    @Transactional(readOnly = true)
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
