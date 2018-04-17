package com.casesoft.dmc.service.logistics;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.logistics.MonthAccountUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.dao.logistics.*;
import com.casesoft.dmc.extend.third.request.BaseService;
import com.casesoft.dmc.model.logistics.*;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.shop.PointsChange;
import com.casesoft.dmc.model.stock.CodeFirstTime;
import com.casesoft.dmc.model.sys.PointsRule;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.BusinessDtl;
import com.casesoft.dmc.model.task.Record;
import com.casesoft.dmc.service.task.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.spi.CurrencyNameProvider;

/**
 * Created by Administrator on 2017-06-29.
 */
@Service
@Transactional
public class SaleOrderReturnBillService extends BaseService<SaleOrderReturnBill, String> {
    @Autowired
    private SaleOrderReturnBillDao saleOrderReturnBillDao;

    @Autowired
    private TaskService taskService;
    @Autowired
    private ConsignmentBillDtlDao consignmentBillDtlDao;
    @Autowired
    private ConsignmentBillDao consignmentBillDao;
    @Autowired
    private MonthAccountStatementDao monthAccountStatementDao;


    @Override
    public Page<SaleOrderReturnBill> findPage(Page<SaleOrderReturnBill> page, List<PropertyFilter> filters) {
        return saleOrderReturnBillDao.findPage(page, filters);
    }


    @Override
    public void save(SaleOrderReturnBill entity) {

        this.saleOrderReturnBillDao.saveOrUpdate(entity);
    }


