package com.casesoft.dmc.extend.mirror;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.mirror.MediaInfo;
import com.casesoft.dmc.model.mirror.NewProduct;
import com.casesoft.dmc.service.mirror.MediaInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/smart/media")
public class MediaInfoController extends BaseController implements IBaseInfoController<MediaInfo> {

    @Autowired
    private MediaInfoService mediaInfoService;

    @RequestMapping(value = "/index")
    @Override
    public String index() {
         return "views/smart/mediaInfo";
    }

    @RequestMapping(value = "/page")
    @ResponseBody
    @Override
    public Page<MediaInfo> findPage(Page<MediaInfo> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page=this.mediaInfoService.findPage(page,filters);
        return page;
    }

    @RequestMapping(value = "/edit")
    @ResponseBody
    public ModelAndView findById(String id){
        this.logAllRequestParams();
        MediaInfo mediaInfo = this.mediaInfoService.findById(id);
        ModelAndView model = new ModelAndView();
        model.addObject("mediaInfo", mediaInfo);
        model.setViewName("/views/smart/mediaInfo_edit");
        return model;
    }

    @RequestMapping(value = "/saveVideo")
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
        String fileParant=rootPath + "/mirror/media/"+id+"/";
        File folder = new File(fileParant);
        if (!folder.exists()){
            folder.mkdirs();
        }
        File f = new File(folder,fileName);
        file.transferTo(f);
        String result=id+"/"+fileName;
        MediaInfo mediaInfo=this.mediaInfoService.findById(id);
        mediaInfo.setUrl(result);
        this.mediaInfoService.save(mediaInfo);
        return returnSuccessInfo(result);
    }

    @RequestMapping(value = "/save")
    @ResponseBody
    @Override
    public MessageBox save(MediaInfo mediaInfo) throws Exception {
        MediaInfo mi=this.mediaInfoService.findById(mediaInfo.getId());
        if (CommonUtil.isBlank(mi)){
            mi=new MediaInfo();
            mi.setSeqNo(this.mediaInfoService.findMaxSeqNo()+1);
        }
        mi.setRemark(mediaInfo.getRemark());
        mi.setId(mediaInfo.getId());
        mi.setName(mediaInfo.getName());
        mi.setShowArea(mediaInfo.getShowArea());
        mi.setIsShow(mediaInfo.getIsShow());
        mi.setUpdateTime(new Date());
        mi.setUrl(mediaInfo.getUrl());
        mi.setUpdater(this.getCurrentUser().getOwnerId());
        this.mediaInfoService.save(mi);
        return returnSuccessInfo("保存成功");
    }

    @RequestMapping(value = "/deleteVideo")
    @ResponseBody
    public MessageBox deleteVideo(String url)throws Exception{
        try {
            String sPath = this.getSession().getServletContext().getRealPath("/") +"/mirror/media/"+url;
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
    public List<MediaInfo> list() throws Exception {
        return null;
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
