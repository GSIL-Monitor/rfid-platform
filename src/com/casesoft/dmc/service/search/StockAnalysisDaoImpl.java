package com.casesoft.dmc.service.search;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.dao.search.StockAnalysisDao;
import com.casesoft.dmc.model.search.StockAnalysis;
import com.casesoft.dmc.model.sys.Unit;
import oracle.jdbc.driver.OracleTypes;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yushen on 2017/11/17.
 */
@Transactional
@Component
public class StockAnalysisDaoImpl implements StockAnalysisDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public DataSourceResult getStockAnalysis(DataSourceRequest request) throws Exception {
        Connection con = null;
        CallableStatement cs = null;
        try {
            Session session = sessionFactory.getCurrentSession();
            con = SessionFactoryUtils.getDataSource(session.getSessionFactory()).getConnection();
            cs = con.prepareCall("{call STOCKANALYSISTIMEANDQTY(?,?,?,?,?,?,?)}");

            List<DataSourceRequest.FilterDescriptor> filters = request.getFilter().getFilters();
            String wareHouseId = null;
            Integer inStockQtyThreshold = 0;
            Integer inStockTimeThreshold = 0;
            if (CommonUtil.isNotBlank(filters)) {
                for (DataSourceRequest.FilterDescriptor filter : filters) {
                    if ("wareHouseId".equals(filter.getField())) {
                        wareHouseId = filter.getValue().toString();
                    }
                    if ("inStockQty".equals(filter.getField())) {
                        inStockQtyThreshold = Integer.parseInt(filter.getValue().toString());
                    }
                    if ("inStockTime".equals(filter.getField())) {
                        inStockTimeThreshold = Integer.parseInt(filter.getValue().toString());
                    }
                }
            }
            cs.setString(1, wareHouseId);
            cs.setDouble(2, inStockQtyThreshold);
            cs.setDouble(3, inStockTimeThreshold);
            Integer beginIndex = (request.getPage() - 1) * (request.getPageSize()) + 1;
            Integer endIndex = (request.getPage()) * (request.getPageSize());
            cs.setDouble(4, beginIndex.doubleValue());
            cs.setDouble(5, endIndex.doubleValue());
            cs.registerOutParameter(6, Types.INTEGER);
            cs.registerOutParameter(7, OracleTypes.CURSOR);
            cs.execute();
            Integer rowSum = (Integer) cs.getObject(6);
            ResultSet rs = (ResultSet) cs.getObject(7);

            ArrayList<StockAnalysis> StockAnalysisList = new ArrayList<>();
            while (rs != null && rs.next()) {
                StockAnalysis stockAnalysis = new StockAnalysis();
                if (CommonUtil.isBlank(wareHouseId) || !wareHouseId.equals("All")) {
                    Object column1 = rs.getObject(1);
                    if (CommonUtil.isNotBlank(column1)) {
                        stockAnalysis.setWareHouseId(column1.toString());
                        Unit wareHouseName = CacheManager.getUnitById(column1.toString());
                        if (CommonUtil.isNotBlank(wareHouseName)) {
                            stockAnalysis.setWareHouseName(wareHouseName.getName());
                        }
                    }
                    Object column2 = rs.getObject(2);
                    if (CommonUtil.isNotBlank(column2)) {
                        stockAnalysis.setSku(column2.toString());
                    }
                    Object column3 = rs.getObject(3);
                    if (CommonUtil.isNotBlank(column3)) {
                        BigDecimal columnValue3 = (BigDecimal) column3;
                        stockAnalysis.setInStockQty(columnValue3.intValue());
                    }
                    Object column4 = rs.getObject(4);
                    if (CommonUtil.isNotBlank(column4)) {
                        BigDecimal temp = (BigDecimal) column4;
                        stockAnalysis.setInStockTime(temp.intValue());
                    }
                } else {
                    stockAnalysis.setWareHouseId("All");
                    stockAnalysis.setWareHouseName("所有仓库");
                    Object column1 = rs.getObject(1);
                    if (CommonUtil.isNotBlank(column1)) {
                        stockAnalysis.setSku(column1.toString());
                    }
                    Object column2 = rs.getObject(2);
                    if (CommonUtil.isNotBlank(column2)) {
                        BigDecimal columnValue3 = (BigDecimal) column2;
                        stockAnalysis.setInStockQty(columnValue3.intValue());
                    }
                    Object column3 = rs.getObject(3);
                    if (CommonUtil.isNotBlank(column3)) {
                        BigDecimal temp = (BigDecimal) column3;
                        stockAnalysis.setInStockTime(temp.intValue());
                    }
                }
                StockAnalysisList.add(stockAnalysis);
            }
            DataSourceResult result = new DataSourceResult();
            result.setData(StockAnalysisList);
            result.setTotal(rowSum);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (cs != null) {
                cs.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }
}
