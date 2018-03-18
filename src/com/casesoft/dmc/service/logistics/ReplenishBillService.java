package com.casesoft.dmc.service.logistics;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.logistics.MergeReplenishBillDao;
import com.casesoft.dmc.dao.logistics.ReplenishBillDao;
import com.casesoft.dmc.dao.sys.UserDao;
import com.casesoft.dmc.model.logistics.*;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by Alvin on 2018/1/27.
 */
@Service
@Transactional
public class ReplenishBillService implements IBaseService<ReplenishBill, String> {
    @Autowired
    private ReplenishBillDao replenishBillDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private MergeReplenishBillDao MergeReplenishBillDao;
    @Override
    public Page<ReplenishBill> findPage(Page<ReplenishBill> page, List<PropertyFilter> filters) {
        return this.replenishBillDao.findPage(page,filters);
    }

    @Override
    public void save(ReplenishBill bill) {
        this.replenishBillDao.save(bill);
        if(CommonUtil.isNotBlank(bill.getDtlList())){
            this.replenishBillDao.doBatchInsert(bill.getDtlList());
        }
    }
    public void cancelUpdate(ReplenishBill bill) {
        this.replenishBillDao.saveOrUpdate(bill);

    }

    public void saveMessage(ReplenishBill replenishBill, List<ReplenishBillDtl> replenishBillDtlList) {
        //删除原来数据
        String hql="delete from ReplenishBillDtl t where t.billNo=?";
        this.replenishBillDao.batchExecute(hql, replenishBill.getId());
        String selecthql="from User t where t.id=?";
        User user=this.userDao.findUnique(selecthql,new Object[]{replenishBill.getBusnissId()});
        //查询在库数量

        for(ReplenishBillDtl replenishBillDtl:replenishBillDtlList){
            String hqlsum="select count(t.code) from EpcStock t where t.inStock=1 and t.sku=?";
            Object unique = this.replenishBillDao.findUnique(hqlsum, replenishBillDtl.getSku());
            if(CommonUtil.isNotBlank(unique)){
                replenishBillDtl.setStockQty(Long.parseLong(unique+""));
            }else{
                replenishBillDtl.setStockQty(Long.parseLong(0+""));
            }

            String jmsunit="from Unit t where t.groupId ='JMS'";
            List<Unit> Units = this.replenishBillDao.find(jmsunit);
            String ownerids="";
            for(int i=0;i<Units.size();i++){
               if(i==0){
                   ownerids+="'"+Units.get(i).getId()+"'";
               }else{
                   ownerids+=",'"+Units.get(i).getId()+"'";
               }
            }
            String hqljmssum="select count(t.code) from EpcStock t where t.inStock=1 and t.sku=? and t.ownerId in ("+ownerids+")";

            Object jmssum = this.replenishBillDao.findUnique(hqljmssum, replenishBillDtl.getSku());
            if(CommonUtil.isNotBlank(jmssum)){
                replenishBillDtl.setFranchiseeStockQty(Long.parseLong(jmssum+""));
            }else{
                replenishBillDtl.setFranchiseeStockQty(Long.parseLong(0+""));
            }

        }

        replenishBill.setBusnissName(user.getName());
        this.replenishBillDao.saveOrUpdate(replenishBill);
        if(CommonUtil.isNotBlank(replenishBillDtlList)){
            this.replenishBillDao.doBatchInsert(replenishBillDtlList);
        }
    }

    @Override
    public ReplenishBill load(String id) {
        return this.replenishBillDao.load(id);
    }

    @Override
    public ReplenishBill get(String propertyName, Object value) {
        return this.replenishBillDao.findUniqueBy(propertyName,value);
    }

    @Override
    public List<ReplenishBill> find(List<PropertyFilter> filters) {
        return this.replenishBillDao.find(filters);
    }

