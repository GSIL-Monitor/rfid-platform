package com.casesoft.dmc.model.search;

import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * Created by Administrator on 2018/4/20.
 */
public class TransByOrig {
    @Excel(name="仓库")
    private String origname;
    @Excel(name="调拨数量")
    private String totqty;
    @Excel(name="调出调入")
    private String trantype;

    public String getOrigname() {
        return origname;
    }

    public void setOrigname(String origname) {
        this.origname = origname;
    }

    public String getTotqty() {
        return totqty;
    }

    public void setTotqty(String totqty) {
        this.totqty = totqty;
    }

    public String getTrantype() {
        return trantype;
    }

    public void setTrantype(String trantype) {
        this.trantype = trantype;
    }
}
