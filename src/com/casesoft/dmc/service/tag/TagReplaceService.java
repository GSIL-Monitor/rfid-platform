package com.casesoft.dmc.service.tag;

import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.tag.TagReplaceDao;
import com.casesoft.dmc.dao.task.TaskDao;
import com.casesoft.dmc.model.logistics.*;
import com.casesoft.dmc.model.stock.EpcStock;
import com.casesoft.dmc.model.tag.TagReplaceRecord;
import com.casesoft.dmc.model.task.BusinessDtl;
import com.casesoft.dmc.model.task.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

/**
 * Created by admin on 2017/8/31.
 */
@Service
@Transactional
public class TagReplaceService extends AbstractBaseService {

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private TagReplaceDao tagReplaceDao;

    @Override
    public Page findPage(Page page, List list) {
        return null;
    }

    @Override
    public void save(Object entity) {

    }

    @Override
    public Object load(Serializable id) {
        return null;
    }

    @Override
    public Object get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List getAll() {
        return null;
    }

    @Override
    public void update(Object entity) {

    }

    @Override
    public void delete(Object entity) {

    }

    @Override
    public void delete(Serializable id) {

    }

    @Override
    public List findAll() {
        return null;
    }

    @Override
    public List find(List list) {
        return null;
    }

    public List<BusinessDtl> findBusinessDtl(String taskId) {
        String hql = "from BusinessDtl busdtl where busdtl.taskId=?";
        return this.taskDao.find(hql, new Object[]{taskId});
    }

    public void deleteBSDtlByTaskIdAndSku(String taskId, String sku) {
        this.taskDao.batchExecute("delete from BusinessDtl where taskId=? and sku=?", taskId, sku);
    }

    public List<BusinessDtl> findBusinessDtlByTaskIds(String taskIdStr, String origSku) {
        String hql = "from BusinessDtl businessDtl where ("
                + taskIdStr + "and businessDtl.sku=" + origSku + ")";
        return this.taskDao.find(hql, new Object[]{});
    }

    public void saveBusDtlList(List<BusinessDtl> busDtlList) {
        this.taskDao.doBatchInsert(busDtlList);
    }

    public String findBillNoByTaskId(String taskId) {
        return this.taskDao.findUnique("select billNo from Business where id=?", taskId);
    }

    public <T> List<T> findOrderDtlByBillNo(String billNo, Class<T> clazz) {
        return this.taskDao.find("from " + clazz.getName() + " where billNo=?", billNo);
    }

    public <T> void deleteBillDtlByBillNoAndSku(String billNo, String sku, Class<T> clazz) {
        this.taskDao.batchExecute("delete from " + clazz.getName() + " where billNo=? and sku=?", billNo, sku);
    }

    public <T> void saveBillDtlList(List<T> billDtlList) {
        this.taskDao.doBatchInsert(billDtlList);
    }

    public void updateInBillRecord(String newCode, String newSku, String origCode) {
        this.taskDao.batchExecute("update BillRecord set code=?, sku=? where code=?", newCode, newSku, origCode);
    }

    public void updateInTaskRecord(EpcStock newEpc, String origCode) {
        String newCode = newEpc.getCode();
        String newSku = newEpc.getSku();
        String newStyleId = newEpc.getStyleId();
        String newColorId = newEpc.getColorId();
        String newSizeId = newEpc.getSizeId();
        this.taskDao.batchExecute("update Record set code=?, sku=?, styleId=?, colorId=?, sizeId=? where code=?",
                newCode, newSku, newStyleId, newColorId, newSizeId, origCode);
    }

    public void updateInEpcStock(EpcStock newEpc, String origCode) {
        this.taskDao.batchExecute("update EpcStock set id=?, code=?, styleId=?, colorId=?, sizeId=?, sku=? where code=?",
                newEpc.getCode(), newEpc.getCode(), newEpc.getStyleId(), newEpc.getColorId(), newEpc.getSizeId(), newEpc.getSku(), origCode);
    }

    public String getCustomerTypeId(String billNo) {
        return this.taskDao.findUnique("select customerTypeId from SaleOrderBill where billNo=?", billNo);
    }

    public String getCustomerType(String billNo) {
        return this.taskDao.findUnique("select customerType from SaleOrderReturnBill where billNo=?", billNo);
    }

    public String replaceTag(List<Record> origRecords, EpcStock origEpc, EpcStock newEpc) {
        String newSku = newEpc.getSku();
        String origSku = origEpc.getSku();
        //存单号和更新次数
        Map<String,Integer> billNoMap = new HashMap<>();
        if (CommonUtil.isNotBlank(origRecords)) {
            for (Record record : origRecords) {
                //反写Task_BusinessDtl中的数据s
                this.updateBSDtl(record.getTaskId(), origSku, newEpc);
                Integer count=0;
                String billNo = findBillNoByTaskId(record.getTaskId());
                if(billNoMap.containsKey(billNo)){
                    count = billNoMap.get(billNo);
                    count++;
                }else{
                    billNoMap.put(billNo,1);
                    count=1;
                }
                if (billNo.substring(0, 2).equals("PI")) {//采购单
                    this.updatePI(billNo, origSku, newEpc);

                } else if (billNo.substring(0, 2).equals("SO")) {//销售单
                  /*  loopCountOfSO++;*/
                    this.updateSO(billNo, origSku, newEpc, record.getType(), count);

                } else if (billNo.substring(0, 2).equals("SR")) {//销售退单
                  /*  loopCountOfSR++;*/
                    this.updateSR(billNo, origSku, newEpc, record.getType(), count);

                } else if (billNo.substring(0, 2).equals("TR")) {//调拨单
                   /* loopCountOfTR++;*/
                    this.updateTR(billNo, origSku, newEpc, record.getType(), count);

                } else if (billNo.substring(0, 2).equals("CM")) {//寄售单
                   /* loopCountOfCM++;*/
                    this.updateCM(billNo, origSku, newEpc, record.getType(), count);

                }
            }
            //反写BillRecord中的数据
            updateInBillRecord(newEpc.getCode(), newSku, origEpc.getCode());

            //反写Task_Record中的数据
            updateInTaskRecord(newEpc, origEpc.getCode());
        }
        //反写StockEpc中的数据
        updateInEpcStock(newEpc, origEpc.getCode());

        return "ok";
    }


