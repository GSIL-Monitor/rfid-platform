package com.casesoft.dmc.service.logistics;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.product.StyleUtil;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.logistics.MergeReplenishBillDao;
import com.casesoft.dmc.dao.product.StyleDao;
import com.casesoft.dmc.model.logistics.*;
import com.casesoft.dmc.model.product.Style;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by Administrator on 2018/1/30.
 */
@Service
@Transactional
public class MergeReplenishBillService  implements IBaseService<MergeReplenishBill, String> {
    @Autowired
    private MergeReplenishBillDao mergeReplenishBillDao;
    @Autowired
    private StyleDao styleDao;
    @Override
    public Page<MergeReplenishBill> findPage(Page<MergeReplenishBill> page, List<PropertyFilter> filters) {
        return this.mergeReplenishBillDao.findPage(page,filters);
    }

    @Override
    public void save(MergeReplenishBill entity) {

    }

    @Override
    public MergeReplenishBill load(String id) {
        return this.mergeReplenishBillDao.load(id);
    }

    @Override
    public MergeReplenishBill get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<MergeReplenishBill> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<MergeReplenishBill> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(MergeReplenishBill entity) {

    }

    @Override
    public void delete(MergeReplenishBill entity) {

    }

    @Override
    public void delete(String id) {

    }

    public Map<String,Object> findMergeBillDetail(String billNo,HttpSession session){
        Map<String,Object> savemap=new HashMap<String,Object>();
        String hql="select t.sizeid from Recordsize t where t.billNo= ? group by t.sizeid";
        List<Object> objects = this.mergeReplenishBillDao.find(hql, billNo);
        System.out.println(objects);
        String hqldetle="from MergeReplenishBillDtl t where t.billNo=?";
        List<MergeReplenishBillDtl> MergeReplenishBillDtls = this.mergeReplenishBillDao.find(hqldetle, new Object[]{billNo});
        List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
        List<Map<String,String>> savelist=new ArrayList<Map<String,String>>();
        Map<String,String> mapUrl= new HashMap<String,String>();
        mapUrl.put("name","url");
        mapUrl.put("label","图片");
        mapUrl.put("width","20");
        mapUrl.put("hidden","true");
        mapUrl.put("sortable","false");
       /* String formatter="function (cellValue, options, rowObject) { if (rowObject.url ==  null) {  return \"无图片\";} else { return \"<img width=80 height=100 src='\" +basePath + rowObject.url + \"' alt='\" + rowObject.styleId + \"'/>\"; }}";
        mapUrl.put("formatter",formatter);*/
        savelist.add(mapUrl);
        Map<String,String> styleidmaps= new HashMap<String,String>();
        styleidmaps.put("name","styleid");
        styleidmaps.put("label","款号");
        savelist.add(styleidmaps);
        Map<String,String> coloridmaps= new HashMap<String,String>();
        coloridmaps.put("name","colorid");
        coloridmaps.put("label","颜色");
        savelist.add(coloridmaps);
        Map<String,String> Allqtymaps= new HashMap<String,String>();
        Allqtymaps.put("name","Allqty");
        Allqtymaps.put("label","总数");
        savelist.add(Allqtymaps);
        for(MergeReplenishBillDtl mergeReplenishBillDtl:MergeReplenishBillDtls){
             Map<String,Object> map=new HashMap<String,Object>();
             map.put("styleid",mergeReplenishBillDtl.getStyleid());
             map.put("colorid",mergeReplenishBillDtl.getColorid());
             map.put("Allqty",mergeReplenishBillDtl.getAllqyt());
            String rootPath = session.getServletContext().getRealPath("/");
           /* File file =  new File(rootPath + "/product/photo/" + mergeReplenishBillDtl.getStyleid());
            if(file.exists()){
                File[] files = file.listFiles();
                if(files.length > 0){
                    File[] photos = files[0].listFiles();
                    if(photos.length > 0){
                        File photo = new File(photos[0].getPath());
                        if(photo.getName().lastIndexOf(".")==-1){
                            //将无后缀名文件转为图片
                            File newFile = new File(photo.getParentFile(),photo.getName()+".png");
                            boolean flag = photo.renameTo(newFile);
                            if(flag) {
                                //d.setUrl("/product/photo/" + d.getStyleId() + "/" + files[0].getName() + "/" + newFile.getName());
                                map.put("url","/product/photo/" + mergeReplenishBillDtl.getStyleid() + "/" + files[0].getName() + "/" + newFile.getName());
                            }else{
                            }
                        }else{
                            //d.setUrl("/product/photo/" + d.getStyleId()+"/"+files[0].getName()+"/"+photos[0].getName());
                            map.put("url","/product/photo/" + mergeReplenishBillDtl.getStyleid() +"/"+files[0].getName()+"/"+photos[0].getName());
                        }
                    }
                }
            }else{
                map.put("url","");

            }*/
            String url = StyleUtil.returnImageUrl(mergeReplenishBillDtl.getStyleid(), rootPath);
            map.put("url",url);
            //根据sizeid分组查询结果
            for(Object object:objects){
                String hqlszides="from Recordsize t where t.billNo= ? and t.sizeid=? and t.recordid=?";
                Recordsize Recordsizes = this.mergeReplenishBillDao.findUnique(hqlszides, new Object[]{billNo,object+"",mergeReplenishBillDtl.getId()});

                if(CommonUtil.isNotBlank(Recordsizes)){
                    map.put(object+"",(Recordsizes.getQty()+"(实际),"+Recordsizes.getFranchiseeStockQty()+"(加盟商),"+Recordsizes.getStockQty()+"(在库),"+Recordsizes.getAlreadyChange()+"(已转换)"));
                   /* if(CommonUtil.isNotBlank(Recordsizes.getAlreadyChange())){
                        map.put(object+"alreadyChange",Recordsizes.getAlreadyChange());
                    }else{
                        map.put(object+"alreadyChange",0);
                    }if(CommonUtil.isNotBlank(Recordsizes.getNowChange())){
                        map.put(object+"nowChange",Recordsizes.getNowChange());
                    }else{
                        map.put(object+"nowChange",0);
                    }*/




                }else{
                    map.put(object+"",(0+"(实际),"+0+"(加盟商),"+0+"(在库),"+0+"(已转换)"));
                    /*map.put(object+"alreadyChange",0);
                    map.put(object+"nowChange",0);*/



                }


            }
            list.add(map);


        }

        for(Object object:objects){
            Map<String,String> maps= new HashMap<String,String>();
            maps.put("name",object+"");
            maps.put("label",object+"");
            savelist.add(maps);

          /*  Map<String,String> mapsalreadyChange= new HashMap<String,String>();
            mapsalreadyChange.put("label",object+"以转换的数量");
            mapsalreadyChange.put("name",object+"alreadyChange");
            savelist.add(mapsalreadyChange);
            Map<String,String> mapnowChange= new HashMap<String,String>();
            mapnowChange.put("label",object+"本次转换的数量");
            mapnowChange.put("name",object+"nowChange");
            savelist.add(mapnowChange);*/

        }

        savemap.put("key",savelist);
        savemap.put("result",list);

        return savemap;
    }

