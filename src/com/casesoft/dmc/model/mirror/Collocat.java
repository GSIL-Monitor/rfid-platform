package com.casesoft.dmc.model.mirror;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.alibaba.fastjson.annotation.JSONField;
import com.casesoft.dmc.model.product.Style;

@Entity
@Table(name = "MIRROR_COLLOCAT")
public class Collocat implements java.io.Serializable {
	/**
	 * 搭配id唯一
	 * */
	@Id
	@Column(length = 100)
	private String id;
	/**
	 * 搭配套装明细款id以,分割
	 * */
	@Column(length = 100)
	private String styleIds;
	
	@Column()
	private double price;
	
	@Column(length = 1)
	private String isShow;
	
	/**
	 * 套装图片地址图片名套装id,和款名命名
	 * */
	@Column()
	private String url;
	
	@Column(length=2000)
	private String remark;
	
	@Column(length = 50)
    private String updater;
	
	@Column()
	@JSONField(format="yyyy-MM-dd hh:mm:ss")
	private Date updateTime;
	
	@Column()
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer seqNo;
	@Transient
	private String[] images;

	@Transient
	private List<NewProduct> styleList;
	
	public List<NewProduct> getStyleList() {
		return styleList;
	}

	public void setStyleList(List<NewProduct> styleList) {
		this.styleList = styleList;
	}

	public String[] getImages() {
		return images;
	}

	public void setImages(String[] images) {
		this.images = images;
	}
		
	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStyleIds() {
		return styleIds;
	}

	public void setStyleIds(String styleIds) {
		this.styleIds = styleIds;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
    
}
