package com.casesoft.dmc.model.logistics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by czf on 2018/7/27.
 */
@Entity
@Table(name = "LOGISTICS_AbnormalCodeMessage")
public class AbnormalCodeMessage {
    @Id
    @Column()
    private String id;
    @Column()
    private String billNo;//单号
    @Column()
    private String sku;//sku
    @Column()
    private String styleId;//款号
    @Column()
    private String colorId;//颜色
    @Column()
    private String sizeId;//尺寸
    @Column()
    private String code;//唯一码
    @Column()
    private Integer type;//类型（出库 0，入库 1）
    @Column()
    private Integer status;//状态（0.删除，1.保存）

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
