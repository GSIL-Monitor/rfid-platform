package com.casesoft.dmc.cache;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.extend.third.model.pl.PlWmsShopBindingRelation;
import com.casesoft.dmc.model.cfg.Device;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.cfg.PropertyType;
import com.casesoft.dmc.model.pad.AccessToken;
import com.casesoft.dmc.model.product.*;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.sys.*;
import com.casesoft.dmc.service.cfg.DeviceService;
import com.casesoft.dmc.service.cfg.PropertyKeyService;
import com.casesoft.dmc.service.product.ProductService;
import com.casesoft.dmc.service.product.StyleService;
import com.casesoft.dmc.service.shop.CustomerService;
import com.casesoft.dmc.service.stock.EpcStockService;
import com.casesoft.dmc.service.sys.SettingService;
import com.casesoft.dmc.service.sys.impl.ResourceService;
import com.casesoft.dmc.service.sys.impl.RoleService;
import com.casesoft.dmc.service.sys.impl.UnitService;
import com.casesoft.dmc.service.sys.impl.UserService;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.Map.Entry;

@SuppressWarnings("unchecked")
public class CacheManager {
	private static Logger logger = LoggerFactory.getLogger(CacheManager.class);
	private static net.sf.ehcache.CacheManager cacheManager;
	private static Cache cache;
	private static int sizeMaxSeqNo;
	private static int maxProductId;
	public static Unit company = null;
	private static RedisUtils redisUtils;

	private static String PREFIX_PRODUCT = "product";

	private static String PREFIX_COLOR = "color";

	private static String PREFIX_SIZE = "size";

	private static String PREFIX_STYLE = "style";

	private static String PREFIX_NEWPRODUCT = "newProduct";

	private static String PREFIX_PROPERTYKEY = "propertyKey";

	private static String PREFIX_PROPERTYTYPE = "propertyType";

	private static String PREFIX_PLAYLOUNGEPROPERTYKEY = "playloungePropertyKey";

	private static String PREFIX_SIZESORT = "SizeSort";

	private static String PREFIX_ACCESSTOKEN = "AccessToken";

	private static String PREFIX_MINIPROGRAMACCESSTOKEN = "MiniProgramAccessToken";
	//--------------------------新增redis缓存数据-------------------------------------------------
	private static String PREFIX_UNIT = "unit";

	private static String PREFIX_DEVICE = "DeviceMap";

	private static String PREFIX_ROLE = "role";

	private static String PREFIX_USER = "user";

	private static String PREFIX_ROLERES = "authMap";

	private static String PREFIX_RESOURCE = "resource";


	private static String PREFIX_CUSTOMER = "customer";

	private static String PREFIX_SYSSITTING = "sysSetting";

	private static String PREFIX_CHECKWAREHOUSE="checkwareouse";

	private static long LIVETIME = 3000;

	public static void initCache() throws Exception {
		initRedisManager();
		initUserCache();
		initRoleCache();
		initResourceCache();
		initAuthCache();
		initUnitMap();
		initDeviceMap();
		initProductCache();
		initColorCache();
		initSizeCache();
		initStyleCache();
	    initPropertyCache();
		initPropertyTypeCache();
		initSizeSortCache();
		initSysSetting();
		initCustomerCache();
		initCheckWarehouse();
		initMaxVersionId();
	}

	public static void refresh() throws Exception {
		refreshPropertyCache();
		refreshPropertyTypeCache();
		refreshUserCache();
		refreshCustomer();
		refreshSysSetting();
		refreshSizeCache();
		refreshColorCache();
		refreshStyleCache();
		refreshProductCache();
		refreshDeviceCache();
		refreshUnitCache();
		refreshAuthCache();
		refreshResourceCache();
		refreshRoleCache();
		refreshSizeSortCache();
		refreshMaxVersionId();
	}

	public static void initUserCache() {
		UserService userService = (UserService) SpringContextUtil
				.getBean("userService");
		List<User> userList = userService.getAllUser();
		for (User u : userList) {
			redisUtils.hset(PREFIX_USER, u.getId(), JSON.toJSONString(u));
		}
	}

	public static void refreshUserCache() {
		redisUtils.del(PREFIX_USER);
		initUserCache();
	}

	/**
	 * 增量更新Usr缓存
	 * */
	public static void refreshUserCache(List<User> userList){
		for (User u : userList) {
			redisUtils.hset(PREFIX_USER, u.getId(), JSON.toJSONString(u));
		}
	}

	private static void initRoleCache() {
		RoleService roleService = (RoleService) SpringContextUtil
				.getBean("roleService");
		List<Role> roleList = roleService.getAllRoles();
		for (Role r : roleList) {
			redisUtils.hset(PREFIX_ROLE, r.getId(), JSON.toJSONString(r));

		}
	}


	public static void refreshRoleCache() {
		redisUtils.del(PREFIX_ROLE);
		initRoleCache();
	}

	/**
	 * 增量更新Role缓存
	 * */
	public static void refreshRoleCache(List<Role> roleList) {
		for (Role r : roleList) {
			redisUtils.hset(PREFIX_ROLE, r.getId(), JSON.toJSONString(r));

		}
	}

