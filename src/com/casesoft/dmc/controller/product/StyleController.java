package com.casesoft.dmc.controller.product;

import java.io.InputStream;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.cache.RedisUtils;
import com.casesoft.dmc.cache.SpringContextUtil;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.core.util.json.FastJSONUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.cfg.PropertyType;

import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.product.Term;
import com.casesoft.dmc.model.sys.ResourcePrivilege;
import com.casesoft.dmc.model.tag.Epc;
import com.casesoft.dmc.service.cfg.PropertyService;
import com.casesoft.dmc.service.pad.WeiXinUserService;
import com.casesoft.dmc.service.product.ComponentsService;
import com.casesoft.dmc.service.product.ProductService;
import com.casesoft.dmc.service.product.StyleService;
import com.casesoft.dmc.service.push.pushBaseInfo;
import com.casesoft.dmc.service.sys.ResourcePrivilegeService;
import com.casesoft.dmc.service.sys.impl.UserService;
import com.casesoft.dmc.service.stock.EpcStockService;
import com.casesoft.dmc.service.sys.KeyInfoChangeService;
import com.casesoft.dmc.service.tag.InitService;

import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/prod/style")
public class StyleController extends BaseController implements IBaseInfoController<Style>{

	@Autowired
	private StyleService styleService;
	@Autowired
	public pushBaseInfo wxShopBaseService;
	@Autowired
	private ProductService productService;
	@Autowired
	public InitService initService;
	@Autowired
	public EpcStockService epcStockService;
	@Autowired
	public KeyInfoChangeService keyInfoChangeService;
	@Autowired
	public WeiXinUserService weiXinUserService;
	@Autowired
	private PropertyService propertyService;
	@Autowired
	private ResourcePrivilegeService resourcePrivilegeService;
	@Autowired
	private UserService userService;
	@Autowired
	private ComponentsService componentsService;

	private static RedisUtils redisUtils = (RedisUtils) SpringContextUtil.getBean("redisUtils");

	@RequestMapping(value={"/page","/pageWS"})
	@ResponseBody
	@Override
	public Page<Style> findPage(Page<Style> page) throws Exception {
		this.logAllRequestParams();
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
		page.setPageProperty();
		page = this.styleService.findPage(page, filters);
		return page;
	}


