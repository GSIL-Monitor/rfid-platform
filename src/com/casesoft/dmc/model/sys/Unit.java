package com.casesoft.dmc.model.sys;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;


/**
 * Unit entity.
 */
@Entity
@Table(name = "SYS_UNIT")
public class Unit extends BaseUnit implements java.io.Serializable {

	private static final long serialVersionUID = -5273432077254692156L;

	private String id;
	private String code;
	private String name;
	private Integer type;
	private String ownerId;
	private String hierarchy;
	private Integer seqNo;
	private String postCode;
	private String address;
	private String tel;
	private String fax;
	private String email;
	private String net;
	private String linkman;
	private String remark;
	private String creatorId;
	private Date createTime;
    private String updaterId;
    private Date updateTime;

	private String provinceId;
	private String cityId;
	private String areaId;

	private String stockType;

	private Double longitude;
	private Double latitude;

	private int locked;
	
	private String _parentId;
	private String state = "closed";
	private String province;
	private String city;
	private String unitName;// wing 20140607 父组织名称
	private String groupName;
	private String deviceIds;
    private String src;//来源

	private String updaterName;
	//added for GuestView

	private Integer status =1;
	private Integer sex;//
	private String depositBank;
	private Date birth;
	private String phone;
	private Integer discount;
	private Double storedValue =0D;//储值金额
	private Double owingValue=0D;//欠款金额
	private Date storeDate;
	private String defaultWarehId;//增加默认仓库Id
	@Column( length = 50)
	private String defalutCustomerId;//默认客户Id
	private String defaultSaleStaffId;//默认销售员Id
	private String vipId;
	private String areasId;//区域ID
	@Column( length = 50)
	private String ownerids;//加盟商或供应商id
	@Column( length = 100)
	private String unionid;
	@Column( length = 50)
	private String sendSucess;

	@Transient
	private String rackId;//货架号

	@Transient
	private String levelId;//货层号

	@Transient
	private String allocationId;//货位号

	@Transient
	private String deep;//深度

	public String getRackId() {
		return rackId;
	}

	public void setRackId(String rackId) {
		this.rackId = rackId;
	}

