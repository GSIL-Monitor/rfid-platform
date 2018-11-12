package com.casesoft.dmc.service.logistics;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.logistics.BillConvertUtil;
import com.casesoft.dmc.controller.task.TaskUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.dao.logistics.*;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.logistics.*;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.search.SaleorderCountView;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.shop.PayDetail;
import com.casesoft.dmc.model.stock.CodeFirstTime;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.model.tag.Epc;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.Record;
import com.casesoft.dmc.service.cfg.PropertyService;
import com.casesoft.dmc.service.shop.GuestValueChangeService;
import com.casesoft.dmc.service.shop.PointsChangeService;
import com.casesoft.dmc.service.shop.payDetailService;
import com.casesoft.dmc.service.stock.EpcStockService;
import com.casesoft.dmc.service.sys.GuestService;
import com.casesoft.dmc.service.task.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
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
    private AbnormalCodeMessageDao  abnormalCodeMessageDao;
    @Autowired
    private SaleOrderReturnBillDao saleOrderReturnBillDao;
    @Autowired
    private TransferOrderBillDao transferOrderBillDao;
    @Autowired
    private TransferOrderBillService transferOrderBillService;

    @Autowired
    private PropertyService propertyService;
    @Autowired
    private PointsChangeService pointsChangeService;
    @Autowired
    private MonthAccountStatementDao monthAccountStatementDao;
    @Autowired
    private MonthAccountStatementService monthAccountStatementService;
    @Autowired
    private GuestService guestService;
    @Autowired
    private GuestValueChangeService guestValueChangeService;
    @Autowired
    private payDetailService payDetailService;

    private Logger logger = LoggerFactory.getLogger(SaleOrderBill.class);

    public Double findSumActPrice(String destUnitId){
        return this.saleOrderBillDao.findUnique("select sum(actPrice) from SaleOrderBill where status = 2 and destUnitId =?",destUnitId);
    }

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

    public List<SaleOrderBill> find(String billNo) {
        return this.saleOrderBillDao.find("from SaleOrderBill where billNo=?", new Object[]{billNo});
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

    /*
     *list:单据保存是不能出库异常的唯一码
     */
    public void save(SaleOrderBill saleOrderBill, List<SaleOrderBillDtl> saleOrderBillDtlList,List<AbnormalCodeMessage> list) {
        this.saleOrderBillDao.batchExecute("delete from SaleOrderBillDtl where billNo=?", saleOrderBill.getBillNo());
        this.saleOrderBillDao.batchExecute("delete from BillRecord where billNo=?", saleOrderBill.getBillNo());
        //客户月结表数据Id
        String curYearMonth = CommonUtil.getDateString(new Date(),"yyyy-MM");
        //数据库中查询是否已经存在该订单

        Double diffPrice = saleOrderBill.getActPrice() - saleOrderBill.getPayPrice();
        Double preDiffPrice = this.saleOrderBillDao.findUnique("select s.actPrice-s.payPrice from SaleOrderBill as s where s.billNo = ?", saleOrderBill.getBillNo());
        if(CommonUtil.isBlank(preDiffPrice)){
            preDiffPrice = 0D;
        }
        String destUnitId = saleOrderBill.getDestUnitId();
        String preDestUnitId = this.saleOrderBillDao.findUnique("select destUnitId from SaleOrderBill as s where s.billNo = ?", saleOrderBill.getBillNo());
        //是否更新客户欠款月结表
        Boolean isUpdateMonthAccount = !curYearMonth.equals(CommonUtil.getDateString(saleOrderBill.getBillDate(), "yyyy-MM"));

        //modify by yushen 更新客户月结表数据
        if(isUpdateMonthAccount){
            this.monthAccountStatementService.updateMonthAccountData(saleOrderBill.getBillDate(), preDestUnitId, preDiffPrice, false);

            this.monthAccountStatementService.updateMonthAccountData(saleOrderBill.getBillDate(), destUnitId, diffPrice, true);
        }

        //add by yushen 改变客户或者客户余额变动 保存所有变动记录，更新客户欠款
        if(diffPrice.doubleValue() != preDiffPrice.doubleValue() || !destUnitId.equals(preDestUnitId)){
            //modify by yushen更新之前的客户欠款和积分
            if (CommonUtil.isNotBlank(preDestUnitId)) {
                Unit preUnit = this.saleOrderBillDao.findUnique("from Unit where id = ? and status=1", preDestUnitId);
                Customer preCustomer = this.saleOrderBillDao.findUnique("from Customer where id = ? and status=1", preDestUnitId);
                //add by yushen 保存积分回退记录
                Long pointsBackoff = this.pointsChangeService.savePointsBackoff(saleOrderBill.getId(), preDestUnitId, this.guestService.getVipPoints(preUnit, preCustomer), Constant.ChangeRecordStatus.SaleOrderChange);
                //保存客户欠款回退记录
                this.guestValueChangeService.saveValueBackoff(saleOrderBill.getId(), preDestUnitId, this.guestService.getOwingValue(preUnit, preCustomer), Constant.ChangeRecordStatus.SaleOrderChange);
                //更新客户欠款金额和积分
                this.guestService.resetPreGust(saleOrderBill.getId(), preDiffPrice, pointsBackoff, preUnit, preCustomer);
            }

            Unit unit = this.saleOrderBillDao.findUnique("from Unit where id = ? and status=1", destUnitId);
            Customer customer = this.saleOrderBillDao.findUnique("from Customer where id = ? and status=1", destUnitId);
            //add by yushen 计算销售单积分并保存积分变动记录
            Long points = this.pointsChangeService.savePointsChange(saleOrderBill, destUnitId, this.guestService.getVipPoints(unit, customer));
            //保存客户欠款变动记录
            this.guestValueChangeService.saveValueChange(saleOrderBill.getId(), saleOrderBill.getActPrice(), saleOrderBill.getPayPrice(), destUnitId, this.guestService.getOwingValue(unit, customer), Constant.ChangeRecordStatus.SaleOrder);
            //更新客户欠款金额和积分
            this.guestService.updateCurrentGuest(saleOrderBill.getId(), diffPrice, points, unit, customer);
        }

        //保存订单
        this.saleOrderBillDao.saveOrUpdate(saleOrderBill);
        this.saleOrderBillDao.doBatchInsert(saleOrderBillDtlList);
        if (CommonUtil.isNotBlank(saleOrderBill.getBillRecordList())) {
            this.saleOrderBillDao.doBatchInsert(saleOrderBill.getBillRecordList());
        }
        if(CommonUtil.isNotBlank(list)&&list.size()>0){
            this.saleOrderBillDao.doBatchInsert(list);
        }

        //保存收银表
        PayDetail payDetail = new PayDetail();
        payDetail.setId(saleOrderBill.getBillNo()+saleOrderBill.getPayType());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        payDetail.setPayDate(df.format(new Date()));
        payDetail.setCustomerId(saleOrderBill.getDestUnitId());
        payDetail.setCustomerName(saleOrderBill.getDestUnitName());
        payDetail.setShop(saleOrderBill.getOrigUnitId());
        payDetail.setShopName(saleOrderBill.getOrigUnitName());
        payDetail.setBillNo(saleOrderBill.getBillNo());
        payDetail.setPayType(saleOrderBill.getPayType());
        payDetail.setPayPrice(saleOrderBill.getPayPrice());
        payDetail.setActPayPrice(saleOrderBill.getPayPrice());
        payDetail.setBillType("0");//销售=收款
        payDetail.setStatus("1");
        this.payDetailService.save(payDetail);
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
    public void saveweChat(SaleOrderBill saleOrderBill, List<SaleOrderBillDtl> saleOrderBillDtlList, Business business, List<Epc> epcList) throws Exception{
        //判断是否有异常唯一码
        //拼接code字符串
        String code="";
        for(SaleOrderBillDtl saleOrderBillDtl: saleOrderBillDtlList){
            if(CommonUtil.isBlank(code)){
                code+=saleOrderBillDtl.getUniqueCodes();
            }else{
                code+=","+saleOrderBillDtl.getUniqueCodes();
            }
        }
        List<AbnormalCodeMessage> list = BillConvertUtil.fullAbnormalCodeMessage(saleOrderBillDtlList,0,code);
        this.save(saleOrderBill, saleOrderBillDtlList,list);

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

        Double diffPrice = saleOrderBill.getActPrice() - saleOrderBill.getPayPrice();
        Double preDiffPrice = this.saleOrderBillDao.findUnique("select s.actPrice-s.payPrice from SaleOrderBill as s where s.billNo = ?", saleOrderBill.getBillNo());
        if(CommonUtil.isBlank(preDiffPrice)){
            preDiffPrice = 0D;
        }
        String destUnitId = saleOrderBill.getDestUnitId();
        String preDestUnitId = this.saleOrderBillDao.findUnique("select destUnitId from SaleOrderBill as s where s.billNo = ?", saleOrderBill.getBillNo());


        //add by yushen 改变客户或者客户余额变动 保存所有变动记录，更新客户欠款
        if(diffPrice.doubleValue() != preDiffPrice.doubleValue() || !destUnitId.equals(preDestUnitId)){

            Unit preUnit = this.saleOrderBillDao.findUnique("from Unit where id = ? and status=1", preDestUnitId);
            Customer preCustomer = this.saleOrderBillDao.findUnique("from Customer where id = ? and status=1", preDestUnitId);
            //add by yushen 保存积分回退记录
            Long pointsBackoff = this.pointsChangeService.savePointsBackoff(saleOrderBill.getId(), preDestUnitId, this.guestService.getVipPoints(preUnit, preCustomer), Constant.ChangeRecordStatus.SaleOrderChange);
            //保存客户欠款回退记录
            this.guestValueChangeService.saveValueBackoff(saleOrderBill.getId(), preDestUnitId, this.guestService.getOwingValue(preUnit, preCustomer), Constant.ChangeRecordStatus.SaleOrderChange);
            //更新客户欠款金额和积分
            this.guestService.resetPreGust(saleOrderBill.getId(), preDiffPrice, pointsBackoff, preUnit, preCustomer);

            Unit unit = this.saleOrderBillDao.findUnique("from Unit where id = ? and status=1", destUnitId);
            Customer customer = this.saleOrderBillDao.findUnique("from Customer where id = ? and status=1", destUnitId);
            //add by yushen 计算销售单积分并保存积分变动记录
            Long points = this.pointsChangeService.savePointsChange(saleOrderBill, destUnitId, this.guestService.getVipPoints(unit, customer));
            //保存客户欠款变动记录
            this.guestValueChangeService.saveValueChange(saleOrderBill.getId(), saleOrderBill.getActPrice(), saleOrderBill.getPayPrice(), destUnitId, this.guestService.getOwingValue(unit, customer), Constant.ChangeRecordStatus.SaleOrder);
            //更新客户欠款金额和积分
            this.guestService.updateCurrentGuest(saleOrderBill.getId(), diffPrice, points, unit, customer);
        }

        if (saleOrderBill.getTotOutQty().intValue() + saleOrderBill.getTotRetrunQty() == saleOrderBill.getTotQty().intValue()) {
            saleOrderBill.setOutStatus(BillConstant.BillInOutStatus.OutStore);
            if (saleOrderBill.getCustomerTypeId().equals(BillConstant.customerType.Customer)) {
                saleOrderBill.setStatus(BillConstant.BillStatus.End);
            }
        }
        if (saleOrderBill.getTotInQty().intValue() + saleOrderBill.getTotRetrunQty() == saleOrderBill.getTotQty().intValue()) {
            saleOrderBill.setOutStatus(BillConstant.BillInOutStatus.InStore);
            saleOrderBill.setStatus(BillConstant.BillStatus.End);
        }

        this.saleOrderBillDao.saveOrUpdate(saleOrderBill);
        this.saleOrderBillDao.doBatchInsert(saleOrderBillDtlList);
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
        String origUnitId = bill.getOrigUnitId();
        Unit unit = this.saleOrderBillDao.findUnique("from Unit where id = ? and status=1", origUnitId);
        Customer customer = this.saleOrderBillDao.findUnique("from Customer where id = ? and status=1", origUnitId);
        //add by yushen 计算销售单积分并保存积分变动记录
        Long points = this.pointsChangeService.saleOrder2ReturnPoints(bill, this.guestService.getVipPoints(unit, customer));
        //保存客户欠款变动记录
        this.guestValueChangeService.saveValueChange(bill.getId(), bill.getActPrice(), bill.getPayPrice(), origUnitId, this.guestService.getOwingValue(unit, customer), Constant.ChangeRecordStatus.SaleOrder2ReturnOrder);
        //更新客户欠款金额和积分
        this.guestService.updateCurrentGuest(bill.getId(), diffPrice, points, unit, customer);

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
        Double diffPrice = saleOrderBill.getActPrice() - saleOrderBill.getPayPrice();

        String preDestUnitId = saleOrderBill.getDestUnitId();
        Unit preUnit = this.saleOrderBillDao.findUnique("from Unit where id = ? and status=1", preDestUnitId);
        Customer preCustomer = this.saleOrderBillDao.findUnique("from Customer where id = ? and status=1", preDestUnitId);
        //add by yushen 保存积分回退记录
        Long pointsBackoff = this.pointsChangeService.savePointsBackoff(saleOrderBill.getId(), preDestUnitId, this.guestService.getVipPoints(preUnit, preCustomer), Constant.ChangeRecordStatus.SaleOrderCancel);
        //保存客户欠款回退记录
        this.guestValueChangeService.saveValueBackoff(saleOrderBill.getId(), preDestUnitId, this.guestService.getOwingValue(preUnit, preCustomer), Constant.ChangeRecordStatus.SaleOrderCancel);
        //更新客户欠款金额和积分
        this.guestService.resetPreGust(saleOrderBill.getBillNo(), diffPrice, pointsBackoff, preUnit, preCustomer);
        TransferOrderBill transferOrderBill = this.transferOrderBillService.get("srcBillNo",saleOrderBill.getId());
        if (CommonUtil.isNotBlank(transferOrderBill)){
            transferOrderBill.setStatus(BillConstant.BillStatus.Cancel);
            this.transferOrderBillService.update(transferOrderBill);
        }
        this.saleOrderBillDao.saveOrUpdate(saleOrderBill);
        PayDetail payDetail = payDetailService.get("billNo",saleOrderBill.getBillNo());
        payDetail.setStatus("0");
        payDetailService.save(payDetail);
    }

    public Epc findProductBycode(String code) {
        return this.saleOrderBillDao.findUnique("from Epc where code=?", code);
    }


    @Autowired
    private TaskService taskService;

    public MessageBox saveBusiness(SaleOrderBill saleOrderBill, List<SaleOrderBillDtl> saleOrderBillDtlList, Business business) throws Exception {
        MessageBox messageBox = this.taskService.checkEpcStock(business);
        boolean success = messageBox.getSuccess();
        if(success){
            this.saleOrderBillDao.saveOrUpdate(saleOrderBill);
            this.saleOrderBillDao.doBatchInsert(saleOrderBillDtlList);
            if (CommonUtil.isNotBlank(saleOrderBill.getBillRecordList())) {
                this.saleOrderBillDao.doBatchInsert(saleOrderBill.getBillRecordList());
            }
            this.taskService.webSave(business);
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

    public  MessageBox saveBusinessout(SaleOrderBill saleOrderBill, List<SaleOrderBillDtl> saleOrderBillDtlList, Business business, List<Epc> epcList,List<AbnormalCodeMessage> list) throws Exception {
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
            if(CommonUtil.isNotBlank(list)&&list.size()>0){
                this.saleOrderBillDao.doBatchInsert(list);
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
        List<SaleOrderBillDtl> list = this.saleOrderBillDao.find("select new SaleOrderBillDtl(t.billId,s.billDate,s.busnissName,t.styleId,t.colorId,t.sizeId,t.sku) from SaleOrderBillDtl t , SaleOrderBill s where  t.billId=s.id and s.status<>-1 and  s.status<>2 and t.outStatus<>2 and t.sku = ? ", new Object[]{sku});
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

    public List<AbnormalCodeMessage> findAbnormalCodeMessageByBillNo(String billNo){
        return this.abnormalCodeMessageDao.find("from AbnormalCodeMessage where status=1 and billNo=?",new Object[]{billNo});
    }
    /*add by czf
        *销售单转成调拨申请单
        */
    public Map<String,Object> changeTr(SaleOrderBill saleOrderBill,List<SaleOrderBillDtl> billDtlByBillNo,List<BillRecord> billRecordList, List<AbnormalCodeMessage> abnormalCodeMessageByBillNo,User user){
        Map<String,Object> map =new HashMap<String,Object>();
        Map<String, String> codeMap = new HashMap<>();
        Map<String, String> abnormalCodeMap = new HashMap<>();
        boolean issave=false;//是否能保存
        try {
            //1.判断单据状态是否是录入状态

            //List<SaleOrderBillDtl> newList=new ArrayList<SaleOrderBillDtl>();//根据code筛选后保存的list
            for (BillRecord r : billRecordList) {
                if (codeMap.containsKey(r.getSku())) {
                    String code = codeMap.get(r.getSku());
                    if(CommonUtil.isNotBlank(code)) {
                        code += "," + r.getCode();
                    }
                    codeMap.put(r.getSku(), code);
                } else {
                    codeMap.put(r.getSku(), r.getCode());
                }
            }
            for (AbnormalCodeMessage a : abnormalCodeMessageByBillNo) {
                if (abnormalCodeMap.containsKey(a.getSku())) {
                    String code = abnormalCodeMap.get(a.getSku());
                    code += "," + a.getCode();
                    abnormalCodeMap.put(a.getSku(), code);
                } else {
                    abnormalCodeMap.put(a.getSku(), a.getCode());
                }
            }
            if(saleOrderBill.getStatus().equals(BillConstant.BillStatus.Enter)){
                //2.根据详情单sku来看
                for(SaleOrderBillDtl saleOrderBillDtl:billDtlByBillNo){
                    //查询对应的sku是否有code和异常的code
                    String sku=saleOrderBillDtl.getSku();
                    String codeMaps = codeMap.get(sku);
                    String abnormalCodeMaps = abnormalCodeMap.get(sku);
                    Integer codeMapslength=0;
                    Integer abnormalCodeMapslength=0;
                    Integer lastchangeQty=0;
                    if(CommonUtil.isNotBlank(saleOrderBillDtl.getChangeTRqty())){
                        lastchangeQty=saleOrderBillDtl.getChangeTRqty();
                    }
                    if(CommonUtil.isNotBlank(codeMaps)){
                        codeMapslength=Integer.parseInt(""+codeMaps.split(",").length);
                    }
                    if(CommonUtil.isNotBlank(abnormalCodeMaps)){
                        abnormalCodeMapslength=Integer.parseInt(""+abnormalCodeMaps.split(",").length);
                    }

                    Integer changeQty=Integer.parseInt(""+saleOrderBillDtl.getQty())-codeMapslength-abnormalCodeMapslength-lastchangeQty;
                    //SaleOrderBillDtl newSaleOrderBillDtl=new SaleOrderBillDtl();
                    if(changeQty>0){
                        saleOrderBillDtl.setChangeTRqty(changeQty+lastchangeQty);
                        issave=true;
                    }else{
                        saleOrderBillDtl.setChangeTRqty(0);
                    }
                }
                //查询总部仓库
                if(issave){
                    Unit unitById = CacheManager.getUnitById("1");
                    Unit unit = CacheManager.getUnitById(unitById.getDefaultWarehId());
                    if(CommonUtil.isNotBlank(unit)&&!unit.getId().equals(saleOrderBill.getOrigId())) {
                        //转调拨申请单
                        Map<String, Object> saleChangeTrMap = BillConvertUtil.saleChangeTr(billDtlByBillNo, saleOrderBill, unit, user);
                        TransferOrderBill transferOrderBill = (TransferOrderBill) saleChangeTrMap.get("bill");
                        List<TransferOrderBillDtl> list = (List<TransferOrderBillDtl>) saleChangeTrMap.get("billDel");
                        //查出对应的EpcList
                        //List<Epc> epcList = new ArrayList<Epc>();
                        //List<BillRecord> billRecordLists = new ArrayList<>();
                        /*for (TransferOrderBillDtl transferOrderBillDtl : list) {
                            String hql = "from EpcStock epcStock where epcStock.warehouseId=? and epcStock.sku=? and epcStock.inStock=1";
                            List<EpcStock> epcStockList = this.saleOrderBillDao.find(hql, new Object[]{transferOrderBill.getOrigId(), transferOrderBillDtl.getSku()});

                            if(epcStockList.size()>0) {
                                for (int i = 0; i < transferOrderBillDtl.getQty(); i++) {
                                    Epc epc = new Epc();
                                    epc.setCode(epcStockList.get(i).getCode());
                                    epc.setStyleId(epcStockList.get(i).getStyleId());
                                    epc.setSizeId(epcStockList.get(i).getSizeId());
                                    epc.setColorId(epcStockList.get(i).getColorId());
                                    epc.setSku(epcStockList.get(i).getSku());
                                    epcList.add(epc);
                                    BillRecord billRecord = new BillRecord(transferOrderBillDtl.getBillNo() + "-" + epcStockList.get(i).getCode(), epcStockList.get(i).getCode(), transferOrderBillDtl.getBillNo(), transferOrderBillDtl.getSku());
                                    billRecordLists.add(billRecord);
                                }
                            }
                        }*/
                       /* if(epcList.size()>0){
                            //transferOrderBill.setBillRecordList(billRecordLists);
                            //由于需求销售单转调拨单不能出库屏蔽这两段代码
                            //Business business = BillConvertUtil.covertToTransferOrderBusinessOut(transferOrderBill, list, epcList, user);
                            this.transferOrderBillDao.doBatchInsert(transferOrderBill.getBillRecordList());
                            this.transferOrderBillDao.saveOrUpdate(transferOrderBill);
                            this.transferOrderBillDao.doBatchInsert(list);
                            //MessageBox messageBox = this.transferOrderBillService.saveBusiness(transferOrderBill, list, business);
                        }else{*/
                            this.transferOrderBillDao.saveOrUpdate(transferOrderBill);
                            this.transferOrderBillDao.doBatchInsert(list);
                      /*  }*/
                        this.saleOrderBillDao.doBatchInsert(billDtlByBillNo);
                        map.put("isok", true);
                        map.put("message", "转调拨申请单成功");

                    }else{
                        map.put("isok",false);
                        map.put("message","不能用总部仓库转调拨单");
                    }
                }else{
                    map.put("isok",false);
                    map.put("message","所有SKU都有唯一码");
                }
            }else{
                map.put("isok",false);
                map.put("message","单据不是录入状态");
            }
            return map;
        }catch (Exception e){
            e.printStackTrace();
            map.put("isok",false);
            map.put("message",e.getMessage());
            return map;
        }

    }

    public void deletenoOutPutCode(String billNo,String noOutPutCode) throws Exception{
        List<AbnormalCodeMessage> list = this.abnormalCodeMessageDao.find("from AbnormalCodeMessage where status=1 and code in (?) and billNo=?", new Object[]{noOutPutCode, billNo});
        for(AbnormalCodeMessage abnormalCodeMessage:list){
            abnormalCodeMessage.setStatus(0);
        }
        this.abnormalCodeMessageDao.doBatchInsert(list);
    }
}

