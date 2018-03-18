package com.casesoft.dmc.core.controller;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WingLi on 2015-12-10.
 */
public class UrlMapperFilter implements Filter {



    private FilterConfig config;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.config = filterConfig;
        addOldUrl();
    }


    @Override
    public void destroy() {

    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        String url = req.getRequestURL().toString();//返回客户端发出请求时的完整URL
        String request_uri = req.getRequestURI();//返回请求行中的资源名部分
        String contextPath = req.getContextPath();



        String uri = request_uri.substring(contextPath.length());
        String newUri = getMapperUrl(uri);

        if(uri.equals(newUri)) {
            chain.doFilter(req, resp);
        } else {//老url
            //String URL = url.substring(0,url.length()-uri.length()+1)+newUri;
            //resp.sendRedirect(URL);
            request.getRequestDispatcher(newUri).forward(request,response);
        }
    }


    static Map<String,String> urlMap = new HashMap<>();
    private String getMapperUrl(String url) {
        for(Map.Entry<String,String> entry : urlMap.entrySet()) {
            if(url.contains(entry.getKey())) {
                url = entry.getValue();
                break;
            }
        }
        return url;
    }
    private static void addOldUrl() {
        //region UnitInfo
        urlMap.put("unitAction!listUnitWS","/api/hub/base/listUnitWS.do");
        urlMap.put("deviceAction!findUnitByDeviceWS","/api/hub/base/findUnitByDeviceWS.do");
        urlMap.put("deviceAction!getStorageInfoWS","/api/hub/base/findWarehByDeviceWS.do");
        urlMap.put("deviceAction!validDeviceWS","/api/hub/device/validDeviceWS.do");

        //endregion

        //region UserInfo
        urlMap.put("userAction!listUserWS","/api/hub/base/listUserWS.do"); //用户信息
        //endregion


        //region ProductInfo
        urlMap.put("productAction!findProductListWS","/api/hub/product/findProductListWS.do");
        urlMap.put("productAction!findProductListByVersionWS","/api/hub/product/findProductListByVersionWS.do");
        urlMap.put("productAction!downloadProductZip","/api/hub/product/downloadProductZipFileWS.do");//
        urlMap.put("propertyKeyAction!listPropertyKeyWS","/api/hub/product/listPropertyKeyWS.do");
        urlMap.put("propertyKeyAction!listPropertyNameWS","/api/hub/product/listPropertyNameWS.do");

        urlMap.put("productAction!findEpcListWS","/api/hub/product/findEpcBindListWS.do");
        urlMap.put("downloadPrintFileWS","/api/hub/product/downloadPrintFileWS.do");
        urlMap.put("initAction!updateWS","/api/hub/product/updatePrintStatusWS.do");
        urlMap.put("initAction!boundEpcWS","/api/hub/product/boundEpcWS.do");
        urlMap.put("initAction!unboundEpcWS","/api/hub/product/unboundEpcWS.do");
        urlMap.put("initAction!productEpcWS","api/hub/product/productEpcWS.do");
        urlMap.put("initAction!findPrintWS","api/hub/product/findPrintWS.do");
        urlMap.put("productAction!findEpcListByVersionWS","api/hub/product/getOldBindVersionWS.do");
        urlMap.put("recordAction!listRecordsWS.action","api/hub/product/traceRecordWS.do");
        urlMap.put("productAction!getProductVersionWS","api/hub/product/getProductVersionWS.do");

        //endregion

        //region SHOP
        urlMap.put("userAction!loginWS","/api/hub/base/loginWS.do");
        urlMap.put("saleBillAction!getRefundBillWS","/api/hub/sale/getRefundBillWS.do");
        urlMap.put("saleBillAction!posDateUploadWS","/api/hub/sale/posDataUploadWS.do");
        urlMap.put("saleBillAction!getSaleBillByNoWS","/api/hub/sale/getSaleBillByNoWS.do");
        urlMap.put("saleBillAction!updateIboxPayBillWS","/api/hub/sale/updateIboxPayBillWS.do");

        urlMap.put("customerAction!customerLikeAnakysisWS","/api/hub/sale/customerLikeAnakysisWS.do");
        urlMap.put("saleBillAction!listDtlWS.action","/api/hub/sale/listDtlWS.do");

        urlMap.put("userAction!listWS","/api/hub/sale/listCashierWS.do");  //营业员
        urlMap.put("customerAction!listWS","/api/hub/sale/listCustomerWS.do");//顾客会员

        //urlMap.put("saleConfigAction!listWS","/customer/listGradeRateWS.do");//积分增长率
        //endregion


        //region Supply
        urlMap.put("taskAction!uploadTask","/api/hub/task/uploadTask.do");
        urlMap.put("taskAction!checkEpcStockWS","/api/hub/task/checkEpcStockWS.do");
        urlMap.put("billAction!listBillsByDevice","/api/hub/bill/listBillsByDeviceWS.do");
        urlMap.put("billAction!input.action","/api/hub/bill/findERPBillDtlWS.do");
        urlMap.put("json/recordAction!listUniqueCodeWS","/api/hub/task/listRecordByTaskIdWS.do");
        urlMap.put("billAction!findErpStockWS","/api/hub/bill/findErpStockWS.do");

        urlMap.put("billAction!createBillWS","/api/hub/bill/createCooperateBill.do");
        urlMap.put("billAction!destroyBillWS","/api/hub/bill/endCooperateBill.do");
        urlMap.put("billAction!findERPBasicImgWS","/api/hub/bill/findERPBasicImgWS.do");
        //endregion

        //region fitting
        urlMap.put("scoreAction!saveScoreWS.action","/api/hub/mirror/scoreToFittingDataWS.do");
        urlMap.put("fittingRecordAction!saveWS.action","/api/hub/mirror/uploadFittingDataWS.do");
        urlMap.put("mirror/WS/listNewProduct","/api/hub/mirror/listNewProduct.do");
        urlMap.put("mirror/WS/listCollocat","/api/hub/mirror/listCollocat.do");
        urlMap.put("mirror/WS/listBrand","/api/hub/mirror/listBrand.do");
        urlMap.put("mirror/WS/listMedia.do","/api/hub/mirror/listMedia.do");
        urlMap.put("mirror/WS/listHome","/api/hub/mirror/listHome.do");
        urlMap.put("mirror/WS/listActivity","/api/hub/mirror/listActivity.do");
        urlMap.put("mirror/WS/listStar","/api/hub/mirror/listStar.do");
        urlMap.put("fittingRecordAction!pageFittingRecordWS","/api/hub/mirror/pageFittingRecordWS.do");
        //endregion

        //region KL-B接口
        urlMap.put("json/productAction!listWS.action","/api/hub/mirror/listDetonProductWS.do");
        urlMap.put("productAction!listCollocationWS.action","/api/hub/mirror/listDetonCollocationWS.do");
        urlMap.put("fittingRecordAction!saveWS.action","/api/hub/mirror/saveFittingRecordWS.do");
        //endregion

        //region stock
        urlMap.put("stockAction!countAndFindStockWS","/api/hub/stock/countAndFindStockWS.do");
        //endregion

        //region 设备日志
        urlMap.put("deviceAction!uploadDjDataWS","/api/hub/device/uploadRunLog.do");
        //endregion

        //region 样衣系统接口Hall
        urlMap.put("sampleAction!inboundWS","/api/hub/hall/inboundWS.do");
        urlMap.put("sampleAction!borrowWS","/api/hub/hall/borrowWS.do");
        urlMap.put("amsTaskAction!listWS","/api/hub/hall/listBorrowedSampleWS.do");
        urlMap.put("amsTaskAction!backWS3","/api/hub/hall/backWS3.do");
        urlMap.put("amsInventoryAction!adjustFloorWS","/api/hub/hall/inventoryAdjustWS.do");
        urlMap.put("sampleAction!outboundWS","/api/hub/hall/outboundWS.do");
        urlMap.put("sampleAction!listWS","/api/hub/hall/listTrackWS.do");
        urlMap.put("amsUserAction!listDepartmentWS","/api/hub/hall/listDepartmentWS.do");
        urlMap.put("unitAction!unitComboboxWS","/api/hub/hall/listSampleRoomWS.do");
        urlMap.put("userAction!listWS","/api/hub/hall/listBorrowerWS.do");
        urlMap.put("sampleAction!findBorrowWS","/api/hub/hall/findBorrowWS.do");
        urlMap.put("sampleAction!getSampleInfoWS","/api/hub/hall/getSampleInfoWS.do");
        urlMap.put("sampleAction!getSampleWS","/api/hub/hall/getSampleWS.do");
        urlMap.put("sampleAction!adjustFloorWS","/api/hub/hall/adjustFloorWS.do");
        urlMap.put("sampleAction!getBillInfoWS","/api/hub/hall/getBillInfoWS.do");
        urlMap.put("sampleAction!findDtlListByCodeWS","/api/hub/hall/findDtlListByCodeWS.do");
        urlMap.put("sampleAction!outboundBackWS","/api/hub/hall/outboundBackWS.do");
        urlMap.put("sampleAction!listBadTagWS","/api/hub/hall/listBadTagWS.do");
        urlMap.put("sampleAction!supplyTagInboundWS","/api/hub/hall/supplyTagInboundWS.do");
        urlMap.put("sampleAction!listInvWS.action","/api/hub/hall/listInvWS.do");
        urlMap.put("amsInventoryAction!saveWS","/api/hub/hall/saveWS.do");
        //factoryInitBill
        urlMap.put("factoryBillInitAction!findPrintBillWS.action","/api/hub/factory/findPrintBill.do");
        urlMap.put("factoryBillInitAction!findPrintBillInfoWS.action","/api/hub/factory/findPrintBillInfo.do");
        urlMap.put("downloadFactoryPrintWS!downloadPrintFile.action","/api/hub/factory/downloadPrintFile.do");
        urlMap.put("factoryBillInitAction!updateWS.action","/api/hub/factory/update.do");
        urlMap.put("factoryBillInitAction!findAllEpcWS.action","/api/hub/factory/findAllEpc.do");
        //factoryTask
        urlMap.put("factoryBillSearchAction!findBillByCodeWS.action","/api/hub/factory/findBillByCode.do");
        urlMap.put("factoryBillSearchAction!findBillWS.action","/api/hub/factory/findBill.do");

        urlMap.put("factoryTaskAction!uploadTaskWS.action","/api/hub/factory/uploadTask.do");
        urlMap.put("factoryTaskAction!getInboundWS.action","/api/hub/factory/getInbound.do");


        //factoryEmployee
        urlMap.put("userAction!findEmployeerWS.action","/api/hub/factory/findEmployeer.do");
        urlMap.put("userAction!loginConfirmWS.action","/api/hub/factory/loginConfirm.do");

        //factoryToken
        urlMap.put("billScheduleAction!findCategoryWS.action","/api/hub/factory/findCategory.do");
        urlMap.put("billScheduleAction!findTokeWS.action","/api/hub/factory/findToken.do");

        //pauseReason
        urlMap.put("pauseReasonAction!listWS.action","/api/hub/factory/findPauseList.do");

    }


}
