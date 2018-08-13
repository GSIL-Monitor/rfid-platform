package com.casesoft.dmc.service.logistics;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.logistics.BillConvertUtil;
import com.casesoft.dmc.controller.product.StyleUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.dao.logistics.FloorallocationDao;
import com.casesoft.dmc.dao.logistics.SaleOrderBillDao;
import com.casesoft.dmc.dao.logistics.TransferOrderBillDao;
import com.casesoft.dmc.model.logistics.*;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.stock.CodeFirstTime;
import com.casesoft.dmc.model.sys.Setting;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.model.tag.Epc;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.Record;
import com.casesoft.dmc.service.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by yushen on 2017/7/3.
 */
@Service
@Transactional
public class TransferOrderBillService implements IBaseService<TransferOrderBill,String> {


    @Autowired
    private TransferOrderBillDao transferOrderBillDao;
    @Autowired
    private SaleOrderBillDao saleOrderBillDao;
    @Autowired
    private SaleOrderBillService saleOrderBillService;
    @Autowired
    private FloorallocationDao floorallocationDao;

    @Override
    public Page<TransferOrderBill> findPage(Page<TransferOrderBill> page, List<PropertyFilter> filters) {
        return this.transferOrderBillDao.findPage(page,filters);
    }

    @Override
    public void save(TransferOrderBill entity) {
        this.transferOrderBillDao.save(entity);
    }

    @Override
    public TransferOrderBill load(String id) {
        return this.transferOrderBillDao.load(id);
    }

    @Override
    public TransferOrderBill get(String propertyName, Object value) {
        return this.transferOrderBillDao.findUniqueBy(propertyName,value);
    }

    @Override
    public List<TransferOrderBill> find(List<PropertyFilter> filters) {
        return this.transferOrderBillDao.find(filters);
    }

