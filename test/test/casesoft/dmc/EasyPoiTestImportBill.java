package test.casesoft.dmc;

import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by WinLi on 2017-03-24.
 */
public class EasyPoiTestImportBill {
    @Excel(name = "单号")
    private String billNo;
    @Excel(name = "单据日期")
    private String billDate;
    @Excel(name = "办期")
    private String banDate;
    @Excel(name = "数量")
    private double qty;
    @Excel(name="单价")
    private double price;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getBanDate() {
        return banDate;
    }

    public void setBanDate(String banDate) {
        this.banDate = banDate;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }
}
