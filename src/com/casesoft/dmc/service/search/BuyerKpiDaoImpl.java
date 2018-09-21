package com.casesoft.dmc.service.search;

import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.dao.search.BuyerKpiDao;
import com.casesoft.dmc.model.search.BuyerKpi;
import oracle.jdbc.driver.OracleTypes;
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

@Transactional
@Component
public class BuyerKpiDaoImpl implements BuyerKpiDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public DataSourceResult getBuyerKpi(DataSourceRequest request) {
        DataSourceResult result = null;
        try (Connection connection = SessionFactoryUtils.getDataSource(sessionFactory).getConnection();
             CallableStatement cs = connection.prepareCall("{call buyerKpi(?,?,?,?,?,?,?)}")) {

            List<DataSourceRequest.FilterDescriptor> filters = request.getFilter().getFilters();

            //设置参数
            cs.setString(1, "");
            cs.setString(2, "");
            cs.setString(3, "");
            for (DataSourceRequest.FilterDescriptor filterDescriptor : filters) {
                if (filterDescriptor.getField().equals("billDate") && filterDescriptor.getOperator().equals("gte")) {
                    String timeStr = (String) filterDescriptor.getValue();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    Date date = formatter.parse(timeStr);
                    String time = CommonUtil.getDateString(date, "yyyy-MM-dd");
                    cs.setString(1, time);
                } else if (filterDescriptor.getField().equals("billDate") && filterDescriptor.getOperator().equals("lte")) {
                    String timeStr = (String) filterDescriptor.getValue();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    Date date = formatter.parse(timeStr);
                    String time = CommonUtil.getDateString(date, "yyyy-MM-dd");
                    cs.setString(2, time);
                } else if (filterDescriptor.getField().equals("buyerName")) {
                    String buyerId = (String) filterDescriptor.getValue();
                    cs.setString(3, buyerId);
                }
            }

            Integer beginIndex = (request.getPage() - 1) * (request.getPageSize()) + 1;
            Integer endIndex = (request.getPage()) * (request.getPageSize());
            cs.setDouble(4, beginIndex.doubleValue());
            cs.setDouble(5, endIndex.doubleValue());

            cs.registerOutParameter(6, Types.INTEGER);
            cs.registerOutParameter(7, OracleTypes.CURSOR);

            cs.execute();

            ResultSet rs = (ResultSet) cs.getObject(7);

            List<BuyerKpi> buyerKpiArrayList = new ArrayList<BuyerKpi>();
            while (rs != null && rs.next()) {
                BuyerKpi buyerKpi = new BuyerKpi();
                buyerKpi.setId(rs.getObject(1) == null ? "" : rs.getObject(1).toString());
                buyerKpi.setBuyerId(rs.getObject(2) == null ? "" : rs.getObject(2).toString());
                buyerKpi.setBuyerName(rs.getObject(3) == null ? "" : rs.getObject(3).toString());
                buyerKpi.setPurchaseQty(rs.getObject(4) == null ? 0L : Long.valueOf(rs.getObject(4).toString()));
                buyerKpi.setPurchaseInQty(rs.getObject(5) == null ? 0L : Long.valueOf(rs.getObject(5).toString()));
                buyerKpi.setStockQty(rs.getObject(6) == null ? 0L : Long.valueOf(rs.getObject(6).toString()));
                buyerKpi.setSaleQty(rs.getObject(7) == null ? 0L : Long.valueOf(rs.getObject(7).toString()));
                buyerKpi.setReturnBackQty(rs.getObject(8) == null ? 0L : Long.valueOf(rs.getObject(8).toString()));
                buyerKpiArrayList.add(buyerKpi);
            }
            result = new DataSourceResult();
            result.setData(buyerKpiArrayList);
            result.setTotal(Long.valueOf(cs.getObject(6).toString()));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
