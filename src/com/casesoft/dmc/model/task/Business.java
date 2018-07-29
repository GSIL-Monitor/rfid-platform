package com.casesoft.dmc.model.task;

import com.alibaba.fastjson.annotation.JSONField;
import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.stock.InventoryRecord;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

/**
 * TblTaskBusiness entity. @author
 */
@Entity
@Table(name = "TASK_BUSINESS")
public class Business implements java.io.Serializable {

	private static final Long serialVersionUID = 6072395948739316396L;
	private String id;//出库任务编号
	private Integer status;//对接状态
	private String deviceId;//设备号
	private String ownerId;
	private Integer token;//出库方式
	private Date beginTime;//开始时间
	private Date endTime;//结束时间
	private Long totEpc;//总数量
	private Long totStyle;//款数
	private Long totSku;//SKU数
	private Long totCarton;//箱数


	private String billId;

	private String billNo;//ERP单号
	private String srcTaskId;

	private String srcBillNo;
	private String operator;
	private Integer locked;

	private List<BusinessDtl> dtlList;//

	private List<Box> boxList;

  	private List<Record> recordList;//

  	private List<InventoryRecord> inventoryRecordList;
	private Long totTime;//扫描时长
	private String origId;//出库仓库
	private String origName;
	private String origUnitId;//出库组织
	private String deviceName;//设备名
	private String origUnitName;

 	private double totPreCase;
 	private Bill bill;

	private String destId;//接收仓库
	private String destUnitId; //接收方组织
	private Integer type;// 0:出库1：入库3：盘点
	private String rmId;//库位id
	private String userId;//库位调整操作人

	private String destName;
	private String destUnitName;
	@Column(length = 55,name="origUnitId")
	public String getOrigUnitId() {
		return origUnitId;
	}

	public void setOrigUnitId(String origUnitId) {
		this.origUnitId = origUnitId;
	}

	@Column(length = 50)
	public String getSrcBillNo() {
		return srcBillNo;
	}

	public void setSrcBillNo(String srcBillNo) {
		this.srcBillNo = srcBillNo;
	}

	@Column(length = 50)
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Column(length = 55)
	public String getSrcTaskId() {
		return srcTaskId;
	}

	public void setSrcTaskId(String srcTaskId) {
		this.srcTaskId = srcTaskId;
	}



	@Column()
	public Integer getLocked() {
		return locked;
	}

	public void setLocked(Integer locked) {
		this.locked = locked;
	}

	/** default constructor */
	public Business() {
	}

	/** full constructor */
	public Business(Integer status, String deviceId, String ownerId,
			Integer token, Date beginTime, Date endTime, Long totEpc,
			Long totStyle, Long totSku, Long totCarton) {
		this.status = status;
		this.deviceId = deviceId;
		this.ownerId = ownerId;
		this.token = token;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.totEpc = totEpc;
		this.totStyle = totStyle;
		this.totSku = totSku;
		this.totCarton = totCarton;
	}

	// Property accessors

