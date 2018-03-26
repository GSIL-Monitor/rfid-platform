package com.casesoft.dmc.extend.api.wechat;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.product.StyleUtil;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.ImgUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.util.secret.EpcSecretUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.api.web.ApiBaseController;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.product.*;
import com.casesoft.dmc.model.search.DetailStockChatView;
import com.casesoft.dmc.model.tag.Epc;
import com.casesoft.dmc.service.cfg.PropertyService;
import com.casesoft.dmc.service.product.*;
import com.casesoft.dmc.service.stock.EpcStockService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value = "/api/wx/product")
@Api(description = "微信小程序接口")
public class WXProductApiController extends ApiBaseController {

    @Autowired
    private StyleService styleService;

    @Autowired
    private SizeService sizeService;

    @Autowired
    private ColorService colorService;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private EpcStockService epcStockService;

    @Autowired
    private PhotoService photoService;
    @Autowired
    private CustomerPhotoService customerPhotoService;

    @Override
    public String index() {
        return null;
    }

    @RequestMapping("/getStyleListWS.do")
    @ResponseBody
    public Page<Style> getStyleList(Page<Style> page)throws Exception{
        logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        page.setPageProperty();

        page = this.styleService.findPage(page, filters);
        String rootPath = this.getSession().getServletContext().getRealPath("/");
        for(Style d : page.getRows()){
            File file =  new File(rootPath + "/product/photo/" + d.getStyleId());
            if(file.exists()){
                File[] files = file.listFiles();
                if(files.length > 0){
                    File[] photos = files[0].listFiles();
                    if(photos.length > 0){
                        d.setUrl("/product/photo/" + d.getStyleId()+"/"+files[0].getName()+"/"+photos[0].getName());
                    }
                }
            }else{
                d.setUrl("/imgs/noImg.png");
            }
        }
        return page;
    }

    @RequestMapping(value = "/findStyleList.do")
    @ResponseBody
    public MessageBox findStyleList(String pageSize,String pageNo,String sortName,String order){
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        Page<Style> page = new Page<Style>(Integer.parseInt(pageSize));
        page.setPage(Integer.parseInt(pageSize));
        page.setPageNo(Integer.parseInt(pageNo));
        if(CommonUtil.isNotBlank(sortName)){
            page.setOrderBy(sortName);
        }
        if(CommonUtil.isNotBlank(order)){
            page.setOrder(order);
        }
        page = this.styleService.findPage(page,filters);
        String rootPath = this.getSession().getServletContext().getRealPath("/");
        for(Style d : page.getRows()){
            File file =  new File(rootPath + "/product/photo/" + d.getStyleId());
            if(file.exists()){
                File[] files = file.listFiles();
                if(files.length > 0){
                    File[] photos = files[0].listFiles();
                    if(photos.length > 0){
                        d.setUrl("/product/photo/" + d.getStyleId()+"/"+files[0].getName()+"/"+photos[0].getName());
                    }
                }
            }
        }
        return this.returnSuccessInfo("获取成功",page.getRows());
    }

    @RequestMapping("/saveStyleWS.do")
    @ResponseBody
    public MessageBox saveStyleWS(String styleStr) throws Exception {
        logAllRequestParams();
        Style styleDTO=JSON.parseObject(styleStr,Style.class);
        Style sty =this.styleService.fundByStyleId(styleDTO.getStyleId());
        if(CommonUtil.isBlank(sty)){
            sty=new Style();
            sty.setId(styleDTO.getStyleId());
            sty.setStyleId(styleDTO.getStyleId());
        }
        StyleUtil.copyStyleInfo(sty,styleDTO);
        try {
            this.styleService.save(sty);
            CacheManager.refreshStyleCache();
            return this.returnSuccessInfo("保存成功", styleStr);
        }catch(Exception e ){
            return this.returnFailInfo("保存失败");
        }
    }

    @RequestMapping("/getStyleByIdWS.do")
    @ResponseBody
    public MessageBox getStyleByIdWS(String styleId) throws Exception{
        Style s = CacheManager.getStyleById(styleId);
        return this.returnSuccessInfo("ok", s);
    }

