package com.casesoft.dmc.model.logistics;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2017/9/29 0029.
 */
@Entity
@Table(name="statistics_day")
public class DayStatisticsInandOut {
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
    /*  @Column()
      private Integer outQty;
      @Column()
      private Integer inQty;
      @Column()
      private Integer transferOutQty;
      @Column()
      private Integer transferInQty;*/
    @Column()
    private Long stockQty;//库存量
    @Column()
    private Double stockPrice;//库存量价格

    public Double getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(Double stockPrice) {
        this.stockPrice = stockPrice;
    }

    @Column()

    private Double price;//吊牌价格
    @Column()
    private Double precast;
    @Column()
    private Double puprice;
    @Column()
    private Double wsprice;
    @Column()
    private Long monthStock;//月结库存

    public Long getMonthStock() {
        return monthStock;
    }

    public void setMonthStock(Long monthStock) {
        this.monthStock = monthStock;
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

    /*public Integer getOutQty() {
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
    }*/

    public Long getStockQty() {
        return stockQty;
    }

    public void setStockQty(Long stockQty) {
        this.stockQty = stockQty;
    }

    public Double getPrice() {
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
    }
    @Transient
    private String groupId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
