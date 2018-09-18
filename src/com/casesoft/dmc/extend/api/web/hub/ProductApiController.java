package com.casesoft.dmc.extend.api.web.hub;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.cache.RedisUtils;
import com.casesoft.dmc.cache.SpringContextUtil;
import com.casesoft.dmc.controller.product.ProductUtil;
import com.casesoft.dmc.controller.product.StyleUtil;
import com.casesoft.dmc.controller.stock.StockUtil;
import com.casesoft.dmc.controller.tag.InitUtil;
import com.casesoft.dmc.controller.task.TaskUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.FileUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.api.dto.RespMessage;
import com.casesoft.dmc.extend.api.web.ApiBaseController;
import com.casesoft.dmc.extend.sqlite.db.DBHelper;
import com.casesoft.dmc.extend.sqlite.model.SqliteEpcStock;
import com.casesoft.dmc.extend.sqlite.model.SqliteProduct;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.cfg.PropertyType;
import com.casesoft.dmc.model.product.*;
import com.casesoft.dmc.model.stock.EpcStock;
import com.casesoft.dmc.model.tag.Epc;
import com.casesoft.dmc.model.tag.EpcBindBarcode;
import com.casesoft.dmc.model.tag.Init;
import com.casesoft.dmc.model.tag.InitDtl;
import com.casesoft.dmc.service.cfg.PropertyKeyService;
import com.casesoft.dmc.service.log.SysLogService;
import com.casesoft.dmc.service.product.*;
import com.casesoft.dmc.service.stock.EpcStockService;
import com.casesoft.dmc.service.tag.BindService;
import com.casesoft.dmc.service.tag.InitService;
import com.j256.ormlite.dao.Dao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.apache.cxf.helpers.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.casesoft.dmc.extend.sqlite.db.DBHelper;
import com.casesoft.dmc.extend.sqlite.db.DBService;
import com.casesoft.dmc.extend.sqlite.model.SqliteProduct;


import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by WingLi on 2017-01-04.
 */
@Controller
@RequestMapping(value = "/api/hub/product", method = {RequestMethod.POST, RequestMethod.GET})
@Api(description = "商品、标签信息模块接口")
public class ProductApiController extends ApiBaseController {

    @Autowired
    private ProductService productService;
    @Autowired
    private SysLogService sysLogService;
    @Autowired
    private BindService bindService;
    @Autowired
    private InitService initService;
    @Autowired
    private PropertyKeyService propertyKeyService;
    @Autowired
    private StyleService styleService;
    @Autowired
    private ColorService colorService;
    @Autowired
    private SizeService sizeService;

    @Autowired
    private PhotoService photoService;
    @Autowired
    private EpcStockService epcStockService;

    private static RedisUtils redisUtils = (RedisUtils) SpringContextUtil.getBean("redisUtils");

    @Override
    public String index() {
        return null;
    }

    //    @ApiOperation(value = "商品信息获取", notes = "通过版本号获取商品信息")
//    @ApiResponse(code = 200, message = "success", response = String.class)
//    @RequestMapping(value = "/findProductListByVersionWS.do", produces = "application/json;charset:UTF-8")
//    @ApiAuth
//    @ResponseBody
//    public RespMessage findProductListByVersion(@Valid @RequestBody RequestEntity<Long> requestEntity, BindingResult bindingResult) {
//        Long version = requestEntity.getRequestData();
//        return findProductListByVersionWS(version);
//    }
    @RequestMapping(value = "/findProductListWS.do")
    @ResponseBody
    public List<Product> findProductListWS() {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        List<Product> products = this.productService.find(filters);
        ProductUtil.convertToPageVo(products);
        return products;
    }

    @RequestMapping(value = "/findProductListByVersionWS.do")
    @ResponseBody
    public RespMessage findProductListByVersionWS(@ApiParam @RequestParam Long version) {
        this.logAllRequestParams();
        if (CommonUtil.isBlank(version)) {
            return this.returnApiFailInfo("版本号为空");
        }
        long maxVersion = sysLogService.getVersionByTable("Product");
        if (version >= maxVersion) {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("maxVersion", maxVersion);
            result.put("updateList", new ArrayList<Product>());
            return this.returnApiSuccessInfo("已是最新版", result);
        }
        List<PropertyFilter> filters = PropertyFilter.createOneFilter(
                "GTL_version", "" + version);
        List<Product> productList = this.productService.find(filters);
        ProductUtil.convertListToVo(productList);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("maxVersion", maxVersion);
        result.put("updateList", productList);
        return this.returnApiSuccessInfo("更新条码数:" + productList.size(), result);
    }

