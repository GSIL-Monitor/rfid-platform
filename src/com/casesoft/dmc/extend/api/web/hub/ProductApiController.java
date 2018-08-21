package com.casesoft.dmc.extend.api.web.hub;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.cache.RedisUtils;
import com.casesoft.dmc.cache.SpringContextUtil;
import com.casesoft.dmc.controller.product.ProductUtil;
import com.casesoft.dmc.controller.product.StyleUtil;
import com.casesoft.dmc.controller.tag.InitUtil;
import com.casesoft.dmc.controller.task.TaskUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.FileUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.api.dto.RespMessage;
import com.casesoft.dmc.extend.api.web.ApiBaseController;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.cfg.PropertyType;
import com.casesoft.dmc.model.product.*;
import com.casesoft.dmc.model.tag.Epc;
import com.casesoft.dmc.model.tag.EpcBindBarcode;
import com.casesoft.dmc.model.tag.Init;
import com.casesoft.dmc.model.tag.InitDtl;
import com.casesoft.dmc.service.cfg.PropertyKeyService;
import com.casesoft.dmc.service.log.SysLogService;
import com.casesoft.dmc.service.product.*;
import com.casesoft.dmc.service.tag.BindService;
import com.casesoft.dmc.service.tag.InitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by WingLi on 2017-01-04.
 */
@Controller
@RequestMapping(value = "/api/hub/product", method = {RequestMethod.POST, RequestMethod.GET})
@Api(description = "商品、标签信息模块接口")
public class ProductApiController extends ApiBaseController {

	@Autowired
	private ProductService productService;
	@Autowired
	private SysLogService sysLogService;
	@Autowired
	private BindService bindService;
	@Autowired
	private InitService initService;
	@Autowired
	private PropertyKeyService propertyKeyService;
	@Autowired
	private StyleService styleService;
	@Autowired
	private ColorService colorService;
	@Autowired
	private SizeService sizeService;

	@Autowired
	private PhotoService photoService;

	private static RedisUtils redisUtils = (RedisUtils) SpringContextUtil.getBean("redisUtils");

	@Override
	public String index() {
		return null;
	}

	//    @ApiOperation(value = "商品信息获取", notes = "通过版本号获取商品信息")
//    @ApiResponse(code = 200, message = "success", response = String.class)
//    @RequestMapping(value = "/findProductListByVersionWS.do", produces = "application/json;charset:UTF-8")
//    @ApiAuth
//    @ResponseBody
//    public RespMessage findProductListByVersion(@Valid @RequestBody RequestEntity<Long> requestEntity, BindingResult bindingResult) {
//        Long version = requestEntity.getRequestData();
//        return findProductListByVersionWS(version);
//    }
	@RequestMapping(value = "/findProductListWS.do")
	@ResponseBody
	public List<Product> findProductListWS() {
		this.logAllRequestParams();
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
		List<Product> products = this.productService.find(filters);
		ProductUtil.convertToPageVo(products);
		return products;
	}

	@RequestMapping(value = "/findProductListByVersionWS.do")
	@ResponseBody
	public RespMessage findProductListByVersionWS(@ApiParam @RequestParam Long version) {
		this.logAllRequestParams();
		if (CommonUtil.isBlank(version)) {
			return this.returnApiFailInfo("版本号为空");
		}
		long maxVersion = sysLogService.getVersionByTable("Product");
		if (version >= maxVersion) {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("maxVersion", maxVersion);
			result.put("updateList", new ArrayList<Product>());
			return this.returnApiSuccessInfo("已是最新版", result);
		}
		List<PropertyFilter> filters = PropertyFilter.createOneFilter(
				"GTL_version", "" + version);
		List<Product> productList = this.productService.find(filters);
		ProductUtil.convertListToVo(productList);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("maxVersion", maxVersion);
		result.put("updateList", productList);
		return this.returnApiSuccessInfo("更新条码数:" + productList.size(), result);
	}

