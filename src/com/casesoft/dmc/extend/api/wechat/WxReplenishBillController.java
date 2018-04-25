package com.casesoft.dmc.extend.api.wechat;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.logistics.BillConvertUtil;
import com.casesoft.dmc.controller.product.StyleUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.api.web.ApiBaseController;
import com.casesoft.dmc.model.logistics.BillConstant;
import com.casesoft.dmc.model.logistics.ReplenishBill;
import com.casesoft.dmc.model.logistics.ReplenishBillDtl;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.logistics.ReplenishBillService;
import com.casesoft.dmc.service.product.ProductService;
import com.casesoft.dmc.service.sys.impl.UnitService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.*;

/**
 * Created by Administrator on 2018/2/3.
 */
@Controller
@RequestMapping(value = "/api/wx/replenishBill",method = {RequestMethod.POST, RequestMethod.GET})
@Api(description = "补单管理接口")
public class WxReplenishBillController extends ApiBaseController {

    @Autowired
    private ReplenishBillService replenishBillService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private ProductService productService;
    @Override
    public String index() {
        return null;
    }
    @RequestMapping(value = "/saveWS.do")
    @ResponseBody
    public MessageBox save(String replenishBillStr, String strDtlList, String userId) throws Exception {
        this.logAllRequestParams();
        try {
            ReplenishBill replenishBill = JSON.parseObject(replenishBillStr, ReplenishBill.class);
            replenishBill.setBillType(Constant.ScmConstant.BillType.Save);
            List<ReplenishBillDtl> replenishBillDtlList = JSON.parseArray(strDtlList, ReplenishBillDtl.class);
            User curUser = CacheManager.getUserById(userId);
            if (CommonUtil.isBlank(replenishBill.getBillNo())) {
                String prefix = BillConstant.BillPrefix.ReplenishiBill
                        + CommonUtil.getDateString(new Date(), "yyMMddHHmmssSSS");
                //String billNo = this.saleOrderBillService.findMaxBillNo(prefix);
                replenishBill.setId(prefix);
                replenishBill.setBillNo(prefix);
            }
            replenishBill.setId(replenishBill.getBillNo());
            BillConvertUtil.covertToReplenishBill(replenishBill, replenishBillDtlList, curUser);
            this.replenishBillService.saveMessage(replenishBill,replenishBillDtlList);
            return new MessageBox(true, "保存成功", replenishBill.getBillNo());
        } catch (Exception e) {
            e.printStackTrace();
            return new MessageBox(false, e.getMessage());
        }
    }
    @RequestMapping(value = "/findProductWS.do")
    @ResponseBody
    public MessageBox findProduct(String styleId){
        List<Product> byStyleId = this.productService.findByStyleId(styleId);
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("styleId",styleId);
        map.put("styleName",byStyleId.get(0).getStyleName());
        String rootPath = this.getSession().getServletContext().getRealPath("/");
       /* File file =  new File(rootPath + "/product/photo/" + styleId);
        if(file.exists()){
            File[] files = file.listFiles();
            if(files.length > 0){
                File[] photos = files[0].listFiles();
                if(photos.length > 0){
                    //d.setUrl("/product/photo/" + d.getStyleId()+"/"+files[0].getName()+"/"+photos[0].getName());
                    map.put("url","/product/photo/" + styleId+"/"+files[0].getName()+"/"+photos[0].getName());
                }
            }
        }else{
            map.put("url","");
        }*/
        String url = StyleUtil.returnImageUrl(styleId, rootPath);
        map.put("url",url);
        List<Map<String,Object>> colorIdList= new ArrayList< Map<String,Object>>();

        for(int i=0;i<byStyleId.size();i++){
            if(i==0){
                Map<String,Object> colorIdmap=new HashMap<String,Object>();
                colorIdmap.put("colorId",byStyleId.get(i).getColorId());
                List<String> sizeIdlist= new ArrayList<String>();
                sizeIdlist.add(byStyleId.get(i).getSizeId());
                colorIdmap.put("sizeId",sizeIdlist);
                colorIdList.add(colorIdmap);
            }else{
                boolean isnohave=true;
                for(Map<String,Object> maps:colorIdList){
                    String colorId=(String)maps.get("colorId");
                    if(colorId.equals(byStyleId.get(i).getColorId())){
                        isnohave=false;
                        List<String> sizeIdlist=( List<String> )maps.get("sizeId");
                        sizeIdlist.add(byStyleId.get(i).getSizeId());
                    }
                }
                if(isnohave){
                    Map<String,Object> colorIdmap=new HashMap<String,Object>();
                    colorIdmap.put("colorId",byStyleId.get(i).getColorId());
                    List<String> sizeIdlist= new ArrayList<String>();
                    sizeIdlist.add(byStyleId.get(i).getSizeId());
                    colorIdmap.put("sizeId",sizeIdlist);
                    colorIdList.add(colorIdmap);
                }
            }
        }
        map.put("color",colorIdList);
        return new MessageBox(true, "查询成功", map);
    }


}
