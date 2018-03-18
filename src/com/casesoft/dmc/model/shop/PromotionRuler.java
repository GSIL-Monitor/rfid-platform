package com.casesoft.dmc.model.shop;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "Shop_PromotionRuler")
public class PromotionRuler {
	@Id
	@Column(name="id",nullable = false, length = 50)
	private String id;
	
	@Column(name="billNo", nullable = false, length = 50)
	private String billNo; //对应促销主单中单号
	
	@Column(name="type", nullable = false, length = 2)
	private String type;//
	
	@Column(name="descrip",nullable = false, length = 100)
	private String descrip;//规则描述
	
	@Column(name="tot_Price")
	private Double totPrice;//促销购满金额 
	
	@Column(name="reduct_Price")
	private Double reductPrice;//减免金额
	
	@Column(name="tot_Qty")
	private Integer totQty;//促销购满数量
	
	@Column(name="discount")
	private Double discount;//折率
	
	@Column(name="promotPrice")
	private Double promotPrice;//促销价、特价、换购价
	
	@Column(name="priority")
	private Integer priority;//规则优先级 0表示最低
	
	@Column(name="styleId")
	private String styleId;//换购或赠品款号
	
	@Column(name="price")
	private String price;//换购商品价格
	
	@Column(name="grade")
	private String grade;//最多可用积分

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescrip() {
		return descrip;
	}

	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}

	public Double getTotPrice() {
		return totPrice;
	}

	public void setTotPrice(Double totPrice) {
		this.totPrice = totPrice;
	}

	public Double getReductPrice() {
		return reductPrice;
	}

	public void setReductPrice(Double reductPrice) {
		this.reductPrice = reductPrice;
	}

	public Integer getTotQty() {
		return totQty;
	}

	public void setTotQty(Integer totQty) {
		this.totQty = totQty;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getPromotPrice() {
		return promotPrice;
	}

	public void setPromotPrice(Double promotPrice) {
		this.promotPrice = promotPrice;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getStyleId() {
		return styleId;
	}

	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	

}
