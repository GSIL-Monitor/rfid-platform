package com.casesoft.dmc.controller.shop;

/**
 * Created by WingLi on 2015-03-26.
 */
public class SaleOrderDtlVo implements java.io.Serializable {
    private String id;
    private String uniqueCode;//唯一码
    private String styleNo;
    private String colorNo;
    private String sizeNo;
    private String saleNo;
    private String storeCode;
    private String saleUserCode;
    private double tagPrice;
    private int saleQty;
    private String saleType;
    private String priceType;
    private double salePrice;
    private double actSaleAmount;
    private double discountAmount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getStyleNo() {
        return styleNo;
    }

    public void setStyleNo(String styleNo) {
        this.styleNo = styleNo;
    }

    public String getColorNo() {
        return colorNo;
    }

    public void setColorNo(String colorNo) {
        this.colorNo = colorNo;
    }

    public String getSizeNo() {
        return sizeNo;
    }

    public void setSizeNo(String sizeNo) {
        this.sizeNo = sizeNo;
    }

    public String getSaleNo() {
        return saleNo;
    }

    public void setSaleNo(String saleNo) {
        this.saleNo = saleNo;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getSaleUserCode() {
        return saleUserCode;
    }

    public void setSaleUserCode(String saleUserCode) {
        this.saleUserCode = saleUserCode;
    }

    public double getTagPrice() {
        return tagPrice;
    }

    public void setTagPrice(double tagPrice) {
        this.tagPrice = tagPrice;
    }

    public int getSaleQty() {
        return saleQty;
    }

    public void setSaleQty(int saleQty) {
        this.saleQty = saleQty;
    }

    public String getSaleType() {
        return saleType;
    }

    public void setSaleType(String saleType) {
        this.saleType = saleType;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public double getActSaleAmount() {
        return actSaleAmount;
    }

    public void setActSaleAmount(double actSaleAmount) {
        this.actSaleAmount = actSaleAmount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }
}
