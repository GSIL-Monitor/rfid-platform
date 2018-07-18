package com.casesoft.dmc.controller.task;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.servlet.ServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.FileUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.core.util.page.SinglePage;
import com.casesoft.dmc.model.cfg.Device;
import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.erp.BillDtl;
import com.casesoft.dmc.model.product.Size;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.stock.EpcStock;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.task.Box;
import com.casesoft.dmc.model.task.BoxDtl;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.BusinessDtl;
import com.casesoft.dmc.model.task.CloudBoxDtl;
import com.casesoft.dmc.model.task.CloudBusiness;
import com.casesoft.dmc.model.task.CloudBusinessDtl;
import com.casesoft.dmc.model.task.Record;

/**
 * 将xml数据解析成Business,List<Record>,List<Box>,boxdtl,busDtl,
 *
 * @author Administrator
 */
public class TaskUtil {

    private static Logger logger = LoggerFactory.getLogger(TaskUtil.class);
    private static List<Record> recordList = null;
    private static Map<String, BusinessDtl> dtlMap = null;
    private static Map<String, Box> boxMap = null;


    /**
     * 将Task文件缓存到Map<String,StringBuffer>中
     *
     * @param taskZipFile
     * @return
     * @throws Exception
     */
    @Deprecated
    public static Map<String, StringBuffer> convertToJsonBuffer(File taskZipFile)
            throws Exception {
        ZipFile zipFile = null;
        Business bus = null;
        Map<String, StringBuffer> zipMap = new HashMap<String, StringBuffer>();
        try {
            zipFile = new ZipFile(taskZipFile);
            Enumeration<?> enu = zipFile.entries();
            while (enu.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) enu.nextElement();
                String zipEntryName = entry.getName();
                BufferedInputStream bis = new BufferedInputStream(
                        zipFile.getInputStream(entry));

                StringBuffer jsonStr = FileUtil.readInputStreamToStr(bis);
                zipMap.put(zipEntryName, jsonStr);
            }
            if (zipFile.size() != zipMap.size()) {
                throw new Exception("存在重复文件名!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("解析ZIP压缩文件出错！！！");
        } finally {
            if (zipFile != null) {
                zipFile.close();
            }
        }
        return zipMap;
    }

    // john 修改
    // Action Business bs=new Business();
    public static void initBussiness(List<Business> Blist, Business bs,
                                     String Ids) {
        Ids = Ids.replace("'", "");
        long totStyle = 0;
        long totSku = 0;
        long totCarton = 0;
        long totEpc = 0;
        long totTime = 0;
        Date beginTime = Blist.get(0).getBeginTime();
        Date endTime = Blist.get(0).getEndTime();
        String id = "H" + "TSK"
                + CommonUtil.getDateString(new Date(), "yyyyMMdd")
                + Blist.get(0).getDeviceId() + CommonUtil.randomNumeric(3);
        String srcTaskId = "";
        String ownerId = Blist.get(0).getOwnerId();
        String deviceId = Blist.get(0).getDeviceId();
        for (Business b : Blist) {
            if (b.getId().startsWith("H")) {
                continue;
            }
            if (beginTime.getTime() > b.getBeginTime().getTime()) {
                beginTime = b.getBeginTime();
            }
            if (endTime.getTime() < b.getEndTime().getTime()) {
                endTime = b.getEndTime();
            }
            totStyle += b.getTotStyle();
            totSku += b.getTotSku();
            totCarton += b.getTotCarton();
            totEpc += b.getTotEpc();
            totTime += b.getTotTime();
            srcTaskId += b.getId() + ",";
            b.setLocked(1);
        }
        bs.setTotCarton(totCarton);
        bs.setTotSku(totSku);
        bs.setTotCarton(totCarton);
        bs.setTotEpc(totEpc);
        bs.setTotStyle(totStyle);
        bs.setTotTime(totTime);
        bs.setId(id);
        bs.setLocked(0);
        bs.setSrcTaskId(srcTaskId);
        bs.setStatus(Blist.get(0).getStatus());
        bs.setToken(Blist.get(0).getToken());
        bs.setBeginTime(beginTime);
        bs.setEndTime(endTime);
        bs.setDeviceId(deviceId);
        bs.setOwnerId(ownerId);
        bs.setSrcTaskId(Ids);
        bs.setOrigId(Blist.get(0).getOrigId());
        // bs.setBillNo(Blist.get(0).getBillNo());
    }

    // //////////////
    public static Business convertToDto(File taskZipFile) throws Exception {
        ZipFile zipFile = null;
        Business bus = null;
        Map<String, StringBuffer> zipMap = new HashMap<String, StringBuffer>();
        try {
            // wing 2014-03-04 中文乱码bug解决
            InputStream ins = new FileInputStream(taskZipFile);
            ZipInputStream zipInput = new ZipInputStream(ins);
            ZipEntry zipEntry = null;

            BufferedReader in = null;
            while ((zipEntry = zipInput.getNextEntry()) != null) {

                in = new BufferedReader(
                        new InputStreamReader(zipInput, "UTF-8"));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = in.readLine()) != null) {
                    buffer.append(line);
                }

                zipMap.put(zipEntry.getName(), buffer);
                zipInput.closeEntry();

            }
            zipInput.close();
            ins.close();
            if (in != null) {
                in.close();
            }
            // zipFile = new ZipFile(taskZipFile);
            // Enumeration<?> enu = zipFile.entries();
            // while (enu.hasMoreElements()) {
            // ZipEntry entry = (ZipEntry) enu.nextElement();
            // String zipEntryName = entry.getName();
            // BufferedInputStream bis = new
            // BufferedInputStream(zipFile.getInputStream(entry));
            // // zipMap.put(zipEntryName, bis);//将zipFile Stream放入Map中
            // StringBuffer jsonStr = FileUtil.readInputStreamToStrI18N(bis);
            // zipMap.put(zipEntryName, jsonStr);
            // }
            // if (zipFile.size() != zipMap.size()) {
            // throw new Exception("存在重复文件名!");
            // }
            bus = convertToDtl(zipMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("解析ZIP压缩文件出错！！！");
        } finally {
            if (zipFile != null) {
                // zipFile.close();
            }
        }
        return bus;
    }

    public static Business convertToDtl(Map<String, StringBuffer> zipMap)
            throws Exception {
        Business bus = null;
        List<Box> boxList = new ArrayList<Box>();
        Map<String, BusinessDtl> dtlMap = new HashMap<String, BusinessDtl>();
        // 获取主任务信息
        for (String zipFileName : zipMap.keySet()) {
            if (!zipFileName.contains(".tsk")) {
                continue;
            }

            bus = jsonToBus(zipMap.get(zipFileName).toString());

            List<Record> recordList = new ArrayList<Record>();
            bus.setRecordList(recordList);

            break;
        }
        // 获取箱信息
        for (String zipFileName : zipMap.keySet()) {
            if (!zipFileName.contains(".ctn")) {
                continue;
            }
            String boxId = zipFileName.substring(zipFileName.indexOf("CTN"),
                    zipFileName.indexOf(".ctn"));
            Box box = jsonToBox(zipMap.get(zipFileName).toString(), bus,
                    dtlMap, boxId);

            boxList.add(box);
        }

        // 获取单据信息
        for (String zipFileName : zipMap.keySet()) {
            if (!zipFileName.contains(".bill"))
                continue;
            Bill bill = jsonToBill(zipMap.get(zipFileName).toString());

            bill.setActSkuQty(bus.getTotSku());
            bill.setBoxQty(bus.getTotCarton());
            bill.setOwnerId(bus.getOwnerId());
            bill.setStatus(1);
            bill.setTaskId(bus.getId());

            bus.setBillId(bill.getId());
            bus.setBillNo(bill.getBillNo());
            bus.setBill(bill);
            // 2014-03-11 wing add
            bus.setStatus(1);
            break;
        }

        // 获取单据详细信息
        for (String zipFileName : zipMap.keySet()) {
            if (!zipFileName.contains(".dtl"))
                continue;
            String dtlJsonStr = zipMap.get(zipFileName).toString();
            List<BillDtl> dtlList = jsonToBillDtlList(dtlJsonStr, bus.getBill());// JSON.parseArray(dtlJsonStr,
            // BillDtl.class);
            bus.getBill().setDtlList(dtlList);
            break;
        }

        bus.setBoxList(boxList);
        bus.setDtlList(new ArrayList(dtlMap.values()));
        // preInfo(bus);
        return bus;
    }

