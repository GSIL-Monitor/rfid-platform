package com.casesoft.dmc.cache;

import com.alibaba.fastjson.JSONObject;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.extend.third.model.pl.PlWmsShopBindingRelation;
import com.casesoft.dmc.extend.third.service.pl.PlWmsShopBindingRelationService;
import com.casesoft.dmc.model.cfg.Device;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.cfg.PropertyType;
import com.casesoft.dmc.model.factory.FactoryCategory;
import com.casesoft.dmc.model.factory.FactoryWorkTime;
import com.casesoft.dmc.model.factory.Token;
import com.casesoft.dmc.model.factory.WorkCalendar;
import com.casesoft.dmc.model.hall.Department;
import com.casesoft.dmc.model.hall.Employee;
import com.casesoft.dmc.model.hall.HallFloor;
import com.casesoft.dmc.model.mirror.Collocat;
import com.casesoft.dmc.model.mirror.NewProduct;
import com.casesoft.dmc.model.pad.AccessToken;
import com.casesoft.dmc.model.product.*;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.sys.*;
import com.casesoft.dmc.service.cfg.DeviceService;
import com.casesoft.dmc.service.cfg.PropertyKeyService;
import com.casesoft.dmc.service.factory.FactoryTokenService;
import com.casesoft.dmc.service.factory.FactoryWorkTimeService;
import com.casesoft.dmc.service.factory.WorkCalendarService;
import com.casesoft.dmc.service.hall.DepartmentService;
import com.casesoft.dmc.service.hall.EmployeeService;
import com.casesoft.dmc.service.hall.HallFloorService;
import com.casesoft.dmc.service.mirror.CollocatService;
import com.casesoft.dmc.service.mirror.NewProductService;
import com.casesoft.dmc.service.product.ColorService;
import com.casesoft.dmc.service.product.ProductService;
import com.casesoft.dmc.service.product.StyleService;
import com.casesoft.dmc.service.shop.CustomerService;
import com.casesoft.dmc.service.sys.SettingService;
import com.casesoft.dmc.service.sys.impl.ResourceService;
import com.casesoft.dmc.service.sys.impl.RoleService;
import com.casesoft.dmc.service.sys.impl.UnitService;
import com.casesoft.dmc.service.sys.impl.UserService;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

@SuppressWarnings("unchecked")
public class CacheManager {

	private static net.sf.ehcache.CacheManager cacheManager;
	private static Cache cache;
	private static int sizeMaxSeqNo;
	private static int maxProductId;
	public static Unit company = null;
//add170119

	public static List<PropertyKey> getCodeDetailListByType(String typeCode) {
		Element result = cache.get("propertyKey");
		Map<String, PropertyKey> codeMap = (Map<String, PropertyKey>) result
				.getValue();
		List<PropertyKey> detailList = new ArrayList<>();
		for (PropertyKey detail : codeMap.values()) {
			if (detail.getType().equals(typeCode)) {
				detailList.add(detail);
			}
		}
		return detailList;
	}

	public static Map<String, String> getRoleMap() {
		Element result = cache.get("Role");
		Map<String, String> roleMap = (Map<String, String>) result.getValue();
		return roleMap;
	}

	public static Map<String, Resource> getResourceMap() {
		Element result = cache.get("Resource");
		Map<String, Resource> resourceMap = (Map<String, Resource>) result
				.getValue();
		return resourceMap;
	}

	public static Map<String, List<RoleRes>> getAuthMap() {
		Element element = cache.get("AuthMap");
		Map<String, List<RoleRes>> authMap = (Map<String, List<RoleRes>>) element
				.getValue();
		return authMap;
	}

	public static Map<String, Unit> getUnitMap() {
		Element result = cache.get("UnitMap");
		Map<String, Unit> unitMap = (Map<String, Unit>) result.getValue();
		return unitMap;
	}

	public static Map<String, Device> getDeviceMap() {
		Element result = cache.get("DeviceMap");
		Map<String, Device> deviceMap = (Map<String, Device>) result.getValue();
		return deviceMap;
	}


	public static Map<String, Color> getColorMap() {
		Element result = cache.get("Color");
		Map<String, Color> colorMap = (Map<String, Color>) result.getValue();
		return colorMap;
	}

	public static Map<String, Size> getSizeMap() {
		Element result = cache.get("Size");
		Map<String, Size> sizeMap = (Map<String, Size>) result.getValue();
		return sizeMap;
	}

