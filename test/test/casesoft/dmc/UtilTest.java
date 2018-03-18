package test.casesoft.dmc;

import java.io.*;
import java.util.*;

import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.secret.EpcSecretUtil;
import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;

public class UtilTest extends TestCase {

    public void testResult() {
        Assert.assertTrue(1 == 1);
    }

    public void testEasyPoiImportExcel2003() throws Exception {
        String path = UtilTest.class.getResource("").getPath();
        File file = new File(path + "\\testEasyPoi.xls");
        ImportParams params = new ImportParams();
        params.setTitleRows(0);
        List<EasyPoiTestImportBill> list = ExcelImportUtil.importExcel(
                new FileInputStream(file), EasyPoiTestImportBill.class, params);
        Assert.assertTrue(list.size() > 1);
    }
    public void testEasyPoiImportExcel2007() throws Exception {
        String path = UtilTest.class.getResource("").getPath();
        File file = new File(path + "\\testEasyPoi.xlsx");
        ImportParams params = new ImportParams();
        params.setTitleRows(0);
        List<EasyPoiTestImportBill> list = ExcelImportUtil.importExcelBySax(
                new FileInputStream(file), EasyPoiTestImportBill.class, params);
        Assert.assertTrue(list.size() > 1);
    }

    public void testEasyPoiExportFile() throws IOException {
        List<EasyPoiTestExportBill> billList = new ArrayList<>();
        for(int i=0;i<20;i++) {
            EasyPoiTestExportBill bill = new EasyPoiTestExportBill();
            bill.setBillNo("billNo-"+i);
            bill.setBillDate(CommonUtil.addDay(new Date(),1,"yyyy-MM-dd"));
            bill.setBanDate(CommonUtil.addDay(new Date(),2,"yyyy-MM-dd"));
            bill.setQty(i);
            bill.setPrice(i*0.9);
            billList.add(bill);
        }
        ExportParams params = new ExportParams("EasyPoiExport 测试", "测试导出Excel", ExcelType.HSSF);
        //params.setStyle(ExcelExportStatisticStyler.class);
        Workbook workbook = ExcelExportUtil.exportExcel(params, EasyPoiTestExportBill.class, billList);
        String path = UtilTest.class.getResource("").getPath();
        FileOutputStream fos = new FileOutputStream(path+"\\testEasyPoiExport.xls");
        workbook.write(fos);
        fos.close();
    }

    public void testGlmTag() {
        String uniqueCode = "121101001B0050001";
        int length = uniqueCode.length();
        String seqNo = uniqueCode.substring(length-4,length);
        String sku = uniqueCode.substring(0,length-4);
        System.out.println(seqNo+"--"+sku);
    }

    public void testGeLaiMeiTag() {
        String secretEpc = "FCF6FEFFFF9EFFFFFFF";
        String epc = EpcSecretUtil.decodeEpc(secretEpc);
        System.out.println(epc);
    }

}
