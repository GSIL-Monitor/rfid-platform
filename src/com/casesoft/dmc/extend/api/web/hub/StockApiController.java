package com.casesoft.dmc.extend.api.web.hub;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.stock.StockUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.FileUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.util.page.SinglePage;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.api.web.ApiBaseController;
import com.casesoft.dmc.model.cfg.Device;
import com.casesoft.dmc.model.erp.BillDtl;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.search.DetailStockView;
import com.casesoft.dmc.model.stock.EpcStock;
import com.casesoft.dmc.model.stock.Inventory;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.search.DetailStockViewService;
import com.casesoft.dmc.service.stock.EpcStockService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by WingLi on 2017-01-04.
 */
@Controller
@RequestMapping(value = "/api/hub/stock", method = {RequestMethod.POST, RequestMethod.GET})
@Api(description = "仓库库存、门店库存、ERP库存获取数据接口")
public class StockApiController extends ApiBaseController {

    @Autowired
    private EpcStockService epcStockService;

    @Autowired
    private DetailStockViewService detailStockViewService;

    /* 提供给设备统计并查询实时库存
     * @return
     */
    @RequestMapping(value = "/countAndFindStockWS.do")
    @ResponseBody
    public SinglePage<Inventory> countAndFindStockWS(String filter_EQS_deviceId, String filter_LIKES_sku, String ownerId) {
        this.logAllRequestParams();
        try {

            String deviceId = this.getReqParam("filter_EQS_deviceId");
            Device device = CacheManager.getDeviceByCode(deviceId);
            Unit storage = CacheManager.getUnitById(device.getStorageId());
            String warehouse1Id = storage.getId();

            String sku = this.getReqParam("filter_LIKES_sku");
            String styleId = null;
            List<BillDtl> list = this.epcStockService.findEpcStockSKUByWharehousId(
                    warehouse1Id, sku, ownerId, styleId);
            SinglePage<Inventory> sp = StockUtil.billConvertToPageVo(list);
            return sp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/pageWS.do")
    @ResponseBody
    public MessageBox findPage(String pageSize, String pageNo, String sortIds, String orders, String userId) throws Exception {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        Page<DetailStockView> page = new Page<>(Integer.parseInt(pageSize));
        page.setPage(Integer.parseInt(pageNo));
        if (CommonUtil.isNotBlank(sortIds)) {
            if (sortIds.split(",").length != orders.split(",").length) {
                return new MessageBox(false, "排序字段与排序方向的个数不相等");
            }
            page.setOrderBy(sortIds);
            page.setOrder(orders);
        }
        page.setPageProperty();
        User user = CacheManager.getUserById(userId);
        page = this.detailStockViewService.findPage(page, filters, user.getOwnerId());
        return new MessageBox(true, "ok", page.getRows());

    }

    @Override
    public String index() {
        return null;
    }


    @RequestMapping(value = "/sumInfoOfStockWS.do")
    @ResponseBody
    public MessageBox sumInfoOfStock(String sku, String userId) throws Exception {
        String warehId = null;
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        for (PropertyFilter filter:filters) {
            if(filter.getPropertyName()=="warehId");
            warehId = filter.getMatchValue().toString();
        }
        User user = CacheManager.getUserById(userId);
        if(CommonUtil.isBlank(sku)){
            sku="";
        }
        Map<String, String> sumInfo = this.detailStockViewService.stockSumInfoBySku(sku, user.getOwnerId(), warehId);
        return new MessageBox(true, "ok", sumInfo);
    }
    private static boolean isWriting = false;
    @RequestMapping(value = "/downloadStockFileWS.do")
    public void downloadStockFileWS(HttpServletResponse response) throws Exception {
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
        String tempPath = Constant.rootPath + File.separator + PropertyUtil.getValue("caseSoft_temp");// + "\\"
        String stockTemp = new StringBuffer(tempPath).append(File.separator).append("stock").append(File.separator).toString();//sku 生成文件
        String stockZip = new StringBuffer(tempPath).append(File.separator).append("stockZip").append(File.separator).toString();
        File stockZipFile = new File(stockZip);
        if (stockZipFile.exists()) {
            FileUtil.deleteDir(stockZipFile);
        }
        stockZipFile.mkdirs();
        String sourcePath = new StringBuffer(stockTemp).append("GT").append("_").append(version).append(File.separator).toString();
        String zipFileName = new StringBuffer(stockZip).append("GT").append("_").append(version).append("_casesoft_stock.zip").toString();

//		设置未压缩路径
        File stockDir = new File(sourcePath);
        if (!stockDir.exists()) {
            stockDir.mkdirs();
        }
        /*
         * 此处一个情况为压缩文件不更新，当生成了第一个压缩文件后         *
         * */
        File zipFile = new File(zipFileName);
        isWriting = true;
        writeStocksFiles(stockDir, stockZipFile, zipFileName, sourcePath, stockTemp, version);
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

    private void writeStocksFiles(File stockDir, File stockZipFile, String zipFileName, String sourcePath, String stockTemp, Long version) {

        if (!new File(zipFileName).exists()) {
            //删除sku数量
            if (stockDir.isDirectory()) {
                File[] files = stockDir.listFiles();
                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                }
            }
            int pageSize = 10000;
            Page<EpcStock> page = new Page<EpcStock>();
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
                Page<EpcStock> stockPage = epcStockService.findPage(page, filters);
                if (CommonUtil.isNotBlank(page.getRows())) {
                    //修改图片地址
                    FileUtil.writeStringToFile(JSON.toJSONString(StockUtil.convertEpcStockList(stockPage.getRows())),
                            sourcePath + File.separator + "casesoft_stock_" + 1 + ".json");
                    isWrite = true;
                }

                if (isWrite) {
                    long totPageNum = (stockPage.getTotal() + pageSize - 1) / pageSize;//总页数
                    System.out.println("第1页：" + stockPage.getRows().size());
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
                                writePageJsonStock(taskExecutor, page, filters, new StringBuffer(sourcePath));
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
                            if (stockZipFile.isDirectory()) {
                                File skuTmpFile = new File(stockTemp);
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
                    File skuTmpFile = new File(stockTemp);
                    //删除过期版本sku
                    FileUtil.deleteDir(skuTmpFile);
                    isWriting = false;
                }
            }
        } else {
            System.out.println("已存在");
        }
    }

    private void writePageJsonStock(ThreadPoolTaskExecutor taskExecutor, Page<EpcStock> stockPage, List<PropertyFilter> filters, StringBuffer sourcePath) {
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Page<EpcStock> page = epcStockService.findPage(stockPage, filters);
                if (CommonUtil.isNotBlank(page.getRows())) {
                    System.out.println("taskExecutor:" + stockPage.getPageNo());
                    FileUtil.writeStringToFile(JSON.toJSONString(StockUtil.convertEpcStockList(stockPage.getRows())),
                            sourcePath + File.separator + "casesoft_stock_" + page.getPageNo() + ".json");
                }
            }
        });
    }
}