	public static Map<String, Style> getStyleMap() {
		Element result = cache.get("Style");
		Map<String, Style> styleMap = (Map<String, Style>) result.getValue();
		return styleMap;
	}


	public static Map<String, String> getProvinceMap() {
		Element result = cache.get("Province");
		Map<String, String> provinceMap = (Map<String, String>) result
				.getValue();
		return provinceMap;
	}

	public static Map<String, Product> getProductMap() {
		Element result = cache.get("Product");
		Map<String, Product> Product = (Map<String, Product>) result.getValue();
		return Product;
	}

	//-------------------

	public static void iniAccessToken(AccessToken accessToken){
		Map<String,AccessToken> accessTokenMap  = new HashMap<>();
		accessTokenMap.put("AccessToken",accessToken);
		cache.put(new Element("AccessToken",accessTokenMap));
	}

	public static AccessToken getAccessToken(){
		Element result = cache.get("AccessToken");
		if (result==null){
			AccessToken accessToken = new AccessToken();
			iniAccessToken(accessToken);
			Element element = cache.get("AccessToken");
			Map<String,AccessToken> accessTokenMap = (Map<String, AccessToken>) element.getValue();
			return accessTokenMap.get("AccessToken");
		}else {
			Map<String,AccessToken> accessTokenMap = (Map<String, AccessToken>) result.getValue();
			return accessTokenMap.get("AccessToken");
		}
	}

	public static void remove(){
		cache.remove("AccessToken");
	}


	public static void iniDepartment(){
		DepartmentService departmentService =(DepartmentService) SpringContextUtil.getBean("departmentService");
		Map<String,Department> departmenyMap =new HashMap<String,Department>();
		List<Department> departmentList =departmentService.getAll();
		for(Department d:departmentList){
			departmenyMap.put(d.getCode(),d);
		}
		cache.put(new Element("Department",departmenyMap));
	}


	public static Department getDepartmentByCode(String code){
		Element result =cache.get("Department");
		Map<String,Department> departmentMap =(Map<String,Department>)result.getValue();
		return departmentMap.get(code);
	}

	public  static  void refreshDepartment(){
		cache.remove("Department");
		iniDepartment();
	}


	public static void iniHallFloor(){
		HallFloorService hallFloorService=(HallFloorService)SpringContextUtil.getBean("hallFloorService");
		Map<String,HallFloor> hallFloorMap=new HashMap<String,HallFloor>();
		List<HallFloor> hallFloorList=hallFloorService.getAll();
		for(HallFloor hf:hallFloorList){
			hallFloorMap.put(hf.getCode(),hf);
		}
		cache.put(new Element("HallFloor",hallFloorMap));
	}

	public static HallFloor getFloorByCode(String id){
		Element result =cache.get("HallFloor");
		Map<String,HallFloor> hallFloorMap = (Map<String,HallFloor>)result.getValue();
		return hallFloorMap.get(id);
	}

	public static List getFloorListByOwnerId(String ownerId){
		Element result =cache.get("HallFloor");
		Map floorMap=(Map)result.getValue();
		ArrayList hallFloors =new ArrayList<>();
		Iterator var =floorMap.values().iterator();


		while (var.hasNext()){
			HallFloor hf=(HallFloor)var.next();
			if(hf.getOwnerId().equals(ownerId)){
				hallFloors.add(hf);
			}
		}
		return hallFloors;
	}
	public static void refreshHallFloor(){
		cache.remove("HallFloor");
		iniHallFloor();
	}

	public static void iniEmployee(){
		EmployeeService employeeService=(EmployeeService)SpringContextUtil.getBean("employeeService");
		Map<String,Employee> employeeMap =new HashMap<String,Employee>();
		List<Employee> employeeList=employeeService.getAll();
		for(Employee emp:employeeList){
			employeeMap.put(emp.getId(),emp);
		}
		cache.put(new Element("Employee",employeeMap));
	}
	public static Employee getEmployeeById(String id){
		Element result =cache.get("Employee");
		Map<String,Employee> employeeMap=(Map<String,Employee>)result.getValue();
		return employeeMap.get(id);
	}
	public static void refreshEmployee(){
		cache.remove("Employee");
		iniEmployee();
	}
	//-------------------

	public static Map<String, String> getCityMap() {
		Element result = cache.get("City");
		Map<String, String> cityMap = (Map<String, String>) result.getValue();
		return cityMap;
	}

