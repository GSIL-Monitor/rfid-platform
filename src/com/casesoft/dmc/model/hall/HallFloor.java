package com.casesoft.dmc.model.hall;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2017/3/9 0009.
 */

@Entity
@Table(name="Hall_Floor")
public class HallFloor implements java.io.Serializable {

	@Id
	@Column(nullable = false,length = 20)
	private String code;//名称 库位编号 所属展厅 状态

	@Column(unique = true,nullable = false,length = 50)
	private String name;

	@Column(nullable = false,length = 50)
	private String ownerId;//样衣展厅编号/分区

	@Column()
	private Integer status;

	@Column(length = 2,nullable = false)
	private String areaId;//分区 为* A为分区 E为库位

	@Column
	private Integer catacity;//容量

	@Column
	private Date createTime;

	@Column
	private Integer asDefault;

	@Column
	private String creator;

	@Column
	private Date updateTime;

	@Column
	private String updater;

	@Column(length = 200)
	private String remark;

	@Transient
	private String unitName;

	@Transient
	private String creatorName;

	@Transient
	private String updaterName;


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public Integer getCatacity() {
		return catacity;
	}

	public void setCatacity(Integer catacity) {
		this.catacity = catacity;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@Column(length = 19)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getAsDefault() {
		return asDefault;
	}

	public void setAsDefault(Integer asDefault) {
		this.asDefault = asDefault;
	}

	@Column(length = 50)
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@Column(length = 19)
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Column(length = 50)
	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}


	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}


	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getUpdaterName() {
		return updaterName;
	}

	public void setUpdaterName(String updaterName) {
		this.updaterName = updaterName;
	}

}
