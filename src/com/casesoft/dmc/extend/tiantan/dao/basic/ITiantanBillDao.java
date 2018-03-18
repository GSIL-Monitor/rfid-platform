package com.casesoft.dmc.extend.tiantan.dao.basic;

import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.erp.BillDtl;

import java.util.List;

public interface ITiantanBillDao {
	public List<Bill> findInBills(String storageId, String beginDate,
                                  String endDate, String ownerId, int token);

	public List<BillDtl> findInBillDtls(String billId, int token);

	public List<Bill> findReturnOutBills(String storageId, String beginDate,
                                         String endDate, String ownerId, int token);

	public List<BillDtl> findReturnOutBillDtls(String billId, int token);

	public List<Bill> findReturnInBills(String storageId, String beginDate,
                                        String endDate, String ownerId, int token);

	public List<BillDtl> findReturnInBillDtls(String billId, int token);

	public List<Bill> findOutBills(String storageId, String beginDate,
                                   String endDate, String ownerId, int token);

	public List<BillDtl> findOutBillDtls(String billId, int token);

	public List<Bill> findTransferInBills(String storageId, String beginDate,
                                          String endDate, String ownerId, int token);

	public List<BillDtl> findTransferInBillDtls(String billId, int token);

	public List<Bill> findTransferOutBills(String storageId, String beginDate,
                                           String endDate, String ownerId, int token);

	public List<BillDtl> findTransferOutBillDtls(String billId, int token);

	public List<Bill> findShopReturnOutBills(String storageId,
                                             String beginDate, String endDate, String ownerId, int token);

	public List<BillDtl> findShopReturnOutBillDtls(String billId, int token);

	public List<Bill> findInventoryBills(String beginDate, String endDate,
                                         String ownerId, int token);

	public List<BillDtl> findInventoryBillDtls(final String billId,
                                               final int token);

}
