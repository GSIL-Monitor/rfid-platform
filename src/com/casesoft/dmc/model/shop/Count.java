package com.casesoft.dmc.model.shop;

/**
 * Created by GuoJunwen on 2017/2/27 0027.
 */
public class Count {
    private String name;
    private Long value;

    public Count() {
    }

    public Count(String name, Long value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

}
