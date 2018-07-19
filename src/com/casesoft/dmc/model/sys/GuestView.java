package com.casesoft.dmc.model.sys;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Session on 2017-06-20.
 */
@Entity
@Table(name = "SYS_GUEST_VIEW")
public class GuestView {
	@Id
	@Column()
	private String id;				//编号
	@Column()
	private String name;			//姓名
	@Column()
	private String unitType;
	@Column()
	private Integer sex;				//1男 0女
	@Column()
	private Integer status;			//启用状态		1启用 0未启用
	@Column()
	private String	fax;			//传真
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@Column()
	private Date storeDate;			//储值日期
	@Column()
	private Double	storedValue;	//储值金额

	@Column
	private String linkman;			//联系人

	@Column()
	private String tel;				//电话
	@Column()
	private String	email;			//邮箱
	@Column()
	private String ownerId;			//所属方
	@Column()
	private	String	bankAccount;	//银行账户
	@JSONField(format="yyyy-MM-dd")
	@Column()
	private Date	birth;			//生日
	@Column()
	private	String	phone;			//手机

	@Column()
	private	Integer	discount;		//折扣
	@Column()
	private String address;			//地址
	@Column()
	private String province;		//省份
	@Column()
	private String city;			//城市
	@Column()
	private String areaId;			//区县

    @Column()
	private String defaultWarehId;

	@Column()
	private String	depositBank;	//开户行
	@Column()
    private Double owingValue;
	@Column()
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date  createTime;		//建立时间

	@Column()
	private String remark;

	@Column()
	private String updaterId;

<<<<<<< HEAD
=======
	@Column
	private String idCard;

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

>>>>>>> 0f7a54ab35803fdd90f8b02064e78a59eb8d1be9
	private String vipId;
	@Column()
	private String unionid;
	@Column()
	private String sendsucess;

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getSendsucess() {
		return sendsucess;
	}

	public void setSendsucess(String sendsucess) {
		this.sendsucess = sendsucess;
	}

	@Column( length = 50)
	private String areasId;//区域ID
	@Column( length = 50)
	private String ownerids;//加盟商或供应商id

	public String getAreasId() {
		return areasId;
	}

	public void setAreasId(String areasId) {
		this.areasId = areasId;
	}

	public String getOwnerids() {
		return ownerids;
	}

	public void setOwnerids(String ownerids) {
		this.ownerids = ownerids;
	}

	@Column( length = 50)
	public String getVipId() {
		return vipId;
	}

	public void setVipId(String vipId) {
		this.vipId = vipId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnitType() {
		return unitType;
	}

	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public Date getStoreDate() {
		return storeDate;
	}

	public void setStoreDate(Date storeDate) {
		this.storeDate = storeDate;
	}

	public Double getStoredValue() {
		return storedValue;
	}

	public void setStoredValue(Double storedValue) {
		this.storedValue = storedValue;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getDiscount() {
		return discount;
	}

	public void setDiscount(Integer discount) {
		this.discount = discount;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getDefaultWarehId() {
		return defaultWarehId;
	}

	public void setDefaultWarehId(String defaultWarehId) {
		this.defaultWarehId = defaultWarehId;
	}

	public String getDepositBank() {
		return depositBank;
	}

	public void setDepositBank(String depositBank) {
		this.depositBank = depositBank;
	}

	public Double getOwingValue() {
		return owingValue;
	}

	public void setOwingValue(Double owingValue) {
		this.owingValue = owingValue;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUpdaterId() {
		return updaterId;
	}

	public void setUpdaterId(String updaterId) {
		this.updaterId = updaterId;
	}

	@Transient
	private String updaterName;

	@Transient
	private String unitName;

	@Transient
	private String unitTypeName;

	@Transient
	private String defaultWarehouseName;
	private Double vippoints;
	@Column()
	public Double getVippoints() {
		return vippoints;
	}

	public void setVippoints(Double vippoints) {
		this.vippoints = vippoints;
	}

	public String getUpdaterName() {
		return updaterName;
	}

	public void setUpdaterName(String updaterName) {
		this.updaterName = updaterName;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getUnitTypeName() {
		return unitTypeName;
	}

	public void setUnitTypeName(String unitTypeName) {
		this.unitTypeName = unitTypeName;
	}

	public String getDefaultWarehouseName() {
		return defaultWarehouseName;
	}

	public void setDefaultWarehouseName(String defaultWarehouseName) {
		this.defaultWarehouseName = defaultWarehouseName;
	}
}
