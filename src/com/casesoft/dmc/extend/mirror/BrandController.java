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
import com.casesoft.dmc.model.mirror.BrandInfo;
import com.casesoft.dmc.model.mirror.Collocat;
import com.casesoft.dmc.service.mirror.BrandInfoService;

@RequestMapping("/smart/brand")
@Controller
public class BrandController extends BaseController implements IBaseInfoController<BrandInfo>{

	@Autowired
	private BrandInfoService brandInfoService;
	
	@RequestMapping("/index")
	@Override
	public String index() {
		return "/views/smart/brandInfo";
	}
	
	@RequestMapping("/page")
	@ResponseBody
	public Page<BrandInfo> findPage(Page<BrandInfo> page) throws Exception {
		this.logAllRequestParams();
      List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
              .getRequest());
      page.setPageProperty();
      page =this.brandInfoService.findPage(page, filters);
      return page;
	}
	
	@RequestMapping("/add")
	@ResponseBody
	public ModelAndView add(){
		ModelAndView mav=new ModelAndView("/views/smart/brandInfo_edit");
		Integer seqNo=this.brandInfoService.getMaxSeqNo();
		mav.addObject("seqNo",seqNo+1);
		mav.addObject("pageType","add");
		return mav;
		}
	
	@RequestMapping("/edit")
	@ResponseBody
	public ModelAndView editBrand(String brand){
		ModelAndView mav=new ModelAndView("/views/smart/brandInfo_edit");
		BrandInfo brandInfo=this.brandInfoService.findBrandBybrand(brand);
		mav.addObject("brandInfo",brandInfo);
		mav.addObject("pageType","edit");
		return mav;
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public MessageBox save(BrandInfo brandInfo){
		BrandInfo brand=this.brandInfoService.findBrandBybrand(brandInfo.getBrand());
		if(CommonUtil.isBlank(brand)) {
			brand=new BrandInfo();
			brand.setBrand(brandInfo.getBrand());
			brand.setName(brandInfo.getName());
			brand.setSeqNo(brandInfo.getSeqNo());
			brand.setAddress(brandInfo.getAddress());
			brand.setDesigner(brandInfo.getDesigner());
			brand.setRemark(brandInfo.getRemark());
			brand.setUpdateTime(new Date());
			brand.setUpdater(this.getCurrentUser().getOwnerId());
			brand.setUrl(brandInfo.getUrl());
		} else {
			brand.setName(brandInfo.getName());
			brand.setAddress(brandInfo.getAddress());
			brand.setDesigner(brandInfo.getDesigner());
			brand.setRemark(brandInfo.getRemark());
			brand.setUpdater(this.getCurrentUser().getOwnerId());
			brand.setUpdateTime(new Date());
		}
		try{
			this.brandInfoService.save(brand);
			return returnSuccessInfo("保存成功");
			
		}catch(Exception e) {
			return returnFailInfo("保存失败");
		}
	}
	
	@RequestMapping("/saveBrandImages")
	@ResponseBody
	public MessageBox saveBrandImages(MultipartFile file,String brand)throws Exception
	{
		String rootPath=this.getSession().getServletContext().getRealPath("/");
		Double width = Double.parseDouble(this.getReqParam("tailorWidth"));
        String tailor = this.getReqParam("tailor");
        ImageCut cut = null;
        
        if(!tailor.equals("false")) {
            cut = JSON.parseObject(tailor, ImageCut.class);
        }

        String fileName=file.getOriginalFilename();
        String fileSuffix=fileName.substring(fileName.lastIndexOf('.')+1);
        fileName=brand+"."+fileSuffix;
        
        String filePath=rootPath+"/mirror/brand/"+brand+"/";
        File folder = new File(filePath);
        if (!folder.exists()){
            folder.mkdirs();
        }
        
        File f=new File(filePath,fileName);
        file.transferTo(f);
        
        if(CommonUtil.isNotBlank(cut)) {
        	ImageCutUtil.covertImage(f, width, cut);
        } else {
        	 ImageCutUtil.commpressPic(f.getPath(), f.getPath());
        }        
        return returnSuccessInfo(fileName,filePath+fileName);

	}
	
	@RequestMapping("/delimg")
	@ResponseBody
	public MessageBox deleteImg(String brand,String no) throws Exception{
		String rootPath=this.getSession().getServletContext().getRealPath("/");
        BrandInfo brandInfo = this.brandInfoService.load(brand);
        String imgs=brandInfo.getUrl();
        String img[]=imgs.split(",");
        imgs=null;
        ArrayList<String> ia=new ArrayList<String>();
        for (int i=0;i<img.length;i++) {
            ia.add(i,img[i]);
        }
        String filePath=rootPath;
        File file=new File(rootPath+ia.get(Integer.parseInt(no)));
        if(file.exists()&&file.isFile()) {
        	file.delete();
        }
        
        ia.remove(Integer.parseInt(no));
        for (int i = 0;i < ia.size();i++) {
            if(i == 0) {
                imgs=ia.get(i);
            } else {
                imgs+=","+ia.get(i);
            }

        }
        brandInfo.setUrl(imgs);
        this.brandInfoService.save(brandInfo);;
        return returnSuccessInfo(imgs);
    }
	
	@RequestMapping("/detail")
	@ResponseBody
	public ModelAndView brandInfoDetail(String brand){
		ModelAndView mav=new ModelAndView("/views/smart/brandInfo_detail");
		BrandInfo brandInfo=this.brandInfoService.findBrandBybrand(brand);
		mav.addObject("brandInfo",brandInfo);
		
		return mav;
	}
	
	@Override
	public List list() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MessageBox edit(String taskId) throws Exception {
			
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