    @Override
    public List<TransferOrderBill> getAll() {
        return this.transferOrderBillDao.getAll();
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(TransferOrderBill entity) {
        this.transferOrderBillDao.saveOrUpdate(entity);

    }

    @Override
    public void delete(TransferOrderBill entity) {
        this.transferOrderBillDao.delete(entity);
    }

    @Override
    public void delete(String id) {
        this.transferOrderBillDao.delete(id);
    }

    public void deleteBillDtlByBillNo(String billNo){
        String hql="delete from TransferOrderBillDtl t where t.billNo=?";
        this.transferOrderBillDao.batchExecute(hql,billNo);
    }

    public String findMaxBillNo(String prefix){
        String hql="select max(CAST(SUBSTRING(t.billNo,9),integer))"
                + " from TransferOrderBill as t where t.billNo like ?";
        Integer code = this.transferOrderBillDao.findUnique(hql,prefix+'%');
        return code == null ? (prefix + "001") : prefix + CommonUtil.convertIntToString(code + 1, 3);
    }

    public void save(TransferOrderBill transferOrderBill, List<TransferOrderBillDtl> transferOrderBillDtlList){
        this.transferOrderBillDao.batchExecute("delete from TransferOrderBillDtl t where t.billNo=?",transferOrderBill.getBillNo());
        this.transferOrderBillDao.batchExecute("delete from BillRecord where billNo=?", transferOrderBill.getBillNo());

        this.transferOrderBillDao.saveOrUpdate(transferOrderBill);
        this.transferOrderBillDao.doBatchInsert(transferOrderBillDtlList);
        if(CommonUtil.isNotBlank(transferOrderBill.getBillRecordList())){
            this.transferOrderBillDao.doBatchInsert(transferOrderBill.getBillRecordList());
        }
    }
    /**
     * 客户端调拨收货
     * */
    public void saveBusinessIn(TransferOrderBill transferOrderBill, List<TransferOrderBillDtl> transferOrderBillDtlList, Business bus) {
        this.transferOrderBillDao.batchExecute("delete from TransferOrderBillDtl t where t.billNo=?",transferOrderBill.getBillNo());
        this.transferOrderBillDao.saveOrUpdate(transferOrderBill);
        this.transferOrderBillDao.doBatchInsert(transferOrderBillDtlList);
        if(CommonUtil.isNotBlank(transferOrderBill.getBillRecordList())){
            this.transferOrderBillDao.doBatchInsert(transferOrderBill.getBillRecordList());
        }
        if(bus.getType().equals(Constant.TaskType.Inbound)){
            List<Record> recordList = bus.getRecordList();
            ArrayList<CodeFirstTime> list=new ArrayList<CodeFirstTime>();
            for(int i=0;i<recordList.size();i++){
                CodeFirstTime codeFirstTime =this.transferOrderBillDao.findUnique("from CodeFirstTime where code=? and warehouseId=?",new Object[]{recordList.get(i).getCode(),transferOrderBill.getDestId()});
                if(CommonUtil.isBlank(codeFirstTime)){
                    CodeFirstTime newcodeFirstTime=new CodeFirstTime();
                    newcodeFirstTime.setId(recordList.get(i).getCode()+"-"+transferOrderBill.getDestId());
                    newcodeFirstTime.setCode(recordList.get(i).getCode());
                    newcodeFirstTime.setWarehouseId(transferOrderBill.getDestId());
                    newcodeFirstTime.setFirstTime(new Date());
                    Unit unitByCode = CacheManager.getUnitByCode(transferOrderBill.getDestUnitId());
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
                this.transferOrderBillDao.doBatchInsert(list);
            }
        }
    }

    public List<TransferOrderBillDtl> findBillDtlByBillNo(String billNo){
        String hql="from TransferOrderBillDtl t where t.billNo=?";
        return this.transferOrderBillDao.find(hql,new Object[]{billNo});
    }

    public Epc findProductBycode(String code) {
        return this.transferOrderBillDao.findUnique("from Epc where code=?", code);
    }

    @Autowired
    private TaskService taskService;
    /**
     * web调拨转换出入库任务
     * */
    public MessageBox saveBusiness(TransferOrderBill transferOrderBill, List<TransferOrderBillDtl> transferOrderBillDtlList, Business business) throws Exception {
        List<Style> styleList = new ArrayList<>();
        for(TransferOrderBillDtl dtl : transferOrderBillDtlList){
            if(dtl.getStatus() == BillConstant.BillDtlStatus.InStore){
                Style s = CacheManager.getStyleById(dtl.getStyleId());
                s.setClass6(BillConstant.InStockType.BackOrder);
                styleList.add(s);
            }
        }
        MessageBox messageBox = this.taskService.checkEpcStock(business);
        if(messageBox.getSuccess()){
            this.transferOrderBillDao.saveOrUpdate(transferOrderBill);
            this.transferOrderBillDao.doBatchInsert(transferOrderBillDtlList);
            //记录第一次入库时间
            if(business.getType().equals(Constant.TaskType.Inbound)){
                List<Record> recordList = business.getRecordList();
                ArrayList<CodeFirstTime> list=new ArrayList<CodeFirstTime>();
                for(Record r : recordList){
                    CodeFirstTime codeFirstTime =this.transferOrderBillDao.findUnique("from CodeFirstTime where code=? and warehouseId=?",new Object[]{r.getCode(),transferOrderBill.getDestId()});
                    BillConvertUtil.setEpcStockPrice(codeFirstTime,r,list,transferOrderBill.getDestId());
                }
                if(list.size()!=0){
                    this.transferOrderBillDao.doBatchInsert(list);
                }
            }else{

            }
            this.taskService.webSave(business);
            if(styleList.size() > 0){
                this.transferOrderBillDao.doBatchInsert(styleList);
            }
            return messageBox;
        }else{
            return messageBox;
        }


    }
    /**
     * web调拨转换出入库任务(SrcBillNo有销售单时)
     * */
    public MessageBox saveBusinessOnHaveSaleNo(TransferOrderBill transferOrderBill, List<TransferOrderBillDtl> transferOrderBillDtlList, Business business,List<Epc> epcList, User user, Setting setting) throws Exception {
        List<Style> styleList = new ArrayList<>();
        for(TransferOrderBillDtl dtl : transferOrderBillDtlList){
            if(dtl.getStatus() == BillConstant.BillDtlStatus.InStore){
                Style s = CacheManager.getStyleById(dtl.getStyleId());
                s.setClass6(BillConstant.InStockType.BackOrder);
                styleList.add(s);
            }
        }
        MessageBox messageBox = this.taskService.checkEpcStock(business);
        if(messageBox.getSuccess()){
            this.transferOrderBillDao.saveOrUpdate(transferOrderBill);
            this.transferOrderBillDao.doBatchInsert(transferOrderBillDtlList);
            //记录第一次入库时间
            if(business.getType().equals(Constant.TaskType.Inbound)){
                List<Record> recordList = business.getRecordList();
                ArrayList<CodeFirstTime> list=new ArrayList<CodeFirstTime>();
                for(Record r : recordList){
                    CodeFirstTime codeFirstTime =this.transferOrderBillDao.findUnique("from CodeFirstTime where code=? and warehouseId=?",new Object[]{r.getCode(),transferOrderBill.getDestId()});
                    BillConvertUtil.setEpcStockPrice(codeFirstTime,r,list,transferOrderBill.getDestId());
                }
                if(list.size()!=0){
                    this.transferOrderBillDao.doBatchInsert(list);
                }
            }else{

            }
            this.taskService.webSave(business);
            if(styleList.size() > 0){
                this.transferOrderBillDao.doBatchInsert(styleList);
            }
            SaleOrderBill saleOrderBill = this.saleOrderBillDao.get(transferOrderBill.getSrcBillNo());
            List<SaleOrderBillDtl> billDtlByBillNo = this.saleOrderBillService.findBillDtlByBillNo(transferOrderBill.getSrcBillNo());
            List<BillRecord> billRecordList = new ArrayList<>();
             if(epcList.size()>0){
                 for (Epc epc : epcList) {
                    BillRecord billRecord = new BillRecord(saleOrderBill.getBillNo() + "-" + epc.getCode(), epc.getCode(), saleOrderBill.getBillNo(), epc.getSku());
                    billRecordList.add(billRecord);
                }
            }
            this.transferOrderBillDao.doBatchInsert(billRecordList);
            //是否开启了调拨单入库销售单自动出库
            if(CommonUtil.isNotBlank(setting)&&CommonUtil.isNotBlank(setting.getValue())&&setting.getValue().equals("true")) {
                Business businessSale = BillConvertUtil.covertToSaleOrderBusinessOut(saleOrderBill, billDtlByBillNo, epcList, user);
                messageBox = this.saleOrderBillService.saveBusinessout(saleOrderBill, billDtlByBillNo, businessSale, epcList, null);
            }
            return messageBox;
        }else{
            return messageBox;
        }


    }


    public List<BillRecord> getBillRecod(String billNo) {
        return  this.transferOrderBillDao.find("from BillRecord where billNo=?",new Object[]{billNo});
    }

    public List<Object> findgroupSum(String hql){
        List<Object> objects = this.transferOrderBillDao.find(hql);
        return objects;
    }

    public Long findtransferCountnum(String hql) {
        return this.transferOrderBillDao.findUnique(hql);
    }

    public List<Map<String,Object>> fillTransMap(List<Map<String,Object>> list,String rootPath) throws Exception {

        for(int i=0;i<list.size();i++){
            Map<String, Object> map = list.get(i);
           /* map.put("S",0);
            map.put("M",0);
            map.put("L",0);
            map.put("XL",0);
            map.put("XXL",0);*/

            String styleid="";
            String colorid="";
            Iterator it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String key = (String)entry.getKey();

                if(key.equals("styleid")){
                    styleid=(String) entry.getValue();
                }
                if(key.equals("colorid")){
                    colorid=(String) entry.getValue();
                }

            }
            String url = StyleUtil.returnImageUrl(styleid, rootPath);
            map.put("Url",url);
            String sizeArray = PropertyUtil.getValue("sizeArray");
            String[] sizeArrays = sizeArray.split(",");
            for(int b=0;b<sizeArrays.length;b++){
                map.put(sizeArrays[b],0);
            }
            String hql="select t.sizeId,sum(t.qty) as qty from TransferOrderBillDtl t where t.styleId=? and t.colorId=? group by t.sizeId";
            List<Object> objects = this.transferOrderBillDao.find(hql, new Object[]{styleid, colorid});
            for(int a=0;a<objects.size();a++){
                Object[] object=(Object[])objects.get(a);
                map.put(object[0]+"",object[1]);
            }
        }
        return list;
    }


    public Integer findBillStatus(String billNo) {
        return this.transferOrderBillDao.findUnique("select status from TransferOrderBill where id = ?",billNo);
    }

    public List<FloorallocationAndSku> findFloorallocationAndSku(List<TransferOrderBillDtl> transferOrderBillDtls,TransferOrderBill transferOrderBill){
        //得到入库数量小于数量的sku
        String skus=null;
        for(TransferOrderBillDtl transferOrderBillDtl:transferOrderBillDtls){
            if(transferOrderBillDtl.getInQty()<transferOrderBillDtl.getQty()){
                if(CommonUtil.isBlank(skus)){
                    skus="'"+transferOrderBillDtl.getSku()+"'";
                }else{
                    skus+=",'"+transferOrderBillDtl.getSku()+"'";
                }
            }
        }
        String sql="select sku,Floorallocation,count(code) as sum from STOCK_EPCSTOCK t where t.warehouseid='"+transferOrderBill.getOrigId()+"' and t.instock='1' and t.sku in("+skus+") group by t.sku,t.floorallocation";
        return this.floorallocationDao.findBySQl(FloorallocationAndSku.class,sql,null);
    }
}