	private static void initResourceCache() {
		ResourceService resourceService = (ResourceService) SpringContextUtil
				.getBean("resourceService");
		List<Resource> resList = resourceService.getResourceList();
		for (Resource r : resList) {//
			redisUtils.hset(PREFIX_RESOURCE, r.getCode(), JSON.toJSONString(r));
		}

	}

	/**
	 * 增量更新Resource缓存
	 * */
	public static void refreshResourceCache(List<Resource> resList) {
		for (Resource r : resList) {//
			redisUtils.hset(PREFIX_RESOURCE, r.getCode(), JSON.toJSONString(r));
		}

	}

	public static void refreshResourceCache() {
		redisUtils.del(PREFIX_RESOURCE);
		initResourceCache();
	}

	private static void initAuthCache() {
		RoleService roleService = (RoleService) SpringContextUtil
				.getBean("roleService");
		List<RoleRes> roleResList = roleService.findAllRoleRes();
		Map<String, List<RoleRes>> authMap = new HashMap<String, List<RoleRes>>();
		for (RoleRes rr : roleResList) {
			String roleId = String.valueOf(rr.getRoleId());
			if (authMap.containsKey(roleId)) {
				authMap.get(roleId).add(rr);
			} else {
				List<RoleRes> rrList = new ArrayList<RoleRes>();
				rrList.add(rr);
				authMap.put(roleId, rrList);
			}
		}
		for (String roleId : authMap.keySet()) {
			List<RoleRes> rlist = authMap.get(roleId);
			redisUtils.hset(PREFIX_ROLERES, roleId, JSON.toJSONString(rlist));
		}


	}


	public static void refreshAuthCache() {
		redisUtils.del(PREFIX_ROLERES);
		initAuthCache();
	}

	private static void initUnitMap() throws Exception {
		UnitService unitService = (UnitService) SpringContextUtil
				.getBean("unitService");
		List<Unit> unitList = unitService.getAll();
		for (Unit unit : unitList) {
			if (unit.getType() == 1) {
				company = unit;
			}
			redisUtils.hset(PREFIX_UNIT, unit.getCode(), JSON.toJSONString(unit));
			redisUtils.hset(PREFIX_UNIT, unit.getId(), JSON.toJSONString(unit));//
		}
	}

	public static void refreshUnitCache() throws Exception {
		redisUtils.del(PREFIX_UNIT);
		initUnitMap();
	}

	public static void refreshUnitCache(List<Unit> unitList) throws Exception {
		for (Unit unit : unitList) {
			if (unit.getType() == 1) {
				company = unit;
			}
			redisUtils.hset(PREFIX_UNIT, unit.getCode(), JSON.toJSONString(unit));
			redisUtils.hset(PREFIX_UNIT, unit.getId(), JSON.toJSONString(unit));//
		}
	}

	private static void initDeviceMap() {
		DeviceService deviceService = (DeviceService) SpringContextUtil
				.getBean("deviceService");
		List<Device> deviceList = deviceService.getAll();
		for (Device device : deviceList) {
			redisUtils.hset(PREFIX_DEVICE, device.getCode(), JSON.toJSONString(device));
		}

	}


	public static void refreshDeviceCache() {
		redisUtils.del(PREFIX_DEVICE);
		initDeviceMap();
	}

	public static void refreshDeviceCache(List<Device> deviceList) {
		for (Device device : deviceList) {
			redisUtils.hset(PREFIX_DEVICE, device.getCode(), JSON.toJSONString(device));
		}
	}
	public static void initMaxVersionId(){
		ProductService productService = (ProductService) SpringContextUtil
				.getBean("productService");
		StyleService styleService = (StyleService) SpringContextUtil
				.getBean("styleService");
		//得到当前商品最大版本号放进redis
		EpcStockService epcStockService = (EpcStockService) SpringContextUtil
				.getBean("epcStockService");
		long productMaxVersionId = productService.getMaxVersionId();
		redisUtils.hset("maxVersionId","productMaxVersionId",JSON.toJSONString(productMaxVersionId));
		long EpcStockMaxVersionId = epcStockService.getMaxVersionId();
		redisUtils.hset("maxVersionId","EpcStockMaxVersionId",JSON.toJSONString(EpcStockMaxVersionId));
		long styleMaxVersionId = styleService.getMaxVersionId();
		redisUtils.hset("maxVersionId","styleMaxVersionId",JSON.toJSONString(styleMaxVersionId));
	}
	public static void initProductCache() {
		long startTime = System.currentTimeMillis();   //获取开始时间
		ProductService productService = (ProductService) SpringContextUtil
				.getBean("productService");

		List<Product> list = productService.getAll();
		Collections.sort(list, new Comparator<Product>() {

			@Override
			public int compare(Product o1, Product o2) {
				return o1.getId().compareTo(o2.getId());
			}

		});
		if (CommonUtil.isBlank(list) || list.size() == 0) {
			maxProductId = 0;
		} else {
			if (Integer.parseInt(list.get(list.size() - 1).getId()) - list.size() != 1) {
				maxProductId = Integer.parseInt(list.get(list.size() - 1).getId()) + 1;
			} else {
				maxProductId = list.size();
			}
		}
		for (Product p : list) {
			redisUtils.hset(PREFIX_PRODUCT, p.getCode(), JSON.toJSONString(p));
			redisUtils.hset(PREFIX_PRODUCT, p.getId(), JSON.toJSONString(p));
		}
		long endTime = System.currentTimeMillis(); //获取结束时间

		System.out.println("Product缓存程序运行时间： " + (startTime - endTime) + "ms");

	}

