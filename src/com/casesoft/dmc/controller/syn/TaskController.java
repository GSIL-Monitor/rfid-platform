package com.casesoft.dmc.controller.syn;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.syn.tool.SynTaskUtil;
import com.casesoft.dmc.controller.syn.tool.TaskAdjustUtil;
import com.casesoft.dmc.controller.task.TaskUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.FileUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.core.vo.TagFactory;
import com.casesoft.dmc.model.stock.EpcStock;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.BusinessDtl;
import com.casesoft.dmc.model.task.Record;
import com.casesoft.dmc.service.stock.EpcStockService;
import com.casesoft.dmc.service.task.TaskService;

import org.hibernate.JDBCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by john on 2017/1/5.
 */
@RequestMapping("/syn/task")
@Controller
public class TaskController extends BaseController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private EpcStockService epcStockService;

    @RequestMapping("/index")
    @Override
    public String index() {
        return "/views/syn/taskManager";
    }

    @RequestMapping("/page")
    @ResponseBody
    public Page<Business> list(Page<Business> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page = this.taskService.findPage(page, filters);
        SynTaskUtil.convertToBusVos(page.getRows());
        return page;
    }

    @RequestMapping(value = "/detail/index")
    @ResponseBody
    public ModelAndView viewDtlIndex(String id) {
        this.logAllRequestParams();
        Business business = this.taskService.get("id", id);
        ModelAndView model = new ModelAndView();
        SynTaskUtil.convertToBusVo(business);
        model.addObject("business", business);
        model.addObject("mainUrl","/syn/task/index.do");

        model.setViewName("/views/syn/taskDetail");
        return model;
    }

    @RequestMapping(value = "/record")
    @ResponseBody
    public List<Record> record(String id) throws Exception {
        this.logAllRequestParams();
        List<Record> record = this.taskService.findRecordByTask(id);
        for (Record dtl : record) {
            dtl.setStyleName(CacheManager.getStyleNameById(dtl.getStyleId()));
            dtl.setColorName(CacheManager.getColorNameById(dtl.getColorId()));
            dtl.setSizeName(CacheManager.getSizeNameById(dtl.getSizeId()));
        }
        return record;
    }

    @RequestMapping(value = "/findDetail")
    @ResponseBody
    public List<BusinessDtl> findDetail(String id) throws Exception {
        this.logAllRequestParams();
        
        Assert.notNull(id,"id不能为空");
        List<BusinessDtl> businessDtls = this.taskService.findBusinessDtl(id);
        SynTaskUtil.convertToVo(businessDtls);
        return businessDtls;
    }

    @RequestMapping(value = "/delete")
    @ResponseBody
    public MessageBox delete(String password,String id) throws Exception {
        this.logAllRequestParams();
        if(CommonUtil.isNotBlank(password)&&password.equals("CaseSoft")){
            List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                    .getRequest());
            try {
                if (CommonUtil.isNotBlank(id)) {
                    Business business=this.taskService.get("id",id);
                    if(CommonUtil.isNotBlank(business)){
                        this.taskService.delete(business);
                    }else{
                        return returnFailInfo("任务不存在！");
                    }
                } else {
                    List<Business> businesses = this.taskService.find(filters);
                    List<String> taskIds = SynTaskUtil.getTaskId(businesses);
                    if (CommonUtil.isNotBlank(taskIds) && taskIds.size() >= 1000) {
                        return returnFailInfo("删除失败!数据超过1000");
                    } else {
                        this.taskService.delete("");
                        this.epcStockService.deleteAll();
                    }


                }
                return returnSuccessInfo("删除成功!");
            } catch (Exception e) {
                e.printStackTrace();
                return returnFailInfo("删除失败!");
            }
        }
        return this.returnFailInfo("密码错误！");
    }
    
    @RequestMapping(value = "/deleteByTaskId")
    @ResponseBody
    public MessageBox deleteByTaskId(String taskId) throws Exception {
        this.logAllRequestParams();
         try {
         this.taskService.delete(taskId);
           
            return returnSuccessInfo("删除成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return returnFailInfo("删除失败!");
        }
    }

    @RequestMapping("/uploadTaskFile")
    @ResponseBody
    public MessageBox uploadTaskFile(MultipartHttpServletRequest multipartRequest) throws Exception {
        this.logAllRequestParams();
        try {
            Map<String, MultipartFile> multipartFileMap = multipartRequest.getFileMap();
            String inputPath = PropertyUtil.getValue("MilanUpload") + "\\" + PropertyUtil.getValue("tag_name");
            String successFilePath=PropertyUtil.getValue("MilanUploadHistory") + "\\" + PropertyUtil.getValue("tag_name");
            File uploadFile = new File(inputPath);
            File fileHistory = new File(successFilePath);

            if (!uploadFile.exists()) {
                uploadFile.mkdirs();
            }
            if(!fileHistory.exists()){
                fileHistory.mkdirs();
            }
            if (CommonUtil.isNotBlank(multipartFileMap)) {
                for (Map.Entry<String, MultipartFile> fileEntry : multipartFileMap.entrySet()) {
                    if (!fileEntry.getValue().isEmpty()) {
                        // 转存文件
                        File upFile=new File(inputPath + "\\" + fileEntry.getValue().getOriginalFilename());
                        fileEntry.getValue().transferTo(upFile);
                        Business business = SynTaskUtil.convertZipToDto(fileEntry.getValue().getInputStream());
                        TaskAdjustUtil.adjustStorageId(business);
                        /**
                         * 验证唯一码是否在库存
                         * */
                        if (TagFactory.getCurrentTag().isUniqueCodeStock()) {
                            MessageBox checkResult = this.checkEpcStock(business);
                            if (!checkResult.getSuccess())
                                return checkResult;
                        }
                        logger.error("解析成功");
                        boolean autoUpload = Boolean.parseBoolean(PropertyUtil
                                .getValue("auto_upload_erp"));
                        if (!autoUpload) {
                            this.taskService.save(business);
                        } else {
                            this.taskService.save(business, autoUpload);
                        }
                        FileUtil.moveFile(upFile, successFilePath+"\\" + fileEntry.getValue().getOriginalFilename());
                        boolean dSuccess=upFile.delete();
                        // 删除云同步数据2014-11-23
                        try {
                            if (CommonUtil.isNotBlank(business)) {
                                this.taskService.deleteCloudBusiness(business.getId());
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            System.out.println("删除云数据错误");
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

        }catch (JDBCException e){
            e.printStackTrace();
            return this.returnFailInfo("上传失败！HUB存储失败");
        }catch (Exception e) {
            e.printStackTrace();
            return this.returnFailInfo("上传失败！" + e.getMessage());
        }

        return this.returnSuccessInfo("上传成功！");
    }

    /**
     * 检查库存
     *
     * @param bus
     * @return
     */
    private MessageBox checkEpcStock(Business bus) throws Exception {
        switch (bus.getToken().intValue()) {
            case Constant.Token.Storage_Adjust_Inbound:
            case Constant.Token.Storage_Refund_Inbound:
            case Constant.Token.Storage_Inbound:
            case Constant.Token.Shop_Adjust_Inbound:
            case Constant.Token.Shop_Transfer_Inbound:
            case Constant.Token.Shop_Inbound:
                List<String> codeList = com.casesoft.dmc.controller.task.TaskUtil.getRecordCodes(bus.getRecordList());
                String codes = CommonUtil.getSqlStrByList(codeList, EpcStock.class, "code");
                // 未入库的
                List<EpcStock> list = this.taskService.getInStock(codes, null);
                List<String> copyList = TaskUtil.getDifferEpcStockCodes(list, codeList);
                if (CommonUtil.isNotBlank(list)) {
                    return this.returnFailInfo("存在不能入库的唯一码!", JSON.toJSON(copyList));
                }
                break;
            case Constant.Token.Storage_Adjust_Outbound:
            case Constant.Token.Storage_Outbound:
            case Constant.Token.Storage_Transfer_Outbound:
            case Constant.Token.Shop_Adjust_Outbound:
            case Constant.Token.Shop_Refund_Outbound:
            case Constant.Token.Shop_Transfer_Outbound:
                List<String> ocodeList = com.casesoft.dmc.controller.task.TaskUtil.getRecordCodes(bus.getRecordList());
                String ocodes = com.casesoft.dmc.controller.task.TaskUtil.getSqlStrByList(ocodeList, EpcStock.class, "code");
                String ostorageId = bus.getOrigId();
                // 未入库的
                List<EpcStock> olist = this.taskService.getInStock(ocodes, ostorageId);
                List<String> ocopyList = com.casesoft.dmc.controller.task.TaskUtil.getDifferEpcStockCodes(olist, ocodeList);
                if (ocodeList.size() != 0) {
                    this.returnFailInfo("存在不能出库的唯一码!",
                            JSON.toJSON(ocodeList));
                }
                break;
        }
        return this.returnSuccessInfo("验证成功");
    }

}
