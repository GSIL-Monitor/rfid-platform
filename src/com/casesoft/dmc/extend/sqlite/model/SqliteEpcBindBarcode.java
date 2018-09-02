package com.casesoft.dmc.extend.sqlite.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.persistence.*;
import java.util.Date;

@DatabaseTable(tableName = "TAG_EPCBINDBARCODE")
public class SqliteEpcBindBarcode implements java.io.Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	@DatabaseField(id=true,columnName="epc",index = true )
	private String epc;
	@DatabaseField(columnName="code",index = true )
	private String code;

	@DatabaseField(columnName="tid" )
	private String tid;
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	@DatabaseField(columnName="updateTime" )
	private Date updateTime;
	@DatabaseField(columnName="updaterId" )
	private String updaterId;
	@DatabaseField(columnName="version" )
    private Long version=0l;
	@DatabaseField(columnName="disabled" )
	private Boolean disabled=false;//是否解绑：false 可用，true:不可用

	public String getEpc() {
		return epc;
	}

	public void setEpc(String epc) {
		this.epc = epc;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdaterId() {
		return updaterId;
	}

	public void setUpdaterId(String updaterId) {
		this.updaterId = updaterId;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}
}
