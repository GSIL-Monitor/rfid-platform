package com.casesoft.dmc.extend.mirror;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.mirror.ActivityInfo;
import com.casesoft.dmc.service.mirror.ActivityInfoService;
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
@RequestMapping("/smart/activity")
public class ActivityInfoController extends BaseController implements IBaseInfoController<ActivityInfo> {

    @Autowired
    private ActivityInfoService activityInfoService;

    @RequestMapping(value = "/index")
    @Override
    public String index() {
        return  "/views/smart/activityInfo";
    }

    @RequestMapping(value = "/page")
    @ResponseBody
    @Override
    public Page<ActivityInfo> findPage(Page<ActivityInfo> page) throws Exception {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page=this.activityInfoService.findPage(page,filters);
        return page;
    }

    @RequestMapping(value = "/edit")
    @ResponseBody
    public ModelAndView findByStyleId(String id){
        this.logAllRequestParams();
        ActivityInfo activityInfo = this.activityInfoService.findById(id);
        ModelAndView model = new ModelAndView();
        model.addObject("activityInfo", activityInfo);
        model.setViewName("/views/smart/activityInfo_edit");
        return model;
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

        String fileParant=rootPath + "/mirror/activity/"+id+"/";
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
    @Override
    public List<ActivityInfo> list() throws Exception {
        return null;
    }

    @RequestMapping(value = "/save")
    @ResponseBody
    @Override
    public MessageBox save(ActivityInfo activityInfo) throws Exception {
        ActivityInfo ai=this.activityInfoService.findById(activityInfo.getId());
        if (CommonUtil.isBlank(ai)){//add
            ai=new ActivityInfo();
            ai.setSeqNo(this.activityInfoService.findMaxSeqNo()+1);
        }
        ai.setId(activityInfo.getId());
        ai.setRemark(activityInfo.getRemark());
        ai.setActivityTime(activityInfo.getActivityTime());
        ai.setUrl(activityInfo.getUrl());
        ai.setIsShow(activityInfo.getIsShow());
        ai.setUpdater(this.getCurrentUser().getOwnerId());
        ai.setUpdateTime(new Date());
        this.activityInfoService.save(ai);
        return  returnSuccessInfo("保存成功");
    }

    @RequestMapping(value = "/deleteImg")
    @ResponseBody
    public MessageBox deleteImg(String Id,String no) throws Exception{
        ActivityInfo activityInfo = this.activityInfoService.findById(Id);
        //删除图片地址
        String imgs=activityInfo.getUrl();
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
        activityInfo.setUrl(imgs);
        //删除图片
        try {
            String sPath = this.getSession().getServletContext().getRealPath("/") + "/mirror/activity/" +img[Integer.parseInt(no)];
            File file = new File(sPath);
            // 路径为文件且不为空则进行删除
            if (file.isFile() && file.exists()) {
                file.delete();
            }
        }catch (Exception e){
            returnFailInfo("删除文件失败");
        }
        this.activityInfoService.save(activityInfo);
        return returnSuccessInfo(imgs);
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
