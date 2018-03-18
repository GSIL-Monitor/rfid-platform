package com.casesoft.dmc.extend.third.controller;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.cache.SpringContextUtil;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.extend.third.model.PlEmailTemplate;
import com.casesoft.dmc.extend.third.model.PlFittingAnalysisView;
import com.casesoft.dmc.extend.third.service.PlFittingAnalysisViewService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by john on 2017-03-07.
 */
public class PlEmailTemUtils {

    /**
     * @param plEmailTemplate
     * @return 生成邮件内容
     */
    public static String getEmailTempContent(PlEmailTemplate plEmailTemplate) {

        String[] shopCodes = null;
        String[] class1s = null;
        String[] class2s = null;
        String[] class3s = null;
        String[] class4s = null;
        String[] class10s = null;
        plEmailTemplate.getWarmLevel();
        String warm = "";
        switch (plEmailTemplate.getWarmLevel()) {
            case "1":
                warm = "I级";
                break;
            case "2":
                warm = "II级";
                break;
            case "3":
                warm = "III级";
                break;
            default:
                warm = "全部";
        }
        StringBuffer content = new StringBuffer("<center><table  style=\"font-family:宋体;color:black;font-size:32px;text-align:left\">");
        content.append("<tbody><tr><td>级别</td><td>").append(warm).append("</td></tr>");
        content.append("<tr><td>店铺</td><td>");
        if (CommonUtil.isNotBlank(plEmailTemplate.getShopCode())) {
            shopCodes = plEmailTemplate.getShopCode().split(",");
            for (String shopCode : shopCodes) {
                content.append("《").append(shopCode).append("》");
                if (CommonUtil.isNotBlank(CacheManager.getUnitByCode(shopCode))) {
                    content.append(CacheManager.getUnitByCode(shopCode).getName());
                }
                content.append(";");
            }
        } else {
            content.append("全部");
        }
        content.append("</td></tr><tr><td>品牌</td><td>");
        if (CommonUtil.isNotBlank(plEmailTemplate.getClass1())) {
            class1s = plEmailTemplate.getClass1().split(",");
            for (String class1 : class1s) {
                content.append("《").append(class1).append("》");
                if (CommonUtil.isNotBlank(CacheManager.getPropertyKey("C1-A-" + class1))) {
                    content.append(CacheManager.getPropertyKey("C1-A-" + class1).getName());
                }
                content.append(";");
            }
        } else {
            content.append("全部");
        }
        content.append("</td></tr><tr><td>季节</td><td>");
        if (CommonUtil.isNotBlank(plEmailTemplate.getClass2())) {
            class2s = plEmailTemplate.getClass2().split(",");
            for (String class2 : class2s) {
                content.append("《").append(class2).append("》");
                if (CommonUtil.isNotBlank(CacheManager.getPropertyKey("C2-B-" + class2))) {
                    content.append(CacheManager.getPropertyKey("C2-B-" + class2).getName());
                }
                content.append(";");
            }
        } else {
            content.append("全部");
        }
        content.append("</td></tr><tr><td>大类</td><td>");
        if (CommonUtil.isNotBlank(plEmailTemplate.getClass3())) {
            class3s = plEmailTemplate.getClass3().split(",");
            for (String class3 : class3s) {
                content.append("《").append(class3).append("》");
                if (CommonUtil.isNotBlank(CacheManager.getPropertyKey("C3-D-" + class3))) {
                    content.append(CacheManager.getPropertyKey("C3-D-" + class3).getName());
                }
                content.append(";");
            }
        } else {
            content.append("全部");
        }
        content.append("</td></tr><tr><td>小类</td><td>");
        if (CommonUtil.isNotBlank(plEmailTemplate.getClass4())) {
            class4s = plEmailTemplate.getClass4().split(",");
            for (String class4 : class4s) {
                content.append("《").append(class4).append("》");
                if (CommonUtil.isNotBlank(CacheManager.getPropertyKey("C4-E-" + class4))) {
                    content.append(CacheManager.getPropertyKey("C4-E-" + class4).getName());
                }
                content.append(";");
            }
        } else {
            content.append("全部");
        }
        content.append("</td></tr><tr><td>季节</td><td>");
        if (CommonUtil.isNotBlank(plEmailTemplate.getClass10())) {
            class10s = plEmailTemplate.getClass10().split(",");
            for (String class10 : class10s) {
                content.append("《").append(class10).append("》");
                if (CommonUtil.isNotBlank(CacheManager.getPropertyKey("C10-R-" + class10))) {
                    content.append(CacheManager.getPropertyKey("C10-R-" + class10).getName());
                }
                content.append(";");
            }
        } else {
            content.append("全部");
        }
        content.append("</td></tr></tbody></table></center>");
        return content.toString();
    }

