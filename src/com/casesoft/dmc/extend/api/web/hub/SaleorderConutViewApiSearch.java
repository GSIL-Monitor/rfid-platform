package com.casesoft.dmc.extend.api.web.hub;

import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.dao.search.SaleorderCountDao;
import com.casesoft.dmc.extend.api.web.ApiBaseController;
import com.casesoft.dmc.model.logistics.BillConstant;
import com.casesoft.dmc.model.search.SaleNodeatilViews;
import com.casesoft.dmc.model.search.SaleorderCountView;
import com.casesoft.dmc.model.search.saleorderCount;
import com.casesoft.dmc.service.logistics.SaleOrderBillService;
import com.casesoft.dmc.service.sys.impl.UnitService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/22 0022.
 */
@Controller
@RequestMapping(value = "/api/hub/saleorderConutViewApi", method = {RequestMethod.POST, RequestMethod.GET})
@Api
public class SaleorderConutViewApiSearch extends ApiBaseController {
    @Autowired
    SaleorderCountDao saleorderCountDao;
    @Autowired
    private UnitService unitService;
    @Autowired
    private SaleOrderBillService saleOrderBillService;
    @Override
    public String index() {
        return null;
    }
    @RequestMapping(value = "/listWs")
    public @ResponseBody
    MessageBox read(String pageNo,String pageSize,String userName) {
        DataSourceRequest request=new DataSourceRequest ();
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        request.setPage(Integer.parseInt(pageNo));
        request.setPageSize(Integer.parseInt(pageSize));
        request.setTake(Integer.parseInt(pageSize));
        request.setSkip((Integer.parseInt(pageNo)-1)*Integer.parseInt(pageSize));
      /*  ArrayList<DataSourceRequest.FilterDescriptor> list= new ArrayList<DataSourceRequest.FilterDescriptor>();*/
        DataSourceRequest.FilterDescriptor filter=new DataSourceRequest.FilterDescriptor();
        List<DataSourceRequest.FilterDescriptor> list = filter.getFilters();
        for(PropertyFilter propertyFilter:filters) {
            DataSourceRequest.FilterDescriptor filter1=new DataSourceRequest.FilterDescriptor();
            if(propertyFilter.getPropertyNames()[0].equals("billDate")){
                //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");

                    //propertyFilter.getMatchValue().toString()
                    //Date parse = format.parse(new Date().toString());
                    //2017-09-22T00:00:00.000Z
                filter1.setValue(propertyFilter.getMatchValue().toString()+"T00:00:00.000Z");

                if(propertyFilter.getMatchType().toString().equals("GE")){
                    filter1.setOperator("gte");
                }
                if(propertyFilter.getMatchType().toString().equals("LE")){
                    filter1.setOperator("lte");
                }
            }else{
                if(propertyFilter.getMatchType().toString().equals("IN")){
                    filter1.setOperator("contains");
                    String[] aArray = (String[]) propertyFilter.getMatchValue();
                    filter1.setValue(aArray[0]);
                }
                if(propertyFilter.getMatchType().toString().equals("EQ")){
                    filter1.setOperator("eq");
                    String[] aArray = (String[]) propertyFilter.getMatchValue();
                    filter1.setValue(aArray[0]);
                }

            }
            String[] propertyNames = propertyFilter.getPropertyNames();
            filter1.setField(propertyNames[0]);
            filter1.setIgnoreCase(true);
            list.add(filter1);
        }
        filter.setLogic("and");
        filter.setIgnoreCase(true);
        request.setFilter(filter);
        List<DataSourceRequest.SortDescriptor> sort=new ArrayList<>();
        DataSourceRequest.SortDescriptor all= new DataSourceRequest.SortDescriptor();
        all.setField("billDate");
        all.setDir("desc");
        sort.add(all);
        request.setSort(sort);
        DataSourceResult dataResult = saleorderCountDao.getList(request);
        List<?> data = dataResult.getData();
        List<SaleorderCountView> datanew =new ArrayList<SaleorderCountView>();
        for(int i=0;i<data.size();i++){
            SaleorderCountView saleorderCountView = (SaleorderCountView) data.get(i);
            String billno = saleorderCountView.getBillno();
            if(billno.contains(BillConstant.BillPrefix.saleOrder)){
                saleorderCountView.setSaletype("销售订单");
            }
            if(billno.contains(BillConstant.BillPrefix.SaleOrderReturn)){
                saleorderCountView.setSaletype("销售退货订单");
                Integer qty = saleorderCountView.getQty();
                saleorderCountView.setQty(Integer.parseInt("-"+qty));
            }
            if(userName.equals("admin")){
                saleorderCountView.setIshow(true);
            }
            datanew.add(saleorderCountView);
        }
        int total = (int)dataResult.getTotal();
        int num=total/Integer.parseInt(pageSize);
        int i=total%Integer.parseInt(pageSize);
        if(i>0){
            num+=num+1;
        }

        if(Integer.parseInt(pageNo)>num){
            List<SaleorderCountView> datano =new ArrayList<SaleorderCountView>();
            dataResult.setData(datano);
        }else{

            dataResult.setData(datanew);
        }

        /*HashMap<String,Object> map= new HashMap<String,Object>();
        if(userName.equals("admin")){
            map.put("ishow",true);
            map.put("results",dataResult.getData());
        }*/
        return new MessageBox(true, "查询成功",dataResult);
    }


