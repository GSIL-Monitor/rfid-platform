package com.casesoft.dmc.model.factory;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="Factory_Bill")
public class FactoryBill implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="bill_No",length=30)
	private String billNo;

	@Column(name="bill_Date",length=20)
	private String billDate;

	@Column(name="upload_No",length=50)
	private String uploadNo;
	@Column(name="type",length=255)
	private String type;

	@Column(name="end_Date",length=20)
	private String endDate;
	@Column(name="owner_Id",length=20)
	private String ownerId;
	@Column(name="status")
	private String status;
	@Column(name="wash_Type",length=100)
	private String washType;
	@Column(name="shirt_Type",length=50)
	private String shirtType;
	@Column(name="sex",length=50)
	private String sex;
	@Column(name="customer_Id",length=80)
	private String customerId;
	@Column(name="group_Id",length=255)
	private String groupId;
	@Column(name="tot_qty")
	private long totQty;
	@Column(name="tot_sku")
	private long totSku;
	@Column(name="operator",length=50)
	private String operator;
	@Column(name="remark",length=500)
	private String remark;
	
	@Column(name="isSchedule",length=1)
	private String isSchedule;

    @Column(name="progress",length=20)
    private String progress;


    @Column(name="print_Date",length=20)
    private String printDate;

    public FactoryBill(){}
	
	public FactoryBill(String billNo, String code) {
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
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}	
	public String getUploadNo() {
		return uploadNo;
	}
	public void setUploadNo(String uploadNo) {
		this.uploadNo = uploadNo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public String getWashType() {
		return washType;
	}
	public void setWashType(String washType) {
		this.washType = washType;
	}
	public String getShirtType() {
		return shirtType;
	}
	public void setShirtType(String shirtType) {
		this.shirtType = shirtType;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public long getTotQty() {
		return totQty;
	}
	public void setTotQty(long totQty) {
		this.totQty = totQty;
	}
	public long getTotSku() {
		return totSku;
	}
	public void setTotSku(long totSku) {
		this.totSku = totSku;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }


    @Transient
	private List<FactoryBillDtl> dtlList;

	
	public List<FactoryBillDtl> getDtlList() {
	    return dtlList;
	}

	public void setDtlList(List<FactoryBillDtl> dtlList) {
	    this.dtlList = dtlList;
	}
	
	@Transient
	private List<FactoryBriefBill> recordList;

	public List<FactoryBriefBill> getRecordList() {
		return recordList;
	}
	public void setRecordList(List<FactoryBriefBill> recordList) {
		this.recordList = recordList;
	}
	
	@Transient
	private String code;

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	@Column(name="season",length=200)
	private String season;
	
	@Column(name="category",length=40)
	private String category;
	
	@Column(name="factory",length=40)
	private String factory;

	
	@Column(name="imgUrl",length=200)
	private String imgUrl;




	public String getSeason() {
		return season;
	}


	public void setSeason(String season) {
		this.season = season;
	}


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public String getFactory() {
		return factory;
	}


	public void setFactory(String factory) {
		this.factory = factory;
	}

	public String getImgUrl() {
		return imgUrl;
	}


	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	@Column(name="out_Date",length=20)
	private String outDate;
	public String getOutDate() {
		return outDate;
	}

	public void setOutDate(String outDate) {
		this.outDate = outDate;
	}

	public String getIsSchedule() {
		return isSchedule;
	}

	public void setIsSchedule(String isSchedule) {
		this.isSchedule = isSchedule;
	}

    public String getPrintDate() {
        return printDate;
    }

    public void setPrintDate(String printDate) {
        this.printDate = printDate;
    }
}
