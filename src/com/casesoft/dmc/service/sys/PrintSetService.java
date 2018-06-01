package com.casesoft.dmc.service.sys;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.sys.PrintSetDao;
import com.casesoft.dmc.model.logistics.BillConstant;
import com.casesoft.dmc.model.logistics.SaleOrderBill;
import com.casesoft.dmc.model.logistics.SaleOrderBillDtl;
import com.casesoft.dmc.model.sys.PrintSet;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.logistics.*;
import com.casesoft.dmc.service.sys.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<PrintSet> findPrintSetListByOwnerId(String ownerId){
        String hql="from PrintSet t where t.ownerId=?";
        List<PrintSet> printSets = this.printSetDao.find(hql, new Object[]{ownerId});
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
        return map;
    }
}
