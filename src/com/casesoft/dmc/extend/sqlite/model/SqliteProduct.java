package com.casesoft.dmc.extend.sqlite.model;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

@DatabaseTable(tableName = "PRODUCT_PRODUCT")
public class SqliteProduct implements java.io.Serializable {



	@DatabaseField(id=true,columnName="id",index = true )
	private String id;
	@DatabaseField(columnName="code",index = true)
	private String code;
	@DatabaseField
	private String styleId;
	@DatabaseField
	private String colorId;
	@DatabaseField
	private String sizeId;
	@DatabaseField
	private String remark;
	@DatabaseField
	private String isSample;
	@DatabaseField
	private String sizeSortId;
	@DatabaseField
	private String image;
	@DatabaseField
	private Long version=0l;
	@DatabaseField
	private Integer boxQty=0;//规格
	@DatabaseField
 	private int isDeton=0;

	@DatabaseField
	private String styleEname;

	// @Transient
	@DatabaseField

	private String styleName;
	@DatabaseField
	private String colorName;
	@DatabaseField
	private String sizeName;

	@DatabaseField
	private Double price;
	@DatabaseField
	private Double preCast=0d;
	@DatabaseField
	private Double wsPrice=0d;
	@DatabaseField
	private double puPrice=0d;

	@DatabaseField
	private String class1;//年份
	@DatabaseField
	private String class2;//季节
	@DatabaseField
	private String class3;//性别
	@DatabaseField
	private String class4;//大类
	@DatabaseField
	private String class5;//小类
	@DatabaseField
	private String class6;//面料
	@DatabaseField
	private String class7;//主题系列
	@DatabaseField
	private String class8;//商品质量
	@DatabaseField
	private String class9;//商品分类
	@DatabaseField
	private String class10;//商品级别

	@DatabaseField
	private String styleRemark;
	@DatabaseField
	private String brandName;
	@DatabaseField
	private String styleSortName;
	@DatabaseField
	private String sizeSortName;

	@DatabaseField
	private String push;

	@DatabaseField
	private Integer styleCycle; //款销售退货周期，默认20天

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIsSample() {
		return isSample;
	}

	public void setIsSample(String isSample) {
		this.isSample = isSample;
	}

	public String getSizeSortId() {
		return sizeSortId;
	}

	public void setSizeSortId(String sizeSortId) {
		this.sizeSortId = sizeSortId;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Integer getBoxQty() {
		return boxQty;
	}

	public void setBoxQty(Integer boxQty) {
		this.boxQty = boxQty;
	}


	public int getIsDeton() {
		return isDeton;
	}


	public String getStyleEname() {
		return styleEname;
	}

	public void setStyleEname(String styleEname) {
		this.styleEname = styleEname;
	}


	public String getStyleName() {
		return styleName;
	}

	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getSizeName() {
		return sizeName;
	}

	public void setSizeName(String sizeName) {
		this.sizeName = sizeName;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getPreCast() {
		return preCast;
	}

	public void setPreCast(Double preCast) {
		this.preCast = preCast;
	}

	public Double getWsPrice() {
		return wsPrice;
	}

	public void setWsPrice(Double wsPrice) {
		this.wsPrice = wsPrice;
	}

	public String getClass1() {
		return class1;
	}

	public void setClass1(String class1) {
		this.class1 = class1;
	}

	public String getClass2() {
		return class2;
	}

	public void setClass2(String class2) {
		this.class2 = class2;
	}

	public String getClass3() {
		return class3;
	}

	public void setClass3(String class3) {
		this.class3 = class3;
	}

	public String getClass4() {
		return class4;
	}

	public void setClass4(String class4) {
		this.class4 = class4;
	}

	public String getClass5() {
		return class5;
	}

	public void setClass5(String class5) {
		this.class5 = class5;
	}

	public String getClass6() {
		return class6;
	}

	public void setClass6(String class6) {
		this.class6 = class6;
	}

	public String getClass7() {
		return class7;
	}

	public void setClass7(String class7) {
		this.class7 = class7;
	}

	public String getClass8() {
		return class8;
	}

	public void setClass8(String class8) {
		this.class8 = class8;
	}

	public String getClass9() {
		return class9;
	}

	public void setClass9(String class9) {
		this.class9 = class9;
	}

	public String getClass10() {
		return class10;
	}

	public void setClass10(String class10) {
		this.class10 = class10;
	}

	public String getStyleRemark() {
		return styleRemark;
	}

	public void setStyleRemark(String styleRemark) {
		this.styleRemark = styleRemark;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getStyleSortName() {
		return styleSortName;
	}

	public void setStyleSortName(String styleSortName) {
		this.styleSortName = styleSortName;
	}

	public String getSizeSortName() {
		return sizeSortName;
	}

	public void setSizeSortName(String sizeSortName) {
		this.sizeSortName = sizeSortName;
	}

	public void setIsDeton(int isDeton) {
		this.isDeton = isDeton;
	}

	public double getPuPrice() {
		return puPrice;
	}

	public void setPuPrice(double puPrice) {
		this.puPrice = puPrice;
	}

	public String getPush() {
		return push;
	}

	public void setPush(String push) {
		this.push = push;
	}

	public Integer getStyleCycle() {
		return styleCycle;
	}

	public void setStyleCycle(Integer styleCycle) {
		this.styleCycle = styleCycle;
	}
}