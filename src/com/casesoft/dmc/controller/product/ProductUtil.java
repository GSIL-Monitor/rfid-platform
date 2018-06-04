package com.casesoft.dmc.controller.product;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.Constant.Session;
import com.casesoft.dmc.core.Constant.Sys;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.FileUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.core.vo.ITag;
import com.casesoft.dmc.core.vo.TagFactory;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.cfg.PropertyType;
import com.casesoft.dmc.model.product.*;
import com.casesoft.dmc.model.shop.Collocation;
import com.casesoft.dmc.model.tag.EpcBindBarcode;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.BusinessDtl;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.tools.ant.taskdefs.Zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.persistence.Cache;

public class ProductUtil {
    public final static int styleClassNum = 10;

    public static void initProductList(List<Product> list) {
        for (Product p : list) {
            p.setId(p.getCode());
            p.setUpdateTime(new Date().toString());
            p.setIsDeton(0);
        }
    }

    // 下载Excel
    public static File writeTaskExcelFile(List<Product> list) throws Exception {
        HSSFWorkbook taskExcelBook = new HSSFWorkbook();
        HSSFSheet sheet = taskExcelBook.createSheet("商品信息");
        initExcelWidth(sheet);
        HSSFRow row = null;
        row = sheet.createRow(0);

        initExcelCell(row);
        int count = 1;
        int sheetCount = 1;
        for (int i = 0; i < list.size(); i++) {
            if (count > 65536) {
                sheet = taskExcelBook.createSheet("商品信息" + sheetCount);
                initExcelWidth(sheet);
                initExcelCell(row);
                sheetCount++;
                count = 1;
            }
            row = sheet.createRow(count);
            initExcelData(row, list.get(i));
            count++;
        }
        File file = new File(CommonUtil.getDateString(new Date(),
                "yyyyMMddhhmmss") + "_product.xls");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        taskExcelBook.write(fos);
        fos.flush();
        fos.close();
        return file;
    }

    private static void initExcelWidth(HSSFSheet sheet) {
        sheet.setColumnWidth(0, 30 * 256);
        sheet.setColumnWidth(1, 20 * 256);
        sheet.setColumnWidth(2, 20 * 256);
        sheet.setColumnWidth(3, 10 * 256);
        sheet.setColumnWidth(6, 10 * 256);
        sheet.setColumnWidth(5, 10 * 256);
        sheet.setColumnWidth(6, 10 * 256);
        sheet.setColumnWidth(7, 10 * 256);
        sheet.setColumnWidth(8, 10 * 256);
        sheet.setColumnWidth(9, 100 * 256);
    }

    private static void initExcelWidth2(HSSFSheet sheet) {
        sheet.setColumnWidth(0, 30 * 256);
        sheet.setColumnWidth(1, 20 * 256);
        sheet.setColumnWidth(2, 20 * 256);
        sheet.setColumnWidth(3, 10 * 256);
        sheet.setColumnWidth(6, 10 * 256);
        sheet.setColumnWidth(5, 10 * 256);
        sheet.setColumnWidth(6, 10 * 256);
        sheet.setColumnWidth(7, 10 * 256);
        sheet.setColumnWidth(8, 20 * 256);
        sheet.setColumnWidth(9, 20 * 256);
        sheet.setColumnWidth(10, 10 * 256);
        sheet.setColumnWidth(11, 10 * 256);
        sheet.setColumnWidth(12, 10 * 256);
        sheet.setColumnWidth(13, 10 * 256);
        sheet.setColumnWidth(14, 10 * 256);
        sheet.setColumnWidth(15, 10 * 256);
        sheet.setColumnWidth(16, 10 * 256);
        sheet.setColumnWidth(17, 10 * 256);
        sheet.setColumnWidth(18, 10 * 256);
        sheet.setColumnWidth(19, 10 * 256);
        sheet.setColumnWidth(20, 100 * 256);

    }

    // 初始化商品信息
    private static void initExcelData(HSSFRow row, Product p) {
        HSSFCell cell = null;
        cell = row.createCell(0);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(p.getCode());

        cell = row.createCell(1);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(p.getStyleId());

        cell = row.createCell(2);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(CacheManager.getStyleNameById(p.getStyleId()));

        cell = row.createCell(3);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(p.getColorId());
        cell = row.createCell(4);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(CacheManager.getColorNameById(p.getColorId()));

        cell = row.createCell(5);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(p.getSizeId());
        cell = row.createCell(6);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        if (CommonUtil.isBlank(CacheManager.getSizeById(p.getSizeId()))) {
            cell.setCellValue(p.getSizeId());
        } else {
            cell.setCellValue(CacheManager.getSizeById(p.getSizeId())
                    .getSizeName());
        }
        cell = row.createCell(7);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(p.getUpdateTime());
        cell = row.createCell(8);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(p.getBrandCode());
        cell = row.createCell(9);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(p.getRemark());
    }

    // 初始化商品信息2
    private static void initExcelData2(HSSFRow row, Product p) {
        HSSFCell cell = null;
        cell = row.createCell(0);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(p.getCode());

        cell = row.createCell(1);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(p.getStyleId());

        cell = row.createCell(2);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(p.getStyleName());

        cell = row.createCell(3);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(p.getColorId());
        cell = row.createCell(4);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(p.getColorName());

        cell = row.createCell(5);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(p.getSizeId());
        cell = row.createCell(6);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(p.getSizeName());
        cell = row.createCell(7);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("" + p.getStyleSortName());
        cell = row.createCell(8);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("" + p.getBrandCode());
        cell = row.createCell(9);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("" + p.getClass1());
        cell = row.createCell(10);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("" + p.getClass2());
        cell = row.createCell(11);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("" + p.getClass3());
        cell = row.createCell(12);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("" + p.getClass4());
        cell = row.createCell(13);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("" + p.getClass5());
        cell = row.createCell(14);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("" + p.getClass6());
        cell = row.createCell(15);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("" + p.getClass7());
        cell = row.createCell(16);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("" + p.getClass8());
        cell = row.createCell(17);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("" + p.getClass9());
        cell = row.createCell(18);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("" + p.getClass10());
        cell = row.createCell(19);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("" + p.getRemark());

    }

    private static void initExcelCell(HSSFRow row) {
        HSSFCell cell = null;
        cell = row.createCell(0);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("SKU");

        cell = row.createCell(1);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("款号");

        cell = row.createCell(2);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("款名");

        cell = row.createCell(3);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("色码");
        cell = row.createCell(4);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("颜色名");

        cell = row.createCell(5);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("尺码");
        cell = row.createCell(6);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("尺寸名");
        cell = row.createCell(7);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("更新时间");
        cell = row.createCell(8);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("品牌信息");
        cell = row.createCell(9);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("备注");
    }

    private static void initExcelCell2(HSSFRow row,
                                       List<PropertyType> propertyTypeList) {
        HSSFCell cell = null;
        cell = row.createCell(0);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("SKU");

        cell = row.createCell(1);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("款号");

        cell = row.createCell(2);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("款名");

        cell = row.createCell(3);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("色码");
        cell = row.createCell(4);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("颜色名");

        cell = row.createCell(5);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("尺码");

        cell = row.createCell(6);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("尺寸名");

        cell = row.createCell(7);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("英文名");
        for (int i = 0, size = propertyTypeList.size(); i < size; i++) {
            if (i == size - 1) {
                cell = row.createCell(8);
            } else {
                cell = row.createCell(9 + i);
            }
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(propertyTypeList.get(i).getValue());

        }
        cell = row.createCell(19);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("备注");

    }

