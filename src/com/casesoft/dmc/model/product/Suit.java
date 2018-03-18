package com.casesoft.dmc.model.product;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
@Entity
@Table(name = "PRODUCT_SUIT")
public class Suit  {


	@Id
	@Column(nullable = false,length = 10)
	private String code;	
	
	@Column(nullable = false,length = 50)
	private String name;
	
	@Column(nullable = false,length = 500)
	private String remark;
	
	@Column(nullable = false,length = 50)
	private String imageUrl;
	
	@Column(nullable = false,length = 15)
	private Double price;
	@Column(length = 15)
	private String sex;
	@Column()
	private Date updateDate;

	public Suit() {
		super();
	}
	
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }



	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
 
	@Transient
	List<SuitDtl> list;
	@Transient
	public List<SuitDtl> getList() {
		return list;
	}
	public void setList(List<SuitDtl> list) {
		this.list = list;
	}
	@Transient
	List<String> images;

	@Transient
	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}
	
	

}