package com.casesoft.dmc.extend.chart;

/**
 * Created by WingLi on 2015-05-02.
 */
public class TaskQtyCount implements java.io.Serializable {
    private int year;
    private int month;
    private int day;
    private int totStyle;
    private int totSku;
    private int totEpc;
    private int token;
    public TaskQtyCount( int totStyle, int totSku, int totEpc,Object year, Object month, Object day,int token) {
        this.token = token;
        this.year = Integer.parseInt(year.toString());
        this.month = Integer.parseInt(month.toString());
        this.day = Integer.parseInt(day.toString());
        this.totStyle = totStyle;
        this.totSku = totSku;
        this.totEpc = totEpc;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getTotStyle() {
        return totStyle;
    }

    public void setTotStyle(int totStyle) {
        this.totStyle = totStyle;
    }

    public int getTotSku() {
        return totSku;
    }

    public void setTotSku(int totSku) {
        this.totSku = totSku;
    }

    public int getTotEpc() {
        return totEpc;
    }

    public void setTotEpc(int totEpc) {
        this.totEpc = totEpc;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }
}
