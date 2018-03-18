package com.casesoft.dmc.model.logistics;

/**
 * Created by Administrator on 2017/9/19 0019.
 */
public class SaleBybusinessname {
    private String busnissname;
    private String origname;
    private String precast;
    private String gross;
    private String  totactprice;

    public String getTotactprice() {
        return totactprice;
    }

    public void setTotactprice(String totactprice) {
        this.totactprice = totactprice;
    }

    public String getBusnissname() {
        return busnissname;
    }

    public void setBusnissname(String busnissname) {
        this.busnissname = busnissname;
    }

    public String getOrigname() {
        return origname;
    }

    public void setOrigname(String origname) {
        this.origname = origname;
    }

    public String getPrecast() {
        return precast;
    }

    public void setPrecast(String precast) {
        this.precast = precast;
    }

    public String getGross() {
        return gross;
    }

    public void setGross(String gross) {
        this.gross = gross;
    }

    public String getGrossprofits() {
        return grossprofits;
    }

    public void setGrossprofits(String grossprofits) {
        this.grossprofits = grossprofits;
    }

    private String grossprofits;

}
