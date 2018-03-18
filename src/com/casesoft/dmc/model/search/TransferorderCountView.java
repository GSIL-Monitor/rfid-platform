package com.casesoft.dmc.model.search;

import com.alibaba.fastjson.annotation.JSONField;
import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2018/3/1.
 */
@Entity
@Table(name = "search_transferordercountviews")
public class TransferorderCountView {
    @Id
    @Column()
    private String id;
    @Column()
    @Excel(name = "单号", width = 20D)
    private String billNo;
    @JSONField(format="yyyy-MM-dd")
    @Column(nullable = false,length = 19)
    @Excel(name="日期", width = 25D)
    private Date billDate;
    @Column()
    private Integer status;
    @Column()
    private Integer outStatus;
    @Column()
    private String origUnitId;
    @Column()
    @Excel(name="发货方", width = 25D)
    private String origUnitName;
    @Column()
    private String origId;
    @Column()
    @Excel(name="出库仓库", width = 25D)
    private String origName;
    @Column()
    private String destUnitId;
    @Column()
    @Excel(name="收货方", width = 25D)
    private String destUnitName;
    @Column()
    private String destId;
    @Column()
    @Excel(name="入库仓库", width = 25D)
    private String destName;
    @Column()
    private String styleId;
    @Column()
    @Excel(name="款名", width = 25D)
    private String styleName;
    @Column()
    @Excel(name="成本价", width = 25D)
    private Double precast;
    @Column()
    @Excel(name="颜色", width = 25D)
    private String colorId;
    @Column()
    @Excel(name="尺号", width = 25D)
    private String sizeId;
    @Column()
    @Excel(name="sku", width = 25D)
    private String sku;
    @Column()
    @Excel(name="备注", width = 25D)
    private String remark;
    @Column()
    @Excel(name="数量", width = 25D)
    private Integer qty;
    @Transient
    @Excel(name="图片", width = 25D,type = 2)
    private String url;

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

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOutStatus() {
        return outStatus;
    }

    public void setOutStatus(Integer outStatus) {
        this.outStatus = outStatus;
    }

    public String getOrigUnitId() {
        return origUnitId;
    }

    public void setOrigUnitId(String origUnitId) {
        this.origUnitId = origUnitId;
    }

    public String getOrigUnitName() {
        return origUnitName;
    }

    public void setOrigUnitName(String origUnitName) {
        this.origUnitName = origUnitName;
    }

    public String getOrigId() {
        return origId;
    }

    public void setOrigId(String origId) {
        this.origId = origId;
    }

    public String getOrigName() {
        return origName;
    }

    public void setOrigName(String origName) {
        this.origName = origName;
    }

    public String getDestUnitId() {
        return destUnitId;
    }

    public void setDestUnitId(String destUnitId) {
        this.destUnitId = destUnitId;
    }

    public String getDestUnitName() {
        return destUnitName;
    }

    public void setDestUnitName(String destUnitName) {
        this.destUnitName = destUnitName;
    }

    public String getDestId() {
        return destId;
    }

    public void setDestId(String destId) {
        this.destId = destId;
    }

    public String getDestName() {
        return destName;
    }

    public void setDestName(String destName) {
        this.destName = destName;
    }

    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public Double getPrecast() {
        return precast;
    }

    public void setPrecast(Double precast) {
        this.precast = precast;
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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
