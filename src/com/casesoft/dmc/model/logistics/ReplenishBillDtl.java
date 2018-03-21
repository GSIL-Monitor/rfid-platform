package com.casesoft.dmc.model.logistics;

import javax.persistence.*;

/**
 * Created by Alvin on 2018/1/27.
 * 补货单明细
 */
@Entity
@Table(name = "LOGISTICS_ReplenishBillDtl")
public class ReplenishBillDtl extends BaseBillDtl{
    @Id
    @Column()
    private String id;
    @Column()
    private String replenishStatus;//单据状态
    @Column()
    private String  class1;//厂家

    public String getClass1() {
        return class1;
    }

    public void setClass1(String class1) {
        this.class1 = class1;
    }

    @Column()
    private String buyahandId;//买手的id
    @Column()
    private String styleName;//款名

    @Override
    public String getStyleName() {
        return styleName;
    }

    @Override
    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public String getBuyahandId() {
        return buyahandId;
    }

    public void setBuyahandId(String buyahandId) {
        this.buyahandId = buyahandId;
    }



    public String getReplenishStatus() {
        return replenishStatus;
    }

    public void setReplenishStatus(String replenishStatus) {
        this.replenishStatus = replenishStatus;
    }

    @Column()
    public String buyahandName;

    public String getBuyahandName() {
        return buyahandName;
    }

    public void setBuyahandName(String buyahandName) {
        this.buyahandName = buyahandName;
    }

    @Column()
    private Integer actConvertQty=0;//已转换数量

    @Column()
    private Integer convertQty=0;//本次转换数量

    public Integer getConvertquitQty() {
        return convertquitQty;
    }

    public void setConvertquitQty(Integer convertquitQty) {
        this.convertquitQty = convertquitQty;
    }

    @Column()

    private Integer convertquitQty=0;//本次撤销数量
    @Column()
    private Integer actConvertquitQty=0;//已撤销的数量

    public Integer getActConvertquitQty() {
        return actConvertquitQty;
    }

    public void setActConvertquitQty(Integer actConvertquitQty) {
        this.actConvertquitQty = actConvertquitQty;
    }

    @Column()
    private Long stockQty;//库存数量

    @Column()
    private Long franchiseeStockQty;//加盟店库存数量
    @Transient
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getActConvertQty() {
        return actConvertQty;
    }

    public void setActConvertQty(Integer actConvertQty) {
        this.actConvertQty = actConvertQty;
    }

    public Integer getConvertQty() {
        return convertQty;
    }

    public void setConvertQty(Integer convertQty) {
        this.convertQty = convertQty;
    }

    public Long getStockQty() {
        return stockQty;
    }

    public void setStockQty(Long stockQty) {
        this.stockQty = stockQty;
    }

    public Long getFranchiseeStockQty() {
        return franchiseeStockQty;
    }

    public void setFranchiseeStockQty(Long franchiseeStockQty) {
        this.franchiseeStockQty = franchiseeStockQty;
    }
}
