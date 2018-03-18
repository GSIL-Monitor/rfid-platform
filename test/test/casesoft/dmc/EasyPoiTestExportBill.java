package test.casesoft.dmc;

import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * Created by WinLi on 2017-03-24.
 */
public class EasyPoiTestExportBill {
    @Excel(name = "单号")
    private String billNo;
    @Excel(name = "单据日期", width = 15)
    private String billDate;
    @Excel(name = "办期", width = 20)
    private String banDate;
    @Excel(name = "数量", isStatistics = true)
    private double qty;
    @Excel(name="单价", isStatistics = true)
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