	public static void refreshProductCache() {
		redisUtils.del(PREFIX_PRODUCT);
		initProductCache();
	}

	public static void refreshProductCache(List<Product> productList) {
		for (Product p : productList) {
			redisUtils.hset(PREFIX_PRODUCT, p.getCode(), JSON.toJSONString(p));
			redisUtils.hset(PREFIX_PRODUCT, p.getId(), JSON.toJSONString(p));
		}
	}

	private static void initColorCache() {
		StyleService styleService = (StyleService) SpringContextUtil
				.getBean("styleService");
		List<Color> cList = styleService.findAll();
		for (Color c : cList) {
			redisUtils.hset(PREFIX_COLOR, c.getId(), JSON.toJSONString(c));
		}

	}

	public static void delColorCache(String item){
		redisUtils.hdel(PREFIX_COLOR,item);
	}

	public static void refreshColorCache() {
		redisUtils.del(PREFIX_COLOR);
		initColorCache();
	}

	public static void refreshColorCache(List<Color> colorList) {
		for (Color c : colorList) {
			redisUtils.hset(PREFIX_COLOR, c.getId(), JSON.toJSONString(c));
		}
	}

	private static void initSizeCache() {
		StyleService styleService = (StyleService) SpringContextUtil
				.getBean("styleService");
		List<Size> sList = styleService.findAll2();
		for (Size s : sList) {
			redisUtils.hset(PREFIX_SIZE, s.getId(), JSON.toJSONString(s));
		}

	}

	public static void delSizeCache(String item){
		redisUtils.hdel(PREFIX_SIZE,item);
	}

	public static void refreshSizeCache() {
		redisUtils.del(PREFIX_SIZE);
		initSizeCache();
	}

	public static void refreshSizeCache(List<Size> sizeList) {
		for (Size s : sizeList) {
			redisUtils.hset(PREFIX_SIZE, s.getId(), JSON.toJSONString(s));
		}
	}

	private static void initStyleCache() {
		StyleService styleService = (StyleService) SpringContextUtil
				.getBean("styleService");
		List<Style> styleList = styleService.getAll();
		for (Style style : styleList) {
			redisUtils.hset(PREFIX_STYLE, style.getId(), JSON.toJSONString(style));
		}
	}

	public static void refreshStyleCache() {
		redisUtils.del(PREFIX_STYLE);
		initStyleCache();
	}


	public static void refreshStyleCache(List<Style> styleList) {
		for (Style style : styleList) {
			redisUtils.hset(PREFIX_STYLE, style.getId(), JSON.toJSONString(style));
		}
	}

	public static void initPropertyCache() {
		PropertyKeyService propertyKeyService = (PropertyKeyService) SpringContextUtil
				.getBean("propertyKeyService");
		List<PropertyKey> keyList = propertyKeyService.getAll();
		for (PropertyKey key : keyList) {
			redisUtils.hset(PREFIX_PROPERTYKEY, key.getId(), JSON.toJSONString(key));
			redisUtils.hset(PREFIX_PLAYLOUNGEPROPERTYKEY, key.getCode() + "-" + key.getType(), JSON.toJSONString(key));
		}

	}


	public static void refreshPropertyCache() {
		redisUtils.del(PREFIX_PROPERTYKEY);
		initPropertyCache();
	}
	public static void refreshMaxVersionId(){
		redisUtils.del("maxVersionId");
		initMaxVersionId();
	}

	public static void refreshPropertyCache(List<PropertyKey> propertyKeyList) {
		for (PropertyKey key : propertyKeyList) {
			redisUtils.hset(PREFIX_PROPERTYKEY, key.getId(), JSON.toJSONString(key));
			redisUtils.hset(PREFIX_PLAYLOUNGEPROPERTYKEY, key.getCode() + "-" + key.getType(), JSON.toJSONString(key));
		}
	}

	public static void initPropertyTypeCache() {
		PropertyKeyService propertyKeyService = (PropertyKeyService) SpringContextUtil
				.getBean("propertyKeyService");
		List<PropertyType> typeList = propertyKeyService.findAll();
		for (PropertyType type : typeList) {
			redisUtils.hset(PREFIX_PROPERTYTYPE, type.getKeyId(), JSON.toJSONString(type));
		}
	}

	public static void refreshPropertyTypeCache() {
		redisUtils.del(PREFIX_PROPERTYTYPE);
		initPropertyTypeCache();
	}

	public static void refreshPropertyTypeCache(List<PropertyType> propertyTypeList) {
		for (PropertyType type : propertyTypeList) {
			redisUtils.hset(PREFIX_PROPERTYTYPE, type.getKeyId(), JSON.toJSONString(type));
		}
	}

