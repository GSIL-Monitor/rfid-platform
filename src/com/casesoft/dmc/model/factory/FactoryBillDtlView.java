package com.casesoft.dmc.model.factory;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name="FACTORY_BILL_DTL_VIEW")
public class FactoryBillDtlView implements java.io.Serializable {
	@Id
	@Column(name="Id")
	private String id;
	@Column(name="bill_no")
	private String billNo;
	@JSONField(format="yyyy-MM-dd")
	@Column(name="bill_date")
	private Date billDate;
	@Column(name="token")
	private String token;
    @Column()
    private String tokenName;
	@Column(name="task_Type")
	private String taskType;
	@Column(name="is_OutSource")
	private String isOutSource;
	@Column(name="code")
	private String code;
	@Column(name="style_Id")
	private String styleId;
	@Column(name="color_Id")
	private String colorId;
	@Column(name="size_Id")
	private String sizeId;
	@Column(name="owner_Id")
	private String ownerId;
	@Column(name="customer_Id")
	private String customerId;
	@Column(name="group_Id")
	private String groupId;	
	@Column(name="wash_Type")
	private String washType;
	@Column(name="sex")
	private String sex;
	@Column(name="type")
	private String type;
	@Column(name="shirt_Type")
	private String shirtType;
	@Column(name="remark")
	private String remark;
	@Column(name="bill_operator")
	private String billOperator;
	@Column(name="task_operator")
	private String taskOperator;
	@Column(name="groupCode")
	private String groupCode;
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	@Column(name="task_Date")
	private Date taskDate;
	@JSONField(format="yyyy-MM-dd")
	@Column(name="end_Date")
	private Date endDate;

    @JSONField(format="yyyy-MM-dd")
    @Column(name="printDate")
    private Date printDate;
	
	@Column(name ="total_Time")
	private Double totalTime;	
	@Column(name="qty")
	private long qty;
	
	@Column(name="uploadNo")
	private String uploadNo;

    @Column(name="sign")
    private String sign;

    @Column(name="progress")
    private String progress;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}	
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public Date getBillDate() {
		return billDate;
	}
	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}
	public Double getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(Double totalTime) {
		this.totalTime = totalTime;
	}
	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getStyleId() {
		return styleId;
	}
	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}
	public String getColorId() {
		return colorId;
	}
	public void setColorId(String colorId) {
		this.colorId = colorId;
	}
	public String getSizeId() {
		return sizeId;
	}
	public void setSizeId(String sizeId) {
		this.sizeId = sizeId;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getWashType() {
		return washType;
	}
	public void setWashType(String washType) {
		this.washType = washType;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getShirtType() {
		return shirtType;
	}
	public void setShirtType(String shirtType) {
		this.shirtType = shirtType;
	}
	public String getRemark() {
		return remark;
	}
	public String getUploadNo() {
		return uploadNo;
	}
	public void setUploadNo(String uploadNo) {
		this.uploadNo = uploadNo;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}	
	
	public String getBillOperator() {
		return billOperator;
	}
	public void setBillOperator(String billOperator) {
		this.billOperator = billOperator;
	}
	public String  getToken() {
		return token;
	}
	public void setToken(String  token) {
		this.token = token;
	}

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }


	public Date getTaskDate() {
		return taskDate;
	}
	public void setTaskDate(Date taskDate) {
		this.taskDate = taskDate;
	}	
	

	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


    public Date getPrintDate() {
        return printDate;
    }

    public void setPrintDate(Date printDate) {
        this.printDate = printDate;
    }
	public long getQty() {
		return qty;
	}
	public void setQty(long qty) {
		this.qty = qty;
	}
	
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public String getIsOutSource() {
		return isOutSource;
	}
	public void setIsOutSource(String isOutSource) {
		this.isOutSource = isOutSource;
	}	
	public String getTaskOperator() {
		return taskOperator;
	}
	public void setTaskOperator(String taskOperator) {
		this.taskOperator = taskOperator;
	}

	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

	
	@Transient
	private String taskTypeName;

	
	public String getTaskTypeName() {
		return taskTypeName;
	}
	public void setTaskTypeName(String taskTypeName) {
		this.taskTypeName = taskTypeName;
	}

	@Transient
	private String styleName;
	
	public String getStyleName() {
		return styleName;
	}
	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}
	
	@Transient
	private String colorName;

	public String getColorName() {
		return colorName;
	}
	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	
	@Transient
	private String group;

	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	@Column(name="groupName")
	private String groupName;	
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	@Column(name="season")
	private String season;
	
	@Column(name="category")
	private String category;
	
	@Column(name="factory")
	private String factory;
	

	@Column(name="imgUrl")
	private String imgUrl;




	public String getSeason() {
		return season;
	}


	public void setSeason(String season) {
		this.season = season;
	}


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public String getFactory() {
		return factory;
	}


	public void setFactory(String factory) {
		this.factory = factory;
	}


	public String getImgUrl() {
		return imgUrl;
	}


	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	
	
	
	

}
