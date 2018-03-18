package com.casesoft.dmc.model.search;

/**
 * Created by Administrator on 2017/8/31.
 */
public class saleorderCount {
    private Integer sum;
    private Float allmony;
    private Integer rsum;
    private Float passall;
    private String grossprofits;
    private boolean isshow;

    public boolean isIsshow() {
        return isshow;
    }

    public void setIsshow(boolean isshow) {
        this.isshow = isshow;
    }

    public String getGrossprofits() {
        return grossprofits;
    }

    public void setGrossprofits(String grossprofits) {
        this.grossprofits = grossprofits;
    }

    public Float getPassall() {
        return passall;
    }

    public void setPassall(Float passall) {
        this.passall = passall;
    }

    public saleorderCount() {

    }

    public Integer getRsum() {
        return rsum;
    }

    public void setRsum(Integer rsum) {
        this.rsum = rsum;
    }

    public saleorderCount(Integer sum, Float allmony) {
        this.sum = sum;
        this.allmony = allmony;
    }

    public Float getAllmony() {
        return allmony;
    }

    public void setAllmony(Float allmony) {
        this.allmony = allmony;
    }

    public Integer getSum() {

        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }
}
