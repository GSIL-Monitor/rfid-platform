package com.casesoft.dmc.dao.hall;

import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;

/**
 * Created by session on 2017/3/23 0023.
 */
public interface ISampleDao {
	public DataSourceResult getList(DataSourceRequest request);
}
