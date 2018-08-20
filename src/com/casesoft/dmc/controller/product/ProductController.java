package com.casesoft.dmc.controller.product;

import java.io.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.cache.RedisUtils;
import com.casesoft.dmc.cache.SpringContextUtil;
import com.casesoft.dmc.controller.syn.tool.SynTaskUtil;
import com.casesoft.dmc.controller.syn.tool.TaskAdjustUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.FileUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.core.vo.TagFactory;
import com.casesoft.dmc.model.product.ProductInfoList;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.sys.ResourcePrivilege;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.service.sys.ResourcePrivilegeService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
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
    @Autowired
    private ResourcePrivilegeService resourcePrivilegeService;

    private static RedisUtils redisUtils = (RedisUtils) SpringContextUtil.getBean("redisUtils");
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
        int count = 0;//记录是否更新了版本
        RedisUtils redisUtils = (RedisUtils) SpringContextUtil.getBean("redisUtils");
        long productMaxVersionId = CacheManager.getproductMaxVersionId();
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
            product.setVersion(productMaxVersionId+1);
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
            if(count >0){
                redisUtils.hset("maxVersionId","productMaxVersionId", JSON.toJSONString(productMaxVersionId+1));
                CacheManager.refreshMaxVersionId();
            }
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
                        long styleMaxVersionId = CacheManager.getStyleMaxVersionId();
                        long productMaxVersionId = CacheManager.getproductMaxVersionId();
                        ProductInfoList list = ProductUtil
                                .readProductNewFile((FileInputStream) fileEntry.getValue().getInputStream());
                        this.productService.save(list);
                        //保存成功更新缓存
                        redisUtils.hset("maxVersionId","productMaxVersionId", JSON.toJSONString(productMaxVersionId+1));
                        redisUtils.hset("maxVersionId","styleMaxVersionId",JSON.toJSONString(styleMaxVersionId+1));
                        CacheManager.refreshMaxVersionId();
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

    @RequestMapping("/remarkSave")
    @ResponseBody
    public MessageBox remarkSave (String id,String remark){
        this.logAllRequestParams();
        try {
            Product product = this.productService.load(id);
            if (CommonUtil.isNotBlank(product)){
                this.productService.updateRemarkById(id,remark);
                return returnSuccessInfo("更改成功");
            }else {
                return returnFailInfo("请先保存后更改");
            }
        }catch (Exception e){
            e.printStackTrace();
            return returnFailInfo("更改失败");
        }
    }
    @RequestMapping(value = "/templet")
    public void getTemplet(){
        String path = this.request.getSession().getServletContext().getRealPath("")+"/templet/wareTemplet.xls";
        System.out.println(path);
        String contentType = "application/vnd.openxmlformats-officeodcument.spreadsheetml.sheet;";
        List<ResourcePrivilege> resourcePrivileges = this.resourcePrivilegeService.findPrivilege("prod/style", this.getCurrentUser().getRoleId());
        try {
            File file = new File(path);
            FileInputStream inputStream =new FileInputStream(file);
            HSSFWorkbook workbook = new HSSFWorkbook(inputStream);// 创建对Excel工作薄文件的引用
            HSSFSheet sheet = workbook.getSheetAt(0);// 创建对工作表的引用，也可用workbook.getSheet("sheetName");
            int i = 0;// 从第1行开始读标题
            HSSFRow row = sheet.getRow(i);
            HSSFCellStyle style = workbook.createCellStyle(); // 样式对象
            HSSFCellStyle style1 = workbook.createCellStyle(); // 样式对象

            HSSFFont font=workbook.createFont();
            font.setColor(HSSFColor.RED.index);//HSSFColor.VIOLET.index //字体颜色
            font.setFontHeightInPoints((short)11);
            font.setFontName("等线");

            HSSFFont font1=workbook.createFont();
            font1.setColor(HSSFColor.BLACK.index);//HSSFColor.VIOLET.index //字体颜色
            font1.setFontHeightInPoints((short)11);
            font1.setFontName("等线");
            //把字体应用到当前的样式
            style.setFont(font);
            style1.setFont(font1);
            for (ResourcePrivilege resourcePrivilege:resourcePrivileges){
                if("div".equals(resourcePrivilege.getType()) && "style_price_div".equals(resourcePrivilege.getPrivilegeId())){
                    HSSFCell dpcell = row.getCell(8);//得到列8-12
                    if(resourcePrivilege.getIsShow() == 0){

                        dpcell.setCellValue("吊牌价*");
                        dpcell.setCellStyle(style);
                    }
                    else {
                        dpcell.setCellValue("吊牌价");
                        dpcell.setCellStyle(style1);
                    }
                }
                if("div".equals(resourcePrivilege.getType()) && "style_preCase_div".equals(resourcePrivilege.getPrivilegeId())){
                    HSSFCell cgcell = row.getCell(9);//得到列8-12
                    if(resourcePrivilege.getIsShow() == 0){

                        cgcell.setCellValue("采购价*");
                        cgcell.setCellStyle(style);
                    }
                    else{
                        cgcell.setCellValue("采购价");
                        cgcell.setCellStyle(style1);
                    }
                }
                if("div".equals(resourcePrivilege.getType()) && "style_wsPrice_div".equals(resourcePrivilege.getPrivilegeId())){
                    HSSFCell mdcell = row.getCell(10);//得到列8-12
                    if(resourcePrivilege.getIsShow() == 0){

                        mdcell.setCellValue("门店价*");
                        mdcell.setCellStyle(style);
                    }
                    else {
                        mdcell.setCellValue("门店价");
                        mdcell.setCellStyle(style1);
                    }
                }
                if("div".equals(resourcePrivilege.getType()) && "style_puPrice_div".equals(resourcePrivilege.getPrivilegeId())){
                    HSSFCell dlcell = row.getCell(11);//得到列8-12
                    if(resourcePrivilege.getIsShow() == 0){

                        dlcell.setCellValue("代理价*");
                        dlcell.setCellStyle(style);
                    }
                    else {
                        dlcell.setCellValue("代理价");
                        dlcell.setCellStyle(style1);
                    }
                }
                if("div".equals(resourcePrivilege.getType()) && "style_bargainPrice_div".equals(resourcePrivilege.getPrivilegeId())){
                    HSSFCell tjcell = row.getCell(12);//得到列8-12
                    if(resourcePrivilege.getIsShow() == 0){

                        tjcell.setCellValue("特价*");
                        tjcell.setCellStyle(style);
                    }
                    else {
                        tjcell.setCellValue("特价");
                        tjcell.setCellStyle(style1);
                    }
                }
            }
            FileOutputStream fos = new FileOutputStream(path);
            workbook.write(fos);
            fos.close();
            this.outFile("商品模板.xls", file, contentType);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
