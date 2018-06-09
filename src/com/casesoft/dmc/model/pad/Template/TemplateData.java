package com.casesoft.dmc.model.pad.Template;

/**
 * 模板消息样式
 * Created ltc admin on 2018/6/9.
 */
public class TemplateData {
    private String value;
    private String color;
    public TemplateData(String value,String color){
        this.value = value;
        this.color = color;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