	public static Map<String, Long> getStockTypeMap() {
		Element result = cache.get("StorageType");
		Map<String, Long> stockTypeMap = (Map<String, Long>) result.getValue();
		return stockTypeMap;
	}

	public static Map<String, User> getUserMap() {
		Element result = cache.get("User");
		Map<String, User> userMap = (Map<String, User>) result.getValue();
		return userMap;
	}

	public static Map<String, PropertyKey> getPropertyKeyMap() {
		Element result = cache.get("propertyKey");
		Map<String, PropertyKey> propertyKeyMap = (HashMap<String, PropertyKey>) result
				.getValue();
		return propertyKeyMap;
	}

	public static Map<String, PropertyType> getPropertyTypeMap() {
		Element result = cache.get("propertyType");
		Map<String, PropertyType> propertyTypeMap = (HashMap<String, PropertyType>) result
				.getValue();
		return propertyTypeMap;
	}



	public static void initCacheManager() {
		cacheManager = (net.sf.ehcache.CacheManager) SpringContextUtil
				.getBean("cacheManager");
		cache = cacheManager.getCache("Cache");
	}

	// ///////////////////////////////
	public static void initPropertyCache() {
		PropertyKeyService propertyKeyService = (PropertyKeyService) SpringContextUtil
				.getBean("propertyKeyService");
		Map<String, PropertyKey> propertyKeyMap = new HashMap<String, PropertyKey>();
		Map<String, PropertyKey> propertyKeyMap2 = new HashMap<String, PropertyKey>();
		List<PropertyKey> keyList = propertyKeyService.getAll();
		for (PropertyKey key : keyList) {
			propertyKeyMap.put(key.getId(), key);
			propertyKeyMap2.put(key.getCode()+"-"+key.getType(), key);
		}
		cache.put(new Element("propertyKey", propertyKeyMap));
		cache.put(new Element("playloungePropertyKey", propertyKeyMap2));

	}

	public static void refreshPropertyCache() {
		cache.remove("propertyKey");
		initPropertyCache();
	}
    /**
     * keyId code-type格式
     * **/
	public static PropertyKey getPlayloungePropertyKey(String keyId){
		Element result = cache.get("playloungePropertyKey");
		Map<String, PropertyKey> propertyKeyMap = (HashMap<String, PropertyKey>) result
				.getValue();
		return propertyKeyMap.get(keyId); 
	}
	public static PropertyKey getPropertyKey(String keyId) {
		Element result = cache.get("propertyKey");
		Map<String, PropertyKey> propertyKeyMap = (HashMap<String, PropertyKey>) result
				.getValue();
		PropertyKey propertyKey = propertyKeyMap.get(keyId);
		if(CommonUtil.isBlank(propertyKey)){
			PropertyKeyService propertyKeyService = (PropertyKeyService) SpringContextUtil
					.getBean("propertyKeyService");
			propertyKey = propertyKeyService.get("id",keyId);
		}
		return propertyKey;
	}

	public static void initPropertyTypeCache() {
		PropertyKeyService propertyKeyService = (PropertyKeyService) SpringContextUtil
				.getBean("propertyKeyService");
		Map<String, PropertyType> propertyTypeMap = new HashMap<String, PropertyType>();
		List<PropertyType> typeList = propertyKeyService.findAll();
		for (PropertyType type : typeList) {
			propertyTypeMap.put(type.getKeyId(), type);
		}
		cache.put(new Element("propertyType", propertyTypeMap));
	}

	public static PropertyType getPropertyType(String keyId) {
		Element result = cache.get("propertyType");
		Map<String, PropertyType> propertyTypeMap = (HashMap<String, PropertyType>) result
				.getValue();
		return propertyTypeMap.get(keyId);
	}

	public static void refreshPropertyTypeCache() {
		cache.remove("propertyType");
		initPropertyTypeCache();
	}

	public static void initUserCache() {
		UserService userService = (UserService) SpringContextUtil
				.getBean("userService");
		Map<String, User> userMap = new HashMap<String, User>();
		List<User> userList = userService.getAllUser();
		for (User u : userList) {
			userMap.put(u.getId(), u);
		}
		cache.put(new Element("User", userMap));
		//

	}

	public static User getUserById(String id) {
		Element result = cache.get("User");
		Map<String, User> userMap = (Map<String, User>) result.getValue();
		return userMap.get(id);
	}

	public static void refreshUserCache() {
		cache.remove("User");

		initUserCache();
	}

