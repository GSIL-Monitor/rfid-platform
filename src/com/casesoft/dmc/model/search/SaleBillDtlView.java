package com.casesoft.dmc.model.search;

import java.util.Date;


import javax.persistence.*;

import com.alibaba.fastjson.annotation.JSONField;
import com.casesoft.dmc.model.shop.BaseModelDtl;

/**
 * Created by Wing Li on 2014/6/21.
 */
@Entity
@Table(name = "Search_SaleBillDtlView")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class SaleBillDtlView extends BaseModelDtl {

	private double actPrice;// 实际单价

	private double percent;// 打折率

	private double actValue;// 实际销售额

	private String priceType;// 单价类型

	private String uniqueCode;

	private double refundPrice;
	private String refundBillId;

	private double increaseGrate;// 增长的积分
	
	private double gradeRate;// 积分增长率
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

	public double getGradeRate() {
		return gradeRate;
	}

	public void setGradeRate(double gradeRate) {
		this.gradeRate = gradeRate;
	}


	public double getIncreaseGrate() {
		return increaseGrate;
	}

	public void setIncreaseGrate(double increaseGrate) {
		this.increaseGrate = increaseGrate;
	}

	@Column( length = 50)
	private String client2Id;
	private Date billDate;

	public String getClient2Id() {
		return client2Id;
	}

	public void setClient2Id(String client2Id) {
		this.client2Id = client2Id;
	}

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	@Column( length = 32)
	public String getUniqueCode() {
		return uniqueCode;
	}

	public void setUniqueCode(String uniqueCode) {
		this.uniqueCode = uniqueCode;
	}

	@Column( nullable = true)
	public double getActPrice() {
		return actPrice;
	}

	public void setActPrice(double actPrice) {
		this.actPrice = actPrice;
	}

	@Column( nullable = true)
	public double getPercent() {
		return percent;
	}

	public void setPercent(double percent) {
		this.percent = percent;
	}

	@Column( nullable = true)
	public double getActValue() {
		return actValue;
	}

	public void setActValue(double actValue) {
		this.actValue = actValue;
	}

	@Column(length = 10)
	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

	public double getRefundPrice() {
		return refundPrice;
	}

	public void setRefundPrice(double refundPrice) {
		this.refundPrice = refundPrice;
	}

	public String getRefundBillId() {
		return refundBillId;
	}

	public void setRefundBillId(String refundBillId) {
		this.refundBillId = refundBillId;
	}
	
	 @Column( length = 10)
     public String getClass1() {
		 return class1;
	  }
		
	 public void setClass1(String class1) {
		 this.class1 = class1;
	 }
	 @Column( length = 10)
	 public String getClass2() {
		 return class2;
	 }
		
	 public void setClass2(String class2) {
		 this.class2 = class2;
	 }
	 @Column( length = 10)
	 public String getClass3() {
		 return class3;
	 }
		
	 public void setClass3(String class3) {
		 this.class3 = class3;
	 }
	 @Column( length = 10)
	 public String getClass4() {
		 return class4;
	 }
		
	 public void setClass4(String class4) {
		 this.class4 = class4;
	 }
	 @Column( length = 10)
	 public String getClass5() {
		 return class5;
	 }
		
	 public void setClass5(String class5) {
		 this.class5 = class5;
	 }
	 @Column( length = 10)
	 public String getClass6() {
	      return class6;
	 }

	 public void setClass6(String class6) {
	       this.class6 = class6;
	 }
	 @Column( length = 10)
	 public String getClass7() {
	      return class7;
	 }

	 public void setClass7(String class7) {
	     this.class7 = class7;
	 }
	 @Column( length = 10)
	 public String getClass8() {
	     return class8;
	 }

	 public void setClass8(String class8) {
	     this.class8 = class8;
	 }
	 @Column( length = 10)
	 public String getClass9() {
	      return class9;
	 }

	 public void setClass9(String class9) {
	     this.class9 = class9;
	 }
	 @Column( length = 10)
	 public String getClass10() {
	     return class10;
	 }

	 public void setClass10(String class10) {
	     this.class10 = class10;
	 }	 


}
