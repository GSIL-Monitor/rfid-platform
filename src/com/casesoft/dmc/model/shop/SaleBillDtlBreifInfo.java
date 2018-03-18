package com.casesoft.dmc.model.shop;

import java.io.Serializable;
import java.util.List;

public class SaleBillDtlBreifInfo implements Serializable{
     private String billNo;
	 private String code;
     private double percent;// 打折率

 	 private double actValue;// 实际销售额
 	 
 	 private long qty;
 	 private List<String> images;
 	 
 	 public SaleBillDtlBreifInfo() {}
 	 public SaleBillDtlBreifInfo(String billNo, String code, double percent,
 			double actValue, long qty, List<String> images) {
 		
 		this.billNo = billNo;
 		this.code = code;
 		this.percent = percent;
 		this.actValue = actValue;
 		this.qty = qty;
 		this.images = images;
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

	public double getPercent() {
		return percent;
	}

	public void setPercent(double percent) {
		this.percent = percent;
	}

	public double getActValue() {
		return actValue;
	}

	public void setActValue(double actValue) {
		this.actValue = actValue;
	}

	public long getQty() {
		return qty;
	}

	public void setQty(long qty) {
		this.qty = qty;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}
 	 
 	

}
