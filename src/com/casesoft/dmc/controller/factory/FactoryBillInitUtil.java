package com.casesoft.dmc.controller.factory;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.FileUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.core.vo.ITag;
import com.casesoft.dmc.core.vo.TagFactory;
import com.casesoft.dmc.model.factory.*;
import com.casesoft.dmc.model.product.Color;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.product.Size;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.model.tag.Epc;
import com.casesoft.dmc.service.factory.FactoryInitService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.regex.Pattern;

public class FactoryBillInitUtil {
    public static List<InitEpc> epcList = null;


    private static String getStringFormCell(HSSFCell cell) {
        try {
            return cell.getStringCellValue().toString().trim();
        } catch (IllegalStateException ex) {
            return String.valueOf((int) cell.getNumericCellValue());
        }
    }

    public static String getNewProductId(int index) {
        String format = "000000";
        String suf = String.valueOf(index);
        if (suf.length() < format.length()) {
            String productId = format.substring(0, format.length() - suf.length())
                    + suf;
            return productId;
        } else {
            return suf;
        }

    }

    public static String intToStr(int value) {
        String format = "000000";
        String suf = String.valueOf(value);
        if (suf.length() < format.length()) {
            String str = format.substring(0, format.length() - suf.length())
                    + suf;
            return str;
        } else {
            return suf;
        }
    }


    public static void convertToVos(List<FactoryBillDtl> dtlList) {
        for (FactoryBillDtl dtl : dtlList) {
            dtl.setStyleName(CacheManager.getStyleNameById(dtl.getStyleId().toUpperCase()));
            dtl.setColorName(CacheManager.getColorNameById(dtl.getColorId().toUpperCase()));
        }

    }


    public static File writeTextFile(List<FactoryBillDtl> details,
                                     List<Epc> epcs, boolean isRfid) throws Exception {
        epcList = new ArrayList<InitEpc>();
        String taskId = details.get(0).getUploadNo();

        String sTime = CommonUtil.getDateString(new Date(), "yyyyMMddHHmmss");
        String path = Constant.Folder.Epc_Init_File_Folder + sTime + "\\";// 创建目录
        new File(path).mkdirs();
        for (FactoryBillDtl detail : details) {
            writeTextFile(detail, path, isRfid);
        }
        // 压缩文件夹下所有文件
        String zipFileName = Constant.Folder.Epc_Init_Zip_File_Folder + taskId
                + "_" + sTime + ".zip";
        FileUtil.zip(Constant.Folder.Epc_Init_File_Folder + sTime, zipFileName);
        return new File(zipFileName);
    }


    private static void writeTextFile(FactoryBillDtl detail, String fileFolder,
                                      boolean isRfid) throws Exception {
        String str = writeTextFile(detail, isRfid);
        String fileName = fileFolder + detail.getBillNo() + "_" + detail.getSku().replaceAll("_", "-") + "_" + detail.getQty()
                + "_" + CommonUtil.getDateString(new Date(), "yyyyMMdd")
                + ".txt";
        FileUtil.writeStringToFile(str, fileName);

    }


    private static String writeTextFile(FactoryBillDtl detail, boolean isRfid) throws Exception {
        String title = "";
        if (isRfid)
            title = "客户,办单号,办型,款式,款名,色码,布花,尺寸,尺码,吊牌码,芯片码\r\n";
        else
            title = "客户,办单号,办型,款式,款名,色码,布花,尺寸,尺码,吊牌码r\n";

        StringBuffer sb = new StringBuffer(title);
        int startNo = (int) detail.getStartNum();
        for (int i = 1; i <= detail.getQty(); i++) {

            String className = PropertyUtil.getValue("tag_name");
            ITag tag = TagFactory.getTag(className);
            tag.setStyleId(detail.getStyleId());
            tag.setColorId(detail.getColorId());
            tag.setSizeId(detail.getSizeId());
            tag.setSku(detail.getSku().toUpperCase());
            String uniqueCode = tag.getUniqueCode(startNo, i);
            String epc = tag.getEpc();
            String secretEpc = tag.getSecretEpc();


            sb.append(detail.getCustomerId());
            sb.append(",");
            sb.append(detail.getBillNo());
            sb.append(",");
            sb.append(detail.getType());
            sb.append(",");
            sb.append(detail.getStyleId());
            sb.append(",");
            sb.append(CacheManager.getStyleNameById(detail.getStyleId().toUpperCase()));
            sb.append(",");
            sb.append(detail.getColorId());
            sb.append(",");
            sb.append(CacheManager.getColorNameById(detail.getColorId().toUpperCase()));
            sb.append(",");
            sb.append(detail.getSizeId());
            sb.append(",");
            sb.append(CacheManager.getSizeNameById(detail.getSizeId().toUpperCase()));
            sb.append(",");

            // sb.append(convertToASCII(epc,0));
            if (isRfid) {
                sb.append(uniqueCode);
                sb.append(",");
                sb.append(secretEpc);
            } else
                sb.append(uniqueCode);


            sb.append("\r\n");
            InitEpc epcObj = initEpcObj(detail, uniqueCode, secretEpc);
            epcList.add(epcObj);

        }
        return sb.toString();
    }

