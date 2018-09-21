package com.casesoft.dmc.model.search;

import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

public class BuyerKpi implements Serializable{
    @Excel(name = "id")
    private String id;
    @Excel(name = "买手号")
    private String buyerId;
    @Excel(name = "买手名")
    private String buyerName;
    @Excel(name = "采购数")
    private Long purchaseQty;
    @Excel(name = "采购入库数")
    private Long purchaseInQty;
    @Excel(name = "库存数")
    private Long stockQty;
    @Excel(name = "销售数")
    private Long saleQty;
    @Excel(name = "返厂数")
    private Long returnBackQty;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public Long getPurchaseQty() {
        return purchaseQty;
    }

    public void setPurchaseQty(Long purchaseQty) {
        this.purchaseQty = purchaseQty;
    }

    public Long getPurchaseInQty() {
        return purchaseInQty;
    }

    public void setPurchaseInQty(Long purchaseInQty) {
        this.purchaseInQty = purchaseInQty;
    }

    public Long getStockQty() {
        return stockQty;
    }

    public void setStockQty(Long stockQty) {
        this.stockQty = stockQty;
    }

    public Long getSaleQty() {
        return saleQty;
    }

    public void setSaleQty(Long saleQty) {
        this.saleQty = saleQty;
    }

    public Long getReturnBackQty() {
        return returnBackQty;
    }

    public void setReturnBackQty(Long returnBackQty) {
        this.returnBackQty = returnBackQty;
    }
}
