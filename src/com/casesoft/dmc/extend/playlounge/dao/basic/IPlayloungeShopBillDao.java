package com.casesoft.dmc.extend.playlounge.dao.basic;

import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.erp.BillDtl;

import java.util.List;

public interface IPlayloungeShopBillDao {

	public List<Bill> findShopTransferInBills(String storageId,
                                              String beginDate, String endDate, String ownerId, int token);

	public List<BillDtl> findShopTransferInBillDtls(String billId, int token);

	public List<Bill> findShopTransferOutBills(String storageId,
                                               String beginDate, String endDate, String ownerId, int token);

	public List<BillDtl> findShopTransferOutBillDtls(String billId, int token);

	public List<Bill> findShopReturnOutBills(String storageId,
                                             String beginDate, String endDate, String ownerId, int token);

	public List<BillDtl> findShopReturnOutBillDtls(String billId, int token);

	public List<Bill> findShopInBills(String storageId, String beginDate,
                                      String endDate, String ownerId, int token);

	public List<BillDtl> findShopInBillDtls(String billId, int token);

	public List<Bill> findShopInventoryBills(String storageId,
											 String beginDate, String endDate,
											 String billDate, String conditions,
											 String ownerId, int token);

	public List<BillDtl> findShopInventoryBillDtls(String billId, String billDate,
												   String conditions, int token);

}
