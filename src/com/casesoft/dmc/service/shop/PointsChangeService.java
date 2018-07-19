package com.casesoft.dmc.service.shop;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.shop.PointsChangeDao;
import com.casesoft.dmc.extend.third.request.BaseService;
import com.casesoft.dmc.model.logistics.SaleOrderBill;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.shop.PointsChange;
import com.casesoft.dmc.model.sys.PointsRule;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.service.sys.PointsRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by yushen on 2017/11/3.
 */
@Service
@Transactional
public class PointsChangeService extends BaseService<PointsChange, String> {

    @Autowired
    private PointsChangeDao pointsChangeDao;

    @Autowired
    private PointsRuleService pointsRuleService;

    @Override
    public Page<PointsChange> findPage(Page<PointsChange> page, List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public void save(PointsChange entity) {
        this.pointsChangeDao.saveOrUpdate(entity);
    }

    @Override
    public PointsChange load(String id) {
        return null;
    }

    @Override
    public PointsChange get(String propertyName, Object value) {
        return this.pointsChangeDao.findUniqueBy(propertyName, value);
    }

    @Override
    public List<PointsChange> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<PointsChange> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(PointsChange entity) {

    }

    @Override
    public void delete(PointsChange entity) {

    }

    @Override
    public void delete(String id) {

    }

    public Long getPointsById(String id) {
        return this.pointsChangeDao.findUnique("select pointsChange from PointsChange where id = ?", id);
    }

    public void savePointsChange(SaleOrderBill saleOrderBill, Unit unit, Customer customer) {
        try {
            //积分规则应该取开单日期，而不是保存的时间
            String date = "20" + saleOrderBill.getBillNo().substring(2, 8);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date billDate = sdf.parse(date);

            PointsRule rule = this.pointsRuleService.findRuleByUnitAndDate(saleOrderBill.getOrigUnitId(), billDate);
            if (CommonUtil.isBlank(rule)) {
                rule = this.pointsRuleService.findDefaultRule();
            }
            double ratio = 1D;
            if (CommonUtil.isNotBlank(rule)) {
                ratio = new BigDecimal(Double.valueOf(rule.getUnitPoints())).multiply(new BigDecimal(0.01)).doubleValue(); // 积分规则中 unitPoints 表示 每消费100元人民币克对应的积分
            }

            Long prePoints = this.getPointsById(saleOrderBill.getBillNo());
            if (CommonUtil.isBlank(prePoints)) {
                prePoints = 0L;
            }

            PointsChange pointsChange = new PointsChange();
            pointsChange.setId(saleOrderBill.getBillNo());
            pointsChange.setCustomerId(saleOrderBill.getDestUnitId());
            pointsChange.setRecordDate(new Date());
            pointsChange.setTransactionVal(saleOrderBill.getActPrice());
            pointsChange.setPointsChange((long) Math.floor(pointsChange.getTransactionVal() * ratio));
            if (CommonUtil.isBlank(rule)) {
                pointsChange.setPointsRuleId("");
            } else {
                pointsChange.setPointsRuleId(rule.getId());
            }
            pointsChange.setStatus(0);
            pointsChange.setOwnerId(saleOrderBill.getOwnerId());
            this.pointsChangeDao.saveOrUpdate(pointsChange);

            //保存客户积分
            if (CommonUtil.isBlank(unit)) {
                Double customerVipPoints = customer.getVippoints();
                if (CommonUtil.isBlank(customerVipPoints)) {
                    customerVipPoints = 0D;
                }
                customer.setVippoints(customerVipPoints - prePoints + pointsChange.getPointsChange());
                this.pointsChangeDao.saveOrUpdateX(customer);
            } else {
                Double unitVipPoints = unit.getVippoints();
                if (CommonUtil.isBlank(unitVipPoints)) {
                    unitVipPoints = 0D;
                }
                unit.setVippoints(unitVipPoints - prePoints + pointsChange.getPointsChange());
                this.pointsChangeDao.saveOrUpdateX(unit);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void cancelPointsChange(SaleOrderBill saleOrderBill, Unit unit, Customer customer) {

        Long prePoints = 0L;
        PointsChange pointsChange = this.get("id", saleOrderBill.getBillNo());
        if(CommonUtil.isNotBlank(pointsChange)){
            prePoints = pointsChange.getPointsChange();
            pointsChange.setStatus(-1);
            this.pointsChangeDao.saveOrUpdate(pointsChange);
        }
        if (CommonUtil.isBlank(unit)) {
            Double customerVipPoints = customer.getVippoints();
            if (CommonUtil.isBlank(customerVipPoints)) {
                customerVipPoints = 0D;
            }
            customer.setVippoints(customerVipPoints - prePoints);
            this.pointsChangeDao.saveOrUpdateX(customer);
        } else {
            Double unitVipPoints = unit.getVippoints();
            if (CommonUtil.isBlank(unitVipPoints)) {
                unitVipPoints = 0D;
            }
            unit.setVippoints(unitVipPoints - prePoints);
            this.pointsChangeDao.saveOrUpdateX(unit);
        }

    }
}
