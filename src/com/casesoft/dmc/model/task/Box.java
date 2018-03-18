package com.casesoft.dmc.model.task;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

/**
 * TblTaskBusinessBox entity.
 */
@Entity
@Table(name = "TASK_BOX")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Box implements java.io.Serializable {

	private static final long serialVersionUID = -3800986314768352699L;

	private String id;
	private String cartonId;
	private String taskId;
	private Integer seqNo;
	private String ownerId;
	private Integer token;
	private String deviceId;
	private long totEpc;
	private long totSku;
	private long totStyle;
	private Date scanTime;

	private String floorId;
	private String srcTaskId;
	private List<BoxDtl> boxDtls;
	private String tokenName;
	private String unitName;

	private Integer type;// 0:出库1：入库3：盘点


	@Column(length = 50)
	public String getFloorId() {
		return floorId;
	}

	public void setFloorId(String floorId) {
		this.floorId = floorId;
	}

	/** default constructor */
	public Box() {
	}

	public Box(String cartonId, String taskId, String ownerId) {
		super();
		this.cartonId = cartonId;
		this.taskId = taskId;
		this.ownerId = ownerId;
	}

	/** full constructor */
	public Box(String cartonId, String taskId, Integer seqNo, String ownerId,
			Integer token, String deviceId, long totEpc, long totSku,
			long totStyle) {
		this.cartonId = cartonId;
		this.taskId = taskId;
		this.seqNo = seqNo;
		this.ownerId = ownerId;
		this.token = token;
		this.deviceId = deviceId;
		this.totEpc = totEpc;
		this.totSku = totSku;
		this.totStyle = totStyle;
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

	@Column(nullable = false, length = 50)
	public String getCartonId() {
		return this.cartonId;
	}

	public void setCartonId(String cartonId) {
		this.cartonId = cartonId;
	}

	@Column(nullable = false, length = 50)
	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	@Column(nullable = false)
	public Integer getSeqNo() {
		return this.seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
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

	@Column(nullable = false, length = 50)
	public String getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@Column(nullable = false)
	public long getTotEpc() {
		return this.totEpc;
	}

	public void setTotEpc(long totEpc) {
		this.totEpc = totEpc;
	}

	@Column(nullable = false)
	public long getTotSku() {
		return this.totSku;
	}

	public void setTotSku(long totSku) {
		this.totSku = totSku;
	}

	@Column(nullable = false)
	public long getTotStyle() {
		return this.totStyle;
	}

	public void setTotStyle(long totStyle) {
		this.totStyle = totStyle;
	}

	@Column(nullable = false, length = 19)
	public Date getScanTime() {
		return scanTime;
	}

	public void setScanTime(Date scanTime) {
		this.scanTime = scanTime;
	}

	@Column(length = 50)
	public String getSrcTaskId() {
		return srcTaskId;
	}

	public void setSrcTaskId(String srcTaskId) {
		this.srcTaskId = srcTaskId;
	}

	// ======>
	

	@Transient
	public List<BoxDtl> getBoxDtls() {
		return boxDtls;
	}

	public void setBoxDtls(List<BoxDtl> boxDtls) {
		this.boxDtls = boxDtls;
	}

	// ======>
	

	@Transient
	public String getTokenName() {
		return tokenName;
	}

	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}

	@Transient
	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}


	@Column()
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Transient
	private Long cartonTotEpc;
	@Transient
	private Long cartonTotSku;
	@Transient
	private Long cartonTotStyle;
	@Transient
	private String cartonScanTime;
	@Transient
	private String taskDeviceId;
	@Transient
	private List<BoxDtl>skuItemList;
	@Transient
	public List<BoxDtl> getSkuItemList() {
		return skuItemList;
	}

	public void setSkuItemList(List<BoxDtl> skuItemList) {
		this.skuItemList = skuItemList;
	}

	@Transient
	public String getTaskDeviceId() {
		return taskDeviceId;
	}

	public void setTaskDeviceId(String taskDeviceId) {
		this.taskDeviceId = taskDeviceId;
	}

	@Transient
	public Long getCartonTotEpc() {
		return cartonTotEpc;
	}

	public void setCartonTotEpc(Long cartonTotEpc) {
		this.cartonTotEpc = cartonTotEpc;
	}

	@Transient
	public Long getCartonTotSku() {
		return cartonTotSku;
	}

	public void setCartonTotSku(Long cartonTotSku) {
		this.cartonTotSku = cartonTotSku;
	}

	@Transient
	public Long getCartonTotStyle() {
		return cartonTotStyle;
	}

	public void setCartonTotStyle(Long cartonTotStyle) {
		this.cartonTotStyle = cartonTotStyle;
	}

	@Transient
	public String getCartonScanTime() {
		return cartonScanTime;
	}

	public void setCartonScanTime(String cartonScanTime) {
		this.cartonScanTime = cartonScanTime;
	}

}