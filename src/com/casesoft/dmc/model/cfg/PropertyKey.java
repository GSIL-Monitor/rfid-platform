package com.casesoft.dmc.model.cfg;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.annotation.JSONField;

@Entity
@Table(name = "CFG_PROPERTYKEY")
public class PropertyKey implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;//id   格式 type+code
	private String type;//PropertyType id
	private String name;
	private String ownerId;// 所属方Id
	private String code;//属性编码
	private String iconCode;//图标编码
	private String isDefault;//是否默认 取值0：否，1：是
	// john code_list
	// private String cd_brand;
	// private String cd_codeType;
	// private String cd_code;
	private Integer seqNo;//no_line//序列号自增（按照type分类自增）
	// private String ds_code;
	private String relation;//
	private String ynuse;//是否使用 Y N
	private String remark;//备注
	private String registerId;//新增时会话用户id
	private Date registerDate;//
	private int locked;//是否锁定 	取值0：否，1：是
	private String unitName;
	
	@Column(nullable=false, length = 2)
	public String getYnuse() {
		return ynuse;
	}
	public void setYnuse(String ynuse) {
		this.ynuse = ynuse;
	}
	@Column(nullable=true)
	public Integer getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}
	@Column(nullable=true)
	public String getRelation() {
		return relation;
	}
	
	public void setRelation(String relation) {
		this.relation = relation;
	}

	@Column(nullable=true)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column(nullable=false, length = 20)
	public String getRegisterId() {
		return registerId;
	}
	public void setRegisterId(String registerId) {
		this.registerId = registerId;
	}

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@Column(nullable=true)
	public Date getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
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

	@Column(nullable = false, length = 10)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(nullable = false, length = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(length = 50)
	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	@Transient
	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	@Column(nullable=false)
	public int getLocked() {
		return locked;
	}
	public void setLocked(int locked) {
		this.locked = locked;
	}
	@Column(nullable=false,length=20)
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	@Column(length = 5)
	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	@Column(length = 20)
	public String getIconCode() {
		return iconCode;
	}

	public void setIconCode(String iconCode) {
		this.iconCode = iconCode;
	}
}
