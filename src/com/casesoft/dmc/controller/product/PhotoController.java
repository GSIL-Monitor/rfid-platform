package com.casesoft.dmc.controller.product;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.FileUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.product.Photo;
import com.casesoft.dmc.service.product.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by session on 2017-05-04.
 */
@Controller
@RequestMapping(value = "/prod/photo")
public class PhotoController extends BaseController implements IBaseInfoController<Photo> {

	@Autowired
	private PhotoService photoService;


	@Override
	@RequestMapping(value = "/index")
	public String index() {
		return "/views/prod/photo";
	}


	@RequestMapping(value = "/page")
	@ResponseBody
	@Override
	public Page<Photo> findPage(Page<Photo> page) throws Exception {
		this.logAllRequestParams();
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
		page.setPageProperty();
		page = this.photoService.findPage(page, filters);
		if (page.getRows().size() > 0) {
			for (Photo p : page.getRows()) {
				if (CommonUtil.isNotBlank(CacheManager.getStyleById(p.getStyleId()))) {
					p.setStyleName(CacheManager.getStyleNameById(p.getStyleId()));
				}
				if (CommonUtil.isNotBlank(CacheManager.getUserById(p.getCreator()))) {
					p.setCreatorName(CacheManager.getUserById(p.getCreator()).getName());
				}
				if (CommonUtil.isNotBlank(CacheManager.getStyleById(p.getStyleId())))
				p.setPrice(CacheManager.getStyleById(p.getStyleId()).getPrice());
			}
		}
		return page;
	}

	@RequestMapping(value = "/list")
	@ResponseBody
	@Override
	public List<Photo> list() throws Exception {
		this.logAllRequestParams();
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
		List<Photo> photoList= this.photoService.find(filters);
		for(Photo p:photoList){
			p.setPrice(CacheManager.getStyleById(p.getStyleId()).getPrice());
		}
		return photoList;
	}

