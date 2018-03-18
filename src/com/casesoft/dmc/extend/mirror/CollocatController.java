package com.casesoft.dmc.extend.mirror;

import java.io.File;
import java.text.SimpleDateFormat;
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
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.mirror.Collocat;
import com.casesoft.dmc.model.mirror.NewProduct;
import com.casesoft.dmc.service.mirror.CollocatService;

@RequestMapping(value="/smart/collocat")
@Controller
public class CollocatController extends BaseController implements IBaseInfoController<Collocat>{
	@Autowired
	private CollocatService collocatService;
	
	
	@RequestMapping("/index")
	public String index(){
		return "/views/smart/collocat";
	}
	
	@RequestMapping("/page")
	@ResponseBody
	public Page<Collocat> findPage(Page<Collocat> page)throws Exception{
		this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page =this.collocatService.findPage(page, filters);
        return page;
	}
	
	@RequestMapping("/add")
	@ResponseBody
	public ModelAndView addStyle(){
		Integer seq=this.collocatService.findMaxSeqNofromCollocat();
		ModelAndView mav=new ModelAndView("/views/smart/collocat_edit");
		mav.addObject("pageType","add");
		mav.addObject("seqNo",seq+1);
		mav.addObject("id","");
		return mav;
	}

	
	@RequestMapping("/edit")
	@ResponseBody
	public ModelAndView editStyle(String id){
		ModelAndView mav =new ModelAndView("/views/smart/collocat_edit");
		Collocat collocat =this.collocatService.findCollocatById(id);
		mav.addObject("collocat",collocat);
		mav.addObject("pageType","edit");
		return mav;
	}

	@RequestMapping("/detail")
	@ResponseBody
	public ModelAndView detailStyle(String id){
		ModelAndView mav =new ModelAndView("/views/smart/collocat_detail");
		Collocat collocat =this.collocatService.findCollocatById(id);
		mav.addObject("collocat",collocat);
		return mav;
	}

	@Override
	public List list() throws Exception {
		return null;
	}

	@RequestMapping("/save")
	@ResponseBody
	public MessageBox save(Collocat collocat) throws Exception {
		this.logAllRequestParams();

		Collocat col=this.collocatService.findCollocatById(collocat.getId());
		if(CommonUtil.isNotBlank(col)) {
			col.setUpdateTime(new Date());
			col.setUpdater(this.getCurrentUser().getOwnerId());
			col.setIsShow(collocat.getIsShow());
			col.setPrice(collocat.getPrice());
			col.setRemark(collocat.getRemark());
			col.setUrl(collocat.getUrl());
		} else {
			col=new Collocat();
			col.setId(collocat.getId());
			col.setStyleIds(collocat.getStyleIds());
			col.setUpdateTime(new Date());
			col.setUpdater(this.getCurrentUser().getOwnerId());
			col.setIsShow(collocat.getIsShow());
			col.setSeqNo(collocat.getSeqNo());
			col.setPrice(collocat.getPrice());
			col.setRemark(collocat.getRemark());
			col.setUrl(collocat.getUrl());
		}
		try{
			this.collocatService.save(col);
			return returnSuccessInfo("保存成功");
		} catch(Exception e) {
			return returnFailInfo("保存失败");
		}
		
	}
	
	@RequestMapping("/saveColImages")
	@ResponseBody
	public MessageBox saveImage(MultipartFile file,String id)throws Exception{
		this.logAllRequestParams();

		//String rootPath =this.getRequest().getRealPath("/");
		String rootPath=this.getSession().getServletContext().getRealPath("/");
	        Double width = Double.parseDouble(this.getReqParam("tailorWidth"));
	        String tailor = this.getReqParam("tailor");
	        ImageCut cut = null;
	        
	        if(!tailor.equals("false")) {
	            cut = JSON.parseObject(tailor, ImageCut.class);
	        }
	        String fileName=file.getOriginalFilename();
	        String fileSuffix=fileName.substring(fileName.lastIndexOf('.')+1);
	        fileName=String.valueOf(System.currentTimeMillis())+"."+fileSuffix;
	       
	        String filePath=rootPath+"/mirror/collocat/"+id+"/";
	        File folder = new File(filePath);
	        if (!folder.exists()) {
	            folder.mkdirs();
	        }
	        
	        File f=new File(filePath,fileName);
	        file.transferTo(f);
	        
	        if(CommonUtil.isNotBlank(cut)) {
	        	ImageCutUtil.covertImage(f, width, cut);
	        } else {
	        	 ImageCutUtil.commpressPic(f.getPath(), f.getPath());
	        }        
	        return returnSuccessInfo(id+"/"+fileName,filePath+fileName);
	}
	
	@RequestMapping("/delimg")
	@ResponseBody
	public MessageBox deleteImg(String id,String no) throws Exception{
		String rootPath=this.getSession().getServletContext().getRealPath("/");
        Collocat collocat = this.collocatService.findCollocatById(id);
        String imgs=collocat.getUrl();
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
        for (int i = 0;i < ia.size(); i++) {
            if(i == 0){
                imgs = ia.get(i);
            }else{
                imgs += "," +ia.get(i);
            }

        }
        collocat.setUrl(imgs);
        this.collocatService.save(collocat);;
        return returnSuccessInfo(imgs);
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