    @RequestMapping("/findProductByUniqueCode.do")
    @ResponseBody
    public MessageBox findProductByUniqueCode(String code) throws Exception{
        this.logAllRequestParams();
        if (code.length() > 13) {
            String epcCode = code.toUpperCase();
            code = EpcSecretUtil.decodeEpc(epcCode).substring(0, 13);
        }else if(code.length() < 13){
            return new MessageBox(false,"唯一码信息错误");
        }
        Epc tagEpc = this.epcStockService.findTagEpcByCode(code);
        if(CommonUtil.isNotBlank(tagEpc)){
            return new MessageBox(true,"",tagEpc.getStyleId());
        }else{
            return new MessageBox(false,"唯一码信息错误");
        }
    }

    @RequestMapping(value = "/sizeSortPageWS.do")
    @ResponseBody
    public Page<SizeSort> getSizeSortpageWS(Page<SizeSort> page){
        this.logAllRequestParams();
        page.setPageProperty();
        List<PropertyFilter> filters=PropertyFilter.buildFromHttpRequest(this.getRequest());
        page=this.sizeService.find(page,filters);
        return  page;

    }

    @RequestMapping(value = "/findSizeSortListWS.do")
    @ResponseBody
    public List<SizeSort> getSortList(){
        this.logAllRequestParams();
        List<SizeSort> sizeSortList=this.sizeService.getAllSort();

        return sizeSortList;

    }

    @RequestMapping(value = "/findSizeListWS.do")
    @ResponseBody
    public Page<Size> findSizeListWS(Page<Size> page)throws Exception{
        this.logAllRequestParams();
        page.setPageProperty();
        List<PropertyFilter> filters=PropertyFilter.buildFromHttpRequest(this.getRequest());
        return this.sizeService.findPage(page,filters);
    }

    @RequestMapping(value = "/getSizeInfoWS.do")
    @ResponseBody
    public Size findSizeBySizeId(String sizeId){
        this.logAllRequestParams();
        Size size=this.sizeService.findSizeBySizeId(sizeId);
        return size;
    }

