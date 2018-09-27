package com.casesoft.dmc.extend.api.wechat;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.pad.templatemsg.WechatTemplate;
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
import com.casesoft.dmc.model.cfg.PropertyType;
import com.casesoft.dmc.model.product.*;
import com.casesoft.dmc.model.product.vo.ColorVo;
import com.casesoft.dmc.model.product.vo.SizeVo;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.model.tag.Epc;
import com.casesoft.dmc.service.cfg.PropertyService;
import com.casesoft.dmc.service.pad.WeiXinUserService;
import com.casesoft.dmc.service.product.*;
import com.casesoft.dmc.service.stock.EpcStockService;
import com.casesoft.dmc.service.sys.KeyInfoChangeService;
import com.casesoft.dmc.service.sys.impl.PricingRulesService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.PrintWriter;
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

    @Autowired
    private PricingRulesService pricingRulesService;
    @Autowired
    private KeyInfoChangeService keyInfoChangeService;
    @Autowired
    public WeiXinUserService weiXinUserService;
    @Override
    public String index() {
        return null;
    }

    @RequestMapping("/getStyleListWS.do")
    @ResponseBody
    public Page<Style> getStyleList(Page<Style> page) throws Exception {
        logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        page.setSort("updateTime");
        page.setOrder("desc");
        page.setPageProperty();
        page = this.styleService.findPage(page, filters);
        String rootPath = this.getSession().getServletContext().getRealPath("/");
        for (Style d : page.getRows()) {
            String imgUrl = StyleUtil.returnImageUrl(d.getStyleId(), rootPath);
            d.setUrl(imgUrl);
            //PropertyKey propertyKey = this.propertyService.findPropertyKeyBytypeAndCode(d.getClass1());
            //d.setClass1Name(propertyKey.getName());
            PropertyKey propertyKey = CacheManager.getPropertyKey("C1-" + d.getClass1());
            if (CommonUtil.isNotBlank(propertyKey)) {
                d.setClass1Name(propertyKey.getName());
            } else {
                d.setClass1Name("");
            }
        }
        return page;
    }

    @RequestMapping(value = "/findStyleList.do")
    @ResponseBody
    public MessageBox findStyleList(String pageSize, String pageNo, String sortName, String order) {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        Page<Style> page = new Page<Style>(Integer.parseInt(pageSize));
        page.setOrderBy("updateTime");
        page.setOrder("desc");
        page.setPage(Integer.parseInt(pageSize));
        page.setPageNo(Integer.parseInt(pageNo));
        if (CommonUtil.isNotBlank(sortName)) {
            page.setOrderBy(sortName);
        }
        if (CommonUtil.isNotBlank(order)) {
            page.setOrder(order);
        }
        page = this.styleService.findPage(page, filters);
        String rootPath = this.getSession().getServletContext().getRealPath("/");
        for (Style d : page.getRows()) {
           /* File file =  new File(rootPath + "/product/photo/" + d.getStyleId());
            if(file.exists()){
                File[] files = file.listFiles();
                if(files.length > 0){
                    File[] photos = files[0].listFiles();
                    if(photos.length > 0){
                        d.setUrl("/product/photo/" + d.getStyleId()+"/"+files[0].getName()+"/"+photos[0].getName());
                    }
                }
            }*/
            String url = StyleUtil.returnImageUrl(d.getStyleId(), rootPath);
            d.setUrl(url);
        }
        return this.returnSuccessInfo("获取成功", page.getRows());
    }

    /**
     * @param styleStr
     * @param colorStr
     * @param sizeStr
     * @param userId
     * @param pageType 保存类型： add edit
     * @return
     * @throws Exception
     */
    @RequestMapping("/saveStyleWS.do")
    @ResponseBody
    public MessageBox saveStyleWS(HttpServletRequest request, String styleStr, String colorStr, String sizeStr, String userId, String pageType) {
        logAllRequestParams();
        try {
            HashMap<String, Object> prePriceMap = new HashMap<>();
            Style styleDTO = JSON.parseObject(styleStr, Style.class);
            Style sty = CacheManager.getStyleById(styleDTO.getStyleId());
            if ("add".equals(pageType)) {
                //判断sytleId在数据库中是否存在
                if (CommonUtil.isBlank(sty)) {
                    sty = new Style();
                    sty.setId(styleDTO.getStyleId());
                    sty.setStyleId(styleDTO.getStyleId());
                    sty.setIsUse("Y");
                } else {
                    return this.returnFailInfo("新增保存失败!" + sty.getId() + "款号已存在，请换一个款号保存");
                }
            } else if ("edit".equals(pageType)) {
                if (CommonUtil.isBlank(sty)) {
                    return this.returnFailInfo("编辑失败!" + sty.getId() + "款号不存在");
                }
                prePriceMap.put("price", sty.getPrice());
                prePriceMap.put("puPrice", sty.getPuPrice());
                prePriceMap.put("wsPrice", sty.getWsPrice());
                prePriceMap.put("preCast", sty.getPreCast());
            } else {
                throw new RuntimeException("保存类型只能传字符串：'add' or 'edit'");
            }
            List<ColorVo> colorVoList = JSON.parseArray(colorStr, ColorVo.class);
            List<SizeVo> sizeVoList = JSON.parseArray(sizeStr, SizeVo.class);
            List<Product> productList = new ArrayList<>();
            String remark ;
            for (ColorVo colorVo : colorVoList) {
                for (SizeVo sizeVo : sizeVoList) {
                    Product product = new Product();
                    product.setCode(styleDTO.getStyleId() + colorVo.getId() + sizeVo.getId());
                    product.setColorId(colorVo.getId());
                    product.setSizeId(sizeVo.getId());
                    // 给单个sku保存Remark
                    if (styleDTO.getIsCoverSkuRemark()) {
                        remark = styleDTO.getRemark();

                    } else {
                        remark = styleDTO.getRemarkOrigin();
                    }
                    product.setRemark(remark);
                    productList.add(product);
                }
            }
            sty.setOprId(userId);
            List<Product> saveList = StyleUtil.covertToProductInfo(sty, styleDTO, productList);
            this.styleService.saveStyleAndProducts(sty, saveList);
            List<Style> styles = new ArrayList<>();
            styles.add(sty);
            CacheManager.refreshStyleCache(styles);
            if(saveList.size() > 0){
                CacheManager.refreshProductCache(saveList);
            }
            //如果价格发生变动，记录变动信息
            String infoChangeRemark = "";
            if (CommonUtil.isNotBlank(prePriceMap)) {
                HashMap<String, Object> aftPriceMap = new HashMap<>();
                aftPriceMap.put("price", sty.getPrice());
                aftPriceMap.put("puPrice", sty.getPuPrice());
                aftPriceMap.put("wsPrice", sty.getWsPrice());
                aftPriceMap.put("preCast", sty.getPreCast());

                long countValue = this.epcStockService.countAllByStyleId(sty.getId());
                //大于0说明入过库
                if(countValue > 0){
                    infoChangeRemark = this.keyInfoChangeService.commonSave(userId, request.getRequestURL().toString(), sty.getId(), prePriceMap, aftPriceMap);
                }
            }
            //价格发生变动时，向管理员推送公众号消息
            if(CommonUtil.isNotBlank(infoChangeRemark)){
                String[] infoArray = infoChangeRemark.split("\r\n");
                User admin = CacheManager.getUserById("admin");
                if(CommonUtil.isBlank(admin)){
                    logger.error("管理员账号不存在");
                }else {
                    String openId = this.weiXinUserService.getByPhone(admin.getPhone()).getOpenId();
                    String originalPrice = infoArray[0].replace("原价：","");
                    String currentPrice = infoArray[1].replace("现价：","");
                    WechatTemplate.priceChangeMsg(openId, sty.getStyleId(), originalPrice, currentPrice, userId);
                }
            }

            return this.returnSuccessInfo("保存成功", infoChangeRemark);
        } catch (Exception e) {
            logger.error("保存失败", e);
            return this.returnFailInfo("保存失败");
        }
    }

    /**
     * 小程序款编辑价格发生变动，监控数据变化提醒
     * add by Anna
     *
     * @param openId      小程序操作者openId
     * @param formId      表单绑定的form_id
     * @param styleId     当前编辑的款号
     * @param brandName   为了跳转传参的品牌名字
     * @param description 操作返回的描述
     */
    @RequestMapping("/sendDataChangeMsgWS.do")
    @ResponseBody
    public MessageBox sendDataChangeMsgWS(String openId, String formId, String styleId, String brandName, String description) {
        try {
            // 推送模版消息
            String sendState = WechatTemplate.dataChangeSendMsg(openId, formId, styleId, brandName, description);
            if (sendState.equals("success")) {
                return this.returnSuccessInfo("发送消息成功", sendState);
            } else {
                return this.returnFailInfo(sendState);
            }
        } catch (Exception e) {
            logger.error("小程序发送监控数据变化提醒失败", e);
            return this.returnFailInfo("发送失败" + e.getMessage());
        }
    }


    @RequestMapping("/getStyleByIdWS.do")
    @ResponseBody
    public MessageBox getStyleByIdWS(String styleId) throws Exception {
        Style s = this.styleService.get("styleId", styleId);
        String rootPath = this.getSession().getServletContext().getRealPath("/");
        String imgUrl = StyleUtil.returnImageUrl(styleId, rootPath);
        s.setUrl(imgUrl);
        return this.returnSuccessInfo("ok", s);
    }

    @RequestMapping("/findProductByUniqueCode.do")
    @ResponseBody
    public MessageBox findProductByUniqueCode(String code) throws Exception {
        this.logAllRequestParams();
        if (code.length() > 13) {
            String epcCode = code.toUpperCase();
            code = EpcSecretUtil.decodeEpc(epcCode).substring(0, 13);
        } else if (code.length() < 13) {
            return new MessageBox(false, "唯一码信息错误");
        }
        Epc tagEpc = this.epcStockService.findTagEpcByCode(code);
        if (CommonUtil.isNotBlank(tagEpc)) {
            return new MessageBox(true, "", tagEpc.getStyleId());
        } else {
            return new MessageBox(false, "唯一码信息错误");
        }
    }

    @RequestMapping(value = "/sizeSortPageWS.do")
    @ResponseBody
    public Page<SizeSort> getSizeSortpageWS(Page<SizeSort> page) {
        this.logAllRequestParams();
        page.setPageProperty();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        page = this.sizeService.find(page, filters);
        return page;

    }

    @RequestMapping(value = "/findSizeSortListWS.do")
    @ResponseBody
    public List<SizeSort> getSortList() {
        this.logAllRequestParams();
        List<SizeSort> sizeSortList = this.sizeService.getAllSort();

        return sizeSortList;

    }

    @RequestMapping(value = "/findSizeListWS.do")
    @ResponseBody
    public Page<Size> findSizeListWS(Page<Size> page) throws Exception {
        this.logAllRequestParams();
        page.setPageProperty();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        return this.sizeService.findPage(page, filters);
    }

    @RequestMapping(value = "/getSizeInfoWS.do")
    @ResponseBody
    public Size findSizeBySizeId(String sizeId) {
        this.logAllRequestParams();
        Size size = this.sizeService.findSizeBySizeId(sizeId);
        return size;
    }

    @RequestMapping(value = "/saveSizeWS.do")
    @ResponseBody
    public MessageBox saveSizeWS(String sizeInfo) {
        this.logAllRequestParams();
        Size size = JSON.parseObject(sizeInfo, Size.class);
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
            List<Size> sizeList = new ArrayList<>();
            sizeList.add(s);
            CacheManager.refreshSizeCache(sizeList);
            return returnSuccessInfo("ok");
        } catch (Exception e) {
            e.printStackTrace();
            return returnFailInfo("fail");
        }
    }


    @RequestMapping(value = "/findColorListWS.do")
    @ResponseBody
    public Page<Color> findColorListWS(Page<Color> page) {
        this.logAllRequestParams();
        page.setSort("colorName");
        page.setOrder("asc");
        page.setPageProperty();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        return this.colorService.findPage(page, filters);
    }


    @RequestMapping(value = "/getColorInfo.do")
    @ResponseBody
    public Color findColorByColorNo(String colorId) {
        this.logAllRequestParams();
        Color color = this.colorService.findById(colorId);
        return color;
    }


    @RequestMapping(value = "/saveColorWS.do")
    @ResponseBody
    public MessageBox saveColor(String colorInfo) {
        this.logAllRequestParams();
        Color color = JSON.parseObject(colorInfo, Color.class);
        Color col = CacheManager.getColorById(color.getColorId());
        if (CommonUtil.isBlank(col)) {
            col = new Color(color.getColorId(), color.getColorId(), color.getColorName());
            col.setIsUse("Y");
        }
//        User u = getCurrentUser();
        col.setOprId("weChat");
        col.setColorName(color.getColorName());
        col.setHex(color.getHex());
        col.setUpdateTime(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        this.colorService.save(col);
        List<Color> colorList = new ArrayList<>();
        colorList.add(col);
        CacheManager.refreshColorCache(colorList);
        return returnSuccessInfo("ok");
    }

    @RequestMapping(value = "/searchByTypeWS")
    @ResponseBody
    public List<List<PropertyKey>> searchByTypesWS(String[] typeList) throws Exception {
        this.logAllRequestParams();
        List<List<PropertyKey>> pkList = this.propertyService.getPropertyKeyByTypes(typeList);
        return pkList;
    }

    @RequestMapping(value = "/searchCycle")
    @ResponseBody
    public Style searchCycle(String styleId) throws Exception {
        this.logAllRequestParams();
        List<PropertyType> propertyTypeList = this.styleService.findStylePropertyType();
        Style s = CacheManager.getStyleById(styleId);
        return s;
    }

    /**
     * add by Anna
     * 微信小程序－商品款式新增－品牌筛选
     */
    @RequestMapping(value = "/searchBrands")
    @ResponseBody
    public Page<PropertyKey> searchBrands(Page<PropertyKey> page) {
        this.logAllRequestParams();
        page.setSort("registerDate");
        page.setOrder("desc");
        page.setPageProperty();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        return this.propertyService.findPageForKey(page, filters);

    }


    /**
     * 微信小程序上传图片测试
     */

    @RequestMapping("/picture")
    public void uploadPicture(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");  //设置编码
        String rootPath = request.getSession().getServletContext().getRealPath("");
        String styleId = getReqParam("styleId");
        String userId = getReqParam("userId");
        String path = rootPath + "product\\template\\";
        System.out.println("上传的图片路径:" + path);
        Integer seqNo = this.photoService.getMaxSeq();
        PrintWriter printWriter = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        HashMap<String, Object> res = new HashMap<String, Object>();
        try {
            //可以上传多个文件
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            List<MultipartFile> fileList = multipartRequest.getFiles("file");
            for (MultipartFile file : fileList) {
                String fileName = file.getOriginalFilename();
                String picprefix = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                String fileType = file.getContentType().split("/")[0];
                if (!"image".equals(fileType)) {
                    /* return new MessageBox(false,"文件格式错误,请传图片");*/
                    res.put("success", false);
                    res.put("msg", "文件格式错误,请传图片");


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
                File f = new File(folder, photo.getId() + "." + picprefix);
                file.transferTo(f);
                String url = "/" + styleId + "/-/" + photo.getId() + "." + picprefix;
                photo.setSrc(url);
                ImgUtil.img_change(filePath, photo.getId() + "." + picprefix);
                this.photoService.save(photo);
                res.put("success", true);
                res.put("msg", url);
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.put("success", false);
            res.put("msg", "保存失败");
        }
        printWriter.write(JSON.toJSONString(res));
        printWriter.flush();
    }

    /**
     * 微信小程序客户图像上传图片测试
     */

    @RequestMapping("/customerPicture")
    public void uploadCustomerPicture(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");  //设置编码
        String rootPath = request.getSession().getServletContext().getRealPath("");
        String unionid = getReqParam("unionid");
        //String userId = getReqParam("userId");
        String path = rootPath + "product\\template\\";
        System.out.println("上传的图片路径:" + path);
        Integer seqNo = this.photoService.getMaxSeq();
        PrintWriter printWriter = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        HashMap<String, Object> res = new HashMap<String, Object>();
        try {
            //可以上传多个文件
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            List<MultipartFile> fileList = multipartRequest.getFiles("file");
            for (MultipartFile file : fileList) {
                String fileName = file.getOriginalFilename();
                String picprefix = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                String fileType = file.getContentType().split("/")[0];
                if (!"image".equals(fileType)) {
                    /* return new MessageBox(false,"文件格式错误,请传图片");*/
                    res.put("success", false);
                    res.put("msg", "文件格式错误,请传图片");


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
                File f = new File(folder, photo.getId() + "." + picprefix);
                file.transferTo(f);
                String url = "/" + unionid + "/-/" + photo.getId() + "." + picprefix;
                photo.setSrc(url);
                ImgUtil.img_change(filePath, photo.getId() + "." + picprefix);
                this.customerPhotoService.save(photo);
                res.put("success", true);
                res.put("msg", url);
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.put("success", false);
            res.put("msg", "保存失败");
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
     * 查找所有颜色尺寸，不分页
     */
    @RequestMapping(value = "/searchColors")
    @ResponseBody
    public Map<String, Object> searchColors() throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        List<PropertyFilter> colorFilters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        Map<String, String> colorSortMap = new HashMap<>();
        colorSortMap.put("colorName", "asc");

        List<Color> colors = this.colorService.find(colorFilters, colorSortMap);
        map.put("colors", colors);

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

    /**
     * add by yushen
     * 上传或重传款式图片，上传图片时，如果本来已经存在图片，把原来的图片删掉
     */
    @RequestMapping("/uploadStylePicture")
    public void uploadStylePicture(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //删除文件
        String rootPath = request.getSession().getServletContext().getRealPath("");
        String styleId = getReqParam("styleId");
        String filePath = rootPath + "/product/photo/" + styleId + "/-/";
        File dir = new File(filePath);
        if (dir.exists()) {
            File[] tmpFiles = dir.listFiles();
            for (File file : tmpFiles) {
                file.delete();
            }
        }
        //上传文件
        this.uploadPicture(request, response);
    }
}
