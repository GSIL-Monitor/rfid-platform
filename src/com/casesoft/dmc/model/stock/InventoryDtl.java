package com.casesoft.dmc.model.stock;

import java.util.Date;

public class InventoryDtl implements java.io.Serializable {

	private static final long serialVersionUID = 8234851678784463571L;
	
	private String storageId;
	private String iEpc;
	private Date iScanTime;
	
	private String oEpc;
	private Date oScanTime;
	public String getStorageId() {
		return storageId;
	}
	public void setStorageId(String storageId) {
		this.storageId = storageId;
	}
	public String getIEpc() {
		return iEpc;
	}
	public void setIEpc(String epc) {
		this.iEpc = epc;
	}
	
	public String getOEpc() {
		return oEpc;
	}
	public void setOEpc(String oEpc) {
		this.oEpc = oEpc;
	}
	public Date getIScanTime() {
		return iScanTime;
	}
	public void setIScanTime(Date iScanTime) {
		this.iScanTime = iScanTime;
	}
	public Date getOScanTime() {
		return oScanTime;
	}
	public void setOScanTime(Date oScanTime) {
		this.oScanTime = oScanTime;
	}

	
}
