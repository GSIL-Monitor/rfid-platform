package com.casesoft.dmc.model.product;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Administrator on 2018/1/12.
 */
@Entity
@Table(name = "PRODUCT_SEND_INVENTORY")
public class SendInventory {
    @Id
    @Column()
    private String id;
    @Column()
    private String styleId;
    @Column()
    private String colorId;
    @Column()
    private String sizeId;
    @Column()
    private Integer qty;
    @Column()
    private String Message;
    @Column()
    private String styleName;
    @Column()
    private String pushsuccess;
    @Column(nullable = false, length = 19)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date billDate;

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public String getPushsuccess() {
        return pushsuccess;
    }

    public void setPushsuccess(String pushsuccess) {
        this.pushsuccess = pushsuccess;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

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

    public String getSizeId() {
        return sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}
