package com.casesoft.dmc.extend.tiantan.dao.basic;

import com.casesoft.dmc.model.task.Business;

public interface ITiantanPostBillDao {

	public void batchInBill(Business bus);

	public void batchInFromFactoryBill(Business bus);

	public void batchOutBill(Business bus);

	public void batchReturnInBill(Business bus);

	public void batchReturnOutBill(Business bus);

	public void batchTransferInBill(Business bus);

	public void batchTransferOutBill(Business bus);

	public void batchInventory(Business bus);

	public void batchInventoryBill(Business bus);

	public void batchShopInBill(Business bus);

	public void batchShopTransferInBill(Business bus);

	public void batchShopReturnOutBill(Business bus);
}
