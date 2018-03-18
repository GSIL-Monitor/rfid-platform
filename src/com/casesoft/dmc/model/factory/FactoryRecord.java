package com.casesoft.dmc.model.factory;

import com.casesoft.dmc.core.util.CommonUtil;

import javax.persistence.*;

@Entity
@Table(name = "Factory_Record")
public class FactoryRecord implements java.io.Serializable {	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="id", nullable = false, length = 50)
	private String id;
	@Column(name="code", nullable = false, length = 50)
	private String code;
	@Column(name="task_Id", nullable = false, length = 50)
	private String taskId;
	@Column(name="token", nullable = false)
	private Integer token;
	@Column(name="style_Id")
	private String styleId;
	@Column(name="color_Id")
	private String colorId;
	@Column(name="size_Id")
	private String sizeId;
	@Column(name="sku",length=50)
	private String sku;
	@Column(name="scan_Time", length = 20)
	private String scanTime;
	@Column(name="owner_Id", length = 20)
	private String ownerId;
	@Column(name="device_Id", length = 20)
	private String deviceId;
	@Column(name="type",length=1,nullable = false)
	private String type;
	
	@Column(name="is_OutSource",length=1)
	private String isOutSource;
	
	@Column(name="total_Time")
	private double totalTime;

	@Column(name="startTaskId")
	private String startTaskId;
    @Column(name="sign")
    private String sign;
	public FactoryRecord(){}

    public FactoryRecord(String code) {
		
		this.code = code;
	}

    public FactoryRecord(String code, String billNo) {
        this.code = code;
        this.billNo = billNo;
    }

    public FactoryRecord(Integer token, String type, String scanTime) {
		
		this.token = token;
		this.type = type;
		this.scanTime = scanTime;
	}
    

	public FactoryRecord(String code, String token, String scanTime,
                         String type, String isOutSource) {
	
		this.code = code;
		if(CommonUtil.isNotBlank(token)){
			this.token = Integer.parseInt(token);
		}
		this.scanTime = scanTime;
		if(CommonUtil.isNotBlank(type)){
			this.type=type;
		}
		this.isOutSource = isOutSource;
	}
	public FactoryRecord(Integer token, String scanTime, String type,
                         String operator, String operatorName, double totalTime, String isOutSource) {
		
		this.token = token;
		this.scanTime = scanTime;
		this.type = type;
		this.operator = operator;
		this.operatorName = operatorName;
		this.totalTime = totalTime;
		this.isOutSource = isOutSource;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public Integer getToken() {
		return token;
	}
	public void setToken(Integer token) {
		this.token = token;
	}
	public String getStyleId() {
		return styleId;
	}
	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}
	public String getColorId() {
		return colorId;
	}
	public void setColorId(String colorId) {
		this.colorId = colorId;
	}
	public String getSizeId() {
		return sizeId;
	}
	public void setSizeId(String sizeId) {
		this.sizeId = sizeId;
	}
	public String getScanTime() {
		return scanTime;
	}
	public void setScanTime(String scanTime) {
		this.scanTime = scanTime;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getType() {
		return type;
	}
	public String getIsOutSource() {
		return isOutSource;
	}

	public void setIsOutSource(String isOutSource) {
		this.isOutSource = isOutSource;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Transient
	private String tokenName;

	public String getStartTaskId() {
		return startTaskId;
	}

	public void setStartTaskId(String startTaskId) {
		this.startTaskId = startTaskId;
	}

	public String getTokenName() {
		return tokenName;
	}


	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}
	@Transient
	private String operator;
	@Transient
	private String operatorName;

    @Transient
    private String billNo;

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOperatorName() {
		return operatorName;
	}

	

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	
	public double getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(double totalTime) {
		this.totalTime = totalTime;
	}

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
}
