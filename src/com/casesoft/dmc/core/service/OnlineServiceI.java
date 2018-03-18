package com.casesoft.dmc.core.service;

import com.casesoft.dmc.core.model.Tonline;

/**
 * 在线用户Service
 * 
 * @author 孙宇
 * 
 */
public interface OnlineServiceI {

	/**
	 * 更新或插入用户在线列表
	 * 
	 * @param online
	 */
	public void updateOnline(Tonline online);

	/**
	 * 删除用户在线列表
	 * 
	 * @param loginName
	 * @param ip
	 */
	public void deleteOnline(String loginName, String ip);

	/**
	 * 获得用户在线列表datagrid
	 * 
	 * @param online
	 * @return
	 */
//	public DataGrid datagrid(Online online);

}
