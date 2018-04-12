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
import com.casesoft.dmc.model.product.vo.ColorVo;
import com.casesoft.dmc.model.product.vo.SizeVo;
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
import java.util.*;

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

    @Autowired
    private ProductService productService;

    @Override
    public String index() {
        return null;
    }

    @RequestMapping("/getStyleListWS.do")
    @ResponseBody
    public Page<Style> getStyleList(Page<Style> page)throws Exception{
        logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        page.setSort("updateTime");
        page.setOrder("desc");
        page.setPageProperty();
        page = this.styleService.findPage(page, filters);
        String rootPath = this.getSession().getServletContext().getRealPath("/");
        for(Style d : page.getRows()){
            String imgUrl = ImgUtil.fetchImgUrl(d.getStyleId(), rootPath);
            d.setUrl(imgUrl);
            //PropertyKey propertyKey = this.propertyService.findPropertyKeyBytypeAndCode(d.getClass1());
            //d.setClass1Name(propertyKey.getName());
            PropertyKey propertyKey = CacheManager.getPropertyKey("C1-" + d.getClass1());
            if(CommonUtil.isNotBlank(propertyKey)){
                d.setClass1Name(propertyKey.getName());
            }else{
                d.setClass1Name("");
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
    public MessageBox saveStyleWS(String styleStr, String colorStr, String sizeStr, String userId) throws Exception {
        logAllRequestParams();
        try {
            Style styleDTO=JSON.parseObject(styleStr,Style.class);
            Style sty = CacheManager.getStyleById(styleDTO.getStyleId());
            if(CommonUtil.isBlank(sty)){
                sty=new Style();
                sty.setId(styleDTO.getStyleId());
                sty.setStyleId(styleDTO.getStyleId());
                sty.setIsUse("Y");
            }
            List<ColorVo> colorVoList = JSON.parseArray(colorStr, ColorVo.class);
            List<SizeVo> sizeVoList = JSON.parseArray(sizeStr, SizeVo.class);
            List<Product> productList = new ArrayList<>();
            for(ColorVo colorVo: colorVoList){
                for(SizeVo sizeVo: sizeVoList){
                    Product product = new Product();
                    product.setCode(styleDTO.getStyleId()+colorVo.getId()+sizeVo.getId());
                    product.setColorId(colorVo.getId());
                    product.setSizeId(sizeVo.getId());
                    productList.add(product);
                }
            }
            sty.setOprId(userId);
            List<Product> saveList = StyleUtil.covertToProductInfo(sty,styleDTO,productList);

            this.styleService.saveStyleAndProducts(sty,saveList);
            long time1=System.currentTimeMillis();
            CacheManager.refreshStyleCache();
            long time2=System.currentTimeMillis();
            System.out.println("刷新款式缓存时间："+ (time2-time1) +"ms");
            CacheManager.refreshProductCache();
            long time3=System.currentTimeMillis();
            System.out.println("刷新商品缓存时间："+ (time3-time2) +"ms");

            return this.returnSuccessInfo("保存成功", styleStr);
        }catch(Exception e ){
            return this.returnFailInfo("保存失败");
        }
    }

    @RequestMapping("/getStyleByIdWS.do")
    @ResponseBody
    public MessageBox getStyleByIdWS(String styleId) throws Exception{
        Style s = CacheManager.getStyleById(styleId);
        String rootPath = this.getSession().getServletContext().getRealPath("/");
        String imgUrl = ImgUtil.fetchImgUrl(styleId, rootPath);
        s.setUrl(imgUrl);
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
        page.setSort("colorName");
        page.setOrder("asc");
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

    /**
     * add by yushen
     * 查找所有颜色尺寸，不分页
     */
    @RequestMapping(value = "/listColorAndSize")
    @ResponseBody
    public Map<String, Object> listColorAndSize() throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        List<PropertyFilter> colorFilters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        Map<String, String> colorSortMap = new HashMap<>();
        colorSortMap.put("colorName", "asc");

        List<PropertyFilter> sizeFilters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        Map<String, String> sizeSortMap = new HashMap<>();
        sizeSortMap.put("seqNo", "asc");

        List<Color> colors = this.colorService.find(colorFilters, colorSortMap);
        List<Size> sizes = this.sizeService.find(sizeFilters, sizeSortMap);
        map.put("colors", colors);
        map.put("sizes", sizes);
        return map;
    }

    /**
     * add by yushen
     * 查找款所对应商品的所有颜色尺寸
     */
    @RequestMapping(value = "/listColorAndSizeByStyleId")
    @ResponseBody
    public Map<String, Object> listColorAndSizeByStyleId(String styleId) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        List<ColorVo> colorVoList = this.productService.getColorsByStyleId(styleId);
        List<SizeVo> sizeVoList = this.productService.getSizesByStyleId(styleId);

        map.put("colorVoList", colorVoList);
        map.put("sizeVoList", sizeVoList);
        return map;
    }
}
