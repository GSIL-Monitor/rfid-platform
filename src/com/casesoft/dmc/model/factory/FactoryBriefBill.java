package com.casesoft.dmc.model.factory;

import java.util.List;

public class FactoryBriefBill {
    
	private String billNo;
	private String code;
	private List<FactoryRecord> recordList;
	
	public FactoryBriefBill(){}
	
	public FactoryBriefBill(String code) {
		super();
		this.code = code;
	}

	public FactoryBriefBill(String billNo, String code) {
		super();
		this.billNo = billNo;
		this.code = code;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<FactoryRecord> getRecordList() {
		return recordList;
	}
	public void setRecordList(List<FactoryRecord> recordList) {
		this.recordList = recordList;
	}
}