	private static void initSizeSortCache() {
		sizeSortList = new ArrayList<SizeSort>();
		StyleService styleService = (StyleService) SpringContextUtil
				.getBean("styleService");
		sizeSortList = styleService.findSizeSort();
		if (getSizeMap() == null)
			refreshSizeCache();
		for (SizeSort ss : sizeSortList) {
			for (Size s : getSizeMap().values()) {
                /*
                 * if (s.getSortId().equals(ss.getId())) { ss.addSize(s); }
                 */
			}
			Collections.sort(ss.getSizeList(), new Comparator<Size>() {

				@Override
				public int compare(Size s1, Size s2) {
					return s1.getSeqNo() - s2.getSeqNo();
				}

			});

			if (ss.getSizeList().size() > sizeMaxSeqNo) {
				sizeMaxSeqNo = ss.getSizeList().size();
			}
		}
		Collections.sort(sizeSortList, new Comparator<SizeSort>() {

			@Override
			public int compare(SizeSort o1, SizeSort o2) {
				return o1.getSizeList().size() - o2.getSizeList().size();
			}

		});

		//cache.put(new Element("SizeSort", sizeSortList));
		for (SizeSort ss : sizeSortList) {
			redisUtils.lSet(PREFIX_SIZESORT, JSON.toJSONString(ss));
		}
	}


	public static void refreshSizeSortCache() {
		redisUtils.del(PREFIX_SIZESORT);
		initSizeSortCache();
	}


	public static void refreshSizeSortCache(List<SizeSort> sizeSortList) {
		for (SizeSort ss : sizeSortList) {
			redisUtils.lSet(PREFIX_SIZESORT, JSON.toJSONString(ss));
		}
	}

	public static void initSysSetting() {
		SettingService settingService = (SettingService) SpringContextUtil
				.getBean("settingService");
		List<Setting> sList = settingService.getAll();//
		for (Setting s : sList) {
			redisUtils.hset(PREFIX_SYSSITTING, s.getId(), JSON.toJSONString(s));
		}
	}

	public static void refreshSysSetting() {
		redisUtils.del(PREFIX_SYSSITTING);
		initSysSetting();
	}

	public static void refreshSysSetting(List<Setting> settingList) {
		for (Setting s : settingList) {
			redisUtils.hset(PREFIX_SYSSITTING, s.getId(), JSON.toJSONString(s));
		}
	}

	public static void initCustomerCache() {
		CustomerService customerService = (CustomerService) SpringContextUtil
				.getBean("customerService");
		List<Customer> sList = customerService.getAll();
		Map<String, Customer> settingMap = new HashMap<String, Customer>();
		for (Customer s : sList) {
//            settingMap.put(s.getId(), s);
			redisUtils.hset(PREFIX_CUSTOMER, s.getId(), JSON.toJSONString(s));
		}
	}

	public static void refreshCustomer() {
		redisUtils.del(PREFIX_CUSTOMER);
		initCustomerCache();
	}

	public static void refreshCustomer(List<Customer> customerList) {
		for (Customer s : customerList) {
			redisUtils.hset(PREFIX_CUSTOMER, s.getId(), JSON.toJSONString(s));
		}
	}

	public static List<PropertyKey> getCodeDetailListByType(String typeCode) {
		String pStr = null;
		PropertyKey p = null;
		Map<Object, Object> pMap = redisUtils.hmget(PREFIX_PROPERTYKEY);
		List<PropertyKey> detailList = new ArrayList<>();
		if (CommonUtil.isNotBlank(pMap)) {
			for (Entry<Object, Object> entry : pMap.entrySet()) {
				pStr = (String) entry.getValue();
				p = JSONObject.parseObject(pStr, PropertyKey.class);
				detailList.add(p);
			}
		}
		return detailList;
	}

	public static Map<String, String> getRoleMap() {
		Map<Object, Object> roleObjMap = redisUtils.hmget(PREFIX_ROLERES);
		Map<String, String> roleMap = new HashMap<>();
		if (CommonUtil.isNotBlank(roleObjMap)) {
			for (String m : roleMap.keySet()) {
				roleMap.put(m.toString(), roleMap.get(m).toString());
			}
		}
		return roleMap;
	}

	public static Map<String, Resource> getResourceMap() {
		Map<Object, Object> resObjMap = redisUtils.hmget(PREFIX_ROLERES);
		Map<String, Resource> resMap = new HashMap<>();
		if (CommonUtil.isNotBlank(resObjMap)) {
			for (Object m : resObjMap.values()) {
				Resource resource = JSON.parseObject(m.toString(), Resource.class);
				resMap.put(resource.getCode(), resource);
			}
		}
		return resMap;

	}

	public static Map<String, List<RoleRes>> getAuthMap() {
		Map<Object, Object> keyMap = redisUtils.hmget(PREFIX_ROLERES);
		Map<String, List<RoleRes>> resMap = new HashMap<>();
		for (Object roleId : keyMap.keySet()) {
			String roleRes = redisUtils.hgetString(PREFIX_ROLE, roleId.toString());
			List<RoleRes> resList = JSON.parseArray(roleRes, RoleRes.class);
			resMap.put(roleId.toString(), resList);
		}

		return resMap;
	}