	public static void initCache() throws Exception {
		initCacheManager();
		initUserCache();
		initRoleCache();
		initResourceCache();
		initAuthCache();
		initUnitMap();
		initDeviceMap();
		iniEmployee();
//		iniHallRoom();
		iniHallFloor();
		initProductCache();
		initColorCache();
		initSizeCache();
		initStyleCache();
		initNewProductCache();
		initPropertyCache();
		initPropertyTypeCache();
		initSizeSortCache();
		iniDepartment();
		initCollocatCache();
		/*
		 * 库位
		 * */
		
		//initFloorAreaCache();
		//initFloorCache();
		//initFloorRackCache();

        initSysSetting();
        initCustomerCache();
		initWmsPlRackBindingRelation();

		initFactoryToken();//成衣流程
        initWorkCalendar();
        initFactoryWorkTime();
		initFactoryCategory();
		initCheckWarehouse();
	}

	public static void initCheckWarehouse() throws Exception {
		boolean checkWarehouse = Boolean.parseBoolean(PropertyUtil
				.getValue("checkWarehouse"));
		cache.put(new Element("checkWarehouse", checkWarehouse));
	}

	public static boolean getCheckWarehhouse(){
		Element result = cache.get("checkWarehouse");
		return Boolean.parseBoolean(result.getValue().toString());
	}

	public static void initFactoryCategory() {

		FactoryTokenService factoryTokenService = (FactoryTokenService) SpringContextUtil
				.getBean("factoryTokenService");
		List<FactoryCategory> list = factoryTokenService.findAllCategory();
		Map<String,FactoryCategory> map = new HashMap<String, FactoryCategory>();
		Map<String,FactoryCategory> map2 = new HashMap<String, FactoryCategory>();

		for(FactoryCategory d : list){
			map.put(d.getName(),d);
			map2.put(d.getCode(), d);
		}
		cache.put(new Element("category", map));
		cache.put(new Element("category2", map2));

	}
	public static void refreshFactoryCategory(){
		cache.remove("category");
		cache.remove("category2");
		initFactoryCategory();
	}

	public static FactoryCategory getFactoryCategoryByName(String name){
		Element result = cache.get("category");
		Map<String, FactoryCategory> map = (HashMap<String, FactoryCategory>) result.getValue();
		return map.get(name);
	}

	public static FactoryCategory getFactoryCategoryByCode(String code){
		Element result = cache.get("category2");
		Map<String, FactoryCategory> map = (HashMap<String, FactoryCategory>) result.getValue();
		return map.get(code);
	}

	public static Map<String,FactoryCategory> getFactoryCategory(){
		Element result = cache.get("category");
		Map<String, FactoryCategory> map = (HashMap<String, FactoryCategory>) result.getValue();
		return map;
	}

	public static boolean isHaveFactoryCategory(String name){
		Element result = cache.get("category");
		Map<String, FactoryCategory> map = (HashMap<String, FactoryCategory>) result.getValue();
		return map.containsKey(name);
	}

    private static void initFactoryWorkTime() {
        FactoryWorkTimeService factoryWorkTimeService = (FactoryWorkTimeService) SpringContextUtil
                .getBean("factoryWorkTimeService");
        List<FactoryWorkTime> list = factoryWorkTimeService.getAll();
        Map<String , FactoryWorkTime> map = new HashMap<String , FactoryWorkTime>();
        for(FactoryWorkTime workTime : list){
            map.put(workTime.getCode()+workTime.getToken(),workTime);
        }
        cache.put(new Element("factoryWorkTime",map));

    }

    public static void refreshFactoryWorkTime(){
        cache.remove("factoryWorkTime");
        initFactoryWorkTime();
    }

    public static FactoryWorkTime getFactoryWorkTime(String code , Integer token){
        Element result = cache.get("factoryWorkTime");
        Map<String, FactoryWorkTime> map = (HashMap<String, FactoryWorkTime>) result.getValue();
        return map.get(code+token);
    }

    public static void initWorkCalendar(){
        WorkCalendarService workCalendarService = (WorkCalendarService) SpringContextUtil
                .getBean("workCalendarService");
        List<WorkCalendar> workCalendarList = workCalendarService.getAll();
        Map<String,WorkCalendar> calendarMap = new HashMap<String,WorkCalendar>();
        for(WorkCalendar workCalendar : workCalendarList){
            calendarMap.put(workCalendar.getDay(),workCalendar);
        }
        cache.put(new Element("workCalendar",calendarMap));
    }

    public static void refreshWorkCalendar(){
        cache.remove("workCalendar");
        initWorkCalendar();
    }

