package com.casesoft.dmc.extend.playlounge.dao.basic;

import com.casesoft.dmc.model.task.Business;

public interface IPlayloungeShopPostBillDao {

	public void batchShopInBill(Business bus);

	public void batchShopTransferInBill(Business bus);
	
	public void batchShopTransferOutBill(Business bus);

	public void batchShopInventory(Business bus);

	public void batchShopReturnOutBill(Business bus);
}
