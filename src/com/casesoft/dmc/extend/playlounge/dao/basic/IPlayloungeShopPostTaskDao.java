package com.casesoft.dmc.extend.playlounge.dao.basic;

import com.casesoft.dmc.model.task.Business;

public interface IPlayloungeShopPostTaskDao {

	public void batchShopInTask(Business bus);

	public void batchShopTransferInTask(Business bus);
	
	public void batchShopTransferOutTask(Business bus);

	public void batchShopReturnOutTask(Business bus);
	
	public void batchShopInventoryTask(Business bus);
}
