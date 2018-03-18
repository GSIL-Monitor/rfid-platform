package com.casesoft.dmc.dao.hall;

import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;

/**
 * Created by session on 2017/3/30 0030.
 */
public interface IHallInventoryDao {
	public DataSourceResult getList(DataSourceRequest request);
}