    public static WorkCalendar getWorkCalendarByDay(String day){
        Element result = cache.get("workCalendar");
        Map<String, WorkCalendar> map = (HashMap<String, WorkCalendar>) result.getValue();
        return map.get(day);
    }

    public static void initFactoryToken() {
		FactoryTokenService factoryTokenService = (FactoryTokenService) SpringContextUtil
				.getBean("factoryTokenService");
		List<Token> list = factoryTokenService.getAll();
		Map<Integer,Token> map = new HashMap<Integer, Token>();
		for(Token t:list){
			map.put(t.getToken(), t);
		}
		cache.put(new Element("token", map));

	}
	public static Token getFactoryTokenByToken(Integer token){
		Element result = cache.get("token");
		Map<Integer, Token> map = (HashMap<Integer, Token>) result
				.getValue();
		return map.get(token);
	}
	public static void refreshFactoryToken(){
		cache.remove("token");
		initFactoryToken();
	}

	public static void initWmsPlRackBindingRelation() {
		PlWmsShopBindingRelationService wmsFloorViewService = (PlWmsShopBindingRelationService) SpringContextUtil
				.getBean("plWmsShopBindingRelationService");
		List<PlWmsShopBindingRelation> sList = wmsFloorViewService.findWmsPlShopRelations();
		Map<String, PlWmsShopBindingRelation> wmsPlRackBindingRelationMap = new HashMap<String, PlWmsShopBindingRelation>();
		if(CommonUtil.isNotBlank(sList)){
			for (PlWmsShopBindingRelation s : sList) {
				wmsPlRackBindingRelationMap.put(s.getUnitCode()+s.getStyleId()+s.getColorId(), s);
			}
		}
		cache.put(new Element("wmsPlRackBindingRelation", wmsPlRackBindingRelationMap));
	}
	public static void refreshWmsPlRackBindingRelationCache() {
		cache.remove("wmsPlRackBindingRelation");
		initWmsPlRackBindingRelation();
	}
	public static Customer getCustomerById(String id) {
		Element result = cache.get("customer");
		Map<String, Customer> customerMap = (Map<String, Customer>) result
				.getValue();
		return customerMap.get(id);
	}
	public static PlWmsShopBindingRelation getWmsPlRackBindingRelationByUnitCodeSku(String unitCodeSku) {
		Element result = cache.get("wmsPlRackBindingRelation");
		if(CommonUtil.isNotBlank(result)){
			Map<String, PlWmsShopBindingRelation> customerMap = (Map<String, PlWmsShopBindingRelation>) result
					.getValue();
			return customerMap.get(unitCodeSku);
		}else{
			return null;
		}
	}
	public static void initCustomerCache() {
		CustomerService customerService = (CustomerService) SpringContextUtil
				.getBean("customerService");
		List<Customer> sList = customerService.getAll();
		Map<String, Customer> settingMap = new HashMap<String, Customer>();
		for (Customer s : sList) {
			if(s.getStatus() ==1){
				settingMap.put(s.getId(), s);
			}
		}
		cache.put(new Element("customer", settingMap));
	}
	public static void refreshCustomer() {
		cache.remove("customer");
		initCustomerCache();
	}

    public static void refreshSysSetting() {
        cache.remove("sysSetting");
        initSysSetting();
    }
	public static void initSysSetting() {
        SettingService settingService = (SettingService) SpringContextUtil
                .getBean("settingService");
        List<Setting> sList = settingService.getAll();
        Map<String, Setting> settingMap = new HashMap<String, Setting>();
        for (Setting s : sList) {
            settingMap.put(s.getId(), s);
        }
        cache.put(new Element("sysSetting", settingMap));
    }

    public static Setting getSetting(String code) {
        Element result = cache.get("sysSetting");
        Map<String, Setting> settingMap = (Map<String, Setting>) result
                .getValue();
        return settingMap.get(code);
    }



	public static String getCityById(String id) {

		Element result = cache.get("City");
		Map<String, String> cityMap = (Map<String, String>) result.getValue();
		return cityMap.get(id);
	}

	public static String getProvinceById(String id) {
		Element result = cache.get("Province");
		Map<String, String> provinceMap = (Map<String, String>) result
				.getValue();
		return provinceMap.get(id);
	}

	private static void initSizeCache() {
		StyleService styleService = (StyleService) SpringContextUtil
				.getBean("styleService");
		List<Size> sList = styleService.findAll2();
		Map<String, Size> sizeMap = new HashMap<String, Size>();
		for (Size s : sList) {
			sizeMap.put(s.getId(), s);
		}
		cache.put(new Element("Size", sizeMap));

	}

