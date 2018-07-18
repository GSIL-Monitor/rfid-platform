package com.casesoft.dmc.controller.syn.third;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.syn.third.basic.ISynProductController;
import com.casesoft.dmc.controller.syn.third.basic.ISynUnitController;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.service.ISynErpService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.tiantan.service.TiantanSynErpService;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.log.ServerLogMessage;
import com.casesoft.dmc.model.product.*;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.shop.FittingRecord;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.cfg.PropertyKeyService;
import com.casesoft.dmc.service.log.ServerLogMessageService;
import com.casesoft.dmc.service.product.ProductService;
import com.casesoft.dmc.service.product.StyleService;
import com.casesoft.dmc.service.shop.CustomerService;
import com.casesoft.dmc.service.shop.FittingRecordService;
import com.casesoft.dmc.service.sys.impl.UnitService;
import com.casesoft.dmc.service.sys.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by john on 2017/1/13.
 */
@Controller
@RequestMapping("/syn")
public class SynchronizeController extends BaseController implements ISynProductController, ISynUnitController {

    @Autowired
    private TiantanSynErpService synErpService;

    @Autowired
    private FittingRecordService fittingRecordService;
    @Autowired
    private StyleService styleService;


    @Autowired
    private PropertyKeyService propertyKeyService;

    @Autowired
    private UnitService unitService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;