    public  List<Recordsize> findRecordsizeBybillNo(String billNo){
        String hql="from Recordsize t where t.billNo=? and t.isChange='N'";
        List<Recordsize> Recordsizes = this.mergeReplenishBillDao.find(hql, new Object[]{billNo});
        return Recordsizes;
    }

    public void saveoruodateRecordsizeList( List<Recordsize> Recordsizes){
        this.mergeReplenishBillDao.doBatchInsert(Recordsizes);
    }

    public Boolean changePurchase(String billNo){
        try {
            String hql="from Recordsize t where t.billNo=? and t.isChange='N' and t.nowChange>0";
            List<Recordsize> Recordsizes = this.mergeReplenishBillDao.find(hql, new Object[]{billNo});
            //得到供应商分组的情况
            String hqlgroupBy="select t.origUnitId from Recordsize t where t.billNo=? and t.isChange='N' and t.nowChange>0 group by t.origUnitId";
            List<Object> origUnitIds = this.mergeReplenishBillDao.find(hqlgroupBy, billNo);
            //根据供应商分组的情况来分割数据
            Map<String, List<Recordsize>> map =new HashMap<String, List<Recordsize>>();
            boolean issucecss=true;
            for(Object object:origUnitIds){
                List<Recordsize> list=new ArrayList<Recordsize>();
                for(Recordsize recordsize:Recordsizes){
                    if((object+"").equals(recordsize.getOrigUnitId())){
                        list.add(recordsize);
                    }

                }
                map.put(object+"",list);
            }
           //根据供应商分组的情况来保存单据
            List<PurchaseOrderBill> savePurchaseOrderBill=new ArrayList<PurchaseOrderBill>();
            List<PurchaseOrderBillDtl> savePurchaseOrderBillDtl=new ArrayList<PurchaseOrderBillDtl>();
            for(Object object:origUnitIds){
                PurchaseOrderBill purchaseOrderBill=new PurchaseOrderBill();
                String prefix = BillConstant.BillPrefix.purchase
                        + CommonUtil.getDateString(new Date(), "yyMMddHHmmssSSS");
                purchaseOrderBill.setId(prefix);
                purchaseOrderBill.setBillNo(prefix);
                purchaseOrderBill.setOrigUnitId(object+"");
                List<Recordsize> list=map.get(object+"");
                purchaseOrderBill.setOwnerId("1");
                purchaseOrderBill.setStatus(BillConstant.BillStatus.Enter);
                purchaseOrderBill.setOrigUnitName(list.get(0).getOrigUnitName());
                purchaseOrderBill.setBillDate(new Date());
                purchaseOrderBill.setDestId("AUTO_WH001");
                purchaseOrderBill.setDestName("总部仓库");
                Double allPrice=0.0D;
                Integer allsum=0;
                for(Recordsize recordsize:list){
                    PurchaseOrderBillDtl purchaseOrderBillDtl=new PurchaseOrderBillDtl();
                    purchaseOrderBillDtl.setId(new GuidCreator().toString());
                    purchaseOrderBillDtl.setId(new GuidCreator().toString());
                    purchaseOrderBillDtl.setBillId(purchaseOrderBill.getId());
                    purchaseOrderBillDtl.setBillNo(purchaseOrderBill.getBillNo());
                    purchaseOrderBillDtl.setActPrintQty(0);
                    purchaseOrderBillDtl.setQty(Long.parseLong(recordsize.getNowChange()+""));
                    purchaseOrderBillDtl.setActQty(Long.parseLong(recordsize.getNowChange()+""));
                    purchaseOrderBillDtl.setInQty(0);
                    String recordid = recordsize.getRecordid();
                    String[] split = recordid.split("_");
                    purchaseOrderBillDtl.setStyleId(split[0]);
                    purchaseOrderBillDtl.setColorId(split[1]);
                    purchaseOrderBillDtl.setSizeId(recordsize.getSizeid());
                    purchaseOrderBillDtl.setSku(split[0]+split[1]+recordsize.getSizeid());
                    purchaseOrderBillDtl.setInStockType("BH");
                    Style styleById = CacheManager.getStyleById(split[0]);
                    purchaseOrderBillDtl.setPrice(styleById.getPreCast());
                    purchaseOrderBillDtl.setTotPrice(styleById.getPreCast()*recordsize.getNowChange());
                    purchaseOrderBillDtl.setActPrice(styleById.getPreCast());
                    purchaseOrderBillDtl.setTotActPrice(styleById.getPreCast()*recordsize.getNowChange());
                    purchaseOrderBillDtl.setActPrintQty(0);
                    purchaseOrderBillDtl.setPrintQty(Integer.parseInt(recordsize.getNowChange()+""));
                    allPrice+=styleById.getPreCast()*recordsize.getNowChange();
                    allsum+=Integer.parseInt(recordsize.getNowChange()+"");
                    savePurchaseOrderBillDtl.add(purchaseOrderBillDtl);
                    //更改recordsize的状态
                    if(Integer.parseInt(recordsize.getQty())==(recordsize.getNowChange()+recordsize.getAlreadyChange())){
                        String updatehql="update Recordsize t set t.isChange='Y',t.alreadyChange=?,t.nowChange=0 where t.id=?";
                        this.mergeReplenishBillDao.batchExecute(updatehql, Integer.parseInt(recordsize.getQty()),recordsize.getId());
                    }
                    if(Integer.parseInt(recordsize.getQty())>(recordsize.getNowChange()+recordsize.getAlreadyChange())){
                        String updatehql="update Recordsize t set t.alreadyChange=?, t.nowChange=0 where t.id=?";
                        this.mergeReplenishBillDao.batchExecute(updatehql, recordsize.getNowChange()+recordsize.getAlreadyChange(),recordsize.getId());
                        issucecss=false;
                    }

                }
                purchaseOrderBill.setTotQty(Long.parseLong(allsum+""));
                purchaseOrderBill.setPayPrice(allPrice);
                savePurchaseOrderBill.add(purchaseOrderBill);
            }
            this.mergeReplenishBillDao.doBatchInsert(savePurchaseOrderBill);
            this.mergeReplenishBillDao.doBatchInsert(savePurchaseOrderBillDtl);
            if(issucecss){
                String updatehql="update MergeReplenishBill t set t.status=? where t.id=?";
                this.mergeReplenishBillDao.batchExecute(updatehql, BillConstant.BillStatus.End,billNo);
            }else{
                String updatehql="update MergeReplenishBill t set t.status=? where t.id=?";
                this.mergeReplenishBillDao.batchExecute(updatehql, BillConstant.BillStatus.Doing,billNo);
            }
            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    public Boolean cancelbill(String billNo){
        try {
            MergeReplenishBill mergeReplenishBill = this.mergeReplenishBillDao.load(billNo);
            mergeReplenishBill.setStatus(BillConstant.BillStatus.Cancel);
            String updatehql="update ReplenishBill t set t.srcBillNo='', t.status=? where t.srcBillNo=?";
            this.mergeReplenishBillDao.batchExecute(updatehql, BillConstant.BillStatus.Enter,billNo);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
