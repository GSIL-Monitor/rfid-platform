package com.casesoft.dmc.model.cfg;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
@Entity
@Table(name="CFG_DEVICELOG")
public class DeviceLog implements Serializable{

	private static final long serialVersionUID = 7526627484263542980L;
	private String id;
	private String deviceId;
	private String title;
	private String content;
	private String ownerId;
	private String storageId;
	private Integer type;
	private Date logDate;
    @Id
    @Column(nullable = false, length = 50)
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
	@Column()
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    @Column(nullable = false, length = 50)
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	@Column(nullable = false, length = 100)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Column(nullable = false, length = 200)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Column(nullable = false, length = 50)
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	@Column(nullable = false, length = 50)
	public String getStorageId() {
		return storageId;
	}
	public void setStorageId(String storageId) {
		this.storageId = storageId;
	}
	
}
