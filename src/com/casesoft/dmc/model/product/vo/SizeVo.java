package com.casesoft.dmc.model.product.vo;

import java.io.Serializable;

/**
 * Created by boy on 2018/4/9.
 * 用于从数据库取出简单的尺寸数据，传递给前端显示
 */
public class SizeVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String sizeName;

    public SizeVo(){}

    public SizeVo(String id, String sizeName){
        this.id = id;
        this.sizeName = sizeName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }
}
