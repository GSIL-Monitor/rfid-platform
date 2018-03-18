package com.casesoft.dmc.extend.api.web.hub;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.shop.ShopUtil;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.json.FastJSONUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.api.vo.OldProduct;
import com.casesoft.dmc.extend.api.web.ApiBaseController;
import com.casesoft.dmc.model.cfg.Device;
import com.casesoft.dmc.model.mirror.*;
import com.casesoft.dmc.model.product.BaseStyle;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.shop.FittingRecord;
import com.casesoft.dmc.model.shop.Score;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.service.mirror.*;
import com.casesoft.dmc.service.shop.FittingRecordService;
import com.casesoft.dmc.service.shop.ScoreService;
import io.swagger.annotations.Api;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.security.auth.callback.CallbackHandler;
import java.util.*;

/**
 * Created by WingLi on 2017-01-17.
 * 试衣间方案所有接口
 */
@Controller
@RequestMapping(value = "/api/hub/mirror", method = {RequestMethod.POST, RequestMethod.GET})
@Api(description = "试衣间数据上传和获取接口")
public class MirrorApiController extends ApiBaseController {

    @Autowired
    private FittingRecordService fittingRecordService;

    @Autowired
    private BrandInfoService brandInfoService;

    @Autowired
    private CollocatService collocatService;

    @Autowired
    private ActivityInfoService activityInfoService;

    @Autowired
    private StarInfoService starInfoService;

    @Autowired
    private HomeInfoService homeInfoService;

    @Autowired
    private NewProductService newProductService;

    @Autowired
    private MediaInfoService mediaInfoService;

    @Autowired
    private ScoreService scoreService;


    @Override
    public String index() {
        return null;
    }

    //region KL-B接口
    @RequestMapping(value = "/listDetonProductWS.do")
    @ResponseBody
    public List<OldProduct> listDetonProductWS() {
        this.logAllRequestParams();
        Collection<NewProduct> newProductList = this.getAllNewProduct();
        List<Product> productList = new ArrayList<>();
        for(NewProduct newProduct : newProductList) {
            List<Product> cacheProdList = this.getProductListFormCache(newProduct);
            productList.addAll(cacheProdList);
        }

        List<OldProduct> oldProductList = new ArrayList<>();
        for(Product product : productList) {
            OldProduct oldProduct = new OldProduct();
            BeanUtils.copyProperties(product,oldProduct);
            oldProductList.add(oldProduct);
        }
        return oldProductList;
    }
    private List<Product> getProductListFormCache(NewProduct newProduct ) {
        List<Product> productList = new ArrayList<>();
        Collection<Product> allProduct = CacheManager.getProductMap().values();
        for(Product cache : allProduct) {
            if(cache.getStyleId().equals(newProduct.getStyleId())) {
                if(CommonUtil.isBlank(newProduct.getUrl())) {
                    continue;
                }
                String[] images = newProduct.getUrl().split(",");
                cache.setImages(Arrays.asList(images));
                if(CommonUtil.isNotBlank(cache.getImages())) {
                    cache.setImage(cache.getImages().get(0));
                }
                productList.add(cache);
            }
        }
        return productList;
    }
    @RequestMapping(value = "/listDetonCollocationWS")
    @ResponseBody
    public OldProduct listDetonCollocationWS(String filter_EQS_code) throws Exception {
        String sku = filter_EQS_code;
        Product mainProduct = CacheManager.getProductByCode(sku);
        List<Product> collocaProdList = new ArrayList<>();
        Collection<Collocat> collocatCollection = CacheManager.getCollocatMap().values();

        for(Collocat collocat : collocatCollection) {
            if(collocat.getStyleIds().contains(mainProduct.getStyleId())) {
                String[] styleIds = collocat.getStyleIds().split(",");
                for(String styleId : styleIds) {

                    if(styleId.equals(mainProduct.getStyleId())) {
                        continue;
                    }
                    NewProduct newProduct = CacheManager.getNewProductByStyleId(styleId);
                    List<Product> cacheProdList = this.getProductListFormCache(newProduct);
                    collocaProdList.addAll(cacheProdList);
                }
            }
        }

        OldProduct oldProduct = new OldProduct();
        BeanUtils.copyProperties(mainProduct,oldProduct);
        oldProduct.setCollocation(collocaProdList);

        return oldProduct;
    }
    @RequestMapping(value = "/saveFittingRecordWS.do")
    @ResponseBody
    public MessageBox saveFittingRecordWS(String jsonString) {
        this.logAllRequestParams();
        jsonString = jsonString.replace("\\/", "-").replace('/',
                '-');
        try{
            List<FittingRecord> recordList=JSON.parseArray(jsonString,FittingRecord.class);
            if(CommonUtil.isBlank(recordList)){
                return this.returnFailInfo("上传试衣数据失败！");

            }else{
                for(FittingRecord f:recordList){
                    if (!CommonUtil.isBlank(f.getDeviceId())
                            &&!CommonUtil.isBlank(CacheManager.getDeviceByCode(f.getDeviceId()))) {
                        Device d = CacheManager.getDeviceByCode(f.getDeviceId());
                        f.setId(new GuidCreator().toString());
                        f.setOwnerId(d.getStorageId());// 门店ID
                        f.setParentId(d.getOwnerId());// 父组织id
                        f.setTaskId(new GuidCreator().toString());
                    }else{
                        this.returnFailur("设备号为空！");
                        return null;
                    }
                }
                this.fittingRecordService.saveAll(recordList);
            }
            return this.returnSuccessInfo("上传成功");
        }catch (Exception e){
            return this.returnFailInfo("上传试衣数据失败！"+e.getMessage());
        }
    }
    //endregion

