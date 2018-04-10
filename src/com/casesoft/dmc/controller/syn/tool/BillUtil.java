package com.casesoft.dmc.controller.syn.tool;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.logistics.BillConvertUtil;
import com.casesoft.dmc.controller.syn.BillVo;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.FileUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.core.util.page.SinglePage;
import com.casesoft.dmc.model.cfg.Device;
import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.erp.BillDtl;
import com.casesoft.dmc.model.logistics.BillConstant;
import com.casesoft.dmc.model.logistics.InventoryBill;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.product.Size;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.stock.EpcStock;
import com.casesoft.dmc.model.stock.InventoryMergeBill;
import com.casesoft.dmc.model.stock.InventoryMergeBillDtl;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.BusinessDtl;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class BillUtil {

    // ///////////////john convert user to bill
    public static void convertEPayBill(User udtl, Bill bill) {
        String billPrefix = Constant.Bill.EPayBill_Prefix;
        bill.setType(4);// 电商销售单
        bill.setId(new GuidCreator().toString());
        bill.setAddress2(udtl.getAddress());
        bill.setPhone1(udtl.getPhone());
        bill.setOprId(udtl.getId());
        // bill.setClient(udtl.getName());
        // bill.setRemark(udtl.getRemark());
        // bill.setPayState(0);
        bill.setBillNo(billPrefix
                + CommonUtil.getDateString(new Date(), "yyMMddhhmmss")
                + CommonUtil.randomNumeric(3));
        bill.setClient(udtl.getCode());
        bill.setOwnerId(udtl.getOwnerId());
    }

    public static List<BillDtl> conventSpcStocksToBillDtls(int type,
                                                           List<EpcStock> listEpcStock) {
        if (CommonUtil.isBlank(listEpcStock)) {
            return null;
        }
        Map<String, BillDtl> billDtlMap = new HashMap<String, BillDtl>();
        for (EpcStock ep : listEpcStock) {
            if (billDtlMap.containsKey(ep.getSku())) {
                billDtlMap.get(ep.getSku()).setQty(
                        billDtlMap.get(ep.getSku()).getQty() + 1);
            } else {
                BillDtl bl = new BillDtl();
                bl.setStyleId(ep.getStyleId());
                bl.setColorId(ep.getColorId());
                bl.setSizeId(ep.getSizeId());
                bl.setSku(ep.getSku());
                bl.setQty(1L);
                // bl.setId(ep.getCode());
                bl.setType(type);
                billDtlMap.put(ep.getSku(), bl);
            }
        }

        return new ArrayList<BillDtl>(billDtlMap.values());
    }

    /**
     * 库存
     **/
    static public SinglePage<BillDtl> billDtlConvertToPageVo(
            List<BillDtl> countStock) {
        SinglePage<BillDtl> sp = new SinglePage<BillDtl>();

        long totStock = 0;
        for (BillDtl bdl : countStock) {
            totStock += bdl.getActQty();
            bdl.setStyleName(CacheManager.getStyleNameById(bdl.getStyleId()));
            bdl.setSizeName(CacheManager.getSizeNameById(bdl.getSizeId()));
            bdl.setColorName(CacheManager.getColorNameById(bdl.getColorId()));
        }
        sp.setRows(countStock);
        sp.addFooter("sku", "总库存:");
        sp.addFooter("actQty", "" + totStock);
        return sp;

    }

    static public void initSkuName(List<EpcStock> epcStockList) {
        for (EpcStock st : epcStockList) {
            st.setStyleName(CacheManager.getStyleNameById(st.getStyleId()));
            st.setSizeName(CacheManager.getSizeNameById(st.getSizeId()));
            st.setColorName(CacheManager.getColorNameById(st.getColorId()));
        }
    }

    public static File writeShopInventory(List<BillDtl> list)
            throws Exception {
        HSSFWorkbook taskExcelBook = new HSSFWorkbook();
        HSSFSheet sheet = taskExcelBook.createSheet("RFID库存明细");
        HSSFRow row = null;
        HSSFCell cell = null;
        row = sheet.createRow(0);
        cell = row.createCell(0);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("仓库编码");
        cell = row.createCell(1);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("商品编码");
        cell = row.createCell(2);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("款号");
        cell = row.createCell(3);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("色号");
        cell = row.createCell(4);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("码号");
        cell = row.createCell(5);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("库存数量");
        for (int i = 1; i <= list.size(); i++) {
            row = sheet.createRow(i);
            BillDtl dtl = list.get(i - 1);
            cell = row.createCell(0);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(dtl.getUnitId());
            Product p = CacheManager.getProductByCode(dtl.getSku());
            cell = row.createCell(1);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(p.getBrandCode());
            cell = row.createCell(2);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(dtl.getStyleId());
            cell = row.createCell(3);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(dtl.getColorId());
            cell = row.createCell(4);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(dtl.getSizeId());
            cell = row.createCell(5);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(dtl.getActQty());


        }
        File file = new File(PropertyUtil.getValue("MilanUploadHistory")
                + File.separatorChar
                + CommonUtil.getDateString(new Date(), "yyyyMMddHHmmss") +
                ".xls");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        taskExcelBook.write(fos);
        fos.flush();
        fos.close();
        return file;

    }
    // //////////////////
    // //////////////

    public static void iniBillAndBillDetail(Bill bill, List<BillDtl> listDtl) {
        bill.setId(new GuidCreator().toString());
        String billPrefix = Constant.Bill.EPayBill_Prefix;
        bill.setBillNo(billPrefix
                + CommonUtil.getDateString(new Date(), "yyMMddhhmmss")
                + CommonUtil.randomNumeric(3));
        bill.setSkuQty(new Long(listDtl.size()));
        bill.setBillDate(new Date());
        double sum = 0;
        long totQty = 0;
        for (BillDtl dtl : listDtl) {
            totQty += dtl.getQty();
            sum += dtl.getPrice() * dtl.getQty();
            dtl.setId(new GuidCreator().toString());
            dtl.setBillId(bill.getId());
            dtl.setBillNo(bill.getBillNo());
        }
        bill.setTotPrice(sum);
        bill.setTotQty(totQty);
    }

    /*
     * public static void convertToDtlList(Bill bill,List<Product>
     * listP,List<BillDtl> listDtl){
     *
     * double sum=0; for(Product product: listP){ BillDtl dtl=new BillDtl();
     * dtl.setBillId(bill.getId()); dtl.setBillNo(bill.getBillNo());
     * dtl.setId(new GuidCreator().toString()); dtl.setQty(1L);
     * dtl.setColorId(product.getColorNo()); dtl.setImage(product.getImage());
     * dtl.setSizeId(product.getSizeNo()); dtl.setStyleId(product.getStyleNo());
     * dtl.setPrice(product.getPrice()); dtl.setSku(product.getCode());
     * sum+=product.getPrice(); listDtl.add(dtl); } bill.setSkuQty(new
     * Long(listDtl.size())); bill.setBillDate(new Date());
     * bill.setTotPrice(sum); bill.setTotQty(new Long(listDtl.size())); }
     */
    // ///////////
    public static void initBill(Bill bill, List<BillDtl> dtls) {
        if (CommonUtil.isBlank(bill.getId())) {
            bill.setId(bill.getType() + "-" + bill.getOwnerId() + "-" + bill.getBillNo());

        }

        bill.setSkuQty(new Long(dtls.size()));

        for (BillDtl dtl : dtls) {
            dtl.setId(new GuidCreator().toString());
            dtl.setBillId(bill.getId());
            dtl.setBillNo(bill.getBillNo());
        }
    }

    public static Bill convertToDto(Bill billDto, Bill bill) {
        billDto.setBillDate(bill.getBillDate());
        billDto.setDestUnitId(bill.getDestUnitId());
        billDto.setTotQty(bill.getTotQty());
        return billDto;
    }

    public static void covertToVos(List<Bill> bills) {
        for (Bill bill : bills) {

            if (!CommonUtil.isBlank(bill.getOrigId())
                    && CommonUtil.isNotBlank(CacheManager.getUnitById(bill.getOrigId()))) {
                Unit u = CacheManager.getUnitById(bill.getOrigId());
                bill.setOrigName(u.getName());
            }
            if (!CommonUtil.isBlank(bill.getDestId())
                    && CommonUtil.isNotBlank(CacheManager.getUnitById(bill.getDestId()))) {
                bill.setDestName(CacheManager.getUnitById(bill.getDestId())
                        .getName());
            }
            if (!CommonUtil.isBlank(bill.getDestUnitId())
                    && CommonUtil.isNotBlank(CacheManager.getUnitById(bill.getDestUnitId()))) {
                Unit u = CacheManager.getUnitById(bill.getDestUnitId());
                bill.setDestUnitName(u.getName());
            }
            if (!CommonUtil.isBlank(bill.getOrigUnitId())
                    && CommonUtil.isNotBlank(CacheManager.getUnitById(bill.getOrigUnitId()))) {
                bill.setOrigUnitName(CacheManager.getUnitById(bill.getOrigUnitId())
                        .getName());
            }
            if(CommonUtil.isNotBlank(bill.getType())){
                bill.setBillType(CacheManager.getSetting(bill.getType().toString()).getValue());
            }
        }

    }

    public static void addActQty(Bill bill, List<BillDtl> billDtls,
                                 List<BusinessDtl> busDtls) {
        long actTotQty = 0;
        int actSkuQty = 0;
        for (BillDtl dtl : billDtls) {
            for (BusinessDtl busDtl : busDtls) {
                if (dtl.getSku().equals(busDtl.getSku())) {
                    actTotQty += busDtl.getQty();
                    dtl.setActQty(busDtl.getQty());
                    actSkuQty++;
                }
            }
        }
        bill.setActQty(actTotQty);
        bill.setActSkuQty(new Long(actSkuQty));
    }

    public static List<BillDtl> convertToVo(List<BillDtl> dtls) {
        if (CommonUtil.isNotBlank(dtls)) {
            for (BillDtl dtl : dtls) {
                Style style = CacheManager.getStyleById(dtl.getStyleId());
                if (CommonUtil.isNotBlank(style)) {
                    dtl.setStyleName(style.getStyleName());
                }
                // dtl.setPrice(style.getPrice());
                dtl.setColorName(CacheManager.getColorNameById(dtl.getColorId()));

                dtl.setSizeName(CacheManager.getSizeNameById(dtl.getSizeId()));

            }
        }
        return dtls;
    }

    public static List<BillVo> convertToVos(List<Bill> billList)
            throws Exception {
        List<BillVo> voList = new ArrayList<BillVo>();
        for (Bill bill : billList) {
            BillVo vo = new BillVo();
            BeanUtils.copyProperties(vo, bill);
            if (!CommonUtil.isBlank(vo.getDestUnitId())) {
                vo.setDestUnitName(CacheManager.getUnitById(vo.getDestUnitId())
                        .getName());
                List<Device> ds = CacheManager.getDevicesByUnitId(vo
                        .getDestUnitId());
                if (!CommonUtil.isBlank(ds))
                    vo.setDeviceId2(ds.get(0).getCode());
            }
            if (!CommonUtil.isBlank(vo.getOrigUnitId())) {
                vo.setOrigUnitName(CacheManager.getUnitById(vo.getOrigUnitId())
                        .getName());
                List<Device> ds = CacheManager.getDevicesByUnitId(vo
                        .getOrigUnitId());
                if (!CommonUtil.isBlank(ds))
                    vo.setDeviceId1(ds.get(0).getCode());
            }
            if (!CommonUtil.isBlank(vo.getDestId()) && CommonUtil.isNotBlank(CacheManager.getUnitByCode(vo.getDestId()))) {
                vo.setDestName(CacheManager.getUnitById(vo.getDestId())
                        .getName());

            }
            if (!CommonUtil.isBlank(vo.getOrigId()) && CommonUtil.isNotBlank(CacheManager.getUnitByCode(vo.getOrigId()))) {
                vo.setOrigId(CacheManager.getUnitById(vo.getOrigId())
                        .getName());

            }
            voList.add(vo);
        }
        return voList;
    }

    public static List<Bill> convertToVoList(List<Bill> billList)
            throws Exception {
        if (CommonUtil.isNotBlank(billList)) {
            for (Bill vo : billList) {
                if (!CommonUtil.isBlank(vo.getDestUnitId()) && CommonUtil.isNotBlank(CacheManager.getUnitByCode(vo.getDestUnitId()))) {
                    vo.setDestUnitName(CacheManager.getUnitById(vo.getDestUnitId())
                            .getName());

                }
                if (!CommonUtil.isBlank(vo.getOrigUnitId()) && CommonUtil.isNotBlank(CacheManager.getUnitByCode(vo.getOrigUnitId()))) {
                    vo.setOrigUnitName(CacheManager.getUnitById(vo.getOrigUnitId())
                            .getName());

                }
                if (!CommonUtil.isBlank(vo.getDestId()) && CommonUtil.isNotBlank(CacheManager.getUnitByCode(vo.getDestId()))) {
                    vo.setDestName(CacheManager.getUnitById(vo.getDestId())
                            .getName());

                }
                if (!CommonUtil.isBlank(vo.getOrigId()) && CommonUtil.isNotBlank(CacheManager.getUnitByCode(vo.getOrigId()))) {
                    vo.setOrigName(CacheManager.getUnitById(vo.getOrigId())
                            .getName());

                }
            }
        }
        return billList;
    }

    public static List<BillDtl> demoEbillDtlList() {
        List<BillDtl> ebillDetails = new ArrayList<BillDtl>();

        BillDtl dtl = new BillDtl();
        dtl.setStyleId("S2903");
        dtl.setStyleName("绿格子衬衫");
        dtl.setPrice(70.00);
        dtl.setColorName("绿色");
        dtl.setSizeName("M");
        dtl.setQty(1L);

        ebillDetails.add(dtl);

        dtl = new BillDtl();
        dtl.setStyleId("S2904");
        dtl.setStyleName("黄格子衬衫");
        dtl.setPrice(70.00);
        dtl.setColorName("黄色");
        dtl.setSizeName("M");
        dtl.setQty(1L);

        ebillDetails.add(dtl);

        dtl = new BillDtl();
        dtl.setStyleId("S2905");
        dtl.setStyleName("红格子衬衫");
        dtl.setPrice(70.00);
        dtl.setColorName("红色");
        dtl.setSizeName("M");
        dtl.setQty(1L);

        ebillDetails.add(dtl);

        return ebillDetails;
    }

    public static Map<String, String> setReportParameter(Bill bill) {
        Map<String, String> reportParameter = new HashMap<String, String>();

        String temp = bill.getDestUnitName();
        if (CommonUtil.isBlank(temp))
            temp = CacheManager.getUnitById(bill.getOrigUnitId()).toString();
        reportParameter.put("unit2Name", temp);
        reportParameter.put("reportTitle", "电子商务发货单");
        reportParameter.put("printTime",
                CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        reportParameter.put("client", bill.getClient());
        reportParameter.put("address2", bill.getAddress2());
        reportParameter.put("payInfo", bill.getPayInfo());
        reportParameter.put("payTime", bill.getPayTime());
        reportParameter.put("mobile", bill.getMobile2());
        reportParameter.put("phone", bill.getPhone2());
        reportParameter.put("remark", bill.getRemark());

        reportParameter.put("billNo", bill.getBillNo());

        return reportParameter;

    }

    public static List<BillDtl> countDetailsInfo(List<BillDtl> ebillDetails,
                                                 String rootPath) {
        for (BillDtl dtl : ebillDetails) {
            if (CommonUtil.isBlank(dtl.getStyleName()))
                dtl.setStyleName(CacheManager.getStyleNameById(dtl.getStyleId()));
            if (CommonUtil.isBlank(dtl.getColorName()))
                dtl.setColorName(CacheManager.getColorNameById(dtl.getColorId()));
            if (CommonUtil.isBlank(dtl.getSizeName()))
                dtl.setSizeName(CacheManager.getSizeNameById(dtl.getSizeId()));

            dtl.setImage(rootPath + "images/sku/" + dtl.getSku() + ".jpg");
        }
        return ebillDetails;
    }

    public static List<Bill> processExcelFile(File fileInput, String unitId,
                                              int token) throws Exception {
        ZipFile zipFile = new ZipFile(fileInput);
        List<Bill> billList = null;
        Map<String, Bill> zipMap = new HashMap<String, Bill>();
        try {
            // wing 2014-03-04 中文乱码bug解决
            InputStream ins = new FileInputStream(fileInput);
            ZipInputStream zipInput = new ZipInputStream(ins);
            ZipEntry zipEntry = null;

            BufferedReader in = null;
            Enumeration<ZipEntry> enu = (Enumeration<ZipEntry>) zipFile
                    .entries();
            while (enu.hasMoreElements()) {
                zipEntry = enu.nextElement();
                System.out.println("......");
                InputStream read = zipFile.getInputStream(zipEntry);

                String fileName = zipEntry.getName();
                if (fileName != null && fileName.indexOf(".") != -1) {// 是否为文件
                    unZipFile(zipEntry, read,
                            PropertyUtil.getValue("MilanUpload")
                                    + File.separator + fileInput.getName());
                }
            }

            zipInput.close();
            ins.close();
            if (in != null) {
                in.close();
            }
            File path = new File(PropertyUtil.getValue("MilanUpload")
                    + File.separator + fileInput.getName());
            File[] fileList = FileUtil.filterFile(path, ".xls");
            for (File file : fileList) {
                FileInputStream inputStream = new FileInputStream(file);
                processFileToBillList(zipMap, inputStream, file.getName(),
                        unitId, token);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage() + "解析ZIP压缩文件出错！！！");
        } finally {
            if (zipFile != null) {
                // zipFile.close();
            }
        }
        return new ArrayList<Bill>(zipMap.values());
    }

    private static void processFileToBillList(Map<String, Bill> zipMap,
                                              FileInputStream inputStream, String fileName, String unitId,
                                              int token) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);// 创建对Excel工作薄文件的引用
        HSSFSheet sheet = workbook.getSheetAt(0);// 创建对工作表的引用，也可用workbook.getSheet("sheetName");

        int i = 3;// 从第二行开始读数据
        HSSFRow row = sheet.getRow(i);
        String tempCode = row.getCell(1).getStringCellValue().trim();
        String tempName = row.getCell(2).getStringCellValue().trim();
        String styleId = fileName.split("\\.")[0].split("-")[0];
        String colorId = fileName.split("\\.")[0].split("-")[1];

        System.out.println(styleId + " " + colorId);

        List<Size> sizeList = CacheManager.getSizeSortById(
                CacheManager.getStyleById(styleId).getSizeSortId())
                .getSizeList();
        while (!CommonUtil.isBlank(row)
                && (CommonUtil.isNotBlank(tempCode) && CommonUtil
                .isNotBlank(tempName))) {
            System.out.println(tempCode + " " + tempName);
            if (zipMap.containsKey(tempCode)) {
                Bill bill = zipMap.get(tempCode);

                addBillDtlFromRow(bill, sizeList, styleId, colorId, row);

            } else {
                Bill bill = new Bill();
                bill.setId(Constant.Bill.Outbound_Prefix
                        + CommonUtil.getDateString(new Date(), "yyMMdd")
                        + (int) (Math.random() * 9000 + 1000));
                bill.setBillNo(bill.getId());
                bill.setOwnerId(CacheManager.getUnitById(unitId).getOwnerId());// 获取仓库的ownerId
                bill.setOrigUnitId(unitId);
                bill.setSkuQty(1L);
                bill.setTotQty(0L);
                bill.setDestUnitId(tempCode);
                bill.setType(token);

                addBillDtlFromRow(bill, sizeList, styleId, colorId, row);
                zipMap.put(tempCode, bill);

            }

            i++;
            row = sheet.getRow(i);
            if (row != null) {
                if (row.getCell(1) != null
                        && !CommonUtil.isBlank(row.getCell(1)
                        .getStringCellValue())) {
                    tempCode = row.getCell(1).getStringCellValue().trim();
                    tempName = row.getCell(2).getStringCellValue().trim();
                } else {
                    break;
                }
            }
        }
    }

    private static void addBillDtlFromRow(Bill bill, List<Size> sizeList,
                                          String styleId, String colorId, HSSFRow row) {
        for (int i = 3; i < sizeList.size() + 3; i++) {
            Size size = sizeList.get(i - 3);
            BillDtl billDtl = new BillDtl();
            String sku = styleId + colorId + size.getId();
            long qty = (long) getQtyFormCell(row, i);
            if (qty != 0) {
                billDtl.setId(bill.getId() + "-" + sku);
                billDtl.setBillId(bill.getId());
                billDtl.setBillNo(bill.getBillNo());
                billDtl.setType(bill.getType());
                bill.setBillDate(new Date());
                billDtl.setStyleId(styleId);
                billDtl.setColorId(colorId);
                billDtl.setSizeId(size.getId());
                billDtl.setSku(sku);
                billDtl.setQty(qty);
                bill.setTotQty(bill.getTotQty() + qty);
                bill.setSkuQty(bill.getSkuQty().longValue() + 1);
                bill.addDtl(billDtl);
            }
        }
    }

    private static double getQtyFormCell(HSSFRow row, int cellIndex) {
        double qty = 0;
        try {
            qty = row.getCell(cellIndex).getNumericCellValue();
        } catch (Exception ex) {
            if (ex instanceof NullPointerException) {
                qty = 0;
            } else {
                String strQty = row.getCell(cellIndex).getStringCellValue()
                        .trim();
                qty = Double.parseDouble(strQty);
            }
        }
        return qty;
    }

    /**
     * @param
     * @return void 返回类型
     * @throws
     * @Description: TODO(找到文件并读取解压到指定目录)
     */
    public static void unZipFile(ZipEntry ze, InputStream read,
                                 String saveRootDirectory) throws IOException {
        // 如果只读取图片，自行判断就OK.
        String fileName = ze.getName();
        // 判断文件是否符合要求或者是指定的某一类型
        // if (fileName.equals("WebRoot/WEB-INF/web.xml")) {
        // 指定要解压出来的文件格式（这些格式可抽取放置在集合或String数组通过参数传递进来，方法更通用）
        File file = new File(saveRootDirectory + File.separator + fileName);
        if (!file.exists()) {
            File rootDirectoryFile = new File(file.getParent());
            // 创建目录
            if (!rootDirectoryFile.exists()) {
                boolean ifSuccess = rootDirectoryFile.mkdirs();
                if (ifSuccess) {
                    System.out.println("文件夹创建成功!");
                } else {
                    System.out.println("文件创建失败!");
                }
            }
            // 创建文件
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 写入文件
        BufferedOutputStream write = new BufferedOutputStream(
                new FileOutputStream(file));
        int cha = 0;
        while ((cha = read.read()) != -1) {
            write.write(cha);
        }
        // 要注意IO流关闭的先后顺序
        write.flush();
        write.close();
        read.close();
        // }
        // }
    }

    public static List<Bill> convertBusToBill(List<Business> busList, int type) {
        List<Bill> billList = new ArrayList<Bill>();
        if (CommonUtil.isNotBlank(busList)) {
            for (Business bus : busList) {
                Bill bill = convertBusToBill(bus, type);
                billList.add(bill);
            }
        }
        return billList;
    }

    public static Bill convertBusToBill(Business bus, int type) {
        Bill bill = new Bill();
/*		bill.setId("" + bus.getToken() + "-" + bus.getOwnerId() + "-" + bus.getId());
*/
        bill.setId(bus.getId());
        bill.setBillNo(bus.getId());
        bill.setBillDate(bus.getBeginTime());
        bill.setOwnerId(bus.getOwnerId());
        Unit unit1 = CacheManager.getUnitByCode(bus.getOrigId());
        if (CommonUtil.isNotBlank(unit1)) {
            bill.setOrigName(unit1.getName());
        }
        Unit unit2 = CacheManager.getUnitByCode(bus.getDestId());
        if (CommonUtil.isNotBlank(unit2)) {
            bill.setDestName(unit2.getName());
        }
        bill.setOrigId(bus.getOrigId());
        bill.setDestId(bus.getDestId());
        bill.setDestUnitId(bus.getDestUnitId());
        bill.setOrigUnitId(bus.getOrigUnitId());
        bill.setTotQty(bus.getTotEpc());
        bill.setType(type);
        bill.setBillType("" + bus.getToken());
        if(CommonUtil.isNotBlank(bus.getSrcBillNo())){
            bill.setSrcBillNo(bus.getSrcBillNo());
        }else{
            bill.setSrcBillNo(bus.getBillNo());
        }
        return bill;
    }

    public static List<BillDtl> convertBusDtlToBillDtl(
            List<BusinessDtl> busDtlList, String billId) {
        List<BillDtl> dtlList = new ArrayList<BillDtl>();
        //int type = Integer.parseInt(billId.split("-")[0]);
        for (BusinessDtl busDtl : busDtlList) {
            BillDtl dtl = new BillDtl();
            dtl.setId(new GuidCreator().toString());
            dtl.setBillId(billId);
            dtl.setBillNo(busDtl.getTaskId());
            dtl.setType(0);
            dtl.setQty(busDtl.getQty());
            dtl.setSku(busDtl.getSku());
            dtl.setStyleId(busDtl.getStyleId());
            dtl.setColorId(busDtl.getColorId());
            dtl.setSizeId(busDtl.getSizeId());
            dtlList.add(dtl);
        }
        return dtlList;
    }

    public static File writeBillExcelFile(Bill bill, List<BillDtl> detailList, boolean isDiff)
            throws Exception {
        HSSFWorkbook taskExcelBook = new HSSFWorkbook();
        HSSFSheet sheet = taskExcelBook.createSheet("明细");
        HSSFRow row = null;
        HSSFCell cell = null;

        row = sheet.createRow(0);

        cell = row.createCell(0);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("SKU");

        cell = row.createCell(1);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("款号");

        cell = row.createCell(2);
        cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        cell.setCellValue("色号");

        cell = row.createCell(3);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("尺码");

        cell = row.createCell(4);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("数量");
        for (int i = 1; i <= detailList.size(); i++) {
            row = sheet.createRow(i);
            BillDtl dtl = detailList.get(i - 1);

            cell = row.createCell(0);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(dtl.getSku());

            cell = row.createCell(1);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(dtl.getStyleId());

            cell = row.createCell(2);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(dtl.getColorId());

            cell = row.createCell(3);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(dtl.getSizeId());


            cell = row.createCell(4);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            if (isDiff) {
                if (dtl.getActQty() == null) {

                    cell.setCellValue(String.valueOf(dtl.getQty()));
                } else {
                    cell.setCellValue(String.valueOf(dtl.getQty() - dtl.getActQty()));
                }
            } else {
                cell.setCellValue(String.valueOf(dtl.getQty()));

            }
        }
        String suffix = "";
        if (isDiff) {
            suffix = "diff";

        } else {
            suffix = "dtl";
        }
        File file = new File(PropertyUtil.getValue("MilanUploadHistory")
                + File.separatorChar
                + CommonUtil.getDateString(new Date(), "yyyyMMddHHmmss") + "_"
                + bill.getBillNo() + "_" + suffix + ".xls");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        taskExcelBook.write(fos);
        fos.flush();
        fos.close();
        return file;
    }


    /***
     * 将盘点合并单据转换成库存调整单
     * @param reason 原因
     * @param inventoryMergeBill billNo的信息
     * @param currentUser 调出用户
     * @param inventoryMergeBillDtlList 前台上传的数组对象
     * @param resultStr 返回提示信息
     * */
    public static List<InventoryBill> convertInventoryBill(List<InventoryMergeBillDtl> inventoryMergeBillDtlList,String reason, InventoryMergeBill inventoryMergeBill, List<EpcStock> epcStockList, User currentUser, List<Business> businessList,StringBuffer resultStr) {
        List<InventoryBill> inventoryBillList = new ArrayList<>();
        Map<String,InventoryMergeBillDtl> inventoryMergeBillDtlMap = new HashMap<>();
        for(InventoryMergeBillDtl dtl : inventoryMergeBillDtlList) {
            inventoryMergeBillDtlMap.put(dtl.getCode(),dtl);
            dtl.setBillNo(inventoryMergeBill.getBillNo());
        }
         for (EpcStock epcStock : epcStockList){
            //判断商品当前所在位置及在库情况
            if(epcStock.getWarehouseId().equals(inventoryMergeBill.getWarehouseId()) && epcStock.getInStock()==0) {
                InventoryBill inventoryBill = new InventoryBill();
                String prefix = BillConstant.BillPrefix.Inventory + CommonUtil.getDateString(new Date(), "yyMMddHHmmssSSS");
                inventoryBill.setId(prefix);
                inventoryBill.setBillNo(prefix);
                inventoryBill.setColorId(epcStock.getColorId());
                inventoryBill.setColorName(epcStock.getColorId());
                inventoryBill.setSizeId(epcStock.getSizeId());
                Size size = CacheManager.getSizeById(epcStock.getSizeId());
                inventoryBill.setSizeName(size.getSizeName());
                inventoryBill.setSku(epcStock.getSku());
                inventoryBill.setStyleId(epcStock.getStyleId());
                inventoryBill.setStyleName(epcStock.getStyleName());
                inventoryBill.setBillDate(new Date());
                inventoryBill.setActQty(1L);
                inventoryBill.setActPrice(epcStock.getPrice());
                inventoryBill.setDestId(inventoryMergeBill.getOwnerId());
                inventoryBill.setDestUnitId(inventoryMergeBill.getOwnerId());
                inventoryBill.setOwnerId(inventoryMergeBill.getWarehouseId());
                inventoryBill.setCode(epcStock.getCode());
                inventoryBill.setState(Constant.Token.Storage_Adjust_Outbound + "");
                inventoryBill.setReason(reason);
                inventoryBill.setOprId(currentUser.getId());
                inventoryBill.setSrcBillNo(inventoryMergeBill.getBillNo());
                inventoryBillList.add(inventoryBill);
                Business business = BillConvertUtil.covertToInventoryBusinessOut(epcStock, inventoryBill, currentUser, "on");
                epcStock.setInStock(0);
                businessList.add(business);
                inventoryMergeBillDtlMap.get(epcStock.getCode()).setState("Y");
                resultStr.append(epcStock.getCode()+"/"+epcStock.getSku()+"转换成功"+" \n ");
            }else {
                inventoryMergeBillDtlMap.get(epcStock.getCode()).setState("X");
                resultStr.append(epcStock.getCode()+"/"+epcStock.getSku()+"转换失败"+" \n ");
            }
        }
        /*清空list将map中的数据添加到list中*/
        inventoryMergeBillDtlList.clear();
        inventoryMergeBillDtlList.addAll(inventoryMergeBillDtlMap.values());
        return inventoryBillList;
    }
}
