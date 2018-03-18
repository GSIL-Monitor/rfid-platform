package com.casesoft.dmc.model.factory;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "factory_init")
public class FactoryInit implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="billNo",length=50)
	private String billNo;
	@Column(name="bill_Date",length=30)
	private String billDate;
	@Column(name="file_Name",length=200)
	private String fileName;
	@Column(name="tot_Qty")
	private long totQty;
	@Column(name="tot_Sku")
	private long totSku;
	@Column(name="owner_Id",length=20)
	private String ownerId;
	@Column(name="status")
	private Integer status;
	@Id
	@Column(name="factoryBillNo")
	private String factoryBillNo;
	public FactoryInit(){}

    public FactoryInit(String billNo, String factoryBillNo, String isSchedule, Integer status, String ownerId) {
        this.billNo = billNo;
        this.factoryBillNo = factoryBillNo;
        this.isSchedule = isSchedule;
        this.status = status;
        this.ownerId = ownerId;
    }

    public FactoryInit(String billNo, String factoryBillNo, String billDate,
			String fileName, long totQty, long totSku, String ownerId,Integer status) {
		this.billNo = billNo;
		this.factoryBillNo = factoryBillNo;
		this.billDate =billDate;
		this.fileName = fileName;
		this.totQty = totQty;
		this.totSku = totSku;
		this.ownerId = ownerId;
		this.status=status;
		
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
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
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
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}	
	
	public String getFactoryBillNo() {
		return factoryBillNo;
	}
	public void setFactoryBillNo(String factoryBillNo) {
		this.factoryBillNo = factoryBillNo;
	}

	@Transient
	private List<FactoryBill> billList;	
	
	public List<FactoryBill> getBillList() {
		return billList;
	}
	public void setBillList(List<FactoryBill> billList) {
		this.billList = billList;
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
    private String isSchedule;


    public String getIsSchedule() {
        return isSchedule;
    }

    public void setIsSchedule(String isSchedule) {
        this.isSchedule = isSchedule;
    }
}