    @RequestMapping(value = "/listsaleWs")
    public @ResponseBody
    MessageBox readsale(String pageNo,String pageSize,String userName) {
        DataSourceRequest request=new DataSourceRequest ();
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        request.setPage(Integer.parseInt(pageNo));
        request.setPageSize(Integer.parseInt(pageSize));
        request.setTake(Integer.parseInt(pageSize));
        request.setSkip((Integer.parseInt(pageNo)-1)*Integer.parseInt(pageSize));
      /*  ArrayList<DataSourceRequest.FilterDescriptor> list= new ArrayList<DataSourceRequest.FilterDescriptor>();*/
        DataSourceRequest.FilterDescriptor filter=new DataSourceRequest.FilterDescriptor();
        List<DataSourceRequest.FilterDescriptor> list = filter.getFilters();
        for(PropertyFilter propertyFilter:filters) {
            DataSourceRequest.FilterDescriptor filter1=new DataSourceRequest.FilterDescriptor();
            if(propertyFilter.getPropertyNames()[0].equals("billDate")){
                //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");

                //propertyFilter.getMatchValue().toString()
                //Date parse = format.parse(new Date().toString());
                //2017-09-22T00:00:00.000Z
                filter1.setValue(propertyFilter.getMatchValue().toString()+"T00:00:00.000Z");

                if(propertyFilter.getMatchType().toString().equals("GE")){
                    filter1.setOperator("gte");
                }
                if(propertyFilter.getMatchType().toString().equals("LE")){
                    filter1.setOperator("lte");
                }
            }else{
                if(propertyFilter.getMatchType().toString().equals("IN")){
                    filter1.setOperator("contains");
                    String[] aArray = (String[]) propertyFilter.getMatchValue();
                    filter1.setValue(aArray[0]);
                }
                if(propertyFilter.getMatchType().toString().equals("EQ")){
                    filter1.setOperator("eq");
                    String[] aArray = (String[]) propertyFilter.getMatchValue();
                    filter1.setValue(aArray[0]);
                }

            }
            String[] propertyNames = propertyFilter.getPropertyNames();
            filter1.setField(propertyNames[0]);
            filter1.setIgnoreCase(true);
            list.add(filter1);
        }
        filter.setLogic("and");
        filter.setIgnoreCase(true);
        request.setFilter(filter);
        DataSourceResult dataResult = saleorderCountDao.getSaleList(request);
        List<?> data = dataResult.getData();
        List<SaleNodeatilViews> datanew =new ArrayList<SaleNodeatilViews>();
        for(int i=0;i<data.size();i++){
            SaleNodeatilViews saleNodeatilViews = (SaleNodeatilViews) data.get(i);
            String billno = saleNodeatilViews.getBillno();
            if(billno.contains(BillConstant.BillPrefix.saleOrder)){
                saleNodeatilViews.setSaletype("销售订单");
            }
            if(billno.contains(BillConstant.BillPrefix.SaleOrderReturn)){
                saleNodeatilViews.setSaletype("销售退货订单");
                Integer qty = saleNodeatilViews.getTotqty();
                saleNodeatilViews.setTotqty(Integer.parseInt("-"+qty));
            }
            if(userName.equals("admin")){
                saleNodeatilViews.setIshow(true);
            }
            datanew.add(saleNodeatilViews);
        }

        int total = (int)dataResult.getTotal();
        int num=total/Integer.parseInt(pageSize);
        int i=total%Integer.parseInt(pageSize);
        if(i>0){
            num+=num+1;
        }

        if(Integer.parseInt(pageNo)>num){
            List<SaleNodeatilViews> datano =new ArrayList<SaleNodeatilViews>();
            dataResult.setData(datano);
        }else{

            dataResult.setData(datanew);
        }
       /* HashMap<String,Object> map= new HashMap<String,Object>();
        if(userName.equals("admin")){
            map.put("ishow",true);
            map.put("results",dataResult.getData());
        }*/
        return new MessageBox(true, "查询成功",dataResult);
    }
    @RequestMapping(value = "/listSaleBybusinessnameWs")
    public @ResponseBody
    MessageBox readSaleBybusinessname(String pageNo,String pageSize,String userName) {
        DataSourceRequest request=new DataSourceRequest ();
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        request.setPage(Integer.parseInt(pageNo));
        request.setPageSize(Integer.parseInt(pageSize));
        request.setTake(Integer.parseInt(pageSize));
        request.setSkip((Integer.parseInt(pageNo)-1)*Integer.parseInt(pageSize));
      /*  ArrayList<DataSourceRequest.FilterDescriptor> list= new ArrayList<DataSourceRequest.FilterDescriptor>();*/
        DataSourceRequest.FilterDescriptor filter=new DataSourceRequest.FilterDescriptor();
        List<DataSourceRequest.FilterDescriptor> list = filter.getFilters();
        for(PropertyFilter propertyFilter:filters) {
            if(propertyFilter.getPropertyNames()[0].equals("billDate")){
                //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");

                //propertyFilter.getMatchValue().toString()
                //Date parse = format.parse(new Date().toString());
                //2017-09-22T00:00:00.000Z
                filter.setValue(propertyFilter.getMatchValue().toString()+"T00:00:00.000Z");

                if(propertyFilter.getMatchType().toString().equals("GE")){
                    filter.setOperator("gte");
                }
                if(propertyFilter.getMatchType().toString().equals("LE")){
                    filter.setOperator("lte");
                }
            }else{
                if(propertyFilter.getMatchType().toString().equals("IN")){
                    filter.setOperator("contains");
                    String[] aArray = (String[]) propertyFilter.getMatchValue();
                    filter.setValue(aArray[0]);
                }
                if(propertyFilter.getMatchType().toString().equals("EQ")){
                    filter.setOperator("eq");
                    String[] aArray = (String[]) propertyFilter.getMatchValue();
                    filter.setValue(aArray[0]);
                }

            }
            String[] propertyNames = propertyFilter.getPropertyNames();
            filter.setField(propertyNames[0]);
            filter.setIgnoreCase(true);
            list.add(filter);
        }
        request.setFilter(filter);
        DataSourceResult dataResult = null;
        try {
            dataResult = saleorderCountDao.getSaleBybusinessnameList(request);
            /*HashMap<String,Object> map= new HashMap<String,Object>();
            if(userName.equals("admin")){
                map.put("ishow",true);
                map.put("results",dataResult.getData());
            }*/
           /* List<?> data = dataResult.getData();*/
            int total = (int)dataResult.getTotal();
            int num=total/Integer.parseInt(pageSize);
            int i=total%Integer.parseInt(pageSize);
            if(i>0){
                num+=num+1;
            }

            if(Integer.parseInt(pageNo)>num){
                List<?> datano =new ArrayList<>();
                dataResult.setData(datano);
            }
            return new MessageBox(true, "查询成功",dataResult);

        } catch (Exception e) {
            e.printStackTrace();
            return new MessageBox(false, "查询失败",e.getMessage());
        }
       /* List<?> data = dataResult.getData();
        List<SaleorderCountView> datanew =new ArrayList<SaleorderCountView>();
        for(int i=0;i<data.size();i++){
            SaleorderCountView saleorderCountView = (SaleorderCountView) data.get(i);
            String billno = saleorderCountView.getBillno();
            if(billno.contains(BillConstant.BillPrefix.saleOrder)){
                saleorderCountView.setSaletype("销售订单");
            }
            if(billno.contains(BillConstant.BillPrefix.SaleOrderReturn)){
                saleorderCountView.setSaletype("销售退货订单");
                Integer qty = saleorderCountView.getQty();
                saleorderCountView.setQty(Integer.parseInt("-"+qty));
            }
            datanew.add(saleorderCountView);
        }
        dataResult.setData(datanew);*/

    }
    @RequestMapping(value = "/listSaleByorignameWs")
    public @ResponseBody
    MessageBox readSaleByorigname(String pageNo,String pageSize,String userName) {
        DataSourceRequest request=new DataSourceRequest ();
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        request.setPage(Integer.parseInt(pageNo));
        request.setPageSize(Integer.parseInt(pageSize));
        request.setTake(Integer.parseInt(pageSize));
        request.setSkip((Integer.parseInt(pageNo)-1)*Integer.parseInt(pageSize));
      /*  ArrayList<DataSourceRequest.FilterDescriptor> list= new ArrayList<DataSourceRequest.FilterDescriptor>();*/
        DataSourceRequest.FilterDescriptor filter=new DataSourceRequest.FilterDescriptor();
        List<DataSourceRequest.FilterDescriptor> list = filter.getFilters();
        for(PropertyFilter propertyFilter:filters) {
            if(propertyFilter.getPropertyNames()[0].equals("billDate")){
                //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");

                //propertyFilter.getMatchValue().toString()
                //Date parse = format.parse(new Date().toString());
                //2017-09-22T00:00:00.000Z
                filter.setValue(propertyFilter.getMatchValue().toString()+"T00:00:00.000Z");

                if(propertyFilter.getMatchType().toString().equals("GE")){
                    filter.setOperator("gte");
                }
                if(propertyFilter.getMatchType().toString().equals("LE")){
                    filter.setOperator("lte");
                }
            }else{
                if(propertyFilter.getMatchType().toString().equals("IN")){
                    filter.setOperator("contains");
                    String[] aArray = (String[]) propertyFilter.getMatchValue();
                    filter.setValue(aArray[0]);
                }
                if(propertyFilter.getMatchType().toString().equals("EQ")){
                    filter.setOperator("eq");
                    String[] aArray = (String[]) propertyFilter.getMatchValue();
                    filter.setValue(aArray[0]);
                }

            }
            String[] propertyNames = propertyFilter.getPropertyNames();
            filter.setField(propertyNames[0]);
            filter.setIgnoreCase(true);
            list.add(filter);
        }
        request.setFilter(filter);
        DataSourceResult dataResult = null;
        try {
            dataResult = saleorderCountDao.getSaleByorignameList(request);
           /* HashMap<String,Object> map= new HashMap<String,Object>();
            if(userName.equals("admin")){
                map.put("ishow",true);
                map.put("results",dataResult.getData());
            }*/
            int total = (int)dataResult.getTotal();
            int num=total/Integer.parseInt(pageSize);
            int i=total%Integer.parseInt(pageSize);
            if(i>0){
                num+=num+1;
            }

            if(Integer.parseInt(pageNo)>num){
                List<?> datano =new ArrayList<>();
                dataResult.setData(datano);
            }
            return new MessageBox(true, "查询成功",dataResult);
           // return new MessageBox(true, "查询成功",dataResult.getData());
        } catch (Exception e) {
            e.printStackTrace();
            return new MessageBox(false, "查询失败",e.getMessage());
        }
       /* List<?> data = dataResult.getData();
        List<SaleorderCountView> datanew =new ArrayList<SaleorderCountView>();
        for(int i=0;i<data.size();i++){
            SaleorderCountView saleorderCountView = (SaleorderCountView) data.get(i);
            String billno = saleorderCountView.getBillno();
            if(billno.contains(BillConstant.BillPrefix.saleOrder)){
                saleorderCountView.setSaletype("销售订单");
            }
            if(billno.contains(BillConstant.BillPrefix.SaleOrderReturn)){
                saleorderCountView.setSaletype("销售退货订单");
                Integer qty = saleorderCountView.getQty();
                saleorderCountView.setQty(Integer.parseInt("-"+qty));
            }
            datanew.add(saleorderCountView);
        }
        dataResult.setData(datanew);*/

    }



