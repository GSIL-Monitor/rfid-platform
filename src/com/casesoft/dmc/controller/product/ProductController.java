package com.casesoft.dmc.controller.product;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.syn.tool.SynTaskUtil;
import com.casesoft.dmc.controller.syn.tool.TaskAdjustUtil;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.FileUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.core.vo.TagFactory;
import com.casesoft.dmc.model.product.ProductInfoList;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.task.Business;
import org.hibernate.JDBCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.service.product.ProductService;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
@RequestMapping("/prod/product")
public class ProductController extends BaseController implements IBaseInfoController<Product> {

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/index")
    @Override
    public String index() {
        return "views/prod/product";
    }

    @RequestMapping(value = "/page")
    @ResponseBody
    @Override
    public Page<Product> findPage(Page<Product> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page = this.productService.findPage(page, filters);
        for(Product p:page.getRows()){
            Style style = CacheManager.getStyleById(p.getStyleId());
            if(CommonUtil.isNotBlank(style)){
                p.setPrice(style.getPrice());
            }

        }
        return page;
    }
    @RequestMapping(value="/list")
    @ResponseBody
    @Override
    public List<Product> list() throws Exception {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        List<Product> products = this.productService.find(filters);
        return products;
    }

    /**
     * add by yushen 订单中选择商品接口，显示商品按颜色尺寸排序。颜色按颜色名首字母排序，尺寸按XS,S,M,L,XL,XXL排序(seqNo)
     */
    @RequestMapping(value="/listOrderByColorAndSize")
    @ResponseBody
    public List<Product> listOrderByColorAndSize(String styleId) throws Exception {
       List<Product> products = this.productService.listOrderByColorAndSize(styleId);
        return products;
    }

    @RequestMapping(value="/save")
    @ResponseBody
    public MessageBox saveSCSProduct(String styleId,String colorId,String sizeIds){
        int index = CacheManager.getMaxProductId();
        //用于保存款式尺寸组
        List<Product> products = new LinkedList<Product>();
        String[] sizeId=sizeIds.split(",");
        for(int i=0;i<sizeId.length;i++){
            Product valPro =CacheManager.getProductByCode(styleId+colorId+sizeId[i]);
            if(CommonUtil.isNotBlank(valPro)){
                continue;
            }
            String id = ProductUtil.getNewProductId(index+i);
            Product product =new Product();
            product.setId(id);
            product.setStyleId(styleId);
            product.setStyleName(CacheManager.getStyleNameById(styleId));
            product.setColorId(colorId);
            product.setColorName(CacheManager.getColorNameById(colorId));
            product.setSizeId(sizeId[i]);
            product.setSizeName(CacheManager.getSizeNameById(sizeId[i]));
            product.setCode(styleId+colorId+product.getSizeId());
            product.setBrandCode(product.getCode());
            products.add(product);
        }
        try{
            this.productService.save(products);
                CacheManager.refreshProductCache();
            return returnSuccessInfo("添加成功");
        }catch(Exception e){
            return returnFailInfo("添加失败");
        }
    }

    @Override
    public MessageBox save(Product entity) throws Exception {

        return null;
    }

    @Override
    public MessageBox edit(String taskId) throws Exception {

        return null;
    }

    @RequestMapping(value = "/delete")
    @ResponseBody
    @Override
    public MessageBox delete(String code) throws Exception {

        Product product =this.productService.findProductByCode(code);
        if(CommonUtil.isBlank(product)){
            return this.returnSuccessInfo("库中无该条数据");
        }
        try {
            this.productService.delete(product);
            CacheManager.refreshProductCache();
            return returnSuccessInfo("删除成功");
        }catch(Exception e){
            return this.returnFailInfo("删除失败");
        }
    }

    @Override
    public void exportExcel() throws Exception {


    }

    @Override
    public MessageBox importExcel(MultipartFile file) throws Exception {
        return null;
    }

    @RequestMapping("/importExcel")
    @ResponseBody
    public MessageBox importExcel(MultipartHttpServletRequest multipartRequest) throws Exception {
        this.logAllRequestParams();
        try {
            Map<String, MultipartFile> multipartFileMap = multipartRequest.getFileMap();
            if (CommonUtil.isNotBlank(multipartFileMap)) {
                for (Map.Entry<String, MultipartFile> fileEntry : multipartFileMap.entrySet()) {
                    if (!fileEntry.getValue().isEmpty()) {
                        // 转存文件
                        ProductInfoList list = ProductUtil
                                .readProductNewFile((FileInputStream) fileEntry.getValue().getInputStream());
                        this.productService.save(list);
                        if (CommonUtil.isNotBlank(list.getProductList())) {
                            CacheManager.refreshProductCache();
                        }
                        if (CommonUtil.isNotBlank(list.getStyleList())) {
                            CacheManager.refreshStyleCache();
                        }
                        if (CommonUtil.isNotBlank(list.getColorList())) {
                            CacheManager.refreshColorCache();
                        }
                        if (CommonUtil.isNotBlank(list.getSizeList())) {
                            CacheManager.refreshSizeCache();
                        }
                        if (CommonUtil.isNotBlank(list.getSizeSort())) {
                            CacheManager.refreshSizeSortCache();
                        }
                        if (CommonUtil.isNotBlank(list.getPropertyKeyList())) {
                            CacheManager.refreshPropertyCache();
                        }
                        if (CommonUtil.isNotBlank(list.getPropertyTypeList())) {
                            CacheManager.refreshPropertyTypeCache();
                        }
                    }
                }
            } else {
                return this.returnFailInfo("文件为空或不支持此类型文件！");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return this.returnFailInfo("压缩文件出错！");
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return this.returnFailInfo("压缩文件不合法！");

        } catch (JDBCException e) {
            e.printStackTrace();
            return this.returnFailInfo("上传失败！HUB存储失败");
        } catch (Exception e) {
            e.printStackTrace();
            return this.returnFailInfo("上传失败！" + e.getMessage());
        }
        return this.returnSuccessInfo("上传成功！");
    }

    @RequestMapping(value = "/changeProductStatus")
    @ResponseBody
    public MessageBox changeColorStatus(String code,String status){
        this.logAllRequestParams();
        try{
            Product pro = CacheManager.getProductByCode(code);
            pro.setIsUse(status);
            this.productService.saveOrUpdate(pro);
            return returnSuccessInfo("更改成功");
        }catch(Exception e){
            e.printStackTrace();
            return returnFailInfo("更改失败");
        }
    }

}
