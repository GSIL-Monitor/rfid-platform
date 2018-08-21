package com.casesoft.dmc.model.product;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;



/**
 * StyleSort entity. @author
 */

@Entity
@Table(name = "PRODUCT_STYLESCORE")
public class StyleScore implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator ="uuid",strategy = GenerationType.IDENTITY)
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(nullable = false, length = 50)
	private String id;	
	
	@Column(nullable = false, length = 20)
	private String styleId;	
	
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Column(length = 19)	
	private Date scoreTime;
	
	@Column(length = 20)
	private String deviceId;
	
	@Column(nullable = false, length = 1)
	private Integer score;
	
	@Column(length = 20)
	private String shopId;
    
	@Column(length = 20)
	private String customerId;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStyleId() {
		return styleId;
	}

	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}

	public Date getScoreTime() {
		return scoreTime;
	}

	public void setScoreTime(Date scoreTime) {
		this.scoreTime = scoreTime;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
}