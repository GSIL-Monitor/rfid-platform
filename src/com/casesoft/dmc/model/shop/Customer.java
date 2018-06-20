package com.casesoft.dmc.model.shop;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;

import java.util.Date;
import java.util.List;
@Entity
@Table(name = "shop_customer")
public class Customer implements java.io.Serializable {

    private static final Long serialVersionUID = -7987577278729516532L;

    private String id;
    private String code;
    private String password;
    private String name;
    private String ownerId;

    private String areaCode;//区域编号
    private String socialNo;//身份证
    private Integer status;//状态
    private Integer birthType;//生日类型
    private Long buyQty;
    private Double buyAmount;

    private Double grade;// 累加积分 grade-usedGrade=当前积分
    private Double usedGrade;//使用积分
    private String weChat;
    private String epay;

    private Date birth;
    // john 添加属性
    private String idCard;
    private Integer sex;
    private String rank;//等级
    private Date endDate;
    private String company;
    private String job;
    private String companyPhone;
    private String companyZibcode;
    private String companyAddress;
    //
    private String tel;//变更为tel
    private String homeZipcode;
    private String remark;
    private String phone;//电话
    private String creatorId;
    private Date createTime;
    private Integer locked;

    private String unitName;


    private String saveMon;

    private String email;
    private Date lastBuyDate;

    private String depositBank;
    private Integer discount;
    private Double storedValue;
    private Double owingValue;
    private String defaultWarehId;
    private Integer type;
    private String Email;
    private String bankCode;
    private String bankAccount;
    private String fax;
    private String address;
    private String areaId;
    private String Province;
    private String city;
    private Date storeDate;
    private String updaterId;
    private String linkman;
    private Date updateTime;
    private String vipId;
    private Double vippoints;
    @Column( length = 100)
    private String unionid;
    @Column( length = 50)
    private String sendSucess;

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

    @Column()
    public Double getVippoints() {
        return vippoints;
    }

    public void setVippoints(Double vippoints) {
        this.vippoints = vippoints;
    }

    @Column( length = 50)
    public String getVipId() {
        return vipId;
    }

    public void setVipId(String vipId) {
        this.vipId = vipId;
    }

    private Integer zk;
    @Column( length = 3)
    public Integer getZk() {
        return zk;
    }

    public void setZk(Integer zk) {
        this.zk = zk;
    }

    @Column( length = 50)
    public String getSaveMon() {
        return saveMon;
    }

    public void setSaveMon(String saveMon) {
        this.saveMon = saveMon;
    }
    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    @Column( length = 50)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(nullable = false, length = 50)
    public String getCreatorId() {
        return this.creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @Column(name="lastBuyDate")
    public Date getLastBuyDate() {
        return lastBuyDate;
    }

    public void setLastBuyDate(Date lastBuyDate) {
        this.lastBuyDate = lastBuyDate;
    }



    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @Column(length = 19)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    @Column()
    public Integer getLocked() {
        return locked;
    }

    public void setLocked(Integer locked) {
        this.locked = locked;
    }
    @Column(nullable = false, length = 50)
    public String getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @Column(nullable = false, length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Column(length = 20)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @Column(unique = true, nullable = false, length = 50)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Id
    @Column(nullable = false, length = 50)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String qq;
	@Column(name = "qq", length = 20)
    public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}



    @Column(length = 20)
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Column( length = 20)
    public String getHomeZipcode() {
        return homeZipcode;
    }

    public void setHomeZipcode(String homeZipcode) {
        this.homeZipcode = homeZipcode;
    }
    @Column( length = 50)
    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    @Column(length = 50)
    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    @Column( nullable = false, length = 10)
    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }


    @Column(length = 50)
    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @Column(length = 19)
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Column(length = 200)
    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Column( length = 100)
    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    @Column(length = 50)
    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    @Column( length = 200)
    public String getCompanyAddress() {
        return companyAddress;
    }

    @Column( length = 50)
    public String getCompanyZibcode() {
        return companyZibcode;
    }

