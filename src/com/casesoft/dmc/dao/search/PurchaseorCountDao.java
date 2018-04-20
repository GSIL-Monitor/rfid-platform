package com.casesoft.dmc.dao.search;

import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;

import java.sql.SQLException;

/**
 * Created by Administrator on 2017/10/11.
 */
public interface PurchaseorCountDao {
    DataSourceResult getList(DataSourceRequest request);
    DataSourceResult getpurchaseList(DataSourceRequest request);
    DataSourceResult getPurchaseBybystyleidList(DataSourceRequest request) throws SQLException;
    DataSourceResult getPurchaseBydestunitidList(DataSourceRequest request) throws SQLException;
}