    @Autowired
    private ServerLogMessageService serverLogMessageService;
    @RequestMapping("/synWarehouse")
    @ResponseBody
    @Override
    public MessageBox synchronizeWarehouse() {
        this.logger.info("开始同步仓库！");
        long beginTime = System.currentTimeMillis();//2、结束时间
        ServerLogMessage logMessage = new ServerLogMessage();
        logMessage.setCreateTime(new Date());
        logMessage.setMessage("同步仓库");
        logMessage.setMethod("/syn/synWarehouse");
        logMessage.setType(Constant.LogType.SYN);
        logMessage.setCreatorId("admin");
        try {
            List<Unit> listStorage = this.synErpService.synchronizeStorage();
            if (CommonUtil.isBlank(listStorage)) {
                return this.returnFailInfo("未获取相关同步数据");
            }
            this.unitService.saveList(listStorage);
            CacheManager.refreshUnitCache();
            this.logger.info("同步仓库成功！");
            System.out.println("同步仓库成功！");
            return this.returnSuccessInfo("同步仓库成功!共" + listStorage.size());
        } catch (Exception e) {
            e.printStackTrace();
            this.logger.error("仓库同步失败！");
            System.err.println("仓库同步失败！");
            try {
                return this.returnFailInfo("仓库同步失败");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }finally {
            logMessage.setConsumeTime(System.currentTimeMillis()-beginTime);
            this.serverLogMessageService.save(logMessage);
        }
        return this.returnSuccessInfo("同步仓库成功");
    }

    @RequestMapping("/synStyle")
    @ResponseBody
    @Override
    public MessageBox synchronizeStyle() {
        String brandCode = getReqParam("brandCode");
        this.logger.info("开始同步款！");
        long beginTime = System.currentTimeMillis();//2、结束时间
        ServerLogMessage logMessage = new ServerLogMessage();
        logMessage.setCreateTime(new Date());
        logMessage.setMessage("同步款");
        logMessage.setMethod("/syn/synStyle");
        logMessage.setType(Constant.LogType.SYN);
        logMessage.setCreatorId("admin");
        try {
            List<Style> styleList = this.synErpService.synchronizeStyle(brandCode);
            if (CommonUtil.isBlank(styleList)) {
                return this.returnFailInfo("未获取相关同步数据");
            }
            this.styleService.saveList(styleList);
            CacheManager.refreshStyleCache();
            System.out.println("款同步成功！共" + styleList.size());
            this.logger.info("款同步成功!共" + styleList.size());
            return this.returnSuccessInfo("款同步成功！共" + styleList.size());
        } catch (Exception e) {
            e.printStackTrace();
            this.logger.error("款同步失败！");
            System.out.println("款同步失败！");
            try {
                return this.returnFailInfo("款同步失败");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }finally {
            logMessage.setConsumeTime(System.currentTimeMillis()-beginTime);
            this.serverLogMessageService.save(logMessage);
        }
        return this.returnSuccessInfo("同步成功");
    }

    @RequestMapping("/synColor")
    @ResponseBody
    @Override
    public MessageBox synchronizeColor() {
        this.logger.info("开始同步颜色！");
        long beginTime = System.currentTimeMillis();//2、结束时间
        ServerLogMessage logMessage = new ServerLogMessage();
        logMessage.setCreateTime(new Date());
        logMessage.setType(Constant.LogType.SYN);
        logMessage.setCreatorId("admin");
        logMessage.setMessage("同步颜色");
        logMessage.setMethod("/syn/synColor");

        try {
            String brandCode = getReqParam("brandCode");
            List<Color> colorList = this.synErpService.synchronizeColor(brandCode);
            if (CommonUtil.isBlank(colorList)) {
                return this.returnFailInfo("未获取相关同步数据");
            }
            this.styleService.saveList2(colorList);
            CacheManager.refreshColorCache();
            System.out.println("颜色同步成功");
            this.logger.info("颜色同步成功");
            return this.returnSuccessInfo("颜色同步成功！共" + colorList.size());
        } catch (Exception e) {
            e.printStackTrace();
            this.logger.error("颜色同步失败！");
            System.err.println("颜色同步失败！");
            try {
                return this.returnFailInfo("颜色同步失败");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }finally {
            logMessage.setConsumeTime(System.currentTimeMillis()-beginTime);
            this.serverLogMessageService.save(logMessage);
        }
        return this.returnSuccessInfo("颜色同步成功");
    }

    @RequestMapping("/synShop")
    @ResponseBody
    @Override
    public MessageBox synchronizeShop() {
        System.out.println("开始同步店铺！"+CommonUtil.getDateString(new Date(),"yyyy-MM-dd HH:mm:ss"));
        this.logger.info("开始同步店铺！");
        long beginTime = System.currentTimeMillis();//2、结束时间
        ServerLogMessage logMessage = new ServerLogMessage();
        logMessage.setCreateTime(new Date());
        logMessage.setMessage("同步店铺");
        logMessage.setMethod("/syn/synShop");
        logMessage.setType(Constant.LogType.SYN);
        logMessage.setCreatorId("admin");
        try {
            Unit unit = CacheManager.getUnitById("1");
            if (CommonUtil.isNotBlank(unit)) {
                List<Unit> listShop = this.synErpService.synchronizeShop(unit.getOwnerId());
                if (CommonUtil.isBlank(listShop)) {
                    return this.returnFailInfo("未获取相关同步数据");
                }
                this.unitService.saveList(listShop);
                CacheManager.refreshUnitCache();
                this.logger.info("同步店铺成功！");
                System.out.println("同步店铺成功");
                return this.returnSuccessInfo("同步店铺成功!+共" + listShop.size());
            } else {
                return this.returnFailInfo("店铺同步失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.logger.error("店铺同步失败！");
            System.err.println("店铺同步失败！");
            try {
                return this.returnFailInfo("店铺同步失败");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }finally {
            logMessage.setConsumeTime(System.currentTimeMillis()-beginTime);
            this.serverLogMessageService.save(logMessage);
        }
        return this.returnSuccessInfo("同步店铺成功!");
    }

    @RequestMapping("/synSize")
    @ResponseBody
    @Override
    public MessageBox synchronizeSize() {
        this.logger.info("开始同步尺码！");
        long beginTime = System.currentTimeMillis();//2、结束时间
        ServerLogMessage logMessage = new ServerLogMessage();
        logMessage.setCreateTime(new Date());
        logMessage.setMessage("同步尺码");
        logMessage.setMethod("/syn/synSize");
        logMessage.setType(Constant.LogType.SYN);
        logMessage.setCreatorId("admin");
        try {
            String brandCode = getReqParam("brandCode");
            List<SizeSort> ssList = this.synErpService.synchronizeSizeSort(brandCode);
            List<Size> sizeList = this.synErpService.synchronizeSize(brandCode);
            if (CommonUtil.isBlank(sizeList)) {
                return this.returnFailInfo("未获取相关同步数据");
            }
            this.styleService.saveList3(sizeList, ssList);
            CacheManager.refreshSizeCache();
            this.logger.info("同步尺码成功");
            return returnSuccessInfo("同步尺码成功!共" + sizeList.size());
        } catch (Exception e) {
            e.printStackTrace();
            this.logger.error("尺码同步失败！");
            System.err.println("尺码同步失败！");
            try {
                return this.returnFailInfo("尺码同步失败");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }finally {
            logMessage.setConsumeTime(System.currentTimeMillis()-beginTime);
            this.serverLogMessageService.save(logMessage);
        }
        return this.returnSuccessInfo("尺码同步成功！");
    }

    @RequestMapping("/synAgent")
    @ResponseBody
    @Override
    public MessageBox synchronizeAgent() {
        this.logger.info("开始同步代理商！");
        long beginTime = System.currentTimeMillis();//2、结束时间
        ServerLogMessage logMessage = new ServerLogMessage();
        logMessage.setCreateTime(new Date());
        logMessage.setMessage("同步代理商");
        logMessage.setMethod("/syn/synAgent");
        logMessage.setType(Constant.LogType.SYN);
        logMessage.setCreatorId("admin");
        try {
            List<Unit> listAgent = this.synErpService.synchronizeAgent();
            if (CommonUtil.isBlank(listAgent)) {
                return this.returnFailInfo("未获取相关同步数据");
            }
            this.unitService.saveList(listAgent);
            CacheManager.refreshUnitCache();
            this.logger.info("同步代理商成功！");
            System.out.println("同步代理商成功");
            return this.returnSuccessInfo("同步代理商成功!共" + listAgent.size());
        } catch (Exception e) {
            e.printStackTrace();
            this.logger.error("代理商同步失败！");
            System.err.println("代理商同步失败！");
            try {
                return this.returnFailInfo("代理商同步失败");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }finally {
            logMessage.setConsumeTime(System.currentTimeMillis()-beginTime);
            this.serverLogMessageService.save(logMessage);
        }
        return this.returnSuccessInfo("同步代理商成功!");
    }

    @RequestMapping("/synStyleSort")
    @ResponseBody
    @Override
    public MessageBox synchronizeStyleSort() {
        return null;
    }

    @RequestMapping("/synVender")
    @ResponseBody
    @Override
    public MessageBox synchronizeVender() {
        this.logger.info("开始同步供应商！");
        long beginTime = System.currentTimeMillis();//2、结束时间
        ServerLogMessage logMessage = new ServerLogMessage();
        logMessage.setCreateTime(new Date());
        logMessage.setMessage("同步供应商");
        logMessage.setMethod("/syn/synVender");
        logMessage.setType(Constant.LogType.SYN);
        logMessage.setCreatorId("admin");
        try {
            Unit unit = CacheManager.getUnitById(getCurrentUser().getOwnerId());
            if (CommonUtil.isNotBlank(unit)) {
                List<Unit> venderList = this.synErpService.synchronizeVender(unit.getOwnerId());
                if (CommonUtil.isBlank(venderList)) {
                    return this.returnFailInfo("未获取相关同步数据");
                }
                this.unitService.saveList(venderList);
                CacheManager.refreshUnitCache();
                this.logger.info("同步供应商成功！");
                System.out.println("同步供应商成功");
                return this.returnSuccessInfo("同步供应商成功!共" + venderList.size());
            } else {
                System.err.println("同步供应商失败！未登录");
                return this.returnFailInfo("同步供应商失败!未登录");
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.logger.error("同步供应商失败！");
            System.err.println("同步供应商失败！");
            try {
                return this.returnFailInfo("同步供应商失败");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }finally {
            logMessage.setConsumeTime(System.currentTimeMillis()-beginTime);
            this.serverLogMessageService.save(logMessage);
        }
        return this.returnSuccessInfo("同步供应商成功!");
    }

    @RequestMapping("/synSizeSort")
    @ResponseBody
    @Override
    public MessageBox synchronizeSizeSort() {
        return null;
    }

    @RequestMapping("/synUnit")
    @ResponseBody
    @Override
    public MessageBox synchronizeUnit() {
        return null;
    }

    @RequestMapping("/synColorGroup")
    @ResponseBody
    @Override
    public MessageBox synchronizeColorGroup() {
        return null;
    }

    @RequestMapping("/synFactory")
    @ResponseBody
    @Override
    public MessageBox synchronizeFactory() {
        this.logger.info("开始同步工厂！");
        long beginTime = System.currentTimeMillis();//2、结束时间
        ServerLogMessage logMessage = new ServerLogMessage();
        logMessage.setCreateTime(new Date());
        logMessage.setMessage("同步工厂");
        logMessage.setMethod("/syn/synFactory");
        logMessage.setType(Constant.LogType.SYN);
        logMessage.setCreatorId("admin");
        try {
            Unit unit = CacheManager.getUnitById(getCurrentUser().getOwnerId());
            if (CommonUtil.isNotBlank(unit)) {
                List<Unit> factoryList = this.synErpService.synchronizeFactory();
                if (CommonUtil.isBlank(factoryList)) {
                    return this.returnFailInfo("未获取相关同步数据");
                }
                this.unitService.saveList(factoryList);
                CacheManager.refreshUnitCache();
                this.logger.info("同步工厂成功！");
                System.out.println("同步工厂成功");
                return this.returnSuccessInfo("同步工厂成功!共" + factoryList.size());
            } else {
                System.err.println("同步工厂失败！未登录");
                return this.returnFailInfo("同步工厂失败!未登录");
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.logger.error("同步工厂失败！");
            System.err.println("同步工厂失败！");
            try {
                return this.returnFailInfo("同步工厂失败");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }finally {
            logMessage.setConsumeTime(System.currentTimeMillis()-beginTime);
            this.serverLogMessageService.save(logMessage);
        }
        return this.returnSuccessInfo("同步工厂成功!");    }

    @RequestMapping("/synProduct")
    @ResponseBody
    @Override
    public MessageBox synchronizeProduct() {
        this.logger.info("开始同步商品！");
        long beginTime = System.currentTimeMillis();//2、结束时间
        ServerLogMessage logMessage = new ServerLogMessage();
        logMessage.setCreateTime(new Date());
        logMessage.setMessage("同步商品");
        logMessage.setMethod("/syn/synProduct");
        logMessage.setType(Constant.LogType.SYN);
        logMessage.setCreatorId("admin");
        try {
            String brandCode = getReqParam("brandCode");
            List<Product> productList = this.synErpService.synchronizeProduct(brandCode);
            if (CommonUtil.isBlank(productList)) {
                return this.returnFailInfo("未获取相关同步数据");
            }
            this.productService.save(productList);
            CacheManager.refreshProductCache();
            this.logger.info("同步商品成功");
            return this.returnSuccessInfo("同步商品成功!共" + productList.size());
        } catch (Exception e) {
            e.printStackTrace();
            this.logger.error("商品同步失败！");
            System.err.println("商品同步失败！");
            try {
                return this.returnFailInfo("商品同步失败");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }finally {
            logMessage.setConsumeTime(System.currentTimeMillis()-beginTime);
            this.serverLogMessageService.save(logMessage);
        }
        return this.returnSuccessInfo("同步商品成功！");
    }

    @RequestMapping("/synRole")
    @ResponseBody
    @Override
    public MessageBox synchronizeRole() {
        return null;
    }

    @RequestMapping("/synPropertyKey")
    @ResponseBody
    @Override
    public MessageBox synchronizePropertyKey() {
        this.logger.info("开始同步属性！");
        long beginTime = System.currentTimeMillis();//2、结束时间
        ServerLogMessage logMessage = new ServerLogMessage();
        logMessage.setCreateTime(new Date());
        logMessage.setMessage("同步属性");
        logMessage.setMethod("/syn/synPropertyKey");
        logMessage.setType(Constant.LogType.SYN);
        logMessage.setCreatorId("admin");
        try {
            String brandcode = getReqParam("brandCode");
            List<PropertyKey> list = this.synErpService.synchronizeProperty(brandcode);
            if (CommonUtil.isBlank(list)) {
                return this.returnFailInfo("未获取相关同步数据");
            }
            this.propertyKeyService.saveAllPropertyKey(list);
            CacheManager.refreshPropertyCache();
            this.logger.info("同步属性成功！");
            return this.returnSuccessInfo("同步属性成功!共" + list.size());
        } catch (Exception e) {
            e.printStackTrace();
            this.logger.error("属性同步失败！");
            System.err.println("属性同步失败！");
            try {
                return this.returnFailInfo("属性同步失败");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }finally {
            logMessage.setConsumeTime(System.currentTimeMillis()-beginTime);
            this.serverLogMessageService.save(logMessage);
        }
        return this.returnSuccessInfo("同步属性成功！");
    }

    @RequestMapping("/synRoleRes")
    @ResponseBody
    @Override
    public MessageBox synchronizeRoleRes() {
        return null;
    }

    @RequestMapping("/synFitting")
    @ResponseBody
    @Override
    public MessageBox synchronizeFitting() {
        this.logAllRequestParams();
        this.logger.info("开始同步试衣数据！");
        long beginTime = System.currentTimeMillis();//2、结束时间
        ServerLogMessage logMessage = new ServerLogMessage();
        logMessage.setCreateTime(new Date());
        logMessage.setMessage("同步试衣数据");
        logMessage.setMethod("/syn/synFitting");
        logMessage.setType(Constant.LogType.SYN);
        logMessage.setCreatorId("admin");
        try {
            Date endDate = new Date();
            Calendar ca = Calendar.getInstance();
            String begin = CommonUtil.getDateString(ca.getTime(), "yyyy-MM-dd 00:00:00");
            String end = CommonUtil.getDateString(endDate, "yyyy-MM-dd 23:59:59");
            List<FittingRecord> fittings = this.fittingRecordService.findCountRecord(CommonUtil.converStrToDate(begin, "yyyy-MM-dd HH:mm:ss"),
                    CommonUtil.converStrToDate(end, "yyyy-MM-dd HH:mm:ss"));
            if(CommonUtil.isBlank(fittings)){
                return this.returnFailInfo("未获取相关同步数据");
            }
            this.synErpService.synchronizeFitting(fittings);
            this.logger.info("同步试衣数据成功！");
            System.out.println("同步试衣数据成功");
            return this.returnSuccessInfo("同步试衣数据成功！共"+fittings.size());
        } catch (Exception e) {
            e.printStackTrace();
            this.logger.error("同步试衣数据失败！");
            System.err.println("同步试衣数据失败！");
            try {
                return this.returnFailInfo("同步试衣数据失败");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }finally {
            logMessage.setConsumeTime(System.currentTimeMillis()-beginTime);
            this.serverLogMessageService.save(logMessage);
        }
        return this.returnSuccessInfo("同步试衣数据成功！");

    }

    @RequestMapping("/synCustomer")
    @ResponseBody
    @Override
    public MessageBox synchronizeCustomer() {
        this.logger.info("开始同步会员！");
        long beginTime = System.currentTimeMillis();//2、结束时间
        ServerLogMessage logMessage = new ServerLogMessage();
        logMessage.setCreateTime(new Date());
        logMessage.setMessage("同步会员");
        logMessage.setMethod("/syn/synCustomer");
        logMessage.setType(Constant.LogType.SYN);
        logMessage.setCreatorId("admin");
        try {
            String shopId = this.getReqParam("shopId");
            List<Customer> customerList = this.synErpService.synchronizeCustomer(shopId);
            if(CommonUtil.isBlank(customerList)){
                return this.returnFailInfo("未获取相关同步数据");
            }
            this.customerService.saveList(customerList);
            System.out.println("同步会员成功");
            this.logger.info("同步会员成功！");
            return this.returnSuccessInfo("同步会员成功!共"+customerList.size());
        } catch (Exception e) {
            e.printStackTrace();
            this.logger.error("同步会员失败！");
            System.err.println("同步会员失败！");
            try {
                return this.returnFailInfo("同步会员失败");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }finally {
            logMessage.setConsumeTime(System.currentTimeMillis()-beginTime);
            this.serverLogMessageService.save(logMessage);
        }
        return this.returnSuccessInfo("同步会员成功");
    }

    @RequestMapping("/")
    @ResponseBody
    @Override
    public MessageBox synchronize() {
        return null;
    }

    @RequestMapping("/synResource")
    @ResponseBody
    @Override
    public MessageBox synchronizeResource() {
        return null;
    }

    @RequestMapping("/synUser")
    @ResponseBody
    @Override
    public MessageBox synchronizeUser() {
        this.logger.info("开始同步用户！");
        long beginTime = System.currentTimeMillis();//2、结束时间
        ServerLogMessage logMessage = new ServerLogMessage();
        logMessage.setCreateTime(new Date());
        logMessage.setMessage("同步用户");
        logMessage.setMethod("/syn/synUser");
        logMessage.setType(Constant.LogType.SYN);
        logMessage.setCreatorId("admin");
        try {
            Unit unit = CacheManager.getUnitById("1");
            if (CommonUtil.isNotBlank(unit)) {
                List<User> userList = this.synErpService.synchronizeUser(unit.getOwnerId());
                if (CommonUtil.isBlank(userList)) {
                    return this.returnFailInfo("未获取相关同步数据");
                }
                this.userService.saveList(userList);
                System.out.println("同步用户成功");
                this.logger.info("同步用户成功！");
                return this.returnSuccessInfo("同步用户成功!共" + userList.size());
            } else {
                this.logger.error("同步会员失败");
                System.err.println("同步用户失败！");
                return this.returnFailInfo("同步会员失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.logger.error("同步用户失败！");
            System.err.println("同步用户失败！");
            try {
                return this.returnFailInfo("同步用户失败");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }finally {
            logMessage.setConsumeTime(System.currentTimeMillis()-beginTime);
            this.serverLogMessageService.save(logMessage);
        }
        return this.returnSuccessInfo("同步用户成功!");
    }

    @RequestMapping("/synProductInfo")
    @ResponseBody
    public MessageBox synchronizeProductInfo() {
        System.out.println("开始商品信息同步！"+CommonUtil.getDateString(new Date(),"yyyy-MM-dd HH:mm:ss"));
        this.logger.info("开始商品信息同步！");
        long beginTime = System.currentTimeMillis();//2、结束时间
        ServerLogMessage logMessage = new ServerLogMessage();
        logMessage.setCreateTime(new Date());
        logMessage.setMessage("同步商品信息");
        logMessage.setMethod("/syn/synProductInfo");
        logMessage.setType(Constant.LogType.SYN);
        logMessage.setCreatorId("admin");
        try{

            List<Unit> shopList = this.unitService.findShop();
            for(Unit shop :shopList){
                ProductInfoList productInfo = this.synErpService.synProductInfoList(shop.getCode());
                if(CommonUtil.isNotBlank(productInfo)){
                    this.productService.saveProductInfo(productInfo);
                    if(productInfo.getProductList().size() > 0){
                        CacheManager.refreshProductCache();
                    }
                    if(productInfo.getStyleList().size() > 0){
                        CacheManager.refreshStyleCache();
                    }
                    if(productInfo.getColorList().size() > 0){
                        CacheManager.refreshColorCache();
                    }
                    if(productInfo.getSizeList().size() > 0){
                        CacheManager.refreshSizeCache();
                    }
                    if(productInfo.getPropertyKeyList().size() > 0){
                        CacheManager.refreshPropertyCache();
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            this.logger.error("同步商品信息失败！");
            System.err.println("同步商品信息失败！");
        }finally {
            logMessage.setConsumeTime(System.currentTimeMillis()-beginTime);
            this.serverLogMessageService.save(logMessage);
        }

        return this.returnSuccessInfo("同步商品信息成功!");
    }

    @RequestMapping("/synNeoenProductInfo")
    @ResponseBody
    public MessageBox synNeoenProductInfo() {
        System.out.println("开始商品信息同步！"+CommonUtil.getDateString(new Date(),"yyyy-MM-dd HH:mm:ss"));
        this.logger.info("开始商品信息同步！");
        long beginTime = System.currentTimeMillis();//2、结束时间
        ServerLogMessage logMessage = new ServerLogMessage();
        logMessage.setCreateTime(new Date());
        logMessage.setMessage("同步商品信息");
        logMessage.setMethod("/syn/synProductInfo");
        logMessage.setType(Constant.LogType.SYN);
        logMessage.setCreatorId("admin");
        try{

            ProductInfoList productInfoList = this.synErpService.synProductInfoList("");
            this.productService.saveProductInfo(productInfoList);
            if(productInfoList.getProductList().size() > 0){
                CacheManager.refreshProductCache();
            }
            if(productInfoList.getStyleList().size() > 0){
                CacheManager.refreshStyleCache();
            }
            if(productInfoList.getColorList().size() > 0){
                CacheManager.refreshColorCache();
            }
            if(productInfoList.getSizeList().size() > 0){
                CacheManager.refreshSizeCache();
            }
            if(CommonUtil.isNotBlank(productInfoList.getPropertyKeyList())){
                CacheManager.refreshPropertyCache();
            }

        } catch (Exception e) {
            e.printStackTrace();
            this.logger.error("同步商品信息失败！");
            System.err.println("同步商品信息失败！");
            return this.returnFailInfo("同步商品信息失败!");
        }finally {
            logMessage.setConsumeTime(System.currentTimeMillis()-beginTime);
            this.serverLogMessageService.save(logMessage);
        }
        return this.returnSuccessInfo("同步商品信息成功!");
    }



    @Override
    public String index() {
        return null;
    }
}
