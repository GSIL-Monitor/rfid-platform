package com.casesoft.dmc.service.search;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.search.FindSkuInformationDao;
import com.casesoft.dmc.model.logistics.ReplenishBill;
import com.casesoft.dmc.model.search.DetailStockChatView;
import com.casesoft.dmc.model.search.FindSkuInformation;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/5/4.
 */
@Service
@Transactional
public class FindSkuInformationService implements IBaseService<FindSkuInformation, String> {
    @Autowired
    private FindSkuInformationDao findSkuInformationDao;
    @Autowired
    private SessionFactory sessionFactory;
    @Override
    public Page<FindSkuInformation> findPage(Page<FindSkuInformation> page, List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public void save(FindSkuInformation entity) {

    }

    @Override
    public FindSkuInformation load(String id) {
        return null;
    }

    @Override
    public FindSkuInformation get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<FindSkuInformation> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<FindSkuInformation> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(FindSkuInformation entity) {

    }

    @Override
    public void delete(FindSkuInformation entity) {

    }

    @Override
    public void delete(String id) {

    }

    public Page<FindSkuInformation> findPagePro(Page<FindSkuInformation> page, List<PropertyFilter> filters){
        ResultSet rs=null;
        CallableStatement cs=null;
        Connection con=null;
        try {
            Session session = sessionFactory.getCurrentSession();
            con = SessionFactoryUtils.getDataSource(session.getSessionFactory()).getConnection();
            cs = con.prepareCall("{call findskuinformation(?,?,?,?,?,?,?,?,?,?,?)}");
            //设置参数
            //填充空数据
            cs.setString(1, "");
            cs.setString(2, "");
            cs.setString(3, "");
            cs.setString(4, "");
            cs.setString(5, "");
            cs.setString(6, "");
            cs.setString(7, "");
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
                    String billNo =(String) propertyFilter.getMatchValue();
                    cs.setString(4, billNo);
                }
                if(propertyName.equals("class1")&&name.equals("EQ")){
                    String class1 =(String) propertyFilter.getMatchValue();
                    cs.setString(5, class1);
                }
                if(propertyName.equals("origUnitId")&&name.equals("EQ")){
                    String origUnitId =(String) propertyFilter.getMatchValue();
                    cs.setString(6, origUnitId);
                }
                if(propertyName.equals("buyahandid")&&name.equals("EQ")){
                    String buyahandid =(String) propertyFilter.getMatchValue();
                    cs.setString(7, buyahandid);
                }

            }
            Integer beginIndex=(page.getPage()-1)*(page.getPageSize())+1;
            Integer endIndex=(page.getPage())*(page.getPageSize());
            cs.setDouble(8,beginIndex.doubleValue());
            cs.setDouble(9,endIndex.doubleValue());
            cs.registerOutParameter(10, Types.INTEGER);
            cs.registerOutParameter(11, OracleTypes.CURSOR);
            //cs.registerOutParameter("resultSet", -10);
            cs.execute();
            rs=(ResultSet)cs.getObject(11);
            ArrayList<FindSkuInformation> list=new ArrayList<FindSkuInformation>();
            while (rs!=null&&rs.next()){
                FindSkuInformation findSkuInformation=new FindSkuInformation();
                if(CommonUtil.isNotBlank(rs.getObject(1))){
                    findSkuInformation.setSku(rs.getObject(1).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(2))){
                    findSkuInformation.setFristtime(rs.getObject(2).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(3))){
                    findSkuInformation.setEndtime(rs.getObject(3).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(4))){
                    findSkuInformation.setBilldate(rs.getObject(4).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(5))){
                    findSkuInformation.setId(rs.getObject(5).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(6))){
                    findSkuInformation.setQty(rs.getObject(6).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(7))){
                    findSkuInformation.setInqty(rs.getObject(7).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(8))){
                    findSkuInformation.setDestname(rs.getObject(8).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(9))){
                    findSkuInformation.setDestid(rs.getObject(9).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(10))){
                    findSkuInformation.setInstocktype(rs.getObject(10).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(11))){
                    findSkuInformation.setOrigunitname(rs.getObject(11).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(12))){
                    findSkuInformation.setClass1name(rs.getObject(12).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(13))){
                    findSkuInformation.setBuyahandname(rs.getObject(13).toString());
                }
                if(CommonUtil.isNotBlank(rs.getObject(14))){
                    findSkuInformation.setStyleid(rs.getObject(14).toString());
                }
                list.add(findSkuInformation);
            }
            page.setRows(list);
            Object object = cs.getObject(10);
            page.setTotal(Integer.parseInt(object.toString()));
            int totPage=Integer.parseInt(object.toString())/page.getPageSize();
            page.setTotPage(totPage+1);
            return page;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

}
