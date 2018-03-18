package com.casesoft.dmc.model.search;





import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Created by WingLi on 516-01-14.
 *  产品视图和明细查询视图中使用
 */

@MappedSuperclass
public class BaseProductView extends BaseStyle {

    @Column(length=200)
    protected String prodRemark;

    @Column(nullable = false, length = 10)
    @Excel(name = "颜色")
	protected String colorId;

    @Column(nullable = false, length = 10)
    @Excel(name = "尺寸")
	protected String sizeId;

    public String getSizeId() {
        return sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
    }

    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    public String getProdRemark() {
        return prodRemark;
    }

    public void setProdRemark(String prodRemark) {
        this.prodRemark = prodRemark;
    }


}
