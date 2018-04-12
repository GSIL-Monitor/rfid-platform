package com.casesoft.dmc.service.search;

import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.dao.search.PurchaseorCountDao;
import com.casesoft.dmc.model.logistics.PurchaseBydestunitid;
import com.casesoft.dmc.model.logistics.PurchaseBystyleid;
import com.casesoft.dmc.model.logistics.SaleBybusinessname;
import com.casesoft.dmc.model.search.PurchaseNodeatilViews;
import com.casesoft.dmc.model.search.PurchaseorCountviews;
import com.casesoft.dmc.model.search.SaleorderCountView;
import oracle.jdbc.driver.OracleTypes;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/10/11.
 */
@Transactional
@Component
public class PurchaseorCountDaoImpl implements PurchaseorCountDao {
    @Autowired
    private SessionFactory sessionFactory;
    @Override
    public DataSourceResult getList(DataSourceRequest request) {
        try {
            return request.toDataSourceResult(sessionFactory.getCurrentSession(), PurchaseorCountviews.class);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public DataSourceResult getpurchaseList(DataSourceRequest request) {
        try {
            return request.toDataSourceResult(sessionFactory.getCurrentSession(), PurchaseNodeatilViews.class);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public DataSourceResult getPurchaseBybusinessnameList(DataSourceRequest request) throws SQLException {
        ResultSet rs=null;
        CallableStatement cs=null;
        Connection con=null;
        try{
            Session session = sessionFactory.getCurrentSession();
            con = SessionFactoryUtils.getDataSource(session.getSessionFactory()).getConnection();
            cs = con.prepareCall("{call findpurchasebystyleid(?,?,?,?,?,?,?,?,?)}");
            //设置参数
            DataSourceRequest.FilterDescriptor filter = request.getFilter();
            List<DataSourceRequest.FilterDescriptor> filters = filter.getFilters();
            //填充空数据
            cs.setString(1, "");
            cs.setString(2, "");
            cs.setString(3, "");
            cs.setString(4, "");
            cs.setString(5, "");
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
                }else if(filterDescriptor.getField().equals("destunitid")){
                    String deport=(String)filterDescriptor.getValue();
                    cs.setString(3, deport);
                }else if(filterDescriptor.getField().equals("destid")){
                    String deport=(String)filterDescriptor.getValue();
                    cs.setString(4, deport);
                }else if(filterDescriptor.getField().equals("styleid")){
                    String deport=(String)filterDescriptor.getValue();
                    cs.setString(5, deport);
                }
            }
            Integer beginIndex=(request.getPage()-1)*(request.getPageSize())+1;
            Integer endIndex=(request.getPage())*(request.getPageSize());
            cs.setDouble(6,beginIndex.doubleValue());
            cs.setDouble(7,endIndex.doubleValue());
            cs.registerOutParameter(8, Types.INTEGER);
            cs.registerOutParameter(9, OracleTypes.CURSOR);
            //cs.registerOutParameter("resultSet", -10);
            cs.execute();
            rs=(ResultSet)cs.getObject(9);
            ArrayList<PurchaseBystyleid> list=new ArrayList<PurchaseBystyleid>();
            while (rs!=null&&rs.next()){
                PurchaseBystyleid purchaseBystyleid=new PurchaseBystyleid();
                if(CommonUtil.isNotBlank(rs.getObject(1))){
                    purchaseBystyleid.setSku(rs.getObject(1).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(2))){
                    purchaseBystyleid.setStylename(rs.getObject(2).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(3))){
                    purchaseBystyleid.setDestunitname(rs.getObject(3).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(4))){
                    purchaseBystyleid.setQty(rs.getObject(4).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(5))){
                    purchaseBystyleid.setTotactprice(rs.getObject(5).toString());
                }
                list.add(purchaseBystyleid);
            }
            DataSourceResult result = new DataSourceResult();
            result.setData(list);
            Object object = cs.getObject(8);
            result.setTotal(Integer.parseInt(object.toString()));
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
    public DataSourceResult getPurchaseBydestunitidList(DataSourceRequest request) throws SQLException {
        ResultSet rs=null;
        CallableStatement cs=null;
        Connection con=null;
        try{
            Session session = sessionFactory.getCurrentSession();
            con = SessionFactoryUtils.getDataSource(session.getSessionFactory()).getConnection();
            cs = con.prepareCall("{call findpurchasebydestunitid(?,?,?,?,?,?,?,?,?)}");
            //设置参数
            DataSourceRequest.FilterDescriptor filter = request.getFilter();
            List<DataSourceRequest.FilterDescriptor> filters = filter.getFilters();
            //填充空数据
            cs.setString(1, "");
            cs.setString(2, "");
            cs.setString(3, "");
            cs.setString(4, "");
            cs.setString(5, "");
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
                }else if(filterDescriptor.getField().equals("destunitid")){
                    String deport=(String)filterDescriptor.getValue();
                    cs.setString(3, deport);
                }else if(filterDescriptor.getField().equals("destid")){
                    String deport=(String)filterDescriptor.getValue();
                    cs.setString(4, deport);
                }else if(filterDescriptor.getField().equals("styleid")){
                    String deport=(String)filterDescriptor.getValue();
                    cs.setString(5, deport);
                }
            }
            Integer beginIndex=(request.getPage()-1)*(request.getPageSize())+1;
            Integer endIndex=(request.getPage())*(request.getPageSize());
            cs.setDouble(6,beginIndex.doubleValue());
            cs.setDouble(7,endIndex.doubleValue());
            cs.registerOutParameter(8, Types.INTEGER);
            cs.registerOutParameter(9, OracleTypes.CURSOR);
            //cs.registerOutParameter("resultSet", -10);
            cs.execute();
            rs=(ResultSet)cs.getObject(9);
            ArrayList<PurchaseBydestunitid> list=new ArrayList<PurchaseBydestunitid>();
            while (rs!=null&&rs.next()){
                PurchaseBydestunitid purchaseBydestunitid=new PurchaseBydestunitid();
                if(CommonUtil.isNotBlank(rs.getObject(1))){
                    purchaseBydestunitid.setDestunitname(rs.getObject(1).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(2))){
                    purchaseBydestunitid.setQty(rs.getObject(2).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(3))){
                    purchaseBydestunitid.setTotactprice(rs.getObject(3).toString());
                }
              /*  if(CommonUtil.isNotBlank(rs.getObject(4))){
                    purchaseBystyleid.setTotactprice(rs.getObject(4).toString());
                }*/
                list.add(purchaseBydestunitid);
            }
            DataSourceResult result = new DataSourceResult();
            result.setData(list);
            Object object = cs.getObject(8);
            result.setTotal(Integer.parseInt(object.toString()));
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
