package com.casesoft.dmc.controller.task;

import java.util.List;

public class TaskData {

	private String id;
	
	private int token;
	
	private String uploadStatus;
	private String status;
	private String deviceId;
	private String beginTime;
	private String endTime;
	
	private int totEpc;
	private int totCarton;
	private int totSku;	
	private int totStyle;
	private int epcCartonQty;
	
	private List<String> epcList;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getToken() {
		return token;
	}

	public void setToken(int token) {
		this.token = token;
	}

	public String getUploadStatus() {
		return uploadStatus;
	}

	public void setUploadStatus(String uploadStatus) {
		this.uploadStatus = uploadStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getTotEpc() {
		return totEpc;
	}

	public void setTotEpc(int totEpc) {
		this.totEpc = totEpc;
	}

	public int getTotCarton() {
		return totCarton;
	}

	public void setTotCarton(int totCarton) {
		this.totCarton = totCarton;
	}

	public int getTotSku() {
		return totSku;
	}

	public void setTotSku(int totSku) {
		this.totSku = totSku;
	}

	public int getTotStyle() {
		return totStyle;
	}

	public void setTotStyle(int totStyle) {
		this.totStyle = totStyle;
	}

	public int getEpcCartonQty() {
		return epcCartonQty;
	}

	public void setEpcCartonQty(int epcCartonQty) {
		this.epcCartonQty = epcCartonQty;
	}

	public List<String> getEpcList() {
		return epcList;
	}

	public void setEpcList(List<String> epcList) {
		this.epcList = epcList;
	}
	
	
}
