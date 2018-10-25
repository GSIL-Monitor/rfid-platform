package com.casesoft.dmc.controller.tag;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.FileUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.util.secret.EpcSecretUtil;
import com.casesoft.dmc.core.vo.ITag;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.core.vo.TagFactory;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.model.tag.Epc;
import com.casesoft.dmc.model.tag.ExcelReader;
import com.casesoft.dmc.model.tag.Init;
import com.casesoft.dmc.model.tag.InitDtl;
import com.casesoft.dmc.service.tag.InitService;
import org.hibernate.JDBCException;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/tag/birth")
public class BirthController extends BaseController implements IBaseInfoController<Init> {

	@Autowired
	private InitService initService;

	@RequestMapping("/index")
	@Override
	public String index() {

		return "/views/tag/birth";
	}

	@RequestMapping(value = "/page")
	@ResponseBody
	@Override
	public Page<Init> findPage(Page<Init> page) throws Exception {
		this.logAllRequestParams();
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
				.getRequest());
		page.setPageProperty();
		page = this.initService.findPage(page, filters);
		return page;
	}

	@RequestMapping(value = "/detailPage")
	@ResponseBody
	public List<InitDtl> findDetailPage(String billNo) throws Exception {
		this.logAllRequestParams();
		List<InitDtl> dtlList = this.initService.findInitDtl(billNo);
		for (InitDtl dtl : dtlList) {
			Product p = CacheManager.getProductByCode(dtl.getSku());
			if (CommonUtil.isNotBlank(p)) {
				dtl.setStyleName(p.getStyleName());
				dtl.setColorName(p.getColorName());
				dtl.setSizeName(p.getSizeName());
			}
		}
		return dtlList;
	}

	@RequestMapping(value = "/detailEpcPage")
	@ResponseBody
	public List<Epc> getDpcofDetail(String billNo){
		this.logAllRequestParams();
		List<Epc> epcList= this.initService.findEpcList(billNo);
		for(Epc epc:epcList){
			Product p=CacheManager.getProductByCode(epc.getSku());
			if(CommonUtil.isNotBlank(p)){
				epc.setStyleName(p.getStyleName());
				epc.setColorName(p.getStyleName());
				epc.setSizeName(p.getSizeName());
			}
		}
		return epcList;
	}

	@RequestMapping(value = "/detail")
	@ResponseBody
	public ModelAndView showDetail(String billNo) {
		this.logAllRequestParams();
		Init init = this.initService.load(billNo);
		ModelAndView model = new ModelAndView();
		model.addObject("init", init);
		model.setViewName("/views/tag/birth_detail");
		return model;
	}

	@RequestMapping(value = "/exportFile")
	@ResponseBody
	public void exportFile(String billNo) throws Exception {
		this.logAllRequestParams();
		List<InitDtl> details = this.initService.findInitDtl(billNo);
		File inputPath = null;
		Init master = this.initService.get("billNo", billNo);
		if("CODE".equals(master.getImportType())){
			List<Epc> epcs =this.initService.findEpcList(billNo);
			inputPath= InitUtil.writeTextFile(details,epcs,true);
		} else {
			inputPath = InitUtil.writeTextFile(details, null, true);
		}
//		inputPath = InitUtil.writeTextFile(details, null, true);
		String filename = inputPath.getName();


		String contentType = "application/zip;charset=utf-8";
		this.outFile(filename, inputPath, contentType);
	}

	@Override
	public List<Init> list() throws Exception {

		return null;
	}

	@Override
	public MessageBox save(Init entity) throws Exception {

		return null;
	}

	@Override
	public MessageBox edit(String taskId) throws Exception {

		return null;
	}

	@RequestMapping("/delete")
	@ResponseBody
	@Override
	public MessageBox delete(String billNo) throws Exception {
		Init init = this.initService.load(billNo);
		try {
			if (init.getStatus() == 0) {
				this.initService.deleteNoStatus(billNo);
				return returnSuccessInfo("删除成功");
			}
			return returnFailInfo("已确认的数据不可删除");
		} catch (Exception e) {
			return returnFailInfo("删除失败");
		}
	}

	@Override
	public void exportExcel() throws Exception {


	}

	@RequestMapping(value = "/updateStatus")
	@ResponseBody
	public MessageBox updateStatus(String billNo) throws Exception {
		try {
			int i = initService.updateStatus(1, billNo);
			return this.returnSuccessInfo("确认成功");
		} catch (Exception e) {
			return this.returnFailInfo("确认失败");
		}

	}


	@RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	@ResponseBody
	@Override
	public MessageBox importExcel(MultipartFile file) throws Exception {
		this.logAllRequestParams();
		InputStream in = file.getInputStream();


		User currentUser = (User) this.getSession().getAttribute(
				Constant.Session.User_Session);
		String prefixTaskId = Constant.Bill.Tag_Init_Prefix
				+ CommonUtil.getDateString(new Date(), "yyMMdd");
		String taskId = this.initService.findMaxNo(prefixTaskId);// 重新定义单号规则
		try {
			String unit2Type = this.getReqParam("unit2Type");
			String unit2Id = this.getReqParam("unit2Id");
			Init master = InitUtil.processExcel(taskId, in, initService, currentUser);
			master.setFileName(file.getOriginalFilename().toString());
			master.setUnit2Type(unit2Type == null ? null : Integer
					.parseInt(unit2Type));
			master.setDestId(unit2Id);
			master.setImportType("BARCODE");
			initService.save(master);
			return this.returnSuccessInfo("保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return this.returnFailInfo("保存失败", e.getMessage());
		}
	}

	/**
	 * 通过条码上传打印信息
	 *
	 * @return
	 */
	@RequestMapping(value = "/importExcelBarcode", method = RequestMethod.POST)
	@ResponseBody
	public MessageBox uploadFile4(MultipartHttpServletRequest multipartRequest) throws Exception {
		this.logAllRequestParams();
		try {
			Map<String, MultipartFile> multipartFileMap = multipartRequest.getFileMap();
			if (CommonUtil.isNotBlank(multipartFileMap)) {
				for (Map.Entry<String, MultipartFile> fileEntry : multipartFileMap.entrySet()) {
					if (!fileEntry.getValue().isEmpty()) {
						User currentUser = (User) this.getSession().getAttribute(
								Constant.Session.User_Session);
						String prefixTaskId = Constant.Bill.Tag_Init_Prefix
								+ CommonUtil.getDateString(new Date(), "yyMMdd");
						String taskId = this.initService.findMaxNo(prefixTaskId);// 重新定义单号规则
						String unit2Type = this.getReqParam("unit2Type");
						String unit2Id = this.getReqParam("unit2Id");
						Init master = InitUtil.processExcel4(taskId, fileEntry.getValue().getInputStream(), initService, currentUser);
						master.setFileName(fileEntry.getValue().getOriginalFilename());
						master.setUnit2Type(unit2Type == null ? null : Integer
								.parseInt(unit2Type));
						master.setDestId(unit2Id);
						initService.save(master);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return this.returnFailInfo("压缩文件出错！");
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return this.returnFailInfo("压缩文件不合法！");

		} catch (JDBCException e) {
			e.printStackTrace();
			return this.returnFailInfo("上传失败！HUB存储失败");
		} catch (Exception e) {
			e.printStackTrace();
			return this.returnFailInfo("上传失败！" + e.getMessage());
		}
		return this.returnSuccessInfo("上传成功！");
	}

	@RequestMapping(value = "/importExcelCode")
	@ResponseBody
	public synchronized MessageBox importExcelCode(MultipartFile file) throws IOException {
		this.logAllRequestParams();
		ImportParams params = new ImportParams();//样式实体，用于确定解析的excel格式
		params.setSheetNum(1);
		String rootPath = this.getSession().getServletContext().getRealPath("/") + "tag/template/";
		File folder = new File(rootPath);//创建文件夹用于存放表格
		if (!folder.exists()) {
			folder.mkdirs();
		}

		List<Epc> epcs = new ArrayList<Epc>();
		Init init = new Init();
		List<InitDtl> initDtls = new ArrayList<InitDtl>();

		File excel = new File(folder, file.getOriginalFilename());
		file.transferTo(excel);

		List<String> skus = new ArrayList<String>();//用于计数
		User currentUser = (User) this.getSession().getAttribute(
				Constant.Session.User_Session);
		String prefixTaskId = Constant.Bill.Tag_Init_Prefix
				+ CommonUtil.getDateString(new Date(), "yyMMdd");
		String taskId = this.initService.findMaxNo(prefixTaskId);// 重新定义单号规则
		List<ExcelReader> excelReaders = ExcelImportUtil.importExcel(excel, ExcelReader.class, params);
		try {
			init.setBillNo(taskId);
			init.setBillDate(new Date());
			init.setFileName(file.getOriginalFilename());
			init.setOwnerId(currentUser.getOwnerId());
			init.setHostId(currentUser.getId());
			init.setStatus(1);
			init.setTotEpc(excelReaders.size());
			init.setImportType("CODE");

			for (ExcelReader row : excelReaders) {
				if (CommonUtil.isNotBlank(CacheManager.getProductByCode(row.getBarcode()))) {
					Epc epc = new Epc();
					InitDtl initDtl = new InitDtl();
					Product p = CacheManager.getProductByCode(row.getBarcode());
						if (!skus.contains(row.getBarcode())) {
							skus.add(row.getBarcode());
							initDtl.setId(init.getBillNo() + "-" + row.getBarcode());
							initDtl.setBillNo(init.getBillNo());
							initDtl.setColorId(p.getColorId());
							initDtl.setOwnerId(currentUser.getOwnerId());
							initDtl.setSizeId(p.getSizeId());
							initDtl.setColorId(p.getColorId());
							initDtl.setStyleId(p.getStyleId());
							initDtl.setStartNum(1);
							initDtl.setEndNum(1);
							CacheManager.setMaxTagSkuNum(initDtl.getSku(),initDtl.getEndNum());
							initDtl.setStatus(1);
							initDtl.setQty(1);
							initDtl.setSku(row.getBarcode());
							if (!initDtls.contains(initDtl))
								initDtls.add(initDtl);
						} else {                                            //用于添加initDtl的qty
							for (InitDtl dtl : initDtls) {
								if (row.getBarcode().equals(dtl.getSku())) {
									dtl.setQty(dtl.getQty() + 1);
								}
							}
						}
						epc.setCode(row.getCode());
						epc.setId(new GuidCreator().toString());
						epc.setBillNo(taskId);
						epc.setStyleId(p.getStyleId());
						epc.setSizeId(p.getSizeId());
						epc.setColorId(p.getColorId());
						epc.setOwnerId(currentUser.getOwnerId());
						epc.setSku(row.getBarcode());


						ITag iTag = TagFactory.getTag(PropertyUtil.getValue("tag_name"));
						iTag.setUniqueCode(epc.getCode());
						String epcOrigin = iTag.getEpc();
						epc.setDimension(PropertyUtil.getValue("webservice") + epc.getCode());
						epc.setEpc(iTag.getSecretEpc());
						epcs.add(epc);

				}else {
					throw new Exception("条码 "+row.getBarcode()+" 不存在!");
				}
			}
			init.setTotSku(skus.size());
			this.initService.saveInportWithCode(init,epcs,initDtls);
			FileUtil.deleteDir(folder);
			return returnSuccessInfo("上传成功");
		} catch (Exception e) {
			e.printStackTrace();
			return returnFailInfo("上传失败",e.getMessage());
		}
	}

	@RequestMapping(value = "/scanCode")
	@ResponseBody
	public MessageBox scanCode(String uniqueCode){
		if(CommonUtil.isHexNumberRex(uniqueCode) || uniqueCode.length() ==13){
			if (uniqueCode.length() != 13) {
				String epcCode = uniqueCode.toUpperCase();
				uniqueCode = EpcSecretUtil.decodeEpc(epcCode).substring(0, 13);

			}

			Epc epc = this.initService.findEpc(uniqueCode);
			if(CommonUtil.isNotBlank(epc)){
				return new MessageBox(true,"uniqueCode", epc.getBillNo());
			}else {
				return new MessageBox(false, "未查到对应单号");
			}
		}
        else {
			return new MessageBox(false, "唯一码格式不正确");
		}
	}
}