    public static File writeZipFile(List<Product> list,
                                    List<PropertyType> propertyTypeList) throws Exception {
        HSSFWorkbook taskExcelBook = new HSSFWorkbook();
        HSSFSheet sheet = taskExcelBook.createSheet("商品信息");
        initExcelWidth2(sheet);
        HSSFRow row = null;
        row = sheet.createRow(0);
        String sTime = CommonUtil.getDateString(new Date(), "yyyyMMddHHmmss");
        String path = Constant.Folder.Product_File_Folder + sTime + "\\";// 创建目录
        new File(path).mkdirs();
        initExcelCell2(row, propertyTypeList);
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            initExcelData2(row, list.get(i));
        }
        File file = new File(path
                + CommonUtil.getDateString(new Date(), "yyyyMMddhhmmss")
                + "_product.xls");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        taskExcelBook.write(fos);
        fos.flush();
        fos.close();
        String zipFileName = CommonUtil.getDateString(new Date(),
                "yyyyMMddhhmmss") + "_product.zip";
        FileUtil.zip(Constant.Folder.Product_File_Folder + sTime, zipFileName);
        return new File(zipFileName);

    }

    @Deprecated
    public static Product convertToVo(List<Object[]> objs) {
        if (objs == null || objs.size() <= 0)
            return null;
        Product p = new Product();
        Object[] os = objs.get(0);
        p.setCode(os[0].toString());
        p.setStyleId(os[1].toString());
        p.setColorId(os[2].toString());
        p.setSizeId(os[3].toString());
        if (CommonUtil.isNotBlank(os[5])) {
            p.setImage(os[5].toString());
        }

        Style s = CacheManager.getStyleById(p.getStyleId());
        if (s == null) {
            return null;
        }
        p.setStyleName(s.getStyleName());
        p.setPrice(s.getPrice());
        p.setColorName(CacheManager.getColorNameById(p.getColorId()));
        p.setSizeName(CacheManager.getSizeNameById(p.getSizeId()));
        if (CommonUtil.isNotBlank(os[4])) {
            p.setRemark(os[4].toString());
        }
        Product po = null;
        List<Product> pList = new ArrayList<Product>();
        for (Object[] o_s : objs) {
            boolean have = false;
            for (Product tempP : pList) {
                if (o_s[6].toString().equals(tempP.getCode())) {
                    have = true;
                    break;
                }
            }
            if (have)
                continue;

            po = new Product();
            po.setCode(o_s[6].toString());
            po.setStyleId(o_s[7].toString());
            po.setColorId(o_s[8].toString());
            po.setSizeId(o_s[9].toString());
            if (CommonUtil.isNotBlank(o_s[11])) {
                po.setImage(o_s[11].toString());

            }
            if (CommonUtil.isNotBlank(o_s[10])) {
                po.setRemark(o_s[10].toString());
            }

            s = CacheManager.getStyleById(po.getStyleId());
            po.setStyleName(s.getStyleName());
            po.setPrice(s.getPrice());
            po.setColorName(CacheManager.getColorNameById(po.getColorId()));
            po.setSizeName(CacheManager.getSizeNameById(po.getSizeId()));

            pList.add(po);
        }
        p.setCollocation(pList);
        return p;
    }

    public static List<Product> convertToListVo(List<Object[]> objs) {
        if (objs == null || objs.size() <= 0)
            return null;
        Map<String, List<Object[]>> productMap = new HashMap<String, List<Object[]>>();
        for (Object[] os : objs) {
            String code = os[0].toString();
            if (productMap.containsKey(code)) {
                List<Object[]> objList = productMap.get(code);
                objList.add(os);
            } else {
                List<Object[]> list = new ArrayList<Object[]>();
                list.add(os);
                productMap.put(code, list);
            }
        }

        List<Product> productList = new ArrayList<Product>();
        for (String code : productMap.keySet()) {
            List<Object[]> list = productMap.get(code);
            Product p = convertToVo(list);
            if (p != null)
                productList.add(p);
        }

        return productList;
    }

    public static List<String> setImagePath(Product p, String realyPath) {
        File[] images = FileUtil.filterFile(new File(realyPath + "images/sku"),
                p.getCode());
        List<String> paths = new ArrayList<String>();
        for (File file : images) {
            paths.add("/images/sku/" + file.getName());
        }
        p.setImages(paths);
        return paths;
    }

    /**
     * @param p
     * @param realyPath
     * @return
     */
    public static List<String> setImagePathByStyleId(Product p, String realyPath) {
        String parentPath = realyPath + "images" + File.separator + "style"
                + File.separator;
        File[] images = FileUtil.filterFile(
                new File(parentPath + p.getStyleId()), "jpg");
        if (images == null || images.length == 0)
            return null;
        List<String> paths = new ArrayList<String>();
        for (File file : images) {
            // if(file.getName().startsWith(p.getColorId())){
            paths.add("/images/style/" + p.getStyleId() + "/" + file.getName());
            // }

        }
        p.setImages(paths);
        if (!paths.isEmpty()) {
            p.setImage(paths.get(0));
        }
        return paths;
    }

    // john 添加
    public static List<String> setImagePath2(Product p, String realyPath) {
        File[] images = FileUtil.filterFile(
                new File(realyPath + "images/style"), p.getStyleId());
        List<String> paths = new ArrayList<String>();
        for (File file : images) {
            paths.add("/images/style/" + file.getName());
        }
        p.setImage("/images/style/" + p.getStyleId() + ".jpg");
        p.setImages(paths);
        return paths;
    }

    public static List<Product> convertToPageVo(List<Product> rows) {
        for (Product p : rows) {
            Style style = CacheManager.getStyleById(p.getStyleId());
            if (null != style) {
                p.setStyleSortName("" + style.getStyleEname());
                p.setStyleName("" + style.getStyleName());
                p.setPrice(style.getPrice());
                p.setStyleRemark("" + style.getRemark());
                p.setClass1("" + style.getClass1());
                p.setClass2("" + style.getClass2());
                p.setClass3("" + style.getClass3());
                p.setClass4("" + style.getClass4());
                p.setClass5("" + style.getClass5());
                p.setClass6("" + style.getClass6());
                p.setClass7("" + style.getClass7());
                p.setClass8("" + style.getClass8());
                p.setClass9("" + style.getClass9());
                p.setClass10("" + style.getClass10());
                p.setPreCast(style.getPreCast());
                p.setWsPrice(style.getWsPrice());
                p.setPuPrice(CommonUtil.isBlank(style.getPuPrice()) ? 0 : style.getPuPrice());
            }
            if (CommonUtil.isBlank(p.getSizeName())) {

                p.setSizeName(CacheManager.getSizeNameById(p.getSizeId()));
            }
            if (CommonUtil.isBlank(p.getColorName())) {
                p.setColorName(CacheManager.getColorNameById(p.getColorId()));
            }
        }
        return rows;
    }

    public static List<Product> convertToPageImagsVo(String root, List<Product> rows) {
        Map<String, String> content = new HashMap<>();
        List<Product> lists = new ArrayList<>();
        for (Product p : rows) {
            if (content.containsKey(p.getStyleId() + p.getColorId())) {
                continue;
            }
            content.put(p.getStyleId() + p.getColorId(), p.getStyleId() + p.getColorId());
            Style style = CacheManager.getStyleById(p.getStyleId());
            if (null != style) {
                p.setStyleSortName("" + style.getStyleEname());
                p.setStyleName("" + style.getStyleName());
                p.setPrice(style.getPrice());
                p.setStyleRemark("" + style.getRemark());
                p.setClass1("" + style.getClass1());
                p.setClass2("" + style.getClass2());
                p.setClass3("" + style.getClass3());
                p.setClass4("" + style.getClass4());
                p.setClass5("" + style.getClass5());
                p.setClass6("" + style.getClass6());
                p.setClass7("" + style.getClass7());
                p.setClass8("" + style.getClass8());
                p.setClass9("" + style.getClass9());
                p.setClass10("" + style.getClass10());
            }
            p.setImages(setProducImagePath(p.getStyleId(), p.getColorId(), root));
            p.setColorName(CacheManager.getColorNameById(p.getColorId()));
            p.setSizeName(CacheManager.getSizeNameById(p.getSizeId()));
            lists.add(p);
        }
        return lists;
    }

    public static List<String> setProducImagePath(String styleId, String colorId, String root) {
        List<String> list = new ArrayList<String>();
        if (CommonUtil.isNotBlank(styleId) && CommonUtil.isNotBlank(colorId)) {
            File file = new File(root + styleId + "/" + colorId);
            File[] imageFile = FileUtil.filterFile(file, "JPG");
            if (CommonUtil.isNotBlank(imageFile)) {
                for (File f : imageFile) {
                    list.add("/images/style/" + styleId + "/" + colorId + "/" + f.getName());
                }
            }
        }
        return list;
    }

    public static void convertListToVo(List<Product> list) {
        for (Product p : list) {
            if (CommonUtil.isBlank(p.getBrandCode())) {
                p.setBarcode("");
                p.setBrandName("");
            } else {
                PropertyKey propertyKey = CacheManager.getPropertyKey(p
                        .getBrandCode());
                p.setBrandName(CommonUtil.isBlank(propertyKey) ? ""
                        : propertyKey.getName());
            }
            if (CommonUtil.isBlank(p.getBarcode())) {
                p.setBarcode("");
            }
            if (CommonUtil.isBlank(p.getEan())) {
                p.setEan("");
            }
            if (CommonUtil.isBlank(p.getRemark())) {
                p.setRemark("");
            }
            Style style = CacheManager.getStyleById(p.getStyleId());
            if (CommonUtil.isNotBlank(style)) {
                p.setStyleSortName(CommonUtil.isBlank(style.getStyleEname()) ? ""
                        : style.getStyleEname());
                p.setStyleName(style.getStyleName());
                p.setPrice(style.getPrice());
                if (CommonUtil.isBlank(style.getPreCast())) {
                    p.setPreCast(0d);
                } else {
                    p.setPreCast(style.getPreCast());
                }
                p.setStyleRemark(CommonUtil.isBlank(style.getRemark()) ? null
                        : style.getRemark());
                CacheManager.getPropertyKeyMap();
                if (CommonUtil.isNotBlank(style.getClass1())) {
                    p.setClass1(style.getClass1());
                    PropertyKey propertyKey = CacheManager.getPropertyKey(style
                            .getClass1());
                    p.setClass1Name(CommonUtil.isBlank(propertyKey) ? ""
                            : propertyKey.getName());
                } else {
                    p.setClass1("");
                    p.setClass1Name("");
                }
                if (CommonUtil.isNotBlank(style.getClass2())) {
                    p.setClass2(style.getClass2());
                    PropertyKey propertyKey = CacheManager.getPropertyKey(style
                            .getClass2());
                    p.setClass1Name(CommonUtil.isBlank(propertyKey) ? ""
                            : propertyKey.getName());
                } else {
                    p.setClass2("");
                    p.setClass2Name("");
                }
                if (CommonUtil.isNotBlank(style.getClass3())) {
                    p.setClass3(style.getClass3());
                    PropertyKey propertyKey = CacheManager.getPropertyKey(style
                            .getClass3());
                    p.setClass1Name(CommonUtil.isBlank(propertyKey) ? ""
                            : propertyKey.getName());
                } else {
                    p.setClass3("");
                    p.setClass3Name("");
                }
                if (CommonUtil.isNotBlank(style.getClass4())) {
                    p.setClass4(style.getClass4());
                    PropertyKey propertyKey = CacheManager.getPropertyKey(style
                            .getClass4());
                    p.setClass1Name(CommonUtil.isBlank(propertyKey) ? ""
                            : propertyKey.getName());
                } else {
                    p.setClass4("");
                    p.setClass4Name("");
                }
                if (CommonUtil.isNotBlank(style.getClass5())) {
                    p.setClass5(style.getClass5());
                    PropertyKey propertyKey = CacheManager.getPropertyKey(style
                            .getClass5());
                    p.setClass1Name(CommonUtil.isBlank(propertyKey) ? ""
                            : propertyKey.getName());
                } else {
                    p.setClass5("");
                    p.setClass5Name("");
                }
                if (CommonUtil.isNotBlank(style.getClass6())) {
                    p.setClass6(style.getClass1());
                    PropertyKey propertyKey = CacheManager.getPropertyKey(style
                            .getClass6());
                    p.setClass1Name(CommonUtil.isBlank(propertyKey) ? ""
                            : propertyKey.getName());
                } else {
                    p.setClass6("");
                    p.setClass6Name("");
                }

                if (CommonUtil.isNotBlank(style.getClass7())) {
                    p.setClass7(style.getClass7());
                    PropertyKey propertyKey = CacheManager.getPropertyKey(style
                            .getClass7());
                    p.setClass1Name(CommonUtil.isBlank(propertyKey) ? ""
                            : propertyKey.getName());
                } else {
                    p.setClass7("");
                    p.setClass7Name("");
                }
                if (CommonUtil.isNotBlank(style.getClass8())) {
                    p.setClass8(style.getClass8());
                    PropertyKey propertyKey = CacheManager.getPropertyKey(style
                            .getClass8());
                    p.setClass1Name(CommonUtil.isBlank(propertyKey) ? ""
                            : propertyKey.getName());
                } else {
                    p.setClass8("");
                    p.setClass8Name("");
                }
                if (CommonUtil.isNotBlank(style.getClass9())) {
                    p.setClass9(style.getClass9());
                    PropertyKey propertyKey = CacheManager.getPropertyKey(style
                            .getClass9());
                    p.setClass1Name(CommonUtil.isBlank(propertyKey) ? ""
                            : propertyKey.getName());
                } else {
                    p.setClass9("");
                    p.setClass9Name("");
                }
                if (CommonUtil.isNotBlank(style.getClass10())) {
                    p.setClass10(style.getClass10());
                    PropertyKey propertyKey = CacheManager.getPropertyKey(style
                            .getClass10());
                    p.setClass1Name(CommonUtil.isBlank(propertyKey) ? ""
                            : propertyKey.getName());
                } else {
                    p.setClass10("");
                    p.setClass10Name("");
                }

            }
            p.setColorName(CacheManager.getColorNameById(p.getColorId()));
            p.setSizeName(CacheManager.getSizeNameById(p.getSizeId()));
            p.setColorName(CacheManager.getColorNameById(p.getColorId()));
            SizeSort ss = CacheManager.getSizeSortById(p.getSizeSortId());
            p.setSizeSortId(CommonUtil.isBlank(ss) ? "" : p.getSizeSortId());
            p.setSizeSortName(CommonUtil.isBlank(ss) ? "" : p.getSizeSortName());
        }

    }

    public static void deletePic(String imageid, String realyPath) {

        File path = new File(realyPath + imageid);

        if (path.exists()) {
            path.delete();
        }

    }

    public static List<Collocation> readCollocationFile(
            FileInputStream inputStream) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);// 创建对Excel工作薄文件的引用
        HSSFSheet sheet = workbook.getSheetAt(0);// 创建对工作表的引用，也可用workbook.getSheet("sheetName");

        int i = 1;// 从第二行开始读数据
        HSSFRow row = sheet.getRow(i);
        String cell0 = processData(row, 0), cell1 = processData(row, 1), cell2 = processData(
                row, 2), cell3 = processData(row, 3), cell4 = processData(row,
                4), cell5 = processData(row, 5);

        Map<String, Collocation> collMap = new HashMap<String, Collocation>();

        while (!CommonUtil.isBlank(row) && CommonUtil.isLetterOrNum(cell0)
                && CommonUtil.isLetterOrNum(cell1)
                && CommonUtil.isLetterOrNum(cell2)
                && CommonUtil.isLetterOrNum(cell3)
                && CommonUtil.isLetterOrNum(cell4)
                && CommonUtil.isLetterOrNum(cell5)) {

            // ITag tag = TagFactory.getTag(PropertyUtil.getValue("tag_name"));
            // tag.setStyleId(cell0);
            // tag.setColorId(cell1);
            // tag.setSizeId(cell2);

            String sku = cell0 + cell1 + cell2;// tag.getSku();// cell0 + cell1
            // + cell2;
            // tag.setStyleId(cell3);
            // tag.setColorId(cell4);
            // tag.setSizeId(cell5);
            String sku2 = cell3 + cell4 + cell5;// tag.getSku();// cell3 + cell4
            // + cell5;
            String id = sku + sku2;
            Collocation c = new Collocation();
            c.setId(id);
            c.setCode(sku);
            c.setStyleNo(cell0);
            c.setColorNo(cell1);
            c.setSizeNo(cell2);
            c.setCode2(sku2);
            c.setStyleNo2(cell3);
            c.setColorNo2(cell4);
            c.setSizeNo2(cell5);
            if (!collMap.containsKey(id)) {
                collMap.put(id, c);
            }

            i++;
            row = sheet.getRow(i);
            if (row != null)
                if (!CommonUtil.isBlank(row) && CommonUtil.isLetterOrNum(cell0)
                        && CommonUtil.isLetterOrNum(cell1)
                        && CommonUtil.isLetterOrNum(cell2)
                        && CommonUtil.isLetterOrNum(cell3)
                        && CommonUtil.isLetterOrNum(cell4)
                        && CommonUtil.isLetterOrNum(cell5)) {
                    cell0 = processData(row, 0);
                    cell1 = processData(row, 1);
                    cell2 = processData(row, 2);
                    cell3 = processData(row, 3);
                    cell4 = processData(row, 4);
                    cell5 = processData(row, 5);
                } else {
                    break;
                }
        }
        return new ArrayList<Collocation>(collMap.values());
    }

    private static String processData(HSSFRow row, int colIndex)
            throws Exception {
        String str = "";
        try {
            str = row.getCell(colIndex).getStringCellValue().trim();// 款
        } catch (Exception e) {
            HSSFCell cell = row.getCell(colIndex);
            if (cell == null)
                return "";
            int temp = (int) row.getCell(colIndex).getNumericCellValue();
            // str = ""+temp;//如果是颜色，尺码编码时，可能需要加0
            switch (colIndex) {
                case 0:
                    // str = CommonUtil.convertToStyle(temp);//wing 2014-04-01
                    str = TagFactory.getTag(PropertyUtil.getValue("tag_name"))
                            .convertToStyle(temp);
                    break;
                case 1:
                    // str = CommonUtil.convertToColor(temp);
                    str = TagFactory.getTag(PropertyUtil.getValue("tag_name"))
                            .convertToColor(temp);
                    break;
                case 2:
                    // str = CommonUtil.convertToSize(temp);
                    str = TagFactory.getTag(PropertyUtil.getValue("tag_name"))
                            .convertToSize(temp);
                    break;
                case 3:
                    // str = CommonUtil.convertToStyle(temp);
                    str = TagFactory.getTag(PropertyUtil.getValue("tag_name"))
                            .convertToStyle(temp);
                    break;
                case 4:
                    // str = CommonUtil.convertToColor(temp);
                    str = TagFactory.getTag(PropertyUtil.getValue("tag_name"))
                            .convertToColor(temp);
                    break;
                case 5:
                    // str = CommonUtil.convertToSize(temp);
                    str = TagFactory.getTag(PropertyUtil.getValue("tag_name"))
                            .convertToSize(temp);
                    break;
            }
        }
        return str;
    }

    // john
    public static List<StyleCollocation> readStyleCollocation(
            FileInputStream inputStream) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = workbook.getSheetAt(0);
        List<StyleCollocation> listStyleCollocation = new ArrayList<StyleCollocation>();
        Iterator<Row> iterator = sheet.iterator();
        int i = 0;
        String id;
        while (iterator.hasNext()) {
            Row row = iterator.next();
            if (i == 0) {
                i++;
                continue;
            }
            StyleCollocation styleCollocation = new StyleCollocation();
            String style1 = row.getCell(0).getStringCellValue().trim();
            String style2 = row.getCell(1).getStringCellValue().trim();
            if (CommonUtil.isBlank(CacheManager.getStyleById(style1))) {
                throw new Exception("第" + (i + 1) + "行" + "第1列款号不存在,请上传对应信息");
            }
            if (CommonUtil.isBlank(CacheManager.getStyleById(style2))) {
                throw new Exception("第" + (i + 1) + "行" + "第2列款号不存在,请上传对应信息");
            }
            styleCollocation.setStyle1(style1);
            styleCollocation.setStyle2(style2);
            id = styleCollocation.getStyle1() + styleCollocation.getStyle2();
            styleCollocation.setId(id);
            listStyleCollocation.add(styleCollocation);
            styleCollocation = new StyleCollocation();
            styleCollocation.setStyle1(style2);
            styleCollocation.setStyle2(style1);
            id = styleCollocation.getStyle1() + styleCollocation.getStyle2();
            styleCollocation.setId(id);
            listStyleCollocation.add(styleCollocation);
            i++;
        }
        return listStyleCollocation;
    }


    public static List<Product> readProductFile2(FileInputStream inputStream)
            throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = workbook.getSheetAt(0);
        List<Product> products = new ArrayList<Product>();
        Iterator<Row> iterator = sheet.iterator();
        ITag tag = TagFactory.getTag(PropertyUtil.getValue("tag_name"));
        String[] sizeInfo = tag.getSizeConfig();
        int count = sizeInfo.length;
        int i = 0;
        HSSFRow row = sheet.getRow(i);
        while (iterator.hasNext() && !CommonUtil.isBlank(row)) {
            iterator.next();
            if (i == 0) {
                i++;
                continue;
            }
            row = sheet.getRow(i);
            if (!CommonUtil.isBlank(row)) {
                String cell0 = processData(row, 0), cell1 = processData(row, 1);
                String cell3 = processData(row, count + 2);
                if (!CommonUtil.isBlank(cell0) && !CommonUtil.isBlank(cell1)
                        && !CommonUtil.isBlank(cell3))
                    for (int c = 0; c < count; c++) {
                        int qty = (int) row.getCell(c + 2)
                                .getNumericCellValue();
                        if (qty != 0) {
                            Product product = new Product();
                            String cell2 = sizeInfo[c];
                            // tag.setStyleId(cell0);
                            // tag.setColorId(cell1);
                            // tag.setSizeId(cell2);
                            // String sku = tag.getSku();
                            String sku = cell0 + cell1 + cell2;
                            product.setCode(sku);
                            product.setStyleId(cell0);
                            product.setColorId(cell1);
                            product.setSizeId(cell2);
                            product.setRemark(cell3);
                            product.setImage("/images/style/" + cell0 + ".jpg");
                            products.add(product);
                        }
                    }
            }
            i++;
        }
        return products;
    }

    public static List<Product> readProductFile(FileInputStream inputStream)
            throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);// 创建对Excel工作薄文件的引用
        HSSFSheet sheet = workbook.getSheetAt(0);// 创建对工作表的引用，也可用workbook.getSheet("sheetName");

        int i = 1;// 从第二行开始读数据
        HSSFRow row = sheet.getRow(i);
        String cell0 = processData(row, 0), cell1 = processData(row, 1), cell2 = processData(
                row, 2), cell3 = processData(row, 3);// cessData(row, 4), cell5
        // = processData(row,
        // 5);

        Map<String, Product> pMap = new HashMap<String, Product>();
        int index = CacheManager.getMaxProductId();
        while (!CommonUtil.isBlank(row) && CommonUtil.isLetterOrNum(cell0)
                && CommonUtil.isLetterOrNum(cell1)
                && CommonUtil.isLetterOrNum(cell2)) {

            // ITag tag = TagFactory.getTag(PropertyUtil.getValue("tag_name"));
            // tag.setStyleId(cell0);
            // tag.setColorId(cell1);
            // tag.setSizeId(cell2);
            String id = getNewProductId(index);
            String sku = cell0 + cell1 + cell2;// tag.getSku();//
            Product p = new Product();
            p.setId("" + id);
            p.setCode(sku);
            p.setStyleId(cell0);
            p.setColorId(cell1);
            p.setSizeId(cell2);
            p.setImage("/images/sku/" + sku + ".jpg");
            p.setRemark(cell3);
            if (!pMap.containsKey(sku)) {
                pMap.put(sku, p);
                i++;
            }

            i++;
            row = sheet.getRow(i);
            if (row != null)
                if (!CommonUtil.isBlank(row) && CommonUtil.isLetterOrNum(cell0)
                        && CommonUtil.isLetterOrNum(cell1)
                        && CommonUtil.isLetterOrNum(cell2)) {
                    cell0 = processData(row, 0);
                    cell1 = processData(row, 1);
                    cell2 = processData(row, 2);
                    cell3 = processData(row, 3);
                } else {
                    break;
                }
        }
        return new ArrayList<Product>(pMap.values());
    }

    public static String productSkuString(List<Product> pList) {
        StringBuffer sb = new StringBuffer();
        for (Product p : pList) {
            sb.append(",").append("'").append(p.getCode()).append("'");
        }
        return sb.substring(1);
    }

    public static ProductInfoList readxinshuangProductFile(
            FileInputStream inputStream) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);// 创建对Excel工作薄文件的引用
        HSSFSheet sheet = workbook.getSheetAt(0);// 创建对工作表的引用，也可用workbook.getSheet("sheetName");

        int i = 1;// 从第二行开始读数据
        HSSFRow row = sheet.getRow(i);
        String styleNo = row.getCell(0).getStringCellValue();
        String styleName = row.getCell(1).getStringCellValue();
        String colorNo = "";
        try {
            colorNo = row.getCell(2).getStringCellValue();
        } catch (java.lang.IllegalStateException e) {
            colorNo = "" + (int) (row.getCell(2).getNumericCellValue());
        }
        String colorName = row.getCell(3).getStringCellValue();
        String sizeNo = "";
        try {
            sizeNo = row.getCell(4).getStringCellValue();
        } catch (java.lang.IllegalStateException e) {
            sizeNo = "" + (int) (row.getCell(4).getNumericCellValue());
        }
        String sizeName = sizeNo;
        String sku = styleNo + colorNo + sizeNo;
        double price = 0d;
        Map<String, Product> pMap = new HashMap<String, Product>();
        Map<String, Style> styleMap = new HashMap<String, Style>();
        Map<String, Color> colorMap = new HashMap<String, Color>();
        Map<String, Size> sizeMap = new HashMap<String, Size>();
        int id = CacheManager.getMaxProductId();
        while (!CommonUtil.isBlank(row)
                && CommonUtil.isNotBlank(row.getCell(1).getStringCellValue())

                ) {
            /* ITag tag = TagFactory.getCurrentTag(); */
            // sizeId = tag.getSizeIdBySku(sku);

            // 检查是否保存Style
            if (!CacheManager.isHaveStyleNo(styleNo)) {
                produceNewStyle(styleMap, styleNo, styleName, price);
            }
            if (!CacheManager.isHaveColorNo(colorNo)) {
                produceNewColor(colorMap, colorNo, colorName);
            }
            if (CommonUtil.isNotBlank(sizeNo)
                    && !CacheManager.isHaveSizeNo(sizeNo)) {
                produceNewSize(sizeMap, sizeNo, sizeName);
            }

            Product p = new Product();
            p.setId("" + id);
            p.setCode(sku);
            p.setStyleId(styleNo);
            p.setColorId(colorNo);
            p.setSizeId(sizeNo);
            p.setStyleName(styleName);
            p.setColorName(colorName);
            p.setSizeName(sizeName);
            p.setImage("/images/sku/" + sku + ".jpg");

            if (pMap.containsKey(sku)) {
                throw new Exception("第" + (i + 1) + "行，重复商品");
            } else if (CommonUtil.isBlank(CacheManager.getProductByCode(sku))) {
                pMap.put(sku, p);
                id++;
            }
            i++;
            System.out.println("当前第" + i + "行");
            row = sheet.getRow(i);
            if (row != null)
                if (!CommonUtil.isBlank(row)
                        && CommonUtil.isNotBlank(row.getCell(1))) {
                    styleNo = row.getCell(0).getStringCellValue();
                    styleName = row.getCell(1).getStringCellValue();
                    try {
                        colorNo = row.getCell(2).getStringCellValue();
                    } catch (java.lang.IllegalStateException e) {
                        colorNo = ""
                                + (int) (row.getCell(2).getNumericCellValue());
                    }
                    colorName = row.getCell(1).getStringCellValue();
                    try {
                        sizeNo = row.getCell(4).getStringCellValue();
                    } catch (java.lang.IllegalStateException e) {
                        sizeNo = ""
                                + (int) (row.getCell(4).getNumericCellValue());
                    }
                    sizeName = sizeNo;
                    sku = styleNo + colorNo + sizeNo;
                    price = 0d;

                } else {
                    break;
                }
        }

        return new ProductInfoList(new ArrayList<Product>(pMap.values()),
                new ArrayList<Style>(styleMap.values()), new ArrayList<Color>(
                colorMap.values()), new ArrayList<Size>(
                sizeMap.values()));

    }

    /**
     * 读取临时文件epc映射文件
     */
    public static Map<String, EpcBindBarcode> readProductMapFile(FileInputStream inputStream)
            throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);// 创建对Excel工作薄文件的引用
        HSSFSheet sheet = workbook.getSheetAt(0);// 创建对工作表的引用，也可用workbook.getSheet("sheetName");

        int i = 1;// 从第二行开始读数据
        HSSFRow row = sheet.getRow(i);
        String epc = null;
        String sku = null;
        Product product = null;
        Map<String, EpcBindBarcode> tmps = new HashMap<>();
        try {
            epc = row.getCell(0).getStringCellValue().toUpperCase();
            sku = row.getCell(1).getStringCellValue().toUpperCase();
        } catch (java.lang.IllegalStateException e) {
            throw new Exception("第" + (i + 1) + "行，非文本类型");
        }

        while (!CommonUtil.isBlank(row)) {
            if (CommonUtil.isBlank(sku) || CommonUtil.isBlank(epc)) {
                throw new Exception("第" + (i + 1) + "行，无信息");
            }
            product = CacheManager.getProductByCode(sku);
            if (tmps.containsKey(epc)) {
                throw new Exception("第" + (i + 1) + "行，重复EPC");
            } else if (CommonUtil.isBlank(product)) {
                throw new Exception("第" + (i + 1) + "行，无对应SKU信息");
            }
            EpcBindBarcode p = new EpcBindBarcode();
            p.setEpc(epc);
            p.setCode(sku);
            tmps.put(epc, p);
            i++;
            System.out.println("当前第" + i + "行");
            row = sheet.getRow(i);
            if (row != null) {
                if (!CommonUtil.isBlank(row)) {
                    boolean is = false;
                    try {
                        epc = row.getCell(0).getStringCellValue().toUpperCase();
                        sku = row.getCell(1).getStringCellValue().toUpperCase();
                        is = true;
                    } catch (java.lang.IllegalStateException e) {
                        e.printStackTrace();
                    } finally {
                        if (!is) {
                            throw new Exception("第" + (i + 1) + "行，非文本类型");
                        }
                    }

                } else {
                    break;
                }
            }
        }
        return tmps;
    }

    public static ProductInfoList readProductNewFile(FileInputStream inputStream)
            throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);// 创建对Excel工作薄文件的引用
        HSSFSheet sheet = workbook.getSheetAt(0);// 创建对工作表的引用，也可用workbook.getSheet("sheetName");
        int count = 0;// 统计增加商品记录数
        int i = 1;// 从第二行开始读数据
        HSSFRow row = sheet.getRow(i);
        String styleNo = getStringFormCell(row.getCell(0));
        String styleName = getStringFormCell(row.getCell(1));
        String colorNo = getStringFormCell(row.getCell(2));
        String colorName = getStringFormCell(row.getCell(3));
        String sizeNo = getStringFormCell(row.getCell(4));
        String sizeName = getStringFormCell(row.getCell(5));
        String sizeSort = getStringFormCell(row.getCell(6));
        String sizeSortName = getStringFormCell(row.getCell(7));
        Double price;
        Double preCast;
        Double wsPrice;
        Double purPrice;
        String sku = styleNo + colorNo + sizeNo;
        try {
            String priceString = getStringFormCell(row.getCell(8));
            price = Double.parseDouble(priceString);
            String strPreCast = CommonUtil.isBlank(getStringFormCell(row.getCell(9))) ? "0" : getStringFormCell(row.getCell(9));
            if (CommonUtil.isBlank(strPreCast)) {
                preCast = 0.0;
            } else {
                preCast = Double.parseDouble(strPreCast);
            }
            String strWsPrice = CommonUtil.isBlank(getStringFormCell(row.getCell(10))) ? "0" : getStringFormCell(row.getCell(10));
            if (CommonUtil.isBlank(strWsPrice)) {
                wsPrice = 0.0;
            } else {
                wsPrice = Double.parseDouble(strWsPrice);
            }
            String strPurPrice = CommonUtil.isBlank(getStringFormCell(row.getCell(11))) ? "0" : getStringFormCell(row.getCell(11));
            if (CommonUtil.isBlank(strPurPrice)) {
                purPrice = 0.0;
            } else {
                purPrice = Double.parseDouble(strPurPrice);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("第" + (i + 1) + "行，I,J,K,L列格式应该为文本类型");
        }

        String code = getStringFormCell(row.getCell(12));
        String barcode = getStringFormCell(row.getCell(13));
        String ean = getStringFormCell(row.getCell(14));
        String epcPre = getStringFormCell(row.getCell(15));
        String styleEname = getStringFormCell(row.getCell(16));
        String remark = getStringFormCell(row.getCell(17));
        String deton = getStringFormCell(row.getCell(18));
        String brandCode = getStringFormCell(row.getCell(19));
        String brand = getStringFormCell(row.getCell(20));
        String rules = getStringFormCell(row.getCell(21));

        int isDeton = 0;
        Map<String, Product> pMap = new HashMap<String, Product>();
        Map<String, Style> styleMap = new HashMap<String, Style>();
        Map<String, Color> colorMap = new HashMap<String, Color>();
        Map<String, Size> sizeMap = new HashMap<String, Size>();
        Map<String, SizeSort> sizeSortMap = new HashMap<String, SizeSort>();
        Map<String, PropertyKey> propertyKeyMap = new HashMap<String, PropertyKey>();
        Map<String, PropertyType> propertyTypeMap = new HashMap<String, PropertyType>();
        newPropertyKey(propertyTypeMap, sheet);
        int index = CacheManager.getMaxProductId();
        String productId = getNewProductId(index);
        while (CommonUtil.isNotBlank(row)) {
            if (deton.equals("Y")) {
                isDeton = 1;
            } else {
                isDeton = 0;
            }
            if (CommonUtil.isBlank(styleNo)) {
                throw new Exception("第" + (i + 1) + "行，款号列为空请完善信息");
            }

            if (CommonUtil.isBlank(styleName)) {
                throw new Exception("第" + (i + 1) + "行，款名列为空请完善信息");
            }

            produceNewStyle(styleMap, styleNo, styleNo, isDeton, sizeSort,
                    styleName, styleEname, price, preCast, wsPrice, purPrice, brandCode,
                    brand,rules, row);
            if (CommonUtil.isBlank(colorNo)) {
                throw new Exception("第" + (i + 1) + "行，色号列为空请完善信息");
            }

            if (CommonUtil.isBlank(colorName)) {
                throw new Exception("第" + (i + 1) + "行，颜色列为空请完善信息");
            }

            produceNewColor(colorMap, colorNo, colorName);
            if (CommonUtil.isBlank(sizeNo)) {
                throw new Exception("第" + (i + 1) + "行，尺寸编码列为空请完善信息");
            }
            if (CommonUtil.isBlank(sizeName)) {
                throw new Exception("第" + (i + 1) + "行，尺寸名称列为空请完善信息");
            }

            if (CommonUtil.isBlank(sizeSort)) {
                throw new Exception("第" + (i + 1) + "行，尺寸分组编码列为空请完善信息");
            }
            if (CommonUtil.isBlank(sizeSortName)) {
                throw new Exception("第" + (i + 1) + "行，尺寸分组名列称为空请完善信息");
            }

            if (CommonUtil.isBlank(CacheManager.getSizeSortById(sizeSort))) {
                produceNewSizeSort(sizeSortMap, sizeSort, sizeSort,
                        sizeSortName);
            }
            produceNewSize(sizeMap, sizeSortMap, sizeNo, sizeName, sizeSort);
            newProperty(propertyKeyMap, row, i + 1);

            String pCode;
            if (CommonUtil.isBlank(code)) {
                pCode = sku;
            } else {
                pCode = code;
            }
            if (pMap.containsKey(pCode)) {
                throw new Exception("第" + (i + 1) + "行，商品信息重复");
            }
            Product p = CacheManager.getProductByCode(pCode);
            if (CommonUtil.isBlank(p)) {

                p = new Product();
                if (pMap.containsKey(pCode)) {
                    throw new Exception("第" + (i + 1) + "行，商品信息重复");
                }
                if (CommonUtil.isNotBlank(epcPre)) {
                    p.setId(epcPre);
                } else {
                    p.setId(productId);
                }
                p.setCode(pCode);
                p.setStyleId(styleNo);
                p.setStyleName(styleName);
                p.setColorId(colorNo);
                p.setColorName(colorName);
                p.setSizeId(sizeNo);
                p.setSizeName(sizeName);
                p.setBrandCode(brandCode);
                p.setPrice(price);
                p.setBarcode(barcode);
                p.setEan(ean);
                p.setIsDeton(isDeton);
                p.setImage("/images/sku/" + p.getCode() + ".jpg");
                p.setRemark(remark);
                pMap.put(pCode, p);
                //CacheManager.getProductMap().put(p.getCode(), p);
                index++;
                count++;
                productId = getNewProductId(index);
            } else {
                p.setCode(pCode);
                p.setStyleId(styleNo);
                p.setStyleName(styleName);
                p.setColorId(colorNo);
                p.setColorName(colorName);
                p.setSizeId(sizeNo);
                p.setSizeName(sizeName);
                p.setBrandCode(brandCode);
                p.setPrice(price);
                p.setBarcode(barcode);
                p.setEan(ean);
                p.setIsDeton(isDeton);
                p.setImage("/images/sku/" + p.getCode() + ".jpg");
                p.setRemark(remark);
                pMap.put(pCode, p);
                CacheManager.getProductMap().put(p.getCode(), p);
            }
            i++;
            System.out.println("当前第" + i + "行");
            row = sheet.getRow(i);
            if (CommonUtil.isNotBlank(row) && CommonUtil.isNotBlank(getStringFormCell(row.getCell(0)))) {
                styleNo = getStringFormCell(row.getCell(0));
                styleName = getStringFormCell(row.getCell(1));
                colorNo = getStringFormCell(row.getCell(2));
                colorName = getStringFormCell(row.getCell(3));
                sizeNo = getStringFormCell(row.getCell(4));
                sizeName = getStringFormCell(row.getCell(5));
                sizeSort = getStringFormCell(row.getCell(6));
                sizeSortName = getStringFormCell(row.getCell(7));
                sku = styleNo + colorNo + sizeNo;
                try {
                    price = Double.parseDouble(getStringFormCell(row.getCell(8)));
                    String strPreCast = CommonUtil.isBlank(getStringFormCell(row.getCell(9))) ? "0" : getStringFormCell(row.getCell(9));
                    if (CommonUtil.isBlank(strPreCast)) {
                        preCast = 0.0;
                    } else {
                        preCast = Double.parseDouble(strPreCast);
                    }
                    String strWsPrice = CommonUtil.isBlank(getStringFormCell(row.getCell(10))) ? "0" : getStringFormCell(row.getCell(10));
                    if (CommonUtil.isBlank(strWsPrice)) {
                        wsPrice = 0.0;
                    } else {
                        wsPrice = Double.parseDouble(strWsPrice);
                    }
                    String strPurPrice = CommonUtil.isBlank(getStringFormCell(row.getCell(11))) ? "0" : getStringFormCell(row.getCell(11));
                    if (CommonUtil.isBlank(strPurPrice)) {
                        purPrice = 0.0;
                    } else {
                        purPrice = Double.parseDouble(strPurPrice);
                    }
                } catch (java.lang.IllegalStateException e) {
                    throw new Exception("第" + (i + 1) + "行，I,J,K列格式应该为文本类型");
                }
                code = getStringFormCell(row.getCell(12));
                barcode = getStringFormCell(row.getCell(13));
                ean = getStringFormCell(row.getCell(14));
                epcPre = getStringFormCell(row.getCell(15));
                styleEname = getStringFormCell(row.getCell(16));
                remark = getStringFormCell(row.getCell(17));
                deton = getStringFormCell(row.getCell(18));
                brandCode = getStringFormCell(row.getCell(19));
                brand = getStringFormCell(row.getCell(20));

            } else {
                break;
            }

        }

        return new ProductInfoList(new ArrayList<Product>(pMap.values()),
                new ArrayList<Style>(styleMap.values()), new ArrayList<Color>(
                colorMap.values()), new ArrayList<Size>(
                sizeMap.values()), new ArrayList<SizeSort>(
                sizeSortMap.values()), new ArrayList<PropertyType>(
                propertyTypeMap.values()), new ArrayList<PropertyKey>(
                propertyKeyMap.values()), count, pMap.values().size()
                - count);
    }

    private static void newProperty(Map<String, PropertyKey> propertyKeyMap,
                                    HSSFRow row, Integer index) throws Exception {
        String brandCode = getStringFormCell(row.getCell(19));
        String brandName = getStringFormCell(row.getCell(20));
        if (CommonUtil.isNotBlank(brandCode)) {
            if (!propertyKeyMap.containsKey(brandCode)) {
                PropertyKey p = new PropertyKey();
                if (CommonUtil.isBlank(brandName)) {
                    throw new Exception("第" + (index) + "行，第T列为空请添加对应名称");
                }
                p.setCode(brandCode);
                p.setName(brandName);
                p.setLocked(0);
                p.setOwnerId("1");
                p.setRegisterDate(new Date());
                p.setRegisterId("admin");
                p.setYnuse("Y");
                p.setType("BD");
                p.setId(p.getType() + "-" + brandCode);
                propertyKeyMap.put(brandCode, p);
            }
        }

        for (int i = 0; i < styleClassNum + 1; i++) {
            String classCode = getStringFormCell(row.getCell(21 + i * 2));
            String className = getStringFormCell(row.getCell(22 + i * 2));
            if (CommonUtil.isBlank(classCode)) {
                continue;
            }
            if (!propertyKeyMap.containsKey(classCode)) {
                PropertyKey p = new PropertyKey();
                if (CommonUtil.isBlank(className)) {
                    throw new Exception("第" + (index) + "行，第" + (22 + i * 2)
                            + "列为空请添加对应名称");
                }
                p.setCode(classCode);
                p.setName(className);
                p.setLocked(0);
                p.setOwnerId("1");
                p.setRegisterDate(new Date());
                p.setRegisterId("admin");
                p.setYnuse("Y");
                p.setType("C" + (i + 1));
                p.setId(p.getType() + "-" + classCode);
                propertyKeyMap.put(classCode, p);
            }

        }
    }

    private static void newPropertyKey(
            Map<String, PropertyType> PropertyTypeMap, HSSFSheet sheet) {
        HSSFRow row = sheet.getRow(0);

        for (int i = 0; i < styleClassNum; i++) {
            String classType = getStringFormCell(row.getCell(21 + i * 2));
            if (CommonUtil.isNotBlank(classType)) {
                PropertyType p = new PropertyType("C" + (i + 1), "C" + (i + 1),
                        classType, "商品代码分类", "*");

                PropertyTypeMap.put(p.getKeyId(), p);
                CacheManager.getPropertyTypeMap().put(p.getKeyId(), p);

            }
        }

    }

    private static void produceNewSize(Map<String, Size> sizeMap,
                                       Map<String, SizeSort> sizeSortMap, String sizeNo, String sizeName,
                                       String sizeSort) {

        SizeSort ss = CacheManager.getSizeSortById(sizeSort);
        Integer seqNo;

        if (CommonUtil.isNotBlank(CacheManager.getSizeByNo(sizeNo))) {
            seqNo = CacheManager.getSizeByNo(sizeNo).getSeqNo();
        } else {
            seqNo = CommonUtil.isBlank(ss.getSizeList().size()) ? 1 : ss
                    .getSizeList().size() + 1;
        }
        Size s = new Size(sizeNo, sizeNo, sizeName, sizeSort, seqNo);
        if (!sizeMap.containsKey(sizeNo)) {
            ss.addSize(s);
            for (int i = 0; i < CacheManager.sizeSortList.size(); i++) {
                if (CacheManager.sizeSortList.get(i).getSortNo()
                        .equals(sizeSort)) {
                    CacheManager.sizeSortList.remove(i);
                    CacheManager.sizeSortList.add(i, ss);
                }
            }
        }
        sizeMap.put(sizeNo, s);
        CacheManager.getSizeMap().put(sizeNo, s);
    }

    private static void produceNewSizeSort(Map<String, SizeSort> sizeSortMap,
                                           String sizeSort, String sizeSort2, String sizeSortName) {

        SizeSort ss = new SizeSort(sizeSort, sizeSort, sizeSortName);
        sizeSortMap.put(sizeSort, ss);
        CacheManager.sizeSortList.add(ss);

    }

    private static void produceNewStyle(Map<String, Style> styleMap,
                                        String styleId, String styleNo, int isDeton, String sizeSort,
                                        String styleName, String styleEname, Double price, Double preCast,
                                        Double wsPrice, Double purPrice, String brandCode, String brand, String rules, HSSFRow row) {
        Style s = new Style(styleId, styleNo, isDeton, sizeSort, styleName,
                styleEname, price, preCast, wsPrice, purPrice, brandCode, brand,
                getStringFormCell(row.getCell(22)),
                getStringFormCell(row.getCell(24)),
                getStringFormCell(row.getCell(26)),
                getStringFormCell(row.getCell(28)),
                getStringFormCell(row.getCell(30)),
                getStringFormCell(row.getCell(32)),
                getStringFormCell(row.getCell(34)),
                getStringFormCell(row.getCell(36)),
                getStringFormCell(row.getCell(38)),
                getStringFormCell(row.getCell(40)));
        s.setRemark(getStringFormCell(row.getCell(17)));
        styleMap.put(styleNo, s);
        CacheManager.getStyleMap().put(styleNo, s); // TODO Auto-generated
        // method stub

    }

    private static String getStringFormCell(HSSFCell cell) {
        try {
            if (CommonUtil.isBlank(cell)) {
                return "";
            }
            return cell.getStringCellValue().trim();
        } catch (java.lang.IllegalStateException ex) {
            return String.valueOf(cell.getNumericCellValue()).trim();
        }
    }

    public static String getNewProductId(int index) {
        String format = "000000";
        String suf = String.valueOf(index);
        String productId = format.substring(0, format.length() - suf.length())
                + suf;
        return productId;
    }

    private static void produceNewSize(Map<String, Size> sizeMap,
                                       String sizeNo, String sizeName) {

        Size s = new Size(sizeNo, sizeNo, sizeName, sizeNo);
        sizeMap.put(sizeNo, s);
        CacheManager.getSizeMap().put(sizeNo, s);

    }

    private static void produceNewColor(Map<String, Color> colorMap,
                                        String colorNo, String colorName) {
        Color c = new Color(colorNo, colorNo, colorName);
        colorMap.put(colorNo, c);
        CacheManager.getColorMap().put(colorNo, c);
    }

    private static void produceNewStyle(Map<String, Style> styleMap,
                                        String styleNo, String styleName, double price) {
        Style s = new Style(styleNo, styleNo, styleName, price);
        styleMap.put(styleNo, s);
        CacheManager.getStyleMap().put(styleNo, s);
    }

    // john
    public static void convertToVo(Product product, List<Product> products) {
        List<Product> collocations = new ArrayList<Product>();

        for (Product p : products) {
            convertToVo(p);
            collocations.add(p);
        }
        convertToVo(product);
        product.setCollocation(collocations);
    }

    public static Product convertToVo(String sku, List<Product> products) {
        Product product = null;
        if (1 == products.size()) {
            product = products.get(0);
            convertToVo(product);
            return product;
        }
        for (Product p : products) {
            // if (null == product && !p.getCode().equals(sku))
            // continue;
            if (p.getCode().equals(sku)) {
                product = p;
                break;
            }
            // product = p;
        }
        List<Product> collocations = new ArrayList<Product>();
        for (Product p : products) {
            if (p.getCode().equals(sku))
                continue;
            convertToVo(p);
            collocations.add(p);
        }
        convertToVo(product);
        product.setCollocation(collocations);
        return product;
    }

    private static Product convertToVo(Product p) {
        if (p == null)
            return null;
        Style s = CacheManager.getStyleById(p.getStyleId());
        p.setStyleName(s.getStyleName());
        p.setPrice(s.getPrice());
        p.setColorName(CacheManager.getColorNameById(p.getColorId()));
        p.setSizeName(CacheManager.getSizeNameById(p.getSizeId()));
        return p;
    }

    public static List<Color> convertToColorVo(List<Product> productList) {
        List<Color> colorList = new ArrayList<Color>();
        for (Product p : productList) {
            boolean have = false;
            for (Color c : colorList) {
                if (p.getColorId().equals(c.getColorId())) {
                    have = true;
                    Size size = CacheManager.getSizeById(p.getSizeId());
                    String sizeList = c.getSizeList();
                    sizeList = sizeList + ","
                            + Constant.ScmConstant.Code.Size_Qty_Field_Prefix
                            + size.getSeqNo();
                    c.setSizeList(sizeList);
                    break;
                }
            }
            if (!have) {
                Color color = CacheManager.getColorById(p.getColorId());
                Size size = CacheManager.getSizeById(p.getSizeId());
                String sizeList = Constant.ScmConstant.Code.Size_Qty_Field_Prefix
                        + size.getSeqNo();
                color.setSizeList(sizeList);
                colorList.add(color);
            }
        }
        return colorList;
    }

    public static void readZipImageFile(File file, String rootPath)
            throws Exception {

        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(file));
        BufferedInputStream buffIn = new BufferedInputStream(zipIn);

        File folder = null;
        ZipEntry entry;
        while ((entry = zipIn.getNextEntry()) != null) {

            folder = new File(rootPath, entry.getName());
            if (!folder.getParentFile().exists()) {
                folder.getParentFile().mkdirs();
            }
            if (entry.isDirectory()) {
                String styleNo = entry.getName();
                if (!CacheManager.isHaveStyleNo(styleNo.replace("/", ""))) {
                    throw new Exception("款号" + styleNo.replace("/", "")
                            + "不存在请上传对应信息");
                }
                continue;
            } else {
                String imageName = entry.getName().split("/")[1];
                String colorNo = imageName.split("_")[0];
                if (!CacheManager.isHaveColorNo(colorNo)) {
                    throw new Exception("色号" + colorNo + "不存在请上传对应信息");
                }
            }

            FileOutputStream fileOut = new FileOutputStream(folder);
            BufferedOutputStream buffOut = new BufferedOutputStream(fileOut);
            int b;
            while ((b = buffIn.read()) != -1) {
                buffOut.write(b);
            }
            buffOut.close();
            fileOut.close();
            System.out.println(folder + "解压成功");

        }
        buffIn.close();
        zipIn.close();

    }

}
