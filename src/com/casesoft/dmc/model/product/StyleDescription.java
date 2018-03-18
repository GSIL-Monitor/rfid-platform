package com.casesoft.dmc.model.product;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PRODUCT_STYLEDESCRIPTION")
public class StyleDescription implements java.io.Serializable {
	@Id
	@Column(nullable = false,length = 50)
	private String styleId;
	@Column(length = 1000)
	private String description;
	@Column()
	private Date updateDate;
	@JSONField(format = "yyyy-MM-dd")
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getStyleId() {
		return styleId;
	}
	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