    // 更改数据类型
    private static void preInfo(Business business) {
        Bill bill = business.getBill();

        if (bill == null) {
            return;
        }
        // john add
        business.setType(bill.getType());
        business.setDestId(bill.getDestId());
        business.setDestUnitId(bill.getDestUnitId());
        for (BusinessDtl b : business.getDtlList()) {
            b.setType(bill.getType());
            b.setDestId(bill.getDestId());
            b.setDestUnitId(bill.getDestUnitId());
        }
        for (Record r : business.getRecordList()) {
            r.setType(bill.getType());
            r.setDestId(bill.getDestId());
            r.setDestUnitId(bill.getDestUnitId());
        }
        for (Box b : business.getBoxList()) {
            b.setType(bill.getType());
//			b.setStorage2Id(bill.getDestId());
//			b.setUnit2Id(bill.getDestUnitId());
//			for (BoxDtl bd : b.getBoxDtls()) {
//				bd.setType(bill.getType());
//				bd.setStorage2Id(bill.getDestId());
//				bd.setUnit2Id(bill.getDestUnitId());
//			}
        }
    }

    private static List<BillDtl> jsonToBillDtlList(String json, Bill bill) {
        // JSON.parseArray(dtlJsonStr, BillDtl.class);直接用fastjson时，在生产环境下存在解析bug
        List<BillDtl> dtlList = new ArrayList<BillDtl>();
        JSONArray jsonArray = JSON.parseArray(json);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            BillDtl dtl = new BillDtl();
            String dtlId = jsonObj.getString("id");
            dtl.setId(CommonUtil.isBlank(dtlId) ? new GuidCreator().toString()
                    : dtlId);
            dtl.setBillId(bill.getId());
            dtl.setBillNo(bill.getBillNo());
            dtl.setSku(jsonObj.getString("sku"));
            dtl.setStyleId(jsonObj.getString("styleId"));
            dtl.setColorId(jsonObj.getString("colorId"));
            dtl.setSizeId(jsonObj.getString("sizeId"));
            dtl.setQty(jsonObj.getLongValue("qty"));
            dtl.setActQty(jsonObj.getLongValue("actQty"));
            dtlList.add(dtl);
        }
        return dtlList;
    }

    private static Bill jsonToBill(String json) {
        return JSON.parseObject(json, Bill.class);
    }

    private static Box jsonToBox(String json, Business bus,
                                 Map<String, BusinessDtl> dtlMap, String boxId) throws Exception {
        Box box = new Box();
        List<BoxDtl> dtlList = new ArrayList<BoxDtl>();

        JSONObject boxJsonObj = JSON.parseObject(json);// 主箱信息
        JSONObject o = boxJsonObj.getJSONObject("carton");
        box.setId(new GuidCreator().toString());
        box.setTaskId(bus.getId());
        box.setDeviceId(bus.getDeviceId());
        box.setOwnerId(bus.getOwnerId());
        box.setToken(bus.getToken());
//		box.setStorageId(bus.getOrigId());
        box.setCartonId(boxId);
        box.setTotEpc(o.getLongValue("cartonTotEpc"));
        box.setTotSku(o.getLongValue("cartonTotSku"));
        box.setTotStyle(o.getLongValue("cartonTotStyle"));
        String scanTimeStr = o.getString("cartonScanTime");
        box.setScanTime(CommonUtil.converStrToDate(scanTimeStr,
                "yyyy/MM/dd hh:mm:ss"));
        box.setSeqNo(o.getInteger("autoId"));

        JSONArray jsonArray = boxJsonObj.getJSONArray("cartonDtl");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            BoxDtl dtl = new BoxDtl();
            dtl.setCartonId(boxId);
            dtl.setId(new GuidCreator().toString());
            dtl.setOwnerId(bus.getOwnerId());
            dtl.setDeviceId(bus.getDeviceId());
            dtl.setTaskId(bus.getId());
            dtl.setToken(bus.getToken());
//			dtl.setStorageId(bus.getOrigId());

            dtl.setSku(jsonObj.getString("sku"));
            dtl.setStyleId(jsonObj.getString("styleId"));
            dtl.setColorId(jsonObj.getString("colorId"));
            dtl.setSizeId(jsonObj.getString("sizeId"));
            dtl.setQty(Long.parseLong(jsonObj.getString("skuCount")));
            dtlList.add(dtl);

            // 增加到dtl
            if (dtlMap.containsKey(dtl.getSku())) {
                BusinessDtl busDtl = dtlMap.get(dtl.getSku());
                busDtl.setQty(busDtl.getQty() + dtl.getQty());
            } else {
                BusinessDtl busDtl = new BusinessDtl();
                busDtl.setId(new GuidCreator().toString());
                busDtl.setOwnerId(bus.getOwnerId());
                busDtl.setDeviceId(bus.getDeviceId());
                busDtl.setTaskId(bus.getId());
                busDtl.setToken(bus.getToken());
                busDtl.setOrigId(bus.getOrigId());
                busDtl.setSku(jsonObj.getString("sku"));
                busDtl.setStyleId(jsonObj.getString("styleId"));
                busDtl.setColorId(jsonObj.getString("colorId"));
                busDtl.setSizeId(jsonObj.getString("sizeId"));
                busDtl.setQty(Long.parseLong(jsonObj.getString("skuCount")));

                dtlMap.put(busDtl.getSku(), busDtl);
            }

            // 增加Record
            JSONArray jsonArr = jsonObj.getJSONArray("epcList");
            for (Object epcStr : jsonArr) {
                Record r = new Record();
                r.setId(new GuidCreator().toString());
                r.setCartonId(boxId);
                r.setDeviceId(bus.getDeviceId());
                r.setTaskId(bus.getId());
                r.setOwnerId(bus.getOwnerId());
                r.setCode(epcStr.toString());
                r.setOrigId(bus.getOrigId());
                r.setToken(bus.getToken());
                r.setScanTime(box.getScanTime());// boxMap.get(boxId).getScanTime());
                r.setSku(dtl.getSku());

                r.setStyleId(dtl.getStyleId());
                r.setSizeId(dtl.getSizeId());
                r.setColorId(dtl.getColorId());

                bus.getRecordList().add(r);
            }
        }
        box.setBoxDtls(dtlList);
        return box;
    }

    private static Business jsonToBus(String json) throws Exception {
        Business business = new Business();
        JSONObject jsonObj = JSON.parseObject(json);

        business.setId(jsonObj.getString("taskId"));
        business.setStatus(("finish".equals(jsonObj.getString("flagTaskStatus"))) ? 0
                : -1);

        business.setDeviceId(jsonObj.getString("taskDeviceId"));
        business.setOwnerId(CacheManager
                .getDeviceByCode(business.getDeviceId()).getOwnerId());
        business.setOrigId(CacheManager.getDeviceByCode(
                business.getDeviceId()).getStorageId());

        business.setBeginTime(CommonUtil.converStrToDate(
                jsonObj.getString("taskBeginTime"), "yyyy/MM/dd hh:mm:ss"));
        business.setEndTime(CommonUtil.converStrToDate(
                jsonObj.getString("taskEndTime"), "yyyy/MM/dd hh:mm:ss"));
        business.setTotEpc(jsonObj.getLongValue("taskTotEpc"));
        business.setTotCarton(jsonObj.getLongValue("taskTotCarton"));
        business.setTotSku(jsonObj.getLongValue("taskTotSku"));
        business.setTotStyle(jsonObj.getLongValue("taskTotStyle"));
        business.setToken(jsonObj.getIntValue("taskRfidToken"));
        return business;
    }


    static boolean deleteRefreshDir(File[] dir) {
        for (int i = 0; i < dir.length; i++) {
            boolean success = moveToHistoryPath(dir[i]);
            logger.debug("移动目录" + dir[i].getName() + "下的文件,"
                    + (success ? "成功" : "失败"));
        }
        return true;
    }

    /**
     * 获取task文件的历史目录,并将task相关文件复制其目录下，删除掉xml文件
     */
    private static boolean moveToHistoryPath(File srcPath) {
        String destPath = srcPath.getAbsolutePath().replace("MilanUpload",
                "MilanUploadHistory");
        File destFile = new File(destPath);
        boolean success = FileUtil.rename(srcPath, destFile);
        // if(success)
        // deleteXmlFile(destFile);//删除目录下的xml文件
        // else {
        // logger.debug("挪动文件失败！！！");
        // System.out.println("挪动文件失败！！！");
        // }
        return success;
    }

    @SuppressWarnings("unused")
    private static boolean deleteXmlFile(File historyPath) {
        File[] files = FileUtil.filterFile(historyPath, ".xml");
        for (File file : files) {
            boolean suc = file.delete();
            logger.debug("删除文件" + file.getAbsolutePath() + " "
                    + (suc ? "成功" : "失败"));
        }
        return true;
    }

    /**
     * 在该目录中转换xml文件
     *
     * @throws Exception
     */
    static Business xmlToBean(File file) throws Exception {
        Business bus = new Business();
        dtlMap = new HashMap<String, BusinessDtl>();
        recordList = new ArrayList<Record>();
        boxMap = new HashMap<String, Box>();

        File[] files = file.listFiles();

        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            // if (!f.getName().endsWith(".tsk.xml"))//
            if (!f.getName().endsWith(".tsk")) {
                continue;
            }
            // if(f.getName().endsWith(".tsk.xml")) {
            if (f.getName().endsWith(".tsk")) {
                xmlToBean(f, bus, 1);
            }
            break;
        }
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            // if (!f.getName().endsWith(".ctn.xml"))//
            if (!f.getName().endsWith(".ctn"))//
            {
                continue;
            }
            // if(f.getName().endsWith(".ctn.xml")) {
            if (f.getName().endsWith(".ctn")) {
                xmlToBean(f, bus, 2);
            }
            break;
        }
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            // if (!f.getName().endsWith(".sku.xml"))//
            if (!f.getName().endsWith(".sku"))//
            {
                continue;
            }
            // if(f.getName().endsWith(".sku.xml")) {
            if (f.getName().endsWith(".sku")) {
                xmlToBean(f, bus, 3);
            }

        }

        bus.setBoxList(new ArrayList<Box>(boxMap.values()));
        bus.setDtlList(new ArrayList<BusinessDtl>(dtlMap.values()));
        bus.setRecordList(recordList);
        return bus;
    }

    /**
     * 处理xml文件，将数据存入bus中，i:1,表示tsk文件,2:1,ctn文件,3:1,sku文件
     *
     * @param f
     * @param bus
     * @param i
     * @throws Exception
     */
    static void xmlToBean(File f, Business bus, int i) throws Exception {
        switch (i) {
            case 1:
                xmlToBus(f, bus);
                break;
            case 2:
                xmlToCtn(f, bus);
                break;
            case 3:
                xmlToSku(f, bus);
                break;
        }

    }

    @SuppressWarnings("rawtypes")
    static void xmlToSku(File f, Business bus) throws Exception {
        String fileName = f.getName();
        String boxId = fileName.substring(fileName.indexOf("CTN"),
                // fileName.indexOf(".sku.xml"));
                fileName.indexOf(".sku"));
        SAXReader reader = new SAXReader();
        Document doc = null;

        List<BoxDtl> boxDtls = new ArrayList<BoxDtl>();
        try {
            doc = reader.read(f);
            Element rootEle = doc.getRootElement();
            logger.debug("获取到的根节点：" + rootEle.getName());

            // doc = DocumentHelper.parseText(rootEle.getStringValue());

            Iterator it = doc.getRootElement().elementIterator("MilanDataSku");

            while (it.hasNext()) {
                BoxDtl dtl = new BoxDtl();
                dtl.setId(new GuidCreator().toString());
                Element ele = (Element) it.next();

                String value = ele.elementTextTrim("sku");
                if (CommonUtil.isBlank(value)) {
                    throw new Exception("空异常：" + boxId + "任务号:" + bus.getId());
                }
                dtl.setSku(value);

                value = ele.elementTextTrim("styleId");
                dtl.setStyleId(value);
                value = ele.elementTextTrim("colorId");
                dtl.setColorId(value);
                value = ele.elementTextTrim("sizeId");
                dtl.setSizeId(value);
                value = ele.elementTextTrim("skuCount");
                dtl.setQty(Long.parseLong(value));

                // 获取单据详单信息
                if (bus.getBill() != null) {
                    BillDtl billDtl = new BillDtl();
                    billDtl.setId(new GuidCreator().toString());
                    billDtl.setBillId(bus.getBill().getId());
                    billDtl.setBillNo(bus.getBill().getBillNo());
                    billDtl.setSku(dtl.getSku());
                    billDtl.setStyleId(dtl.getStyleId());
                    billDtl.setColorId(dtl.getColorId());
                    billDtl.setSizeId(dtl.getSizeId());
                    billDtl.setActQty(dtl.getQty());

                    value = ele.elementTextTrim("billQty");
                    billDtl.setQty(Long.parseLong(value));

                    bus.getBill().addDtl(billDtl);
                }

                dtl.setCartonId(boxId);
                dtl.setDeviceId(bus.getDeviceId());
                dtl.setTaskId(bus.getId());
                dtl.setOwnerId(bus.getOwnerId());
                dtl.setToken(bus.getToken());

                Element epcEle = ele.element("epcList");
                Iterator itEpc = epcEle.elementIterator("string");
                while (itEpc.hasNext()) {
                    Element sEle = (Element) itEpc.next();
                    String epc = sEle.getText().trim();
                    Record r = new Record();
                    r.setId(new GuidCreator().toString());
                    r.setCartonId(boxId);
                    r.setDeviceId(bus.getDeviceId());
                    r.setTaskId(bus.getId());
                    r.setOwnerId(bus.getOwnerId());
                    r.setCode(epc);
                    r.setOrigId(bus.getOrigId());
                    r.setToken(bus.getToken());
                    r.setScanTime(boxMap.get(boxId).getScanTime());
                    r.setSku(dtl.getSku());

                    r.setStyleId(dtl.getStyleId());
                    r.setSizeId(dtl.getSizeId());
                    r.setColorId(dtl.getColorId());

                    recordList.add(r);
                }

                boxDtls.add(dtl);

                if (dtlMap.containsKey(dtl.getSku())) {
                    BusinessDtl bd = dtlMap.get(dtl.getSku());
                    bd.setQty(dtl.getQty() + bd.getQty());
                    dtlMap.put(dtl.getSku(), bd);
                } else {
                    BusinessDtl bd = new BusinessDtl();
                    bd.setId(new GuidCreator().toString());
                    bd.setTaskId(bus.getId());
                    bd.setDeviceId(bus.getDeviceId());
                    bd.setOwnerId(bus.getOwnerId());
                    bd.setOrigId(bus.getOrigId());
                    bd.setToken(bus.getToken());

                    bd.setSku(dtl.getSku());
                    bd.setStyleId(dtl.getStyleId());
                    bd.setColorId(dtl.getColorId());
                    bd.setSizeId(dtl.getSizeId());
                    bd.setQty(dtl.getQty());
                    dtlMap.put(bd.getSku(), bd);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        Box box = boxMap.get(boxId);
        box.setBoxDtls(boxDtls);
    }

    @SuppressWarnings("rawtypes")
    static void xmlToCtn(File f, Business bus) throws Exception {
        SAXReader reader = new SAXReader();
        Document doc = null;

        try {
            doc = reader.read(f);
            Element rootEle = doc.getRootElement();
            logger.debug("获取到的根节点：" + rootEle.getName());

            // doc = DocumentHelper.parseText(rootEle.getStringValue());

            Iterator it = doc.getRootElement().elementIterator(
                    "MilanDataCarton");

            while (it.hasNext()) {
                Box box = new Box();
                box.setId(new GuidCreator().toString());
                Element ele = (Element) it.next();

                String value = ele.elementTextTrim("autoId");
                box.setSeqNo(Integer.parseInt(value));
                value = ele.elementTextTrim("cartonId");
                box.setCartonId(value);
                value = ele.elementTextTrim("taskId");
                box.setTaskId(value);
                value = ele.elementTextTrim("taskDeviceId");
                box.setDeviceId(value);
                value = ele.elementTextTrim("cartonTotEpc");
                box.setTotEpc(Long.parseLong(value));
                value = ele.elementTextTrim("cartonTotSku");
                box.setTotSku(Long.parseLong(value));
                value = ele.elementTextTrim("cartonTotStyle");
                box.setTotStyle(Long.parseLong(value));
                value = ele.elementTextTrim("cartonScanTime");
                box.setScanTime(CommonUtil.converStrToDate(value));

                box.setToken(bus.getToken());
                box.setOwnerId(bus.getOwnerId());

                boxMap.put(box.getCartonId(), box);
            }
        } catch (Exception e) {

            e.printStackTrace();
            throw e;
        }

    }

    static void xmlToBus(File f, Business bus) throws Exception {
        SAXReader reader = new SAXReader();
        Document doc = null;

        try {
            doc = reader.read(f);
            Element rootEle = doc.getRootElement();
            logger.debug("获取到的根节点：" + rootEle.getName());

            String value = rootEle.element("taskId").getTextTrim();
            bus.setId(value);

            value = rootEle.elementTextTrim("flagTaskStatus");
            bus.setStatus(value.equals("finish") ? 0 : -1);
            value = rootEle.elementTextTrim("taskDeviceId");
            bus.setDeviceId(value);
            value = rootEle.elementTextTrim("taskBeginTime");
            bus.setBeginTime(CommonUtil.converStrToDate(value));
            value = rootEle.elementTextTrim("taskEndTime");
            bus.setEndTime(CommonUtil.converStrToDate(value));
            value = rootEle.elementTextTrim("taskTotEpc");
            bus.setTotEpc(Long.parseLong(value));
            value = rootEle.elementTextTrim("taskTotCarton");
            bus.setTotCarton(Long.parseLong(value));
            value = rootEle.elementTextTrim("taskTotSku");
            bus.setTotSku(Long.parseLong(value));
            value = rootEle.elementTextTrim("taskTotStyle");
            bus.setTotStyle(Long.parseLong(value));
            value = rootEle.elementTextTrim("taskEachCartonTotEpc");

            value = rootEle.elementTextTrim("taskRfidToken");
            bus.setToken(Integer.parseInt(value));

            Device device = CacheManager.getDeviceByCode(bus.getDeviceId());
            bus.setOwnerId(device.getOwnerId());
            bus.setOrigId(device.getStorageId());// 仓库可以为空，根据设备而定

            // 获取BillId,billNo
            value = rootEle.elementTextTrim("billId");
            if (value != null && !value.equals("")) {
                Bill bill = new Bill();
                bill.setId(value);
                bill.setTaskId(bus.getId());
                value = rootEle.elementTextTrim("billNo");
                bill.setBillNo(value);
                value = rootEle.elementTextTrim("billTotQty");
                bill.setTotQty(Long.parseLong(value));
                value = rootEle.elementTextTrim("type");
                bill.setType(Integer.parseInt(value));
                value = rootEle.elementTextTrim("billDate");
                bill.setBillDate(CommonUtil
                        .converStrToDate(value, "yyyy-MM-dd"));
                value = rootEle.elementTextTrim("unit2Name");
                bill.setDestUnitName(value);
                value = CacheManager.getDeviceByCode(bus.getDeviceId())
                        .getOwnerId();
                bill.setOwnerId(value);

                bill.setActQty(bus.getTotEpc());
                bill.setActSkuQty(bus.getTotSku());
                bill.setActBoxQty(bus.getTotCarton());
                bill.setStatus(1);

                bus.setBillId(bill.getId());
                bus.setBillNo(bill.getBillNo());
                bus.setBill(bill);
                bus.setStatus(1);// 已匹配单据
            }

        } catch (Exception e) {

            e.printStackTrace();
            throw e;
        }
    }

    static List<BusinessDtl> convertToVo(List<BusinessDtl> dtlList,
                                         boolean isConvertUnit) {
        for (BusinessDtl dtl : dtlList) {
            Style style = CacheManager.getStyleById(dtl.getStyleId());
            dtl.setStyleName(style.getStyleName());
            // dtl.set
            dtl.setColorName(CacheManager.getColorNameById(dtl.getColorId()));
            dtl.setSizeName(CacheManager.getSizeNameById(dtl.getSizeId()));
            if (isConvertUnit) {
                Unit u = CacheManager.getUnitById(dtl.getOwnerId());
                if (CommonUtil.isBlank(u)) {
                    dtl.setOrigUnitName(dtl.getOwnerId());
                } else {
                    dtl.setOrigUnitName(u.getName());
                }
                if (CommonUtil.isNotBlank(dtl.getOrigId())) {
                    dtl.setOrigName(CacheManager.getUnitById(
                            dtl.getOrigId()).getName());
                }
                if (CommonUtil.isNotBlank(dtl.getDestUnitId())) {
                    dtl.setDestUnitName(CacheManager.getUnitById(dtl.getDestUnitId())
                            .getName());
                }
            }
        }
        return dtlList;
    }

    static List<BusinessDtl> convertToVo(List<BusinessDtl> dtlList) {
        for (BusinessDtl dtl : dtlList) {
            dtl.setStyleName(CacheManager.getStyleNameById(dtl.getStyleId()));
            dtl.setColorName(CacheManager.getColorNameById(dtl.getColorId()));
            dtl.setSizeName(CacheManager.getSizeNameById(dtl.getSizeId()));
        }
        return dtlList;
    }

    static SinglePage<BusinessDtl> convertToSinglePageVo(
            List<BusinessDtl> dtlList) {
        SinglePage<BusinessDtl> singlePage = new SinglePage<BusinessDtl>();
        long totQty = 0;
        for (BusinessDtl dtl : dtlList) {
            Style style = CacheManager.getStyleById(dtl.getStyleId());
            if (CommonUtil.isNotBlank(style)) {
                dtl.setStyleName(style.getStyleName());
            }
            // dtl.set
            dtl.setColorName(CacheManager.getColorNameById(dtl.getColorId()));
            dtl.setSizeName(CacheManager.getSizeNameById(dtl.getSizeId()));

            Unit u = CacheManager.getUnitById(dtl.getOwnerId());
            if (CommonUtil.isBlank(u)) {
                dtl.setOrigUnitName(dtl.getOwnerId());
            } else {
                dtl.setOrigUnitName(u.getName());
            }
            if (CommonUtil.isNotBlank(dtl.getOrigId())) {
                Unit storage = CacheManager.getUnitById(dtl.getOrigId());
                if (CommonUtil.isNotBlank(storage))
                    dtl.setOrigName(storage.getName());
            }
            if (CommonUtil.isNotBlank(dtl.getDestUnitId())) {
                dtl.setDestUnitName(CacheManager.getUnitById(dtl.getDestUnitId())
                        .getName());
            }

            totQty += dtl.getQty();
        }
        singlePage.setRows(dtlList);
        singlePage.addFooter("qty", "" + totQty);
        return singlePage;
    }

    static List<BoxDtl> convertToBoxDtlVos(List<BoxDtl> boxDtls) {
        for (BoxDtl dtl : boxDtls) {
            dtl.setStyleName(CacheManager.getStyleNameById(dtl.getStyleId()));
            dtl.setColorName(CacheManager.getColorNameById(dtl.getColorId()));
            dtl.setSizeName(CacheManager.getSizeNameById(dtl.getSizeId()));
        }
        return boxDtls;
    }

    public static void convertToBusVo(List<Business> rows) {
        for (Business bus : rows) {
            bus.setTotTime(bus.getEndTime().getTime()
                    - bus.getBeginTime().getTime());

            bus.setDeviceName(CommonUtil.isBlank(CacheManager.getDeviceByCode(bus.getDeviceId())) ? "" :
                    CacheManager.getDeviceByCode(bus.getDeviceId()).getName());
            // Storage s = CacheManager.getStorageById(bus.getOrigId());
            // //wing 2014-0607
            Unit s = CacheManager.getStorageById(bus.getOrigId()); // wing
            // 2014-0607
            bus.setOrigName(s == null ? "" : s.getName());
            bus.setOrigUnitName(CacheManager.getUnitById(bus.getOwnerId())
                    .getName());

            if (CommonUtil.isNotBlank(bus.getDestUnitId())) {
                Unit unit2 = CacheManager.getUnitById(bus.getDestUnitId());
                bus.setDestUnitName(unit2 == null ? bus.getDestUnitId() : unit2
                        .getName());
            }
            if (CommonUtil.isNotBlank(bus.getDestId())) {
                bus.setDestName(CacheManager.getUnitById(
                        bus.getDestId()).getName());
            }
        }

    }

    public static void convertToBusDtlVo(List<BusinessDtl> dtlList) {
        for (BusinessDtl dtl : dtlList) {
            Style style = CacheManager.getStyleById(dtl.getStyleId());
            if (CommonUtil.isNotBlank(style)) {
                dtl.setStyleName(style.getStyleName());
            }
            // dtl.set
            dtl.setColorName(CacheManager.getColorNameById(dtl.getColorId()));
            dtl.setSizeName(CacheManager.getSizeNameById(dtl.getSizeId()));

            Unit u = CacheManager.getUnitById(dtl.getOwnerId());
            if (CommonUtil.isBlank(u)) {
                dtl.setOrigUnitName(dtl.getOwnerId());
            } else {
                dtl.setOrigUnitName(u.getName());
            }
            if (CommonUtil.isNotBlank(dtl.getOrigId())) {
                Unit storage = CacheManager.getUnitById(dtl.getOrigId());
                if (CommonUtil.isNotBlank(storage))
                    dtl.setOrigName(storage.getName());
            }
            /*if (CommonUtil.isNotBlank(dtl.getDestUnitId())) {
				dtl.setDestUnitName(CacheManager.getUnitByCode(dtl.getDestUnitId())
						.getName());
			}*/

        }
    }

    static void convertToBoxVos(List<Box> boxs) throws Exception {
        for (Box box : boxs) {
            box.setUnitName(CacheManager.getUnitById(box.getOwnerId())
                    .getName());
            box.setTokenName(PropertyUtil.getValue(Integer.toString(box
                    .getToken())));
        }

    }

    /**
     * object==>dtl.ownerId,dtl.sku,dtl.styleId,dtl.colorId,dtl.sizeId,sum(dtl.
     * qty) as sumQty categories = ['男式西服', '女士西服', '衬衫',
     * '羽绒服','马甲','背心','牛仔裤','休闲裤']; series = [{name: '第一季度',data:
     * [370,162,13,13,370,162,13,13]}];
     *
     * @param l
     * @return
     */
    static String convertToChartResult(List<Object> l) {
        String categories = "";
        String data = "";
        String name = "";
        for (Object o : l) {
            Object[] os = (Object[]) o;
            name = (String) os[0];
            categories += (",\"" + os[1]) + "\"";
            data += ("," + os[5]);
        }
        String result = "{\"categories\":[" + categories.substring(1) + "],"
                + "\"series\":[{" + "\"name\":\""
                + CacheManager.getUnitById(name).getName() + "\",\"data\":["
                + data.substring(1) + "]}]}";
        return result;
    }

    static String convertToChartResult(List<Object> l,
                                       List<BusinessDtl> saleObjs) {
        String categories = "";
        String data = "";
        String data2 = "";
        String name = "试衣量";
        String name2 = "销售量";
        for (Object o : l) {
            Object[] os = (Object[]) o;
            // name = (String) os[0];
            categories += (",\"" + os[1]) + "\"";
            data += ("," + os[5]);
            boolean have = false;
            for (BusinessDtl dtl : saleObjs) {
                if (dtl.getSku().equals(os[1])) {
                    have = true;
                    data2 += ("," + dtl.getQty());
                    break;
                }
            }
            if (!have) {
                data2 += ("," + 0);
            }
        }
        String result = "{\"categories\":[" + categories.substring(1) + "],"
                + "\"series\":[{" + "\"name\":\"" + name + "\",\"data\":["
                + data.substring(1) + "]},{" + "\"name\":\"" + name2
                + "\",\"data\":[" + data2.substring(1) + "]" + "}] }";
        return result;
    }

    static List<DtlDiffVo> diffDtl(List<BusinessDtl> busDtls1,
                                   List<BusinessDtl> busDtls2) {
        List<DtlDiffVo> diffList = new ArrayList<DtlDiffVo>();
        DtlDiffVo diff = null;
        for (BusinessDtl dtl1 : busDtls1) {
            diff = copyProperty(dtl1);
            for (BusinessDtl dtl2 : busDtls2) {
                if (diff.getSku().equals(dtl2.getSku())) {
                    diff.setQty2(dtl2.getQty());
                    continue;
                }
            }
            diffList.add(diff);
        }
        return diffList;
    }

    private static DtlDiffVo copyProperty(BusinessDtl dtl) {
        DtlDiffVo diff = new DtlDiffVo();
        diff.setSku(dtl.getSku());
        diff.setStyleId(dtl.getStyleId());
        diff.setColorId(dtl.getColorId());
        diff.setSizeId(dtl.getSizeId());
        diff.setStyleName(CacheManager.getStyleNameById(diff.getStyleId()));
        diff.setColorName(CacheManager.getColorNameById(diff.getColorId()));
        diff.setSizeName(CacheManager.getSizeNameById(diff.getSizeId()));
        diff.setQty1(dtl.getQty());

        return diff;
    }

    static String getSkuSql(String jsonResult) {
        int index1 = jsonResult.indexOf("[");
        int index2 = jsonResult.indexOf("]");
        jsonResult = jsonResult.substring(index1 + 1, index2);
        return jsonResult.replace("\"", "'");
    }

    static Map<String, StringBuffer> getParametersInWith(
            ServletRequest request, String flag) {
        Enumeration paramNames = request.getParameterNames();
        Map<String, StringBuffer> params = new TreeMap<String, StringBuffer>();
        if (flag == null) {
            flag = "";
        }
        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            if ("".equals(flag) || paramName.contains(flag)) {
                StringBuffer sb = new StringBuffer();
                String[] values = request.getParameterValues(paramName);
                if (values == null || values.length == 0) {
                    // Do nothing, no values found at all.
                } else if (values.length > 1) {
                    params.put(paramName.replace("_", "."),
                            sb.append(values[0]));
                } else {
                    if (values[0] == null || values[0].equals(""))// 当values[0]为“”时，不执行
                        continue;
                    params.put(paramName.replace("_", "."),
                            sb.append(values[0]));
                }
            }
        }
        return params;
    }

    static void writeDataToDir(Map<String, StringBuffer> strMap, String filePath) {
        String taskPath = "";
        for (String key : strMap.keySet()) {
            if (key.contains(".tsk")) {
                String dir = key.substring(0, key.indexOf(".tsk"));
                taskPath = filePath + "\\" + dir;
                File taskDir = new File(taskPath);
                taskDir.mkdir();// 创建目录
                String taskFileName = taskDir.getAbsolutePath() + "\\" + key;
                FileUtil.writeStringToFile(strMap.get(key).toString(),
                        taskFileName);
                break;
            }
        }
        for (String key : strMap.keySet()) {
            if (key.contains(".tsk"))
                continue;

            FileUtil.writeStringToFile(strMap.get(key).toString(), taskPath
                    + "\\" + key);
        }
    }

    static String filterRecordProperty(List<Record> records) {
        PropertyFilter filter = new PropertyFilter() {

            @Override
            public boolean apply(Object source, String name, Object value) {
                return "code".equals(name);
            }
        };
        SerializeWriter sw = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(sw);
        serializer.getPropertyFilters().add(filter);
        serializer.write(records);
        return sw.toString();
    }

    /**
     * 为hongdou添加任务明细文件下载功能
     *
     * @param taskId
     * @param detailList
     * @return
     * @throws Exception
     */
    public static File writeTaskExcelFile(String taskId,
                                          List<BusinessDtl> detailList) throws Exception {
        HSSFWorkbook taskExcelBook = new HSSFWorkbook();
        HSSFSheet sheet = taskExcelBook.createSheet("RFID清点明细");
        HSSFRow row = null;
        HSSFCell cell = null;
        row = sheet.createRow(0);
        cell = row.createCell(0);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("条码");

        cell = row.createCell(1);
        cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        cell.setCellValue("数量");
        int totQty = 0;
        for (int i = 1; i <= detailList.size(); i++) {
            row = sheet.createRow(i);
            BusinessDtl dtl = detailList.get(i - 1);
            Size size = CacheManager.getSizeById(dtl.getSizeId());
            String barcode = dtl.getStyleId() + dtl.getColorId()
                    + (size == null ? dtl.getSizeId() : size.getSizeId());

            cell = row.createCell(0);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(barcode);

            cell = row.createCell(1);
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(dtl.getQty());

            totQty += dtl.getQty();
        }
        File file = new File(PropertyUtil.getValue("MilanUploadHistory")
                + File.separatorChar + taskId + "_" + totQty + ".xls");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        taskExcelBook.write(fos);
        fos.flush();
        fos.close();
        return file;
    }

    /**
     * 为attos添加任务明细文件下载功能
     *
     * @param taskId
     * @param detailList
     * @return
     * @throws Exception
     */
    public static File writeTaskTxtFile(String taskId,
                                        List<BusinessDtl> detailList, boolean haveTitle) throws Exception {
        String title = "条码,数量  \r\n";
        StringBuffer sb = new StringBuffer();
        if (haveTitle) {
            sb = new StringBuffer(title);
        }
        int totQty = 0;
        for (BusinessDtl dtl : detailList) {
            Size size = CacheManager.getSizeById(dtl.getSizeId());
            String barcode = dtl.getStyleId() + dtl.getColorId()
                    + (size == null ? dtl.getSizeId() : size.getSizeId());
            sb.append(barcode);
            sb.append(",");
            sb.append(dtl.getQty());
            sb.append("\r\n");
            totQty += dtl.getQty();
        }

        File file = new File(PropertyUtil.getValue("MilanUploadHistory")
                + File.separatorChar + taskId + "_" + totQty + ".txt");
        FileUtil.writeStringToFile(sb.toString(), file.getAbsolutePath());
        return file;
    }

    public static void convertToCloudVo(List<CloudBusiness> businessList) {
        for (CloudBusiness bus : businessList) {
            if (CommonUtil.isNotBlank(bus.getStorageId())) {
                bus.setStorageName(CacheManager.getStorageById(
                        bus.getStorageId()).getName());
            }
            bus.setUnitName(CacheManager.getUnitById(bus.getOwnerId())
                    .getName());
        }
    }

    public static void convertToCloudDtlVo(List<CloudBusinessDtl> businessList) {
        for (BusinessDtl dtl : businessList) {
            dtl.setStyleName(CacheManager.getStyleNameById(dtl.getStyleId()));
            dtl.setColorName(CacheManager.getColorNameById(dtl.getColorId()));
            dtl.setSizeName(CacheManager.getSizeNameById(dtl.getSizeId()));
        }
    }

    static List<CloudBoxDtl> convertToCloudBoxDtlVos(List<CloudBoxDtl> boxDtls) {
        for (BoxDtl dtl : boxDtls) {
            dtl.setStyleName(CacheManager.getStyleNameById(dtl.getStyleId()));
            dtl.setColorName(CacheManager.getColorNameById(dtl.getColorId()));
            dtl.setSizeName(CacheManager.getSizeNameById(dtl.getSizeId()));
        }
        return boxDtls;
    }

    /**
     * 根据单据的仓库Id修改任务的仓库ID
     */
    public static void adjustStorageId(Business bus) {
        Bill bill = bus.getBill();
        if (CommonUtil.isBlank(bill)) {
            return;
        }
        if (bus.getType().intValue() == Constant.TaskType.Inbound) {
            adjustInbound(bus, bill);
            return;
        }
        if (bus.getType().intValue() == Constant.TaskType.Outbound) {
            adjustOutbound(bus, bill);
            return;
        }
        if (bus.getType().intValue() == Constant.TaskType.Inventory) {
            adjustInventory(bus, bill);
            return;
        }
    }

    public static List<EpcStock> recordConvertEpsStock(List<Record> listRecords) {
        List<EpcStock> listStock = null;
        if (CommonUtil.isNotBlank(listRecords)) {
            listStock = new ArrayList<EpcStock>();
            for (Record record : listRecords) {
                EpcStock epcStock = new EpcStock();
                epcStock.setCode(record.getCode());
                epcStock.setStyleId(record.getStyleId());
                epcStock.setColorId(record.getColorId());
                epcStock.setSizeId(record.getSizeId());
                epcStock.setTaskId(record.getTaskId());
                epcStock.setToken(record.getToken());
                epcStock.setId(record.getCode());
                epcStock.setSku(record.getSku());
                epcStock.setOwnerId(record.getOwnerId());
                if (record.getType() == Constant.TaskType.Inbound) {
                    epcStock.setWarehouseId(record.getDestId());
                    epcStock.setWarehouse2Id(record.getOrigId());

                    epcStock.setOwner2Id(CommonUtil.isBlank(CacheManager.getUnitById(record.getOrigUnitId())) ? null
                            : CacheManager.getUnitById(record.getOrigUnitId())
                            .getOwnerId());
                } else if (record.getType() == Constant.TaskType.Outbound) {
                    epcStock.setWarehouseId(record.getOrigId());
                    epcStock.setWarehouse2Id(record.getDestId());

                    epcStock.setOwner2Id(CommonUtil.isBlank(CacheManager.getUnitById(record.getDestUnitId())) ? null
                            : CacheManager.getUnitById(record.getDestUnitId())
                            .getOwnerId());
                }

                epcStock.setDeviceId(record.getDeviceId());
                epcStock.setUpdateDate(record.getScanTime());
                if (CommonUtil.isNotBlank(record.getPrice())) {
                    epcStock.setStockPrice(record.getPrice());
                }
                if (CommonUtil.isNotBlank(record.getExtField())) {
                    epcStock.setInSotreType(record.getExtField());
                }
                //解析库位号
                String rackId = null;
                String levelId = null;
                String allocationId = null;
                if(CommonUtil.isNotBlank(record.getRmId())){
                    String[] rmId = record.getRmId().split("-");
                    if (rmId.length == 4) {
                        rackId = rmId[1];
                        levelId = rmId[2];
                        allocationId = rmId[3];
                    }
                }
                switch (record.getToken().intValue()) {
                    case Constant.Token.Storage_Adjust_Inbound:
                    case Constant.Token.Shop_Adjust_Inbound:
                        epcStock.setFloorRack(rackId);
                        epcStock.setFloorArea(levelId);
                        epcStock.setFloorAllocation(allocationId);
                        epcStock.setInStock(1);
                        epcStock.setProgress(EpcStock.ADJUSTING);
                        break;
                    case Constant.Token.Storage_Refund_Inbound:
                    case Constant.Token.Shop_Sales_refund:
                        epcStock.setFloorRack(rackId);
                        epcStock.setFloorArea(levelId);
                        epcStock.setFloorAllocation(allocationId);
                        epcStock.setProgress(EpcStock.REFUNDING);
                        epcStock.setInStock(1);
                        break;
                    case Constant.Token.Storage_Inbound:
                    case Constant.Token.Storage_Inbound_customer:
                    case Constant.Token.Storage_Consigment_Inbound:
                    case Constant.Token.Shop_Inbound:
                        epcStock.setFloorRack(rackId);
                        epcStock.setFloorArea(levelId);
                        epcStock.setFloorAllocation(allocationId);
                        epcStock.setProgress(EpcStock.INSOTRAGE);
                        epcStock.setInStock(1);
                        break;
                    case Constant.Token.Shop_Transfer_Inbound:
                    case Constant.Token.Storage_Transfer_Inbound:
                        epcStock.setFloorRack(rackId);
                        epcStock.setFloorArea(levelId);
                        epcStock.setFloorAllocation(allocationId);
                        epcStock.setProgress(EpcStock.TRANING);
                        epcStock.setInStock(1);
                        break;
                }
                listStock.add(epcStock);
            }


        }
        return listStock;
    }

    public static List<String> getRecordCodes(List<Record> list) {
        List<String> codeList = new ArrayList<String>();
        for (Record dtl : list) {
            codeList.add(dtl.getCode());
        }
        return codeList;
    }

    /**
     * 把超过1000的申请号集合拆分成数量splitNum的多组sql的in 集合。
     *
     * @param sqhList    申请号的List
     * @param columnName SQL中引用的字段名例如： Z.SHENQINGH
     * @return
     */
    public static String getSqlStrByList(List<String> sqhList, Class<?> c,
                                         String columnName) {
        StringBuffer sql = new StringBuffer("");
        if (sqhList != null) {
            sql.append(" ").append(c.getSimpleName().toLowerCase()).append(".")
                    .append(columnName).append(" IN ( ");
            for (int i = 0; i < sqhList.size(); i++) {
                sql.append("'").append(sqhList.get(i) + "',");
                if ((i + 1) % 999 == 0 && (i + 1) < sqhList.size()) {
                    sql.deleteCharAt(sql.length() - 1);
                    sql.append(" ) OR ")
                            .append(c.getSimpleName().toLowerCase())
                            .append(".").append(columnName).append(" IN (");
                }
            }
            sql.deleteCharAt(sql.length() - 1);
            sql.append(" )");
        }
        return sql.toString();
    }

    static List<String> getDifferRecordCodes(List<Record> list,
                                             List<String> codeList) {
        List<String> copyList = new ArrayList<String>();
        if (CommonUtil.isNotBlank(list)) {
            for (Record dtl : list) {
                copyList.add(dtl.getCode());
                codeList.remove(dtl.getCode());
            }
        }
        return copyList;
    }

    public static List<String> getDifferEpcStockCodes(List<EpcStock> list,
                                                      List<String> codeList) {
        List<String> copyList = new ArrayList<String>();
        if (CommonUtil.isNotBlank(list)) {
            for (EpcStock dtl : list) {
                copyList.add(dtl.getCode());
                codeList.remove(dtl.getCode());
            }
        }
        return copyList;
    }

    static void adjustInventory(Business bus, Bill bill) {
        bus.setOrigId(bill.getOrigUnitId());
        switch (bus.getToken()) {
            case Constant.Token.Storage_Inventory:// = 9;
//			bus.setStorageType(Constant.UnitType.Warehouse);
                break;
            case Constant.Token.Shop_Inventory:// = 16;
//			bus.setStorageType(Constant.UnitType.Shop);
                break;
        }
        copyBusinessUnitInfo(bus);
    }

    static void adjustOutbound(Business bus, Bill bill) {
        bus.setOrigId(bill.getOrigUnitId());
        Unit unit2 = CacheManager.getUnitById(bill.getDestUnitId());
//		bus.setUnit2Type(CommonUtil.isNotBlank(unit2) ? unit2.getType() : null);
        switch (bus.getToken()) {
            // case Constant.Token.Label_Data_Send:// = 5;//品牌商将标签发放给供应商
            // case Constant.Token.Factory_Box_Send:// = 7;//加工厂装箱发货
            case Constant.Token.Storage_Refund_Outbound:// = 26;//退货给供应商
//			bus.setStorageType(Constant.UnitType.Warehouse);
                bus.setDestUnitId(unit2.getId());
                break;
            case Constant.Token.Storage_Transfer_Outbound:// = 24;
//			bus.setStorageType(Constant.UnitType.Warehouse);
                bus.setDestUnitId(unit2.getId());
                break;
            case Constant.Token.Storage_Outbound:// = 10;
//			bus.setStorageType(Constant.UnitType.Warehouse);
                if (CacheManager.getUnitById(unit2.getId()).getType() == Constant.UnitType.Shop) {
                    bus.setDestUnitId(unit2.getId());
                } else {// 代理商
                    bus.setDestUnitId(unit2.getId());
                    for (Unit unit : CacheManager.getUnitMap().values()) {
                        if (unit.getOwnerId().equals(bus.getDestUnitId())) {
                            bus.setDestId(unit.getId());
                            break;
                        }
                    }
                }
                break;
            case Constant.Token.Storage_Adjust_Outbound:// = 28;
//			bus.setStorageType(Constant.UnitType.Warehouse);
                bus.setDestUnitId(unit2.getId());
                break;

            case Constant.Token.Shop_Sales:// = 15;
//			bus.setStorageType(Constant.UnitType.Shop);
                bus.setDestUnitId(null);
                bus.setDestId(null);
                break;
            case Constant.Token.Shop_Refund_Outbound:// = 27;
//			bus.setStorageType(Constant.UnitType.Shop);
                bus.setDestUnitId(unit2.getId());
                break;
            case Constant.Token.Shop_Adjust_Outbound:// = 30;
//			bus.setStorageType(Constant.UnitType.Shop);
                bus.setDestUnitId(null);
                bus.setDestId(null);
                break;
            case Constant.Token.Shop_Transfer_Outbound:// = 18;
//			bus.setStorageType(Constant.UnitType.Warehouse);
                bus.setDestUnitId(unit2.getId());
                break;

        }

        copyBusinessUnitInfo(bus);
    }

    static void adjustInbound(Business bus, Bill bill) {
        bus.setOrigId(bill.getDestUnitId());
        Unit sendUnit = CacheManager.getUnitById(bill.getOrigUnitId());
//		bus.setUnit2Type(CommonUtil.isNotBlank(sendUnit) ? sendUnit.getType()
//				: null);
        switch (bus.getToken()) {
            // case Constant.Token.Label_Data_Receive:// = 4;//品牌商标签接收
            // case Constant.Token.Label_Data_Feedback:// = 6;//供应商接收到标签
            case Constant.Token.Storage_Inbound:// = 8;//
//			bus.setStorageType(Constant.UnitType.Warehouse);
                bus.setDestUnitId(sendUnit.getId());
                break;
            case Constant.Token.Storage_Refund_Inbound:// = 23;//代理商或门店退给总部
//			bus.setStorageType(Constant.UnitType.Warehouse);
                if (CacheManager.getUnitById(sendUnit.getId()).getType() == Constant.UnitType.Shop) {
                    bus.setDestUnitId(sendUnit.getId());
                } else {// 代理商
                    bus.setDestUnitId(sendUnit.getId());
                    for (Unit unit : CacheManager.getUnitMap().values()) {
                        if (unit.getOwnerId().equals(bus.getDestUnitId())) {
                            bus.setDestId(unit.getId());
                            break;
                        }
                    }
                }
                break;
            case Constant.Token.Storage_Transfer_Inbound:// = 25;
//			bus.setStorageType(Constant.UnitType.Warehouse);
                bus.setDestUnitId(sendUnit.getId());

                break;
            case Constant.Token.Storage_Adjust_Inbound:// = 29;
//			bus.setStorageType(Constant.UnitType.Warehouse);
                bus.setDestUnitId(sendUnit.getId());

                break;
            case Constant.Token.Shop_Adjust_Inbound:// = 31;
//			bus.setStorageType(Constant.UnitType.Shop);
                bus.setDestUnitId(sendUnit.getId());
                // bus.setDestId(null);
                break;
            case Constant.Token.Shop_Inbound:// = 14;//门店入库
//			bus.setStorageType(Constant.UnitType.Shop);
                bus.setDestUnitId(sendUnit.getId());
                // bus.setDestId(unit2.getId());
                break;
            case Constant.Token.Shop_Transfer_Inbound:// = 19;
//			bus.setStorageType(Constant.UnitType.Shop);
                bus.setDestUnitId(sendUnit.getId());
                // bus.setDestId(unit2.getId());
                break;
            case Constant.Token.Shop_Sales_refund:// = 17;
//			bus.setStorageType(Constant.UnitType.Shop);
                bus.setDestUnitId(null);
                bus.setDestId(null);
                break;
        }

        copyBusinessUnitInfo(bus);

    }

    private static void copyBusinessUnitInfo(Business bus) {
        for (BusinessDtl dtl : bus.getDtlList()) {
            dtl.setOrigId(bus.getOrigId());
//			dtl.setStorageType(bus.getStorageType());
//			dtl.setUnit2Type(bus.getUnit2Type());
            dtl.setDestUnitId(bus.getDestUnitId());
            dtl.setDestId(bus.getDestId());
        }
        for (Box box : bus.getBoxList()) {
//			box.setStorageId(bus.getOrigId());
//			box.setStorageType(bus.getStorageType());
//			box.setUnit2Type(bus.getUnit2Type());
//			box.setUnit2Id(bus.getDestUnitId());
//			box.setStorage2Id(bus.getDestId());
//			for (BoxDtl dtl : box.getBoxDtls()) {
//				dtl.setStorageId(bus.getOrigId());
//				dtl.setStorageType(bus.getStorageType());
//				dtl.setUnit2Type(bus.getUnit2Type());
//				dtl.setUnit2Id(bus.getDestUnitId());
//				dtl.setStorage2Id(bus.getDestId());
//			}
        }
        for (Record r : bus.getRecordList()) {
            r.setOrigId(bus.getOrigId());
//			r.setStorageType(bus.getStorageType());
//			r.setUnit2Type(bus.getUnit2Type());
            r.setDestUnitId(bus.getDestUnitId());
            r.setDestId(bus.getDestId());
        }
    }

    public static File writeTaskExcelFile(List<BusinessDtl> detailList)
            throws Exception {
        HSSFWorkbook taskExcelBook = new HSSFWorkbook();
        HSSFSheet sheet = taskExcelBook.createSheet("RFID清点明细");
        HSSFRow row = null;
        HSSFCell cell = null;

        row = sheet.createRow(0);

        cell = row.createCell(0);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("任务日期");

        cell = row.createCell(1);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("任务编号");

        cell = row.createCell(2);
        cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        cell.setCellValue("组织名称1");

        cell = row.createCell(3);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("组织名称2");

        cell = row.createCell(4);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("产品代码");

        cell = row.createCell(5);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("产品名称");

        cell = row.createCell(6);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("颜色代码");

        cell = row.createCell(7);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("颜色名称");

        cell = row.createCell(8);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("尺寸代码");

        cell = row.createCell(9);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("尺寸名称");

        cell = row.createCell(10);
        cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        cell.setCellValue("数量");
        int totQty = 0;
        for (int i = 1; i <= detailList.size(); i++) {
            row = sheet.createRow(i);
            BusinessDtl dtl = detailList.get(i - 1);

            cell = row.createCell(0);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(dtl.getTaskId().substring(3, 11));

            cell = row.createCell(1);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(dtl.getTaskId());

            cell = row.createCell(2);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(CacheManager.getUnitById(dtl.getOrigId())
                    .getName());

            cell = row.createCell(3);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            if (CommonUtil.isBlank(dtl.getDestUnitId())) {
                cell.setCellValue("");
            } else {
                cell.setCellValue(CacheManager.getUnitById(dtl.getDestUnitId())
                        .getName());
            }

            cell = row.createCell(4);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(dtl.getStyleId());

            Style style = CacheManager.getStyleById(dtl.getStyleId());
            cell = row.createCell(5);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(style == null ? dtl.getStyleId() : style
                    .getStyleName());

            cell = row.createCell(6);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(dtl.getColorId());
            cell = row.createCell(7);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(CacheManager.getColorNameById(dtl.getColorId()));

            cell = row.createCell(8);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(dtl.getSizeId());
            cell = row.createCell(9);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(CacheManager.getSizeNameById(dtl.getSizeId()));

            cell = row.createCell(10);
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(dtl.getQty());

            totQty += dtl.getQty();
        }
        File file = new File(PropertyUtil.getValue("MilanUploadHistory")
                + File.separatorChar
                + CommonUtil.getDateString(new Date(), "yyyyMMddHHmmss") + "_"
                + totQty + ".xls");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        taskExcelBook.write(fos);
        fos.flush();
        fos.close();
        return file;
    }
}
