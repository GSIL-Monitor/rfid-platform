package com.casesoft.dmc.extend.playlounge.dao.basic;

import com.casesoft.dmc.model.task.Business;

public interface IPlayloungeWarehousePostTaskDao {

	public void batchWarehouseInTask(Business bus);

	public void batchWarehouseOutTask(Business bus);

	public void batchWarehouseReturnInTask(Business bus);
	
	public void batchWarehouseReturnOutTask(Business bus);

	public void batchWarehouseTransferInTask(Business bus);

	public void batchWarehouseTransferOutTask(Business bus);
	
	public void batchWarehouseInventoryTask(Business bus);
}