    @RequestMapping(value = "/findtitledateWs")
    @ResponseBody
  /*  public MessageBox findtitledate(String dates,String userName){*/
    public MessageBox findtitledate(String filter_gte_billDate,String filter_lte_billDate,String filter_contains_billno,String  filter_in_deport,String  filter_eq_styleid,String filter_eq_stylename ,String userName){
        this.logAllRequestParams();
        /*Map<String,String> map4Json = JSONUtil.getMap4Json(dates);*/
        //String hql="select new saleorderCount (sum(t.qty), sum(t.totactprice)) from saleorderCountOViews t where 1=1";
        String hqlsqty="select sum(t.qty) from saleorderCountOViews t where 1=1";
        String hqlstotactprice="select  sum(t.totactprice) from saleorderCountOViews t where 1=1";
        String hqlrqty="select sum(t.qty) from saleorderCountRViews t where 1=1";
        String hqlrtotactprice="select sum(t.totactprice) from saleorderCountRViews t where 1=1";
        String hqlrtogross="select sum(t.gross) from saleorderCountRViews t where 1=1";
        String hqlrtogrossall="select sum(t.grossall) from saleorderCountRViews t where 1=1";
        String hqlstogrossall="select sum(t.grossall) from saleorderCountOViews t where 1=1";
        String hqlstogross="select sum(t.gross) from saleorderCountOViews t where 1=1";
        if(CommonUtil.isNotBlank(filter_gte_billDate)){
            String[] split = "filter_gte_billDate".split("_");
            String time =filter_gte_billDate+" 00:00:00";
            hqlsqty+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
            hqlstotactprice+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
            hqlrqty+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
            hqlrtotactprice+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
            hqlrtogross+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
            hqlrtogrossall+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
            hqlstogrossall+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
            hqlstogross+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
        }
        if(CommonUtil.isNotBlank(filter_lte_billDate)){
            String[] split = "filter_lte_billDate".split("_");
            String time =filter_lte_billDate+" 23:59:59";
            hqlsqty+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
            hqlstotactprice+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
            hqlrqty+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
            hqlrtotactprice+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
            hqlrtogross+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
            hqlrtogrossall+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
            hqlstogrossall+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
            hqlstogross+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
        }
        if(CommonUtil.isNotBlank(filter_contains_billno)){
            String[] split = "filter_contains_billno".split("_");
            hqlsqty+=" and "+split[2]+" like '%"+filter_contains_billno+"%'";
            hqlstotactprice+=" and "+split[2]+" like'%"+filter_contains_billno+"%'";
            hqlrqty+=" and "+split[2]+" like '%"+filter_contains_billno+"%'";
            hqlrtotactprice+=" and "+split[2]+" like '%"+filter_contains_billno+"%'";
            hqlrtogross+=" and "+split[2]+" like '%"+filter_contains_billno+"%'";
            hqlrtogrossall+=" and "+split[2]+" like '%"+filter_contains_billno+"%'";
            hqlstogrossall+=" and "+split[2]+" like '%"+filter_contains_billno+"%'";
            hqlstogross+=" and "+split[2]+" like '%"+filter_contains_billno+"%'";
        }
        if(CommonUtil.isNotBlank(filter_in_deport)){
            String[] split = "filter_in_deport".split("_");
            hqlsqty+=" and "+split[2]+"='"+filter_in_deport+"'";
            hqlstotactprice+=" and "+split[2]+"='"+filter_in_deport+"'";
            hqlrqty+=" and "+split[2]+"='"+filter_in_deport+"'";
            hqlrtotactprice+=" and "+split[2]+"='"+filter_in_deport+"'";
            hqlrtogross+=" and "+split[2]+"='"+filter_in_deport+"'";
            hqlrtogrossall+=" and "+split[2]+"='"+filter_in_deport+"'";
            hqlstogrossall+=" and "+split[2]+"='"+filter_in_deport+"'";
            hqlstogross+=" and "+split[2]+"='"+filter_in_deport+"'";
        }
        if(CommonUtil.isNotBlank(filter_eq_styleid)){
            String[] split = "filter_eq_styleid".split("_");
            hqlsqty+=" and "+split[2]+"='"+filter_eq_styleid+"'";
            hqlstotactprice+=" and "+split[2]+"='"+filter_eq_styleid+"'";
            hqlrqty+=" and "+split[2]+"='"+filter_eq_styleid+"'";
            hqlrtotactprice+=" and "+split[2]+"='"+filter_eq_styleid+"'";
            hqlrtogross+=" and "+split[2]+"='"+filter_eq_styleid+"'";
            hqlrtogrossall+=" and "+split[2]+"='"+filter_eq_styleid+"'";
            hqlstogrossall+=" and "+split[2]+"='"+filter_eq_styleid+"'";
            hqlstogross+=" and "+split[2]+"='"+filter_eq_styleid+"'";
        }
        if(CommonUtil.isNotBlank(filter_eq_stylename)){
            String[] split = "filter_eq_stylename".split("_");
            hqlsqty+=" and "+split[2]+"='"+filter_eq_stylename+"'";
            hqlstotactprice+=" and "+split[2]+"='"+filter_eq_stylename+"'";
            hqlrqty+=" and "+split[2]+"='"+filter_eq_stylename+"'";
            hqlrtotactprice+=" and "+split[2]+"='"+filter_eq_stylename+"'";
            hqlrtogross+=" and "+split[2]+"='"+filter_eq_stylename+"'";
            hqlrtogrossall+=" and "+split[2]+"='"+filter_eq_stylename+"'";
            hqlstogrossall+=" and "+split[2]+"='"+filter_eq_stylename+"'";
            hqlstogross+=" and "+split[2]+"='"+filter_eq_stylename+"'";
        }
        /*Iterator<Map.Entry<String, String>> iterator = map4Json.entrySet().iterator();*/
       /* while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            String key = next.getKey();
            String value=map4Json.get(key);
            if(CommonUtil.isNotBlank(value)){
                if(key.equals("filter_gte_billDate")){
                    String[] split = key.split("_");
                    String time =value+" 00:00:00";
                    hqlsqty+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqlstotactprice+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqlrqty+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqlrtotactprice+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqlrtogross+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqlrtogrossall+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqlstogrossall+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqlstogross+=" and "+split[2]+" >= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                  *//* hqlsqty+=" and '"+value+" '<= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlstotactprice+=" and '"+value+" '<= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlrqty+=" and '"+value+" '<= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlrtotactprice+=" and '"+value+" '<= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlrtogross+=" and '"+value+" '<= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlrtogrossall+=" and '"+value+" '<= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlstogrossall+=" and '"+value+" '<= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlstogross+=" and '"+value+" '<= to_char("+split[2]+",'yyyy-MM-dd')";*//*
                }else if(key.equals("filter_lte_billDate")){
                    String[] split = key.split("_");
                    String time =value+" 23:59:59";
                    hqlsqty+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqlstotactprice+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqlrqty+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqlrtotactprice+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqlrtogross+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqlrtogrossall+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqlstogrossall+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                    hqlstogross+=" and "+split[2]+" <= to_date('"+time+"','yyyy-MM-dd HH24:mi:ss')";
                 *//*   hqlsqty+=" and '"+value+"' >= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlstotactprice+=" and '"+value+"' >= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlrqty+=" and '"+value+"' >= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlrtotactprice+=" and '"+value+"' >= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlrtogross+=" and '"+value+"' >= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlrtogrossall+=" and '"+value+"' >= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlstogrossall+=" and '"+value+"' >= to_char("+split[2]+",'yyyy-MM-dd')";
                    hqlstogross+=" and '"+value+"' >= to_char("+split[2]+",'yyyy-MM-dd')";*//*
                }else {
                    String[] split = key.split("_");
                    if(split[1].equals("contains")){
                        hqlsqty+=" and "+split[2]+" like '%"+value+"%'";
                        hqlstotactprice+=" and "+split[2]+" like'%"+value+"%'";
                        hqlrqty+=" and "+split[2]+" like '%"+value+"%'";
                        hqlrtotactprice+=" and "+split[2]+" like '%"+value+"%'";
                        hqlrtogross+=" and "+split[2]+" like '%"+value+"%'";
                        hqlrtogrossall+=" and "+split[2]+" like '%"+value+"%'";
                        hqlstogrossall+=" and "+split[2]+" like '%"+value+"%'";
                        hqlstogross+=" and "+split[2]+" like '%"+value+"%'";
                    }else{
                        hqlsqty+=" and "+split[2]+"='"+value+"'";
                        hqlstotactprice+=" and "+split[2]+"='"+value+"'";
                        hqlrqty+=" and "+split[2]+"='"+value+"'";
                        hqlrtotactprice+=" and "+split[2]+"='"+value+"'";
                        hqlrtogross+=" and "+split[2]+"='"+value+"'";
                        hqlrtogrossall+=" and "+split[2]+"='"+value+"'";
                        hqlstogrossall+=" and "+split[2]+"='"+value+"'";
                        hqlstogross+=" and "+split[2]+"='"+value+"'";
                    }


                }

            }
        }*/

        Integer longsqty= null;
        Long aLong = this.saleOrderBillService.findsaleOrderCount(hqlsqty);
        if(CommonUtil.isBlank(aLong)){
            longsqty=0;
        }else{
            longsqty= aLong.intValue();
        }
        Double longstotactprice= this.saleOrderBillService.findsaleOrderCountnum(hqlstotactprice);
        if(CommonUtil.isBlank(longstotactprice)){
            longstotactprice=0.D;
        }
        Integer longrqty= null;
        Long count = this.saleOrderBillService.findsaleOrderCount(hqlrqty);
        if(CommonUtil.isBlank(count)){
            longrqty=0;
        }else{
            longrqty=count.intValue();
        }
        Double longrtotactprice= this.saleOrderBillService.findsaleOrderCountnum(hqlrtotactprice);
        if(CommonUtil.isBlank(longrtotactprice)){
            longrtotactprice=0.D;
        }
        Double longrtogross= this.saleOrderBillService.findsaleOrderCountnum(hqlrtogross);
        if(CommonUtil.isBlank(longrtogross)){
            longrtogross=0.D;
        }
        Double longrtogrossall= this.saleOrderBillService.findsaleOrderCountnum(hqlrtogrossall);
        if(CommonUtil.isBlank(longrtogrossall)){
            longrtogrossall=0.D;
        }
        Double longstogrossall= this.saleOrderBillService.findsaleOrderCountnum(hqlstogrossall);
        if(CommonUtil.isBlank(longstogrossall)){
            longstogrossall=0.D;
        }
        Double longstogross= this.saleOrderBillService.findsaleOrderCountnum(hqlstogross);
        if(CommonUtil.isBlank(longstogross)){
            longstogross=0.D;
        }

        //saleorderCount saleorderCount = this.saleOrderBillService.findsaleOrderCount(hql);


        saleorderCount saleorderCounts=new saleorderCount();
        saleorderCounts.setSum(longsqty);
        saleorderCounts.setRsum(longrqty);
        Double Allmony=longstotactprice+longrtotactprice;
        BigDecimal   b   =   new BigDecimal(Allmony.floatValue());
        saleorderCounts.setAllmony(b.setScale(2,   BigDecimal.ROUND_HALF_UP).floatValue());
        Double Passall=longstotactprice+longrtotactprice;
        BigDecimal   b1   =   new BigDecimal(Passall.floatValue());
        saleorderCounts.setPassall(b1.setScale(2,   BigDecimal.ROUND_HALF_UP).floatValue());
        Double Grossprofits=(longstogross+longrtogross)/(longstotactprice+longrtotactprice)*100;
        BigDecimal   b2   =   new BigDecimal(Grossprofits);
        saleorderCounts.setGrossprofits(b2.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue()+"%");
        if(userName.equals("admin")){
            saleorderCounts.setIsshow(true);
        }else{
            saleorderCounts.setIsshow(false);
        }

        return new MessageBox(true, "成功",saleorderCounts);









    }

}

