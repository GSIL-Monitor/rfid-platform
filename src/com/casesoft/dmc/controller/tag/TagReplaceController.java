package com.casesoft.dmc.controller.tag;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.util.secret.EpcSecretUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.stock.EpcStock;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.model.tag.Epc;
import com.casesoft.dmc.model.tag.Init;
import com.casesoft.dmc.model.tag.InitDtl;
import com.casesoft.dmc.model.tag.TagReplaceRecord;
import com.casesoft.dmc.model.task.Record;
import com.casesoft.dmc.service.stock.EpcStockService;
import com.casesoft.dmc.service.tag.InitService;
import com.casesoft.dmc.service.tag.TagReplaceService;
import com.casesoft.dmc.service.trace.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yushen on 2017/8/24.
 */

@Controller
@RequestMapping("/tag/tagReplace")
public class TagReplaceController extends BaseController {

    @Autowired
    private EpcStockService epcStockService;
    @Autowired
    private InitService initService;
    @Autowired
    private RecordService recordService;
    @Autowired
    private TagReplaceService tagReplaceService;

    @RequestMapping("/index")
    @Override
    public String index() {
        return "/views/tag/tagReplace";
    }

    @RequestMapping("/findInfo")
    @ResponseBody
    public MessageBox findInfo(String code) throws Exception {
        if (code.length() != 13) {
            String epcCode = code.toUpperCase();
            code = EpcSecretUtil.decodeEpc(epcCode).substring(0, 13);
        }
        EpcStock epcStock = this.epcStockService.findStockEpcByCode(code);
        if (CommonUtil.isNotBlank(epcStock)) {
            Product product = epc2product(epcStock);
            return new MessageBox(true, code, product);
        } else {
            return new MessageBox(false, "未查询到此码" + code);
        }
    }

    @RequestMapping("/findInfoInTagEpc")
    @ResponseBody
    public MessageBox findInfoInTagEpc(String code) throws Exception {
        if (code.length() != 13) {
            String epcCode = code.toUpperCase();
            code = EpcSecretUtil.decodeEpc(epcCode).substring(0, 13);
        }
        Epc epc = this.epcStockService.findTagEpcByCode(code);
        if (CommonUtil.isNotBlank(epc)) {
            EpcStock stock_epc = new EpcStock();
            stock_epc.setSku(epc.getSku());
            stock_epc.setStyleId(epc.getStyleId());
            stock_epc.setColorId(epc.getColorId());
            stock_epc.setSizeId(epc.getSizeId());
            Product product = epc2product(stock_epc);
            return new MessageBox(true, code, product);
        } else {
            return new MessageBox(false, "未查询到此码" + code);
        }
    }

    //epc转为product，返回前段product对象，携带price，styleName字段
    public Product epc2product(EpcStock epc) {
        Product product = new Product(epc.getSku(), epc.getStyleId(), epc.getColorId(), epc.getSizeId());
        product.setPrice(CacheManager.getStyleById(epc.getStyleId()).getPrice());
        product.setStyleName(CacheManager.getStyleNameById(epc.getStyleId()));
        return product;
    }


    @RequestMapping("/produceNewTag")
    @ResponseBody
    public MessageBox produceNewTag(String colorSizeStr) {
        try {
            Product product = JSON.parseObject(colorSizeStr, Product.class);
            String sku = product.getStyleId() + product.getColorId() + product.getSizeId();
            String prefixTaskId = Constant.Bill.Tag_Init_Prefix
                    + CommonUtil.getDateString(new Date(), "yyMMdd");
            String taskId = this.initService.findMaxNo(prefixTaskId);
            User currentUser = this.getCurrentUser();

            InitDtl initDtl = new InitDtl();
            initDtl.setId(taskId + "-" + sku);
            initDtl.setBillNo(taskId);
            initDtl.setOwnerId(currentUser.getOwnerId());
            initDtl.setStatus(0);
            initDtl.setStyleId(product.getStyleId());
            initDtl.setColorId(product.getColorId());
            initDtl.setSizeId(product.getSizeId());
            initDtl.setSku(sku);
            initDtl.setQty(1);
            initDtl.setStartNum(initService.findMaxNoBySkuNo(sku) + 1);
            initDtl.setEndNum(initService.findMaxNoBySkuNo(sku) + 1);
            CacheManager.setMaxTagSkuNum(initDtl.getSku(),initDtl.getEndNum());
            ArrayList<InitDtl> initDtlList = new ArrayList<>();
            initDtlList.add(initDtl);

            Init master = new Init();
            master.setBillNo(taskId);
            master.setTotEpc(1);
            master.setTotSku(1);
            master.setBillDate(new Date());
            master.setOwnerId(currentUser.getOwnerId());
            master.setHostId(currentUser.getId());
            master.setStatus(0);// 已导入
            master.setDtlList(initDtlList);

            master.setFileName("标签替换");
            String unit2Type = this.getReqParam("unit2Type");
            String unit2Id = this.getReqParam("unit2Id");
            master.setUnit2Type(unit2Type == null ? null : Integer
                    .parseInt(unit2Type));
            master.setDestId(unit2Id);
//            master.setImportType("BARCODE");
            initService.save(master);
            return new MessageBox(true, "标签初始化成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new MessageBox(false, "标签初始化失败");
        }

    }

    @RequestMapping("/replaceTag")
    @ResponseBody
    public MessageBox replaceTag(String origInfoStr, String newInfoStr) throws Exception {
        try {
            EpcStock origEpc = JSON.parseObject(origInfoStr, EpcStock.class);
            EpcStock newEpc = JSON.parseObject(newInfoStr, EpcStock.class);
            if(!CommonUtil.isOctNumberRex(origEpc.getCode()) || origEpc.getCode().length() != 13
                    || !CommonUtil.isOctNumberRex(newEpc.getCode()) || newEpc.getCode().length() != 13){
                return new MessageBox(false, "唯一码格式错误");
            }
            String origSku = origEpc.getStyleId() + origEpc.getColorId() + origEpc.getSizeId();
            String newSku = newEpc.getStyleId() + newEpc.getColorId() + newEpc.getSizeId();
            origEpc.setSku(origSku);
            newEpc.setSku(newSku);

            List<Record> origRecords = this.recordService.getRecordsByCode(origEpc.getCode());
            String msg = this.tagReplaceService.replaceTag(origRecords, origEpc, newEpc);
            this.tagReplaceService.saveReplaceRecord(origEpc, newEpc, msg, getCurrentUser().getId());

            if(msg.equals("ok")){
                return new MessageBox(true, "标签替换成功");
            }else {
                return new MessageBox(false, msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new MessageBox(false, "标签替换失败");
        }
    }

    @RequestMapping("/viewReplaceRecord")
    @ResponseBody
    public ModelAndView viewReplaceRecord(){
        ModelAndView mv = new ModelAndView("/views/tag/tagReplaceRecord");
        return mv;
    }

    @RequestMapping("/findRecordPage")
    @ResponseBody
    public Page<TagReplaceRecord> findRecordPage(Page<TagReplaceRecord> page){
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        page.setPageProperty();
        page = this.tagReplaceService.findRecordPage(page, filters);
        return page;
    }
}
