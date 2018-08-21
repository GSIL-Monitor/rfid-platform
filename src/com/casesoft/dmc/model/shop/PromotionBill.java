package com.casesoft.dmc.model.shop;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Shop_PromotionBill")
public class PromotionBill {
	private String id;
	private String ownerId;// 品牌商，管理方ID
	private String billDate;// 单据日期
	private String name;
	private String registerId;
	private Date registeTime;

	private String priceType;
	private String redSDate;
	private String redEDate;	
	private Double point;
	private Double realPrice;
	private String isAll;//全部门店
	private int qty;
	private String status;

	@Column(name = "isAll")
	public String getIsAll() {
		return isAll;
	}

	public void setIsAll(String isAll) {
		this.isAll = isAll;
	}

	@Column(name = "qty")
	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	@Id
	@Column(name = "id", nullable = false, length = 50)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "ownerId", nullable = false, length = 20)
	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	@Column(name = "billDate", nullable = false, length = 10)
	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	@Column(nullable = false, length = 50)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(nullable = false, length = 20)
	public String getRegisterId() {
		return registerId;
	}

	public void setRegisterId(String registerId) {
		this.registerId = registerId;
	}

	@Column(nullable = false)
	public Date getRegisteTime() {
		return registeTime;
	}

	public void setRegisteTime(Date registeTime) {
		this.registeTime = registeTime;
	}

	@Column(name = "point")
	public Double getPoint() {
		return point;
	}

	public void setPoint(Double point) {
		this.point = point;
	}

	@Column()
	public Double getRealPrice() {
		return realPrice;
	}

	public void setRealPrice(Double realPrice) {
		this.realPrice = realPrice;
	}

	@Column()
	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}	

	@Column()
	public String getRedSDate() {
		return redSDate;
	}

	public void setRedSDate(String redSDate) {
		this.redSDate = redSDate;
	}

	@Column()
	public String getRedEDate() {
		return redEDate;
	}

	public void setRedEDate(String redEDate) {
		this.redEDate = redEDate;
	}
	
	@Column(name ="remark")	
	private String remark;
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column(length = 2)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private List<PromotionBillDtl> listDtl;
	@Transient
	public List<PromotionBillDtl> getListDtl() {
		return listDtl;
	}

	public void setListDtl(List<PromotionBillDtl> listDtl) {
		this.listDtl = listDtl;
	}
	
	private List<PromotionRuler> listRuler;
	@Transient
	public List<PromotionRuler> getListRuler() {
		return listRuler;
	}

	public void setListRuler(List<PromotionRuler> listDtl) {
		this.listRuler = listDtl;
	}
    
	
}
