package com.casesoft.dmc.service.search;

import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.dao.search.SaleorderCountDao;
import com.casesoft.dmc.model.logistics.SaleByOrignames;
import com.casesoft.dmc.model.logistics.SaleBybusinessname;
import com.casesoft.dmc.model.search.SaleNodeatilViews;
import com.casesoft.dmc.model.search.SaleorderCountView;
import oracle.jdbc.driver.OracleTypes;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/8/23.
 */
@Transactional
@Component
public class SaleorderCountViewDaoImpl implements SaleorderCountDao {
    @Autowired
    private SessionFactory sessionFactory;
  /*  private List<DataSourceRequest.SortDescriptor> sort;
    private List<DataSourceRequest.GroupDescriptor> group;

    public List<DataSourceRequest.SortDescriptor> getSort() {
        return sort;
    }

    public void setSort(List<DataSourceRequest.SortDescriptor> sort) {
        this.sort = sort;
    }

    public List<DataSourceRequest.GroupDescriptor> getGroup() {
        return group;
    }

    public void setGroup(List<DataSourceRequest.GroupDescriptor> group) {
        this.group = group;
    }*/

    @Override
    public DataSourceResult getList(DataSourceRequest request) {
        try {
            return request.toDataSourceResult(sessionFactory.getCurrentSession(), SaleorderCountView.class);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public DataSourceResult getSaleList(DataSourceRequest request) {
        try {
            return request.toDataSourceResult(sessionFactory.getCurrentSession(), SaleNodeatilViews.class);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public DataSourceResult getSaleBybusinessnameList(DataSourceRequest request) throws Exception {
        ResultSet rs=null;
        CallableStatement cs=null;
        Connection con=null;
        try{
            Session session = sessionFactory.getCurrentSession();
            con = SessionFactoryUtils.getDataSource(session.getSessionFactory()).getConnection();
            cs = con.prepareCall("{call findsaleBybusinessnames(?,?,?,?,?,?,?,?)}");
            //设置参数
            DataSourceRequest.FilterDescriptor filter = request.getFilter();
            List<DataSourceRequest.FilterDescriptor> filters = filter.getFilters();
            //填充空数据
            cs.setString(1, "");
            cs.setString(2, "");
            cs.setString(3, "");
            cs.setString(4, "");
            for(int i=0;i<filters.size();i++){
                DataSourceRequest.FilterDescriptor filterDescriptor = filters.get(i);
                if(filterDescriptor.getField().equals("billDate")&&filterDescriptor.getOperator().equals("gte")){
                    String timeStr=(String)filterDescriptor.getValue();
                    SimpleDateFormat formatter = new SimpleDateFormat(
                            "yyyy-MM-dd'T'HH:mm:ss");
                    Date date = formatter.parse(timeStr);
                    String time =CommonUtil.getDateString(date,"yyyy-MM-dd");
                    cs.setString(1, time);
                }else if(filterDescriptor.getField().equals("billDate")&&filterDescriptor.getOperator().equals("lte")){
                    String timeStr=(String)filterDescriptor.getValue();
                    SimpleDateFormat formatter = new SimpleDateFormat(
                            "yyyy-MM-dd'T'HH:mm:ss");
                    Date date = formatter.parse(timeStr);
                    String time =CommonUtil.getDateString(date,"yyyy-MM-dd");
                    cs.setString(2, time);
                }else if(filterDescriptor.getField().equals("deport")){
                    String deport=(String)filterDescriptor.getValue();
                    cs.setString(3, deport);
                }else if(filterDescriptor.getField().equals("groupid")){
                    String groupid=(String)filterDescriptor.getValue();
                    cs.setString(4, groupid);
                }
            }

            Integer beginIndex=(request.getPage()-1)*(request.getPageSize())+1;
            Integer endIndex=(request.getPage())*(request.getPageSize());
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
           DataSourceResult result = new DataSourceResult();
          /* Criteria criteria = session.createCriteria(SaleBybusinessname.class);
            sort(criteria, sortDescriptors());*/
            result.setData(list);
            result.setTotal(Long.parseLong(cs.getObject(7)+""));

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
    public DataSourceResult getSaleByorignameList(DataSourceRequest request) throws Exception {
        ResultSet rs=null;
        CallableStatement cs=null;
        Connection con=null;
        try{
            Session session = sessionFactory.getCurrentSession();
            con = SessionFactoryUtils.getDataSource(session.getSessionFactory()).getConnection();
            cs = con.prepareCall("{call findsaleByorignames(?,?,?,?,?,?,?,?)}");
            //设置参数
            DataSourceRequest.FilterDescriptor filter = request.getFilter();
            List<DataSourceRequest.FilterDescriptor> filters = filter.getFilters();
            //填充空数据
            cs.setString(1, "");
            cs.setString(2, "");
            cs.setString(3, "");
            cs.setString(4, "");
            for(int i=0;i<filters.size();i++){
                DataSourceRequest.FilterDescriptor filterDescriptor = filters.get(i);
                if(filterDescriptor.getField().equals("billDate")&&filterDescriptor.getOperator().equals("gte")){
                    String timeStr=(String)filterDescriptor.getValue();
                    SimpleDateFormat formatter = new SimpleDateFormat(
                            "yyyy-MM-dd'T'HH:mm:ss");
                    Date date = formatter.parse(timeStr);
                    String time =CommonUtil.getDateString(date,"yyyy-MM-dd");
                    cs.setString(1, time);
                }else if(filterDescriptor.getField().equals("billDate")&&filterDescriptor.getOperator().equals("lte")){
                    String timeStr=(String)filterDescriptor.getValue();
                    SimpleDateFormat formatter = new SimpleDateFormat(
                            "yyyy-MM-dd'T'HH:mm:ss");
                    Date date = formatter.parse(timeStr);
                    String time =CommonUtil.getDateString(date,"yyyy-MM-dd");
                    cs.setString(2, time);
                }else if(filterDescriptor.getField().equals("deport")){
                    String deport=(String)filterDescriptor.getValue();
                    cs.setString(3, deport);
                }else if(filterDescriptor.getField().equals("groupid")){
                    String groupid=(String)filterDescriptor.getValue();
                    cs.setString(4, groupid);
                }
            }

            Integer beginIndex=(request.getPage()-1)*(request.getPageSize())+1;
            Integer endIndex=(request.getPage())*(request.getPageSize());
            cs.setDouble(5,beginIndex.doubleValue());
            cs.setDouble(6,endIndex.doubleValue());
            cs.registerOutParameter(7, Types.INTEGER);
            cs.registerOutParameter(8, OracleTypes.CURSOR);
            //cs.registerOutParameter("resultSet", -10);
            cs.execute();
            rs=(ResultSet)cs.getObject(8);
            ArrayList<SaleByOrignames> list=new ArrayList<SaleByOrignames>();
            while (rs!=null&& rs.next()){
                SaleByOrignames saleByOrignames=new SaleByOrignames();
                //String a=rs.getObject(1).toString();
                /*if(CommonUtil.isNotBlank(rs.getObject(1))){
                    saleBybusinessname.setBusnissname(rs.getObject(1).toString());
                }*/
                if(CommonUtil.isNotBlank(rs.getObject(1))){
                    saleByOrignames.setOrigname(rs.getObject(1).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(2))){
                    saleByOrignames.setPrecast(rs.getObject(2).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(3))){
                    saleByOrignames.setGross(rs.getObject(3).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(4))){
                    saleByOrignames.setSalesum(rs.getObject(4).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(5))){
                    saleByOrignames.setSalereturnsum(rs.getObject(5).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(6))){
                    saleByOrignames.setSalemoney(rs.getObject(6).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(7))){
                    saleByOrignames.setSalereturnmoney(rs.getObject(7).toString());
                }

                if(CommonUtil.isNotBlank(rs.getObject(8))){
                    saleByOrignames.setGrossprofits(rs.getObject(8).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(9))){
                    saleByOrignames.setTotactprice(rs.getObject(9).toString());
                }
                list.add(saleByOrignames);
            }
            DataSourceResult result = new DataSourceResult();
          /* Criteria criteria = session.createCriteria(SaleBybusinessname.class);
            sort(criteria, sortDescriptors());*/
            result.setData(list);
            result.setTotal(Long.parseLong(cs.getObject(7)+""));

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
   /* private List<DataSourceRequest.SortDescriptor> sortDescriptors() {
        List<DataSourceRequest.SortDescriptor> sort = new ArrayList<DataSourceRequest.SortDescriptor>();

        List<DataSourceRequest.GroupDescriptor> groups = getGroup();
        List<DataSourceRequest.SortDescriptor> sorts = getSort();

        if (groups != null) {
            sort.addAll(groups);
        }

        if (sorts != null) {
            sort.addAll(sorts);
        }
        return sort;
    }
    private static void sort(Criteria criteria, List<DataSourceRequest.SortDescriptor> sort) {
        if (sort != null && !sort.isEmpty()) {
            for (DataSourceRequest.SortDescriptor entry : sort) {
                String field = entry.getField();
                String dir = entry.getDir();

                if (dir.equals("asc")) {
                    criteria.addOrder(Order.asc(field));
                } else if (dir.equals("desc")) {
                    criteria.addOrder(Order.desc(field));
                }
            }
        }
    }*/

}