	public static Map<String, Unit> getUnitMap() {
		UnitService unitService = (UnitService) SpringContextUtil.getBean("unitService");
		List<Unit> unitList = unitService.getAll();
		Map<String, Unit> unitMap = new HashMap<String, Unit>();
		for (Unit unit : unitList) {
			unitMap.put(unit.getId(), unit);
		}
		return unitMap;
	}

	public static Map<String, Device> getDeviceMap() {
		Map<Object, Object> deviceStrMap = redisUtils.hmget(PREFIX_DEVICE);
		Map<String, Device> deviceMap = new HashMap<>();
		for (Object deviceStr : deviceStrMap.values()) {
			Device device = JSON.parseObject(deviceStr.toString(), Device.class);
			deviceMap.put(device.getCode(), device);
		}
		return deviceMap;
	}


	public static Map<String, Color> getColorMap() {
		String colorStr = null;
		Color color = null;
		Map<Object, Object> colorMap = redisUtils.hmget(PREFIX_COLOR);
		HashMap<String, Color> map = new HashMap<String, Color>();
		if (CommonUtil.isNotBlank(colorMap)) {
			for (Entry<Object, Object> entry : colorMap.entrySet()) {
				colorStr = (String) entry.getValue();
				color = JSONObject.parseObject(colorStr, Color.class);
				map.put(color.getId(), color);
			}
		}
		return map;
	}

	public static Map<String, Size> getSizeMap() {
		String sizeStr = null;
		Size size = null;
		Map<Object, Object> sizeMap = redisUtils.hmget(PREFIX_SIZE);
		HashMap<String, Size> map = new HashMap<String, Size>();
		if (CommonUtil.isNotBlank(sizeMap)) {
			for (Entry<Object, Object> entry : sizeMap.entrySet()) {
				sizeStr = (String) entry.getValue();
				size = JSONObject.parseObject(sizeStr, Size.class);
				map.put(size.getId(), size);
			}
		}
		return map;
	}

	public static Map<String, Style> getStyleMap() {
		Map<String, Style> styleMap = new HashMap<>();
		Map<Object, Object> redisMap = redisUtils.hmget(PREFIX_STYLE);
		for (Object styleStr : redisMap.values()) {
			Style style = JSON.parseObject(styleStr.toString(), Style.class);
			styleMap.put(style.getStyleId(), style);
		}

		return styleMap;
	}


	public static Map<String, Product> getProductMap() {
		String productStr = null;
		Product product = null;
		Map<Object, Object> productMap = redisUtils.hmget(PREFIX_PRODUCT);
		HashMap<String, Product> map = new HashMap<String, Product>();
		if (CommonUtil.isNotBlank(productMap)) {
			for (Entry<Object, Object> entry : productMap.entrySet()) {
				productStr = (String) entry.getValue();
				product = JSONObject.parseObject(productStr, Product.class);
				map.put(product.getCode(), product);
			}
		}
		return map;
	}

	//-------------------

	public static void iniAccessToken(AccessToken accessToken){
		Map<String,AccessToken> accessTokenMap  = new HashMap<>();
		accessTokenMap.put("AccessToken",accessToken);
		redisUtils.hset(PREFIX_ACCESSTOKEN,"AccessToken", JSON.toJSONString(accessTokenMap));
		/*cache.put(new Element("AccessToken",accessTokenMap));*/
	}

	public static AccessToken getAccessToken(){
		String accessTokenStr = null;
		AccessToken accessToken = null;
		Map<Object,Object> accessTokenMap = redisUtils.hmget(PREFIX_ACCESSTOKEN);
		if (CommonUtil.isNotBlank(accessTokenMap)){
			for (Entry<Object, Object> entry : accessTokenMap.entrySet()){
				accessTokenStr = (String) entry.getValue();
				accessToken = JSONObject.parseObject(accessTokenStr, AccessToken.class);
			}
			return accessToken;
		}else {
			AccessToken token = new AccessToken();
			iniAccessToken(token);
			return token;
		}
	}

	public static void remove(){
		redisUtils.del(PREFIX_ACCESSTOKEN);
		/*cache.remove("AccessToken");*/
	}


	/**
	 * 小程序初始化和获取token
	 */
	public static void initMiniProgramAccessToken(AccessToken accessToken){
		redisUtils.hset(PREFIX_MINIPROGRAMACCESSTOKEN,"AccessToken", JSON.toJSONString(accessToken));
	}

	public static AccessToken getMiniProgramAccessToken(){
		AccessToken accessToken = null;
		String accessTokenStr = redisUtils.hgetString(PREFIX_MINIPROGRAMACCESSTOKEN, "AccessToken");
		if(StringUtils.isNotBlank(accessTokenStr)){
			accessToken = JSON.parseObject(accessTokenStr, AccessToken.class);
		}
		return accessToken;
	}


