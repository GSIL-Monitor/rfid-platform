package com.casesoft.dmc.model.sys;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;

/**
 * User entity. @author
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "SYS_USER")
public class User implements java.io.Serializable {

	private static final long serialVersionUID = 242279331976665817L;

	private String id;
	private String code;
	private String password;
	private String name;

	private String ownerId;
	private String phone;
	private String creatorId;
	private Date createDate;
	private String email;
	private String roleId;// 角色ID
	private Integer isAdmin;
	private String remark;

	private String address;// 默认地址
	private String addressId;// 默认地址ID

	private int locked;
	private String roleName;
	private String unitName;
	// 增加类型标记
	private Integer type;//0 普通用户 1系统管理员 4:收银员
	private String src;
	private String defaultWH;//默认仓库
	private Integer acceptMessageOfOA; // 接受微信公众号消息 1接受 0不接受；只有管理员角色这个参数才有效


	@Column(nullable = false)
	public int getLocked() {
		return locked;
	}

	public void setLocked(int locked) {
		this.locked = locked;
	}

	// Constructors

	/** default constructor */
	public User() {
	}

	/** minimal constructor */
	public User(String code, String password, String name, String ownerId,
			String creatorId, Date createDate, String typeId, Integer isAdmin) {
		this.code = code;
		this.password = password;
		this.name = name;
		this.ownerId = ownerId;
		this.creatorId = creatorId;
		this.createDate = createDate;
		this.roleId = typeId;
		this.isAdmin = isAdmin;
	}

	/** full constructor */
	public User(String code, String password, String name, int locked,
			String ownerId, String phone, String creatorId, Date createDate,
			String email, String typeId, Integer isAdmin, String remark) {
		this.code = code;
		this.password = password;
		this.name = name;
		this.locked = locked;
		this.ownerId = ownerId;
		this.phone = phone;
		this.creatorId = creatorId;
		this.createDate = createDate;
		this.email = email;
		this.roleId = typeId;
		this.isAdmin = isAdmin;
		this.remark = remark;
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

	@Column(unique = true, nullable = false, length = 50)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(nullable = false, length = 50)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(nullable = false, length = 50)
	public String getOwnerId() {
		return this.ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	@Column(length = 50, nullable = false, unique = true)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(nullable = false, length = 50)
	public String getCreatorId() {
		return this.creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
    
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	@Column(nullable = false, length = 19)
	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(length = 50)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(nullable = false, length = 50)
	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String typeId) {
		this.roleId = typeId;
	}

	@Column(nullable = false)
	public Integer getIsAdmin() {
		return this.isAdmin;
	}

	public void setIsAdmin(Integer isAdmin) {
		this.isAdmin = isAdmin;
	}

	@Column(length = 400)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(length = 200)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(length = 200)
	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}
    @Column(length = 5)
    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
// /------------------>
	

	@Transient
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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
	public String getDefaultWH() {
		return defaultWH;
	}

	public void setDefaultWH(String defaultWH) {
		this.defaultWH = defaultWH;
	}

	@Column()
	public Integer getAcceptMessageOfOA() {
		return acceptMessageOfOA;
	}

	public void setAcceptMessageOfOA(Integer acceptMessageOfOA) {
		this.acceptMessageOfOA = acceptMessageOfOA;
	}
}