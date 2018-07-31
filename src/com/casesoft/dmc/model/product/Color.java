package com.casesoft.dmc.model.product;

import javax.persistence.*;

/**
 * Color entity. @author
 */
@Entity
@Table(name = "PRODUCT_COLOR")
public class Color extends BaseModel implements java.io.Serializable {

    private static final long serialVersionUID = 923562961060456349L;
    // @JSONField(serialize = false)
    private String id;
    private String colorId;
    private String colorName;
    private String hex;
    private String isUse;


    private String brandCode;

    @Column(length = 30)
    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }
    // Constructors

    /**
     * default constructor
     */
    public Color() {
    }

    public Color(String id, String colorId, String colorName) {
        super();
        this.id = id;
        this.colorId = colorId;
        this.colorName = colorName;
    }

    public Color(String id, String colorId, String colorName, String brandCode) {
        super();
        this.id = id;
        this.colorId = colorId;
        this.colorName = colorName;
        this.brandCode = brandCode;
    }

    /**
     * full constructor
     */
    public Color(String colorNo, String colorName) {
        this.colorId = colorNo;
        this.colorName = colorName;
    }

    // Property accessors
    @Id
    @Column(nullable = false, length = 15)
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(unique = true, nullable = false, length = 15)
    public String getColorId() {
        return this.colorId;
    }

    public void setColorId(String colorNo) {
        this.colorId = colorNo;
    }

    @Column(nullable = false, length = 45)
    public String getColorName() {
        return this.colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }


    private String sizeList;

    @Transient
    public String getSizeList() {
        return sizeList;
    }

    public void setSizeList(String sizeList) {
        this.sizeList = sizeList;
    }

    @Column(length = 30)
    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    @Column(length = 2)
    public String getIsUse() {
        return isUse;
    }

    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }
}