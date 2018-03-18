package com.casesoft.dmc.extend.third.searcher.controller;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.third.descriptor.DataResult;
import com.casesoft.dmc.extend.third.model.pl.PlWmsShopBindingRelation;
import com.casesoft.dmc.extend.third.request.RequestPageData;
import com.casesoft.dmc.extend.third.searcher.model.SearchDtl;
import com.casesoft.dmc.extend.third.searcher.model.SearchMain;
import com.casesoft.dmc.extend.third.searcher.service.SearchMainService;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.erp.ErpStock;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.shop.FittingRecord;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.syn.IBillWSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by john on 2017-04-18.
 * 试衣要货
 */
@Controller
@RequestMapping("/third/searcher")
public class SearchMainController extends BaseController {
    @Autowired
    private SearchMainService searchMainService;
    @Autowired
    private IBillWSService billWSService;

    @RequestMapping(value = "/index")
    public String index() {
        return "/views/third/searcher/searcher";
    }

    @RequestMapping(value = "/pick/index")
    public String pickIndex() {
        return "/views/third/searcher/pick";
    }

    @RequestMapping("/list")
    @ResponseBody
    public DataResult read(@RequestBody RequestPageData<SearchMain> request) {
        DataResult dataResult = this.searchMainService.find(request);
        List<SearchMain> searchMains = (List<SearchMain>) dataResult.getData();
        if(CommonUtil.isNotBlank(searchMains)){
            Date cuDate=new Date();
            for(SearchMain searchMain:searchMains){
                if(Integer.parseInt(searchMain.getStatus())<3&&Integer.parseInt(searchMain.getStatus())!=-1){
                    searchMain.setDiffDate(CommonUtil.dateDiff(searchMain.getSendDate(),cuDate));
                }
            }
        }
        return dataResult;
    }

    @RequestMapping("/findUnique")
    @ResponseBody
    public MessageBox findUnique(String mainId) {
        this.logAllRequestParams();
        SearchMain searchMain = this.searchMainService.load(mainId);
        if (CommonUtil.isNotBlank(searchMain)) {
            return this.returnSuccessInfo("查询成功", searchMain);
        }
        return this.returnFailInfo("查询失败！", new SearchMain());
    }

    /**
     * 获取未处理单据
     * */
    @RequestMapping("/findChangedBillCount")
    @ResponseBody
    public MessageBox findChangedBillCount(String userCode,String type) {
        this.logAllRequestParams();
        SearchMain searchMain = null;
        if (CommonUtil.isNotBlank(searchMain)) {
            return this.returnSuccessInfo("查询成功", searchMain);
        }
        return this.returnFailInfo("查询失败！", new SearchMain());
    }
    @RequestMapping("/listFitting")
    @ResponseBody
    public DataResult listFitting(@RequestBody RequestPageData<FittingRecord> request) throws Exception {
        DataResult dataResult = this.searchMainService.find(request, FittingRecord.class);
        List<FittingRecord> showRecords = (List<FittingRecord>) dataResult.getData();
        if (CommonUtil.isNotBlank(showRecords)) {
            StringBuffer skus=new StringBuffer("'");
            for (FittingRecord record : showRecords) {
                skus.append(record.getSku()).append("','");
                record.setStyleName(CacheManager.getStyleNameById(record.getStyleId()));
                record.setColorName(CacheManager.getColorNameById(record.getColorId()));
                record.setSizeName(CacheManager.getSizeNameById(record.getSizeId()));
                Product p=CacheManager.getProductByCode(record.getSku());
                //查询图片加载图片
                if(CommonUtil.isNotBlank(p)){
                    record.setImage(p.getImage());
                }
                StringBuffer unique=new StringBuffer(record.getOwnerId());
                unique.append(record.getStyleId()).append(record.getColorId());
                PlWmsShopBindingRelation wmsPlRackBindingRelation= CacheManager.getWmsPlRackBindingRelationByUnitCodeSku(unique.toString());
                if(CommonUtil.isNotBlank(wmsPlRackBindingRelation)){
                    record.setRackId(wmsPlRackBindingRelation.getRackBarcode());
                }
                //加载库位
                if(CommonUtil.isNotBlank(CacheManager.getStyleById(record.getStyleId()))) {
                    PropertyKey pk3 = CacheManager.getPropertyKey("C7-U-" + CacheManager.getStyleById(record.getStyleId()).getClass3());
                    if (CommonUtil.isNotBlank(pk3)) {
                        record.setFloorId(pk3.getName());
                    }
                }
            }
            skus.append("'");
            //查询erp库存
            List<ErpStock> erpStocks = this.billWSService.findErpStock(
                    new String[]{"filter_LIKES_styleId",
                            "filter_LIKES_colorId", "filter_LIKES_sizeId",
                            "filter_LIKES_warehouseId,filter_LIKES_sku"},
                    new String[]{null, null, null, null, skus.toString(),"erp"});
            if(CommonUtil.isNotBlank(erpStocks)){
                for (FittingRecord record : showRecords) {
                    for(ErpStock erpStock:erpStocks){
                        if(record.getSku().equals(erpStock.getStyleId()+erpStock.getColorId()+erpStock.getSizeId())){
                            if(record.getOwnerId().equals(erpStock.getWarehouseId())){
                                record.setStockQty(erpStock.getQty());
                            }
                            if(CommonUtil.isBlank(record.getOtherErpStocks())){
                                record.setOtherErpStocks(new ArrayList<ErpStock>());
                            }
                            record.getOtherErpStocks().add(erpStock);
                        }
                    }
                }
            }
          }
        return dataResult;
    }

