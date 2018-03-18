package com.casesoft.dmc.extend.mirror;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.mirror.StarInfo;
import com.casesoft.dmc.service.mirror.StarInfoService;

@RequestMapping("/smart/star")
@Controller
public class StarController extends BaseController implements
		IBaseInfoController<StarInfo> {

	@Autowired
    StarInfoService starInfoService;

	@RequestMapping("/index")
	@Override
	public String index() {
		return "/views/smart/starInfo";
	}

	@RequestMapping("page")
	@ResponseBody
	public Page<StarInfo> findPage(Page<StarInfo> page) throws Exception {
		this.logAllRequestParams();

		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
				.getRequest());
		page.setPageProperty();

		page = this.starInfoService.findPage(page, filters);
		return page;
	}

	@RequestMapping("/add")
	@ResponseBody
	public ModelAndView add() {
		ModelAndView mav = new ModelAndView("/views/smart/starInfo_edit");
		mav.addObject("pageType", "add");
		Integer seqNo=this.starInfoService.getMaxseqNo();
		mav.addObject("seqNo", seqNo+1);
		return mav;

	}

	@RequestMapping("/edit")
	@ResponseBody
	public ModelAndView editStar(String id) {
		ModelAndView mav = new ModelAndView("/views/smart/starInfo_edit");
		StarInfo starInfo = this.starInfoService.load(id);
		mav.addObject("starInfo", starInfo);
		mav.addObject("pageType", "edit");
		return mav;
	}

	@RequestMapping("/save")
	@ResponseBody
	@Override
	public MessageBox save(StarInfo starInfo) throws Exception {
		StarInfo starinfo = this.starInfoService.get("id", starInfo.getId());
		if (CommonUtil.isBlank(starinfo)) {
			starinfo = new StarInfo();
			starinfo.setId(starInfo.getId());
			starinfo.setIsShow(starInfo.getIsShow());
			starinfo.setStarName(starInfo.getStarName());
			starinfo.setSeqNo(starInfo.getSeqNo());
			starinfo.setUpdater(this.getCurrentUser().getOwnerId());
			starinfo.setUpdateTime(new Date());
			starinfo.setUrl(starInfo.getUrl());
		} else {
			starinfo.setStarName(starInfo.getStarName());
			starinfo.setIsShow(starInfo.getIsShow());
			starinfo.setUpdater(this.getCurrentUser().getOwnerId());
			starinfo.setUpdateTime(new Date());
		}
		try {
			this.starInfoService.save(starinfo);
			return returnSuccessInfo("保存成功");
		} catch (Exception e) {
			return returnFailInfo("保存失败");
		}
	}

	@RequestMapping("/savestarImages")
	@ResponseBody
	public MessageBox saveImage(MultipartFile file, String id) throws Exception {
		this.logAllRequestParams();

		// String rootPath =this.getRequest().getRealPath("/");
		String rootPath = this.getSession().getServletContext()
				.getRealPath("/");
		Double width = Double.parseDouble(this.getReqParam("tailorWidth"));
		String tailor = this.getReqParam("tailor");
		ImageCut cut = null;

		if (!tailor.equals("false")) {
			cut = JSON.parseObject(tailor, ImageCut.class);
		}

		String fileName = file.getOriginalFilename();
		String fileSuffix = fileName.substring(fileName.lastIndexOf('.') + 1);
		fileName = id + "." + fileSuffix;

		String filePath = rootPath + "/mirror/starInfo/" + id + "/";
		File folder = new File(filePath);
		if (!folder.exists()) {
			folder.mkdirs();
		}

		File f = new File(filePath, fileName);
		file.transferTo(f);

		if (CommonUtil.isNotBlank(cut)) {
			ImageCutUtil.covertImage(f, width, cut);
		} else {
			ImageCutUtil.commpressPic(f.getPath(), f.getPath());
		}
		return returnSuccessInfo(fileName, filePath + fileName);
	}

	@RequestMapping("/delimg")
	@ResponseBody
	public MessageBox deleteImg(String id, String no) throws Exception {
		String rootPath = this.getSession().getServletContext()
				.getRealPath("/");
		StarInfo starInfo = this.starInfoService.findById(id);
		String imgs = starInfo.getUrl();
		String img[] = imgs.split(",");
		imgs = null;
		ArrayList<String> ia = new ArrayList<String>();
		for (int i = 0; i < img.length; i++) {
			ia.add(i, img[i]);
		}
		String filePath = rootPath;
		File file = new File(rootPath + ia.get(Integer.parseInt(no)));
		if (file.exists() && file.isFile()) {
			file.delete();
		}

		ia.remove(Integer.parseInt(no));
		for (int i = 0; i < ia.size(); i++) {
			if (i == 0) {
				imgs = ia.get(i);
			} else {
				imgs += "," + ia.get(i);
			}

		}
		starInfo.setUrl(imgs);
		this.starInfoService.save(starInfo);
		;
		return returnSuccessInfo(imgs);
	}

	@RequestMapping("/detail")
	@ResponseBody
	public ModelAndView detailStar(String id) {
		ModelAndView mav = new ModelAndView("/views/smart/starInfo_detail");
		StarInfo starInfo = this.starInfoService.findById(id);
		mav.addObject("starInfo", starInfo);
		return mav;
	}

	@Override
	public List<StarInfo> list() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MessageBox edit(String taskId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MessageBox delete(String taskId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void exportExcel() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public MessageBox importExcel(MultipartFile file) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
