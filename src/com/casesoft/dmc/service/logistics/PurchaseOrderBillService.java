package com.casesoft.dmc.service.logistics;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.logistics.BillConvertUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.logistics.MonthAccountStatementDao;
import com.casesoft.dmc.dao.logistics.PurchaseBillOrderDao;
import com.casesoft.dmc.dao.sys.UnitDao;
import com.casesoft.dmc.model.logistics.BillConstant;
import com.casesoft.dmc.model.logistics.MonthAccountStatement;
import com.casesoft.dmc.model.logistics.PurchaseOrderBill;
import com.casesoft.dmc.model.logistics.PurchaseOrderBillDtl;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.stock.CodeFirstTime;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.tag.Epc;
import com.casesoft.dmc.model.tag.Init;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.Record;
import com.casesoft.dmc.service.task.TaskService;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CaseSoft-Software on 2017-06-13.
 */
@Service
@Transactional
public class PurchaseOrderBillService implements IBaseService<PurchaseOrderBill,String> {
    @Autowired
    private PurchaseBillOrderDao purchaseBillOrderDao;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UnitDao unitDao;
    @Autowired
    private MonthAccountStatementDao monthAccountStatementDao;
    @Override
    public Page<PurchaseOrderBill> findPage(Page<PurchaseOrderBill> page, List<PropertyFilter> filters) {
        return this.purchaseBillOrderDao.findPage(page,filters);
    }

    @Override
    public void save(PurchaseOrderBill entity) {
        this.purchaseBillOrderDao.save(entity);
    }

    @Override
    public PurchaseOrderBill load(String id) {
        return this.purchaseBillOrderDao.load(id);
    }

    @Override
    public PurchaseOrderBill get(String propertyName, Object value) {
        return this.purchaseBillOrderDao.findUniqueBy(propertyName, value);
    }

    @Override
    public List<PurchaseOrderBill> find(List<PropertyFilter> filters) {
        return this.purchaseBillOrderDao.find(filters);
    }

