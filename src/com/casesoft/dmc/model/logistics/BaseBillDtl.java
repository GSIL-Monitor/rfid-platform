package com.casesoft.dmc.model.logistics;

/**
 * Created by CaseSoft-Software on 2017-06-13.
 */

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.List;
import java.util.Map;

@MappedSuperclass
public abstract class BaseBillDtl {

    @Column(nullable = false, length = 50)
    protected String billId;
    @Column(nullable = false, length = 50)
    protected String billNo;
    @Column(length = 1)
    private Integer status;
    @Column(nullable = false, length = 50)
    protected String sku;
    @Column(nullable = false, length = 20)
    protected String styleId;
    @Column(nullable = false, length = 20)
    protected String colorId;
    @Column(nullable = false, length = 20)
    protected String sizeId;

    @Column(nullable = false)
    protected Long qty;
    @Column()
        protected Long actQty;
    @Column()
    protected Double price;
    @Column()
    protected Double actPrice;
    @Column()
    protected Double totPrice;
    @Column()
    protected Double totActPrice;
    @Column()
    protected Long initQty=0l;
    @Column()
    protected Long scanQty=0l;
    @Column()
    protected Long manualQty=0l;//本次
    @Column()
    protected Long preManualQty=0l;//累计
    @Column(length = 500)
    protected String remark;

    @Column(length = 20)
    protected String barcode;


    @Transient
    protected String styleName;
    @Transient
    protected String colorName;
    @Transient
    protected String sizeName;
    @Transient
    protected String uniqueCodes;
    @Column()
    protected Double discount;
    @Transient
    protected String imgUrl;
    //到貨數量
    @Column()
    protected Integer arrival;
    @Column()
    protected Integer abnormalStatus;//异常单状态(1.异常 0.非异常)


    @Transient
    protected String stylePriceMap;


    public Integer getArrival() {
        return arrival;
    }

    public void setArrival(Integer arrival) {
        this.arrival = arrival;
    }



    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }

    public Long getActQty() {
        return actQty;
    }

    public void setActQty(Long actQty) {
        this.actQty = actQty;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getActPrice() {
        return actPrice;
    }

    public void setActPrice(Double actPrice) {
        this.actPrice = actPrice;
    }

    public Long getInitQty() {
        return initQty;
    }

    public void setInitQty(Long initQty) {
        this.initQty = initQty;
    }

    public Long getScanQty() {
        return scanQty;
    }

    public void setScanQty(Long scanQty) {
        this.scanQty = scanQty;
    }

    public Long getManualQty() {
        return manualQty;
    }

    public void setManualQty(Long manualQty) {
        this.manualQty = manualQty;
    }

    public Long getPreManualQty() {
        return preManualQty;
    }

    public void setPreManualQty(Long preManualQty) {
        this.preManualQty = preManualQty;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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

    public Double getTotPrice() {
        return totPrice;
    }

    public void setTotPrice(Double totPrice) {
        this.totPrice = totPrice;
    }

    public Double getTotActPrice() {
        return totActPrice;
    }

    public void setTotActPrice(Double totActPrice) {
        this.totActPrice = totActPrice;
    }

    public String getUniqueCodes() {
        return uniqueCodes;
    }

    public void setUniqueCodes(String uniqueCodes) {
        this.uniqueCodes = uniqueCodes;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getStylePriceMap() {
        return stylePriceMap;
    }

    public void setStylePriceMap(String stylePriceMap) {
        this.stylePriceMap = stylePriceMap;
    }

    public Integer getAbnormalStatus() {
        return abnormalStatus;
    }

    public void setAbnormalStatus(Integer abnormalStatus) {
        this.abnormalStatus = abnormalStatus;
    }
}
