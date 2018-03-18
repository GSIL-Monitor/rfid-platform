package com.casesoft.dmc.model.search;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;



@Entity
@Table(name = "SEARCH_DETAILFITTINGVIEW")
public class DetailFittingView extends BaseProductView {
	@Id
	@Column(nullable = false,length=50)
    private String id;	
	@Column(nullable = false, length = 50)
	private String taskId;	
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	@Column(nullable = false,length = 19)
	private Date scanDate;
    @Column(nullable = false,length = 50)
    private String ownerId;    
    @Column(nullable = false,length = 50)
    private String warehId;
   
    @Column(nullable = false,length = 50)
    private String sku;
    @Column()
    private String year;
    
	@Column()
    private String quarter;
    @Column()
    private String month;
    
	@Column()
    private String week;
    
    @Column()
    private String day;

    @Column()
    private Integer qty;
    public DetailFittingView(){
    	
    }
    public DetailFittingView(String countVal,String styleId,String colorId,String sizeId,
    	   String sizeSortId,String sku,Long qty,String ownerId,String warehId,String year,String count){
    	super();
    	switch(count){
    	    case "day":
    		    this.day = countVal;
    		    break;
    	    case "month":
    		    this.month = countVal;
    		    break;
    	    case "week":
    		    this.week = countVal;
    		    break;
    	    case "quarter":
    	    	this.quarter = countVal;
    		    break;
    		
    	}    	    	
    	this.styleId = styleId;
    	this.colorId = colorId;
    	this.sizeId = sizeId;
    	this.sizeSortId = sizeSortId;
    	this.sku = sku;
    	this.qty = qty.intValue();
    	this.ownerId = ownerId;
    	this.warehId = warehId;
    	this.year = year;
    	
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
	public Date getScanDate() {
		return scanDate;
	}

	public void setScanDate(Date scanDate) {
		this.scanDate = scanDate;
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
    
    public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getQuarter() {
		return quarter;
	}    
	public void setQuarter(String quarter) {
		this.quarter = quarter;
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
