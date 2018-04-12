package com.casesoft.dmc.model.product.vo;

import java.io.Serializable;

/**
 * Created by boy on 2018/4/9.
 * 用于从数据库取出简单的颜色数据，传递给前端显示
 */
public class ColorVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String colorName;

    public ColorVo(){}

    public ColorVo(String id, String colorName){
        this.id = id;
        this.colorName = colorName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }
}
