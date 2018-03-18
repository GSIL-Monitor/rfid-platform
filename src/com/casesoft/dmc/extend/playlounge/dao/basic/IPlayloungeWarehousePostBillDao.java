package com.casesoft.dmc.extend.playlounge.dao.basic;

import com.casesoft.dmc.model.task.Business;

public interface IPlayloungeWarehousePostBillDao {

	public void batchWarehouseInBill(Business bus);

	public void batchWarehouseOutBill(Business bus);

	public void batchWarehouseOutAddedBill(Business bus);

	public void batchWarehouseOutNotBill(Business bus);

	public void batchWarehouseOutCustBill(Business bus);

	public void batchWarehouseOutCustAddedBill(Business bus);

	public void batchWarehouseOutCustNotBill(Business bus);

	public void batchWarehouseReturnInBill(Business bus);
	
	public void batchWarehouseReturnInFromCustBill(Business bus);

	public void batchWarehouseReturnOutBill(Business bus);

	public void batchWarehouseTransferInBill(Business bus);

	public void batchWarehouseTransferOutBill(Business bus);

	public void batchWarehouseInventory(Business bus);
}
