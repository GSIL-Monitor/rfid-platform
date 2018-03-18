package com.casesoft.dmc.extend.tiantan;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.service.ISynErpService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.service.cfg.PropertyKeyService;
import com.casesoft.dmc.service.product.ProductService;
import com.casesoft.dmc.service.product.StyleService;
import com.casesoft.dmc.service.shop.CustomerService;
import com.casesoft.dmc.service.sys.impl.UnitService;
import com.casesoft.dmc.service.sys.impl.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/*@Service
@SuppressWarnings("unchecked")*/
public class TiantanSyn {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private StyleService styleService;
	private PropertyKeyService propertyKeyService;
	private UnitService unitService;
	private ProductService productService;
    private CustomerService customerService;
	private UserService userService;
	private ISynErpService synErpService;
	public void synBasicinfo() throws Exception{
		List list = this.synErpService.synchronizeProperty("1");
		this.propertyKeyService.saveAllPropertyKey(list);
        CacheManager.refreshPropertyCache();
    	logger.info(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss")+";属性同步成功！");
		System.out.println(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss")+";属性同步成功！");
		
		List listStorage = this.synErpService.synchronizeStorage();
		this.unitService.saveList(listStorage);
		CacheManager.refreshUnitCache();
		logger.info(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss")+";仓库同步成功！");
		System.out.println(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss")+";仓库同步成功！");
			
		List listShop = this.synErpService.synchronizeShop("1");
		this.unitService.saveList(listShop);
		CacheManager.refreshUnitCache();
		logger.info(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss")+";门店同步成功！");
		System.out.println(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss")+";门店同步成功！");
		
		List<Unit> venderList = this.synErpService.synchronizeVender("1");
		this.unitService.saveList(venderList);
 		CacheManager.refreshUnitCache();
 		logger.info(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss")+";代理商同步成功！");
		System.out.println(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss")+";代理商同步成功！");
		
		List listAgent = this.synErpService.synchronizeAgent();
		this.unitService.saveList(listAgent);
		CacheManager.refreshUnitCache();
		logger.info(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss")+";供应商同步成功！");
		System.out.println(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss")+";代理商同步成功！");
		
		List factorylists = this.synErpService.synchronizeFactory();
		this.unitService.saveList(factorylists);
 		CacheManager.refreshUnitCache();
 		logger.info(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss")+";工厂同步成功！");
		System.out.println(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss")+";工厂同步成功！");
		
		
	}
	public void synStyleInfo() throws Exception{
		List styleList = this.synErpService.synchronizeStyle("1");
		this.styleService.saveList(styleList);
		CacheManager.refreshStyleCache();
		logger.info(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss")+";款同步成功");
		System.out.println("款同步成功");
		List colorList = this.synErpService.synchronizeColor("1");
		this.styleService.saveList2(colorList);
		CacheManager.refreshColorCache();
		logger.info(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss")+";颜色同步成功");
		System.out.println(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss")+";颜色同步成功");
 		List ssList = this.synErpService.synchronizeSizeSort("1");
		List sizeList = this.synErpService.synchronizeSize("1");
		this.styleService.saveList3(sizeList, ssList);
 		CacheManager.refreshSizeCache();
		logger.info(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss")+";尺寸同步成功");
		System.out.println(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss")+";尺寸同步成功");
		List productList = this.synErpService.synchronizeProduct("1");
		this.productService.save(productList);
		CacheManager.refreshProductCache();
		logger.info(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss")+";商品同步成功");
		System.out.println(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss")+";商品同步成功");
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
}
