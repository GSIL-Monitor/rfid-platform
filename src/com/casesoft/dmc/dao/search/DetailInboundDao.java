package com.casesoft.dmc.dao.search;

import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;

public interface DetailInboundDao {
	 public DataSourceResult getList(DataSourceRequest request);
}
