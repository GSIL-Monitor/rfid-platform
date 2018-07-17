package com.casesoft.dmc.service.shop;

import com.casesoft.dmc.controller.task.TaskUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.shop.PointsChangeDao;
import com.casesoft.dmc.extend.third.request.BaseService;
import com.casesoft.dmc.model.logistics.*;
import com.casesoft.dmc.model.shop.PointsChange;
import com.casesoft.dmc.model.sys.PointsRule;
import com.casesoft.dmc.service.sys.PointsRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    public List<PointsChange> findLatestByOrderIdAndUnitId(String orderId, String unitId){
        String hql = "from PointsChange where orderId = ? and customerId = ? order by recordTime desc";
        return this.pointsChangeDao.find(hql, orderId, unitId);
    }

    //销售单积分变动
    public Long savePointsChange(SaleOrderBill saleOrderBill, String unitId, Long prePoints) {

        PointsRule rule = this.pointsRuleService.findRuleByUnitAndDate(saleOrderBill.getOrigUnitId(), saleOrderBill.getBillDate());
        if (CommonUtil.isBlank(rule)) {
            rule = this.pointsRuleService.findDefaultRule();
        }
        double ratio = 1D;
        if (CommonUtil.isNotBlank(rule)) {
            ratio = new BigDecimal(Double.valueOf(rule.getUnitPoints())).multiply(new BigDecimal(0.01)).doubleValue(); // 积分规则中 unitPoints 表示 每消费100元人民币克对应的积分
        }

        PointsChange pointsChange = new PointsChange();
        pointsChange.setId(new GuidCreator().toString());
        pointsChange.setOrderId(saleOrderBill.getId());
        pointsChange.setCustomerId(unitId);
        pointsChange.setRecordDate(new Date());
        pointsChange.setRecordTime((new Date()).getTime());
        pointsChange.setTransactionVal(saleOrderBill.getActPrice());
        pointsChange.setPointsChange((long) Math.floor(pointsChange.getTransactionVal() * ratio));
        if (CommonUtil.isBlank(rule)) {
            pointsChange.setPointsRuleId("");
        } else {
            pointsChange.setPointsRuleId(rule.getId());
        }
        pointsChange.setPrePoints(prePoints);
        pointsChange.setAfterPoints(prePoints + pointsChange.getPointsChange());
        pointsChange.setStatus(Constant.ChangeRecordStatus.SaleOrder);
        pointsChange.setRatio(ratio);
        this.save(pointsChange);
        return pointsChange.getPointsChange();
    }

    //销售单 改变客户或者交易金额变动时，客户积分回退
    public Long savePointsBackoff(String billId, String unitId, Long prePoints, Integer status){

        //兼容之前已开订单，没有改动记录的情况
        List<PointsChange> preChangeList = this.findLatestByOrderIdAndUnitId(billId, unitId);

        if(CommonUtil.isNotBlank(preChangeList)){
            //上一次变动记录
            PointsChange latestChange = preChangeList.get(0);

            //保存变动
            PointsChange pointsChange = new PointsChange();
            pointsChange.setId(new GuidCreator().toString());
            pointsChange.setOrderId(billId);
            pointsChange.setCustomerId(unitId);
            pointsChange.setRecordDate(new Date());
            pointsChange.setRecordTime((new Date()).getTime());
            pointsChange.setTransactionVal(0 - latestChange.getTransactionVal());
            pointsChange.setPointsChange(0 - latestChange.getPointsChange());
            pointsChange.setPointsRuleId(latestChange.getPointsRuleId());
            pointsChange.setPrePoints(prePoints);
            pointsChange.setAfterPoints(prePoints + pointsChange.getPointsChange());
            pointsChange.setStatus(status);
            pointsChange.setRatio(latestChange.getRatio());
            this.save(pointsChange);
            return pointsChange.getPointsChange();
        }
        return 0L;
    }

    public void cancelPointsChange(String billNo) {

        PointsChange pointsChange = this.get("id", billNo);
        if(CommonUtil.isNotBlank(pointsChange)){
            pointsChange.setStatus(-1);
            this.pointsChangeDao.saveOrUpdate(pointsChange);
        }
    }

    //销售退货单 积分回退
    public Long savePointsFallback(SaleOrderReturnBill bill, List<SaleOrderReturnBillDtl> details, Long prePoints) {
        //根据每个code追溯到最近的销售单，扣除对应销售单产生的积分
        Long totPoints = 0L;
        for (SaleOrderReturnBillDtl saleOrderReturnBillDtl : details) {
            String uniqueCodes = saleOrderReturnBillDtl.getUniqueCodes();
            if (CommonUtil.isNotBlank(uniqueCodes)) {
                String[] codes = uniqueCodes.split(",");
                for (String code : codes) {
                    List<BillRecord> BillRecords = this.pointsChangeDao.find("from BillRecord where code = ? and billNo like 'SO%' order by billNo desc", code);
                    List<PointsChange> pointsChanges = this.pointsChangeDao.find("from PointsChange where orderId = ? and orderId like 'SO%' and status = 1 order by recordTime desc", BillRecords.get(0).getBillNo());
                    if (CommonUtil.isNotBlank(pointsChanges)) {
                        Double actPrice = saleOrderReturnBillDtl.getActPrice();
                        Long points = (long) Math.floor(actPrice * pointsChanges.get(0).getRatio());
                        totPoints += points;
                    }
                }
            }
        }
        PointsChange pointsChange = new PointsChange();
        pointsChange.setId(new GuidCreator().toString());
        pointsChange.setOrderId(bill.getId());
        pointsChange.setCustomerId(bill.getOrigUnitId());
        pointsChange.setRecordDate(new Date());
        pointsChange.setRecordTime((new Date()).getTime());
        pointsChange.setTransactionVal(0 - bill.getActPrice());
        pointsChange.setPointsChange(0 - totPoints);
        pointsChange.setPrePoints(prePoints);
        pointsChange.setAfterPoints(prePoints + pointsChange.getPointsChange());
        pointsChange.setPointsRuleId("saleOrderReturn");
        pointsChange.setStatus(Constant.ChangeRecordStatus.SaleReturnOrder);
        pointsChange.setRatio(-1D);
        this.pointsChangeDao.saveOrUpdate(pointsChange);
        return pointsChange.getPointsChange();
    }

    //销售单直接关联生成退货单时，保存积分变动
    public Long saleOrder2ReturnPoints(SaleOrderReturnBill bill, Long prePoints) {
        //兼容之前已开订单，没有改动记录的情况
        List<PointsChange> preChangeList = this.findLatestByOrderIdAndUnitId(bill.getSrcBillNo(), bill.getOrigUnitId());
        if(CommonUtil.isNotBlank(preChangeList)){
            PointsChange latestChange = preChangeList.get(0);
            Double ratio = latestChange.getRatio();
            if(CommonUtil.isBlank(ratio)){
                ratio = 1D;
            }
            PointsChange pointsChange = new PointsChange();
            pointsChange.setId(new GuidCreator().toString());
            pointsChange.setOrderId(bill.getId());
            pointsChange.setCustomerId(bill.getOrigUnitId());
            pointsChange.setRecordDate(new Date());
            pointsChange.setRecordTime((new Date()).getTime());
            pointsChange.setTransactionVal(bill.getActPrice());
            pointsChange.setPointsChange((long) Math.floor(pointsChange.getTransactionVal() * ratio));
            pointsChange.setPrePoints(prePoints);
            pointsChange.setAfterPoints(prePoints + pointsChange.getPointsChange());
            if (CommonUtil.isBlank(latestChange)) {
                pointsChange.setPointsRuleId("");
            } else {
                pointsChange.setPointsRuleId(latestChange.getPointsRuleId());
            }
            pointsChange.setStatus(Constant.ChangeRecordStatus.SaleOrder2ReturnOrder);
            pointsChange.setRatio(ratio);
            this.pointsChangeDao.saveOrUpdate(pointsChange);
            return pointsChange.getPointsChange();
        }
        return 0L;
    }

    public Long cm2ReturnOrderPointsFallback(SaleOrderReturnBill bill, List<SaleOrderReturnBillDtl> details, Long prePoints) {
        Long totPoints = 0L;
        for(SaleOrderReturnBillDtl dtl : details){
            //寄存数量可能会大于退款数量，循环所有寄存商品的唯一码，查唯一码对应当前客户最新的销售单，查销售单当时的积分记录和比例。累加 积分比例×实际价格 为 所有寄存商品总积分
            //退单积分 = (所有寄存商品总积分 × 退款商品数量)/寄存商品数量
            List<BillRecord> cmBillRecords = this.pointsChangeDao.find("from BillRecord where billNo = ? and sku = ?", bill.getSrcBillNo(), dtl.getSku());
            if(CommonUtil.isNotBlank(cmBillRecords)){

                Long totCMPoints = 0L; //所有寄存商品的总积分
                for(BillRecord record : cmBillRecords){
                    List<BillRecord> soBillRecords = this.pointsChangeDao.find("from BillRecord where code = ? and billNo like 'SO%' order by billNo desc", record.getCode());
                    List<String> saleOrderIdStrList = new ArrayList<>();
                    for (BillRecord r : soBillRecords) {
                        saleOrderIdStrList.add(r.getBillNo());
                    }
                    String saleOrderIdStr = TaskUtil.getSqlStrByList(saleOrderIdStrList, PointsChange.class, "orderId");
                    String hql = "from PointsChange pointschange where (" + saleOrderIdStr + ") and pointschange.customerId = ? and pointschange.status = 1 order by recordTime desc";
                    List<PointsChange> pointsChanges = this.pointsChangeDao.find(hql, bill.getOrigUnitId());
                    if(CommonUtil.isNotBlank(pointsChanges)){
                        Double actPrice = dtl.getActPrice();
                        Long points = (long) Math.floor(actPrice * pointsChanges.get(0).getRatio());
                        totCMPoints += points;
                    }
                }
                Long averagePoints = (totCMPoints * dtl.getQty()) / cmBillRecords.size();
                totPoints += averagePoints;
            }
        }

        PointsChange pointsChange = new PointsChange();
        pointsChange.setId(new GuidCreator().toString());
        pointsChange.setOrderId(bill.getId());
        pointsChange.setCustomerId(bill.getOrigUnitId());
        pointsChange.setRecordDate(new Date());
        pointsChange.setRecordTime((new Date()).getTime());
        pointsChange.setTransactionVal(0 - bill.getActPrice());
        pointsChange.setPointsChange(0 - totPoints);
        pointsChange.setPrePoints(prePoints);
        pointsChange.setAfterPoints(prePoints + pointsChange.getPointsChange());
        pointsChange.setPointsRuleId("saleOrderReturn");
        pointsChange.setStatus(Constant.ChangeRecordStatus.CMOrder2ReturnOrder);
        pointsChange.setRatio(-1D);
        this.pointsChangeDao.saveOrUpdate(pointsChange);
        return pointsChange.getPointsChange();
    }
}
