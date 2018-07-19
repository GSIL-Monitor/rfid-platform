package com.casesoft.dmc.service.logistics;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.dao.logistics.ConsignmentBillDao;
import com.casesoft.dmc.dao.logistics.ConsignmentBillDtlDao;
import com.casesoft.dmc.dao.logistics.MonthAccountStatementDao;
import com.casesoft.dmc.dao.logistics.SaleOrderReturnBillDao;
import com.casesoft.dmc.extend.third.request.BaseService;
import com.casesoft.dmc.model.logistics.*;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.stock.CodeFirstTime;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.BusinessDtl;
import com.casesoft.dmc.model.task.Record;
import com.casesoft.dmc.service.shop.GuestValueChangeService;
import com.casesoft.dmc.service.shop.PointsChangeService;
import com.casesoft.dmc.service.sys.GuestService;
import com.casesoft.dmc.service.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    @Autowired
    private MonthAccountStatementService monthAccountStatementService;
    @Autowired
    private PointsChangeService pointsChangeService;
    @Autowired
    private GuestService guestService;
    @Autowired
    private GuestValueChangeService guestValueChangeService;

    public Double findSumActPrice(String origUnitId){
        return this.saleOrderReturnBillDao.findUnique("select sum(actPrice) from SaleOrderReturnBill where status = 2 and origUnitId =?",origUnitId);
    }

    @Override
    public Page<SaleOrderReturnBill> findPage(Page<SaleOrderReturnBill> page, List<PropertyFilter> filters) {
        return saleOrderReturnBillDao.findPage(page, filters);
    }


    @Override
    public void save(SaleOrderReturnBill entity) {

        this.saleOrderReturnBillDao.saveOrUpdate(entity);
    }

    //更新关联单号
    public void updateNo(String billNo,String rbillNo){
        this.saleOrderReturnBillDao.batchExecute("update SaleOrderReturnBill set srcBillNo = '"+billNo+"' where billNo = ?",rbillNo);
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
        String curYearMonth = CommonUtil.getDateString(new Date(), "yyyy-MM");
        Double diffPrice = bill.getActPrice() - bill.getPayPrice();
        Double preDiffPrice = this.saleOrderReturnBillDao.findUnique("select s.actPrice-s.payPrice from SaleOrderReturnBill as s where s.billNo = ?", bill.getBillNo());
        if(CommonUtil.isBlank(preDiffPrice)){
            preDiffPrice = 0D;
        }
        String origUnitId = bill.getOrigUnitId();
        String preOrigUnitId = this.saleOrderReturnBillDao.findUnique("select origUnitId from SaleOrderReturnBill as s where s.billNo = ?", bill.getBillNo());
        Boolean isUpdateMonthAccount = !curYearMonth.equals(CommonUtil.getDateString(bill.getBillDate(), "yyyy-MM"));
        if (isUpdateMonthAccount) {
            this.monthAccountStatementService.updateMonthAccountData(bill.getBillDate(), preOrigUnitId, preDiffPrice, false);

            this.monthAccountStatementService.updateMonthAccountData(bill.getBillDate(), bill.getOrigUnitId(), diffPrice, true);
        }
        //add by yushen 改变客户或者客户余额变动 保存所有变动记录，更新客户欠款
        if(diffPrice.doubleValue() != preDiffPrice.doubleValue() || !origUnitId.equals(preOrigUnitId)){
            //modify by yushen更新之前的客户欠款和积分
            if (CommonUtil.isNotBlank(preOrigUnitId)) {
                Unit preUnit = this.saleOrderReturnBillDao.findUnique("from Unit where id = ? and status=1", preOrigUnitId);
                Customer preCustomer = this.saleOrderReturnBillDao.findUnique("from Customer where id = ? and status=1", preOrigUnitId);
                //add by yushen 保存积分回退记录
                Long pointsBackoff = this.pointsChangeService.savePointsBackoff(bill.getId(), preOrigUnitId, this.guestService.getVipPoints(preUnit, preCustomer), Constant.ChangeRecordStatus.SaleReturnOrderChange);
                //保存客户欠款回退记录
                this.guestValueChangeService.saveValueBackoff(bill.getId(), preOrigUnitId, this.guestService.getOwingValue(preUnit, preCustomer), Constant.ChangeRecordStatus.SaleReturnOrderChange);
                //更新客户欠款金额和积分
                this.guestService.resetPreGust(bill.getId(), preDiffPrice, pointsBackoff, preUnit, preCustomer);
            }
            Unit unit = this.saleOrderReturnBillDao.findUnique("from Unit where id = ? and status=1", origUnitId);
            Customer customer = this.saleOrderReturnBillDao.findUnique("from Customer where id = ? and status=1", origUnitId);
            Long points = 0L;
            if(CommonUtil.isNotBlank(bill.getSrcBillNo())){//寄存转退货
                points = this.pointsChangeService.cm2ReturnOrderPointsFallback(bill, details, guestService.getVipPoints(unit, customer));
            }else {//正常退货
                //add by yushen 计算销售单积分并保存积分变动记录
                points = this.pointsChangeService.savePointsFallback(bill, details, guestService.getVipPoints(unit, customer));
            }
            //保存客户欠款变动记录
            this.guestValueChangeService.saveValueChange(bill.getId(), bill.getActPrice(), bill.getPayPrice(), origUnitId, this.guestService.getOwingValue(unit, customer), Constant.ChangeRecordStatus.SaleReturnOrder);
            //更新客户欠款金额和积分
            this.guestService.updateCurrentGuest(bill.getId(), diffPrice, points, unit, customer);
        }
        //保存订单
        this.saleOrderReturnBillDao.saveOrUpdate(bill);
        this.saleOrderReturnBillDao.doBatchInsert(details);
        if (CommonUtil.isNotBlank(bill.getBillRecordList())) {
            this.saleOrderReturnBillDao.doBatchInsert(bill.getBillRecordList());
        }
    }

    public void cancelUpdate(SaleOrderReturnBill saleOrderReturnBill) {
        Double diffPrice = saleOrderReturnBill.getActPrice() - saleOrderReturnBill.getPayPrice();
        String preOrigUnitId = saleOrderReturnBill.getOrigUnitId();
        Unit preUnit = this.saleOrderReturnBillDao.findUnique("from Unit where id = ? and status=1", saleOrderReturnBill.getOrigUnitId());
        Customer preCustomer = this.saleOrderReturnBillDao.findUnique("from Customer where id = ? and status=1", saleOrderReturnBill.getOrigUnitId());
        //add by yushen 保存积分回退记录
        Long pointsBackoff = this.pointsChangeService.savePointsBackoff(saleOrderReturnBill.getId(), preOrigUnitId, this.guestService.getVipPoints(preUnit, preCustomer), Constant.ChangeRecordStatus.SaleReturnOrderCancel);
        //保存客户欠款回退记录
        this.guestValueChangeService.saveValueBackoff(saleOrderReturnBill.getId(), preOrigUnitId, this.guestService.getOwingValue(preUnit, preCustomer), Constant.ChangeRecordStatus.SaleReturnOrderCancel);
        //更新客户欠款金额和积分
        this.guestService.resetPreGust(saleOrderReturnBill.getBillNo(), diffPrice, pointsBackoff, preUnit, preCustomer);

        this.saleOrderReturnBillDao.saveOrUpdate(saleOrderReturnBill);
    }

    @Override
    public SaleOrderReturnBill load(String id) {
        return this.saleOrderReturnBillDao.load(id);
    }

    @Override
    public SaleOrderReturnBill get(String propertyName, Object value) {
        return this.saleOrderReturnBillDao.findUniqueBy(propertyName, value);
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

    public SaleOrderReturnBill findUniqueByBillNo(String billNo) {
        return this.saleOrderReturnBillDao.findUnique("from SaleOrderReturnBill pcrb where  pcrb.billNo=?", new Object[]{billNo});
    }

    public List<SaleOrderReturnBillDtl> findDetailsByBillNo(String billNo) {
        String hql = "from SaleOrderReturnBillDtl pd where pd.billNo=?";
        return this.saleOrderReturnBillDao.find(hql, new Object[]{billNo});
    }

    public MessageBox saveBusiness(SaleOrderReturnBill saleOrderReturnBill, List<SaleOrderReturnBillDtl> purchaseOrderBillDtlList, Business business) throws Exception {
        MessageBox messageBox = this.taskService.checkEpcStock(business);
        if (messageBox.getSuccess()) {
            this.saleOrderReturnBillDao.saveOrUpdate(saleOrderReturnBill);
            this.saleOrderReturnBillDao.doBatchInsert(purchaseOrderBillDtlList);
            this.taskService.webSave(business);
            if (CommonUtil.isNotBlank(saleOrderReturnBill.getBillRecordList())) {
                this.saleOrderReturnBillDao.doBatchInsert(saleOrderReturnBill.getBillRecordList());
            }
            //记录第一次入库时间
            if (business.getType().equals(Constant.TaskType.Inbound)) {
                List<Record> recordList = business.getRecordList();
                ArrayList<CodeFirstTime> list = new ArrayList<CodeFirstTime>();
                for (int i = 0; i < recordList.size(); i++) {
                    CodeFirstTime codeFirstTime = this.saleOrderReturnBillDao.findUnique("from CodeFirstTime where code=? and warehouseId=?", new Object[]{recordList.get(i).getCode(), saleOrderReturnBill.getDestId()});
                    if (CommonUtil.isBlank(codeFirstTime)) {
                        CodeFirstTime newcodeFirstTime = new CodeFirstTime();
                        newcodeFirstTime.setId(recordList.get(i).getCode() + "-" + saleOrderReturnBill.getDestId());
                        newcodeFirstTime.setCode(recordList.get(i).getCode());
                        newcodeFirstTime.setWarehouseId(saleOrderReturnBill.getDestId());
                        newcodeFirstTime.setFirstTime(new Date());
                        Unit unitByCode = CacheManager.getUnitByCode(saleOrderReturnBill.getDestUnitId());
                        if (CommonUtil.isNotBlank(unitByCode) && CommonUtil.isNotBlank(unitByCode.getGroupId()) && unitByCode.getGroupId().equals("JMS")) {
                            newcodeFirstTime.setWarehousePrice(recordList.get(i).getPrice());
                        } else {
                            Style styleById = CacheManager.getStyleById(recordList.get(i).getStyleId());
                            newcodeFirstTime.setWarehousePrice(styleById.getPreCast());
                        }
                        list.add(newcodeFirstTime);
                    }
                }
                if (list.size() != 0) {
                    this.saleOrderReturnBillDao.doBatchInsert(list);
                }
            }
            return messageBox;
        } else {
            return messageBox;
        }


    }


    public List<SaleOrderReturnBillDtl> findBillDtlByBillNo(String billNo) {
        return this.saleOrderReturnBillDao.find("from SaleOrderReturnBillDtl where billNo=?", new Object[]{billNo});
    }


    public List<BillRecord> getBillRecod(String billNo) {
        return this.saleOrderReturnBillDao.find("from BillRecord where billNo=?", new Object[]{billNo});
    }

    public List<BillRecord> getBillRecordForCycle( String code,String billNo) {
        String hql = "from BillRecord billrecord " +
                "WHERE  "+ code  +
                "AND billrecord.billNo=?";
        return this.saleOrderReturnBillDao.find(hql, new Object[]{billNo});
    }


    public String handleMoneycancel(String billNo, String srcBillNo) {
        try {
            List<SaleOrderReturnBillDtl> SaleOrderReturnBillDtllist = this.saleOrderReturnBillDao.find("from SaleOrderReturnBillDtl where billNo=?", new Object[]{billNo});
            List<ConsignmentBillDtl> ConsignmentBillDtllist = this.saleOrderReturnBillDao.find("from ConsignmentBillDtl where billNo=?", new Object[]{srcBillNo});
            for (SaleOrderReturnBillDtl saleOrderReturnBillDtl : SaleOrderReturnBillDtllist) {
                for (int i = 0; i < ConsignmentBillDtllist.size(); i++) {
                    if (saleOrderReturnBillDtl.getSku().equals(ConsignmentBillDtllist.get(i).getSku())) {
                        ConsignmentBillDtl consignmentBillDtl = ConsignmentBillDtllist.get(i);
                        consignmentBillDtl.setOutMonyQty(consignmentBillDtl.getOutMonyQty() - 1);
                        this.consignmentBillDtlDao.update(consignmentBillDtl);
                    }
                }
            }
            return "";
        } catch (Exception e) {
            return "取消失败";
        }
    }

    public String handleMoneycancelSO(String billNo, String srcBillNo) {
        try {
            List<SaleOrderReturnBillDtl> SaleOrderReturnBillDtllist = this.saleOrderReturnBillDao.find("from SaleOrderReturnBillDtl where billNo=?", new Object[]{billNo});
            List<SaleOrderBillDtl> SaleOrderBillDtllist = this.saleOrderReturnBillDao.find("from SaleOrderBillDtl where billNo=?", new Object[]{srcBillNo});
            for (SaleOrderReturnBillDtl saleOrderReturnBillDtl : SaleOrderReturnBillDtllist) {
                for (int i = 0; i < SaleOrderBillDtllist.size(); i++) {
                    if (saleOrderReturnBillDtl.getSku().equals(SaleOrderBillDtllist.get(i).getSku())) {
                        SaleOrderBillDtl saleOrderBillDtl = SaleOrderBillDtllist.get(i);
                        this.consignmentBillDtlDao.batchExecute("update SaleOrderBillDtl set returnQty=? where id=?", saleOrderBillDtl.getReturnQty() - 1, saleOrderBillDtl.getId());
                    }
                }
            }
            return "";
        } catch (Exception e) {
            return "取消失败";
        }
    }

    public String handleQtycancel(String billNo, String srcBillNo, User currentUser) {
        try {
            boolean isok = true;
            List<BillRecord> list = this.saleOrderReturnBillDao.find("from BillRecord where billNo=? ", new Object[]{billNo});
            ConsignmentBill load = this.consignmentBillDao.load(srcBillNo);
            List<Record> recordList = new ArrayList<>();
            Business bus = new Business();
            String taksId = "TSK" + CommonUtil.getDateString(new Date(), "yyyyMMdd") + System.currentTimeMillis();
            Map<String, BusinessDtl> businessDtlMap = new HashMap<>();
            for (BillRecord billRecord : list) {
                List<BillRecord> listcode = this.saleOrderReturnBillDao.find("from BillRecord where code=? order by id desc", new Object[]{billRecord.getCode()});
                BillRecord billRecord1 = listcode.get(0);
                if (!billRecord1.getBillNo().equals(billNo)) {
                    isok = false;
                }
            }
            if (isok) {
                for (BillRecord billRecord : list) {
                    this.saleOrderReturnBillDao.batchExecute("update EpcStock set inStock=1, warehouseId=? where code=?", load.getDestId(), billRecord.getCode());
                    this.saleOrderReturnBillDao.batchExecute("update ConsignmentBill set totOutQty=?", load.getTotOutQty() - 1L);
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
                List<SaleOrderReturnBillDtl> SaleOrderReturnBillDtllist = this.saleOrderReturnBillDao.find("from SaleOrderReturnBillDtl where billNo=?", new Object[]{billNo});
                List<ConsignmentBillDtl> ConsignmentBillDtllist = this.saleOrderReturnBillDao.find("from ConsignmentBillDtl where billNo=?", new Object[]{srcBillNo});
                Double totPreVal = 0D;
                Double totRcvPrice = 0d;
                for (SaleOrderReturnBillDtl saleOrderReturnBillDtl : SaleOrderReturnBillDtllist) {
                    for (int i = 0; i < ConsignmentBillDtllist.size(); i++) {
                        if (saleOrderReturnBillDtl.getSku().equals(ConsignmentBillDtllist.get(i).getSku())) {
                            ConsignmentBillDtl consignmentBillDtl = ConsignmentBillDtllist.get(i);
                            consignmentBillDtl.setOutQty(consignmentBillDtl.getOutQty() - 1);
                            consignmentBillDtl.setBeforeoutQty(consignmentBillDtl.getBeforeoutQty() - 1);
                            this.consignmentBillDtlDao.update(consignmentBillDtl);
                            this.saleOrderReturnBillDao.batchExecute("update ConsignmentBill set totOutVal=?", load.getTotOutVal() - consignmentBillDtl.getActPrice());
                        }
                        for (int a = 0; a < recordList.size(); a++) {
                            if (ConsignmentBillDtllist.get(i).getSku().equals(recordList.get(a).getSku())) {
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
                                totRcvPrice += ConsignmentBillDtllist.get(i).getPrice();
                                dtl.setPreVal(dtl.getQty() * ConsignmentBillDtllist.get(i).getActPrice());
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
            } else {
                return "取消失败";
            }
        } catch (Exception e) {
            return "取消失败";
        }

    }

    public Integer findBillStatus(String billNo) {
        return this.saleOrderReturnBillDao.findUnique("select status from SaleOrderReturnBill where id=?", billNo);
    }

    public long findSbByDuId (String origUnitId){
        return this.saleOrderReturnBillDao.findUnique("select count (*) from SaleOrderReturnBill where origUnitId=?",origUnitId);
    }
}