    @RequestMapping(value = "/pageFittingRecordWS.do")
    @ResponseBody
    public MessageBox pageFittingRecord(Page<FittingRecord> page) {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page = this.fittingRecordService.findPage(page, filters);
        for (FittingRecord record : page.getRows()) {
            Unit unit = CacheManager.getUnitByCode(record.getOwnerId());
            record.setUnitName(null == unit ? "" : unit.getName());//门店名称
            Style style = CacheManager.getStyleById(record.getStyleId());
            if (null != style) {
                record.setStyleName(style.getStyleName());
                record.setPrice(style.getPrice());
            }
            record.setColorName(CacheManager.getColorNameById(record.getColorId()));
            record.setSizeName(CacheManager.getSizeNameById(record.getSizeId()));
        }
        page.setTotPage(page.getTotal());//返回总记录数注解 total
        return this.returnSuccessInfo("获取成功", page);
    }

    @RequestMapping(value = "/scoreToFittingDataWS.do")
    @ResponseBody
    public MessageBox scoreToFittingDataWS(String jsonString) {
        try {
            this.logAllRequestParams();
            jsonString = jsonString.replace("\\/", "-").replace('/',
                    '-');
            List<Score> scoreList = JSON.parseArray(jsonString,
                    Score.class);
            List<FittingRecord> records = new ArrayList<FittingRecord>();
            for (Score s : scoreList) {
                s.setId(new GuidCreator().toString());
                String deviceId = s.getDeviceId();
                if (!CommonUtil.isBlank(deviceId)) {
                    // s.setOwnerId(CacheManager.getDeviceByCode(deviceId).getOwnerId());
                    Device d = CacheManager.getDeviceByCode(deviceId);
                    s.setOwnerId(d.getStorageId());// 门店ID
                    s.setParentId(d.getOwnerId());// 父组织id
                }
                ShopUtil.convertToScoreVo(s, records);
            }

            this.scoreService.saveScoreList(scoreList, records);
            return this.returnSuccessInfo("保存成功！",scoreList);
        } catch (Exception e) {
            e.printStackTrace();
            return this.returnFailInfo(e.getMessage(),e);

        }
    }