	public static Map<String, User> getUserMap() {
		User user = null;
		Map<Object, Object> redisMap = redisUtils.hmget(PREFIX_USER);
		Map<String, User> userMap = new HashMap<>();
		for (Object userJSON : redisMap.values()) {
			user = JSON.parseObject(userJSON.toString(), User.class);
			userMap.put(user.getId(), user);
		}
		return userMap;
	}

	public static Map<String, PropertyKey> getPropertyKeyMap() {
		String pStr = null;
		PropertyKey p = null;
		Map<Object, Object> pMap = redisUtils.hmget(PREFIX_PROPERTYKEY);
		HashMap<String, PropertyKey> map = new HashMap<String, PropertyKey>();
		if (CommonUtil.isNotBlank(pMap)) {
			for (Entry<Object, Object> entry : pMap.entrySet()) {
				pStr = (String) entry.getValue();
				p = JSONObject.parseObject(pStr, PropertyKey.class);
				map.put(p.getId(), p);
			}
		}
		return map;
	}

	public static Map<String, PropertyType> getPropertyTypeMap() {
		String pStr = null;
		PropertyType p = null;
		Map<Object, Object> pMap = redisUtils.hmget(PREFIX_PROPERTYTYPE);
		HashMap<String, PropertyType> map = new HashMap<String, PropertyType>();
		if (CommonUtil.isNotBlank(pMap)) {
			for (Entry<Object, Object> entry : pMap.entrySet()) {
				pStr = (String) entry.getValue();
				p = JSONObject.parseObject(pStr, PropertyType.class);
				map.put(p.getKeyId(), p);
			}
		}
		return map;
	}



	public static void initRedisManager() {
		redisUtils = (RedisUtils) SpringContextUtil.getBean("redisUtils");
	}

	// ///////////////////////////////


    /**
     * keyId code-type格式
     * **/
	public static PropertyKey getPlayloungePropertyKey(String keyId){
		PropertyKey p = null;
		String pStr = redisUtils.hgetString(PREFIX_PLAYLOUNGEPROPERTYKEY, keyId);
		if (StringUtils.isNotBlank(pStr)) {
			p = JSONObject.parseObject(pStr, PropertyKey.class);
		}
		return p;
	}
	public static PropertyKey getPropertyKey(String keyId) {
		PropertyKey p = null;
		String pStr = redisUtils.hgetString(PREFIX_PROPERTYKEY, keyId);
		if (StringUtils.isNotBlank(pStr)) {
			p = JSONObject.parseObject(pStr, PropertyKey.class);
		}
		return p;
	}



	public static PropertyType getPropertyType(String keyId) {
		PropertyType p = null;
		String pStr = redisUtils.hgetString(PREFIX_PROPERTYTYPE, keyId);
		if (StringUtils.isNotBlank(pStr)) {
			p = JSONObject.parseObject(pStr, PropertyType.class);
		}
		return p;
	}




	public static User getUserById(String id) {
		User user = null;
		String userStr = redisUtils.hgetString(PREFIX_USER, id);
		if (CommonUtil.isNotBlank(userStr)) {
			user = JSON.parseObject(userStr, User.class);
		}
		return user;
	}

	public static void initCheckWarehouse() throws Exception {
		SettingService settingService = (SettingService)SpringContextUtil.getBean("settingService");
		Setting setting = settingService.get("id","checkWarehouse");
		String checkWarehouse = setting.getValue();
		redisUtils.hset(PREFIX_CHECKWAREHOUSE, "checkWarehouse", JSON.toJSONString(checkWarehouse));

	}

	public static boolean getCheckWarehhouse(){
		String setJSON = redisUtils.hgetString(PREFIX_CHECKWAREHOUSE, "checkWarehouse");
		String setting="";
		if (CommonUtil.isNotBlank(setJSON)) {
			setting = JSON.parseObject(setJSON, String.class);
		}
		if(CommonUtil.isNotBlank(setting)){
            return Boolean.parseBoolean(setting);
		}else{
			return true;
		}

	}



	public static Customer getCustomerById(String id) {
		Customer customer = null;
		String custring = redisUtils.hgetString(PREFIX_CUSTOMER, id);
		if (CommonUtil.isNotBlank(custring)) {
			customer = JSON.parseObject(custring, Customer.class);
		}
		return customer;
	}
	public static PlWmsShopBindingRelation getWmsPlRackBindingRelationByUnitCodeSku(String unitCodeSku) {		/**/
		return null;
	}





    public static Setting getSetting(String code) {
		Setting setting = null;
		String setJSON = redisUtils.hgetString(PREFIX_SYSSITTING, code);
		if (CommonUtil.isNotBlank(setJSON)) {
			setting = JSON.parseObject(setJSON, Setting.class);
		}
		return setting;
    }

	public static Size getSizeById(String sizeId) {
		Size size = null;
		String sizeStr = redisUtils.hgetString(PREFIX_SIZE, sizeId);
		if (StringUtils.isNotBlank(sizeStr)) {
			size = JSONObject.parseObject(sizeStr, Size.class);
		}
		return size;
	}

