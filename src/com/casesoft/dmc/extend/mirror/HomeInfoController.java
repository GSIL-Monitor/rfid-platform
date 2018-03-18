package com.casesoft.dmc.extend.mirror;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.mirror.HomeInfo;
import com.casesoft.dmc.model.mirror.MediaInfo;
import com.casesoft.dmc.model.mirror.NewProduct;
import com.casesoft.dmc.service.mirror.HomeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/smart/home")
public class HomeInfoController extends BaseController implements IBaseInfoController<HomeInfo> {

    @Autowired
    private HomeInfoService homeInfoService;

    @RequestMapping(value = "/index")
    @Override
    public String index() {
        return "views/smart/homeInfo";
    }

    @RequestMapping(value = "/page")
    @ResponseBody
    @Override
    public Page<HomeInfo> findPage(Page<HomeInfo> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page=this.homeInfoService.findPage(page,filters);
        return page;
    }

    @RequestMapping(value = "/edit")
    @ResponseBody
    public ModelAndView findById(String id){
        this.logAllRequestParams();
        HomeInfo homeInfo = this.homeInfoService.findById(id);
        ModelAndView model = new ModelAndView();
        model.addObject("homeInfo",homeInfo);
        model.setViewName("/views/smart/homeInfo_edit");
        return model;
    }

    @RequestMapping(value = "saveVideo")
    @ResponseBody
    public MessageBox saveVideo(MultipartFile file,String id) throws Exception{
        this.logAllRequestParams();
        String rootPath = this.getSession().getServletContext().getRealPath("/");
        String fileName = file.getOriginalFilename();
        String fileSuffix=fileName.substring(fileName.lastIndexOf(".")+1);
        if (!fileSuffix.equals("mp4")){
            returnFailInfo("请上传MP4格式视频");
        }
        fileName=String.valueOf(System.currentTimeMillis())+"."+fileSuffix;
        String fileParant=rootPath + "/mirror/home/"+id+"/";
        File folder = new File(fileParant);
        if (!folder.exists()){
            folder.mkdirs();
        }
        File f = new File(folder,fileName);
        file.transferTo(f);
        String result=id+"/"+fileName;
        HomeInfo homeInfo=this.homeInfoService.findById(id);
        homeInfo.setUrl(result);
        this.homeInfoService.save(homeInfo);
        return returnSuccessInfo(result);
    }

    @Override
    public List<HomeInfo> list() throws Exception {
        return null;
    }

    @RequestMapping(value = "/save")
    @ResponseBody
    @Override
    public MessageBox save(HomeInfo homeInfo) throws Exception {
        HomeInfo hi=this.homeInfoService.findById(homeInfo.getId());
        if (CommonUtil.isBlank(hi)){
            hi=new HomeInfo();
            hi.setCreator(this.getCurrentUser().getOwnerId());
            hi.setCreateTime(new Date());
            hi.setSeqNo(this.homeInfoService.findMaxSeqNo()+1);
        }
        hi.setId(homeInfo.getId());
        hi.setName(homeInfo.getName());
        hi.setUrl(homeInfo.getUrl());
        hi.setFileType(homeInfo.getFileType());
        hi.setUpdater(this.getCurrentUser().getOwnerId());
        hi.setUpdateTime(new Date());
        hi.setRemark(homeInfo.getRemark());

        this.homeInfoService.save(hi);
        return returnSuccessInfo("保存成功");
    }

    @RequestMapping(value = "/saveImages")
    @ResponseBody
    public MessageBox saveImages(MultipartFile file,String id) throws Exception{
        this.logAllRequestParams();

        String rootPath = this.getSession().getServletContext().getRealPath("/");
        Double width = Double.parseDouble(this.getReqParam("tailorWidth")); //图片缩放所需宽度
        String tailor = this.getReqParam("tailor");
        ImageCut cut = null;
        if(!tailor.equals("false")){
            cut = JSON.parseObject(tailor, ImageCut.class);
        }

        //重命名
        String fileName = file.getOriginalFilename();
        String fileSuffix=fileName.substring(fileName.lastIndexOf(".")+1);
        fileName=String.valueOf(System.currentTimeMillis())+"."+fileSuffix;

        String fileParant=rootPath + "/mirror/home/"+id+"/";
        File folder = new File(fileParant);
        if (!folder.exists()){
            folder.mkdirs();
        }
        File f = new File(folder,fileName);
        file.transferTo(f);
        //图片裁剪
        if(CommonUtil.isNotBlank(cut)){
            ImageCutUtil.covertImage(f, width, cut);
        }else{
            ImageCutUtil.commpressPic(f.getPath(), f.getPath());
        }
        String url=id+"/"+fileName;
        return returnSuccessInfo(url,fileParant+fileName);
    }

    @RequestMapping(value = "/deleteImg")
    @ResponseBody
    public MessageBox deleteImg(String Id,String no) throws Exception{
        HomeInfo homeInfo = this.homeInfoService.findById(Id);
        //删除图片地址
        String imgs=homeInfo.getUrl();
        String img[]=imgs.split(",");
        imgs=null;
        ArrayList<String> ia=new ArrayList<String>();
        for (int i=0;i<img.length;i++){
            ia.add(i,img[i]);
        }
        ia.remove(Integer.parseInt(no));
        for (int i=0;i<ia.size();i++){
            if(i==0){
                imgs=ia.get(i);
            }else{
                imgs+=","+ia.get(i);
            }

        }
        homeInfo.setUrl(imgs);
        //删除图片
        try {
            String sPath = this.getSession().getServletContext().getRealPath("/") + "/mirror/home/" +img[Integer.parseInt(no)];
            File file = new File(sPath);
            // 路径为文件且不为空则进行删除
            if (file.isFile() && file.exists()) {
                file.delete();
            }
        }catch (Exception e){
            returnFailInfo("删除文件失败");
        }

        this.homeInfoService.save(homeInfo);
        return returnSuccessInfo(imgs);
    }

    @RequestMapping(value = "/deleteVideo")
    @ResponseBody
    public MessageBox deleteVideo(String url)throws Exception{
        try {
            String sPath = this.getSession().getServletContext().getRealPath("/")+"/mirror/home/"+url;
            File file = new File(sPath);
            // 路径为文件且不为空则进行删除
            if (file.isFile() && file.exists()) {
                file.delete();
            }
        }catch (Exception e) {
            returnFailInfo("删除文件失败");
        }
        return returnSuccessInfo("删除成功");
    }

    @Override
    public MessageBox edit(String taskId) throws Exception {
        return null;
    }

    @Override
    public MessageBox delete(String taskId) throws Exception {
        return null;
    }

    @Override
    public void exportExcel() throws Exception {

    }

    @Override
    public MessageBox importExcel(MultipartFile file) throws Exception {
        return null;
    }
}
