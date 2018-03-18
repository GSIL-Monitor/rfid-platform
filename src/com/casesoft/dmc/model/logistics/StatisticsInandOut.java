package com.casesoft.dmc.model.logistics;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by chenzhifan on 2017/7/8.
 */
@Entity
@Table(name="statistics_inbound_outbound")
public class StatisticsInandOut {
    @Id
    @Column()
    private String id;
    @Column()
    private String warehId;
    @Column()
    private Date timedate;
    @Column()
    private String timemonth;//月份
    @Column()
    private String sku;//
    @Column()
    private String styleId;
    @Column()
    private String colorId;
    @Column()
    private String sizeId;
    @Column()
    private String styleName;
    @Column()
    private String colorName;
    @Column()
    private String sizeName;
    @Column()
    private Integer outQty;
    @Column()
    private Integer inQty;
    @Column()
    private Integer transferOutQty;
    @Column()
    private Integer transferInQty;
    @Column()
    private Long stockQty;//库存量
    /*@Column()
    private Double price;//吊牌价格
    @Column()
    private Double precast;
    @Column()
    private Double puprice;
    @Column()
    private Double wsprice;*/
    @Column()
    private Double preval;
    @Column()
    private Integer token;
    @Column()
    private String billno;

    public String getBillno() {
        return billno;
    }

    public void setBillno(String billno) {
        this.billno = billno;
    }

    public Integer getToken() {
        return token;
    }

    public void setToken(Integer token) {
        this.token = token;
    }

    public Double getPreval() {
        return preval;
    }

    public void setPreval(Double preval) {
        this.preval = preval;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWarehId() {
        return warehId;
    }

    public void setWarehId(String warehId) {
        this.warehId = warehId;
    }

    public Date getTimedate() {
        return timedate;
    }

    public void setTimedate(Date timedate) {
        this.timedate = timedate;
    }

    public String getTimemonth() {
        return timemonth;
    }

    public void setTimemonth(String timemonth) {
        this.timemonth = timemonth;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
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

    public Integer getOutQty() {
        return outQty;
    }

    public void setOutQty(Integer outQty) {
        this.outQty = outQty;
    }

    public Integer getInQty() {
        return inQty;
    }

    public void setInQty(Integer inQty) {
        this.inQty = inQty;
    }

    public Integer getTransferOutQty() {
        return transferOutQty;
    }

    public void setTransferOutQty(Integer transferOutQty) {
        this.transferOutQty = transferOutQty;
    }

    public Integer getTransferInQty() {
        return transferInQty;
    }

    public void setTransferInQty(Integer transferInQty) {
        this.transferInQty = transferInQty;
    }

    public Long getStockQty() {
        return stockQty;
    }

    public void setStockQty(Long stockQty) {
        this.stockQty = stockQty;
    }

   /* public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPrecast() {
        return precast;
    }

    public void setPrecast(Double precast) {
        this.precast = precast;
    }

    public Double getPuprice() {
        return puprice;
    }

    public void setPuprice(Double puprice) {
        this.puprice = puprice;
    }

    public Double getWsprice() {
        return wsprice;
    }

    public void setWsprice(Double wsprice) {
        this.wsprice = wsprice;
    }*/
    @Transient
    private Long monthStock;//月结库存

    public Long getMonthStock() {
        return monthStock;
    }

    public void setMonthStock(Long monthStock) {
        this.monthStock = monthStock;
    }



    @Transient
    private String groupId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    @Transient
    private Double outprevalNum=0.0D;
    @Transient
    private Double inprevalNum=0.0D;
    @Transient
    private Double transferOutprevalNum=0.0D;
    @Transient
    private Double transferInprevalQtyNum=0.0D;
    @Transient
    private Double  outpreval=0.0D;
    @Transient
    private Double inpreval=0.0D;
    @Transient
    private Double transferOutpreval=0.0D;
    @Transient
    private String typeinout;

    public String getTypeinout() {
        return typeinout;
    }

    public void setTypeinout(String typeinout) {
        this.typeinout = typeinout;
    }

    public Double getOutprevalNum() {
        return outprevalNum;
    }

    public void setOutprevalNum(Double outprevalNum) {
        this.outprevalNum = outprevalNum;
    }

    public Double getInprevalNum() {
        return inprevalNum;
    }

    public void setInprevalNum(Double inprevalNum) {
        this.inprevalNum = inprevalNum;
    }

    public Double getTransferOutprevalNum() {
        return transferOutprevalNum;
    }

    public void setTransferOutprevalNum(Double transferOutprevalNum) {
        this.transferOutprevalNum = transferOutprevalNum;
    }

    public Double getTransferInprevalQtyNum() {
        return transferInprevalQtyNum;
    }

    public void setTransferInprevalQtyNum(Double transferInprevalQtyNum) {
        this.transferInprevalQtyNum = transferInprevalQtyNum;
    }

    public Double getOutpreval() {
        return outpreval;
    }

    public void setOutpreval(Double outpreval) {
        this.outpreval = outpreval;
    }

    public Double getInpreval() {
        return inpreval;
    }

    public void setInpreval(Double inpreval) {
        this.inpreval = inpreval;
    }

    public Double getTransferOutpreval() {
        return transferOutpreval;
    }

    public void setTransferOutpreval(Double transferOutpreval) {
        this.transferOutpreval = transferOutpreval;
    }

    public Double getTransferInpreval() {
        return transferInpreval;
    }

    public void setTransferInpreval(Double transferInpreval) {
        this.transferInpreval = transferInpreval;
    }

    @Transient

    private Double transferInpreval=0.0D;

    @Transient
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getTotpreval() {
        return totpreval;
    }

    public void setTotpreval(Double totpreval) {
        this.totpreval = totpreval;
    }

    @Transient

    private Double totpreval;
}