	public static String getSizeNameById(String sizeId) {
		Size size = null;
		String sizeStr = redisUtils.hgetString(PREFIX_SIZE, sizeId);
		if (StringUtils.isNotBlank(sizeStr)) {
			size = JSONObject.parseObject(sizeStr, Size.class);
		}
		return size == null ? "" : size.getSizeName();
	}



	public static String getColorNameById(String colorId) {
		Color color = null;
		String colorStr = redisUtils.hgetString(PREFIX_COLOR, colorId);
		if (StringUtils.isNotBlank(colorStr)) {
			color = JSONObject.parseObject(colorStr, Color.class);
		}
		return color == null ? "" : color.getColorName();
	}

	public static Color getColorById(String colorId) {
		Color color = null;
		String colorStr = redisUtils.hgetString(PREFIX_COLOR, colorId);
		if (StringUtils.isNotBlank(colorStr)) {
			color = JSONObject.parseObject(colorStr, Color.class);
		}
		return color;
	}

	public static Style getStyleById(String styleId) {
		/*Element result = cache.get("Style");
		Map<String, Style> styleMap = (Map<String, Style>) result.getValue();
		Style s = styleMap.get(styleId);
		if(CommonUtil.isBlank(s)){

			StyleService styleService = (StyleService) SpringContextUtil
					.getBean("styleService");
			s= styleService.get("styleId",styleId);
		}
		*//*StyleService styleService = (StyleService) SpringContextUtil
				.getBean("styleService");
		Style s= styleService.get("styleId",styleId);*//*

		return s;*/
		Style style = null;
		String styleStr = redisUtils.hgetString(PREFIX_STYLE, styleId);
		if (StringUtils.isNotBlank(styleStr)) {
			style = JSONObject.parseObject(styleStr, Style.class);
		}
		return style;
	}

	public static String getStyleNameById(String styleId) {
		Style style = null;
		String styleStr = redisUtils.hgetString(PREFIX_STYLE, styleId);
		if (StringUtils.isNotBlank(styleStr)) {
			style = JSONObject.parseObject(styleStr, Style.class);
		}
		return style == null ? "" : style.getStyleName();
	}

	public static Product getProductByCode(String code) {
		String product = redisUtils.hgetString(PREFIX_PRODUCT, code);
		return JSONObject.parseObject(product, Product.class);
	}


	/**
	 * wing 20140607 将storage移植到unit中type=9
	 * 
	 * @param id
	 * @return
	 */
	@Deprecated
	public static Unit getStorageById(String id) {

		Unit u = getUnitById(id);

		return u;
	}

	public static Device getDeviceByCode(String code) {
		Device device = null;
		String deviceStr = redisUtils.hgetString(PREFIX_DEVICE, code);
		if (CommonUtil.isNotBlank(deviceStr)) {
			device = JSON.parseObject(deviceStr, Device.class);
		}
		return device;
	}

	public static List<Device> getDevicesByUnitId(String unitId) {
		List<Device> devices = new ArrayList<Device>();
		Map<Object, Object> redisMap = redisUtils.hmget(PREFIX_DEVICE);
		for (Object deviceJSON : redisMap.values()) {
			Device device = JSON.parseObject(deviceJSON.toString(), Device.class);
			if (CommonUtil.isNotBlank(unitId) && unitId.equals(device.getStorageId())) {
				devices.add(device);
			}
		}
		return devices;
	}

	public static List<Device> getDevices(String unitId, String deviceType) {
		List<Device> devices = new ArrayList<Device>();
		Map<Object, Object> redisMap = redisUtils.hmget(PREFIX_DEVICE);
		for (Object deviceJSON : redisMap.values()) {
			Device device = JSON.parseObject(deviceJSON.toString(), Device.class);
			if (device.getStorageId().trim().equals(unitId.trim()) && device.getCode().startsWith(deviceType)) {
				devices.add(device);
			}
		}
		return devices;
	}



