package com.casesoft.dmc.service.logistics;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.logistics.BillConvertUtil;
import com.casesoft.dmc.controller.task.TaskUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.dao.logistics.ConsignmentBillDao;
import com.casesoft.dmc.extend.third.request.BaseService;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.logistics.*;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.stock.CodeFirstTime;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.model.tag.Epc;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.Record;
import com.casesoft.dmc.service.cfg.PropertyService;
import com.casesoft.dmc.service.stock.InventoryService;
import com.casesoft.dmc.service.task.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import static oracle.net.aso.C05.e;


/**
 * Created by Administrator on 2017/8/14.
 */
@Service
@Transactional
public class ConsignmentBillService extends BaseService<ConsignmentBill, String> {
    @Autowired
    private TaskService taskService;
    @Autowired
    private ConsignmentBillDao consignmentBillDao;
    @Autowired
    private SaleOrderReturnBillService saleOrderReturnBillService;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private InventoryService inventoryService;
    @Override
    public Page<ConsignmentBill> findPage(Page<ConsignmentBill> page, List<PropertyFilter> filters) {
        return consignmentBillDao.findPage(page, filters);
    }

    @Override
    public void save(ConsignmentBill entity) {
        this.consignmentBillDao.saveOrUpdate(entity);

    }


    @Override
    public ConsignmentBill load(String id) {
        return this.consignmentBillDao.load(id);
    }

    @Override
    public ConsignmentBill get(String propertyName, Object value) {
        return this.consignmentBillDao.findUniqueBy(propertyName,value);
    }

    @Override
    public List<ConsignmentBill> find(List<PropertyFilter> filters) {
        return this.consignmentBillDao.find(filters);
    }

    @Override
    public List<ConsignmentBill> getAll() {
        return this.consignmentBillDao.getAll();
    }
    public ConsignmentBill findBillByBillNo(String billNo) {
        String hql = "from ConsignmentBill srb where srb.billNo =?";
        return this.consignmentBillDao.findUnique(hql, new Object[]{billNo});
    }
    public List<ConsignmentBillDtl> findDtlByBillNo(String billNo) {
        String hql = "from ConsignmentBillDtl dtl where billNo=?";
        return this.consignmentBillDao.find(hql, new Object[]{billNo});
    }
    public String findMaxBillNO(String prefix) {
        String hql = "select max(CAST(SUBSTRING(sorb.billNo,9),integer)) from SaleOrderReturnBill sorb where sorb.billNo like ?";
        Integer No = this.consignmentBillDao.findUnique(hql, new Object[]{prefix + "%"});
        return No == null ? (prefix + "001") : prefix + CommonUtil.convertIntToString(No + 1, 3);
    }
    public String findMaxBillNo(String prefix) {
        String hql = "select max(CAST(SUBSTRING(sorb.billNo,9),integer)) from ConsignmentBill sorb where sorb.billNo like ?";
        Integer No = this.consignmentBillDao.findUnique(hql, new Object[]{prefix + "%"});
        return No == null ? (prefix + "001") : prefix + CommonUtil.convertIntToString(No + 1, 3);
    }
    public void saveBus(Business bus, ConsignmentBill bill) {
        this.consignmentBillDao.saveOrUpdate(bill);
        this.consignmentBillDao.saveOrUpdateX(bus);
        this.consignmentBillDao.doBatchInsert(bus.getDtlList());
    }


    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(ConsignmentBill entity) {

    }



    @Override
    public void delete(ConsignmentBill entity) {

    }
    public void deleteBatchDtl(String billNo) {
        String hql = "delete from ConsignmentBillDtl sd where sd.billNo=?";
        this.consignmentBillDao.batchExecute(hql, billNo);
    }

    @Override
    public void delete(String id) {

    }
    public SaleOrderReturnBill findUniqueByBillNo(String billNo){
        return this.consignmentBillDao.findUnique("from ConsignmentBill pcrb where  pcrb.billNo=?",new Object[]{billNo});
    }

    public List<SaleOrderReturnBillDtl> findDetailsByBillNo(String billNo){
        String hql="from ConsignmentBillDtl pd where pd.billNo=?";
        return this.consignmentBillDao.find(hql,new Object[]{billNo});
    }

    public void updateDetailsByid(String id,Integer outQtys){
        //String hql="from ConsignmentBillDtl pd where pd.id=?";
        //update Unit set owingValue = owingValue - ? where id =?
        String hql="update ConsignmentBillDtl p set p.outQtys=? where p.id=?";
        this.consignmentBillDao.batchExecute(hql,outQtys,id);
    }

