package com.casesoft.dmc.model.tag;

import javax.persistence.*;

/**
 * Epc entity. @author
 */
@Entity
@Table(name = "TAG_EPC")
public class Epc implements java.io.Serializable {

    private static final long serialVersionUID = 790925789806122627L;
    @Id
    @Column(nullable = false, length = 50)
    private String id;

    @Column(nullable = false, length = 50)
    private String billNo;

    @Column(nullable = false, length = 50)
    private String ownerId;

    @Column(unique = true, nullable = false, length = 50)
    private String code;

    @Column()
    private Integer status=0;//状态 0,标识未入库 1标识入库

    @Column(unique = true, nullable = false, length = 50)
    private String epc;

    @Column(unique = true, nullable = false, length = 100)
    private String dimension;

    @Column(nullable = false, length = 50)
    private String sku;

    @Column(nullable = false, length = 20)
    private String styleId;

    @Column(nullable = false, length = 20)
    private String colorId;

    @Column(nullable = false, length = 10)
    private String sizeId;

    @Column(unique = true, nullable = true, length = 100)
    private String tid;// 2014-10-23 TID号
    @Column()
    private Integer isDestruct;// 被毁坏0否1是2人工破坏


    @Column(nullable = true, length = 20)
    private String brandCode;

    @Transient
    private String styleName;

    @Transient
    private String colorName;

    @Transient
    private String sizeName;

    @Transient
    private Double preCast = 0d;//事前成本价(采购价)

    @Transient
    private Double price = 0d;//吊牌价格

    @Transient
    private Double puPrice = 0d;//代理商批发价格

    @Transient
    private Double wsPrice = 0D;//门店批发价格

    @Transient
    private String class6;//


    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }


    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }


    public Integer getIsDestruct() {
        return isDestruct;
    }

    public void setIsDestruct(Integer isDestruct) {
        this.isDestruct = isDestruct;
    }

    // Constructors


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * default constructor
     */
    public Epc() {
    }

    public Epc(String code, String billNo, String sku, String epc, String styleId, String colorId, String sizeId) {
        this.code = code;
        this.billNo = billNo;
        this.sku = sku;
        this.epc = epc;
        this.styleId = styleId;
        this.colorId = colorId;
        this.sizeId = sizeId;
    }


    public String getEpc() {
        return this.epc;
    }


    public String getBillNo() {
        return this.billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }


    public String getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }


    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setEpc(String epc) {
        this.epc = epc;
    }


    public String getDimension() {
        return this.dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }


    public String getSku() {
        return this.sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getStyleId() {
        return this.styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }


    public String getColorId() {
        return this.colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }


    public String getSizeId() {
        return this.sizeId;
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


    public Double getPreCast() {
        return preCast;
    }

    public void setPreCast(Double preCast) {
        this.preCast = preCast;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public Double getPuPrice() {
        return puPrice;
    }

    public void setPuPrice(Double puPrice) {
        this.puPrice = puPrice;
    }


    public Double getWsPrice() {
        return wsPrice;
    }

    public void setWsPrice(Double wsPrice) {
        this.wsPrice = wsPrice;
    }


    public String getClass6() {
        return class6;
    }

    public void setClass6(String class6) {
        this.class6 = class6;
    }

    @Override
    public boolean equals(Object obj) {
        Epc epc = (Epc)obj;
        return this.code.equals(epc.code);
    }
}