    @RequestMapping("/listProduct")
    @ResponseBody
    public DataResult listProduct(@RequestBody RequestPageData<Product> request) throws Exception{
        DataResult dataResult = this.searchMainService.find(request, Product.class);
        List<Product> showRecords = (List<Product>) dataResult.getData();
        List<FittingRecord> fittingRecords=new ArrayList<>();
        dataResult.setData(fittingRecords);
        User user=this.getCurrentUser();
        if (CommonUtil.isNotBlank(showRecords)) {
            StringBuffer skus=new StringBuffer("'");

            for (Product record : showRecords) {
                FittingRecord fittingRecord=new FittingRecord();
                fittingRecord.setSku(record.getCode());
                fittingRecord.setStyleId(record.getStyleId());
                fittingRecord.setColorId(record.getColorId());
                fittingRecord.setSizeId(record.getSizeId());
                fittingRecord.setStyleName(CacheManager.getStyleNameById(record.getStyleId()));
                fittingRecord.setColorName(CacheManager.getColorNameById(record.getColorId()));
                fittingRecord.setSizeName(CacheManager.getSizeNameById(record.getSizeId()));
                if(CommonUtil.isNotBlank(user)){
                    fittingRecord.setOwnerId(user.getOwnerId());
                }
                Product p=CacheManager.getProductByCode(fittingRecord.getSku());
                //查询图片加载图片
                if(CommonUtil.isNotBlank(p)){
                    fittingRecord.setImage(p.getImage());
                }
                StringBuffer unique=new StringBuffer(user.getOwnerId());
                unique.append(fittingRecord.getStyleId()).append(fittingRecord.getColorId());
                PlWmsShopBindingRelation wmsPlRackBindingRelation= CacheManager.getWmsPlRackBindingRelationByUnitCodeSku(unique.toString());
                if(CommonUtil.isNotBlank(wmsPlRackBindingRelation)){
                    fittingRecord.setRackId(wmsPlRackBindingRelation.getRackBarcode());
                }
                //加载库位
                if(CommonUtil.isNotBlank(CacheManager.getStyleById(record.getStyleId()))) {
                    PropertyKey pk3 = CacheManager.getPropertyKey("C7-U-" + CacheManager.getStyleById(record.getStyleId()).getClass3());
                    if (CommonUtil.isNotBlank(pk3)) {
                        fittingRecord.setFloorId(pk3.getName());
                    }
                }
                skus.append(fittingRecord.getSku()).append("','");

                fittingRecords.add(fittingRecord);
            }
            skus.append("'");
            List<ErpStock> erpStocks = this.billWSService.findErpStock(
                    new String[]{"filter_LIKES_styleId",
                            "filter_LIKES_colorId", "filter_LIKES_sizeId",
                            "filter_LIKES_warehouseId,filter_LIKES_sku"},
                    new String[]{null, null, null, null, skus.toString(),"erp"});
            if(CommonUtil.isNotBlank(erpStocks)){
                for (FittingRecord record : fittingRecords) {
                    for(ErpStock erpStock:erpStocks){
                        if(record.getSku().equals(erpStock.getStyleId()+erpStock.getColorId()+erpStock.getSizeId())){
                            if(record.getOwnerId().equals(erpStock.getWarehouseId())){
                                record.setStockQty(erpStock.getQty());
                            }
                            if(CommonUtil.isBlank(record.getOtherErpStocks())){
                                record.setOtherErpStocks(new ArrayList<ErpStock>());
                            }
                            record.getOtherErpStocks().add(erpStock);
                        }
                    }
                }
            }
        }
        return dataResult;
    }

