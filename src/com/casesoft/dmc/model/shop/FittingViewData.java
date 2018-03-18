package com.casesoft.dmc.model.shop;

import java.util.List;

/**
 * 试衣统计数据
 * Created by GuoJunwen on 2017/2/28 0028.
 */
public class FittingViewData {
    private List<Count> countc3;
    private List<Count> countc10;
    private List<Count> sortShop;
    private List<Count> sortStyle;
    private List<Count> sumYear;
    private List<Count> sumMonth;
    private List<Count> sumDay;
    private List<Count> sumWeek;
    private List<Count> sortColor;

    public List<Count> getSortColor() {
        return sortColor;
    }

    public void setSortColor(List<Count> sortColor) {
        this.sortColor = sortColor;
    }

    public FittingViewData(List<Count> countc3, List<Count> countc10, List<Count> sortShop, List<Count> sortStyle, List<Count> sumYear, List<Count> sumMonth, List<Count> sumDay, List<Count> sumWeek, List<Count> sortColor) {
        this.countc3 = countc3;
        this.countc10 = countc10;
        this.sortShop = sortShop;
        this.sortStyle = sortStyle;
        this.sumYear = sumYear;
        this.sumMonth = sumMonth;
        this.sumDay = sumDay;
        this.sumWeek = sumWeek;
        this.sortColor = sortColor;
    }

    public FittingViewData() {
    }

    public FittingViewData(List<Count> countc3, List<Count> countc10, List<Count> sortShop, List<Count> sortStyle) {
        this.countc3 = countc3;
        this.countc10 = countc10;
        this.sortShop = sortShop;
        this.sortStyle = sortStyle;
    }



    public List<Count> getCountc3() {
        return countc3;
    }

    public void setCountc3(List<Count> countc3) {
        this.countc3 = countc3;
    }

    public List<Count> getCountc10() {
        return countc10;
    }

    public void setCountc10(List<Count> countc10) {
        this.countc10 = countc10;
    }

    public List<Count> getSortShop() {
        return sortShop;
    }

    public void setSortShop(List<Count> sortShop) {
        this.sortShop = sortShop;
    }

    public List<Count> getSortStyle() {
        return sortStyle;
    }

    public void setSortStyle(List<Count> sortStyle) {
        this.sortStyle = sortStyle;
    }

    public List<Count> getSumYear() {
        return sumYear;
    }

    public void setSumYear(List<Count> sumYear) {
        this.sumYear = sumYear;
    }

    public List<Count> getSumMonth() {
        return sumMonth;
    }

    public void setSumMonth(List<Count> sumMonth) {
        this.sumMonth = sumMonth;
    }

    public List<Count> getSumDay() {
        return sumDay;
    }

    public void setSumDay(List<Count> sumDay) {
        this.sumDay = sumDay;
    }

    public List<Count> getSumWeek() {
        return sumWeek;
    }

    public void setSumWeek(List<Count> sumWeek) {
        this.sumWeek = sumWeek;
    }
}
