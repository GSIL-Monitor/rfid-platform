package com.casesoft.dmc.model.product;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Administrator on 2018/1/4.
 */
@Entity
@Table(name = "PRODUCT_NOPUSH_PRODUCT")
public class NoPushProduct extends BaseProduct{
    private String id;
    private String code;
    private String styleId;
    private String colorId;
    private String sizeId;
    private String remark;
    private String isSample;

    private String sizeSortId;
    private String image;

    private Long version;

    private Integer boxQty;//规格

    private List<Product> collocation;
    private List<String> images;
    private int isDeton;
    private String isUse;
    @Column()
    private String push;

    public String getPush() {
        return push;
    }

    public void setPush(String push) {
        this.push = push;
    }
    // Constructors


    @Column()
    public Integer getBoxQty() {
        return boxQty;
    }

    public void setBoxQty(Integer boxQty) {
        this.boxQty = boxQty;
    }

    @Column()
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }


    @Column(length = 50)
    public String getSizeSortId() {
        return sizeSortId;
    }

    public void setSizeSortId(String sizeSortId) {
        this.sizeSortId = sizeSortId;
    }

    // Property accessors
    @Id
    @Column(nullable = false, length = 50)
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(nullable = false, unique = true, length = 50)
    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(nullable = false, length = 20)
    public String getStyleId() {
        return this.styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    @Column(nullable = false, length = 15)
    public String getColorId() {
        return this.colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    @Column(nullable = false, length = 10)
    public String getSizeId() {
        return this.sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
    }

    @Column(length = 400)
    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(length = 500)
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Column(length=1)
    public String getIsSample() {
        return isSample;
    }

    public void setIsSample(String isSample) {
        this.isSample = isSample;
    }

    @Transient
    public List<Product> getCollocation() {
        return collocation;
    }

    public void setCollocation(List<Product> collocation) {
        this.collocation = collocation;
    }


    @Transient
    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Column()
    public int getIsDeton() {
        return isDeton;
    }

    public void setIsDeton(int isDeton) {
        this.isDeton = isDeton;
    }

    @Column(length = 2)
    public String getIsUse() {
        return isUse;
    }

    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }





}
