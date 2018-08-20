package com.casesoft.dmc.extend.mirror;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.echarts.style.ControlStyle;
import com.casesoft.dmc.model.mirror.BrandInfo;
import com.casesoft.dmc.model.mirror.NewProduct;
import com.casesoft.dmc.model.product.Color;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.product.Size;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.service.mirror.BrandInfoService;
import com.casesoft.dmc.service.mirror.NewProductService;
import com.casesoft.dmc.service.product.ProductService;
import com.casesoft.dmc.service.product.StyleService;
import com.casesoft.dmc.service.sys.impl.CompanyService;
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

/**
 * 图片放入 web根目录下/mirror/newProduct/款号/图片名.jpg
 */
@Controller
@RequestMapping("/smart/newProduct")
public class NewProductController extends BaseController implements IBaseInfoController<NewProduct> {

    @Autowired
    private NewProductService newProductService;
    @Autowired
    private StyleService styleService;
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandInfoService brandInfoService;

    @RequestMapping("/index")
    @Override
    public String index() {
        return "/views/smart/newProduct";
    }

    @RequestMapping(value = "/page")
    @ResponseBody
    @Override
    public Page<NewProduct> findPage(Page<NewProduct> page) throws Exception {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page=this.newProductService.findPage(page,filters);
        return page;
    }

    @RequestMapping(value = "/findBrandCode")
    @ResponseBody
    public List<BrandInfo> findBrandCode()throws Exception{
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        List<BrandInfo> brandInfo=this.brandInfoService.find(filters);
        return  brandInfo;
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    @Override
    public List<NewProduct> list() throws Exception {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        List<NewProduct> product=this.newProductService.find(filters);
        return product;
    }


    @RequestMapping(value = "/detail")
    @ResponseBody
    public ModelAndView detail(String styleId)throws Exception{
        this.logAllRequestParams();
        NewProduct newProduct = this.newProductService.findByStyleId(styleId);
        ModelAndView model = new ModelAndView();
        model.addObject("newProduct", newProduct);
        model.setViewName("/views/smart/newProduct_detail");
        return model;
    }

    @RequestMapping(value = "/edit")
    @ResponseBody
    public ModelAndView findByStyleId(String styleId){
        this.logAllRequestParams();
        NewProduct newProduct = this.newProductService.findByStyleId(styleId);
        ModelAndView model = new ModelAndView();
        model.addObject("newProduct", newProduct);
        model.setViewName("/views/smart/newProduct_edit");
        return model;
    }

    @RequestMapping(value = "/show")
    @ResponseBody
    public NewProduct show(String styleId){
        NewProduct newProduct=new NewProduct();
        Style style=CacheManager.getStyleById(styleId);
        newProduct.setName(style.getStyleName());
        if(style.getSeqNo()!=null){
            newProduct.setSeqNo(style.getSeqNo());
        }
        newProduct.setPrice(style.getPrice());
        List<Product> products = this.productService.findByStyleId(styleId);
        List<Size> prodSizes = convertToSizeVo(products);
        String sizeIds="";
        for(Size s : prodSizes){
            sizeIds += (","+s.getSizeId());
        }
        newProduct.setSizeIds(sizeIds.substring(1));
        List<Color> prodColors =convertToColorVo(products);
        String colorIds="";
        for(Color c : prodColors){
            colorIds += (","+c.getColorId());
        }
        newProduct.setColorIds(colorIds.substring(1));
        return newProduct;
    }

    public static List<Color> convertToColorVo(List<Product> productList) {
        List<Color> colorList = new ArrayList<Color>();
        for(Product p : productList) {
            boolean have = false;
            for(Color c : colorList) {
                if(p.getColorId().equals(c.getColorId())) {
                    have = true;
                }
            }
            if(!have) {
                Color color = CacheManager.getColorById(p.getColorId());
                colorList.add(color);
            }
        }
        return colorList;
    }
    public static List<Size> convertToSizeVo(List<Product> productList) {
        List<Size> sizeList = new ArrayList<Size>();
        for(Product p : productList) {
            boolean have = false;
            for(Size s : sizeList) {
                if(p.getSizeId().equals(s.getSizeId())) {
                    have = true;
                }
            }
            if(!have) {
                Size s = CacheManager.getSizeById(p.getSizeId());
                sizeList.add(s);
            }
        }
        return sizeList;
    }


    @RequestMapping(value = "/saveImages")
    @ResponseBody
    public MessageBox saveImages(MultipartFile file,String styleid) throws Exception{
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

        String fileParant=rootPath + "/mirror/newProduct/"+styleid+"/";
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
        String url=styleid+"/"+fileName;
       /* CacheManager.refreshNewProductCache();*/
        return returnSuccessInfo(url,fileParant+fileName);
    }
    @RequestMapping(value = "/save")
    @ResponseBody
    @Override
    public MessageBox save(NewProduct newProduct) throws Exception {
        newProduct.setUpdateTime(new Date());
        newProduct.setUpdater(this.getCurrentUser().getOwnerId());
        newProduct.setIsDet("N");
        try{
            this.newProductService.save(newProduct);
        }catch (Exception e){
           return returnFailInfo("保存失败");
        }
        /*CacheManager.refreshNewProductCache();*/
        return returnSuccessInfo("保存成功");
    }

    @RequestMapping(value = "/setDet")
    @ResponseBody
    public MessageBox setDet(String styleId) throws Exception{
        NewProduct newProduct = this.newProductService.findByStyleId(styleId);
        if (newProduct.getIsDet().equals("N")){
            newProduct.setIsDet("Y");
        }else{
            newProduct.setIsDet("N");
        }
        this.newProductService.save(newProduct);
        /*CacheManager.refreshNewProductCache();*/
        return returnSuccessInfo("设置成功");
    }

    @RequestMapping(value = "/deleteImg")
    @ResponseBody
    public MessageBox deleteImg(String styleId,String no) throws Exception{
        NewProduct newProduct = this.newProductService.findByStyleId(styleId);
        //删除图片地址
        String imgs=newProduct.getUrl();
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
        newProduct.setUrl(imgs);
        //删除图片
       try {
           String sPath = this.getSession().getServletContext().getRealPath("/") + "/mirror/newProduct/" + styleId + "/" + img[Integer.parseInt(no)];
           File file = new File(sPath);
           // 路径为文件且不为空则进行删除
           if (file.isFile() && file.exists()) {
               file.delete();
           }
       }catch (Exception e){
           returnFailInfo("删除文件失败");
       }

        this.newProductService.save(newProduct);
        /*CacheManager.refreshNewProductCache();*/
        return returnSuccessInfo(imgs);
    }

    @Override
    public MessageBox edit(String taskId) throws Exception {
        return null;
    }


    @RequestMapping(value = "/delete")
    @ResponseBody
    @Override
    public MessageBox delete(String styleId) throws Exception {
        //删除文件夹及其内容

        //删除数据库数据
        this.newProductService.delete(styleId);
       /* CacheManager.refreshNewProductCache();*/
        return  returnSuccessInfo("删除成功");
    }



    @Override
    public void exportExcel() throws Exception {

    }

    @Override
    public MessageBox importExcel(MultipartFile file) throws Exception {
        return null;
    }
}