    /**
     * @param searchMain
     * @param searchDtls 提交要货单
     */
    @RequestMapping("/save")
    @ResponseBody
    public MessageBox save(SearchMain searchMain, String searchDtls) {
        this.logAllRequestParams();
        try {
            User user = this.getCurrentUser();
            List<SearchDtl> list = JSON.parseArray(searchDtls, SearchDtl.class);
            searchMain.setFromCode(user.getCode());
            searchMain.setFromName(user.getName());
            User toUser= CacheManager.getUserById(searchMain.getToCode());
            if(CommonUtil.isNotBlank(toUser)){
                searchMain.setToName(toUser.getName());
            }
            searchMain.setSendDate(new Date());
            searchMain.setOrigCode(user.getOwnerId());
            searchMain.setId(this.searchMainService.findMainId());
            for (SearchDtl searchDtl : list) {
                searchDtl.setBarcode(searchDtl.getSku());
                searchDtl.setMainId(searchMain.getId());
                searchDtl.setOrigCode(searchMain.getOrigCode());
                searchDtl.setId(searchMain.getId() + searchDtl.getSku());
                StringBuffer unique=new StringBuffer(searchMain.getOrigCode());
                unique.append(searchDtl.getStyleId()).append(searchDtl.getColorId());
                PlWmsShopBindingRelation wmsPlRackBindingRelation= CacheManager.getWmsPlRackBindingRelationByUnitCodeSku(unique.toString());
                if(CommonUtil.isNotBlank(wmsPlRackBindingRelation)){
                    searchDtl.setRackId(wmsPlRackBindingRelation.getRackBarcode());
                }
            }
            searchMain.setSkuQty(list.size());
            searchMain.setSearchDtlList(list);
            this.searchMainService.save(searchMain);
        } catch (Exception e) {
            e.printStackTrace();
            return this.returnFailInfo("提交失败！");
        }
        return this.returnSuccessInfo("提交成功！");
    }

