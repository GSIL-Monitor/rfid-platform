package com.casesoft.dmc.core.controller;

import java.util.List;

import com.casesoft.dmc.core.util.page.Page;

public interface ISearchController<T> {

	public abstract Page<T> findPage(Page<T> page) throws Exception;
	public abstract List<T> list() throws Exception;
    public abstract void exportExcel() throws Exception;

}
