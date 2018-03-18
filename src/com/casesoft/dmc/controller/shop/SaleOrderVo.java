package com.casesoft.dmc.controller.shop;

/**
 * Created by WingLi on 2015-03-26.
 */
public class SaleOrderVo implements java.io.Serializable{
    private String saleDate;
    private String storeCode;//门店编号
    private String saleNoCode;// 销售单/退货单号
    private int saleQty;
    private double actSaleAmount;
    private double discountAmout;//差额
    private double supplyAmount;//?
    private double taxAmount;//附加税?
    private int reSaleQty;
    private String saleUserCode;
    private int actSaleQty;
    private double saleAmount;
    private double reacSaleAmount;
    private double realSaleAmount;
    private double looseAmount;
    private double cashAmount;
    private double crCardAmount;
    private double giftAmount;
    private double usedIntegral; //
    private double increaseIntegral;
    private String customerCode;
    private String mileageCode;
    private double mileageRate;

    public String getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getSaleNoCode() {
        return saleNoCode;
    }

    public void setSaleNoCode(String saleNoCode) {
        this.saleNoCode = saleNoCode;
    }

    public int getSaleQty() {
        return saleQty;
    }

    public void setSaleQty(int saleQty) {
        this.saleQty = saleQty;
    }

    public double getActSaleAmount() {
        return actSaleAmount;
    }

    public void setActSaleAmount(double actSaleAmount) {
        this.actSaleAmount = actSaleAmount;
    }

    public double getDiscountAmout() {
        return discountAmout;
    }

    public void setDiscountAmout(double discountAmout) {
        this.discountAmout = discountAmout;
    }

    public double getSupplyAmount() {
        return supplyAmount;
    }

    public void setSupplyAmount(double supplyAmount) {
        this.supplyAmount = supplyAmount;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public int getReSaleQty() {
        return reSaleQty;
    }

    public void setReSaleQty(int reSaleQty) {
        this.reSaleQty = reSaleQty;
    }

    public String getSaleUserCode() {
        return saleUserCode;
    }

    public void setSaleUserCode(String saleUserCode) {
        this.saleUserCode = saleUserCode;
    }

    public int getActSaleQty() {
        return actSaleQty;
    }

    public void setActSaleQty(int actSaleQty) {
        this.actSaleQty = actSaleQty;
    }

    public double getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(double saleAmount) {
        this.saleAmount = saleAmount;
    }

    public double getReacSaleAmount() {
        return reacSaleAmount;
    }

    public void setReacSaleAmount(double reacSaleAmount) {
        this.reacSaleAmount = reacSaleAmount;
    }

    public double getRealSaleAmount() {
        return realSaleAmount;
    }

    public void setRealSaleAmount(double realSaleAmount) {
        this.realSaleAmount = realSaleAmount;
    }

    public double getLooseAmount() {
        return looseAmount;
    }

    public void setLooseAmount(double looseAmount) {
        this.looseAmount = looseAmount;
    }

    public double getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(double cashAmount) {
        this.cashAmount = cashAmount;
    }

    public double getCrCardAmount() {
        return crCardAmount;
    }

    public void setCrCardAmount(double crCardAmount) {
        this.crCardAmount = crCardAmount;
    }

    public double getGiftAmount() {
        return giftAmount;
    }

    public void setGiftAmount(double giftAmount) {
        this.giftAmount = giftAmount;
    }

    public double getUsedIntegral() {
        return usedIntegral;
    }

    public void setUsedIntegral(double usedIntegral) {
        this.usedIntegral = usedIntegral;
    }

    public double getIncreaseIntegral() {
        return increaseIntegral;
    }

    public void setIncreaseIntegral(double increaseIntegral) {
        this.increaseIntegral = increaseIntegral;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getMileageCode() {
        return mileageCode;
    }

    public void setMileageCode(String mileageCode) {
        this.mileageCode = mileageCode;
    }

    public double getMileageRate() {
        return mileageRate;
    }

    public void setMileageRate(double mileageRate) {
        this.mileageRate = mileageRate;
    }
}