    @RequestMapping("/destroySearchMain")
    @ResponseBody
    public MessageBox destroySearchMain(String id ,String updateRemark) {
        this.logAllRequestParams();
        try {
            SearchMain searchMain=this.searchMainService.load(id);
            searchMain.setStatus(SearchMain.Status.MAIN_STATUS_DESTROY);
            searchMain.setUpdateRemark(updateRemark);
            searchMain.setUpdateDate(new Date());
            this.searchMainService.update(searchMain);
        } catch (Exception e) {
            e.printStackTrace();
            return this.returnFailInfo("提交失败！");
        }
        return this.returnSuccessInfo("提交成功！");
    }
    @RequestMapping("/updateStatus")
    @ResponseBody
    public MessageBox updateStatus(SearchMain searchMain, String searchDtls) {
        this.logAllRequestParams();
        try {
            List<SearchDtl> list = JSON.parseArray(searchDtls, SearchDtl.class);
            SearchMain orSearch = this.searchMainService.get("id", searchMain.getId());
            if(CommonUtil.isNotBlank(orSearch.getUpdateRemark())){
                orSearch.setUpdateRemark(orSearch.getUpdateRemark()+";"+searchMain.getRemark());
            }else{
                orSearch.setUpdateRemark(searchMain.getRemark());

            }
            orSearch.setUpdateDate(new Date());
            if(Integer.parseInt(searchMain.getStatus())<3&&Integer.parseInt(searchMain.getStatus())!=-1){
                searchMain.setDiffDate("总耗时："+CommonUtil.dateDiff(searchMain.getSendDate(),orSearch.getUpdateDate()));
            }
           // orSearch.setStatus(searchMain.getStatus());
            StringBuffer skus = new StringBuffer("'");
            for (SearchDtl searchDtl : list) {
                skus.append(searchDtl.getSku()).append("','");
            }
            skus.append("'");
            orSearch.setSearchQty(list.size());
            orSearch.setLostQty(orSearch.getSkuQty() - list.size());
            if(orSearch.getSkuQty()==orSearch.getSearchQty()){
                orSearch.setStatus(SearchMain.Status.MAIN_STATUS_END);
            }else{
                orSearch.setStatus(SearchMain.Status.MAIN_STATUS_PARTON);
            }
            this.searchMainService.updateStatus(orSearch, skus.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return this.returnFailInfo("提交失败！");
        }
        return this.returnSuccessInfo("提交成功！");
    }
    @RequestMapping("/updateMainStatus")
    @ResponseBody
    public MessageBox updateMainStatus(SearchMain searchMain) {
        this.logAllRequestParams();
        try {
            SearchMain orSearch = this.searchMainService.get("id", searchMain.getId());
            if (CommonUtil.isNotBlank(searchMain.getRemark())) {
                orSearch.setRemark(searchMain.getRemark());
            }
            orSearch.setStatus(searchMain.getStatus());
            this.searchMainService.update(orSearch);
        } catch (Exception e) {
            e.printStackTrace();
            return this.returnFailInfo("提交失败！");
        }
        return this.returnSuccessInfo("提交成功！");
    }

    @RequestMapping("/listDtl")
    @ResponseBody
    public DataResult listDtl(@RequestBody RequestPageData<SearchDtl> request) throws Exception {
        DataResult dataResult = this.searchMainService.find(request, SearchDtl.class);
        List<SearchDtl> showRecords = (List<SearchDtl>) dataResult.getData();
        User user = this.getCurrentUser();
        String ownerId=user.getOwnerId();
        if (CommonUtil.isNotBlank(showRecords)) {
            StringBuffer skus = new StringBuffer("'");
            for (SearchDtl record : showRecords) {
                if(CommonUtil.isNotBlank(CacheManager.getStyleById(record.getStyleId()))) {
                    PropertyKey pk3 = CacheManager.getPropertyKey("C7-U-" + CacheManager.getStyleById(record.getStyleId()).getClass3());
                    if (CommonUtil.isNotBlank(pk3)) {
                        record.setFloorId(pk3.getName());
                    }
                }
                record.setStyleName(CacheManager.getStyleNameById(record.getStyleId()));
                record.setColorName(CacheManager.getColorNameById(record.getColorId()));
                record.setSizeName(CacheManager.getSizeNameById(record.getSizeId()));
                Product p=CacheManager.getProductByCode(record.getSku());
                //查询图片加载图片
                if(CommonUtil.isNotBlank(p)){
                    record.setImage(p.getImage());
                }
                //加载库位
                if(CommonUtil.isNotBlank(CacheManager.getStyleById(record.getStyleId()))) {
                    PropertyKey pk3 = CacheManager.getPropertyKey("C7-U-" + CacheManager.getStyleById(record.getStyleId()).getClass3());
                    if (CommonUtil.isNotBlank(pk3)) {
                        record.setFloorId(pk3.getName());
                    }
                }
                skus.append(record.getSku()).append("','");
            }
            skus.append("'");
            List<ErpStock> erpStocks = this.billWSService.findErpStock(
                    new String[]{"filter_LIKES_styleId",
                            "filter_LIKES_colorId", "filter_LIKES_sizeId",
                            "filter_LIKES_warehouseId,filter_LIKES_sku"},
                    new String[]{null, null, null, null, skus.toString(),"erp"});
            if(CommonUtil.isNotBlank(erpStocks)){
                for (SearchDtl record : showRecords) {
                    for(ErpStock erpStock:erpStocks){
                        if(record.getSku().equals(erpStock.getStyleId()+erpStock.getColorId()+erpStock.getSizeId())){
                            if(record.getOrigCode().equals(erpStock.getWarehouseId())){
                                record.setStockQty(erpStock.getQty());
                            }
                            if(CommonUtil.isBlank(record.getOtherErpStocks())){
                                record.setOtherErpStocks(new ArrayList<ErpStock>());
                            }
                            record.getOtherErpStocks().add(erpStock);
                        }
                    }
                }
            }
        }
        return dataResult;
    }

    @RequestMapping("/findErpStock")
    @ResponseBody
    public MessageBox findErpStock(String sku) throws Exception {
        this.logAllRequestParams();
        SearchDtl searchDtl = new SearchDtl();
        List<ErpStock> erpStocks = this.billWSService.findErpStock(
                new String[]{"filter_LIKES_styleId",
                        "filter_LIKES_colorId", "filter_LIKES_sizeId",
                        "filter_LIKES_warehouseId,filter_LIKES_sku"},
                new String[]{null, null, null, null, sku});
        User user = this.getCurrentUser();
        String ownerId=user.getOwnerId();
        Product product=CacheManager.getProductByCode(sku);

        if(CommonUtil.isNotBlank(product)){
            searchDtl.setSku(sku);
            searchDtl.setStyleId(product.getStyleId());
            searchDtl.setColorId(product.getColorId());
            searchDtl.setSizeId(product.getSizeId());
            searchDtl.setStyleName(CacheManager.getStyleNameById(product.getStyleId()));
            searchDtl.setColorName(CacheManager.getColorNameById(product.getColorId()));
            searchDtl.setSizeName(CacheManager.getSizeNameById(product.getSizeId()));
            if(CommonUtil.isNotBlank(CacheManager.getStyleById(product.getStyleId()))){
                PropertyKey pk3 = CacheManager.getPropertyKey("C7-U-" + CacheManager.getStyleById(product.getStyleId()).getClass3());
                if (CommonUtil.isNotBlank(pk3)) {
                    searchDtl.setFloorId(pk3.getName());
                }
            }
        }
        if (CommonUtil.isNotBlank(erpStocks)) {
            for (ErpStock erpStock : erpStocks) {
                if(ownerId.equals(erpStock.getWarehouseId())){
                    searchDtl.setStockQty(erpStock.getQty());
                    break;
                }
            }
            searchDtl.setOtherErpStocks(erpStocks);
        }
        return this.returnSuccessInfo("查询成功！", searchDtl);
    }
}