	@Id
	@Column(nullable = false, length = 50)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(nullable = false)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(nullable = false, length = 50)
	public String getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@Column(nullable = false, length = 50)
	public String getOwnerId() {
		return this.ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	@Column(nullable = false)
	public Integer getToken() {
		return this.token;
	}

	public void setToken(Integer token) {
		this.token = token;
	}
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@Column(nullable = false, length = 19)
	public Date getBeginTime() {
		return this.beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@Column(nullable = false, length = 19)
	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Column(nullable = false)
	public Long getTotEpc() {
		return this.totEpc;
	}

	public void setTotEpc(Long totEpc) {
		this.totEpc = totEpc;
	}

	@Column(nullable = false)
	public Long getTotStyle() {
		return this.totStyle;
	}

	public void setTotStyle(Long totStyle) {
		this.totStyle = totStyle;
	}

	@Column(nullable = false)
	public Long getTotSku() {
		return this.totSku;
	}

	public void setTotSku(Long totSku) {
		this.totSku = totSku;
	}

	@Column(nullable = false)
	public Long getTotCarton() {
		return this.totCarton;
	}

	public void setTotCarton(Long totCarton) {
		this.totCarton = totCarton;
	}

	@Column(length = 50)
	public String getOrigId() {
		return origId;
	}

	public void setOrigId(String origId) {
		this.origId = origId;
	}

	@Column(length = 50)
	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	@Column(length = 50)
	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	// =======>

	private Double totPreVal;//出库总成本

	public Double getTotPreVal() {
		return totPreVal;
	}

	public void setTotPreVal(Double totPreVal) {
		this.totPreVal = totPreVal;
	}

	@Column()


	@Transient
	public List<BusinessDtl> getDtlList() {
		return dtlList;
	}

	public void setDtlList(List<BusinessDtl> dtlList) {
		this.dtlList = dtlList;
	}

	@Transient
	public List<Box> getBoxList() {
		return boxList;
	}

	public void setBoxList(List<Box> boxList) {
		this.boxList = boxList;
	}

	// @Transient
	// public List<BoxDtl> getBoxDtlList() {
	// return boxDtlList;
	// }
	//
	// public void setBoxDtlList(List<BoxDtl> boxDtlList) {
	// this.boxDtlList = boxDtlList;
	// }
	@Transient
	public List<Record> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<Record> recordList) {
		this.recordList = recordList;
	}

	@Transient
	public List<InventoryRecord> getInventoryRecordList() {
		return inventoryRecordList;
	}

	public void setInventoryRecordList(List<InventoryRecord> inventoryRecordList) {
		this.inventoryRecordList = inventoryRecordList;
	}

	@Transient
	public Long getTotTime() {
		return totTime;
	}

	public void setTotTime(Long totTime) {
		this.totTime = totTime;
	}


 	@Transient
	public double getTotPreCase() {
		return totPreCase;
	}

	public void setTotPreCase(double totPreCase) {
		this.totPreCase = totPreCase;
	}

	@Transient
	public String getOrigName() {
		return origName;
	}

	public void setOrigName(String origName) {
		this.origName = origName;
	}

	@Transient
	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	@Transient
	public String getOrigUnitName() {
		return origUnitName;
	}

	public void setOrigUnitName(String origUnitName) {
		this.origUnitName = origUnitName;
	}



	@Transient
	public Bill getBill() {
		return bill;
	}

	public void setBill(Bill bill) {
		this.bill = bill;
	}



	public String remark;
	@Column(length = 500)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	@Transient
	public String getDestUnitName() {
		return destUnitName;
	}

	public void setDestUnitName(String destUnitName) {
		this.destUnitName = destUnitName;
	}

	@Transient
	public String getDestName() {
		return destName;
	}

	public void setDestName(String destName) {
		this.destName = destName;
	}

	@Column(length = 50)
	public String getDestId() {
		return destId;
	}

	public void setDestId(String destId) {
		this.destId = destId;
	}

	@Column(length = 50)
	public String getDestUnitId() {
		return destUnitId;
	}

	public void setDestUnitId(String destUnitId) {
		this.destUnitId = destUnitId;
	}

	@Column()
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	@Transient
	private Bill srcBill;
	@Transient
	public Bill getSrcBill() {
		return srcBill;
	}

	public void setSrcBill(Bill srcBill) {
		this.srcBill = srcBill;
	}

	@Transient
	private double totPrice;
	@Transient
	private double totPuPrice;

	@Transient
	private String taskType;
	@Transient
	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	@Transient
	public double getTotPuPrice() {
		return totPuPrice;
	}

	public void setTotPuPrice(double totPuPrice) {
		this.totPuPrice = totPuPrice;
	}

	@Transient
	public double getTotPrice() {
		return totPrice;
	}

	public void setTotPrice(double totPrice) {
		this.totPrice = totPrice;
	}

	public String getRmId() {
		return rmId;
	}

	public void setRmId(String rmId) {
		this.rmId = rmId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}