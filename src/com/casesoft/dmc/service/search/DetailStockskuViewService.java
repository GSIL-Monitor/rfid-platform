package com.casesoft.dmc.service.search;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.search.DetailStockCodeViewDao;
import com.casesoft.dmc.dao.search.DetailStockskuViewDao;
import com.casesoft.dmc.dao.stock.EpcStockDao;
import com.casesoft.dmc.extend.api.wechatShop.model.Wxshoppramer;
import com.casesoft.dmc.extend.third.request.BaseService;
import com.casesoft.dmc.model.logistics.BillConstant;
import com.casesoft.dmc.model.logistics.BillRecord;
import com.casesoft.dmc.model.logistics.SaleOrderBill;
import com.casesoft.dmc.model.logistics.SaleOrderBillDtl;
import com.casesoft.dmc.model.product.PaymentMessage;
import com.casesoft.dmc.model.search.DetailStockView;
import com.casesoft.dmc.model.search.DetailStockskuView;
import com.casesoft.dmc.model.stock.EpcStock;
import com.casesoft.dmc.model.sys.Unit;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by Administrator on 2018/1/8.
 */
@Service
@Transactional
public class DetailStockskuViewService extends BaseService<DetailStockView, String> {
    @Autowired
    private DetailStockskuViewDao detailStockskuViewDao;
    @Autowired
    private DetailStockCodeViewDao detailStockCodeViewDao;
    @Autowired
    private EpcStockDao epcStockDao;

