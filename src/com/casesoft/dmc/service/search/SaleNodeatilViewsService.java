package com.casesoft.dmc.service.search;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.search.SaleNodeatilViewsDao;
import com.casesoft.dmc.model.logistics.SaleBybusinessname;
import com.casesoft.dmc.model.search.SaleNodeatilViews;
import com.casesoft.dmc.model.search.Salebystyleid;
import oracle.jdbc.driver.OracleTypes;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/12/21.
 */
@Service
@Transactional
public class SaleNodeatilViewsService implements IBaseService<SaleNodeatilViews, String> {
    @Autowired
    private SaleNodeatilViewsDao saleNodeatilViewsDao;
    @Autowired
    private SessionFactory sessionFactory;
    @Override
    public Page<SaleNodeatilViews> findPage(Page<SaleNodeatilViews> page, List<PropertyFilter> filters) {
        return this.saleNodeatilViewsDao.findPage(page, filters);
    }

    @Override
    public void save(SaleNodeatilViews entity) {

    }

    @Override
    public SaleNodeatilViews load(String id) {
        return null;
    }

    @Override
    public SaleNodeatilViews get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<SaleNodeatilViews> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<SaleNodeatilViews> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(SaleNodeatilViews entity) {

    }

    @Override
    public void delete(SaleNodeatilViews entity) {

    }

    @Override
    public void delete(String id) {

    }

