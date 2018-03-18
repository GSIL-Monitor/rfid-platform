package com.casesoft.dmc.extend.playlounge;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.cache.SpringContextUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.ISynErpService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.secret.EpcSecretUtil;
import com.casesoft.dmc.extend.playlounge.service.SynOtherService;

import com.casesoft.dmc.extend.third.model.DayThirdStock;
import com.casesoft.dmc.extend.third.model.ThirdStock;
import com.casesoft.dmc.extend.third.service.DayThirdStockService;
import com.casesoft.dmc.extend.third.service.ThirdStockService;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.shop.FittingRecord;
import com.casesoft.dmc.model.shop.SaleBill;
import com.casesoft.dmc.model.shop.SaleBillDtl;
import com.casesoft.dmc.model.syn.BasicConfiguration;
import com.casesoft.dmc.model.syn.Config;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.model.wms.pl.PlWmsRack;
import com.casesoft.dmc.service.cfg.PropertyKeyService;
import com.casesoft.dmc.service.product.ProductService;
import com.casesoft.dmc.service.product.StyleService;
import com.casesoft.dmc.service.shop.CustomerService;
import com.casesoft.dmc.service.shop.FittingRecordService;
import com.casesoft.dmc.service.shop.SaleBillService;
import com.casesoft.dmc.service.sys.impl.UnitService;
import com.casesoft.dmc.service.sys.impl.UserService;
import com.casesoft.dmc.service.wms.pl.PlShopWmsViewService;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.TriggerKey;
import org.quartz.impl.StdScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//@Service
@SuppressWarnings("unchecked")
public class PlayloungeSyn {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private StyleService styleService;
    private PropertyKeyService propertyKeyService;
    private UnitService unitService;
    private ProductService productService;
    private CustomerService customerService;
    private UserService userService;
    private ISynErpService synErpService;
    private SynOtherService synOtherService;
    private SaleBillService saleBillService;
    private FittingRecordService fittingRecordService;
    //第三方库存查询
    private ThirdStockService thirdStockService;
    private DayThirdStockService dayThirdStockService;
    private PlShopWmsViewService plShopWmsViewService;

    public void synBasicinfo() throws Exception {
        List<User> userList = this.synErpService.synchronizeUser("1");
        this.userService.saveList(userList);
        logger.info(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + ";用户同步成功！");
        System.out.println(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + ";用户同步成功！");

        List list = this.synErpService.synchronizeProperty("1");
        this.propertyKeyService.saveAllPropertyKey(list);
        CacheManager.refreshPropertyCache();
        logger.info(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + ";属性同步成功！");
        System.out.println(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + ";属性同步成功！");

        List listStorage = this.synErpService.synchronizeStorage();
        this.unitService.saveList(listStorage);
        CacheManager.refreshUnitCache();
        logger.info(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + ";仓库同步成功！");
        System.out.println(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + ";仓库同步成功！");

        List listShop = this.synErpService.synchronizeShop("1");
        this.unitService.saveList(listShop);
        CacheManager.refreshUnitCache();
        logger.info(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + ";门店同步成功！");
        System.out.println(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + ";门店同步成功！");

        List<Unit> venderList = this.synErpService.synchronizeVender("1");
        this.unitService.saveList(venderList);
        CacheManager.refreshUnitCache();
        logger.info(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + ";代理商同步成功！");
        System.out.println(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + ";代理商同步成功！");

        List<Customer> customerList = this.synErpService.synchronizeCustomer("1");
        this.customerService.saveList(customerList);
        logger.info(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + ";客户同步成功！");
        System.out.println(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + ";客户同步成功！");
    }

    public void synStyleInfo() throws Exception {
        List styleList = this.synErpService.synchronizeStyle("1");
        this.styleService.saveList(styleList);
        CacheManager.refreshStyleCache();
        logger.info(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + ";款同步成功");
        System.out.println("款同步成功");
        List colorList = this.synErpService.synchronizeColor("1");
        this.styleService.saveList2(colorList);
        CacheManager.refreshColorCache();
        logger.info(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + ";颜色同步成功");
        System.out.println(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + ";颜色同步成功");
        List ssList = this.synErpService.synchronizeSizeSort("1");
        List sizeList = this.synErpService.synchronizeSize("1");
        this.styleService.saveList3(sizeList, ssList);
        CacheManager.refreshSizeCache();
        logger.info(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + ";尺寸同步成功");
        System.out.println(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + ";尺寸同步成功");
        List productList = this.synErpService.synchronizeProduct("1");
        this.productService.save(productList);
        CacheManager.refreshProductCache();
        logger.info(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + ";商品同步成功");
        System.out.println(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + ";同商品步成功");
    }

