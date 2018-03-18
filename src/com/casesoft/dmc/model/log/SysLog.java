package com.casesoft.dmc.model.log;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;

@Entity
@Table(name="LOG_SYSLOG")
public class SysLog {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "S_CrudLog")
	@SequenceGenerator(name = "S_CrudLog", allocationSize = 1, initialValue = 1, sequenceName = "S_CrudLog")
	private Long id;
	@Column(nullable = false, length = 50)
	private String tableName;
	@Column(length = 20)
	private String cmd;
	@Column(length = 20)
	private String operator;
	@Column(length = 45)
	private String deviceId;
	@Column(nullable = false)
	private Date logDate;
    @Column(length = 20)
    private String creatorId;
	@Column()
	private Long version;
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    @Column()
    private Long consumeTime;

    public Long getConsumeTime() {
        return consumeTime;
    }

    public void setConsumeTime(Long consumeTime) {
        this.consumeTime = consumeTime;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }
}
