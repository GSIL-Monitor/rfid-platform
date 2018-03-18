package com.casesoft.dmc.dao.search;

import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;

/**
 * Created by Administrator on 2017/8/24.
 */
public interface SaleorderCountDao {
    DataSourceResult getList(DataSourceRequest request);
    DataSourceResult getSaleList(DataSourceRequest request);
    DataSourceResult getSaleBybusinessnameList(DataSourceRequest request) throws Exception;
    DataSourceResult getSaleByorignameList(DataSourceRequest request) throws Exception;
}