	@Override
	public MessageBox save(Photo entity) throws Exception {

		return null;
	}
//根据款色信息添加图片
	@RequestMapping(value = "/uploadPhoto")
	@ResponseBody
	public synchronized MessageBox uploadPhoto(MultipartFile file, String styleId, String colorId) throws Exception {
		this.logAllRequestParams();
		Photo photo = new Photo();
		String rootPath = this.getSession().getServletContext().getRealPath("/");
		String fileName = file.getOriginalFilename();
		String fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		String fileType = file.getContentType().split("/")[0];
		if (!"image".equals(fileType)) {
			return returnFailInfo("文件格式不支持");
		}
		photo.setId(new GuidCreator().toString());
		photo.setStyleId(styleId);
		photo.setColorId(colorId);
		photo.setCreator(this.getCurrentUser().getCode());
		photo.setCreateTime(new Date());
		photo.setSeqNo(this.photoService.getMaxSeq());
		fileName = photo.getId() + "." + fileSuffix;
		String filePath = rootPath + "/product/photo/" + styleId + "/" + colorId + "/";
		File folder = new File(filePath);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File f = new File(folder, fileName);
		file.transferTo(f);
		String url = "/" + styleId + "/" + colorId + "/" + fileName;
		photo.setSrc(url);
		this.photoService.save(photo);

		return returnSuccessInfo("上传成功", photo);
	}
	@RequestMapping(value = "/uplaodBatch2Photo")
	@ResponseBody
	public  MessageBox uploadBatch2Photoes(MultipartFile file){
		this.logAllRequestParams();
		Integer seqNo=this.photoService.getMaxSeq();
		Integer successCount=0,failCount=0;
		String errorInfo="";
		Boolean isPic=false;  //用于判断图片类型
		String rootPath =this.getSession().getServletContext().getRealPath("/");
		String fileName =file.getOriginalFilename();
		String fileType = file.getContentType().split("/")[0];
		String filePath = rootPath + "product/template/";
		String[] imageType = {"jpg", "jpeg", "png"};//过滤图片信息
		if (!"application".equals(fileType)) {
			return returnFailInfo("请上传图片压缩包!");
		}
		File folder = new File(filePath);
		FileUtil.deleteDir(folder);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File unZip = new File(folder, fileName);
		List<Photo> photoList = new ArrayList<Photo>();
		String path = unZip.getAbsolutePath();
		try{
			file.transferTo(unZip);
			FileUtil.unzip(unZip.getAbsolutePath(), filePath);
			String[] styleIds = folder.list();
			if(styleIds.length>0) {
				for (Integer i=0;i<styleIds.length;i++) {
					File pic = new File(folder, styleIds[i]);
					String picName=pic.getName();
					String styleId=picName.substring(0,picName.lastIndexOf(".")).split("_")[0];
					String picSuffix=picName.substring(picName.lastIndexOf(".")+1).toLowerCase();
					for(String suf:imageType){
						if(suf.equals(picSuffix)) {
							isPic = true;
							break;
						}
					}
					if(CommonUtil.isBlank(CacheManager.getStyleById(styleId))&&!"zip".equals(picSuffix)){
						errorInfo+="款"+styleId+"不存在("+picName+");";
						continue;
					}
					if("zip".equals(picSuffix)){
						continue;
					}
					if(isPic){
						isPic=false;
						Photo photo = new Photo();
						photo.setId(new GuidCreator().toString());
						photo.setStyleId(styleId);
						photo.setColorId("-");
						photo.setSrc("/" + photo.getStyleId() + "/-/" + photo.getId() + "." + picSuffix);
						photo.setSeqNo(seqNo++);
						photo.setCreator(this.getCurrentUser().getId());
						photo.setCreateTime(new Date());
						successCount++;

						File newFolder = new File(rootPath + "/product/photo/" + photo.getStyleId() + "/" + photo.getColorId());
						if(!newFolder.exists()){
							newFolder.mkdirs();
						}
						File newFilePath = new File(rootPath + "/product/photo/" + photo.getStyleId() + "/" + photo.getColorId(),photo.getId()+"."+picSuffix);

						boolean isMove = FileUtil.moveFile(pic,newFilePath.getAbsolutePath());
						if(!isMove){
							return returnFailInfo("上传失败,移动文件失败");
						}
						pic.renameTo(new File(photo.getId() + "." + picSuffix));
						photoList.add(photo);
					}
				}
			} else{
				return returnFailInfo("该压缩包内不含图片文件");
			}
			this.photoService.saveBatch(photoList);
			FileUtil.deleteDir(folder);

			if(successCount==styleIds.length-1){
				return returnSuccessInfo("成功上传"+successCount+"张图片");
			} else{
				return returnSuccessInfo("成功上传"+successCount+"/"+(styleIds.length-1)+"张图片;"+errorInfo);
			}
		}catch (Exception e){
			e.printStackTrace();
			return  returnFailInfo("上传失败,请检查压缩包格式");
		}
	}
//	批量上传款色图片
	@RequestMapping(value = "/uploadBatchPhoto")
	@ResponseBody
	public MessageBox uploadBatchPhotoes(MultipartFile file){
		this.logAllRequestParams();
		Integer seqNo=this.photoService.getMaxSeq();
		Integer count = 0, stylecount=0,colorcount=0,errCount = 0;//统计成功上传图片、失败图片、款式失败信息、颜色信息
		String rootPath = this.getSession().getServletContext().getRealPath("/");
		String fileName = file.getOriginalFilename();
		String fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		String fileType = file.getContentType().split("/")[0];
		String filePath = rootPath + "product/template/";
		String[] imageType = {"jpg", "jpeg", "png"};//过滤图片信息
		if (!"application".equals(fileType)) {
			return returnFailInfo("请上传图片压缩包!");
		}
		File folder = new File(filePath);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File unZip = new File(folder, fileName);
		List<Photo> photoList = new ArrayList<Photo>();
		String path = unZip.getAbsolutePath();
		String styleE="";
		String colorE="";
		String picE="";
		//遍历文件夹，规定为两级目录
		try {
			file.transferTo(unZip);
			FileUtil.unzip(unZip.getAbsolutePath(), filePath);
			String[] styleIds = folder.list();
			if (styleIds.length > 0) {
				for (Integer i = 0; i < styleIds.length; i++) {//遍历template目录
					File styleIdFolder = new File(folder, styleIds[i]);
					//	String[] styleprefixs=styleIdFolder.getAbsolutePath().toString().split(".");
					Integer styletype = styleIdFolder.getAbsolutePath().toString().substring(styleIdFolder.getAbsolutePath().toString().indexOf("product")).split("\\.").length;//根据后缀判断是否为文件夹，不是就跳过
					if (styletype != 1) {
//						styleE+=styleIds[i]+"不是文件夹"+styletype+";";
						errCount++;
						continue;
					}
					if (CommonUtil.isBlank(CacheManager.getStyleById(styleIds[i]))) {//可从此处判断是否存在款式
						styleE+="无款"+styleIds[i]+";";
						continue;
					}
					stylecount++;
					String[] colorIds = styleIdFolder.list();
					if (colorIds.length > 0) {
						for (Integer j = 0; j < colorIds.length; j++) {//遍历每一个styleId目录
							File colorIdFolder = new File(styleIdFolder, colorIds[j]);
							Integer colorType = colorIdFolder.getAbsolutePath().toString().substring(colorIdFolder.getAbsolutePath().toString().indexOf("product")).split("\\.").length;
							if (colorType != 1) {
								colorE+=colorIds[j]+"不是文件夹"+colorType+";";
								errCount++;
								continue;
							}
							if (CommonUtil.isBlank(CacheManager.getColorById(colorIds[j]))) {//判断是否存在颜色
									colorE+="无颜色"+colorIds[j]+";";
									continue;
							}
							colorcount++;
							String[] pictures = colorIdFolder.list();
							if (pictures.length > 0) {
								for (Integer k = 0; k < pictures.length; k++) {//遍历colorId目录
									Boolean judgeIsJmg = true;
									File pic = new File(colorIdFolder, pictures[k]);
									Integer picType = pic.getAbsolutePath().toString().substring(pic.getAbsolutePath().toString().indexOf("product")).split("\\.").length;//判断是否为文件
									if (picType != 2) {
										errCount++;
										picE+=pictures[k]+"不是图片;";
										continue;
									}
									String picprefix = pic.getAbsolutePath().toString().substring(pic.getAbsolutePath().toString().indexOf("product")).split("\\.")[1];
									for (Integer judge = 0; judge < imageType.length; judge++) {
										if (imageType[judge].equals(picprefix)) {
											judgeIsJmg = false;
											break;
										}
									}
									if (judgeIsJmg) {
											picE+=pictures[k]+"图片格式有误;";
											errCount++;
											continue;
									}

									Photo photo = new Photo();
									photo.setId(new GuidCreator().toString());
									photo.setStyleId(styleIds[i]);
									photo.setColorId(colorIds[j]);
									photo.setSrc("/" + styleIds[i] + "/" + colorIds[j] + "/" + photo.getId() + "." + picprefix);
									photo.setSeqNo(seqNo++);
									photo.setCreator(this.getCurrentUser().getId());
									photo.setCreateTime(new Date());
									count++;
									File newPic=new File(rootPath+"/product/photo/"+photo.getStyleId()+"/"+photo.getColorId()+"/",photo.getId()+"\\."+picprefix);
									File newFolder = new File(rootPath + "/product/photo/" + photo.getStyleId() + "/" + photo.getColorId());
									if(!newFolder.exists()){
										newFolder.mkdirs();
									}
									File newFilePath = new File(rootPath + "/product/photo/" + photo.getStyleId() + "/" + photo.getColorId(),photo.getId()+"."+picprefix);

									boolean isMove = FileUtil.moveFile(pic,newFilePath.getAbsolutePath());
									if(!isMove){
										return returnFailInfo("上传失败,移动文件失败");
									}
									pic.renameTo(new File(photo.getId() + "." + picprefix));
									photoList.add(photo);
								}
							}
						}
					}
				}
			} else {
				return returnFailInfo("该压缩包内不含图片文件");
			}
			this.photoService.saveBatch(photoList);
			FileUtil.deleteDir(folder);
			String errorString="";
			if(count==0)
				errorString+=",请检查文件款色是否存在";
			String successInfo="上传成功"+count+"张图片"+errorString;
			if("".equals(styleE)&&"".equals(colorE)&&"".equals(picE)){
				return  returnSuccessInfo(successInfo);
			} else {
				return returnSuccessInfo(successInfo+","+styleE+colorE+picE);
			}
		} catch (Exception e){
			return returnFailInfo("上传失败,请检查压缩包格式");
		}
	}

