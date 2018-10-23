package com.casesoft.dmc.model.sys;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * PricingRules entity.
 * @author liutinaci 2018/3/21
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "PRODUCT_PRICE")
public class PricingRules implements java.io.Serializable {

	private static final long serialVersionUID = -521893168086214431L;
	@Id
	@Column(nullable = false,length = 50)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name="uuid",strategy = "uuid")
	private String id; //规则ID
	@Column(length = 200)
	private String name;//定价规则名
	@Column()
	private double rule1;//规则1 表示吊牌价与采购价之间关系（double 保留两位小数）
	@Column()
	private double rule2;//规则2 门店与吊牌价直接关系（double 保留两位小数）
	@Column()
	private double rule3;//规则3 代理商价与吊牌价之间关系 （double 保留两位小数）
	@Column(length = 20)
	private String userId;//更新人
	@Column(length = 50,unique = true)
	private String series;//系列
	@Column(length = 50)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date upDateTime;//更新时间
	@Column(length = 1)
	private String state;//标记是否使用/ Y/N
	@Transient
	private String seriesName;//系列名
	@Column(length = 50)
	private String class3;//大类
	@Column(length = 50)
	private String class3Name;//大类名称double
	@Column()
	private double tagPrice=0D;//吊牌价
	@Column(length=2)
	private String priceRule;//价格规则 GT表示高于 LT表示低于 吊牌价
	@Column(length=1)
	private String isAllUse; //是否适用所有大类 Y表示是N表示否
	@Column(length=5)
	private String prefix;//款号前缀
	@Column(length=5)
	private String suffix;//款号后缀


	public String getClass3Name() {
		return class3Name;
	}

	public void setClass3Name(String class3Name) {
		this.class3Name = class3Name;
	}

	public String getClass3() {
		return class3;
	}

	public void setClass3(String class3) {
		this.class3 = class3;
	}

	public PricingRules() {
		super();
	}

	public PricingRules(String name, double rule1, double rule2, double rule3, String userId, String series, Date upDateTime, String state, String seriesName, String class3, String class3Name,
						Double tagPrice, String priceRule, String isAllUse, String prefix, String suffix) {
		this.name = name;
		this.rule1 = rule1;
		this.rule2 = rule2;
		this.rule3 = rule3;
		this.userId = userId;
		this.series = series;
		this.upDateTime = upDateTime;
		this.state = state;
		this.seriesName = seriesName;
		this.class3 = class3;
		this.class3Name = class3Name;
		this.tagPrice = tagPrice;
		this.priceRule = priceRule;
		this.isAllUse = isAllUse;
		this.prefix = prefix;
		this.suffix = suffix;
	}

	public PricingRules(String id, String name, double rule1, double rule2, double rule3, String userId, String series, Date upDateTime, String state, String seriesName) {
		this.id = id;
		this.name = name;
		this.rule1 = rule1;
		this.rule2 = rule2;
		this.rule3 = rule3;
		this.userId = userId;
		this.series = series;
		this.upDateTime = upDateTime;
		this.state = state;
		this.seriesName = seriesName;
	}
	public PricingRules(String name,double rule1, double rule2, double rule3,String series,String state){
		this.name = name;
		this.rule1 = rule1;
		this.rule2 = rule2;
		this.rule3 = rule3;
		this.series = series;
		this.state = state;
	}

	@Override
	public String toString() {

		return "PricingRules{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", rule1=" + rule1 +
				", rule2=" + rule2 +
				", rule3=" + rule3 +
				", userId='" + userId + '\'' +
				", series='" + series + '\'' +
				", upDateTime='" + upDateTime + '\'' +
				", state='" + state + '\'' +
				'}';
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getRule1() {
		return rule1;
	}

	public void setRule1(double rule1) {
		this.rule1 = rule1;
	}

	public double getRule2() {
		return rule2;
	}

	public void setRule2(double rule2) {
		this.rule2 = rule2;
	}

	public double getRule3() {
		return rule3;
	}

	public void setRule3(double rule3) {
		this.rule3 = rule3;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdateTime() {
		return upDateTime;
	}

	public void setUpdateTime(Date upDateTime) {
		this.upDateTime = upDateTime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}


	public String getSeriesName() {
		return seriesName;
	}

	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}

	public String getIsAllUse() {
		return isAllUse;
	}

	public void setIsAllUse(String isAllUse) {
		this.isAllUse = isAllUse;
	}

	public Date getUpDateTime() {
		return upDateTime;
	}

	public void setUpDateTime(Date upDateTime) {
		this.upDateTime = upDateTime;
	}

	public Double getTagPrice() {
		return tagPrice;
	}

	public void setTagPrice(Double tagPrice) {
		this.tagPrice = tagPrice;
	}

	public String getPriceRule() {
		return priceRule;
	}

	public void setPriceRule(String priceRule) {
		this.priceRule = priceRule;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
}