package com.casesoft.dmc.model.product;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SizeSort entity. @author
 */
@Entity
@Table(name = "PRODUCT_SIZESORT")
public class SizeSort extends BaseModel implements java.io.Serializable {

    private static final long serialVersionUID = -5247268673477187802L;
    // Fields

    private String id;
    private String sortNo;
    private String sortName;
    private Integer seqNo;
    private String sizeNames;
    private String remark;
    private String isUse;

    private String brandCode;
    private List<Size> sizeList = new ArrayList<Size>();

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
    public SizeSort() {
    }

    /**
     * minimal constructor
     */
    public SizeSort(String sortNo, String sortName) {
        this.sortNo = sortNo;
        this.sortName = sortName;
    }

    public SizeSort(String id, String sortNo, String sortName) {
        this.id = id;
        this.sortNo = sortNo;
        this.sortName = sortName;
    }

    /**
     * full constructor
     */
    public SizeSort(String sortNo, String sortName, Integer seqNo, String sizeNames, String remark) {
        this.sortNo = sortNo;
        this.sortName = sortName;
        this.seqNo = seqNo;
        this.sizeNames = sizeNames;
        this.remark = remark;
    }

    public SizeSort(String id, String sortNo, String sortName, Integer seqNo,
                    String sizeNames, String remark, String brandCode) {
        super();
        this.id = id;
        this.sortNo = sortNo;
        this.sortName = sortName;
        this.seqNo = seqNo;
        this.sizeNames = sizeNames;
        this.remark = remark;
        this.brandCode = brandCode;
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

    @Column(unique = true, nullable = false, length = 45)
    public String getSortNo() {
        return this.sortNo;
    }

    public void setSortNo(String sortNo) {
        this.sortNo = sortNo;
    }

    @Column(nullable = false, length = 45)
    public String getSortName() {
        return this.sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    @Column()
    public Integer getSeqNo() {
        return this.seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    @Column(length = 200)
    public String getSizeNames() {
        return this.sizeNames;
    }

    public void setSizeNames(String sizeNames) {
        this.sizeNames = sizeNames;
    }

    @Column(length = 400)
    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(length = 2)
    public String getIsUse() {
        return isUse;
    }

    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((sortNo == null) ? 0 : sortNo.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SizeSort other = (SizeSort) obj;
        if (sortNo == null) {
            if (other.sortNo != null)
                return false;
        } else if (!sortNo.equals(other.sortNo))
            return false;
        return true;
    }


    @Transient
    public List<Size> getSizeList() {
        return sizeList;
    }

    public void setSizeList(List<Size> sizeList) {
        this.sizeList = sizeList;
    }

    public void addSize(Size s) {
        this.sizeList.add(s);
    }
}