    public void synTagInfo() {
        synOtherService.batchEpc(null);
        System.out.println("synTagInfo");
        logger.info(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + ";标签数据同步成功");
        System.out.println(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + ";标签数据同步成功");
    }

    public void synSaleInfo() {

        List<SaleBill> bills = this.synOtherService.downloadSaleInfo();
        if (CommonUtil.isNotBlank(bills)) {
            List<PropertyFilter> propertyFilters=new ArrayList<>();
            List<PlWmsRack> plWmsRacks=this.plShopWmsViewService.findRackFilter(propertyFilters);
            for (SaleBill bill : bills) {
                List<SaleBillDtl> dtls = bill.getDtlList();
                for (SaleBillDtl billDtl : dtls) {
                    if(CommonUtil.isNotBlank(billDtl.getRackId())){
                        for(PlWmsRack plWmsRack:plWmsRacks){
                            if(billDtl.getRackId().equals(plWmsRack.getId())){
                                if(billDtl.getActPrice()>0){
                                    if(plWmsRack.getSaleTotQty()==null){
                                        plWmsRack.setSaleTotQty(billDtl.getQty());
                                        plWmsRack.setSaleTotPrice(billDtl.getActPrice());
                                    }else{
                                        plWmsRack.setSaleTotQty(plWmsRack.getSaleTotQty()+billDtl.getQty());
                                        plWmsRack.setSaleTotPrice(plWmsRack.getSaleTotPrice()+billDtl.getActPrice());
                                    }
                                }else{
                                    if(plWmsRack.getBackTotQty()==null){
                                        plWmsRack.setBackTotQty(billDtl.getQty());
                                        plWmsRack.setBackTotPrice(billDtl.getActPrice());
                                    }else{
                                        plWmsRack.setBackTotQty(plWmsRack.getBackTotQty()+billDtl.getQty());
                                        plWmsRack.setBackTotPrice(plWmsRack.getBackTotPrice()+billDtl.getActPrice());
                                    }
                                }
                                break;
                            }
                        }
                    }
                    if (CommonUtil.isNotBlank(billDtl.getUniqueCode())) {

                        String code = EpcSecretUtil.decodeEpc(billDtl.getUniqueCode());
                        if (code.length() >= 13 && code.substring(0, 13).endsWith(Constant.TagSerial.Playlounge)) {
                            code = code.substring(0, 13);
                        } else {
                            code = billDtl.getUniqueCode();
                        }

                        billDtl.setUniqueCode(code);
                    }
                }
            }
            this.plShopWmsViewService.updatePlWmsRacks(plWmsRacks);
        }

        saleBillService.batchBills(bills);
        logger.info(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + ";零售同步成功");
        System.out.println(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + ";零售数据同步成功");
    }

    public void synFitting() throws ParseException {
        System.out.println("fitting");
        logger.info(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + ";开始fitting");

        Date endDate = new Date();
        Calendar ca = Calendar.getInstance();
        String end = CommonUtil.getDateString(endDate, "yyyy-MM-dd 23:59:59");
        String begin = CommonUtil.getDateString(ca.getTime(), "yyyy-MM-dd 00:00:00");
        List<FittingRecord> fittings = this.fittingRecordService.findCountRecord(CommonUtil.converStrToDate(begin, "yyyy-MM-dd HH:mm:ss"),
                CommonUtil.converStrToDate(end, "yyyy-MM-dd HH:mm:ss"));
        this.synErpService.synchronizeFitting(fittings);
        logger.info(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + ";试衣数据同步成功");
        System.out.println(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + ";试衣数据同步成功");
    }

