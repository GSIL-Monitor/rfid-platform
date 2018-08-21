package com.casesoft.dmc.core.controller;

import com.casesoft.dmc.core.util.page.Page;

import java.util.List;

public interface ISearchController<T> {

	public abstract Page<T> findPage(Page<T> page) throws Exception;
	public abstract List<T> list() throws Exception;
    public abstract void exportExcel() throws Exception;

}