    @Override
    public Page<DetailStockView> findPage(Page<DetailStockView> page, List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public void save(DetailStockView entity) {

    }

    @Override
    public DetailStockView load(String id) {
        return null;
    }

    @Override
    public DetailStockView get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<DetailStockView> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<DetailStockView> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(DetailStockView entity) {

    }

    @Override
    public void delete(DetailStockView entity) {

    }

    @Override
    public void delete(String id) {

    }

    public List<Map<String,Object>> findSkuStock(PaymentMessage paymentMessage,List <Wxshoppramer>list,String customerId,String customerName,String discount){
        String skus="";
        String weraIds="";
        String alreadyweraIds="";
        Map<String,String> savealreadyweraIdsmap=new HashMap<String,String>();//保存对应sku已经查询的仓库id
        boolean iswhile=false;//判断是否循环查看订单货物
        for(int i=0;i<list.size();i++){
           if(i==0){
               skus+="'"+list.get(i).getSku()+"'";
               weraIds+="'"+list.get(i).getWeraId()+"'";
           }else{
               skus+=",'"+list.get(i).getSku()+"'";
               weraIds+=",'"+list.get(i).getWeraId()+"'";
           }
        }
        /*
           1.查询说有的对应库存的sku
           2.和商城得到的数据做比较
           （
               1.1有则且数量足够该仓库出
               1.2有且数量数量却不够，部分从该仓库出，不够的从其他的仓库出
               2.1没有则查询该sku，在数量多的仓库出
           ）
           4.生成新的单据信息（对应的code，时间等信息）
           5.改变对应code的状态
         */
        String hql="from DetailStockskuView t where t.sku in("+skus+")";
        List<DetailStockskuView> StockskuList = this.detailStockskuViewDao.find(hql);
        //区分是否从本店出的货物
        List<Wxshoppramer> thiswarehIdList=new ArrayList<Wxshoppramer>();
        List<Wxshoppramer> nothiswarehIdList=new ArrayList<Wxshoppramer>();
        boolean isok=true;
        List<Map<String, Object>> sendList = new ArrayList<Map<String, Object>>();
        if(StockskuList.size()==0){
            String skusno = "";
            Map<String, Object> dtlmap = new HashMap<String, Object>();
            for (Wxshoppramer wxshoppramer : list) {
                skusno += wxshoppramer.getSku() + "  ";
            }
            dtlmap.put("mes", skusno);
            sendList.add(dtlmap);

        }else {
            for (Wxshoppramer wxshoppramer : list) {
                for (DetailStockskuView detailStockskuView : StockskuList) {

                    if (wxshoppramer.getSku().equals(detailStockskuView.getSku()) && wxshoppramer.getWeraId().equals(detailStockskuView.getWarehId())) {
                        String wxshopp = wxshoppramer.getQty();
                        Integer detail = detailStockskuView.getQty();
                        if (detail >= Integer.parseInt(wxshopp)) {
                            //wxshoppramer.setPrice(detailStockskuView.getPrice());
                            wxshoppramer.setOwnerId(detailStockskuView.getOwnerId());
                            wxshoppramer.setColorId(detailStockskuView.getColorId());
                            wxshoppramer.setSizeId(detailStockskuView.getSizeId());
                            wxshoppramer.setStyleId(detailStockskuView.getStyleId());
                            //wxshoppramer.setPrice(wxshoppramer.getPrice());
                            wxshoppramer.setTotPrice((wxshoppramer.getPrice() * Integer.parseInt(wxshoppramer.getQty())) + "");
                            thiswarehIdList.add(wxshoppramer);

                        } else {
                           /*String WeraIds=savealreadyweraIdsmap.get(wxshoppramer.getSku());
                            if(CommonUtil.isBlank(WeraIds)){
                                savealreadyweraIdsmap.put(wxshoppramer.getSku(),"'"+wxshoppramer.getWeraId()+"'");
                            }else{
                                WeraIds+=",'"+wxshoppramer.getWeraId()+"'";
                                savealreadyweraIdsmap.put(wxshoppramer.getSku(),WeraIds);
                            }*/

                            Wxshoppramer newwxshoppramer = new Wxshoppramer();
                            newwxshoppramer.setQty((Integer.parseInt(wxshoppramer.getQty()) - detailStockskuView.getQty()) + "");
                            newwxshoppramer.setSku(wxshoppramer.getSku());
                            newwxshoppramer.setWeraId(wxshoppramer.getWeraId());
                            newwxshoppramer.setPrice(wxshoppramer.getPrice());
                            newwxshoppramer.setTotPrice((newwxshoppramer.getPrice() * Integer.parseInt(newwxshoppramer.getQty())) + "");
                            nothiswarehIdList.add(newwxshoppramer);
                            Wxshoppramer nowxshoppramer = new Wxshoppramer();
                            nowxshoppramer.setQty(detailStockskuView.getQty() + "");
                            nowxshoppramer.setSku(wxshoppramer.getSku());
                            nowxshoppramer.setWeraId(wxshoppramer.getWeraId());
                            nowxshoppramer.setPrice(wxshoppramer.getPrice());
                            nowxshoppramer.setOwnerId(detailStockskuView.getOwnerId());
                            nowxshoppramer.setColorId(detailStockskuView.getColorId());
                            nowxshoppramer.setSizeId(detailStockskuView.getSizeId());
                            nowxshoppramer.setStyleId(detailStockskuView.getStyleId());
                            nowxshoppramer.setTotPrice((wxshoppramer.getPrice() * detailStockskuView.getQty()) + "");
                            thiswarehIdList.add(nowxshoppramer);
                            iswhile=true;
                        }
                        isok = true;
                       if (alreadyweraIds.indexOf(wxshoppramer.getWeraId()) != -1) {

                        } else {
                            if (CommonUtil.isNotBlank(alreadyweraIds)) {
                                alreadyweraIds += ",'" + wxshoppramer.getWeraId() + "'";
                            } else {
                                alreadyweraIds += "'" + wxshoppramer.getWeraId() + "'";
                            }

                        }
                       //Wxshoppramer wxshoppramer = nothiswarehIdList.get(i);
                        String WeraIds=savealreadyweraIdsmap.get(wxshoppramer.getSku());
                        if(CommonUtil.isBlank(WeraIds)){
                            savealreadyweraIdsmap.put(wxshoppramer.getSku(),"'"+wxshoppramer.getWeraId()+"'");
                        }else{
                            if(WeraIds.indexOf(wxshoppramer.getWeraId())!= -1){

                            }else{
                                WeraIds+=",'"+wxshoppramer.getWeraId()+"'";
                                savealreadyweraIdsmap.put(wxshoppramer.getSku(),WeraIds);
                            }

                        }


                        break;
                    } else {
                       /* if (alreadyweraIds.indexOf(wxshoppramer.getWeraId()) != -1) {

                        } else {
                            if (CommonUtil.isNotBlank(alreadyweraIds)) {
                                alreadyweraIds += ",'" + wxshoppramer.getWeraId() + "'";
                            } else {
                                alreadyweraIds += "'" + wxshoppramer.getWeraId() + "'";
                            }

                        }*/

                        isok = false;
                    }
                }
                if (!isok) {
                   /* String WeraIds=savealreadyweraIdsmap.get(wxshoppramer.getSku());
                    if(CommonUtil.isBlank(WeraIds)){
                        savealreadyweraIdsmap.put(wxshoppramer.getSku(),"'"+wxshoppramer.getWeraId()+"'");
                    }else{
                        WeraIds+=",'"+wxshoppramer.getWeraId()+"'";
                        savealreadyweraIdsmap.put(wxshoppramer.getSku(),WeraIds);
                    }
                    wxshoppramer.setWeraId("");*/
                    nothiswarehIdList.add(wxshoppramer);
                    iswhile=true;
                }

            }

            //循环查看订单货物，门店是否有
            while (iswhile && nothiswarehIdList.size() != 0) {
                Map<String, Object> stringObjectMap = whileDetailStockskuView(nothiswarehIdList, thiswarehIdList, alreadyweraIds,savealreadyweraIdsmap);
                iswhile = (boolean) stringObjectMap.get("isok");
                nothiswarehIdList = (List<Wxshoppramer>) stringObjectMap.get("nowarehIdList");
                alreadyweraIds=(String)stringObjectMap.get("alreadyweraIds");

            }

            //保存商城的单据
            if (!iswhile && nothiswarehIdList.size() == 0) {

                //根据仓库id分组
                String[] split = alreadyweraIds.split(",");
                //List<SaleOrderBill> listsale = new ArrayList<SaleOrderBill>();
                //List<SaleOrderBillDtl> snedlistsaleDtl = new ArrayList<SaleOrderBillDtl>();
                //List<SaleOrderBillDtl> listsaleDtl = new ArrayList<SaleOrderBillDtl>();
                List<BillRecord> listBillRecord = new ArrayList<BillRecord>();
                //Map<String,List<SaleOrderBillDtl>> map=new HashMap<String,List<SaleOrderBillDtl>>();
                //根据仓库id生成销售单
                for (int i = 0; i < split.length; i++) {
                    List<SaleOrderBill> listsale = new ArrayList<SaleOrderBill>();
                    List<SaleOrderBillDtl> listsaleDtl = new ArrayList<SaleOrderBillDtl>();
                    List<SaleOrderBillDtl> snedlistsaleDtl = new ArrayList<SaleOrderBillDtl>();
                    List<PaymentMessage> listpaymentMessage = new ArrayList<PaymentMessage>();
                    String weraId = split[i].replace("'", "");
                    String prefix = BillConstant.BillPrefix.saleOrder
                            + CommonUtil.getDateString(new Date(), "yyMMddHHmmssSSS");
                    Long totQty = 0L;
                    Long actQty = 0L;
                    Integer totOutQty = 0;
                    Integer totInQty = 0;
                    Double totPrice = 0D;
                    Double actPrice = 0D;
                    Double totOutVal = 0D;
                    Double totInVal = 0D;
                    String ownerId = "";
                    Map<String, Object> dtlmap = new HashMap<String, Object>();
                    if (thiswarehIdList.size() != 0) {
                        for (Wxshoppramer wxshoppramers : thiswarehIdList) {

                            List<SaleOrderBillDtl> listDtl = new ArrayList<SaleOrderBillDtl>();
                            if (weraId.equals(wxshoppramers.getWeraId())) {
                                ownerId = wxshoppramers.getOwnerId();
                                SaleOrderBillDtl saleOrderBillDtl = new SaleOrderBillDtl();
                                saleOrderBillDtl.setId(new GuidCreator().toString());
                                saleOrderBillDtl.setBillId(prefix);
                                saleOrderBillDtl.setBillNo(prefix);
                                saleOrderBillDtl.setPrice(wxshoppramers.getPrice());
                                saleOrderBillDtl.setActPrice(wxshoppramers.getPrice() * Integer.parseInt(wxshoppramers.getQty()));
                                saleOrderBillDtl.setTotActPrice(wxshoppramers.getPrice() * Integer.parseInt(wxshoppramers.getQty()));
                                saleOrderBillDtl.setTotPrice(wxshoppramers.getPrice() * Integer.parseInt(wxshoppramers.getQty()));
                                saleOrderBillDtl.setColorId(wxshoppramers.getColorId());
                                saleOrderBillDtl.setSizeId(wxshoppramers.getSizeId());
                                saleOrderBillDtl.setStyleId(wxshoppramers.getStyleId());
                                saleOrderBillDtl.setSku(wxshoppramers.getSku());
                                Long aLong = Long.parseLong(wxshoppramers.getQty());
                                saleOrderBillDtl.setQty(aLong);
                                saleOrderBillDtl.setActQty(Long.getLong(wxshoppramers.getQty()));
                                saleOrderBillDtl.setOutQty(0);
                                saleOrderBillDtl.setInQty(0);
                                saleOrderBillDtl.setReturnQty(0);
                                totQty += saleOrderBillDtl.getQty();
                                actQty += saleOrderBillDtl.getQty();
                                totPrice += saleOrderBillDtl.getPrice() * saleOrderBillDtl.getQty();
                                actPrice += saleOrderBillDtl.getPrice() * saleOrderBillDtl.getQty();
                                totOutQty += saleOrderBillDtl.getOutQty();
                                totInQty += saleOrderBillDtl.getInQty();
                                totOutVal = totOutQty * saleOrderBillDtl.getActPrice();
                                totInVal = totInQty * saleOrderBillDtl.getActPrice();
                                listsaleDtl.add(saleOrderBillDtl);
                                snedlistsaleDtl.add(saleOrderBillDtl);
                                dtlmap.put("del", snedlistsaleDtl);
                                String codehql = "from EpcStock t where t.sku =? and t.warehouseId=? and t.inStock=1";
                                List<EpcStock> EpcStocklist = this.epcStockDao.find(codehql, new Object[]{wxshoppramers.getSku(), wxshoppramers.getWeraId()});
                                int qty = Integer.parseInt(wxshoppramers.getQty());
                                String codes = "";
                                for (int c = 0; c < qty; c++) {
                                    EpcStock epcStock = EpcStocklist.get(c);
                                    if (c == 0) {
                                        codes += "'" + epcStock.getCode() + "'";
                                    } else {
                                        codes += ",'" + epcStock.getCode() + "'";
                                    }
                                    BillRecord billRecord = new BillRecord(prefix + "-" + EpcStocklist.get(c).getCode(), EpcStocklist.get(c).getCode(), prefix, wxshoppramers.getSku());
                                    listBillRecord.add(billRecord);
                                }
                                String updatehql = "update EpcStock t  set t.inStock =2 where t.code in(" + codes + ")";
                                this.epcStockDao.batchExecute(updatehql);


                            }
                        }
                        SaleOrderBill saleOrderBill = new SaleOrderBill();

                        //String billNo = this.saleOrderBillService.findMaxBillNo(prefix);
                        saleOrderBill.setId(prefix);
                        dtlmap.put("id", prefix);
                        saleOrderBill.setBillNo(prefix);
                        dtlmap.put("billNo", prefix);
                        saleOrderBill.setOrigId(weraId);
                        //GuestView load = this.guestViewService.findByid(weraId);
                        Unit load = CacheManager.getUnitById(weraId);
                        if(CommonUtil.isNotBlank(load)){
                            saleOrderBill.setOrigName(load.getName());
                        }
                        dtlmap.put("origId", weraId);
                        saleOrderBill.setOwnerId(ownerId);
                        saleOrderBill.setOrigUnitId(ownerId);
                        Unit loads = CacheManager.getUnitById(ownerId);
                        if(CommonUtil.isNotBlank(loads)){
                            saleOrderBill.setOrigUnitName(loads.getName());
                        }

                        dtlmap.put("ownerId", weraId);
                        saleOrderBill.setBillDate(new Date());
                        dtlmap.put("billDate", saleOrderBill.getBillDate());
                        saleOrderBill.setDestUnitId(customerId);
                        dtlmap.put("destUnitId", customerId);
                        saleOrderBill.setDestUnitName(customerName);
                        dtlmap.put("destUnitName", customerName);
                        saleOrderBill.setTotQty(totQty);
                        dtlmap.put("totQty", totQty);
                        saleOrderBill.setTotPrice(totPrice);
                        dtlmap.put("totPrice", totPrice);
                        saleOrderBill.setActQty(actQty);
                        dtlmap.put("actQty", actQty);
                        saleOrderBill.setActPrice(actPrice);
                        saleOrderBill.setTotOutQty(totOutQty.longValue());
                        dtlmap.put("totOutQty", totOutQty.longValue());
                        saleOrderBill.setTotInQty(totInQty.longValue());
                        dtlmap.put("totInQty", totInQty.longValue());
                        saleOrderBill.setTotOutVal(totOutVal);
                        dtlmap.put("totOutVal", totOutVal);
                        saleOrderBill.setTotInVal(totInVal);
                        dtlmap.put("totInVal", totInVal);
                        saleOrderBill.setRemark("商城单据");
                        dtlmap.put("remark", "商城单据");
                        saleOrderBill.setCustomerTypeId("CT-LS");
                        //saleOrderBill.setStatus(BillConstant.BillStatus.shophold);
                        saleOrderBill.setStatus(BillConstant.BillStatus.shophold);
                        saleOrderBill.setPayPrice(saleOrderBill.getActPrice());
                        PaymentMessage newpaymentMessage=new PaymentMessage();
                        BeanUtils.copyProperties(paymentMessage,newpaymentMessage);
                        newpaymentMessage.setId(new GuidCreator().toString());
                        newpaymentMessage.setBillNo(saleOrderBill.getBillNo());
                        listpaymentMessage.add(newpaymentMessage);
                        listsale.add(saleOrderBill);
                        sendList.add(dtlmap);
                    }
                    this.detailStockskuViewDao.doBatchInsert(listpaymentMessage);
                    this.detailStockskuViewDao.doBatchInsert(listsale);
                    this.detailStockskuViewDao.doBatchInsert(listsaleDtl);
                    this.detailStockskuViewDao.doBatchInsert(listBillRecord);
                }

                return sendList;
            }
            //保存商城的单据失败，还有剩余商品
            if (!iswhile && nothiswarehIdList.size() != 0) {

                String skusno = "";
                Map<String, Object> dtlmap = new HashMap<String, Object>();
                for (Wxshoppramer wxshoppramer : nothiswarehIdList) {
                    skusno += wxshoppramer.getSku() + "  ";
                }
                dtlmap.put("mes", skusno);
                sendList.add(dtlmap);
                return sendList;
            }
        }

        return sendList;

    }

    public Map<String,Object>  whileDetailStockskuView( List<Wxshoppramer> nothiswarehIdList,  List<Wxshoppramer> thiswarehIdList, String alreadyweraIds,Map<String,String> savealreadyweraIdsmap){
        List<Wxshoppramer> nowarehIdList=new ArrayList<Wxshoppramer>();
        Map<String,Object> map=new HashMap<String,Object>();

        boolean ishave=true;
        List<Wxshoppramer> nohavewarehIdList=new ArrayList<Wxshoppramer>();
        //对应每个sku做处理
        for(int i=0;i<nothiswarehIdList.size();i++){
            String nohql="";
            String skualreadyweraIds = savealreadyweraIdsmap.get(nothiswarehIdList.get(i).getSku());
            if(CommonUtil.isBlank(skualreadyweraIds)){
                nohql="from DetailStockView t where t.sku ='"+nothiswarehIdList.get(i).getSku()+"' order by t.qty desc";
            }else{
                nohql="from DetailStockView t where t.sku ='"+nothiswarehIdList.get(i).getSku()+"' and t.warehId not in("+skualreadyweraIds+") order by t.qty desc";
            }
            List<DetailStockView> noStockskuList = this.detailStockskuViewDao.find(nohql);
            if(noStockskuList.size()==0){
                ishave=false;
                nohavewarehIdList.add(nothiswarehIdList.get(i));
            }else {
                String warehIqty = nothiswarehIdList.get(i).getQty();
                Integer Stockskuqty = noStockskuList.get(0).getQty();
                if (Stockskuqty >= Integer.parseInt(warehIqty)) {
                    Wxshoppramer wxshoppramer = nothiswarehIdList.get(i);
                    Wxshoppramer wxnewshoppramer=new Wxshoppramer();
                    wxnewshoppramer.setSku(wxshoppramer.getSku());
                    wxnewshoppramer.setWeraId(noStockskuList.get(0).getWarehId());
                    //wxshoppramer.setPrice(nothiswarehIdList.get(i).getPrice());
                    wxnewshoppramer.setOwnerId(noStockskuList.get(0).getOwnerId());
                    wxnewshoppramer.setColorId(noStockskuList.get(0).getColorId());
                    wxnewshoppramer.setSizeId(noStockskuList.get(0).getSizeId());
                    wxnewshoppramer.setStyleId(noStockskuList.get(0).getStyleId());
                    wxnewshoppramer.setPrice(wxshoppramer.getPrice());
                    wxnewshoppramer.setQty(wxshoppramer.getQty());
                    wxnewshoppramer.setTotPrice((wxshoppramer.getPrice() * Integer.parseInt(wxshoppramer.getQty())) + "");
                    thiswarehIdList.add(wxnewshoppramer);
                    if (alreadyweraIds.indexOf(noStockskuList.get(0).getWarehId()) != -1) {

                    } else {
                        if (CommonUtil.isNotBlank(alreadyweraIds)) {
                            alreadyweraIds += ",'" + noStockskuList.get(0).getWarehId() + "'";
                        } else {
                            alreadyweraIds += "'" + noStockskuList.get(0).getWarehId() + "'";
                        }

                    }
                    String WeraIds = savealreadyweraIdsmap.get(wxshoppramer.getSku());
                    if (CommonUtil.isBlank(WeraIds)) {
                        savealreadyweraIdsmap.put(wxshoppramer.getSku(), "'" + noStockskuList.get(0).getWarehId() + "'");
                    } else {
                        WeraIds += ",'" +noStockskuList.get(0).getWarehId() + "'";
                        savealreadyweraIdsmap.put(wxshoppramer.getSku(), WeraIds);
                    }

                } else {
                    Wxshoppramer wxshoppramer = nothiswarehIdList.get(i);
                    Wxshoppramer wxnewnoshoppramer=new Wxshoppramer();
                    wxnewnoshoppramer.setSku(wxshoppramer.getSku());
                    wxnewnoshoppramer.setWeraId(wxshoppramer.getWeraId());
                    //wxshoppramer.setPrice(nothiswarehIdList.get(i).getPrice());
                    wxnewnoshoppramer.setOwnerId(wxshoppramer.getOwnerId());
                    wxnewnoshoppramer.setColorId(wxshoppramer.getColorId());
                    wxnewnoshoppramer.setSizeId(wxshoppramer.getSizeId());
                    wxnewnoshoppramer.setStyleId(wxshoppramer.getStyleId());
                    wxnewnoshoppramer.setPrice(wxshoppramer.getPrice());
                    wxnewnoshoppramer.setTotPrice(wxshoppramer.getTotPrice());
                    String WeraIds = savealreadyweraIdsmap.get(wxshoppramer.getSku());
                    if (CommonUtil.isBlank(WeraIds)) {
                        savealreadyweraIdsmap.put(wxshoppramer.getSku(), "'" + noStockskuList.get(0).getWarehId() + "'");
                    } else {
                        if(WeraIds.indexOf(noStockskuList.get(0).getWarehId())!= -1){

                        }else{
                            WeraIds += ",'" + noStockskuList.get(0).getWarehId() + "'";
                            savealreadyweraIdsmap.put(wxshoppramer.getSku(), WeraIds);
                        }

                    }
                    if (alreadyweraIds.indexOf(noStockskuList.get(0).getWarehId()) != -1) {

                    } else {
                        if (CommonUtil.isNotBlank(alreadyweraIds)) {
                            alreadyweraIds += ",'" + noStockskuList.get(0).getWarehId() + "'";
                        } else {
                            alreadyweraIds += "'" + noStockskuList.get(0).getWarehId() + "'";
                        }

                    }
                    wxnewnoshoppramer.setQty((Integer.parseInt(warehIqty) - Stockskuqty) + "");
                    nowarehIdList.add(wxnewnoshoppramer);
                    //处理有的情况
                    Wxshoppramer wxshoppramers = nothiswarehIdList.get(i);
                    Wxshoppramer wxnewshoppramer=new Wxshoppramer();
                    wxnewshoppramer.setSku(wxshoppramers.getSku());
                    wxnewshoppramer.setWeraId(noStockskuList.get(0).getWarehId());
                    //wxshoppramer.setPrice(nothiswarehIdList.get(i).getPrice());
                    wxnewshoppramer.setOwnerId(noStockskuList.get(0).getOwnerId());
                    wxnewshoppramer.setColorId(noStockskuList.get(0).getColorId());
                    wxnewshoppramer.setSizeId(noStockskuList.get(0).getSizeId());
                    wxnewshoppramer.setStyleId(noStockskuList.get(0).getStyleId());
                    wxnewshoppramer.setPrice(wxshoppramer.getPrice());
                    wxnewshoppramer.setQty(Stockskuqty+"");
                    wxnewshoppramer.setTotPrice((wxshoppramer.getPrice() *Stockskuqty) + "");
                    thiswarehIdList.add(wxnewshoppramer);
                }
            }




        }
        if(nowarehIdList.size()!=0&&ishave){
            //单据货物的库存还没有查好

            map.put("isok",true);
            map.put("nowarehIdList",nowarehIdList);
            map.put("alreadyweraIds",alreadyweraIds);
            map.put("savealreadyweraIdsmap",savealreadyweraIdsmap);

        }
        if(nowarehIdList.size()==0&&ishave){
            //单据货物的库存都已查好
            map.put("isok",false);
            map.put("nowarehIdList",nowarehIdList);
            map.put("alreadyweraIds",alreadyweraIds);
            map.put("savealreadyweraIdsmap",savealreadyweraIdsmap);

        }
        if(!ishave){
            map.put("isok",false);
            map.put("nowarehIdList",nohavewarehIdList);
            map.put("alreadyweraIds",alreadyweraIds);
            map.put("savealreadyweraIdsmap",savealreadyweraIdsmap);
        }
        return map;
    }


    public void updateSalestate(List <PaymentMessage> list){
        String billNos="";
        for(int i=0;i<list.size();i++){
            list.get(i).setId(new GuidCreator().toString());
            if(i==0){
                billNos+="'"+list.get(i).getBillNo()+"'";
            }else{
                billNos+=",'"+list.get(i).getBillNo()+"'";
            }
        }
       // String hql="update SaleOrderBill t set t.status="+BillConstant.BillStatus.shopEnter+" where t.id in("+billNos+")";
        String hql="from SaleOrderBill t  where t.id in("+billNos+")";
        List<SaleOrderBill> SaleOrderBilllist = this.detailStockCodeViewDao.find(hql);
        for(SaleOrderBill saleOrderBill:SaleOrderBilllist){
            saleOrderBill.setStatus(BillConstant.BillStatus.shopEnter);
            saleOrderBill.setPayPrice(saleOrderBill.getActPrice());
        }
        //this.detailStockCodeViewDao.batchExecute(hql);
        this.detailStockCodeViewDao.doBatchInsert(list);
        this.detailStockCodeViewDao.doBatchInsert(SaleOrderBilllist);

    }

}
