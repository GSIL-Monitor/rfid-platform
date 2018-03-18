package com.casesoft.dmc.dao.search;

import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;

import javax.servlet.http.HttpSession;

public interface DetailStockDao {
	 DataSourceResult getList(DataSourceRequest request);
	DataSourceResult getCodeList(DataSourceRequest request);
	DataSourceResult getStyleList(DataSourceRequest request, HttpSession session);
}