    @RequestMapping(value = "/uploadFittingDataWS.do")
    @ResponseBody
    public MessageBox uploadFittingDataWS(String jsonString) {
        this.logAllRequestParams();
        Assert.notNull("试衣数据为空", jsonString);
        jsonString = jsonString.replace("\\/", "-").replace('/',
                '-');
        try {
            List<FittingRecord> recordList = JSON.parseArray(jsonString, FittingRecord.class);
            if (CommonUtil.isBlank(recordList)) {
                return this.returnFailInfo("上传数据格式错误");

            } else {
                for (FittingRecord f : recordList) {
                    if (!CommonUtil.isBlank(f.getDeviceId())
                            && !CommonUtil.isBlank(CacheManager.getDeviceByCode(f.getDeviceId()))) {
                        Device d = CacheManager.getDeviceByCode(f.getDeviceId());
                        if (CommonUtil.isBlank(f.getId())) {
                            f.setId(new GuidCreator().toString());
                        }
                        f.setOwnerId(d.getStorageId());// 门店ID
                        f.setParentId(d.getOwnerId());// 父组织id
                    } else {
                        return this.returnFailInfo("设备号为空！");
                    }
                }
                this.fittingRecordService.saveAll(recordList);
                return this.returnSuccessInfo("上传成功");
            }

        } catch (Exception e) {
            return this.returnSuccessInfo(e.getLocalizedMessage());
        }
    }

    @RequestMapping(value = "/listCollocat.do")
    @ResponseBody
    public MessageBox listCollocat(String filter_LIKES_styleIds) {
        this.logAllRequestParams();
        try {
            List<Collocat> collocatList = this.collocatService.getAll();
            for (Collocat collocat : collocatList) {
                if (CommonUtil.isNotBlank(collocat.getUrl())) {
                    String[] images = collocat.getUrl().split(",");
                    for (int i = 0; i < images.length; i++) {
                        images[i] = "/mirror" + "/collocat/" + images[i];
                    }
                    if (CommonUtil.isNotBlank(collocat.getStyleIds())) {
                        String[] styleIds = collocat.getStyleIds().split(",");
                        List<NewProduct> newProductList = new ArrayList<NewProduct>();
                        for (String styleId : styleIds) {
                            NewProduct newProduct = CacheManager.getNewProductByStyleId(styleId);
                            if (null != newProduct) {
                                convertNewUrl(newProduct);
                                newProductList.add(newProduct);
                            }
                        }
                        collocat.setStyleList(newProductList);
                    }
                    collocat.setImages(images);
                }
            }
            return returnSuccessInfo("ok", collocatList);
        } catch (Exception e) {
            e.printStackTrace();
            return returnFailInfo("error");
        }
    }

    @RequestMapping(value = "/listBrand.do")
    @ResponseBody
    public MessageBox listBrand() {
        this.logAllRequestParams();
        try {
            List<BrandInfo> brandInfoList = this.brandInfoService.getAll();
            for (BrandInfo brandInfo : brandInfoList) {
                brandInfo.setUrl("/mirror/brand/" + brandInfo.getUrl());
            }
            return returnSuccessInfo("ok", brandInfoList);
        } catch (Exception e) {
            return returnFailInfo("error");
        }
    }

    @RequestMapping(value = "/listActivity.do")
    @ResponseBody
    public MessageBox listActivity() {
        this.logAllRequestParams();
        try {
            List<ActivityInfo> activityInfoList = this.activityInfoService.getAll();
            for (ActivityInfo activityInfo : activityInfoList) {
                if (CommonUtil.isNotBlank(activityInfo.getUrl())) {
                    String[] imageArr = activityInfo.getUrl().split(",");
                    StringBuffer sb = new StringBuffer();
                    for (String image : imageArr) {
                        if (!image.contains("mirror")) {
                            sb.append(",").append("/mirror")
                                    .append("/activity/").append(image);
                        } else {
                            sb.append(",").append(image);
                        }
                    }
                    activityInfo.setUrl(sb.substring(1));
                }
            }
            return returnSuccessInfo("ok", activityInfoList);
        } catch (Exception e) {
            return returnFailInfo("error");
        }
    }