	@Override
	public MessageBox edit(String taskId) throws Exception {
		return null;
	}

	@RequestMapping(value = "/delPhoto")
	@ResponseBody
	@Override
	public MessageBox delete(String src) throws Exception {
		String rootPath = this.getSession().getServletContext().getRealPath("/");
		Photo photo = this.photoService.getPhotoByUrl(src);

		if (CommonUtil.isBlank(photo)) {
			return returnFailInfo("删除失败");
		}
		File file = new File(rootPath + "/product/photo" + src);

		String directory2 = src.substring(src.indexOf("/"), src.lastIndexOf("/"));
		String directory = src.substring(directory2.indexOf("/"), directory2.lastIndexOf("/"));

		File directoryparent1 = new File(rootPath + "/product/photo" + directory2);

		File directoryparent2 = new File(rootPath + "/product" + directory);
		try {
			if (file.exists() && file.isFile()) {
				file.delete();
				if (JudgeDirectoryIsNull(directoryparent1)) {
					directoryparent1.delete();
					if (JudgeDirectoryIsNull(directoryparent2)) {
						directoryparent2.delete();
					}
				}
			}
			this.photoService.delete(photo);
			return returnSuccessInfo("删除成功");
		} catch (Exception e) {
			return returnFailInfo("删除失败");
		}
	}

	public Boolean JudgeDirectoryIsNull(File f) {
		if (CommonUtil.isNotBlank(f)) {
			File[] files = f.listFiles();
			if (CommonUtil.isNotBlank(files) && files.length > 0) {
				return true;
			}
			return false;
		}
		return false;
	}

	@Override
	public void exportExcel() throws Exception {

	}

	@Override
	public MessageBox importExcel(MultipartFile file) throws Exception {
		return null;
	}


}