    @Override
    public List<PurchaseOrderBill> getAll() {
        return this.purchaseBillOrderDao.getAll();
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(PurchaseOrderBill entity) {
        this.purchaseBillOrderDao.saveOrUpdate(entity);
    }
    public void cancel(PurchaseOrderBill entity) {
        String destId = entity.getDestUnitId();
        Unit unit = this.unitDao.get(destId);
        Double actPrice = entity.getActPrice();
        if(CommonUtil.isBlank(actPrice)){
            actPrice=0.0;
        }
        Double payPrice = entity.getPayPrice();
        if(CommonUtil.isBlank(payPrice)){
            payPrice=0.0;
        }
        Double owingValue = unit.getOwingValue()==null?0.0:unit.getOwingValue();
        unit.setOwingValue(owingValue-(actPrice-payPrice));
        this.unitDao.update(unit);
        this.purchaseBillOrderDao.saveOrUpdate(entity);
    }

    public void cancelApply(PurchaseOrderBill entity,MonthAccountStatement monthAccountStatement) {
        String destId = entity.getDestUnitId();
        Unit unit = this.unitDao.get(destId);
        Double actPrice = entity.getActPrice();
        if(CommonUtil.isBlank(actPrice)){
            actPrice=0.0;
        }
        Double payPrice = entity.getPayPrice();
        if(CommonUtil.isBlank(payPrice)){
            payPrice=0.0;
        }
        Double owingValue = unit.getOwingValue()==null?0.0:unit.getOwingValue();
        unit.setOwingValue(owingValue-(actPrice-payPrice));
        monthAccountStatement.setTotVal(monthAccountStatement.getTotVal()-(actPrice-payPrice));
        this.unitDao.update(unit);
        this.purchaseBillOrderDao.saveOrUpdate(entity);
        this.monthAccountStatementDao.saveOrUpdate(monthAccountStatement);
    }

    @Override
    public void delete(PurchaseOrderBill entity) {
        this.purchaseBillOrderDao.delete(entity);
    }

    @Override
    public void delete(String id) {
        this.purchaseBillOrderDao.delete(id);
    }

    public String findMaxBillNo(String prefix) {
        String hql = "select max(CAST(SUBSTRING(t.billNo,9),integer))"
                + " from PurchaseOrderBill as t where t.billNo like ?";
        Integer code = this.purchaseBillOrderDao.findUnique(hql, prefix + '%');
        return code == null ? (prefix + "001") : prefix + CommonUtil.convertIntToString(code + 1, 3);
    }

    public void save(PurchaseOrderBill purchaseOrderBill, List<PurchaseOrderBillDtl> purchaseOrderBillDtlList) {
        PurchaseOrderBill purchaseOrderBill1 = this.purchaseBillOrderDao.get(purchaseOrderBill.getBillNo());
        if(CommonUtil.isBlank(purchaseOrderBill1)){
            Double actPrice = purchaseOrderBill.getActPrice();
            Double payPrice = purchaseOrderBill.getPayPrice();
            if(CommonUtil.isBlank(actPrice)){
                actPrice=0.0;
            }
            if(CommonUtil.isBlank(payPrice)){
                payPrice=0.0;
            }
            Unit unit = this.unitDao.get(purchaseOrderBill.getOrigUnitId());
            Double owingValue = unit.getOwingValue();
            unit.setOwingValue(owingValue+(actPrice-payPrice));
            this.unitDao.update(unit);
        }
        this.purchaseBillOrderDao.saveOrUpdate(purchaseOrderBill);
        this.purchaseBillOrderDao.doBatchInsert(purchaseOrderBillDtlList);
        if(CommonUtil.isNotBlank(purchaseOrderBill.getBillRecordList())){
            this.purchaseBillOrderDao.doBatchInsert(purchaseOrderBill.getBillRecordList());
        }
    }

    public List<PurchaseOrderBillDtl> findBillDtlByBillNo(String billNo) {
        return this.purchaseBillOrderDao.find("from PurchaseOrderBillDtl where billNo=?",new Object[]{billNo});
    }

    public void saveBirthTag(Init master, List<PurchaseOrderBillDtl> purchaseOrderBillDtlList) {
        this.purchaseBillOrderDao.saveOrUpdateX(master);
        this.purchaseBillOrderDao.doBatchInsert(master.getDtlList());
        this.purchaseBillOrderDao.doBatchInsert(purchaseOrderBillDtlList);
    }

    public List<Epc> findNotInEpc(String billNo) {
        String sql = "select  e.code,e.billNo ,e.sku,e.epc,e.styleId,e.colorId,e.sizeId" +
                " from TAG_INIT i, TAG_EPC e left join  STOCK_EPCSTOCK s on e.code = s.id " +
                "where i.billNo = e.billNo  and i.remark=? and s.id is null";
        List<Object> objectList = this.purchaseBillOrderDao.findBySQL(sql, billNo);
        List<Epc> epcList = new ArrayList<>();
        for(Object o : objectList){
            JSONArray jsonArray = JSONArray.fromObject(o);
            Epc epc = new Epc(""+jsonArray.get(0),""+jsonArray.get(1),""+jsonArray.get(2),""+jsonArray.get(3),
                    ""+jsonArray.get(4),""+jsonArray.get(5),""+jsonArray.get(6));
            epcList.add(epc);
        }
        return  epcList;
    }

    public void saveBusiness(PurchaseOrderBill purchaseOrderBill, List<PurchaseOrderBillDtl> purchaseOrderBillDtlList, Business business) {
        List<Style> styleList = new ArrayList<>();
        for(PurchaseOrderBillDtl dtl : purchaseOrderBillDtlList){
            if(dtl.getStatus() == BillConstant.BillDtlStatus.InStore && dtl.getInStockType().equals(BillConstant.InStockType.NewStyle)){
                Style s = CacheManager.getStyleById(dtl.getStyleId());
                s.setClass6(BillConstant.InStockType.BackOrder);
                styleList.add(s);
            }
        }
        this.purchaseBillOrderDao.saveOrUpdate(purchaseOrderBill);
        this.purchaseBillOrderDao.doBatchInsert(purchaseOrderBillDtlList);
        if(business.getType().equals(Constant.TaskType.Inbound)) {
            List<Record> recordList = business.getRecordList();
            ArrayList<CodeFirstTime> list = new ArrayList<CodeFirstTime>();
            for (Record r : recordList) {
                CodeFirstTime codeFirstTime = this.purchaseBillOrderDao.findUnique("from CodeFirstTime where code=? and warehouseId=?", new Object[]{r.getCode(), purchaseOrderBill.getDestId()});
                BillConvertUtil.setEpcStockPrice(codeFirstTime,r,list,purchaseOrderBill.getDestId());
            }
            if (list.size() != 0) {
                this.purchaseBillOrderDao.doBatchInsert(list);
            }
        }
        this.taskService.save(business);
        if(styleList.size() > 0){
            this.purchaseBillOrderDao.doBatchInsert(styleList);
        }
    }

    public Long findpurchaseCount(String hql){
        Long unique = this.purchaseBillOrderDao.findUnique(hql);
        return  unique;
    }
    public List<Object> findpurchaseCounts(String hql){
        List<Object> objects = this.purchaseBillOrderDao.find(hql);
        return  objects;
    }
    public Double findpurchaseCountMon(String hql){
        Double unique = this.purchaseBillOrderDao.findUnique(hql);
        return  unique;
    }
}