    @RequestMapping(value = "/listStar.do")
    @ResponseBody
    public MessageBox listStar() {
        this.logAllRequestParams();
        try {
            List<StarInfo> starInfoList = this.starInfoService.getAll();
            for (StarInfo starInfo : starInfoList) {
                if (CommonUtil.isNotBlank(starInfo.getUrl())) {
                    String[] imageArr = starInfo.getUrl().split(",");
                    StringBuffer sb = new StringBuffer();
                    for (String image : imageArr) {
                        if (!image.contains("mirror")) {
                            sb.append(",").append("/mirror")
                                    .append("/starInfo/").append(image);
                        } else {
                            sb.append(",").append(image);
                        }
                    }
                    starInfo.setUrl(sb.substring(1));
                }
            }
            return returnSuccessInfo("ok", starInfoList);
        } catch (Exception e) {
            return returnFailInfo("error");
        }
    }

    @RequestMapping(value = "/listHome.do")
    @ResponseBody
    public MessageBox listHome() {
        this.logAllRequestParams();
        try {
            List<HomeInfo> homeInfoList = this.homeInfoService.getAll();
            for (HomeInfo homeInfo : homeInfoList) {
                homeInfo.setUrl("/mirror/home/" + homeInfo.getUrl());
            }
            return returnSuccessInfo("ok", homeInfoList);
        } catch (Exception e) {
            return returnFailInfo("error");
        }
    }

    @RequestMapping(value = "/listNewProduct.do")
    @ResponseBody
    public MessageBox listNewProduct() {
        this.logAllRequestParams();
        try {
            Collection<NewProduct> newProducts = this.getAllNewProduct();
            return returnSuccessInfo("ok", newProducts);
        } catch (Exception e) {
            return returnFailInfo("error");
        }
    }

    private Collection<NewProduct> getAllNewProduct() {
        Collection<NewProduct> newProducts = CacheManager.getNewProductMap().values();//this.newProductService.getAll();
        for (NewProduct product : newProducts) {
            convertNewUrl(product);
        }
        return newProducts;
    }

    private NewProduct convertNewUrl(NewProduct product) {
        if (CommonUtil.isNotBlank(product.getUrl())) {
            String[] imageArr = product.getUrl().split(",");
            StringBuffer sb = new StringBuffer();
            for (String image : imageArr) {
                if (!image.contains("mirror")) {
                    sb.append(",").append("/mirror")
                            .append("/newProduct/").append(image);
                } else {
                    sb.append(",").append(image);
                }
            }
            product.setUrl(sb.substring(1));
        }
        return product;
    }


    @RequestMapping(value = "/listMedia.do")
    @ResponseBody
    public MessageBox listMedia() {
        this.logAllRequestParams();
        List<MediaInfo> mediaInfoList = this.mediaInfoService.getAll();
        for (MediaInfo mediaInfo : mediaInfoList) {
            mediaInfo.setUrl("/mirror/media/" + mediaInfo.getUrl());
        }
        return this.returnSuccessInfo("获取成功", mediaInfoList);
    }