    public void setCompanyZibcode(String companyZibcode) {
        this.companyZibcode = companyZibcode;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    @Column()
    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    @Column(unique = true, length = 100)
    public String getWeChat() {
        return weChat;
    }

    public void setWeChat(String weChat) {
        this.weChat = weChat;
    }

    @Column( unique = true, length = 100)
    public String getEpay() {
        return epay;
    }

    public void setEpay(String epay) {
        this.epay = epay;
    }



    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @Column( length = 19)
    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    @Column()
    public Double getUsedGrade() {
        return usedGrade;
    }

    public void setUsedGrade(Double usedGrade) {
        this.usedGrade = usedGrade;
    }

    @Column(length = 4)
    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
    @Column(length = 40)
    public String getSocialNo() {
        return socialNo;
    }

    public void setSocialNo(String socialNo) {
        this.socialNo = socialNo;
    }
    @Column()
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column()
    public Integer getBirthType() {
        return birthType;
    }

    public void setBirthType(Integer birthType) {
        this.birthType = birthType;
    }
    @Column(name = "byqt")
    public Long getBuyQty() {
        return buyQty;
    }

    public void setBuyQty(Long buyQty) {
        this.buyQty = buyQty;
    }
    @Column()
    public Double getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(Double buyAmount) {
        this.buyAmount = buyAmount;
    }
    @Column(length = 400)
    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    @Column()
    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @Column(length = 19)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    private String lastBill;//末销单号
  
    private List<SaleBillColorAnalysis> colorLike;//颜色喜好
    
    private List<SaleBillSizeAnalysis> sizeLike;//尺寸喜好
    
    private List<StyleMaterialAnalysis> styleMateriaLike;//材质喜好
    
    private List<StyleSubClassAnalysis> styleSubClassLike;//小类喜好
    
    private Double refundRate;//退货率
    
    private Double associatedRate;//连带率
   
    private Double springSummerAvgPrice;//春夏均价
   
    private Double fallWIntegererAvgPrice;//秋冬均价
    
    
    private Long refundQty;//退货数量

    @Column()
    public String getDepositBank() {
        return depositBank;
    }

    public void setDepositBank(String depositBank) {
        this.depositBank = depositBank;
    }

    @Column()
    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
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
    public String getDefaultWarehId() {
        return defaultWarehId;
    }

    public void setDefaultWarehId(String defaultWarehId) {
        this.defaultWarehId = defaultWarehId;
    }

    @Column()
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }



    @Column()
    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    @Column()
    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    @Column()
    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    @Column()
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column()
    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    @Column()
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column()
    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    @Column()
    public Date getStoreDate() {
        return storeDate;
    }

    public void setStoreDate(Date storeDate) {
        this.storeDate = storeDate;
    }




    @Column()
    public String getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(String updaterId) {
        this.updaterId = updaterId;
    }

    @Transient
	public String getLastBill() {
		return lastBill;
	}

	public void setLastBill(String lastBill) {
		this.lastBill = lastBill;
	}
	@Transient
	public List<SaleBillColorAnalysis> getColorLike() {
		return colorLike;
	}

	public void setColorLike(List<SaleBillColorAnalysis> colorLike) {
		this.colorLike = colorLike;
	}
	@Transient
	public List<SaleBillSizeAnalysis> getSizeLike() {
		return sizeLike;
	}

	public void setSizeLike(List<SaleBillSizeAnalysis> sizeLike) {
		this.sizeLike = sizeLike;
	}
	@Transient
	public List<StyleMaterialAnalysis> getStyleMateriaLike() {
		return styleMateriaLike;
	}

	public void setStyleMateriaLike(List<StyleMaterialAnalysis> styleMateriaLike) {
		this.styleMateriaLike = styleMateriaLike;
	}
	@Transient
	public List<StyleSubClassAnalysis> getStyleSubClassLike() {
		return styleSubClassLike;
	}

	public void setStyleSubClassLike(List<StyleSubClassAnalysis> styleSubClassLike) {
		this.styleSubClassLike = styleSubClassLike;
	}
	@Transient
	public Double getRefundRate() {
		return refundRate;
	}

	public void setRefundRate(Double refundRate) {
		this.refundRate = refundRate;
	}
	@Transient
	public Double getAssociatedRate() {
		return associatedRate;
	}

	public void setAssociatedRate(Double associatedRate) {
		this.associatedRate = associatedRate;
	}
	@Transient
	public Double getSpringSummerAvgPrice() {
		return springSummerAvgPrice;
	}

	public void setSpringSummerAvgPrice(Double springSummerAvgPrice) {
		this.springSummerAvgPrice = springSummerAvgPrice;
	}
	@Transient
	public Double getFallWIntegererAvgPrice() {
		return fallWIntegererAvgPrice;
	}

	public void setFallWIntegererAvgPrice(Double fallWIntegererAvgPrice) {
		this.fallWIntegererAvgPrice = fallWIntegererAvgPrice;
	}
	@Transient
	public Long getRefundQty() {
		return refundQty;
	}

	public void setRefundQty(Long refundQty) {
		this.refundQty = refundQty;
	}
    
}
