package com.casesoft.dmc.service.search;

import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.dao.search.BuyerKpiStyleDetailDao;
import com.casesoft.dmc.model.search.BuyerKpiStyleDetail;
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
public class BuyerKpiStyleDetailDaoImpl implements BuyerKpiStyleDetailDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public DataSourceResult getStyleDetail(DataSourceRequest request) throws SQLException {
        DataSourceResult result = null;
        try (Connection connection = SessionFactoryUtils.getDataSource(sessionFactory).getConnection();
             CallableStatement cs = connection.prepareCall("{call buyerKpiStyleDetail(?,?,?,?,?,?,?,?)}")) {

            List<DataSourceRequest.FilterDescriptor> filters = request.getFilter().getFilters();

            //设置参数
            cs.setString(1, "");
            cs.setString(2, "");
            cs.setString(3, "");
            cs.setString(4, "");
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
                } else if (filterDescriptor.getField().equals("buyerId")) {
                    String buyerId = (String) filterDescriptor.getValue();
                    cs.setString(3, buyerId);
                } else if (filterDescriptor.getField().equals("styleId")) {
                    String styleId = (String) filterDescriptor.getValue();
                    cs.setString(4, styleId);
                }
            }

            Integer beginIndex = (request.getPage() - 1) * (request.getPageSize()) + 1;
            Integer endIndex = (request.getPage()) * (request.getPageSize());
            cs.setDouble(5, beginIndex.doubleValue());
            cs.setDouble(6, endIndex.doubleValue());

            cs.registerOutParameter(7, Types.INTEGER);
            cs.registerOutParameter(8, OracleTypes.CURSOR);

            cs.execute();

            ResultSet rs = (ResultSet) cs.getObject(8);

            List<BuyerKpiStyleDetail> styleDetailArrayList = new ArrayList<BuyerKpiStyleDetail>();
            while (rs != null && rs.next()) {
                BuyerKpiStyleDetail styleDetail = new BuyerKpiStyleDetail();
                styleDetail.setId(rs.getObject(1) == null ? "" : rs.getObject(1).toString());
                styleDetail.setStyleId(rs.getObject(2) == null ? "" : rs.getObject(2).toString());
                styleDetail.setPurchaseQty(rs.getObject(3) == null ? 0L : Long.valueOf(rs.getObject(3).toString()));
                styleDetail.setPurchaseInQty(rs.getObject(4) == null ? 0L : Long.valueOf(rs.getObject(4).toString()));
                styleDetail.setStockQty(rs.getObject(5) == null ? 0L : Long.valueOf(rs.getObject(5).toString()));
                styleDetail.setSaleQty(rs.getObject(6) == null ? 0L : Long.valueOf(rs.getObject(6).toString()));
                styleDetail.setReturnBackQty(rs.getObject(7) == null ? 0L : Long.valueOf(rs.getObject(7).toString()));
                styleDetailArrayList.add(styleDetail);
            }
            result = new DataSourceResult();
            result.setData(styleDetailArrayList);
            result.setTotal(Long.valueOf(cs.getObject(7).toString()));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
