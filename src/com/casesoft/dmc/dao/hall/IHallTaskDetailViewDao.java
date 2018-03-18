package com.casesoft.dmc.dao.hall;

import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;

/**
 * Created by session on 2017/3/22 0022.
 */
public interface IHallTaskDetailViewDao {
	public DataSourceResult getList(DataSourceRequest request);
}
