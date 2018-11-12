package com.casesoft.dmc.model.tag;

import javax.persistence.*;

/**
 * InitDtlDao entity. @author
 */
@Entity
@Table(name = "TAG_INITDTL")
public class InitDtl implements java.io.Serializable {

    private static final long serialVersionUID = -3991016494021088428L;
    @Id
    @Column(nullable = false, length = 50)
    private String id;

    @Column(nullable = false, length = 50)
    private String billNo;

    @Column(nullable = false, length = 50)
    private String ownerId;

    @Column(nullable = false)
    private Integer status;

    @Column(nullable = false, length = 20)
    private String styleId;

    @Column(nullable = false, length = 20)
    private String colorId;

    @Column(nullable = false, length = 10)
    private String sizeId;

    @Column(nullable = false, length = 50)
    private String sku;

    @Column(nullable = false)
    private long qty;

    @Column()
    private long printQty;

    @Column(nullable = false)
    private long startNum;

    @Column(nullable = false)
    private long endNum;

    @Column()
    private Long detectQty;

    @Column()
    private Long receiveQty;

    @Transient
    private String styleName;

    @Transient
    private String colorName;

    @Transient
    private String sizeName;

    @Column()
    private Integer editStatus;


    public Long getDetectQty() {
        return detectQty;
    }

    public void setDetectQty(Long detectQty) {
        this.detectQty = detectQty;
    }


    public Long getReceiveQty() {
        return receiveQty;
    }

    public void setReceiveQty(Long receiveQty) {
        this.receiveQty = receiveQty;
    }

    /**
     * default constructor
     */
    public InitDtl() {
    }

    // Property accessors

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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


    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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


    public String getSku() {
        return this.sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }


    public long getQty() {
        return this.qty;
    }

    public void setQty(long qty) {
        this.qty = qty;
    }

    public long getPrintQty() {
        return printQty;
    }

    public void setPrintQty(long printQty) {
        this.printQty = printQty;
    }

    public long getStartNum() {
        return this.startNum;
    }

    public void setStartNum(long startNum) {
        this.startNum = startNum;
    }


    public long getEndNum() {
        return this.endNum;
    }

    public void setEndNum(long endNum) {
        this.endNum = endNum;
    }

    // //===================>


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

    public Integer getEditStatus() {
        return editStatus;
    }

    public void setEditStatus(Integer editStatus) {
        this.editStatus = editStatus;
    }


}