	@RequestMapping("/list")
	@ResponseBody
	@Override
	public List<Style> list() throws Exception {
		this.logAllRequestParams();
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
				.getRequest());
		return this.styleService.find(filters);
	}

	@RequestMapping("/save")
	@ResponseBody
	@Override
	public MessageBox save(Style style) throws Exception {
		Style sty =this.styleService.fundByStyleId(style.getStyleId());
		long styleMaxVersionId = CacheManager.getStyleMaxVersionId();
		sty.setVersion(styleMaxVersionId);
		if(CommonUtil.isBlank(sty)){
			sty=new Style();
			sty.setId(style.getStyleId());
			sty.setStyleId(style.getStyleId());
			sty.setVersion(styleMaxVersionId);
		}
		StyleUtil.copyStyleInfo(sty,style);
		try {
			this.styleService.save(sty);
			redisUtils.hset("maxVersionId","styleMaxVersionId",JSON.toJSONString(styleMaxVersionId+1));
			CacheManager.refreshMaxVersionId();
			List<Style> styleList = new ArrayList<>();
			styleList.add(sty);
			CacheManager.refreshStyleCache(styleList);
			return this.returnSuccessInfo("保存成功", style);
		}catch(Exception e ){
			e.printStackTrace();
			return this.returnFailInfo("保存失败");
		}
	}

	/**
	 * @param styleStr 款信息json字符串
	 * @param productStr 商品jsonArray字符串
	 * @param  userId 当前登录用户
	 * @param pageType 判断是add（）还是edit（）的请求
	 * @author  刘天赐 2018.3.20
	 * */
	@RequestMapping("/saveStyleAndProduct")
	@ResponseBody
	public MessageBox saveStyleAndProduct(HttpServletRequest request, String styleStr, String productStr, String userId, String pageType) throws Exception {
		try {
			HashMap<String, Object> prePriceMap = new HashMap<>();
			Style style = JSON.parseObject(styleStr,Style.class);
			Style sty = CacheManager.getStyleById(style.getStyleId());
			Long productMaxVersionId = CacheManager.getproductMaxVersionId();
			//查询当前款最新的版本号
			Long maxVersionId = CacheManager.getStyleMaxVersionId();
			//sty.setVersion(maxVersionId+1);
			//判断是 add（）的请求还是 edit（）的请求
			if ("add".equals(pageType)){
				//判断sytleId在数据库中是否存在
				if(CommonUtil.isBlank(sty)){
					//赋值
					sty=new Style();
					sty.setId(style.getStyleId());
					sty.setStyleId(style.getStyleId());
					sty.setVersion(maxVersionId+1);
					sty.setIsUse("Y");
				}else {
					return this.returnFailInfo("保存失败!"+sty.getId()+"款号已存在请重新输入");
				}
			}else if ("edit".equals(pageType)) {
				if (CommonUtil.isBlank(sty)) {
					return this.returnFailInfo("编辑失败!" + sty.getId() + "款号不存在");
				}
				sty.setVersion(maxVersionId+1);
				prePriceMap.put("price", sty.getPrice());
				prePriceMap.put("puPrice", sty.getPuPrice());
				prePriceMap.put("wsPrice", sty.getWsPrice());
				prePriceMap.put("preCast", sty.getPreCast());
			} else {
				throw new RuntimeException("保存类型只能传字符串：'add' or 'edit'");
			}
			sty.setOprId(userId);
			List<Product> productList = JSON.parseArray(productStr,Product.class);
			List<Product> saveList = StyleUtil.covertToProductInfo(sty,style,productList);

			/*//保存款成分表
			PinyinTool tool = new PinyinTool();//汉字转换拼音工具
			List<ComponentsProduct> saveComponentsList = new ArrayList<>();
			sty.setRemark(sty.getRemark().trim());
			String[] remarks = style.getRemark().split("\r\n");
			for(int i = 0;i < remarks.length;i++){
				ComponentsProduct componentsProduct = new ComponentsProduct();
				String[] remarkDetail = remarks[i].split(":");
				//成分数组一定是两个元素
				if(remarkDetail.length > 1){
					componentsProduct.setId(sty.getId()+tool.toPinYin(remarkDetail[0],"", PinyinTool.Type.LOWERCASE));
					componentsProduct.setStyleId(sty.getId());
					componentsProduct.setComponentsName(remarkDetail[0]);
					componentsProduct.setCremark(remarkDetail[1]);
					componentsProduct.setComponentsId(tool.toPinYin(remarkDetail[0],"", PinyinTool.Type.LOWERCASE));
					saveComponentsList.add(componentsProduct);
				}
			}
			this.componentsService.saveBatchComponentsProduct(saveComponentsList);*/

			this.styleService.saveStyleAndProducts(sty,saveList);

			HashMap<String, Object> aftPriceMap = new HashMap<>();
			aftPriceMap.put("price", sty.getPrice());
			aftPriceMap.put("puPrice", sty.getPuPrice());
			aftPriceMap.put("wsPrice", sty.getWsPrice());
			aftPriceMap.put("preCast", sty.getPreCast());
			//保存价格变动信息并向微信公众号推送消息
			this.keyInfoChangeService.saveChangeInfoAndPushOAMsg(userId, request.getRequestURI().toUpperCase(), sty.getId(), prePriceMap, aftPriceMap);

            //保存成功更新缓存
            redisUtils.hset("maxVersionId","productMaxVersionId", JSON.toJSONString(productMaxVersionId+1));
            redisUtils.hset("maxVersionId","styleMaxVersionId",JSON.toJSONString(maxVersionId+1));
            CacheManager.refreshMaxVersionId();
            List<Style> styleList = new ArrayList<>();
            styleList.add(sty);
            CacheManager.refreshStyleCache(styleList);
            CacheManager.refreshProductCache(saveList);
			//推送微信商城
			//读取congif.properties文件
			boolean is_wxshop = Boolean.parseBoolean(PropertyUtil
					.getValue("is_wxshop"));
			if (is_wxshop) {
				boolean ispush = this.wxShopBaseService.WxShopStyle(sty, saveList);
				if (ispush) {
					return this.returnSuccessInfo("保存成功", style);
				} else {
					return this.returnSuccessInfo("保存成功,推送失败", style);
				}
			} else {
				return this.returnSuccessInfo("保存成功", style);
			}
		}catch(Exception e ){
			e.printStackTrace();
			return this.returnFailInfo("保存失败");
		}
	}
	@Override
	public MessageBox edit(String taskId) throws Exception {

		return null;
	}
	@RequestMapping("/add")
	@ResponseBody
	public ModelAndView addStyle(){
		ModelAndView mv = new ModelAndView("/views/prod/style_edit");
		List<PropertyType> propertyTypeList = this.styleService.findStylePropertyType();
		String roleId = getCurrentUser().getRoleId();
		List<ResourcePrivilege> resourcePrivilegeList = this.resourcePrivilegeService.findButtonByCodeAndRoleId("prod/style",roleId,"div");
		mv.addObject("pageType","add");
		mv.addObject("classTypes",propertyTypeList);
		mv.addObject("styleId", "");
		mv.addObject("roleId",getCurrentUser().getRoleId());
		mv.addObject("userId",getCurrentUser().getId());
		mv.addObject("fieldList", FastJSONUtil.getJSONString(resourcePrivilegeList));
		return mv;
	}
	@RequestMapping("/edit")
	@ResponseBody
	public ModelAndView editStyle(String styleId){
		ModelAndView mv = new ModelAndView("/views/prod/style_edit");
		List<PropertyType> propertyTypeList = this.styleService.findStylePropertyType();
		String roleId = getCurrentUser().getRoleId();
		//查询当前用户对应字段
		List<ResourcePrivilege> resourcePrivilegeList = this.resourcePrivilegeService.findButtonByCodeAndRoleId("prod/style",roleId,"div");
		Style s = CacheManager.getStyleById(styleId);
		mv.addObject("pageType","edit");
		mv.addObject("style",s);
		mv.addObject("styleId", styleId);
		mv.addObject("classTypes",propertyTypeList);
		mv.addObject("roleId",roleId);
		//传递字段
		mv.addObject("fieldList", FastJSONUtil.getJSONString(resourcePrivilegeList));
		mv.addObject("userId",getCurrentUser().getId());
		return mv;
	}
	@RequestMapping("/findStyleById")
	@ResponseBody
	public MessageBox findStyleById(String styleId) throws Exception{
		Style s = CacheManager.getStyleById(styleId);
		return this.returnSuccessInfo("ok", s);
	}

	@Override
	public MessageBox delete(String taskId) throws Exception {

		return null;
	}

	@Override
	public void exportExcel() throws Exception {


	}
	@RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	@ResponseBody
	@Override
	public MessageBox importExcel(MultipartFile file) throws Exception {
		InputStream in = file.getInputStream();
		try {
			List<Style> styleList = StyleUtil.uploadNewExcel(in,file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")));
			this.styleService.saveList(styleList);
			CacheManager.refreshStyleCache(styleList);
			return this.returnSuccessInfo("保存成功");
		}catch(Exception e){
			logger.error(e.getMessage());
			return this.returnFailInfo("保存失败", e.getMessage());
		}

	}
	@RequestMapping("/index")
	public ModelAndView indexMV(){
		ModelAndView mv = new ModelAndView("/views/prod/style");
		mv.addObject("roleId",getCurrentUser().getRoleId());
		mv.addObject("userId",getCurrentUser().getId());
		return mv;
	}
	@Override
	public String index() {
		return "/views/prod/style";
	}


	@RequestMapping(value = "/changeStyleStatus")
	@ResponseBody
	public MessageBox changeStyleStatus(String styleId,String status){
		this.logAllRequestParams();
		try{
			Style style = CacheManager.getStyleById(styleId);
			style.setIsUse(status);
			this.styleService.save(style);
			return returnSuccessInfo("更改成功");
		}catch(Exception e){
			e.printStackTrace();
			return returnFailInfo("更改失败");
		}
	}
	@RequestMapping(value = "/listProuctBySkuWS")
	@ResponseBody
	public MessageBox listProuctBySku(String codes){
		try {
			List<Style> list = this.styleService.listProuctBySku(codes);
			return  new MessageBox(true,"查询成功",list);
		}catch (Exception e){
			e.printStackTrace();
			return  new MessageBox(true,"查询失败",e.getMessage());
		}
	}
	@RequestMapping(value = "/changePS")
	@ResponseBody
	public MessageBox changePS(String code,String status){
		this.logAllRequestParams();
		Product pro = CacheManager.getProductByCode(code);
		List<Epc> epc = this.initService.findEpcBySkuList(code);
		if (status.equals("N")){
			try{
				pro.setIsUse(status);
				this.productService.saveOrUpdate(pro);
				return returnSuccessInfo("更改成功");
			}catch(Exception e){
				e.printStackTrace();
				return returnFailInfo("更改失败");
			}
		}else {
			if (CommonUtil.isBlank(epc)){
				try{
					pro.setIsUse(status);
					this.productService.saveOrUpdate(pro);
					return returnSuccessInfo("更改成功");
				}catch(Exception e){
					e.printStackTrace();
					return returnFailInfo("更改失败");
				}
			}else {
				return returnFailInfo("更改失败");
			}

		}

	}
	@RequestMapping(value = {"/remark","/remarkWS"})
	@ResponseBody
	public net.sf.json.JSON remark(){
		try {
			String termString = request.getParameter("term");
			List<PropertyKey> propertyTypeList = this.propertyService.findByRemark(termString);
			List<Term> termList = new ArrayList<>();
			for (PropertyKey propertyKey : propertyTypeList){
				Term term = new Term();
				term.setId(propertyKey.getId());
				term.setLabel(propertyKey.getName());
				term.setValue(propertyKey.getName());
				termList.add(term);
			}
			return JSONArray.fromObject(termList);
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/*
	* 根据权限查询div
	* */
	@RequestMapping(value = {"/getResourceButtonList","/getResourceButtonListWS"})
	@ResponseBody
	public List<ResourcePrivilege> getResourceButtonList(String userId){
		String roleId =this.userService.getUser(userId).getRoleId();
		return this.resourcePrivilegeService.findButtonByCodeAndRoleId("prod/style",roleId,"div");

	}

	/**
	 * 根据权限查询table
	 */
	@RequestMapping(value = "/initStyleGridGroup")
	@ResponseBody
	public MessageBox initStyleGridGroup(){
		String roleId = getCurrentUser().getRoleId();
		try {
			List<ResourcePrivilege> resourcePrivileges = this.resourcePrivilegeService.findButtonByCodeAndRoleId("prod/style",roleId,"table");
			return new MessageBox(true,"查询成功", resourcePrivileges);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}