    /**
     * 根据版本号获取EPC与条码绑定关系
     *
     * @param version
     * @return
     */
    @RequestMapping(value = "/findEpcListWS.do")
    @ResponseBody
    public List<EpcBindBarcode> findEpcList(@ApiParam @RequestParam Long version) {
        this.logAllRequestParams();
        if (CommonUtil.isBlank(version)) {
            List<EpcBindBarcode> epcBindBarcodeList = this.bindService.getAll();
            return epcBindBarcodeList;
        }
        long maxVersion = sysLogService.getVersionByTable("EpcBindBarcode");
        if (version >= maxVersion) {
            return new ArrayList<>();
        }
        List<PropertyFilter> filters = PropertyFilter.createOneFilter(
                "GTL_version", "" + version);
        List<EpcBindBarcode> productList = this.bindService.find(filters);
        return productList;
    }

    @RequestMapping(value = "/getProductVersionWS.do")
    @ResponseBody
    public RespMessage getProductVersionWS() {
        try {
            long version = this.sysLogService.getVersionByTable(Product.class
                    .getName());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("version", version);
            return this.returnApiSuccessInfo(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return this.returnApiFailInfo(e.getLocalizedMessage());
        }
    }

    /**
     * 获取EPC绑定关系最大版本号
     *
     * @return
     */
    @RequestMapping(value = "/getBindVersionWS.do")
    @ResponseBody
    public RespMessage getBindVersionWS() {
        try {
            long version = this.bindService.findMaxProductTempVersion();
            return this.returnApiSuccessInfo(version);
        } catch (Exception e) {
            e.printStackTrace();
            return this.returnApiFailInfo(e.getLocalizedMessage());
        }
    }

    @RequestMapping(value = "/getOldBindVersionWS.do")
    @ResponseBody
    public RespMessage getOldBindVersionWS() {
        try {
            long version = this.bindService.findMaxProductTempVersion();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("version", version);
            return this.returnApiSuccessInfo(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return this.returnApiFailInfo(e.getLocalizedMessage());
        }
    }

    @RequestMapping(value = "/boundEpcWS.do")
    @ResponseBody
    public MessageBox boundEpcWS(@ApiParam @RequestParam String boundInfo) throws Exception {
        this.logAllRequestParams();
        String tempStr = boundInfo;
        try {
            List<EpcBindBarcode> bindList = JSON.parseArray(tempStr, EpcBindBarcode.class);
            if (CommonUtil.isBlank(bindList)) {
                return this.returnFailInfo("绑定信息为空！绑定失败！");
            } else {
                for (EpcBindBarcode epcBind : bindList) {
                    Product p = CacheManager.getProductByCode(epcBind.getCode());
                    if (CommonUtil.isBlank(p)) {
                        for (Map.Entry<String, Product> entry : CacheManager.getProductMap().entrySet()) {
                            if (CommonUtil.isNotBlank(entry.getValue().getBarcode())
                                    && epcBind.getCode().equals(entry.getValue().getBarcode())) {
                                p = entry.getValue();
                                break;
                            }
                        }
                        if (CommonUtil.isBlank(p)) {
                            return this.returnFailInfo("绑定失败！条码错误！" + epcBind.getCode());
                        }
                    }
                    epcBind.setCode(p.getCode());
                    epcBind.setUpdateTime(new Date());
                }
                this.bindService.saveList(bindList);
                return this.returnSuccessInfo("绑定成功!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return this.returnFailInfo("绑定失败！");
        }
    }

    @RequestMapping(value = "/unboundEpcWS.do")
    @ResponseBody
    public MessageBox unboundEpcWS(@ApiParam @RequestParam String epcs) throws Exception {
        this.logAllRequestParams();
        String tempStr = epcs;

        try {
            List<String> cds = JSON.parseArray(tempStr, String.class);
            if (CommonUtil.isBlank(cds)) {
                return this.returnFailInfo("信息为空！解绑失败！");
            } else {
                String epcInSql = TaskUtil.getSqlStrByList(cds, EpcBindBarcode.class, "epc");
                this.bindService.deleteBindInEPCList(epcInSql);
                return this.returnSuccessInfo("解绑成功!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return this.returnFailInfo("解绑失败！");
        }
    }

    private static boolean isWriting = false;

    @RequestMapping(value = "/downloadProductZipFileWS.do")
    public void downloadProductZipFileWS(HttpServletResponse response) throws Exception {
        String versionStr = this.getReqParam("version");
        Long version = 0L;
        if (CommonUtil.isNotBlank(versionStr)) {
            version = Long.parseLong(versionStr);
        }
        this.getRequest().setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        System.out.println("start:" + CommonUtil.getDateString(new Date(), "yy-MM-dd HH:mm:ss"));
        while (isWriting) {//是否在生成压缩文件
            if (isWriting) {
                Thread.sleep(500);
            }
        }//

//
        String tempPath = Constant.rootPath + File.separator + PropertyUtil.getValue("caseSoft_temp");// + "\\"
        String skuTemp = new StringBuffer(tempPath).append(File.separator).append("sku").append(File.separator).toString();//sku 生成文件
        String skuZip = new StringBuffer(tempPath).append(File.separator).append("skuZip").append(File.separator).toString();
        File skuZipFile = new File(skuZip);

        if (skuZipFile.exists()) {
            FileUtil.deleteDir(skuZipFile);
        }
        skuZipFile.mkdirs();
        String sourcePath = new StringBuffer(skuTemp).append("GT").append("_").append(version).append(File.separator).toString();
        String zipFileName = new StringBuffer(skuZip).append("GT").append("_").append(version).append("_casesoft_sku.zip").toString();

//		设置未压缩路径
        File skuDir = new File(sourcePath);
        if (!skuDir.exists()) {
            skuDir.mkdirs();
        }
        /*
         * 此处一个情况为压缩文件不更新，当生成了第一个压缩文件后         *
         * */
        File zipFile = new File(zipFileName);
        isWriting = true;
        writeProductsFiles(skuDir, skuZipFile, zipFileName, sourcePath, skuTemp, version);
        try {
            if (zipFile.exists()) {
                String fileName = zipFile.getName();
                String contentType = "application/zip;charset=utf-8";
                System.out.println("end:" + CommonUtil.getDateString(new Date(), "yy-MM-dd HH:mm:ss"));
                this.outFile(response, fileName, zipFile, contentType);

            }
            System.out.println("end:" + CommonUtil.getDateString(new Date(), "yy-MM-dd HH:mm:ss"));
            isWriting = false;
            return ;
        } catch (Exception e) {
            isWriting = false;

            e.printStackTrace();
        } finally {
        }
    }

    /**
     * 写所有页文件
     *
     * @param skuDir
     * @param skuZipFile
     * @param zipFileName
     * @param sourcePath
     * @param skuTemp
     * @param version
     */
    private void writeProductsFiles(File skuDir, File skuZipFile, String zipFileName, String sourcePath, String skuTemp, Long version) {
        if (!new File(zipFileName).exists()) {
            //删除sku数量
            if (skuDir.isDirectory()) {
                File[] files = skuDir.listFiles();
                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                }
            }
            int pageSize = 10000;
            Page<Product> page = new Page<Product>();
            page.setOrderBy("code");
            page.setOrder("desc");
            page.setPageNo(1);
            page.setPageSize(pageSize);
            List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
            if (version != 0L) {
                filters.add(new PropertyFilter("GTL_version", String.valueOf(version)));
            }
            boolean isex = new File(zipFileName).exists();
            if (!isex) {
                boolean isWrite = false;
                Page<Product> prodPage = productService.findPage(page, filters);
                if (CommonUtil.isNotBlank(page.getRows())) {
                    //修改图片地址
                    FileUtil.writeStringToFile(JSON.toJSONString(ProductUtil.convertToPageVo(prodPage.getRows())),
                            sourcePath + File.separator + "casesoft_sku_" + 1 + ".json");
                    isWrite = true;
                }

                if (isWrite) {
                    long totPageNum = (prodPage.getTotal() + pageSize - 1) / pageSize;//总页数
                    System.out.println("第1页：" + prodPage.getRows().size());
                    ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
                    taskExecutor.setKeepAliveSeconds(200);
                    taskExecutor.setCorePoolSize(10);
                    taskExecutor.setQueueCapacity(10);
                    taskExecutor.setMaxPoolSize(10 + (int) totPageNum);
                    taskExecutor.initialize();
                    for (int i = 2; i <= totPageNum; i++) {
                        System.out.println("第" + i + "页：新线程");
                        page = new Page<>();
                        page.setOrderBy("code");
                        page.setOrder("desc");
                        page.setPageNo(i);
                        page.setPageSize(pageSize);
                        boolean isMax = true;
                        while (isMax) {
                            if (taskExecutor.getActiveCount() < 10) {
                                writePageJsonProduct(taskExecutor, page, filters, new StringBuffer(sourcePath));
                                break;
                            } else {
                                isMax = true;
                            }
                        }
                    }
                    boolean isEnded = false;
                    while (!isEnded) {
                        int count = taskExecutor.getActiveCount();
                        if (count == 0) {
                            isEnded = true;

                            FileUtil.zip("/" + sourcePath, zipFileName);
                            if (skuZipFile.isDirectory()) {
                                File skuTmpFile = new File(skuTemp);
                                //删除过期版本sku
                                FileUtil.deleteDir(skuTmpFile);
                                taskExecutor.shutdown();
                                isWriting = false;
                            }
                        } else {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                } else {
                    File skuTmpFile = new File(skuTemp);
                    //删除过期版本sku
                    FileUtil.deleteDir(skuTmpFile);
                    isWriting = false;
                }
            }
        } else {
            System.out.println("已存在");
        }
    }


    @RequestMapping(value = "/downloadPrintFileWS.do")
    public void downloadPrintFile(@ApiParam @RequestParam String billNo,
                                  @ApiParam @RequestParam String deviceId, @ApiParam @RequestParam String isRfid) throws Exception {
        this.logAllRequestParams();


        Assert.notNull(deviceId, "deviceId不能为空");
        String ownerId = CacheManager.getDeviceByCode(deviceId).getOwnerId();// deviceId
        // 不能为空
        boolean isRfidBool = Boolean.valueOf(isRfid);

        Init master = this.initService.get("billNo", billNo);

        List<InitDtl> details = this.initService.findDtls(billNo, ownerId);
        File inputPath;
        if ("CODE".equals(master.getImportType())) {
            List<Epc> epcs = this.initService.findEpcList(billNo);
            inputPath = InitUtil.writeTextFile(details, epcs, isRfidBool);
        } else {
            inputPath = InitUtil.writeTextFile(details, null, isRfidBool);
        }

        if (master.getStatus() == 1) {// 已确认状态
            // master.setStatus(-1);// 设置状态 打印中...
            this.initService.update(InitUtil.epcList, master);

        }


        String filename = inputPath.getName();
        String contentType = "application/zip;charset=utf-8";

        this.outFile(filename, inputPath, contentType);
    }

    /**
     * 上传打印结果 status==2 表示已打印
     *
     * @param billNo
     * @param status
     * @return
     */
    @RequestMapping(value = "/updatePrintStatusWS.do")
    @ResponseBody
    public RespMessage updatePrintStatus(@ApiParam @RequestParam String billNo,
                                         @ApiParam @RequestParam String status) {
        this.logAllRequestParams();
        Assert.notNull(billNo, "billNo不能为空");
        Assert.notNull(status, "status不能为空");
        Init initBill = this.initService.get("billNo", billNo);
        initBill.setStatus(Integer.parseInt(status));
        this.initService.update(initBill);
        return this.returnApiSuccessInfo(initBill);
    }

    @RequestMapping(value = "/listPropertyKeyWS.do")
    @ResponseBody
    public List<PropertyKey> listPropertyKeyWS() {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        List<PropertyKey> listKey = this.propertyKeyService.find(filters);
        return listKey;
    }

    @RequestMapping(value = "/listPropertyNameWS.do")
    @ResponseBody
    public List<PropertyType> listPropertyNameWS() {
        this.logAllRequestParams();
        List<PropertyType> listType = this.propertyKeyService.findPrpertyByType();
        return listType;
    }

    @RequestMapping(value = "/findEpcBindListWS.do")
    @ResponseBody
    public List<EpcBindBarcode> findEpcBindListWS() {
        String version = this.getReqParam("version");
        if (CommonUtil.isNotBlank(version)) {
            return this.findEpcList(Long.parseLong(version));
        } else {
            List<EpcBindBarcode> bindList = this.bindService.getAll();
            return bindList;
        }
    }


    /**
     * 根据条码或SKU获取EPC
     *
     * @param barcode
     * @param deviceId
     * @param qty
     * @return
     */
    @RequestMapping(value = "/productEpcWS.do")
    @ResponseBody
    public MessageBox productEpcWS(String barcode, String deviceId, String qty) {
        List<String> epcs = new ArrayList<String>();
        if (CommonUtil.isBlank(barcode)) {
            return this.returnFailInfo("无条码输入！", epcs);
        } else if (CommonUtil.isBlank(CacheManager.getDeviceByCode(deviceId))) {
            return this.returnFailInfo("设备号无效！", epcs);
        } else {
            int totQty = 1;
            try {
                if (CommonUtil.isNotBlank(qty)) {
                    totQty = Integer.parseInt(qty);
                }
                boolean iss = InitUtil.excuteBarcodeEpc(barcode, totQty, deviceId,
                        this.initService, epcs);
                if (iss) {
                    return this.returnSuccessInfo("成功！", epcs);
                } else {
                    return this.returnFailInfo("生成失败！", epcs);
                }

            } catch (Exception e) {
                e.printStackTrace();
                return this.returnFailInfo(e.getMessage());
            }
        }
    }

    /**
     * 通过barcode获取打印信息
     */

    @RequestMapping(value = "/findPrintWS.do")
    @ResponseBody
    public MessageBox findPrintWS(String barcode, String deviceId, String qty) {
        List<String> epcs = new ArrayList<String>();
        if (CommonUtil.isBlank(barcode)) {
            return this.returnFailInfo("无条码输入！", epcs);
        } else if (CommonUtil.isBlank(CacheManager.getDeviceByCode(deviceId))) {
            return this.returnFailInfo("设备号无效！", epcs);
        } else {
            int totQty = 1;
            try {
                if (CommonUtil.isNotBlank(qty)) {
                    totQty = Integer.parseInt(qty);
                }
                List<Epc> epcList = InitUtil.excuteBarcodeEpcList(barcode, totQty, deviceId,
                        this.initService, epcs);
                if (CommonUtil.isNotBlank(epcList)) {
                    return this.returnSuccessInfo("成功！", epcList);
                } else {
                    return this.returnFailInfo("生成失败！", barcode);
                }

            } catch (Exception e) {
                e.printStackTrace();
                return this.returnFailInfo(e.getMessage());
            }
        }
    }

    @RequestMapping(value = "/findImgWS")
    @ResponseBody
    public MessageBox productImage(String styleId, String colorId) {

        List<Product> productList = new ArrayList<Product>();
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        if (CommonUtil.isNotBlank(styleId)) {
            filters.add(new PropertyFilter("EQS_styleId", styleId));
        }
        if (CommonUtil.isNotBlank(colorId)) {
            filters.add(new PropertyFilter("EQS_colorId", colorId));
        }
        List<Photo> photos = this.photoService.find(filters);
        for (Photo photo : photos) {
            Product product = new Product();
            product.setStyleId(styleId);
            if (CommonUtil.isNotBlank(colorId)) {
                product.setColorId(colorId);
            } else {
                product.setColorId("-");
            }
            product.setImage("/product/photo/" + photo.getSrc());
            productList.add(product);
        }
        return returnSuccessInfo("获取成功", productList);
    }

    /**
     * @Param styleStr 款信息json字符串
     * @Param productStr 商品jsonArray字符串
     */
    @RequestMapping("/saveStyleAndProductWS")
    @ResponseBody
    public MessageBox saveStyleAndProduct(String styleStr, String productStr) throws Exception {

        Style style = JSON.parseObject(styleStr, Style.class);
        Style sty = CacheManager.getStyleById(style.getStyleId());
        //查询当前最新的版本号
        Long productMaxVersionId = CacheManager.getproductMaxVersionId();
        Long maxVersionId = CacheManager.getStyleMaxVersionId();
        sty.setVersion(maxVersionId + 1);
        if (CommonUtil.isBlank(sty)) {
            sty = new Style();
            sty.setId(style.getStyleId());
            sty.setStyleId(style.getStyleId());
            sty.setVersion(maxVersionId + 1);
        }
        List<Product> productList = JSON.parseArray(productStr, Product.class);
        List<Product> saveList = StyleUtil.covertToProductInfo(sty, style, productList);
        try {
            this.styleService.saveStyleAndProducts(sty, saveList);
            //保存成功更新缓存
            redisUtils.hset("maxVersionId", "productMaxVersionId", JSON.toJSONString(productMaxVersionId + 1));
            redisUtils.hset("maxVersionId", "styleMaxVersionId", JSON.toJSONString(maxVersionId + 1));
            CacheManager.refreshMaxVersionId();
            List<Style> styleList = new ArrayList<>();
            styleList.add(sty);
            CacheManager.refreshStyleCache(styleList);
            if (saveList.size() > 0) {
                CacheManager.refreshProductCache(saveList);
            }
            return this.returnSuccessInfo("保存成功", style);
        } catch (Exception e) {
            return this.returnFailInfo("保存失败", e.toString());
        }
    }

    //region Pairs项目接口
    @RequestMapping("/findStylesWS")
    @ResponseBody
    public MessageBox findStyles(String pageSize, String pageNo, String sortIds, String orders) throws Exception {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        Page<Style> page = new Page<>(Integer.parseInt(pageSize));
        page.setPage(Integer.parseInt(pageNo));
        if (CommonUtil.isNotBlank(sortIds)) {
            if (sortIds.split(",").length != orders.split(",").length) {
                return new MessageBox(false, "排序字段与排序方向的个数不相等");
            }
            page.setOrderBy(sortIds);
            page.setOrder(orders);
        }
        page.setPageProperty();
        page = this.styleService.findPage(page, filters);
        return this.returnSuccessInfo("获取成功", page.getRows());
    }

    @RequestMapping("/findColorsWS")
    @ResponseBody
    public MessageBox findColors(String pageSize, String pageNo, String sortIds, String orders) throws Exception {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        Page<Color> page = new Page<>(Integer.parseInt(pageSize));
        page.setPage(Integer.parseInt(pageNo));
        if (CommonUtil.isNotBlank(sortIds)) {
            if (sortIds.split(",").length != orders.split(",").length) {
                return new MessageBox(false, "排序字段与排序方向的个数不相等");
            }
            page.setOrderBy(sortIds);
            page.setOrder(orders);
        }
        page.setPageProperty();
        page = this.colorService.findPage(page, filters);
        return this.returnSuccessInfo("获取成功", page.getRows());
    }

    @RequestMapping("/findSizesWS")
    @ResponseBody
    public MessageBox findSizes(String pageSize, String pageNo, String sortIds, String orders) throws Exception {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        Page<Size> page = new Page<>(Integer.parseInt(pageSize));
        page.setPage(Integer.parseInt(pageNo));
        if (CommonUtil.isNotBlank(sortIds)) {
            if (sortIds.split(",").length != orders.split(",").length) {
                return new MessageBox(false, "排序字段与排序方向的个数不相等");
            }
            page.setOrderBy(sortIds);
            page.setOrder(orders);
        }
        page.setPageProperty();
        page = this.sizeService.findPage(page, filters);
        return this.returnSuccessInfo("获取成功", page.getRows());
    }

    @RequestMapping("/findProductDtlWS")
    @ResponseBody
    public MessageBox findProductDtl() throws Exception {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        List<Product> prodList = this.productService.find(filters);
        for (Product p : prodList) {
            p.setColorName(CacheManager.getColorNameById(p.getColorId()));
            p.setSizeName(CacheManager.getSizeNameById(p.getSizeId()));
        }
        return this.returnSuccessInfo("获取成功", prodList);
    }

    @RequestMapping("/findSizeSortWS")
    @ResponseBody
    public MessageBox findSizeSort() throws Exception {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        List<SizeSort> sizeSortList = this.sizeService.findSort(filters);
        return this.returnSuccessInfo("获取成功", sizeSortList);
    }


    private static boolean isWriteSqlite = false;
    @Autowired
    private DBService dbService;


    //@RequestMapping(value = "/downloadSqliteZipFileWS.do")
    public void downloadSqliteZipFileWS(HttpServletResponse response) throws Exception {

        System.out.println("start:" + CommonUtil.getDateString(new Date(), "yy-MM-dd HH:mm:ss"));
        this.getRequest().setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        while (isWriteSqlite) {//是否在生成压缩文件
            Thread.sleep(500);
        }

        String tempPath = Constant.rootPath + File.separator + PropertyUtil.getValue("caseSoft_temp");// + "\\"
        String sqliteZip = tempPath + File.separator + "sqliteZip";
        File skuZipFile = new File(sqliteZip);
        if (!skuZipFile.exists()) {
            skuZipFile.mkdirs();
        }
        //并发请求要考虑文件命名方式加时间戳，并考虑合适方法删除已下载的文件
        String zipFileName = sqliteZip + File.separator + "casesoft_sqlite.zip";

        File zipFile = new File(zipFileName);
        if (zipFile.exists()) {
            FileUtil.deleteDir(zipFile);
        }
        isWriteSqlite = true;
        writeSqliteFiles(skuZipFile, zipFileName);

        try {

            String fileName = zipFile.getName();
            String contentType = "application/zip;charset=utf-8";
            System.out.println("end:" + CommonUtil.getDateString(new Date(), "yy-MM-dd HH:mm:ss"));
            this.outFile(response, fileName, zipFile, contentType);
            isWriteSqlite = false;
        } catch (Exception e) {
            isWriteSqlite = false;
            e.printStackTrace();
        } finally {
        }
    }

    private void writeSqliteFiles(File skuZipFile, String zipFileName) {
        Dao<SqliteProduct, String> sqliteProductDao = DBHelper.createDao(SqliteProduct.class);
        Dao<SqliteEpcStock, String> sqliteStockDao = DBHelper.createDao(SqliteEpcStock.class);
        if (!new File(zipFileName).exists()) {
            try {
                int pageSize = 10000;
                Page<Product> searchProdPage = new Page<Product>();
                searchProdPage.setOrderBy("code");
                searchProdPage.setOrder("desc");
                searchProdPage.setPageNo(1);
                searchProdPage.setPageSize(pageSize);

                Page<EpcStock> searchStockPage = new Page<EpcStock>();
                searchStockPage.setOrderBy("code");
                searchStockPage.setOrder("desc");
                searchStockPage.setPageNo(1);
                searchStockPage.setPageSize(pageSize);
                List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
                boolean isex = new File(zipFileName).exists();
                if (!isex) {
                    boolean isWrite = false;
                    Page<Product> prodPage = productService.findPage(searchProdPage, filters);
                    Page<EpcStock> stockPage = epcStockService.findPage(searchStockPage, filters);
                    isWrite = true;
                    if (CommonUtil.isNotBlank(searchProdPage.getRows())) {
                        //修改图片地址
                        dbService.batchProducts(sqliteProductDao, transferSqlite(searchProdPage.getRows()));
                        isWrite = true;
                    }
                    if (isWrite) {
                        long totPageProdNum = (prodPage.getTotal() + pageSize - 1) / pageSize;//总页数
                        long totStockPageNum = (stockPage.getTotal() + pageSize - 1) / pageSize;
                        System.out.println("prod_第1页：" + prodPage.getRows().size());
                        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
                        taskExecutor.setKeepAliveSeconds(200);
                        taskExecutor.setCorePoolSize(10);
                        taskExecutor.setQueueCapacity(10);
                        taskExecutor.setMaxPoolSize(10 + (int) totPageProdNum + (int) totStockPageNum);
                        taskExecutor.initialize();
                        for (int i = 2; i <= totPageProdNum; i++) {
                            System.out.println("prod_第" + i + "页：新线程");
                            searchProdPage = new Page<>();
                            searchProdPage.setOrderBy("code");
                            searchProdPage.setOrder("desc");
                            searchProdPage.setPageNo(i);
                            searchProdPage.setPageSize(pageSize);
                            boolean isMax = true;
                            while (isMax) {
                                if (taskExecutor.getActiveCount() < 10) {
                                    writeSqlitePageProduct(taskExecutor, sqliteProductDao, searchProdPage, filters);
                                    break;
                                } else {
                                    isMax = true;
                                }
                            }
                        }

                        for (int i = 1; i <= totStockPageNum; i++) {
                            System.out.println("stockPage_第" + i + "页：新线程");
                            searchStockPage = new Page<>();
                            searchStockPage.setOrderBy("code");
                            searchStockPage.setOrder("desc");
                            searchStockPage.setPageNo(i);
                            searchStockPage.setPageSize(pageSize);
                            boolean isMax = true;
                            while (isMax) {
                                if (taskExecutor.getActiveCount() < 10) {
                                    writeSqlitePageStock(taskExecutor, sqliteStockDao, searchStockPage, filters);
                                    break;
                                } else {
                                    isMax = true;
                                }
                            }
                        }
                        boolean isEnded = false;
                        while (!isEnded) {
                            int count = taskExecutor.getActiveCount();
                            if (count == 0) {
                                isEnded = true;
                                if (skuZipFile.isDirectory()) {
                                    //删除过期版本sku压缩包
                                    Boolean isSuZip = DBHelper.zipDB(zipFileName);
                                    if (isSuZip) {
                                        File[] files = skuZipFile.listFiles();
                                        for (int i = 0; i < files.length; i++) {
                                            System.out.println(files[i].getPath());
                                            if (!files[i].getPath().contains(zipFileName)) {
                                                FileUtil.deleteDir(files[i]);
                                            }
                                        }
                                    }

                                    taskExecutor.shutdown();
                                    isWriteSqlite = false;
                                }
                            } else {
                                try {
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }
                }
            } catch (Exception e) {
                isWriteSqlite = false;
                e.printStackTrace();
            }
        } else {
            System.out.println("已存在");
        }
    }


    private List<SqliteEpcStock> transferStockSqlite(List<EpcStock> epcStocks) {
        List<SqliteEpcStock> sqliteEpcStocks = new ArrayList<>();
        for (EpcStock epc : epcStocks) {
            try {
                StockUtil.convertEpcStock(epc);
                SqliteEpcStock sqliteEpcStock = new SqliteEpcStock();
                BeanUtils.copyProperties(epc, sqliteEpcStock);
                sqliteEpcStocks.add(sqliteEpcStock);
            } catch (Exception e) {
                this.logger.error("转换失败！" + e.getMessage());
                e.printStackTrace();
            }
        }
        return sqliteEpcStocks;
    }

    private void writeSqlitePageStock(ThreadPoolTaskExecutor taskExecutor, Dao<SqliteEpcStock, String> sqliteStockDao, Page<EpcStock> ecpPage, List<PropertyFilter> filters) {

        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Page<EpcStock> page = epcStockService.findPage(ecpPage, filters);
                if (CommonUtil.isNotBlank(page.getRows())) {
                    try {
                        dbService.batchEpcStocks(sqliteStockDao, transferStockSqlite(page.getRows()));
                    } catch (SQLException e) {
                        logger.error("SqlException:" + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    private List<SqliteProduct> transferSqlite(List<Product> products) {
        List<SqliteProduct> sqliteProducts = new ArrayList<>();

        if (CommonUtil.isNotBlank(products)) {
            //修改图片地址
            String contextpath = "/product/photo/";
            for (Product product : products) {
                try {
                    ProductUtil.convertToProductVo(product);
                    SqliteProduct sqliteProduct = new SqliteProduct();
                    BeanUtils.copyProperties(product, sqliteProduct);
                    sqliteProducts.add(sqliteProduct);
                } catch (Exception e) {
                    this.logger.error("转换失败！" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return sqliteProducts;
    }

    /**
     * 多线程写文件
     */
    private void writeSqlitePageProduct(ThreadPoolTaskExecutor taskExecutor, final Dao<SqliteProduct, String> sqliteProductDao,
                                        final Page<Product> prodPaget, List<PropertyFilter> filters) {
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Page<Product> page = productService.findPage(prodPaget, filters);
                if (CommonUtil.isNotBlank(page.getRows())) {
                    try {
                        dbService.batchProducts(sqliteProductDao, transferSqlite(page.getRows()));
                    } catch (SQLException e) {
                        logger.error("SqlException:" + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 多线程写文件
     */
    private void writePageJsonProduct(ThreadPoolTaskExecutor taskExecutor, final Page<Product> prodPaget, List<PropertyFilter> filters, StringBuffer sourcePath) {
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Page<Product> page = productService.findPage(prodPaget, filters);
                if (CommonUtil.isNotBlank(page.getRows())) {
                    System.out.println("taskExecutor:" + prodPaget.getPageNo());
                    FileUtil.writeStringToFile(JSON.toJSONString(ProductUtil.convertToPageVo(prodPaget.getRows())),
                            sourcePath + File.separator + "casesoft_sku_" + page.getPageNo() + ".json");
                }
            }
        });
    }


    //endregion
}
