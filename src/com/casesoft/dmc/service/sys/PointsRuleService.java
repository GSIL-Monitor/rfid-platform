package com.casesoft.dmc.service.sys;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.sys.PointsRuleDao;
import com.casesoft.dmc.extend.third.request.BaseService;
import com.casesoft.dmc.model.sys.PointsRule;
import com.casesoft.dmc.model.sys.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by yushen on 2017/11/3.
 */
@Service
@Transactional
public class PointsRuleService extends BaseService<PointsRule, String> {
    @Autowired
    private PointsRuleDao pointsRuleDao;

    @Override
    public Page<PointsRule> findPage(Page<PointsRule> page, List<PropertyFilter> filters) {
        page = this.pointsRuleDao.findPage(page, filters);
        for (PointsRule pointsRule : page.getRows()) {
            if (pointsRule.isDefaultRule()) {
                pointsRule.setUnitName("全部");
            } else {
                Unit unit = CacheManager.getUnitById(pointsRule.getUnitId());
                pointsRule.setUnitName(unit.getName());
            }
        }
        return page;
    }

    @Override
    public void save(PointsRule entity) {
        this.pointsRuleDao.saveOrUpdate(entity);
    }

    @Override
    public PointsRule load(String id) {
        return null;
    }

    @Override
    public PointsRule get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<PointsRule> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<PointsRule> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(PointsRule entity) {

    }

    @Override
    public void delete(PointsRule entity) {

    }

    @Override
    public void delete(String id) {

    }

    public PointsRule findDefaultRule() {
        return this.pointsRuleDao.findUnique("from PointsRule where defaultRule=1 and status=1");
    }

    public List<PointsRule> findRulesByDate(Date startDate, Date endDate, String unitId, String id) {
        String hql = "from PointsRule where ((startDate <= ? and endDate >= ?) or (startDate <= ? and endDate >= ?) or (startDate >= ? and endDate <= ?)) and unitId = ? and defaultRule=0 and status=1 and id!=?";
        return this.pointsRuleDao.find(hql, startDate, startDate, endDate, endDate, startDate, endDate, unitId, id);
    }

    public void updateRules(List<PointsRule> pointsRules) {
        this.pointsRuleDao.doBatchInsert(pointsRules);
    }

    public List<PointsRule> findRulesByIds(String ruleIdSqlStr) {
        return this.pointsRuleDao.find("from PointsRule pointsrule where (" + ruleIdSqlStr + ") and pointsrule.status=1");
    }

    public PointsRule findRuleByUnitAndDate(String unitId, Date date){

        return this.pointsRuleDao.findUnique("from PointsRule where unitId=? and startDate <= ? and endDate >= ?", unitId, date, date);
    }

    public PointsRule findRuleById(String id) {
        return this.pointsRuleDao.get(id);
    }
}