    /**
     * 保存销售退货单，同时更新客户欠款金额
     *
     * @param bill
     * @param details
     */
    public void saveReturnBatch(SaleOrderReturnBill bill, List<SaleOrderReturnBillDtl> details) {
        this.saleOrderReturnBillDao.batchExecute("delete from SaleOrderReturnBillDtl sd where sd.billNo=?", bill.getBillNo());
        this.saleOrderReturnBillDao.batchExecute("delete from BillRecord where billNo=?", bill.getBillNo());
        String curYearMonth = CommonUtil.getDateString(new Date(),"yyyy-MM");
        Logger logger = LoggerFactory.getLogger(SaleOrderReturnBill.class);
        Double diffPrice = bill.getActPrice() - bill.getPayPrice();
        String preOrigUnitId= this.saleOrderReturnBillDao.findUnique("select origUnitId from SaleOrderReturnBill as s where s.billNo = ?", bill.getBillNo());
        Unit unit = this.saleOrderReturnBillDao.findUnique("from Unit where id = ?", new Object[]{bill.getOrigUnitId()});
        Double preDiffPrice = this.saleOrderReturnBillDao.findUnique("select s.actPrice-s.payPrice from SaleOrderReturnBill as s where s.billNo = ?", bill.getBillNo());
        Customer customer = this.saleOrderReturnBillDao.findUnique("from Customer where id = ?", new Object[]{bill.getOrigUnitId()});
        Boolean isUpdateMonthAccount = false;
        isUpdateMonthAccount = !curYearMonth.equals(CommonUtil.getDateString(bill.getBillDate(), "yyyy-MM"));
        if(isUpdateMonthAccount){
            Map<String,MonthAccountStatement> preMonthAccountMap = new HashMap<>();
            List<MonthAccountStatement> preMonthAcountList = this.monthAccountStatementDao.find("from MonthAccountStatement where unitId = ? order by month  asc",preOrigUnitId);
            for(MonthAccountStatement m : preMonthAcountList){
                preMonthAccountMap.put(m.getId(),m);
            }
            List<MonthAccountStatement> updateMonthAccountList = new ArrayList<>();
            MonthAccountUtil.updateMonthAcoutData(bill.getBillDate(),preMonthAccountMap,updateMonthAccountList,preOrigUnitId,preDiffPrice,false);
            this.monthAccountStatementDao.doBatchInsert(updateMonthAccountList);

        }
        if(CommonUtil.isNotBlank(preOrigUnitId)){
            Unit preUnit = CacheManager.getUnitByCode(preOrigUnitId);
            if(CommonUtil.isNotBlank(preUnit)){
                this.saleOrderReturnBillDao.batchExecute("update Unit set owingValue = owingValue - ? where id =?", preDiffPrice,preOrigUnitId);
                /*logger.error("销售退货单"+bill.getBillNo()+"金额"+diffPrice+"客户"+preUnit.getName()+"欠款金额"+(preUnit.getOwingValue()-preDiffPrice));*/
                logger.error("Unit原来客户"+preUnit.getName()+"Unit原来客户编号"+preUnit.getId()+"原单本单差额"+preDiffPrice);

            }else{
               /* Customer customer = CacheManager.getCustomerById(preOrigUnitId);*/

                this.saleOrderReturnBillDao.batchExecute("update Customer set owingValue = owingValue - ? where id =?", preDiffPrice,preOrigUnitId);
                /*logger.error("销售退货单"+bill.getBillNo()+"金额"+diffPrice+"客户"+customer.getName()+"欠款金额"+(customer.getOwingValue()-preDiffPrice));*/
                logger.error("Customer原来客户编号"+preOrigUnitId+"原单本单差额"+preDiffPrice);
            }
        }
        if (CommonUtil.isBlank(unit)) {

            if(CommonUtil.isBlank(customer.getOwingValue())){
                customer.setOwingValue(0.0);
            }
            logger.error("销售退货单"+bill.getBillNo()+"本单差额"+diffPrice+"unit客户"+customer.getName()+"unit客户编号"+customer.getId()+"欠款金额"+(customer.getOwingValue()+diffPrice)+"原欠款金额"+(customer.getOwingValue()));
            if(CommonUtil.isNotBlank(preOrigUnitId) && preOrigUnitId.equals(customer.getCode())){
                this.saleOrderReturnBillDao.batchExecute("update Customer set owingValue = ? where id =?", customer.getOwingValue() -preDiffPrice + diffPrice,preOrigUnitId);
            }else{
                customer.setOwingValue(customer.getOwingValue()  + diffPrice);
            }
            this.saleOrderReturnBillDao.saveOrUpdateX(customer);
        } else {
            if(CommonUtil.isBlank(unit.getOwingValue())){
                unit.setOwingValue(0.0);
            }
            logger.error("销售退货单"+bill.getBillNo()+"本单差额"+diffPrice+"unit客户"+unit.getName()+"unit客户编号"+unit.getId()+"欠款金额"+(unit.getOwingValue()+diffPrice)+"原欠款金额"+(unit.getOwingValue()));
            if(CommonUtil.isNotBlank(preOrigUnitId) && preOrigUnitId.equals(unit.getCode())){
                this.saleOrderReturnBillDao.batchExecute("update Unit set owingValue = ? where id =?", unit.getOwingValue() -preDiffPrice + diffPrice,preOrigUnitId);
            }else{
                unit.setOwingValue(unit.getOwingValue()  + diffPrice);
            }
            this.saleOrderReturnBillDao.saveOrUpdateX(unit);
        }

        if(isUpdateMonthAccount){
            Map<String,MonthAccountStatement> monthAccountMap = new HashMap<>();
            List<MonthAccountStatement> monthAcountList = this.monthAccountStatementDao.find("from MonthAccountStatement where unitId = ? order by month  asc",bill.getOrigUnitId());
            for(MonthAccountStatement m : monthAcountList){
                monthAccountMap.put(m.getId(),m);
            }
            List<MonthAccountStatement> updateMonthAccountList = new ArrayList<>();
            MonthAccountUtil.updateMonthAcoutData(bill.getBillDate(),monthAccountMap,updateMonthAccountList,bill.getOrigUnitId(),diffPrice,true);
            this.monthAccountStatementDao.doBatchInsert(updateMonthAccountList);
        }
        this.saleOrderReturnBillDao.saveOrUpdate(bill);
        this.saleOrderReturnBillDao.doBatchInsert(details);
        if(CommonUtil.isNotBlank(bill.getBillRecordList())){
            this.saleOrderReturnBillDao.doBatchInsert(bill.getBillRecordList());
        }
        //退算积分
        //查询库户
       customer = this.saleOrderReturnBillDao.findUnique("from Customer where id = ?", new Object[]{bill.getOrigUnitId()});
        Unit units = this.saleOrderReturnBillDao.findUnique("from Unit where id = ?", new Object[]{bill.getOrigUnitId()});
        if(CommonUtil.isNotBlank(customer)){
            if(CommonUtil.isNotBlank(customer.getVipMessage())){
                //根据code查询
                ArrayList<RecordReturnScore> list=new  ArrayList<RecordReturnScore>();
                for(SaleOrderReturnBillDtl saleOrderReturnBillDtl:details) {
                    String uniqueCodes = saleOrderReturnBillDtl.getUniqueCodes();
                    if (CommonUtil.isNotBlank(uniqueCodes)) {
                        String[] split = uniqueCodes.split(",");
                        for (int i = 0; i < split.length; i++) {
                            List<BillRecord> BillRecords = this.saleOrderReturnBillDao.find("from BillRecord where code = ? order by billNo desc", new Object[]{split[i]});
                            PointsChange pointsChange = this.saleOrderReturnBillDao.findUnique("from PointsChange where id = ?", new Object[]{BillRecords.get(0).getBillNo()});
                            if(CommonUtil.isNotBlank(pointsChange)){
                                PointsRule pointsRule = this.saleOrderReturnBillDao.findUnique("from PointsRule where id = ?", new Object[]{pointsChange.getPointsRuleId()});
                                if(CommonUtil.isNotBlank(pointsRule)){
                                    Double actPrice = saleOrderReturnBillDtl.getActPrice();
                                    double points = pointsRule.getUnitPoints() / 100 * actPrice;
                                    //Customer customer = this.saleOrderReturnBillDao.findUnique("from Customer where id = ?", new Object[]{bill.getOrigUnitId()});
                                    Double v = customer.getVippoints();
                                    if (CommonUtil.isBlank(v)) {
                                        v = 0D;
                                    }
                                    customer.setVippoints(v - points);
                                    this.saleOrderReturnBillDao.saveOrUpdateX(customer);
                                    RecordReturnScore recordReturnScore = new RecordReturnScore();
                                    recordReturnScore.setId(saleOrderReturnBillDtl.getId() + "-" + split[i]);
                                    recordReturnScore.setBillno(bill.getBillNo());
                                    recordReturnScore.setCode(split[i]);
                                    recordReturnScore.setRetrunDetailid(saleOrderReturnBillDtl.getId());
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
            if(CommonUtil.isNotBlank(units.getVipMessage())){
                //根据code查询
                ArrayList<RecordReturnScore> list=new  ArrayList<RecordReturnScore>();
                for(SaleOrderReturnBillDtl saleOrderReturnBillDtl:details) {
                    String uniqueCodes = saleOrderReturnBillDtl.getUniqueCodes();
                    if (CommonUtil.isNotBlank(uniqueCodes)) {
                        String[] split = uniqueCodes.split(",");
                        for (int i = 0; i < split.length; i++) {
                            List<BillRecord> BillRecords = this.saleOrderReturnBillDao.find("from BillRecord where code = ? order by billNo desc", new Object[]{split[i]});
                            PointsChange pointsChange = this.saleOrderReturnBillDao.findUnique("from PointsChange where id = ?", new Object[]{BillRecords.get(0).getBillNo()});
                            if(CommonUtil.isNotBlank(pointsChange)){
                                PointsRule pointsRule = this.saleOrderReturnBillDao.findUnique("from PointsRule where id = ?", new Object[]{pointsChange.getPointsRuleId()});
                                if(CommonUtil.isNotBlank(pointsRule)){
                                    Double actPrice = saleOrderReturnBillDtl.getActPrice();
                                    double points = pointsRule.getUnitPoints() / 100 * actPrice;
                                    //Customer customer = this.saleOrderReturnBillDao.findUnique("from Customer where id = ?", new Object[]{bill.getOrigUnitId()});
                                    Double v = units.getVippoints();
                                    if (CommonUtil.isBlank(v)) {
                                        v = 0D;
                                    }
                                    units.setVippoints(v - points);
                                    this.saleOrderReturnBillDao.saveOrUpdateX(units);
                                    RecordReturnScore recordReturnScore = new RecordReturnScore();
                                    recordReturnScore.setId(saleOrderReturnBillDtl.getId() + "-" + split[i]);
                                    recordReturnScore.setBillno(bill.getBillNo());
                                    recordReturnScore.setCode(split[i]);
                                    recordReturnScore.setRetrunDetailid(saleOrderReturnBillDtl.getId());
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

    public void cancelUpdate(SaleOrderReturnBill saleOrderReturnBill) {
        Logger logger = LoggerFactory.getLogger(SaleOrderReturnBill.class);
        Double diffPrice = saleOrderReturnBill.getActPrice() - saleOrderReturnBill.getPayPrice();
        Unit unit = this.saleOrderReturnBillDao.findUnique("from Unit where id = ?", new Object[]{saleOrderReturnBill.getOrigUnitId()});
        if (CommonUtil.isBlank(unit)) {
            Customer customer = this.saleOrderReturnBillDao.findUnique("from Customer where id = ?", new Object[]{saleOrderReturnBill.getOrigUnitId()});
            customer.setOwingValue(customer.getOwingValue() - diffPrice);
            logger.error("销售退货单"+saleOrderReturnBill.getBillNo()+"金额"+diffPrice+"客户"+customer.getName()+"欠款金额"+(customer.getOwingValue() - diffPrice));
            this.saleOrderReturnBillDao.saveOrUpdateX(customer);
        } else {
            unit.setOwingValue(unit.getOwingValue() - diffPrice);
            logger.error("销售退货单"+saleOrderReturnBill.getBillNo()+"金额"+diffPrice+"客户"+unit.getName()+"欠款金额"+(unit.getOwingValue() - diffPrice));
            this.saleOrderReturnBillDao.saveOrUpdateX(unit);
        }

        this.saleOrderReturnBillDao.saveOrUpdate(saleOrderReturnBill);
        //退算积分
        Customer customer = this.saleOrderReturnBillDao.findUnique("from Customer where id = ?", new Object[]{saleOrderReturnBill.getOrigUnitId()});
        if(CommonUtil.isNotBlank(customer)){
            if(CommonUtil.isNotBlank(customer.getVipMessage())){
                //根据code查询
                ArrayList<RecordReturnScore> list=new  ArrayList<RecordReturnScore>();
                List<SaleOrderReturnBillDtl> details = this.saleOrderReturnBillDao.find("from SaleOrderReturnBillDtl where billNo = ? ", new Object[]{saleOrderReturnBill.getBillNo()});
                if(CommonUtil.isNotBlank(details)){
                    for(SaleOrderReturnBillDtl saleOrderReturnBillDtl:details) {
                        String uniqueCodes = saleOrderReturnBillDtl.getUniqueCodes();
                        if (CommonUtil.isNotBlank(uniqueCodes)) {
                            String[] split = uniqueCodes.split(",");
                            for (int i = 0; i < split.length; i++) {
                                List<BillRecord> BillRecords = this.saleOrderReturnBillDao.find("from BillRecord where code = ? order by billNo desc", new Object[]{split[i]});
                                PointsChange pointsChange = this.saleOrderReturnBillDao.findUnique("from PointsChange where id = ?", new Object[]{BillRecords.get(0).getBillNo()});
                                if(CommonUtil.isNotBlank(pointsChange)){
                                    PointsRule pointsRule = this.saleOrderReturnBillDao.findUnique("from PointsRule where id = ?", new Object[]{pointsChange.getPointsRuleId()});
                                    if(CommonUtil.isNotBlank(pointsRule)){
                                        Double actPrice = saleOrderReturnBillDtl.getActPrice();
                                        double points = pointsRule.getUnitPoints() / 100 * actPrice;
                                        // Customer customer = this.saleOrderReturnBillDao.findUnique("from Customer where id = ?", new Object[]{saleOrderReturnBill.getOrigUnitId()});
                                        Double v = customer.getVippoints();
                                        if (CommonUtil.isBlank(v)) {
                                            v = 0D;
                                        }
                                        customer.setVippoints(v + points);
                                        this.saleOrderReturnBillDao.saveOrUpdateX(customer);
                                        RecordReturnScore recordReturnScore = new RecordReturnScore();
                                        recordReturnScore.setId(saleOrderReturnBillDtl.getId() + "-" + split[i]);
                                        recordReturnScore.setBillno(saleOrderReturnBill.getBillNo());
                                        recordReturnScore.setCode(split[i]);
                                        recordReturnScore.setRetrunDetailid(saleOrderReturnBillDtl.getId());
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

    @Override
    public SaleOrderReturnBill load(String id) {
        return this.saleOrderReturnBillDao.load(id);
    }

    @Override
    public SaleOrderReturnBill get(String propertyName, Object value) {
        return this.saleOrderReturnBillDao.findUniqueBy(propertyName,value);
    }

    @Override
    public List<SaleOrderReturnBill> find(List<PropertyFilter> filters) {
        return this.saleOrderReturnBillDao.find(filters);
    }

    @Override
    public List<SaleOrderReturnBill> getAll() {
        return this.saleOrderReturnBillDao.getAll();
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    public SaleOrderReturnBill findBillByBillNo(String billNo) {
        String hql = "from SaleOrderReturnBill srb where srb.billNo =?";
        return this.saleOrderReturnBillDao.findUnique(hql, new Object[]{billNo});
    }

    public List<SaleOrderReturnBillDtl> findDtlByBillNo(String billNo) {
        String hql = "from SaleOrderReturnBillDtl dtl where billNo=?";
        return this.saleOrderReturnBillDao.find(hql, new Object[]{billNo});
    }

    public String findMaxBillNO(String prefix) {
        String hql = "select max(CAST(SUBSTRING(sorb.billNo,9),integer)) from SaleOrderReturnBill sorb where sorb.billNo like ?";
        Integer No = this.saleOrderReturnBillDao.findUnique(hql, new Object[]{prefix + "%"});
        return No == null ? (prefix + "001") : prefix + CommonUtil.convertIntToString(No + 1, 3);
    }

    public void saveBus(Business bus, SaleOrderReturnBill bill) {
        this.saleOrderReturnBillDao.saveOrUpdate(bill);
        this.saleOrderReturnBillDao.saveOrUpdateX(bus);
        this.saleOrderReturnBillDao.doBatchInsert(bus.getDtlList());
    }

    @Override
    public void update(SaleOrderReturnBill entity) {

    }

    @Override
    public void delete(SaleOrderReturnBill entity) {

    }

    public void deleteBatchDtl(String billNo) {
        String hql = "delete from SaleOrderReturnBillDtl sd where sd.billNo=?";
        this.saleOrderReturnBillDao.batchExecute(hql, billNo);
    }

    @Override
    public void delete(String billNo) {

    }

    public SaleOrderReturnBill findUniqueByBillNo(String billNo){
        return this.saleOrderReturnBillDao.findUnique("from SaleOrderReturnBill pcrb where  pcrb.billNo=?",new Object[]{billNo});
    }

    public List<SaleOrderReturnBillDtl> findDetailsByBillNo(String billNo){
        String hql="from SaleOrderReturnBillDtl pd where pd.billNo=?";
        return this.saleOrderReturnBillDao.find(hql,new Object[]{billNo});
    }
    public MessageBox saveBusiness(SaleOrderReturnBill saleOrderReturnBill, List<SaleOrderReturnBillDtl> purchaseOrderBillDtlList, Business business) throws Exception {
        MessageBox messageBox = this.taskService.checkEpcStock(business);
        if(messageBox.getSuccess()){
            this.saleOrderReturnBillDao.saveOrUpdate(saleOrderReturnBill);
            this.saleOrderReturnBillDao.doBatchInsert(purchaseOrderBillDtlList);
            this.taskService.webSave(business);
            if(CommonUtil.isNotBlank(saleOrderReturnBill.getBillRecordList())){
                this.saleOrderReturnBillDao.doBatchInsert(saleOrderReturnBill.getBillRecordList());
            }
            //记录第一次入库时间
            if(business.getType().equals(Constant.TaskType.Inbound)){
                List<Record> recordList = business.getRecordList();
                ArrayList<CodeFirstTime> list=new ArrayList<CodeFirstTime>();
                for(int i=0;i<recordList.size();i++){
                    CodeFirstTime codeFirstTime =this.saleOrderReturnBillDao.findUnique("from CodeFirstTime where code=? and warehouseId=?",new Object[]{recordList.get(i).getCode(),saleOrderReturnBill.getDestId()});
                    if(CommonUtil.isBlank(codeFirstTime)){
                        CodeFirstTime newcodeFirstTime=new CodeFirstTime();
                        newcodeFirstTime.setId(recordList.get(i).getCode()+"-"+saleOrderReturnBill.getDestId());
                        newcodeFirstTime.setCode(recordList.get(i).getCode());
                        newcodeFirstTime.setWarehouseId(saleOrderReturnBill.getDestId());
                        newcodeFirstTime.setFirstTime(new Date());
                        Unit unitByCode = CacheManager.getUnitByCode(saleOrderReturnBill.getDestUnitId());
                        if(CommonUtil.isNotBlank(unitByCode)&&CommonUtil.isNotBlank(unitByCode.getGroupId())&&unitByCode.getGroupId().equals("JMS")){
                            newcodeFirstTime.setWarehousePrice(recordList.get(i).getPrice());
                        }else{
                            Style styleById = CacheManager.getStyleById(recordList.get(i).getStyleId());
                            newcodeFirstTime.setWarehousePrice(styleById.getPreCast());
                        }
                        list.add(newcodeFirstTime);
                    }
                }
                if(list.size()!=0){
                    this.saleOrderReturnBillDao.doBatchInsert(list);
                }
            }
            return messageBox;
        }else{
            return messageBox;
        }


    }


    public List<SaleOrderReturnBillDtl> findBillDtlByBillNo(String billNo) {
        return this.saleOrderReturnBillDao.find("from SaleOrderReturnBillDtl where billNo=?", new Object[]{billNo});
    }


    public List<BillRecord> getBillRecod(String billNo) {
        return  this.saleOrderReturnBillDao.find("from BillRecord where billNo=?",new Object[]{billNo});
    }


    public String handleMoneycancel(String billNo,String srcBillNo){
        try{
            List<SaleOrderReturnBillDtl> SaleOrderReturnBillDtllist= this.saleOrderReturnBillDao.find("from SaleOrderReturnBillDtl where billNo=?", new Object[]{billNo});
            List<ConsignmentBillDtl> ConsignmentBillDtllist=this.saleOrderReturnBillDao.find("from ConsignmentBillDtl where billNo=?", new Object[]{srcBillNo});
            for(SaleOrderReturnBillDtl saleOrderReturnBillDtl:SaleOrderReturnBillDtllist){
               for(int i=0;i<ConsignmentBillDtllist.size();i++){
                   if(saleOrderReturnBillDtl.getSku().equals(ConsignmentBillDtllist.get(i).getSku())){
                       ConsignmentBillDtl consignmentBillDtl= ConsignmentBillDtllist.get(i);
                       consignmentBillDtl.setOutMonyQty( consignmentBillDtl.getOutMonyQty()-1);
                       this.consignmentBillDtlDao.update(consignmentBillDtl);
                   }
               }
            }
            return "";
        }catch (Exception e){
            return "取消失败";
        }
    }
    public String handleMoneycancelSO(String billNo,String srcBillNo){
        try{
            List<SaleOrderReturnBillDtl> SaleOrderReturnBillDtllist= this.saleOrderReturnBillDao.find("from SaleOrderReturnBillDtl where billNo=?", new Object[]{billNo});
            List<SaleOrderBillDtl> SaleOrderBillDtllist=this.saleOrderReturnBillDao.find("from SaleOrderBillDtl where billNo=?", new Object[]{srcBillNo});
            for(SaleOrderReturnBillDtl saleOrderReturnBillDtl:SaleOrderReturnBillDtllist){
                for(int i=0;i<SaleOrderBillDtllist.size();i++){
                    if(saleOrderReturnBillDtl.getSku().equals(SaleOrderBillDtllist.get(i).getSku())){
                        SaleOrderBillDtl saleOrderBillDtl= SaleOrderBillDtllist.get(i);
                        this.consignmentBillDtlDao.batchExecute("update SaleOrderBillDtl set returnQty=? where id=?",saleOrderBillDtl.getReturnQty()-1,saleOrderBillDtl.getId());
                    }
                }
            }
            return "";
        }catch (Exception e){
            return "取消失败";
        }
    }

    public String handleQtycancel(String billNo,String srcBillNo,User currentUser){
        try{
            boolean isok=true;
            List<BillRecord> list= this.saleOrderReturnBillDao.find("from BillRecord where billNo=? ", new Object[]{billNo});
            ConsignmentBill load = this.consignmentBillDao.load(srcBillNo);
            List<Record> recordList = new ArrayList<>();
            Business bus = new Business();
            String taksId = "TSK" + CommonUtil.getDateString(new Date(), "yyyyMMdd") + System.currentTimeMillis();
            Map<String, BusinessDtl> businessDtlMap = new HashMap<>();
            for(BillRecord billRecord:list){
                List<BillRecord> listcode= this.saleOrderReturnBillDao.find("from BillRecord where code=? order by id desc", new Object[]{billRecord.getCode()});
                BillRecord billRecord1 = listcode.get(0);
               if(!billRecord1.getBillNo().equals(billNo)){
                   isok=false;
               }
            }
            if(isok){
                for(BillRecord billRecord:list){
                    this.saleOrderReturnBillDao.batchExecute("update EpcStock set inStock=1, warehouseId=? where code=?",load.getDestId(),billRecord.getCode());
                    this.saleOrderReturnBillDao.batchExecute("update ConsignmentBill set totOutQty=?",load.getTotOutQty()-1L);
                    Record record = new Record(billRecord.getCode(), taksId, taksId, Constant.Token.Storage_Consigment_Inbound, "KE000001", "");
                    BusinessDtl dtl = new BusinessDtl(taksId, currentUser.getOwnerId(), Constant.Token.Storage_Consigment_Inbound, "KE000001", billRecord.getSku(), 1);
                    record.setSku(billRecord.getSku());
                    record.setScanTime(new Date());
                    record.setId(new GuidCreator().toString());
                    record.setType(Constant.TaskType.Inbound);
                    recordList.add(record);
                    dtl.setId(new GuidCreator().toString());
                    dtl.setType(Constant.TaskType.Inbound);
                    businessDtlMap.put(billRecord.getSku(), dtl);
                }
                List<SaleOrderReturnBillDtl> SaleOrderReturnBillDtllist= this.saleOrderReturnBillDao.find("from SaleOrderReturnBillDtl where billNo=?", new Object[]{billNo});
                List<ConsignmentBillDtl> ConsignmentBillDtllist=this.saleOrderReturnBillDao.find("from ConsignmentBillDtl where billNo=?", new Object[]{srcBillNo});
                Double totPreVal = 0D;
                Double totRcvPrice = 0d;
                for(SaleOrderReturnBillDtl saleOrderReturnBillDtl:SaleOrderReturnBillDtllist){
                    for(int i=0;i<ConsignmentBillDtllist.size();i++){
                        if(saleOrderReturnBillDtl.getSku().equals(ConsignmentBillDtllist.get(i).getSku())){
                            ConsignmentBillDtl consignmentBillDtl= ConsignmentBillDtllist.get(i);
                            consignmentBillDtl.setOutQty( consignmentBillDtl.getOutQty()-1);
                            consignmentBillDtl.setBeforeoutQty(consignmentBillDtl.getBeforeoutQty()-1);
                            this.consignmentBillDtlDao.update(consignmentBillDtl);
                            this.saleOrderReturnBillDao.batchExecute("update ConsignmentBill set totOutVal=?",load.getTotOutVal()-consignmentBillDtl.getActPrice());
                        }
                        for(int a=0;a<recordList.size();a++){
                            if(ConsignmentBillDtllist.get(i).getSku().equals(recordList.get(a).getSku())){
                                Record record = recordList.get(a);
                                record.setOwnerId(currentUser.getOwnerId());
                                record.setDestId(load.getDestId());
                                record.setDestUnitId(load.getDestUnitId());
                                record.setOrigId(load.getOrigId());
                                record.setOrigUnitId(load.getOrigUnitId());
                                record.setStyleId(ConsignmentBillDtllist.get(i).getStyleId());
                                record.setColorId(ConsignmentBillDtllist.get(i).getColorId());
                                record.setSizeId(ConsignmentBillDtllist.get(i).getSizeId());
                                record.setExtField(ConsignmentBillDtllist.get(i).getInStockType());
                                record.setPrice(ConsignmentBillDtllist.get(i).getActPrice());
                                BusinessDtl dtl = businessDtlMap.get(recordList.get(a).getSku());
                                totPreVal += recordList.get(a).getPrice();
                                totRcvPrice+= ConsignmentBillDtllist.get(i).getPrice();
                                dtl.setPreVal(dtl.getQty()*ConsignmentBillDtllist.get(i).getActPrice());
                            }
                        }

                    }

                }
                bus.setDtlList(new ArrayList<>(businessDtlMap.values()));
                bus.setId(taksId);
                bus.setToken(Constant.Token.Storage_Refund_Inbound);
                bus.setBeginTime(new Date());
                bus.setEndTime(new Date());
                bus.setBillId(load.getBillNo());
                bus.setBillNo(load.getBillNo());
                bus.setDestId(load.getDestId());
                bus.setDestUnitId(load.getDestUnitId());
                bus.setOrigId(load.getOrigId());
                bus.setOrigUnitId(load.getOrigUnitId());
                bus.setOwnerId(currentUser.getOwnerId());
                bus.setDeviceId("KE000001");
                bus.setStatus(Constant.TaskStatus.Submitted);
                bus.setTotCarton(1L);
                bus.setTotEpc((long) list.size());
                bus.setTotPrice(totRcvPrice);
                bus.setTotSku((long) bus.getDtlList().size());
                bus.setTotStyle((long) list.size());
                bus.setType(Constant.TaskType.Inbound);
                bus.setRecordList(recordList);
                bus.setTotPreVal(totRcvPrice);
                this.taskService.save(bus);

                /*ConsignmentBill consignmentBill =this.saleOrderReturnBillDao.findUnique("from ConsignmentBill where billNo=?", new Object[]{srcBillNo});*/
              /*  load.setTotOutQty(Long.getLong((load.getTotOutQty().intValue()-sum)+""));
                this.consignmentBillDao.update(load);*/
                return null;
            }else{
                return "取消失败";
            }
        }catch (Exception e){
            return "取消失败";
        }

    }
}
