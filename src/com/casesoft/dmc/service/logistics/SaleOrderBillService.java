package com.casesoft.dmc.service.logistics;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.logistics.BillConvertUtil;
import com.casesoft.dmc.controller.logistics.MonthAccountUtil;
import com.casesoft.dmc.controller.task.TaskUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.dao.logistics.MonthAccountStatementDao;
import com.casesoft.dmc.dao.logistics.SaleOrderBillDao;
import com.casesoft.dmc.dao.logistics.SaleOrderReturnBillDao;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.logistics.*;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.search.SaleorderCountView;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.shop.PointsChange;
import com.casesoft.dmc.model.stock.CodeFirstTime;
import com.casesoft.dmc.model.sys.PointsRule;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.model.tag.Epc;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.Record;
import com.casesoft.dmc.service.cfg.PropertyService;
import com.casesoft.dmc.service.shop.PointsChangeService;
import com.casesoft.dmc.service.stock.EpcStockService;
import com.casesoft.dmc.service.task.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by koudepei on 2017/6/18.
 */
@Service
@Transactional
public class SaleOrderBillService implements IBaseService<SaleOrderBill, String> {

    @Autowired
    private SaleOrderBillDao saleOrderBillDao;
    @Autowired
    private SaleOrderReturnBillDao saleOrderReturnBillDao;

    @Autowired
    private PropertyService propertyService;
    @Autowired
    private PointsChangeService pointsChangeService;

    @Autowired
    private MonthAccountStatementDao monthAccountStatementDao;


    @Override
    public Page<SaleOrderBill> findPage(Page<SaleOrderBill> page, List<PropertyFilter> filters) {
        return this.saleOrderBillDao.findPage(page, filters);
    }

    @Override
    public void save(SaleOrderBill entity) {
        this.saleOrderBillDao.saveOrUpdate(entity);
    }

    @Override
    public SaleOrderBill load(String id) {
        return this.saleOrderBillDao.load(id);
    }

    @Override
    public SaleOrderBill get(String propertyName, Object value) {
        return this.saleOrderBillDao.findUniqueBy(propertyName, value);
    }

    @Override
    public List<SaleOrderBill> find(List<PropertyFilter> filters) {
        return this.saleOrderBillDao.find(filters);
    }