    private static InitEpc initEpcObj(FactoryBillDtl dtl, String uniqueCode, String epc) {
        InitEpc epcObj = new InitEpc();

        epcObj.setCode(uniqueCode);// 唯一码
        epcObj.setEpc(epc);
        epcObj.setSku(dtl.getSku());
        epcObj.setBillDate(dtl.getBillDate());
        epcObj.setEndDate(dtl.getEndDate());
        epcObj.setOwnerId(dtl.getOwnerId());
        epcObj.setStyleId(dtl.getStyleId());
        epcObj.setColorId(dtl.getColorId());
        epcObj.setSizeId(dtl.getSizeId());
        epcObj.setBillNo(dtl.getBillNo());
        epcObj.setUploadNo(dtl.getUploadNo());
        epcObj.setType(dtl.getType());

        return epcObj;
    }


    public static File writeTextFile(List<FactoryBillDtl> details,
                                     boolean isRfid) throws Exception {
        epcList = new ArrayList<InitEpc>();
        String taskId = details.get(0).getUploadNo();

        String sTime = CommonUtil.getDateString(new Date(), "yyyyMMddHHmmss");
        String path = Constant.Folder.Epc_Init_File_Folder + sTime + "\\";// 创建目录
        new File(path).mkdirs();
        String title = "";
        if (isRfid)
            title = "客户,办单号,办型,款式,款名,色码,布花,尺寸,尺码,吊牌码,芯片码 \r\n";
        else
            title = "客户,办单号,办型,款式,款名,色码,布花,尺寸,尺码,吊牌码\r\n";

        StringBuffer sb = new StringBuffer(title);

        for (FactoryBillDtl detail : details) {
            String str = writeTextFile3(detail, isRfid);
            sb.append(str);
        }
        String fileName = path + taskId + "_" + sTime + ".txt";
        FileUtil.writeStringToFile(sb.toString(), fileName);
        // 压缩文件夹下所有文件
        String zipFileName = Constant.Folder.Epc_Init_Zip_File_Folder + taskId
                + "_" + sTime + ".zip";
        FileUtil.zip(Constant.Folder.Epc_Init_File_Folder + sTime, zipFileName);
        return new File(zipFileName);
    }


    private static String writeTextFile3(FactoryBillDtl detail, boolean isRfid) throws Exception {
        StringBuffer sb = new StringBuffer();
        int startNo = (int) detail.getStartNum();
        for (int i = 1; i <= detail.getQty(); i++) {
            String className = PropertyUtil.getValue("tag_name");
            ITag tag = TagFactory.getTag(className);
            tag.setStyleId(detail.getStyleId());
            tag.setColorId(detail.getColorId());
            tag.setSizeId(detail.getSizeId());
            tag.setSku(detail.getSku());
            String uniqueCode = tag.getUniqueCode(startNo, i);
            String secretEpc = tag.getSecretEpc();

            sb.append(detail.getCustomerId());
            sb.append(",");
            sb.append(detail.getBillNo());
            sb.append(",");
            sb.append(detail.getType());
            sb.append(",");
            sb.append(detail.getStyleId());
            sb.append(",");
            sb.append(CacheManager.getStyleNameById(detail.getStyleId().toUpperCase()));
            sb.append(",");
            sb.append(detail.getColorId());
            sb.append(",");
            sb.append(CacheManager.getColorNameById(detail.getColorId().toUpperCase()));
            sb.append(",");
            sb.append(detail.getSizeId());
            sb.append(",");
            sb.append(CacheManager.getSizeNameById(detail.getSizeId().toUpperCase()));
            sb.append(",");

            // sb.append(convertToASCII(epc,0));
            if (isRfid) {
                sb.append(uniqueCode);
                sb.append(",");
                sb.append(secretEpc);
            } else
                sb.append(uniqueCode);


            sb.append("\r\n");

            InitEpc epcObj = initEpcObj(detail, uniqueCode, secretEpc);
            epcList.add(epcObj);

        }
        return sb.toString();
    }


