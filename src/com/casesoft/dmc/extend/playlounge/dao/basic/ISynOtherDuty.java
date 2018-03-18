package com.casesoft.dmc.extend.playlounge.dao.basic;

 import com.casesoft.dmc.model.shop.SaleBill;
 import com.casesoft.dmc.model.tag.Epc;

import java.util.List;

public interface ISynOtherDuty {
	public void batchEpc(List<Epc> epcs);

	public void batchFittingRecord();

	public List<SaleBill> downloadSaleInfo();
}