    @Override
    public List<SaleOrderBill> getAll() {
        return this.saleOrderBillDao.getAll();
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(SaleOrderBill entity) {
        this.saleOrderBillDao.saveOrUpdate(entity);
    }

    //更新支付方式
    public void update(String payType,String billNo){
        this.saleOrderBillDao.batchExecute("update SaleOrderBill set payType = '" +payType+"' where billNo = ?",billNo);
    }

    //更新关联单号
    public void updateNo(String billNo,String rbillNo){
        this.saleOrderBillDao.batchExecute("update SaleOrderBill set srcBillNo = '"+rbillNo+"' where billNo = ?",billNo);
    }

    @Override
    public void delete(SaleOrderBill entity) {
        this.saleOrderBillDao.delete(entity);
    }

    @Override
    public void delete(String id) {
        this.saleOrderBillDao.delete(id);
    }

    public List<SaleOrderBillDtl> findBillDtlByBillNo(String billNo) {
        return this.saleOrderBillDao.find("from SaleOrderBillDtl where billNo=?", new Object[]{billNo});
    }

    public String findMaxBillNo(String prefix) {
        String hql = "select max(CAST(SUBSTRING(t.billNo,9),integer))"
                + " from SaleOrderBill as t where t.billNo like ?";
        Integer code = this.saleOrderBillDao.findUnique(hql, prefix + '%');
        return code == null ? (prefix + "001") : prefix + CommonUtil.convertIntToString(code + 1, 3);
    }

    public void save(SaleOrderBill saleOrderBill, List<SaleOrderBillDtl> saleOrderBillDtlList) {
        this.saleOrderBillDao.batchExecute("delete from SaleOrderBillDtl where billNo=?", saleOrderBill.getBillNo());
        this.saleOrderBillDao.batchExecute("delete from BillRecord where billNo=?", saleOrderBill.getBillNo());
        //客户月结表数据Id
        String curYearMonth = CommonUtil.getDateString(new Date(),"yyyy-MM");
        //数据库中查询是否已经存在该订单
        Logger logger = LoggerFactory.getLogger(SaleOrderBill.class);
        Double diffPrice = saleOrderBill.getActPrice() - saleOrderBill.getPayPrice();
        String preDestUnitId = this.saleOrderBillDao.findUnique("select destUnitId from SaleOrderBill as s where s.billNo = ?", saleOrderBill.getBillNo());
        //是否更新客户欠款月结表
        Boolean isUpdateMonthAccount = false;
        isUpdateMonthAccount = !curYearMonth.equals(CommonUtil.getDateString(saleOrderBill.getBillDate(), "yyyy-MM"));


        Unit unit = this.saleOrderBillDao.findUnique("from Unit where id = ? and status=1", new Object[]{saleOrderBill.getDestUnitId()});
        Customer customer = this.saleOrderBillDao.findUnique("from Customer where id = ? and status=1", new Object[]{saleOrderBill.getDestUnitId()});
        Double preDiffPrice = this.saleOrderBillDao.findUnique("select s.actPrice-s.payPrice from SaleOrderBill as s where s.billNo = ?", saleOrderBill.getBillNo());
        //更新客户月结表数据
        if(isUpdateMonthAccount){
            Map<String,MonthAccountStatement> preMonthAccountMap = new HashMap<>();
            List<MonthAccountStatement> preMonthAcountList = this.monthAccountStatementDao.find("from MonthAccountStatement where unitId = ? order by month  asc",preDestUnitId);
            for(MonthAccountStatement m : preMonthAcountList){
                preMonthAccountMap.put(m.getId(),m);
            }
            List<MonthAccountStatement> updateMonthAccountList = new ArrayList<>();
            MonthAccountUtil.updateMonthAcoutData(saleOrderBill.getBillDate(),preMonthAccountMap,updateMonthAccountList,preDestUnitId,preDiffPrice,false);
            this.monthAccountStatementDao.doBatchInsert(updateMonthAccountList);

        }
        if (CommonUtil.isNotBlank(preDestUnitId)) {
            Unit preUnit = CacheManager.getUnitByCode(preDestUnitId);
            if (CommonUtil.isNotBlank(preUnit)) {
                this.saleOrderBillDao.batchExecute("update Unit set owingValue = owingValue - ? where id=?", preDiffPrice, preDestUnitId);
                logger.error("Unit原来客户"+preUnit.getName()+"Unit原来客户编号"+preUnit.getId()+"原单本单差额"+preDiffPrice);
            } else {
                this.saleOrderBillDao.batchExecute("update Customer set owingValue = owingValue - ? where id=?", preDiffPrice, preDestUnitId);
                logger.error("Customer原来客户编号"+preDestUnitId+"原单本单差额"+preDiffPrice);
            }
        }
        if (CommonUtil.isBlank(unit)) {
            if(CommonUtil.isBlank(customer.getOwingValue())){
                customer.setOwingValue(0.0);
            }
            logger.error("销售单"+saleOrderBill.getBillNo()+"本单差额"+diffPrice+"unit客户"+customer.getName()+"unit客户编号"+customer.getId()+"欠款金额"+(customer.getOwingValue()+diffPrice)+"原欠款金额"+(customer.getOwingValue()));
            if(CommonUtil.isNotBlank(preDestUnitId) && preDestUnitId.equals(customer.getCode())){
                this.saleOrderBillDao.batchExecute("update Customer set owingValue = ? where id=?", customer.getOwingValue() - preDiffPrice+diffPrice, preDestUnitId);
            }else{
                customer.setOwingValue(customer.getOwingValue() + diffPrice);
            }
            this.saleOrderBillDao.saveOrUpdateX(customer);
        } else {
            if(CommonUtil.isBlank(unit.getOwingValue())){
                unit.setOwingValue(0.0);
            }
            logger.error("销售单"+saleOrderBill.getBillNo()+"本单差额"+diffPrice+"unit客户"+unit.getName()+"unit客户编号"+unit.getId()+"欠款金额"+(unit.getOwingValue()+diffPrice)+"原欠款金额"+(unit.getOwingValue()));
            if(CommonUtil.isNotBlank(preDestUnitId) && preDestUnitId.equals(unit.getCode())){
                this.saleOrderBillDao.batchExecute("update Unit set owingValue =  ? where id=?", unit.getOwingValue() -preDiffPrice + diffPrice, preDestUnitId);
            }else {
                unit.setOwingValue(unit.getOwingValue() + diffPrice);
            }
            this.saleOrderBillDao.saveOrUpdateX(unit);
        }
        if(isUpdateMonthAccount){
            Map<String,MonthAccountStatement> monthAccountMap = new HashMap<>();
            List<MonthAccountStatement> monthAcountList = this.monthAccountStatementDao.find("from MonthAccountStatement where unitId = ? order by month  asc",saleOrderBill.getDestUnitId());
            for(MonthAccountStatement m : monthAcountList){
                monthAccountMap.put(m.getId(),m);
            }
            List<MonthAccountStatement> updateMonthAccountList = new ArrayList<>();
            MonthAccountUtil.updateMonthAcoutData(saleOrderBill.getBillDate(),monthAccountMap,updateMonthAccountList,saleOrderBill.getDestUnitId(),diffPrice,true);
            this.monthAccountStatementDao.doBatchInsert(updateMonthAccountList);
        }
        this.saleOrderBillDao.saveOrUpdate(saleOrderBill);
        this.saleOrderBillDao.doBatchInsert(saleOrderBillDtlList);
        if (CommonUtil.isNotBlank(saleOrderBill.getBillRecordList())) {
            this.saleOrderBillDao.doBatchInsert(saleOrderBill.getBillRecordList());
        }
        //保存积分变动记录
        String vipInfo;
        if (CommonUtil.isBlank(unit)) {
            vipInfo = customer.getVipMessage();
        } else {
            vipInfo = unit.getVipMessage();
        }

        if (CommonUtil.isNotBlank(vipInfo)) {
            this.pointsChangeService.savePointsChange(saleOrderBill, unit, customer);
        }
    }
    /**
     * 客户端加盟商收货
     * */
    public void saveBusinessIn(SaleOrderBill saleOrderInBill, List<SaleOrderBillDtl> saleOrderInBillDtlList, Business bus) {
        this.saleOrderBillDao.batchExecute("delete from SaleOrderBillDtl where billNo=?", saleOrderInBill.getBillNo());
        this.saleOrderBillDao.saveOrUpdate(saleOrderInBill);
        this.saleOrderBillDao.doBatchInsert(saleOrderInBillDtlList);
        if(bus.getType().equals(Constant.TaskType.Inbound)) {
            List<Record> recordList = bus.getRecordList();
            ArrayList<CodeFirstTime> list = new ArrayList<CodeFirstTime>();
            for (int i = 0; i < recordList.size(); i++) {
                CodeFirstTime codeFirstTime = this.saleOrderBillDao.findUnique("from CodeFirstTime where code=? and warehouseId=?", new Object[]{recordList.get(i).getCode(), saleOrderInBill.getDestId()});
                if (CommonUtil.isBlank(codeFirstTime)) {
                    CodeFirstTime newcodeFirstTime = new CodeFirstTime();
                    newcodeFirstTime.setId(recordList.get(i).getCode() + "-" + saleOrderInBill.getDestId());
                    newcodeFirstTime.setCode(recordList.get(i).getCode());
                    newcodeFirstTime.setWarehouseId(saleOrderInBill.getDestId());
                    newcodeFirstTime.setFirstTime(new Date());
                    Unit unitByCode = CacheManager.getUnitByCode(saleOrderInBill.getDestUnitId());
                    if(CommonUtil.isNotBlank(unitByCode)&&CommonUtil.isNotBlank(unitByCode.getGroupId())&&unitByCode.getGroupId().equals("JMS")){
                        newcodeFirstTime.setWarehousePrice(recordList.get(i).getPrice());
                    }else{
                        Style styleById = CacheManager.getStyleById(recordList.get(i).getStyleId());
                        newcodeFirstTime.setWarehousePrice(styleById.getPreCast());
                    }
                    list.add(newcodeFirstTime);
                }
            }
            if (list.size() != 0) {
                this.saleOrderBillDao.doBatchInsert(list);
            }
        }
    }

    /**
     * 微信小程序保存
     * @param saleOrderBill
     * @param saleOrderBillDtlList
     */
    public void saveweChat(SaleOrderBill saleOrderBill, List<SaleOrderBillDtl> saleOrderBillDtlList, Business business, List<Epc> epcList) {
        this.saleOrderBillDao.batchExecute("delete from SaleOrderBillDtl where billNo=?", saleOrderBill.getBillNo());
        this.saleOrderBillDao.batchExecute("delete from BillRecord where billNo=?", saleOrderBill.getBillNo());
        Logger logger = LoggerFactory.getLogger(SaleOrderBill.class);

        //数据库中查询是否已经存在该订单
        Double diffPrice = saleOrderBill.getActPrice() - saleOrderBill.getPayPrice();
        String preDestUnitId = this.saleOrderBillDao.findUnique("select destUnitId from SaleOrderBill as s where s.billNo = ?", saleOrderBill.getBillNo());
        Unit unit = this.saleOrderBillDao.findUnique("from Unit where id = ?", new Object[]{saleOrderBill.getDestUnitId()});
        Customer customer = this.saleOrderBillDao.findUnique("from Customer where id = ?", new Object[]{saleOrderBill.getDestUnitId()});
        if (CommonUtil.isNotBlank(preDestUnitId)) {
            Double preDiffPrice = this.saleOrderBillDao.findUnique("select s.actPrice-s.payPrice from SaleOrderBill as s where s.billNo = ?", saleOrderBill.getBillNo());
            Unit preUnit = CacheManager.getUnitByCode(preDestUnitId);
            if (CommonUtil.isNotBlank(preUnit)) {
                this.saleOrderBillDao.batchExecute("update Unit set owingValue = owingValue - ? where id=?", preDiffPrice, preDestUnitId);
                logger.error("Unit原来客户"+preUnit.getName()+"Unit原来客户编号"+preUnit.getId()+"原单本单差额"+preDiffPrice);

            } else {
                this.saleOrderBillDao.batchExecute("update Customer set owingValue = owingValue - ? where id=?", preDiffPrice, preDestUnitId);
                logger.error("Customer原来客户编号"+preDestUnitId+"原单本单差额"+preDiffPrice);
            }
        }
        if (CommonUtil.isBlank(unit)) {
            if(CommonUtil.isBlank(customer.getOwingValue())){
                customer.setOwingValue(0.0);
            }
            logger.error("销售单"+saleOrderBill.getBillNo()+"本单差额"+diffPrice+"unit客户"+customer.getName()+"unit客户编号"+customer.getId()+"欠款金额"+(customer.getOwingValue()+diffPrice)+"原欠款金额"+(customer.getOwingValue()));
            customer.setOwingValue(customer.getOwingValue() + diffPrice);
            this.saleOrderBillDao.saveOrUpdateX(customer);
        } else {
            if(CommonUtil.isBlank(unit.getOwingValue())){
                unit.setOwingValue(0.0);
            }
            logger.error("销售单"+saleOrderBill.getBillNo()+"本单差额"+diffPrice+"unit客户"+unit.getName()+"unit客户编号"+unit.getId()+"欠款金额"+(unit.getOwingValue()+diffPrice)+"原欠款金额"+(unit.getOwingValue()));
            unit.setOwingValue(unit.getOwingValue() + diffPrice);
            this.saleOrderBillDao.saveOrUpdateX(unit);
        }
        this.saleOrderBillDao.saveOrUpdate(saleOrderBill);
        this.saleOrderBillDao.doBatchInsert(saleOrderBillDtlList);
        if (CommonUtil.isNotBlank(saleOrderBill.getBillRecordList())) {
            this.saleOrderBillDao.doBatchInsert(saleOrderBill.getBillRecordList());
        }
        //保存积分变动记录
        String vipInfo;
        if (CommonUtil.isBlank(unit)) {
            vipInfo = customer.getVipMessage();
        } else {
            vipInfo = unit.getVipMessage();
        }

        if (CommonUtil.isNotBlank(vipInfo)) {
            this.pointsChangeService.savePointsChange(saleOrderBill, unit, customer);
        }
        //出库
        List<Style> styleList = new ArrayList<>();
        for (SaleOrderBillDtl dtl : saleOrderBillDtlList) {
            if (dtl.getStatus() == BillConstant.BillDtlStatus.InStore) {
                Style s = CacheManager.getStyleById(dtl.getStyleId());
                s.setClass6(BillConstant.InStockType.BackOrder);
                styleList.add(s);
            }
        }
        if (CommonUtil.isNotBlank(saleOrderBill.getBillRecordList())) {
            this.saleOrderBillDao.doBatchInsert(saleOrderBill.getBillRecordList());
        }
        this.taskService.save(business);
        if (styleList.size() > 0) {
            this.saleOrderBillDao.doBatchInsert(styleList);
        }
        List<Record> recordList = business.getRecordList();
        List<String> codeStrList = new ArrayList<String>();
        for (Record record : recordList) {
            if (CommonUtil.isNotBlank(record.getExtField()) && record.getExtField().equals(BillConstant.InStockType.Consignment)) {
                codeStrList.add(record.getCode());
            }
        }
    }

    public void saveRetrun(SaleOrderBill saleOrderBill, List<SaleOrderBillDtl> saleOrderBillDtlList, String billNos, String skusql, User curUser) {
        //删除
        String hql = "delete from SaleOrderBillDtl saleorderbilldtl where saleorderbilldtl.billNo=? and " + skusql;
        this.saleOrderBillDao.batchExecute(hql, billNos);
        saleOrderBill.setId(saleOrderBill.getBillNo());
        saleOrderBill.setBillNo(saleOrderBill.getBillNo());
        BillConvertUtil.covertToSaleOrderBillOnRetrun(saleOrderBill, saleOrderBillDtlList, curUser);
        //保存退货单
        String prefix = BillConstant.BillPrefix.SaleOrderReturn + CommonUtil.getDateString(new Date(), "yyMMddHHmmssSSS");
        //String billNo = this.findMaxBillNO(prefix);
        List<SaleOrderReturnBillDtl> saleOrderReturnBillDtls = new ArrayList<SaleOrderReturnBillDtl>();
        SaleOrderReturnBill saleOrderReturnBill = new SaleOrderReturnBill();
        List<PropertyKey> pkList = this.propertyService.getPropertyKeyByType("RX");
        String code = null;
        for (int i = 0; i < pkList.size(); i++) {
            if (pkList.get(i).getName().equals("订单退货")) {
                code = pkList.get(i).getCode();
            }
        }
        BillConvertUtil.saveReturnSaleOrder(saleOrderBill, saleOrderBillDtlList, saleOrderReturnBillDtls, saleOrderReturnBill, prefix, code);

        this.saveReturnBatch(saleOrderReturnBill, saleOrderReturnBillDtls);

        //数据库中查询是否已经存在该订单

        Double preDiffPrice = this.saleOrderBillDao.findUnique("select s.actPrice-s.payPrice from SaleOrderBill as s where s.billNo = ?", saleOrderBill.getBillNo());
        if (CommonUtil.isBlank(preDiffPrice)) {
            preDiffPrice = 0D;
        }
        //根据是否存在订单算出应付实付差额
        Double diffPrice = saleOrderBill.getActPrice() - saleOrderBill.getPayPrice();
        Unit unit = this.saleOrderBillDao.findUnique("from Unit where id = ?", new Object[]{saleOrderBill.getDestUnitId()});
        if (CommonUtil.isBlank(unit)) {
            Customer customer = this.saleOrderBillDao.findUnique("from Customer where id = ?", new Object[]{saleOrderBill.getDestUnitId()});
            customer.setOwingValue(customer.getOwingValue() - preDiffPrice + diffPrice);
            this.saleOrderBillDao.saveOrUpdateX(customer);
            if (saleOrderBill.getTotOutQty().intValue() + saleOrderBill.getTotRetrunQty() == saleOrderBill.getTotQty().intValue()) {
                saleOrderBill.setOutStatus(BillConstant.BillInOutStatus.OutStore);
                if (saleOrderBill.getCustomerTypeId().equals(BillConstant.customerType.Customer)) {
                    saleOrderBill.setStatus(BillConstant.BillStatus.End);
                }
            }
        } else {
            unit.setOwingValue(unit.getOwingValue() - preDiffPrice + diffPrice);
            this.saleOrderBillDao.saveOrUpdateX(unit);
            if (saleOrderBill.getTotOutQty().intValue() + saleOrderBill.getTotRetrunQty() == saleOrderBill.getTotQty().intValue()) {
                saleOrderBill.setOutStatus(BillConstant.BillInOutStatus.OutStore);
            }
            if (saleOrderBill.getTotInQty().intValue() + saleOrderBill.getTotRetrunQty() == saleOrderBill.getTotQty().intValue()) {
                saleOrderBill.setOutStatus(BillConstant.BillInOutStatus.InStore);
                saleOrderBill.setStatus(BillConstant.BillStatus.End);
            }
        }
        this.saleOrderBillDao.saveOrUpdate(saleOrderBill);
        this.saleOrderBillDao.doBatchInsert(saleOrderBillDtlList);
        //退算积分
        //查询库户
        Customer customer = this.saleOrderReturnBillDao.findUnique("from Customer where id = ?", new Object[]{saleOrderBill.getDestUnitId()});
        Unit units = this.saleOrderReturnBillDao.findUnique("from Unit where id = ?", new Object[]{saleOrderBill.getDestUnitId()});
        if(CommonUtil.isNotBlank(customer)){
            if(CommonUtil.isNotBlank(customer.getVipMessage())){
                //根据code查询
                ArrayList<RecordReturnScore> list=new  ArrayList<RecordReturnScore>();
                for(SaleOrderBillDtl saleOrderBillDtl:saleOrderBillDtlList) {
                    String uniqueCodes = saleOrderBillDtl.getUniqueCodes();
                    if (CommonUtil.isNotBlank(uniqueCodes)) {
                        String[] split = uniqueCodes.split(",");
                        for (int i = 0; i < split.length; i++) {
                            //List<BillRecord> BillRecords = this.saleOrderReturnBillDao.find("from BillRecord where code = ? order by billNo desc", new Object[]{split[i]});
                            PointsChange pointsChange = this.saleOrderReturnBillDao.findUnique("from PointsChange where id = ?", new Object[]{saleOrderBill.getBillNo()});
                            if(CommonUtil.isNotBlank(pointsChange)){
                                PointsRule pointsRule = this.saleOrderReturnBillDao.findUnique("from PointsRule where id = ?", new Object[]{pointsChange.getPointsRuleId()});
                                if(CommonUtil.isNotBlank(pointsRule)){
                                    Double actPrice = saleOrderBillDtl.getActPrice();
                                    double points = pointsRule.getUnitPoints() / 100 * actPrice;
                                    //Customer customer = this.saleOrderReturnBillDao.findUnique("from Customer where id = ?", new Object[]{bill.getOrigUnitId()});
                                    Double v = customer.getVippoints();
                                    if (CommonUtil.isBlank(v)) {
                                        v = 0D;
                                    }
                                    customer.setVippoints(v - points);
                                    this.saleOrderReturnBillDao.saveOrUpdateX(customer);
                                    RecordReturnScore recordReturnScore = new RecordReturnScore();
                                    recordReturnScore.setId(saleOrderBillDtl.getId() + "-" + split[i]);
                                    recordReturnScore.setBillno(saleOrderBill.getBillNo());
                                    recordReturnScore.setCode(split[i]);
                                    recordReturnScore.setRetrunDetailid(saleOrderBillDtl.getId());
                                    list.add(recordReturnScore);
                                }

                            }

                        }
                    }
                    if(list.size()!=0){
                        this.saleOrderReturnBillDao.doBatchInsert(list);
                    }

                }
            }
        }
        if(CommonUtil.isNotBlank(units)){
            if(CommonUtil.isNotBlank(units)){
                if(CommonUtil.isNotBlank(units.getVipMessage())){
                    //根据code查询
                    ArrayList<RecordReturnScore> list=new  ArrayList<RecordReturnScore>();
                    for(SaleOrderBillDtl saleOrderBillDtl:saleOrderBillDtlList) {
                        String uniqueCodes = saleOrderBillDtl.getUniqueCodes();
                        if (CommonUtil.isNotBlank(uniqueCodes)) {
                            String[] split = uniqueCodes.split(",");
                            for (int i = 0; i < split.length; i++) {
                                //List<BillRecord> BillRecords = this.saleOrderReturnBillDao.find("from BillRecord where code = ? order by billNo desc", new Object[]{split[i]});
                                PointsChange pointsChange = this.saleOrderReturnBillDao.findUnique("from PointsChange where id = ?", new Object[]{saleOrderBill.getBillNo()});
                                if(CommonUtil.isNotBlank(pointsChange)){
                                    PointsRule pointsRule = this.saleOrderReturnBillDao.findUnique("from PointsRule where id = ?", new Object[]{pointsChange.getPointsRuleId()});
                                    if(CommonUtil.isNotBlank(pointsRule)){
                                        Double actPrice = saleOrderBillDtl.getActPrice();
                                        double points = pointsRule.getUnitPoints() / 100 * actPrice;
                                        //Customer customer = this.saleOrderReturnBillDao.findUnique("from Customer where id = ?", new Object[]{bill.getOrigUnitId()});
                                        Double v = units.getVippoints();
                                        if (CommonUtil.isBlank(v)) {
                                            v = 0D;
                                        }
                                        units.setVippoints(v - points);
                                        this.saleOrderReturnBillDao.saveOrUpdateX(units);
                                        RecordReturnScore recordReturnScore = new RecordReturnScore();
                                        recordReturnScore.setId(saleOrderBillDtl.getId() + "-" + split[i]);
                                        recordReturnScore.setBillno(saleOrderBill.getBillNo());
                                        recordReturnScore.setCode(split[i]);
                                        recordReturnScore.setRetrunDetailid(saleOrderBillDtl.getId());
                                        list.add(recordReturnScore);
                                    }

                                }

                            }
                        }
                        if(list.size()!=0){
                            this.saleOrderReturnBillDao.doBatchInsert(list);
                        }

                    }
                }
            }
        }


    }

    public String findMaxBillNO(String prefix) {
        String hql = "select max(CAST(SUBSTRING(sorb.billNo,9),integer)) from SaleOrderReturnBill sorb where sorb.billNo like ?";
        Integer No = this.saleOrderReturnBillDao.findUnique(hql, new Object[]{prefix + "%"});
        return No == null ? (prefix + "001") : prefix + CommonUtil.convertIntToString(No + 1, 3);
    }

    /**
     * 保存销售退货单，同时更新客户欠款金额
     *
     * @param bill
     * @param details
     */
    public void saveReturnBatch(SaleOrderReturnBill bill, List<SaleOrderReturnBillDtl> details) {

        Double diffPrice = bill.getActPrice() - bill.getPayPrice();
        Unit unit = this.saleOrderReturnBillDao.findUnique("from Unit where id = ?", new Object[]{bill.getOrigUnitId()});
        if (CommonUtil.isBlank(unit)) {
            Customer customer = this.saleOrderReturnBillDao.findUnique("from Customer where id = ?", new Object[]{bill.getOrigUnitId()});
            customer.setOwingValue(customer.getOwingValue() + diffPrice);
            this.saleOrderReturnBillDao.saveOrUpdateX(customer);
        } else {
            if (CommonUtil.isBlank(unit.getOwingValue()))
                unit.setOwingValue(0.0);
            unit.setOwingValue(unit.getOwingValue() + diffPrice);
            this.saleOrderReturnBillDao.saveOrUpdateX(unit);
        }
        this.saleOrderReturnBillDao.saveOrUpdate(bill);
        this.saleOrderReturnBillDao.doBatchInsert(details);
    }

    public void deleteBillDtlByBillNo(String billNo) {
        String hql = "delete from SaleOrderBillDtl where billNo=?";
        this.saleOrderBillDao.batchExecute(hql, billNo);
    }

    /**
     * 撤销销售订单时更新客户欠款金额
     *
     * @param saleOrderBill 当前撤销的销售订单
     */
    public void cancelUpdate(SaleOrderBill saleOrderBill) {
        Logger logger = LoggerFactory.getLogger(SaleOrderBill.class);
        Double diffPrice = saleOrderBill.getActPrice() - saleOrderBill.getPayPrice();

        Unit unit = this.saleOrderBillDao.findUnique("from Unit where id = ?", new Object[]{saleOrderBill.getDestUnitId()});
        Customer customer = this.saleOrderBillDao.findUnique("from Customer where id = ?", new Object[]{saleOrderBill.getDestUnitId()});
        if (CommonUtil.isBlank(unit)) {
            customer.setOwingValue(customer.getOwingValue() - diffPrice);
            logger.error("销售单"+saleOrderBill.getBillNo()+"撤销金额"+diffPrice+"客户"+customer.getName()+"欠款金额"+(customer.getOwingValue()-diffPrice));

            this.saleOrderBillDao.saveOrUpdateX(customer);
        } else {
            unit.setOwingValue(unit.getOwingValue() - diffPrice);
            logger.error("销售单"+saleOrderBill.getBillNo()+"撤销金额"+diffPrice+"客户"+unit.getName()+"欠款金额"+(unit.getOwingValue()-diffPrice));

            this.saleOrderBillDao.saveOrUpdateX(unit);
        }
        this.saleOrderBillDao.saveOrUpdate(saleOrderBill);
        this.pointsChangeService.cancelPointsChange(saleOrderBill, unit, customer);
    }

    public Epc findProductBycode(String code) {
        return this.saleOrderBillDao.findUnique("from Epc where code=?", code);
    }


    @Autowired
    private TaskService taskService;

    public MessageBox saveBusiness(SaleOrderBill saleOrderBill, List<SaleOrderBillDtl> saleOrderBillDtlList, Business business) throws Exception {
        List<Style> styleList = new ArrayList<>();
        for (SaleOrderBillDtl dtl : saleOrderBillDtlList) {
            if (dtl.getStatus() == BillConstant.BillDtlStatus.InStore) {
                Style s = CacheManager.getStyleById(dtl.getStyleId());
                s.setClass6(BillConstant.InStockType.BackOrder);
                styleList.add(s);
            }
        }
        //检查epc
        MessageBox messageBox = this.taskService.checkEpcStock(business);
        boolean success = messageBox.getSuccess();
        if(success){
            this.saleOrderBillDao.saveOrUpdate(saleOrderBill);
            this.saleOrderBillDao.doBatchInsert(saleOrderBillDtlList);
            if (CommonUtil.isNotBlank(saleOrderBill.getBillRecordList())) {
                this.saleOrderBillDao.doBatchInsert(saleOrderBill.getBillRecordList());
            }
            this.taskService.webSave(business);
            if (styleList.size() > 0) {
                this.saleOrderBillDao.doBatchInsert(styleList);
            }
            //记录第一次入库时间
            if(business.getType().equals(Constant.TaskType.Inbound)) {
                List<Record> recordList = business.getRecordList();
                ArrayList<CodeFirstTime> list = new ArrayList<CodeFirstTime>();
                for (int i = 0; i < recordList.size(); i++) {
                    CodeFirstTime codeFirstTime = this.saleOrderBillDao.findUnique("from CodeFirstTime where code=? and warehouseId=?", new Object[]{recordList.get(i).getCode(), saleOrderBill.getDestId()});
                    if (CommonUtil.isBlank(codeFirstTime)) {
                        CodeFirstTime newcodeFirstTime = new CodeFirstTime();
                        newcodeFirstTime.setId(recordList.get(i).getCode() + "-" + saleOrderBill.getDestId());
                        newcodeFirstTime.setCode(recordList.get(i).getCode());
                        newcodeFirstTime.setWarehouseId(saleOrderBill.getDestId());
                        newcodeFirstTime.setFirstTime(new Date());
                        Unit unitByCode = CacheManager.getUnitByCode(saleOrderBill.getDestUnitId());
                        if(CommonUtil.isNotBlank(unitByCode)&&CommonUtil.isNotBlank(unitByCode.getGroupId())&&unitByCode.getGroupId().equals("JMS")){
                            newcodeFirstTime.setWarehousePrice(recordList.get(i).getPrice());
                        }else{
                            Style styleById = CacheManager.getStyleById(recordList.get(i).getStyleId());
                            newcodeFirstTime.setWarehousePrice(styleById.getPreCast());
                        }
                        list.add(newcodeFirstTime);
                    }
                }
                if (list.size() != 0) {
                    this.saleOrderBillDao.doBatchInsert(list);
                }
            }
            return messageBox;
        }else{
            return messageBox;
        }



    }

    public  MessageBox saveBusinessout(SaleOrderBill saleOrderBill, List<SaleOrderBillDtl> saleOrderBillDtlList, Business business, List<Epc> epcList) throws Exception {
        List<Style> styleList = new ArrayList<>();
        for (SaleOrderBillDtl dtl : saleOrderBillDtlList) {
            if (dtl.getStatus() == BillConstant.BillDtlStatus.InStore) {
                Style s = CacheManager.getStyleById(dtl.getStyleId());
                s.setClass6(BillConstant.InStockType.BackOrder);
                styleList.add(s);
            }
        }
        MessageBox messageBox = this.taskService.checkEpcStock(business);
        if(messageBox.getSuccess()){
            this.saleOrderBillDao.saveOrUpdate(saleOrderBill);
            this.saleOrderBillDao.doBatchInsert(saleOrderBillDtlList);
            if (CommonUtil.isNotBlank(saleOrderBill.getBillRecordList())) {
                this.saleOrderBillDao.doBatchInsert(saleOrderBill.getBillRecordList());
            }
            this.taskService.webSave(business);
            if (styleList.size() > 0) {
                this.saleOrderBillDao.doBatchInsert(styleList);
            }
            List<Record> recordList = business.getRecordList();
            List<String> codeStrList = new ArrayList<String>();
            for (Record record : recordList) {
                if (CommonUtil.isNotBlank(record.getExtField()) && record.getExtField().equals(BillConstant.InStockType.Consignment)) {
                    codeStrList.add(record.getCode());
                }
            }

            if (CommonUtil.isNotBlank(codeStrList) && recordList.size() > 0) {
                //更新寄售单数据
                String code = TaskUtil.getSqlStrByList(codeStrList, BillRecord.class, "code");
                String cmBillNo = getConsignmentBillNo(code);
                if (CommonUtil.isNotBlank(cmBillNo)) {
                    List<BillRecord> billRecordList = getBillRecod(cmBillNo, code);
                    Map<String, Integer> skuCountMap = new HashMap<>();
                    for (BillRecord billRecord : billRecordList) {
                        String billNo = billRecord.getBillNo();
                        String sku = billRecord.getSku();
                        if (billNo.contains(BillConstant.BillPrefix.Consignment)) {
                            if (skuCountMap.containsKey(billNo + "-" + sku)) {
                                Integer totQty = skuCountMap.get(billNo + "-" + sku);
                                totQty += 1;
                                skuCountMap.put(billNo + "-" + sku, totQty);
                            } else {
                                skuCountMap.put(billNo + "-" + sku, 1);
                            }
                        }
                    }
                    for (String key : skuCountMap.keySet()) {
                        this.saleOrderBillDao.batchExecute("update ConsignmentBillDtl set sale = sale + ? where billNo = ? and sku = ?", skuCountMap.get(key), key.split("-")[0], key.split("-")[1]);
                    /*this.saleOrderBillDao.batchExecute("update ConsignmentBillDtl set readysale = readysale + ? where billNo = ? and sku = ?", skuCountMap.get(key), key.split("-")[0], key.split("-")[1]);*/

                    }
                }
            }
            return  messageBox;
        }else{
            return  messageBox;
        }

    }


    public String getConsignmentBillNo(String Code) {
        return this.saleOrderBillDao.findUnique("select max(billrecord.billNo) from BillRecord  billrecord where " + Code + " and billrecord.billNo like  'CM%'");
    }

    public List<BillRecord> getBillRecods(String code) {
        return this.saleOrderBillDao.find("from BillRecord where code=?", new Object[]{code});
    }

    public List<BillRecord> getBillRecod(String billNo) {
        return this.saleOrderBillDao.find("from BillRecord where billNo=?", new Object[]{billNo});
    }

    private List<BillRecord> getBillRecod(String billNo, String code) {
        return this.saleOrderBillDao.find("from BillRecord  billrecord where " + code + " and billrecord.billNo=?", new Object[]{billNo});
    }

    public void deleteBillDtlByBillNoAndSku(String billNo, String skusql) {
        String hql = "delete from SaleOrderBillDtl saleorderbilldtl where saleorderbilldtl.billNo=? and " + skusql;
        this.saleOrderBillDao.batchExecute(hql, billNo);
    }

    public List<SaleOrderBillDtl> findSaleOrderBillDtlListBysku(String sku) {
        List<SaleOrderBillDtl> list = this.saleOrderBillDao.find("select new SaleOrderBillDtl(t.billId,s.billDate,s.busnissName,t.styleId,t.colorId,t.sizeId,t.sku) from SaleOrderBillDtl t , SaleOrderBill s where  t.billId=s.id and s.status<>-1 and  s.status<>2 and t.status<>0 and t.sku = ? ", new Object[]{sku});
        return list;
    }

    public Long findsaleOrderCount(String hql) {
        return this.saleOrderBillDao.findUnique(hql);
    }

    public Double findsaleOrderCountnum(String hql) {
        return this.saleOrderBillDao.findUnique(hql);
    }

    public Object findsaleOrderOrsaleRetrunMessage(String hql){
        Object unique = this.saleOrderBillDao.findUnique(hql);
        return unique;
    }

    //销售数量详情
    public List<SaleorderCountView> findsaleDtlByStyleId(String styleId, String sbillDate, String ebillDate) {
        String hql = "SELECT new com.casesoft.dmc.model.search.SaleorderCountView" +
                "(s.origname,SUM(s.qty) as sumQty) " +
                "FROM SaleorderCountView s " +
                "WHERE s.styleid=? " +
                "AND s.status <> -1  " +
                "AND s.groupid=? "+
                "AND s.saletype='销售订单' "+
                "AND s.billDate >= To_date(?,'yyyy-mm-dd HH24:mi:ss') AND s.billDate <= To_date(?,'yyyy-mm-dd HH24:mi:ss') " +
                "GROUP BY s.origname "+
                "ORDER BY s.origname asc";
        return this.saleOrderBillDao.find(hql, new Object[]{styleId,"DG", sbillDate, ebillDate});
    }



    @Autowired
    private EpcStockService epcStockService;

    public void confirmExchange(String origCode, String exchangeCode, String origSku, String exchangeSku, String billNo) {

        BillRecord exchangeBillRecord = new BillRecord();
        exchangeBillRecord.setId(billNo + "-" + exchangeCode);
        exchangeBillRecord.setBillNo(billNo);
        exchangeBillRecord.setCode(exchangeCode);
        exchangeBillRecord.setSku(exchangeSku);
        this.saleOrderBillDao.batchExecute("update SaleOrderBill set billType=? where billNo=?","E",billNo);
        this.saleOrderBillDao.saveOrUpdateX(exchangeBillRecord);
        this.saleOrderBillDao.batchExecute("delete from BillRecord where billNo=? and code=?", billNo, origCode);

        List<String> taskIdList = new ArrayList<>();
        List<Business> businessList = this.taskService.findBusinessByBillNo(billNo);
        for (Business business : businessList) {
            taskIdList.add(business.getId());
        }
        String taskIdSqlString = TaskUtil.getSqlStrByList(taskIdList, Record.class, "taskId");
        String sql = "update Record record set record.code=? where record.code=? and " + taskIdSqlString;
        this.saleOrderBillDao.batchExecute(sql, exchangeCode, origCode);

        this.epcStockService.updateEpcStockIn(origCode);
        this.epcStockService.updateEpcStockOut(exchangeCode);
    }

    /**
     * 判断单据是否是商城
     *@param billNo  单据编号
     *@param codes 唯一码
     */
    public void ShopBilldeal(String billNo,String codes){
        //销售单微商城出库逻辑
        SaleOrderBill saleOrderBill = this.saleOrderBillDao.load(billNo);
        if(saleOrderBill.getStatus().equals(BillConstant.BillStatus.shopEnter)){
            String hql = "update EpcStock t set t.inStock=1 where t.code in (?)";
            this.saleOrderBillDao.batchExecute(hql, codes);
        }

    }

    public long findSbByDuId (String destUnitId){
        return this.saleOrderBillDao.findUnique("select count(*) from SaleOrderBill where destUnitId=?",destUnitId);
    }

    public Integer findBillStatus(String billNo) {
       return this.saleOrderBillDao.findUnique("select status from SaleOrderBill where id=?",billNo);
    }
}