    public static void covertToEcplList(List<InitEpc> initEpcList) {
        for (InitEpc e : initEpcList) {
            e.setStyleName(CacheManager.getStyleNameById(e.getStyleId().toUpperCase()));
            e.setColorName(CacheManager.getColorNameById(e.getColorId().toUpperCase()));
            e.setSizeName(CacheManager.getSizeNameById(e.getSizeId().toUpperCase()));
        }
    }


    public static FactoryBillInfoList processNewExcel(
            FileInputStream inputStream, FactoryInitService epcService,
            User user, FactoryInit master) throws Exception {

        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);// 创建对Excel工作薄文件的引用
        HSSFSheet sheet = workbook.getSheetAt(0);// 创建对工作表的引用，也可用workbook.getSheet("sheetName");
        Pattern FilePattern = FactoryConstant.illegCharacter.FilePattern;
        int i = 1;// 从第二行开始读数据
        HSSFRow row = sheet.getRow(i);
        String billNo = FilePattern.matcher(getStringFormCell(row.getCell(3))).replaceAll("-");
        String styleId = FilePattern.matcher(getStringFormCell(row.getCell(4))).replaceAll("-");
        String colorId = FilePattern.matcher(getStringFormCell(row.getCell(9))).replaceAll("-");
        String sizeId = FilePattern.matcher(getStringFormCell(row.getCell(11))).replaceAll("-");
        String category = getStringFormCell(row.getCell(16));
        String factory = getStringFormCell(row.getCell(17));
        String season = getStringFormCell(row.getCell(18));