	/**
	 * 根据版本号获取EPC与条码绑定关系
	 *
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "/findEpcListWS.do")
	@ResponseBody
	public List<EpcBindBarcode> findEpcList(@ApiParam @RequestParam Long version) {
		this.logAllRequestParams();
		if (CommonUtil.isBlank(version)) {
			List<EpcBindBarcode> epcBindBarcodeList = this.bindService.getAll();
			return epcBindBarcodeList;
		}
		long maxVersion = sysLogService.getVersionByTable("EpcBindBarcode");
		if (version >= maxVersion) {
			return new ArrayList<>();
		}
		List<PropertyFilter> filters = PropertyFilter.createOneFilter(
				"GTL_version", "" + version);
		List<EpcBindBarcode> productList = this.bindService.find(filters);
		return productList;
	}

	@RequestMapping(value = "/getProductVersionWS.do")
	@ResponseBody
	public RespMessage getProductVersionWS() {
		try {
			long version = this.sysLogService.getVersionByTable(Product.class
					.getName());
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("version", version);
			return this.returnApiSuccessInfo(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
			return this.returnApiFailInfo(e.getLocalizedMessage());
		}
	}

	/**
	 * 获取EPC绑定关系最大版本号
	 *
	 * @return
	 */
	@RequestMapping(value = "/getBindVersionWS.do")
	@ResponseBody
	public RespMessage getBindVersionWS() {
		try {
			long version = this.bindService.findMaxProductTempVersion();
			return this.returnApiSuccessInfo(version);
		} catch (Exception e) {
			e.printStackTrace();
			return this.returnApiFailInfo(e.getLocalizedMessage());
		}
	}

