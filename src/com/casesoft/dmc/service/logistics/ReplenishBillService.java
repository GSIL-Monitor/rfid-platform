package com.casesoft.dmc.service.logistics;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.logistics.BillConvertUtil;
import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.logistics.*;
import com.casesoft.dmc.dao.sys.UserDao;
import com.casesoft.dmc.model.logistics.*;
import com.casesoft.dmc.model.logistics.vo.ReplenishCodeVO;
import com.casesoft.dmc.model.logistics.vo.ReplenishSkuVO;
import com.casesoft.dmc.model.logistics.vo.ReplenishStyleVO;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import oracle.jdbc.driver.OracleTypes;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
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
    @Autowired
    private MergeReplenishBillDtlDao  MergeReplenishBillDtlDao;
    @Autowired
    private RecordsizeDao recordsizeDao;
    @Autowired
    private CheckReplenishInfoDao checkReplenishInfoDao;
    @Autowired
    private ReplenishBillDtlDao replenishBillDtlDao;

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Page<ReplenishBill> findPage(Page<ReplenishBill> page, List<PropertyFilter> filters) {
        return this.replenishBillDao.findPage(page,filters);
    }
    public  Page<ReplenishBill> findPagePro(Page<ReplenishBill> page, List<PropertyFilter> filters){
        ResultSet rs=null;
        CallableStatement cs=null;
        Connection con=null;
        try {
            Session session = sessionFactory.getCurrentSession();
            con = SessionFactoryUtils.getDataSource(session.getSessionFactory()).getConnection();
            cs = con.prepareCall("{call findrelenishBillORDel(?,?,?,?,?,?,?,?,?,?)}");
            //设置参数
            //填充空数据
            cs.setString(1, "");
            cs.setString(2, "");
            cs.setString(3, "");
            cs.setString(4, "");
            cs.setString(5, "");
            cs.setString(6, "");
            for(int i=0;i<filters.size();i++){
                PropertyFilter propertyFilter = filters.get(i);
                String propertyName = propertyFilter.getPropertyNames()[0];
                PropertyFilter.MatchType matchType = propertyFilter.getMatchType();
                String name = matchType.name();
                if(propertyName.equals("billDate")&&name.equals("GE")){
                    Date matchValue =(Date) propertyFilter.getMatchValue();
                    String dateString = CommonUtil.getDateString(matchValue, "yyyy-MM-dd");
                    cs.setString(1, dateString);
                }
                if(propertyName.equals("billDate")&&name.equals("LE")){
                    Date matchValue =(Date) propertyFilter.getMatchValue();
                    String dateString = CommonUtil.getDateString(matchValue, "yyyy-MM-dd");
                    cs.setString(2, dateString);
                }
                if(propertyName.equals("styleid")&&name.equals("EQ")){
                    String styleid =(String) propertyFilter.getMatchValue();
                    cs.setString(3, styleid);
                }
                if(propertyName.equals("billNo")&&name.equals("LIKE")){
                    String styleid =(String) propertyFilter.getMatchValue();
                    cs.setString(4, styleid);
                }
                if(propertyName.equals("busnissId")&&name.equals("EQ")){
                    String busnissId =(String) propertyFilter.getMatchValue();
                    cs.setString(5, busnissId);
                }
                if(propertyName.equals("class1")&&name.equals("EQ")){
                    String class1 =(String) propertyFilter.getMatchValue();
                    cs.setString(6, class1);
                }

            }
            Integer beginIndex=(page.getPage()-1)*(page.getPageSize())+1;
            Integer endIndex=(page.getPage())*(page.getPageSize());
            cs.setDouble(7,beginIndex.doubleValue());
            cs.setDouble(8,endIndex.doubleValue());
            cs.registerOutParameter(9, Types.INTEGER);
            cs.registerOutParameter(10, OracleTypes.CURSOR);
            //cs.registerOutParameter("resultSet", -10);
            cs.execute();
            rs=(ResultSet)cs.getObject(10);
            ArrayList<ReplenishBill> list=new ArrayList<ReplenishBill>();
            while (rs!=null&&rs.next()){
                ReplenishBill replenishBill=new ReplenishBill();
                if(CommonUtil.isNotBlank(rs.getObject(1))){
                    replenishBill.setBillNo(rs.getObject(1).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(2))){
                    replenishBill.setStatus(Integer.parseInt(rs.getObject(2).toString()));
                }
                if(CommonUtil.isNotBlank(rs.getObject(3))){
                    replenishBill.setBusnissId(rs.getObject(3).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(4))){
                    replenishBill.setBusnissName(rs.getObject(4).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(5))){
                    replenishBill.setRemark(rs.getObject(5).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(6))){
                    replenishBill.setBillDate(CommonUtil.converStrToDate(rs.getObject(6).toString(),"yyyy-MM-dd"));
                }
                if(CommonUtil.isNotBlank(rs.getObject(7))){
                    replenishBill.setTotQty(Long.parseLong(rs.getObject(7).toString()));
                }
                list.add(replenishBill);
            }
            page.setRows(list);
            Object object = cs.getObject(9);
            page.setTotal(Integer.parseInt(object.toString()));
            int totPage=Integer.parseInt(object.toString())/page.getPageSize();
            page.setTotPage(totPage+1);
            return page;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


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

    public boolean newMergeBill( List<ReplenishBill> replenishBills,List<ReplenishBillDtl> replenishBillDtls, String billNos){
        try {
            /**
             * 1.得到replenishBillDtls
             * 2.循环查询MergeReplenishBillDtl，看是否有对应的style和颜色
             * 3.如果有则修改数据，没有则添加数据
             * 4.查询是否有对应的Recordsize，如果有则修改数据，没有则修改数据
             */

            for(int i=0;i<replenishBillDtls.size();i++){
                ReplenishBillDtl replenishBillDtl = replenishBillDtls.get(i);
                String key=replenishBillDtl.getStyleId()+","+replenishBillDtl.getColorId();
                //循环查询MergeReplenishBillDtl
                String hql="from MergeReplenishBillDtl t where t.id=?";
                MergeReplenishBillDtl MergeReplenishBillDtl = this.MergeReplenishBillDtlDao.findUnique(hql, new Object[]{key});
                if(CommonUtil.isNotBlank(MergeReplenishBillDtl)){
                    String allqyt = MergeReplenishBillDtl.getAllqyt();
                    int allqyts = Integer.parseInt(allqyt);
                    MergeReplenishBillDtl.setAllqyt(allqyts+replenishBillDtl.getQty()+"");
                    this.MergeReplenishBillDtlDao.update(MergeReplenishBillDtl);
                }else{
                    MergeReplenishBillDtl mergeReplenishBillDtl=new MergeReplenishBillDtl();
                    mergeReplenishBillDtl.setId(replenishBillDtl.getStyleId()+"_"+replenishBillDtl.getColorId());
                    mergeReplenishBillDtl.setColorid(replenishBillDtl.getColorId());
                    mergeReplenishBillDtl.setStyleid(replenishBillDtl.getStyleId());
                    mergeReplenishBillDtl.setAllqyt(replenishBillDtl.getQty()+"");
                    this.MergeReplenishBillDtlDao.save(MergeReplenishBillDtl);
                }
                //查询Recordsize
                String hqlRecordsize="from Recordsize t where t.recordid=? and t.sizeid=?";
                Recordsize recordsizes = this.replenishBillDao.findUnique(hqlRecordsize, new Object[]{key, replenishBillDtl.getSizeId()});
                if(CommonUtil.isNotBlank(recordsizes)){
                    String qty = recordsizes.getQty();


                }else{

                }


            }
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

   public Map<Boolean,String> checkReplenishBill(String replenishBillNo, User currentUser,String remark){
       ReplenishBill replenishBill = this.replenishBillDao.get(replenishBillNo);
       if(CommonUtil.isNotBlank(replenishBill)){
           replenishBill.setStatus(BillConstant.BillStatus.Check);
           this.replenishBillDao.update(replenishBill);
           CheckReplenishInfo checkReplenishInfo=new CheckReplenishInfo();
           checkReplenishInfo.setId(new GuidCreator().toString());
           checkReplenishInfo.setCheckType(BillConstant.BillStatus.Check);
           checkReplenishInfo.setReplenishBillNo(replenishBillNo);
           checkReplenishInfo.setHandlersId(currentUser.getId());
           checkReplenishInfo.setRemark(remark);
           checkReplenishInfo.setBillDate(new Date());
           this.checkReplenishInfoDao.save(checkReplenishInfo);
           Map<Boolean,String> map=new HashMap<Boolean,String>();
           map.put(true,"审核成功");
           return  map;
       }else{
           Map<Boolean,String> map=new HashMap<Boolean,String>();
           map.put(false,"审核没成功");
           return  map;
       }

   }

    public Map<Boolean,String> noCheckReplenishBill(String replenishBillNo, User currentUser,String remark){
        ReplenishBill replenishBill = this.replenishBillDao.get(replenishBillNo);
        if(CommonUtil.isNotBlank(replenishBill)){
            replenishBill.setStatus(BillConstant.BillStatus.noCheck);
            this.replenishBillDao.update(replenishBill);
            CheckReplenishInfo checkReplenishInfo=new CheckReplenishInfo();
            checkReplenishInfo.setId(new GuidCreator().toString());
            checkReplenishInfo.setCheckType(BillConstant.BillStatus.noCheck);
            checkReplenishInfo.setReplenishBillNo(replenishBillNo);
            checkReplenishInfo.setHandlersId(currentUser.getId());
            checkReplenishInfo.setRemark(remark);
            checkReplenishInfo.setBillDate(new Date());
            this.checkReplenishInfoDao.save(checkReplenishInfo);
            Map<Boolean,String> map=new HashMap<Boolean,String>();
            map.put(true,"反审核成功");
            return  map;
        }else{
            Map<Boolean,String> map=new HashMap<Boolean,String>();
            map.put(false,"审核没成功");
            return  map;
        }

    }

    public boolean changePurchase(String replenishBillNO,String userId){
        /*
            1.查询replenishBill和replenishBillDel的数据
            2.根据申请单类型，生成采购单或采购退货单
            3.根据商家replenishBillDel的sku做分组
            4.做保存和修改状态的操作
         */
        try {
            ReplenishBill replenishBill = this.replenishBillDao.get(replenishBillNO);
            User curUser = CacheManager.getUserById(userId);
            String delhql="from ReplenishBillDtl t where t.billId=?";
            List<ReplenishBillDtl> dels = this.replenishBillDao.find(delhql, new Object[]{replenishBillNO});
            if(replenishBill.getReplenishType().equals("1")){
                //生成采购单
                List<PurchaseOrderBill> savelist=new ArrayList<PurchaseOrderBill>();//存储采购单
                List<PurchaseOrderBillDtl> saveDelList=new ArrayList<PurchaseOrderBillDtl>();//存储采购详情单
                List<ChangeReplenishBillDtl> saveChangeList=new ArrayList<ChangeReplenishBillDtl>();
                BillConvertUtil.replenishBillcovertToPurchaseBill(replenishBill,dels,savelist,saveDelList,curUser,saveChangeList);
                Boolean issuccess=true;
                //更改申请单的数据
                for(ReplenishBillDtl del:dels){
                    if(del.getActConvertQty()+del.getConvertQty()>=del.getQty()){
                        del.setActConvertQty(del.getActConvertQty()+del.getConvertQty());
                        del.setConvertQty(0);
                        this.replenishBillDtlDao.update(del);

                    }else {
                        del.setActConvertQty(del.getActConvertQty()+del.getConvertQty());
                        del.setConvertQty(0);
                        this.replenishBillDtlDao.update(del);
                        issuccess=false;
                    }

                }
                if(issuccess){
                    replenishBill.setStatus(BillConstant.BillStatus.End);
                }else {
                    replenishBill.setStatus(BillConstant.BillStatus.Doing);
                }

                this.replenishBillDao.update(replenishBill);
                this.replenishBillDao.doBatchInsert(savelist);
                this.replenishBillDao.doBatchInsert(saveDelList);
                this.replenishBillDao.doBatchInsert(saveChangeList);
            }else{
                //生成采购单退货单
                List<PurchaseReturnBill> savelist=new ArrayList<PurchaseReturnBill>();//存储采购单
                List<PurchaseReturnBillDtl> saveDelList=new ArrayList<PurchaseReturnBillDtl>();//存储采购详情单
                List<ChangeReplenishBillDtl> saveChangeList=new ArrayList<ChangeReplenishBillDtl>();
                BillConvertUtil.replenishBillcovertToPurchaseReturnBill(replenishBill,dels,savelist,saveDelList,curUser,saveChangeList);
                Boolean issuccess=true;
                //更改申请单的数据
                for(ReplenishBillDtl del:dels){
                    if(del.getActConvertQty()+del.getConvertQty()>=del.getQty()){
                        del.setActConvertQty(del.getActConvertQty()+del.getConvertQty());
                        del.setConvertQty(0);
                        this.replenishBillDtlDao.update(del);

                    }else {
                        del.setActConvertQty(del.getActConvertQty()+del.getConvertQty());
                        del.setConvertQty(0);
                        this.replenishBillDtlDao.update(del);
                        issuccess=false;
                    }

                }
                if(issuccess){
                    replenishBill.setStatus(BillConstant.BillStatus.End);
                }else {
                    replenishBill.setStatus(BillConstant.BillStatus.Doing);
                }

                this.replenishBillDao.update(replenishBill);
                this.replenishBillDao.doBatchInsert(savelist);
                this.replenishBillDao.doBatchInsert(saveDelList);
                this.replenishBillDao.doBatchInsert(saveChangeList);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    public List<PurchaseOrderBill> findpurchaseOrderBillonReplenishBill(String billno){
        String hql="from PurchaseOrderBill where srcBillNo=?";
        List<PurchaseOrderBill> lists = this.replenishBillDao.find(hql, new Object[]{billno});
        return lists;
    }


    /**
     * add by yushen 补货单查询补单情况，数据库里查询出以SKU汇总的结果，然后根据款号分组，拼成StyleVO。note：没传session，在controller里设置图片路径
     */
    public List<ReplenishStyleVO> findReplenishStyleVO(String billNo){
        String getSkuVOHql = "select new com.casesoft.dmc.model.logistics.vo.ReplenishSkuVO" +
                "(rd.sku, rd.styleId, rd.colorId, rd.sizeId, rd.qty as skuTotQty, rd.actConvertQty as skuTotActConvertQty, COUNT(c.code) as skuTotInstockQty) " +
                "from ReplenishBill r, ReplenishBillDtl rd, PurchaseOrderBill p, BillRecord c, EpcStock s " +
                "where r.billNo=? and r.billNo = p.srcBillNo and p.billNo = c.billNo and rd.sku=c.sku and c.code = s.code " +
                "GROUP by rd.sku, rd.styleId, rd.colorId, rd.sizeId, rd.qty, rd.actConvertQty";
        List<ReplenishSkuVO> replenishSkuVOList = this.replenishBillDao.find(getSkuVOHql, billNo);

        String getCodeVOHql = "select new com.casesoft.dmc.model.logistics.vo.ReplenishCodeVO" +
                "(rd.sku, c.code, s.warehouseId) " +
                "from ReplenishBillDtl rd, PurchaseOrderBill p, BillRecord c, EpcStock s " +
                "where  rd.billNo=? and rd.billNo = p.srcBillNo and p.billNo = c.billNo and rd.sku=c.sku and c.code = s.code";
        List<ReplenishCodeVO> replenishCodeVOList = this.replenishBillDao.find(getCodeVOHql, billNo);
        // TODO: 2018/5/15  codeVOList 放入skuVOList



        Map<String, ReplenishStyleVO> styleVOMap = new HashMap<>();
        for (ReplenishSkuVO skuVO : replenishSkuVOList){
            String currentStyleId = skuVO.getStyleId();
            if(styleVOMap.containsKey(currentStyleId)){
                //累加sku中的数量，放入styleVO中
                styleVOMap.get(currentStyleId).setStyleTotQty(skuVO.getSkuTotQty() + styleVOMap.get(currentStyleId).getStyleTotQty());
                styleVOMap.get(currentStyleId).setStyleTotActConvertQty(skuVO.getSkuTotActConvertQty() + styleVOMap.get(currentStyleId).getStyleTotActConvertQty());
                styleVOMap.get(currentStyleId).setStyleTotInstockQty(skuVO.getSkuTotInstockQty() + styleVOMap.get(currentStyleId).getStyleTotInstockQty());

                styleVOMap.get(currentStyleId).getSkuVOList().add(skuVO);
            }else {
                ReplenishStyleVO newStyleVO = new ReplenishStyleVO();
                List<ReplenishSkuVO> newSubSkuVOList = new ArrayList<>();
                newSubSkuVOList.add(skuVO);
                newStyleVO.setStyleId(skuVO.getStyleId());
                Style style = CacheManager.getStyleById(skuVO.getStyleId());
                if(CommonUtil.isNotBlank(style)){
                    newStyleVO.setStyleName(style.getStyleName());
                }
                newStyleVO.setStyleTotQty(skuVO.getSkuTotQty());
                newStyleVO.setStyleTotActConvertQty(skuVO.getSkuTotActConvertQty());
                newStyleVO.setStyleTotInstockQty(skuVO.getSkuTotInstockQty());
                newStyleVO.setSkuVOList(newSubSkuVOList);
            }
        }
        return new ArrayList<>(styleVOMap.values());
    }
}