	public static void refreshSizeCache() {
		cache.remove("Size");

		initSizeCache();
	}

	public static Size getSizeById(String sizeId) {
		Element result = cache.get("Size");
		Map<String, Size> sizeMap = (Map<String, Size>) result.getValue();
		return sizeMap.get(sizeId);
	}

	public static String getSizeNameById(String sizeId) {
		Element result = cache.get("Size");
		Map<String, Size> sizeMap = (Map<String, Size>) result.getValue();
		Size s = sizeMap.get(sizeId);
		return s == null ? "" : s.getSizeName();
	}

	private static void initColorCache() {
		StyleService styleService = (StyleService) SpringContextUtil
				.getBean("styleService");
		List<Color> cList = styleService.findAll();
		Map<String, Color> colorMap = new HashMap<String, Color>();
		for (Color c : cList) {
			colorMap.put(c.getId(), c);
		}
		cache.put(new Element("Color", colorMap));

	}

	public static void refreshColorCache() {

		cache.remove("Color");

		initColorCache();
	}

	public static String getColorNameById(String colorId) {
		Element result = cache.get("Color");
		Map<String, Color> colorMap = (Map<String, Color>) result.getValue();
		Color c = colorMap.get(colorId);
		if (CommonUtil.isBlank(c)){
			ColorService colorService = (ColorService) SpringContextUtil.getBean("colorService");
			c = colorService.get("id",colorId);
		}
		return c.getColorName();
	}

	public static Color getColorById(String colorId) {

		Element result = cache.get("Color");
		Map<String, Color> colorMap = (Map<String, Color>) result.getValue();
		Color c = colorMap.get(colorId);
		return c;
	}

	private static void initStyleCache() {
		StyleService styleService = (StyleService) SpringContextUtil
				.getBean("styleService");
		List<Style> styleList = styleService.getAll();
		Map<String, Style> styleMap = new HashMap<String, Style>();
		for (Style s : styleList) {
			styleMap.put(s.getId(), s);
		}
		cache.put(new Element("Style", styleMap));

	}

	public static void refreshStyleCache() {
		cache.remove("Style");

		initStyleCache();
	}
	public static void refreshCollocatCache(){
		cache.remove("Collocat");
	}

	public static Collocat getCollocatById(String id){
		Map<String,Collocat> collocatMap = getCollocatMap();
		return collocatMap.get(id);
	}
	public static Map<String,Collocat> getCollocatMap(){
		Element result =cache.get("Collocat");
		Map<String,Collocat> collocatMap =(HashMap<String,Collocat>) result.getValue();
		return collocatMap;
	}
	
	public static void initCollocatCache(){
		CollocatService collocatService=(CollocatService) SpringContextUtil.getBean("collocatService");
		List<Collocat> list=collocatService.getAll();
		Map<String,Collocat> collocatMap=new HashMap<String, Collocat>();
		for(Collocat c:list){
			collocatMap.put(c.getId(), c);
		}
		cache.put(new Element("Collocat",collocatMap));
		
	}
	public static Style getStyleById(String styleId) {
		Element result = cache.get("Style");
		Map<String, Style> styleMap = (Map<String, Style>) result.getValue();
		Style s = styleMap.get(styleId);
		if(CommonUtil.isBlank(s)){

			StyleService styleService = (StyleService) SpringContextUtil
					.getBean("styleService");
			s= styleService.get("styleId",styleId);
		}
		/*StyleService styleService = (StyleService) SpringContextUtil
				.getBean("styleService");
		Style s= styleService.get("styleId",styleId);*/

		return s;
	}

	public static String getStyleNameById(String styleId) {
		Element result = cache.get("Style");
		Map<String, Style> styleMap = (Map<String, Style>) result.getValue();
		Style style = styleMap.get(styleId);
		if(CommonUtil.isBlank(style)){
			StyleService styleService = (StyleService) SpringContextUtil
					.getBean("styleService");
			style= styleService.get("styleId",styleId);
		}
		return style == null ? "" : style.getStyleName();
	}
	public static void initNewProductCache(){
		NewProductService newProductService = (NewProductService) SpringContextUtil
				.getBean("newProductService");
		List<NewProduct> list = newProductService.getAll();
		Map<String , NewProduct> newProductMap = new HashMap<String, NewProduct>();
		for(NewProduct p : list){
			newProductMap.put(p.getStyleId(),p);
		}
		cache.put(new Element("newProduct", newProductMap));
	}
	
