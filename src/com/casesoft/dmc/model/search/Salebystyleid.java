package com.casesoft.dmc.model.search;

/**
 * Created by Administrator on 2017/12/22.
 */
public class Salebystyleid {
    private String id;
    private String styleid;
    private Integer stylenum;
    private String stylename;
    private Double price;
    private Double precast;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStyleid() {
        return styleid;
    }

    public void setStyleid(String styleid) {
        this.styleid = styleid;
    }

    public Integer getStylenum() {
        return stylenum;
    }

    public void setStylenum(Integer stylenum) {
        this.stylenum = stylenum;
    }

    public String getStylename() {
        return stylename;
    }

    public void setStylename(String stylename) {
        this.stylename = stylename;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPrecast() {
        return precast;
    }

    public void setPrecast(Double precast) {
        this.precast = precast;
    }
}