	@RequestMapping(value = "/getOldBindVersionWS.do")
	@ResponseBody
	public RespMessage getOldBindVersionWS() {
		try {
			long version = this.bindService.findMaxProductTempVersion();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("version", version);
			return this.returnApiSuccessInfo(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
			return this.returnApiFailInfo(e.getLocalizedMessage());
		}
	}

	@RequestMapping(value = "/boundEpcWS.do")
	@ResponseBody
	public MessageBox boundEpcWS(@ApiParam @RequestParam String boundInfo) throws Exception {
		this.logAllRequestParams();
		String tempStr = boundInfo;
		try {
			List<EpcBindBarcode> bindList = JSON.parseArray(tempStr, EpcBindBarcode.class);
			if (CommonUtil.isBlank(bindList)) {
				return this.returnFailInfo("绑定信息为空！绑定失败！");
			} else {
				for (EpcBindBarcode epcBind : bindList) {
					Product p = CacheManager.getProductByCode(epcBind.getCode());
					if (CommonUtil.isBlank(p)) {
						for (Map.Entry<String, Product> entry : CacheManager.getProductMap().entrySet()) {
							if (CommonUtil.isNotBlank(entry.getValue().getBarcode())
									&& epcBind.getCode().equals(entry.getValue().getBarcode())) {
								p = entry.getValue();
								break;
							}
						}
						if (CommonUtil.isBlank(p)) {
							return this.returnFailInfo("绑定失败！条码错误！" + epcBind.getCode());
						}
					}
					epcBind.setCode(p.getCode());
					epcBind.setUpdateTime(new Date());
				}
				this.bindService.saveList(bindList);
				return this.returnSuccessInfo("绑定成功!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.returnFailInfo("绑定失败！");
		}
	}

	@RequestMapping(value = "/unboundEpcWS.do")
	@ResponseBody
	public MessageBox unboundEpcWS(@ApiParam @RequestParam String epcs) throws Exception {
		this.logAllRequestParams();
		String tempStr = epcs;

		try {
			List<String> cds = JSON.parseArray(tempStr, String.class);
			if (CommonUtil.isBlank(cds)) {
				return this.returnFailInfo("信息为空！解绑失败！");
			} else {
				String epcInSql = TaskUtil.getSqlStrByList(cds, EpcBindBarcode.class, "epc");
				this.bindService.deleteBindInEPCList(epcInSql);
				return this.returnSuccessInfo("解绑成功!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.returnFailInfo("解绑失败！");
		}
	}

	@RequestMapping(value = "/downloadProductZipFileWS.do")
	public void downloadProductZipFileWS(Long version) throws IOException {
		int pageSize = 10000;
		Page<Product> page = new Page<Product>();
		page.setOrderBy("code");
		page.setOrder("desc");
		page.setPageNo(1);
		page.setPageSize(pageSize);
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		if(CommonUtil.isNotBlank(version)){
			PropertyFilter filter = new PropertyFilter("GTL_version",version.toString());//比version大的
			filters.add(filter);
		}
		Page<Product> prodPage = productService.findPage(page, filters);
		if(CommonUtil.isNotBlank(page.getRows())){
			ProductUtil.convertToPageVo(page.getRows());
		}
		FileUtil.writeStringToFile(JSON.toJSONString(prodPage.getRows()),
				Constant.rootPath+File.separator+"casesoft_temp"+File.separator+"sku"+File.separator+"casesoft_sku_1.json");
		long totPageNum = (prodPage.getTotal() + pageSize - 1) / pageSize;//总页数
		System.out.println("第1页：" + prodPage.getRows().size());
		for (int i = 2; i <= totPageNum; i++) {
			prodPage.setOrderBy("code");
			prodPage.setOrder("desc");
			prodPage.setPageNo(i);
			prodPage.setPageSize(pageSize);
			prodPage = productService.findPage(page, filters);
			if(CommonUtil.isNotBlank(page.getRows())){
				ProductUtil.convertToPageVo(page.getRows());
			}
			FileUtil.writeStringToFile(JSON.toJSONString(prodPage.getRows()),
					Constant.rootPath+File.separator+"casesoft_temp"+File.separator+"sku"+File.separator+"casesoft_sku_" + i + ".json");
			System.out.println("第" + i + "页：" + prodPage.getRows().size());
		}

		String zipFileName = Constant.rootPath+File.separator+"casesoft_temp"+File.separator+"casesoft_sku.zip";
		String sourcePath = Constant.rootPath+File.separator+"casesoft_temp"+File.separator+"sku"+File.separator;
		File skuDir = new File(sourcePath);
		if (!skuDir.exists()) {
			skuDir.mkdir();
		}
		FileUtil.zip(sourcePath, zipFileName);

		File inputPathFile = new File(zipFileName);
		String fileName = inputPathFile.getName();
		String contentType = "application/zip;charset=utf-8";

		this.outFile(fileName, inputPathFile, contentType);


	}

	@RequestMapping(value = "/downloadPrintFileWS.do")
	public void downloadPrintFile(@ApiParam @RequestParam String billNo,
								  @ApiParam @RequestParam String deviceId, @ApiParam @RequestParam String isRfid) throws Exception {
		this.logAllRequestParams();


		Assert.notNull(deviceId, "deviceId不能为空");
		String ownerId = CacheManager.getDeviceByCode(deviceId).getOwnerId();// deviceId
		// 不能为空
		boolean isRfidBool = Boolean.valueOf(isRfid);

		Init master = this.initService.get("billNo", billNo);

		List<InitDtl> details = this.initService.findDtls(billNo, ownerId);
		File inputPath;
		if("CODE".equals(master.getImportType())){
			List<Epc> epcs =this.initService.findEpcList(billNo);
			inputPath= InitUtil.writeTextFile(details,epcs,isRfidBool);
		} else {
			inputPath = InitUtil.writeTextFile(details, null, isRfidBool);
		}

		if (master.getStatus() == 1) {// 已确认状态
			// master.setStatus(-1);// 设置状态 打印中...
			this.initService.update(InitUtil.epcList, master);

		}



		String filename = inputPath.getName();
		String contentType = "application/zip;charset=utf-8";

		this.outFile(filename, inputPath, contentType);
	}

	/**
	 * 上传打印结果 status==2 表示已打印
	 *
	 * @param billNo
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "/updatePrintStatusWS.do")
	@ResponseBody
	public RespMessage updatePrintStatus(@ApiParam @RequestParam String billNo,
										 @ApiParam @RequestParam String status) {
		this.logAllRequestParams();
		Assert.notNull(billNo, "billNo不能为空");
		Assert.notNull(status, "status不能为空");
		Init initBill = this.initService.get("billNo", billNo);
		initBill.setStatus(Integer.parseInt(status));
		this.initService.update(initBill);
		return this.returnApiSuccessInfo(initBill);
	}

	@RequestMapping(value = "/listPropertyKeyWS.do")
	@ResponseBody
	public List<PropertyKey> listPropertyKeyWS() {
		this.logAllRequestParams();
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
				.getRequest());
		List<PropertyKey> listKey = this.propertyKeyService.find(filters);
		return listKey;
	}

	@RequestMapping(value = "/listPropertyNameWS.do")
	@ResponseBody
	public List<PropertyType> listPropertyNameWS() {
		this.logAllRequestParams();
		List<PropertyType> listType = this.propertyKeyService.findPrpertyByType();
		return listType;
	}

	@RequestMapping(value = "/findEpcBindListWS.do")
	@ResponseBody
	public List<EpcBindBarcode> findEpcBindListWS() {
		String version=this.getReqParam("version");
		if(CommonUtil.isNotBlank(version)){
			return this.findEpcList(Long.parseLong(version));
		}else {
			List<EpcBindBarcode> bindList = this.bindService.getAll();
			return bindList;
		}
	}


	/**
	 * 根据条码或SKU获取EPC
	 *
	 * @param barcode
	 * @param deviceId
	 * @param qty
	 * @return
	 */
	@RequestMapping(value = "/productEpcWS.do")
	@ResponseBody
	public MessageBox productEpcWS(String barcode, String deviceId, String qty) {
		List<String> epcs = new ArrayList<String>();
		if (CommonUtil.isBlank(barcode)) {
			return this.returnFailInfo("无条码输入！", epcs);
		} else if (CommonUtil.isBlank(CacheManager.getDeviceByCode(deviceId))) {
			return this.returnFailInfo("设备号无效！", epcs);
		} else {
			int totQty = 1;
			try {
				if (CommonUtil.isNotBlank(qty)) {
					totQty = Integer.parseInt(qty);
				}
				boolean iss = InitUtil.excuteBarcodeEpc(barcode, totQty, deviceId,
						this.initService, epcs);
				if (iss) {
					return this.returnSuccessInfo("成功！", epcs);
				} else {
					return this.returnFailInfo("生成失败！", epcs);
				}

			} catch (Exception e) {
				e.printStackTrace();
				return this.returnFailInfo(e.getMessage());
			}
		}
	}

    /**
     * 通过barcode获取打印信息
     * */

    @RequestMapping(value = "/findPrintWS.do")
    @ResponseBody
    public MessageBox findPrintWS(String barcode, String deviceId, String qty) {
        List<String> epcs = new ArrayList<String>();
        if (CommonUtil.isBlank(barcode)) {
            return this.returnFailInfo("无条码输入！", epcs);
        } else if (CommonUtil.isBlank(CacheManager.getDeviceByCode(deviceId))) {
            return this.returnFailInfo("设备号无效！", epcs);
        } else {
            int totQty = 1;
            try {
                if (CommonUtil.isNotBlank(qty)) {
                    totQty = Integer.parseInt(qty);
                }
                List<Epc> epcList  = InitUtil.excuteBarcodeEpcList(barcode, totQty, deviceId,
                        this.initService, epcs);
                if (CommonUtil.isNotBlank(epcList)) {
                    return this.returnSuccessInfo("成功！", epcList);
                } else {
                    return this.returnFailInfo("生成失败！",barcode);
                }

            } catch (Exception e) {
                e.printStackTrace();
                return this.returnFailInfo(e.getMessage());
            }
        }
    }
	@RequestMapping(value = "/findImgWS")
	@ResponseBody
	public MessageBox productImage(String styleId, String colorId) {

		List<Product> productList = new ArrayList<Product>();
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		if (CommonUtil.isNotBlank(styleId)) {
			filters.add(new PropertyFilter("EQS_styleId", styleId));
		}
		if (CommonUtil.isNotBlank(colorId)) {
			filters.add(new PropertyFilter("EQS_colorId", colorId));
		}
		List<Photo> photos = this.photoService.find(filters);
		for (Photo photo : photos) {
			Product product = new Product();
			product.setStyleId(styleId);
			if(CommonUtil.isNotBlank(colorId)){
				product.setColorId(colorId);
			}else{
				product.setColorId("-");
			}
			product.setImage("/product/photo/"+photo.getSrc());
			productList.add(product);
		}
		return returnSuccessInfo("获取成功", productList);
	}

	/**
	 * @Param styleStr 款信息json字符串
	 * @Param productStr 商品jsonArray字符串
	 * */
	@RequestMapping("/saveStyleAndProductWS")
	@ResponseBody
	public MessageBox saveStyleAndProduct(String styleStr,String productStr) throws Exception {

		Style style = JSON.parseObject(styleStr,Style.class);
		Style sty = CacheManager.getStyleById(style.getStyleId());
		//查询当前最新的版本号
		Long productMaxVersionId = CacheManager.getproductMaxVersionId();
		Long maxVersionId = CacheManager.getStyleMaxVersionId();
		sty.setVersion(maxVersionId+1);
		if(CommonUtil.isBlank(sty)){
			sty=new Style();
			sty.setId(style.getStyleId());
			sty.setStyleId(style.getStyleId());
			sty.setVersion(maxVersionId+1);
		}
		List<Product> productList = JSON.parseArray(productStr,Product.class);
		List<Product> saveList = StyleUtil.covertToProductInfo(sty,style,productList);
		try {
			this.styleService.saveStyleAndProducts(sty,saveList);
			//保存成功更新缓存
			redisUtils.hset("maxVersionId","productMaxVersionId", JSON.toJSONString(productMaxVersionId+1));
			redisUtils.hset("maxVersionId","styleMaxVersionId",JSON.toJSONString(maxVersionId+1));
			CacheManager.refreshMaxVersionId();
			CacheManager.refreshStyleCache();
			if(saveList.size() > 0){
				CacheManager.refreshProductCache();
			}
			return this.returnSuccessInfo("保存成功", style);
		} catch (Exception e){
			return this.returnFailInfo("保存失败",e.toString());
		}
	}
	
	//region Pairs项目接口
	@RequestMapping("/findStylesWS")
	@ResponseBody
	public MessageBox findStyles(String pageSize,String pageNo,String sortIds,String  orders) throws Exception {
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
		Page<Style> page = new Page<>(Integer.parseInt(pageSize));
		page.setPage(Integer.parseInt(pageNo));
		if(CommonUtil.isNotBlank(sortIds)){
			if(sortIds.split(",").length != orders.split(",").length){
				return new MessageBox(false,"排序字段与排序方向的个数不相等");
			}
			page.setOrderBy(sortIds);
			page.setOrder(orders);
		}
		page.setPageProperty();
		page = this.styleService.findPage(page,filters);
		return this.returnSuccessInfo("获取成功",page.getRows());
	}
	@RequestMapping("/findColorsWS")
	@ResponseBody
	public MessageBox findColors(String pageSize,String pageNo,String sortIds,String  orders) throws Exception {
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
		Page<Color> page = new Page<>(Integer.parseInt(pageSize));
		page.setPage(Integer.parseInt(pageNo));
		if(CommonUtil.isNotBlank(sortIds)){
			if(sortIds.split(",").length != orders.split(",").length){
				return new MessageBox(false,"排序字段与排序方向的个数不相等");
			}
			page.setOrderBy(sortIds);
			page.setOrder(orders);
		}
		page.setPageProperty();
		page = this.colorService.findPage(page,filters);
		return this.returnSuccessInfo("获取成功", page.getRows());
	}
	@RequestMapping("/findSizesWS")
	@ResponseBody
	public MessageBox findSizes(String pageSize,String pageNo,String sortIds,String  orders) throws Exception {
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
		Page<Size> page = new Page<>(Integer.parseInt(pageSize));
		page.setPage(Integer.parseInt(pageNo));
		if(CommonUtil.isNotBlank(sortIds)){
			if(sortIds.split(",").length != orders.split(",").length){
				return new MessageBox(false,"排序字段与排序方向的个数不相等");
			}
			page.setOrderBy(sortIds);
			page.setOrder(orders);
		}
		page.setPageProperty();
		page = this.sizeService.findPage(page,filters);
		return this.returnSuccessInfo("获取成功", page.getRows());
	}
	@RequestMapping("/findProductDtlWS")
	@ResponseBody
	public MessageBox findProductDtl() throws Exception {
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
	    List<Product> prodList = this.productService.find(filters);
	    for(Product p : prodList) {
	    	p.setColorName(CacheManager.getColorNameById(p.getColorId()));
	    	p.setSizeName(CacheManager.getSizeNameById(p.getSizeId()));
	    }
	    return this.returnSuccessInfo("获取成功", prodList);
	}

	@RequestMapping("/findSizeSortWS")
	@ResponseBody
	public MessageBox findSizeSort() throws Exception {
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
		List<SizeSort> sizeSortList = this.sizeService.findSort(filters);
		return  this.returnSuccessInfo("获取成功",sizeSortList);
	}
	
	//endregion
}
