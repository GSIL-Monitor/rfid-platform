package com.casesoft.dmc.service.logistics;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.logistics.BillConvertUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.logistics.*;
import com.casesoft.dmc.dao.sys.UnitDao;
import com.casesoft.dmc.model.logistics.*;
import com.casesoft.dmc.model.logistics.vo.PurchaseStyleVo;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.stock.CodeFirstTime;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.model.tag.Epc;
import com.casesoft.dmc.model.tag.Init;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.Record;
import com.casesoft.dmc.service.task.TaskService;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by CaseSoft-Software on 2017-06-13.
 */
@Service
@Transactional
public class PurchaseOrderBillService implements IBaseService<PurchaseOrderBill, String> {
    @Autowired
    private PurchaseBillOrderDao purchaseBillOrderDao;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UnitDao unitDao;
    @Autowired
    private MonthAccountStatementDao monthAccountStatementDao;
    @Autowired
    private ReplenishBillDtlDao replenishBillDtlDao;
    @Autowired
    private ReplenishBillDao replenishBillDao;
    @Autowired
    private ChangeReplenishBillDtlDao changeReplenishBillDtlDao;

    @Override
    public Page<PurchaseOrderBill> findPage(Page<PurchaseOrderBill> page, List<PropertyFilter> filters) {
        return this.purchaseBillOrderDao.findPage(page, filters);
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

    public List<PurchaseOrderBill>find(String billNo){
        return this.purchaseBillOrderDao.find("from PurchaseOrderBill where billNo=?", new Object[]{billNo});
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
        if (CommonUtil.isBlank(actPrice)) {
            actPrice = 0.0;
        }
        Double payPrice = entity.getPayPrice();
        if (CommonUtil.isBlank(payPrice)) {
            payPrice = 0.0;
        }
        Double owingValue = unit.getOwingValue() == null ? 0.0 : unit.getOwingValue();
        unit.setOwingValue(owingValue - (actPrice - payPrice));
        this.unitDao.update(unit);
        this.purchaseBillOrderDao.saveOrUpdate(entity);
        //判断是否有补货单的数据
        String hql = "from ChangeReplenishBillDtl t where t.purchaseNo=?";
        List<ChangeReplenishBillDtl> changeReplenishBillDtls = this.purchaseBillOrderDao.find(hql, new Object[]{entity.getBillNo()});
        if (CommonUtil.isNotBlank(changeReplenishBillDtls)) {
            if (changeReplenishBillDtls.size() != 0) {
                for (int i = 0; i < changeReplenishBillDtls.size(); i++) {
                    String hqlpurchaseDelt = " from ReplenishBillDtl t where t.billId=? and t.sku=?";
                    ReplenishBillDtl replenishBillDtl = this.replenishBillDtlDao.findUnique(hqlpurchaseDelt, new Object[]{changeReplenishBillDtls.get(i).getReplenishNo(), changeReplenishBillDtls.get(i).getSku()});
                    Integer actConvertQty = replenishBillDtl.getActConvertQty();
                    replenishBillDtl.setActConvertQty(actConvertQty - Integer.parseInt(changeReplenishBillDtls.get(i).getQty()));
                    if (replenishBillDtl.getQty() > replenishBillDtl.getActConvertQty()) {
                        replenishBillDtl.setStatus(1);
                    } else {
                        replenishBillDtl.setStatus(0);
                    }
                    this.replenishBillDtlDao.update(replenishBillDtl);
                }
            }
        }


    }

    public void cancelApply(PurchaseOrderBill entity, MonthAccountStatement monthAccountStatement) {
        String destId = entity.getDestUnitId();
        Unit unit = this.unitDao.get(destId);
        Double actPrice = entity.getActPrice();
        if (CommonUtil.isBlank(actPrice)) {
            actPrice = 0.0;
        }
        Double payPrice = entity.getPayPrice();
        if (CommonUtil.isBlank(payPrice)) {
            payPrice = 0.0;
        }
        Double owingValue = unit.getOwingValue() == null ? 0.0 : unit.getOwingValue();
        unit.setOwingValue(owingValue - (actPrice - payPrice));
        monthAccountStatement.setTotVal(monthAccountStatement.getTotVal() - (actPrice - payPrice));
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
        this.purchaseBillOrderDao.batchExecute("delete from PurchaseOrderBillDtl where billNo=?", purchaseOrderBill.getBillNo());
        this.purchaseBillOrderDao.batchExecute("delete from BillRecord where billNo=?", purchaseOrderBill.getBillNo());
        String hql = "select billNo from PurchaseOrderBill where billNo=?";
        // PurchaseOrderBill purchaseOrderBill1 = this.purchaseBillOrderDao.get(purchaseOrderBill.getBillNo());
        String billNo = this.purchaseBillOrderDao.findUnique(hql, new Object[]{purchaseOrderBill.getBillNo()});
        if (CommonUtil.isBlank(billNo)) {
            Double actPrice = purchaseOrderBill.getActPrice();
            Double payPrice = purchaseOrderBill.getPayPrice();
            if (CommonUtil.isBlank(actPrice)) {
                actPrice = 0.0;
            }
            if (CommonUtil.isBlank(payPrice)) {
                payPrice = 0.0;
            }
            Unit unit = this.unitDao.get(purchaseOrderBill.getOrigUnitId());
            Double owingValue = unit.getOwingValue();
            unit.setOwingValue(owingValue + (actPrice - payPrice));
            this.unitDao.update(unit);
        }
        this.purchaseBillOrderDao.saveOrUpdate(purchaseOrderBill);
        this.purchaseBillOrderDao.doBatchInsert(purchaseOrderBillDtlList);
        if (CommonUtil.isNotBlank(purchaseOrderBill.getBillRecordList())) {
            this.purchaseBillOrderDao.doBatchInsert(purchaseOrderBill.getBillRecordList());
        }
    }

    /**
     * 筛选purchaseOrderBillDtlList中的数据
     *
     * @param purchaseOrderBillDtlList
     * @param replenishBillNo
     * @return
     */
    public List<PurchaseOrderBillDtl> filtrateMessage(List<PurchaseOrderBillDtl> purchaseOrderBillDtlList, String replenishBillNo) throws ParseException {
        if (purchaseOrderBillDtlList.size() != 0) {
            for (int i = 0; i < purchaseOrderBillDtlList.size(); ) {
                PurchaseOrderBillDtl purchaseOrderBillDtl = purchaseOrderBillDtlList.get(i);
                String hql = "from ReplenishBillDtl t where t.sku=? and t.billId=?";
                ReplenishBillDtl unique = this.replenishBillDtlDao.findUnique(hql, new Object[]{purchaseOrderBillDtl.getSku(), replenishBillNo});
                /*if (unique.getQty() <= unique.getActConvertQty()) {
                    if(purchaseOrderBillDtl.getQty()>(unique.getQty()-unique.getActConvertQty())){
                        purchaseOrderBillDtlList.remove(i);
                        String changehql = "from ChangeReplenishBillDtl t where t.ReplenishNo=? and t.sku=?";
                        ChangeReplenishBillDtl changeReplenishBillDtl = this.replenishBillDtlDao.findUnique(changehql, new Object[]{replenishBillNo,purchaseOrderBillDtl.getSku()});
                        changeReplenishBillDtl.setExpectTime(CommonUtil.converStrToDate(purchaseOrderBillDtl.getExpectTime(), "yyyy-MM-dd"));
                        this.changeReplenishBillDtlDao.update(changeReplenishBillDtl);
                    }else{
                        i++;
                    }

                }else{
                    i++;
                }*/
                if (purchaseOrderBillDtl.getQty() == 0 && unique.getActConvertQty() != 0) {
                    purchaseOrderBillDtlList.remove(i);
                    String changehql = "from ChangeReplenishBillDtl t where t.ReplenishNo=? and t.sku=?";
                    ChangeReplenishBillDtl changeReplenishBillDtl = this.replenishBillDtlDao.findUnique(changehql, new Object[]{replenishBillNo, purchaseOrderBillDtl.getSku()});
                    changeReplenishBillDtl.setExpectTime(purchaseOrderBillDtl.getExpectTime());
                    this.changeReplenishBillDtlDao.update(changeReplenishBillDtl);
                } else {
                    i++;
                }
            }
        }
        return purchaseOrderBillDtlList;
    }


    //modify by yushen 同时反写补货单明细的数据。
    public void processReplenishBill(PurchaseOrderBill purchaseOrderBill, List<PurchaseOrderBillDtl> purchaseOrderBillDtlList, ReplenishBill replenishBill, List<ReplenishBillDtl> replenishBillDtl, User curUser) {

        if (purchaseOrderBill.getId() != null) {
            purchaseOrderBill.setSrcBillNo(replenishBill.getBillNo());
            this.purchaseBillOrderDao.saveOrUpdate(purchaseOrderBill);
            this.purchaseBillOrderDao.doBatchInsert(purchaseOrderBillDtlList);
            if (CommonUtil.isNotBlank(purchaseOrderBill.getBillRecordList())) {
                this.purchaseBillOrderDao.doBatchInsert(purchaseOrderBill.getBillRecordList());
            }
        }

        this.replenishBillDao.saveOrUpdate(replenishBill);
        this.replenishBillDao.doBatchInsert(replenishBillDtl);
    }

    public List<PurchaseOrderBillDtl> findBillDtlByBillNo(String billNo) {
        return this.purchaseBillOrderDao.find("from PurchaseOrderBillDtl where billNo=?", new Object[]{billNo});
    }


    /**
     * 采购单按款搜索去重得到billNo
     *
     * @param styleSearch
     */
    public List<String> findBillDtlBillNoByStyleId(String styleSearch) {
        String hql = "select distinct billNo "
                + "from PurchaseOrderBillDtl  where styleId like '%" + styleSearch + "%'";
        return this.purchaseBillOrderDao.find(hql);
    }


    public List<PurchaseStyleVo> findStyleListByBillNo(String billNO) {
        String hql = "select distinct new com.casesoft.dmc.model.logistics.vo.PurchaseStyleVo"
                + "(dtl.styleId) from PurchaseOrderBillDtl dtl where dtl.billNo = ? ";
        return this.purchaseBillOrderDao.find(hql, billNO);
    }

    //采购数量详情
    public List<PurchaseBystyleid> findBillDtlByStyleId(String styleId, String sbillDate, String ebillDate) {
        String hql = "SELECT new com.casesoft.dmc.model.logistics.PurchaseBystyleid" +
                "(bill.billDate,bill.buyahandId,SUM(dtl.qty) as totQty) " +
                "FROM PurchaseOrderBillDtl dtl,PurchaseOrderBill bill " +
                "WHERE dtl.billNo=bill.billNo " +
                "AND dtl.styleId=? " +
                "AND bill.status <> -1  " +
                "And bill.billDate >= To_date(?,'yyyy-mm-dd HH24:mi:ss') AND bill.billDate <= To_date(?,'yyyy-mm-dd HH24:mi:ss') " +
                "GROUP BY bill.billDate,bill.buyahandId " +
                "ORDER BY bill.billDate asc ";
        return this.purchaseBillOrderDao.find(hql, new Object[]{styleId, sbillDate, ebillDate});
    }

    //采购总数
    public List<PurchaseBystyleid> findPurchaseTotByStyleId(String styleId, String sbillDate, String ebillDate) {
        String hql = "SELECT new com.casesoft.dmc.model.logistics.PurchaseBystyleid(dtl.styleId,SUM(dtl.qty) as totQty) " +
                "FROM PurchaseOrderBillDtl dtl,PurchaseOrderBill bill " +
                "WHERE dtl.billNo=bill.billNo " +
                "AND dtl.styleId=? " +
                "AND bill.status <> -1 " +
                "And bill.billDate >= To_date(?,'yyyy-mm-dd HH24:mi:ss') AND bill.billDate <= To_date(?,'yyyy-mm-dd HH24:mi:ss') " +
                "GROUP BY dtl.styleId";
        return this.purchaseBillOrderDao.find(hql, new Object[]{styleId, sbillDate, ebillDate});
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
        for (Object o : objectList) {
            JSONArray jsonArray = JSONArray.fromObject(o);
            Epc epc = new Epc("" + jsonArray.get(0), "" + jsonArray.get(1), "" + jsonArray.get(2), "" + jsonArray.get(3),
                    "" + jsonArray.get(4), "" + jsonArray.get(5), "" + jsonArray.get(6));
            epcList.add(epc);
        }
        return epcList;
    }

    public void saveBusiness(PurchaseOrderBill purchaseOrderBill, List<PurchaseOrderBillDtl> purchaseOrderBillDtlList, Business business) {
        List<Style> styleList = new ArrayList<>();
        for (PurchaseOrderBillDtl dtl : purchaseOrderBillDtlList) {
            if (dtl.getStatus() == BillConstant.BillDtlStatus.InStore && dtl.getInStockType().equals(BillConstant.InStockType.NewStyle)) {
                Style s = CacheManager.getStyleById(dtl.getStyleId());
                s.setClass6(BillConstant.InStockType.BackOrder);
                styleList.add(s);
            }
        }
        this.purchaseBillOrderDao.saveOrUpdate(purchaseOrderBill);
        this.purchaseBillOrderDao.doBatchInsert(purchaseOrderBillDtlList);
        if (business.getType().equals(Constant.TaskType.Inbound)) {
            List<Record> recordList = business.getRecordList();
            ArrayList<CodeFirstTime> list = new ArrayList<CodeFirstTime>();
            for (Record r : recordList) {
                CodeFirstTime codeFirstTime = this.purchaseBillOrderDao.findUnique("from CodeFirstTime where code=? and warehouseId=?", new Object[]{r.getCode(), purchaseOrderBill.getDestId()});
                BillConvertUtil.setEpcStockPrice(codeFirstTime, r, list, purchaseOrderBill.getDestId());
            }
            if (list.size() != 0) {
                this.purchaseBillOrderDao.doBatchInsert(list);
            }
        }
        this.taskService.webSave(business);
        if (styleList.size() > 0) {
            this.purchaseBillOrderDao.doBatchInsert(styleList);
        }
        if (CommonUtil.isNotBlank(purchaseOrderBill.getBillRecordList())) {
            this.purchaseBillOrderDao.doBatchInsert(purchaseOrderBill.getBillRecordList());
        }
    }

    public Object findpurchaseCount(String hql) {
        Object unique = this.purchaseBillOrderDao.findUnique(hql);
        return unique;

    }

    public List<Object> findpurchaseCounts(String hql) {
        List<Object> objects = this.purchaseBillOrderDao.find(hql);
        return objects;
    }

    public Double findpurchaseCountMon(String hql) {
        Double unique = this.purchaseBillOrderDao.findUnique(hql);
        return unique;
    }

    public Integer findBillStatus(String billNo) {
        return this.purchaseBillOrderDao.findUnique("select status from PurchaseOrderBill where id = ?", billNo);
    }
}