    public List<SaleBybusinessname> WxfindSaleBybusinessnameList(String pageSize,String pageNo,List<PropertyFilter> filters) throws SQLException {
        ResultSet rs=null;
        CallableStatement cs=null;
        Connection con=null;
        try{
            Session session = sessionFactory.getCurrentSession();
            con = SessionFactoryUtils.getDataSource(session.getSessionFactory()).getConnection();
            cs = con.prepareCall("{call findsaleBybusinessnames(?,?,?,?,?,?,?,?)}");
            //设置参数

            //填充空数据
            cs.setString(1, "");
            cs.setString(2, "");
            cs.setString(3, "");
            cs.setString(4, "");
            for(int i=0;i<filters.size();i++){
                PropertyFilter propertyFilter = filters.get(i);
                String name = propertyFilter.getMatchType().name();
                String PropertyName = propertyFilter.getPropertyNames()[0];
                if(name.equals("GE")&&PropertyName.equals("billDate")){
                   Date date= (Date)propertyFilter.getMatchValue();
                    String time = CommonUtil.getDateString(date,"yyyy-MM-dd");
                    cs.setString(1, time);
                }
                if(name.equals("LE")&&PropertyName.equals("billDate")){
                    Date date= (Date)propertyFilter.getMatchValue();
                    String time = CommonUtil.getDateString(date,"yyyy-MM-dd");
                    cs.setString(2, time);
                }
                if(PropertyName.equals("deport")){
                    String deport=(String)propertyFilter.getMatchValue();
                    cs.setString(3, deport);
                }
                if(PropertyName.equals("groupid")){
                    String groupid=(String)propertyFilter.getMatchValue();
                    cs.setString(4, groupid);
                }

            }

            //Integer beginIndex=(request.getPage()-1)*(request.getPageSize())+1;
            //Integer endIndex=(request.getPage())*(request.getPageSize());

            Integer beginIndex=(Integer.parseInt(pageNo)-1)*(Integer.parseInt(pageSize))+1;
            Integer endIndex=(Integer.parseInt(pageNo))*(Integer.parseInt(pageSize));
            cs.setDouble(5,beginIndex.doubleValue());
            cs.setDouble(6,endIndex.doubleValue());
            cs.registerOutParameter(7, Types.INTEGER);
            cs.registerOutParameter(8, OracleTypes.CURSOR);
            //cs.registerOutParameter("resultSet", -10);
            cs.execute();
            rs=(ResultSet)cs.getObject(8);
            ArrayList<SaleBybusinessname> list=new ArrayList<SaleBybusinessname>();
            while (rs!=null&& rs.next()){
                SaleBybusinessname saleBybusinessname=new SaleBybusinessname();
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
                    saleBybusinessname.setSalesum(rs.getObject(5).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(6))){
                    saleBybusinessname.setSalereturnsum(rs.getObject(6).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(7))){
                    saleBybusinessname.setSalemoney(rs.getObject(7).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(8))){
                    saleBybusinessname.setSalereturnmoney(rs.getObject(8).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(9))){
                    saleBybusinessname.setGrossprofits(rs.getObject(9).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(10))){
                    saleBybusinessname.setTotactprice(rs.getObject(10).toString());
                }
                list.add(saleBybusinessname);
            }
            //DataSourceResult result = new DataSourceResult();
          /* Criteria criteria = session.createCriteria(SaleBybusinessname.class);
            sort(criteria, sortDescriptors());*/
           // result.setData(list);
            //result.setTotal(list.size());

           /* request.toDataSourceResult(sessionFactory.getCurrentSession(), SaleBybusinessname.class);*/

            return list;


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

    public List<SaleBybusinessname> WxfindSaleByorignameList(String pageSize,String pageNo,List<PropertyFilter> filters) throws SQLException {
        ResultSet rs=null;
        CallableStatement cs=null;
        Connection con=null;
        try{
            Session session = sessionFactory.getCurrentSession();
            con = SessionFactoryUtils.getDataSource(session.getSessionFactory()).getConnection();
            cs = con.prepareCall("{call findsaleByorignames(?,?,?,?,?,?,?,?)}");
            //设置参数

            //填充空数据
            cs.setString(1, "");
            cs.setString(2, "");
            cs.setString(3, "");
            cs.setString(4, "");
            for(int i=0;i<filters.size();i++){
                PropertyFilter propertyFilter = filters.get(i);
                String name = propertyFilter.getMatchType().name();
                String PropertyName = propertyFilter.getPropertyNames()[0];
                if(name.equals("GE")&&PropertyName.equals("billDate")){
                    Date date= (Date)propertyFilter.getMatchValue();
                    String time = CommonUtil.getDateString(date,"yyyy-MM-dd");
                    cs.setString(1, time);
                }
                if(name.equals("LE")&&PropertyName.equals("billDate")){
                    Date date= (Date)propertyFilter.getMatchValue();
                    String time = CommonUtil.getDateString(date,"yyyy-MM-dd");
                    cs.setString(2, time);
                }
                if(PropertyName.equals("deport")){
                    String deport=(String)propertyFilter.getMatchValue();
                    cs.setString(3, deport);
                }
                if(PropertyName.equals("groupid")){
                    String groupid=(String)propertyFilter.getMatchValue();
                    cs.setString(4, groupid);
                }

            }

            //Integer beginIndex=(request.getPage()-1)*(request.getPageSize())+1;
            //Integer endIndex=(request.getPage())*(request.getPageSize());

            Integer beginIndex=(Integer.parseInt(pageNo)-1)*(Integer.parseInt(pageSize))+1;
            Integer endIndex=(Integer.parseInt(pageNo))*(Integer.parseInt(pageSize));
            cs.setDouble(5,beginIndex.doubleValue());
            cs.setDouble(6,endIndex.doubleValue());
            cs.registerOutParameter(7, Types.INTEGER);
            cs.registerOutParameter(8, OracleTypes.CURSOR);
            //cs.registerOutParameter("resultSet", -10);
            cs.execute();
            rs=(ResultSet)cs.getObject(8);
            ArrayList<SaleBybusinessname> list=new ArrayList<SaleBybusinessname>();
            while (rs!=null&& rs.next()){
                SaleBybusinessname saleBybusinessname=new SaleBybusinessname();
                //String a=rs.getObject(1).toString();

                if(CommonUtil.isNotBlank(rs.getObject(1))){
                    saleBybusinessname.setOrigname(rs.getObject(1).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(2))){
                    saleBybusinessname.setPrecast(rs.getObject(2).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(3))){
                    saleBybusinessname.setGross(rs.getObject(3).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(4))){
                    saleBybusinessname.setSalesum(rs.getObject(4).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(5))){
                    saleBybusinessname.setSalereturnsum(rs.getObject(5).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(6))){
                    saleBybusinessname.setSalemoney(rs.getObject(6).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(7))){
                    saleBybusinessname.setSalereturnmoney(rs.getObject(7).toString());
                }

                if(CommonUtil.isNotBlank(rs.getObject(8))){
                    saleBybusinessname.setGrossprofits(rs.getObject(8).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(9))){
                    saleBybusinessname.setTotactprice(rs.getObject(9).toString());
                }
                list.add(saleBybusinessname);
            }
            //DataSourceResult result = new DataSourceResult();
          /* Criteria criteria = session.createCriteria(SaleBybusinessname.class);
            sort(criteria, sortDescriptors());*/
            // result.setData(list);
            //result.setTotal(list.size());

           /* request.toDataSourceResult(sessionFactory.getCurrentSession(), SaleBybusinessname.class);*/

            return list;


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
    public List<Salebystyleid> WxfindSalebystyleidList(String pageSize, String pageNo, List<PropertyFilter> filters) throws SQLException {
        ResultSet rs=null;
        CallableStatement cs=null;
        Connection con=null;
        try{
            Session session = sessionFactory.getCurrentSession();
            con = SessionFactoryUtils.getDataSource(session.getSessionFactory()).getConnection();
            cs = con.prepareCall("{call findsalebystyleid(?,?,?,?,?,?,?,?,?)}");
            //设置参数

            //填充空数据
            cs.setString(1, "");
            cs.setString(2, "");
            cs.setString(3, "");
            cs.setString(4, "");
            cs.setString(5, "");
            for(int i=0;i<filters.size();i++){
                PropertyFilter propertyFilter = filters.get(i);
                String name = propertyFilter.getMatchType().name();
                String PropertyName = propertyFilter.getPropertyNames()[0];
                if(name.equals("GE")&&PropertyName.equals("billDate")){
                    Date date= (Date)propertyFilter.getMatchValue();
                    String time = CommonUtil.getDateString(date,"yyyy-MM-dd");
                    cs.setString(1, time);
                }
                if(name.equals("LE")&&PropertyName.equals("billDate")){
                    Date date= (Date)propertyFilter.getMatchValue();
                    String time = CommonUtil.getDateString(date,"yyyy-MM-dd");
                    cs.setString(2, time);
                }
                if(PropertyName.equals("deport")){
                    String deport=(String)propertyFilter.getMatchValue();
                    cs.setString(3, deport);
                }
                if(PropertyName.equals("groupid")){
                    String groupid=(String)propertyFilter.getMatchValue();
                    cs.setString(4, groupid);
                }
                if(PropertyName.equals("styleid")){
                    String styleid=(String)propertyFilter.getMatchValue();
                    cs.setString(5, styleid);
                }

            }

            //Integer beginIndex=(request.getPage()-1)*(request.getPageSize())+1;
            //Integer endIndex=(request.getPage())*(request.getPageSize());

            Integer beginIndex=(Integer.parseInt(pageNo)-1)*(Integer.parseInt(pageSize))+1;
            Integer endIndex=(Integer.parseInt(pageNo))*(Integer.parseInt(pageSize));
            cs.setDouble(6,beginIndex.doubleValue());
            cs.setDouble(7,endIndex.doubleValue());
            cs.registerOutParameter(8, Types.INTEGER);
            cs.registerOutParameter(9, OracleTypes.CURSOR);
            //cs.registerOutParameter("resultSet", -10);
            cs.execute();
            rs=(ResultSet)cs.getObject(9);
            ArrayList<Salebystyleid> list=new ArrayList<Salebystyleid>();
            while (rs!=null&& rs.next()){
                Salebystyleid salebystyleid=new Salebystyleid();
                //String a=rs.getObject(1).toString();
                if(CommonUtil.isNotBlank(rs.getObject(1))){

                    salebystyleid.setId(rs.getObject(1).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(2))){

                    salebystyleid.setStyleid(rs.getObject(2).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(3))){

                    salebystyleid.setStylenum(Integer.parseInt(rs.getObject(3).toString()));
                }
                if(CommonUtil.isNotBlank(rs.getObject(4))){

                    salebystyleid.setStylename(rs.getObject(4).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(5))){

                    salebystyleid.setPrice(Double.parseDouble(rs.getObject(5).toString()));
                }
                if(CommonUtil.isNotBlank(rs.getObject(6))){

                    salebystyleid.setPrecast(Double.parseDouble(rs.getObject(6).toString()));
                }
                list.add(salebystyleid);
            }
            //DataSourceResult result = new DataSourceResult();
          /* Criteria criteria = session.createCriteria(SaleBybusinessname.class);
            sort(criteria, sortDescriptors());*/
            // result.setData(list);
            //result.setTotal(list.size());

           /* request.toDataSourceResult(sessionFactory.getCurrentSession(), SaleBybusinessname.class);*/

            return list;


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
