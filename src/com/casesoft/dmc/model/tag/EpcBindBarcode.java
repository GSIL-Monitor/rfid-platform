package com.casesoft.dmc.model.tag;

import java.util.Date;

import javax.persistence.*;


import com.alibaba.fastjson.annotation.JSONField;

@Entity
@Table(name = "TAG_EPCBINDBARCODE")
public class EpcBindBarcode  implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(length=50)
	private String epc;
	@Column(length=50)
	private String code;
	@Transient
	private String styleId;

	@Transient
	private String colorId;
	@Transient
	private String sizeId;
	@Transient
	private String styleName;
	@Transient
	private String colorName;
	@Transient
	private String sizeName;

	@Column(length=50)
	private String tid;
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	@Column(length=20)
	private Date updateTime;
	@Column(length=20)
	private String updaterId;
    @Column
    private Long version;
	@Column(name="disabled")
	private Boolean disabled=false;//是否解绑：false 可用，true:不可用
	@Transient
	private String code2;//二维码
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

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}



	public String getCode2() {
		return code2;
	}

	public void setCode2(String code2) {
		this.code2 = code2;
	}

	public EpcBindBarcode(){}
	public EpcBindBarcode(String epc, String code) {
		super();
		this.epc = epc;
        this.code = code;
	}
	public String getEpc() {
		return epc;
	}
	public void setEpc(String epc) {
		this.epc = epc;
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
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

	public String getUpdaterId() {
		return updaterId;
	}

	public void setUpdaterId(String updaterId) {
		this.updaterId = updaterId;
	}
}
