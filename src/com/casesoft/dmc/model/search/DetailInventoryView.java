package com.casesoft.dmc.model.search;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "SEARCH_DETAILINVENTORYVIEW")
public class DetailInventoryView extends BaseProductView {
	@Id
	@Column(nullable = false,length=50)
    private String id;	
	@Column(nullable = false, length = 50)
	private String taskId;
	@Column(nullable = false, length = 50)
	private String billNo;	
	@Column()
	private String deviceId;	
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	@Column(nullable = false,length = 19)
	private Date billDate;
    @Column(nullable = false,length = 50)
    private String ownerId;    
    @Column(nullable = false,length = 50)
    private String warehId;
   
    @Column(nullable = false,length = 50)
    private String sku;

    @Column()
    private int qty;
    
    @Column()
    private String isBill;
    
	@Column()
    private Integer actQty;
    @Column()
    private Integer billQty;
    
    @Column()
    private int token;
    
    public int getToken() {
		return token;
	}

	public void setToken(int token) {
		this.token = token;
	}
    
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}


    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getWarehId() {
        return warehId;
    }

    public void setWarehId(String warehId) {
        this.warehId = warehId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
    
    public String getIsBill() {
		return isBill;
	}

	public void setIsBill(String isBill) {
		this.isBill = isBill;
	}

	public Integer getActQty() {
		return actQty;
	}

	public void setActQty(Integer actQty) {
		this.actQty = actQty;
	}

	public Integer getBillQty() {
		return billQty;
	}

	public void setBillQty(Integer billQty) {
		this.billQty = billQty;
	}

    @Transient
    private String warehName;

    public String getWarehName() {
        return warehName;
    }

    public void setWarehName(String warehName) {
        this.warehName = warehName;
    }
    
   
}