    /**
     * 更新库存
     */
    public void synThirdStock() throws ParseException {
        logger.info(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + ";同步第三方erp库存");
        System.out.println(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + "同步第三方erp库存");
        List<ThirdStock> thirdStocks = this.synErpService.synchronizeThirdStock("1");
        this.thirdStockService.doBatchInsert(thirdStocks);
        System.out.println(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + ";同步第三方erp库存成功");
        logger.info(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + ";同步第三方erp库存成功");
    }

    /**
     * 更新每天库存
     */
    public void synDayThirdStock() throws ParseException {
        logger.info(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + ";同步第三方单天erp库存");

        System.out.println(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + "同步第三方单天erp库存");
        List<DayThirdStock> dayThirdStockList = new ArrayList<>();
        Date sysDate = new Date();
        String begin = CommonUtil.getDateString(sysDate, "yyyy-MM-dd");
        StringBuffer days = new StringBuffer("'");
        for (int i = 1; i <= 30; i++) {
            try {
                List<DayThirdStock> dayThirdStocks = synErpService.synchronizeDayThirdStock("", CommonUtil.reduceDay(begin, i));
                if (CommonUtil.isNotBlank(dayThirdStocks)) {
                    dayThirdStockList.addAll(dayThirdStocks);
                }
                days.append(begin).append("','");
                System.out.println(CommonUtil.reduceDay(begin, i));
            } catch (Exception e) {
                System.out.println(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + ";同步第三方单天erp库存失败；获取erp失败");
                logger.info(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + ";同步第三方单天erp库存失败；获取erp失败");

                e.printStackTrace();
            }
        }
        days.append("'");
        if (CommonUtil.isNotBlank(dayThirdStockList)) {
            this.dayThirdStockService.doBatchInsert(dayThirdStockList, days.toString());
            System.out.println(dayThirdStockList.size());
        }
        try {
            System.out.println(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + ";开始刷新物化试图");
            logger.info(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + ";开始刷新物化试图");
            this.dayThirdStockService.freshMaterializedView();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + ";同步第三方单天erp库存失败");
        }
        System.out.println(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + ";同步第三方单天erp库存成功");
        logger.info(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + ";同步第三方单天erp库存成功");

    }


    public StyleService getStyleService() {
        return styleService;
    }

    public void setStyleService(StyleService styleService) {
        this.styleService = styleService;
    }


    public PropertyKeyService getPropertyKeyService() {
        return propertyKeyService;
    }

    public void setPropertyKeyService(PropertyKeyService propertyKeyService) {
        this.propertyKeyService = propertyKeyService;
    }

    public UnitService getUnitService() {
        return unitService;
    }

    public void setUnitService(UnitService unitService) {
        this.unitService = unitService;
    }

    public ProductService getProductService() {
        return productService;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    public CustomerService getCustomerService() {
        return customerService;
    }

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public ISynErpService getSynErpService() {
        return synErpService;
    }

    public void setSynErpService(ISynErpService synErpService) {
        this.synErpService = synErpService;
    }

    public SynOtherService getSynOtherService() {
        return synOtherService;
    }

    public void setSynOtherService(SynOtherService synOtherService) {
        this.synOtherService = synOtherService;
    }

    public SaleBillService getSaleBillService() {
        return saleBillService;
    }

    public void setSaleBillService(SaleBillService saleBillService) {
        this.saleBillService = saleBillService;
    }

    public FittingRecordService getFittingRecordService() {
        return fittingRecordService;
    }

    public void setFittingRecordService(FittingRecordService fittingRecordService) {
        this.fittingRecordService = fittingRecordService;
    }

    public ThirdStockService getThirdStockService() {
        return thirdStockService;
    }

    public void setThirdStockService(ThirdStockService thirdStockService) {
        this.thirdStockService = thirdStockService;
    }

    public DayThirdStockService getDayThirdStockService() {
        return dayThirdStockService;
    }

    public void setDayThirdStockService(DayThirdStockService dayThirdStockService) {
        this.dayThirdStockService = dayThirdStockService;
    }

    public PlShopWmsViewService getPlShopWmsViewService() {
        return plShopWmsViewService;
    }

    public void setPlShopWmsViewService(PlShopWmsViewService plShopWmsViewService) {
        this.plShopWmsViewService = plShopWmsViewService;
    }
}
