package com.casesoft.dmc.controller.logistics;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.ILogisticsBillController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.logistics.MergeReplenishBill;
import com.casesoft.dmc.model.logistics.Recordsize;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.service.logistics.MergeReplenishBillService;
import com.casesoft.dmc.service.sys.impl.UnitService;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.jeecgframework.poi.excel.entity.params.ExcelExportEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/30.
 */
@Controller
@RequestMapping("/logistics/mergeReplenishBillController")
public class MergeReplenishBillController extends BaseController implements ILogisticsBillController<MergeReplenishBill> {
    @Autowired
    private UnitService unitService;
    @Autowired
    private MergeReplenishBillService mergeReplenishBillService;

    @RequestMapping(value = "/index")
    public ModelAndView indexMV() throws Exception {
        ModelAndView mv = new ModelAndView("/views/logistics/mergeReplenishBill");
        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        Unit unit = this.unitService.getunitbyId(getCurrentUser().getOwnerId());
        mv.addObject("ownersId", unit.getOwnerids());
        mv.addObject("userId", getCurrentUser().getId());
        return mv;
    }
    @RequestMapping(value = "/page")
    @ResponseBody
    @Override
    public Page<MergeReplenishBill> findPage(Page<MergeReplenishBill> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        //权限设置，增加过滤条件，只显示当前ownerId下的销售单信息
      /*  User CurrentUser = CacheManager.getUserById(userId);
        String ownerId = CurrentUser.getOwnerId();
        String id = CurrentUser.getId();
        if (!id.equals("admin")) {
            PropertyFilter filter = new PropertyFilter("EQS_ownerId", ownerId);
            filters.add(filter);
        }*/
        page.setPageProperty();
        page = this.mergeReplenishBillService.findPage(page, filters);
        return page;
    }

    @Override
    public List<MergeReplenishBill> list() throws Exception {
        return null;
    }

    @Override
    public MessageBox save(String bill, String strDtlList, String userId) throws Exception {
        return null;
    }

    @Override
    public ModelAndView add() throws Exception {
        return null;
    }
    @RequestMapping(value = "/edit")
    @ResponseBody
    @Override
    public ModelAndView edit(String billNo) throws Exception {
        MergeReplenishBill mergeReplenishBill = this.mergeReplenishBillService.load(billNo);
        ModelAndView mv = new ModelAndView("/views/logistics/mergeReplenishBillDetail");
        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        mv.addObject("userId", getCurrentUser().getId());
        mv.addObject("pageType", "edit");
        mv.addObject("mergeReplenishBill", mergeReplenishBill);
        Unit unit = this.unitService.getunitbyId(getCurrentUser().getOwnerId());
        mv.addObject("ownersId", unit.getOwnerids());
        mv.addObject("roleid", getCurrentUser().getRoleId());
        mv.addObject("Codes", getCurrentUser().getCode());
        mv.addObject("mainUrl", "/logistics/mergeReplenishBillController/index.do");
        return mv;
    }

    @Override
    public MessageBox check(String billNo) throws Exception {
        return null;
    }

    @Override
    public MessageBox end(String billNo) throws Exception {
        return null;
    }

    @Override
    public MessageBox cancel(String billNo) throws Exception {
        return null;
    }

    @Override
    public MessageBox convert(String strDtlList, String recordList) throws Exception {
        return null;
    }

    @Override
    public String index() {
        return null;
    }

    @RequestMapping(value = "/findMergeBillDetail")
    @ResponseBody
    public MessageBox findMergeBillDetail(String billNo){
        HttpSession session=this.getSession();
        Map<String,Object> mergeBillDetail = this.mergeReplenishBillService.findMergeBillDetail(billNo,session);
        return new MessageBox(true, "查询成功", mergeBillDetail);
    }
    @RequestMapping(value = "/findRecordsizeBybillNo")
    @ResponseBody
    public List<Recordsize> findRecordsizeBybillNo(String billNo){
        List<Recordsize> recordsizeBybillNo = this.mergeReplenishBillService.findRecordsizeBybillNo(billNo);
        return recordsizeBybillNo;
    }
    @RequestMapping(value = "/saveRecordsize")
    @ResponseBody
    public MessageBox saveRecordsize(String strDtlList){
        List<Recordsize> Recordsizes = JSON.parseArray(strDtlList, Recordsize.class);
        this.mergeReplenishBillService.saveoruodateRecordsizeList(Recordsizes);
        System.out.println(Recordsizes);
        return new MessageBox(true,"保存成功");
    }
    @RequestMapping(value = "/changePurchase")
    @ResponseBody
    public MessageBox changePurchase(String billNo){
        Boolean aBoolean = this.mergeReplenishBillService.changePurchase(billNo);
        if(aBoolean){
            return new MessageBox(true,"转换成功");
        }else{
            return new MessageBox(false,"转换失败");
        }

    }
    @RequestMapping(value = "/exportmessage")
    @ResponseBody
    public void exportmessage(String billNo){
        try {
            HttpSession session=this.getSession();
            Map<String,Object> mergeBillDetail = this.mergeReplenishBillService.findMergeBillDetail(billNo,session);
            List<ExcelExportEntity> entity = new ArrayList<ExcelExportEntity>();
            List<Map<String,String>> savelist = (List<Map<String,String>>)mergeBillDetail.get("key");
            List<Map<String,Object>> list=(List<Map<String,Object>>)mergeBillDetail.get("result");
            for(int i=0;i<savelist.size();i++){
                Map<String,String> map=savelist.get(i);
                if(map.get("name").equals("url")){
                    ExcelExportEntity excelentity = new ExcelExportEntity(map.get("label"), map.get("name"));
                    excelentity.setHeight(30D);
                    excelentity.setWidth(30D);
                    excelentity.setExportImageType(1);
                    excelentity.setType(2);
                    entity.add(excelentity);
                }else{
                    ExcelExportEntity excelentity = new ExcelExportEntity(map.get("label"), map.get("name"));
                    excelentity.setWidth(40D);
                    entity.add(excelentity);
                }
            }
            for(int a=0;a<list.size();a++){
                Map<String,Object> map=list.get(a);
                if(CommonUtil.isBlank(map.get("url"))){
                    map.put("url","/product/photo/noImg.png");
                }
            }
            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(billNo+"合并补货单", "sheet1", ExcelType.XSSF), entity,
                    list);
            String path = Constant.Folder.Report_File_Folder;
            String dateString = CommonUtil.getDateString(new Date(), "yyyyMMdd HH_mm_ss");
            File files = new File(path + "\\billNo+合并补货单");

            if (!files.exists())
                files.mkdirs();
            File file = new File(files, billNo+"合并补货单-" + dateString + ".xlsx");
            file.createNewFile();
           /* for(DetailStockChatView d : list){
                if(CommonUtil.isBlank(d.getUrl())){
                    //没有图片设置默认图片
                    d.setUrl("/product/photo/noImg.png");
                }
            }*/

            FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());
            workbook.write(fos);
            FileWriter fileWriter = new FileWriter(file.getName(), true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.close();
            String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;";
            this.outFile(billNo+"合并补货单-" + dateString + ".xlsx", file, contentType);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @RequestMapping(value = "/cancelbill")
    @ResponseBody
    public MessageBox cancelbill(String billNo){
        Boolean cancelbill = this.mergeReplenishBillService.cancelbill(billNo);
        if(cancelbill){
            return new MessageBox(true,"取消成功");
        }else {
            return new MessageBox(false,"取消失败");
        }

    }
}