    private void updateBSDtl(String taskId, String origSku, EpcStock newEpc) {
        String newSku = newEpc.getSku();
        Map<String, BusinessDtl> businessDtlMap = new HashMap<>();
        List<BusinessDtl> businessDtlList = findBusinessDtl(taskId);
        for (BusinessDtl businessDtl : businessDtlList) {
            businessDtlMap.put(businessDtl.getSku(), businessDtl);
        }

        if (CommonUtil.isNotBlank(businessDtlMap.get(newSku))) {
            businessDtlMap.get(newSku).setQty(businessDtlMap.get(newSku).getQty() + 1);
        } else {
            BusinessDtl newBusinessDtl = new BusinessDtl(
                    businessDtlMap.get(origSku).getTaskId(),
                    businessDtlMap.get(origSku).getOwnerId(),
                    businessDtlMap.get(origSku).getToken(),
                    businessDtlMap.get(origSku).getDeviceId(),
                    newSku, 1);
            newBusinessDtl.setId(new GuidCreator().toString());
            newBusinessDtl.setDestId(businessDtlMap.get(origSku).getDestId());
            newBusinessDtl.setDestUnitId(businessDtlMap.get(origSku).getDestUnitId());
            newBusinessDtl.setOrigId(businessDtlMap.get(origSku).getOrigId());
            newBusinessDtl.setOrigUnitId(businessDtlMap.get(origSku).getOrigUnitId());
            newBusinessDtl.setPreVal(businessDtlMap.get(origSku).getPreVal());
            newBusinessDtl.setStyleId(newEpc.getStyleId());
            newBusinessDtl.setColorId(newEpc.getColorId());
            newBusinessDtl.setSizeId(newEpc.getSizeId());
            newBusinessDtl.setType(businessDtlMap.get(origSku).getType());
            businessDtlMap.put(newSku, newBusinessDtl);
        }
        System.out.println(taskId);
        if (businessDtlMap.get(origSku).getQty() > 1) {
            businessDtlMap.get(origSku).setQty(businessDtlMap.get(origSku).getQty() - 1);
        } else if (businessDtlMap.get(origSku).getQty() == 1) {
            businessDtlMap.remove(origSku);
            deleteBSDtlByTaskIdAndSku(taskId, origSku);
        }
        List<BusinessDtl> newBusinessDtlList = new ArrayList<>(businessDtlMap.values());
        saveBusDtlList(newBusinessDtlList);
    }
    /**
     * @param  billNo 原始单据单号
     * @param origSku  原始sku
     * @param  newEpc 替换后的epcsku
     * */
    public void updatePI(String billNo, String origSku, EpcStock newEpc) {
        String newSku = newEpc.getSku();
        List<PurchaseOrderBillDtl> pIBillDtlList = findOrderDtlByBillNo(billNo, PurchaseOrderBillDtl.class);
        Map<String, PurchaseOrderBillDtl> pIBillDtlMap = new HashMap<>();
        for (PurchaseOrderBillDtl pIBillDtl : pIBillDtlList) {
            pIBillDtlMap.put(pIBillDtl.getSku(), pIBillDtl);
        }
        if (CommonUtil.isNotBlank(pIBillDtlMap.get(newSku))) {
            pIBillDtlMap.get(newSku).setQty(pIBillDtlMap.get(newSku).getQty() + 1);
            pIBillDtlMap.get(newSku).setActQty(pIBillDtlMap.get(newSku).getActQty() + 1);
            pIBillDtlMap.get(newSku).setInQty(pIBillDtlMap.get(newSku).getInQty() + 1);
            pIBillDtlMap.get(newSku).setPrintQty(pIBillDtlMap.get(newSku).getPrintQty() + 1);
            if (pIBillDtlMap.get(newSku).getQty().intValue() == pIBillDtlMap.get(newSku).getInQty()) {
                pIBillDtlMap.get(newSku).setInStatus(BillConstant.BillDtlStatus.InStore);
            } else if (pIBillDtlMap.get(newSku).getQty().intValue() > pIBillDtlMap.get(newSku).getInQty()) {
                pIBillDtlMap.get(newSku).setInStatus(BillConstant.BillDtlStatus.Ining);
            }
        } else {
            PurchaseOrderBillDtl pIBillDtl = new PurchaseOrderBillDtl();
            pIBillDtl.setId(new GuidCreator().toString());
            pIBillDtl.setQty(1L);
            pIBillDtl.setActQty(1L);
            pIBillDtl.setInQty(1);
            pIBillDtl.setPrintQty(1);
            pIBillDtl.setBillId(billNo);
            pIBillDtl.setBillNo(billNo);
            pIBillDtl.setRemark("tagReplace");
            pIBillDtl.setInStatus(BillConstant.BillDtlStatus.InStore);
            pIBillDtl.setStyleId(newEpc.getStyleId());
            pIBillDtl.setColorId(newEpc.getColorId());
            pIBillDtl.setSizeId(newEpc.getSizeId());
            pIBillDtl.setSku(newSku);
            pIBillDtl.setPrice(pIBillDtlMap.get(origSku).getPrice());
            pIBillDtl.setTotPrice(pIBillDtl.getPrice());
            pIBillDtl.setActPrice(pIBillDtlMap.get(origSku).getActPrice());
            pIBillDtl.setTotActPrice(pIBillDtl.getActPrice());
            pIBillDtl.setInVal(pIBillDtl.getPrice());
            pIBillDtlMap.put(newSku, pIBillDtl);
        }
        //更新采购单明细数据
        if (pIBillDtlMap.get(origSku).getQty() > 1) {
            pIBillDtlMap.get(origSku).setQty(pIBillDtlMap.get(origSku).getQty() - 1);
            pIBillDtlMap.get(origSku).setActQty(pIBillDtlMap.get(origSku).getActQty() - 1);
            if (pIBillDtlMap.get(origSku).getInQty() > 0) {
                pIBillDtlMap.get(origSku).setInQty(pIBillDtlMap.get(origSku).getInQty() - 1);
            }
            if (pIBillDtlMap.get(origSku).getPrintQty() > 0) {
                pIBillDtlMap.get(origSku).setPrintQty(pIBillDtlMap.get(origSku).getPrintQty() - 1);
            }
            if (pIBillDtlMap.get(origSku).getInQty() == 0) {
                pIBillDtlMap.get(origSku).setStatus(BillConstant.BillDtlStatus.Order);
                pIBillDtlMap.get(origSku).setInStatus(BillConstant.BillDtlStatus.Order);
            }
        } else if (pIBillDtlMap.get(origSku).getQty() == 1) {
            pIBillDtlMap.remove(origSku);
            deleteBillDtlByBillNoAndSku(billNo, origSku, PurchaseOrderBillDtl.class);
        }
        List<PurchaseOrderBillDtl> newPIBillDtlList = new ArrayList<>(pIBillDtlMap.values());
        saveBillDtlList(newPIBillDtlList);
    }
    /**
     * @param  billNo 原始单据单号
     * @param origSku  原始sku
     * @param  newEpc 替换后的epcsku
     * @param type 出入库类型
     * @param loopCountOfSO 单据更新次数 1，表示第一次更新，2，表示本单明细第二次更新
     * */
    private void updateSO(String billNo, String origSku, EpcStock newEpc, Integer type, Integer loopCountOfSO) {
        String newSku = newEpc.getSku();
        List<SaleOrderBillDtl> sOBillDtlList = findOrderDtlByBillNo(billNo, SaleOrderBillDtl.class);
        Map<String, SaleOrderBillDtl> sOBillDtlMap = new HashMap<>();
        for (SaleOrderBillDtl sOBillDtl : sOBillDtlList) {
            sOBillDtlMap.put(sOBillDtl.getSku(), sOBillDtl);
        }
        //新sku 存在时候替换逻辑
        if (CommonUtil.isNotBlank(sOBillDtlMap.get(newSku))) {
            //loopCountOfSO =1 时候本单第一次出库或者入库操作数据更新
            if (loopCountOfSO == 1) {
                sOBillDtlMap.get(newSku).setQty(sOBillDtlMap.get(newSku).getQty() + 1);
                sOBillDtlMap.get(newSku).setTotPrice(sOBillDtlMap.get(newSku).getQty() * sOBillDtlMap.get(newSku).getPrice());
                sOBillDtlMap.get(newSku).setTotActPrice(sOBillDtlMap.get(newSku).getQty() * sOBillDtlMap.get(newSku).getActPrice());
                if (CommonUtil.isNotBlank(sOBillDtlMap.get(newSku).getActQty())) {
                    sOBillDtlMap.get(newSku).setActQty(sOBillDtlMap.get(newSku).getActQty() + 1);
                }
            }
            if (type == Constant.TaskType.Outbound) {
                if (CommonUtil.isNotBlank(sOBillDtlMap.get(newSku).getOutQty())) {
                    sOBillDtlMap.get(newSku).setOutQty(sOBillDtlMap.get(newSku).getOutQty() + 1);
                } else {
                    sOBillDtlMap.get(newSku).setOutQty(1);
                }
                if (CommonUtil.isNotBlank(sOBillDtlMap.get(newSku).getOutVal())) {
                    sOBillDtlMap.get(newSku).setOutVal(sOBillDtlMap.get(newSku).getOutVal() + sOBillDtlMap.get(newSku).getPrice());
                } else {
                    sOBillDtlMap.get(newSku).setOutVal(sOBillDtlMap.get(newSku).getPrice());
                }
            } else if (type == Constant.TaskType.Inbound) {
                if (CommonUtil.isNotBlank(sOBillDtlMap.get(newSku).getInQty())) {
                    sOBillDtlMap.get(newSku).setInQty(sOBillDtlMap.get(newSku).getInQty() + 1);
                } else {
                    sOBillDtlMap.get(newSku).setInQty(1);
                }
                if (CommonUtil.isNotBlank(sOBillDtlMap.get(newSku).getInVal())) {
                    sOBillDtlMap.get(newSku).setInVal(sOBillDtlMap.get(newSku).getInVal() + sOBillDtlMap.get(newSku).getPrice());
                }
                sOBillDtlMap.get(newSku).setInVal(sOBillDtlMap.get(newSku).getPrice());
            }
            if (sOBillDtlMap.get(newSku).getQty().intValue() == sOBillDtlMap.get(newSku).getOutQty()) {
                sOBillDtlMap.get(newSku).setOutStatus(BillConstant.BillDtlStatus.OutStore);
            } else if (sOBillDtlMap.get(newSku).getQty().intValue() > sOBillDtlMap.get(newSku).getOutQty()) {
                sOBillDtlMap.get(newSku).setOutStatus(BillConstant.BillDtlStatus.Outing);
            }
            if (sOBillDtlMap.get(newSku).getQty().intValue() == sOBillDtlMap.get(newSku).getInQty()) {
                sOBillDtlMap.get(newSku).setInStatus(BillConstant.BillDtlStatus.InStore);
            } else if (sOBillDtlMap.get(newSku).getQty().intValue() > sOBillDtlMap.get(newSku).getOutQty()) {
                sOBillDtlMap.get(newSku).setInStatus(BillConstant.BillDtlStatus.Ining);
            }
        } else {
            //新SKU 不存在时候替换逻辑
            SaleOrderBillDtl sOBillDtl = new SaleOrderBillDtl();
            sOBillDtl.setId(new GuidCreator().toString());
            sOBillDtl.setQty(1L);
            sOBillDtl.setActQty(1L);
            sOBillDtl.setPrice(sOBillDtlMap.get(origSku).getPrice());
            sOBillDtl.setTotPrice(sOBillDtl.getPrice());
            sOBillDtl.setActPrice(sOBillDtlMap.get(origSku).getActPrice());
            sOBillDtl.setTotActPrice(sOBillDtl.getActPrice());
            sOBillDtl.setDiscount(sOBillDtlMap.get(origSku).getDiscount());
            sOBillDtl.setStockVal(sOBillDtlMap.get(origSku).getStockVal());
            sOBillDtl.setProfit(sOBillDtlMap.get(origSku).getProfit());
            sOBillDtl.setProfitRate(sOBillDtlMap.get(origSku).getProfitRate());
            sOBillDtl.setBillId(billNo);
            sOBillDtl.setBillNo(billNo);
            sOBillDtl.setRemark("tagReplace");
            sOBillDtl.setStyleId(newEpc.getStyleId());
            sOBillDtl.setColorId(newEpc.getColorId());
            sOBillDtl.setSizeId(newEpc.getSizeId());
            sOBillDtl.setSku(newSku);
            sOBillDtl.setOutStatus(BillConstant.BillDtlStatus.Order);
            sOBillDtl.setInStatus(BillConstant.BillDtlStatus.Order);
            if (type == Constant.TaskType.Outbound) {
                sOBillDtl.setOutQty(1);
                sOBillDtl.setOutVal(sOBillDtl.getPrice());
                sOBillDtl.setOutStatus(BillConstant.BillDtlStatus.OutStore);
            }
            if (type == Constant.TaskType.Inbound) {
                sOBillDtl.setInQty(1);
                sOBillDtl.setInVal(sOBillDtl.getPrice());
                sOBillDtl.setInStatus(BillConstant.BillDtlStatus.InStore);
            }
            sOBillDtlMap.put(newSku, sOBillDtl);
        }
        if (loopCountOfSO == 1) {
            //销售单据只有出库或者入库时候更新销售单明细数据
            if (CommonUtil.isNotBlank(sOBillDtlMap.get(origSku))) {
                Long origSkuQty = sOBillDtlMap.get(origSku).getQty();
                if (origSkuQty > 1) {
                    sOBillDtlMap.get(origSku).setQty(sOBillDtlMap.get(origSku).getQty() - 1);
                    sOBillDtlMap.get(origSku).setTotPrice(sOBillDtlMap.get(origSku).getQty() * sOBillDtlMap.get(origSku).getPrice());
                    sOBillDtlMap.get(origSku).setTotActPrice(sOBillDtlMap.get(origSku).getQty() * sOBillDtlMap.get(origSku).getActPrice());
                    if (CommonUtil.isNotBlank(sOBillDtlMap.get(origSku).getActQty())) {
                        sOBillDtlMap.get(origSku).setActQty(sOBillDtlMap.get(origSku).getActQty() - 1);
                    }
                    if (type == Constant.TaskType.Outbound) {
                        sOBillDtlMap.get(origSku).setOutQty(sOBillDtlMap.get(origSku).getOutQty() - 1);
                        sOBillDtlMap.get(origSku).setOutVal(sOBillDtlMap.get(origSku).getOutVal() - sOBillDtlMap.get(origSku).getPrice());
                    }
                    if (type == Constant.TaskType.Inbound) {
                        sOBillDtlMap.get(origSku).setInQty(sOBillDtlMap.get(origSku).getInQty() - 1);
                        sOBillDtlMap.get(origSku).setInVal(sOBillDtlMap.get(origSku).getInVal() - sOBillDtlMap.get(origSku).getPrice());
                    }
                } else if (origSkuQty == 1) {
                    sOBillDtlMap.remove(origSku);
                    deleteBillDtlByBillNoAndSku(billNo, origSku, SaleOrderBillDtl.class);
                }
            }
        }else if (loopCountOfSO == 2) {
            //销售单据有出库也有入库时候更新销售单明细数据（第二次更新单据时候逻辑）
            if (CommonUtil.isNotBlank(sOBillDtlMap.get(origSku))) {
                if (type == Constant.TaskType.Outbound) {
                    sOBillDtlMap.get(origSku).setOutQty(sOBillDtlMap.get(origSku).getOutQty() - 1);
                    sOBillDtlMap.get(origSku).setOutVal(sOBillDtlMap.get(origSku).getOutVal() - sOBillDtlMap.get(origSku).getPrice());
                }
                if (type == Constant.TaskType.Inbound) {
                    sOBillDtlMap.get(origSku).setInQty(sOBillDtlMap.get(origSku).getInQty() - 1);
                    sOBillDtlMap.get(origSku).setInVal(sOBillDtlMap.get(origSku).getInVal() - sOBillDtlMap.get(origSku).getPrice());
                }
            }
        }
        List<SaleOrderBillDtl> newSOBillDtlList = new ArrayList<>(sOBillDtlMap.values());
        saveBillDtlList(newSOBillDtlList);
    }
    /**
     * @param  billNo 原始单据单号
     * @param origSku  原始sku
     * @param  newEpc 替换后的epcsku
     * @param type 出入库类型
     * @param loopCountOfSR 单据更新次数 1，表示第一次更新，2，表示本单明细第二次更新
     * */
    private void updateSR(String billNo, String origSku, EpcStock newEpc, Integer type, Integer loopCountOfSR) {
        String newSku = newEpc.getSku();
        List<SaleOrderReturnBillDtl> sRBillDtlList = findOrderDtlByBillNo(billNo, SaleOrderReturnBillDtl.class);
        Map<String, SaleOrderReturnBillDtl> sRBillDtlMap = new HashMap<>();
        for (SaleOrderReturnBillDtl sRBillDtl : sRBillDtlList) {
            sRBillDtlMap.put(sRBillDtl.getSku(), sRBillDtl);
        }
        if (CommonUtil.isNotBlank(sRBillDtlMap.get(newSku))) {
            //原始SKU存在，做新SKU替换（同一单号只有出库或者入库）
                if (loopCountOfSR == 1) {
                    sRBillDtlMap.get(newSku).setQty(sRBillDtlMap.get(newSku).getQty() + 1);
                    sRBillDtlMap.get(newSku).setTotPrice(0 - sRBillDtlMap.get(newSku).getQty() * sRBillDtlMap.get(newSku).getPrice());
                    sRBillDtlMap.get(newSku).setTotActPrice(0 - sRBillDtlMap.get(newSku).getQty() * sRBillDtlMap.get(newSku).getActPrice());
                    if (CommonUtil.isNotBlank(sRBillDtlMap.get(newSku).getActQty())) {
                        sRBillDtlMap.get(newSku).setActQty(sRBillDtlMap.get(newSku).getActQty() + 1);
                    }
                }
                if (type == Constant.TaskType.Outbound) {
                    if (CommonUtil.isNotBlank(sRBillDtlMap.get(newSku).getOutQty())) {
                        sRBillDtlMap.get(newSku).setOutQty(sRBillDtlMap.get(newSku).getOutQty() + 1);
                    } else {
                        sRBillDtlMap.get(newSku).setOutQty(1L);
                    }
                    if (CommonUtil.isNotBlank(sRBillDtlMap.get(newSku).getOutVal())) {
                        sRBillDtlMap.get(newSku).setOutVal(sRBillDtlMap.get(newSku).getOutVal() + sRBillDtlMap.get(newSku).getPrice());
                    } else {
                        sRBillDtlMap.get(newSku).setOutVal(sRBillDtlMap.get(newSku).getPrice());
                    }
                } else if (type == Constant.TaskType.Inbound) {
                    if (CommonUtil.isNotBlank(sRBillDtlMap.get(newSku).getInQty())) {
                        sRBillDtlMap.get(newSku).setInQty(sRBillDtlMap.get(newSku).getInQty() + 1);
                    } else {
                        sRBillDtlMap.get(newSku).setInQty(1L);
                    }
                    if (CommonUtil.isNotBlank(sRBillDtlMap.get(newSku).getInVal())) {
                        sRBillDtlMap.get(newSku).setInVal(sRBillDtlMap.get(newSku).getInVal() + sRBillDtlMap.get(newSku).getPrice());
                    }
                    sRBillDtlMap.get(newSku).setInVal(sRBillDtlMap.get(newSku).getPrice());
                }
                if (sRBillDtlMap.get(newSku).getQty().intValue() == sRBillDtlMap.get(newSku).getOutQty()) {
                    sRBillDtlMap.get(newSku).setOutStatus(BillConstant.BillDtlStatus.OutStore);
                } else if (sRBillDtlMap.get(newSku).getQty().intValue() > sRBillDtlMap.get(newSku).getOutQty()) {
                    sRBillDtlMap.get(newSku).setOutStatus(BillConstant.BillDtlStatus.Outing);
                }
                if (sRBillDtlMap.get(newSku).getQty().intValue() == sRBillDtlMap.get(newSku).getInQty()) {
                    sRBillDtlMap.get(newSku).setInStatus(BillConstant.BillDtlStatus.InStore);
                } else if (sRBillDtlMap.get(newSku).getQty().intValue() > sRBillDtlMap.get(newSku).getOutQty()) {
                    sRBillDtlMap.get(newSku).setInStatus(BillConstant.BillDtlStatus.Ining);
                }

        } else {
            //新SKU 不存在时候替换逻辑
            SaleOrderReturnBillDtl sRBillDtl = new SaleOrderReturnBillDtl();
            sRBillDtl.setId(new GuidCreator().toString());
            sRBillDtl.setQty(1L);
            sRBillDtl.setActQty(1L);
            sRBillDtl.setPrice(sRBillDtlMap.get(origSku).getPrice());
            sRBillDtl.setTotPrice(0 - sRBillDtl.getPrice());
            sRBillDtl.setActPrice(sRBillDtlMap.get(origSku).getActPrice());
            sRBillDtl.setTotActPrice(0 - sRBillDtl.getActPrice());
            sRBillDtl.setDiscount(sRBillDtlMap.get(origSku).getDiscount());
            sRBillDtl.setStockVal(sRBillDtlMap.get(origSku).getStockVal());
            sRBillDtl.setBillId(billNo);
            sRBillDtl.setBillNo(billNo);
            sRBillDtl.setRemark("tagReplace");
            sRBillDtl.setStyleId(newEpc.getStyleId());
            sRBillDtl.setColorId(newEpc.getColorId());
            sRBillDtl.setSizeId(newEpc.getSizeId());
            sRBillDtl.setSku(newSku);
            sRBillDtl.setOutStatus(BillConstant.BillDtlStatus.Order);
            sRBillDtl.setInStatus(BillConstant.BillDtlStatus.Order);
            if (type == Constant.TaskType.Inbound) {
                sRBillDtl.setInQty(1L);
                sRBillDtl.setInVal(sRBillDtl.getPrice());
                sRBillDtl.setInStatus(BillConstant.BillDtlStatus.InStore);
            }
            if (type == Constant.TaskType.Outbound) {
                sRBillDtl.setOutQty(1L);
                sRBillDtl.setOutVal(sRBillDtl.getPrice());
                sRBillDtl.setOutStatus(BillConstant.BillDtlStatus.OutStore);
            }
            sRBillDtlMap.put(newSku, sRBillDtl);
        }
        if (loopCountOfSR == 1) {
            // //销售退货单据只有出库或者入库时候更新销售单明细数据
            if (CommonUtil.isNotBlank(sRBillDtlMap.get(origSku))) {
                Long origSkuQty = sRBillDtlMap.get(origSku).getQty();
                if (origSkuQty > 1) {
                    sRBillDtlMap.get(origSku).setQty(sRBillDtlMap.get(origSku).getQty() - 1);
                    sRBillDtlMap.get(origSku).setTotPrice(0 - sRBillDtlMap.get(origSku).getQty() * sRBillDtlMap.get(origSku).getPrice());
                    sRBillDtlMap.get(origSku).setTotActPrice(0 - sRBillDtlMap.get(origSku).getQty() * sRBillDtlMap.get(origSku).getActPrice());
                    if (CommonUtil.isNotBlank(sRBillDtlMap.get(origSku).getActQty())) {
                        sRBillDtlMap.get(origSku).setActQty(sRBillDtlMap.get(origSku).getActQty() - 1);
                    }
                    if (type == Constant.TaskType.Outbound) {
                        sRBillDtlMap.get(origSku).setOutQty(sRBillDtlMap.get(origSku).getOutQty() - 1);
                        sRBillDtlMap.get(origSku).setOutVal(sRBillDtlMap.get(origSku).getOutVal() - sRBillDtlMap.get(origSku).getPrice());
                    }
                    if (type == Constant.TaskType.Inbound) {
                        sRBillDtlMap.get(origSku).setInQty(sRBillDtlMap.get(origSku).getInQty() - 1);
                        sRBillDtlMap.get(origSku).setInVal(sRBillDtlMap.get(origSku).getInVal() - sRBillDtlMap.get(origSku).getPrice());
                    }
                } else if (origSkuQty == 1) {
                    sRBillDtlMap.remove(origSku);
                    deleteBillDtlByBillNoAndSku(billNo, origSku, SaleOrderReturnBillDtl.class);
                }
            }
        }else if (loopCountOfSR == 2) {
            //销售退货单据有出库也有入库时候更新销售单明细数据（第二次更新单据时候逻辑）
            if (CommonUtil.isNotBlank(sRBillDtlMap.get(origSku))) {
                if (type == Constant.TaskType.Outbound) {
                    sRBillDtlMap.get(origSku).setOutQty(sRBillDtlMap.get(origSku).getOutQty() - 1);
                    sRBillDtlMap.get(origSku).setOutVal(sRBillDtlMap.get(origSku).getOutVal() - sRBillDtlMap.get(origSku).getPrice());
                }
                if (type == Constant.TaskType.Inbound) {
                    sRBillDtlMap.get(origSku).setInQty(sRBillDtlMap.get(origSku).getInQty() - 1);
                    sRBillDtlMap.get(origSku).setInVal(sRBillDtlMap.get(origSku).getInVal() - sRBillDtlMap.get(origSku).getPrice());
                }
            }
        }
        List<SaleOrderReturnBillDtl> newSRBillDtlList = new ArrayList<>(sRBillDtlMap.values());
        saveBillDtlList(newSRBillDtlList);
    }
    /**
     * @param  billNo 原始单据单号
     * @param origSku  原始sku
     * @param  newEpc 替换后的epcsku
     * @param type 出入库类型
     * @param loopCountOfTR 单据更新次数 1，表示第一次更新，2，表示本单明细第二次更新
     * */
    private void updateTR(String billNo, String origSku, EpcStock newEpc, Integer type, Integer loopCountOfTR) {
        String newSku = newEpc.getSku();
        List<TransferOrderBillDtl> tRBillDtlList = findOrderDtlByBillNo(billNo, TransferOrderBillDtl.class);
        Map<String, TransferOrderBillDtl> tRBillDtlMap = new HashMap<>();
        for (TransferOrderBillDtl tRBillDtl : tRBillDtlList) {
            tRBillDtlMap.put(tRBillDtl.getSku(), tRBillDtl);
        }
        if (CommonUtil.isNotBlank(tRBillDtlMap.get(newSku))) {
            //原始SKU存在，做新SKU替换（同一单号只有出库或者入库）
                if (loopCountOfTR == 1) {
                    tRBillDtlMap.get(newSku).setQty(tRBillDtlMap.get(newSku).getQty() + 1);
                    tRBillDtlMap.get(newSku).setTotPrice(tRBillDtlMap.get(newSku).getQty() * tRBillDtlMap.get(newSku).getPrice());
                    if (CommonUtil.isNotBlank(tRBillDtlMap.get(newSku).getActQty())) {
                        tRBillDtlMap.get(newSku).setActQty(tRBillDtlMap.get(newSku).getActQty() + 1);
                    }
                }
                if (type == Constant.TaskType.Outbound) {
                    if (CommonUtil.isNotBlank(tRBillDtlMap.get(newSku).getOutQty())) {
                        tRBillDtlMap.get(newSku).setOutQty(tRBillDtlMap.get(newSku).getOutQty() + 1);
                    } else {
                        tRBillDtlMap.get(newSku).setOutQty(1);
                    }
                    if (CommonUtil.isNotBlank(tRBillDtlMap.get(newSku).getOutVal())) {
                        tRBillDtlMap.get(newSku).setOutVal(tRBillDtlMap.get(newSku).getOutVal() + tRBillDtlMap.get(newSku).getPrice());
                    } else {
                        tRBillDtlMap.get(newSku).setOutVal(tRBillDtlMap.get(newSku).getPrice());
                    }
                } else if (type == Constant.TaskType.Inbound) {
                    if (CommonUtil.isNotBlank(tRBillDtlMap.get(newSku).getInQty())) {
                        tRBillDtlMap.get(newSku).setInQty(tRBillDtlMap.get(newSku).getInQty() + 1);
                    } else {
                        tRBillDtlMap.get(newSku).setInQty(1);
                    }
                    if (CommonUtil.isNotBlank(tRBillDtlMap.get(newSku).getInVal())) {
                        tRBillDtlMap.get(newSku).setInVal(tRBillDtlMap.get(newSku).getInVal() + tRBillDtlMap.get(newSku).getPrice());
                    }
                    tRBillDtlMap.get(newSku).setInVal(tRBillDtlMap.get(newSku).getPrice());
                }
                if (tRBillDtlMap.get(newSku).getQty().intValue() == tRBillDtlMap.get(newSku).getOutQty()) {
                    tRBillDtlMap.get(newSku).setOutStatus(BillConstant.BillDtlStatus.OutStore);
                } else if (tRBillDtlMap.get(newSku).getQty().intValue() > tRBillDtlMap.get(newSku).getOutQty()) {
                    tRBillDtlMap.get(newSku).setOutStatus(BillConstant.BillDtlStatus.Outing);
                }
                if (tRBillDtlMap.get(newSku).getQty().intValue() == tRBillDtlMap.get(newSku).getInQty()) {
                    tRBillDtlMap.get(newSku).setInStatus(BillConstant.BillDtlStatus.InStore);
                } else if (tRBillDtlMap.get(newSku).getQty().intValue() > tRBillDtlMap.get(newSku).getOutQty()) {
                    tRBillDtlMap.get(newSku).setInStatus(BillConstant.BillDtlStatus.Ining);
                }


        } else {
            //新SKU 不存在时候替换逻辑
            TransferOrderBillDtl tRBillDtl = new TransferOrderBillDtl();
            tRBillDtl.setId(new GuidCreator().toString());
            tRBillDtl.setQty(1L);
            tRBillDtl.setActQty(1L);
            tRBillDtl.setPrice(tRBillDtlMap.get(origSku).getPrice());
            tRBillDtl.setTotPrice(tRBillDtl.getPrice());
            tRBillDtl.setDiscount(tRBillDtlMap.get(origSku).getDiscount());
            tRBillDtl.setBillId(billNo);
            tRBillDtl.setBillNo(billNo);
            tRBillDtl.setRemark("tagReplace");
            tRBillDtl.setStyleId(newEpc.getStyleId());
            tRBillDtl.setColorId(newEpc.getColorId());
            tRBillDtl.setSizeId(newEpc.getSizeId());
            tRBillDtl.setSku(newSku);
            tRBillDtl.setOutStatus(BillConstant.BillDtlStatus.OutStore);
            tRBillDtl.setInStatus(BillConstant.BillDtlStatus.InStore);
            if (type == Constant.TaskType.Inbound) {
                tRBillDtl.setInQty(1);
                tRBillDtl.setInVal(tRBillDtl.getPrice());
                tRBillDtl.setInStatus(BillConstant.BillDtlStatus.InStore);
            }
            if (type == Constant.TaskType.Outbound) {
                tRBillDtl.setInQty(1);
                tRBillDtl.setOutVal(tRBillDtl.getPrice());
                tRBillDtl.setOutStatus(BillConstant.BillDtlStatus.OutStore);
            }
            tRBillDtlMap.put(newSku, tRBillDtl);
        }
        if (loopCountOfTR == 1) {
            //更新调拨单明细
            if (CommonUtil.isNotBlank(tRBillDtlMap.get(origSku))) {
                Long origSkuQty = tRBillDtlMap.get(origSku).getQty();
                if (origSkuQty > 1) {
                    tRBillDtlMap.get(origSku).setQty(tRBillDtlMap.get(origSku).getQty() - 1);
                    tRBillDtlMap.get(origSku).setTotPrice(tRBillDtlMap.get(origSku).getQty() * tRBillDtlMap.get(origSku).getPrice());
//                tRBillDtlMap.get(origSku).setTotActPrice(tRBillDtlMap.get(origSku).getQty() * tRBillDtlMap.get(origSku).getActPrice());
                    if (CommonUtil.isNotBlank(tRBillDtlMap.get(origSku).getActQty())) {
                        tRBillDtlMap.get(origSku).setActQty(tRBillDtlMap.get(origSku).getActQty() - 1);
                    }
                    if (type == Constant.TaskType.Outbound) {
                        tRBillDtlMap.get(origSku).setOutQty(tRBillDtlMap.get(origSku).getOutQty() - 1);
                        tRBillDtlMap.get(origSku).setOutVal(tRBillDtlMap.get(origSku).getOutVal() - tRBillDtlMap.get(origSku).getPrice());
                    }
                    if (type == Constant.TaskType.Inbound) {
                        tRBillDtlMap.get(origSku).setInQty(tRBillDtlMap.get(origSku).getInQty() - 1);
                        tRBillDtlMap.get(origSku).setInVal(tRBillDtlMap.get(origSku).getInVal() - tRBillDtlMap.get(origSku).getPrice());
                    }
                } else if (origSkuQty == 1) {
                    tRBillDtlMap.remove(origSku);
                    deleteBillDtlByBillNoAndSku(billNo, origSku, TransferOrderBillDtl.class);
                }
            }
        }else if (loopCountOfTR == 2) {
            //调拨单据有出库也有入库时候更新销售单明细数据（第二次更新单据时候逻辑）
            if (CommonUtil.isNotBlank(tRBillDtlMap.get(origSku))) {
                if (type == Constant.TaskType.Outbound) {
                    tRBillDtlMap.get(origSku).setOutQty(tRBillDtlMap.get(origSku).getOutQty() - 1);
                    tRBillDtlMap.get(origSku).setOutVal(tRBillDtlMap.get(origSku).getOutVal() - tRBillDtlMap.get(newSku).getPrice());
                }
                if (type == Constant.TaskType.Inbound) {
                    tRBillDtlMap.get(origSku).setInQty(tRBillDtlMap.get(origSku).getInQty() - 1);
                    tRBillDtlMap.get(origSku).setInVal(tRBillDtlMap.get(origSku).getInVal() - tRBillDtlMap.get(newSku).getPrice());
                }
            }
        }
        List<TransferOrderBillDtl> newTRBillDtlList = new ArrayList<>(tRBillDtlMap.values());
        saveBillDtlList(newTRBillDtlList);
    }
    /**
     * @param  billNo 原始单据单号
     * @param origSku  原始sku
     * @param  newEpc 替换后的epcsku
     * @param type 出入库类型
     * @param loopCountOfCM 单据更新次数 1，表示第一次更新，2，表示本单明细第二次更新
     * */
    private void updateCM(String billNo, String origSku, EpcStock newEpc, Integer type, Integer loopCountOfCM) {
        String newSku = newEpc.getSku();
        List<ConsignmentBillDtl> cMBillDtlList = findOrderDtlByBillNo(billNo, ConsignmentBillDtl.class);
        Map<String, ConsignmentBillDtl> cMBillDtlMap = new HashMap<>();
        for (ConsignmentBillDtl cMBillDtl : cMBillDtlList) {
            cMBillDtlMap.put(cMBillDtl.getSku(), cMBillDtl);
        }
        if (CommonUtil.isNotBlank(cMBillDtlMap.get(newSku))) {
            //原始SKU存在，做新SKU替换（同一单号只有出库或者入库）
            if (loopCountOfCM == 1) {
                cMBillDtlMap.get(newSku).setQty(cMBillDtlMap.get(newSku).getQty() + 1);
                if (cMBillDtlMap.get(origSku).getQty().intValue() == cMBillDtlMap.get(origSku).getSale()) {
                    cMBillDtlMap.get(newSku).setSale(cMBillDtlMap.get(newSku).getSale() + 1);
                }
                cMBillDtlMap.get(newSku).setTotPrice(0 - cMBillDtlMap.get(newSku).getQty() * cMBillDtlMap.get(newSku).getPrice());
                cMBillDtlMap.get(newSku).setTotActPrice(0 - cMBillDtlMap.get(newSku).getQty() * cMBillDtlMap.get(newSku).getActPrice());
                if (CommonUtil.isNotBlank(cMBillDtlMap.get(newSku).getActQty())) {
                    cMBillDtlMap.get(newSku).setActQty(cMBillDtlMap.get(newSku).getActQty() + 1);
                }
            }
            if (type == Constant.TaskType.Outbound) {
                if (CommonUtil.isNotBlank(cMBillDtlMap.get(newSku).getOutQty())) {
                    cMBillDtlMap.get(newSku).setOutQty(cMBillDtlMap.get(newSku).getOutQty() + 1);
                } else {
                    cMBillDtlMap.get(newSku).setOutQty(1);
                }
                if (CommonUtil.isNotBlank(cMBillDtlMap.get(newSku).getOutVal())) {
                    cMBillDtlMap.get(newSku).setOutVal(cMBillDtlMap.get(newSku).getOutVal() + cMBillDtlMap.get(newSku).getPrice());
                } else {
                    cMBillDtlMap.get(newSku).setOutVal(cMBillDtlMap.get(newSku).getPrice());
                }
            } else if (type == Constant.TaskType.Inbound) {
                if (CommonUtil.isNotBlank(cMBillDtlMap.get(newSku).getInQty())) {
                    cMBillDtlMap.get(newSku).setInQty(cMBillDtlMap.get(newSku).getInQty() + 1);
                } else {
                    cMBillDtlMap.get(newSku).setInQty(1);
                }
                if (CommonUtil.isNotBlank(cMBillDtlMap.get(newSku).getInVal())) {
                    cMBillDtlMap.get(newSku).setInVal(cMBillDtlMap.get(newSku).getInVal() + cMBillDtlMap.get(newSku).getPrice());
                }
                cMBillDtlMap.get(newSku).setInVal(cMBillDtlMap.get(newSku).getPrice());
            }
            if (cMBillDtlMap.get(newSku).getQty().intValue() == cMBillDtlMap.get(newSku).getOutQty()) {
                cMBillDtlMap.get(newSku).setOutStatus(BillConstant.BillDtlStatus.OutStore);
            } else if (cMBillDtlMap.get(newSku).getQty().intValue() > cMBillDtlMap.get(newSku).getOutQty()) {
                cMBillDtlMap.get(newSku).setOutStatus(BillConstant.BillDtlStatus.Outing);
            }
            if (cMBillDtlMap.get(newSku).getQty().intValue() == cMBillDtlMap.get(newSku).getInQty()) {
                cMBillDtlMap.get(newSku).setInStatus(BillConstant.BillDtlStatus.InStore);
            } else if (cMBillDtlMap.get(newSku).getQty().intValue() > cMBillDtlMap.get(newSku).getOutQty()) {
                cMBillDtlMap.get(newSku).setInStatus(BillConstant.BillDtlStatus.Ining);
            }
        } else {
            //新SKU不存在时候
            ConsignmentBillDtl cMBillDtl = new ConsignmentBillDtl();
            cMBillDtl.setId(new GuidCreator().toString());
            cMBillDtl.setQty(1L);
            cMBillDtl.setActQty(1L);
            if (cMBillDtlMap.get(origSku).getQty().intValue() == cMBillDtlMap.get(origSku).getSale()) {
                cMBillDtl.setSale(1);
            }
            cMBillDtl.setPrice(cMBillDtlMap.get(origSku).getPrice());
            cMBillDtl.setTotPrice(0 - cMBillDtl.getPrice());
            cMBillDtl.setActPrice(cMBillDtlMap.get(origSku).getActPrice());
            cMBillDtl.setTotActPrice(0 - cMBillDtl.getActPrice());
            cMBillDtl.setDiscount(cMBillDtlMap.get(origSku).getDiscount());
            cMBillDtl.setStockVal(cMBillDtlMap.get(origSku).getStockVal());
            cMBillDtl.setBillId(billNo);
            cMBillDtl.setBillNo(billNo);
            cMBillDtl.setRemark("tagReplace");
            cMBillDtl.setStyleId(newEpc.getStyleId());
            cMBillDtl.setColorId(newEpc.getColorId());
            cMBillDtl.setSizeId(newEpc.getSizeId());
            cMBillDtl.setSku(newSku);
            cMBillDtl.setOutStatus(BillConstant.BillDtlStatus.Order);
            cMBillDtl.setInStatus(BillConstant.BillDtlStatus.Order);
            if (type == Constant.TaskType.Inbound) {
                cMBillDtl.setInQty(1);
                cMBillDtl.setInVal(cMBillDtl.getPrice());
                cMBillDtl.setInStatus(BillConstant.BillDtlStatus.InStore);
            }
            if (type == Constant.TaskType.Outbound) {
                cMBillDtl.setOutQty(1);
                cMBillDtl.setOutVal(cMBillDtl.getPrice());
                cMBillDtl.setOutStatus(BillConstant.BillDtlStatus.OutStore);
            }
            cMBillDtlMap.put(newSku, cMBillDtl);
        }
        if (loopCountOfCM == 1) {
            //更新寄存单明细
            Long origSkuQty = cMBillDtlMap.get(origSku).getQty();
            if (origSkuQty > 1) {
                cMBillDtlMap.get(origSku).setQty(cMBillDtlMap.get(origSku).getQty() - 1);
                cMBillDtlMap.get(origSku).setTotPrice(0 - cMBillDtlMap.get(origSku).getQty() * cMBillDtlMap.get(origSku).getPrice());
                cMBillDtlMap.get(origSku).setTotActPrice(0 - cMBillDtlMap.get(origSku).getQty() * cMBillDtlMap.get(origSku).getActPrice());
                if (cMBillDtlMap.get(origSku).getQty().intValue() == cMBillDtlMap.get(origSku).getSale()) {
                    cMBillDtlMap.get(origSku).setSale(cMBillDtlMap.get(origSku).getSale() - 1);
                }
                if (CommonUtil.isNotBlank(cMBillDtlMap.get(origSku).getActQty())) {
                    cMBillDtlMap.get(origSku).setActQty(cMBillDtlMap.get(origSku).getActQty() - 1);
                }
                if (type == Constant.TaskType.Outbound) {
                    cMBillDtlMap.get(origSku).setOutQty(cMBillDtlMap.get(origSku).getOutQty() - 1);
                    cMBillDtlMap.get(origSku).setOutVal(cMBillDtlMap.get(origSku).getOutVal() - cMBillDtlMap.get(origSku).getPrice());
                }
                if (type == Constant.TaskType.Inbound) {
                    cMBillDtlMap.get(origSku).setInQty(cMBillDtlMap.get(origSku).getInQty() - 1);
                    cMBillDtlMap.get(origSku).setInVal(cMBillDtlMap.get(origSku).getInVal() - cMBillDtlMap.get(origSku).getPrice());
                }
            } else if (origSkuQty == 1) {
                cMBillDtlMap.remove(origSku);
                deleteBillDtlByBillNoAndSku(billNo, origSku, ConsignmentBillDtl.class);
            }
        } else if (loopCountOfCM == 2) {
            //寄存单据有出库也有入库时候更新销售单明细数据（第二次更新单据时候逻辑）
            if (CommonUtil.isNotBlank(cMBillDtlMap.get(origSku))) {
                if (type == Constant.TaskType.Outbound) {
                    cMBillDtlMap.get(origSku).setOutQty(cMBillDtlMap.get(origSku).getOutQty() - 1);
                    cMBillDtlMap.get(origSku).setOutVal(cMBillDtlMap.get(origSku).getOutVal() - cMBillDtlMap.get(origSku).getPrice());
                }
                if (type == Constant.TaskType.Inbound) {
                    cMBillDtlMap.get(origSku).setInQty(cMBillDtlMap.get(origSku).getInQty() - 1);
                    cMBillDtlMap.get(origSku).setInVal(cMBillDtlMap.get(origSku).getInVal() - cMBillDtlMap.get(origSku).getPrice());
                }
            }
        }
        List<ConsignmentBillDtl> newCMBillDtlList = new ArrayList<>(cMBillDtlMap.values());
        saveBillDtlList(newCMBillDtlList);
    }

    public void saveReplaceRecord(EpcStock origEpc, EpcStock newEpc, String msg, String id) {
        TagReplaceRecord record = new TagReplaceRecord();
        record.setId(new GuidCreator().toString());
        record.setStatus(0);
        if("ok".equals(msg)){
            record.setStatus(1);
            record.setRemark("替换成功");
        }
        record.setRecordDate(new Date());
        record.setOrigCode(origEpc.getCode());
        record.setOrigSku(origEpc.getSku());
        record.setOrigStyleId(origEpc.getStyleId());
        record.setOrigColorId(origEpc.getColorId());
        record.setOrigSizeId(origEpc.getSizeId());
        record.setNewCode(newEpc.getCode());
        record.setNewSku(newEpc.getSku());
        record.setNewStyleId(newEpc.getStyleId());
        record.setNewColorId(newEpc.getColorId());
        record.setNewSizeId(newEpc.getSizeId());
        record.setOprId(id);
        this.tagReplaceDao.saveOrUpdate(record);
    }

    public Page<TagReplaceRecord> findRecordPage(Page<TagReplaceRecord> page, List<PropertyFilter> filters) {
        return this.tagReplaceDao.findPage(page, filters);
    }
}
