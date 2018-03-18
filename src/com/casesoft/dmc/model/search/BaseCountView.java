package com.casesoft.dmc.model.search;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "SEARCH_BASECOUNTVIEW")
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class BaseCountView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -266204860751567547L;
	@Id
	@Column(nullable = false,length=80) 
	private String id;
	@Column(nullable = false, length = 50)
	private String warehId;

	@Column()
	private String year;

	@Column()
	private String quarter;
	@Column()
	private String month;

	@Column()
	private String week;

	@Column()
	private String day;

	@Column()
	private Integer dayQty;
	@Column()
	private Integer weekQty;
	@Column()
	private Integer quarterQty;
	@Column()
	private Integer monthQty;
	
	@Column()
	private Integer yearQty;
	@Column()
	private String lastWeekDay;
	@Column()
	private String lastMonthDay;
	@Column()
	private String lastYearDay;
	
	public Integer getMonthQty() {
		return monthQty;
	}
	public void setMonthQty(Integer monthQty) {
		this.monthQty = monthQty;
	}
	public String getLastWeekDay() {
		return lastWeekDay;
	}
	public void setLastWeekDay(String lastWeekDay) {
		this.lastWeekDay = lastWeekDay;
	}
	public String getLastMonthDay() {
		return lastMonthDay;
	}
	public void setLastMonthDay(String lastMonthDay) {
		this.lastMonthDay = lastMonthDay;
	}
	public String getLastYearDay() {
		return lastYearDay;
	}
	public void setLastYearDay(String lastYearDay) {
		this.lastYearDay = lastYearDay;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Transient
	private String warehName;
	@Transient
	public String getWarehName() {
		return warehName;
	}
	public void setWarehName(String warehName) {
		this.warehName = warehName;
	}
	public String getWarehId() {
		return warehId;
	}
	public void setWarehId(String warehId) {
		this.warehId = warehId;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getQuarter() {
		return quarter;
	}
	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public Integer getDayQty() {
		return dayQty;
	}
	public void setDayQty(Integer dayQty) {
		this.dayQty = dayQty;
	}
	public Integer getWeekQty() {
		return weekQty;
	}
	public void setWeekQty(Integer weekQty) {
		this.weekQty = weekQty;
	}
	public Integer getQuarterQty() {
		return quarterQty;
	}
	public void setQuarterQty(Integer quarterQty) {
		this.quarterQty = quarterQty;
	}
	public Integer getYearQty() {
		return yearQty;
	}
	public void setYearQty(Integer yearQty) {
		this.yearQty = yearQty;
	}

}