    @Override
    public List<ReplenishBill> getAll() {
        return this.replenishBillDao.getAll();
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(ReplenishBill entity) {
        this.replenishBillDao.update(entity);
    }

    @Override
    public void delete(ReplenishBill entity) {
        this.replenishBillDao.delete(entity);
    }

    @Override
    public void delete(String id) {
        this.replenishBillDao.delete(id);
    }

    public List<ReplenishBillDtl> findBillDtl(String billNo){
        String hql="from ReplenishBillDtl t where t.billNo=?";
        return this.replenishBillDao.find(hql,new Object[]{billNo});
    }

    public List<ReplenishBill> findmergeReplenishBill(){
        String hql="from ReplenishBill t where t.status=0";
        return this.replenishBillDao.find(hql);
    }

    public List<ReplenishBillDtl> findmereReplenishBillDtl(String billNos){
        String hql="from ReplenishBillDtl t where t.billNo in("+billNos+")";
        return this.replenishBillDao.find(hql);
    }

    public boolean mergeBill( List<ReplenishBill> replenishBills,List<ReplenishBillDtl> replenishBillDtls, String billNos){
        try {
            Map<String,MergeReplenishBillDtl> map=new HashMap<String,MergeReplenishBillDtl>();//记录style和颜色对应的MergeReplenishBillDtl
            Map<String,Recordsize> maprecordsize=new HashMap<String,Recordsize>();//记录MergeReplenishBillDtl中id对应的recordsize
            List<MergeReplenishBillDtl> listMergeReplenishBillDtl=new ArrayList<MergeReplenishBillDtl>();
            List<Recordsize> listRecordsize= new ArrayList<Recordsize>();
            String prefix = BillConstant.BillPrefix.ReplenishiBillMerge
                    + CommonUtil.getDateString(new Date(), "yyMMddHHmmssSSS");
            int sumqty=0;
            for(int i=0;i<replenishBillDtls.size();i++){
                ReplenishBillDtl replenishBillDtl = replenishBillDtls.get(i);
                sumqty+=replenishBillDtl.getQty();
                String key=replenishBillDtl.getStyleId()+","+replenishBillDtl.getColorId();
                if(i==0){
                    MergeReplenishBillDtl mergeReplenishBillDtl=new MergeReplenishBillDtl();
                    mergeReplenishBillDtl.setId(replenishBillDtl.getStyleId()+"_"+replenishBillDtl.getColorId());
                    mergeReplenishBillDtl.setColorid(replenishBillDtl.getColorId());
                    mergeReplenishBillDtl.setStyleid(replenishBillDtl.getStyleId());
                    mergeReplenishBillDtl.setAllqyt(replenishBillDtl.getQty()+"");
                    mergeReplenishBillDtl.setBillNo(prefix);
                    ReplenishBill replenishBill = this.selectedReplenishBill(replenishBillDtl.getBillNo(), replenishBills);
                    Unit unitById = CacheManager.getUnitById(replenishBill.getOwnerId());
                    mergeReplenishBillDtl.setRecordunits(unitById.getName());
                    map.put(key,mergeReplenishBillDtl);
                    Recordsize recordsizes=new Recordsize();
                    recordsizes.setId(new GuidCreator().toString());
                    recordsizes.setSizeid(replenishBillDtl.getSizeId());
                    recordsizes.setRecordid(replenishBillDtl.getStyleId()+"_"+replenishBillDtl.getColorId());
                    recordsizes.setQty(replenishBillDtl.getQty()+"");
                    recordsizes.setStockQty(replenishBillDtl.getStockQty());
                    recordsizes.setFranchiseeStockQty(replenishBillDtl.getFranchiseeStockQty());
                    recordsizes.setAlreadyChange(0);
                    recordsizes.setNowChange(0);
                    recordsizes.setIsChange("N");
                    recordsizes.setBillNo(prefix);
                    //listRecordsize.add(recordsizes)
                    maprecordsize.put((key+replenishBillDtl.getSizeId()),recordsizes);
                }else{
                    MergeReplenishBillDtl mergeReplenishBillDtl = map.get(key);
                    if(CommonUtil.isNotBlank(mergeReplenishBillDtl)){
                        String qty=mergeReplenishBillDtl.getAllqyt();
                        mergeReplenishBillDtl.setAllqyt((Integer.parseInt(qty)+Integer.parseInt(replenishBillDtl.getQty()+""))+"");
                        ReplenishBill replenishBill = this.selectedReplenishBill(replenishBillDtl.getBillNo(), replenishBills);
                        Unit unitById = CacheManager.getUnitById(replenishBill.getOwnerId());
                        String recordunits = mergeReplenishBillDtl.getRecordunits();
                        if(recordunits.indexOf(unitById.getName())!=-1){

                        }else{
                            mergeReplenishBillDtl.setRecordunits(recordunits+","+unitById.getName());
                        }

                   /* Recordsize recordsizes=new Recordsize();
                    recordsizes.setId(new GuidCreator().toString());
                    recordsizes.setSizeid(replenishBillDtl.getSizeId());
                    recordsizes.setRecordid(key);
                    recordsizes.setQty(replenishBillDtl.getQty()+"");
                    listRecordsize.add(recordsizes);*/
                        Recordsize recordsize = maprecordsize.get(key + replenishBillDtl.getSizeId());
                        if(CommonUtil.isNotBlank(recordsize)){
                            String qtys=recordsize.getQty();
                            recordsize.setQty((Integer.parseInt(qtys)+Integer.parseInt(replenishBillDtl.getQty()+""))+"");
                        }else{
                            Recordsize recordsizes=new Recordsize();
                            recordsizes.setId(new GuidCreator().toString());
                            recordsizes.setSizeid(replenishBillDtl.getSizeId());
                            recordsizes.setRecordid(key);
                            recordsizes.setQty(replenishBillDtl.getQty()+"");
                            maprecordsize.put((key+replenishBillDtl.getSizeId()),recordsizes);
                        }
                    }else{
                        MergeReplenishBillDtl mergeReplenishBillDtls=new MergeReplenishBillDtl();
                        mergeReplenishBillDtls.setId(replenishBillDtl.getStyleId()+"_"+replenishBillDtl.getColorId());
                        mergeReplenishBillDtls.setColorid(replenishBillDtl.getColorId());
                        mergeReplenishBillDtls.setStyleid(replenishBillDtl.getStyleId());
                        mergeReplenishBillDtls.setAllqyt(replenishBillDtl.getQty()+"");
                        ReplenishBill replenishBill = this.selectedReplenishBill(replenishBillDtl.getBillNo(), replenishBills);
                        Unit unitById = CacheManager.getUnitById(replenishBill.getOwnerId());
                        mergeReplenishBillDtls.setRecordunits(unitById.getName());
                        mergeReplenishBillDtls.setBillNo(prefix);
                        map.put(key,mergeReplenishBillDtls);
                        Recordsize recordsizes=new Recordsize();
                        recordsizes.setId(new GuidCreator().toString());
                        recordsizes.setSizeid(replenishBillDtl.getSizeId());
                        recordsizes.setRecordid(replenishBillDtl.getStyleId()+"_"+replenishBillDtl.getColorId());
                        recordsizes.setQty(replenishBillDtl.getQty()+"");
                        recordsizes.setStockQty(replenishBillDtl.getStockQty());
                        recordsizes.setFranchiseeStockQty(replenishBillDtl.getFranchiseeStockQty());
                        recordsizes.setAlreadyChange(0);
                        recordsizes.setNowChange(0);
                        recordsizes.setIsChange("N");
                        recordsizes.setBillNo(prefix);
                        //listRecordsize.add(recordsizes)
                        maprecordsize.put((key+replenishBillDtl.getSizeId()),recordsizes);
                    }
                }
            }
            //保存单据
            Iterator<Map.Entry<String, MergeReplenishBillDtl>> iteratormap = map.entrySet().iterator();
            while (iteratormap.hasNext()) {
                Map.Entry<String, MergeReplenishBillDtl> entry = iteratormap.next();
                System.out.println("key:value = " + entry.getKey() + ":" + entry.getValue());
                listMergeReplenishBillDtl.add(entry.getValue());
            }
            Iterator<Map.Entry<String, Recordsize>> iteratormaprecordsize = maprecordsize.entrySet().iterator();
            while (iteratormaprecordsize.hasNext()) {
                Map.Entry<String, Recordsize> entry = iteratormaprecordsize.next();
                System.out.println("key:value = " + entry.getKey() + ":" + entry.getValue());
                listRecordsize.add(entry.getValue());
            }
            MergeReplenishBill mergeReplenishBill=new MergeReplenishBill();

            //String billNo = this.saleOrderBillService.findMaxBillNo(prefix);
            mergeReplenishBill.setId(prefix);
            mergeReplenishBill.setBillNo(prefix);
            mergeReplenishBill.setTotQty(Long.parseLong(sumqty+""));
            mergeReplenishBill.setBillDate(new Date());
            mergeReplenishBill.setStatus(BillConstant.BillStatus.Enter);
            String hql="update ReplenishBill t set t.srcBillNo=?,t.status=? where t.billNo in ("+billNos+")";
            this.replenishBillDao.batchExecute(hql, prefix,BillConstant.BillStatus.End);
            this.MergeReplenishBillDao.save(mergeReplenishBill);
            this.replenishBillDao.doBatchInsert(listRecordsize);

            this.replenishBillDao.doBatchInsert(listMergeReplenishBillDtl);



            return true;
        }catch (Exception e){
            e.printStackTrace();
            return  false;
        }

    }

   public ReplenishBill selectedReplenishBill(String billNo,List<ReplenishBill> replenishBills){
        boolean isok=true;
       ReplenishBill replenishBilllist=null;
        for(ReplenishBill replenishBill:replenishBills){
            if(isok){
                if(billNo.equals(replenishBill.getId())){
                    isok=false;
                    replenishBilllist=replenishBill;
                }
            }

        }
       return replenishBilllist;
   }
}
