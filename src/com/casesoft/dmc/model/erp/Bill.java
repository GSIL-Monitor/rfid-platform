package com.casesoft.dmc.model.erp;

import com.alibaba.fastjson.annotation.JSONField;
import com.casesoft.dmc.model.task.Record;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ERP_BILL")
public class Bill implements java.io.Serializable {

	private static final long serialVersionUID = -5758318718336546699L;

	private String id;
	private String ownerId;
	private String billNo;
	private Integer type;// token值
	private String billType;// 手动创建，配货单、订货单等
	private Date billDate;

	private String destUnitId;// 收货方组织ID
	private String destUnitName;//
	private String destId;
	private String destName;// 收货仓库
	private String address2;// 收货地址

	private String origId;//发货仓库
	private String origName;// 发货仓库
	private String origUnitId;// 发货方组织ID
	private String origUnitName;// 发货方
	private String address;// 发货地址

	private String deliverNo;
	private String deliverType;

	private Integer isOrderBox = 0;
	private Long totQty;
	private Long skuQty;
	private Long boxQty;

	private Integer status = 0;// 对接状态0:未对接，1：验收中，2：已中止
	private String oprId;// 操作者Id（owner组织下的操作者）
	private Long actQty;
	private Long actSkuQty;
	private Long actBoxQty;

	private String taskId;
	private String srcBillNo;

	private String remark;

	private String rmRecord;//盘点库位
	// 电商单据增加字段
	private String client;// 客户
	private String phone1;
	private String phone2;
	private String mobile1;
	private String mobile2;
	private String payInfo;
	private String payTime;
	private Double totPrice;
	private double totPrePrice;
	private double totPuPrice;
	private Integer payState;// 0:未确认1：已确认(未支付)2.已支付
	private Long initQty=0l;
	private Long scanQty=0l;
	private Long manualQty=0l;

	private Long preManualQty=0l;
	@Column(name = "preManualQty")
	public Long getPreManualQty() {
		return preManualQty;
	}

	public void setPreManualQty(Long preManualQty) {
		this.preManualQty = preManualQty;
	}

	@Column(name = "manualQty")
	public Long getManualQty() {
		return manualQty;
	}

	public void setManualQty(Long manualQty) {
		this.manualQty = manualQty;
	}

	@Column(name = "scanQty")
	public Long getScanQty() {
		return scanQty;
	}

	public void setScanQty(Long scanQty) {
		this.scanQty = scanQty;
	}

	@Column(name = "initQty")
	public Long getInitQty() {
		return initQty;
	}

	public void setInitQty(Long initQty) {
		this.initQty = initQty;
	}

	@NotNull(message = "单据明细不能为空")
	private List<BillDtl> dtlList;
	/**
	 * 物流信息
	 * */
	private String logistical;//物流名称
	private String trackNo;//物流单号
	@Column(name="logistical",length = 300)
	public String getLogistical() {
		return logistical;
	}

	public void setLogistical(String logistical) {
		this.logistical = logistical;
	}
	@Column(name="trackNo",length = 150)
	public String getTrackNo() {
		return trackNo;
	}

	public void setTrackNo(String trackNo) {
		this.trackNo = trackNo;
	}


	@Id
	@Column(nullable = false, length = 50)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(nullable = false, length = 50)
	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	@Column(nullable = false, length = 50)
	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	@Column(nullable = false)
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(length = 50)
	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	@Column(nullable = false, length = 19)
    @JSONField(format = "yyyy-MM-dd")
	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	@Column(length = 50)
	public String getOrigId() {
		return origId;
	}

	public void setOrigId(String origId) {
		this.origId = origId;
	}

	@Column(length = 200)
	public String getOrigName() {
		return origName;
	}

	public void setOrigName(String origName) {
		this.origName = origName;
	}

	@Column(length = 50)
	public String getDestUnitId() {
		return destUnitId;
	}

	public void setDestUnitId(String destUnitId) {
		this.destUnitId = destUnitId;
	}

	@Column(length = 50)
	public String getDestId() {
		return destId;
	}

	public void setDestId(String destId) {
		this.destId = destId;
	}

	@Column(length = 200)
	public String getDestName() {
		return destName;
	}

	public void setDestName(String destName) {
		this.destName = destName;
	}

	@Column()
	public Integer getIsOrderBox() {
		return isOrderBox;
	}

	public void setIsOrderBox(Integer isOrderBox) {
		this.isOrderBox = isOrderBox;
	}

	@Column(nullable = false)
	public Long getTotQty() {
		return totQty;
	}

