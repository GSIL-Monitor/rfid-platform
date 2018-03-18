package com.casesoft.dmc.dao.search;

import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;

/**
 * Created by yushen on 2017/11/17.
 */
public interface StockAnalysisDao {
    DataSourceResult getStockAnalysis(DataSourceRequest request) throws Exception;
}
