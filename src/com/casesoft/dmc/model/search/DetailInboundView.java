package com.casesoft.dmc.model.search;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "SEARCH_DETAILINBOUNDVIEW")
public class DetailInboundView extends BaseProductView {
	@Id
	@Column(nullable = false,length=50)
    private String id;	
	@Column(nullable = false, length = 50)
	private String taskId;	
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	@Column(nullable = false,length = 19)
	private Date billDate;
	@Column(nullable = false, length = 50)
	private String billNo;	
	@Column()
	private String deviceId;	
    @Column(nullable = false,length = 50)
    private String ownerId;    
    @Column(nullable = false,length = 50)
    private String destid;
    @Column()
    private  String destUnitId;
    @Column(nullable = false,length = 50)
    private String origid;
   
    @Column(nullable = false,length = 50)
    private String sku;

    @Column()
    private Integer qty;
    
    @Column()
    private Integer token;
    @Column()
    private String isBill;    
	@Column()
    private Integer actQty;
    @Column()
    private Integer billQty;

    public Integer getToken() {
		return token;
	}

	public void setToken(Integer token) {
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
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
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

	public int getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDestid() {
        return destid;
    }

    public void setDestid(String destid) {
        this.destid = destid;
    }

    public String getOrigid() {
        return origid;
    }

    public void setOrigid(String origid) {
        this.origid = origid;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getDestUnitId() {
        return destUnitId;
    }

    public void setDestUnitId(String destUnitId) {
        this.destUnitId = destUnitId;
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
    private String destName;

    @Transient
    private String origName;

    @Transient
    private String destUnitName;

    public String getDestName() {
        return destName;
    }

    public void setDestName(String destName) {
        this.destName = destName;
    }

    public String getOrigName() {
        return origName;
    }

    public void setOrigName(String origName) {
        this.origName = origName;
    }

    public String getDestUnitName() {
        return destUnitName;
    }

    public void setDestUnitName(String destUnitName) {
        this.destUnitName = destUnitName;
    }
}
