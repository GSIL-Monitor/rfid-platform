package com.casesoft.dmc.service.logistics;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.logistics.BillConvertUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.logistics.InventoryBillDao;
import com.casesoft.dmc.dao.task.TaskDao;
import com.casesoft.dmc.model.logistics.BillConstant;
import com.casesoft.dmc.model.logistics.InventoryBill;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.stock.EpcStock;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.BusinessDtl;
import com.casesoft.dmc.model.task.Record;
import com.casesoft.dmc.service.stock.EpcStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/9/8.
 */
@Service
@Transactional
public class InventoryBillService implements IBaseService<InventoryBill, String> {
    @Autowired
    private InventoryBillDao inventoryBillDao;
    @Autowired
    private EpcStockService epcStockService;
    @Autowired
    private TaskDao taskDao;
    @Override
    public Page<InventoryBill> findPage(Page<InventoryBill> page, List<PropertyFilter> filters) {
        return this.inventoryBillDao.findPage(page,filters);
    }

    @Override
    public void save(InventoryBill entity) {

    }

    @Override
    public InventoryBill load(String id) {
        return null;
    }

    @Override
    public InventoryBill get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<InventoryBill> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<InventoryBill> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(InventoryBill entity) {

    }

    @Override
    public void delete(InventoryBill entity) {

    }

    @Override
    public void delete(String id) {

    }

    public EpcStock checkEpcStock(String code){
        String hql="from EpcStock t where t.code=?";
        EpcStock epcStock =this.inventoryBillDao.findUnique(hql,new Object[]{code});
        return epcStock;
    }

    public List<EpcStock> findEpcStockListBycodes(String codes){
        //String hql="from EpcStock t where t.code in (?)";
        //String hql="from EpcStock t where t.code in ( "+codes+" )";
        String[] split = codes.split(",");
        String code="";
        for(int i=0;i<split.length;i++){
            if(i==0){
                code+="'"+split[i]+"'";
            }else{
                code+=",'"+split[i]+"'";
            }
        }
        String hql="from EpcStock t where t.code in ( "+code+")";
        //List<EpcStock> list=this.inventoryBillDao.find(hql,new Object[]{codes});
        List<EpcStock> list=this.inventoryBillDao.find(hql);
        return list;
    }
    public String findMaxBillNo(String prefix) {
        String hql = "select max(CAST(SUBSTRING(t.billNo,9),integer))"
                + " from InventoryBill as t where t.billNo like ?";
        Integer code = this.inventoryBillDao.findUnique(hql, prefix + '%');
        return code == null ? (prefix + "001") : prefix + CommonUtil.convertIntToString(code + 1, 3);
    }

    public void saveInventory(List<EpcStock> list,User currentUser){
        //区分在库和不在库的

        ArrayList<EpcStock> onlist= new ArrayList<EpcStock>();
        ArrayList<EpcStock> noonlist= new ArrayList<EpcStock>();
        for(EpcStock epcStock:list){
            if(epcStock.getInStock()==0){
                noonlist.add(epcStock);
            }
            if(epcStock.getInStock()==1){
                onlist.add(epcStock);
            }
        }
        //处理在库出库的
        ArrayList<InventoryBill> Inventorylist = new ArrayList<InventoryBill>();
        for(EpcStock epcStock:onlist){
            InventoryBill inventoryBill =new InventoryBill();
            String prefix = BillConstant.BillPrefix.Inventory
                    + CommonUtil.getDateString(new Date(), "yyMMddHHmmssSSS");
           // String billNo = findMaxBillNo(prefix);
            inventoryBill.setId(prefix);
            inventoryBill.setBillNo(prefix);
            inventoryBill.setColorId(epcStock.getColorId());
            inventoryBill.setColorName(epcStock.getColorName());
            inventoryBill.setSizeId(epcStock.getSizeId());
            inventoryBill.setSizeName(epcStock.getSizeName());
            inventoryBill.setSku(epcStock.getSku());
            inventoryBill.setStyleId(epcStock.getStyleId());
            inventoryBill.setStyleName(epcStock.getStyleName());
            inventoryBill.setBillDate(new Date());
            inventoryBill.setActQty(1L);
            Style style = CacheManager.getStyleById(epcStock.getStyleId());
            inventoryBill.setActPrice(style.getPrice());
            inventoryBill.setDestId(epcStock.getOwnerId());
            inventoryBill.setDestUnitId(epcStock.getWarehouseId());
            inventoryBill.setOwnerId(epcStock.getOwnerId());
            inventoryBill.setCode(epcStock.getCode());
            inventoryBill.setState(Constant.Token.Storage_Adjust_Outbound+"");
            Inventorylist.add(inventoryBill);
            Business business = BillConvertUtil.covertToInventoryBusinessOut(epcStock,inventoryBill,currentUser,"on");
            saveBusinessout(business);
            epcStock.setInStock(0);
            this.epcStockService.update(epcStock);
            this.inventoryBillDao.save(inventoryBill);
        }
        //this.inventoryBillDao.doBatchInsert(Inventorylist);
        //处理不在库入库的
        ArrayList<InventoryBill> Inventorylistno = new ArrayList<InventoryBill>();
        for(EpcStock epcStock:noonlist){
            InventoryBill inventoryBill =new InventoryBill();
            String prefix = BillConstant.BillPrefix.Inventory
                    + CommonUtil.getDateString(new Date(), "yyMMddHHmmssSSS");
            //String billNo = findMaxBillNo(prefix);
            inventoryBill.setId(prefix);
            inventoryBill.setBillNo(prefix);
            inventoryBill.setColorId(epcStock.getColorId());
            inventoryBill.setColorName(epcStock.getColorName());
            inventoryBill.setSizeId(epcStock.getSizeId());
            inventoryBill.setSizeName(epcStock.getSizeName());
            inventoryBill.setSku(epcStock.getSku());
            inventoryBill.setStyleId(epcStock.getStyleId());
            inventoryBill.setStyleName(epcStock.getStyleName());
            inventoryBill.setBillDate(new Date());
            inventoryBill.setActQty(1L);
            Style style = CacheManager.getStyleById(epcStock.getStyleId());
            inventoryBill.setActPrice(style.getPrice());
            inventoryBill.setOrigId(epcStock.getOwnerId());
            inventoryBill.setOrigUnitId(epcStock.getWarehouseId());
            inventoryBill.setOwnerId(epcStock.getOwnerId());
            inventoryBill.setCode(epcStock.getCode());
            inventoryBill.setState(Constant.Token.Storage_Adjust_Inbound+"");
            Inventorylistno.add(inventoryBill);
            Business business = BillConvertUtil.covertToInventoryBusinessOut(epcStock,inventoryBill,currentUser,"no");
            saveBusinessout(business);
            epcStock.setInStock(1);
            this.epcStockService.update(epcStock);
            this.inventoryBillDao.save(inventoryBill);
        }
        /*for(InventoryBill InventoryBill:Inventorylist){
            Inventorylistno.add(InventoryBill);
        }*/
        //this.inventoryBillDao.doBatchInsert(Inventorylistno);


    }

    public void saveBusinessout(Business business){
        List<Record> recordList = business.getRecordList();
        List<BusinessDtl> dtlList = business.getDtlList();
        if(recordList.size()>0){
            this.inventoryBillDao.doBatchInsert(recordList);
        }
        if(dtlList.size()>0){
            this.inventoryBillDao.doBatchInsert(dtlList);
        }
        this.taskDao.save(business);



    }
}
