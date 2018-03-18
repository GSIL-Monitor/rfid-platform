package com.casesoft.dmc.model.search;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "SEARCH_SALEWOWMOMVIEW")
public class SaleWoWMoMView extends BaseCountView{
	@Column()
	private Integer lastWeekDayQty;
	@Column()
	private Integer lastWeekQty;
	@Column()
	private Integer lastMonthQty;
	@Column()
	private Integer lastYearQty;
	@Column()
	private Integer lastYearWeekQty;
	@Column()
	private Integer lastYearMonthQty;
	@Transient
	private String mom;
	@Transient
	private String yoy;
	@Transient
	private String wow;
	@Transient
	private String wowh;
	@Transient
	private String momh;
	@Transient
	public String getWowh() {
		return wowh;
	}
	public void setWowh(String wowh) {
		this.wowh = wowh;
	}
	@Transient
	public String getMomh() {
		return momh;
	}
	public void setMomh(String momh) {
		this.momh = momh;
	}
	public Integer getLastWeekDayQty() {
		return lastWeekDayQty;
	}
	public void setLastWeekDayQty(Integer lastWeekDayQty) {
		this.lastWeekDayQty = lastWeekDayQty;
	}
	public Integer getLastWeekQty() {
		return lastWeekQty;
	}
	public void setLastWeekQty(Integer lastWeekQty) {
		this.lastWeekQty = lastWeekQty;
	}
	public Integer getLastMonthQty() {
		return lastMonthQty;
	}
	public void setLastMonthQty(Integer lastMonthQty) {
		this.lastMonthQty = lastMonthQty;
	}
	public Integer getLastYearQty() {
		return lastYearQty;
	}
	public void setLastYearQty(Integer lastYearQty) {
		this.lastYearQty = lastYearQty;
	}
	public Integer getLastYearWeekQty() {
		return lastYearWeekQty;
	}
	public void setLastYearWeekQty(Integer lastYearWeekQty) {
		this.lastYearWeekQty = lastYearWeekQty;
	}
	public Integer getLastYearMonthQty() {
		return lastYearMonthQty;
	}
	public void setLastYearMonthQty(Integer lastYearMonthQty) {
		this.lastYearMonthQty = lastYearMonthQty;
	}
	@Transient
	public String getMom() {
		return mom;
	}
	public void setMom(String mom) {
		this.mom = mom;
	}
	@Transient
	public String getYoy() {
		return yoy;
	}
	public void setYoy(String yoy) {
		this.yoy = yoy;
	}
	@Transient
	public String getWow() {
		return wow;
	}
	public void setWow(String wow) {
		this.wow = wow;
	}
	
}
