package com.casesoft.dmc.service.syn;

import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.erp.BillDtl;
import com.casesoft.dmc.model.erp.ErpStock;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.shop.SaleBill;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.Record;

import java.util.ArrayList;
import java.util.List;

public interface IBillWSService {

	List<Bill> findBills(String[] properties, String[] values)
			throws Exception;

	List<BillDtl> findBillDtls(String[] properties, String[] values)
			throws Exception;
	List<ErpStock> findErpStock(String[] properties, String[] values)
			throws Exception;
	List<Product> findErpBasic(String styleId, String colorId);
	List<BillDtl> findBillDtls(String id);

	String findBillsJSON(String[] properties, String[] values)
			throws Exception;

	String findBillDtlsJSON(String[] properties, String[] values)
			throws Exception;

	String findBillDtlsJSON(String id) throws Exception;

	MessageBox uploadToERP(Bill bill);

	MessageBox uploadTaskToErp(Business bus) throws Exception;

	MessageBox delete(String id);

	MessageBox update(Business bus);

	MessageBox uploadPosToERP(SaleBill bill);
	
	Bill productBill(String[] properties, String[] values);
	
	String destroyBill(String[] properties, String[] values);
    MessageBox updateUnitInfo(Business bus);

    default MessageBox checkEpcStock(Business bus)throws Exception {
    	return new MessageBox(true,"默认方法实现");
    }

	default MessageBox checkEpcStock(String uniqueCodeList, int token, String deviceId) {
    	return new MessageBox(true,"默认方法实现");
	}
	List<Record> findRecordByTask(String taskId);
}