    @RequestMapping(value = "/saveSizeWS.do")
    @ResponseBody
    public MessageBox saveSizeWS(String sizeInfo){
        this.logAllRequestParams();
        Size size=JSON.parseObject(sizeInfo,Size.class);
        Size s = CacheManager.getSizeById(size.getSizeId());
        if (CommonUtil.isBlank(s)) {
            s = new Size();
            s.setId(size.getSizeId());
            Integer maxSeqNo = this.sizeService.findMaxSeqNoInSizeBySortId(size.getSortId());
            s.setSeqNo(maxSeqNo);
            s.setIsUse("Y");
        }
        s.setOprId("weChat");
        s.setSizeName(size.getSizeName());
        s.setSizeId(size.getSizeId());
        s.setSortId(size.getSortId());
        s.setUpdateTime(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        try {
            this.sizeService.save(s);
            CacheManager.refreshSizeCache();
            return returnSuccessInfo("ok");
        } catch (Exception e) {
            e.printStackTrace();
            return  returnFailInfo("fail");
        }
    }


    @RequestMapping(value = "/findColorListWS.do")
    @ResponseBody
    public  Page<Color> findColorListWS(Page<Color> page){
            this.logAllRequestParams();

            page.setPageProperty();
            List<PropertyFilter> filters =PropertyFilter.buildFromHttpRequest(this.getRequest());
            return this.colorService.findPage(page,filters);
    }


    @RequestMapping(value = "/getColorInfo.do")
    @ResponseBody
    public Color findColorByColorNo(String colorId){
        this.logAllRequestParams();
        Color color=this.colorService.findById(colorId);
        return color;
    }


    @RequestMapping(value = "/saveColorWS.do")
    @ResponseBody
    public MessageBox saveColor(String colorInfo){
        this.logAllRequestParams();
        Color color= JSON.parseObject(colorInfo,Color.class);
        Color col = CacheManager.getColorById(color.getColorId());
        if (CommonUtil.isBlank(col)) {
            col = new Color(color.getColorId(),color.getColorId(),color.getColorName());
            col.setIsUse("Y");
        }
//        User u = getCurrentUser();
        col.setOprId("weChat");
        col.setColorName(color.getColorName());
        col.setHex(color.getHex());
        col.setUpdateTime(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        this.colorService.save(col);
        CacheManager.refreshColorCache();
        return returnSuccessInfo("ok");
    }

    @RequestMapping(value="/searchByTypeWS")
    @ResponseBody
    public List<List<PropertyKey>> searchByTypesWS(String[] typeList) throws Exception {
        this.logAllRequestParams();
        List<List<PropertyKey>> pkList = this.propertyService.getPropertyKeyByTypes(typeList);
        return pkList;
    }

    /**
     * 微信小程序上传图片测试*/

    @RequestMapping("/picture")
    public void uploadPicture(HttpServletRequest request,HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");  //设置编码
        String rootPath = request.getSession().getServletContext().getRealPath("");
        String styleId = getReqParam("styleId");
        String userId = getReqParam("userId");
        String path = rootPath + "product\\template\\";
        System.out.println("上传的图片路径:"+path);
        Integer seqNo=this.photoService.getMaxSeq();
        PrintWriter printWriter = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        HashMap<String, Object> res = new HashMap<String, Object>();
        try {
            //可以上传多个文件
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            List<MultipartFile> fileList = multipartRequest.getFiles("file");
            for(MultipartFile file : fileList){
                String fileName = file.getOriginalFilename();
                String picprefix=fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();
                String fileType = file.getContentType().split("/")[0];
                if (!"image".equals(fileType)) {
                   /* return new MessageBox(false,"文件格式错误,请传图片");*/
                    res.put("success", false);
                    res.put("msg","文件格式错误,请传图片");


                }
                Photo photo = new Photo();
                photo.setId(new GuidCreator().toString());
                photo.setStyleId(styleId);
                photo.setColorId("-");
                photo.setSrc("/" + styleId + "/-/" + photo.getId() + "." + picprefix);
                photo.setSeqNo(seqNo++);
                photo.setCreator(userId);
                photo.setCreateTime(new Date());
                String filePath = rootPath + "/product/photo/" + styleId + "/-/";
                File folder = new File(filePath);
                if (!folder.exists()) {
                    folder.mkdirs();
                }
                File f = new File(folder, photo.getId()+ "." + picprefix);
                file.transferTo(f);
                String url = "/" + styleId + "/-/" +  photo.getId()+ "." + picprefix;
                photo.setSrc(url);
                ImgUtil.img_change(filePath,photo.getId()+ "." + picprefix);
                this.photoService.save(photo);
                res.put("success", true);
                res.put("msg",url);
            }
        }catch (Exception e) {
            e.printStackTrace();
            res.put("success", false);
            res.put("msg","保存失败");
        }
        printWriter.write(JSON.toJSONString(res));
        printWriter.flush();
    }
    /**
     * 微信小程序客户图像上传图片测试*/

    @RequestMapping("/customerPicture")
    public void uploadCustomerPicture(HttpServletRequest request,HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");  //设置编码
        String rootPath = request.getSession().getServletContext().getRealPath("");
        String unionid = getReqParam("unionid");
        //String userId = getReqParam("userId");
        String path = rootPath + "product\\template\\";
        System.out.println("上传的图片路径:"+path);
        Integer seqNo=this.photoService.getMaxSeq();
        PrintWriter printWriter = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        HashMap<String, Object> res = new HashMap<String, Object>();
        try {
            //可以上传多个文件
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            List<MultipartFile> fileList = multipartRequest.getFiles("file");
            for(MultipartFile file : fileList){
                String fileName = file.getOriginalFilename();
                String picprefix=fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();
                String fileType = file.getContentType().split("/")[0];
                if (!"image".equals(fileType)) {
                   /* return new MessageBox(false,"文件格式错误,请传图片");*/
                    res.put("success", false);
                    res.put("msg","文件格式错误,请传图片");


                }
                CustomerPhoto photo = new CustomerPhoto();
                photo.setId(new GuidCreator().toString());
                photo.setUnionid(unionid);
                photo.setColorId("-");
                photo.setSrc("/" + unionid + "/-/" + photo.getId() + "." + picprefix);
                photo.setSeqNo(seqNo++);
                //photo.setCreator(userId);
                photo.setCreateTime(new Date());
                String filePath = rootPath + "/product/photo/customer/" + unionid + "/-/";
                File folder = new File(filePath);
                if (!folder.exists()) {
                    folder.mkdirs();
                }
                File f = new File(folder, photo.getId()+ "." + picprefix);
                file.transferTo(f);
                String url = "/" + unionid + "/-/" +  photo.getId()+ "." + picprefix;
                photo.setSrc(url);
                ImgUtil.img_change(filePath,photo.getId()+ "." + picprefix);
                this.customerPhotoService.save(photo);
                res.put("success", true);
                res.put("msg",url);
            }
        }catch (Exception e) {
            e.printStackTrace();
            res.put("success", false);
            res.put("msg","保存失败");
        }
        printWriter.write(JSON.toJSONString(res));
        printWriter.flush();
    }
}
