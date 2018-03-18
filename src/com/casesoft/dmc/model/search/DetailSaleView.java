package com.casesoft.dmc.model.search;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;


@SuppressWarnings("serial")
@Entity
@Table(name = "SEARCH_DETAILSALEVIEW")
public class DetailSaleView extends BaseProductView {
	@Id
	@Column(nullable = false,length=50)
    private String id;	
	@Column(nullable = false, length = 50)
	private String taskId;	
	@Column()
	private String type;	
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
    private String month;
    
	@Column()
    private String week;
    
    @Column()
    private String day;
    @Column()
    private double actPrice;
    @Column()
    private Integer qty;
    public DetailSaleView(){}
    public DetailSaleView(String day,String month,String week,String styleId,String colorId,String sizeId,
    	   String sizeSortId,String sku,Long qty,String ownerId,String warehId){
    	super();
    	this.day = day;
    	this.month = month;
    	this.week = week;
    	this.week = week;
    	this.styleId = styleId;
    	this.colorId = colorId;
    	this.sizeId = sizeId;
    	this.sizeSortId = sizeSortId;
    	this.sku = sku;
    	this.qty = qty.intValue();
    	this.ownerId = ownerId;
    	this.warehId = warehId;
    	
    }
    public double getActPrice() {
		return actPrice;
	}
	public void setActPrice(double actPrice) {
		this.actPrice = actPrice;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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

    public void setQty(Integer qty) {
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
    

    
    public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
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