	public String getLevelId() {
		return levelId;
	}

	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}

	public String getAllocationId() {
		return allocationId;
	}

	public void setAllocationId(String allocationId) {
		this.allocationId = allocationId;
	}

	public String getSendSucess() {
		return sendSucess;
	}

	public void setSendSucess(String sendSucess) {
		this.sendSucess = sendSucess;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getOwnerids() {
		return ownerids;
	}

	public void setOwnerids(String ownerids) {
		this.ownerids = ownerids;
	}

	public String getAreasId() {
		return areasId;
	}

	public void setAreasId(String areasId) {
		this.areasId = areasId;
	}

	@Column( length = 50)
	public String getVipId() {
		return vipId;
	}

	public void setVipId(String vipId) {
		this.vipId = vipId;
	}
	// Constructors

	/** default constructor */
	public Unit() {
	}

	/** minimal constructor */
	public Unit(String code, String name, Integer type, String ownerId,
			String creatorId, Date createTime) {
		this.code = code;
		this.name = name;
		this.type = type;
		this.ownerId = ownerId;
		this.creatorId = creatorId;
		this.createTime = createTime;
	}

	/** full constructor */
	public Unit(String code, String name, Integer type, String ownerId,
			String hierarchy, Integer seqNo, String postCode, String address,
			String tel, String fax, String email, String net, String linkman,
			String remark, String creatorId, Date createTime) {
		this.code = code;
		this.name = name;
		this.type = type;
		this.ownerId = ownerId;
		this.hierarchy = hierarchy;
		this.seqNo = seqNo;
		this.postCode = postCode;
		this.address = address;
		this.tel = tel;
		this.fax = fax;
		this.email = email;
		this.net = net;
		this.linkman = linkman;
		this.remark = remark;
		this.creatorId = creatorId;
		this.createTime = createTime;
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

	@Column(unique = true, nullable = false, length = 45)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(nullable = false, length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(nullable = false)
	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(nullable = false, length = 50)
	public String getOwnerId() {
		return this.ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	@Column(length = 50)
	public String getHierarchy() {
		return this.hierarchy;
	}

	public void setHierarchy(String hierarchy) {
		this.hierarchy = hierarchy;
	}

	@Column()
	public Integer getSeqNo() {
		return this.seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	@Column(length = 6)
	public String getPostCode() {
		return this.postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	@Column(length = 100)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(length = 25)
	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@Column(length = 25)
	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Column(length = 30)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(length = 50)
	public String getNet() {
		return this.net;
	}

	public void setNet(String net) {
		this.net = net;
	}

	@Column(length = 50)
	public String getLinkman() {
		return this.linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	@Column(length = 50)
	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	@Column(length = 50)
	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	@Column(length = 50)
	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	@Column(length = 300)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(nullable = false, length = 50)
	public String getCreatorId() {
		return this.creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@Column(nullable = false, length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(length = 10)
	public String getStockType() {
		return stockType;
	}

	public void setStockType(String stockType) {
		this.stockType = stockType;
	}

	// //=================>

	@Column(length = 20)
	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	@Column(length = 20)
	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	@Column(length = 1)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(length = 1)
	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	@Column(length = 50)
	public String getDepositBank() {
		return depositBank;
	}

	public void setDepositBank(String depositBank) {
		this.depositBank = depositBank;
	}

	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	@Column(length = 19)
	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	@Column(length = 13)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	@Column()
	public Double getStoredValue() {
		return storedValue;
	}

	public void setStoredValue(Double storedValue) {
		this.storedValue = storedValue;
	}
	@Column()
	public Double getOwingValue() {
		return owingValue;
	}

	public void setOwingValue(Double owingValue) {
		this.owingValue = owingValue;
	}

	@Column()
	public Integer getDiscount() {
		return discount;
	}

	public void setDiscount(Integer discount) {
		this.discount = discount;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	@Column(length = 19)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getStoreDate() {
		return storeDate;
	}

	public void setStoreDate(Date storeDate) {
		this.storeDate = storeDate;
	}

	@Transient
	public String get_parentId() {
		return _parentId;
	}

	public void set_parentId(String _parentId) {
		this._parentId = _parentId;
	}

	@Transient
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	
	@Column(length = 30)
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Column(length = 30)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}


	@Transient
	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	
	private String defaultWarehouseName;

	@Transient
	public String getDefaultWarehouseName() {
		return defaultWarehouseName;
	}

	public void setDefaultWarehouseName(String defaultWarehouseName) {
		this.defaultWarehouseName = defaultWarehouseName;
	}

	@Transient
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	
	@Transient
	public String getDeviceIds() {
		return deviceIds;
	}

	public void setDeviceIds(String deviceIds) {
		this.deviceIds = deviceIds;
	}

	@Transient
	public String getUpdaterName() {
		return updaterName;
	}

	public void setUpdaterName(String updaterName) {
		this.updaterName = updaterName;
	}

	@Column(length = 10)
    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    @Column(nullable = false)
    public int getLocked() {
        return locked;
    }

    public void setLocked(int locked) {
        this.locked = locked;
    }
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(length = 19)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    @Column(length = 20)
    public String getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(String updaterId) {
        this.updaterId = updaterId;
    }
	@Column(length = 50)
	public String getDefaultWarehId() {
		return defaultWarehId;
	}

	public void setDefaultWarehId(String defaultWarehId) {
		this.defaultWarehId = defaultWarehId;
	}

	@Column(length = 50)
	public String getDefalutCustomerId() {
		return defalutCustomerId;
	}

	public void setDefalutCustomerId(String defalutCustomerId) {
		this.defalutCustomerId = defalutCustomerId;
	}
	@Column(length = 50)
	public String getDefaultSaleStaffId() {
		return defaultSaleStaffId;
	}

	public void setDefaultSaleStaffId(String defaultSaleStaffId) {
		this.defaultSaleStaffId = defaultSaleStaffId;
	}



	private Double vippoints;
	@Column()
	public Double getVippoints() {
		return vippoints;
	}

	public void setVippoints(Double vippoints) {
		this.vippoints = vippoints;
	}

	public String getDeep() {
		return deep;
	}

	public void setDeep(String deep) {
		this.deep = deep;
	}
}