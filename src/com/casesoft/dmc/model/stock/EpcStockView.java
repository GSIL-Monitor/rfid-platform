package com.casesoft.dmc.model.stock;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "STOCK_EPCSTOCKVIEW")
public class EpcStockView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * 表名为： TTE_STK， 中文为：EPC的库存 字段有 code 唯一码， warehouseId 发货仓库1ID， ownerId
	 * 发货组织1ID， inStock 是否在库， purIng 购买途中 ， tranIng调拨途中， refundIng退货途中，
	 * warehouse2Id收货仓库2Id， owner2Id收货组织2
	 */
	public static final int INSOTRAGE=0;
	public static final int PURING=1;
	public static final int TRANING=2;
	public static final int REFUNDING=3;
	public static final int ADJUSTING=4;
    /*private String id;*/
	private String code;
	private String warehouse1Id;
	private String warehouse2Id;
	private String ownerId;
	private String owner2Id;
	private int inStock;
	private String styleId;
	private String colorId;
	private String sizeId;
	private String deviceId;
	private String sku;
	private Date updateDate;
	private int token;
	private String taskId;
	private int progress;//0:库中，1:购买中，2:调拨中，3：退货中，4：调整
	
	private String class1;//年份
	private String class2;//季节
	private String class3;//性别
	private String class4;//大类
	private String class5;//小类
	private String class6;//面料
	private String class7;//主题系列
	private String class8;//商品质量
	private String class9;//商品分类
	private String class10;//商品级别
	
	private String styleName;
	private String colorName;
	private String sizeName;
	@Column(nullable = false, length = 50)
	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	@Column(nullable = false)
	public int getToken() {
		return token;
	}

	public void setToken(int token) {
		this.token = token;
	}
	@Column(length = 50)
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	


	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@Column(length = 19)
	public Date getUpdateDate() {
		return updateDate;
	}
	@Column()
	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Column(length = 50)
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/*@Id
	@Column(name = "id", nullable = false, length = 45)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}*/

	@Id
	@Column(nullable = false, length = 45)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(nullable = false, length = 50)
	public String getWarehouse1Id() {
		return warehouse1Id;
	}

	public void setWarehouse1Id(String warehouse1Id) {
		this.warehouse1Id = warehouse1Id;
	}

	@Column(length = 50)
	public String getWarehouse2Id() {
		return warehouse2Id;
	}

	public void setWarehouse2Id(String warehouse2Id) {
		this.warehouse2Id = warehouse2Id;
	}

	@Column(nullable = false, length = 50)
	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	@Column(length = 50)
	public String getOwner2Id() {
		return owner2Id;
	}

	public void setOwner2Id(String owner2Id) {
		this.owner2Id = owner2Id;
	}

	@Column(nullable = false)
	public int getInStock() {
		return inStock;
	}

	public void setInStock(int inStock) {
		this.inStock = inStock;
	}

	@Column(length = 20)
	public String getStyleId() {
		return styleId;
	}

	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}

	@Column(length = 10)
	public String getColorId() {
		return colorId;
	}

	public void setColorId(String colorId) {
		this.colorId = colorId;
	}

	@Column(length = 10)
	public String getSizeId() {
		return sizeId;
	}

	public void setSizeId(String sizeId) {
		this.sizeId = sizeId;
	}

	 
	  @Transient
	  public String getStyleName() {
	    return styleName;
	  }

	  public void setStyleName(String styleName) {
	    this.styleName = styleName;
	  }

	  @Transient
	  public String getColorName() {
	    return colorName;
	  }

	  public void setColorName(String colorName) {
	    this.colorName = colorName;
	  }

	  @Transient
	  public String getSizeName() {
	    return sizeName;
	  }

	  public void setSizeName(String sizeName) {
	    this.sizeName = sizeName;
	  }
	  
	  @Column(length = 10)
      public String getClass1() {
		 return class1;
	  }
		
	 public void setClass1(String class1) {
		 this.class1 = class1;
	 }
	 @Column(length = 10)
	 public String getClass2() {
		 return class2;
	 }
		
	 public void setClass2(String class2) {
		 this.class2 = class2;
	 }
	 @Column(length = 10)
	 public String getClass3() {
		 return class3;
	 }
		
	 public void setClass3(String class3) {
		 this.class3 = class3;
	 }
	 @Column(length = 10)
	 public String getClass4() {
		 return class4;
	 }
		
	 public void setClass4(String class4) {
		 this.class4 = class4;
	 }
	 @Column(name = "c5", length = 10)
	 public String getClass5() {
		 return class5;
	 }
		
	 public void setClass5(String class5) {
		 this.class5 = class5;
	 }
	 @Column(name = "c6", length = 10)
	 public String getClass6() {
	      return class6;
	 }

	 public void setClass6(String class6) {
	       this.class6 = class6;
	 }
	 @Column(name = "c7", length = 10)
	 public String getClass7() {
	      return class7;
	 }

	 public void setClass7(String class7) {
	     this.class7 = class7;
	 }
	 @Column(name = "c8", length = 10)
	 public String getClass8() {
	     return class8;
	 }

	 public void setClass8(String class8) {
	     this.class8 = class8;
	 }
	 @Column(name = "c9", length = 10)
	 public String getClass9() {
	      return class9;
	 }

	 public void setClass9(String class9) {
	     this.class9 = class9;
	 }
	 @Column(name = "c10", length = 10)
	 public String getClass10() {
	     return class10;
	 }

	 public void setClass10(String class10) {
	     this.class10 = class10;
	 }	 

	   
	    
}
