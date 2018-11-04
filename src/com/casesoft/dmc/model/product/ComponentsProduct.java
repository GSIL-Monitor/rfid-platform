package com.casesoft.dmc.model.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by lly on 2018/11/3.
 */
@Entity
@Table(name = "PRODUCT_COMPONENTSPRODUCT")
public class ComponentsProduct {
    @Id
    @Column
    private String id;
    @Column
    private String styleId;//款号
    @Column
    private String componentsId;//成分code
    @Column
    private String componentsName;//成分名
    @Column
    private String cremark;//成分含量

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComponentsId() {
        return componentsId;
    }

    public void setComponentsId(String componentsId) {
        this.componentsId = componentsId;
    }


    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    public String getCremark() {
        return cremark;
    }

    public void setCremark(String cremark) {
        this.cremark = cremark;
    }

    public String getComponentsName() {
        return componentsName;
    }

    public void setComponentsName(String componentsName) {
        this.componentsName = componentsName;
    }
}
