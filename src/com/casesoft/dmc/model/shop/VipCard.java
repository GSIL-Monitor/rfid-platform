package com.casesoft.dmc.model.shop;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 会员卡管理
 * Created by luis on 2018/6/20.
 */
@Entity
@Table(name = "shop_vipcard")
public class VipCard {
    @Id
    @Column(length = 1)
    private String id;
    @Column(length = 20)
    private String name; //会员卡名  白金会员，铂金会员，钻石会员等
    @Column(length = 1)
    private String rank; //会员卡等级 1 2 3 4
    @Column(length = 5)
    private Integer discount; //折扣
    @Column(length = 1)
    private int freeShipping; //是否包邮 1包邮 0不包邮
    @Column(length = 1)
    private int upgradeType; //升级类型， 1自动升级 0手动升级
    @Column(length = 5)
    private Long upgradeDealNo; //升级规则 成交数量
    @Column(length = 8)
    private Double upgradeConsumeNo;//升级规则 总消费金额
    @Column(length = 8)
    private Long upgradePoints;//升级规则 总累计积分

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    @Column(length = 20)

    private String createTime;//创建时间
    @Column(length = 100)
    private String remark;//备注

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public int getFreeShipping() {
        return freeShipping;
    }

    public void setFreeShipping(int freeShipping) {
        this.freeShipping = freeShipping;
    }

    public int getUpgradeType() {
        return upgradeType;
    }

    public void setUpgradeType(int upgradeType) {
        this.upgradeType = upgradeType;
    }

    public Long getUpgradeDealNo() {
        return upgradeDealNo;
    }

    public void setUpgradeDealNo(Long upgradeDealNo) {
        this.upgradeDealNo = upgradeDealNo;
    }

    public Double getUpgradeConsumeNo() {
        return upgradeConsumeNo;
    }

    public void setUpgradeConsumeNo(Double upgradeConsumeNo) {
        this.upgradeConsumeNo = upgradeConsumeNo;
    }

    public Long getUpgradePoints() {
        return upgradePoints;
    }

    public void setUpgradePoints(Long upgradePoints) {
        this.upgradePoints = upgradePoints;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
