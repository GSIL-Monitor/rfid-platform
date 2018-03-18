package com.casesoft.dmc.extend.third.model.pl;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by GuoJunwen on 2017/4/6 0006.
 */
@Entity
@Table(name = "Pl_Shop_BindingRecord")
public class PlWmsShopBindingRecord implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "Id", nullable = false, length = 80)
    private String  id;
    @Column(name = "styleId", nullable = false, length = 30)
    private String styleId;
    @Column(name = "colorId", nullable = false, length = 20)
    private String colorId;
    @Column(name = "rackId", length = 32)
    private String rackId;//货架编号
    @Column(name = "rackBarcode", length = 32)
    private String rackBarcode;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updateDate",nullable = false)
    private Date updateDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getRackId() {
        return rackId;
    }

    public void setRackId(String rackId) {
        this.rackId = rackId;
    }

    public String getRackBarcode() {
        return rackBarcode;
    }

    public void setRackBarcode(String rackBarcode) {
        this.rackBarcode = rackBarcode;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
