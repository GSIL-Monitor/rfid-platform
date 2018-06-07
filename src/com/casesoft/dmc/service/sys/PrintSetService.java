package com.casesoft.dmc.service.sys;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.sys.PrintSetDao;
import com.casesoft.dmc.model.logistics.*;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.sys.PrintSet;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.logistics.*;
import com.casesoft.dmc.service.sys.impl.UserService;
import org.codehaus.jackson.mrbean.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by Administrator on 2018/5/31.
 */
@Service
@Transactional
public class PrintSetService implements IBaseService<PrintSet,String> {
    @Autowired
    private PrintSetDao printSetDao;
    @Autowired
    private SaleOrderBillService saleOrderBillService;

    @Autowired
    private PurchaseOrderBillService purchaseOrderBillService;

    @Autowired
    private PurchaseReturnBillService purchaseReturnBillService;

    @Autowired
    private SaleOrderReturnBillService saleOrderReturnBillService;

    @Autowired
    private TransferOrderBillService transferOrderBillService;

    @Autowired
    private UserService userService;

    @Autowired
    private GuestViewService guestViewService;
    @Override
    public Page<PrintSet> findPage(Page<PrintSet> page, List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public void save(PrintSet entity) {

    }

    @Override
    public PrintSet load(String id) {
        return null;
    }

    @Override
    public PrintSet get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<PrintSet> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<PrintSet> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(PrintSet entity) {

    }

    @Override
    public void delete(PrintSet entity) {

    }

    @Override
    public void delete(String id) {

    }

    public void savePrintSetMessage( PrintSet entity){

        this.printSetDao.saveOrUpdate(entity);
    }

    public PrintSet findPrintSet(String ruleReceipt,String type,String ownerId){
        String hql="from PrintSet t where t.ownerId=? and t.type=? and t.ruleReceipt=?";
        PrintSet printSet = this.printSetDao.findUnique(hql, new Object[]{ownerId, type, ruleReceipt});
        return printSet;
    }

    public List<PrintSet> findPrintSetListByOwnerId(String ownerId,String type,String ruleReceipt){
        List<PrintSet> printSets;
        String hql="from PrintSet t where t.ownerId=? and type=?";
        if(CommonUtil.isNotBlank(ruleReceipt)){
            if(ruleReceipt.equals("A4")){
                hql+=" and ruleReceipt=?";
                printSets = this.printSetDao.find(hql, new Object[]{ownerId,type,ruleReceipt});
            }else{
                hql+=" and ruleReceipt<>'A4'";
                printSets = this.printSetDao.find(hql, new Object[]{ownerId,type});
            }

        }else{
            printSets = this.printSetDao.find(hql, new Object[]{ownerId,type});
        }

        return printSets;
    }
    public List<PrintSet> findPrintSetListByOwnerIdA4(String type,String ruleReceipt){
        List<PrintSet> printSets;
        String hql="from PrintSet t where ruleReceipt=? and type=?";
        printSets= this.printSetDao.find(hql, new Object[]{ruleReceipt,type});
        for(int i=0;i<printSets.size();i++){
            if(i>1){
                printSets.remove(i);
            }
        }
        return printSets;
    }

    public Map<String,Object> printMessage(String id,String billno){
        Map<String,Object> map=new HashMap<String,Object>();
        if(billno.indexOf(BillConstant.BillPrefix.saleOrder)!=-1){
            Map<String,Object> mapcont=new HashMap<String,Object>();
            String hql="from PrintSet t where t.id=?";
            PrintSet printSet = this.printSetDao.findUnique(hql, new Object[]{Long.parseLong(id)});
            SaleOrderBill saleOrderBill = this.saleOrderBillService.load(billno);
            mapcont.put("storeName","Ancient Stone");
            mapcont.put("billType","销售单");
            mapcont.put("billNo",billno);
            User user = CacheManager.getUserById(saleOrderBill.getOprId());
            mapcont.put("makeBill",user.getName());
            mapcont.put("billDate", CommonUtil.getDateString(saleOrderBill.getBillDate(),"yyyy-MM-dd"));
            mapcont.put("coustmer",saleOrderBill.getDestUnitName());
            mapcont.put("remark",saleOrderBill.getRemark());
            mapcont.put("businessId",saleOrderBill.getBusnissName());
            mapcont.put("printTime",CommonUtil.getDateString(new Date(),"yyyy-MM-dd HH:mm:ss"));
            if(CommonUtil.isNotBlank(saleOrderBill.getAfterBalance())){
                mapcont.put("shopAfter",saleOrderBill.getAfterBalance());
            }else{
                mapcont.put("shopAfter","");
            }
            if(CommonUtil.isNotBlank(saleOrderBill.getPreBalance())){
                mapcont.put("shopBefore",saleOrderBill.getPreBalance());
            }else{
                mapcont.put("shopBefore","");
            }
            List<SaleOrderBillDtl> billDtlByBillNo = this.saleOrderBillService.findBillDtlByBillNo(billno);
            map.put("print",printSet);
            map.put("cont",mapcont);
            map.put("contDel",billDtlByBillNo);
        }
        if(billno.indexOf(BillConstant.BillPrefix.purchase)!=-1){
            Map<String,Object> mapcont=new HashMap<String,Object>();
            String hql="from PrintSet t where t.id=?";
            PrintSet printSet = this.printSetDao.findUnique(hql, new Object[]{Long.parseLong(id)});
            PurchaseOrderBill purchaseOrderBill = this.purchaseOrderBillService.load(billno);
            mapcont.put("storeName","Ancient Stone");
            mapcont.put("billType","采购单");
            mapcont.put("billNo",billno);
            User user = CacheManager.getUserById(purchaseOrderBill.getOprId());
            mapcont.put("makeBill",user.getName());
            mapcont.put("billDate", CommonUtil.getDateString(purchaseOrderBill.getBillDate(),"yyyy-MM-dd"));
            mapcont.put("coustmer",purchaseOrderBill.getDestUnitName());
            mapcont.put("remark",purchaseOrderBill.getRemark());
            mapcont.put("printTime",CommonUtil.getDateString(new Date(),"yyyy-MM-dd HH:mm:ss"));
            List<PurchaseOrderBillDtl> billDtlByBillNo = this.purchaseOrderBillService.findBillDtlByBillNo(billno);
            map.put("print",printSet);
            map.put("cont",mapcont);
            map.put("contDel",billDtlByBillNo);
        }
        if(billno.indexOf(BillConstant.BillPrefix.SaleOrderReturn)!=-1){
            Map<String,Object> mapcont=new HashMap<String,Object>();
            String hql="from PrintSet t where t.id=?";
            PrintSet printSet = this.printSetDao.findUnique(hql, new Object[]{Long.parseLong(id)});
            SaleOrderReturnBill saleOrderReturnBill = this.saleOrderReturnBillService.load(billno);
            mapcont.put("storeName","Ancient Stone");
            mapcont.put("billType","销售退货");
            mapcont.put("billNo",billno);
            User user = CacheManager.getUserById(saleOrderReturnBill.getOprId());
            mapcont.put("makeBill",user.getName());
            mapcont.put("billDate", CommonUtil.getDateString(saleOrderReturnBill.getBillDate(),"yyyy-MM-dd"));
            mapcont.put("coustmer",saleOrderReturnBill.getDestUnitName());
            mapcont.put("remark",saleOrderReturnBill.getRemark());
            mapcont.put("printTime",CommonUtil.getDateString(new Date(),"yyyy-MM-dd HH:mm:ss"));
            User user1 = this.userService.getUser(saleOrderReturnBill.getBusnissId());
            mapcont.put("businessId",user1.getName());
            List<SaleOrderReturnBillDtl> billDtlByBillNo = this.saleOrderReturnBillService.findBillDtlByBillNo(billno);
            map.put("print",printSet);
            map.put("cont",mapcont);
            map.put("contDel",billDtlByBillNo);
        }
        if(billno.indexOf(BillConstant.BillPrefix.purchaseReturn)!=-1){
            Map<String,Object> mapcont=new HashMap<String,Object>();
            String hql="from PrintSet t where t.id=?";
            PrintSet printSet = this.printSetDao.findUnique(hql, new Object[]{Long.parseLong(id)});
            PurchaseReturnBill purchaseReturnBill = this.purchaseReturnBillService.load(billno);
            mapcont.put("storeName","Ancient Stone");
            mapcont.put("billType","采购退货");
            mapcont.put("billNo",billno);
            User user = CacheManager.getUserById(purchaseReturnBill.getOprId());
            mapcont.put("makeBill",user.getName());
            mapcont.put("coustmer",purchaseReturnBill.getOrigUnitName());
            mapcont.put("billDate", CommonUtil.getDateString(purchaseReturnBill.getBillDate(),"yyyy-MM-dd"));
            mapcont.put("remark",purchaseReturnBill.getRemark());
            mapcont.put("printTime",CommonUtil.getDateString(new Date(),"yyyy-MM-dd HH:mm:ss"));
            List<PurchaseReturnBillDtl> billDtlByBillNo = this.purchaseReturnBillService.findBillDtlByBillNo(billno);
            map.put("print",printSet);
            map.put("cont",mapcont);
            map.put("contDel",billDtlByBillNo);
        }
        if(billno.indexOf(BillConstant.BillPrefix.Transfer)!=-1){
            Map<String,Object> mapcont=new HashMap<String,Object>();
            String hql="from PrintSet t where t.id=?";
            PrintSet printSet = this.printSetDao.findUnique(hql, new Object[]{Long.parseLong(id)});
            TransferOrderBill transferOrderBill = this.transferOrderBillService.load(billno);
            mapcont.put("storeName","Ancient Stone");
            mapcont.put("billType","调拨");
            mapcont.put("billNo",billno);
            User user = CacheManager.getUserById(transferOrderBill.getOprId());
            mapcont.put("makeBill",user.getName());
            mapcont.put("coustmer",transferOrderBill.getOrigUnitName());
            mapcont.put("billDate", CommonUtil.getDateString(transferOrderBill.getBillDate(),"yyyy-MM-dd"));
            mapcont.put("remark",transferOrderBill.getRemark());
            mapcont.put("printTime",CommonUtil.getDateString(new Date(),"yyyy-MM-dd HH:mm:ss"));
            List<TransferOrderBillDtl> billDtlByBillNo= this.transferOrderBillService.findBillDtlByBillNo(billno);
            map.put("print",printSet);
            map.put("cont",mapcont);
            map.put("contDel",billDtlByBillNo);
        }
        return map;
    }

    public Map<String,Object> printMessageA4(String id,String billno){
        Map<String,Object> map=new HashMap<String,Object>();
        if(billno.indexOf(BillConstant.BillPrefix.saleOrder)!=-1){
            Map<String,Object> mapcont=new HashMap<String,Object>();
            String hql="from PrintSet t where t.id=?";
            PrintSet printSet = this.printSetDao.findUnique(hql, new Object[]{Long.parseLong(id)});
            SaleOrderBill saleOrderBill = this.saleOrderBillService.load(billno);
            mapcont.put("storeName","Ancient Stone");
            mapcont.put("billType","销售单");
            mapcont.put("billNo","单号:"+billno);
            User user = CacheManager.getUserById(saleOrderBill.getOprId());
            mapcont.put("makeBill","制单人:"+user.getName());
            mapcont.put("billDate", "日期:"+CommonUtil.getDateString(saleOrderBill.getBillDate(),"yyyy-MM-dd"));
            mapcont.put("coustmer","客户:"+saleOrderBill.getDestUnitName());
            if(CommonUtil.isNotBlank(saleOrderBill.getRemark())){
                mapcont.put("remark","备注:"+saleOrderBill.getRemark());
            }else{
                mapcont.put("remark","备注:");
            }

            if(CommonUtil.isNotBlank(saleOrderBill.getAfterBalance())){
                mapcont.put("shopAfter","售后余额:"+saleOrderBill.getAfterBalance());
            }else{
                mapcont.put("shopAfter","售后余额:");
            }
            if(CommonUtil.isNotBlank(saleOrderBill.getPreBalance())){
                mapcont.put("shopBefore","售前余额:"+saleOrderBill.getPreBalance());
            }else{
                mapcont.put("shopBefore","售前余额:");
            }
            List<SaleOrderBillDtl> billDtlByBillNo = this.saleOrderBillService.findBillDtlByBillNo(billno);
            List<SaleOrderBillDtl> sendbillDtlByBillNo=new ArrayList<SaleOrderBillDtl>();
            for(SaleOrderBillDtl saleOrderBillDtl:billDtlByBillNo){
                SaleOrderBillDtl newSaleOrderBillDtl=new SaleOrderBillDtl();
                BeanUtils.copyProperties(saleOrderBillDtl,newSaleOrderBillDtl);
                newSaleOrderBillDtl.setStyleName(CacheManager.getStyleNameById(saleOrderBillDtl.getStyleId()));
                Style style = CacheManager.getStyleById(saleOrderBillDtl.getStyleId());
                newSaleOrderBillDtl.setPrice(style.getPrice());
                newSaleOrderBillDtl.setTotPrice(CommonUtil.doubleChange(style.getPrice()*saleOrderBillDtl.getQty(),2));
                sendbillDtlByBillNo.add(newSaleOrderBillDtl);
            }
            map.put("print",printSet);
            map.put("cont",mapcont);
            map.put("contDel",sendbillDtlByBillNo);
        }
        return map;
    }
}