        Date billDate = row.getCell(14).getDateCellValue();
        Date endDate = row.getCell(15).getDateCellValue();
        long totQty = 0;
        int qty = 0;
        qty = (int) row.getCell(12).getNumericCellValue();// 数量
        // String makeOrderNo = row.getCell(4).getStringCellValue();//制单号
        Map<String, FactoryInit> factoryInitMap = new HashMap<String, FactoryInit>();
        Map<String, FactoryBill> initMap = new HashMap<String, FactoryBill>();
        Map<String, FactoryBillDtl> initDtlMap = new HashMap<String, FactoryBillDtl>();
        Map<String, Product> productMap = new HashMap<String, Product>();
        Map<String, Style> styleMap = new HashMap<String, Style>();
        Map<String, Color> colorMap = new HashMap<String, Color>();
        Map<String, Size> sizeMap = new HashMap<String, Size>();
        Map<String, Integer> skuCountMap = new HashMap<String, Integer>();
        Map<String, Long> skuBillCountMap = new HashMap<String, Long>();
        Map<String,FactoryCategory> categoryMap = new HashMap<String, FactoryCategory>();
        /*int categorySize = CacheManager.getFactoryCategory().size();*/
        int index = CacheManager.getMaxProductId();
        int dtlIndex = 0;
        String productId = getNewProductId(index);
        while (CommonUtil.isNotBlank(row)
                && (CommonUtil.isNotBlank(billNo)
                && CommonUtil.isNotBlank(styleId)
                && CommonUtil.isNotBlank(colorId)
                && CommonUtil.isNotBlank(sizeId)
                && CommonUtil.isLetterOrNum("" + qty))) {
            String sku = styleId + colorId + sizeId;
            if (CommonUtil.isNotBlank(epcService.get("billNo", billNo))) {
                throw new Exception("第" + (i + 1) + "行办单已录入,请删除后重新上传");
            }
            if (initDtlMap.containsKey(billNo + sku))
                throw new Exception("第" + (i + 1) + "行数据数据重复,请删除后重新上传");

            if (qty != 0) {// qty=0，sku不参与打印
                totQty += qty;
                FactoryBillDtl initDtl = new FactoryBillDtl();
                initDtl.setBillNo(billNo);
                initDtl.setStyleId(styleId);
                initDtl.setCategory(category);
                initDtl.setFactory(factory);
                initDtl.setSeason(season);

                if (CommonUtil.isBlank(CacheManager.getProductByCode(sku))) {
                    //productMap增加数据
                    if (!productMap.containsKey(sku)) {
                        index++;
                        Product p = new Product(sku, styleId, colorId, sizeId);
                        productId = getNewProductId(index);
                        p.setId(productId);

                        productMap.put(sku, p);
                    }

                }
                if (CacheManager.getStyleById(styleId) == null) {
                    //styleMap增加数据
                    String styleName = getStringFormCell(row.getCell(6));
                    if (CommonUtil.isBlank(styleName)) {
                        throw new Exception("第" + (i + 1) + "行第7列数据为空,请完善相应信息");
                    }
                    Style style = new Style(styleId, styleId, styleName, 0.0);
                    styleMap.put(styleId, style);
                }
                if (CommonUtil.isNotBlank(colorId)) {
                    initDtl.setColorId(colorId);
                    if (CacheManager.getColorById((colorId)) == null) {
                        //colorMap增加数据
                        String colorName = getStringFormCell(row.getCell(10));

                        Color color = new Color(colorId, colorId, colorName);
                        colorMap.put(colorId, color);
                    }

                }
                if (CommonUtil.isNotBlank(sizeId)) {
                    initDtl.setSizeId(sizeId);
                    if (CacheManager.getColorById((sizeId)) == null) {
                        //sizeMap增加数据
                        Size size = new Size(sizeId, sizeId, sizeId, sizeId);
                        sizeMap.put(sizeId, size);
                    }

                }
                String type = getStringFormCell(row.getCell(5));
                if (CommonUtil.isBlank(type)) {
                    throw new Exception("第" + (i + 1) + "行第6列数据为空,请完善相应信息");
                } else {
                    initDtl.setType(type);
                }
                String sex = getStringFormCell(row.getCell(7));
                if (CommonUtil.isBlank(sex)) {
                    throw new Exception("第" + (i + 1) + "行第8列数据为空,请完善相应信息");
                } else {
                    initDtl.setSex(sex);
                }
                initDtl.setGroupId(getStringFormCell(row.getCell(0)));
                initDtl.setOperator(getStringFormCell(row.getCell(2)));
                initDtl.setCustomerId(getStringFormCell(row.getCell(1)));
                String shirType = getStringFormCell(row.getCell(8));
                initDtl.setShirtType(CommonUtil.isBlank(shirType) ? "" : shirType);
                String washType = getStringFormCell(row.getCell(13));
                if (washType.equals("NULL")) {
                    initDtl.setWashType("");
                } else {
                    initDtl.setWashType(CommonUtil.isBlank(washType) ? "" : washType);
                }
                initDtl.setBillDate(CommonUtil.getDateString(billDate, "yyyy-MM-dd"));
                initDtl.setEndDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd"));
                initDtl.setQty(qty);
                initDtl.setSku(sku);
                initDtl.setOwnerId(user.getOwnerId());
                initDtl.setStatus("0");

                if (skuCountMap.containsKey(sku)) {
                    initDtl.setStartNum(epcService.findMaxNoBySkuNo(initDtl.getSku()) + skuCountMap.get(sku) + 1);
                    initDtl.setEndNum(epcService.findMaxNoBySkuNo(initDtl.getSku()) + skuCountMap.get(sku) + qty);
                    skuCountMap.put(sku, skuCountMap.get(sku) + qty);
                } else {
                    initDtl.setStartNum(epcService.findMaxNoBySkuNo(initDtl.getSku()) + 1);
                    initDtl.setEndNum(epcService.findMaxNoBySkuNo(initDtl.getSku()) + qty);
                    skuCountMap.put(sku, qty);
                }
                initDtlMap.put(billNo + sku, initDtl);
                FactoryBill init;
                if (initMap.containsKey(billNo)) {
                    init = initMap.get(billNo);
                    init.setTotQty(init.getTotQty() + qty);
                    initDtl.setUploadNo(init.getUploadNo());
                    init.setTotSku(init.getTotSku() + 1);
                    List<FactoryBillDtl> dtlList = init.getDtlList();
                    dtlList.add(initDtl);
                    init.setDtlList(dtlList);

                } else {

                    init = new FactoryBill();
                    initDtl.setUploadNo(master.getBillNo() + dtlIndex);
                    dtlIndex++;
                    setInitInfo(init, initDtl);
                    List<FactoryBillDtl> dtlList = new ArrayList<FactoryBillDtl>();
                    dtlList.add(initDtl);
                    init.setDtlList(dtlList);

                }
                initMap.put(billNo, init);

                FactoryInit factoryInit;

                if (factoryInitMap.containsKey(initDtl.getBillNo() + master.getBillNo())) {
                    factoryInit = factoryInitMap.get(initDtl.getBillNo() + master.getBillNo());
                    factoryInit.setTotQty(factoryInit.getTotQty() + initDtl.getQty());
                    factoryInit.setTotSku(init.getTotSku());
                } else {
                    factoryInit = new FactoryInit(initDtl.getUploadNo(), initDtl.getBillNo(), CommonUtil.getDateString(new Date(),"yyyy-MM-dd HH:mm:ss"), master.getFileName(), initDtl.getQty(), 1L, "1", 0);
                }

                factoryInitMap.put(initDtl.getBillNo() + master.getBillNo(), factoryInit);

                if(CommonUtil.isNotBlank(initDtl.getCategory())){
                    /*if(!CacheManager.isHaveFactoryCategory(initDtl.getCategory())){
                        if(!categoryMap.containsKey(initDtl.getCategory())){
                            categorySize++;
                            FactoryCategory c = new FactoryCategory(intToStr(categorySize), initDtl.getCategory());
                            categoryMap.put(initDtl.getCategory(),c);
                        }
                    }*/
                }

            }

            i++;
            row = sheet.getRow(i);
            if (row != null)
                if (row.getCell(0) != null
                        && !CommonUtil.isBlank(row.getCell(0)
                        .getStringCellValue())) {
                    billDate = row.getCell(14).getDateCellValue();
                    endDate = row.getCell(15).getDateCellValue();
                    billNo = FilePattern.matcher(getStringFormCell(row.getCell(3))).replaceAll("-");
                    styleId = FilePattern.matcher(getStringFormCell(row.getCell(4))).replaceAll("-");
                    colorId = FilePattern.matcher(getStringFormCell(row.getCell(9))).replaceAll("-");
                    sizeId = FilePattern.matcher(getStringFormCell(row.getCell(11))).replaceAll("-");
                    category = getStringFormCell(row.getCell(16));
                    factory = getStringFormCell(row.getCell(17));
                    season = getStringFormCell(row.getCell(18));
                    qty = (int) row.getCell(12).getNumericCellValue();// 数量
                } else {
                    break;
                }
        }
        master.setOwnerId(user.getOwnerId());
        master.setStatus(0);
        master.setTotQty(totQty);
        master.setTotSku(skuCountMap.size());
        List<FactoryBill> billList = epcService.findBillListByBillNos(CommonUtil.getSqlStrByList(new ArrayList<String>(initMap.keySet()), FactoryBill.class, "billNo"));
        if (CommonUtil.isNotBlank(billList)) {
            String str = "";
            for (FactoryBill b : billList) {
                str += "," + b.getBillNo();
            }
            throw new Exception("系统中存在办单信息" + str.substring(1));
        }
        return new FactoryBillInfoList(new ArrayList(factoryInitMap.values()),
                new ArrayList<FactoryBill>(initMap.values()),
                new ArrayList<FactoryBillDtl>(initDtlMap.values()),
                new ArrayList<Product>(productMap.values()),
                new ArrayList<Style>(styleMap.values()),
                new ArrayList<Color>(colorMap.values()),
                new ArrayList<Size>(sizeMap.values()),
                new ArrayList<FactoryCategory>(categoryMap.values()));

    }

    private static void setInitInfo(FactoryBill init, FactoryBillDtl initDtl) {
        init.setBillNo(initDtl.getBillNo());
        init.setBillDate(initDtl.getBillDate());
        init.setEndDate(initDtl.getEndDate());
        init.setUploadNo(initDtl.getUploadNo());
        init.setCustomerId(initDtl.getCustomerId());
        init.setGroupId(initDtl.getGroupId());
        init.setOperator(initDtl.getOperator());
        init.setTotSku(1L);
        init.setStatus(initDtl.getStatus());
        init.setTotQty(initDtl.getQty());
        init.setWashType(initDtl.getWashType());
        init.setShirtType(initDtl.getShirtType());
        init.setType(initDtl.getType());
        init.setSex(initDtl.getSex());
        init.setOwnerId(initDtl.getOwnerId());
        init.setFactory(initDtl.getFactory());
        init.setCategory(initDtl.getCategory());
        init.setSeason(initDtl.getSeason());
        init.setImgUrl(initDtl.getImgUrl());
        init.setIsSchedule("N");
    }

}