	public void setTotQty(Long totQty) {
		this.totQty = totQty;
	}

	@Column()
	public Long getSkuQty() {
		return skuQty;
	}

	public void setSkuQty(Long skuQty) {
		this.skuQty = skuQty;
	}

	@Column()
	public Long getBoxQty() {
		return boxQty;
	}

	public void setBoxQty(Long boxQty) {
		this.boxQty = boxQty;
	}

	@Column(nullable = false)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(length = 50)
	public String getOprId() {
		return oprId;
	}

	public void setOprId(String oprId) {
		this.oprId = oprId;
	}

	@Column()
	public Long getActQty() {
		return actQty;
	}

	public void setActQty(Long actQty) {
		this.actQty = actQty;
	}

	@Column()
	public Long getActSkuQty() {
		return actSkuQty;
	}

	public void setActSkuQty(Long actSkuQty) {
		this.actSkuQty = actSkuQty;
	}

	@Column()
	public Long getActBoxQty() {
		return actBoxQty;
	}

	public void setActBoxQty(Long actBoxQty) {
		this.actBoxQty = actBoxQty;
	}

	@Column(length = 50)
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	@Column(length = 200)
	public String getRemark() {
		if (remark == null) {
			remark = "";
		}
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(length = 200)
	public String getDestUnitName() {
		return destUnitName;
	}

	public void setDestUnitName(String destUnitName) {
		this.destUnitName = destUnitName;
	}

	

	@Transient
	public List<BillDtl> getDtlList() {
		return dtlList;
	}

	public void setDtlList(List<BillDtl> dtlList) {
		this.dtlList = dtlList;
	}

	public void addDtl(BillDtl dtl) {
		if (dtlList == null)
			dtlList = new ArrayList<BillDtl>();
		dtlList.add(dtl);
	}

	@Column(length = 200)
	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	@Column(length = 50)
	public String getOrigUnitId() {
		return origUnitId;
	}

	public void setOrigUnitId(String origUnitId) {
		this.origUnitId = origUnitId;
	}

	@Column(length = 200)
	// 三个字节一个汉字
	public String getOrigUnitName() {
		return origUnitName;
	}

	public void setOrigUnitName(String origUnitName) {
		this.origUnitName = origUnitName;
	}

	@Column(length = 200)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(length = 50)
	public String getDeliverNo() {
		return deliverNo;
	}

	public void setDeliverNo(String deliverNo) {
		this.deliverNo = deliverNo;
	}

	@Column(length = 50)
	public String getSrcBillNo() {
		return srcBillNo;
	}

	public void setSrcBillNo(String srcBillNo) {
		this.srcBillNo = srcBillNo;
	}

	// 电商应用字段
	@Column(length = 100)
	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	@Column(length = 50)
	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	@Column(length = 50)
	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	@Column(length = 50)
	public String getMobile1() {
		return mobile1;
	}

	public void setMobile1(String mobile1) {
		this.mobile1 = mobile1;
	}

	@Column(length = 50)
	public String getMobile2() {
		return mobile2;
	}

	public void setMobile2(String mobile2) {
		this.mobile2 = mobile2;
	}

	@Column(length = 200)
	public String getPayInfo() {
		return payInfo;
	}

	public void setPayInfo(String payInfo) {
		this.payInfo = payInfo;
	}

	@Column(length = 50)
	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	@Column()
	public Double getTotPrice() {
		return totPrice;
	}

	public void setTotPrice(Double totPrice) {
		this.totPrice = totPrice;
	}

	@Transient
	public double getTotPrePrice() {
		return totPrePrice;
	}

	public void setTotPrePrice(double totPrePrice) {
		this.totPrePrice = totPrePrice;
	}

	@Transient
	public double getTotPuPrice() {
		return totPuPrice;
	}

	public void setTotPuPrice(double totPuPrice) {
		this.totPuPrice = totPuPrice;
	}

	@Column()
	public Integer getPayState() {
		return payState;
	}

	public void setPayState(Integer payState) {
		this.payState = payState;
	}

	@Column(length = 200)
	public String getDeliverType() {
		return deliverType;
	}

	public void setDeliverType(String deliverType) {
		this.deliverType = deliverType;
	}
	@Transient
	private List<Record> records;
	@Transient
	public List<Record> getRecords() {
		return records;
	}

	public void setRecords(List<Record> records) {
		this.records = records;
	}

	public String getRmRecord() {
		return rmRecord;
	}

	public void setRmRecord(String rmRecord) {
		this.rmRecord = rmRecord;
	}
}
