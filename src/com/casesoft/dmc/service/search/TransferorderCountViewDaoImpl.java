package com.casesoft.dmc.service.search;

import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.dao.search.TransferorderCountDao;

import com.casesoft.dmc.model.logistics.TransferOrderBill;
import com.casesoft.dmc.model.search.TransByOrig;
import com.casesoft.dmc.model.search.TransByStyleId;
import com.casesoft.dmc.model.search.TransferorderCountView;
import oracle.jdbc.driver.OracleTypes;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2018/3/1.
 */
@Transactional
@Component
public class TransferorderCountViewDaoImpl implements TransferorderCountDao{
    @Autowired
    private SessionFactory sessionFactory;
    @Override
    public DataSourceResult getList(DataSourceRequest request) {
        try {
            return request.toDataSourceResult(sessionFactory.getCurrentSession(), TransferorderCountView.class);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public DataSourceResult getTranList(DataSourceRequest request) {
        try {
            return request.toDataSourceResult(sessionFactory.getCurrentSession(), TransferOrderBill.class);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public DataSourceResult getTransByStyleId(DataSourceRequest request) throws Exception{
        ResultSet rs=null;
        CallableStatement cs=null;
        Connection con=null;
        try {
            Session session = sessionFactory.getCurrentSession();
            con = SessionFactoryUtils.getDataSource(session.getSessionFactory()).getConnection();
            cs = con.prepareCall("{call findtransByStyleId(?,?,?,?,?,?,?)}");
            //设置参数
            DataSourceRequest.FilterDescriptor filter = request.getFilter();
            List<DataSourceRequest.FilterDescriptor> filters = filter.getFilters();
            //填充空数据
            cs.setString(1, "");
            cs.setString(2, "");
            cs.setString(3, "");
            for(int i=0;i<filters.size();i++){
                DataSourceRequest.FilterDescriptor filterDescriptor = filters.get(i);
                if(filterDescriptor.getField().equals("billDate")&&filterDescriptor.getOperator().equals("gte")){
                    String timeStr=(String)filterDescriptor.getValue();
                    SimpleDateFormat formatter = new SimpleDateFormat(
                            "yyyy-MM-dd'T'HH:mm:ss");
                    Date date = formatter.parse(timeStr);
                    String time = CommonUtil.getDateString(date,"yyyy-MM-dd");
                    cs.setString(1, time);
                }else if(filterDescriptor.getField().equals("billDate")&&filterDescriptor.getOperator().equals("lte")){
                    String timeStr=(String)filterDescriptor.getValue();
                    SimpleDateFormat formatter = new SimpleDateFormat(
                            "yyyy-MM-dd'T'HH:mm:ss");
                    Date date = formatter.parse(timeStr);
                    String time =CommonUtil.getDateString(date,"yyyy-MM-dd");
                    cs.setString(2, time);
                }else if(filterDescriptor.getField().equals("styleId")){
                    String deport=(String)filterDescriptor.getValue();
                    cs.setString(3, deport);
                }
            }

            Integer beginIndex=(request.getPage()-1)*(request.getPageSize())+1;
            Integer endIndex=(request.getPage())*(request.getPageSize());
            cs.setDouble(4,beginIndex.doubleValue());
            cs.setDouble(5,endIndex.doubleValue());
            cs.registerOutParameter(6, Types.INTEGER);
            cs.registerOutParameter(7, OracleTypes.CURSOR);
            //cs.registerOutParameter("resultSet", -10);
            cs.execute();
            rs=(ResultSet)cs.getObject(7);
            ArrayList<TransByStyleId> list=new ArrayList<TransByStyleId>();
            while (rs!=null&& rs.next()){
               /* SaleBybusinessname saleBybusinessname=new SaleBybusinessname();
                //String a=rs.getObject(1).toString();
                if(CommonUtil.isNotBlank(rs.getObject(1))){
                    saleBybusinessname.setBusnissname(rs.getObject(1).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(2))){
                    saleBybusinessname.setOrigname(rs.getObject(2).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(3))){
                    saleBybusinessname.setPrecast(rs.getObject(3).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(4))){
                    saleBybusinessname.setGross(rs.getObject(4).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(5))){
                    saleBybusinessname.setGrossprofits(rs.getObject(5).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(6))){
                    saleBybusinessname.setTotactprice(rs.getObject(6).toString());
                }*/
                //Map<String,Object> map=new HashMap<String,Object>();
                TransByStyleId transByStyleId=new TransByStyleId();
                if(CommonUtil.isNotBlank(rs.getObject(1))){
                    //map.put("styleid",rs.getObject(1).toString());
                    transByStyleId.setStyleid(rs.getObject(1).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(2))){
                    //map.put("stylename",rs.getObject(2).toString());
                    transByStyleId.setStylename(rs.getObject(2).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(3))){
                    //map.put("totqty",rs.getObject(3).toString());
                    transByStyleId.setTotqty(rs.getObject(3).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(4))){
                    //map.put("class3",rs.getObject(4).toString());
                    transByStyleId.setClass3(rs.getObject(4).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(5))){
                    //map.put("class4",rs.getObject(5).toString());
                    transByStyleId.setClass4(rs.getObject(5).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(6))){
                    //map.put("class8",rs.getObject(6).toString());
                    transByStyleId.setClass8(rs.getObject(6).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(7))){
                    //map.put("class2",rs.getObject(7).toString());
                    transByStyleId.setClass2(rs.getObject(7).toString());
                }
                list.add(transByStyleId);
            }
            DataSourceResult result = new DataSourceResult();
          /* Criteria criteria = session.createCriteria(SaleBybusinessname.class);
            sort(criteria, sortDescriptors());*/
            result.setData(list);
            result.setTotal(Long.parseLong(cs.getObject(6)+""));

           /* request.toDataSourceResult(sessionFactory.getCurrentSession(), SaleBybusinessname.class);*/

            return result;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            if(cs != null) {
                cs.close();
            }

            if(con != null) {
                con.close();
            }

        }

    }

    @Override
    public DataSourceResult getTransByOrig(DataSourceRequest request) throws Exception {
        ResultSet rs=null;
        CallableStatement cs=null;
        Connection con=null;
        try {
            Session session = sessionFactory.getCurrentSession();
            con = SessionFactoryUtils.getDataSource(session.getSessionFactory()).getConnection();
            cs = con.prepareCall("{call findtransByOrig(?,?,?,?,?,?)}");
            //设置参数
            DataSourceRequest.FilterDescriptor filter = request.getFilter();
            List<DataSourceRequest.FilterDescriptor> filters = filter.getFilters();
            //填充空数据
            cs.setString(1, "");
            cs.setString(2, "");
            cs.setString(3, "");
            for(int i=0;i<filters.size();i++){
                DataSourceRequest.FilterDescriptor filterDescriptor = filters.get(i);
                if(filterDescriptor.getField().equals("billDate")&&filterDescriptor.getOperator().equals("gte")){
                    String timeStr=(String)filterDescriptor.getValue();
                    SimpleDateFormat formatter = new SimpleDateFormat(
                            "yyyy-MM-dd'T'HH:mm:ss");
                    Date date = formatter.parse(timeStr);
                    String time = CommonUtil.getDateString(date,"yyyy-MM-dd");
                    cs.setString(1, time);
                }else if(filterDescriptor.getField().equals("billDate")&&filterDescriptor.getOperator().equals("lte")){
                    String timeStr=(String)filterDescriptor.getValue();
                    SimpleDateFormat formatter = new SimpleDateFormat(
                            "yyyy-MM-dd'T'HH:mm:ss");
                    Date date = formatter.parse(timeStr);
                    String time =CommonUtil.getDateString(date,"yyyy-MM-dd");
                    cs.setString(2, time);
                }
            }

            Integer beginIndex=(request.getPage()-1)*(request.getPageSize())+1;
            Integer endIndex=(request.getPage())*(request.getPageSize());
            cs.setDouble(3,beginIndex.doubleValue());
            cs.setDouble(4,endIndex.doubleValue());
            cs.registerOutParameter(5, Types.INTEGER);
            cs.registerOutParameter(6, OracleTypes.CURSOR);
            //cs.registerOutParameter("resultSet", -10);
            cs.execute();
            rs=(ResultSet)cs.getObject(6);
            ArrayList<TransByOrig> list=new ArrayList<TransByOrig>();
            while (rs!=null&& rs.next()){
                TransByOrig transByOrig=new TransByOrig();
                //Map<String,Object> map=new HashMap<String,Object>();
                if(CommonUtil.isNotBlank(rs.getObject(1))){
                    //map.put("origname",rs.getObject(1).toString());
                    transByOrig.setOrigname(rs.getObject(1).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(2))){
                    //map.put("totqty",rs.getObject(2).toString());
                    transByOrig.setTotqty(rs.getObject(2).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(3))){
                    //map.put("trantype",rs.getObject(3).toString());
                    transByOrig.setTrantype(rs.getObject(3).toString());
                }

                list.add(transByOrig);
            }
            DataSourceResult result = new DataSourceResult();
          /* Criteria criteria = session.createCriteria(SaleBybusinessname.class);
            sort(criteria, sortDescriptors());*/
            result.setData(list);
            result.setTotal(Long.parseLong(cs.getObject(5)+""));

           /* request.toDataSourceResult(sessionFactory.getCurrentSession(), SaleBybusinessname.class);*/

            return result;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            if(cs != null) {
                cs.close();
            }

            if(con != null) {
                con.close();
            }

        }
    }

    @Override
    public DataSourceResult getTransBystyleandsize(DataSourceRequest request) throws Exception {
        ResultSet rs=null;
        CallableStatement cs=null;
        Connection con=null;
        try {
            Session session = sessionFactory.getCurrentSession();
            con = SessionFactoryUtils.getDataSource(session.getSessionFactory()).getConnection();
            cs = con.prepareCall("{call findtranbystyleandsize(?,?,?,?,?,?,?,?,?,?,?,?)}");
            //设置参数
            DataSourceRequest.FilterDescriptor filter = request.getFilter();
            List<DataSourceRequest.FilterDescriptor> filters = filter.getFilters();
            //填充空数据
            cs.setString(1, "");
            cs.setString(2, "");
            cs.setString(3, "");
            cs.setString(4, "");
            cs.setString(5, "");
            cs.setString(6, "");
            cs.setString(7, "");
            cs.setString(8, "");
            for(int i=0;i<filters.size();i++){
                DataSourceRequest.FilterDescriptor filterDescriptor = filters.get(i);
                if(filterDescriptor.getField().equals("billDate")&&filterDescriptor.getOperator().equals("gte")){
                    String timeStr=(String)filterDescriptor.getValue();
                    SimpleDateFormat formatter = new SimpleDateFormat(
                            "yyyy-MM-dd'T'HH:mm:ss");
                    Date date = formatter.parse(timeStr);
                    String time = CommonUtil.getDateString(date,"yyyy-MM-dd");
                    cs.setString(1, time);
                }else if(filterDescriptor.getField().equals("billDate")&&filterDescriptor.getOperator().equals("lte")){
                    String timeStr=(String)filterDescriptor.getValue();
                    SimpleDateFormat formatter = new SimpleDateFormat(
                            "yyyy-MM-dd'T'HH:mm:ss");
                    Date date = formatter.parse(timeStr);
                    String time =CommonUtil.getDateString(date,"yyyy-MM-dd");
                    cs.setString(2, time);
                }else if(filterDescriptor.getField().equals("styleId")){
                    String styleId=(String)filterDescriptor.getValue();
                    cs.setString(3, styleId);
                }else if(filterDescriptor.getField().equals("origUnitId")){
                    String origUnitId=(String)filterDescriptor.getValue();
                    cs.setString(4, origUnitId);
                }else if(filterDescriptor.getField().equals("origId")){
                    String origId=(String)filterDescriptor.getValue();
                    cs.setString(5, origId);
                }else if(filterDescriptor.getField().equals("destUnitId")){
                    String destUnitId=(String)filterDescriptor.getValue();
                    cs.setString(6, destUnitId);
                }else if(filterDescriptor.getField().equals("destId")){
                    String destId=(String)filterDescriptor.getValue();
                    cs.setString(7, destId);
                }else if(filterDescriptor.getField().equals("billNo")){
                    String billNo=(String)filterDescriptor.getValue();
                    cs.setString(8, billNo);
                }
            }

            Integer beginIndex=(request.getPage()-1)*(request.getPageSize())+1;
            Integer endIndex=(request.getPage())*(request.getPageSize());
            cs.setDouble(9,beginIndex.doubleValue());
            cs.setDouble(10,endIndex.doubleValue());
            cs.registerOutParameter(11, Types.INTEGER);
            cs.registerOutParameter(12, OracleTypes.CURSOR);
            //cs.registerOutParameter("resultSet", -10);
            cs.execute();
            rs=(ResultSet)cs.getObject(12);
            ArrayList<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
            while (rs!=null&& rs.next()){

                Map<String,Object> map=new HashMap<String,Object>();
                if(CommonUtil.isNotBlank(rs.getObject(1))){
                    map.put("styleid",rs.getObject(1).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(2))){
                    map.put("colorid",rs.getObject(2).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(3))){
                    map.put("billno",rs.getObject(3).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(4))){
                    map.put("styleName",rs.getObject(4).toString());
                }

                list.add(map);
            }
            DataSourceResult result = new DataSourceResult();
          /* Criteria criteria = session.createCriteria(SaleBybusinessname.class);
            sort(criteria, sortDescriptors());*/
            result.setData(list);
            result.setTotal(Long.parseLong(cs.getObject(11)+""));

           /* request.toDataSourceResult(sessionFactory.getCurrentSession(), SaleBybusinessname.class);*/

            return result;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            if(cs != null) {
                cs.close();
            }

            if(con != null) {
                con.close();
            }

        }
    }
}
