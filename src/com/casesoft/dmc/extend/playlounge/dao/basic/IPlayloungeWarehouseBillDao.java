package com.casesoft.dmc.extend.playlounge.dao.basic;

import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.erp.BillDtl;

import java.util.List;

public interface IPlayloungeWarehouseBillDao {
    public List<Bill> findWarehouseInBills(String storageId, String beginDate,
                                           String endDate, String ownerId, int token);

    public List<BillDtl> findWarehouseInBillDtls(String billId, int token);

    public List<Bill> findWarehouseReturnOutBills(String storageId,
                                                  String beginDate, String endDate, String ownerId, int token);

    public List<BillDtl> findWarehouseReturnOutBillDtls(String billId, int token);

    public List<Bill> findWarehouseReturnInBills(String storageId,
                                                 String beginDate, String endDate, String ownerId, int token);

    public List<BillDtl> findWarehouseReturnInBillDtls(String billId, int token);

    public List<Bill> findWarehouseOutBills(String storageId, String beginDate,
                                            String endDate, String ownerId, int token);

    public List<BillDtl> findWarehouseOutBillDtls(String billId, int token);

    public List<Bill> findWarehouseTransferInBills(String storageId,
                                                   String beginDate, String endDate, String ownerId, int token);

    public List<BillDtl> findWarehouseTransferInBillDtls(String billId,
                                                         int token);

    public List<Bill> findWarehouseTransferOutBills(String storageId,
                                                    String beginDate, String endDate, String ownerId, int token);

    public List<BillDtl> findWarehouseTransferOutBillDtls(String billId,
                                                          int token);

    public List<Bill> findWarehouseInventoryBills(String storageId,
                                                  String beginDate, String endDate,
                                                  String billDate, String conditions,
                                                  String ownerId, int token);

    public List<BillDtl> findWarehouseInventoryBillDtls(String billId, String billDate,
                                                        String conditions, int token);

}