    /**
     * 生成pl_email文件
     */
    public static String buildEmailFile(String rootPath, PlEmailTemplate plEmailTemplate) {
        PlFittingAnalysisViewService plFittingAnalysisViewService = (PlFittingAnalysisViewService) SpringContextUtil.getApplicationContext()
                .getBean("plFittingAnalysisViewService");
        Date sysDate = new Date();
        String begin = CommonUtil.getDateString(sysDate, "yyyy-MM-dd");
        try {
            begin = CommonUtil.reduceDay(begin, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        PropertyFilter filter = new PropertyFilter("EQS_stockDay", begin);
        filters.add(filter);

        switch (plEmailTemplate.getWarmLevel()) {
            case "1":
                filter = new PropertyFilter("GEI_lazyDays", "3");
                filters.add(filter);
                filter = new PropertyFilter("LEI_lazyDays", "7");
                filters.add(filter);
                break;
            case "2":
                filter = new PropertyFilter("GEI_lazyDays", "7");
                filters.add(filter);
                filter = new PropertyFilter("LEI_lazyDays", "14");
                filters.add(filter);
                break;
            case "3":
                filter = new PropertyFilter("GEI_lazyDays", "14");
                filters.add(filter);
                break;
            default:
                filter = new PropertyFilter("GEI_lazyDays", "3");
                filters.add(filter);
        }

        if (CommonUtil.isNotBlank(plEmailTemplate.getShopCode())) {
            filter = new PropertyFilter("INS_stockCode", plEmailTemplate.getShopCode());
            filters.add(filter);
        }

        if (CommonUtil.isNotBlank(plEmailTemplate.getClass1())) {
            filter = new PropertyFilter("INS_class1", plEmailTemplate.getClass1());
            filters.add(filter);
        }
        if (CommonUtil.isNotBlank(plEmailTemplate.getClass2())) {
            filter = new PropertyFilter("INS_class2", plEmailTemplate.getClass2());
            filters.add(filter);
        }
        if (CommonUtil.isNotBlank(plEmailTemplate.getClass3())) {
            filter = new PropertyFilter("INS_class3", plEmailTemplate.getClass3());
            filters.add(filter);

        }
        if (CommonUtil.isNotBlank(plEmailTemplate.getClass4())) {
            filter = new PropertyFilter("INS_class4", plEmailTemplate.getClass4());
            filters.add(filter);
        }
        if (CommonUtil.isNotBlank(plEmailTemplate.getClass10())) {
            filter = new PropertyFilter("INS_class10", plEmailTemplate.getClass10());
            filters.add(filter);
        }
        List<PlFittingAnalysisView> plFittingAnalysisViews = plFittingAnalysisViewService.find(filters);
        return createPLExcel(rootPath, plFittingAnalysisViews);
    }

    public static String createPLExcel(String rootPath, List<PlFittingAnalysisView> plFittingAnalysisViews) {
        try {
            HSSFWorkbook taskExcelBook = new HSSFWorkbook();
            HSSFSheet sheet = taskExcelBook.createSheet("试衣预警");
            HSSFRow row = null;
            row = sheet.createRow(0);
            initExcelTitle(row);
            int count = 1;
            int sheetCount = 1;
            if (CommonUtil.isNotBlank(plFittingAnalysisViews)) {
                for (int i = 0; i < plFittingAnalysisViews.size(); i++) {
                    if (count > 65536) {
                        sheet = taskExcelBook.createSheet("试衣预警" + sheetCount);
                        initExcelTitle(row);
                        sheetCount++;
                        count = 1;
                    }
                    row = sheet.createRow(count);
                    writeExcelData(taskExcelBook, row, plFittingAnalysisViews.get(i));
                    count++;
                }
            }
            String path = rootPath + "/email/" + new java.util.Random(100).nextInt() + "_" + CommonUtil.getDateString(new Date(),
                    "yyyyMMddhhmmss") + "_warm.xls";
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            taskExcelBook.write(fos);
            fos.flush();
            fos.close();
            return path;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * @param row 初始化Excel标题
     */
    private static void initExcelTitle(HSSFRow row) {
        HSSFCell cell = null;
        cell = row.createCell(0);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("库存日期");

        cell = row.createCell(1);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("入库日期");

        cell = row.createCell(2);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("未动天数");

        cell = row.createCell(3);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("等级");

        cell = row.createCell(4);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("款号");
        cell = row.createCell(5);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("款式");

        cell = row.createCell(6);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("色码");
        cell = row.createCell(7);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("颜色");

        cell = row.createCell(8);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("库存");

        cell = row.createCell(9);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("进店天数");
        cell = row.createCell(10);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("试衣量");
        cell = row.createCell(11);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("销售量");


        cell = row.createCell(12);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("最近销售日期");
        cell = row.createCell(13);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("最近试衣日期");

        cell = row.createCell(14);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("平均折扣");
        cell = row.createCell(15);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("销售金额");
        cell = row.createCell(16);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("库存金额");
        cell = row.createCell(17);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("月销售量");
        cell = row.createCell(18);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("月退货量");
        cell = row.createCell(19);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("月试衣量");
    }

    /**
     * @param row
     * @param plFittingAnalysisView 写入数据
     */
    private static void writeExcelData(HSSFWorkbook taskExcelBook, HSSFRow row, PlFittingAnalysisView plFittingAnalysisView) {
        HSSFCell cell = null;
        HSSFCellStyle style = taskExcelBook.createCellStyle();
        HSSFFont font = taskExcelBook.createFont();
        cell = row.createCell(3);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        if (plFittingAnalysisView.getLazyDays() >= 3 && plFittingAnalysisView.getLazyDays() < 7) {
            cell.setCellValue("I级");
            font.setColor(HSSFColor.GOLD.index);
            style.setFont(font);
            cell.setCellStyle(style);

        } else if (plFittingAnalysisView.getLazyDays() >= 7 && plFittingAnalysisView.getLazyDays() < 14) {
            cell.setCellValue("II级");
            font.setColor(HSSFColor.ROSE.index);
            style.setFont(font);
            cell.setCellStyle(style);

        } else if (plFittingAnalysisView.getLazyDays() >= 14) {
            cell.setCellValue("III级");
            font.setColor(HSSFColor.RED.index);
            style.setFont(font);
            cell.setCellStyle(style);
        }
        cell = row.createCell(0);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(plFittingAnalysisView.getStockDay());
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(plFittingAnalysisView.getInDate().toString());
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(plFittingAnalysisView.getLazyDays().toString());
        cell.setCellStyle(style);


        cell = row.createCell(4);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(plFittingAnalysisView.getStyleId());
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(plFittingAnalysisView.getStyleName());
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(plFittingAnalysisView.getColorId());
        cell.setCellStyle(style);

        cell = row.createCell(7);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(plFittingAnalysisView.getColorName());
        cell.setCellStyle(style);

        cell = row.createCell(8);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(plFittingAnalysisView.getStockQty().toString());
        cell.setCellStyle(style);

        cell = row.createCell(9);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellStyle(style);
        if (CommonUtil.isNotBlank(plFittingAnalysisView.getInDays())) {
            cell.setCellValue(plFittingAnalysisView.getInDays().toString());
        }
        cell = row.createCell(10);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(plFittingAnalysisView.getFittingQty().toString());
        cell.setCellStyle(style);

        cell = row.createCell(11);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(plFittingAnalysisView.getSaleQty().toString());
        cell.setCellStyle(style);

        cell = row.createCell(12);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(plFittingAnalysisView.getLastestSaleDate());
        cell.setCellStyle(style);


        cell = row.createCell(13);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(plFittingAnalysisView.getLastestFittingDate());
        cell.setCellStyle(style);

        cell = row.createCell(14);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(plFittingAnalysisView.getAvgPercent().toString());
        cell.setCellStyle(style);

        cell = row.createCell(15);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(plFittingAnalysisView.getActPrice().toString());
        cell.setCellStyle(style);

        cell = row.createCell(16);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(plFittingAnalysisView.getStockPrice().toString());
        cell.setCellStyle(style);

        cell = row.createCell(17);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(plFittingAnalysisView.getSaleMonthQty().toString());
        cell.setCellStyle(style);

        cell = row.createCell(18);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(plFittingAnalysisView.getBackMonthQty());
        cell.setCellStyle(style);
        cell = row.createCell(19);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(plFittingAnalysisView.getFittingMonthQty());
        cell.setCellStyle(style);

    }
}
