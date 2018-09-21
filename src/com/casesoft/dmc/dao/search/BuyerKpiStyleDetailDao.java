package com.casesoft.dmc.dao.search;

import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;

import java.sql.SQLException;

public interface BuyerKpiStyleDetailDao {
    DataSourceResult getStyleDetail(DataSourceRequest request) throws SQLException;
}