    public MessageBox saveBusiness(ConsignmentBill consignmentBill, List<ConsignmentBillDtl> consignmentBillDtlList, Business business) throws Exception {
        List<Style> styleList = new ArrayList<>();
        for(ConsignmentBillDtl dtl : consignmentBillDtlList){
            if(dtl.getStatus() == BillConstant.BillDtlStatus.InStore ){
                Style s = CacheManager.getStyleById(dtl.getStyleId());
                s.setClass6(BillConstant.InStockType.BackOrder);
                styleList.add(s);
            }
        }
        MessageBox messageBox = this.taskService.checkEpcStock(business);
        if(messageBox.getSuccess()){
            this.consignmentBillDao.saveOrUpdate(consignmentBill);
            this.consignmentBillDao.doBatchInsert(consignmentBillDtlList);
            //记录第一次入库时间
            if(business.getType().equals(Constant.TaskType.Inbound)) {
                List<Record> recordList = business.getRecordList();
                ArrayList<CodeFirstTime> list = new ArrayList<CodeFirstTime>();
                for (Record r : recordList) {
                    CodeFirstTime codeFirstTime = this.consignmentBillDao.findUnique("from CodeFirstTime where code=? and warehouseId=?", new Object[]{r.getCode(), consignmentBill.getDestId()});
                    BillConvertUtil.setEpcStockPrice(codeFirstTime,r,list,consignmentBill.getDestId());
                }
                if (list.size() != 0) {
                    this.consignmentBillDao.doBatchInsert(list);
                }
            }
            this.taskService.webSave(business);
            if(styleList.size() > 0){
                this.consignmentBillDao.doBatchInsert(styleList);
            }
            return messageBox;
        }else{
            return messageBox;
        }


    }

    public void saveBusinessout(ConsignmentBill consignmentBill, List<ConsignmentBillDtl> consignmentBillDtlList, Business business) {

        this.consignmentBillDao.saveOrUpdate(consignmentBill);
        this.consignmentBillDao.doBatchInsert(consignmentBillDtlList);
        this.taskService.save(business);

    }

    public List<ConsignmentBillDtl> findBillDtlByBillNo(String billNo) {
        return this.consignmentBillDao.find("from ConsignmentBillDtl where billNo=?", new Object[]{billNo});
    }
    /**
     * 保存寄售单，同时更新客户欠款金额
     *
     * @param bill
     * @param consignmentBillDtls
     */
    public void saveReturnBatch(ConsignmentBill bill,  List<ConsignmentBillDtl> consignmentBillDtls) {
        this.deleteBatchDtl(bill.getBillNo());
        this.consignmentBillDao.batchExecute("delete from BillRecord where billNo=?", bill.getBillNo());

        this.consignmentBillDao.saveOrUpdate(bill);
        this.consignmentBillDao.doBatchInsert(consignmentBillDtls);
        if(CommonUtil.isNotBlank(bill.getBillRecordList())){
            this.consignmentBillDao.doBatchInsert(bill.getBillRecordList());
        }

    }

    public void cancelUpdate(ConsignmentBill bill) {
        this.consignmentBillDao.saveOrUpdate(bill);
    }


