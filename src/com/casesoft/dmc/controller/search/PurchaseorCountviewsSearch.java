package com.casesoft.dmc.controller.search;

import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.json.JSONUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.dao.search.PurchaseorCountDao;
import com.casesoft.dmc.model.logistics.BillConstant;
import com.casesoft.dmc.model.search.*;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.logistics.PurchaseOrderBillService;
import com.casesoft.dmc.service.sys.impl.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/11.
 */
@Controller
@RequestMapping("/search/PurchaseorCountviews")
public class PurchaseorCountviewsSearch extends BaseController {
    @Autowired
    PurchaseorCountDao purchaseorCountDao;
    @Autowired
    private UnitService unitService;
    @Autowired
    private PurchaseOrderBillService purchaseOrderBillService;
    @Override
    public String index() {
        return null;
    }
    @RequestMapping(value = "/index")
    public ModelAndView indexMV() throws Exception {
        ModelAndView mv = new ModelAndView("/views/search/PurchaseorCountviewsSearch");
        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        User currentUser = getCurrentUser();
        Unit unit = this.unitService.getunitbyId(getCurrentUser().getOwnerId());
        mv.addObject("deportId", unit.getDefaultWarehId());
        Unit unit1 = this.unitService.getunitbyId(unit.getDefaultWarehId());
        mv.addObject("deportName", unit1.getName());
        mv.addObject("roleid", currentUser.getRoleId());
        return mv;
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public @ResponseBody
    DataSourceResult read(@RequestBody DataSourceRequest request) {

        DataSourceResult dataResult = purchaseorCountDao.getList(request);
        String rootPath = session.getServletContext().getRealPath("/");
       List<?> data = dataResult.getData();
        List<PurchaseorCountviews> datanew =new ArrayList<PurchaseorCountviews>();
        for(int i=0;i<data.size();i++){
            PurchaseorCountviews purchaseorCountviews = (PurchaseorCountviews) data.get(i);
            String billno = purchaseorCountviews.getBillid();
            if(billno.contains(BillConstant.BillPrefix.purchase)){
                purchaseorCountviews.setSaletype("采购订单");
            }
            if(billno.contains(BillConstant.BillPrefix.purchaseReturn)){
                purchaseorCountviews.setSaletype("采购退货订单");
                Integer qty = purchaseorCountviews.getQty();
                purchaseorCountviews.setQty(Integer.parseInt("-"+qty));
            }
            File file =  new File(rootPath + "/product/photo/" + purchaseorCountviews.getStyleid());
            if(file.exists()){
                File[] files = file.listFiles();
                if(files.length > 0){
                    File[] photos = files[0].listFiles();
                    if(photos.length > 0){
                        purchaseorCountviews.setUrl("/product/photo/" + purchaseorCountviews.getStyleid()+"/"+files[0].getName()+"/"+photos[0].getName());
                    }
                }
            }
            datanew.add(purchaseorCountviews);
        }
        dataResult.setData(datanew);
        return dataResult;
    }
    @RequestMapping(value = "/listpurchase", method = RequestMethod.POST)
    public @ResponseBody
    DataSourceResult readpurchase(@RequestBody DataSourceRequest request) {

        DataSourceResult dataResult = purchaseorCountDao.getpurchaseList(request);
        String rootPath = session.getServletContext().getRealPath("/");
        List<?> data = dataResult.getData();
        List<PurchaseNodeatilViews> datanew =new ArrayList<PurchaseNodeatilViews>();
        for(int i=0;i<data.size();i++){
            PurchaseNodeatilViews purchaseNodeatilViews = (PurchaseNodeatilViews) data.get(i);
            String billno = purchaseNodeatilViews.getBillno();
            if(billno.contains(BillConstant.BillPrefix.purchase)){
                purchaseNodeatilViews.setSaletype("采购订单");
            }
            if(billno.contains(BillConstant.BillPrefix.purchaseReturn)){
                purchaseNodeatilViews.setSaletype("采购退货订单");
                Integer qty = purchaseNodeatilViews.getTotqty();
                purchaseNodeatilViews.setTotqty(Integer.parseInt("-"+qty));
            }

            datanew.add(purchaseNodeatilViews);
        }
        dataResult.setData(datanew);
        return dataResult;
    }
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public @ResponseBody
    void export(String fileName, String base64, String contentType,
                HttpServletResponse response) throws IOException {

        response.setHeader("Content-Disposition", "attachment;filename="
                + fileName);
        response.setContentType(contentType);

        byte[] data = DatatypeConverter.parseBase64Binary(base64);

        response.setContentLength(data.length);
        response.getOutputStream().write(data);
        response.flushBuffer();
    }
    @RequestMapping(value = "/findtitledate", method = RequestMethod.POST)
    @ResponseBody
    public MessageBox findtitledate(String dates){
        System.out.print(dates);
        Map<String,String> map4Json = JSONUtil.getMap4Json(dates);
        //String hql="select new saleorderCount (sum(t.qty), sum(t.totactprice)) from saleorderCountOViews t where 1=1";
       /* String hqlsqty="select sum(t.qty) from saleorderCountOViews t where 1=1";
        String hqlstotactprice="select  sum(t.totactprice) from saleorderCountOViews t where 1=1";
        String hqlrqty="select sum(t.qty) from saleorderCountRViews t where 1=1";
        String hqlrtotactprice="select sum(t.totactprice) from saleorderCountRViews t where 1=1";
        String hqlrtogross="select sum(t.gross) from saleorderCountRViews t where 1=1";
        String hqlrtogrossall="select sum(t.grossall) from saleorderCountRViews t where 1=1";
        String hqlstogrossall="select sum(t.grossall) from saleorderCountOViews t where 1=1";
        String hqlstogross="select sum(t.gross) from saleorderCountOViews t where 1=1";*/
        String hqlpurchasesum="select sum(t.qty) from PurchaseorCountviews t where 1=1";
        String hqlpurchasonesum="select count(t.billid) from PurchaseorCountviews t where 1=1";
        String hqlpurchasmony="select sum(t.totactprice) from PurchaseorCountviews t where 1=1";
        Iterator<Map.Entry<String, String>> iterator = map4Json.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            String key = next.getKey();
            String value=map4Json.get(key);
            if(CommonUtil.isNotBlank(value)){
                if(key.equals("filter_gte_billDate")){
                    String[] split = key.split("_");
                    String time =value+" 00:00:00";
                    hqlpurchasesum+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqlpurchasonesum+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqlpurchasmony+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                   /*  hqlrtotactprice+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqlrtogross+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqlrtogrossall+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqlstogrossall+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqlstogross+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";*/
                  /* hqlsqty+=" and '"+value+" '<= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlstotactprice+=" and '"+value+" '<= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlrqty+=" and '"+value+" '<= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlrtotactprice+=" and '"+value+" '<= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlrtogross+=" and '"+value+" '<= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlrtogrossall+=" and '"+value+" '<= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlstogrossall+=" and '"+value+" '<= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlstogross+=" and '"+value+" '<= to_char("+split[2]+",'yyyy-MM-dd')";*/
                }else if(key.equals("filter_lte_billDate")){
                    String[] split = key.split("_");
                    String time =value+" 23:59:59";
                    hqlpurchasesum+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqlpurchasonesum+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqlpurchasmony+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                     /* hqlrtotactprice+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqlrtogross+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqlrtogrossall+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqlstogrossall+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqlstogross+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";*/
                 /*   hqlsqty+=" and '"+value+"' >= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlstotactprice+=" and '"+value+"' >= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlrqty+=" and '"+value+"' >= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlrtotactprice+=" and '"+value+"' >= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlrtogross+=" and '"+value+"' >= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlrtogrossall+=" and '"+value+"' >= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlstogrossall+=" and '"+value+"' >= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlstogross+=" and '"+value+"' >= to_char("+split[2]+",'yyyy-MM-dd')";*/
                }else {
                    String[] split = key.split("_");
                    if(split[1].equals("contains")){
                        hqlpurchasesum+=" and "+split[2]+" like '%"+value+"%'";
                        hqlpurchasonesum+=" and "+split[2]+" like'%"+value+"%'";
                        hqlpurchasmony+=" and "+split[2]+" like '%"+value+"%'";
                        /*hqlrtotactprice+=" and "+split[2]+" like '%"+value+"%'";
                        hqlrtogross+=" and "+split[2]+" like '%"+value+"%'";
                        hqlrtogrossall+=" and "+split[2]+" like '%"+value+"%'";
                        hqlstogrossall+=" and "+split[2]+" like '%"+value+"%'";
                        hqlstogross+=" and "+split[2]+" like '%"+value+"%'";*/
                    }else{
                        hqlpurchasesum+=" and "+split[2]+"='"+value+"'";
                        hqlpurchasonesum+=" and "+split[2]+"='"+value+"'";
                        hqlpurchasmony+=" and "+split[2]+"='"+value+"'";
                        /*hqlrtotactprice+=" and "+split[2]+"='"+value+"'";
                        hqlrtogross+=" and "+split[2]+"='"+value+"'";
                        hqlrtogrossall+=" and "+split[2]+"='"+value+"'";
                        hqlstogrossall+=" and "+split[2]+"='"+value+"'";
                        hqlstogross+=" and "+split[2]+"='"+value+"'";*/
                    }


                }

            }
        }
        hqlpurchasonesum+=" group by billid";
        Integer longsqty= null;
        Long aLong = this.purchaseOrderBillService.findpurchaseCount(hqlpurchasesum);
        if(CommonUtil.isBlank(aLong)){
            longsqty=0;
        }else{
            longsqty= aLong.intValue();
        }
        Integer purchasonesumqty= null;
        List<Object> objects = this.purchaseOrderBillService.findpurchaseCounts(hqlpurchasonesum);
        purchasonesumqty=objects.size();

        Double purchasmony = this.purchaseOrderBillService.findpurchaseCountMon(hqlpurchasmony);
        if(CommonUtil.isBlank(purchasmony)){
            purchasmony=0.D;
        }
        //saleorderCount saleorderCount = this.saleOrderBillService.findsaleOrderCount(hql);

        purchaseCount purchasecount = new purchaseCount();
        purchasecount.setPurchasesum(longsqty);
        purchasecount.setPurchasonesum(purchasonesumqty);
        purchasecount.setPurchasmony(purchasmony);
        return new MessageBox(true, "成功",purchasecount);


    }

    @RequestMapping(value = "/readpurchaseBybusinessname", method = RequestMethod.POST)
    public @ResponseBody
    DataSourceResult readSaleBybusinessname(@RequestBody DataSourceRequest request) {

        DataSourceResult dataResult = null;
        try {
            dataResult = purchaseorCountDao.getPurchaseBybusinessnameList(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataResult;
    }
    @RequestMapping(value = "/readpurchaseBydestunitid", method = RequestMethod.POST)
    public @ResponseBody
    DataSourceResult readSaleBydestunitid(@RequestBody DataSourceRequest request) {

        DataSourceResult dataResult = null;
        try {
            dataResult = purchaseorCountDao.getPurchaseBydestunitidList(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataResult;
    }

}