    /**
     * 客户端查询试衣报表接口
     *
     * @param deviceId
     * @param warehouseCode
     * @param beginDate
     * @param endDate
     * @param xyType        数据类型yoy:同比,mom:环比,trend:单数据
     * @param chartBy       试衣数据依据 date,sku,shop
     * @return
     */
    @RequestMapping(value = "/v1/findAnalysisWS.do")
    @ResponseBody
    public MessageBox findAnalysisWS(String deviceId, String warehouseCode, String beginDate, String endDate, String xyType, String chartBy) {
        this.logAllRequestParams();
        String beginDateY = this.getReqParam("beginDateY");
        String endDateY = this.getReqParam("endDateY");
        JSONObject dataResult = new JSONObject();
        if (CommonUtil.isBlank(CacheManager.getDeviceByCode(deviceId))) {
            return this.returnFailInfo("设备号未注册！", dataResult);
        }
        if (CommonUtil.isBlank(beginDate) ||
                CommonUtil.isBlank(endDate)
                || CommonUtil.isBlank(xyType) || CommonUtil.isBlank(chartBy)) {
            return this.returnFailInfo("试衣日期端缺失！或数据类型缺失！", dataResult);
        }
        int betweenDays = 0;
        try {
            Date sDate = null;
            Date eDate = null;
            if (beginDate.contains("-")) {
                sDate = CommonUtil.converStrToDate(beginDate, "yyyy-MM-dd");
            } else {
                sDate = CommonUtil.converStrToDate(beginDate, "yyyy/MM/dd");
            }
            if (endDate.contains("-")) {
                eDate = CommonUtil.converStrToDate(endDate, "yyyy-MM-dd");
            } else {
                eDate = CommonUtil.converStrToDate(endDate, "yyyy/MM/dd");
            }
            betweenDays = CommonUtil.daysBetween(sDate, eDate);
        } catch (Exception e) {
            return this.returnFailInfo("日期格式错误");
        }
        JSONObject origTitle = new JSONObject();
        dataResult.put("trendTitle", origTitle);//title
        List<String> xDate = new ArrayList<>();
        List<String> yDate = new ArrayList<>();
        origTitle.put("xDate", xDate);
        origTitle.put("yDate", yDate);
        switch (chartBy) {
            case "date":
                List<Object[]> trends = null;
                origTitle.put("title", beginDate + "至" + endDate);
                if (betweenDays <= 3) {
                    trends = this.fittingRecordService.findHourTrend(warehouseCode, beginDate, endDate);

                } else if (betweenDays >=7&&betweenDays<=31) {
                    trends = this.fittingRecordService.findDayTrend(warehouseCode, beginDate, endDate);
                } else {
                    trends = this.fittingRecordService.findMonthTrend(warehouseCode, beginDate, endDate);
                }
                if (CommonUtil.isNotBlank(trends)) {
                    for (Object[] objects : trends) {
                        xDate.add(objects[0].toString());
                        yDate.add(objects[1].toString());
                    }
                }
                switch (xyType) {
                    case "trend":
                        break;
                    //同比
                    case "yoy":
                        JSONObject cmpTitle = new JSONObject();
                        dataResult.put("cmpTitle", cmpTitle);//title
                        List<String> xDateCmp = new ArrayList<>();
                        List<String> yDateCmp = new ArrayList<>();
                        cmpTitle.put("xDate", xDateCmp);
                        cmpTitle.put("yDate", yDateCmp);
                        List<Object[]> trendsY = null;
                        cmpTitle.put("title", beginDateY + "至" + endDateY);
                        if (CommonUtil.isBlank(beginDateY) || CommonUtil.isBlank(endDateY)) {
                            return this.returnFailInfo("缺省同比日期！");
                        }
                        if (betweenDays <= 3) {
                            trendsY = this.fittingRecordService.findHourTrend(warehouseCode, beginDateY, endDateY);
                        }else if (betweenDays >=7&&betweenDays<=31) {
                            trendsY = this.fittingRecordService.findDayTrend(warehouseCode, beginDateY, endDateY);
                        }else {
                            trendsY = this.fittingRecordService.findMonthTrend(warehouseCode, beginDateY, endDateY);
                        }
                        if (CommonUtil.isNotBlank(trendsY)) {
                            for (Object[] objects : trendsY) {
                                xDateCmp.add(objects[0].toString());
                                yDateCmp.add(objects[1].toString());
                            }
                        }
                        break;
                    //环比
                    case "mom":
                        break;
                    default:
                        return this.returnFailInfo("数据类型无效", dataResult);

                }
                return this.returnSuccessInfo("调用成功！", dataResult);
            case "sku":
                List<Object[]> skuTrends = null;
                origTitle.put("title", beginDate + "至" + endDate);
                skuTrends=this.fittingRecordService.findSkuTrend(warehouseCode,beginDate,endDate);
                if (CommonUtil.isNotBlank(skuTrends)) {
                    for (Object[] objects : skuTrends) {
                        xDate.add(objects[0].toString());
                        yDate.add(objects[1].toString());
                    }
                }
                switch (xyType) {
                    case "trend":
                        break;
                    //同比
                    case "yoy":
                        JSONObject cmpTitle = new JSONObject();
                        dataResult.put("cmpTitle", cmpTitle);//title
                        List<String> xDateCmp = new ArrayList<>();
                        List<String> yDateCmp = new ArrayList<>();
                        cmpTitle.put("xDate", xDateCmp);
                        cmpTitle.put("yDate", yDateCmp);
                        List<Object[]> skuTrendsY = null;
                        cmpTitle.put("title", beginDateY + "至" + endDateY);
                        if (CommonUtil.isBlank(beginDateY) || CommonUtil.isBlank(endDateY)) {
                            return this.returnFailInfo("缺省同比日期！");
                        }
                        if(CommonUtil.isNotBlank(skuTrends)){
                            StringBuffer codes=new StringBuffer("'");
                            for (Object[] objects : skuTrends) {
                                codes.append(objects[0]).append("','");
                            }
                            codes.append("'");
                            skuTrendsY=this.fittingRecordService.findYoYSkuTrend(warehouseCode,beginDateY,endDateY,codes.toString());
                            if (CommonUtil.isNotBlank(skuTrendsY)) {
                                for (Object[] objects : skuTrendsY) {
                                    xDateCmp.add(objects[0].toString());
                                    yDateCmp.add(objects[1].toString());
                                }
                            }
                        }
                        break;
                    //环比
                    case "mom":
                        break;
                    default:
                        return this.returnFailInfo("数据类型无效", dataResult);

                }
                break;
            case "shop":
                List<Object[]> shopTrends = null;
                origTitle.put("title", beginDate + "至" + endDate);
                shopTrends=this.fittingRecordService.findShopTrend(beginDate,endDate);
                if (CommonUtil.isNotBlank(shopTrends)) {
                    for (Object[] objects : shopTrends) {
                        xDate.add(objects[0].toString());
                        yDate.add(objects[1].toString());
                    }
                }
                switch (xyType) {
                    case "trend":
                        break;
                    //同比
                    case "yoy":
                        JSONObject cmpTitle = new JSONObject();
                        dataResult.put("cmpTitle", cmpTitle);//title
                        List<String> xDateCmp = new ArrayList<>();
                        List<String> yDateCmp = new ArrayList<>();
                        cmpTitle.put("xDate", xDateCmp);
                        cmpTitle.put("yDate", yDateCmp);
                        List<Object[]> shopTrendsY = null;
                        cmpTitle.put("title", beginDateY + "至" + endDateY);
                        if (CommonUtil.isBlank(beginDateY) || CommonUtil.isBlank(endDateY)) {
                            return this.returnFailInfo("缺省同比日期！");
                        }
                        shopTrendsY=this.fittingRecordService.findShopTrend(beginDateY,endDateY);
                        if (CommonUtil.isNotBlank(shopTrends)) {
                            for (Object[] objects : shopTrends) {
                                if (CommonUtil.isNotBlank(shopTrendsY)) {
                                    for (Object[] objectsY : shopTrendsY) {
                                        if(objects[0].toString().equals(objectsY[0].toString())){
                                            xDateCmp.add(objectsY[0].toString());
                                            yDateCmp.add(objectsY[1].toString());
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    //环比
                    case "mom":
                        break;
                    default:
                        return this.returnFailInfo("数据类型无效", dataResult);

                }
                break;
            default:
                return this.returnFailInfo("数据类型依据无效", dataResult);

        }
        return this.returnSuccessInfo("调用成功", dataResult);
    }

}