	private static String setShopLngAndLat() throws Exception {
		UnitService unitService = (UnitService) SpringContextUtil
				.getBean("unitService");
		List<Unit> list = unitService.findShopAddrNotNull();
		for (Unit u : list) {
			if (CommonUtil.isBlank(u.getLatitude())
					|| CommonUtil.isBlank(u.getLongitude())) {
				String addr = u.getAddress().replace(" ", "");
				String url = "http://api.map.baidu.com/geocoder?address="
						+ addr + "&output=json&ak=rnm8udmHdWaHFWZTO2tuTiG8";
				String json = loadJson(url);
				try {
					JSONObject obj = JSONObject.parseObject(json);
					if (obj != null
							&& CommonUtil.isNotBlank(obj.get("result")
							.toString())) {
						double lng = obj.getJSONObject("result")
								.getJSONObject("location").getDouble("lng");
						double lat = obj.getJSONObject("result")
								.getJSONObject("location").getDouble("lat");
						u.setLatitude(lat);
						u.setLongitude(lng);
					} else {
						// System.out.println("未找到相匹配的经纬度！");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
		unitService.saveList(list);
		return null;

	}

	private static String loadJson(String url) {
		StringBuilder json = new StringBuilder();
		try {
			URL urlObject = new URL(url);
			URLConnection uc = urlObject.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					uc.getInputStream()));
			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				json.append(inputLine);
			}
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	public static Unit getUnitById(String id) {
		Unit unit = null;
		String unitJSON = redisUtils.hgetString(PREFIX_UNIT, id);
		if (CommonUtil.isNotBlank(unitJSON)) {
			unit = JSON.parseObject(unitJSON, Unit.class);
		}
		return unit;
	}

	public static Unit getUnitByCode(String code) {
		UnitService unitService = (UnitService) SpringContextUtil
				.getBean("unitService");
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		PropertyFilter filter = new PropertyFilter("EQS_code", code);
		filters.add(filter);
		List<Unit> list = unitService.find(filters);
		if (list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);
	}



	public static List<RoleRes> getAuthByRoleId(String roleId) {
		List<RoleRes> roleResList = null;
		Object resList = redisUtils.hget(PREFIX_ROLERES, roleId);
		if (CommonUtil.isNotBlank(resList)) {
			roleResList = JSON.parseArray(resList.toString(), RoleRes.class);
		}
		return roleResList;
	}


	public static Resource getResourceById(String code) {
		Resource resource = null;
		String res = redisUtils.hgetString(PREFIX_RESOURCE, code);
		if (CommonUtil.isNotBlank(res)) {
			resource = JSON.parseObject(res, Resource.class);
		}
		return resource;
	}


	public static String getRoleNameById(String id) {
		String roleName = "";
		Role role = null;
		String rstring = redisUtils.hgetString(PREFIX_ROLE, id);
		if (CommonUtil.isNotBlank(rstring)) {
			role = JSON.parseObject(rstring, Role.class);
			roleName = role.getName();
		}
		return roleName;
	}

	public static List<SizeSort> sizeSortList = null;


	public static List<SizeSort> getAllSizeSort() {
		List<Object> list = redisUtils.lGet(PREFIX_SIZESORT, 0, -1);
		ArrayList<SizeSort> arrayList = new ArrayList<SizeSort>();
		for (Object object : list) {
			String ss = (String) object;
			SizeSort parseObject = JSONObject.parseObject(ss, SizeSort.class);
			arrayList.add(parseObject);
		}
		return arrayList;
	}

	public static SizeSort getSizeSortById(String sizeSortId) {
		SizeSort sizeSort = null;
		List<Object> list = redisUtils.lGet(PREFIX_SIZESORT, 0, -1);
		for (Object object : list) {
			String ss = (String) object;
			sizeSort = JSONObject.parseObject(ss, SizeSort.class);
			if (sizeSort.getId().equals(sizeSortId)) {
				break;
			}
		}
		return sizeSort;
	}

	public static int getSizeMaxSeqNo() {
		return sizeMaxSeqNo;
	}

	public static Size getSizeByNo(String sizeNo) {
		String sizeStr = null;
		Size size = null;
		Map<Object, Object> sizeMap = redisUtils.hmget(PREFIX_SIZE);
		if (CommonUtil.isNotBlank(sizeMap)) {
			for (Entry<Object, Object> entry : sizeMap.entrySet()) {
				sizeStr = (String) entry.getValue();
				size = JSONObject.parseObject(sizeStr, Size.class);
				if (size.getSizeId().equalsIgnoreCase(sizeNo)) {
					return size;
				}
			}
		}
		return null;
	}

	public static boolean isHaveStyleNo(String styleNo) {
		String styleStr = redisUtils.hgetString(PREFIX_STYLE, styleNo);
		return styleStr == null ? false : true;
	}

	public static boolean isHaveColorNo(String colorNo) {
		String colorStr = redisUtils.hgetString(PREFIX_COLOR, colorNo);
		return colorStr == null ? false : true;

	}

	public static boolean isHaveSizeNo(String sizeNo) {
		String sizeStr = redisUtils.hgetString(PREFIX_SIZE, sizeNo);
		return sizeStr == null ? false : true;

	}

	public static void setMaxProductId(int index) {
		maxProductId = index;
	}

	public static int getMaxProductId() {
		ProductService productService = (ProductService) SpringContextUtil
				.getBean("productService");
		String maxProductId = productService.getMaxProductId();
		Integer maxId=Integer.parseInt(maxProductId)+1;
		return maxId;
	}

	public static Long getproductMaxVersionId(){
		Long maxVersionId = Long.parseLong(redisUtils.hget("maxVersionId","productMaxVersionId").toString());
		return maxVersionId;
	}
	public static Long getStyleMaxVersionId(){
		Long maxVersionId = Long.parseLong(redisUtils.hget("maxVersionId","styleMaxVersionId").toString());
		return maxVersionId;
	}


	public static Long getMaxEpcstockVersionId() {
		Long maxVersionId = Long.parseLong(redisUtils.hget("maxVersionId","EpcStockMaxVersionId").toString());
		return maxVersionId;
	}
	public static void setEpcstockVersionId(Long version){
		redisUtils.hset("maxVersionId","EpcStockMaxVersionId",JSON.toJSONString(version));
	}
}
