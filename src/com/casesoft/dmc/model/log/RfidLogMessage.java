package com.casesoft.dmc.model.log;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by pc on 2016/3/23.
 */
@Entity
@Table(name = "LOG_RFIDLOGMESSAGE")
public class RfidLogMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6554424315906742787L;
	/**
	 * 
	 */
	
	/*@Id
	@SequenceGenerator(name = "sequenceGenerator", sequenceName = "RfidLogMessage_SEQ")
	@GeneratedValue(generator = "sequenceGenerator", strategy = GenerationType.SEQUENCE)
	@Column(name = "ID", unique = true, nullable = false)*/
	@SequenceGenerator(name = "sequenceGenerator", sequenceName = "RfidLogMessage_SEQ")
	@GeneratedValue(generator = "sequenceGenerator", strategy = GenerationType.AUTO)
	@Id
	@Column(nullable = false)
	private long id;
	@Column(nullable = false, length = 200)
	private String method;
	@Column(nullable = false)
	private String description;
	@Column(length = 10)
	private String type;
	@Column()
	private Date logDate;
	@Column(length = 30)
	private String usercode;
	@Column(length = 45)
	private String deviceId;
	@Column(length = 200)
	private String ip;
	@Column(length=500)
	private String cause;

	@Column(length = 200)
	private String serverPoint;
	
	public String getServerPoint() {
		return serverPoint;
	}

	public void setServerPoint(String serverPoint) {
		this.serverPoint = serverPoint;
	}

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }




	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}


	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}
}