	public static NewProduct getNewProductByStyleId(String styleId){
		Element result = cache.get("newProduct");
		Map<String, NewProduct> newProductMap = (Map<String, NewProduct>) result
				.getValue();
		return newProductMap.get(styleId);
	}
	
	public static void refreshNewProductCache(){
		cache.remove("newProduct");
		initNewProductCache();
	}
	public static Map<String, NewProduct> getNewProductMap() {
		Element result = cache.get("newProduct");
		Map<String, NewProduct> newProductMap = (Map<String, NewProduct>) result
				.getValue();
		return newProductMap;
	}



	public static void initProductCache() {
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
			if(Integer.parseInt(list.get(list.size()-1).getId())-list.size()!=1){
				maxProductId =Integer.parseInt(list.get(list.size()-1).getId())+1;
			}else{
				maxProductId=list.size();
			}
		}
		Map<String, Product> productMap = new HashMap<String, Product>();
		for (Product p : list) {
			productMap.put(p.getCode(), p);
		}
		cache.put(new Element("Product", productMap));

	}

	public static Product getProductByCode(String code) {
		Element result = cache.get("Product");
		Map<String, Product> productMap = (Map<String, Product>) result
				.getValue();
		Product p = productMap.get(code);
		if(CommonUtil.isBlank(p)){
			ProductService productService = (ProductService) SpringContextUtil
					.getBean("productService");
			p = productService.findProductByCode(code);
		}
		return p;
	}

	public static void refreshProductCache() {
		cache.remove("Product");

		initProductCache();
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


	private static void initDeviceMap() {
		DeviceService deviceService = (DeviceService) SpringContextUtil
				.getBean("deviceService");
		List<Device> deviceList = deviceService.getAll();
		Map<String, Device> deviceMap = new HashMap<String, Device>();
		for (Device device : deviceList) {
			deviceMap.put(device.getCode(), device);
		}
		cache.put(new Element("DeviceMap", deviceMap));

	}

	public static void refreshDeviceCache() {
		cache.remove("DeviceMap");

		initDeviceMap();
	}

	public static Device getDeviceByCode(String code) {
		Element result = cache.get("DeviceMap");
		Map<String, Device> deviceMap = (Map<String, Device>) result.getValue();

		return deviceMap.get(code);
	}

	public static List<Device> getDevicesByUnitId(String unitId) {
		List<Device> devices = new ArrayList<Device>();
		Element result = cache.get("DeviceMap");
		Map<String, Device> deviceMap = (Map<String, Device>) result.getValue();
		for (Device device : deviceMap.values()) {
			if (device.getStorageId().trim().equals(unitId.trim()))
				devices.add(device);
		}
		return devices;
	}

	public static List<Device> getDevices(String unitId, String deviceType) {
		List<Device> devices = new ArrayList<Device>();
		Element result = cache.get("DeviceMap");
		Map<String, Device> deviceMap = (Map<String, Device>) result.getValue();
		for (Device device : deviceMap.values()) {
			if (device.getStorageId().trim().equals(unitId.trim())
					&& device.getCode().startsWith(deviceType))
				devices.add(device);
		}
		return devices;
	}

	private static void initUnitMap() throws Exception {
		UnitService unitService = (UnitService) SpringContextUtil
				.getBean("unitService");
		//setShopLngAndLat();
		List<Unit> unitList = unitService.getAll();
		Map<String, Unit> unitMap = new HashMap<String, Unit>();
		for (Unit unit : unitList) {
			if (unit.getType() == 1) {
				company = unit;
			}
			if(unit.getStatus()== 1){
				unitMap.put(unit.getId(), unit);
			}
		}
		cache.put(new Element("UnitMap", unitMap));

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

	public static void refreshUnitCache() throws Exception {
		cache.remove("UnitMap");

		initUnitMap();
	}

	public static Unit getUnitById(String id) {
		Element result = cache.get("UnitMap");
		Map<String, Unit> unitMap = (Map<String, Unit>) result.getValue();

		return unitMap.get(id);
	}

	public static Unit getUnitByCode(String code) {
		Element result = cache.get("UnitMap");
		Map<String, Unit> unitMap = (Map<String, Unit>) result.getValue();
		return unitMap.get(code);
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
		cache.put(new Element("AuthMap", authMap));

	}

	public static void refreshAuthCache() {
		cache.remove("AuthMap");

		initAuthCache();
	}

	public static List<RoleRes> getAuthByRoleId(String roleId) {
		Element element = cache.get("AuthMap");
		Map<String, List<RoleRes>> authMap = (Map<String, List<RoleRes>>) element
				.getValue();
		return authMap.get(roleId);
		// return authMap.get(roleId);
	}

	private static void initResourceCache() {
		ResourceService resourceService = (ResourceService) SpringContextUtil
				.getBean("resourceService");
		List<Resource> resList = resourceService.getResourceList();
		Map<String, Resource> resourceMap = new HashMap<String, Resource>();
		for (Resource r : resList) {
			resourceMap.put(r.getCode(), r);
		}
		cache.put(new Element("Resource", resourceMap));

	}

	public static void refreshResourceCache() {
		cache.remove("Resource");

		initResourceCache();
	}

	public static Resource getResourceById(String code) {
		// return resourceMap.get(code);
		Element result = cache.get("Resource");
		Map<String, Resource> resourceMap = (Map<String, Resource>) result
				.getValue();
		return resourceMap.get(code);
	}

	private static void initRoleCache() {
		RoleService roleService = (RoleService) SpringContextUtil
				.getBean("roleService");
		List<Role> roleList = roleService.getAllRoles();
		Map<String, String> roleMap = new HashMap<String, String>();
		for (Role r : roleList) {
			String id = r.getId();
			roleMap.put(id, r.getName());
		}
		cache.put(new Element("Role", roleMap));

	}

	public static void refreshRoleCache() {
		cache.remove("Role");

		initRoleCache();
	}

	public static String getRoleNameById(String id) {
		// return roleMap.get(id);
		Element result = cache.get("Role");
		Map<String, String> roleMap = (Map<String, String>) result.getValue();
		// for (Role key : list) {
		// if (key.getId().equals(id)) {
		// return key.getName();
		// }
		//
		// }
		return roleMap.get(id);
	}

	public static void refresh() throws Exception {
		cache.removeAll();
		cache.flush();
		initCache();
	}

	public static List<SizeSort> sizeSortList = null;

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

		cache.put(new Element("SizeSort", sizeSortList));

	}

	public static List<SizeSort> getAllSizeSort() {
		Element result = cache.get("SizeSort");
		if (CommonUtil.isNotBlank(result)) {
			sizeSortList = (List<SizeSort>) result.getValue();
		}

		return sizeSortList;
	}

	public static SizeSort getSizeSortById(String sizeSortId) {
		Element result = cache.get("SizeSort");
		sizeSortList = (List<SizeSort>) result.getValue();
		SizeSort sizeSort = null;
		for (SizeSort ss : sizeSortList) {
			if (ss.getId().equals(sizeSortId)) {
				sizeSort = ss;
				break;
			}
		}
		return sizeSort;
	}

	public static void refreshSizeSortCache() {
		cache.remove("SizeSort");
		initSizeSortCache();
	}

	public static int getSizeMaxSeqNo() {
		return sizeMaxSeqNo;
	}

	public static Size getSizeByNo(String sizeNo) {
		Map<String, Size> sizeMap = (Map<String, Size>) cache.get("Size")
				.getValue();
		Size s = null;
		for (Size size : sizeMap.values()) {
			if (size.getSizeId().equalsIgnoreCase(sizeNo)) {
				return size;
			}
		}
		return s;
	}

	public static boolean isHaveStyleNo(String styleNo) {
		Element result = cache.get("Style");
		Map<String, Style> styleMap = (Map<String, Style>) result.getValue();
		return styleMap.containsKey(styleNo);
	}

	public static boolean isHaveColorNo(String colorNo) {
		Element result = cache.get("Color");
		Map<String, Color> colorMap = (Map<String, Color>) result.getValue();
		return colorMap.containsKey(colorNo);

	}

	public static boolean isHaveSizeNo(String sizeNo) {
		Element result = cache.get("Size");
		Map<String, Size> sizeMap = (Map<String, Size>) result.getValue();
		return sizeMap.containsKey(sizeNo);

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
	/*public static void initRmCache() {
		userService = (UserService) SpringContextUtil
				.getBean("userService");
		Map<String, User> userMap = new HashMap<String, User>();
		List<User> userList = userService.getAllUser();
		for (User u : userList) {
			userMap.put(u.getId(), u);
		}
		cache.put(new Element("User", userMap));
		//

	}*/

}