    public boolean saleRetrun(ConsignmentBill consignmentBill,List<ConsignmentBillDtl> billDtlByBillNo,User curUser,List<Epc> epcList){
        try {
            List<ConsignmentBillDtl> listm=new ArrayList<ConsignmentBillDtl>();
            List<ConsignmentBillDtl> listq=new ArrayList<ConsignmentBillDtl>();
           for(ConsignmentBillDtl dtl : billDtlByBillNo){
                if(dtl.getSale()>0){
                    listm.add(dtl);
                }
                if(dtl.getQty()-dtl.getSale()>0){
                    //dtl.setTotActPrice(0D);
                    listq.add(dtl);
                }

            }

            //价格退货
            if(listm.size()>0) {
                List<SaleOrderReturnBillDtl> saleOrderReturnBillDtlsm = new ArrayList<SaleOrderReturnBillDtl>();
                SaleOrderReturnBill saleOrderReturnBillm = new SaleOrderReturnBill();
                String prefixm = BillConstant.BillPrefix.SaleOrderReturn + CommonUtil.getDateString(new Date(), "yyMMddHHmmssSSS");
                //String billNom = findMaxBillNO(prefixm);
                consignmentBill.setSaleRetrunBillNom(prefixm);
                String codem = null;
                List<PropertyKey> pkList = this.propertyService.getPropertyKeyByType("RX");
                for (int i = 0; i < pkList.size(); i++) {
                    if (pkList.get(i).getName().equals("寄售退款")) {
                        codem = pkList.get(i).getCode();
                    }
                }
                saleOrderReturnBillm.setSrcBillNo(consignmentBill.getBillNo());

                BillConvertUtil.saveReturnConsignmentBill(consignmentBill, listm, saleOrderReturnBillDtlsm, saleOrderReturnBillm, prefixm, "m", codem);
                this.saleOrderReturnBillService.saveReturnBatch(saleOrderReturnBillm, saleOrderReturnBillDtlsm);
            }
            //货物退货
            if(listq.size()>0) {
                List<SaleOrderReturnBillDtl> saleOrderReturnBillDtlsq = new ArrayList<SaleOrderReturnBillDtl>();
                SaleOrderReturnBill saleOrderReturnBillq = new SaleOrderReturnBill();
                String prefixq = BillConstant.BillPrefix.SaleOrderReturn + CommonUtil.getDateString(new Date(), "yyMMddHHmmssSSS");
                //String billNoq = findMaxBillNO(prefixq);
                consignmentBill.setSaleRetrunBillNoq(prefixq);
                String codeq = null;
                List<PropertyKey> pkListq = this.propertyService.getPropertyKeyByType("RX");
                for (int i = 0; i < pkListq.size(); i++) {
                    if (pkListq.get(i).getName().equals("寄售实物退货")) {
                        codeq = pkListq.get(i).getCode();
                    }
                }
                saleOrderReturnBillq.setSrcBillNo(consignmentBill.getBillNo());
                BillConvertUtil.saveReturnConsignmentBill(consignmentBill, listq, saleOrderReturnBillDtlsq, saleOrderReturnBillq, prefixq, "q", codeq);
                this.saleOrderReturnBillService.saveReturnBatch(saleOrderReturnBillq, saleOrderReturnBillDtlsq);
                Business business = BillConvertUtil.covertToConsignmentBillBusinessOut(consignmentBill, listq, epcList, curUser);
                saveBusinessout(consignmentBill,listq,business);
            }
            //出库
            /*Business business = BillConvertUtil.covertToConsignmentBillBusinessOut(consignmentBill, billDtlByBillNo, epcList, curUser);
            saveBusinessout(consignmentBill,billDtlByBillNo,business);*/
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean saleRetrunNo(ConsignmentBill consignmentBill,List<ConsignmentBillDtl> billDtlByBillNo,User curUser,List<Epc> epcList){
        try{
            Logger logger = LoggerFactory.getLogger(ConsignmentBill.class);
            List<ConsignmentBillDtl> listm=new ArrayList<ConsignmentBillDtl>();
            List<ConsignmentBillDtl> listq=new ArrayList<ConsignmentBillDtl>();
            for(ConsignmentBillDtl dtl : billDtlByBillNo){
                if(dtl.getReadysale()>0){
                    listm.add(dtl);
                }
                if((dtl.getOutQty()-dtl.getBeforeoutQty())>0){
                    dtl.setTotActPrice(0D);
                    listq.add(dtl);
                }
            }
            logger.error("寄存单："+consignmentBill.getBillNo());
            //价格退货
            if(listm.size()>0) {
                List<SaleOrderReturnBillDtl> saleOrderReturnBillDtlsm = new ArrayList<SaleOrderReturnBillDtl>();
                SaleOrderReturnBill saleOrderReturnBillm = new SaleOrderReturnBill();
                String prefixm = BillConstant.BillPrefix.SaleOrderReturn + CommonUtil.getDateString(new Date(), "yyMMddHHmmssSSS");
                //String billNom = findMaxBillNO(prefixm);
                logger.error("价格退货  寄存单："+consignmentBill.getBillNo()+"退货单："+prefixm);
                consignmentBill.setSaleRetrunBillNom(prefixm);
                String codem = null;
                List<PropertyKey> pkList = this.propertyService.getPropertyKeyByType("RX");
                for (int i = 0; i < pkList.size(); i++) {
                    if (pkList.get(i).getName().equals("寄售退款")) {
                        codem = pkList.get(i).getCode();
                    }
                }
                saleOrderReturnBillm.setSrcBillNo(consignmentBill.getBillNo());

                BillConvertUtil.saveReturnConsignmentBill(consignmentBill, listm, saleOrderReturnBillDtlsm, saleOrderReturnBillm, prefixm, "m", codem);
                this.saleOrderReturnBillService.saveReturnBatch(saleOrderReturnBillm, saleOrderReturnBillDtlsm);
                //把//准备销售数量（记录每次退款的销售数量）变成0
                for(ConsignmentBillDtl consignmentBillDtl:listm){
                    consignmentBillDtl.setReadysale(0);
                    consignmentBillDtl.setBeforeoutQty(consignmentBillDtl.getOutQty().intValue());
                }
                this.consignmentBillDao.doBatchInsert(listm);
            }
            //货物退货
            if(listq.size()>0) {
                List<SaleOrderReturnBillDtl> saleOrderReturnBillDtlsq = new ArrayList<SaleOrderReturnBillDtl>();
                SaleOrderReturnBill saleOrderReturnBillq = new SaleOrderReturnBill();
                String prefixq = BillConstant.BillPrefix.SaleOrderReturn + CommonUtil.getDateString(new Date(), "yyMMddHHmmssSSS");
                //String billNoq = findMaxBillNO(prefixq);
                logger.error("货物退货  寄存单："+consignmentBill.getBillNo()+"退货单："+prefixq);
                consignmentBill.setSaleRetrunBillNoq(prefixq);
                String codeq = null;
                List<PropertyKey> pkListq = this.propertyService.getPropertyKeyByType("RX");
                for (int i = 0; i < pkListq.size(); i++) {
                    if (pkListq.get(i).getName().equals("寄售实物退货")) {
                        codeq = pkListq.get(i).getCode();
                    }
                }
                saleOrderReturnBillq.setSrcBillNo(consignmentBill.getBillNo());
                BillConvertUtil.saveReturnConsignmentBill(consignmentBill, listq, saleOrderReturnBillDtlsq, saleOrderReturnBillq, prefixq, "q", codeq);
                this.saleOrderReturnBillService.saveReturnBatch(saleOrderReturnBillq, saleOrderReturnBillDtlsq);
                Business business = BillConvertUtil.covertToConsignmentBillBusinessOut(consignmentBill, listq, epcList, curUser);
                //把记录之前退货件数
                for(ConsignmentBillDtl consignmentBillDtl:listq){
                    consignmentBillDtl.setBeforeoutQty(consignmentBillDtl.getOutQty());
                    consignmentBillDtl.setSavehaveuniqueCodes("");
                    consignmentBillDtl.setSavenohanveuniqueCodes("");
                }
                saveBusinessout(consignmentBill,listq,business);
                //保存logistics_billrecord
                ArrayList<BillRecord> list=new ArrayList<BillRecord>();
                for(Epc epc:epcList){
                    BillRecord billRecord= new BillRecord();
                    billRecord.setBillNo(prefixq);
                    billRecord.setCode(epc.getCode());
                    billRecord.setSku(epc.getSku());
                    billRecord.setId(prefixq+"-"+epc.getCode());
                    list.add(billRecord);
                }
                this.consignmentBillDao.doBatchInsert(list);
                //更改出入库信息
                String codes="";
                for(int i=0;i<epcList.size();i++){
                    Epc epc = epcList.get(i);
                    if(i==0){
                        codes+=epc.getCode();
                    }else{
                        codes+=","+epc.getCode();
                    }
                }
              /*  codes+=" )";*/
                this.inventoryService.updateEpcStockInStockss(codes);

            }
            //改变consignmentBill单据的状态
            Integer countnum=0;//记录详情不能退货的个数
            consignmentBill.setBillType(Constant.ScmConstant.BillType.Save);
            for(ConsignmentBillDtl consignmentBillDtl :billDtlByBillNo){
                if((consignmentBillDtl.getOutQty()+consignmentBillDtl.getOutMonyQty())>=consignmentBillDtl.getInQty()){
                    countnum++;
                }

            }
            if(countnum==billDtlByBillNo.size()){
                consignmentBill.setStatus(BillConstant.BillStatus.End);

            }
            this.consignmentBillDao.saveOrUpdate(consignmentBill);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }



    public void saveConsignmentBillDtl(ConsignmentBillDtl billNoconsignmentDel){
        this.consignmentBillDao.saveOrUpdateX(billNoconsignmentDel);
    }

    public List<BillRecord> findBillRecordByBillNo(String BillNo,String sku){
        return this.consignmentBillDao.find("from BillRecord   where billNo = ? and sku = ? ",new Object[]{BillNo,sku});
    }


   public List<SaleOrderReturnBill> findSaleOrderReturnBillNo(String billno){
       return this.consignmentBillDao.find("from SaleOrderReturnBill   where srcBillNo = ?",new Object[]{billno});
   }


    public Integer findBillStatus(String billNo) {
        return this.consignmentBillDao.findUnique("select status from ConsignmentBill where id = ? ",billNo);
    }
}
