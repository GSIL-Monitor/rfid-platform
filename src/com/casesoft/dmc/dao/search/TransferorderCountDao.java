package com.casesoft.dmc.dao.search;

import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;

/**
 * Created by Administrator on 2018/3/1.
 */
public interface TransferorderCountDao {
    DataSourceResult getList(DataSourceRequest request);
    DataSourceResult getTranList(DataSourceRequest request);
    DataSourceResult getTransByStyleId(DataSourceRequest request) throws Exception;
    DataSourceResult getTransByOrig(DataSourceRequest request) throws Exception;
    DataSourceResult getTransBystyleandsize(DataSourceRequest request) throws Exception;
}
